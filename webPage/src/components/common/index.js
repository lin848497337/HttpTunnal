import Slider from "./Slider"

function registerComponent(Vue){
  Vue.component("slider", Slider);
}

export default{
  register(Vue){
    registerComponent(Vue);
  }
}
