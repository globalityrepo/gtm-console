(function () {
    'use strict';

    stockModule.controller('EventoNivelController', EventoNivelController);

    EventoNivelController.$inject = ['$scope', '$rootScope', '$location', 'RESTfulResourceService', 'notificationsFactory'];
    function EventoNivelController($scope, $rootScope, $location, RESTfulResourceService, notificationsFactory) {
        
    	// Verificando permissão de acesso ao recurso.
    	$rootScope.$watch('AUTORIZADOR.recursos', function() {
    		if (!$rootScope.AUTORIZADOR.isRecursoPermitido($rootScope.AUTORIZADOR.REC_CAD_NIVEL_EVENTO)) {
	    		$location.path('/acessonegado');
	    	}
    	});
    	
    	// Variável global para armazenar o módulo carregado.
    	$rootScope.globals.moduleLoaded=2;
    	$rootScope.aplicarBackground = false;
    	
    	// Controles de Paginação.
    	$scope.pageSize = 10;
    	$scope.currentPage = 1;
    	$scope.querySize = 0;
    	$scope.queryPages = 1;
    	$scope.numFirstReg = 0;
    	$scope.numLastReg = 0;
    	
    	$scope.initPaginacao = initPaginacao;
    	$scope.getNumber = getNumber;
    	
    	function initPaginacao() {
    		if ($scope.itens.length>0) { 
	    		$scope.querySize = $scope.itens[0].paginacaoQtdeRegConsulta;
	    		$scope.queryPages = Math.ceil($scope.querySize / $scope.pageSize);
	    		$scope.numFirstReg = (($scope.currentPage-1)*$scope.pageSize)+1;
	    		if ($scope.currentPage == $scope.queryPages) {
	    			$scope.numLastReg = $scope.querySize;
	    		}
	    		else {
	    			$scope.numLastReg = $scope.currentPage*$scope.pageSize;
	    		}    			
    		}
    		else {
    			$scope.querySize = 0;
    			$scope.queryPages = 1;
    			$scope.numFirstReg = 0;
    			$scope.numLastReg = 0;
    		}
    	}
    	
    	function getNumber(num) {
    	    return new Array(num);   
    	}
    	
    	// Controles da Funcionalidade.
    	$scope.modo = 0; // 0 -> Inclusão | 1 -> Alteração
    	$scope.filtro = '';
    	$scope.item = '';
    	$scope.itens = '';
    	$scope.init = init;
    	$scope.buscar = buscar;
    	$scope.incluir = incluir;
    	$scope.alterar = alterar;
    	$scope.excluir = excluir;
    	$scope.selecionarItem = selecionarItem;    
    	$scope.limparItemSelecionado = limparItemSelecionado;
    	limparItemSelecionado
    	$scope.divisoes = '';
    	$scope.fecharJanelaCadastroModal = fecharJanelaCadastroModal;
    	$scope.fecharJanelaDeleteModal = fecharJanelaDeleteModal;
    	$scope.resetCadastroForm = resetCadastroForm;
    	$scope.abrirPagina = abrirPagina;
    	$scope.aplicarFiltro = aplicarFiltro;
    	$scope.acionarInclusao = acionarInclusao;
    	$scope.acionarAlteracao = acionarAlteracao;
    	$scope.acionarExclusao = acionarExclusao;
    	    	
    	function init() {
    		$rootScope.AUTORIZADOR.carregarAcoesPermitidas($rootScope.AUTORIZADOR.REC_CAD_NIVEL_EVENTO);
    		$scope.buscar();
    		$scope.carregarDivisoes();
    	}
    	
    	function limparItemSelecionado() {
    		$scope.item = '';
    		$scope.modo = 0;
    	}
    	
    	function selecionarItem(itemSel,acao) {
    		$scope.item = {
    				'id': itemSel.id, 
    				'descricao': itemSel.descricao,
    				'ordem': itemSel.ordem
    		};
    		$scope.modo = 1;
    	}
    	
    	function acionarInclusao() {
    		if ($rootScope.AUTORIZADOR.isAcaoPermitida($rootScope.AUTORIZADOR.ACAO_INCLUIR)) {
	    		$scope.limparItemSelecionado();
	    		$('#cadastro-modal').modal('show');
    		}
    	}
    	
    	function acionarAlteracao(itemSel) {
    		if ($rootScope.AUTORIZADOR.isAcaoPermitida($rootScope.AUTORIZADOR.ACAO_ALTERAR)) {
    			$scope.selecionarItem(itemSel,'alt');
        		$('#cadastro-modal').modal('show');
    		}
    	}
    	
    	function acionarExclusao(itemSel) {
    		if ($rootScope.AUTORIZADOR.isAcaoPermitida($rootScope.AUTORIZADOR.ACAO_EXCLUIR)) {
    			$scope.selecionarItem(itemSel,'exc');
	    		$('#delete-modal').modal('show');
    		}
    	}
    	
        function buscar() {
            $scope.dataLoading = true;
            RESTfulResourceService.buscar($rootScope.ENTITIES.EVENTO_NIVEL, $scope.pageSize, $scope.currentPage, $scope.filtro, function (response) {
                if (response.success) {
                	$scope.itens = response.data;
                	$scope.initPaginacao();
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        function incluir() {
        	$scope.dataLoading = true;
        	RESTfulResourceService.incluir($rootScope.ENTITIES.EVENTO_NIVEL, $scope.item, function (response) {
                if (response.success) {
                	$scope.buscar();
                	$scope.dataLoading = false;
                	notificationsFactory.success();
                	fecharJanelaCadastroModal();
                	
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);
                	fecharJanelaCadastroModal();
                }
            });
        };
        
        function alterar() {
        	$scope.dataLoading = true;
        	RESTfulResourceService.alterar($rootScope.ENTITIES.EVENTO_NIVEL, $scope.item, function (response) {
                if (response.success) {
                	$scope.buscar();
                	$scope.dataLoading = false;
                	notificationsFactory.success();
                	fecharJanelaCadastroModal();
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);
                	fecharJanelaCadastroModal();
                }
            });
        };
        
        function excluir() {
        	$scope.dataLoading = true;
        	RESTfulResourceService.excluir($rootScope.ENTITIES.EVENTO_NIVEL, $scope.item, function (response) {
                if (response.success) {
                	$scope.buscar();                	
                	$scope.dataLoading = false;                	
                	fecharJanelaDeleteModal();
                	notificationsFactory.success();
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);
                	fecharJanelaDeleteModal();
                }
            });
        };
        
        function fecharJanelaCadastroModal() {
        	$scope.limparItemSelecionado();
        	resetCadastroForm();
        	$('#cadastro-modal').modal('hide');
        }
        
        function fecharJanelaDeleteModal() {
        	$scope.limparItemSelecionado();
        	$('#delete-modal').modal('hide');
        }
        
        function resetCadastroForm() {
        	$scope.form.$setPristine();
        }
        
        function abrirPagina(pagina) {
        	$scope.currentPage = pagina;
        	$scope.buscar();
        }
        
        function aplicarFiltro() {
        	abrirPagina(1);
        }
        
    }

})();