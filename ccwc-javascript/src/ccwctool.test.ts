import test, { beforeEach, describe } from "node:test";
import { CcwcTool } from "./ccwctool";

describe("Ccwc Tool", function () {
  let ccwc: CcwcTool;

  beforeEach(function () {
    ccwc = new CcwcTool();
  });

  test("Should a correct number of lines", function () {
    const filename = "test.txt";
    const countLine = ccwc["countLines"](filename);
    // expect(countLine).toBe("12")
  })
})