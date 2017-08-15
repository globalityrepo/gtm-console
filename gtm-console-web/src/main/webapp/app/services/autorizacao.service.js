(function () {
    'use strict';

    stockModule.factory('AutorizacaoService', AutorizacaoService);

    AutorizacaoService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout'];
    function AutorizacaoService($http, $cookieStore, $rootScope, $timeout) {
        var service = {};

        service.listarPermissoesAcessoPerfil = listarPermissoesAcessoPerfil;
        service.listarPermissoesAcessoRecurso = listarPermissoesAcessoRecurso;

        return service;
        
        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        }
            
        function listarPermissoesAcessoPerfil(idPerfil, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/autorizacao/perfil/' + idPerfil)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function listarPermissoesAcessoRecurso(idPerfil, codRecurso, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/autorizacao/perfil/' + idPerfil + '/recurso/' + codRecurso)
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