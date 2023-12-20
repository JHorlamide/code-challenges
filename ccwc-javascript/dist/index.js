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
const promises_1 = __importDefault(require("node:fs/promises"));
const commander_1 = require("commander");
class CcwcTool {
    constructor() {
        this.program = new commander_1.Command();
        this.program
            .version("1.0")
            .description("Simple wc tool to count the number for lines, words, characters from a file")
            .option("-l, --line <value>", "Count the number of lines in a file")
            .option("-w, --words <value>", "Count the number of words in a file")
            .option("-m, --character <value>", "Count the number of character in a file")
            .option("-c, --byte_count <value>", "Count the number of bytes in a file")
            .parse(process.argv);
        this.options = this.program.opts();
    }
    run() {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                if (!process.argv.slice(2).length) {
                    this.program.outputHelp();
                }
                if (process.argv.slice(2).length === 1) {
                    this.printDefaultValue(process.argv[process.argv.length - 1]);
                }
                if (this.options.line) {
                    const filename = this.getFilename(this.options.line);
                    console.log(yield this.countLines(filename), filename);
                }
                if (this.options.words) {
                    const filename = this.getFilename(this.options.words);
                    console.log(yield this.countWords(filename), filename);
                }
                if (this.options.character) {
                    const filename = this.getFilename(this.options.character);
                    console.log(yield this.countCharacter(filename), filename);
                }
                if (this.options.byte_count) {
                    const filename = this.getFilename(this.options.byte_count);
                    console.log(yield this.countByte(filename), filename);
                }
            }
            catch (error) {
                console.error(`Error occurred: ${error}`);
            }
        });
    }
    countLines(filepath) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const fileContent = yield this.readFileContent(filepath);
                return fileContent.split("\n").length;
            }
            catch (error) {
                throw new Error(error);
            }
        });
    }
    countWords(filepath) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const fileContent = yield this.readFileContent(filepath);
                return fileContent.split(/\s+/).filter(word => word !== "").length;
            }
            catch (error) {
                throw new Error(error);
            }
        });
    }
    countCharacter(filepath) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const fileContent = yield this.readFileContent(filepath);
                return fileContent.split("").length;
            }
            catch (error) {
                throw new Error(error);
            }
        });
    }
    countByte(filepath) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const fileSizeInBytes = yield this.getFileSizeInByte(filepath);
                return fileSizeInBytes;
            }
            catch (error) {
                throw new Error(error);
            }
        });
    }
    printDefaultValue(filepath) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const [lineCount, wordsCount, byteCount] = yield Promise.all([
                    this.countLines(filepath),
                    this.countWords(filepath),
                    this.countByte(filepath)
                ]);
                console.log(lineCount, wordsCount, byteCount, filepath);
            }
            catch (error) {
                throw new Error(error);
            }
        });
    }
    getFilename(optionType) {
        return typeof optionType === "string" ? optionType : __dirname;
    }
    readFileContent(filepath) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                return yield promises_1.default.readFile(filepath, { encoding: 'utf8' });
            }
            catch (error) {
                throw new Error(`Error reading file: ${error}`);
            }
        });
    }
    getFileSizeInByte(filepath) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const fileStat = yield promises_1.default.stat(filepath);
                return fileStat.size;
            }
            catch (error) {
                throw new Error(`Error reading file: ${error}`);
            }
        });
    }
}
const ccwc = new CcwcTool();
ccwc.run();
//# sourceMappingURL=index.js.map