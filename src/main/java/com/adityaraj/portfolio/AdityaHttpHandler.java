package com.adityaraj.portfolio;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles HTTP requests for static web pages, assets, resume downloads,
 * and REST APIs including AI chatbot conversations.
 */
public class AdityaHttpHandler implements HttpHandler {

    private final Path webRoot;
    private final Path resumePath;

    public AdityaHttpHandler(Path webRoot, Path resumePath) {
        this.webRoot = webRoot;
        this.resumePath = resumePath;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        // CORS headers
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {
            if ("/api/chat".equals(path) && "POST".equalsIgnoreCase(method)) {
                handleChatApi(exchange);
            } else if ("/api/profile".equals(path) && "GET".equalsIgnoreCase(method)) {
                handleProfileApi(exchange);
            } else if ("/api/resume".equals(path) && "GET".equalsIgnoreCase(method)) {
                handleResumeDownload(exchange);
            } else if ("GET".equalsIgnoreCase(method)) {
                handleStaticFiles(exchange, path);
            } else {
                sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void handleChatApi(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        String query = extractJsonField(body, "message");

        if (query == null || query.isBlank()) {
            query = extractJsonField(body, "query");
        }

        AdityaKnowledgeBot.BotResponse response = AdityaKnowledgeBot.answerQuery(query != null ? query : "");

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"reply\":\"").append(escapeJson(response.reply())).append("\",");

        json.append("\"links\":[");
        List<AdityaKnowledgeBot.ActionLink> links = response.links();
        for (int i = 0; i < links.size(); i++) {
            AdityaKnowledgeBot.ActionLink link = links.get(i);
            json.append("{")
                .append("\"label\":\"").append(escapeJson(link.label())).append("\",")
                .append("\"url\":\"").append(escapeJson(link.url())).append("\",")
                .append("\"type\":\"").append(escapeJson(link.type())).append("\"")
                .append("}");
            if (i < links.size() - 1) json.append(",");
        }
        json.append("],");

        json.append("\"suggestions\":[");
        List<String> suggestions = response.suggestions();
        for (int i = 0; i < suggestions.size(); i++) {
            json.append("\"").append(escapeJson(suggestions.get(i))).append("\"");
            if (i < suggestions.size() - 1) json.append(",");
        }
        json.append("]");
        json.append("}");

        sendJsonResponse(exchange, 200, json.toString());
    }

    private void handleProfileApi(HttpExchange exchange) throws IOException {
        String profileJson = """
        {
          "name": "Aditya Raj",
          "title": "Senior Software Engineer",
          "tagline": "Java | Full-Stack | Google AppSheet | GCP",
          "location": "Bengaluru, Karnataka, India",
          "email": "iadityaraj1412@gmail.com",
          "phone": "+91 9570119979",
          "linkedin": "https://www.linkedin.com/in/iadityaraj1412",
          "github": "https://github.com/iadityaraj1412",
          "stats": {
            "experience": "2+ Years",
            "speedup": "60x GCP Optimization",
            "appsMaintained": "7+ Enterprise Apps",
            "uptime": "99%+",
            "dsaSolved": "500+ LeetCode/GFG",
            "incidentsResolved": "200+ (30% Defect Cut)"
          },
          "certifications": [
            { "name": "Capgemini Certified - Java Full Stack (React)", "date": "May 2025", "issuer": "Capgemini" },
            { "name": "Microsoft Certified: Azure AI Fundamentals (AI-900)", "issuer": "Microsoft" },
            { "name": "Microsoft Certified: Azure Fundamentals (AZ-900)", "issuer": "Microsoft" },
            { "name": "AWS Certified Cloud Practitioner", "issuer": "Amazon Web Services" }
          ]
        }
        """;
        sendJsonResponse(exchange, 200, profileJson);
    }

    private void handleResumeDownload(HttpExchange exchange) throws IOException {
        if (!Files.exists(resumePath)) {
            sendJsonResponse(exchange, 404, "{\"error\": \"Resume file not found\"}");
            return;
        }

        byte[] pdfBytes = Files.readAllBytes(resumePath);
        exchange.getResponseHeaders().set("Content-Type", "application/pdf");
        exchange.getResponseHeaders().set("Content-Disposition", "inline; filename=\"Aditya_Raj_Resume.pdf\"");
        exchange.sendResponseHeaders(200, pdfBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(pdfBytes);
        }
    }

    private void handleStaticFiles(HttpExchange exchange, String requestPath) throws IOException {
        String safePath = requestPath.equals("/") ? "/index.html" : requestPath;
        safePath = URLDecoder.decode(safePath, StandardCharsets.UTF_8);

        // Prevent directory traversal
        if (safePath.contains("..")) {
            sendJsonResponse(exchange, 403, "{\"error\": \"Forbidden\"}");
            return;
        }

        Path targetFile = webRoot.resolve(safePath.startsWith("/") ? safePath.substring(1) : safePath);

        if (!Files.exists(targetFile) || Files.isDirectory(targetFile)) {
            // Check if index.html is requested or fallback
            targetFile = webRoot.resolve("index.html");
            if (!Files.exists(targetFile)) {
                sendJsonResponse(exchange, 404, "{\"error\": \"File Not Found\"}");
                return;
            }
        }

        String mimeType = getMimeType(targetFile.getFileName().toString());
        byte[] fileBytes = Files.readAllBytes(targetFile);

        exchange.getResponseHeaders().set("Content-Type", mimeType);
        exchange.sendResponseHeaders(200, fileBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(fileBytes);
        }
    }

    private String getMimeType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".html") || lower.endsWith(".htm")) return "text/html; charset=utf-8";
        if (lower.endsWith(".css")) return "text/css; charset=utf-8";
        if (lower.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        if (lower.endsWith(".ico")) return "image/x-icon";
        if (lower.endsWith(".pdf")) return "application/pdf";
        if (lower.endsWith(".json")) return "application/json; charset=utf-8";
        if (lower.endsWith(".woff2")) return "font/woff2";
        if (lower.endsWith(".woff")) return "font/woff";
        return "application/octet-stream";
    }

    private void sendJsonResponse(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String extractJsonField(String json, String field) {
        if (json == null) return null;
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(field) + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return unescapeJson(matcher.group(1));
        }
        return null;
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < ' ') {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }

    private String unescapeJson(String s) {
        return s.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }
}

