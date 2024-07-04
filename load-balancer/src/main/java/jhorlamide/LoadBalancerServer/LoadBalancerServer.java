package jhorlamide.LoadBalancerServer;

import jhorlamide.LoadBalancerStrategy.ILbStrategy;
import jhorlamide.LoadBalancerStrategy.RandomRoundRobin;
import jhorlamide.RequestLogger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.List;

public class LoadBalancerServer {
   public LoadBalancerServer(int port, List<String> backendServers) throws Exception {
      // ILbStrategy lbStrategy = new SimpleRoundRobin(backendServers);
      // ILbStrategy lbStrategy = new BetterRoundRobin(backendServers);
      ILbStrategy lbStrategy = new RandomRoundRobin(backendServers);
      lbStrategy.startServersHealthCheck();

      // Create a thread pool with a minimum of 10 threads and a maximum of 20 threads
      QueuedThreadPool threadPool = new QueuedThreadPool(20);

      // Create server with the thread pool
      Server server = new Server(threadPool);

      // Create server connector
      ServerConnector connector = new ServerConnector(server);
      connector.setPort(port);
      server.addConnector(connector);

      // create servlet context handler
      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");
      server.setHandler(context);

      // Add the HelloServlet to handle request to the path "/"
      context.addServlet(new ServletHolder(new LBRequestHandler(lbStrategy)), "/");

      RequestLogger.logServerStart("Load balancer server started on port", port);

      // Start the server
      server.start();
      server.join();
   }
}
