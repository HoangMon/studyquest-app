/**
 * StudyQuest - Mobile Web & PWA Core Engine
 * Implements 5-Minute Procrastination Breakthrough, Pomodoro, EXP Gamification, Guilt-Free Rewards & Accountability
 */

// ============================================================================
// 1. STATE MANAGEMENT & STORAGE
// ============================================================================
const DEFAULT_STATE = {
  user: {
    name: "Minh",
    level: 2,
    levelName: "Thợ Săn Deadline",
    currentExp: 150,
    maxExp: 300,
    availableExp: 200, // Quỹ giải trí khả dụng (đổi ra phút: 200 EXP = 40 phút)
    streakDays: 4
  },
  currentFocusTaskId: 2,
  tasks: [
    {
      id: 1,
      title: "Tìm 3 tài liệu tiểu luận",
      subject: "Pháp luật ĐC",
      duration: 25,
      exp: 50,
      status: "done", // "todo" | "doing" | "done"
      inTop3: true
    },
    {
      id: 2,
      title: "Viết dàn ý đề cương chi tiết",
      subject: "Tư duy luận ĐC",
      duration: 25,
      exp: 50,
      status: "doing",
      inTop3: true
    },
    {
      id: 3,
      title: "Đọc và tóm tắt chương 1",
      subject: "Kinh tế chính trị",
      duration: 25,
      exp: 50,
      status: "todo",
      inTop3: true
    },
    {
      id: 4,
      title: "Viết 300 từ phần mở đầu",
      subject: "Pháp luật ĐC",
      duration: 25,
      exp: 50,
      status: "todo",
      inTop3: false
    },
    {
      id: 5,
      title: "Rà soát lỗi chính tả & định dạng APA",
      subject: "Tư duy luận ĐC",
      duration: 25,
      exp: 50,
      status: "todo",
      inTop3: false
    }
  ],
  buddy: {
    name: "@Minh",
    avatar: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&auto=format&fit=crop&q=80",
    completedTasks: 2,
    totalTasks: 3,
    currentFocus: "Viết dàn ý đề cương chi tiết (25m)",
    lastPing: "Mình vừa giải quyết xong bài tập lớn Pháp luật ĐC rồi nhé, cố lên!"
  },
  soundEnabled: true
};

let appState = loadState();

function loadState() {
  try {
    const saved = localStorage.getItem('studyquest_state');
    if (saved) return JSON.parse(saved);
  } catch (e) {
    console.warn("Storage load error:", e);
  }
  return JSON.parse(JSON.stringify(DEFAULT_STATE));
}

function saveState() {
  try {
    localStorage.setItem('studyquest_state', JSON.stringify(appState));
  } catch (e) {
    console.warn("Storage save error:", e);
  }
}

// Level Names Hierarchy
const LEVEL_TITLES = [
  "Tân Binh Khởi Động", // Lv 1
  "Thợ Săn Deadline",    // Lv 2
  "Chiến Thần Tập Trung",// Lv 3
  "Bậc Thầy Năng Suất", // Lv 4
  "Huyền Thoại Đồ Án"    // Lv 5
];

// ============================================================================
// 2. SYNTHETIC SOUND & AUDIO ENGINE (Web Audio API)
// ============================================================================
let audioCtx = null;
let ambientOsc = null;
let ambientGain = null;

function getAudioContext() {
  if (!audioCtx) {
    const AudioContextClass = window.AudioContext || window.webkitAudioContext;
    if (AudioContextClass) {
      audioCtx = new AudioContextClass();
    }
  }
  if (audioCtx && audioCtx.state === 'suspended') {
    audioCtx.resume();
  }
  return audioCtx;
}

