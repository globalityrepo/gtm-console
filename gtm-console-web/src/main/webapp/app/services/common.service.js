(function () {
    'use strict';

    stockModule.factory('CommonService', CommonService);

    CommonService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout'];
    function CommonService($http, $cookieStore, $rootScope, $timeout) {
        var service = {};
        
        service.baixarArquivoExternal = baixarArquivoExternal;
        service.buscarAcessosDelegados = buscarAcessosDelegados;
        
        return service;
        
        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        }
        
        function baixarArquivoExternal(filename, callback) {
        	var response;
        	$http({method: 'GET', url: '/gtm-console-web-api/common/download/external/' + filename}).
        	  success(function(data, status, headers, config) {
        		  var anchor = angular.element('<a/>');
        		  anchor.css({display: 'none'});
        		  angular.element(document.body).append(anchor);
        		  anchor.attr({
        		    href: 'data:attachment/csv;charset=utf-8,' + encodeURIComponent(data),
        		    target: '_self',
        		    download: filename
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
        
        function buscarAcessosDelegados(codigoRecurso, idRef, callback) {
        	var response;
        	$http.get('/gtm-console-web-api/common/acessosdelegados/' + codigoRecurso + '/' + idRef)
        	.success(function(data, status, headers, config) {
        		response = { success: true , data: data};
		    	callback(response);
        	})
        	.error(function(data, status, headers, config) {
        		response = { success: false , data: data};
		    	callback(response);
        	});
        }
        
    }

})();