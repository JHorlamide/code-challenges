package jhorlamide;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "ccwc", mixinStandardHelpOptions = true, version = "ccwc 1.0",
        description = "simple wc tool to counts the number for lines, words, characters from a file")
public class Main implements Callable<String> {
    public static void main(String[] args) {
        var ccwc = new Ccwc();
        var command = new CommandLine(ccwc);
        var exitCode = command.execute(args);
        var result = command.getExecutionResult();


        System.out.println(result);
        System.exit(exitCode);
    }

    @Override
    public String call() throws Exception {
        return "Hello, World!";
    }
}