describe('RegisterController', function() {
    var $scope, $location, $rootScope, createController;


    beforeEach(module('devcampBlog')); 
    
    beforeEach(inject(function($injector) {
        $location = $injector.get('$location');
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();

        var $controller = $injector.get('$controller');

        createController = function() {
            return $controller('RegisterController', {
                '$scope': $scope
            });
        };
    }));

    it('should have a method to check if the path is active', function() {
        var controller = createController();
        $location.path('/register');
        expect($location.path()).toBe('/register');
       
    });
});