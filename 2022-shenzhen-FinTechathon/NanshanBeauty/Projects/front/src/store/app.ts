import defaultSettings from '@/settings'
import { defineStore } from 'pinia'
import { SettingTy } from '~/common'
export const useAppStore = defineStore('app', {
  /***
   *类似于组件的 data数据的 ,用来存储全局状态的
   * 1、必须是箭头函数
   */
  state: () => {
    return {
      sidebar: { opened: true },
      device: 'desktop',
      settings: defaultSettings as SettingTy,
      cachedViews: [] as Array<string>,
      cachedViewsDeep: [] as Array<string>
    }
  },

  /***
   *封装处理数据的函数（业务逻辑)：修改数据
   */
  actions: {
    M_settings(data) {
      this.$patch((state) => {
        state.settings = { ...state.settings, ...data }
      })
    },
    M_sidebar_opened(data: boolean) {
      this.$patch((state) => {
        state.sidebar.opened = data
      })
    },
    M_toggleSideBar() {
      this.$patch((state) => {
        state.sidebar.opened = !state.sidebar.opened
      })
    },
    /*keepAlive缓存*/
    M_ADD_CACHED_VIEW(view) {
      this.$patch((state) => {
        if (state.cachedViews.includes(view)) return
        state.cachedViews.push(view)
      })
    },

    M_DEL_CACHED_VIEW(view) {
      this.$patch((state) => {
        const index = state.cachedViews.indexOf(view)
        index > -1 && state.cachedViews.splice(index, 1)
      })
    },
    /*third  keepAlive*/
    M_ADD_CACHED_VIEW_DEEP(view) {
      this.$patch((state) => {
        if (state.cachedViewsDeep.includes(view)) return
        state.cachedViewsDeep.push(view)
      })
    },
    M_DEL_CACHED_VIEW_DEEP(view) {
      this.$patch((state) => {
        const index = state.cachedViewsDeep.indexOf(view)
        index > -1 && state.cachedViewsDeep.splice(index, 1)
      })
    },

    A_sidebar_opened(data: boolean) {
      this.M_sidebar_opened(data)
    }
  }
})
