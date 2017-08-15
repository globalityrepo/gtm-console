(function () {
    'use strict';

    stockModule.controller('EsqueciSenhaController', EsqueciSenhaController);

    EsqueciSenhaController.$inject = ['$scope', '$rootScope', '$location', 'EsqueciSenhaService', 'notificationsFactory'];
    function EsqueciSenhaController($scope, $rootScope, $location, EsqueciSenhaService, notificationsFactory) {
        
    	$scope.item = '';
    	
    	$scope.fecharJanelaModal = fecharJanelaModal;
    	$scope.resetForm = resetForm;
    	$scope.concluir = concluir;
    	        
        function fecharJanelaModal() {
        	$('#esquecisenha-modal').modal('hide');
        	resetForm();
        }
        
        function resetForm() {
        	$scope.item = '';
        	$scope.form.$setPristine();
        }
              
        function concluir() {
	    	$scope.dataLoading = true;
	    	EsqueciSenhaService.executar($scope.item.login, function (response) {
	            if (response.success) {
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