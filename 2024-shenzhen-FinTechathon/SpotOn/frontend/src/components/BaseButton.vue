<template>
  <button 
    :class="[buttonClass, customClasses]" 
    :disabled="disabled" 
    @click="$emit('click')" 
    :style="{ marginTop: marginTop }" 
    aria-label="按钮"
  >
    <slot></slot>
  </button>
</template>

<script>
export default {
  name: 'BaseButton',
  props: {
    disabled: {
      type: Boolean,
      default: false,
    },
    type: {
      type: String,
      default: 'primary', // 可选值: primary, success, danger
    },
    marginTop: {
      type: String,
      default: '15px', // 默认的按钮上间距
    },
    customClasses: {
      type: String,
      default: '',
    },
  },
  computed: {
    buttonClass() {
      return {
        'base-button': true,
        [`button-${this.type}`]: true,
      };
    },
  },
};
</script>

<style scoped>
.base-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 200px;      /* 固定宽度 */
  height: 50px;      /* 固定高度 */
  padding: 0;        /* 移除内边距 */
  background-color: #6c0aeb; /* 默认蓝色 */
  color: rgb(255, 255, 255);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 20px;
  transition: background-color 0.3s ease;
}

.button-primary {
  background-color: #61049e;
}

.button-primary:hover:not(:disabled) {
  background-color: #840ae7;
}

.button-success {
  background-color: #3e0350; /* 用户指定的绿色 */
}

.button-success:hover:not(:disabled) {
  background-color: #772188;
}

.button-danger {
  background-color: #dc3545;
}

.button-danger:hover:not(:disabled) {
  background-color: #c82333;
}

.base-button:disabled {
  background-color: #1ae921;
  cursor: not-allowed;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .base-button {
    width: 100%;      /* 小屏幕下占满宽度 */
    height: 45px;     /* 调整高度 */
    font-size: 14px;  /* 调整字体大小 */
    margin-top: 10px; /* 调整按钮上间距 */
  }
}
</style>