package jhorlamide.BackendServer;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jhorlamide.RequestLogger;

import java.io.IOException;
import java.io.PrintWriter;

public class BaseRequestHandler extends HttpServlet {
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      processRequest(request, response);
   }

   private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String responseMessage = "Replied with a hello message";

      response.setContentType("text/plain");
      PrintWriter responseWriter = response.getWriter();

      responseWriter.println(responseMessage);
      responseWriter.flush();
      responseWriter.close();

      RequestLogger.logRequest(request, responseMessage);
   }
}
