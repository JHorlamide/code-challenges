package jhorlamide.LoadBalancerStrategy;

import java.util.List;

public class SimpleRoundRobin extends BaseLBStrategy {
   private int currentIndex = -1;

   public SimpleRoundRobin(List<String> servers) {
      super(servers);
   }

   @Override
   public String getNextServer() {
      currentIndex = (currentIndex + 1) % servers.size();
      return servers.get(currentIndex);
   }
}
