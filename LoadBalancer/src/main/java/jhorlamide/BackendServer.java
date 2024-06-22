package jhorlamide;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BackendServer {
   private static final Logger logger = Logger.getLogger(BackendServer.class.getName());

   protected BackendServer(int port) throws Exception {
      Server server = new Server(port);

      // create servlet context handler
      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");
      server.setHandler(context);

      // Add the HelloServlet to handle request to the path "/"
      context.addServlet(new ServletHolder(new BackendServlet()), "/");
      context.addServlet(new ServletHolder(new HealthServlet()), "/health");

      logger.info(String.format("Backend server started on port %s ...", port));

      // Start the server
      server.start();
      server.join();
   }

   private static void logRequest(HttpServletRequest request) {
      var reqAddress = request.getRemoteAddr();
      var reqMethod = request.getMethod();
      var reqURI = request.getRequestURI();
      var reqProtocol = request.getProtocol();
      var reqHeader = request.getHeader("User-Agent");
      var reqHost = request.getRemoteHost();

      logger.info(String.format("Received request from %s\n%s %s %s\nUser-Agent: %s\nHost: %s", reqAddress, reqMethod, reqURI, reqProtocol, reqHeader, reqHost));
   }

   public static class BackendServlet extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
         response.setContentType("text/plain");
         var responseWriter = response.getWriter();

         responseWriter.println("Respond with hello jetty!");
         responseWriter.flush();
         responseWriter.close();

         BackendServer.logRequest(request);
      }
   }

   public static class HealthServlet extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
         try {
            Thread.sleep(250);

         } catch (InterruptedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
         }

         response.setContentType("text/plain");
         var responseWriter = response.getWriter();

         responseWriter.println("Backend server is healthy");
         responseWriter.flush();
         responseWriter.close();

         BackendServer.logRequest(request);
      }
   }
}
