/**
 * Aditya Raj Portfolio - Client Application & AI Chatbot Controller
 */

document.addEventListener('DOMContentLoaded', () => {
  initHeroTyping();
  initSkillsFilter();
  initResumeModal();
  initAIChatbot();
});

/* ==========================================================================
   1. HERO TYPING EFFECT
   ========================================================================== */
function initHeroTyping() {
  const typedElem = document.getElementById('typedTitle');
  if (!typedElem) return;

  const titles = [
    'Senior Java & Cloud Engineer',
    'Spring Boot & Microservices Specialist',
    'Google Cloud Platform & AppSheet Expert',
    '60x Pipeline Optimization Engineer',
    '500+ LeetCode & GFG Problem Solver'
  ];

  let titleIndex = 0;
  let charIndex = 0;
  let isDeleting = false;
  let typingSpeed = 80;

  function typeStep() {
    const currentTitle = titles[titleIndex];

    if (isDeleting) {
      typedElem.textContent = currentTitle.substring(0, charIndex - 1);
      charIndex--;
      typingSpeed = 40;
    } else {
      typedElem.textContent = currentTitle.substring(0, charIndex + 1);
      charIndex++;
      typingSpeed = 90;
    }

    if (!isDeleting && charIndex === currentTitle.length) {
      isDeleting = true;
      typingSpeed = 2000; // Pause at end of text
    } else if (isDeleting && charIndex === 0) {
      isDeleting = false;
      titleIndex = (titleIndex + 1) % titles.length;
      typingSpeed = 500;
    }

    setTimeout(typeStep, typingSpeed);
  }

  typeStep();
}

/* ==========================================================================
   2. SKILLS FILTER
   ========================================================================== */
function initSkillsFilter() {
  const filterBtns = document.querySelectorAll('.filter-btn');
  const skillCards = document.querySelectorAll('.skill-card');

  filterBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      filterBtns.forEach(b => b.classList.remove('active'));
      btn.classList.add('active');

      const filter = btn.getAttribute('data-filter');

      skillCards.forEach(card => {
        const category = card.getAttribute('data-category');
        if (filter === 'all' || category === filter) {
          card.style.display = 'flex';
          setTimeout(() => { card.style.opacity = '1'; card.style.transform = 'scale(1)'; }, 10);
        } else {
          card.style.opacity = '0';
          card.style.transform = 'scale(0.95)';
          setTimeout(() => { card.style.display = 'none'; }, 200);
        }
      });
    });
  });
}

/* ==========================================================================
   3. RESUME PREVIEW MODAL
   ========================================================================== */
function initResumeModal() {
  const modal = document.getElementById('resumeModal');
  const previewBtn = document.getElementById('previewResumeBtn');
  const closeBtn = document.getElementById('closeResumeModal');

  if (!modal) return;

  function openModal() {
    modal.classList.add('open');
    document.body.style.overflow = 'hidden';
  }

  function closeModal() {
    modal.classList.remove('open');
    document.body.style.overflow = 'auto';
  }

  if (previewBtn) previewBtn.addEventListener('click', openModal);
  if (closeBtn) closeBtn.addEventListener('click', closeModal);

  modal.addEventListener('click', (e) => {
    if (e.target === modal) closeModal();
  });

  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && modal.classList.contains('open')) closeModal();
  });
}

/* ==========================================================================
   4. CONTACT FORM HANDLER
   ========================================================================== */
window.handleContactSubmit = function () {
  const name = document.getElementById('contactName').value;
  const email = document.getElementById('contactEmail').value;
  const msg = document.getElementById('contactMsg').value;
  const statusDiv = document.getElementById('contactStatus');

  statusDiv.innerHTML = `<span style="color: #60a5fa;">⏳ Preparing message...</span>`;

  // Create mailto fallback with prefilled content
  setTimeout(() => {
    statusDiv.innerHTML = `
      <div style="color: #34d399; margin-top: 10px;">
        ✓ Thank you, ${name}! Your message is queued. Opening your email client to dispatch directly to <strong>iadityaraj1412@gmail.com</strong>...
      </div>
    `;

    const subject = encodeURIComponent(`Portfolio Inquiry from ${name}`);
    const body = encodeURIComponent(`Hi Aditya,\n\nName: ${name}\nEmail: ${email}\n\nMessage:\n${msg}`);
    window.location.href = `mailto:iadityaraj1412@gmail.com?subject=${subject}&body=${body}`;
  }, 600);
};

