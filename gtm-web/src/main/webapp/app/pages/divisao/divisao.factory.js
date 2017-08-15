// Repository 
stockModule.factory("DivisaoFactory", DivisaoFactory);

function DivisaoFactory(ajaxServiceFactory) {
	return ajaxServiceFactory.getService('/gtm-rest/divisao');
}