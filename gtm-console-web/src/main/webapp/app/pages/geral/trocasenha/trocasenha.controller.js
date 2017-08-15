(function () {
    'use strict';

    stockModule.controller('TrocaSenhaController', TrocaSenhaController);

    TrocaSenhaController.$inject = ['$scope', '$rootScope', '$location', 'TrocaSenhaService', 'notificationsFactory'];
    function TrocaSenhaController($scope, $rootScope, $location, TrocaSenhaService, notificationsFactory) {
        
    	$scope.item = '';
    	
    	$scope.fecharJanelaModal = fecharJanelaModal;
    	$scope.resetForm = resetForm;
    	$scope.concluir = concluir;
    	$scope.validarSenhaConfirmacao = validarSenhaConfirmacao;
    	        
        function fecharJanelaModal() {
        	$('#trocarsenha-modal').modal('hide');
        	resetForm();
        }
        
        function resetForm() {
        	$scope.item = '';
        	$scope.form.$setPristine();
        }
              
        function validarSenhaConfirmacao() {
        	if ($scope.item.senhaNova != $scope.item.senhaNovaConfirmacao) {
        		return false;
        	}
        	return true;
        }
        
        function concluir() {
        	if (validarSenhaConfirmacao()) {
	        	$scope.dataLoading = true;
	        	TrocaSenhaService.executar($rootScope.globals.currentUser.user.codigo, $scope.item.senhaAtual, $scope.item.senhaNova, function (response) {
	                if (response.success) {
	                	notificationsFactory.success();
	                	$scope.dataLoading = false;
	                	fecharJanelaModal();
	                } else {
	                	notificationsFactory.error(response.data);
	                	$scope.dataLoading = false;
	                }
	            });
        	}
        };
                
    }

})();