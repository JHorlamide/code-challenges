package jhorlamide.LoadBalancerServer;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jhorlamide.RequestLogger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

public class LBRequestHandler extends HttpServlet {
   private final List<String> backendServers;

   public LBRequestHandler(List<String> backendServers) {
      this.backendServers = backendServers;
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
      Request.Builder requestBuilder = new Request.Builder()
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

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) {
      for (String backendUrl : backendServers) {
         try {
            Response backendResponse = executeRequest(backendUrl, request);
            processRequest(request, response, backendResponse, backendUrl);
         } catch (IOException e) {
            RequestLogger.logError(e.getMessage(), e);
         }
      }
   }

   private void processRequest(
           HttpServletRequest request, HttpServletResponse response,
           Response backendResponse, String backendUrl) throws IOException {
      int responseStatusCode = backendResponse.code();
      String responseBody = backendResponse.body().string();
      String responseContentType = backendResponse.header("Content-Type");

      System.out.println(responseBody);

      response.setContentType(responseContentType);
      var responseWriter = response.getWriter();

      response.setStatus(responseStatusCode);
      responseWriter.println(responseBody);
      responseWriter.flush();
      backendResponse.close();
      responseWriter.close();

      String logMessage = "Response from server: " + request.getProtocol() + " " + response.getStatus();
      RequestLogger.logRequest(backendUrl, request, logMessage, responseBody);
   }
}
