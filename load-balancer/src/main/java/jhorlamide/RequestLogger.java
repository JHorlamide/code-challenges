package jhorlamide;

import jakarta.servlet.http.HttpServletRequest;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestLogger {
   private static final Logger logger = Logger.getLogger(RequestLogger.class.getName());

   public static void log(HttpServletRequest request) {
      var reqAddress = request.getRemoteAddr();
      var reqMethod = request.getMethod();
      var reqURI = request.getRequestURI();
      var reqProtocol = request.getProtocol();
      var reqHeader = request.getHeader("User-Agent");
      var reqHost = request.getRemoteHost();

      logger.info(String.format(
          "Received request from %s\n%s %s %s\nUser-Agent: %s\nHost: %s",
          reqAddress, reqMethod, reqURI, reqProtocol, reqHeader, reqHost
      ));
   }

   public static void log(String url, HttpServletRequest request) {
      var reqAddress = request.getRemoteAddr();
      var reqMethod = request.getMethod();
      var reqURI = request.getRequestURI();
      var reqProtocol = request.getProtocol();
      var reqHeader = request.getHeader("User-Agent");
      var reqHost = request.getRemoteHost();

      String formattedString = url + "\n" +
          String.format(
              "Received request from %s\n%s %s %s\nUser-Agent: %s\nHost: %s",
              reqAddress, reqMethod, reqURI, reqProtocol, reqHeader, reqHost
          );

      logger.info(formattedString);
   }

   public static void logServerStart(String message, int port) {
      logger.info(String.format(message + " %d...", port));
   }

   public static void logError(String message, Exception e) {
      logger.log(Level.SEVERE, message, e);
   }
}
