alias b := build
alias r := run
alias e := execute
alias s := server
alias i := irc

default: build

build:
    javac src/*/*.java

run FILE *PARAMS:
    java -cp bin:src/: {{FILE}} {{PARAMS}}

execute FILE *PARAMS:
    just b
    just r {{FILE}} {{PARAMS}}

irc ETAPE NAME:
    just e etape{{ETAPE}}/Irc {{NAME}}

server ETAPE PORT='8080':
    just e etape{{ETAPE}}/Server Server {{PORT}}