(function () {
    'use strict';

    stockModule.factory('RESTfulResourceService', RESTfulResourceService);

    RESTfulResourceService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout'];
    function RESTfulResourceService($http, $cookieStore, $rootScope, $timeout) {
        var service = {};

        service.buscar = buscar;
        service.listar = listar;
        service.incluir = incluir;
        service.alterar = alterar;
        service.excluir = excluir;

        return service;
        
        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        }

        function buscar(entity, pageSize, currentPage, filtro, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/restfulresource/' + entity  + '/' + pageSize + '/' + currentPage + '/' + filtro )
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function listar(entity, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/restfulresource/' + entity)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function incluir(entity, item, callback) {
        	var response;
        	$http.post('/gtm-console-web-api/restfulresource/' + entity, item )
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function alterar(entity, item, callback) {
        	var response;
        	$http.put('/gtm-console-web-api/restfulresource/' + entity, item )
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function excluir(entity, item, callback) {
        	var response;
        	$http.delete('/gtm-console-web-api/restfulresource/' + entity  + '/' + item.id)
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