const SoundEngine = {
  click() {
    if (!appState.soundEnabled) return;
    const ctx = getAudioContext();
    if (!ctx) return;
    const osc = ctx.createOscillator();
    const gain = ctx.createGain();
    osc.type = 'sine';
    osc.frequency.setValueAtTime(600, ctx.currentTime);
    osc.frequency.exponentialRampToValueAtTime(200, ctx.currentTime + 0.05);
    gain.gain.setValueAtTime(0.15, ctx.currentTime);
    gain.gain.exponentialRampToValueAtTime(0.01, ctx.currentTime + 0.05);
    osc.connect(gain);
    gain.connect(ctx.destination);
    osc.start();
    osc.stop(ctx.currentTime + 0.05);
  },

  success() {
    if (!appState.soundEnabled) return;
    const ctx = getAudioContext();
    if (!ctx) return;
    const notes = [523.25, 659.25, 783.99, 1046.50]; // C5, E5, G5, C6
    notes.forEach((freq, idx) => {
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();
      osc.type = 'triangle';
      osc.frequency.setValueAtTime(freq, ctx.currentTime + idx * 0.08);
      gain.gain.setValueAtTime(0.2, ctx.currentTime + idx * 0.08);
      gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + idx * 0.08 + 0.35);
      osc.connect(gain);
      gain.connect(ctx.destination);
      osc.start(ctx.currentTime + idx * 0.08);
      osc.stop(ctx.currentTime + idx * 0.08 + 0.35);
    });
  },

  levelUp() {
    if (!appState.soundEnabled) return;
    const ctx = getAudioContext();
    if (!ctx) return;
    const fanfare = [440, 554.37, 659.25, 880];
    fanfare.forEach((f, i) => {
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();
      osc.type = 'sine';
      osc.frequency.setValueAtTime(f, ctx.currentTime + i * 0.12);
      gain.gain.setValueAtTime(0.3, ctx.currentTime + i * 0.12);
      gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + i * 0.12 + 0.45);
      osc.connect(gain);
      gain.connect(ctx.destination);
      osc.start(ctx.currentTime + i * 0.12);
      osc.stop(ctx.currentTime + i * 0.12 + 0.45);
    });
  },

  bell() {
    if (!appState.soundEnabled) return;
    const ctx = getAudioContext();
    if (!ctx) return;
    const osc = ctx.createOscillator();
    const gain = ctx.createGain();
    osc.type = 'sine';
    osc.frequency.setValueAtTime(880, ctx.currentTime);
    osc.frequency.exponentialRampToValueAtTime(440, ctx.currentTime + 1.2);
    gain.gain.setValueAtTime(0.3, ctx.currentTime);
    gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + 1.2);
    osc.connect(gain);
    gain.connect(ctx.destination);
    osc.start();
    osc.stop(ctx.currentTime + 1.2);
  },

  setAmbient(type) {
    const ctx = getAudioContext();
    if (!ctx) return;
    if (ambientOsc) {
      try {
        ambientOsc.stop();
        ambientOsc.disconnect();
      } catch (e) {}
      ambientOsc = null;
    }
    if (type === 'none' || !appState.soundEnabled) return;

    if (type === 'rain' || type === 'whitenoise') {
      const bufferSize = ctx.sampleRate * 2;
      const noiseBuffer = ctx.createBuffer(1, bufferSize, ctx.sampleRate);
      const output = noiseBuffer.getChannelData(0);
      let lastOut = 0.0;
      for (let i = 0; i < bufferSize; i++) {
        const white = Math.random() * 2 - 1;
        output[i] = (lastOut + (0.02 * white)) / 1.02; // Pink-ish rain noise
        lastOut = output[i];
      }
      const whiteNoise = ctx.createBufferSource();
      whiteNoise.buffer = noiseBuffer;
      whiteNoise.loop = true;
      ambientGain = ctx.createGain();
      ambientGain.gain.setValueAtTime(0.04, ctx.currentTime);
      whiteNoise.connect(ambientGain);
      ambientGain.connect(ctx.destination);
      whiteNoise.start();
      ambientOsc = whiteNoise;
    } else if (type === 'cafe') {
      const osc = ctx.createOscillator();
      ambientGain = ctx.createGain();
      osc.type = 'sine';
      osc.frequency.setValueAtTime(140, ctx.currentTime);
      ambientGain.gain.setValueAtTime(0.02, ctx.currentTime);
      osc.connect(ambientGain);
      ambientGain.connect(ctx.destination);
      osc.start();
      ambientOsc = osc;
    }
  }
};

// ============================================================================
// 3. CONFETTI CELEBRATION ENGINE
// ============================================================================
const ConfettiEngine = {
  canvas: null,
  ctx: null,
  particles: [],
  animId: null,

  init() {
    this.canvas = document.getElementById('confettiCanvas');
    if (!this.canvas) return;
    this.ctx = this.canvas.getContext('2d');
    this.resize();
    window.addEventListener('resize', () => this.resize());
  },

  resize() {
    if (!this.canvas) return;
    const parent = this.canvas.parentElement;
    this.canvas.width = parent.clientWidth;
    this.canvas.height = parent.clientHeight;
  },

  fire() {
    this.init();
    if (!this.ctx) return;
    const colors = ['#0284C7', '#38BDF8', '#F59E0B', '#10B981', '#EC4899', '#8B5CF6'];
    for (let i = 0; i < 70; i++) {
      this.particles.push({
        x: this.canvas.width / 2,
        y: this.canvas.height / 3,
        vx: (Math.random() - 0.5) * 8,
        vy: (Math.random() - 0.8) * 10,
        size: Math.random() * 8 + 4,
        color: colors[Math.floor(Math.random() * colors.length)],
        rotation: Math.random() * 360,
        vRot: (Math.random() - 0.5) * 10,
        opacity: 1
      });
    }
    if (!this.animId) {
      this.loop();
    }
  },

  loop() {
    if (!this.ctx) return;
    this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
    for (let i = this.particles.length - 1; i >= 0; i--) {
      const p = this.particles[i];
      p.x += p.vx;
      p.y += p.vy;
      p.vy += 0.25; // gravity
      p.rotation += p.vRot;
      p.opacity -= 0.012;

      if (p.opacity <= 0) {
        this.particles.splice(i, 1);
        continue;
      }

      this.ctx.save();
      this.ctx.globalAlpha = p.opacity;
      this.ctx.translate(p.x, p.y);
      this.ctx.rotate((p.rotation * Math.PI) / 180);
      this.ctx.fillStyle = p.color;
      this.ctx.fillRect(-p.size / 2, -p.size / 2, p.size, p.size);
      this.ctx.restore();
    }

    if (this.particles.length > 0) {
      this.animId = requestAnimationFrame(() => this.loop());
    } else {
      this.animId = null;
    }
  }
};

// ============================================================================
// 4. TOAST NOTIFICATION SYSTEM
// ============================================================================
function showToast(text, icon = '✨') {
  const container = document.getElementById('toastContainer');
  if (!container) return;
  const toast = document.createElement('div');
  toast.className = 'toast-msg';
  toast.innerHTML = `<span class="toast-icon">${icon}</span><span>${text}</span>`;
  container.appendChild(toast);
  setTimeout(() => {
    toast.remove();
  }, 3000);
}

