#!/bin/bash
# 一键构建（Git Bash / Windows）：javac 全量编译 + 资源打包 + jar
# 前置条件：PATH 中有 javac/jar/jimage（JDK21）；仓库根有 game-jars/ 与 .sandbox-game/游戏端/jre-9/lib/modules
set -eu
cd "$(dirname "$0")"

MODULES="../.sandbox-game/游戏端/jre-9/lib/modules"
[ -f "$MODULES" ] || { echo "[build] 缺少 $MODULES，无法抽取 JavaFX 9 classes"; exit 1; }

mkdir -p out/lib out/classes

# ---- 1) 依赖 jar（原始基准 QueshengDaPai.jar 不进编译 classpath）----
cp -f ../game-jars/*.jar out/lib/
rm -f out/lib/QueshengDaPai.jar
echo "[build] 依赖 jar 已复制到 out/lib"

# ---- 2) JavaFX 9 classes（jimage 抽取，产物在 javafx9/ 下，git 忽略）----
if [ ! -f javafx9/javafx9-classes.jar ]; then
  echo "[build] 抽取 JavaFX 9 classes ..."
  rm -rf javafx9/_extract
  mkdir -p javafx9/_extract
  jimage extract --include 'regex:/javafx\.(base|graphics|controls|fxml|swing|media|web)/.*' \
    --dir javafx9/_extract "$MODULES"
  find javafx9/_extract -name module-info.class -delete
  ARGS=""
  for m in javafx.base javafx.graphics javafx.controls javafx.fxml javafx.swing javafx.media javafx.web; do
    ARGS="$ARGS -C javafx9/_extract/$m ."
  done
  # shellcheck disable=SC2086
  jar cf javafx9/javafx9-classes.jar $ARGS
  rm -rf javafx9/_extract
fi
echo "[build] javafx9/javafx9-classes.jar OK"

# ---- 3) 编译 ----
rm -rf out/classes
mkdir -p out/classes
find src -name "*.java" | sort > out/sources.txt
javac --release 8 -encoding UTF-8 -Xlint:-options -J-Duser.language=en \
  -cp 'javafx9/javafx9-classes.jar;out/lib/*' \
  -d out/classes @out/sources.txt
echo "javac exit=$?"
echo "compiled classes=$(find out/classes -name '*.class' | wc -l)"

# ---- 4) 打包（manifest.mf 保留原 jar 的 Main-Class 与 Class-Path）----
rm -f out/quesheng.jar
jar cfm out/quesheng.jar manifest.mf -C out/classes . -C resources .
echo "jar=out/quesheng.jar size=$(wc -c < out/quesheng.jar) bytes"
echo "[build] 完成。运行: cd out && java -jar quesheng.jar"
