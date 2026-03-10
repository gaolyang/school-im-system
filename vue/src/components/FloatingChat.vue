<template>
  <div class="chat-wrapper" :style="{ right: position === 'right' ? '30px' : 'auto', left: position === 'left' ? '30px' : 'auto' }">    <transition name="bounce">
      <div v-if="!isOpen" class="float-btn" @click="toggleChat">
        <div class="icon-wrap">
          <span class="emoji">💬</span>
          <span class="text">{{ currentUserId === 1 ? '联系老师' : '联系学生' }}</span>
        </div>
        <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
      </div>
    </transition>

    <transition name="slide-up">
      <div v-if="isOpen" class="chat-window">

        <div class="chat-header">
          <div class="header-left">
            <div class="avatar-circle">
              {{ targetRoleName === '二线指导老师' ? '师' : '生' }}
            </div>
            <div class="info">
              <span class="name">{{ targetRoleName }}</span>
              <span class="status">
                <span class="dot"></span>在线
              </span>
            </div>
          </div>
          <div class="header-right">
            <button class="close-btn" @click="toggleChat" title="关闭">×</button>
          </div>
        </div>

        <div class="chat-body" ref="msgBoxRef" @click="handleImageClick">
          <div v-for="(msg, index) in messages" :key="index"
               :class="['message-row', isMe(msg.senderId) ? 'row-right' : 'row-left']">

            <div v-if="!isMe(msg.senderId)" class="avatar">
              {{ targetRoleName === '二线指导老师' ? '师' : '生' }}
            </div>

            <div class="content-wrapper">
              <div class="bubble-wrapper">
                <div class="bubble" v-html="msg.content"></div>
              </div>

              <div class="meta-info">
                <span v-if="isMe(msg.senderId)" class="read-status" :class="{ read: msg.isRead === 1 }">
                  <i class="status-icon" v-if="msg.isRead === 1">✓✓</i>
                  <i class="status-icon" v-else>✓</i>
                  {{ msg.isRead === 1 ? '已读' : '未读' }}
                </span>
                <span class="time">{{ formatTime(msg.createTime) }}</span>
              </div>
            </div>

            <div v-if="isMe(msg.senderId)" class="avatar me">我</div>
          </div>
        </div>

        <div class="chat-footer">
          <div class="editor-container">
            <Toolbar
                style="border-bottom: 1px solid #f0f0f0"
                :editor="editorRef"
                :defaultConfig="toolbarConfig"
                :mode="mode"
            />
            <Editor
                style="height: 120px; overflow-y: hidden;"
                v-model="valueHtml"
                :defaultConfig="editorConfig"
                :mode="mode"
                @onCreated="handleCreated"
                @customPaste="customPaste"
            />
          </div>
          <div class="footer-actions">
            <span class="tip">按 Enter 发送，Shift + Enter 换行</span>
            <button class="send-btn" @click="sendMessage">发送</button>
          </div>
        </div>
      </div>
    </transition>

    <transition name="fade">
      <div v-if="previewUrl" class="image-preview-overlay" @click="closePreview">
        <img :src="previewUrl" alt="预览图片" @click.stop />
        <button class="close-preview-btn" @click="closePreview">×</button>
      </div>
    </transition>
  </div>
</template>

<script setup>
/* eslint-disable no-undef */
import { ref, onMounted, nextTick, shallowRef, onBeforeUnmount, computed, watch } from 'vue';
import SockJS from 'sockjs-client/dist/sockjs.min.js';
import Stomp from 'stompjs';
import '@wangeditor/editor/dist/css/style.css' // 引入 WangEditor 样式
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import axios from 'axios';

// ===================== 1. 核心配置与状态 =====================

// 接收父组件传入的参数
// eslint-disable-next-line no-undef
const props = defineProps({
  orderId: {
    type: [Number, String],
    required: true
  },
  currentUserId: {
    type: Number,
    required: true
  },
  targetRoleName: {
    type: String,
    default: '对方'
  },
  position: {
    type: String,
    default: 'right'
  }
});

// 计算属性：根据当前用户ID推算接收者ID
const targetUserId = computed(() => props.currentUserId === 1 ? 2 : 1);

// API 地址动态化
const WS_URL = 'http://localhost:8080/ws-chat';
const UPLOAD_API = '/api/chat/upload';

