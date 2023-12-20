package jhorlamide;

import picocli.CommandLine;

@CommandLine.Command(name = "ccwc", mixinStandardHelpOptions = true, version = "ccwc 1.0",
        description = "simple wc tool to counts the number for lines, words, characters from a file")
public class Main {
    public static void main(String[] args) {
        var ccwc = new Ccwc();
        var cmd = new CommandLine(ccwc);
        var exitCode = cmd.execute(args);
        var result = cmd.getExecutionResult();

        System.out.println(result);
        System.exit(exitCode);
    }
}