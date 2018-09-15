<template>
<div>
  <footer id="footer"><!--footer starts -->
    <section class="container">
      <section class="one-third">
        <h4>Twitter Feeds</h4>
        <ul class="twitter-feeds">
          <!-- twitter widget starts-->
        </ul>
        <!-- twitter widget ends-->
        <p>
          <a href="#">Follow Me on Twitter</a>
        </p>
      </section>
      <section class="one-third">
        <h4>Latest Posts</h4>
        <ul>
          <!-- latest posts widget starts-->
          <li><a href="blog-post.html">Free Responsive HTML5 Template.</a></li>
          <li><a href="blog-post.html">PSD Widget UI Free Download</a></li>
          <li><a href="blog-post.html">Free Business Bootsrap Template</a></li>
          <li><a href="blog-post.html">Custom WordPress Themes</a></li>
        </ul>
        <!-- lastest posts widget ends-->
      </section>
      <section class="one-third">
        <h4>Flickr Photos</h4>
        <div class="flickr-widget">
          <!-- flickr widget starts-->
        </div>
      </section>
    </section>
  </footer>
  <section id="copyrights">
    <section class="container">
      <div class="one-half">
        <p>
          Copyright &copy; 2014.Company name All rights reserved.More Templates <a href="http://www.cssmoban.com/" target="_blank" title="模板之家">模板之家</a> - Collect from <a href="http://www.cssmoban.com/" title="网页模板" target="_blank">网页模板</a>
        </p>
      </div>
      <div class="one-half">
        <ul class="copyright_links">
          <li><a href="#" title="Home">Home</a></li>
          <li><a href="#" title="About">About us</a></li>
          <li><a href="#" title="Blog">Blog</a></li>
          <li><a href="#" title="Contact">Contact</a></li>
        </ul>
      </div>
    </section>
  </section>
</div>
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
        name: "c-footer",
        mounted(){
          /*----------------------------------------------------------*/
          /*              6 - STICKY NAVIGATION                       */
          /*----------------------------------------------------------*/
          var aboveHeight = $('#copyrights').outerHeight();
          var _$ = $;
          $(window).scroll(function () {
            $ = _$;
            if ($(window).scrollTop() > aboveHeight && $(window).width() > 959) {
              $('#main-navigation.fixed').addClass('sticky');
              $('body').addClass('empty');
            } else {
              $('#main-navigation.fixed').removeClass('sticky');
              $('body').removeClass('empty');
            }
          });
        }
    }
</script>

<style scoped>

</style>
