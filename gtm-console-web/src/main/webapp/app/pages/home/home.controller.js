(function () {
    'use strict';

    stockModule.controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$rootScope', '$location', 'notificationsFactory'];
    function HomeController($scope, $rootScope, $location, notificationsFactory) {
       
    	$rootScope.aplicarBackground = true;
    	
    }

})();