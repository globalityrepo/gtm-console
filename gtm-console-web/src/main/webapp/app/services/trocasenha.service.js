(function () {
    'use strict';

    stockModule.factory('TrocaSenhaService', TrocaSenhaService);

    TrocaSenhaService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout'];
    function TrocaSenhaService($http, $cookieStore, $rootScope, $timeout) {
        var service = {};

        service.executar = executar;
        
        return service;
        
        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        }
    
        function executar(login, senhaAtual, senhaNova, callback) {
        	var response;
        	var encodeString = "login=" + login + "&senhaAtual=" + Base64.encode(senhaAtual) + "&senhaNova=" + Base64.encode(senhaNova);
            $http({
                method: "POST",
                url: "/gtm-console-web-api/trocasenha",
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