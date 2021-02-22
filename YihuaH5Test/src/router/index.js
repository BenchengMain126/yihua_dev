import Vue from 'vue'
import Router from 'vue-router'
import NotFound from '../views/404'
import android from '../views/android'
import showResult from '../views/showResult'


Vue.use(Router);

export default new Router({

  routes: [
    {
      path: '/',
      redirect: 'android'
    }, {
      path: '/android',
      name: 'android',
      component: android,
    }, {
      path: '/showResult',
      name: 'showResult',
      component: showResult,
    },{
      path: '*',
      component: NotFound
    }
  ]
})
