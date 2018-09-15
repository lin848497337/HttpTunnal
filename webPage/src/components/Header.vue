<template>
  <header id="header">
    <!--header starts -->
    <div id="header-links">
      <div class="container">
        <div class="one-half">
          <ul class="contact-links">
            <!-- header social links starts-->
            <li><a href="#"><i class="icon-home"></i>{{homeText}}</a></li>
            <li><a href="#"><i class="icon-phone"></i>{{phoneNumber}}</a></li>
            <li><a href="#"><i class="icon-map-marker"></i>{{email}}</a></li>
          </ul>
        </div>
        <div class="one-half">
          <div class="pull-right">
            <ul class="contact-links">
              <!-- header social links starts-->
              <li><router-link to="/register" class="tooltip " title="Some Title"><i class="icon-pencil"></i>register</router-link></li>
              <li><router-link to="/login" class="tooltip " title="Some Title"><i class="icon-user"></i>login</router-link></li>
            </ul>
          </div>
        </div>
    </div>
    </div>
    <div class="container">
      <div class="head-wrapp">
        <a href="index.html" id="logo"><img src="../../static/images/logo.png" alt=""/><span>Simple Game</span></a>
        <!--your logo-->
        <nav class="top-search">
          <!-- search starts-->
          <form action="404-error.html" method="get">
            <button class="search-btn"></button>
            <input class="search-field" type="text" onblur="if(this.value=='')this.value='Search';" onfocus="if(this.value=='Search')this.value='';" value="Search"/>
          </form>
        </nav>
        <!--search ends -->
      </div>
    </div>
    <div id="main-navigation">
      <!--main navigation wrapper starts -->
      <div class="container">
        <ul class="main-menu">
          <li v-for="menuItem in menu">
            <router-link :to="menuItem.link">{{menuItem.title}}</router-link>
            <ul v-if="menuItem.submenu && menuItem.submenu.length > 0">
              <li v-for="subMenuItem in menuItem.submenu"><router-link :to="subMenuItem.link">{{subMenuItem.title}}</router-link> </li>
            </ul>
          </li>
        </ul>
        <!-- main navigation ends-->
        <div class="after-nav-info">
          <h4>contact phone: {{phoneNumber}}</h4>
        </div>
      </div>
    </div>
    <!--main navigation wrapper ends -->
  </header>
</template>

<script>
  import '../../static/js/jquery.min.js'
  import '../../static/js/jquery-ui.js'
  import '../../static/js/flex-slider.min.js'
  import '../../static/js/navigation.min.js'
  import '../../static/js/jquery.layerslider.js'
  import '../../static/js/layerslider.transitions.js'
  import '../../static/js/carousel.js'
  import '../../static/js/jquery.theme.plugins.min.js'
  import '../../static/js/jquery.themepunch.revolution.min.js'
  import '../../static/js/flickr.js'
  import '../../static/js/instagram.js'
  import '../../static/js/jquery.twitter.js'
  import '../../static/js/prettyPhoto.min.js'
  import '../../static/js/jquery.tooltips.min.js'
  import '../../static/js/isotope.min.js'
  import '../../static/js/scrolltopcontrol.js'
  import '../../static/js/jquery.easy-pie-chart.js'
  import '../../static/js/jquery.transit.min.js'
    export default {
        name: "c-header",
        mounted(){
          /*----------------------------------------------------------*/
          /*              9 - RESPONSIVE NAVIGATION                   */
          /*----------------------------------------------------------*/
          var $menu_select = $("<select />");
          $("<option />", {
            "selected": "selected",
            "value": "",
            "text": "Main Navigation"
          }).appendTo($menu_select);
          $menu_select.appendTo("#main-navigation");
          $("#main-navigation ul li a").each(function () {
            var menu_url = $(this).attr("href");
            var menu_text = $(this).text();
            if ($(this).parents("li").length == 2) {
              menu_text = '- ' + menu_text;
            }
            if ($(this).parents("li").length == 3) {
              menu_text = "-- " + menu_text;
            }
            if ($(this).parents("li").length > 3) {
              menu_text = "--- " + menu_text;
            }
            $("<option />", {
              "value": menu_url,
              "text": menu_text
            }).appendTo($menu_select)
          })
          var field_id = "#main-navigation select";
          $(field_id).change(function () {
            value = $(this).attr('value');
            window.location = value;
          });
          /*----------------------------------------------------------*/
          /*              10 - MAIN NAVIGATION JS                     */
          /*----------------------------------------------------------*/
          $('ul.main-menu').superfish({
            delay: 100, // one second delay on mouseout
            animation: {
              opacity: 'show',
              height: 'show'
            }, // fade-in and slide-down animation
            speed: '1000', // faster animation speed
            autoArrows: false // disable generation of arrow mark-up
          });
          /*----------------------------------------------------------*/
        },
        data(){
          var menu = [
            {title:"Home", link:"/" , submenu:[
                // {title:"SlideTest", link:"#"}
              ]},
            {title:"AboutUs", link:"/about-us" , submenu:[

              ]},
            {title:"Production", link:"" , submenu:[

              ]},
            {title:"Contact", link:""}
          ]
          return {
            menu: menu,
            homeText: "5 Avenue, Some City ",
            phoneNumber: "+987 654 321 ",
            email:"848497337@qq.com"
          }
        }
    }
</script>

<style scoped>

</style>
