'use strict';

/* Directives */


angular.module('devcampBlog.directives', []).
  directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
      elm.text(version);
    };
  }])
  
.directive('toggleLogin', function() {
    return function(scope, elem, attr) {
      
        elem.click(function (e) {
           e.preventDefault();
           elem.next().toggle();
       });
    };
});