(function () {
    'use strict';

    stockModule.controller('TransacaoController', TransacaoController);

    TransacaoController.$inject = ['$scope', '$rootScope', '$location', '$translate', 'TransacaoService', 'RESTfulResourceService', 'notificationsFactory'];
    function TransacaoController($scope, $rootScope, $location, $translate, TransacaoService, RESTfulResourceService, notificationsFactory) {
        
    	// Verificando permissão de acesso ao recurso.
    	$rootScope.$watch('AUTORIZADOR.recursos', function() {
    		if (!$rootScope.AUTORIZADOR.isRecursoPermitido($rootScope.AUTORIZADOR.REC_CAD_PRINC_TRANSACAO)) {
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
    	$scope.filtro = '';
    	$scope.item = { };
    	$scope.itens = '';
    	$scope.init = init;
    	$scope.buscar = buscar;
    	$scope.incluir = incluir;
    	$scope.alterar = alterar;
    	$scope.excluir = excluir;
    	$scope.selecionarItem = selecionarItem;    
    	$scope.limparItemSelecionado = limparItemSelecionado;
    	$scope.fecharJanelaCadastroModal = fecharJanelaCadastroModal;
    	$scope.fecharJanelaDeleteModal = fecharJanelaDeleteModal;
    	$scope.resetCadastroForm = resetCadastroForm;
    	$scope.abrirPagina = abrirPagina;
    	$scope.aplicarFiltro = aplicarFiltro;
    	$scope.carregarParametros = carregarParametros;
    	$scope.carregarPassos = carregarPassos;
    	$scope.msgValidacaoCadastro = '';
    	$scope.submeterFormCadastro = submeterFormCadastro;
    	$scope.execExcluir = execExcluir;
    	$scope.validarFormCadastro = validarFormCadastro;
    	$scope.acionarInclusao = acionarInclusao;
    	$scope.acionarAlteracao = acionarAlteracao;
    	$scope.acionarExclusao = acionarExclusao;
    		    	
    	function init() {
    		$rootScope.AUTORIZADOR.carregarAcoesPermitidas($rootScope.AUTORIZADOR.REC_CAD_PRINC_TRANSACAO);
    		$scope.buscar();
    	}
    	
    	function limparItemSelecionado() {
    		
    		// Reset tab navigator principal.
    		$("#tabDadosGeraisOpt").removeClass("active");
    		$("#tabParametrosOpt").removeClass("active");
    		$("#tabPassosOpt").removeClass("active");
    		$("#tabGruposOpt").removeClass("active");
    		$('#tabDadosGeraisOpt').addClass('active');
    		
    		$("#tabParametros").removeClass("in active");
    		$("#tabPassos").removeClass("in active");
    		$("#tabGrupos").removeClass("in active");
    		$('#tabDadosGerais').addClass('in active');
    		
    		// Reset do item do cadadtro.
    		$scope.item = {
        			'restricao': 'Y',
        			'parametros': [],
    				'passos': [],
    				'grupos': [],
    				'modelosSimulados': []
        	};
    		
    		$scope.msgValidacaoCadastro = '';
    	
    	}
    	
    	function selecionarItem(itemSel,acao) {
    		if (acao=='alt') {
    			$scope.item = {
	    				'id': itemSel.id,
	    				'codigo': itemSel.codigo,
	    				'descricao': itemSel.descricao,     				
	    				'qtdeDiaEvento': itemSel.qtdeDiaEvento,
	    				'qtdeDiaConteudoEvento': itemSel.qtdeDiaConteudoEvento,
	    				'restricao': itemSel.restricao,
	    				'parametros': itemSel.parametros,
	    				'passos': itemSel.passos,
	    				'grupos': itemSel.grupos,	
	    				'modelosSimulados': itemSel.modelosSimulados	
	    		};
    			$scope.carregarParametros();
	    		$scope.carregarPassos();
	    		$scope.carregarListasGrupos();
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
    		if ($rootScope.AUTORIZADOR.isAcaoPermitida($rootScope.AUTORIZADOR.ACAO_EXCLUIR)) {
    			$scope.selecionarItem(itemSel,'exc');
	    		$('#delete-modal').modal('show');
    		}
    	}
    	    	
        function buscar() {
            $scope.dataLoading = true;
            RESTfulResourceService.buscar($rootScope.ENTITIES.TRANSACAO, $scope.pageSize, $scope.currentPage, $scope.filtro, function (response) {
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
        
        function validarFormCadastro() {
        	if ($scope.item.passos.length==0) {
        		$scope.msgValidacaoCadastro = $translate.instant('label.mensagemadicioneumpasso');
        		return false;
        	} 
        	else if ($scope.item.grupos.length==0) {
        		$scope.msgValidacaoCadastro = $translate.instant('label.mensagemadicioneumgrupo');
        		return false;
        	} 
        	return true;
        }
   
        function submeterFormCadastro() {
        	if (validarFormCadastro()) {
        		$scope.dataLoading = true;
                TransacaoService.checkRuleDuplicidadeCodigoTransacao($scope.item.codigo, $scope.item.id, function (response) {
                    if (response.success) {
                    	$scope.msgValidacaoCadastro = '';
                    	$scope.dataLoading = false;
                    	if (!$scope.item.id) {
                    		$scope.incluir();
                    	}
                    	else {
                    		$scope.alterar();
                    	}
                    }
                    else {
                    	$scope.msgValidacaoCadastro = response.data;
                    	$scope.dataLoading = false;
                    }
                }); 
        	} 
        }
        
        function incluir() {
        	if ($scope.validarFormCadastro()) {
	        	$scope.dataLoading = true;
	        	TransacaoService.incluir($scope.item, function (response) {
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
        	}
        };
        
        function alterar() {
        	if ($scope.validarFormCadastro()) {
	        	$scope.dataLoading = true;
	        	TransacaoService.alterar($scope.item, function (response) {
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
        	}
        };
        
        function removerGrupo(transacaoGrupoSel) {
        	if (transacaoGrupoSel.id>0) {
        		$scope.dataLoading = true;
                TransacaoService.checkRuleExclusaoTransacaoGrupo(transacaoGrupoSel.id, function (response) {
                    if (response.success) {
                    	$scope.execRemoverGrupo(transacaoGrupoSel);
                    	$scope.dataLoading = false;
                    } else {
                    	$scope.dataLoading = false;
                    	notificationsFactory.error(response.data);                	
                    }
                });        		
        	}
        	else {
        		$scope.execRemoverGrupo(transacaoGrupoSel);
        	}    		
    	}
        
        function excluir() {
        	$scope.dataLoading = true;
        	TransacaoService.checkRuleExclusaoTransacao($scope.item.id, function (response) {
                if (response.success) {
                	$scope.execExcluir();                	
                	$scope.dataLoading = false;   
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);
                }
            });
        };
        
        function execExcluir() {
        	$scope.dataLoading = true;
        	TransacaoService.excluir($scope.item, function (response) {
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
        
        function carregarTransacaoGrupo() {
            $scope.dataLoading = true;
            RESTfulResourceService.listar($rootScope.ENTITIES.GRUPO, function (response) {
                if (response.success) {
                	$scope.transacaoGrupos = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        function carregarParametros() {
            $scope.dataLoading = true;
            TransacaoService.buscarTransacaoParametroByIdTransacao($scope.item.id, function (response) {
                if (response.success) {
                	$scope.item.parametros = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };             
        
        function validarFormPasso() {
        	if ($scope.item.passos.length>0) {
        		alert(1);
        		return true;
        	} 
        	alert(2);
        	return false;
        }
        
        function carregarPassos() {
            $scope.dataLoading = true;
            TransacaoService.buscarTransacaoPassoByIdTransacao($scope.item.id, function (response) {
                if (response.success) {
                	$scope.item.passos = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
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
        
        //=============
        // TAB GRUPOS
        //=============
        $scope.carregarGrupos = carregarGrupos;
    	$scope.carregarGruposAdicionados = carregarGruposAdicionados;
    	$scope.carregarListasGrupos = carregarListasGrupos;
    	$scope.concluirAdicaoGrupo = concluirAdicaoGrupo;
    	$scope.selecionarTodosAddGrupo = selecionarTodosAddGrupo;
    	$scope.fecharJanelaAddGrupoModal = fecharJanelaAddGrupoModal;
    	$scope.gruposPendentes = [];
    	$scope.flagSelTodosAddGrupo = false;
    	$scope.removerGrupo = removerGrupo;
    	$scope.execRemoverGrupo = execRemoverGrupo;
    	
        $('#add-transacao-grupo-modal').on('shown.bs.modal', function() {
        	$scope.carregarGrupos();
    		if ($scope.item.id) {
    			$scope.filtrarGruposPendentes();
    		}
    	});
    	
        function concluirAdicaoGrupo() {
        	for (var i=0; i<$scope.gruposPendentes.length; i++) {
        		if ($scope.gruposPendentes[i].selecionado) {
        			var idFake = (-99) + $scope.item.grupos.length;
        			$scope.item.grupos.push({
        				'id': idFake,
        				'grupo': $scope.gruposPendentes[i]
        			});
        			$scope.gruposPendentes.splice(i,1);
        			i--;
        		}
        	}
        	$scope.msgValidacaoCadastro = '';
        	fecharJanelaAddGrupoModal();
    	}
    	
    	function carregarGruposAdicionados() {
            $scope.dataLoading = true;
            TransacaoService.listarGruposAdicionados($scope.item.id, function (response) {
                if (response.success) {
                	$scope.item.grupos = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);       	
                }
            });
        };
        
        function carregarGrupos() {
            $scope.dataLoadingAddGrupo = true;
            RESTfulResourceService.listar($rootScope.ENTITIES.GRUPO, function (response) {
                if (response.success) {
                	$scope.gruposPendentes = response.data;
                	concluirCarregarGruposPendentes();
                	$scope.dataLoadingAddGrupo = false;
                } else {
                	$scope.dataLoadingAddGrupo = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
                
        function concluirCarregarGruposPendentes() {
        	for (var i=0; i<$scope.item.grupos.length; i++) {
	        	for (var j=0; j<$scope.gruposPendentes.length; j++) {
	        		if ($scope.item.grupos[i].grupo.id == $scope.gruposPendentes[j].id) {
	        			$scope.gruposPendentes.splice(j,1);
	        			j--;
	        		}
	        	}
        	}
        }
        
        function carregarListasGrupos() {
        	$scope.carregarGruposAdicionados();
        }
    	
    	function fecharJanelaAddGrupoModal() {
        	$scope.flagSelTodosAddGrupo = false;
        	$('#add-transacao-grupo-modal').modal('hide');
        }
    	
    	function selecionarTodosAddGrupo() {
        	$scope.flagSelTodosAddGrupo = !$scope.flagSelTodosAddGrupo;
        	for (var i=0; i<$scope.gruposPendentes.length; i++) {
        		$scope.gruposPendentes[i].selecionado = $scope.flagSelTodosAddGrupo;
        	}
        }
    	
    	function removerGrupo(transacaoGrupoSel) {
        	if (transacaoGrupoSel.id>0) {
        		$scope.dataLoading = true;
                TransacaoService.checkRuleExclusaoTransacaoGrupo(transacaoGrupoSel.id, function (response) {
                    if (response.success) {
                    	$scope.execRemoverGrupo(transacaoGrupoSel);
                    	$scope.dataLoading = false;
                    } else {
                    	$scope.dataLoading = false;
                    	notificationsFactory.error(response.data);                	
                    }
                });        		
        	}
        	else {
        		$scope.execRemoverGrupo(transacaoGrupoSel);
        	}    		
    	}
    	
    	function execRemoverGrupo(transacaoGrupoSel) {
    		for (var i=0; i<$scope.item.grupos.length; i++) {
        		if ($scope.item.grupos[i].id == transacaoGrupoSel.id) {
        			$scope.item.grupos.splice(i,1);
        		}
        	}	
    	}	
    	
    	//=================
	    // TAB PARÂMETROS
    	//=================
    	$scope.itemTabParametro = {};
    	$scope.limparItemTabParametroSel = limparItemTabParametroSel;
    	$scope.selecionarItemTabParametro = selecionarItemTabParametro;
    	$scope.fecharJanelaAddParametroModal = fecharJanelaAddParametroModal;
    	$scope.adicionarParametro = adicionarParametro;
    	$scope.alterarParametro = alterarParametro;
    	$scope.removerParametro = removerParametro;
    	$scope.execRemoverParametro = execRemoverParametro;
    	$scope.validarFormParametro = validarFormParametro;
    	$scope.resetValidacaoFormParametro = resetValidacaoFormParametro;
    	$scope.msgValidacaoCadParametro = '';
    	
	    function limparItemTabParametroSel() {
	    	$scope.itemTabParametro = {
					'ativo': 'Y'
			};
	    	$scope.resetValidacaoFormParametro();
		}
		
		function selecionarItemTabParametro(itemSel) {
			$scope.itemTabParametro = {
    				'id': itemSel.id, 
    				'transacao': itemSel.transacao,
    				'nome': itemSel.nome,
    				'caminho': itemSel.caminho,
    				'namespace': itemSel.namespace,
    				'momento': itemSel.momento,
    				'prefixo': itemSel.prefixo,
    				'ativo': itemSel.ativo
    		};
		}
				
		function fecharJanelaAddParametroModal() {
	    	$scope.limparItemTabParametroSel();
	    	$('#add-transacao-parametro-modal').modal('hide');
		}
		
		function adicionarParametro() {
			if (validarFormParametro()) {
				var idFake = (-99) + $scope.item.parametros.length;
				$scope.item.parametros.push({
					'id': idFake,
					'transacao': $scope.itemTabParametro.transacao,
					'nome': $scope.itemTabParametro.nome,
					'caminho': $scope.itemTabParametro.caminho,
					'namespace': $scope.itemTabParametro.namespace,
					'momento': $scope.itemTabParametro.momento,
					'prefixo': $scope.itemTabParametro.prefixo,
					'ativo': $scope.itemTabParametro.ativo
				});
				fecharJanelaAddParametroModal();
			}
		}
		
		function alterarParametro() {
			if (validarFormParametro()) {
				for (var i=0; i<$scope.item.parametros.length; i++) {
					if ($scope.item.parametros[i].id == $scope.itemTabParametro.id) {
						$scope.item.parametros[i] = {
							'id': $scope.itemTabParametro.id,
							'transacao': $scope.itemTabParametro.transacao,
							'nome': $scope.itemTabParametro.nome,
							'caminho': $scope.itemTabParametro.caminho,
							'namespace': $scope.itemTabParametro.namespace,
							'momento': $scope.itemTabParametro.momento,
							'prefixo': $scope.itemTabParametro.prefixo,
							'ativo': $scope.itemTabParametro.ativo
						};
					}
				}
				fecharJanelaAddParametroModal();
			}
		}
		
		function removerParametro(itemSel) {
        	if (itemSel.id>0) {
        		$scope.dataLoading = true;
                TransacaoService.checkRuleExclusaoTransacaoParametro(itemSel.id, function (response) {
                    if (response.success) {
                    	$scope.execRemoverParametro(itemSel);
                    	$scope.dataLoading = false;
                    } else {
                    	$scope.dataLoading = false;
                    	notificationsFactory.error(response.data);                	
                    }
                });        		
        	}
        	else {
        		$scope.execRemoverParametro(itemSel);
        	}    		
    	}
    	
    	function execRemoverParametro(itemSel) {
    		for (var i=0; i<$scope.item.parametros.length; i++) {
				if ($scope.item.parametros[i].id == itemSel.id) {
					$scope.item.parametros.splice(i,1);
				}
			}
    	}	
				
		function resetValidacaoFormParametro() {
        	$scope.msgValidacaoCadParametro = '';
        }
        
        function validarFormParametro() {
        	var isValid = true;
    		for (var i=0; i<$scope.item.parametros.length; i++) {
        		if ($scope.item.parametros[i].id!=$scope.itemTabPasso.id
        				&& $scope.item.parametros[i].nome.toUpperCase()==$scope.itemTabParametro.nome.toUpperCase()) {
        			$scope.msgValidacaoCadParametro = $translate.instant('label.mensagemnomeduplicado');
        			isValid = false;
        			break;
        		}
        	}
        	return isValid;
        }
		
		//============
	    // TAB PASSO
	    //============
		$scope.itemTabPasso = {};
		$scope.selecionarItemTabPasso = selecionarItemTabPasso;
		$scope.limparItemTabPassoSel = limparItemTabPassoSel;
	    $scope.fecharJanelaAddPassoModal = fecharJanelaAddPassoModal;
	    $scope.flagSelTodosEventoNivel = false;
		$scope.carregarEventoNivelInclusao = carregarEventoNivelInclusao;
		$scope.adicionarPasso = adicionarPasso;
    	$scope.alterarPasso = alterarPasso;
    	$scope.removerPasso = removerPasso;
    	$scope.selecionarTodosEventoNivel = selecionarTodosEventoNivel;
    	$scope.resetValidacaoLista = resetValidacaoLista;
    	$scope.msgValidacaoCadPasso = '';
    	$scope.validarForm = validarForm;
    	$scope.qtdeNiveisEvento = 0;
    	$scope.incrementarNiveisEventoSel = incrementarNiveisEventoSel;
    	$scope.execRemoverPasso = execRemoverPasso;
    	
    	function selecionarItemTabPasso(itemSel) {
			$scope.itemTabPasso = {
    				'id': itemSel.id, 
    				'transacao': itemSel.transacao,
    				'codigo': itemSel.codigo, 
    				'descricao': itemSel.descricao,
    				'gravarNaBase': itemSel.gravarNaBase,
    				'eventoNiveis': itemSel.eventoNiveis,
    				'acoes': itemSel.acoes
	    	};
			// Totalizador apresentado na aba Níveis de Eventos.
			for (var i=0; i<itemSel.eventoNiveis.length; i++) {
				if (itemSel.eventoNiveis[i].selecionado) {
					$scope.qtdeNiveisEvento++;
				}
			}			
		}
    	
	    function limparItemTabPassoSel() {
	    	// Reset do tab navigator.
	    	$("#tabPassoNiveisEventoOpt").removeClass("active");
	    	$("#tabPassoAcoesOpt").removeClass("active");
	    	$('#tabPassoNiveisEventoOpt').addClass('active');
	    	
	    	$("#tabPassoAcoes").removeClass("in active");
	    	$('#tabPassoNiveisEvento').addClass('in active');
    		
	    	// Reset do item do cadastro.
	    	$scope.itemTabPasso = {
	    			'gravarNaBase': 'Y',
    				'eventoNiveis': [],
    				'acoes': []
			};
	    	$scope.qtdeNiveisEvento = 0;
	    	$scope.flagSelTodosEventoNivel = false;
	    	$scope.resetValidacaoLista();
    		$scope.carregarEventoNivelInclusao();
		}
	    
	    function fecharJanelaAddPassoModal() {
	    	$scope.limparItemTabPassoSel();
	    	$('#add-transacao-passo-modal').modal('hide');
		}
	    
	    function carregarEventoNivelInclusao() {
        	$scope.dataLoading = true;
        	RESTfulResourceService.listar($rootScope.ENTITIES.EVENTO_NIVEL, function (response) {
                if (response.success) {
                	$scope.itemTabPasso.eventoNiveis = response.data;
                	for (var i=0; i<$scope.itemTabPasso.eventoNiveis.length; i++) {
                		$scope.itemTabPasso.eventoNiveis[i].conteudo = 'Y';
                	}
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
		
        function incrementarNiveisEventoSel(itemSel) {
            if (!itemSel.selecionado) {
            	$scope.qtdeNiveisEvento++;
            }
            else {
            	$scope.qtdeNiveisEvento--;
            }
        };
        
        function adicionarPasso() {
        	if (validarForm()) {
	        	var idFake = (-99) + $scope.item.passos.length;
				$scope.item.passos.push({
					'id': idFake,
					'transacao': $scope.itemTabPasso.transacao,
					'codigo': $scope.itemTabPasso.codigo, 
					'descricao': $scope.itemTabPasso.descricao,
					'gravarNaBase': $scope.itemTabPasso.gravarNaBase,
					'eventoNiveis': $scope.itemTabPasso.eventoNiveis,
					'acoes': $scope.itemTabPasso.acoes					
				});
				$scope.msgValidacaoCadastro = '';
				fecharJanelaAddPassoModal();
        	}
		}
		
		function alterarPasso() {
			if (validarForm()) {
				for (var i=0; i<$scope.item.passos.length; i++) {
					if ($scope.item.passos[i].id == $scope.itemTabPasso.id) {
						$scope.item.passos[i] = {
							'id': $scope.itemTabPasso.id, 
							'transacao': $scope.itemTabPasso.transacao,
		    				'codigo': $scope.itemTabPasso.codigo, 
		    				'descricao': $scope.itemTabPasso.descricao,
		    				'gravarNaBase': $scope.itemTabPasso.gravarNaBase,
		    				'eventoNiveis': $scope.itemTabPasso.eventoNiveis,
		    				'acoes': $scope.itemTabPasso.acoes
						};
					}
				}
				fecharJanelaAddPassoModal();
			}
		}
		
		function removerPasso(itemSel) {
        	if (itemSel.id>0) {
        		$scope.dataLoading = true;
                TransacaoService.checkRuleExclusaoTransacaoPasso(itemSel.id, function (response) {
                    if (response.success) {
                    	$scope.execRemoverPasso(itemSel);
                    	$scope.dataLoading = false;
                    } else {
                    	$scope.dataLoading = false;
                    	notificationsFactory.error(response.data);                	
                    }
                });        		
        	}
        	else {
        		$scope.execRemoverPasso(itemSel);
        	}    		
    	}
    	
    	function execRemoverPasso(itemSel) {
    		for (var i=0; i<$scope.item.passos.length; i++) {
				if ($scope.item.passos[i].id == itemSel.id) {
					$scope.item.passos.splice(i,1);
				}
			}
    	}	
				
		function selecionarTodosEventoNivel() {
        	$scope.flagSelTodosEventoNivel = !$scope.flagSelTodosEventoNivel;
        	for (var i=0; i<$scope.itemTabPasso.eventoNiveis.length; i++) {
        		$scope.itemTabPasso.eventoNiveis[i].selecionado = $scope.flagSelTodosEventoNivel;
        	}
        	if ($scope.flagSelTodosEventoNivel) {
        		$scope.qtdeNiveisEvento = $scope.itemTabPasso.eventoNiveis.length;
        	}
        	else {
        		$scope.qtdeNiveisEvento = 0;
        	}
        	$scope.resetValidacaoLista();
        }
        
        function resetValidacaoLista() {
        	$scope.msgValidacaoCadPasso = '';
        }
        
        function validarForm() {
        	var isValid = false;
        	for (var i=0; i<$scope.itemTabPasso.eventoNiveis.length; i++) {
        		if ($scope.itemTabPasso.eventoNiveis[i].selecionado) {
        			isValid = true;
        			break;
        		}
        	}
        	if (!isValid) {
        		$scope.msgValidacaoCadPasso = $translate.instant('label.mensagemselecionenivelevento');
        	}
        	else if (!$scope.itemTabPasso.acoes || $scope.itemTabPasso.acoes.length==0) {
        		$scope.msgValidacaoCadPasso =  $translate.instant('label.mensagemadicioneacao');
        		isValid = false;
        	}
        	else {
	        	for (var i=0; i<$scope.item.passos.length; i++) {
	        		if ($scope.item.passos[i].id!=$scope.itemTabPasso.id
	        				&& $scope.item.passos[i].codigo.toUpperCase()==$scope.itemTabPasso.codigo.toUpperCase()) {
	        			$scope.msgValidacaoCadPasso = $translate.instant('label.mensagemcodigoduplicado');
	        			isValid = false;
	        			break;
	        		}
	        	}
        	}
        	return isValid;
        }
        
        //============
	    // TAB ACAO EM PASSO DE TRANSACAO
	    //============
		$scope.itemTabPassoAcao = {};
		$scope.limparItemTabPassoAcaoSelecionado = limparItemTabPassoAcaoSelecionado;
		$scope.fecharJanelaAddPassoAcaoModal = fecharJanelaAddPassoAcaoModal;
		$scope.selecionarItemTabPassoAcao = selecionarItemTabPassoAcao;
		$scope.adicionarPassoAcao = adicionarPassoAcao;
		$scope.alterarPassoAcao = alterarPassoAcao;
		$scope.removerPassoAcao = removerPassoAcao;
		$scope.carregarEventoTipo = carregarEventoTipo;
		$scope.eventoTipos = [];
		$scope.execRemoverPassoAcao = execRemoverPassoAcao;
		
		$('#add-transacao-passo-acao-modal').on('shown.bs.modal', function() {
			if (!$scope.itemTabPassoAcao.id) {
				$scope.limparItemTabPassoAcaoSelecionado();
			}	
			$scope.carregarEventoTipo();
		});
		
		function carregarEventoTipo() {
            $scope.dataLoading = true;
            RESTfulResourceService.listar($rootScope.ENTITIES.EVENTO_TIPO, function (response) {
                if (response.success) {
                	$scope.eventoTipos = response.data;
                	if (!$scope.itemTabPassoAcao.idFake && $scope.itemTabPasso.acoes.length>0) {
	                	for (var i=0; i<$scope.eventoTipos.length; i++) {
	                		for (var j=0; j<$scope.itemTabPasso.acoes.length; j++) {
	                			if ($scope.itemTabPasso.acoes[j].eventoTipo.id==$scope.eventoTipos[i].id) {
	                				$scope.eventoTipos.splice(i,1);
	                				break;
	                			}
	                		}                		
	                	}
                	}
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
		
		function limparItemTabPassoAcaoSelecionado() {
    		$scope.itemTabPassoAcao = {
    				'id': {
    					'idTransacaoPasso': '',
    					'idEventoTipo': ''
    				},
    				'idFake': '',
    				'transacaoPasso': '',
    				'eventoTipo': '',     				
    				'intervalo': '',
    				'qtdeTentativas': '',
    				'filaDestino': '',
    				'destinatario': '',
    				'reenvio': 'Y',
    				'xpath': '',
    				'namespace': '',
    				'prefixoNameSpace': ''
    		};
    	}
		
		function selecionarItemTabPassoAcao(itemSel) {
			$scope.itemTabPassoAcao = {
					'id': {
						'idTransacaoPasso': itemSel.id.idTransacaoPasso,
						'idEventoTipo': itemSel.id.idEventoTipo
					},
					'idFake': itemSel.idFake,
    				'transacaoPasso': itemSel.transacaoPasso,
    				'eventoTipo': itemSel.eventoTipo,     				
    				'intervalo': itemSel.intervalo,
    				'qtdeTentativas': itemSel.qtdeTentativas,
    				'filaDestino': itemSel.filaDestino,
    				'destinatario': itemSel.destinatario,
    				'reenvio': itemSel.reenvio,
    				'xpath': itemSel.xpath,
    				'namespace': itemSel.namespace,
    				'prefixoNameSpace': itemSel.prefixoNameSpace
    		};
	    }
    	
		function fecharJanelaAddPassoAcaoModal() {
			$scope.limparItemTabPassoAcaoSelecionado();
	    	$('#add-transacao-passo-acao-modal').modal('hide');
	    }
		
		function adicionarPassoAcao() {
			var idFake = (-99) + $scope.itemTabPasso.acoes.length;
			$scope.itemTabPasso.acoes.push({
				'id': {
					'idTransacaoPasso': '',
					'idEventoTipo': ''
				},
				'idFake': idFake,
				'eventoTipo': $scope.itemTabPassoAcao.eventoTipo,     				
				'intervalo': $scope.itemTabPassoAcao.intervalo,
				'qtdeTentativas': $scope.itemTabPassoAcao.qtdeTentativas,
				'filaDestino': $scope.itemTabPassoAcao.filaDestino,
				'destinatario': $scope.itemTabPassoAcao.destinatario,
				'reenvio': $scope.itemTabPassoAcao.reenvio,
				'xpath': $scope.itemTabPassoAcao.xpath,
				'namespace': $scope.itemTabPassoAcao.namespace,
				'prefixoNameSpace': $scope.itemTabPassoAcao.prefixoNameSpace
			});
			$scope.msgValidacaoCadPasso = '';
			fecharJanelaAddPassoAcaoModal();
		}
		
		function alterarPassoAcao() {
			for (var i=0; i<$scope.itemTabPasso.acoes.length; i++) {
				if ($scope.itemTabPasso.acoes[i].idFake == $scope.itemTabPassoAcao.idFake) {
					$scope.itemTabPasso.acoes[i] = {
						'id': $scope.itemTabPassoAcao.id,
						'idFake': $scope.itemTabPassoAcao.idFake,
						'transacaoPasso': $scope.itemTabPassoAcao.transacaoPasso,
						'eventoTipo': $scope.itemTabPassoAcao.eventoTipo,     				
						'intervalo': $scope.itemTabPassoAcao.intervalo,
						'qtdeTentativas': $scope.itemTabPassoAcao.qtdeTentativas,
						'filaDestino': $scope.itemTabPassoAcao.filaDestino,
						'destinatario': $scope.itemTabPassoAcao.destinatario,
						'reenvio': $scope.itemTabPassoAcao.reenvio,
						'xpath': $scope.itemTabPassoAcao.xpath,
						'namespace': $scope.itemTabPassoAcao.namespace,
						'prefixoNameSpace': $scope.itemTabPassoAcao.prefixoNameSpace
					};
				}
			}
			fecharJanelaAddPassoAcaoModal();
		}
		
		function removerPassoAcao(itemSel) {
        	if (itemSel.idFake>0) {
        		$scope.dataLoading = true;
                TransacaoService.checkRuleExclusaoTransacaoPassoAcao(itemSel.id.idTransacaoPasso, itemSel.id.idEventoTipo, function (response) {
                    if (response.success) {
                    	$scope.execRemoverPassoAcao(itemSel);
                    	$scope.dataLoading = false;
                    } else {
                    	$scope.dataLoading = false;
                    	notificationsFactory.error(response.data);                	
                    }
                });        		
        	}
        	else {
        		$scope.execRemoverPassoAcao(itemSel);
        	}    		
    	}
    	
    	function execRemoverPassoAcao(itemSel) {
    		for (var i=0; i<$scope.itemTabPasso.acoes.length; i++) {
				if ($scope.itemTabPasso.acoes[i].idFake == itemSel.idFake) {
					$scope.itemTabPasso.acoes.splice(i,1);
				}
			}
    	}	
    	
    	//=================
	    // TAB MOCKUPS
    	//=================
    	$scope.itemTabMockup = {};
    	$scope.limparItemTabMockupSel = limparItemTabMockupSel;
    	$scope.selecionarItemTabMockup = selecionarItemTabMockup;
    	$scope.fecharJanelaAddMockupModal = fecharJanelaAddMockupModal;
    	$scope.adicionarMockup = adicionarMockup;
    	$scope.alterarMockup = alterarMockup;
    	$scope.removerMockup = removerMockup;
    	$scope.execRemoverParametro = execRemoverMockup;
    	$scope.execRemoverMockup = validarFormMockup;
    	$scope.msgValidacaoCadMockup = '';
    	
	    function limparItemTabMockupSel() {
	    	
		}
		
		function selecionarItemTabMockup(itemSel) {
			$scope.itemTabMockup = {
    				'id': itemSel.id, 
    				'capacidade': itemSel.capacidade,
    				'tipo': itemSel.tipo,
    				'situacao': itemSel.situacao
    		};
		}
				
		function fecharJanelaAddMockupModal() {
	    	$scope.limparItemTabMockupSel();
	    	$('#add-transacao-mockup-modal').modal('hide');
		}
		
		function adicionarMockup() {
			if (validarFormMockup()) {
				
				fecharJanelaAddMockupModal();
			}
		}
		
		function alterarMockup() {
			if (validarFormMockup()) {
				
				fecharJanelaAddMockupModal();
			}
		}
		
		function removerMockup(itemSel) {
        	if (itemSel.id>0) {
        		$scope.dataLoading = true;
        		$scope.execRemoverMockup(itemSel);      		
        	}
        	else {
        		$scope.execRemoverMockup(itemSel);
        	}    		
    	}
    	
    	function execRemoverMockup(itemSel) {
    		for (var i=0; i<$scope.item.modelosSimulados.length; i++) {
				if ($scope.item.modelosSimulados[i].id == itemSel.id) {
					$scope.item.modelosSimulados.splice(i,1);
				}
			}
    	}	
				
		function resetValidacaoFormMockup() {
        	$scope.msgValidacaoCadMockup = '';
        }
        
        function validarFormMockup() {
        	var isValid = true;
    		for (var i=0; i<$scope.item.modelosSimulados.length; i++) {
        		
        	}
        	return isValid;
        }
				
    }
    
})();