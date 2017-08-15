(function () {
    'use strict';

    stockModule.controller('UsuarioController', UsuarioController);

    UsuarioController.$inject = ['$rootScope', '$scope', '$location', 'UsuarioService', 'RESTfulResourceService', 'notificationsFactory'];
    function UsuarioController($rootScope, $scope, $location, UsuarioService, RESTfulResourceService, notificationsFactory) {
        
    	// Verificando permissão de acesso ao recurso.
    	$rootScope.$watch('AUTORIZADOR.recursos', function() {
    		if (!$rootScope.AUTORIZADOR.isRecursoPermitido($rootScope.AUTORIZADOR.REC_ADM_USUARIO)) {
	    		$location.path('/acessonegado');
	    	}
    	});
    	    	
    	// Variável global para armazenar o módulo carregado.
    	$rootScope.globals.moduleLoaded=0;
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
    	$scope.carregarPerfis = carregarPerfis;
    	$scope.perfis = '';
    	$scope.gruposPendentes = '';
    	$scope.flagSelTodosAddGrupo = false;
    	$scope.fecharJanelaCadastroModal = fecharJanelaCadastroModal;
    	$scope.fecharJanelaDeleteModal = fecharJanelaDeleteModal;
    	$scope.fecharJanelaAddGrupoModal = fecharJanelaAddGrupoModal;
    	$scope.resetCadastroForm = resetCadastroForm;
    	$scope.abrirPagina = abrirPagina;
    	$scope.aplicarFiltro = aplicarFiltro;
    	$scope.carregarGrupoAdicionados = carregarGrupoAdicionados;
    	$scope.carregarGruposPendentes = carregarGruposPendentes;
    	$scope.carregarListasGrupoFormUsuario = carregarListasGrupoFormUsuario;
    	$scope.concluirAdicaoGrupo = concluirAdicaoGrupo;
    	$scope.selecionarTodosAddGrupo = selecionarTodosAddGrupo;
    	$scope.removerGrupoUsuario = removerGrupoUsuario;
    	$scope.carregarGrupos = carregarGrupos;
    	$scope.acionarInclusao = acionarInclusao;
    	$scope.acionarAlteracao = acionarAlteracao;
    	$scope.acionarExclusao = acionarExclusao;
    	    	    	    	
    	function init() {
    		$rootScope.AUTORIZADOR.carregarAcoesPermitidas($rootScope.AUTORIZADOR.REC_ADM_USUARIO);
    		$scope.buscar();
    		$scope.carregarPerfis();
    	}
    	
    	function limparItemSelecionado() {
    		$scope.item = {
    			'grupos': []
    		};
    	}
    	
    	$('#add-grupo-modal').on('shown.bs.modal', function() {
        	if (!$scope.item.id) {
    			$scope.carregarGrupos();
    		}
    	});
    	
    	function selecionarItem(itemSel, acao) {
    		if (acao=='alt') {
	    		$scope.item = {
	    				'id': itemSel.id, 
	    				'codigo': itemSel.codigo, 
	    				'nome': itemSel.nome,
	    				'email': itemSel.email,
	    				'perfil': itemSel.perfil,
	    				'bloqueado': itemSel.bloqueado,
	    				'admDePara': itemSel.admDePara,
	    				'grupos': itemSel.grupos
	    		};
	    		carregarListasGrupoFormUsuario();
    		}
    		else {
    			$scope.item = {
        				'id': itemSel.id
        		};
    		}
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
    		if ($rootScope.AUTORIZADOR.isAcaoPermitida($rootScope.AUTORIZADOR.ACAO_EXCLUIR) 
    				&& !itemSel.hasRestricaoExclusao) {
    			$scope.selecionarItem(itemSel,'exc');
	    		$('#delete-modal').modal('show');
    		}
    	}
    	    	
        function buscar() {
            $scope.dataLoading = true;
            UsuarioService.buscar($scope.pageSize, $scope.currentPage, $scope.filtro, function (response) {
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
        	UsuarioService.incluir($scope.item, function (response) {
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
        	UsuarioService.alterar($scope.item, function (response) {
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
        	UsuarioService.excluir($scope.item, function (response) {
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
        
        function carregarPerfis() {
            $scope.dataLoading = true;
            RESTfulResourceService.listar($rootScope.ENTITIES.PERFIL, function (response) {
                if (response.success) {
                	$scope.perfis = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
                
        function carregarGrupoAdicionados() {
            $scope.dataLoading = true;
            UsuarioService.listarGruposAdicionados($scope.item.id, function (response) {
                if (response.success) {
                	$scope.item.grupos = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);       	
                }
            });
        };
        
        function carregarGruposPendentes() {
            $scope.dataLoading = true;
            UsuarioService.listarGruposPendentes($scope.item.id, function (response) {
                if (response.success) {
                	$scope.gruposPendentes = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        function carregarGrupos() {
            if ($scope.item.grupos.length==0) {
	            $scope.dataLoading = true;
	            RESTfulResourceService.listar($rootScope.ENTITIES.GRUPO, function (response) {
	                if (response.success) {
	                	$scope.gruposPendentes = response.data;
	                	$scope.dataLoading = false;
	                } else {
	                	$scope.dataLoading = false;
	                	notificationsFactory.error(response.data);                	
	                }
	            });
            }
        };
        
        function carregarListasGrupoFormUsuario() {
        	carregarGrupoAdicionados();
        	carregarGruposPendentes();
    	}
        
        function concluirAdicaoGrupo() {
        	for (var i=0; i<$scope.gruposPendentes.length; i++) {
        		if ($scope.gruposPendentes[i].selecionado) {
        			$scope.item.grupos.push($scope.gruposPendentes[i]);
        			$scope.gruposPendentes.splice(i,1);
        			i--;
        		}
        	}
        	fecharJanelaAddGrupoModal();
    	}
        
        function removerGrupoUsuario(grupoSel) {
        	for (var i=0; i<$scope.item.grupos.length; i++) {
        		if ($scope.item.grupos[i].id == grupoSel.id) {
        			$scope.item.grupos[i].selecionado = false;
        			$scope.gruposPendentes.push($scope.item.grupos[i]);
        			$scope.item.grupos.splice(i,1);
        		}
        	}	
    	}
        
        function fecharJanelaCadastroModal() {
        	$scope.limparItemSelecionado();
        	resetCadastroForm();
        	$('#cadastro-modal').modal('hide');
        }
        
        function fecharJanelaDeleteModal() {
        	$scope.limparItemSelecionado();
        	$('#delete-modal').modal('hide');
        }
        
        function fecharJanelaAddGrupoModal() {
        	$scope.flagSelTodosAddGrupo = false;
        	$('#add-grupo-modal').modal('hide');
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
        
        function selecionarTodosAddGrupo() {
        	$scope.flagSelTodosAddGrupo = !$scope.flagSelTodosAddGrupo;
        	for (var i=0; i<$scope.gruposPendentes.length; i++) {
        		$scope.gruposPendentes[i].selecionado = $scope.flagSelTodosAddGrupo;
        	}
        }
        
    }

})();