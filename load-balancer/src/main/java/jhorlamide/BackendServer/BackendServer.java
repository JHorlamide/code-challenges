package jhorlamide.BackendServer;

import jhorlamide.RequestLogger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class BackendServer {
   public BackendServer(int port) throws Exception {
      // Create a thread pool with a minimum of 10 threads and a maximum of 20 threads
      QueuedThreadPool threadPool = new QueuedThreadPool(20);

      // Create server with the thread pool
      Server server = new Server(threadPool);

      // Create a server connector
      ServerConnector connector = new ServerConnector(server);
      connector.setPort(port);
      server.addConnector(connector);

      // create servlet context handler
      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");
      server.setHandler(context);

      // Add the BaseRequestHandler to handle requests to the path "/*"
      context.addServlet(new ServletHolder(new BaseRequestHandler()), "/*");

      // Add the HealthCheckRequestHandler to handle requests to the path "/health/*"
      context.addServlet(new ServletHolder(new HealthCheckRequestHandler()), "/health/*");

      RequestLogger.logServerStart("Backend server started on port", port);

      // Start the server
      server.start();
      server.join();
   }
}
