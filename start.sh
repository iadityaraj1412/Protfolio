#!/bin/bash
set -e

# Determine project directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$SCRIPT_DIR"

# Locate Java JDK
if [ -x "/Library/Java/JavaVirtualMachines/jdk-26.jdk/Contents/Home/bin/java" ]; then
    JAVA_HOME_PATH="/Library/Java/JavaVirtualMachines/jdk-26.jdk/Contents/Home"
    JAVA_BIN="$JAVA_HOME_PATH/bin/java"
    JAVAC_BIN="$JAVA_HOME_PATH/bin/javac"
elif command -v java >/dev/null 2>&1; then
    JAVA_BIN="java"
    JAVAC_BIN="javac"
else
    echo "❌ Error: Java runtime not found. Please ensure JDK 21+ or JDK 26 is installed."
    exit 1
fi

echo "=================================================================="
echo "  🚀 Starting Aditya Raj's Portfolio Application & AI Chatbot   "
echo "=================================================================="
echo "  Java Compiler: $($JAVAC_BIN -version 2>&1)"
echo "  Java Runtime:  $($JAVA_BIN -version 2>&1 | head -n 1)"
echo "------------------------------------------------------------------"

mkdir -p bin
echo "🔨 Compiling Java sources..."
"$JAVAC_BIN" -d bin src/main/java/com/adityaraj/portfolio/*.java

echo "✨ Starting Portfolio Server..."
exec "$JAVA_BIN" -cp bin com.adityaraj.portfolio.AdityaPortfolioApp

