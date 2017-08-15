(function () {
    'use strict';

    stockModule.controller('AutorizacaoController', AutorizacaoController);

    AutorizacaoController.$inject = ['$scope', '$rootScope', '$location', '$window', 'AutorizacaoService', 'notificationsFactory'];
    function AutorizacaoController($scope, $rootScope, $location, $window, AutorizacaoService, notificationsFactory) {
        
    	$rootScope.AUTORIZADOR = {};
    	
    	$rootScope.AUTORIZADOR.REC_ADM_CONFIGURACAO 		= "REC_ADM_CONFIGURACAO";
    	$rootScope.AUTORIZADOR.REC_ADM_USUARIO 				= "REC_ADM_USUARIO";
    	$rootScope.AUTORIZADOR.REC_CAD_APLICACAO 			= "REC_CAD_APLICACAO";
    	$rootScope.AUTORIZADOR.REC_CAD_DIVISAO 				= "REC_CAD_DIVISAO";
    	$rootScope.AUTORIZADOR.REC_CAD_EVENTO 				= "REC_CAD_EVENTO";
    	$rootScope.AUTORIZADOR.REC_CAD_NIVEL_EVENTO 		= "REC_CAD_NIVEL_EVENTO";
    	$rootScope.AUTORIZADOR.REC_CAD_TIPO_EVENTO 			= "REC_CAD_TIPO_EVENTO";
    	$rootScope.AUTORIZADOR.REC_CAD_TRANSACAO 			= "REC_CAD_TRANSACAO";
    	$rootScope.AUTORIZADOR.REC_CAD_ACAO_TRANSACAO 		= "REC_CAD_ACAO_TRANSACAO";
    	$rootScope.AUTORIZADOR.REC_CAD_PRINC_TRANSACAO 		= "REC_CAD_TRANSACAO";
    	$rootScope.AUTORIZADOR.REC_CAD_GRUPO_TRANSACAO 		= "REC_CAD_GRUPO_TRANSACAO";
    	$rootScope.AUTORIZADOR.REC_CAD_PARAM_TRANSACAO 		= "REC_CAD_PARAM_TRANSACAO";
    	$rootScope.AUTORIZADOR.REC_CAD_PASSO_TRANSACAO 		= "REC_CAD_PASSO_TRANSACAO";
    	$rootScope.AUTORIZADOR.REC_CON_DASHBOARD 			= "REC_CON_DASHBOARD";
    	$rootScope.AUTORIZADOR.REC_CAD_ENTIDADE 			= "REC_CAD_ENTIDADE";
    	$rootScope.AUTORIZADOR.REC_CAD_DEPARA	 			= "REC_CAD_DEPARA";
    	
    	$rootScope.AUTORIZADOR.ACAO_INCLUIR = "INC";
    	$rootScope.AUTORIZADOR.ACAO_ALTERAR = "ALT";
    	$rootScope.AUTORIZADOR.ACAO_EXCLUIR = "EXC";
    	
    	$rootScope.AUTORIZADOR.carregarRecursosPermitidos = carregarRecursosPermitidos;
    	$rootScope.AUTORIZADOR.isRecursoPermitido = isRecursoPermitido;
    	
    	$rootScope.AUTORIZADOR.carregarAcoesPermitidas = carregarAcoesPermitidas;
    	$rootScope.AUTORIZADOR.isAcaoPermitida = isAcaoPermitida;
    	
        $rootScope.$watch('globals.currentUser', carregarRecursosPermitidos);
        		
        function carregarRecursosPermitidos() {
        	$scope.dataLoading = true;
            AutorizacaoService.listarPermissoesAcessoPerfil($rootScope.globals.currentUser.user.perfil.id, function (response) {
                if (response.success) {
                	$rootScope.AUTORIZADOR.recursos = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);  
                }
            });
        }
        	    
	    function isRecursoPermitido(codigoRecurso) {
	    	if ($rootScope.globals.currentUser) {
	    		for (var i=0; i<$rootScope.AUTORIZADOR.recursos.length; i++) {
		    		if ($rootScope.AUTORIZADOR.recursos[i].codigo==codigoRecurso) {
		    			return true;
		    		}
		    	}
	    	}
	    	return false;
	    };
	    
	    function carregarAcoesPermitidas(codigoRecurso) {
        	$scope.dataLoading = true;
            AutorizacaoService.listarPermissoesAcessoRecurso($rootScope.globals.currentUser.user.perfil.id, codigoRecurso, function (response) {
                if (response.success) {
                	$rootScope.AUTORIZADOR.acoes = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);  
                }
            });
        }
	    
	    function isAcaoPermitida(codigoAcao) {
    		for (var i=0; i<$rootScope.AUTORIZADOR.acoes.length; i++) {
	    		if ($rootScope.AUTORIZADOR.acoes[i].permissao.codigo==codigoAcao) {
	    			return true;
	    		}
	    	}
    		return false;
	    }
	            
    }

})();