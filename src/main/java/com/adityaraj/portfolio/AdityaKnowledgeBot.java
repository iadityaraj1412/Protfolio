package com.adityaraj.portfolio;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Intelligent Knowledge Engine and NLP intent classifier for Aditya Raj.
 * Contains exhaustive details extracted from his resume, projects, certifications,
 * experience, and social profiles, generating structured answers with actionable links.
 */
public class AdityaKnowledgeBot {

    public record ActionLink(String label, String url, String type) {}
    public record BotResponse(String reply, List<ActionLink> links, List<String> suggestions) {}

    private static final String LINKEDIN_URL = "https://www.linkedin.com/in/iadityaraj1412";
    private static final String GITHUB_URL = "https://github.com/iadityaraj1412";
    private static final String EMAIL_URL = "mailto:iadityaraj1412@gmail.com";
    private static final String PHONE_URL = "tel:+919570119979";
    private static final String RESUME_URL = "/api/resume";

    public static BotResponse answerQuery(String rawQuery) {
        if (rawQuery == null || rawQuery.trim().isEmpty()) {
            return getGreetingResponse();
        }

        String q = rawQuery.trim().toLowerCase();

        // 1. Greetings
        if (matchesAny(q, "hi", "hello", "hey", "hola", "namaste", "greetings", "good morning", "good evening", "what's up", "sup")) {
            return getGreetingResponse();
        }

        // 2. Resume / CV download
        if (matchesAny(q, "resume", "cv", "curriculum vitae", "download resume", "view resume", "pdf", "profile doc")) {
            return new BotResponse(
                "### 📄 Aditya Raj's Resume\n\n" +
                "You can view or download Aditya's official resume directly right here. It highlights his **2+ years of experience** as a Senior Software Engineer at Capgemini, expertise in Java, Spring Boot, Google Cloud Platform, and 60x pipeline optimization achievements.",
                List.of(
                    new ActionLink("📥 Download Resume (PDF)", RESUME_URL, "download"),
                    new ActionLink("🔗 View LinkedIn Profile", LINKEDIN_URL, "external")
                ),
                List.of("Tell me about his work at Capgemini", "What are his key skills?", "How did he achieve 60x optimization?")
            );
        }

        // 3. Contact & Social Media
        if (matchesAny(q, "contact", "email", "phone", "number", "mobile", "linkedin", "social", "github", "reach", "hire", "call", "location", "address", "city", "bangalore", "bengaluru")) {
            return new BotResponse(
                "### 📬 Get in Touch with Aditya Raj\n\n" +
                "Aditya is currently based in **Bengaluru, Karnataka, India** and is open to opportunities as a **Senior Software Engineer / Java Full-Stack Specialist**.\n\n" +
                "- 📧 **Email**: [iadityaraj1412@gmail.com](mailto:iadityaraj1412@gmail.com)\n" +
                "- 📱 **Phone**: [+91 9570119979](tel:+919570119979)\n" +
                "- 💼 **LinkedIn**: [linkedin.com/in/iadityaraj1412](https://www.linkedin.com/in/iadityaraj1412)\n" +
                "- 🐙 **GitHub**: [github.com/iadityaraj1412](https://github.com/iadityaraj1412)\n" +
                "- 📍 **Location**: Bengaluru, Karnataka, India",
                List.of(
                    new ActionLink("📧 Send an Email", EMAIL_URL, "email"),
                    new ActionLink("💼 Connect on LinkedIn", LINKEDIN_URL, "external"),
                    new ActionLink("📞 Call Aditya (+91 9570119979)", PHONE_URL, "tel"),
                    new ActionLink("📄 View Resume", RESUME_URL, "download")
                ),
                List.of("Tell me about his Capgemini experience", "What are his technical skills?", "Show me his projects")
            );
        }

        // 4. 60x Airflow Optimization & Big Achievements
        if (matchesAny(q, "60x", "airflow", "cloud composer", "optimization", "optimise", "pipeline", "speedup", "dag", "performance", "achievement", "award", "champion")) {
            return new BotResponse(
                "### ⚡ 60x Pipeline Acceleration & Major Achievements\n\n" +
                "Aditya engineered standout performance optimizations and critical migrations at **Capgemini** (Client: APTIV / Versigent):\n\n" +
                "1. 🚀 **60x GCP Data Pipeline Optimization**:\n" +
                "   - Refactored complex Apache Airflow (Google Cloud Composer) DAG logic.\n" +
                "   - Implemented task parallelisation and optimised query execution, slashing end-to-end processing time **from 90 minutes down to just 90 seconds** (a 60x improvement!).\n\n" +
                "2. 🛡️ **Microsoft Graph API Migration**:\n" +
                "   - Seamlessly migrated legacy SharePoint file-transfer scheduler jobs to Microsoft Graph API ahead of Microsoft's April 2026 legacy authentication deprecation, eliminating downtime and hardening security.\n\n" +
                "3. 🏆 **Capgemini 'Champion' Award**:\n" +
                "   - Recognized with the prestigious Capgemini 'Champion' title for consistently high-quality technical delivery and exceeding client satisfaction benchmarks.\n\n" +
                "4. 🛠️ **Production Reliability & Incident Management**:\n" +
                "   - Resolved **200+ production incidents** with in-depth root cause analysis (RCA), cutting recurring failures by **30%** while maintaining **99%+ uptime** across 7+ enterprise Java Spring Boot and Google AppSheet apps.",
                List.of(
                    new ActionLink("📄 Verify in Resume", RESUME_URL, "download"),
                    new ActionLink("💼 View Experience on LinkedIn", LINKEDIN_URL, "external")
                ),
                List.of("What is his role at Capgemini?", "What technologies did he use?", "Tell me about his certifications")
            );
        }

        // 5. Work Experience / Companies (Capgemini, Aptiv, Celebal)
        if (matchesAny(q, "experience", "work", "job", "career", "company", "companies", "capgemini", "aptiv", "celebal", "versigent", "senior software engineer", "history", "role", "current role")) {
            return new BotResponse(
                "### 💼 Professional Experience Overview (2+ Years)\n\n" +
                "Aditya Raj is a **Senior Software Engineer** with extensive expertise across Java, Spring Boot, Google Cloud Platform, and enterprise workflow automation:\n\n" +
                "**1. Capgemini — Senior Software Engineer** *(Client: APTIV / Versigent)*\n" +
                "*Bengaluru, Karnataka | Jul 2026 – Present*\n" +
                "- Oversees 24/7 production support and feature enhancements for **7+ enterprise Java Spring Boot & Google AppSheet applications** with **99%+ uptime**.\n" +
                "- Migrated critical SharePoint file-transfer scheduler jobs to **Microsoft Graph API**.\n" +
                "- Slashed GCP data pipeline processing runtime from **90 minutes to 90 seconds (60x speedup)** via Apache Airflow DAG refactoring.\n" +
                "- Resolved **200+ production tickets** with proactive RCA, cutting recurring defects by 30%.\n\n" +
                "**2. Capgemini — Software Engineer** *(Client: APTIV)*\n" +
                "*Bengaluru, Karnataka | Oct 2024 – Jun 2026*\n" +
                "- Built enterprise backend microservices and REST APIs using **Java and Spring Boot**.\n" +
                "- Developed enterprise workflow automation apps with **Google AppSheet**.\n" +
                "- Automated third-party API data ingestion pipelines.\n" +
                "- Awarded the **Capgemini 'Champion' recognition** for technical excellence.\n\n" +
                "**3. Celebal Technology — Node.JS Developer Intern**\n" +
                "*Remote | Jun 2023 – Aug 2023*\n" +
                "- Engineered RESTful backend services in Node.js & Express.js.\n" +
                "- Designed MongoDB schema architectures for optimal read/write throughput.",
                List.of(
                    new ActionLink("📄 View Full Resume", RESUME_URL, "download"),
                    new ActionLink("💼 Visit LinkedIn", LINKEDIN_URL, "external")
                ),
                List.of("Tell me about the 60x Airflow speedup", "What technical skills does he have?", "What projects has he built?")
            );
        }

        // 6. Skills & Tech Stack
        if (matchesAny(q, "skill", "skills", "tech", "stack", "technology", "technologies", "java", "spring", "spring boot", "backend", "frontend", "database", "gcp", "cloud", "appsheet", "react", "node", "express", "mongodb", "sql", "microservice", "tools")) {
            return new BotResponse(
                "### 💻 Technical Skills & Core Competencies\n\n" +
                "Aditya possesses deep full-stack and cloud engineering proficiency:\n\n" +
                "- ☕ **Programming Languages**: Java (Java 8 to 26), SQL, JavaScript, HTML5, CSS3\n" +
                "- 🍃 **Backend Frameworks**: Spring Framework, Spring Boot, Node.js, Express.js, REST APIs, MVC, JDBC, Microservices\n" +
                "- ☁️ **Cloud & Low-Code**: Google Cloud Platform (GCP), Google AppSheet, GenAI, Cloud Composer, BigQuery, Apache Airflow, Microsoft Azure, AWS\n" +
                "- 🗄️ **Databases**: MongoDB, Microsoft SQL Server, Database Schema Design, Indexing & Query Tuning\n" +
                "- ⚛️ **Frontend**: React.js, Java Swing, AWT, Modern Responsive Web Design\n" +
                "- 🛠️ **Tools & Platforms**: Git, Postman, Visual Studio Code, Eclipse, Android Studio, Flutter, ServiceNow\n" +
                "- 🧠 **Methodologies & Concepts**: Object-Oriented Programming (OOP), Data Structures & Algorithms (DSA), System Design, Agile/Scrum, CI/CD\n" +
                "- 🎯 **Core Competencies**: Production Support, Incident Management (200+ RCA), API Integration, Workflow Automation, Performance Tuning",
                List.of(
                    new ActionLink("📂 Explore Projects", "#projects", "internal"),
                    new ActionLink("📜 View Certifications", "#certifications", "internal"),
                    new ActionLink("📥 Download Resume", RESUME_URL, "download")
                ),
                List.of("What projects did he build with Java?", "Tell me about his certifications", "What is his experience at Capgemini?")
            );
        }

        // 7. Projects
        if (matchesAny(q, "project", "projects", "portfolio", "electricity", "billing", "employee management", "website", "built", "app", "application")) {
            return new BotResponse(
                "### 🚀 Featured Engineering Projects\n\n" +
                "**1. Electricity Billing System** *(Java, Swing, MongoDB)*\n" +
                "- A robust desktop application for power distribution management.\n" +
                "- Handles consumer records, meter reading logs, tariff tiers, and automatic bill calculation.\n" +
                "- Implements complete CRUD persistence layer with MongoDB and an intuitive Java Swing GUI.\n\n" +
                "**2. Employee Management Website** *(Node.js, Express.js, MongoDB, HTML5, CSS3)*\n" +
                "- Full-stack web application for organizational HR operations.\n" +
                "- Features RESTful endpoints for employee onboarding, profile tracking, departmental categorization, and role updates.\n" +
                "- Responsive frontend interfaced seamlessly with an Express/MongoDB backend.\n\n" +
                "**3. Cloud Data Pipeline Optimizer** *(GCP, Apache Airflow / Cloud Composer, BigQuery)*\n" +
                "- Production pipeline refactor at enterprise scale.\n" +
                "- Slashed daily batch runtimes from **90 minutes down to 90 seconds (60x acceleration)** by parallelizing DAG tasks and eliminating data bottlenecks.",
                List.of(
                    new ActionLink("💼 View GitHub Profile", GITHUB_URL, "external"),
                    new ActionLink("📄 View Resume for Details", RESUME_URL, "download")
                ),
                List.of("Tell me about his technical skills", "What certifications does he hold?", "How can I contact Aditya?")
            );
        }

        // 8. Certifications & Achievements
        if (matchesAny(q, "cert", "certification", "certifications", "certified", "azure", "aws", "cloud practitioner", "ai-900", "az-900", "credential", "exam")) {
            return new BotResponse(
                "### 🏆 Certifications & Professional Credentials\n\n" +
                "Aditya holds multiple industry-recognized cloud and software certifications:\n\n" +
                "1. ☕ **Capgemini Certified — Java Full Stack (React)** *(May 2025)*\n" +
                "   - Verified mastery in end-to-end enterprise Java backend, Spring Boot, React, and modern microservice architectures.\n\n" +
                "2. 🤖 **Microsoft Certified: Azure AI Fundamentals (AI-900)**\n" +
                "   - Fundamental concepts of Machine Learning, Artificial Intelligence, computer vision, and NLP on Microsoft Azure.\n\n" +
                "3. ☁️ **Microsoft Certified: Azure Fundamentals (AZ-900)**\n" +
                "   - Cloud computing principles, Azure security, network architecture, and cloud governance.\n\n" +
                "4. 🟧 **AWS Certified Cloud Practitioner**\n" +
                "   - Comprehensive understanding of Amazon Web Services cloud infrastructure, IAM, billing, and core services.\n\n" +
                "5. 🎖️ **Capgemini 'Champion' Award**\n" +
                "   - Honored for exceptional software engineering contributions and client satisfaction on the APTIV account.",
                List.of(
                    new ActionLink("📄 Download Resume with Credentials", RESUME_URL, "download"),
                    new ActionLink("💼 View LinkedIn Licenses", LINKEDIN_URL, "external")
                ),
                List.of("Tell me about his problem solving skills", "Where did he go to college?", "How to hire Aditya?")
            );
        }

        // 9. Education & College
        if (matchesAny(q, "education", "college", "university", "school", "degree", "btech", "b.tech", "study", "studied", "lpu", "lovely professional university", "cgpa", "marks", "gpa", "graduat")) {
            return new BotResponse(
                "### 🎓 Education & Academic Background\n\n" +
                "**Bachelor of Technology (B.Tech) in Computer Engineering**\n" +
                "- 🏛️ **Institution**: Lovely Professional University (LPU), Phagwara, Punjab\n" +
                "- 📅 **Graduation Period**: 2020 – 2024\n" +
                "- 📊 **Academic Score**: **CGPA 7.79 / 10**\n" +
                "- 💡 **Core Coursework**: Data Structures & Algorithms, Object-Oriented Analysis & Design, Database Management Systems (DBMS), Operating Systems, Computer Networks, Cloud Computing, Software Engineering.",
                List.of(
                    new ActionLink("📄 View Resume Document", RESUME_URL, "download"),
                    new ActionLink("💼 Connect on LinkedIn", LINKEDIN_URL, "external")
                ),
                List.of("What are his technical skills?", "Show me his certifications", "Tell me about his work experience")
            );
        }

        // 10. DSA & Problem Solving (LeetCode, GeeksforGeeks)
        if (matchesAny(q, "leetcode", "geeksforgeeks", "gfg", "dsa", "data structures", "algorithm", "algorithms", "problem solving", "competitive")) {
            return new BotResponse(
                "### 🧠 Problem Solving & Algorithmic Foundations\n\n" +
                "Aditya is passionate about algorithmic efficiency and clean data structure design:\n\n" +
                "- 🧩 **LeetCode**: **300+ Problems Solved** across Arrays, Trees, Dynamic Programming, Graphs, and Hash Tables.\n" +
                "- 💻 **GeeksforGeeks**: **200+ Problems Solved** focusing on core algorithm design, recursion, and search/sort optimization.\n" +
                "- ⚡ **Total**: **500+ algorithmic problems solved**, building a rock-solid foundation in writing time- and memory-efficient enterprise code.",
                List.of(
                    new ActionLink("💼 View LinkedIn", LINKEDIN_URL, "external"),
                    new ActionLink("📄 View Resume", RESUME_URL, "download")
                ),
                List.of("What is his tech stack?", "Tell me about his projects", "How can I contact Aditya?")
            );
        }

        // 11. "Who is Aditya?", "About", "Tell me about yourself", "Why hire Aditya"
        if (matchesAny(q, "about", "who is", "tell me about", "summary", "overview", "bio", "yourself", "intro", "introduction", "why hire", "why should i hire")) {
            return new BotResponse(
                "### 👋 Meet Aditya Raj\n\n" +
                "Aditya Raj is a results-driven **Senior Software Engineer** based in **Bengaluru, Karnataka**, specializing in **Java, Spring Boot, Google Cloud Platform (GCP), and Google AppSheet**.\n\n" +
                "**Key Highlights:**\n" +
                "- 💼 **2+ Years of Enterprise Experience** at **Capgemini** serving global automotive and tech clients (APTIV / Versigent).\n" +
                "- ⚡ **Proven Impact**: Engineered a **60x performance boost** on GCP Cloud Composer pipelines (90 mins ➔ 90 secs) and resolved **200+ production incidents** cutting repeat issues by 30%.\n" +
                "- 🛡️ **Modernization Leader**: Led legacy SharePoint migration to **Microsoft Graph API** ahead of authentication deprecation.\n" +
                "- 📜 **Certified Specialist**: AWS Cloud Practitioner, Azure AI-900, Azure AZ-900, and Capgemini Java Full Stack.\n" +
                "- 🧠 **500+ Algorithmic Challenges Solved** across LeetCode & GeeksforGeeks.\n\n" +
                "He combines deep backend architecture skills with low-code enterprise automation and cloud-native reliability.",
                List.of(
                    new ActionLink("📄 Download Full Resume", RESUME_URL, "download"),
                    new ActionLink("💼 Connect on LinkedIn", LINKEDIN_URL, "external"),
                    new ActionLink("📧 Email Aditya", EMAIL_URL, "email")
                ),
                List.of("What is his work experience?", "Tell me about his technical skills", "How to contact Aditya?")
            );
        }

        // Default Fallback with intelligent keywords detection
        return getFallbackResponse(rawQuery);
    }

