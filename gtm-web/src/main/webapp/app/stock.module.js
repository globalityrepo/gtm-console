var stockModule = angular.module('stockModule', [
    'ui.router',
    'ui.bootstrap',
    'ngResource',
    'ngRoute',
	'ngCookies'
]);

stockModule.
	config(function ($stateProvider, $urlRouterProvider) {

        // For any unmatched URL redirect to dashboard URL
        $urlRouterProvider.otherwise("/login");

        $stateProvider
            // Known items
	        .state('login', {
	            url: "/login",
	            views: {
	                'main': {
	                    templateUrl: "app/pages/login/login.view.html",
	                    controller: "LoginController as loginCtrl"
	                }
	            },
	        })
        	.state('divisao', {
                url: "/divisao",
                views: {
                    'main': {
                        templateUrl: "app/pages/divisao/divisao.view.html",
                        controller: "DivisaoController as divisaoCtrl"
                    }
                },
            });

    });

stockModule.
	run(function ($rootScope, $location, $cookieStore, $http) {
	    // keep user logged in after page refresh
	    $rootScope.globals = $cookieStore.get('globals') || {};
	    if ($rootScope.globals.currentUser) {
	        $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
	    }
	
	    $rootScope.$on('$locationChangeStart', function (event, next, current) {
	        // redirect to login page if not logged in and trying to access a restricted page
	        var restrictedPage = $.inArray($location.path(), ['/login']) === -1;
	        var loggedIn = $rootScope.globals.currentUser;
	        if (restrictedPage && !loggedIn) {
	            $location.path('/login');
	        }
	    });
    });

