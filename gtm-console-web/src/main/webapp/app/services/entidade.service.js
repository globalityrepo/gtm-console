(function () {
    'use strict';

    stockModule.factory('EntidadeService', EntidadeService);

    EntidadeService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout', '$window'];
    function EntidadeService($http, $cookieStore, $rootScope, $timeout, $window) {
        
    	var service = {};
        
    	service.buscar = buscar;
        service.incluir = incluir;
        service.alterar = alterar;
        service.excluir = excluir;
        service.upload = upload;
        service.buscarEntidadeAplicacoesByEntidade = buscarEntidadeAplicacoesByEntidade;
        service.buscarAllEntidadeAplicacoesByEntidade = buscarAllEntidadeAplicacoesByEntidade;
        service.buscarRegistrosByEntidadeAplicacao = buscarRegistrosByEntidadeAplicacao;
        service.checkRuleExclusaoRegistro = checkRuleExclusaoRegistro;
        service.checkRuleExclusaoEntidadeAplicacao = checkRuleExclusaoEntidadeAplicacao;
        
        return service;
        
        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        }
        
        function buscar(pageSize, currentPage, idUsuario, filtro, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/entidade/' + pageSize + '/' + currentPage + '/' + idUsuario + '/' + filtro )
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
        	$http.post('/gtm-console-web-api/entidade/', item )
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
        	$http.put('/gtm-console-web-api/entidade/', item )
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function excluir(item, idUsuario, callback) {
        	var response;
        	$http.delete('/gtm-console-web-api/entidade/' + item.id + '/' + idUsuario)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function upload(formdata, callback) {
        	var configContent = {
        		transformRequest: angular.identity,
                headers : {
                    'Content-Type': undefined
                }
            }
        	var response;
            $http.post('/gtm-console-web-api/entidade/upload/', formdata, configContent)
            .success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function buscarEntidadeAplicacoesByEntidade(idEntidade, idUsuario, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/entidade/aplicacoes/' + idEntidade + '/' + idUsuario)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function buscarAllEntidadeAplicacoesByEntidade(idEntidade, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/entidade/aplicacoes/' + idEntidade)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function buscarRegistrosByEntidadeAplicacao(idEntidadeAplicacao, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/entidade/aplicacao/registros/' + idEntidadeAplicacao)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function checkRuleExclusaoRegistro(id, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/entidade/checkrule/delete/registro/' + id)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function checkRuleExclusaoEntidadeAplicacao(id, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/entidade/checkrule/delete/entidadeaplicacao/' + id)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function checkRuleExclusaoEntidade(id, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/entidade/checkrule/delete/' + id)
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