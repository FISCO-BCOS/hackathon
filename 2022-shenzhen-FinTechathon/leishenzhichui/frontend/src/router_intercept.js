import router from './router'
import store from './store'

router.beforeEach(async(to, from, next) => {
  if (to.path == '/login') {
    next();
  } else {
    let getRoles = store && store.getters.roles && store.getters.roles.length>0;
    if (getRoles) {
      next();
    } else {
      // 刷新
      await store.dispatch('GETINFO');
      let roles = store.getters.roles;
      if (roles) {
        next({path: to.path});
      } else {
        next({path: '/login'});
      }
    }
  }
})