const HISTORY_API = computed(() => `http://localhost:8080/api/chat/history/${props.orderId}`);
const READ_API = computed(() => `http://localhost:8080/api/chat/read/${props.orderId}?userId=${props.currentUserId}`);

// 响应式变量
const isOpen = ref(false);
const messages = ref([]);
const unreadCount = ref(0);
const msgBoxRef = ref(null);
const previewUrl = ref(null);
let stompClient = null;

// ===================== 2. WangEditor 配置 =====================

const editorRef = shallowRef();
const valueHtml = ref('');
const mode = 'simple';

const toolbarConfig = {
  excludeKeys: ['group-video', 'insertTable', 'codeBlock', 'fullScreen', 'headerSelect', 'todo', 'emotion']
};

const editorConfig = {
  placeholder: '在此输入内容...',
  onKeydown(editor, event) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      sendMessage();
      return false;
    }
  },
  MENU_CONF: {
    uploadImage: {
      server: UPLOAD_API,
      fieldName: 'file',
      maxFileSize: 10 * 1024 * 1024,
      allowedFileTypes: ['image/*'],
      customInsert(res, insertFn) {
        if (res.errno === 0 && res.data && res.data.url) {
          insertFn(res.data.url, '', '');
        } else {
          console.error('上传失败', res);
          alert('图片上传失败');
        }
      }
    }
  }
};

const handleCreated = (editor) => {
  editorRef.value = editor;
};

// ===================== 3. 业务逻辑方法 =====================

const toggleChat = () => {
  isOpen.value = !isOpen.value;
  if (isOpen.value) {
    unreadCount.value = 0;
    loadHistory();
    markMessagesAsRead();
    scrollToBottom();
  }
};

const sendMessage = () => {
  const editor = editorRef.value;
  if (!editor) return;

  const text = editor.getText().trim();
  const html = valueHtml.value;

  if ((!text && !html.includes('<img')) || !stompClient) return;

  const msgObj = {
    senderId: props.currentUserId,
    receiverId: targetUserId.value,
    content: html,
    isRead: 0,
    msgType: 1
  };

  stompClient.send(`/app/send/${props.orderId}`, {}, JSON.stringify(msgObj));

  editor.clear();
  valueHtml.value = '';
};

const handleReceiveMessage = (msg) => {
  if (msg.msgType === 3) {
    if (msg.senderId !== props.currentUserId) {
      messages.value.forEach(m => {
        if (m.senderId === props.currentUserId) {
          m.isRead = 1;
        }
      });
    }
    return;
  }

  messages.value.push(msg);

  if (!isOpen.value) {
    unreadCount.value++;
  } else {
    if (msg.senderId !== props.currentUserId) {
      markMessagesAsRead();
    }
  }

  scrollToBottom();
};

const initWebSocket = () => {
  if (stompClient && stompClient.connected) return;

  const socket = new SockJS(WS_URL);
  stompClient = Stomp.over(socket);

  // ✅ 修正：移除了未使用的 frame 参数，改为 ()
  stompClient.connect({}, () => {
    console.log(`用户 ${props.currentUserId} 已连接工单 ${props.orderId}`);

    stompClient.subscribe(`/topic/order/${props.orderId}`, (frame) => {
      if (frame.body) {
        const msg = JSON.parse(frame.body);
        handleReceiveMessage(msg);
      }
    });
  }, (err) => {
    console.error('WebSocket 连接失败', err);
    setTimeout(() => initWebSocket(), 5000);
  });
};

const loadHistory = async () => {
  try {
    const res = await axios.get(HISTORY_API.value);
    if (res.data) {
      messages.value = res.data;
      scrollToBottom();
    }
  } catch (err) { console.error('获取历史记录失败', err); }
};

const markMessagesAsRead = async () => {
  try {
    await axios.post(READ_API.value);
  } catch (err) { console.error('标记已读失败', err); }
};

const handleImageClick = (e) => {
  const target = e.target;
  if (target.tagName === 'IMG' && target.closest('.bubble')) {
    previewUrl.value = target.src;
  }
};

const closePreview = () => {
  previewUrl.value = null;
};

const scrollToBottom = () => {
  nextTick(() => {
    setTimeout(() => {
      if (msgBoxRef.value) {
        msgBoxRef.value.scrollTo({
          top: msgBoxRef.value.scrollHeight,
          behavior: 'smooth'
        });
      }
    }, 100);
  });
};

// const isMe = (senderId) => senderId === props.currentUserId;
const isMe = (senderId) => Number(senderId) === Number(props.currentUserId);