// ============================================================================
// 5. TIMER CONTROLLER (5-Minute Rule & Pomodoro)
// ============================================================================
const FocusTimer = {
  mode: '5min', // '5min' | '25min' | 'break'
  totalSeconds: 300,
  remainingSeconds: 298, // default starting near 04:58 matching screenshot
  interval: null,
  isRunning: false,

  setMode(newMode) {
    this.mode = newMode;
    if (newMode === '5min') {
      this.totalSeconds = 300;
      this.remainingSeconds = 300;
      document.getElementById('focusModeBadge').innerText = "QUY TẮC 5 PHÚT";
      document.getElementById('timerModeCaption').innerText = "QUY TẮC 5 PHÚT KHỞI ĐỘNG";
      document.getElementById('encourageText').innerText =
        "Bạn chỉ cần làm trong 5 phút. Hết giờ, bạn hoàn toàn có quyền dừng lại mà không có tội gì!";
    } else if (newMode === '25min') {
      this.totalSeconds = 1500;
      this.remainingSeconds = 1500;
      document.getElementById('focusModeBadge').innerText = "POMODORO 25 PHÚT";
      document.getElementById('timerModeCaption').innerText = "TẬP TRUNG SÂU (+50 EXP)";
      document.getElementById('encourageText').innerText =
        "Tuyệt vời! Bạn đang trong trạng thái dòng chảy (flow state). Hãy tập trung cho mục tiêu này!";
    } else if (newMode === 'break') {
      this.totalSeconds = 300;
      this.remainingSeconds = 300;
      document.getElementById('focusModeBadge').innerText = "NGHỈ GIẢI LAO 5M";
      document.getElementById('timerModeCaption').innerText = "THƯ GIÃN KHÔNG TỘI LỖI";
      document.getElementById('encourageText').innerText =
        "Hãy vươn vai, uống nước và rời mắt khỏi màn hình một chút nhé!";
    }

    // Update active pill
    document.querySelectorAll('.mode-pill').forEach(pill => {
      pill.classList.toggle('active', pill.dataset.mode === newMode);
    });

    this.render();
  },

  start() {
    if (this.isRunning) return;
    this.isRunning = true;
    SoundEngine.click();
    document.getElementById('btnTimerLabel').innerText = "TẠM DỪNG";
    document.getElementById('btnTimerIcon').innerText = "⏸";

    this.interval = setInterval(() => {
      if (this.remainingSeconds > 0) {
        this.remainingSeconds--;
        this.render();
      } else {
        this.complete();
      }
    }, 1000);
  },

  pause() {
    this.isRunning = false;
    SoundEngine.click();
    clearInterval(this.interval);
    this.interval = null;
    document.getElementById('btnTimerLabel').innerText = "TIẾP TỤC";
    document.getElementById('btnTimerIcon').innerText = "▶";
  },

  toggle() {
    if (this.isRunning) {
      this.pause();
    } else {
      this.start();
    }
  },

  render() {
    const mins = Math.floor(this.remainingSeconds / 60).toString().padStart(2, '0');
    const secs = (this.remainingSeconds % 60).toString().padStart(2, '0');
    const displayEl = document.getElementById('timerDisplay');
    if (displayEl) displayEl.innerText = `${mins}:${secs}`;

    // SVG Circular Progress
    const circle = document.getElementById('timerProgressCircle');
    if (circle) {
      const radius = 115;
      const circumference = 2 * Math.PI * radius; // ~722.56
      const fraction = this.remainingSeconds / this.totalSeconds;
      const offset = circumference * (1 - fraction);
      circle.style.strokeDasharray = `${circumference}`;
      circle.style.strokeDashoffset = `${offset}`;
    }
  },

  complete() {
    this.pause();
    SoundEngine.bell();

    if (this.mode === '5min') {
      showToast("🎉 Hoàn thành 5 phút khởi động! Bạn đã chiến thắng sức ì!", "🏆");
      ConfettiEngine.fire();
      // Prompt user to choose
    } else if (this.mode === '25min') {
      awardExp(50, "Hoàn thành phiên tập trung 25 phút!");
      showToast("🔥 Xuất sắc! Nhận ngay +50 EXP vào quỹ giải trí!", "⚡");
      ConfettiEngine.fire();
      
      // Auto complete the task if selected
      if (appState.currentFocusTaskId) {
        const task = appState.tasks.find(t => t.id === appState.currentFocusTaskId);
        if (task && task.status !== 'done') {
          task.status = 'done';
          saveState();
          renderUI();
        }
      }
    } else {
      showToast("Đã hết giờ giải lao! Sẵn sàng cho phiên tiếp theo chưa?", "⏰");
    }
  }
};

// ============================================================================
// 6. EXP, REWARDS & GAMIFICATION ENGINE
// ============================================================================
function awardExp(amount, reason = "") {
  appState.user.currentExp += amount;
  appState.user.availableExp += amount;

  // Level Up Check
  if (appState.user.currentExp >= appState.user.maxExp) {
    appState.user.level += 1;
    appState.user.currentExp = appState.user.currentExp - appState.user.maxExp;
    const titleIdx = Math.min(appState.user.level - 1, LEVEL_TITLES.length - 1);
    appState.user.levelName = LEVEL_TITLES[titleIdx];
    
    SoundEngine.levelUp();
    showToast(`🆙 THĂNG CẤP: Level ${appState.user.level} - ${appState.user.levelName}!`, "🎉");
    ConfettiEngine.fire();
  } else {
    SoundEngine.success();
  }

  saveState();
  renderUI();
}

