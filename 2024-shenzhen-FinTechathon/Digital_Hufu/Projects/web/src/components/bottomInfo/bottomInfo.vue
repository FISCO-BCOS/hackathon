<template>
  <div class="flex flex-col md:flex-row gap-2 items-center text-sm text-slate-700 dark:text-slate-500 justify-center py-2">
    <div class="text-center">
      <span class="mr-1">Powered by</span>
      <span>
        <a
          class="font-bold text-active"
        >数字虎符</a>
      </span>
    </div>
    <slot />
  </div>
</template>

<script  setup>
defineOptions({
  name: 'BottomInfo'
})

console.log(
  `%c powered by %c flipped-aurorae %c`,
  'background:#0081ff; padding: 1px; border-radius: 3px 0 0 3px; color: #fff',
  'background:#354855; padding: 1px 5px; border-radius: 0 3px 3px 0; color: #fff; font-weight: bold;',
  'background:transparent'
)
</script>