/* ==========================================================================
   5. AI CHATBOT CONTROLLER
   ========================================================================== */
function initAIChatbot() {
  const triggerBtn = document.getElementById('aiTriggerBtn');
  const drawer = document.getElementById('aiChatDrawer');
  const closeBtn = document.getElementById('closeChatBtn');
  const clearBtn = document.getElementById('clearChatBtn');
  const ttsBtn = document.getElementById('ttsToggleBtn');
  const navChatBtn = document.getElementById('openChatNavBtn');
  const heroChatBtn = document.getElementById('heroChatBtn');
  const unreadBadge = document.getElementById('aiUnreadBadge');
  const messagesContainer = document.getElementById('chatMessages');
  const chatInput = document.getElementById('chatInput');
  const promptsBar = document.getElementById('chatPromptsBar');
  const typingRow = document.getElementById('chatTypingRow');

  let isTtsEnabled = false;

  function toggleDrawer(forceOpen = null) {
    const shouldOpen = forceOpen !== null ? forceOpen : !drawer.classList.contains('open');
    if (shouldOpen) {
      drawer.classList.add('open');
      if (unreadBadge) unreadBadge.style.display = 'none';
      setTimeout(() => chatInput && chatInput.focus(), 300);
    } else {
      drawer.classList.remove('open');
      if ('speechSynthesis' in window) window.speechSynthesis.cancel();
    }
  }

  if (triggerBtn) triggerBtn.addEventListener('click', () => toggleDrawer());
  if (closeBtn) closeBtn.addEventListener('click', () => toggleDrawer(false));
  if (navChatBtn) navChatBtn.addEventListener('click', () => toggleDrawer(true));
  if (heroChatBtn) heroChatBtn.addEventListener('click', () => toggleDrawer(true));

  // Voice toggle
  if (ttsBtn) {
    ttsBtn.addEventListener('click', () => {
      isTtsEnabled = !isTtsEnabled;
      ttsBtn.classList.toggle('active', isTtsEnabled);
      if (!isTtsEnabled && 'speechSynthesis' in window) {
        window.speechSynthesis.cancel();
      }
    });
  }

  // Clear chat
  if (clearBtn) {
    clearBtn.addEventListener('click', () => {
      messagesContainer.innerHTML = '';
      sendInitialGreeting();
    });
  }

  // Quick Prompt Chips
  if (promptsBar) {
    promptsBar.addEventListener('click', (e) => {
      if (e.target.classList.contains('chip-btn')) {
        const query = e.target.getAttribute('data-query');
        if (query) {
          executeChatQuery(query);
        }
      }
    });
  }

  // Submit via global function
  window.sendChatMessage = function () {
    const text = chatInput.value.trim();
    if (!text) return;
    chatInput.value = '';
    executeChatQuery(text);
  };

  function executeChatQuery(query) {
    appendUserMessage(query);
    showTyping(true);

    fetch('/api/chat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ message: query })
    })
    .then(res => {
      if (!res.ok) throw new Error('API unreachable');
      return res.json();
    })
    .then(data => {
      showTyping(false);
      appendBotMessage(data);

      if (isTtsEnabled && 'speechSynthesis' in window) {
        speakResponse(data.reply);
      }
    })
    .catch(err => {
      // Offline / Static deployment fallback: execute client-side intelligence engine
      showTyping(false);
      const clientData = getClientSideBotResponse(query);
      appendBotMessage(clientData);

      if (isTtsEnabled && 'speechSynthesis' in window) {
        speakResponse(clientData.reply);
      }
    });
  }

  function appendUserMessage(text) {
    const wrap = document.createElement('div');
    wrap.className = 'message-wrap user';
    wrap.innerHTML = `
      <div class="message-avatar user">You</div>
      <div class="message-bubble">${escapeHtml(text)}</div>
    `;
    messagesContainer.appendChild(wrap);
    scrollToBottom();
  }

  function appendBotMessage(data) {
    const wrap = document.createElement('div');
    wrap.className = 'message-wrap bot';

    let linksHtml = '';
    if (data.links && data.links.length > 0) {
      linksHtml = '<div class="chat-action-links">';
      data.links.forEach(l => {
        const target = l.type === 'external' || l.type === 'download' ? 'target="_blank"' : '';
        linksHtml += `<a href="${l.url}" ${target} class="action-link-btn">${l.label}</a>`;
      });
      linksHtml += '</div>';
    }

    let formattedReply = renderMarkdown(data.reply);

    wrap.innerHTML = `
      <div class="message-avatar bot">AI</div>
      <div class="message-bubble">
        ${formattedReply}
        ${linksHtml}
      </div>
    `;

    messagesContainer.appendChild(wrap);

    // Update suggestions chips if provided
    if (data.suggestions && data.suggestions.length > 0) {
      updateSuggestionChips(data.suggestions);
    }

    scrollToBottom();
  }

  function updateSuggestionChips(suggestions) {
    promptsBar.innerHTML = `<span class="prompts-label">Follow-up:</span>`;
    suggestions.forEach(s => {
      const chip = document.createElement('button');
      chip.className = 'chip-btn';
      chip.setAttribute('data-query', s);
      chip.textContent = s;
      promptsBar.appendChild(chip);
    });
  }

  function showTyping(show) {
    typingRow.style.display = show ? 'flex' : 'none';
    if (show) scrollToBottom();
  }

  function scrollToBottom() {
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
  }

  function speakResponse(markdownText) {
    const plainText = markdownText
      .replace(/###/g, '')
      .replace(/\*\*/g, '')
      .replace(/\[([^\]]+)\]\([^)]+\)/g, '$1')
      .replace(/[•▸-]/g, '')
      .trim();

    window.speechSynthesis.cancel();
    const utterance = new SpeechSynthesisUtterance(plainText);
    utterance.rate = 1.05;
    window.speechSynthesis.speak(utterance);
  }

  function sendInitialGreeting() {
    appendBotMessage({
      reply: "### 👋 Hello! I'm Aditya's AI Assistant\n\nI have complete knowledge of **Aditya Raj's resume, enterprise experience at Capgemini, 60x pipeline optimization, certifications, and projects**.\n\nAsk me anything or pick a quick suggestion below!",
      links: [
        { label: "📄 Download Resume", url: "/api/resume", type: "download" },
        { label: "💼 LinkedIn", url: "https://www.linkedin.com/in/iadityaraj1412", type: "external" },
        { label: "📧 Email Aditya", url: "mailto:iadityaraj1412@gmail.com", type: "email" }
      ],
      suggestions: [
        "Tell me about your Capgemini experience",
        "How did you achieve a 60x pipeline speedup?",
        "What are your top technical skills?",
        "What certifications do you hold?",
        "How can I contact or hire Aditya?"
      ]
    });
  }

  // Send greeting on start
  sendInitialGreeting();

  // Subtle auto-open prompt notification after 3 seconds
  setTimeout(() => {
    if (!drawer.classList.contains('open') && unreadBadge) {
      unreadBadge.style.display = 'flex';
    }
  }, 3000);
}

