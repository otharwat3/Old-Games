#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")/.."
sh scripts/build.sh
javac -encoding UTF-8 -cp out/classes -d out/classes tests/DesktopRulesTest.java tests/Snake/SnakeRulesTest.java tests/Pong/PongRulesTest.java
java -Djava.awt.headless=true -cp out/classes DesktopRulesTest
java -Djava.awt.headless=true -cp out/classes Snake.SnakeRulesTest
java -Djava.awt.headless=true -cp out/classes Pong.PongRulesTest
node --test tests/engine.test.mjs
