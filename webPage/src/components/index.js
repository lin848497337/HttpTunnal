import Header from "./Header"
import Footer from "./Footer"
import Common from "./common"

function registerComponent(Vue){
  Vue.component("c-header", Header);
  Vue.component("c-footer", Footer);
}

export default {
  register(Vue){
    Common.register(Vue);
    registerComponent(Vue);
  }
}
