(function($) {
	"use strict";
	
	var base_url=$('#base_url').val();
	var BASE_URL=$('#base_url').val();
	var csrf_token=$('#csrf_token').val();
	var csrfName=$('#csrfName').val();
	var csrfHash=$('#csrfHash').val();
	var service_id ='';
var provider_id ='';
var final_gig_amount1 ='';
var booking_date ='';
var booking_time ='';
var service_location ='';
var service_latitude ='';
var service_longitude ='';
var notes='';
	function booking_proccess(e){
   
		service_id = $(e).attr('data-id');
		provider_id = $(e).attr('data-provider');
		final_gig_amount1 = $(e).attr('data-amount');
		booking_date = $("#booking_date").val();
		booking_time = $("#from_time").val();
		service_location = $("#service_location").val();
		service_latitude = $("#service_latitude").val();
		service_longitude = $("#service_longitude").val();
		notes = $("#notes").val();


   if(parseInt(final_gig_amount1)==0)
   {
     alert('Service amount cannot be empty');
   }
   else
   {
     var booking_date1 = $("#booking_date").val();
     var booking_time1 = $("#from_time").val();
     var service_location1 = $("#service_location").val();
     
     
     if(booking_date1 == '')
     {
       $('.error_date').show();
       return false;
     }
     else if(booking_time1 == '' || booking_time == null)
     {
       $('.error_time').show();
       return false;
     }
     else if(service_location1 ==  '')
     {
       $('.error_date').hide();
       $('.error_loc').show();
       return false;
     }
     $('#load_div').html('<img src="'+base_url+'assets/img/loader.gif" alt="" />');
     $('#load_div').show();

     $('.submit_service_book').attr('disabled',true);


     /*book*/
     $.ajax({
       
       url: base_url+'user/booking/book_service/',
       data: {service_id:service_id,final_gig_amount:final_gig_amount1,provider_id:provider_id,tokenid:'old flow',booking_time:booking_time,service_location:service_location,service_latitude:service_latitude,service_longitude:service_longitude,notes:notes,booking_date:booking_date,csrf_token_name:csrf_token},
       type: 'POST',
       dataType: 'JSON',
       beforeSend: function() {
        $('.page-loading').fadeIn();
      },
      success: function(response){
       $('.page-loading').fadeOut();
       swal({
         title: "Booking Confirmation...",
         text: "Your booking was booked Successfully ...!",
         icon: "success",
         button: "okay",
         closeOnEsc: false,
         closeOnClickOutside: false
         
       }).then(function(){
        
         window.location.href = base_url+'user-bookings';

       });
     },
     error: function(error){
       $('.page-loading').fadeOut();
       swal({
         title: "Booking Confirmation...",
         text: "Somethings went to wrong so try later ...!",
         icon: "error",
         button: "okay",
         closeOnEsc: false,
         closeOnClickOutside: false
         
       }).then(function(){
        
         window.location.reload();

       });
       console.log(error);
     }
   });
     
   }

 }
 
        
         

          $('#book_services').bootstrapValidator({
            fields: {

              booking_date: {
                validators: {
                  notEmpty: {
                    message: 'Please Enter Date'
                  }
                }
              },
              from_time: {
                validators: {
                  notEmpty: {
                    message: 'Please select Time Slot...'
                  }
                }
              },
              
              service_location: {
                validators: {
                  notEmpty: {
                    message: 'Please Enter service location...'
                  }
                }
              }
              
            }
          }).on('success.form.bv', function(e) {
            e.preventDefault();
            service_id = $('.submit_service_book').attr('data-id');
            provider_id = $('.submit_service_book').attr('data-provider');
            final_gig_amount1 = $('.submit_service_book').attr('data-amount');
            booking_date = $("#booking_date").val();
            booking_time = $("#from_time").val();
            service_location = $("#service_location").val();
            service_latitude = $("#service_latitude").val();
            service_longitude = $("#service_longitude").val();
            notes = $("#notes").val();
            
            $.ajax({
             url: base_url+'user/booking/book_service/',
             data: {service_id:service_id,final_gig_amount:final_gig_amount1,provider_id:provider_id,tokenid:'old flow',booking_time:booking_time,service_location:service_location,service_latitude:service_latitude,service_longitude:service_longitude,notes:notes,booking_date:booking_date,csrf_token_name:csrf_token},
             type: 'POST',
             dataType: 'JSON',
             beforeSend: function() {
               button_loading();
              },
              success: function(response){
               button_unloading();
                swal({
                 title: "Booking Confirmation...",
                 text: "Your booking was booked Successfully ...!",
                 icon: "success",
                 button: "okay",
                 closeOnEsc: false,
                 closeOnClickOutside: false
               }).then(function(){
                 window.location.href = base_url+'user-bookings';

               });
             },
             error: function(error){
             button_unloading();
              swal({
               title: "Booking Confirmation...",
               text: "Somethings went to wrong so try later ...!",
               icon: "error",
               button: "okay",
               closeOnEsc: false,
               closeOnClickOutside: false
             }).then(function(){
               window.location.reload();

             });
           }
         });
          });    

          
	function button_loading(){
		var $this = $('.btn');
		var loadingText = '<i class="fa fa-circle-o-notch fa-spin"></i> loading...';
		if ($this.html() !== loadingText) {
			$this.data('original-text', $this.html());
			$this.html(loadingText).prop('disabled','true').bind('click', false);
		}
	}
	function button_unloading(){
		var $this = $('.btn');
		$this.html($this.data('original-text')).prop('disabled','false');
	}
})(jQuery);