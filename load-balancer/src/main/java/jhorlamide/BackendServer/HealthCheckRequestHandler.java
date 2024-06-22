package jhorlamide.BackendServer;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jhorlamide.RequestLogger;

import java.io.IOException;
import java.io.PrintWriter;

public class HealthCheckRequestHandler extends HttpServlet {
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      try {
         Thread.sleep(250);
         processRequest(request, response);
      } catch (InterruptedException e) {
         RequestLogger.logError(e.getMessage(), e);
      }
   }

   private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.setContentType("text/plain");
      PrintWriter responseWriter = response.getWriter();
      String responseMessage = "Backend server is healthy";

      responseWriter.println(responseMessage);
      responseWriter.flush();
      responseWriter.close();

      RequestLogger.logRequest(request, responseMessage);
   }
}
