(function () {
    'use strict';

    stockModule.factory('DashboardService', DashboardService);

    DashboardService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout'];
    function DashboardService($http, $cookieStore, $rootScope, $timeout) {
        var service = {};
        
        service.buscar = buscar;
        service.baixarArquivoConteudo = baixarArquivoConteudo;
       
        return service;
        
        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        }
        
        function buscar(filtro, callback) {
        	var response;
        	$http.post('/gtm-console-web-api/dashboard/', filtro )
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
        function baixarArquivoConteudo(id, callback) {
        	var response;
        	$http({method: 'GET', url: '/gtm-console-web-api/dashboard/download/conteudo/' + id}).
        	  success(function(data, status, headers, config) {
        		  var anchor = angular.element('<a/>');
        		  anchor.css({display: 'none'});
        		  angular.element(document.body).append(anchor);
        		  anchor.attr({
        		    href: 'data:attachment/csv;charset=utf-8,' + encodeURIComponent(data),
        		    target: '_self',
        		    download: new Date().getTime() + '.txt'	
        		  })[0].click();
        		  anchor.remove();
        		  response = { success: true , data: data};
  		    	  callback(response);
        	  }).
        	  error(function(data, status, headers, config) {
        		  response = { success: false , data: data};
  		    	  callback(response);
        	  });        	
		}
        
    }

})();