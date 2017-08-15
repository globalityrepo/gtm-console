(function () {
    'use strict';

    stockModule.controller('ConfiguracaoController', ConfiguracaoController);

    ConfiguracaoController.$inject = ['$scope', '$rootScope', '$location', '$translate', 'ConfiguracaoService', 'notificationsFactory'];
    function ConfiguracaoController($scope, $rootScope, $location, $translate, ConfiguracaoService, notificationsFactory) {
    	
    	$scope.item = '';
    	
    	$scope.fecharJanelaModal = fecharJanelaModal;
    	$scope.concluir = concluir;
    	$scope.consultar = consultar;
    	
    	$('#configuracao-modal').on('shown.bs.modal', function() {
    		$scope.consultar();
    	})
    	    	
    	function fecharJanelaModal() {
        	$('#configuracao-modal').modal('hide');
        }
        
    	function consultar() { 
    		$scope.dataLoading = true;
	    	ConfiguracaoService.buscar(function (response) {
	            if (response.success) {
	            	$scope.item = response.data;
	            	$scope.dataLoading = false;
	            } else {
	            	notificationsFactory.error(response.data);
	            	$scope.dataLoading = false;
	            }
	        });
    	}
    	
    	function concluir() {
	    	$scope.dataLoading = true;
	    	ConfiguracaoService.alterar($scope.item, function (response) {
	            if (response.success) {
	            	$translate.use($scope.item.locale);
	            	$rootScope.carregarConfiguracoesGerais();
	            	notificationsFactory.success();
	            	$scope.dataLoading = false;
	            	fecharJanelaModal();
	            } else {
	            	notificationsFactory.error(response.data);
	            	$scope.dataLoading = false;
	            }
	        });
        };
                
    }

})();

