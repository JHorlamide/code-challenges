package jhorlamide;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BackendServer {
   private final int PORT;
   private static final Logger logger = LoggerFactory.getLogger(BackendServer.class);


   public BackendServer(int port) {
      this.PORT = port;
   }

   public void createServer() throws Exception {
      Server server = new Server(PORT);

      // create servlet context handler
      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");
      server.setHandler(context);

      // Add the HelloServlet to handle request to the path "/"
      context.addServlet(new ServletHolder(new HelloServlet()), "/hello");
      logger.info("Backend server started on port {} ...", PORT);

      // Start the server
      server.start();
      server.join();
   }

   public static class HelloServlet extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
         response.setContentType("text/plain");
         response.getWriter().println("Hello, Jetty!");
      }
   }
}
