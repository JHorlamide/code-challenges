package jhorlamide.LoadBalancerServer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jhorlamide.LoadBalancerStrategy.RoundRobin;
import jhorlamide.RequestLogger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class LBRequestHandler extends HttpServlet {
   private final RoundRobin lbStrategy;

   public LBRequestHandler(RoundRobin lbStrategy) {
      this.lbStrategy = lbStrategy;
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse res)
           throws IOException, ServletException {
      var server = lbStrategy.getNextServer();

      if (server.isEmpty()) {
         throw new ServletException("No backend server found");
      }

      Response backendResponse = forwardRequestToServer(server, req);
      processRequest(req, res, backendResponse, server);
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse res)
           throws ServletException, IOException {
      var server = lbStrategy.getNextServer();

      if (server.isEmpty()) {
         throw new ServletException("No backend server found");
      }

      Response backendResponse = forwardRequestToServer(server, req);
      processRequest(req, res, backendResponse, server);
   }

   @Override
   protected void doPut(HttpServletRequest req, HttpServletResponse res)
           throws ServletException, IOException {
      var server = lbStrategy.getNextServer();

      if (server.isEmpty()) {
         throw new ServletException("No backend server found");
      }

      Response backendResponse = forwardRequestToServer(server, req);
      processRequest(req, res, backendResponse, server);
   }

   @Override
   protected void doDelete(HttpServletRequest req, HttpServletResponse res)
           throws ServletException, IOException {
      var server = lbStrategy.getNextServer();

      if (server.isEmpty()) {
         throw new ServletException("No backend server found");
      }

      Response backendResponse = forwardRequestToServer(server, req);
      processRequest(req, res, backendResponse, server);
   }

   /**
    * Executes an HTTP request using OkHttp, forwarding the incoming request headers and URI.
    *
    * @param backendUrl the base URL to which the request is being sent
    * @param req        the incoming HttpServletRequest containing headers and request URI
    * @return the response from executing the HTTP request
    * @throws IOException if an I/O error occurs during request execution
    */
   private Response forwardRequestToServer(String backendUrl, HttpServletRequest req) throws IOException {
      OkHttpClient httpClient = new OkHttpClient();
      Builder requestBuilder = new Request.Builder()
              .url(backendUrl + req.getRequestURI());

      // Retrieve all the header names from the incoming HttpServletRequest
      Enumeration<String> requestHeaders = req.getHeaderNames();

      // Iterate over each header name and add it to the requestBuilder header.
      while (requestHeaders.hasMoreElements()) {
         String requestHeaderKey = requestHeaders.nextElement();
         String requestHeaderValue = req.getHeader(requestHeaderKey);
         requestBuilder.header(requestHeaderKey, requestHeaderValue);
      }

      Request request = requestBuilder.build();
      return httpClient.newCall(request).execute();
   }

   private void processRequest(
           HttpServletRequest req, HttpServletResponse res,
           Response backendResponse, String backendUrl) throws IOException {
      int responseStatusCode = backendResponse.code();
      String responseBody = backendResponse.body().string();
      String responseContentType = backendResponse.header("Content-Type");

      res.setContentType(responseContentType);
      PrintWriter responseWriter = res.getWriter();

      res.setStatus(responseStatusCode);
      responseWriter.println(responseBody);
      responseWriter.flush();
      backendResponse.close();
      responseWriter.close();

      String logMessage = "Response from server: " + req.getProtocol() + " " + res.getStatus();
      RequestLogger.log(backendUrl, req, logMessage, responseBody);
   }
}
