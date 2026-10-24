#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")/.."
mkdir -p out/classes
javac -encoding UTF-8 -d out/classes src/arcade/*.java src/*.java src/Pong/*.java src/Snake/*.java
jar --create --file out/old-games.jar --main-class OldGamesMain -C out/classes .
printf 'Built out/old-games.jar — run: java -jar out/old-games.jar\n'
