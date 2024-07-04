package jhorlamide.LoadBalancerStrategy;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class RandomRoundRobin extends BaseLBStrategy {
   private final Logger logger = Logger.getLogger(RandomRoundRobin.class.getName());
   private final String defaultServer;

   public RandomRoundRobin(List<String> servers) {
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

      Random random = new Random();
      int randomIndex = random.nextInt(servers.size());
      return servers.get(randomIndex);
   }
}
