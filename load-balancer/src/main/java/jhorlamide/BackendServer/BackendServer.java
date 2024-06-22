package jhorlamide.BackendServer;

import jhorlamide.RequestLogger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class BackendServer {
   public BackendServer(int port) throws Exception {
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
}
