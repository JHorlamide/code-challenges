package jhorlamide;

import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "LoadBalancer", mixinStandardHelpOptions = true, version = "LoadBalancer 1.0", description = "A simple application layer load balancer server")
public class Main implements Callable<Result> {
   @Option(names = {"-p"}, description = "The server port")
   private int port = 8080;

   @Option(names = {"-b"}, arity = "0..", description = "-b for backend position")
   private boolean isBackend;

   @Option(names = {"-blist"}, description = "-blist for accepting list of backend servers")
   private String backendList;

   public static void main(String[] args) {
      var command = new CommandLine(new Main());
      var exitCode = command.execute(args);
      command.getExecutionResult();

      //System.exit(exitCode);
   }

   @Override
   public Result call() throws Exception {
      if (isBackend) {
         new BackendServer(port);
      } else {
         System.out.println("Not backend");
      }

      return new Result();
   }
}