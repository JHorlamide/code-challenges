package jhorlamide;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "ccwc", mixinStandardHelpOptions = true, version = "ccwc 1.0",
        description = "simple wc tool to counts the number for lines, words, characters from a file")
public class Ccwc implements Callable<Result> {
    @Parameters(index = "0", description = "The file to calculate for.", defaultValue = "./text.txt")
    private File file;

    @Option(names = {"-c"}, description = "-c for counting character")
    private boolean switchCharacters;

    @Option(names = {"-l"}, description = "-l for count lines")
    private boolean switchLine;

    @Option(names = {"-w"}, description = "-w for count words")
    private boolean switchWords;

    public void run(String args) {
        var command = new CommandLine(new Ccwc());
        var exitCode = command.execute(args);
        var result = command.getExecutionResult();
        var resultString = new StringBuffer();
        resultString.append(" ");

        System.out.println(result.toString());
        System.exit(exitCode);
    }

    @Override
    public Result call() throws Exception {
        var result = new Result();

        try {

            Path path = Path.of(this.file.toURI());
            byte[] fileContent = Files.readAllBytes(path);
            boolean switchAll = (this.switchCharacters == this.switchLine) && (this.switchLine == this.switchWords);

            if (switchAll) {
                this.switchLine = true;
                this.switchWords = true;
                this.switchCharacters = true;
            }

            if (this.switchCharacters) {
                result.charCount = getCharsCount(fileContent);
            }

            if (this.switchLine) {
                result.lineCount = getLineCount(fileContent);
            }

            if (this.switchWords) {
                result.wordCount = getWordCount(fileContent);
            }

            return result;
        } catch (Exception exception) {
            throw new FileNotFoundException();
        }
    }

    private int getCharsCount(byte[] fileBytes) {
        return fileBytes.length;
    }

    private int getLineCount(byte[] fileBytes) {
        var i = 0;

        for (byte fileByte : fileBytes) {
            if (fileByte == '\n') {
                i++;
            }
        }

        return i;
    }

    private int getWordCount(byte[] fileBytes) {
        var i = 0;
        var lastWordCount = 0;

        for (byte fileByte : fileBytes) {
            if (Character.isWhitespace(fileByte)) {
                if (lastWordCount > 0) {
                    i++;
                    lastWordCount = 0;
                }
            } else {
                lastWordCount++;
            }

            if (lastWordCount > 0) {
                i++;
            }
        }

        return i;
    }
}
