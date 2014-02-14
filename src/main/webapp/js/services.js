'use strict';


var host = "rest/";

angular.module('devcampBlog.services', []).
  value('version', '0.1')


 .factory('Blogger', function( $http ) {
    
        var baseUrl = host + "users/";
        return {
        
        register: function(data, url) {
        
             var _url = baseUrl;
            if(url !== undefined){
              _url = baseUrl + url;
            }  
            
            var promise = $http.post(_url, data).then(function (response) {
                return {"data":response.data, "status":response.status, "headers":response.headers()};
            });
        
            return promise;
        },
        
        registerErrorSuccess : function(data){
            
            var deferred = $q.defer();
                $http.post(url, data).success(function(data, status, header, config) {
                     deferred.resolve({"data":data, "status": status, "headers": header});
            }).error(function(data, status, header, config) {
                    console.log("error");
                    deferred.resolve({"data":data, "status": status, "headers": header});
            });
            
           
             return deferred.promise;
        },
        
        login: function(data, url) {
        
            if(url === undefined){
                url = "";
            }  

             var _url = baseUrl + "login/" + url;
            
       
            
            console.log(data);
            
            // Diebstahl: http://www.webtoolkit.info/javascript-base64.html#.Uvpce_l5NN8
            console.log(data['email'] + ':' + data["password"]);
            $http.defaults.headers.common['Authorization'] = "Basic "+ Base64.encode(data['email'] + ':' + data["password"]);
            
            // $http returns a promise, which has a then function, which also returns a promise
            var promise = $http.post(_url).then(function (response) {
              // The then function here is an opportunity to modify the response
              console.log(response);
              // The return value gets picked up by the then in the controller.
               return {"data":response.data, "status":response.status, "headers":response.headers()};
            });
            // Return the promise to the controller
            return promise;
       
        }
    };
 })


.factory('Post', function( $http ) {

    var baseUrl = host + "posts/";
    
    return{
        query: function(url) {

            if(url === undefined){
                url = "";
            }  

            baseUrl+= url;
         
            var promise = $http.get(baseUrl).then(function (response) {
           
              return {"data":response.data, "status":response.status, "headers":response.headers()};;
            });
           
            return promise;
        },
        
        get: function(url) {

            if(url === undefined){
                url = "";
            }  

            baseUrl+=  url;
           
            var promise = $http.get(baseUrl).then(function (response) {

                return {"data":response.data, "status":response.status, "headers":response.headers()};
            });
          
            return promise;
        },
        
        add: function(data, url) {

            if(url === undefined){
                url = "";
            }  

            baseUrl+= url;
          
            var promise = $http.post(baseUrl, data).then(function (response) {
              
                return {"data":response.data, "status":response.status, "headers":response.headers()};
            });
           
            return promise;
        },
        save: function(data, url) {

            var promise = $http.put(url, data).then(function (response) {
              
              return {"data":response.data, "status":response.status, "headers":response.headers()};
            });
           
            return promise;
        },
        del: function(url) {

            var promise = $http.delete(url).then(function (response) {
              return {"data":response.data, "status":response.status, "headers":response.headers()};
            });
          
            return promise;
        }
    };
})



.factory('Image', function( $http ) {

    var baseUrl = host + "images/";
    
    return{
      
        get: function(url) {

            if(url === undefined){
                url = "";
            }  

            baseUrl+=  url;
           
            var promise = $http.get(baseUrl).then(function (response) {

                return {"data":response.data, "status":response.status, "headers":response.headers()};
            });
          
            return promise;
        },
        
        add: function(data, url) {

            if(url === undefined){
                url = "";
            }  

            baseUrl+= url;
          
            var promise = $http.post(baseUrl, data, {
                withCredentials: true,
                headers: {'Content-Type': undefined },
                transformRequest: angular.identity}).then(function (response) {
              
                return {"data":response.data, "status":response.status, "headers":response.headers()};
            });
           
            return promise;
        },
        save: function(data, url) {
    
            var promise = $http.put(url, data).then(function (response) {
              
              return {"data":response.data, "status":response.status, "headers":response.headers()};
            });
           
            return promise;
        },
        del: function(url) {

            var promise = $http.delete(url).then(function (response) {
              return {"data":response.data, "status":response.status, "headers":response.headers()};
            });
          
            return promise;
        }
    };
})

.factory('dataFactory', function( $rootScope ) {
    var dataList = {};
    var dataService = {};

    dataService.setDataList = function(key, data) {
        dataList[key] = data;
        $rootScope.$broadcast('handleBroadcast');
    };
    dataService.getDataList = function() {
        return dataList;
    };
     dataService.clearDataList = function() {
        dataList = {};
    };

    return dataService;
})


.service('LocalStorage', function ( $log ) {


    var localStorageAvailable = function () {
        try {
            return ('localStorage' in window && window['localStorage'] !== null);
        } catch (e) {
            return false;
        }
    };


    return {

    isSupported: localStorageAvailable()

    ,clear : function ( ) {
        if ( this.isSupported )
        {
            localStorage.clear();
            return this.getPersons();
        }
        return [];
    }
    ,setValue : function ( key , value ) {
        if ( this.isSupported )
        {
            if ( angular.isObject (value) || angular.isArray(value) )
            {
                value = angular.toJson ( value );
            }
            localStorage.setItem( key , value );
        }
    }
    ,getValue : function ( key ) {
        if ( this.isSupported )
        {
            var value = localStorage.getItem( key );
            return value;
        }
        throw new Error ("LocalStorage not Supported");
    }

               

    };
})


.factory('User', function($resource ) {
    
    var url = "http://localhost:8080/devcampblog/rest/users/:id";
    
    var User = $resource( url, 
                        {id:"@id"},
                        {
                         query : { method: "GET"},
                         save : { method: "POST"},
                         edit : { method: "PUT"}
                        }
    );
    
    return User;
})


