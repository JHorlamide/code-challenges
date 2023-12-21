#! /usr/bin/env node
"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const node_fs_1 = __importDefault(require("node:fs"));
class CcwcTool {
    run() {
        return __awaiter(this, void 0, void 0, function* () {
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
        });
    }
    countLines(filename) {
        try {
            const fileContent = this.readFileContent(filename);
            return fileContent.split("\n").length;
        }
        catch (error) {
            throw new Error(error);
        }
    }
    countWords(filename) {
        try {
            const fileContent = this.readFileContent(filename);
            return fileContent.split(/\s+/).filter(word => word !== "").length;
        }
        catch (error) {
            throw new Error(error);
        }
    }
    countCharacter(filename) {
        try {
            const fileContent = this.readFileContent(filename);
            return fileContent.split("").length;
        }
        catch (error) {
            throw new Error(error);
        }
    }
    countByte(filename) {
        try {
            return Buffer.byteLength(this.readFileContent(filename));
        }
        catch (error) {
            throw new Error(error);
        }
    }
    printDefaultValue(filename) {
        try {
            const lineCount = this.countLines(filename);
            const wordsCount = this.countWords(filename);
            const byteCount = this.countByte(filename);
            console.log(lineCount, wordsCount, byteCount, filename);
        }
        catch (error) {
            throw new Error(error);
        }
    }
    readFileContent(filepath) {
        try {
            return node_fs_1.default.readFileSync(filepath, "utf-8");
        }
        catch (error) {
            throw new Error(`Error reading file: ${error}`);
        }
    }
    printResult(result, filename) {
        console.log(result, filename);
    }
}
const ccwc = new CcwcTool();
ccwc.run();
//# sourceMappingURL=index.js.map