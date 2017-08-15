(function () {
    'use strict';

    stockModule.controller('DeParaController', DeParaController);

    DeParaController.$inject = ['$filter', '$translate', '$scope', '$rootScope', '$location', 'DeParaService', 'EntidadeService', 'CommonService', 'UsuarioService', 'RESTfulResourceService', 'notificationsFactory', 'FileUploader'];
    function DeParaController($filter, $translate, $scope, $rootScope, $location, DeParaService, EntidadeService, CommonService, UsuarioService, RESTfulResourceService, notificationsFactory, FileUploader) {
        
    	// Verificando permissão de acesso ao recurso.
    	$rootScope.$watch('AUTORIZADOR.recursos', function() {
    		if (!$rootScope.AUTORIZADOR.isRecursoPermitido($rootScope.AUTORIZADOR.REC_CAD_DEPARA)) {
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
    	$scope.aplicarFiltro = aplicarFiltro;
    	$scope.filtro = '';
    	$scope.item = '';
    	$scope.itemRegistroDePara = '';
    	$scope.itens = '';
    	$scope.entidades = '';
    	$scope.entidadeAplicacoesA = '';
    	$scope.entidadeAplicacoesB = '';
    	$scope.massaDeDadosAplicacaoA = '';
    	$scope.massaDeDadosAplicacaoB = '';
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
    	$scope.downloadTemplateCSV = downloadTemplateCSV;
    	$scope.carregarEntidades = carregarEntidades;
    	$scope.carregarEntidadeAplicacaoA = carregarEntidadeAplicacaoA;
    	$scope.carregarEntidadeAplicacaoB = carregarEntidadeAplicacaoB;
    	$scope.carregarAcessosDelegados = carregarAcessosDelegados;
    	$scope.carregarMassaDeDadosAplicacaoA = carregarMassaDeDadosAplicacaoA;
    	$scope.carregarMassaDeDadosAplicacaoB = carregarMassaDeDadosAplicacaoB;
    	$scope.carregarMassasDeDados = carregarMassasDeDados;
    	$scope.checkAssociacaoEntidadeAplicacao = checkAssociacaoEntidadeAplicacao;
    	$scope.msgValidacaoCadastro = undefined;
    	$scope.validateForm = validateForm;
    	$scope.carregarRegistrosDePara = carregarRegistrosDePara;
    	$scope.acionarInclusao = acionarInclusao;
    	$scope.acionarAlteracao = acionarAlteracao;
    	$scope.acionarExclusao = acionarExclusao;
    	$scope.checkRoleIsUsuarioAdmCrossReference = checkRoleIsUsuarioAdmCrossReference;
    	    	
    	function init() {
    		$rootScope.AUTORIZADOR.carregarAcoesPermitidas($rootScope.AUTORIZADOR.REC_CAD_DEPARA);
    		$scope.checkRoleIsUsuarioAdmCrossReference();
    		$scope.buscar();
    	}
    	
    	function limparItemSelecionado() {
    		$scope.item = {
    				'id': 0,
    				'liberarAcesso': true,
    				'registrosDePara': [],
    				'acessosDelegados': []
    		};
    		$scope.entidadeAplicacoesA = [];
            $scope.entidadeAplicacoesB = [];
            $scope.massaDeDadosAplicacaoA = [];
			$scope.massaDeDadosAplicacaoB = [];
    		$scope.msgValidacaoCadastro = undefined;
    	}
    	
    	function selecionarItem(itemSel,acao) {
    		$scope.checkRoleIsUsuarioAdmCrossReference();
    		if (acao=='alt') {
    			$scope.item = {
        				'id': itemSel.id, 
        				'codigo': itemSel.codigo, 
        				'entidade': itemSel.entidadeAplicacaoDe.entidade,
        				'entidadeAplicacaoDe': itemSel.entidadeAplicacaoDe,
        				'entidadeAplicacaoPara': itemSel.entidadeAplicacaoPara,
        				'registrosDePara': [],
        				'acessosDelegados': [],
        				'usuarioInclusao': itemSel.usuarioInclusao,
        				'usuarioAlteracao': itemSel.usuarioAlteracao,
        				'dataInclusao': itemSel.dataInclusao,
        				'dataAlteracao': itemSel.dataAlteracao,
        				'liberarAcesso': itemSel.liberarAcesso
        		};
    			$scope.carregarEntidades();
    			$scope.carregarRegistrosDePara();
    			$scope.carregarAcessosDelegados();
    		}
    		else {
    			$scope.item = {
	    				'id': itemSel.id
	    		};    			
    		}
    	}
    	
        function buscar() {
        	$scope.dataLoading = true;
        	DeParaService.buscar($scope.pageSize, $scope.currentPage, $rootScope.globals.currentUser.user.id, $scope.filtro, function (response) {
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
        
        function incluir() {
        	if ($scope.validateForm($scope.item)) {
        		$scope.dataLoading = true;
	        	$scope.item.usuarioInclusao = $rootScope.globals.currentUser.user;
	        	DeParaService.incluir($scope.item, function (response) {
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
        	if ($scope.validateForm($scope.item)) {
        		$scope.dataLoading = true;
	        	$scope.item.usuarioAlteracao = $rootScope.globals.currentUser.user;
	        	DeParaService.alterar($scope.item, function (response) {
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
        	DeParaService.excluir($scope.item, $rootScope.globals.currentUser.user.id, function (response) {
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
        
        $('#cadastro-modal').on('shown.bs.modal', function() {
        	$scope.carregarEntidades();
        	$scope.limparFormDePara();
    	});
        
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
        
        function carregarEntidades() {
            $scope.dataLoading = true;
            $scope.entidadeAplicacoesA = [];
            $scope.entidadeAplicacoesB = [];
            if ($scope.item.id==0) {
	            $scope.item.entidadeAplicacaoDe = undefined;
	            $scope.item.entidadeAplicacaoPara = undefined;
            }
            RESTfulResourceService.listar($rootScope.ENTITIES.ENTIDADE, function (response) {
                if (response.success) {
                	$scope.entidades = response.data;
                	if ($scope.item.id>0) {
                		$scope.carregarEntidadeAplicacaoA();
                	}
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        function carregarEntidadeAplicacaoA() {
            $scope.dataLoading = true;
            $scope.entidadeAplicacoesB = [];
            if ($scope.item.id==0) {
	            $scope.item.entidadeAplicacaoDe = undefined;
	            $scope.item.entidadeAplicacaoPara = undefined;
	            $scope.massaDeDadosAplicacaoA = [];
				$scope.massaDeDadosAplicacaoB = [];
            }
            EntidadeService.buscarAllEntidadeAplicacoesByEntidade($scope.item.entidade.id, function (response) {
                if (response.success) {
                	$scope.entidadeAplicacoesA = response.data;
                	if ($scope.item.id>0) {
                		$scope.carregarEntidadeAplicacaoB();
                	}
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        function carregarEntidadeAplicacaoB() {
        	$scope.dataLoading = true;
            DeParaService.buscarEntidadeAplicacoesB($scope.item.entidade.id, $scope.item.entidadeAplicacaoDe.id, function (response) {
                if (response.success) {
                	$scope.entidadeAplicacoesB = response.data;
                	if ($scope.item.id==0) {
                		$scope.item.entidadeAplicacaoPara = undefined;
                		$scope.massaDeDadosAplicacaoA = [];
        				$scope.massaDeDadosAplicacaoB = [];
                	}
                	else {
                		$scope.carregarMassaDeDadosAplicacaoA();
                  	  	$scope.carregarMassaDeDadosAplicacaoB();
                  	  	$scope.msgValidacaoCadastro = undefined;
                	}
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        function carregarMassaDeDadosAplicacaoA() {
            $scope.dataLoading = true;
            DeParaService.buscarMassaDeDadosAplicacaoA($scope.item.entidadeAplicacaoDe.id, function (response) {
                if (response.success) {
                	$scope.massaDeDadosAplicacaoA = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        function carregarMassaDeDadosAplicacaoB() {
            $scope.dataLoading = true;
            DeParaService.buscarMassaDeDadosAplicacaoB($scope.item.entidadeAplicacaoPara.id, function (response) {
                if (response.success) {
                	$scope.massaDeDadosAplicacaoB = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        function carregarMassasDeDados() {
            $scope.checkAssociacaoEntidadeAplicacao();
        };
        
        function checkAssociacaoEntidadeAplicacao() {
			 $scope.dataLoading = true;
			 DeParaService.checkRuleAssociacaoEntidadeAplicacao($scope.item.entidadeAplicacaoDe.id, $scope.item.entidadeAplicacaoPara.id, function (response) {
              if (response.success) {
            	  $scope.carregarMassaDeDadosAplicacaoA();
            	  $scope.carregarMassaDeDadosAplicacaoB();
            	  $scope.msgValidacaoCadastro = undefined;
              	  $scope.dataLoading = false;
              } else {
            	  $scope.msgValidacaoCadastro = $translate.instant('label.entidadeaplicacaoassociacaoredundante');
              	  $scope.dataLoading = false;               	
              }
          });
        };
        
        function carregarRegistrosDePara() {
            $scope.dataLoading = true;
            DeParaService.buscarRegstrosDePara($scope.item.id, function (response) {
                if (response.success) {
                	$scope.item.registrosDePara = response.data;
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        function validateForm(item) {
			if (item.registrosDePara.length==0) {
				$scope.msgValidacaoCadastro = $translate.instant('label.mensagemadicioneumregistrocruzamento');
				return false;
			}
			else if (!item.liberarAcesso && item.acessosDelegados.length==0) {
				$scope.msgValidacaoCadastro = $translate.instant('label.mensagemdelegueumacesso');
				return false;
			}
			$scope.msgValidacaoCadastro = undefined;
			return true;
		}
        
        // ABA DADOS CRUZADOS
        $scope.registroASel = undefined;
        $scope.registroBSel = undefined;
        $scope.limparFormDePara = limparFormDePara;
        $scope.adicionarRegistroDePara = adicionarRegistroDePara;
        $scope.removerRegistroDePara = removerRegistroDePara;
        
        function limparFormDePara() {
        	$scope.registroASel = undefined;
            $scope.registroBSel = undefined;
        }
        
        function adicionarRegistroDePara() {	
			var novoItem = {
				'id': ($scope.item.registrosDePara.length+1)*-1,
				'registroDe': $scope.registroASel,
				'registroPara': $scope.registroBSel
			};
			$scope.item.registrosDePara.unshift(novoItem);
			$scope.limparFormDePara();
		}
        
        function removerRegistroDePara(item) {
			for (var i=0; i<$scope.item.registrosDePara.length; i++) {
        		if ($scope.item.registrosDePara[i].id == item.id) {
        			$scope.item.registrosDePara.splice(i,1);
        		}
        	}	
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
            CommonService.buscarAcessosDelegados($rootScope.AUTORIZADOR.REC_CAD_DEPARA, $scope.item.id, function (response) {
                if (response.success) {
                	$scope.item.acessosDelegados = response.data;
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
    			for (var j=0; j<$scope.item.acessosDelegados.length; j++) {
	        		if ($scope.item.acessosDelegados[j].usuario.id == $scope.usuarios[i].id) {
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
        				'id': ($scope.item.acessosDelegados.length+1)*-1,	
        				'usuario': $scope.usuariosPendentes[i],
        				'dataInclusao': new Date()
        			};
        			$scope.item.acessosDelegados.push(acessoDelegado);
        		}
        	}
        	$scope.fecharJanelaDelegarAcessoModal();        	
        }
        
        function removerAcessoDelegado(item) {
        	for (var i=0; i<$scope.item.acessosDelegados.length; i++) {
        		if ($scope.item.acessosDelegados[i].id == item.id) {
        			$scope.item.acessosDelegados.splice(i,1);
        		}
        	}	
        }
        
        function selecionarTodosDelegarAcesso() {
        	$scope.flagSelTodosDelegarAcesso = !$scope.flagSelTodosDelegarAcesso;
        	for (var i=0; i<$scope.usuariosPendentes.length; i++) {
        		$scope.usuariosPendentes[i].selecionado = $scope.flagSelTodosDelegarAcesso;
        	}
        }
        
		function downloadTemplateCSV() {
			$scope.dataLoading = true;
			CommonService.baixarArquivoExternal('GTM_Template_Importacao_DePara.csv', function (response) {
                if (response.success) {
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);
                }
            });
		}
		
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
			DeParaService.upload(fd, $scope.item.entidadeAplicacaoDe.id, $scope.item.entidadeAplicacaoPara.id, function (response) {
                if (response.success) {
                	var lista = response.data;
                	for (var i=0; i<lista.length; i++) {
                		lista[i].id = ($scope.item.registrosDePara.length+1)*-1;
                		$scope.item.registrosDePara.unshift(lista[i]);
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
          
    }

})();