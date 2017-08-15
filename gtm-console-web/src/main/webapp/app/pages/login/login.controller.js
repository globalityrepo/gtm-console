(function () {
    'use strict';

    stockModule.controller('LoginController', LoginController);

    LoginController.$inject = ['$scope', '$rootScope', '$location', 'AutenticacaoService', 'notificationsFactory'];
    function LoginController($scope, $rootScope, $location, AutenticacaoService, notificationsFactory) {
        
    	$scope.login = login;
    	$rootScope.aplicarBackground = false;
    	
        (function initController() {
            // reset login status
        	AutenticacaoService.ClearCredentials();
        })();

        function login() {
            $scope.dataLoading = true;
            AutenticacaoService.login($scope.username, $scope.password, function (response) {
                if (response.success) {
                	AutenticacaoService.SetCredentials(response.data);
                	$location.path('/home');
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);  
                }
            });
        };
        
    }

})();