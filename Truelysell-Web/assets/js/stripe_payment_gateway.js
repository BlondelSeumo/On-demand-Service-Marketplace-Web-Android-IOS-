(function($) {
	"use strict";
  
	var csrf_token=$('#admin_csrf').val();
	var base_url=$('#base_url').val();
	
	$( document ).ready(function() {
		$('.stripe_payment').on('change',function(){
			var id=$(this).val();
		  payment(id);
		});
	});
	function payment(value) {
		if(value!=''){
			$.ajax({
				type: "post",
				url: base_url+"admin/settings/payment_type",
				data:{type:value,'csrf_token_name':csrf_token}, 
				dataType:'json',
				success: function (data) {
					if(data!=''){
						$('#gateway_name').val(data.gateway_name);
						$('#api_key').val(data.api_key);
						$('#value').val(data.value);
					}
				}
			});		
		}
	}
$( document ).ready(function() {
	$('.paypal_payment').on('change',function(){
			var id=$(this).val();
		  paypal_payment(id);
		});
	});
	function paypal_payment(value) {
		if(value!=''){
			$.ajax({
				type: "post",
				url: base_url+"admin/settings/paypal_payment_type",
				data:{type:value,'csrf_token_name':csrf_token}, 
				dataType:'json',
				success: function (data) {
					if(data!=''){
						$('#braintree_key').val(data.braintree_key);
						$('#braintree_merchant').val(data.braintree_merchant);
						$('#braintree_publickey').val(data.braintree_publickey);
						$('#braintree_privatekey').val(data.braintree_privatekey);
					}
				}
			});		
		}
	}
})(jQuery);