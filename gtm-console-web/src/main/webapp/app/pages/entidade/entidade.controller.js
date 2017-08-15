(function () {
    'use strict';

    stockModule.controller('EntidadeController', EntidadeController);

    EntidadeController.$inject = ['$filter', '$translate', '$scope', '$rootScope', '$location', 'EntidadeService', 'CommonService', 'UsuarioService', 'RESTfulResourceService', 'notificationsFactory', 'FileUploader'];
    function EntidadeController($filter, $translate, $scope, $rootScope, $location, EntidadeService, CommonService, UsuarioService, RESTfulResourceService, notificationsFactory, FileUploader) {
        
    	// Verificando permissão de acesso ao recurso.
    	$rootScope.$watch('AUTORIZADOR.recursos', function() {
    		if (!$rootScope.AUTORIZADOR.isRecursoPermitido($rootScope.AUTORIZADOR.REC_CAD_ENTIDADE)) {
	    		$location.path('/acessonegado');
	    	}
    	});
    	    	    	
    	// Variável global para armazenar o módulo carregado.
    	$rootScope.globals.moduleLoaded=3;
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
    	$scope.isUserAdmCrossReference = false;
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
    	$scope.fecharJanelaCadastroModal = fecharJanelaCadastroModal;
    	$scope.fecharJanelaDeleteModal = fecharJanelaDeleteModal;
    	$scope.resetCadastroForm = resetCadastroForm;
    	$scope.abrirPagina = abrirPagina;
    	$scope.flagSelTodosAddAplicacao = false;
    	$scope.fecharJanelaAddAplicacaoModal = fecharJanelaAddAplicacaoModal;
    	$scope.aplicacoes = [];
    	$scope.aplicacoesPendentes = [];
    	$scope.carregarAplicacoes = carregarAplicacoes;
    	$scope.selecionarTodosAddAplicacao = selecionarTodosAddAplicacao;
    	$scope.filtrarAplicacoesPendentes = filtrarAplicacoesPendentes;
    	$scope.concluirAdicaoAplicacao = concluirAdicaoAplicacao;
    	$scope.removerAplicacaoAssociada = removerAplicacaoAssociada;    	
    	$scope.aplicarFiltro = aplicarFiltro;
    	$scope.downloadTemplateCSV = downloadTemplateCSV;
    	$scope.msgValidacaoCadastro = undefined;
    	$scope.validateFormPrincipal = validateFormPrincipal;
    	$scope.carregarEntidadeAplicacao = carregarEntidadeAplicacao;
    	$scope.execRemoverAplicacaoAssociada = execRemoverAplicacaoAssociada;
    	$scope.checkRoleIsUsuarioAdmCrossReference = checkRoleIsUsuarioAdmCrossReference;
    	$scope.acionarInclusao = acionarInclusao;
    	$scope.acionarAlteracao = acionarAlteracao;
    	$scope.acionarExclusao = acionarExclusao;
    	    	
    	function init() {
    		$rootScope.AUTORIZADOR.carregarAcoesPermitidas($rootScope.AUTORIZADOR.REC_CAD_ENTIDADE);
    		$scope.checkRoleIsUsuarioAdmCrossReference();
    		$scope.buscar();
    	}
    	
    	function limparItemSelecionado() {
    		$scope.item = {
    				'entidadeAplicacoes': []
    		};
    		$scope.msgValidacaoCadastro = undefined;
    	}
    	
    	function acionarInclusao() {
    		if ($scope.isUserAdmCrossReference && $rootScope.AUTORIZADOR.isAcaoPermitida($rootScope.AUTORIZADOR.ACAO_INCLUIR)) {
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
    		if ($scope.isUserAdmCrossReference && $rootScope.AUTORIZADOR.isAcaoPermitida($rootScope.AUTORIZADOR.ACAO_EXCLUIR) 
    				&& !itemSel.hasRestricaoExclusao) {
    			$scope.selecionarItem(itemSel,'exc');
	    		$('#delete-modal').modal('show');
    		}
    	}
    	
    	function selecionarItem(itemSel,acao) {
    		$scope.checkRoleIsUsuarioAdmCrossReference();
    		$scope.item = angular.copy(itemSel);
    		$scope.item.entidadeAplicacoes = [];
    		$scope.carregarEntidadeAplicacao();
    	}
    	
    	$('#add-aplicacao-modal').on('shown.bs.modal', function() {
    		$scope.carregarAplicacoes();
    	});
    	
    	function fecharJanelaAddAplicacaoModal() {
        	$scope.flagSelTodosAddAplicacao = false;
        	$('#add-aplicacao-modal').modal('hide');
        }
    	
    	function selecionarTodosAddAplicacao() {
        	$scope.flagSelTodosAddAplicacao = !$scope.flagSelTodosAddAplicacao;
        	for (var i=0; i<$scope.aplicacoesPendentes.length; i++) {
        		$scope.aplicacoesPendentes[i].selecionada = $scope.flagSelTodosAddAplicacao;
        	}
        }
    	
    	function filtrarAplicacoesPendentes() {
    		$scope.aplicacoesPendentes = [];
    		for (var i=0; i<$scope.aplicacoes.length; i++) {
    			var achou = false;
    			for (var j=0; j<$scope.item.entidadeAplicacoes.length; j++) {
	        		if ($scope.item.entidadeAplicacoes[j].aplicacao.id == $scope.aplicacoes[i].id) {
	        			achou = true;
	        			break;
	        		}
	        	}
    			if (!achou) {
    				$scope.aplicacoesPendentes.push($scope.aplicacoes[i]);
    			}	
    		}
        }
    	
    	function carregarAplicacoes() {
            $scope.dataLoading = true;
            RESTfulResourceService.listar($rootScope.ENTITIES.APLICACAO, function (response) {
                if (response.success) {
                	$scope.aplicacoes = response.data;
                	$scope.filtrarAplicacoesPendentes();
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        function concluirAdicaoAplicacao() {
        	for (var i=0; i<$scope.aplicacoesPendentes.length; i++) {
        		if ($scope.aplicacoesPendentes[i].selecionada) {
        			var itemApp = {
        					'id': ($scope.item.entidadeAplicacoes.length+1)*-1,
        					'aplicacao': $scope.aplicacoesPendentes[i],
        					'configurado': false
        			}
        			$scope.item.entidadeAplicacoes.push(itemApp);
        			$scope.aplicacoesPendentes.splice(i,1);
        			i--;
        		}
        	}
        	$scope.fecharJanelaAddAplicacaoModal();
    	}
        
        function removerAplicacaoAssociada(item) {
			if (item.id>0) {
        		$scope.dataLoading = true;
                EntidadeService.checkRuleExclusaoEntidadeAplicacao(item.id, function (response) {
                    if (response.success) {
                    	$scope.execRemoverAplicacaoAssociada(item);
                    	$scope.dataLoading = false;
                    } else {
                    	$scope.dataLoading = false;
                    	notificationsFactory.error(response.data);                	
                    }
                });        		
        	}
        	else {
        		$scope.execRemoverAplicacaoAssociada(item);
        	}			
		}
		
		function execRemoverAplicacaoAssociada(item) {
			for (var i=0; i<$scope.item.entidadeAplicacoes.length; i++) {
        		if ($scope.item.entidadeAplicacoes[i].id == item.id) {
        			$scope.item.entidadeAplicacoes.splice(i,1);
        		}
        	}	
		}
		
		function checkRoleIsUsuarioAdmCrossReference() {
            $scope.dataLoading = true;
            UsuarioService.isUsuarioAdmCrossReference($rootScope.globals.currentUser.user.id, function (response) {
                if (response.success) {
                	$scope.isUserAdmCrossReference = (response.data=='true') ? true : false;
                	$scope.dataLoading = false;
                } else {
                	$scope.isUserAdmCrossReference = false;
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        }
        
        function carregarEntidadeAplicacao() {
            $scope.dataLoading = true;
            EntidadeService.buscarEntidadeAplicacoesByEntidade($scope.item.id, $rootScope.globals.currentUser.user.id, function (response) {
                if (response.success) {
                	$scope.item.entidadeAplicacoes = response.data;
                	for (var i=0; i<$scope.item.entidadeAplicacoes.length; i++) {
                		$scope.item.entidadeAplicacoes[i].configurado = true;
                	}
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        }
    	
        function buscar() {
            $scope.dataLoading = true;
            EntidadeService.buscar($scope.pageSize, $scope.currentPage, $rootScope.globals.currentUser.user.id, $scope.filtro, function (response) {
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
        	if ($scope.validateFormPrincipal()) {
	        	$scope.dataLoading = true;
	        	$scope.item.usuarioInclusao = $rootScope.globals.currentUser.user;
	        	EntidadeService.incluir($scope.item, function (response) {
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
        	if ($scope.validateFormPrincipal()) {
	        	$scope.dataLoading = true;
	        	$scope.item.usuarioAlteracao = $rootScope.globals.currentUser.user;
	        	EntidadeService.alterar($scope.item, function (response) {
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
        
        function excluir() {
        	$scope.dataLoading = true;
        	EntidadeService.excluir($scope.item, $rootScope.globals.currentUser.user.id, function (response) {
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
        
        function validateFormPrincipal() {
			if ($scope.item.entidadeAplicacoes.length==0) {
				$scope.msgValidacaoCadastro = $translate.instant('label.mensagemadicioneumaaplicacao');
				return false;
			}
			else {
				for (var i=0; i<$scope.item.entidadeAplicacoes.length; i++) {
					if (!$scope.item.entidadeAplicacoes[i].configurado) {
						$scope.msgValidacaoCadastro = $translate.instant('label.mensagemconfiguracaopendente');
						return false;
					}
				}
			}			
			$scope.msgValidacaoCadastro = undefined;
			return true;
		}
        
        //============
	    // TAB APLICACAO
	    //============
		$scope.itemTabAplicacao = '';
		$scope.selecionarItemTabAplicacao = selecionarItemTabAplicacao;
		$scope.fecharJanelaConfigurarAplicacaoModal = fecharJanelaConfigurarAplicacaoModal;
		$scope.adicionarRegistroMassaDeDados = adicionarRegistroMassaDeDados;
		$scope.removerRegistroMassaDeDados = removerRegistroMassaDeDados;
		$scope.validateFormAplicacao = validateFormAplicacao;
		$scope.msgValidacaoAplicacaoCadastro = undefined;
		$scope.concluirConfigurarAplicacao = concluirConfigurarAplicacao;
		$scope.carregarAcessosDelegados = carregarAcessosDelegados;
		$scope.carregarRegistros = carregarRegistros;
		$scope.execRemoverRegistroMassaDeDados = execRemoverRegistroMassaDeDados;
		$scope.onBlurRegistro = onBlurRegistro;
		
		function selecionarItemTabAplicacao(itemSel) {
			$scope.itemTabAplicacao = {
					'id': itemSel.id,
					'liberarAcesso': (itemSel.configurado) ? itemSel.liberarAcesso : true,
    				'registros': (itemSel.registros) ? itemSel.registros : [],
    				'acessosDelegados': (itemSel.acessosDelegados) ? itemSel.acessosDelegados : [],
    				'configurado': itemSel.configurado,
    				'aplicacao': itemSel.aplicacao,
    				'entidade': itemSel.entidade
    		};
			if (itemSel.id>0) {
				$scope.carregarRegistros();
				$scope.carregarAcessosDelegados();
			}
		}
		
		function fecharJanelaConfigurarAplicacaoModal() {
			$scope.msgValidacaoAplicacaoCadastro = undefined;
			$('#edit-aplicacao-modal').modal('hide');
	    }
		
		function downloadTemplateCSV() {
			$scope.dataLoading = true;
			CommonService.baixarArquivoExternal('GTM_Template_Importacao_MassaDeDados.csv', function (response) {
                if (response.success) {
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);
                }
            });
		}
		
		function adicionarRegistroMassaDeDados() {	
			var novoItem = {
				'id': ($scope.itemTabAplicacao.registros.length+1)*-1,
				'codigo': '',
				'descricao': '',
				'hasEdicao': true
  			};
			$scope.itemTabAplicacao.registros.unshift(novoItem);
		}
		
		function removerRegistroMassaDeDados(item) {
			if (item.id>0) {
        		$scope.dataLoading = true;
                EntidadeService.checkRuleExclusaoRegistro(item.id, function (response) {
                    if (response.success) {
                    	$scope.execRemoverRegistroMassaDeDados(item);
                    	$scope.dataLoading = false;
                    } else {
                    	$scope.dataLoading = false;
                    	notificationsFactory.error(response.data);                	
                    }
                });        		
        	}
        	else {
        		$scope.execRemoverRegistroMassaDeDados(item);
        	}			
		}
		
		function execRemoverRegistroMassaDeDados(item) {
			for (var i=0; i<$scope.itemTabAplicacao.registros.length; i++) {
        		if ($scope.itemTabAplicacao.registros[i].id == item.id) {
        			$scope.itemTabAplicacao.registros.splice(i,1);
        		}
        	}	
		}
		
		function validateFormAplicacao(item) {
			if (item.registros.length==0) {
				$scope.msgValidacaoAplicacaoCadastro = $translate.instant('label.mensagemadicioneumregistromassadedados');
				return false;
			}
			else if (!item.liberarAcesso && item.acessosDelegados.length==0) {
				$scope.msgValidacaoAplicacaoCadastro = $translate.instant('label.mensagemdelegueumacesso');
				return false;
			}
			$scope.msgValidacaoAplicacaoCadastro = undefined;
			return true;
		}
		
		function concluirConfigurarAplicacao() {
			if ($scope.validateFormAplicacao($scope.itemTabAplicacao)) {
				for (var i=0; i<$scope.item.entidadeAplicacoes.length; i++) {
					if ($scope.item.entidadeAplicacoes[i].id == $scope.itemTabAplicacao.id) {
						$scope.item.entidadeAplicacoes[i] = angular.copy($scope.itemTabAplicacao);
						$scope.item.entidadeAplicacoes[i].configurado = true;
						break;
					}
				}
				$scope.fecharJanelaConfigurarAplicacaoModal();
			}
		}		
		
		function carregarRegistros() {
			 $scope.dataLoading = true;
            EntidadeService.buscarRegistrosByEntidadeAplicacao($scope.itemTabAplicacao.id, function (response) {
                if (response.success) {
                	$scope.itemTabAplicacao.registros = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        function onBlurRegistro(item) {
            if (item.id>0) {
	        	if (item.codigo!=item.codigoOriginal || item.descricao!=item.descricaoOriginal) {
	            	item.hasEdicao = true;
	            }
	        	else {
	        		item.hasEdicao = false;
	        	}
            }
        };
		
		// Trecho para tratamento do upload de arquivo de importação.
		$scope.importarArquivo = importarArquivo;
		$scope.fecharJanelaImportacaoModal = fecharJanelaImportacaoModal;
		$scope.fileSelected = false;
		$scope.fileValid = false;
		$scope.validarArquivo = validarArquivo;
		$scope.msgValidacaoArquivo = null;
		$scope.resetVariablesImportacao = resetVariablesImportacao; 
		
		$('#import-arquivo-modal').on('shown.bs.modal', function() {
    		$scope.resetVariablesImportacao();
		});
		
		function fecharJanelaImportacaoModal() {
			$scope.resetVariablesImportacao();
			$('#import-arquivo-modal').modal('hide');
		}
		
		function validarArquivo(campo) {
			$scope.resetVariablesImportacao();
			if (campo.files.length>0) {
				var path = campo.files[0].name; 
				var ext = path.split('/').pop();
				ext = ext.indexOf('.') < 1 ? '' : ext.split('.').pop();
				if (ext.toUpperCase()!='CSV') {
					$scope.msgValidacaoArquivo = $translate.instant('label.tipoarquivoinvalido');
				}
				else {
					$scope.fileValid = !$scope.fileValid;
					$scope.fileSelected = !$scope.fileSelected;
				}
				$scope.$apply();
			}
		}
		
		function resetVariablesImportacao() {
			$scope.msgValidacaoArquivo = null;
			$scope.fileSelected = false;
			$scope.fileValid = false;
			$scope.$apply();
		}
		
        function importarArquivo() {
        	var fd = new FormData();
            fd.append('file', $scope.fileArray[0]);
        	$scope.dataLoading = true;
			EntidadeService.upload(fd, function (response) {
                if (response.success) {
                	var lista = response.data;
                	for (var i=0; i<lista.length; i++) {
                		lista[i].id = ($scope.itemTabAplicacao.registros.length+1)*-1;
                		$scope.itemTabAplicacao.registros.unshift(lista[i]);
                	}	
                	$scope.dataLoading = false;
                	notificationsFactory.success();
                	$scope.fecharJanelaImportacaoModal();
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);
                }
            });
		}
          
          // ABA ACESSOS DELEGADOS.
          
	      $scope.usuarios = '';
	      $scope.usuariosPendentes = '';
      	  $scope.carregarUsuarios = carregarUsuarios;
      	  $scope.concluirDelegarAcesso = concluirDelegarAcesso;
      	  $scope.selecionarTodosDelegarAcesso = selecionarTodosDelegarAcesso;
      	  $scope.fecharJanelaDelegarAcessoModal = fecharJanelaDelegarAcessoModal;
          $scope.flagSelTodosDelegarAcesso = false;
          $scope.filtroUsuarioValor = '';
          $scope.filtrarUsuariosLoad = filtrarUsuariosLoad;
          $scope.removerAcessoDelegado = removerAcessoDelegado;
          
          $scope.filterUsuario = function(data) {
          	return (data.codigo + data.nome).indexOf($scope.filtroUsuarioValor) >= 0;
          };
          
          function carregarAcessosDelegados() {
  			  $scope.dataLoading = true;
              CommonService.buscarAcessosDelegados($rootScope.AUTORIZADOR.REC_CAD_ENTIDADE, $scope.itemTabAplicacao.id, function (response) {
                  if (response.success) {
                  	$scope.itemTabAplicacao.acessosDelegados = response.data;
                  	$scope.dataLoading = false;
                  } else {
                  	$scope.dataLoading = false;
                  	notificationsFactory.error(response.data);                	
                  }
              });
          };
          
          function carregarUsuarios() {
              $scope.dataLoading = true;
              UsuarioService.listar(function (response) {
                  if (response.success) {
                  	$scope.usuarios = response.data;
                  	$scope.filtrarUsuariosLoad();
                  	$scope.dataLoading = false;
                  } else {
                  	$scope.dataLoading = false;
                  	notificationsFactory.error(response.data);                	
                  }
              });
          };
          
          function filtrarUsuariosLoad() {
      		$scope.usuariosPendentes = [];
      		for (var i=0; i<$scope.usuarios.length; i++) {
      			var achou = false;
      			for (var j=0; j<$scope.itemTabAplicacao.acessosDelegados.length; j++) {
  	        		if ($scope.itemTabAplicacao.acessosDelegados[j].usuario.id == $scope.usuarios[i].id) {
  	        			achou = true;
  	        			break;
  	        		}
  	        	}
      			if (!achou) {
      				$scope.usuariosPendentes.push($scope.usuarios[i]);
      			}	
      		}
          }
          
          $('#delegar-acesso-modal').on('shown.bs.modal', function() {
          	$scope.carregarUsuarios();
      	  });
          
          function fecharJanelaDelegarAcessoModal() {
          	$scope.flagSelTodosDelegarAcesso = false;
          	$('#delegar-acesso-modal').modal('hide');
          }
          
          function concluirDelegarAcesso() {
          	for (var i=0; i<$scope.usuariosPendentes.length; i++) {
          		if ($scope.usuariosPendentes[i].selecionado) {
          			var acessoDelegado = {
          				'id': ($scope.itemTabAplicacao.acessosDelegados.length+1)*-1,	
          				'usuario': $scope.usuariosPendentes[i],
          				'dataInclusao': new Date()
          			};
          			$scope.itemTabAplicacao.acessosDelegados.push(acessoDelegado);
          		}
          	}
          	$scope.fecharJanelaDelegarAcessoModal();        	
          }
          
          function removerAcessoDelegado(item) {
          	for (var i=0; i<$scope.itemTabAplicacao.acessosDelegados.length; i++) {
          		if ($scope.itemTabAplicacao.acessosDelegados[i].id == item.id) {
          			$scope.itemTabAplicacao.acessosDelegados.splice(i,1);
          		}
          	}	
          }
          
          function selecionarTodosDelegarAcesso() {
          	$scope.flagSelTodosDelegarAcesso = !$scope.flagSelTodosDelegarAcesso;
          	for (var i=0; i<$scope.usuariosPendentes.length; i++) {
          		$scope.usuariosPendentes[i].selecionado = $scope.flagSelTodosDelegarAcesso;
          	}
          }
          
    }

})();