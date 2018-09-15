// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import componentRegister from "./components"

import '../static/js/jquery.min.js'
import '../static/js/jquery-ui.js'
import '../static/js/flex-slider.min.js'
import '../static/js/navigation.min.js'
import '../static/js/jquery.layerslider.js'
import '../static/js/layerslider.transitions.js'
import '../static/js/carousel.js'
import '../static/js/jquery.theme.plugins.min.js'
import '../static/js/jquery.themepunch.revolution.min.js'
import '../static/js/flickr.js'
import '../static/js/instagram.js'
import '../static/js/jquery.twitter.js'
import '../static/js/prettyPhoto.min.js'
import '../static/js/jquery.tooltips.min.js'
import '../static/js/isotope.min.js'
import '../static/js/scrolltopcontrol.js'
import '../static/js/jquery.easy-pie-chart.js'
import '../static/js/jquery.transit.min.js'

Vue.config.productionTip = false

componentRegister.register(Vue);

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
