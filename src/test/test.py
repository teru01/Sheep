import subprocess
import sys
class Color:
    BLACK     = '\033[30m'
    RED       = '\033[31m'
    GREEN     = '\033[32m'
    YELLOW    = '\033[33m'
    BLUE      = '\033[34m'
    PURPLE    = '\033[35m'
    CYAN      = '\033[36m'
    WHITE     = '\033[37m'
    END       = '\033[0m'
    BOLD      = '\038[1m'
    UNDERLINE = '\033[4m'
    INVISIBLE = '\033[08m'
    REVERCE   = '\033[07m'

cmd = ["java", "-cp", "./bin/build:./lib/gluonj.jar", "sheep.array.ArrayRunner"]
testfiles = [
    "tests/assign.sheep",
    "tests/class_test.sheep",
    "tests/if_test.sheep",
    "tests/while_test.sheep",
    "tests/closure_test.sheep",
    "tests/for_test.sheep",
    "tests/const_test.sheep",
    "tests/ary_test.sheep",
]
answer = [
    "21\n2\n9\n32\n",
    "21\n18\n-7\n",
    "5\n",
    "1\n2\n3\n",
    "5\n3\n8\n",
    "0\n1\n2\n0\n1\n1\n1\n10\n",
    "20\n2\n11\n",
    "2\nhoge\nbar\nteru\n"
]


if not subprocess.run(["gradle", "compileJava"]):
    print("compile error")
    exit()

for f, ans in zip(testfiles, answer):
    try:
        result = subprocess.run(cmd + ["run", f], stdout=subprocess.PIPE, check=True, text=True, encoding='utf-8')
        if result.stdout == ans:
            print(f"{f} : " + Color.GREEN + "OK" + Color.END)
        else:
            print(f"{f} : " + Color.RED + "TEST FAILED!!" + Color.END)
    except:
        print(f"{f} : " + Color.YELLOW + "EXCEPTION" + Color.END)
