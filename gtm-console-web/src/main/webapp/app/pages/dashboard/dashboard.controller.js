(function () {
    'use strict';

    stockModule.controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$scope', '$translate', '$rootScope', '$location', '$interval', 'DashboardService', 'TransacaoService', 'RESTfulResourceService', 'notificationsFactory'];
    function DashboardController($scope, $translate, $rootScope, $location, $interval, DashboardService, TransacaoService, RESTfulResourceService, notificationsFactory) {
        
    	// Verificando permissão de acesso ao recurso.
    	$rootScope.$watch('AUTORIZADOR.recursos', function() {
    		if (!$rootScope.AUTORIZADOR.isRecursoPermitido($rootScope.AUTORIZADOR.REC_CON_DASHBOARD)) {
	    		$location.path('/acessonegado');
	    	}
    	});
    	    	
    	// Variável global para armazenar o útimo módulo carregado.
    	$rootScope.globals.moduleLoaded=1;
    	$rootScope.aplicarBackground = false;
    	
    	$scope.itemSelecionado = {};
    	
    	$scope.verItemGrid = function(item) {
    		$scope.itemSelecionado = item;
    		$scope.carregarTransacaoParametroByIdTransacao($scope.itemSelecionado.item.idTransacao);
    	};
    	
    	$scope.exportarItemGrid = function(item) {
    		$scope.itemSelecionado = item;
    		$scope.baixarEventoInstanciaConteudo($scope.itemSelecionado.item.id);
    	};
    	
    	var refreshing;
    	$scope.atualizacaoAutomatica = false;
    	$scope.tratarAtualizacaoAutomatica = tratarAtualizacaoAutomatica;
    	$scope.startAtualizacaoAutomatica = startAtualizacaoAutomatica;
    	$scope.stopAtualizacaoAutomatica = stopAtualizacaoAutomatica;
    	$scope.executarPesquisa = executarPesquisa;
    	$scope.hasFiltroChanged = false;
    	$scope.cancelarJanelaOpcoesFiltro = cancelarJanelaOpcoesFiltro;
    	$scope.restaurarJanelaOpcoesFiltro = restaurarJanelaOpcoesFiltro;
    	
    	$scope.filtrar = filtrar;
    	$scope.atualizar = atualizar;
    	$scope.restaurar = restaurar;
    	$scope.mudarQtdeRegistros = mudarQtdeRegistros;
    	$scope.qtdeRegistros = 25;
    	
    	$scope.fecharJanelaVisualizarEventoModal = fecharJanelaVisualizarEventoModal;
    	$scope.carregarTransacaoParametroByIdTransacao = carregarTransacaoParametroByIdTransacao;
    	$scope.baixarEventoInstanciaConteudo = baixarEventoInstanciaConteudo;
    	
    	// Controles de Filtros
    	$scope.fecharJanelaFiltroTransacaoModal = fecharJanelaFiltroTransacaoModal;
    	$scope.concluirFiltroTransacao = concluirFiltroTransacao;
    	
    	$scope.transacoes = {};
    	$scope.selecionarTodosFiltroTransacao = selecionarTodosFiltroTransacao;
    	$scope.flagSelTodosFiltroTransacao = true;
    	$scope.carregarTransacoesByGrupos = carregarTransacoesByGrupos;
    	
    	$scope.flagSelTodosFiltroNivelEvento = true;
    	$scope.niveisEvento = {};
    	$scope.carregarNivelEvento = carregarNivelEvento;
    	$scope.selecionarTodosFiltroNivelEvento = selecionarTodosFiltroNivelEvento;
    	
    	$scope.flagSelTodosFiltroGrupo = true;
    	$scope.grupos = {};
    	$scope.carregarGrupos = carregarGrupos;
    	$scope.selecionarTodosFiltroGrupo = selecionarTodosFiltroGrupo;
    	    	
    	// Variaveis de backup para restauração ao cancelar filtro.
    	$scope.gruposBkp = {};
    	$scope.transacoesBkp = {};
    	$scope.niveisEventoBkp = {};
    	$scope.filtroBkp = {};
    	
    	// Controles da Tree Principal.
    	$scope.items = {};
    	$scope.initMainTree = initMainTree;
    	$scope.loadMainTree = loadMainTree;
    	
    	$scope.initFiltro = function(init) {
        	$scope.filtro = {
        			"dataInicial": "",
        			"dataFinal": "",
        			"grupos": new Array(),
        			"transacoes": new Array(),
        			"eventoNiveis": new Array(),
        			"qtdeRegistrosConsulta": 0
        	}
        	if (init) {
        		$scope.carregarNivelEvento(init);        		
        	}
        }
    	
    	$rootScope.$watch('config', function() {
    		initController();
    	});
    	    	
        initController();

        function initController() {
        	$scope.initMainTree();
        	$scope.initFiltro(true);
        }
        
	    function initMainTree() {
	        // Controles da Tree Principal
	    	$scope.dataLoading = true;
	    	$scope.mainTree = {};
	        $scope.mainTree.items = new Array();
	        $scope.mainTree.my_tree = getMainTree($scope.mainTree.items, 'id', 'idPai');
	        $scope.mainTree.tree_data = $scope.mainTree.my_tree;   
	        $scope.mainTree.expanding_property = "descricao";
	        $scope.mainTree.expanding_displayName = $translate.instant('label.transacao') + " / " + $translate.instant('label.evento');
	        $scope.mainTree.data_col_displayname = $translate.instant('label.data') + " / " + $translate.instant('label.hora');
	        $scope.mainTree.col_defs = [
	        	{ field: "dataHora", displayName: '', type: "label" }
	        ];
	        $scope.dataLoading = false;
	    }
	    
        function loadMainTree() {
        	$scope.initMainTree();
        	$scope.dataLoading = true;
        	for (var i=0; i<$scope.items.length; i++) {
        		$scope.mainTree.items.push({"id": $scope.items[i].id, "idPai":null, "descricao": $scope.items[i].descricao, "status": $scope.items[i].status, "mensagem": $scope.items[i].mensagem, "dataHora": $scope.items[i].dataFormatada, "item": $scope.items[i].item, "hasConteudo": $scope.items[i].hasConteudo, "level": 1, "showform": false});
    			for (var j=0; j<$scope.items[i].filhos.length; j++) {
    				$scope.mainTree.items.push({"id": $scope.items[i].filhos[j].id, "idPai": $scope.items[i].filhos[j].idPai, "descricao": $scope.items[i].filhos[j].descricao, "status": $scope.items[i].filhos[j].status, "mensagem": $scope.items[i].filhos[j].mensagem, "dataHora": $scope.items[i].filhos[j].dataFormatada, "item": $scope.items[i].filhos[j].item, "hasConteudo": $scope.items[i].filhos[j].hasConteudo, "level": 2, "showform": false});
    			}
        	}
        	$scope.mainTree.my_tree = getMainTree($scope.mainTree.items, 'id', 'idPai');
            $scope.mainTree.tree_data = $scope.mainTree.my_tree;
            $scope.dataLoading = false;
        }
        
        $scope.mainTree.my_tree_handler = function(branch){
        	console.log('you clicked on', branch)
        }
        
	    function getMainTree(data, primaryIdName, parentIdName){
	    	if(!data || data.length==0 || !primaryIdName ||!parentIdName)
	    		return [];
	
	    	var tree = [],
	    		rootIds = [],
	    		item = data[0],
	    		primaryKey = item[primaryIdName],
	    		treeObjs = {},
	    		parentId,
	    		parent,
	    		len = data.length,
	    		i = 0;
	    	
	    	while(i<len){
	    		item = data[i++];
	    		primaryKey = item[primaryIdName];			
	    		treeObjs[primaryKey] = item;
	    		parentId = item[parentIdName];
	
	    		if(parentId){
	    			parent = treeObjs[parentId];	
	
	    			if(parent.children){
	    				parent.children.push(item);
	    			}
	    			else{
	    				parent.children = [item];
	    			}
	    		}
	    		else{
	    			rootIds.push(primaryKey);
	    		}
	    	}
	
	    	for (var i = 0; i < rootIds.length; i++) {
	    		tree.push(treeObjs[rootIds[i]]);
	    	};
	
	    	return tree;
	    }
	    
	    function selecionarTodosFiltroTransacao() {
        	$scope.flagSelTodosFiltroTransacao = !$scope.flagSelTodosFiltroTransacao;
        	for (var i=0; i<$scope.transacoes.length; i++) {
        		$scope.transacoes[i].selecionado = $scope.flagSelTodosFiltroTransacao;
        	}
        }
	    
	    function carregarTransacoesByGrupos(init) {
	    	$scope.dataLoading = true;
            var filtroObj = {
            		"grupos": []
            };
	    	for (var i=0; i<$scope.grupos.length; i++) {
            	if ($scope.grupos[i].selecionado) {
            		filtroObj.grupos.push($scope.grupos[i]);
            	}
            }
	    	TransacaoService.buscarTransacoesByGrupos(filtroObj, function (response) {
                if (response.success) {
                	$scope.transacoes = response.data;
                	for (var i=0; i<$scope.transacoes.length; i++) {
                		$scope.transacoes[i].selecionado = true;
                	}
        	    	if (init) {
        	    		$scope.filtrar();
        	    		$scope.hasFiltroChanged = false;
                	}
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        $(document).ready(function(){
   			var date_input=$('input[name="txDe"]'); //our date input has the name "date"
   			var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
   			date_input.datepicker({
   				format: 'dd/mm/yyyy',
   				container: container,
   				todayHighlight: true,
   				autoclose: true,
   			})
    	})
    	
    	$(document).ready(function(){
   			var date_input=$('input[name="txAte"]'); //our date input has the name "date"
   			var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
   			date_input.datepicker({
   				format: 'dd/mm/yyyy',
   				container: container,
   				todayHighlight: true,
   				autoclose: true,
   			})
    	})
    	
    	function restaurarJanelaOpcoesFiltro() {
        	// Restaurando variaveis de backup.
        	for (var i=0; i<$scope.grupos.length; i++) {
        		for (var j=0; j<$scope.gruposBkp.length; j++) {
        			if ($scope.gruposBkp[j].id==$scope.grupos[i].id) {
        				$scope.grupos[i].selecionado = $scope.gruposBkp[j].selecionado;
        				break;
        			}
        		}
        	}
        	for (var i=0; i<$scope.transacoes.length; i++) {
        		for (var j=0; j<$scope.transacoesBkp.length; j++) {
        			if ($scope.transacoesBkp[j].id==$scope.transacoes[i].id) {
        				$scope.transacoes[i].idTransacaoInstancia = $scope.transacoesBkp[j].idTransacaoInstancia;
        				$scope.transacoes[i].idParametro = $scope.transacoesBkp[j].idParametro;
        				$scope.transacoes[i].valorParametro = $scope.transacoesBkp[j].valorParametro;
        				$scope.transacoes[i].selecionado = $scope.transacoesBkp[j].selecionado;
        				$scope.transacoes[i].paarametros = $scope.transacoesBkp[j].parametros;
        				break;
        			}
        		}
        	}
        	for (var i=0; i<$scope.niveisEvento.length; i++) {
        		for (var j=0; j<$scope.niveisEventoBkp.length; j++) {
        			if ($scope.niveisEventoBkp[j].id==$scope.niveisEvento[i].id) {
        				$scope.niveisEvento[i].selecionado = $scope.niveisEventoBkp[j].selecionado;
        				break;
        			}
        		}
        	}
        	$scope.filtro.dataInicial = $scope.filtroBkp.dataInicial;
        	$scope.filtro.dataFinal = $scope.filtroBkp.dataFinal;
        	$scope.carregarTransacoesByGrupos(false);
        }
    	    	
    	function cancelarJanelaOpcoesFiltro() {
    		$scope.restaurarJanelaOpcoesFiltro();
        	$scope.fecharJanelaFiltroTransacaoModal();
        }
    	
        function fecharJanelaFiltroTransacaoModal() {
        	$('#filter-transacao-modal').modal('hide');
        }
        
        function fecharJanelaVisualizarEventoModal() {
        	$('#view-evento-modal').modal('hide');
        }
        
        function baixarEventoInstanciaConteudo(idEventoInstancia) {
        	DashboardService
        	$scope.dataLoading = true;
        	DashboardService.baixarArquivoConteudo(idEventoInstancia, function (response) {
                if (response.success) {
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);
                }
            });
        };
        
        function carregarTransacaoParametroByIdTransacao(idTransacao) {
            $scope.dataLoading = true;
            TransacaoService.buscarTransacaoParametroByIdTransacao(idTransacao, function (response) {
                if (response.success) {
                	$scope.itemSelecionado.item.parametros = response.data;
                	$scope.itemSelecionado.item.parametrosStr = '';
                	for (var i=0; i<$scope.itemSelecionado.item.parametros.length; i++) {
                		if ($scope.itemSelecionado.item.parametrosStr!='') {
                			$scope.itemSelecionado.item.parametrosStr += ', ';
                		}
                		$scope.itemSelecionado.item.parametrosStr += $scope.itemSelecionado.item.parametros[i].nome;
                	}
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
                
        function carregarNivelEvento(init) {
            $scope.dataLoading = true;
            RESTfulResourceService.listar($rootScope.ENTITIES.EVENTO_NIVEL, function (response) {
                if (response.success) {
                	$scope.niveisEvento = response.data;
                	for (var i=0; i<$scope.niveisEvento.length; i++) {
                		$scope.niveisEvento[i].selecionado = true;
                	}
                	$scope.carregarGrupos(init);
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
                
        function selecionarTodosFiltroNivelEvento() {
        	$scope.flagSelTodosFiltroNivelEvento = !$scope.flagSelTodosFiltroNivelEvento;
        	for (var i=0; i<$scope.niveisEvento.length; i++) {
        		$scope.niveisEvento[i].selecionado = $scope.flagSelTodosFiltroNivelEvento;
        	}
        }
        
        function carregarGrupos(init) {
            $scope.dataLoading = true;
            RESTfulResourceService.listar($rootScope.ENTITIES.GRUPO, function (response) {
                if (response.success) {
                	$scope.grupos = response.data;
                	for (var i=0; i<$scope.grupos.length; i++) {
                		$scope.grupos[i].selecionado = true;
                	}
                	$scope.carregarTransacoesByGrupos(init);
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        };
        
        function selecionarTodosFiltroGrupo() {
        	$scope.flagSelTodosFiltroGrupo = !$scope.flagSelTodosFiltroGrupo;
        	for (var i=0; i<$scope.grupos.length; i++) {
        		$scope.grupos[i].selecionado = $scope.flagSelTodosFiltroGrupo;
        	}
        	carregarTransacoesByGrupos(false);
        }
        
        function concluirFiltroTransacao() {
        	fecharJanelaFiltroTransacaoModal();
        }
        
        function executarPesquisa() {
        	$scope.dataLoading = true;
        	DashboardService.buscar($scope.filtro, function (response) {
                if (response.success) {
                	$scope.items = response.data;
                	$scope.loadMainTree();
                	// Definindo variáveis de backup.
                	$scope.gruposBkp = new Array();
                	for (var i=0; i<$scope.grupos.length; i++) {
                		$scope.gruposBkp.push({ 
                			"id": $scope.grupos[i].id, 
                			"selecionado": ($scope.grupos[i].selecionado) ? true : false
                		});
                	}
                	$scope.transacoesBkp = new Array();
                	for (var i=0; i<$scope.transacoes.length; i++) {
                		$scope.transacoesBkp.push({ 
                			"id": $scope.transacoes[i].id, 
                			"idTransacaoInstancia": $scope.transacoes[i].idTransacaoInstancia,
                			"idParametro": $scope.transacoes[i].idParametro,
                			"valorParametro": $scope.transacoes[i].valorParametro,
                			"selecionado": ($scope.transacoes[i].selecionado) ? true : false,
                			"parametros": $scope.transacoes[i].parametros
                		});
                	}
                	$scope.niveisEventoBkp = new Array();
                	for (var i=0; i<$scope.niveisEvento.length; i++) {
                		$scope.niveisEventoBkp.push({ 
                			"id": $scope.niveisEvento[i].id, 
                			"selecionado": ($scope.niveisEvento[i].selecionado) ? true : false
                		});
                	}
                	$scope.filtroBkp.dataInicial = $scope.filtro.dataInicial;
                	$scope.filtroBkp.dataFinal = $scope.filtro.dataFinal;
                	$scope.fecharJanelaFiltroTransacaoModal();
                	$scope.dataLoading = false;
                } else {
                	$scope.dataLoading = false;
                	notificationsFactory.error(response.data);                	
                }
            });
        }
        
        function filtrar() {
        	// Recuperando grupos selecionados.
        	$scope.filtro.grupos = new Array();
        	for (var i=0; i<$scope.grupos.length; i++) {
        		if ($scope.grupos[i].selecionado) {
    				$scope.filtro.grupos.push($scope.grupos[i]);
    			}
        	}
        	// Recuperando transações selecionadas.
        	$scope.filtro.transacoes = new Array();
        	var countTransacoes = 0;
        	for (var i=0; i<$scope.transacoes.length; i++) {
    			if ($scope.transacoes[i].selecionado) {
        			if (!$scope.transacoes[i].idParametro) {
        				$scope.transacoes[i].idParametro = 0;
        			}
    				$scope.filtro.transacoes.push($scope.transacoes[i]);
    			}
    			else {
    				$scope.transacoes[i].idTransacaoInstancia = "";
    				$scope.transacoes[i].idParametro = "";
    				$scope.transacoes[i].valorParametro = "";
    			}
    			// Verificando se houve alterações no filtro original.
    			if ($scope.transacoes[i].idTransacaoInstancia!="" 
    				|| $scope.transacoes[i].idParametro!=""
    					|| $scope.transacoes[i].valorParametro!="") {
    				$scope.hasFiltroChanged = true;
    			}
        	}
        	// Recuperando níveis de eventos selecionados.
        	$scope.filtro.eventoNiveis = new Array();
        	for (var i=0; i<$scope.niveisEvento.length; i++) {
        		if ($scope.niveisEvento[i].selecionado) {
    				$scope.filtro.eventoNiveis.push($scope.niveisEvento[i]);
    			}
        	}
        	// Verificando se houve alterações no filtro original.
        	if (!$scope.hasFiltroChanged && ($scope.filtro.dataInicial!="" || $scope.filtro.dataFinal!="" 
        		|| $scope.grupos.length!=$scope.filtro.grupos.length
        		|| $scope.niveisEvento.length!=$scope.filtro.eventoNiveis.length
        			|| $scope.transacoes.length!=$scope.filtro.transacoes.length)) {
        		$scope.hasFiltroChanged = true;
        	}
        	// Definindo a quantidade de registros da consulta.
        	$scope.filtro.qtdeRegistrosConsulta = $scope.qtdeRegistros;
        	$scope.executarPesquisa();
        }
        
        function atualizar() {
        	$scope.executarPesquisa();
        }
        
        function restaurar() {
        	$scope.initFiltro(false);
        	for (var i=0; i<$scope.grupos.length; i++) {
        		$scope.grupos[i].selecionado =true;
        	}
        	for (var i=0; i<$scope.niveisEvento.length; i++) {
        		$scope.niveisEvento[i].selecionado =true;
        	}
        	$scope.flagSelTodosFiltroGrupo = true;
        	$scope.flagSelTodosFiltroTransacao = true;
        	$scope.flagSelTodosFiltroNivelEvento = true;
        	$scope.hasFiltroChanged = false;
        	$scope.carregarTransacoesByGrupos(true);
        }
        
        function mudarQtdeRegistros() {
        	$scope.filtro.qtdeRegistrosConsulta = $scope.qtdeRegistros;
        	$scope.executarPesquisa();
        }
        
        function tratarAtualizacaoAutomatica(flag) {
        	$scope.atualizacaoAutomatica = flag;
        	if (flag) {
        		$scope.startAtualizacaoAutomatica();
        	}
        	else {
        		$scope.stopAtualizacaoAutomatica();
        	}
        }
        
        function startAtualizacaoAutomatica() {
        	$scope.stopAtualizacaoAutomatica();
        	refreshing = $interval( function() { $scope.atualizar(); }, 60000);
        }
        
        function stopAtualizacaoAutomatica() {
        	$interval.cancel(refreshing);
        }
        
        $scope.$on('$destroy', function() {
        	$scope.stopAtualizacaoAutomatica();
        });
        
    }

})();