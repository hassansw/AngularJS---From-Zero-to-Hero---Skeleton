'use strict';

/* Filters */
angular.module('devcampBlog.filters', []).
  filter('interpolate', ['version', function(version) {
    return function(text) {
      return String(text).replace(/\%VERSION\%/mg, version);
    };
  }])
  
  .filter('urlEnding', function() {
  return function(input) {

    if(input !== undefined && input !== null){
        var index = input.lastIndexOf("/");
        return input.substr(index);
    }else{
        return input;   
        
    }
  };
})
.filter('postDetailUrl', function() {
  return function(input) {

    if(input !== undefined && input !== null){
        var index = input.lastIndexOf("posts/");
        return input.substr(index + 5 );
    }else{
        return input;   
        
    }
  };
});



