import argparse


class CcwcTool:
    def count_metrics(self, file_content):
        byte_count = len(file_content)
        lines_count = file_content.count(b"\n")
        character_count = len(file_content.decode())
        word_count = len(file_content.decode().split())

        return {
            "-c": byte_count,
            "-l": lines_count,
            "-w": word_count,
            "-m": character_count,
        }

    def ccwc(self, file_content, filename=None, options=None):
        counts = self.count_metrics(file_content)

        try:
            if options == "-c":
                byte_count = counts[options]
                print(f"{byte_count} {filename}" if filename else f"{byte_count}")

            elif options == "-l":
                lines_count = counts[options]
                print(f"{lines_count} {filename}" if filename else f"{lines_count}")

            elif options == "-w":
                word_count = counts[options]
                print(f"{word_count} {filename}" if filename else f"{word_count}")

            elif options == "-m":
                character_count = counts[options]
                print(f"{character_count} {filename}" if filename else f"{character_count}")

            elif options is None:
                print(
                    f"{lines_count} {word_count} {byte_count} {filename}"
                    if filename
                    else f"{lines_count} {word_count} {byte_count}"
                )

            else:
                print(f"Invalid option {options}")

        except Exception as e:
            print(f"An error occurred {e}")

    def main(self):
        parser = argparse.ArgumentParser(
            prog="ccwc",
            description="simple wc tool to counts the number for lines, words, characters from a file",
        )
        parser.add_argument("filename", nargs="?", help="File to analyze")
        parser.add_argument(
            "-c", "--byte-count", action="store_true", help="print the byte counts"
        )
        parser.add_argument(
            "-l", "--lines", action="store_true", help="Print the new line counts"
        )
        parser.add_argument(
            "-w", "--words", action="store_true", help="print the word counts"
        )
        parser.add_argument(
            "-m", "--characters", action="store_true", help="print the character counts"
        )

        args = parser.parse_args()

        options = None

        if args.byte_count:
            options = "-c"

        elif args.lines:
            options = "-l"

        elif args.words:
            options = "-w"

        elif args.characters:
            options = "-m"

        if args.filename:
            try:
                with open(args.filename, "rb") as file:
                    file_content = file.read()
                    self.ccwc(file_content, args.filename, options)
            except FileNotFoundError:
                print(f"Error: File {args.filename} not found")
        else:
            print("Error: Please provide either a standard input or a filename")


if __name__ == "__main__":
    ccwc = CcwcTool()
    ccwc.main()
