package jhorlamide;

public class Result {

    public String fileName;
    public int lineCount = -1;
    public int wordCount = -1;
    public int charCount = -1;

    @Override
    public String toString() {
        var builder = new StringBuilder();
        if (lineCount >= 0) {
            builder.append("\t");
            builder.append(lineCount);
        }
        if (wordCount >= 0) {
            builder.append("\t");
            builder.append(wordCount);
        }
        if (charCount>= 0) {
            builder.append("\t");
            builder.append(charCount);
        }
        builder.append("\t");
        builder.append(this.fileName);
        return builder.toString();
    }
}
