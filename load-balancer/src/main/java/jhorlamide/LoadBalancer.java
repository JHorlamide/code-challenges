package jhorlamide;

import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "LoadBalancer", mixinStandardHelpOptions = true, version = "LoadBalancer 1.0", description = "A simple application layer load balancer server")
public class LoadBalancer implements Callable<Result> {
   @Option(names = {"-p"}, description = "The server port")
   private int port = 8080;

   @Option(names = {"-b"}, arity = "0..", description = "-b for backend position")
   private boolean isBackend;

   @Option(names = {"-blist"}, description = "-blist for accepting list of backend servers")
   private String backendList;

   public static void main(String[] args) {
      var command = new CommandLine(new LoadBalancer());
      var exitCode = command.execute(args);
      command.getExecutionResult();

      //System.exit(exitCode);
   }

   @Override
   public Result call() throws Exception {
      if (isBackend) {
         new BEServer(port);
      } else {
         System.out.println("Not backend: " + backendList);
      }

      return new Result();
   }
}