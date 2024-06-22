package jhorlamide;

import jhorlamide.BackendServer.BackendServer;
import jhorlamide.LoadBalancerServer.LoadBalancerServer;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "LoadBalancer",
    mixinStandardHelpOptions = true,
    version = "LoadBalancer 1.0",
    description = "A simple application layer load balancer server")
public class LoadBalancer implements Callable<Result> {
   @Option(names = {"-p"}, description = "The server port")
   private int PORT = 8080;

   @Option(names = {"-b"}, arity = "0..", description = "-b for backend position")
   private boolean isBackend;

   @Option(names = {"-blist"}, description = "-blist for accepting list of backend servers")
   private String backendList;

   public static void main(String[] args) {
      var command = new CommandLine(new LoadBalancer());
      var exitCode = command.execute(args);
      command.getExecutionResult();
      System.exit(exitCode);
   }

   @Override
   public Result call() throws Exception {
      if (isBackend) {
         new BackendServer(PORT);
      } else {
         if (backendList == null || backendList.isEmpty()) {
            backendList = "http://localhost:9090";
         }

         String[] backendListArray = backendList.split(",");
         List<String> backendServers = new ArrayList<>(Arrays.asList(backendListArray));
         new LoadBalancerServer(PORT, backendServers);
      }

      return new Result();
   }
}