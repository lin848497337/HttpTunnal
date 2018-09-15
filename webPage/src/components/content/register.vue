<template>
  <div id="content">
    <div id="breadcrumb">
      <!-- breadcrumb starts-->
      <div class="container">
        <div class="one-half">
          <h4>Login</h4>
        </div>
        <div class="one-half">
          <nav id="breadcrumbs">
            <!--breadcrumb nav starts-->
            <ul>
              <li>You are here:</li>
              <li><a href="index.html">Home</a></li>
              <li>Register</li>
            </ul>
          </nav>
          <!--breadcrumb nav ends -->
        </div>
      </div>
    </div>
    <!--breadcrumbs ends -->
    <div class="container">
        <div class="center">
          <div class="inline-block">
            <div class="input-group">
              <label class="input-label" for="username">用户名：</label>
              <input  class="input-text" id="username" type="text" v-model="userModel.username" name="username" />
            </div>
            <div class="input-group">
              <label class="input-label" for="password">密码：</label>
              <input class="input-text" id="password" type="password" v-model="userModel.password" name="password" />
            </div>
            <div class="input-group">
              <a href="#" class="button color small round no-margin" @click="login">Register</a>
            </div>
          </div>

        </div>


    </div>
  </div>
</template>

<script>
  import '../../../static/js/jquery.min.js'
  import '../../../static/js/jquery-ui.js'
  import '../../../static/js/flex-slider.min.js'
  import '../../../static/js/navigation.min.js'
  import '../../../static/js/jquery.layerslider.js'
  import '../../../static/js/layerslider.transitions.js'
  import '../../../static/js/carousel.js'
  import '../../../static/js/jquery.theme.plugins.min.js'
  import '../../../static/js/jquery.themepunch.revolution.min.js'
  import '../../../static/js/flickr.js'
  import '../../../static/js/instagram.js'
  import '../../../static/js/jquery.twitter.js'
  import '../../../static/js/prettyPhoto.min.js'
  import '../../../static/js/jquery.tooltips.min.js'
  import '../../../static/js/isotope.min.js'
  import '../../../static/js/scrolltopcontrol.js'
  import '../../../static/js/jquery.easy-pie-chart.js'
  import '../../../static/js/jquery.transit.min.js'
  import userService from "../../ajax/userService.js"

    export default {
        name: "register",
        methods:{
          login:function(){
            var userModel = this.userModel;
            userService.doRegister(userModel.username, userModel.password, function(ret){
                alert(ret.message);
            })
          }
        },
        data(){
          var userModel = {username: "", password: "", remeberMe:false};
          return {
            userModel: userModel
          }
        }
    }
</script>

<style scoped>

form{
  display: inline-block;
}

.inline-block{
  display: inline-block;
}

.center{
  text-align: center;
}
.input-group{
  text-align: right;
  vertical-align: middle;
  clear: both;
}
.input-label{
  float: left;
  width: 80px;
  height: 32px;
  position: relative;
}
.input-text{
  float: left;
  height: 10px;
  position: relative;
}
</style>
