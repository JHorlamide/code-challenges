package jhorlamide.LoadBalancerStrategy;

import java.util.List;

public class RoundRobin {
   private final List<String> servers;
   private int currentIndex = -1;

   public RoundRobin(List<String> servers) {
      this.servers = servers;
   }

   public String getNextServer() {
      currentIndex = (currentIndex + 1) % servers.size();
      return servers.get(currentIndex);
   }
}