    private static BotResponse getGreetingResponse() {
        return new BotResponse(
            "### 👋 Hello! I'm Aditya's AI Assistant\n\n" +
            "I know every detail about **Aditya Raj** — his work at **Capgemini (Client: APTIV)**, technical skills in **Java, Spring Boot, GCP & AppSheet**, major achievements like his **60x pipeline acceleration**, certifications, projects, and contact info!\n\n" +
            "What would you like to know about Aditya?",
            List.of(
                new ActionLink("📄 Download Resume", RESUME_URL, "download"),
                new ActionLink("💼 View LinkedIn", LINKEDIN_URL, "external"),
                new ActionLink("📧 Email Aditya", EMAIL_URL, "email")
            ),
            List.of(
                "Tell me about your Capgemini experience",
                "What are your top technical skills?",
                "How did you achieve a 60x pipeline speedup?",
                "What certifications do you have?",
                "How can I contact you?"
            )
        );
    }

    private static BotResponse getFallbackResponse(String query) {
        return new BotResponse(
            "### 💡 Here is what I know regarding \"" + escapeQuery(query) + "\":\n\n" +
            "Aditya Raj is a **Senior Software Engineer** at **Capgemini** in Bengaluru with 2+ years of enterprise experience building and maintaining high-scale Java Spring Boot and GCP systems.\n\n" +
            "You can ask me specifically about:\n" +
            "- 💼 **Work Experience**: Capgemini (APTIV / Versigent) & Celebal Technology\n" +
            "- ⚡ **Achievements**: 60x Apache Airflow pipeline optimization, 200+ incidents resolved, SharePoint to Graph API migration\n" +
            "- 🛠️ **Tech Stack**: Java, Spring Boot, Google AppSheet, GCP, MongoDB, React, SQL, Node.js\n" +
            "- 📜 **Certifications**: AWS Cloud Practitioner, Azure AI-900, Azure AZ-900, Capgemini Full Stack\n" +
            "- 🎓 **Education**: B.Tech Computer Engineering at Lovely Professional University (CGPA 7.79)\n" +
            "- 📬 **Contact & Socials**: Email, Phone, LinkedIn, and official Resume PDF.",
            List.of(
                new ActionLink("📄 Download Resume (PDF)", RESUME_URL, "download"),
                new ActionLink("💼 Connect on LinkedIn", LINKEDIN_URL, "external"),
                new ActionLink("📧 Email Aditya", EMAIL_URL, "email")
            ),
            List.of(
                "Tell me about his Capgemini experience",
                "What are his core technical skills?",
                "How did he achieve 60x pipeline speedup?",
                "How can I contact or hire Aditya?"
            )
        );
    }

