package jhorlamide.BackendServer;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jhorlamide.RequestLogger;

import java.io.IOException;
import java.io.PrintWriter;

public class HealthCheckRequestHandler extends HttpServlet {
   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
      try {
         Thread.sleep(250);

         res.setContentType("text/plain");
         PrintWriter responseWriter = res.getWriter();
         String responseMessage = "Backend server is healthy";

         responseWriter.println(responseMessage);
         responseWriter.flush();
         responseWriter.close();

         RequestLogger.logRequest(req, responseMessage);
      } catch (InterruptedException e) {
         RequestLogger.logError(e.getMessage(), e);
      }
   }
}
