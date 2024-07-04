package jhorlamide.LoadBalancerStrategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class BetterRoundRobin extends BaseLBStrategy {
   private static final Logger logger = Logger.getLogger(BetterRoundRobin.class.getName());
   private final String defaultServer;
   private final AtomicInteger currentIndex = new AtomicInteger(0);

   public BetterRoundRobin(List<String> servers) {
      super(servers);

      if (servers == null || servers.isEmpty()) {
         throw new IllegalArgumentException("Server list cannot be empty");
      }

      this.defaultServer = servers.get(0);
   }

   @Override
   public String getNextServer() {
      var servers = getServers();

      if (servers.isEmpty()) {
         logger.info("Server list is empty. Returning default server");
         return defaultServer;
      }

      var nextServerIndex = currentIndex.getAndUpdate(i -> (i + 1) % servers.size());
      var nextServer = servers.get(nextServerIndex);

      if (nextServer == null || nextServer.isEmpty()) {
         logger.info("Next server is empty. Returning default server");
         return defaultServer;
      }

      return nextServer;
   }
}
