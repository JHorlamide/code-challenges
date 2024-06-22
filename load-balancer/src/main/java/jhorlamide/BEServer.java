package jhorlamide;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;

public class BEServer {
   protected BEServer(int port) throws Exception {
      Server server = new Server(port);

      // create servlet context handler
      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");
      server.setHandler(context);

      // Add the HelloServlet to handle request to the path "/"
      context.addServlet(new ServletHolder(new BaseRequestHandler()), "/");
      context.addServlet(new ServletHolder(new HealthCheckRequestHandler()), "/health");

      RequestLogger.logServerStart("Backend server started on port", port);

      // Start the server
      server.start();
      server.join();
   }

   public static class BaseRequestHandler extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
         response.setContentType("text/plain");
         var responseWriter = response.getWriter();

         responseWriter.println("Respond with hello jetty!");
         responseWriter.flush();
         responseWriter.close();

         RequestLogger.log(request);
      }
   }

   public static class HealthCheckRequestHandler extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
         try {
            Thread.sleep(250);

            response.setContentType("text/plain");
            var responseWriter = response.getWriter();

            responseWriter.println("Backend server is healthy");
            responseWriter.flush();
            responseWriter.close();

            RequestLogger.log(request);
         } catch (InterruptedException e) {
            RequestLogger.logError(e.getMessage(), e);
         }
      }
   }
}
