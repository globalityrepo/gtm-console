(function () {
    'use strict';

    stockModule.factory('AutenticacaoService', AutenticacaoService);

    AutenticacaoService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout'];
    function AutenticacaoService($http, $cookieStore, $rootScope, $timeout) {
        var service = {};

        service.login = login;
        service.SetCredentials = SetCredentials;
        service.ClearCredentials = ClearCredentials;

        return service;
        
        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        }
    
        function login(username, password, callback) {
        	var response;
        	$http.post('/gtm-console-web-api/login', { codigo: username, senha: Base64.encode(password) } )
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function SetCredentials(user) {
            var authdata = Base64.encode(user.codigo + ':' + user.senha);

            $rootScope.globals = {
                currentUser: {
                    user: user,
                    authdata: authdata
                }
            };
            $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata; // jshint ignore:line
            $cookieStore.put('globals', $rootScope.globals);
        }

        function ClearCredentials() {
            $rootScope.globals = {};
            $cookieStore.remove('globals');
            $http.defaults.headers.common.Authorization = 'Basic';
        }
    }

})();