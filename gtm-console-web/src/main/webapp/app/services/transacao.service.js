(function () {
    'use strict';

    stockModule.factory('TransacaoService', TransacaoService);

    TransacaoService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout'];
    function TransacaoService($http, $cookieStore, $rootScope, $timeout) {
        var service = {};
        
        service.buscarTransacaoParametroByIdTransacao = buscarTransacaoParametroByIdTransacao;
        service.buscarTransacaoPassoByIdTransacao = buscarTransacaoPassoByIdTransacao;
        service.buscarTransacoesByGrupos = buscarTransacoesByGrupos;
        service.incluir = incluir;
        service.alterar = alterar;
        service.excluir = excluir;
        service.listarGruposAdicionados = listarGruposAdicionados;
        service.checkRuleExclusaoTransacaoGrupo = checkRuleExclusaoTransacaoGrupo;
        service.checkRuleExclusaoTransacaoPasso = checkRuleExclusaoTransacaoPasso;
        service.checkRuleExclusaoTransacaoPassoAcao = checkRuleExclusaoTransacaoPassoAcao;
        service.checkRuleExclusaoTransacaoParametro = checkRuleExclusaoTransacaoParametro;
        service.checkRuleDuplicidadeCodigoTransacao = checkRuleDuplicidadeCodigoTransacao;
        service.checkRuleExclusaoTransacao = checkRuleExclusaoTransacao;

        return service;
        
        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        }
        
        function buscarTransacaoParametroByIdTransacao(idTransacao, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/transacao/transacaoparametro/' + idTransacao)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function buscarTransacaoPassoByIdTransacao(idTransacao, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/transacao/transacaopasso/' + idTransacao)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function buscarTransacoesByGrupos(filtro, callback) {
        	var response;
        	$http.post('/gtm-console-web-api/transacao/filtro/transacaogrupo/', filtro)
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
        	$http.post('/gtm-console-web-api/transacao/', item )
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
        	$http.put('/gtm-console-web-api/transacao/', item )
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
        	$http.delete('/gtm-console-web-api/transacao/' + item.id)
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
        	$http.get('/gtm-console-web-api/transacao/grupo/adicionado/' + id)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function checkRuleExclusaoTransacaoGrupo(id, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/transacao/transacaogrupo/checkrule/delete/' + id)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function checkRuleExclusaoTransacaoPasso(id, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/transacao/transacaopasso/checkrule/delete/' + id)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function checkRuleExclusaoTransacaoPassoAcao(idTransacaoPasso, idTipoEvento, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/transacao/transacaopassoacao/checkrule/delete/' + idTransacaoPasso + '/' + idTipoEvento)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function checkRuleExclusaoTransacaoParametro(id, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/transacao/transacaoparametro/checkrule/delete/' + id)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function checkRuleExclusaoTransacao(id, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/transacao/checkrule/delete/' + id)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function checkRuleDuplicidadeCodigoTransacao(codigo, idRef, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/transacao/checkrule/duplicidade/' + codigo + '/' + (!idRef ? 0 : idRef))
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