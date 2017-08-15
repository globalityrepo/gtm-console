(function () {
    'use strict';

    stockModule.controller('LoginController', LoginController);

    LoginController.$inject = ['$scope', '$location', 'AuthenticationService', 'FlashService'];
    function LoginController($scope, $location, AuthenticationService, FlashService) {
        
    	$scope.login = login;
    	
        (function initController() {
            // reset login status
        	AuthenticationService.ClearCredentials();
        })();

        function login() {
            $scope.dataLoading = true;
            AuthenticationService.Login($scope.username, $scope.password, function (response) {
                if (response.success) {
                	AuthenticationService.SetCredentials(response.data);
                	$location.path('/divisao');
                } else {
                	FlashService.Error(response.data.message);
                	$scope.dataLoading = false;
                }
            });
        };
    }

})();