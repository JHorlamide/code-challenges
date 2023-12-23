#! /usr/bin/env node

import fs from "node:fs";

export class CcwcTool {
  public run() {
    const [option, filename] = process.argv.slice(2);

    if (filename === undefined) {
      return this.printDefaultValue(option);
    }

    switch (option) {
      case "-c":
        this.printResult(this.countByte(filename), filename);
        break;
      case "-l":
        this.printResult(this.countLines(filename), filename);
        break;
      case "-w":
        this.printResult(this.countWords(filename), filename);
        break;
      case "-m":
        this.printResult(this.countCharacter(filename), filename);
        break;
      default:
        console.error("Invalid option. Please use -c, -l, -w, or -m.");
        break;
    }
  }

  private countLines(filename: string) {
    const fileContent = this.readFileContent(filename);
    return fileContent.split("\n").length ? fileContent.split("\n").length - 1 : 0;
  }

  private countWords(filename: string) {
    const fileContent = this.readFileContent(filename);
    return fileContent.split(/\s+/).filter(word => word !== "").length;
  }

  private countCharacter(filename: string) {
    const fileContent = this.readFileContent(filename);
    return fileContent.split("").length;
  }

  private countByte(filename: string) {
    return Buffer.byteLength(this.readFileContent(filename));
  }

  private printDefaultValue(filename: string) {
    const lineCount = this.countLines(filename);
    const wordsCount = this.countWords(filename)
    const byteCount = this.countByte(filename);
    console.log(lineCount, wordsCount, byteCount, filename);
  }

  private readFileContent(filepath: string): string {
    try {
      return fs.readFileSync(filepath, "utf-8");
    } catch (error) {
      throw new Error(`Error reading file: ${error}`);
    }
  }

  private printResult(result: number, filename: string) {
    console.log(result, filename);
  }
}

const ccwc = new CcwcTool();
ccwc.run();