const formatTime = (timeStr) => {
  if (!timeStr) return '';
  const date = new Date(timeStr);
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');
  return `${hours}:${minutes}`;
};

// ===================== 4. 生命周期与监听 =====================

watch(() => props.orderId, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    if (stompClient) {
      stompClient.disconnect();
      stompClient = null;
    }
    messages.value = [];
    loadHistory();
    initWebSocket();
  }
});

onMounted(() => {
  setTimeout(() => {
    loadHistory();
    initWebSocket();
  }, 500);
});

onBeforeUnmount(() => {
  if (stompClient) stompClient.disconnect();
  const editor = editorRef.value;
  if (editor) editor.destroy();
});
</script>

<style scoped lang="scss">
/* ================= 全局容器样式 ================= */
.chat-wrapper {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 9999;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans', sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji';
}

/* ================= 悬浮球样式 ================= */
.float-btn {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  border-radius: 50%;
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 8px 24px rgba(37, 99, 235, 0.3);
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  position: relative;

  &:hover {
    transform: scale(1.1) rotate(-5deg);
    box-shadow: 0 12px 32px rgba(37, 99, 235, 0.4);
  }

  .icon-wrap {
    text-align: center;
    line-height: 1;
  }

  .emoji {
    font-size: 26px;
    margin-bottom: 2px;
    display: block;
  }

  .text {
    font-size: 10px;
    font-weight: 500;
  }

  .badge {
    position: absolute;
    top: -2px;
    right: -2px;
    background: #ef4444;
    color: white;
    font-size: 12px;
    font-weight: bold;
    min-width: 20px;
    height: 20px;
    line-height: 20px;
    text-align: center;
    border-radius: 10px;
    border: 2px solid #fff;
    animation: pulse 2s infinite;
  }
}

