package com.adityaraj.portfolio;

import com.sun.net.httpserver.HttpServer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Main launcher for Aditya Raj's Portfolio Application.
 * Starts an embedded HTTP server, auto-opens the modern web UI in the default browser,
 * and launches a sleek desktop companion controller.
 */
public class AdityaPortfolioApp {

    private static final int DEFAULT_PORT = 8080;
    private static HttpServer server;
    private static int activePort = DEFAULT_PORT;

    public static void main(String[] args) {
        // Resolve project paths
        Path currentDir = Paths.get("").toAbsolutePath();
        Path webRoot = currentDir.resolve("web");
        if (!webRoot.toFile().exists()) {
            // Check parent dir
            webRoot = currentDir.resolve("Protfolio/web");
        }

        Path resumePath = currentDir.resolve("Aditya_Raj_Resume.pdf");
        if (!resumePath.toFile().exists()) {
            resumePath = webRoot.resolve("assets/Aditya_Raj_Resume.pdf");
        }

        // Check for cloud environment variable PORT (e.g. Render, Heroku, Cloud Run)
        int initialPort = DEFAULT_PORT;
        String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.isBlank()) {
            try {
                initialPort = Integer.parseInt(envPort.trim());
            } catch (NumberFormatException ignored) {}
        }

        // Start server with fallback ports
        boolean started = false;
        for (int port = initialPort; port < initialPort + 10; port++) {
            try {
                server = HttpServer.create(new InetSocketAddress(port), 0);
                activePort = port;
                started = true;
                break;
            } catch (IOException ignored) {}
        }

        if (!started) {
            System.err.println("❌ Could not bind to any port between 8080 and 8089.");
            System.exit(1);
        }

        AdityaHttpHandler handler = new AdityaHttpHandler(webRoot, resumePath);
        server.createContext("/", handler);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();

        String appUrl = "http://localhost:" + activePort;

        System.out.println("==================================================================");
        System.out.println("  🌟 ADITYA RAJ - PORTFOLIO APPLICATION & AI CHATBOT ENGINE 🌟   ");
        System.out.println("==================================================================");
        System.out.println("  🚀 Server running at: " + appUrl);
        System.out.println("  📁 Web directory:     " + webRoot);
        System.out.println("  📄 Resume source:     " + resumePath);
        System.out.println("  🤖 AI Knowledge Base: Initialized & Ready");
        System.out.println("==================================================================");
        System.out.println("  Opening modern portfolio UI in your default browser...\n");

        // Open in browser
        openWebpage(appUrl);

        // Try launching Desktop Companion UI if GUI environment is supported
        if (!GraphicsEnvironment.isHeadless()) {
            SwingUtilities.invokeLater(() -> createAndShowCompanionUI(appUrl));
        }
    }

    public static void openWebpage(String url) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                // macOS fallback
                Runtime.getRuntime().exec(new String[]{"open", url});
            }
        } catch (Exception e) {
            System.out.println("ℹ️ Please open your browser and navigate to: " + url);
        }
    }

    private static void createAndShowCompanionUI(String appUrl) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JFrame frame = new JFrame("Aditya Raj Portfolio Companion");
        frame.setSize(520, 380);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(15, 23, 42)); // Slate 900
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(15, 23, 42));

        JLabel titleLabel = new JLabel("Aditya Raj Portfolio Server");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(248, 250, 252));

        JLabel statusLabel = new JLabel("● Online (Port " + activePort + ")");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(new Color(34, 197, 94)); // Emerald 500

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(statusLabel, BorderLayout.SOUTH);

        // Center Info Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(30, 41, 59));
        centerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel info1 = new JLabel("Java Backend: Java 26 Embedded HttpServer");
        info1.setForeground(new Color(203, 213, 225));
        info1.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel info2 = new JLabel("Chatbot: AI Resume Knowledge Engine Active");
        info2.setForeground(new Color(203, 213, 225));
        info2.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel info3 = new JLabel("App URL: " + appUrl);
        info3.setForeground(new Color(96, 165, 250));
        info3.setFont(new Font("Segoe UI", Font.BOLD, 14));

        centerPanel.add(info1);
        centerPanel.add(Box.createVerticalStrut(6));
        centerPanel.add(info2);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(info3);

        // Bottom Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(new Color(15, 23, 42));

        JButton openBtn = new JButton("🌐 Open Portfolio Web App");
        openBtn.setBackground(new Color(59, 130, 246));
        openBtn.setForeground(Color.BLACK);
        openBtn.setFocusPainted(false);
        openBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        openBtn.addActionListener(e -> openWebpage(appUrl));

        JButton exitBtn = new JButton("Stop & Exit");
        exitBtn.setBackground(new Color(239, 68, 68));
        exitBtn.setForeground(Color.BLACK);
        exitBtn.setFocusPainted(false);
        exitBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        exitBtn.addActionListener(e -> {
            if (server != null) server.stop(0);
            System.exit(0);
        });

        actionPanel.add(exitBtn);
        actionPanel.add(openBtn);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        frame.setContentPane(panel);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (server != null) server.stop(0);
                System.exit(0);
            }
        });
    }
}

