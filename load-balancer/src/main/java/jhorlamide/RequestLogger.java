package jhorlamide;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestLogger {
   private static final Logger logger = Logger.getLogger(RequestLogger.class.getName());

   public static void log(HttpServletRequest request, @Nullable String message) {
      generateLogFormatAndLogRequest(null, request, null, message);
   }

   public static void log(
           String url, HttpServletRequest request, String statusResponse, String message) {
      generateLogFormatAndLogRequest(url, request, statusResponse, message);
   }

   public static void logServerStart(String message, int port) {
      logger.info(message + " " + port + "...");
   }

   public static void logError(String message, Exception e) {
      logger.log(Level.SEVERE, message, e);
   }

   private static void generateLogFormatAndLogRequest(
           String url, HttpServletRequest request,
           String statusResponse, String message) {
      var reqAddress = request.getRemoteAddr();
      var reqMethod = request.getMethod();
      var reqURI = request.getRequestURI();
      var reqProtocol = request.getProtocol();
      var reqHeader = request.getHeader("User-Agent");
      var reqHost = request.getRemoteHost();

      // Log request details
      StringBuilder logMessage = new StringBuilder();
      logMessage.append("\n").append("Received request from: ").append(reqAddress).append("\n");
      logMessage.append(reqMethod).append(" ").append(reqURI).append(" ").append(reqProtocol).append("\n");
      logMessage.append("User-Agent: ").append(reqHeader).append("\n");
      logMessage.append("Host: ").append(reqHost).append("\n");

      if (url != null) {
         logMessage.insert(0, url).append("\n");
      }

      if (statusResponse != null) {
         logMessage.append("\n").append(statusResponse);
      }

      if (message != null) {
         logMessage.append("\n\n").append(message);
      }

      logger.info(logMessage.toString());
   }
}
