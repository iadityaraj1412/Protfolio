# Multi-stage Docker build for Aditya Raj's Java Portfolio Application
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy source code and resources
COPY src/ ./src/
COPY web/ ./web/
COPY Aditya_Raj_Resume.pdf ./Aditya_Raj_Resume.pdf
COPY me.png ./me.png

# Compile Java sources
RUN mkdir -p bin && \
    javac -d bin src/main/java/com/adityaraj/portfolio/*.java

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy compiled binaries and web assets
COPY --from=builder /app/bin ./bin
COPY --from=builder /app/web ./web
COPY --from=builder /app/Aditya_Raj_Resume.pdf ./Aditya_Raj_Resume.pdf
COPY --from=builder /app/me.png ./me.png

# Set cloud environment defaults
ENV PORT=8080
ENV JAVA_OPTS="-Djava.awt.headless=true -Xmx256m"

EXPOSE 8080

# Launch Java Portfolio Server
CMD ["sh", "-c", "java $JAVA_OPTS -cp bin com.adityaraj.portfolio.AdityaPortfolioApp"]
