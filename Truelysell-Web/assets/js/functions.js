(function($) {
	"use strict";

  var base_url=$('#base_url').val();
  var BASE_URL=$('#base_url').val();
  var csrf_token=$('#csrf_token').val();
  var csrfName=$('#csrfName').val();
  var csrfHash=$('#csrfHash').val();
  var user_type=$('#user_type').val();
  var modules=$('#modules_page').val();
  $( document ).ready(function() {
    $('#flash_succ_message2').hide();
    $('#flash_error_message1').hide();
    $('#otp_final_div').hide();
    $("#reason_div").hide();

    $('.error_rating').hide();
    $('.error_review').hide();
    $('.error_type').hide();

    $('.error_cancel').hide();
    $('.header-content-blk').hide();
    $('#contact_form').bootstrapValidator({
      fields: {
        name:           {
       validators: {		 
         notEmpty: {
         message: 'Please enter your name'
         }
       }
       },
         email:           {
       validators: {		 
         notEmpty: {
         message: 'Please enter your email'
         },
         emailAddress: {
                message: 'The value is not a valid email address'
             }
       }
       },
       message:           {
       validators: {		 
         notEmpty: {
         message: 'Please enter your message'
         }
       }
       }
     }
     }).on('success.form.bv', function(e) {
       var name = $('#name').val();
       var email = $('#email').val();
       var message = $('#message').val();
       $.ajax({
       type:'POST',
       url: base_url+'user/contact/insert_contact',
       data : {name:name,email:email,csrf_token_name:csrf_token,message:message},
       success:function(response)
       {
         if(response==1)
         {
            swal({
            title: "Message Send !",
            text: "Message Send Successfully....!",
            icon: "success",
            button: "okay",
            closeOnEsc: false,
            closeOnClickOutside: false
            }).then(function(){
           window.location.href = base_url+'contact';
         });
         
         } else {
         $("#flash_error_message1").show();
         $('#flash_error_message1').append('Wrong Credentials');
         return false;
         }
       }
       });
       return false;
     });  

    $('#re_send_otp_user').on('click',function(){
      re_send_otp_user();
    }); 
    $('.isNumber').on('keypress',function(){
      var id=$(this).val();
      isNumber(id);
    });  
    $('.chat_clear_all').on('click',function(){
      var id=$(this).attr('data-token');
      chat_clear_all(id);
    });
    $('.noty_clear').on('click',function(){
      var id=$(this).attr('data-token');
      noty_clear(id);
    }); 
    $('#rate_booking').on('click',function(){
      rate_booking();
    }); $('#cancel_booking').on('click',function(){
      cancel_booking();
    });  $('#provider_cancel_booking').on('click',function(){
      provider_cancel_booking();
    }); 
    $('#go_user_settings').on('click',function(){
      window.location=base_url+"user-settings/";
    }); 
    $('#go_book_service').on('click',function(){
      var service_id=$(this).attr('data-id');
      window.location=base_url+'book-service/'+service_id;
    }); 
    $('#add_wallet_money').on('click',function(){
      add_wallet_money();
    });
    $('.reason_modal').on('click',function(){
      var id=$(this).attr('data-id');
      reason_modal(id);
    });
    $('.update_user_booking_status').on('click',function(){
      var id=$(this).attr('data-id');
      var status=$(this).attr('data-status');
      var rowid=$(this).attr('data-rowid');
      var review=$(this).attr('data-review');
      update_user_booking_status(id,status,rowid,review);
    }); 
    $('.update_pro_booking_status').on('click',function(){
      var id=$(this).attr('data-id');
      var status=$(this).attr('data-status');
      var rowid=$(this).attr('data-rowid');
      var review=$(this).attr('data-review');
      update_pro_booking_status(id,status,rowid,review);
    });  
    $('.go_provider_availability').on('click',function(){
      window.location=base_url+"provider-availability";
    });   
    $('#re_send_otp_provider').on('click',function(){
      re_send_otp_provider();
    });   
    $('.get_pro_subscription').on('click',function(){
      get_pro_subscription();
    }); 
    $('.get_pro_availabilty').on('click',function(){
      get_pro_availabilty();
    });
    $('.get_pro_availabilty').on('click',function(){
      get_pro_availabilty();
    }); 
    $('.search_service').on('click',function(){
      $('#search_service').submit();
    }); $('.check_user_reason').on('submit',function(){
      var result=check_user_reason();
      return result;
    }); 
    $('.user_update_status').on('click',function(){
      user_update_status(this);
    }); 
    $('.no_only').on('keyup',function(e){
     $(this).val($(this).val().replace(/[^\d].+/, ""));
     if ((event.which < 48 || event.which > 57)) {
      event.preventDefault();
    }
  }); 
    $('.pagination_no').on('click',function(){
      var id=$(this).attr('data-id');
      getData(id);
    }); 


    $(".user_mobile").on('keyup keypress blur change', function(e) {
    //return false if not 0-9
    if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
     return false;
   }else{
        //limit length but allow backspace so that you can still delete the numbers.
        if( $(this).val().length >= parseInt($(this).attr('maxlength')) && (e.which != 8 && e.which != 0)){
          return false;
        }
      }

    });

    
    $('#step1_footer').prop("disabled", true); 
    $.ajax({
     type: "GET",
     url: base_url+"user/service/get_category",
     data:{id:$(this).val(),csrf_token_name:csrf_token}, 
     beforeSend :function(){

       $('#categorys').find("option:eq(0)").html("Please wait..");

     },                         
     success: function (data) { 

       $('#categorys').find("option:eq(0)").html("Select Category");

       var obj=jQuery.parseJSON(data);       

       $(obj).each(function(){
         var option = $('<option />');
         option.attr('value', this.value).text(this.label);           
         $('#categorys').append(option);
       });       

     }
   });

    $('#categorys').on('change',function(){
     if($(this).val()){
       $('#step1_footer').prop("disabled", false);
     }
     else {$('#step1_footer').prop("disabled", true);
   }


   $('#subcategorys').html('<option value="">Select subcategory</option>');
   if($(this).val()!=''){
     $.ajax({
       type: "POST",
       url: base_url+"user/service/get_subcategory",
       data:{id:$(this).val(),'csrf_token_name':csrf_token}, 
       beforeSend :function(){
         $('#subcategorys').find("option:eq(0)").html("Please wait..");
       },                         
       success: function (data) {   
         $('#subcategorys').find("option:eq(0)").html("Select SubCategory");
         var obj=jQuery.parseJSON(data);    
         $(obj).each(function(){
           var option = $('<option />');
           option.attr('value', this.value).text(this.label);           
           $('#subcategorys').append(option);
         });       
       }
     });
   }
 });
    $('#subcategorys').on('change',function(){
     if($(this).val()){
       $('#step3_footer').prop("disabled", false);
     }
     else {$('#step3_footer').prop("disabled", true);}


   });

    $('#new_fourth_page').bootstrapValidator({
     fields: {
       otp_number: {
         validators: {
           notEmpty: {
             message: 'Please enter OTP'
           }
         }
       },
     }

   }).on('success.form.bv', function(e) {

     var otp =         $('#otp_number').val();
     var userMobile =  $('#userMobile').val();

     var categorys=    $('#categorys').val();
     var subcategorys= $('#subcategorys').val();
     var userName=     $('#userName').val();
     var userEmail=    $('#userEmail').val();
     var country_code=$('.final_provider_c_code').val();
     var is_agree=   $('#agree_checkbox').val();

     $.ajax({
       type: "POST",
       url: base_url+"user/login/check_otp",
       data:{  
         otp:otp,
         mobileno:userMobile,
         country_code:country_code,
         category:categorys,
         subcategory:subcategorys,
         name:userName,
         is_agree:is_agree,
         email:userEmail,
         csrf_token_name:csrf_token,

       }, 

       success: function (data) { 



         var data=jQuery.parseJSON(data);
         if(data.response=='ok')
         {
           window.location.reload();
         }
         else if(data.response=='error')
         {
           $('#otp_error_msg').show();
           $('#otp_error_msg').text(data.msg);
           if(data.result=='otp_expired')
           {
             $('#registration_resend').show();
             $('#registration_final').addClass('invisible');

             $('#registration_resend').removeClass('invisible');


           }
         } 
       }
     });
     return false;
   });

   $('#registration_resend').on('click', function() {
     sendEvent('#modal-wizard', 3);
     $('#otp_error_msg').text('');
     $('#registration_submit').prop('disabled',false);
     $('#otp_number').val('');
     $('#registration_resend').addClass('invisible');
     $('#registration_final').removeClass('invisible');
   });


   $('#new_third_pagelogin').bootstrapValidator({

    fields: {
      userName: {
        validators: {
          notEmpty: {
            message: 'Please enter your service title'
          }
        }
      },
      userEmail: {
        validators: {
         remote: {
          url: base_url + 'user/login/email_chk',
          data: function(validator) {
            return {
              userEmail: validator.getFieldElements('userEmail').val(),
              csrf_token_name:csrf_token,
            };
          },
          message: 'This email is already exist',
          type: 'POST'
        },
        notEmpty: {
          message: 'Please enter email address'
        },


      }
    },
    userMobile: {
      validators: {

       remote: {
        url: base_url + 'user/login/mobileno_chk',
        data: function(validator) {
          return {
            userMobile: validator.getFieldElements('userMobile').val(),
            countryCode: validator.getFieldElements('countryCode').val(),
            csrf_token_name:csrf_token
          };
        },
        message: 'This mobile number is already exist',
        type: 'POST'
      },
      notEmpty: {
        message: 'Please enter mobile'
      },
      regexp: {
        regexp: /^\d{10}$/,
        message: 'Please supply a valid phone number'
      }
    }
  },

}
}).on('success.form.bv', function(e) {

 var categorys=    $('#categorys').val();
 var subcategorys= $('#subcategorys').val();
 var userName=     $('#userName').val();
 var userEmail=    $('#userEmail').val();
 var userMobile=   $('#userMobile').val();
 var countryCode=   $('#countryCode').val();

 $.ajax({
  type: "POST",
  url: base_url+"user/login/login",
  data:{  'category':categorys,
  'subcategory':subcategorys,
  'username':userName,
  'email':userEmail,
  'countryCode':countryCode,
  'csrf_token_name':csrf_token,
  'mobileno':userMobile
},
success: function (data) { 


  var obj = JSON.parse(data);


  if(obj.response=='ok')
  { 
    sendEvent('#modal-wizard', 4);
  }
  else
  {
    $('#registration_submit').prop("disabled", false); 
  }
}
} );
 return false;

});


$('#new_third_page1').bootstrapValidator({
  fields: {
    userName: {
      validators: {
        notEmpty: {
          message: 'Please Enter your service title'
        }
      }
    },
    userEmail: {
      validators: {
       remote: {
        url: base_url + 'user/login/email_chk',
        data: function(validator) {
          return {

            userEmail: validator.getFieldElements('userEmail').val(),
            csrf_token_name:csrf_token
          };
        },
        message: 'This email is already exist',
        type: 'POST'
      },
      notEmpty: {
        message: 'Please enter email address'
      },


    }
  },
  userMobile: {
    validators: {

     remote: {
      url: base_url + 'user/login/mobileno_chk',
      data: function(validator) {
        return {
          userMobile: validator.getFieldElements('userMobile').val(),
          csrf_token_name:csrf_token,
          countryCode: validator.getFieldElements('countryCode').val()
        };
      },
      message: 'This Mobile Number is already exist so Try Another Mobile No..!',
      type: 'POST'
    },
    notEmpty: {
      message: 'Please enter mobile No ...!'
    },
    regexp: {
      regexp: /^\d{10}$/,
      message: 'Please supply a valid Phone Number'
    }
  }
},

}
}).on('success.form.bv', function(e) {


 var userName=     $('#userName').val();
 var userEmail=    $('#userEmail').val();
 var userMobile=   $('#userMobile').val();
 var countryCode=   $('#countryCode').val();

 $.ajax({
  type: "POST",
  url: base_url+"user/login/send_otp_request",
  data:{  
    'username':userName,
    'email':userEmail,
    'countryCode':countryCode,
    'mobileno':userMobile,
    'csrf_token_name':csrf_token
  },
  success: function (data) { 


    var obj = JSON.parse(data);


    if(obj.response=='ok')
    { 
      sendEvent('#modal-wizard1', 2);
    }
    else
    {
      $('#registration_submit').prop("disabled", false); 
    }
  }
} );
 return false;

});
$('#booking_date').datepicker({
  dateFormat: 'dd-mm-yy',
  minDate: new Date(),  
  icons: {
    up: "fas fa-angle-up",
    down: "fas fa-angle-down",
    next: 'fas fa-angle-right',
    previous: 'fas fa-angle-left'
  }, onSelect: function(dateText) {
    var date = dateText;
    var dataString="date="+date;  
    var provider_id = $("#provider_id").val(); 
    var service_id = $("#service_id").val();
    $('#from_time').empty();
    $('#book_services').bootstrapValidator('revalidateField', 'booking_date');
   
    if(date!="" && date!=undefined){
      
      $.ajax({    
        url: base_url+"user/service/service_availability/",
        data : {date:date,provider_id:provider_id, service_id:service_id,csrf_token_name:csrf_token},
        type: "POST",

        success: function(response){      
          $('#from_time').find("option:eq(0)").html("Select time slot");
          if(response!=''){
            var obj=jQuery.parseJSON(response);   

            if(obj != '')
            {        
              $(obj).each(function(){
                var option = $('<option />');
                option.attr('value', this.start_time+ '-' +this.end_time).text(this.start_time+ '-' +this.end_time);           
                $('#from_time').append(option);
              });
            }
            else if(obj == '')
            {
              var msg = 'Availability not found';
              $('#from_time').append(msg);
            } 

            $('#to_time').find("option:eq(0)").html("Select end time");
            var obj=jQuery.parseJSON(response);   


            $(obj).each(function(){
              var option = $('<option />');
              option.attr('value', this.end_time).text(this.end_time);           
              $('#to_time').append(option);
            }); 
          }
        }

      });
    }
  }

});
$('.close').on('click', function() {
 $(".user_mobile").val('');
 $(".countryCode").val('');
})
$('#order-summary').DataTable();




if($('.days_check').is(':checked') == true){

 $('.eachdays').removeAttr('style');
 $('.eachdayfromtime').removeAttr('style');
 $('.eachdaytotime').removeAttr('style');

 if($('.daysfromtime_check').val()==''){
   $('.daysfromtime_check').attr('style','border-color:red');
   error = 1;
 }else{
   $('.daysfromtime_check').removeAttr('style');
 }
 if($('.daystotime_check').val()==''){
   error = 1;
   $('.daystotime_check').attr('style','border-color:red');
   
 }else{
   $('.daystotime_check').removeAttr('style');
 }

}else{
 var oneday = 0;
 $('.daysfromtime_check').removeAttr('style');
 $('.daystotime_check').removeAttr('style');

 $('.eachdays').each(function(){
   if($(this).is(':checked') == true){
    oneday = 1;
  }
});
 if(oneday == 1){
   $('.eachdays').removeAttr('style');
   $('.eachdayfromtime').removeAttr('style');
   $('.eachdaytotime').removeAttr('style');
 }

 $('.eachdays').each(function(){

   if($(this).is(':checked') == true){


     var val = $(this).val();
     val = parseInt(val);

     if($('.eachdayfromtime'+val).val() ==''){
       error = 1;

       $('.eachdayfromtime'+val).attr('style','border-color:red');
     }else{
       $('.eachdayfromtime'+val).removeAttr('style');
     }

     if($('.eachdaytotime'+val).val() ==''){
       error = 1;
       $('.eachdaytotime'+val).attr('style','border-color:red');
     }else{
       $('.eachdaytotime'+val).removeAttr('style');
     }

   }
   
 });
 if(oneday == 0){
   $('.eachdays').attr('style','opacity:unset;position:unset;');
   $('.eachdayfromtime').attr('style','border-color:red');
   $('.eachdaytotime').attr('style','border-color:red');
   var error = 1;
 }else{
 }

}


$(document).on('click','.days_check',function(){
   var from_time = '';
   var to_time = '';
   if($('.daysfromtime_check').val()){
	   var from_time = $('.daysfromtime_check').val();
   }
   if($('.daystotime_check').val()){
	   var to_time = $('.daystotime_check').val();
   }
 if($(this).is(':checked') == true){
  $('.daysfromtime_check').val(from_time);
  $('.daystotime_check').val(to_time);
  $('.eachdays').attr('disabled','disabled');
  $('.eachdayfromtime').attr('disabled','disabled');
  $('.eachdaytotime').attr('disabled','disabled');
  $('.eachdayfromtime').val('');
  $('.eachdaytotime').val('');
  $('.eachdays').prop('checked', false);
  $('.eachdays').removeAttr('style');
  $('.eachdayfromtime').removeAttr('style');
  $('.eachdaytotime').removeAttr('style');

}else{
 $('.eachdays').removeAttr('disabled');
 $('.eachdayfromtime').removeAttr('disabled');
 $('.eachdaytotime').removeAttr('disabled');

 $('.daysfromtime_check').val(from_time);
 $('.daystotime_check').val(to_time);
 $('.daysfromtime_check').removeAttr('style');
 $('.daystotime_check').removeAttr('style');
}

});



$("#loginsubmit").on("click", function(){
 $("#userSignIn").submit();
});

$('#userSignIn').bootstrapValidator({
 fields: {
   user_mobile:           {
    validators: {
      digits: {
        message: 'Please enter valid Number'
      },
      notEmpty: {
        message: 'Please enter your mobile number'
      }
    }
  }
}
}).on('success.form.bv', function(e) {
  var country_code = $('#direct_log_country_code').val();
  var mobile = $('#direct_log_mobile_no').val();
  $.ajax({
    type:'POST',
    url: base_url+'user/login/login',
    data : {mobile:mobile,country_code:country_code,csrf_token_name:csrf_token},
    success:function(response)
    {
      if(response==1)
      {
        window.location.reload();
      }
      else if(response==2)
      {
        window.location.reload();
      }
      else {
        $("#flash_error_message1").show();
        $('#flash_error_message1').append('Wrong Credentials');

        return false;
      }
    }
  });
  return false;
});  


$("#user_submit").on("click", function(){
 $("#reg_user").submit();
});

$('#reg_user').bootstrapValidator({
 fields: {
   userName:   {
     validators:          {
       notEmpty:              {
         message: 'Please enter your Username'
       }
     }
   },
   userEmail:           {
     validators:           {
       notEmpty:               {
         message: 'Please enter your email'
       }
     }
   },
   userMobile:           {
     validators:           {
       notEmpty:               {
         message: 'Please enter your mobile number'
       }
     }
   },
   countryCode:           {
     validators:           {
       notEmpty:               {
         message: 'Please select your countryCode'
       }
     }
   }
 }
}).on('success.form.bv', function(e) {

  var userName = $('#user_Name').val();
  var userEmail = $('#user_Email').val();
  var userMobile = $('#user_Mobile').val();
  var country_code = $('#countryCode').val();
  $.ajax({
    type:'POST',
    url: base_url+'user/login/insert_user',
    data : {username:userName,email:userEmail,mobile:userMobile,country_code:country_code,csrf_token_name:csrf_token},
    success:function(response)
    {
      if(response==1)
      {

        $("#flash_succ_message").show(1000);
        $("#flash_error_message").hide();
        $('#flash_succ_message').append('Registered Successfully');
      }

      else {
        $("#flash_succ_message").hide();
        $("#flash_error_message").show(1000);
        $('#flash_error_message').append('Email id or mobileno already exists');

        return false;
      }
    }
  });
  return false;
});  


$('.rates').on('click', function() {
  $("#myInput").val($("input[name='rates']:checked").val());
})
$('.myReview').on('click', function() {
  $('#booking_id').val('');
  $('#provider_id').val('');
  $('#user_id').val('');
  $('#service_id').val('');
  var booking_id = $(this).attr("data-id");
  var provider_id = $(this).attr("data-providerid");
  var user_id = $(this).attr("data-userid");
  var service_id = $(this).attr("data-serviceid");

  $("#booking_id").val(function() {
    return this.value + booking_id;
  });
  $("#provider_id").val(function() {
    return this.value + provider_id;
  });
  $("#user_id").val(function() {
    return this.value + user_id;
  });
  $("#service_id").val(function() {
    return this.value + service_id;
  });



});

$('.myCancel').on('click', function() { 
  $('#cancel_review').val('');
  $('#booking_id').val('');
  $('#provider_id').val('');
  $('#user_id').val('');
  $('#service_id').val('');
  var booking_id = $(this).attr("data-id");
  var provider_id = $(this).attr("data-providerid");
  var user_id = $(this).attr("data-userid");
  var service_id = $(this).attr("data-serviceid");

  $("#cancel_booking_id").val(function() {
    return this.value + booking_id;
  });
  $("#cancel_provider_id").val(function() {
    return this.value + provider_id;
  });
  $("#cancel_user_id").val(function() {
    return this.value + user_id;
  });
  $("#cancel_service_id").val(function() {
    return this.value + service_id;
  });
});


var timeout = 3000; // in miliseconds (3*1000)
$('#flash_succ_message').delay(timeout).fadeOut(500);
$('#flash_error_message').delay(timeout).fadeOut(500);



var rating = '';
var review = '';
var booking_id = '';
var provider_id = '';
var user_id = '';
var service_id = '';
var type = '';

if(modules=="home"){
 $( ".common_search" ).autocomplete({
  source: "<?php echo site_url('home/get_common_search_value/?');?>"
});
}

if(modules=="services" || modules=="service"){
 if(!$("#service_location").length)     // use this if you are using id to check
 {
   $('.google_input').append("<input type='hidden' id='service_location'>");
 }
}








function date_handler(e){

  var date = e.target.value;
  var dataString="date="+date;  
  var provider_id = $("#provider_id").val(); 
  var service_id = $("#service_id").val(); 



  $.ajax({    
   url: base_url+"user/service/service_availability/",
   data : {date:date,provider_id:provider_id, service_id:service_id,csrf_token_name:csrf_token},
   type: "POST",

   success: function(response){      
     $('#from_time').find("option:eq(0)").html("Select time slot");
     var obj=jQuery.parseJSON(response);   


     if(obj != '')
     {        
       $(obj).each(function(){
         var option = $('<option />');
         option.attr('value', this.start_time+ '-' +this.end_time).text(this.start_time+ '-' +this.end_time);           
         $('#from_time').append(option);
       });
     }
     else if(obj == '')
     {
       var msg = 'Availability not found';
       $('#from_time').append(msg);
     } 

     $('#to_time').find("option:eq(0)").html("Select end time");
     var obj=jQuery.parseJSON(response);   

     $(obj).each(function(){
       var option = $('<option />');
       option.attr('value', this.end_time).text(this.end_time);           
       $('#to_time').append(option);
     }); 
   }
   
 });

}


function re_send_otp_user(){
 var mobile_no=($('.user_final_no').val());
 var country_code=($('.final_country_code').val());


 $.ajax({

   url: base_url+"user/login/re_send_otp_user",
   data: {'mobile_no':mobile_no,'country_code':country_code,'csrf_token_name':csrf_token},
   type: 'POST',
   dataType: 'JSON',
   success: function(response){

     if(response==2 ){
       swal({
         title: "OTP Send !",
         text: "Some Things Went To Wrong....!",
         icon: "danger",
         button: "okay",
         closeOnEsc: false,
         closeOnClickOutside: false
       });
       location.reload();
     }else{
      swal({
       title: "OTP Send !",
       text: "Your OTP Send to Registered Mobile No.....",
       icon: "success",
       button: "okay",
       closeOnEsc: false,
       closeOnClickOutside: false
     });
    }

  }
})
}

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

function re_send_otp_provider(){
 var mobile_no=($('.provider_final_no').val());
 var country_code=($('.final_provider_c_code').val());


 $.ajax({

   url: base_url+"user/login/re_send_otp_provider",
   data: {'mobile_no':mobile_no,'country_code':country_code,'csrf_token_name':csrf_token},
   type: 'POST',
   dataType: 'JSON',
   success: function(response){
     if(response==2){
      swal({
       title: "OTP Send !",
       text: "Some Things Went To Wrong....!",
       icon: "error",
       button: "okay",
       closeOnEsc: false,
       closeOnClickOutside: false
     });
      location.reload();
    }else{
     swal({
       title: "OTP Send !",
       text: "Your OTP Send to Registered Mobile No.....",
       icon: "success",
       button: "okay",
       closeOnEsc: false,
       closeOnClickOutside: false
     });
   }

 }

})
}





function withdraw_wallet_value(input){
  $("#wallet_withdraw_amt").val(input);
}   
function isNumber(evt) {
  evt = (evt) ? evt : window.event;
  var charCode = (evt.which) ? evt.which : evt.keyCode;
  if (charCode > 31 && (charCode < 48 || charCode > 57)) {
    return false;
  }
  return true;
}  


function add_wallet_money(){
 swal({
   title: "Insufficient wallet amount !",
   text: "Please recharge your wallet after book this service....!",
   icon: "error",
   button: "okay",
   closeOnEsc: false,
   closeOnClickOutside: false
 }).then(function() {
  window.location = base_url+'user-wallet';
});
}


function user_update_status(e){
 var user_status=$(e).val();
 if(user_status==5){
  $("#reason_div").show();
}else{
  $("#reason_div").hide();
}
}

function check_user_reason(){
  var sent=true;
  var status=$(".update_user_status").val();
  var reason=$("#reject_reason").val();
  if(status==5){

    if(reason ==''){
     swal({
       title: "Rejection reason.",
       text: "Please Enter Rejection Reason about this Service...",
       icon: "error",
       button: "okay",
       closeOnEsc: false,
       closeOnClickOutside: false
     }).then(function(){
      $("#reject_reason").focus();
    });

     sent=false;

   }

 }
 return sent;
}

//LOGIN


function get_pro_subscription(){
 swal({
   title: "Please Subscription Plan !",
   text: "Choose your plan to subscribe.....",
   icon: "error",
   button: "okay",
   closeOnEsc: false,
   closeOnClickOutside: false
 }).then(function(){
  window.location.href = base_url+'provider-subscription';
});
}

function get_pro_availabilty(){
 swal({
   title: "Please Select Availability !",
   text: "Choose your Service Available day.....",
   icon: "error",
   button: "okay",
   closeOnEsc: false,
   closeOnClickOutside: false
 }).then(function(){
  window.location.href = base_url+'provider-availability';
});
}

function get_pro_account(){
 swal({
   title: "Please Fill Account info !",
   text: "Please Fill Your Account Information for Feature Upgradation.....",
   icon: "error",
   button: "okay",
   closeOnEsc: false,
   closeOnClickOutside: false
 }).then(function(){
  window.location.href = base_url+'provider-availability';
});
}

function  reason_modal(key){
	$('#cancelModal').modal('show');	
	var reason=$('#reason_'+key).val();
	$('.cancel_reason').text(reason);
}

//new


function rate_booking(e)
{


 rating = $("#myInput").val();
 review = $("#review").val();
 booking_id = $("#booking_id").val();
 provider_id = $("#provider_id").val();
 user_id = $("#user_id").val();
 service_id = $("#service_id").val();
 type = $("#type").val();


 if(rating == '')
 {
   $('.error_rating').show();
   return false;
 }
 else if(review == '')
 {
   $('.error_rating').hide();
   $('.error_review').show();
   return false;
 }
 else if(type == '')
 {
   $('.error_rating').hide();
   $('.error_review').hide();
   $('.error_type').show();
   return false;
 }



 $.ajax({

  url: base_url+'user/dashboard/rate_review_post/',
  data: {rating:rating,review:review,booking_id:booking_id,provider_id:provider_id,user_id:user_id,service_id:service_id,type:type,csrf_token_name:csrf_token},
  type: 'POST',
  dataType: 'JSON',
  success: function(response){ 
    swal({
      title: "Rating Updated..!",
      text: "Rating Updated SuccessFully..",
      icon: "success",
      button: "okay",
      closeOnEsc: false,
      closeOnClickOutside: false
    }).then(function(){
      window.location.href = base_url+'user-bookings';
    });
  },
  error: function(error){
    swal({
      title: "Rating Updated..!",
      text: "Rating Not Update..",
      icon: "error",
      button: "okay",
      closeOnEsc: false,
      closeOnClickOutside: false
    }).then(function(){
      window.location.href = base_url+'user-bookings';
    });

  }
});




}

function cancel_booking(e){
  review = $("#cancel_review").val();
  booking_id = $("#cancel_booking_id").val();
  provider_id = $("#cancel_provider_id").val();
  user_id = $("#cancel_user_id").val();
  service_id = $("#cancel_service_id").val();
  if(review == '')
  {
   $('.error_cancel').show();
   return false;
 }
 update_user_booking_status(booking_id,5,0,review);
} 
function provider_cancel_booking(e){
  review = $("#cancel_review").val();
  booking_id = $("#cancel_booking_id").val();
  provider_id = $("#cancel_provider_id").val();
  user_id = $("#cancel_user_id").val();
  service_id = $("#cancel_service_id").val();
  if(review == '')
  {
   $('.error_cancel').show();
   return false;
 }
 var user_type=$('#user_type').val();
 if(user_type=="provider"){
  update_pro_cancel_booking_status(booking_id,7,0,review);
}else{
  update_user_cancel_booking_status(booking_id,5,0,review);
}

}


/*provider accept and reject scenarios*/

function update_pro_booking_status(bookid,status,rowid,category){

  $.confirm({
    title: 'Confirmations..!',
    content: 'Do you want continue on this process..',
    buttons: {
      confirm: function () {

       $.ajax({

         url: base_url+"update_bookingstatus",
         data: {'booking_id':bookid,'status':status,'csrf_token_name':csrf_token},
         type: 'POST',
         dataType: 'JSON',
         beforeSend: function(){
           $(".btn").removeAttr('onclick');    
           $(".btn").removeAttr('data-target');    
           $(".btn").removeAttr('href');    
         },
         success: function(response){

                                if(response=='3'){ // session expiry
                                  swal({
                                    title: "Session was Expired... !",
                                    text: "Session Was Expired ..",
                                    icon: "error",
                                    button: "okay",
                                    closeOnEsc: false,
                                    closeOnClickOutside: false
                                  }).then(function(){
                                    window.location.reload();
                                  });
                                }

                                if(response=='2'){ //not updated
                                  swal({
                                    title: "Somethings wrong !",
                                    text: "Somethings wents to wrongs",
                                    icon: "error",
                                    button: "okay",
                                    closeOnEsc: false,
                                    closeOnClickOutside: false
                                  }).then(function(){
                                    window.location.reload();
                                  });
                                }
                                
                                if(response=='1'){ //not updated
                                  swal({
                                    title: "Updated the booking status !",
                                    text: "Service is Updated successfully...",
                                    icon: "success",
                                    button: "okay",
                                    closeOnEsc: false,
                                    closeOnClickOutside: false
                                  }).then(function(){
                                   if(category==1){
                                     $('#update_pending_div'+rowid).hide();
                                     
                                   }
                                   if(category==2){
                                     $('#update_inprogress_div'+rowid).hide();
                                   }
                                   window.location.reload();
                                 });
                                }


                              }
                            })
     },cancel: function () {

     },
   }
 });
}


/*provider accept and reject scenarios*/

function update_pro_cancel_booking_status(bookid,status,rowid,review){

  $('#myCancel').modal('hide');

  $.ajax({

   url: base_url+"update_bookingstatus",
   data: {'booking_id':bookid,'status':status,'review':review,'csrf_token_name':csrf_token},
   type: 'POST',
   dataType: 'JSON',
   beforeSend: function(){
     $(".btn").removeAttr('onclick');    
     $(".btn").removeAttr('data-target');    
     $(".btn").removeAttr('href');    
   },
   success: function(response){

                                if(response=='3'){ // session expiry
                                  swal({
                                    title: "Session was Expired... !",
                                    text: "Session Was Expired ..",
                                    icon: "error",
                                    button: "okay",
                                    closeOnEsc: false,
                                    closeOnClickOutside: false
                                  }).then(function(){
                                    window.location.reload();
                                  });
                                }

                                if(response=='2'){ //not updated
                                  swal({
                                    title: "Somethings wrong !",
                                    text: "Somethings wents to wrongs",
                                    icon: "error",
                                    button: "okay",
                                    closeOnEsc: false,
                                    closeOnClickOutside: false
                                  }).then(function(){
                                    window.location.reload();
                                  });
                                }
                                
                                if(response=='1'){ //not updated
                                  swal({
                                    title: "Updated the booking status !",
                                    text: "Service is Updated successfully...",
                                    icon: "success",
                                    button: "okay",
                                    closeOnEsc: false,
                                    closeOnClickOutside: false
                                  }).then(function(){


                                   window.location.reload();
                                 });
                                }


                              }
                            });

}


/*user update the status*/

function update_user_booking_status(bookid,status,rowid,review){ 
  if(status==5 || status==7){
   $('#myCancel').modal('hide');
 }
 $.confirm({
  title: 'Confirmations..!',
  content: 'Do you want continue on this proccess..',
  buttons: {
    confirm: function () {
     $.ajax({
       url: base_url+"update_status_user",
       data: {'booking_id':bookid,'status':status,'review':review,'csrf_token_name':csrf_token},
       type: 'POST',
       dataType: 'JSON',
       success: function(response){

                                if(response=='3'){ // session expiry
                                  swal({
                                    title: "Session was Expired... !",
                                    text: "Session Was Expired ..",
                                    icon: "error",
                                    button: "okay",
                                    closeOnEsc: false,
                                    closeOnClickOutside: false
                                  }).then(function(){
                                    window.location.reload();
                                  });
                                }

                                if(response=='2'){ //not updated
                                  swal({
                                    title: "Somethings wrong !",
                                    text: "Somethings wents to wrongs",
                                    icon: "error",
                                    button: "okay",
                                    closeOnEsc: false,
                                    closeOnClickOutside: false
                                  }).then(function(){
                                    window.location.reload();
                                  });
                                }
                                
                                if(response=='1'){ //not updated
                                  swal({
                                    title: "Updated the booking status !",
                                    text: "Service is Updated successfully...",
                                    icon: "success",
                                    button: "okay",
                                    closeOnEsc: false,
                                    closeOnClickOutside: false
                                  }).then(function(){
                                    $('#update_div'+rowid).hide();
                                    window.location.reload();	
                                  });
                                }


                              }
                            })
   },cancel: function () {

   },
 }
});
}

function update_user_cancel_booking_status(bookid,status,rowid,review){ 
  $('#myCancel').modal('hide');
  $.ajax({

   url: base_url+"update_status_user",
   data: {'booking_id':bookid,'status':status,'review':review,'csrf_token_name':csrf_token},
   type: 'POST',
   dataType: 'JSON',
   beforeSend: function(){
     button_loading();
   },
   success: function(response){
    button_unloading();
                                if(response=='3'){ // session expiry
                                  swal({
                                    title: "Session was Expired... !",
                                    text: "Session Was Expired ..",
                                    icon: "error",
                                    button: "okay",
                                    closeOnEsc: false,
                                    closeOnClickOutside: false
                                  }).then(function(){
                                    window.location.reload();
                                  });
                                }

                                if(response=='2'){ //not updated
                                  swal({
                                    title: "Somethings wrong !",
                                    text: "Somethings wents to wrongs",
                                    icon: "error",
                                    button: "okay",
                                    closeOnEsc: false,
                                    closeOnClickOutside: false
                                  }).then(function(){
                                    window.location.reload();
                                  });
                                }
                                
                                if(response=='1'){ //not updated
                                  swal({
                                    title: "Updated the booking status !",
                                    text: "Service is Updated successfully...",
                                    icon: "success",
                                    button: "okay",
                                    closeOnEsc: false,
                                    closeOnClickOutside: false
                                  }).then(function(){
                                    $('#update_div'+rowid).hide();
                                    window.location.reload();	
                                  });
                                }


                              }
                            });

}

function noty_clear(id){
	if(id!=''){
   $.ajax({
     type: "post",
     url: base_url+"home/clear_all_noty",
     data:{csrf_token_name: csrf_token,id:id}, 
     dataType:'json',
     success: function (data) {


       if(data.success){
        $('.notification-list li').remove();
        $('.bg-yellow').text(0);
      }
    }

  });
 }
}

function chat_clear_all(id){ 
  if(id!=''){
   $.ajax({
     type: "post",
     url: base_url+"home/clear_all_chat",
     data:{csrf_token_name: csrf_token,id:id}, 
     dataType:'json',
     success: function (data) {


       if(data.success){
        $('.chat-list li').remove();
        $('.chat-bg-yellow').text(0);
      }
    }

  });
 }
}



//location lat long
function getLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(showPosition);
  } else {
  }
}
function showPosition(position) {
  locations(position.coords.latitude,position.coords.longitude);
}
getLocation();
function locations(lat,lng){ 
	

 var geocoder = new google.maps.Geocoder;
 var latlng = new google.maps.LatLng(lat,lng);
 geocoder.geocode({'location': latlng}, function(results, status) {
  if (status === 'OK') {
   if (results[3]) { 
    var location=results[3].formatted_address;

    $.ajax({
      type: "post",
      url: base_url+"home/current_location",
      data:{csrf_token_name: csrfHash,location:location}, 	
      dataType:'json',
      success: function (data) {
       if(data==2){
        if (results[5]) { 
         var location=results[5].formatted_address;
         $.ajax({
           type: "post",
           url: base_url+"home/current_location",
           data:{csrf_token_name: csrfHash,location:location}, 
           dataType:'json',
           success: function (data) {


           }

         });
       }
     }
   }

 });		
  }else{
    if (results[5]) { 
     var location=results[5].formatted_address;
     $.ajax({
       type: "post",
       url: base_url+"home/current_location",
       data:{csrf_token_name: csrfHash,location:location}, 
       dataType:'json',
       success: function (data) {


       }

     });
   }
 }

}
});
}
var modules=$('#modules_page').val(); 
if(modules=="services" || modules=="service"){

 var placeSearch, autocomplete;

 function initialize() { 
   // Create the autocomplete object, restricting the search
   // to geographical location types.
   autocomplete = new google.maps.places.Autocomplete(
     /** @type {HTMLInputElement} */
     (document.getElementById('service_location')), {
       types: ['geocode']
     });
   
   google.maps.event.addDomListener(document.getElementById('service_location'), 'focus', geolocate);
   autocomplete.addListener('place_changed', get_latitude_longitude);
 }

 function get_latitude_longitude() {
   // Get the place details from the autocomplete object.
   var place = autocomplete.getPlace();
    var key = $("#map_key").val();//alert(map_key);
   //var key = "AIzaSyDeO6sZPbgwN0sWtMi7FhfS-9IAcQQnEs0";
   $.get('https://maps.googleapis.com/maps/api/geocode/json',{address:place.formatted_address,key:key},function(data, status){

     $(data.results).each(function(key,value){

       $('#service_address').val(place.formatted_address);
       $('#service_latitude').val(value.geometry.location.lat);
       $('#service_longitude').val(value.geometry.location.lng);


     });
   });
 }

 function geolocate() {    

   if (navigator.geolocation) {
     navigator.geolocation.getCurrentPosition(function (position) {

       var geolocation = new google.maps.LatLng(
         position.coords.latitude, position.coords.longitude);
       var circle = new google.maps.Circle({
         center: geolocation,
         radius: position.coords.accuracy
       });
       autocomplete.setBounds(circle.getBounds());

     });
   }
 }

 initialize();

}

if(modules=="home"){

  function search_service() {
    $('#search_service').submit();
  } 
  
}

function toaster_msg(status,msg){

  setTimeout(function () {
    Command: toastr[status](msg);

    toastr.options = {
     "closeButton": false,
     "debug": false,
     "newestOnTop": false,
     "progressBar": false,
     "positionClass": "toast-top-right",
     "preventDuplicates": false,
     "onclick": null,
     "showDuration": "3000",
     "hideDuration": "5000",
     "timeOut": "6000",
     "extendedTimeOut": "1000",
     "showEasing": "swing",
     "hideEasing": "linear",
     "showMethod": "fadeIn",
     "hideMethod": "fadeOut"
   }   
 }, 300);

  
}
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
function getData(page){
  var status=$('#status').val();
  var pagination_page=$('#pagination_current_page').val();
  var target=$('#target').val();
  var csrf_token=$('#csrf_token').val();
  $.ajax({
    method: "POST",
    url: pagination_page+page,
    data: { page: page,csrf_token_name:csrf_token,status:status },

    success: function(data){
      $(target).html(data);
      $('.pagination ul li').removeClass('active');
      $('.page_nos_'+page).parent('li').addClass('active');

    }
  });
}

function getService(page){  
 var pagination_page=$('#pagination_current_page').val();
 var target=$('#target').val();
 var price_range=$('#price_range').val();
 var sort_by=$('#sort_by').val();
 var common_search=$('#common_search').val();
 var categories=$('#categories').val();
 var service_latitude=$('#service_latitude').val();
 var service_longitude=$('#service_longitude').val();

 $.ajax({
  method: "POST",
  url: pagination_page+page,
  data: { page:page,price_range:price_range,sort_by:sort_by,common_search:common_search,categories:categories,service_latitude:service_latitude,service_longitude:service_longitude,csrf_token_name:csrf_token},

  success: function(data){

    var obj=jQuery.parseJSON(data);
    $('#service_count').html(obj.count);
    $(target).html(obj.service_details);
  }
});
}
});

})(jQuery);
