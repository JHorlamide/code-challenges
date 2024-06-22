package jhorlamide;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

public class LBServer {
   private final List<String> backendServers;

   protected LBServer(int port, List<String> backendServers) throws Exception {
      this.backendServers = backendServers;

      Server server = new Server(port);

      // create servlet context handler
      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");
      server.setHandler(context);

      // Add the HelloServlet to handle request to the path "/"
      context.addServlet(new ServletHolder(new LBRequestHandler(this)), "/");

      RequestLogger.logServerStart("Load balancer server started on port", port);

      // Start the server
      server.start();
      server.join();
   }

   /**
    * Executes an HTTP request using OkHttp, forwarding the incoming request headers and URI.
    *
    * @param backendUrl     the base URL to which the request is being sent
    * @param servletRequest the incoming HttpServletRequest containing headers and request URI
    * @return the response from executing the HTTP request
    * @throws IOException if an I/O error occurs during request execution
    */
   private static Response executeRequest(String backendUrl, HttpServletRequest servletRequest) throws IOException {
      OkHttpClient httpClient = new OkHttpClient();
      Builder requestBuilder = new Request.Builder()
          .url(backendUrl + servletRequest.getRequestURI());

      // Retrieve all the header names from the incoming HttpServletRequest
      Enumeration<String> requestHeaders = servletRequest.getHeaderNames();

      // Iterate over each header name and add it to the requestBuilder header.
      while (requestHeaders.hasMoreElements()) {
         String requestHeaderKey = requestHeaders.nextElement();
         String requestHeaderValue = servletRequest.getHeader(requestHeaderKey);
         requestBuilder.header(requestHeaderKey, requestHeaderValue);
      }

      Request request = requestBuilder.build();
      return httpClient.newCall(request).execute();
   }

   public static class LBRequestHandler extends HttpServlet {
      private final LBServer lbServer;

      public LBRequestHandler(LBServer lbServer) {
         this.lbServer = lbServer;
      }

      @Override
      protected void doGet(HttpServletRequest request, HttpServletResponse response) {
         for (String backendUrl : lbServer.backendServers) {
            try {
               Response backendResponse = executeRequest(backendUrl, request);

               int responseStatusCode = backendResponse.code();
               String responseBody = backendResponse.body().toString();
               String responseContentType = backendResponse.header("Content-Type");

               System.out.println(responseBody);

               response.setContentType(responseContentType);
               var responseWriter = response.getWriter();

               response.setStatus(responseStatusCode);
               responseWriter.println(responseBody);
               responseWriter.flush();
               backendResponse.close();
               responseWriter.close();

               RequestLogger.log(backendUrl, request);
            } catch (IOException e) {
               RequestLogger.logError(e.getMessage(), e);
            }
         }
      }
   }
}
