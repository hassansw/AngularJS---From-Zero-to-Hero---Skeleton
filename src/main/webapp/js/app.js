'use strict';


// Declare app level module which depends on filters, and services
angular.module('devcampBlog', [
  'ngRoute',
  'devcampBlog.filters',
  'devcampBlog.services',
  'devcampBlog.directives',
  'devcampBlog.controllers',
  'textAngular'
]).
config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {templateUrl: 'partials/main.html', controller: 'MainController'});
    $routeProvider.when('/register', {templateUrl: 'partials/register.html', controller: 'RegisterController'});
    
    $routeProvider.when('/backend', {templateUrl: 'partials/backend.html', controller: 'BackendController'});
    $routeProvider.when('/newPost', {templateUrl: 'partials/postDetail.html', controller: 'PostController'});
    $routeProvider.when('/editPost/:year/:month/:postIndex', {templateUrl: 'partials/postDetail.html', controller: 'PostController'});
    $routeProvider.when('/detail/:year/:month/:postIndex', {templateUrl: 'partials/singlePost.html', controller: 'PostController'});

   
    $routeProvider.otherwise({redirectTo: '/'});
}])

.config(['$httpProvider', function ($httpProvider) {
	 $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);