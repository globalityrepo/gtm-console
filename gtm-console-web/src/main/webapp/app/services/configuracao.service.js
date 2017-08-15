(function () {
    'use strict';

    stockModule.factory('ConfiguracaoService', ConfiguracaoService);

    ConfiguracaoService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout'];
    function ConfiguracaoService($http, $cookieStore, $rootScope, $timeout) {
        var service = {};

        service.buscar = buscar;
        service.buscarNonAuth = buscarNonAuth;
        service.alterar = alterar;
        
        return service;
        
        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        }
        
        // Busca sem passagem de id porque a tabela só possui um registro.
        function buscar(callback) {
        	var response;
        	$http.get('/gtm-console-web-api/configuracao')
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        // Busca sem passagem de id porque a tabela só possui um registro.
        // Este serviço é chamado sem a necessidade de autenticação.
        function buscarNonAuth(callback) {
        	var response;
        	$http.get('/gtm-console-web-api/configuracao/nonauth/')
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function alterar(item, callback) {
        	var response;
        	$http.put('/gtm-console-web-api/configuracao', item )
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