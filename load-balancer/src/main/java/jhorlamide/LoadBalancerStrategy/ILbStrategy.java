package jhorlamide.LoadBalancerStrategy;

public interface ILbStrategy {
   String getNextServer();

   void startServersHealthCheck();
}
