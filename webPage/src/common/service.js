import '../../static/js/jquery.min.js'


function doHttpGet(url, param, callback) {
  jQuery.ajax({
    url: url,
    async: false,
    data: param,
    dataType: "json",
    method:"GET",
    error: function(err){
      alert("ajax error "+err);
    },
    success: function(result){
      if (typeof callback == 'function'){
        callback.call(callback, result);
      }
    }
  })
}

function doHttpPost(url, param, callback) {
  jQuery.ajax({
    url: url,
    async: false,
    data: param,
    dataType: "json",
    method:"POST",
    error: function(err){
      alert("ajax error "+err);
    },
    success: function(result){
      if (typeof callback == 'function'){
        callback.call(callback, result);
      }
    }
  })
}

export default {
  name:"ajax",
  httpPost(url, param, callback){
    doHttpPost(url, param, callback)
  },
  httpGet(url, param, callback){
    doHttpGet(url, param, callback)
  }
}
