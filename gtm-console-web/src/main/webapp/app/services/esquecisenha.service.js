(function () {
    'use strict';

    stockModule.factory('EsqueciSenhaService', EsqueciSenhaService);

    EsqueciSenhaService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout'];
    function EsqueciSenhaService($http, $cookieStore, $rootScope, $timeout) {
        var service = {};

        service.executar = executar;
        
        return service;
        
        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        }
    
        function executar(login, callback) {
        	var response;
        	var encodeString = "login=" + login;
            $http({
                method: "POST",
                url: "/gtm-console-web-api/esquecisenha",
                data: encodeString,
                headers: {'Content-Type': "application/x-www-form-urlencoded"}
            })
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        
    }
    
})();