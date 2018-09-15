import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/content/home'
import AboutUs from '@/components/content/aboutUs'
import Login from '@/components/content/login'
import Register from '@/components/content/register'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/about-us',
      name: 'about-us',
      component: AboutUs
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    }
  ]
})
