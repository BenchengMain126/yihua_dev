// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'


Vue.config.productionTip = false;


//log日志 npm install vconsole
import Vconsole from 'vconsole';
let vConsole = new Vconsole();
export default vConsole


import Vant from 'vant'
import 'vant/lib/index.css'
Vue.use(Vant);

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    components: {App},
    template: '<App/>'
});