function redeemReward(id, title, minutes, cost) {
  SoundEngine.click();
  if (appState.user.availableExp < cost) {
    showToast(`Không đủ EXP! Bạn cần ${cost} EXP (hiện có ${appState.user.availableExp} EXP).`, "⚠️");
    return;
  }

  // Deduct EXP
  appState.user.availableExp -= cost;
  saveState();
  renderUI();

  // Show Guilt-Free Pass Modal
  const modal = document.getElementById('modalRewardPass');
  const iconEl = document.getElementById('passRewardIcon');
  const titleEl = document.getElementById('passRewardTitle');
  const costEl = document.getElementById('passCostText');
  const clockEl = document.getElementById('passTimerClock');

  const icons = {
    game: '🎮',
    anime: '🎬',
    social: '☕',
    music: '🎵'
  };

  if (iconEl) iconEl.innerText = icons[id] || '🎁';
  if (titleEl) titleEl.innerText = `${title} (${minutes} phút)`;
  if (costEl) costEl.innerText = `${cost} EXP`;
  if (clockEl) clockEl.innerText = `${minutes.toString().padStart(2, '0')}:00`;

  modal.classList.add('active');
  SoundEngine.success();
  ConfettiEngine.fire();

  // Start leisure countdown
  startLeisureCountdown(minutes * 60);
}

let leisureInterval = null;
function startLeisureCountdown(seconds) {
  if (leisureInterval) clearInterval(leisureInterval);
  let rem = seconds;
  const clockEl = document.getElementById('passTimerClock');

  leisureInterval = setInterval(() => {
    rem--;
    const m = Math.floor(rem / 60).toString().padStart(2, '0');
    const s = (rem % 60).toString().padStart(2, '0');
    if (clockEl) clockEl.innerText = `${m}:${s}`;

    if (rem <= 0) {
      clearInterval(leisureInterval);
      SoundEngine.bell();
      showToast("Hết thời gian giải trí rồi! Quay lại tiếp tục mục tiêu nhé!", "🔔");
    }
  }, 1000);
}

// ============================================================================
// 7. TASK MANAGEMENT & MICRO-BREAKDOWN
// ============================================================================
function toggleTaskCompletion(taskId) {
  const task = appState.tasks.find(t => t.id === taskId);
  if (!task) return;

  SoundEngine.click();
  if (task.status === 'done') {
    task.status = 'todo';
    showToast(`Đã chuyển lại task "${task.title}" sang Cần làm.`);
  } else {
    task.status = 'done';
    awardExp(task.exp, `Hoàn thành task "${task.title}"`);
    showToast(`✅ Đã xong "${task.title}" (+${task.exp} EXP)!`, "🎉");
    ConfettiEngine.fire();
  }

  saveState();
  renderUI();
}

function startTaskInFocus(taskId) {
  const task = appState.tasks.find(t => t.id === taskId);
  if (!task) return;

  appState.currentFocusTaskId = taskId;
  task.status = 'doing';
  saveState();

  // Switch to Focus Screen
  switchScreen('screenFocus');
  FocusTimer.setMode('5min');
  FocusTimer.start();

  document.getElementById('focusTargetTitle').innerText = task.title;
  document.getElementById('focusTargetMeta').innerText = `${task.subject} • Dự kiến 1 phiên 25m • +${task.exp} EXP`;

  showToast(`Bắt đầu 5 phút cho: "${task.title}"!`, "🎯");
}

function moveTaskColumn(taskId, newStatus) {
  const task = appState.tasks.find(t => t.id === taskId);
  if (!task) return;

  SoundEngine.click();
  const oldStatus = task.status;
  task.status = newStatus;

  if (newStatus === 'done' && oldStatus !== 'done') {
    awardExp(task.exp, `Hoàn thành task "${task.title}"`);
    showToast(`✅ Hoàn thành task "${task.title}"! (+${task.exp} EXP)`, "🎉");
    ConfettiEngine.fire();
  } else {
    showToast(`Đã chuyển task sang cột ${newStatus.toUpperCase()}`);
  }

  saveState();
  renderUI();
}

// Micro-breakdown Presets (Cirillo Pomodoro micro-tasking)
const BREAKDOWN_PRESETS = {
  essay: [
    { title: "Đọc đề & gạch ý chính", subject: "Tiểu luận" },
    { title: "Tìm 3 nguồn tài liệu uy tín", subject: "Tiểu luận" },
    { title: "Lập dàn ý chi tiết từng mục", subject: "Tiểu luận" },
    { title: "Viết mở bài & luận điểm 1", subject: "Tiểu luận" },
    { title: "Viết luận điểm 2 & 3", subject: "Tiểu luận" },
    { title: "Viết kết luận & rà soát trích dẫn", subject: "Tiểu luận" }
  ],
  coding: [
    { title: "Phân tích yêu cầu & thiết kế kiến trúc", subject: "Lập trình" },
    { title: "Thiết kế CSDL & tạo Mock data", subject: "Lập trình" },
    { title: "Dựng khung giao diện (UI skeleton)", subject: "Lập trình" },
    { title: "Viết API xử lý logic chính", subject: "Lập trình" },
    { title: "Ghép nối Frontend với Backend", subject: "Lập trình" },
    { title: "Test luồng người dùng & fix lỗi", subject: "Lập trình" }
  ],
  slide: [
    { title: "Thống nhất nội dung & phân công slide", subject: "Thuyết trình" },
    { title: "Soạn nội dung vắn tắt cho 10 slide", subject: "Thuyết trình" },
    { title: "Tìm hình ảnh minh họa chất lượng cao", subject: "Thuyết trình" },
    { title: "Thiết kế layout & hiệu ứng slide", subject: "Thuyết trình" },
    { title: "Tập dượt thuyết trình 1 lượt", subject: "Thuyết trình" }
  ]
};

