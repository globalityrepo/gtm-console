var stockModule = angular.module('stockModule', [
    'ui.router',
    'ui.bootstrap',
    'ngResource',
    'ngRoute',
	'ngCookies',
	'treeGrid',
	'pascalprecht.translate',
	'angularFileUpload'
]);

stockModule.config(['$translateProvider', function ($translateProvider) {

	$translateProvider.translations('pt_BR', {
	    'label.usuario': 'Usuário',
	    'label.senha': 'Senha',   
	    'label.entrar': 'Entrar',
	    'label.esqueciminhasenha': 'Esqueci minha senha',
	    'label.informeseuusuario': 'Informe seu usuário',
	    'label.solicitarnovasenha': 'Solicitar nova senha',
	    'label.cancelar': 'Cancelar'
	}); 
	
	$translateProvider.translations('en_US', {
	    'label.usuario': 'User',
	    'label.title': 'Password',
	    'label.entrar': 'Enter',
	    'label.esqueciminhasenha': 'Forgot my password',
	    'label.informeseuusuario': 'Tell you user',
	    'label.solicitarnovasenha': 'Request new password',
	    'label.cancelar': 'Cancel'
	}); 
	
	$translateProvider.translations('es_ES', {
	    'label.usuario': 'Usuario',
	    'label.senha': 'Contraseña',
	    'label.entrar': 'Entrar',
	    'label.esqueciminhasenha': 'Olvidé mi contraseña',
	    'label.informeseuusuario': 'Introduzca su usuario',
	    'label.solicitarnovasenha': 'Solicitar nueva contraseña',
	    'label.cancelar': 'Cancelar'
	}); 
	  
	$translateProvider.preferredLanguage('en_US');

}]);

angular.module("stockModule").directive("filesInput", function() {
	  return {
	    require: "ngModel",
	    link: function postLink(scope,elem,attrs,ngModel) {
	      elem.on("change", function(e) {
	        var files = elem[0].files;
	        ngModel.$setViewValue(files);
	      })
	    }
	  }
	});

