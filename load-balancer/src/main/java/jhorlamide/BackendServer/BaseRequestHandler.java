package jhorlamide.BackendServer;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jhorlamide.RequestLogger;

import java.io.IOException;
import java.io.PrintWriter;

public class BaseRequestHandler extends HttpServlet {
   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
      processRequest(req, res);
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

   }

   @Override
   protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {

   }

   @Override
   protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {

   }

   private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
      String responseMessage = "Replied with a hello message";

      res.setContentType("text/plain");
      PrintWriter responseWriter = res.getWriter();

      responseWriter.println(responseMessage);
      responseWriter.flush();
      responseWriter.close();

      RequestLogger.logRequest(req, responseMessage);
   }
}
