package jhorlamide.LoadBalancerServer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jhorlamide.LoadBalancerStrategy.ILbStrategy;
import jhorlamide.RequestLogger;
import okhttp3.*;
import okhttp3.Request.Builder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class LBRequestHandler extends HttpServlet {
   private final ILbStrategy lbStrategy;
   private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

   public LBRequestHandler(ILbStrategy lbStrategy) {
      this.lbStrategy = lbStrategy;
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse res)
           throws IOException, ServletException {
      handleRequest(req, res);
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse res)
           throws ServletException, IOException {
      handleRequest(req, res);
   }

   @Override
   protected void doPut(HttpServletRequest req, HttpServletResponse res)
           throws ServletException, IOException {
      handleRequest(req, res);
   }

   @Override
   protected void doDelete(HttpServletRequest req, HttpServletResponse res)
           throws ServletException, IOException {
      handleRequest(req, res);
   }

   private void handleRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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

      // Determine the HTTP method and handle the request body for POST, PUT, DELETE
      String reqMethod = req.getMethod();
      switch (reqMethod) {
         case "POST":
         case "PUT":
         case "DELETE":
            String body = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            RequestBody requestBody = RequestBody.create(body, JSON);
            requestBuilder.method(reqMethod, requestBody);
            break;

         default:
            requestBuilder.method(reqMethod, null);
            break;
      }

      Request request = requestBuilder.build();
      return httpClient.newCall(request).execute();
   }

   private void processRequest(HttpServletRequest req, HttpServletResponse res,
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
