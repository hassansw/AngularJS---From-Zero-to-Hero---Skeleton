'use strict';

/* Controllers */

angular.module('devcampBlog.controllers', []);


function RegisterController($scope, $location, Blogger) {
    console.log("RegisterController");
    $scope.errormessages = "";


    $scope.register = function(regForm) {
           console.log("register");
        if (regForm.$valid) {

            var jsonData = JSON.stringify($scope.user);

            console.log(jsonData);

            Blogger.register(jsonData).then(function(response) {

                if (response.status === 201) {
                    console.log("success");
                    $location.path("/");
                } else {
                    console.log("error");
                    $scope.errormessages = "Fehler aufgetreten";
                }


            });


        }// isValid
    };
}

function MainController($scope, Post, dataFactory) {
    console.log("Main");
    
    Post.query().then(function(response) {

        if (response.status === 200) {
            $scope.postings = response.data.posts;
            $scope.pageSize = new Array(response.data.pageCount);
            $scope.currentPage = response.data.page;

            dataFactory.setDataList("posts", $scope.postings);

        } else {
            console.log("error");
        }
    });


}


function SidebarController($scope, $location, dataFactory, Blogger) {
    console.log("SidebarController");

    console.log("loggedin " + $scope.isLoggedIn);

    var loc = window.location, ws_uri;

    if (loc.protocol === "https:") {
        ws_uri = "wss:";
    } else {
        ws_uri = "ws:";
    }

    ws_uri += "//" + loc.host;
    ws_uri += loc.pathname + "websocket";

    var ws = new WebSocket(ws_uri);
    ws.onopen = function(){  
        console.log("Socket has been opened!");  
    };
    
    $scope.newCounter = 0;
    ws.onmessage = function(message) {
        console.log(message);
           $scope.$apply($scope.newCounter++);
        
    };
    
    $scope.user = {};
    

    $scope.switchform = function() {
        $scope.isLoggedIn = !$scope.isLoggedIn;
    };

    if ($scope.isLoggedIn === undefined) {
        $scope.isLoggedIn = false;
    }

    $scope.$on('handleBroadcast', function() {
        $scope.recents = dataFactory.getDataList().posts;
    });

    $scope.login = function(login) {
        $scope.submitted = true;

        if (login.$valid) {

            $scope.isLoading = true;

            var jsonData = {
                "email": $scope.user.email,
                "password": $scope.user.password
            };

            console.log(jsonData);

            Blogger.login(jsonData).then(function(response) {
                console.log(response);
                if (response.status === 200) {

                    $scope.isLoggedIn = true;
                    $location.path("/backend");
                } else {
                    console.log("error");
                }


            });
        }
    };

    $scope.logout = function() {
        $scope.isLoggedIn = false;
        dataFactory.clearDataList();
        $location.path("/main");
    };
}


function BackendController($scope, $location, Post, dataFactory) {
    console.log("BackendController");

    loadAllPosts();

    function loadAllPosts() {

        Post.query().then(function(response) {

            if (response.status === 200) {

                $scope.postings = response.data.posts;

                dataFactory.setDataList("posts", $scope.postings);
            } else {
                console.log("error");
            }
        });

    }

    $scope.deletePost = function(post) {

        // var index=$scope.options.indexOf(option);
        //   $scope.options.splice(index,1); 

        Post.del(post.url).then(function(response) {
            console.log("delete");
            console.log(response);

            if (response.status === 204) {
                console.log("success" + response);
                var index=$scope.postings.indexOf(post);
                $scope.postings.splice(index,1); 
            } else {
                console.log("error");
            }
        });
    };

}
function PostController($scope, $location, $routeParams, Post, Image, dataFactory) {
    console.log("PostController " + $location.path());

    var editable = false;


    if ($location.path().indexOf("/editPost") !== -1 || $location.path().indexOf("/detail") !== -1) {
        loadPostDetails();
        console.log("editable");
        editable = true;
        $scope.editImage = false;
    } else {
        $scope.editImage = true;
        // NEu Mockdaten
        $scope.post = {};
    }


    $scope.postIt = function(postForm) {
        $scope.submitted = true;

        if (postForm.$valid) {

            $scope.isLoading = true;

            if (editable === true) {

                if ($scope.file !== undefined) {

                    Image.add($scope.file).then(function(response) {

                        if (response.status === 201) {
                            console.log("success");
                            console.log("save " + $scope.post.url);

                            var jsonData = {
                                "title": $scope.post.title,
                                "subtitle": $scope.post.subtitle,
                                "imageURI": response.headers.location,
                                "content": $scope.post.content
                            };

                            jsonData = JSON.stringify(jsonData);
                            saveEditedPost(jsonData, $scope.post.url);

                        } else {
                            return;
                        }

                    });

                } else {

                    var jsonData = {
                        "title": $scope.post.title,
                        "subtitle": $scope.post.subtitle,
                        "content": $scope.post.content
                    };

                    jsonData = JSON.stringify(jsonData);
                    saveEditedPost(jsonData, $scope.post.url);

                }


            } else {

                // Save New Post

                if ($scope.file !== undefined) {

                    Image.add($scope.file).then(function(response) {

                        if (response.status === 201) {
                            console.log("success");
                            console.log("save " + $scope.post.url);

                            var jsonData = {
                                "title": $scope.post.title,
                                "subtitle": $scope.post.subtitle,
                                "imageURI": response.headers.location,
                                "content": $scope.post.content
                            };

                            jsonData = JSON.stringify(jsonData);

                            savePost(jsonData);
                        } else {
                            return;
                        }

                    });



                } else {

                    var jsonData = {
                        "title": $scope.post.title,
                        "subtitle": $scope.post.subtitle,
                        "content": $scope.post.content
                    };
                    savePost(jsonData);
                }
            }

        }
    };

    $scope.toggleEditImage = function() {
        console.log("toggle");
        $scope.editImage = !$scope.editImage;
        console.log($scope.editImage);
    };

    function savePost(data) {
        Post.add(data).then(function(response) {

            if (response.status === 201) {

                $location.path("/backend");
            } else {
                console.log("error");
            }
        });
    }

    function saveEditedPost(data, _url) {

        Post.save(data, _url).then(function(response) {

            if (response.status === 200) {

                $location.path("/backend");
            } else {
                console.log("error");
            }
        });
    }





    function loadPostDetails() {

        var postList = dataFactory.getDataList()["posts"];
        var postIndex = $routeParams.year + "/" + $routeParams.month + "/" + replaceAll(" ", "%20", $routeParams.postIndex);


        if (postList !== undefined) {
            for (var i = 0; i < postList.length; i++) {

                if (postList[i]["url"].endsWith(postIndex)) {

                        $scope.post = postList[i];
                }
            }
        }else{

            Post.get(postIndex).then(function(response) {
                $scope.post = response.data;
            });
            
            
        }
    }


    $scope.uploadFile = function(files) {

        var fd = new FormData();
        fd.append("file", files[0]);
        $scope.file = fd;
    };
}



String.prototype.endsWith = function(s) {
    return this.length >= s.length && this.substr(this.length - s.length) == s;
};

function replaceAll(find, replace, str)
{
    while (str.indexOf(find) > -1)
    {
        str = str.replace(find, replace);
    }
    return str;
}