/* ================= 聊天窗口样式 ================= */
.chat-window {
  width: 420px;
  height: 650px;
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border: 1px solid rgba(0,0,0,0.05);

  /* --- 头部 --- */
  .chat-header {
    height: 64px;
    background: #f8fafc;
    border-bottom: 1px solid #e2e8f0;
    padding: 0 20px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-shrink: 0;

    .header-left {
      display: flex;
      align-items: center;
      gap: 12px;

      .avatar-circle {
        width: 40px;
        height: 40px;
        background: linear-gradient(135deg, #60a5fa, #3b82f6);
        color: #fff;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: bold;
        font-size: 16px;
        box-shadow: 0 2px 8px rgba(59, 130, 246, 0.3);
      }

      .info {
        display: flex;
        flex-direction: column;
        justify-content: center;

        .name {
          font-weight: 600;
          font-size: 15px;
          color: #1e293b;
          line-height: 1.2;
        }

        .status {
          font-size: 12px;
          color: #10b981;
          display: flex;
          align-items: center;
          gap: 4px;
          margin-top: 2px;

          .dot {
            width: 6px;
            height: 6px;
            background: #10b981;
            border-radius: 50%;
          }
        }
      }
    }

    .close-btn {
      width: 32px;
      height: 32px;
      border: none;
      background: transparent;
      font-size: 24px;
      color: #94a3b8;
      cursor: pointer;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.2s;

      &:hover {
        background: #e2e8f0;
        color: #ef4444;
      }
    }
  }

  /* --- 消息主体 --- */
  .chat-body {
    flex: 1;
    background: #f1f5f9;
    padding: 20px;
    overflow-y: auto;
    overflow-x: hidden;

    /* 滚动条美化 */
    &::-webkit-scrollbar {
      width: 6px;
    }
    &::-webkit-scrollbar-thumb {
      background-color: #cbd5e1;
      border-radius: 3px;
    }

    .message-row {
      display: flex;
      margin-bottom: 24px;
      gap: 12px;
      animation: fadeIn 0.3s ease;

      /* 头像通用样式 */
      .avatar {
        width: 36px;
        height: 36px;
        border-radius: 8px;
        background: #fff;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 13px;
        color: #64748b;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        flex-shrink: 0;
        border: 1px solid #e2e8f0;

        &.me {
          background: #3b82f6;
          color: white;
          border: none;
        }
      }

      /* 消息内容容器 */
      .content-wrapper {
        max-width: 75%;
        display: flex;
        flex-direction: column;

        .bubble-wrapper {
          position: relative;

          .bubble {
            padding: 12px 16px;
            border-radius: 12px;
            background: #fff;
            font-size: 14px;
            line-height: 1.6;
            color: #334155;
            box-shadow: 0 2px 6px rgba(0,0,0,0.04);
            word-wrap: break-word;

            /* 富文本内部样式重置 */
            p { margin: 0; }
          }

          /* 图片样式：关键 */
          :deep(img) {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
            display: block;
            margin: 8px 0;
            cursor: zoom-in;
            border: 1px solid #e2e8f0;
            transition: transform 0.2s;

            &:hover {
              opacity: 0.95;
            }
          }
        }

        /* 状态信息 (时间、已读) */
        .meta-info {
          display: flex;
          align-items: center;
          gap: 6px;
          margin-top: 6px;
          font-size: 11px;
          color: #94a3b8;

          .read-status {
            display: flex;
            align-items: center;
            gap: 2px;

            &.read {
              color: #10b981; /* 已读绿色 */
            }

            .status-icon {
              font-style: normal;
              font-size: 10px;
              font-family: monospace;
            }
          }
        }
      }

      /* === 对方消息样式 (左侧) === */
      &.row-left {
        justify-content: flex-start;

        .bubble {
          border-top-left-radius: 2px;
        }

        .content-wrapper {
          align-items: flex-start;
        }
      }

      /* === 我的消息样式 (右侧) === */
      &.row-right {
        justify-content: flex-end;
        //flex-direction: row-reverse;

        .bubble {
          background: #dbeafe; /* 浅蓝色背景 */
          color: #1e3a8a;
          border-top-right-radius: 2px;
        }

        .content-wrapper {
          align-items: flex-end;
        }

        .meta-info {
          justify-content: flex-end;
        }
      }
    }
  }

  /* --- 底部输入区 --- */
  .chat-footer {
    background: #fff;
    padding: 16px;
    border-top: 1px solid #f1f5f9;
    display: flex;
    flex-direction: column;
    gap: 12px;

    .editor-container {
      border: 1px solid #e2e8f0;
      border-radius: 8px;
      overflow: hidden;
      transition: border-color 0.2s;

      &:focus-within {
        border-color: #3b82f6;
        box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
      }
    }

    .footer-actions {
      display: flex;
      align-items: center;
      justify-content: space-between;

      .tip {
        font-size: 12px;
        color: #94a3b8;
      }

      .send-btn {
        padding: 8px 24px;
        background: #3b82f6;
        color: white;
        border: none;
        border-radius: 6px;
        font-size: 14px;
        font-weight: 500;
        cursor: pointer;
        transition: background 0.2s;
        box-shadow: 0 2px 4px rgba(59, 130, 246, 0.2);

        &:hover {
          background: #2563eb;
        }

        &:active {
          transform: translateY(1px);
        }
      }
    }
  }
}

/* ================= 图片预览遮罩层 ================= */
.image-preview-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.9);
  z-index: 10000;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(5px);

  img {
    max-width: 90%;
    max-height: 90%;
    border-radius: 8px;
    box-shadow: 0 20px 50px rgba(0,0,0,0.5);
    cursor: zoom-out;
    animation: zoomIn 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  }

  .close-preview-btn {
    position: absolute;
    top: 30px;
    right: 30px;
    background: rgba(255, 255, 255, 0.15);
    border: 1px solid rgba(255, 255, 255, 0.2);
    color: white;
    font-size: 32px;
    width: 48px;
    height: 48px;
    border-radius: 50%;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;

    &:hover {
      background: rgba(255, 255, 255, 0.3);
      transform: rotate(90deg);
    }
  }
}

/* ================= 动画定义 ================= */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes zoomIn {
  from { transform: scale(0.9); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

@keyframes pulse {
  0% { box-shadow: 0 0 0 0 rgba(239, 68, 68, 0.7); }
  70% { box-shadow: 0 0 0 6px rgba(239, 68, 68, 0); }
  100% { box-shadow: 0 0 0 0 rgba(239, 68, 68, 0); }
}

/* Vue Transition 组件动画 */
.bounce-enter-active {
  animation: bounce-in 0.5s;
}
.bounce-leave-active {
  animation: bounce-in 0.5s reverse;
}
@keyframes bounce-in {
  0% { transform: scale(0); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}
.slide-up-enter-from,
.slide-up-leave-to {
  opacity: 0;
  transform: translateY(40px) scale(0.9);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