stockModule.
	config(function ($stateProvider, $urlRouterProvider) {

        // For any unmatched URL redirect to dashboard URL
        $urlRouterProvider.otherwise("/login");

        $stateProvider
            // Known items
	        .state('login', {
	            url: "/login",
	            views: {
	                'main': {
	                    templateUrl: "app/pages/login/login.view.html",
	                    controller: "LoginController as loginCtrl"
	                }
	            },
	        })
	        .state('home', {
	            url: "/home",
	            views: {
	                'main': {
	                    templateUrl: "app/pages/home/home.view.html",
	                    controller: "HomeController as homeCtrl"
	                }
	            },
	        })
	        .state('acessomegado', {
	            url: "/acessonegado",
	            views: {
	                'main': {
	                    templateUrl: "app/pages/geral/acessonegado/acessonegado.view.html",
	                    controller: "AcessoNegadoController as acessoNegadoCtrl"
	                }
	            },
	        })
	        .state('usuario', {
	            url: "/usuario",
	            views: {
	                'main': {
	                    templateUrl: "app/pages/usuario/usuario.view.html",
	                    controller: "UsuarioController as usuarioCtrl"
	                }
	            },
	        })
	        .state('dashboard', {
	            url: "/dashboard",
	            views: {
	                'main': {
	                    templateUrl: "app/pages/dashboard/dashboard.view.html",
	                    controller: "DashboardController as dashboardCtrl"
	                }
	            },
	        })
        	.state('divisao', {
                url: "/divisao",
                views: {
                    'main': {
                        templateUrl: "app/pages/divisao/divisao.view.html",
                        controller: "DivisaoController as divisaoCtrl"
                    }
                },
            })
            
            .state('aplicacao', {
                url: "/aplicacao",
                views: {
                    'main': {
                        templateUrl: "app/pages/aplicacao/aplicacao.view.html",
                        controller: "AplicacaoController as aplicacaoCtrl"
                    }
                },
            })
            .state('eventotipo', {
                url: "/eventotipo",
                views: {
                    'main': {
                        templateUrl: "app/pages/evento/tipo/evento.tipo.view.html",
                        controller: "EventoTipoController as eventoTipoCtrl"
                    }
                },
            })
            .state('eventonivel', {
                url: "/eventonivel",
                views: {
                    'main': {
                        templateUrl: "app/pages/evento/nivel/evento.nivel.view.html",
                        controller: "EventoNivelController as eventoNivelCtrl"
                    }
                },
            })
            .state('transacao', {
	            url: "/transacao",
	            views: {
	                'main': {
	                    templateUrl: "app/pages/transacao/transacao.view.html",
	                    controller: "TransacaoController as transacaoCtrl"
	                }
	            },
	        })
	        .state('grupo', {
	            url: "/grupo",
	            views: {
	                'main': {
	                    templateUrl: "app/pages/grupo/grupo.view.html",
	                    controller: "GrupoController as grupoCtrl"
	                }
	            },
	        })
	        .state('esquecisenha', {
	            url: "/esquecisenha",
	            views: {
	                'main': {
	                    templateUrl: "app/pages/esquecisenha/esquecisenha.view.html",
	                    controller: "EsqueciSenhaController as esqueciSenhaCtrl"
	                }
	            },
	        })
	        .state('configuracao', {
	            url: "/configuracao",
	            views: {
	        		'main' : { 
	        			templateUrl: "app/pages/geral/configuracao.view.html",
	                    controller: "ConfiguracaoController as configuracaoCtrl"
	                }
	            },
	        })
	        .state('entidade', {
	            url: "/entidade",
	            views: {
	        		'main' : { 
	        			templateUrl: "app/pages/entidade/entidade.view.html",
	                    controller: "EntidadeController as entidadeCtrl"
	                }
	            },
	        })
	        .state('depara', {
	            url: "/depara",
	            views: {
	        		'main' : { 
	        			templateUrl: "app/pages/depara/depara.view.html",
	                    controller: "DeParaController as deParaCtrl"
	                }
	            },
	        });
		})
    

	stockModule.run(function ($rootScope, $location, $cookieStore, $http, $window, ConfiguracaoService, notificationsFactory, $translate) {		
		
		$rootScope.ENTITIES = {
					"APLICACAO": "aplicacao",
					"DIVISAO": "divisao",
					"EVENTO_NIVEL": "eventonivel",
					"EVENTO_TIPO": "eventotipo",
					"GRUPO": "grupo",
					"TRANSACAO": "transacao",
					"TRANSACAO_PASSO": "transacaopasso",
					"TRANSACAO_PASSO_ACAO": "transacaopassoacao",
					"TRANSACAO_PARAMETRO": "transacaoparametro",
					"PERFIL": "perfil",
					"ENTIDADE": "entidade",
					"DEPARA": "entidadeaplicacaodepara"
		};
		
		$rootScope.config = {};
		$rootScope.carregarConfiguracoesGerais = carregarConfiguracoesGerais;
		$rootScope.aplicarBackground = false;
		
	    function carregarConfiguracoesGerais() {
	    	ConfiguracaoService.buscarNonAuth(function (response) {
	            if (response.success) {
	            	$rootScope.config = response.data;
	            	$translate.use($rootScope.config.locale);
	            	$http.defaults.headers.common['Language'] = 'Locale ' + $rootScope.config.locale;
	            } else {
	            	notificationsFactory.error(response.data);
	            }
	        });
	    }
	    
	    carregarConfiguracoesGerais();
	    	    
		// keep user logged in after page refresh
	    $rootScope.globals = $cookieStore.get('globals') || {};
	    if ($rootScope.globals.currentUser) {
	        $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
	    }
	
	    $rootScope.$on('$locationChangeStart', function (event, next, current) {
	        // redirect to login page if not logged in and trying to access a restricted page
	        var restrictedPage = $.inArray($location.path(), ['/login', '/esquecisenha']) === -1;
	        var loggedIn = $rootScope.globals.currentUser;
	        if (restrictedPage && !loggedIn) {
	            $location.path('/login');
	        }
	    });
	    
    });

	//Base64 encoding service used by AuthenticationService
	var Base64 = {
	
	    keyStr: 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=',
	
	    encode: function (input) {
	        var output = "";
	        var chr1, chr2, chr3 = "";
	        var enc1, enc2, enc3, enc4 = "";
	        var i = 0;
	
	        do {
	            chr1 = input.charCodeAt(i++);
	            chr2 = input.charCodeAt(i++);
	            chr3 = input.charCodeAt(i++);
	
	            enc1 = chr1 >> 2;
	            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
	            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
	            enc4 = chr3 & 63;
	
	            if (isNaN(chr2)) {
	                enc3 = enc4 = 64;
	            } else if (isNaN(chr3)) {
	                enc4 = 64;
	            }
	
	            output = output +
	                this.keyStr.charAt(enc1) +
	                this.keyStr.charAt(enc2) +
	                this.keyStr.charAt(enc3) +
	                this.keyStr.charAt(enc4);
	            chr1 = chr2 = chr3 = "";
	            enc1 = enc2 = enc3 = enc4 = "";
	        } while (i < input.length);
	
	        return output;
	    },
	
	    decode: function (input) {
	        var output = "";
	        var chr1, chr2, chr3 = "";
	        var enc1, enc2, enc3, enc4 = "";
	        var i = 0;
	
	        // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
	        var base64test = /[^A-Za-z0-9\+\/\=]/g;
	        if (base64test.exec(input)) {
	            window.alert("There were invalid base64 characters in the input text.\n" +
	                "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
	                "Expect errors in decoding.");
	        }
	        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
	
	        do {
	            enc1 = this.keyStr.indexOf(input.charAt(i++));
	            enc2 = this.keyStr.indexOf(input.charAt(i++));
	            enc3 = this.keyStr.indexOf(input.charAt(i++));
	            enc4 = this.keyStr.indexOf(input.charAt(i++));
	
	            chr1 = (enc1 << 2) | (enc2 >> 4);
	            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
	            chr3 = ((enc3 & 3) << 6) | enc4;
	
	            output = output + String.fromCharCode(chr1);
	
	            if (enc3 != 64) {
	                output = output + String.fromCharCode(chr2);
	            }
	            if (enc4 != 64) {
	                output = output + String.fromCharCode(chr3);
	            }
	
	            chr1 = chr2 = chr3 = "";
	            enc1 = enc2 = enc3 = enc4 = "";
	
	        } while (i < input.length);
	
	        return output;
	    }
	};

