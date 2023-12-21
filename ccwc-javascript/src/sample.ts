import * as fs from 'fs';
import * as readline from 'readline';

class FileStatsCounter {
  private lineCount = 0;
  private wordCount = 0;
  private charCount = 0;
  private byteCount = 0;

  constructor(private filePath: string) { }

  public async processFile(): Promise<void> {
    const rl = readline.createInterface({
      input: fs.createReadStream(this.filePath, { encoding: 'utf8' }),
      crlfDelay: Infinity
    });

    for await (const line of rl) {
      this.lineCount++;
      this.charCount += line.length;
      this.wordCount += this.countWords(line);
      this.byteCount += Buffer.from(line).length;
    }

    console.log('File Statistics:');
    console.log(`Lines: ${this.lineCount}`);
    console.log(`Words: ${this.wordCount}`);
    console.log(`Characters: ${this.charCount}`);
    console.log(`ByteCount: ${this.byteCount}`);
  }

  private countWords(text: string): number {
    return text.split(/\s+/).filter(Boolean).length;
  };
}

const fileStatsCounter = new FileStatsCounter(process.argv[process.argv.length - 1]);

fileStatsCounter.processFile().catch((error) => {
  console.error('Error processing the file:', error);
});


// try {
//   if (!process.argv.slice(2).length) {
//     this.program.outputHelp();
//   }

//   if (process.argv.slice(2).length === 1) {
//     this.printDefaultValue(process.argv[process.argv.length - 1]);
//   }

//   if (this.options.line) {
//     const filename = this.getFilename(this.options.line);
//     console.log(this.countLines(filename), filename);
//   }

//   if (this.options.words) {
//     const filename = this.getFilename(this.options.words);
//     console.log(this.countWords(filename), filename);
//   }

//   if (this.options.character) {
//     const filename = this.getFilename(this.options.character);
//     console.log(this.countCharacter(filename), filename)
//   }

//   if (this.options.byte_count) {
//     const filename = this.getFilename(this.options.byte_count);
//     console.log(this.countByte(filename), filename);
//   }
// } catch (error) {
//   console.error(`Error occurred: ${error}`)
// }