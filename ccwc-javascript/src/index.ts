#! /usr/bin/env node

import fs from "node:fs";

class CcwcTool {
  public async run() {
    const [option, filename] = process.argv.slice(2);
    const noOption = process.argv.slice(2).length === 1;

    if (noOption) {
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
    try {
      const fileContent = this.readFileContent(filename);
      return fileContent.split("\n").length;
    } catch (error: any) {
      throw new Error(error);
    }
  }

  private countWords(filename: string) {
    try {
      const fileContent = this.readFileContent(filename);
      return fileContent.split(/\s+/).filter(word => word !== "").length;
    } catch (error: any) {
      throw new Error(error);
    }
  }

  private countCharacter(filename: string) {
    try {
      const fileContent = this.readFileContent(filename);
      return fileContent.split("").length;
    } catch (error: any) {
      throw new Error(error);
    }
  }

  private countByte(filename: string) {
    try {
      return Buffer.byteLength(this.readFileContent(filename));
    } catch (error: any) {
      throw new Error(error);
    }
  }

  private printDefaultValue(filename: string) {
    try {
      const lineCount = this.countLines(filename);
      const wordsCount = this.countWords(filename)
      const byteCount = this.countByte(filename);
      console.log(lineCount, wordsCount, byteCount, filename);
    } catch (error: any) {
      throw new Error(error);
    }
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