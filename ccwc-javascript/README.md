# ccwc tool

This is a simple version of the Unix command line tool wc

This is a code challenge to build a version of the Unix command line tool wc by [The Coding Challenges](https://codingchallenges.fyi/challenges/intro/).

Challenge  description can be found [here](https://codingchallenges.fyi/challenges/challenge-wc/)

This script simulate the `wc` command. It provides the options `-c` , `-l`, `-w`, and `-m`. It also supports the default `wc` behaviour where no option is given, and accepts input from the stdin.

### Running the script

Make sure that you have python installed

* Clone the project and `cd` to the ccwc folder
* Run `npm run build`
* Run `npm install -g .` to install globally.
* Run ccwc `<option> <path_to_file>` or
* Run ccwc `<path_to_file>`