let currentBreakdownList = [];

function applyBreakdownPreset(type) {
  SoundEngine.click();
  const list = BREAKDOWN_PRESETS[type] || [];
  currentBreakdownList = list.map(item => ({
    title: item.title,
    subject: item.subject,
    duration: 25,
    exp: 50
  }));
  renderBreakdownPreview();
}

function renderBreakdownPreview() {
  const container = document.getElementById('breakdownTasksPreview');
  if (!container) return;
  container.innerHTML = currentBreakdownList.map((item, idx) => `
    <div class="breakdown-item">
      <span><strong>${idx + 1}.</strong> ${item.title}</span>
      <span class="preview-badge">+50 EXP</span>
    </div>
  `).join('');
}

// ============================================================================
// 8. BUDDY & SOCIAL ACCOUNTABILITY
// ============================================================================
function sendSelectedPing(message) {
  SoundEngine.click();
  appState.buddy.lastPing = `Bạn: "${message}"`;
  saveState();

  const pingModal = document.getElementById('modalPing');
  if (pingModal) pingModal.classList.remove('active');

  const pingContainer = document.getElementById('lastPingText');
  if (pingContainer) {
    pingContainer.innerHTML = `<strong>Bạn:</strong> "${message}"`;
  }

  showToast(`Đã gửi Ping đến ${appState.buddy.name}: "${message}"`, "📢");

  // Bot & Buddy auto reply after 2.5 seconds
  setTimeout(() => {
    const replies = [
      "Nhận được ping rồi nhé! Đang cày deadline cùng bạn nè! 🔥",
      "Cảm ơn đã nhắc, vừa xong 1 task, tiếp tục thôi! 💪",
      "Hay quá, cùng nhau đánh bại deadline nào! ⚡",
      "Đã uống nước và ngồi vào bàn rồi, cùng cố gắng nhé! 🍵"
    ];
    const reply = replies[Math.floor(Math.random() * replies.length)];
    appState.buddy.lastPing = `@Minh: "${reply}"`;
    saveState();
    if (pingContainer) {
      pingContainer.innerHTML = `<strong>@Minh:</strong> "${reply}"`;
    }
    SoundEngine.success();
    showToast(`@Minh vừa phản hồi: "${reply}"`, "💬");
  }, 2500);
}

// ============================================================================
// 9. UI RENDERING
// ============================================================================
function renderUI() {
  // 1. User Header & Profile
  const nameEl = document.getElementById('userNameLabel');
  const badgeEl = document.getElementById('userBadgeLabel');
  if (nameEl) nameEl.innerText = appState.user.name;
  if (badgeEl) badgeEl.innerText = `${appState.user.levelName} (Lv. ${appState.user.level})`;

  // 2. EXP Progress Card
  const progressText = document.getElementById('expProgressText');
  const progressBar = document.getElementById('expProgressBar');
  const restFund = document.getElementById('restFundText');
  const streakText = document.getElementById('streakText');

  if (progressText) {
    progressText.innerText = `${appState.user.currentExp} / ${appState.user.maxExp} EXP`;
  }
  if (progressBar) {
    const pct = Math.min(100, Math.round((appState.user.currentExp / appState.user.maxExp) * 100));
    progressBar.style.width = `${pct}%`;
  }
  if (restFund) {
    // 200 EXP = 40 minutes (5 EXP = 1 minute)
    const minutes = Math.floor(appState.user.availableExp / 5);
    restFund.innerText = `${minutes} phút (${appState.user.availableExp} EXP)`;
  }
  if (streakText) {
    streakText.innerText = `${appState.user.streakDays} ngày`;
  }

  // 3. Shop Available EXP
  const shopExp = document.getElementById('shopAvailableExp');
  const shopTime = document.getElementById('shopEquivTime');
  if (shopExp) shopExp.innerText = appState.user.availableExp;
  if (shopTime) {
    const minutes = Math.floor(appState.user.availableExp / 5);
    shopTime.innerText = `(~ ${minutes} phút giải trí)`;
  }

  // 4. Focus Tasks List (Top 3 Focus Tasks)
  const focusList = document.getElementById('focusTasksContainer');
  const top3Tasks = appState.tasks.filter(t => t.inTop3);
  const doneCount = top3Tasks.filter(t => t.status === 'done').length;

  const badgeCount = document.getElementById('focusCountBadge');
  if (badgeCount) badgeCount.innerText = `${doneCount}/${top3Tasks.length} Task`;

  if (focusList) {
    focusList.innerHTML = top3Tasks.map(task => {
      const isDone = task.status === 'done';
      return `
        <div class="task-item-card ${isDone ? 'is-completed' : ''}" data-id="${task.id}">
          <div class="task-checkbox-wrap" onclick="toggleTaskCompletion(${task.id})">
            <div class="custom-checkbox">
              ${isDone ? '✓' : ''}
            </div>
          </div>
          <div class="task-content-wrap">
            <h4 class="task-title">${task.title}</h4>
            <div class="task-meta-line">
              <span class="task-subject-tag">${task.subject}</span>
              <span class="task-time-badge">⏱ ${task.duration}m</span>
              <span class="task-exp-badge">+${task.exp} EXP</span>
            </div>
          </div>
          ${!isDone ? `
            <button class="btn-task-start" onclick="startTaskInFocus(${task.id})" title="Bắt đầu 5 phút cho việc này">
              🎯
            </button>
          ` : ''}
        </div>
      `;
    }).join('');
  }

  // 5. Kanban Board List
  renderKanban();

  // 6. Current Focus Task Card in Screen 2
  const currentTask = appState.tasks.find(t => t.id === appState.currentFocusTaskId) || appState.tasks[0];
  if (currentTask) {
    const titleEl = document.getElementById('focusTargetTitle');
    const metaEl = document.getElementById('focusTargetMeta');
    if (titleEl) titleEl.innerText = currentTask.title;
    if (metaEl) metaEl.innerText = `${currentTask.subject} • Dự kiến 1 phiên 25m • +${currentTask.exp} EXP`;
  }
}

