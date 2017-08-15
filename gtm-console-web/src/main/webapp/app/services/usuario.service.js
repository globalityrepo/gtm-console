(function () {
    'use strict';

    stockModule.factory('UsuarioService', UsuarioService);

    UsuarioService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout'];
    function UsuarioService($http, $cookieStore, $rootScope, $timeout) {
        var service = {};

        service.buscar = buscar;
        service.listar = listar;
        service.incluir = incluir;
        service.alterar = alterar;
        service.excluir = excluir;
        service.listarGruposAdicionados = listarGruposAdicionados;
        service.listarGruposPendentes = listarGruposPendentes;
        service.isUsuarioAdmCrossReference = isUsuarioAdmCrossReference;

        return service;
        
        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        }

        function buscar(pageSize, currentPage, filtro, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/usuario/' + pageSize + '/' + currentPage + '/' + filtro )
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function listar(callback) {
        	var response;
        	$http.get('/gtm-console-web-api/usuario')
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function incluir(item, callback) {
        	var response;
        	$http.post('/gtm-console-web-api/usuario', item )
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
        	$http.put('/gtm-console-web-api/usuario', item )
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function excluir(item, callback) {
        	var response;
        	$http.delete('/gtm-console-web-api/usuario/' + item.id)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
                
        function listarGruposAdicionados(id, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/usuario/grupo/adicionado/' + id)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function listarGruposPendentes(id, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/usuario/grupo/pendente/' + id)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function isUsuarioAdmCrossReference(id, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/usuario/checkrole/admcrossreference/' + id)
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