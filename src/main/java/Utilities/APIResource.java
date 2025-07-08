package Utilities;

public enum APIResource {
	
	_loginAPIResource("api/ecom/auth/login"),
	_createProductAPIResource("api/ecom/product/add-product"),
	//_createOrderAPIResource(""),
	_deleteProductAPIResource("api/ecom/product/delete-product/{productId}");
	
	private String resource;

	APIResource(String resource) {
		
		this.resource=resource;
	}
	
	public String getResource() {
		return	resource;
	}
			

}