function renderKanban() {
  const colTodo = document.getElementById('listTodo');
  const colDoing = document.getElementById('listDoing');
  const colDone = document.getElementById('listDone');

  const todos = appState.tasks.filter(t => t.status === 'todo');
  const doings = appState.tasks.filter(t => t.status === 'doing');
  const dones = appState.tasks.filter(t => t.status === 'done');

  // Counters
  const cTodo = document.getElementById('badgeTodoCount');
  const cDoing = document.getElementById('badgeDoingCount');
  const cDone = document.getElementById('badgeDoneCount');
  const tTodo = document.getElementById('countTodo');
  const tDoing = document.getElementById('countDoing');
  const tDone = document.getElementById('countDone');

  if (cTodo) cTodo.innerText = `${todos.length} việc`;
  if (cDoing) cDoing.innerText = `${doings.length} việc`;
  if (cDone) cDone.innerText = `${dones.length} việc`;
  if (tTodo) tTodo.innerText = todos.length;
  if (tDoing) tDoing.innerText = doings.length;
  if (tDone) tDone.innerText = dones.length;

  const renderCard = (task) => `
    <div class="kanban-task-card" data-id="${task.id}">
      <div class="kanban-card-title">${task.title}</div>
      <div class="kanban-card-footer">
        <span class="task-subject-tag">${task.subject}</span>
        <div class="kanban-actions-row">
          ${task.status !== 'todo' ? `<button class="btn-move-col" onclick="moveTaskColumn(${task.id}, 'todo')">◀ Cần</button>` : ''}
          ${task.status !== 'doing' ? `<button class="btn-move-col" onclick="moveTaskColumn(${task.id}, 'doing')">⚡ Làm</button>` : ''}
          ${task.status !== 'done' ? `<button class="btn-move-col" onclick="moveTaskColumn(${task.id}, 'done')">✔ Xong</button>` : ''}
        </div>
      </div>
    </div>
  `;

  if (colTodo) colTodo.innerHTML = todos.map(renderCard).join('') || '<small style="color:#94A3B8;">Trống</small>';
  if (colDoing) colDoing.innerHTML = doings.map(renderCard).join('') || '<small style="color:#94A3B8;">Trống</small>';
  if (colDone) colDone.innerHTML = dones.map(renderCard).join('') || '<small style="color:#94A3B8;">Trống</small>';
}

// ============================================================================
// 10. NAVIGATION & EVENT LISTENERS
// ============================================================================
function switchScreen(targetScreenId) {
  SoundEngine.click();
  document.querySelectorAll('.screen-view').forEach(screen => {
    screen.classList.remove('active');
  });

  const target = document.getElementById(targetScreenId);
  if (target) target.classList.add('active');

  // Update Bottom Nav Tab Highlights
  document.querySelectorAll('.nav-tab').forEach(tab => {
    tab.classList.toggle('active', tab.dataset.screen === targetScreenId);
  });
}

