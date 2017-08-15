(function () {
    'use strict';

    stockModule.factory('DeParaService', DeParaService);

    DeParaService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout', '$window'];
    function DeParaService($http, $cookieStore, $rootScope, $timeout, $window) {
        var service = {};
        
        service.buscarEntidadeAplicacoesB = buscarEntidadeAplicacoesB;
        service.buscarMassaDeDadosAplicacaoA = buscarMassaDeDadosAplicacaoA;
        service.buscarMassaDeDadosAplicacaoB = buscarMassaDeDadosAplicacaoB;
        service.buscar = buscar;
        service.incluir = incluir;
        service.alterar = alterar;
        service.excluir = excluir;
        service.checkRuleAssociacaoEntidadeAplicacao = checkRuleAssociacaoEntidadeAplicacao;
        service.upload = upload;
        service.buscarRegstrosDePara = buscarRegstrosDePara;
        
        return service;
        
        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        }
        
        function buscar(pageSize, currentPage, idUsuario, filtro, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/depara/' + pageSize + '/' + currentPage + '/' + idUsuario + '/' + filtro )
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function buscarEntidadeAplicacoesB(idEntidade, idEntidadeAplicacaoA, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/depara/aplicacoes/' + idEntidade + '/exceto/' + idEntidadeAplicacaoA)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function buscarMassaDeDadosAplicacaoA(idEntidadeAplicacao, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/depara/aplicacao/a/registros/' + idEntidadeAplicacao)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function buscarMassaDeDadosAplicacaoB(idEntidadeAplicacao, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/depara/aplicacao/b/registros/' + idEntidadeAplicacao)
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
        	$http.post('/gtm-console-web-api/depara/', item )
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
        	$http.put('/gtm-console-web-api/depara/', item )
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
        	$http.delete('/gtm-console-web-api/depara/' + item.id + '/' + idUsuario)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function checkRuleAssociacaoEntidadeAplicacao(idEntidadeAplicacaoA, idEntidadeAplicacaoB, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/depara/checkrule/associacao/entidadeaplicacao/' + idEntidadeAplicacaoA + '/' + idEntidadeAplicacaoB)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function upload(formdata, idEntidadeAplicacaoA, idEntidadeAplicacaoB, callback) {
        	var configContent = {
        		transformRequest: angular.identity,
                headers : {
                    'Content-Type': undefined
                }
            }
        	var response;
            $http.post('/gtm-console-web-api/depara/upload/' + idEntidadeAplicacaoA + '/' + idEntidadeAplicacaoB, formdata, configContent)
            .success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function buscarRegstrosDePara(idEntidadeAplicacaoDePara, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/depara/registros/' + idEntidadeAplicacaoDePara)
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