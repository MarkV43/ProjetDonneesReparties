alias b := build
alias r := run
alias e := execute
alias s := server

default: build

build:
    javac src/*/*.java

run FILE *PARAMS:
    java -cp bin:src/: {{FILE}} {{PARAMS}}

execute FILE *PARAMS:
    just b
    just r {{FILE}} {{PARAMS}}

server PORT='8080':
    just e etape1/Server Server {{PORT}}