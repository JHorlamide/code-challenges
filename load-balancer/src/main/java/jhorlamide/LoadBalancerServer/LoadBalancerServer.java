package jhorlamide.LoadBalancerServer;

import jhorlamide.RequestLogger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.List;

public class LoadBalancerServer {
   private final List<String> backendServers;

   public LoadBalancerServer(int port, List<String> backendServers) throws Exception {
      this.backendServers = backendServers;

      Server server = new Server(port);

      // create servlet context handler
      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");
      server.setHandler(context);

      // Add the HelloServlet to handle request to the path "/"
      context.addServlet(new ServletHolder(new LBRequestHandler(backendServers)), "/");

      RequestLogger.logServerStart("Load balancer server started on port", port);

      // Start the server
      server.start();
      server.join();
   }
}
