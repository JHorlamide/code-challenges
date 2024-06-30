package jhorlamide.LoadBalancerStrategy;

public interface IRoundRobin {
   public String getNextServer();

   public void startServersHealthCheck();
}