    private static String escapeQuery(String s) {
        if (s == null) return "";
        return s.replace("<", "&lt;").replace(">", "&gt;");
    }

    private static boolean matchesAny(String text, String... keywords) {
        String lower = text.toLowerCase();
        for (String kw : keywords) {
            String kwLower = kw.toLowerCase();
            if (kwLower.length() <= 3) {
                // Use word boundary for short keywords like "hi", "cv", "app", etc.
                Pattern p = Pattern.compile("\\b" + Pattern.quote(kwLower) + "\\b", Pattern.CASE_INSENSITIVE);
                if (p.matcher(lower).find()) return true;
            } else {
                if (lower.contains(kwLower)) return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String[] testQueries = {
            "Hi there",
            "Tell me about yourself",
            "What did you do at Capgemini?",
            "How did you achieve 60x pipeline optimization?",
            "What are your technical skills?",
            "Tell me about your education and college",
            "What certifications do you have?",
            "How many leetcode problems have you solved?",
            "How can I contact you?",
            "Can I see your resume?"
        };

        for (String query : testQueries) {
            System.out.println("\n==========================================");
            System.out.println("USER: " + query);
            BotResponse resp = answerQuery(query);
            System.out.println("BOT:\n" + resp.reply());
            System.out.println("LINKS: " + resp.links());
            System.out.println("SUGGESTIONS: " + resp.suggestions());
        }
    }
}
