<template>
  <div id="app">
    <div id="container">
      <!-- main container starts-->
      <div id="wrapp">
        <!-- main wrapp starts-->
        <c-header ></c-header>
        <router-view/>
        <c-footer></c-footer>
      </div>
      <!-- main wrapp starts-->
    </div>
    <!-- main container ends-->
  </div>
</template>

<script>
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


export default {
  name: 'App',
  mounted(){
    /*----------------------------------------------------------*/
    /*                   TABLE OF CONTENT                        */
    /*----------------------------------------------------------*/
    /*
    1 - CLIENTS IMG HOVER EFFECT
    2 - SHOP JQUERY UI SLIDER
    3 - REVOLUTION SLIDER
    4 - TEAM MEMBER CHART JQUERY
    5 - NOTIFICATIONS BLOCK JQUERY
    6 - STICKY NAVIGATION
    7 - TWITTER PLUGIN JQUERY
    8 - PORTFOLIO ISOTOPE JQUERY
    9 - RESPONSIVE NAVIGATION
    10 - MAIN NAVIGATION JS
    11 - LAYERSLIDER JS
    12 - FLICKR PHOTOS
    13 - INSTAGRAM PHOTOS
    14 - SKILLS BAR ANIMATION
    15 - FLEX SLIDERS
    16 - PRETTYPHOTO
    17 - GOOGLE MAPS
    18 - JQUERY ACCORDION
    19 - JQUERY TABS
    20 - JQUERY CAROUSEL
    21 - CONTACT FORM VALIDATION
    */
    jQuery.noConflict()(function ($) {
      $(document).ready(function ($) {
        /*----------------------------------------------------------*/
        /*              1 - CLIENTS IMAGE HOVER EFFECT                */
        /*----------------------------------------------------------*/
        $('ul.client-block li a img').hover(function () {
          $(this).transition({
            scale: 1.12
          });
        }, function () {
          $(this).transition({
            scale: 1
          });
        });


        /*----------------------------------------------------------*/
        /*              7 - TWITTER PLUGIN JQUERY                 */
        /*----------------------------------------------------------*/






        /*----------------------------------------------------------*/
        /*              21 - CONTACT FORM VALIDATION                */
        /*----------------------------------------------------------*/
        if ($('form#contact-form').length && jQuery()) {
          $('form#contact-form').submit(function () {
            function resetForm($form) {
              $form.find('input:text, input:password, input:file, select, textarea').val('');
              $form.find('input:radio, input:checkbox')
                .removeAttr('checked').removeAttr('selected');
            }
            $('form#contact-form .error').remove();
            var hasError = false;
            $('.requiredField').each(function () {
              if (jQuery.trim($(this).val()) == '') {
                var formInput = $(this).attr("title");
                $("#error-field").append('<div class="notification error"><p><i class="icon-warning-sign"></i>Please Enter ' +
                  formInput + '</p></div> ');
                $(this).addClass('inputError');
                hasError = true;
              } else if ($(this).hasClass('email')) {
                var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
                if (!emailReg.test(jQuery.trim($(this).val()))) {
                  var formInput = $(this).attr("title");
                  $("#error-field").append(
                    '<div class="notification error"><p><i class="icon-warning-sign"></i>You entered an invalid Value - ' +
                    formInput + '</p></div>');
                  $(this).addClass('inputError');
                  hasError = true;
                }
              }
            });
            if (!hasError) {
              $('form#contact-form input.submit').fadeOut('normal', function () {});
              var formInput = $(this).serialize();
              $.post($(this).attr('action'), formInput, function (data) {
                $('#contact-form').prepend(
                  '<div class="notification success"><p> <i class="icon-check-sign"></i>Your email was successfully sent. We will contact you as soon as possible.</p></div>'
                );
                resetForm($('#contact-form'));
              });
            }
            return false;
          });
        }
      });
    });

  }
}
</script>

<style>
</style>
