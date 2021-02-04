(function($) {
	"use strict";
	 $('.paypal_desc').hide();
    $('#paypal-button').hide();
	
	var base_url=$('#base_url').val();
	var BASE_URL=$('#base_url').val();
	var csrf_token=$('#csrf_token').val();
	var csrfName=$('#csrfName').val();
	var csrfHash=$('#csrfHash').val();

	var stripe_key=$("#stripe_key").val();
	var web_logo=$("#logo_front").val();
	$( document ).ready(function() {
		$('#my_stripe_payyment').hide();
		$('.callStripe').on('click',function(){
			var e=this;
			callStripe(e);
		}); 
		$('.plan_notification').on('click',function(){
			plan_notification();
		}); 
	});
	var final_gig_amount = 1;
	var sub_id = '';
	var striep_currency ='';

	var final_gig_amount1 = 1;
	var service_id = '';
	var provider_id = '';
	var booking_date = '';
	var booking_time = '';
	var service_location = '';
	var service_latitude = '';
	var service_longitude = '';
	var final_gig_currency = 'USD';
	var notes = '';

	function plan_notification(){

		swal({
			title: " Plan warning..!",
			text: "Already buyed high range so choose higher plan....!",
			icon: "error",
			button: "okay",
			closeOnEsc: false,
			closeOnClickOutside: false
		});
	}
	function callStripe(e) {
		
		var payment_type = $('input[name="payment_type"]:checked').val();//alert(payment_type);
		sub_id = $(e).attr('data-id');
		final_gig_amount = $(e).attr('data-amount');
		final_gig_currency = $(e).attr('data-currency');
		var curconv = $(e).attr('data-curcon');
		if(parseInt(final_gig_amount)==0.00) {
			free_subscription();
		}
		else {
			
			if (payment_type == '' || payment_type == undefined) {
                swal({
                    title: "payment Type",
                    text: "Kindly Select payment Type...",
                    icon: "error",
                    button: "okay",
                    closeOnEsc: false,
                    closeOnClickOutside: false
                });
               // $("#wallet_amt").select();
                return false;
            }
			if (payment_type == "razorpay" && payment_type != undefined) {
				
				// final_gig_amount = (final_gig_amount * 100);//
				curconv = curconv *100;
				//alert(curconv);
				//var totalAmount = $('#wallet_amt').val();
				var product_id =  '123';
				var product_name =  'Add Subscription';				
				var options = {
					"key": $('#razorpay_apikey').val(),
					"currency": 'INR',
					"amount": Math.round(curconv),
					"name": product_name,
					"description": product_name,
					"handler": function (response){
						  $.ajax({
							url: base_url+'user/subscription/razorpay_payment',
							type: 'post',
							dataType: 'json',
							data: {sub_id:sub_id,final_gig_amount:curconv * 100,csrf_token_name:csrf_token},
							success: function (msg) {						
							  window.location.href = base_url+'provider-subscription';
							}
						});
					},
					"theme": {
						"color": "#F37254"
					}
				}
				var rzp1 = new Razorpay(options);
				rzp1.open();
				e.preventDefault();
				return false;
			}
			if (payment_type == "stripe" && payment_type != undefined) {
				$('#my_stripe_payyment').click();
			}
			if (payment_type == "paypal") {
				
				//$("#frm_paypal_detail").submit();
				//alert('dsfdsf');
				document.getElementById("frm_paypal_detail_"+sub_id).submit();
				var amnt=curconv * 100;
				//window.location.href = base_url+'user/subscription/paypal_payment/'+sub_id+'/'+amnt+'/'+csrf_token;
                //paypal_add_wallet(curconv * 100,final_gig_currency);
				 // $.ajax({
							// url: base_url+'user/subscription/paypal_payment',
							// type: 'post',
							// dataType: 'json',
							// data: {sub_id:sub_id,final_gig_amount:curconv * 100,csrf_token_name:csrf_token},
							// success: function (msg) {

									// $("#paypal-button").html(msg);
				
							 // window.location.href = base_url+'provider-subscription';
							// }
						// });
				
            }
			
		}
	}

	function free_subscription() {
		$.ajax({
			url: base_url+'user/subscription/stripe_payments/',
			data: {sub_id:sub_id,final_gig_amount:final_gig_amount,csrf_token_name:csrf_token},
			type: 'POST',
			dataType: 'JSON',
			beforeSend: function(){
				$('.loading').show();
			},
			success: function(response){
				$('.loading').fadeOut("slow");
				window.location.href = base_url+'provider-subscription';
			},
			error: function(error){
				console.log(error);
			}
		});
	}



	var handler = StripeCheckout.configure({
		key: stripe_key,
		image: web_logo,
		locale: 'auto',
		token: function(token,args) {
		// You can access the token ID with `token.id`.
		$('#access_token').val(token.id);
		var tokenid = token.id;
		$.ajax({
			url: base_url+'user/subscription/stripe_payment/',
			data: {sub_id:sub_id,final_gig_amount:final_gig_amount,tokenid:tokenid,det:token,csrf_token_name:csrf_token},
			type: 'POST',
			dataType: 'JSON',
			success: function(response){
				window.location.href = base_url+'provider-subscription';
			},
			error: function(error){
				console.log(error);
			}
		});
	}
});
	$('#my_stripe_payyment').on('click', function(e) {
	final_gig_amount = (final_gig_amount * 100); //  dollar to cent	
	// Open Checkout with further options:
	handler.open({
		name: base_url,
		description: 'Subscribe',
		amount: final_gig_amount,
		currency:final_gig_currency
	});
	e.preventDefault();
});



	function callStripe_booking(e) {
		service_id = $(e).attr('data-id');
		provider_id = $(e).attr('data-provider');
		final_gig_amount1 = $(e).attr('data-amount');
		booking_date = $("#booking_date").val();
		booking_time = $("#from_time").val();
		service_location = $("#service_location").val();
		service_latitude = $("#service_latitude").val();
		service_longitude = $("#service_longitude").val();
		notes = $("#notes").val();

		if(parseInt(final_gig_amount1)==0) {
			alert('Service amount cannot be empty');
		}
		else {
			var booking_date1 = $("#booking_date").val();
			var booking_time1 = $("#from_time").val();
			var service_location1 = $("#service_location").val();

			if(booking_date1 == '') {
				$('.error_date').show();
				return false;
			}
			else if(booking_time1 == '' || booking_time == null) {
				$('.error_time').show();
				return false;
			}
			else if(service_location1 ==  '') {
				$('.error_date').hide();
				$('.error_loc').show();
				return false;
			}
			$('#stripe_booking').click();
		}
	}



	function callStripe_booking(e) {
		service_id = $(e).attr('data-id');
		provider_id = $(e).attr('data-provider');
		final_gig_amount1 = $(e).attr('data-amount');
		booking_date = $("#booking_date").val();
		booking_time = $("#from_time").val();
		service_location = $("#service_location").val();
		service_latitude = $("#service_latitude").val();
		service_longitude = $("#service_longitude").val();
		notes = $("#notes").val();

		if(parseInt(final_gig_amount1)==0) {
			alert('Service amount cannot be empty');
		}
		else {
			var booking_date1 = $("#booking_date").val();
			var booking_time1 = $("#from_time").val();
			var service_location1 = $("#service_location").val();

			if(booking_date1 == '') {
				$('.error_date').show();
				return false;
			}
			else if(booking_time1 == '' || booking_time == null) {
				$('.error_time').show();
				return false;
			}
			else if(service_location1 ==  '') {
				$('.error_date').hide();
				$('.error_loc').show();
				return false;
			}
			$('#stripe_booking').click();
		}
	}
	
	
	    function paypal_add_wallet(amt,currency_val) {
        // Create a client.
        var username = $('#username').val();
        var mobileno = $('#mobileno').val();
        var address = $('#address').val();
        var pincode = $('#pincode').val();
        var state = $('#state').val();
        var country = $('#country').val();
        var city = $('#city').val();
        var sandbox_type = $('#paypal_gateway').val();
        var braintree_key = $('#braintree_key').val();alert(sandbox_type);
//	alert(sandbox_type);
        braintree.client.create({
            authorization: braintree_key
        }, function (clientErr, clientInstance) {

            if (clientErr) {
                console.error('Error creating client:', clientErr);
                return;
            }
// Create a PayPal Checkout component.
            braintree.paypalCheckout.create({
                client: clientInstance
            }, function (paypalCheckoutErr, paypalCheckoutInstance) {
// Stop if there was a problem creating PayPal Checkout.
// This could happen if there was a network error or if it's incorrectly
// configured.
                if (paypalCheckoutErr) {
                    console.error('Error creating PayPal Checkout:', paypalCheckoutErr);
                    return;
                }

// Set up PayPal with the checkout.js library
                paypal.Button.render({
                    env: sandbox_type,
                    commit: true, // This will add the transaction amount to the PayPal button
                    payment: function () {
                        return paypalCheckoutInstance.createPayment({
                            flow: 'checkout', // Required
                            amount: amt, // Required
                            currency: currency_val, // Required
                            enableShippingAddress: true,
                            shippingAddressEditable: false,
                            shippingAddressOverride: {
                                recipientName: username,
                                line1: address,
                                // line2: 'coimbatore',
                                city: city,
                                countryCode: country,
                                postalCode: pincode,
                                state: state,
                                phone: mobileno
                            }
                        });
                    },
                    onAuthorize: function (data, actions) {
                        return paypalCheckoutInstance.tokenizePayment(data, function (err, payload) {
// Submit `payload.nonce` to your server
                            var intent = data.intent;
                            var paymentID = data.paymentID;
                            var payerID = data.payerID;
                            var paymentToken = data.paymentToken;
                            var paymentMethod = 'PayPal';
                            var orderID = data.orderID;

                            document.getElementById('payload_nonce').value = payload.nonce;
                            document.getElementById('orderID').value = orderID;
                            if (orderID) {
                                
                                $('#paypal_amount').val(amt);
                                $('#paypal-button').hide();
                                $('.paypal_desc').hide();
                                document.getElementById("myForm").submit();
                                button_loading();
                            }
                        });
                    },
                    onCancel: function (data) {
                        location.reload();
                        console.log('checkout.js payment cancelled', JSON.stringify(data, 0, 2));
                    },
                    onError: function (err) {
                        console.error('checkout.js error', err);
                    }
                }, '#paypal-button').then(function () {


// The PayPal button will be rendered in an html element with the id
// `paypal-button`. This function will be called when the PayPal button
// is set up and ready to be used.
                });
            });
        });

        $('#paypal_amount').val(amt);
        setTimeout(function () {
            $('.paypal_desc').show();
            $('#paypal-button').show();
        }, 5000);


    }
	
	
	function button_loading() {
        var $this = $('.btn');
        var loadingText = '<i class="fa fa-circle-o-notch fa-spin"></i> loading...';
        if ($this.html() !== loadingText) {
            $this.data('original-text', $this.html());
            $this.html(loadingText).prop('disabled', 'true').bind('click', false);
        }
    }

})(jQuery);