/* ==========================================================================
   MARKDOWN & HTML UTILITIES
   ========================================================================== */
function renderMarkdown(md) {
  if (!md) return '';

  let html = md;

  // Escape HTML tags first
  html = html.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');

  // Headers (### Heading)
  html = html.replace(/^### (.*$)/gim, '<h3>$1</h3>');
  html = html.replace(/^## (.*$)/gim, '<h2>$1</h2>');

  // Bold (**text**)
  html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

  // Italic (*text*)
  html = html.replace(/\*(.*?)\*/g, '<em>$1</em>');

  // Links [text](url)
  html = html.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank" rel="noopener">$1</a>');

  // Bullet items
  html = html.replace(/^\s*-\s+(.*$)/gim, '<li>$1</li>');

  // Wrap <li> into <ul>
  html = html.replace(/(<li>.*<\/li>)/gms, '<ul>$1</ul>');

  // Paragraph breaks
  html = html.split('\n\n').map(p => {
    if (p.startsWith('<h3>') || p.startsWith('<h2>') || p.startsWith('<ul>')) return p;
    return `<p>${p.replace(/\n/g, '<br/>')}</p>`;
  }).join('');

  return html;
}

function escapeHtml(str) {
  return str
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}

/* ==========================================================================
   CLIENT-SIDE KNOWLEDGE BASE & INTENT MATCHER (OFFLINE / STATIC HOSTING)
   ========================================================================== */
function getClientSideBotResponse(rawQuery) {
  const q = (rawQuery || '').trim().toLowerCase();
  const LINKEDIN_URL = "https://www.linkedin.com/in/iadityaraj1412";
  const GITHUB_URL = "https://github.com/iadityaraj1412";
  const EMAIL_URL = "mailto:iadityaraj1412@gmail.com";
  const PHONE_URL = "tel:+919570119979";
  const RESUME_URL = "assets/Aditya_Raj_Resume.pdf";

  function matchesAny(text, ...keywords) {
    for (const kw of keywords) {
      const kwLower = kw.toLowerCase();
      if (kwLower.length <= 3) {
        const regex = new RegExp(`\\b${kwLower}\\b`, 'i');
        if (regex.test(text)) return true;
      } else {
        if (text.includes(kwLower)) return true;
      }
    }
    return false;
  }

  // 1. Greetings
  if (matchesAny(q, "hi", "hello", "hey", "hola", "namaste", "greetings", "good morning", "good evening", "what's up")) {
    return {
      reply: "### 👋 Hello! I'm Aditya's AI Assistant\n\nI know every detail about **Aditya Raj** — his work at **Capgemini (Client: APTIV)**, technical skills in **Java, Spring Boot, GCP & AppSheet**, major achievements like his **60x pipeline acceleration**, certifications, projects, and contact info!\n\nWhat would you like to know about Aditya?",
      links: [
        { label: "📄 Download Resume", url: RESUME_URL, type: "download" },
        { label: "💼 View LinkedIn", url: LINKEDIN_URL, type: "external" },
        { label: "📧 Email Aditya", url: EMAIL_URL, type: "email" }
      ],
      suggestions: [
        "Tell me about your Capgemini experience",
        "What are your top technical skills?",
        "How did you achieve a 60x pipeline speedup?",
        "What certifications do you have?",
        "How can I contact you?"
      ]
    };
  }

  // 2. Resume / CV
  if (matchesAny(q, "resume", "cv", "curriculum vitae", "download resume", "view resume", "pdf", "profile doc")) {
    return {
      reply: "### 📄 Aditya Raj's Resume\n\nYou can view or download Aditya's official resume directly right here. It highlights his **2+ years of experience** as a Senior Software Engineer at Capgemini, expertise in Java, Spring Boot, Google Cloud Platform, and 60x pipeline optimization achievements.",
      links: [
        { label: "📥 Download Resume (PDF)", url: RESUME_URL, type: "download" },
        { label: "🔗 View LinkedIn Profile", url: LINKEDIN_URL, type: "external" }
      ],
      suggestions: ["Tell me about his work at Capgemini", "What are his key skills?", "How did he achieve 60x optimization?"]
    };
  }

  // 3. Contact & Socials
  if (matchesAny(q, "contact", "email", "phone", "number", "mobile", "linkedin", "social", "github", "reach", "hire", "call", "location", "address", "city", "bangalore", "bengaluru")) {
    return {
      reply: "### 📬 Get in Touch with Aditya Raj\n\nAditya is currently based in **Bengaluru, Karnataka, India** and is open to opportunities as a **Senior Software Engineer / Java Full-Stack Specialist**.\n\n- 📧 **Email**: [iadityaraj1412@gmail.com](mailto:iadityaraj1412@gmail.com)\n- 📱 **Phone**: [+91 9570119979](tel:+919570119979)\n- 💼 **LinkedIn**: [linkedin.com/in/iadityaraj1412](https://www.linkedin.com/in/iadityaraj1412)\n- 🐙 **GitHub**: [github.com/iadityaraj1412](https://github.com/iadityaraj1412)\n- 📍 **Location**: Bengaluru, Karnataka, India",
      links: [
        { label: "📧 Send an Email", url: EMAIL_URL, type: "email" },
        { label: "💼 Connect on LinkedIn", url: LINKEDIN_URL, type: "external" },
        { label: "📞 Call Aditya (+91 9570119979)", url: PHONE_URL, type: "tel" },
        { label: "📄 View Resume", url: RESUME_URL, type: "download" }
      ],
      suggestions: ["Tell me about his Capgemini experience", "What are his technical skills?", "Show me his projects"]
    };
  }

  // 4. 60x Airflow Optimization & Achievements
  if (matchesAny(q, "60x", "airflow", "cloud composer", "optimization", "optimise", "pipeline", "speedup", "dag", "performance", "achievement", "award", "champion")) {
    return {
      reply: "### ⚡ 60x Pipeline Acceleration & Major Achievements\n\nAditya engineered standout performance optimizations and critical migrations at **Capgemini** (Client: APTIV / Versigent):\n\n1. 🚀 **60x GCP Data Pipeline Optimization**:\n   - Refactored complex Apache Airflow (Google Cloud Composer) DAG logic.\n   - Implemented task parallelisation and optimised query execution, slashing end-to-end processing time **from 90 minutes down to just 90 seconds** (a 60x improvement!).\n\n2. 🛡️ **Microsoft Graph API Migration**:\n   - Seamlessly migrated legacy SharePoint file-transfer scheduler jobs to Microsoft Graph API ahead of Microsoft's April 2026 legacy authentication deprecation, eliminating downtime and hardening security.\n\n3. 🏆 **Capgemini 'Champion' Award**:\n   - Recognized with the prestigious Capgemini 'Champion' title for consistently high-quality technical delivery and exceeding client satisfaction benchmarks.\n\n4. 🛠️ **Production Reliability & Incident Management**:\n   - Resolved **200+ production incidents** with in-depth root cause analysis (RCA), cutting recurring failures by **30%** while maintaining **99%+ uptime** across 7+ enterprise Java Spring Boot and Google AppSheet apps.",
      links: [
        { label: "📄 Verify in Resume", url: RESUME_URL, type: "download" },
        { label: "💼 View Experience on LinkedIn", url: LINKEDIN_URL, type: "external" }
      ],
      suggestions: ["What is his role at Capgemini?", "What technologies did he use?", "Tell me about his certifications"]
    };
  }

  // 5. Work Experience
  if (matchesAny(q, "experience", "work", "job", "career", "company", "companies", "capgemini", "aptiv", "celebal", "versigent", "senior software engineer", "history", "role", "current role")) {
    return {
      reply: "### 💼 Professional Experience Overview (2+ Years)\n\nAditya Raj is a **Senior Software Engineer** with extensive expertise across Java, Spring Boot, Google Cloud Platform, and enterprise workflow automation:\n\n**1. Capgemini — Senior Software Engineer** *(Client: APTIV / Versigent)*\n*Bengaluru, Karnataka | Jul 2026 – Present*\n- Oversees 24/7 production support and feature enhancements for **7+ enterprise Java Spring Boot & Google AppSheet applications** with **99%+ uptime**.\n- Migrated critical SharePoint file-transfer scheduler jobs to **Microsoft Graph API**.\n- Slashed GCP data pipeline processing runtime from **90 minutes to 90 seconds (60x speedup)** via Apache Airflow DAG refactoring.\n- Resolved **200+ production tickets** with proactive RCA, cutting recurring defects by 30%.\n\n**2. Capgemini — Software Engineer** *(Client: APTIV)*\n*Bengaluru, Karnataka | Oct 2024 – Jun 2026*\n- Built enterprise backend microservices and REST APIs using **Java and Spring Boot**.\n- Developed enterprise workflow automation apps with **Google AppSheet**.\n- Automated third-party API data ingestion pipelines.\n- Awarded the **Capgemini 'Champion' recognition** for technical excellence.\n\n**3. Celebal Technology — Node.JS Developer Intern**\n*Remote | Jun 2023 – Aug 2023*\n- Engineered RESTful backend services in Node.js & Express.js.\n- Designed MongoDB schema architectures for optimal read/write throughput.",
      links: [
        { label: "📄 View Full Resume", url: RESUME_URL, type: "download" },
        { label: "💼 Visit LinkedIn", url: LINKEDIN_URL, type: "external" }
      ],
      suggestions: ["Tell me about the 60x Airflow speedup", "What technical skills does he have?", "What projects has he built?"]
    };
  }

  // 6. Skills
  if (matchesAny(q, "skill", "skills", "tech", "stack", "technology", "technologies", "java", "spring", "spring boot", "backend", "frontend", "database", "gcp", "cloud", "appsheet", "react", "node", "express", "mongodb", "sql", "microservice", "tools")) {
    return {
      reply: "### 💻 Technical Skills & Core Competencies\n\nAditya possesses deep full-stack and cloud engineering proficiency:\n\n- ☕ **Programming Languages**: Java (Java 8 to 26), SQL, JavaScript, HTML5, CSS3\n- 🍃 **Backend Frameworks**: Spring Framework, Spring Boot, Node.js, Express.js, REST APIs, MVC, JDBC, Microservices\n- ☁️ **Cloud & Low-Code**: Google Cloud Platform (GCP), Google AppSheet, GenAI, Cloud Composer, BigQuery, Apache Airflow, Microsoft Azure, AWS\n- 🗄️ **Databases**: MongoDB, Microsoft SQL Server, Database Schema Design, Indexing & Query Tuning\n- ⚛️ **Frontend**: React.js, Java Swing, AWT, Modern Responsive Web Design\n- 🛠️ **Tools & Platforms**: Git, Postman, Visual Studio Code, Eclipse, Android Studio, Flutter, ServiceNow\n- 🧠 **Methodologies & Concepts**: Object-Oriented Programming (OOP), Data Structures & Algorithms (DSA), System Design, Agile/Scrum, CI/CD\n- 🎯 **Core Competencies**: Production Support, Incident Management (200+ RCA), API Integration, Workflow Automation, Performance Tuning",
      links: [
        { label: "📂 Explore Projects", url: "#projects", type: "internal" },
        { label: "📜 View Certifications", url: "#certifications", type: "internal" },
        { label: "📥 Download Resume", url: RESUME_URL, type: "download" }
      ],
      suggestions: ["What projects did he build with Java?", "Tell me about his certifications", "What is his experience at Capgemini?"]
    };
  }

  // 7. Projects
  if (matchesAny(q, "project", "projects", "portfolio", "electricity", "billing", "employee management", "website", "built", "app", "application")) {
    return {
      reply: "### 🚀 Featured Engineering Projects\n\n**1. Electricity Billing System** *(Java, Swing, MongoDB)*\n- Desktop management system for customer utility records, tariffs, meter readings, and automated billing.\n- Full CRUD with MongoDB persistence.\n\n**2. Employee Management Website** *(Node.js, Express.js, MongoDB, HTML5, CSS3)*\n- Full-stack CRUD application for employee records, department classification, and RESTful APIs.\n\n**3. Cloud Data Pipeline Optimizer** *(GCP, Apache Airflow / Cloud Composer, BigQuery)*\n- Slashed runtime from **90 minutes down to 90 seconds (60x speedup)** by parallelizing DAG tasks.",
      links: [
        { label: "💼 View GitHub Profile", url: GITHUB_URL, type: "external" },
        { label: "📄 View Resume for Details", url: RESUME_URL, type: "download" }
      ],
      suggestions: ["Tell me about his technical skills", "What certifications does he hold?", "How can I contact Aditya?"]
    };
  }

  // 8. Certifications
  if (matchesAny(q, "cert", "certification", "certifications", "certified", "azure", "aws", "cloud practitioner", "ai-900", "az-900", "credential", "exam")) {
    return {
      reply: "### 🏆 Certifications & Professional Credentials\n\nAditya holds multiple industry-recognized cloud and software certifications:\n\n1. ☕ **Capgemini Certified — Java Full Stack (React)** *(May 2025)*\n2. 🤖 **Microsoft Certified: Azure AI Fundamentals (AI-900)**\n3. ☁️ **Microsoft Certified: Azure Fundamentals (AZ-900)**\n4. 🟧 **AWS Certified Cloud Practitioner**\n5. 🎖️ **Capgemini 'Champion' Award**",
      links: [
        { label: "📄 Download Resume with Credentials", url: RESUME_URL, type: "download" },
        { label: "💼 View LinkedIn Licenses", url: LINKEDIN_URL, type: "external" }
      ],
      suggestions: ["Tell me about his problem solving skills", "Where did he go to college?", "How to hire Aditya?"]
    };
  }

  // 9. Education
  if (matchesAny(q, "education", "college", "university", "school", "degree", "btech", "b.tech", "study", "studied", "lpu", "lovely professional university", "cgpa", "marks", "gpa", "graduat")) {
    return {
      reply: "### 🎓 Education & Academic Background\n\n**Bachelor of Technology (B.Tech) in Computer Engineering**\n- 🏛️ **Institution**: Lovely Professional University (LPU), Phagwara, Punjab\n- 📅 **Graduation Period**: 2020 – 2024\n- 📊 **Academic Score**: **CGPA 7.79 / 10**\n- 💡 **Core Coursework**: Data Structures & Algorithms, OOP, DBMS, Operating Systems, Computer Networks, Cloud Computing.",
      links: [
        { label: "📄 View Resume Document", url: RESUME_URL, type: "download" },
        { label: "💼 Connect on LinkedIn", url: LINKEDIN_URL, type: "external" }
      ],
      suggestions: ["What are his technical skills?", "Show me his certifications", "Tell me about his work experience"]
    };
  }

  // 10. DSA & LeetCode
  if (matchesAny(q, "leetcode", "geeksforgeeks", "gfg", "dsa", "data structures", "algorithm", "algorithms", "problem solving", "competitive")) {
    return {
      reply: "### 🧠 Problem Solving & Algorithmic Foundations\n\n- 🧩 **LeetCode**: **300+ Problems Solved**\n- 💻 **GeeksforGeeks**: **200+ Problems Solved**\n- ⚡ **Total**: **500+ algorithmic challenges solved** in Data Structures, Graphs, Trees, and Dynamic Programming.",
      links: [
        { label: "💼 View LinkedIn", url: LINKEDIN_URL, type: "external" },
        { label: "📄 View Resume", url: RESUME_URL, type: "download" }
      ],
      suggestions: ["What is his tech stack?", "Tell me about his projects", "How can I contact Aditya?"]
    };
  }

  // Fallback
  return {
    reply: `### 💡 Information regarding "${escapeHtml(rawQuery)}":\n\nAditya Raj is a **Senior Software Engineer** at **Capgemini** in Bengaluru with 2+ years of enterprise experience in Java, Spring Boot, GCP, and AppSheet.\n\nYou can ask about his **Work Experience at Capgemini**, **60x Pipeline Optimization**, **Technical Skills**, **Certifications**, **Education at LPU**, or request his **Contact Info & Resume**!`,
    links: [
      { label: "📄 Download Resume", url: RESUME_URL, type: "download" },
      { label: "💼 Connect on LinkedIn", url: LINKEDIN_URL, type: "external" },
      { label: "📧 Email Aditya", url: EMAIL_URL, type: "email" }
    ],
    suggestions: [
      "Tell me about his Capgemini experience",
      "What are his core technical skills?",
      "How did he achieve 60x pipeline speedup?",
      "How can I contact or hire Aditya?"
    ]
  };
}


