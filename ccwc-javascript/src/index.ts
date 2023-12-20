/* Core */
import fs from "node:fs/promises";

/* Libraries */
import figlet from "figlet";
import { Command, OptionValues } from "commander";

class CcwcTool {
  readonly program: Command;
  readonly options: OptionValues;

  constructor() {
    this.program = new Command();

    console.log(figlet.textSync("ccwc tool"));

    this.program
      .version("1.0")
      .description("Simple wc tool to count the number for lines, words, characters from a file")
      .option("filename", "Return the line, words, character, and byte counts respectively")
      .option("-l, --line <value>", "Count the number of lines in a file")
      .option("-w, --words <value>", "Count the number of words in a file")
      .option("-m, --character <value>", "Count the number of character in a file")
      .option("-c, --byte_count <value>", "Count the number of bytes in a file")
      .parse(process.argv);

    this.options = this.program.opts();
  }

  public async run() {
    try {
      if (!process.argv.slice(2).length) {
        this.program.outputHelp();
      }

      if (process.argv.slice(2).length === 1) {
        this.printDefaultValue(process.argv[process.argv.length - 1]);
      }

      if (this.options.line) {
        const filename = this.getFilename(this.options.line);
        console.log(await this.countLines(filename), filename);
      }

      if (this.options.words) {
        const filename = this.getFilename(this.options.words);
        console.log(await this.countWords(filename), filename);
      }

      if (this.options.character) {
        const filename = this.getFilename(this.options.character);
        console.log(await this.countCharacter(filename), filename)
      }

      if (this.options.byte_count) {
        const filename = this.getFilename(this.options.byte_count);
        console.log(await this.countByte(filename), filename);
      }
    } catch (error) {
      console.error(`Error occurred: ${error}`)
    }
  }

  private async countLines(filepath: string) {
    try {
      const fileContent = await this.readFileContent(filepath);
      return fileContent.split("\n").length;
    } catch (error) {
      console.error(`Error occurred: ${error}`);
    }
  }

  private async countWords(filepath: string) {
    try {
      const fileContent = await this.readFileContent(filepath);
      return fileContent.split(/\s+/).filter(word => word !== "").length;
    } catch (error) {
      console.error(`Error occurred: ${error}`);
    }
  }

  private async countCharacter(filepath: string) {
    const fileContent = await this.readFileContent(filepath);
    return fileContent.split("").length
  }

  private async countByte(filepath: string) {
    try {
      const fileSizeInBytes = await this.getFileSizeInByte(filepath);
      return fileSizeInBytes;
    } catch (error) {
      console.error(`Error occurred: ${error}`);
    }
  }

  private async printDefaultValue(filepath: string): Promise<void> {
    try {
      const [lineCount, wordsCount, byteCount] = await Promise.all([
        this.countLines(filepath),
        this.countWords(filepath),
        this.countByte(filepath)
      ]);

      console.log(lineCount, wordsCount, byteCount, filepath);
    } catch (error) {
      console.error(`Error occurred: ${error}`)
    }
  }

  private getFilename(filepath: any) {
    return typeof filepath === "string" ? filepath : __dirname;
  }

  private async readFileContent(filepath: string): Promise<string> {
    try {
      return await fs.readFile(filepath, { encoding: 'utf8' })
    } catch (error) {
      throw new Error(`Error reading file: ${error}`);
    }
  }

  private async getFileSizeInByte(filepath: string): Promise<number> {
    try {
      const stat = await fs.stat(filepath);
      return stat.size;
    } catch (error) {
      throw new Error(`Error reading file: ${error}`);
    }
  }
}

const ccwc = new CcwcTool();
ccwc.run();