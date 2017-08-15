// Repository 
stockModule.factory("itemsFactory", itemsFactory);

function itemsFactory(ajaxServiceFactory) {
	return ajaxServiceFactory.getService('/gtm-rest/divisao');
}