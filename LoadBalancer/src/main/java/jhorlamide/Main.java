package jhorlamide;

import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "LoadBalancer", mixinStandardHelpOptions = true, version = "1.0", description = "A simple load balancer server")
public class Main implements Callable<Result> {
   @CommandLine.Option(names = {"-p"}, description = "-p for specify port")
   private boolean switchCharacters;

   @CommandLine.Option(names = {"-b"}, description = "-b for backend position")
   private boolean switchLine;

   @CommandLine.Option(names = {"-blist"}, description = "-blist for accepting list of backend servers")
   private boolean switchWords;

   public static void main(String[] args) throws Exception {
      var command = new CommandLine(new Main());
      var exitCode = command.execute(args);
      var result = command.getExecutionResult();
   }

   @Override
   public Result call() throws Exception {
      return null;
   }
}