document.addEventListener('DOMContentLoaded', () => {
  // Initialize UI & Confetti
  renderUI();
  ConfettiEngine.init();

  // Bottom Navigation Click Handlers
  document.querySelectorAll('.nav-tab').forEach(tab => {
    tab.addEventListener('click', () => {
      const screenId = tab.dataset.screen;
      if (screenId) switchScreen(screenId);
    });
  });

  // 5-Minute Rule Hero Banner
  const btnStart5Min = document.getElementById('btnStart5MinRule');
  if (btnStart5Min) {
    btnStart5Min.addEventListener('click', () => {
      switchScreen('screenFocus');
      FocusTimer.setMode('5min');
      FocusTimer.start();
      showToast("Khởi động 5 phút! Chỉ cần mở file và viết 1 câu!", "⏱️");
    });
  }

  // Exit Focus Screen
  const btnExitFocus = document.getElementById('btnExitFocus');
  if (btnExitFocus) {
    btnExitFocus.addEventListener('click', () => {
      switchScreen('screenHome');
    });
  }

  // Timer Toggle (Pause/Resume)
  const btnTimerToggle = document.getElementById('btnTimerToggle');
  if (btnTimerToggle) {
    btnTimerToggle.addEventListener('click', () => {
      FocusTimer.toggle();
    });
  }

  // Decision 1: Nghỉ ngơi (Không có EXP) - Quyền dừng lại không tội lỗi
  const btnRest = document.getElementById('btnRestOption');
  if (btnRest) {
    btnRest.addEventListener('click', () => {
      SoundEngine.click();
      FocusTimer.pause();
      showToast("Tuyệt vời! Bạn đã vượt qua rào cản trì hoãn. Nghỉ ngơi thoải mái nhé!", "🕊️");
      switchScreen('screenHome');
    });
  }

  // Decision 2: Làm tiếp (Vào 25m Pomodoro • Nhận 50 EXP)
  const btnContinue = document.getElementById('btnContinueOption');
  if (btnContinue) {
    btnContinue.addEventListener('click', () => {
      FocusTimer.setMode('25min');
      FocusTimer.start();
      SoundEngine.success();
      showToast("Bứt phá vào phiên tập trung 25 phút! +50 EXP đang chờ bạn!", "🔥");
    });
  }

  // Timer Mode Switcher Pills
  document.querySelectorAll('.mode-pill').forEach(pill => {
    pill.addEventListener('click', () => {
      SoundEngine.click();
      const mode = pill.dataset.mode;
      FocusTimer.setMode(mode);
    });
  });

  // Ambient Sounds
  document.querySelectorAll('.ambient-chip').forEach(chip => {
    chip.addEventListener('click', () => {
      document.querySelectorAll('.ambient-chip').forEach(c => c.classList.remove('active'));
      chip.classList.add('active');
      const ambient = chip.dataset.ambient;
      SoundEngine.setAmbient(ambient);
      showToast(ambient === 'none' ? 'Đã tắt âm thanh' : `Bật âm thanh: ${chip.innerText}`);
    });
  });

  // Send Ping Modal Handlers
  const btnSendPing = document.getElementById('btnSendPing');
  const modalPing = document.getElementById('modalPing');
  const btnClosePing = document.getElementById('btnClosePing');
  if (btnSendPing && modalPing) {
    btnSendPing.addEventListener('click', () => {
      SoundEngine.click();
      modalPing.classList.add('active');
    });
  }
  if (btnClosePing && modalPing) {
    btnClosePing.addEventListener('click', () => {
      modalPing.classList.remove('active');
    });
  }

  const btnConfirmCustomPing = document.getElementById('btnConfirmSendCustomPing');
  if (btnConfirmCustomPing) {
    btnConfirmCustomPing.addEventListener('click', () => {
      const input = document.getElementById('customPingInput');
      const text = input && input.value.trim() ? input.value.trim() : "Cố lên bạn nhé! 🔥";
      sendSelectedPing(text);
      if (input) input.value = '';
    });
  }

  // Micro-breakdown Modal Handlers
  const modalBreakdown = document.getElementById('modalBreakdown');
  const btnOpenBreakdown1 = document.getElementById('btnOpenBreakdownModal');
  const btnOpenBreakdown2 = document.getElementById('btnAutoBreakdown');
  const btnOpenBreakdown3 = document.getElementById('btnOpenBreakdownFromTasks');
  const btnCloseBreakdown = document.getElementById('btnCloseBreakdown');
  const btnCancelBreakdown = document.getElementById('btnCancelBreakdown');
  const btnSaveBreakdown = document.getElementById('btnSaveBreakdown');

  const openBreakdown = () => {
    SoundEngine.click();
    applyBreakdownPreset('essay');
    modalBreakdown.classList.add('active');
  };

  if (btnOpenBreakdown1) btnOpenBreakdown1.addEventListener('click', openBreakdown);
  if (btnOpenBreakdown2) btnOpenBreakdown2.addEventListener('click', openBreakdown);
  if (btnOpenBreakdown3) btnOpenBreakdown3.addEventListener('click', openBreakdown);

  if (btnCloseBreakdown) btnCloseBreakdown.addEventListener('click', () => modalBreakdown.classList.remove('active'));
  if (btnCancelBreakdown) btnCancelBreakdown.addEventListener('click', () => modalBreakdown.classList.remove('active'));

  if (btnSaveBreakdown) {
    btnSaveBreakdown.addEventListener('click', () => {
      if (currentBreakdownList.length === 0) return;
      currentBreakdownList.forEach((item, i) => {
        appState.tasks.push({
          id: Date.now() + i,
          title: item.title,
          subject: item.subject,
          duration: 25,
          exp: 50,
          status: 'todo',
          inTop3: i < 2 // first two in top 3
        });
      });
      saveState();
      renderUI();
      modalBreakdown.classList.remove('active');
      SoundEngine.success();
      showToast(`Đã thêm ${currentBreakdownList.length} thẻ task vào bảng Kanban!`, "🧩");
      ConfettiEngine.fire();
    });
  }

  // New Task Modal
  const modalNewTask = document.getElementById('modalNewTask');
  const btnQuickAddTask = document.getElementById('btnQuickAddTask');
  const btnAddNewTaskModal = document.getElementById('btnAddNewTaskModal');
  const btnCloseNewTask = document.getElementById('btnCloseNewTask');
  const btnCancelNewTask = document.getElementById('btnCancelNewTask');
  const btnSaveNewTask = document.getElementById('btnSaveNewTask');

  const openNewTask = () => {
    SoundEngine.click();
    modalNewTask.classList.add('active');
  };

  if (btnQuickAddTask) btnQuickAddTask.addEventListener('click', openNewTask);
  if (btnAddNewTaskModal) btnAddNewTaskModal.addEventListener('click', openNewTask);
  if (btnCloseNewTask) btnCloseNewTask.addEventListener('click', () => modalNewTask.classList.remove('active'));
  if (btnCancelNewTask) btnCancelNewTask.addEventListener('click', () => modalNewTask.classList.remove('active'));

  if (btnSaveNewTask) {
    btnSaveNewTask.addEventListener('click', () => {
      const titleInput = document.getElementById('taskTitleInput');
      const subjectInput = document.getElementById('taskSubjectInput');
      const top3Check = document.getElementById('addToTop3Checkbox');

      const title = titleInput ? titleInput.value.trim() : '';
      const subject = subjectInput && subjectInput.value.trim() ? subjectInput.value.trim() : 'Tổng hợp';
      const inTop3 = top3Check ? top3Check.checked : true;

      if (!title) {
        showToast("Vui lòng nhập tên công việc!", "⚠️");
        return;
      }

      appState.tasks.push({
        id: Date.now(),
        title: title,
        subject: subject,
        duration: 25,
        exp: 50,
        status: 'todo',
        inTop3: inTop3
      });

      if (titleInput) titleInput.value = '';
      saveState();
      renderUI();
      modalNewTask.classList.remove('active');
      SoundEngine.success();
      showToast(`Đã tạo thẻ task "${title}" (+50 EXP)!`, "➕");
    });
  }

  // Guilt-Free Pass Modal Close
  const btnCloseRewardPass = document.getElementById('btnCloseRewardPass');
  const btnFinishLeisure = document.getElementById('btnFinishLeisure');
  const modalRewardPass = document.getElementById('modalRewardPass');

  if (btnCloseRewardPass) {
    btnCloseRewardPass.addEventListener('click', () => {
      modalRewardPass.classList.remove('active');
      if (leisureInterval) clearInterval(leisureInterval);
    });
  }
  if (btnFinishLeisure) {
    btnFinishLeisure.addEventListener('click', () => {
      modalRewardPass.classList.remove('active');
      if (leisureInterval) clearInterval(leisureInterval);
      showToast("Nghỉ ngơi sảng khoái rồi! Tiếp tục học nào!", "💪");
    });
  }

  // Custom Reward Creator
  const btnAddCustom = document.getElementById('btnAddCustomReward');
  if (btnAddCustom) {
    btnAddCustom.addEventListener('click', () => {
      const title = prompt("Nhập tên phần thưởng giải trí của bạn:", "Ăn một cốc chè / trà sữa");
      if (!title) return;
      const mins = parseInt(prompt("Thời gian giải trí (phút):", "15") || "15", 10);
      const cost = mins * 5; // 5 EXP per minute

      const grid = document.getElementById('rewardsListContainer');
      const card = document.createElement('div');
      card.className = 'reward-card';
      card.innerHTML = `
        <div class="reward-icon-box">🎁</div>
        <div class="reward-details">
          <h4 class="reward-title">${title} / ${mins} phút</h4>
          <span class="reward-cost">(Chi phí: ${cost} EXP)</span>
        </div>
        <button class="btn-redeem" onclick="redeemReward('custom', '${title}', ${mins}, ${cost})">
          [ ĐỔI NGAY ]
        </button>
      `;
      grid.appendChild(card);
      SoundEngine.success();
      showToast(`Đã thêm phần thưởng "${title}" (${cost} EXP)!`, "🎁");
    });
  }

  // Desktop Simulator Toolbar: Toggle Phone Bezel
  const btnToggleFrame = document.getElementById('btnToggleFrame');
  const devicePhone = document.getElementById('devicePhone');
  if (btnToggleFrame && devicePhone) {
    btnToggleFrame.addEventListener('click', () => {
      devicePhone.classList.toggle('fullscreen-mode');
      const isFull = devicePhone.classList.contains('fullscreen-mode');
      btnToggleFrame.querySelector('.label').innerText = isFull ? 'Toàn màn hình' : 'Khung ĐT';
      ConfettiEngine.resize();
    });
  }

  // Desktop Simulator Toolbar: Reset Sample Data
  const btnResetData = document.getElementById('btnResetData');
  if (btnResetData) {
    btnResetData.addEventListener('click', () => {
      if (confirm("Khôi phục lại dữ liệu mẫu StudyQuest theo đúng bản mẫu prototype?")) {
        appState = JSON.parse(JSON.stringify(DEFAULT_STATE));
        saveState();
        renderUI();
        showToast("Đã khôi phục dữ liệu mẫu Prototype!", "🔄");
      }
    });
  }

  // Desktop Simulator Toolbar: Sound Toggle
  const btnToggleSound = document.getElementById('btnToggleSound');
  const soundIcon = document.getElementById('soundIcon');
  if (btnToggleSound) {
    btnToggleSound.addEventListener('click', () => {
      appState.soundEnabled = !appState.soundEnabled;
      saveState();
      if (soundIcon) soundIcon.innerText = appState.soundEnabled ? '🔊' : '🔇';
      showToast(appState.soundEnabled ? "Đã bật âm thanh" : "Đã tắt âm thanh");
    });
  }

  // Update Status Bar Clock
  const updateClock = () => {
    const clock = document.getElementById('statusClock');
    if (clock) {
      const now = new Date();
      const h = now.getHours().toString().padStart(2, '0');
      const m = now.getMinutes().toString().padStart(2, '0');
      clock.innerText = `${h}:${m}`;
    }
  };
  updateClock();
  setInterval(updateClock, 30000);

  // Register PWA Service Worker
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('sw.js').catch(err => console.log('SW Reg Fail:', err));
  }
});
