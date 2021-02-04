(function($) {
	"use strict";

	var base_url=$('#base_url').val();
	var BASE_URL=$('#base_url').val();
	var csrf_token=$('#csrf_token').val();
	var csrfName=$('#csrfName').val();
	var csrfHash=$('#csrfHash').val();
	var modules=$('#modules_page').val();
	
  $( document ).ready(function() {
    $('#modal-wizard1').on('hidden.bs.modal', function () {  
      $("form#new_third_page_user").bootstrapValidator('resetForm', true);
      $("form#new_third_page_user")[0].reset();
    });
    $('#modal-wizard').on('hidden.bs.modal', function () {  
      $("form#new_third_page").bootstrapValidator('resetForm', true);
      $("form#new_third_page")[0].reset();
    }); 
    $('#tab_login_modal').on('hidden.bs.modal', function () {  
      $('#flash_error_message1').hide();
      $('#mobile_no_error').hide();
      $('#login_mobile').val("");
    });
    $('#tab_login_modal').on('hidden.bs.modal', function () {  
      $('#otp_error_msg_login').hide();
      $('#login_otp').val("");
      $('#otp_final_div').hide();
      $('#login_form_div').show();
    });

    $('.login_mobile').on('keyup',function(){
      var mobile=$(this).val();
      check_mobile_existing(mobile);
    });
	
	
	$('.login_email').on('keyup',function(){
      var emailid=$(this).val();//alert(emailid);
      check_emailid_existing(emailid);
    });
	
	$('#forgot_mailid').on('keyup',function(){
      var emailid=$(this).val();//alert(emailid);
      check_emailid_existing1(emailid);
    });
	
	
	
	 $('#user_forgot_pwd').on('click',function(){
      forgotpwd_function();
    });
	
    $('#login_submit').on('click',function(){
      login_function();
    });
	
	$('#emaillogin_submit').on('click',function(){
      loginpwd_function();
    });
	
    $('#registration_finals').on('click',function(){
      check_login_otp();
    }); 
	
	$('#emailregistration_finals').on('click',function(){
      check_emaillogin_otp();
    }); 
	
	
	
	$('#emaillogin').on('click',function(){
      check_login_email();
    }); 

	
    $('#step1_footer').on('click',function(){
      var data_step=$(this).attr('data-step');
      sendEvent('#modal-wizard', data_step);
    });
    $('#step2_footer').on('click',function(){
      var data_step=$(this).attr('data-step');
      sendEvent('#modal-wizard', data_step);
    }); 
    $('#step3_footer').on('click',function(){
      var data_step=$(this).attr('data-step');
      sendEvent('#modal-wizard', data_step);
    }); 
    $('#login_resend_otp').on('click',function(){
      login_resend_otp();
    }); 
    $('.no_only').on('keyup',function(e){
     $(this).val($(this).val().replace(/[^\d].+/, ""));
     if ((event.which < 48 || event.which > 57)) {
      event.preventDefault();
    }
  });

    function tab_control_login(type){
      if(type==1 || type=='1'){
        $("#login_mobile").attr('placeholder','Enter Mobile No');
        $('#login_mobile').val('');
      }else{
        $("#login_mobile").attr('placeholder','Enter Mobile No');
        $('#login_mobile').val('');
      }

      $("#login_mode").val(type);
    }

    function check_mobile_existing(mobile){ 
      var c_code=$("#login_country_code").val();

      var mode=$("#login_mode").val();
      var intRegex = /^\d+$/;
      if(intRegex.test(mobile)) {

      }else{
       $('#login_mobile').val('');
     }

     if(mode==1 || mode==2){
      var csrf_token=$('#login_csrf').val();
      $('#login_submit').attr('disabled',true);

       if(mobile.length < 15 && mobile.length > 5){

       $.ajax({
         type: "POST",
         url: base_url + 'user/login/check_mobile_existing',
         data:{ 

           'countryCode':c_code,
           'userMobile':mobile,
           'mode':mode,
           'csrf_token_name':csrf_token,
         },
         
         success: function (res) {
          var res=jQuery.parseJSON(res);

          if(res.data==1){
           $('#mobile_no_error').hide();
           $('#login_mode').val(res.mode);
           $('#login_submit').attr('disabled',false);
         }else{  console.log(res.mode);
          $('#login_mode').val(res.mode);
          $('#mobile_no_error').show();
          $('#mobile_no_error').text('Mobile number not registered or Deactivated...').css('color','red');
          $('#login_submit').attr('disabled',true);

        }

      }
  })//ajax
     }

   }

 }
 
 function check_emailid_existing(emailid){ 
      
	  var mode=$("#login_mode").val();
      
     if(mode==1 || mode==2){
      var csrf_token=$('#login_csrf').val();
      $('#emaillogin_submit').attr('disabled',true);

       if(emailid.length > 0){

       $.ajax({
         type: "POST",
         url: base_url + 'user/login/chkmailexist',
         data:{ 
           'userEmail':emailid,
           'mode':mode,
           'csrf_token_name':csrf_token
         },
         
         success: function (res) {
          var res=jQuery.parseJSON(res);
		console.log(res);
          if(res.data==1){
           $('#mailid_error').hide();
           $('#login_mode').val(res.mode);
           $('#emaillogin_submit').attr('disabled',false);
         }else{  console.log(res.mode);
          $('#login_mode').val(res.mode);
          $('#mailid_error').show();
          $('#mailid_error').text('Email ID not registered or Deactivated...').css('color','red');
          $('#emaillogin_submit').attr('disabled',true);

        }

      }
  })//ajax
     }

   }

 }
 
  function check_emailid_existing1(emailid){ 
      
	  var mode=$("#login_mode1").val();alert(mode);
      
     if(mode==1 || mode==2){
      var csrf_token=$('#login_csrf').val();
      //$('#login_submit').attr('disabled',true);

       if(emailid.length > 0){

       $.ajax({
         type: "POST",
         url: base_url + 'user/login/chkmailexist',
         data:{ 
           'userEmail':emailid,
           'mode':mode,
           'csrf_token_name':csrf_token
         },
         
         success: function (res) {
          var res=jQuery.parseJSON(res);
		console.log(res);
          if(res.data==1){
           $('#mailid_error').hide();
           $('#login_mode1').val(res.mode);
           $('#login_submit').attr('disabled',false);
         }else{  console.log(res.mode);
          $('#login_mode1').val(res.mode);
          $('#mailid_error').show();
          $('#mailid_error').text('Email ID not registered or Deactivated...').css('color','red');
          $('#login_submit').attr('disabled',true);

        }

      }
  })//ajax
     }

   }

 }


function forgotpwd_function(){

  var email=$("#login_email_hide").val();
  var mode=$('#login_mode_hide').val();
 

  
 // if(mode==1 || mode=='1'){ //provider login
   $("#flash_error_message1").hide();


   var csrf_token=$('#fp_csrf').val();
   $.ajax({
     type: "POST",
     url: base_url+"user/login/checkforgotmail",
     data:{ 
      'mode':mode,
      'email':email,
      'csrf_token_name':csrf_token,
    },
    success: function (data) { 

//alert(data);return false;
     var obj = JSON.parse(data);

     if(data==1)
		   {
			 //window.location = base_url+'dashboard';
			 $("#err_respwd").html("Reset Link Sent To Your Mail ID...!").css("color","green");
			 //window.location = base_url+'admin';
		   }
		   else {
			//location.reload();
			$("#err_respwd").html("Something went wrong...!").css("color","red");
		  }

   }
 })



 

}




  $('#resetpwdadmin').bootstrapValidator({
    fields: {
      new_password:   {
        validators:          {
          notEmpty:              {
            message: 'Please enter your New Password'
          }
        }
      },
	  
	  confirm_password:   {
        validators:          {
          notEmpty:              {
            message: 'Please enter your Confirm Password'
          }
        }
      }
    }
  }).on('success.form.bv', function(e) {

    var new_password = $('#new_password').val();
    var confirm_password = $('#confirm_password').val();
	
	if(new_password == confirm_password)
	{
		$.ajax({
		 type:'POST',
		 url: base_url+'user/login/save_reset_password',
		 data :  $('#resetpwdadmin').serialize(),
		 success:function(response)
		 {
		   if(response==1)
		   {
			 //window.location = base_url+'dashboard';
			 $("#err_respwd").html("Password Changed SuccessFully...!").css("color","green");
			 window.location = base_url;
		   }
		   else {
			//location.reload();
			$("#err_respwd").html("Something went wrong...!").css("color","red");
		  }
		}
	  });
	}
	else
	{
		$("#err_respwd").html("Password Mismatch...!").css("color","red");
		
	}
    
    return false;
}); 


 function login_function(){

  var c_code=$("#login_country_code").val();
  var mobile=$('#login_mobile').val();
  var mode=$("#login_mode").val();

  if(c_code==''){
    $("#flash_error_message1").show();
    $("#flash_error_message1").text('Please Select Country Code...!');
    return false;
  } 
  if(mobile==''){
    $("#flash_error_message1").show();
    $("#flash_error_message1").text('Please Enter Registered Mobile No...!');
    return false;
  }
  
  if(mode==1 || mode=='1'){ //provider login
   $("#flash_error_message1").hide();

   var c_code=$("#login_country_code").val();
   var mobile=$('#login_mobile').val();
   var mode=$("#login_mode").val();

   $("#login_form_div").hide();
   $("#otp_final_div").show();

   $("#login_country_code_hide").val(c_code);
   $("#login_mobile_hide").val(mobile);
   $("#login_mode_hide").val(mode);
   var csrf_token=$('#login_csrf').val();
   $.ajax({
     type: "POST",
     url: base_url+"user/login/send_otp_request",
     data:{ 
      'category':'',
      'subcategory':'',
      'username':'',
      'email':'',
      'countryCode':c_code,
      'mobileno':mobile,
      'csrf_token_name':csrf_token,
    },
    success: function (data) { 


     var obj = JSON.parse(data);

     if(data.response=='ok')
     {
       swal({
         title: "OTP Send !",
         text: "Your OTP Send to Registered Mobile No.....",
         icon: "success",
         button: "okay",
         closeOnEsc: false,
         closeOnClickOutside: false
       });

     }
     else if(data.response=='error')
     {

       swal({
         title: "OTP Send !",
         text: "Some Things Went To Wrong....!",
         icon: "error",
         button: "okay",
         closeOnEsc: false,
         closeOnClickOutside: false
       });
       location.reload();


     }

   }
 })



 }else{ 
   $("#flash_error_message1").hide();
   var c_code=$("#login_country_code").val();
   var mobile=$('#login_mobile').val();
   var mode=$("#login_mode").val();

   $("#login_form_div").hide();
   $("#otp_final_div").show();

   $("#login_country_code_hide").val(c_code);
   $("#login_mobile_hide").val(mobile);
   $("#login_mode_hide").val(mode);
   var csrf_token=$('#login_csrf').val();
   $.ajax({
     type: "POST",
     url: base_url+"user/login/send_otp_request_user",
     data:{ 
       'email':'',
       'countryCode':c_code,
       'mobileno':mobile,
       'csrf_token_name':csrf_token
     },
     success: function (data) {

       var obj = JSON.parse(data);

       if(data.response=='ok')
       {
         swal({
           title: "OTP Send !",
           text: "Your OTP Send to Registered Mobile No.....",
           icon: "success",
           button: "okay",
           closeOnEsc: false,
           closeOnClickOutside: false
         });

       }
       else if(data.response=='error')
       {

         swal({
           title: "OTP Send !",
           text: "Some Things Went To Wrong....!",
           icon: "error",
           button: "okay",
           closeOnEsc: false,
           closeOnClickOutside: false
         });
         location.reload();


       }

     }
   })
 }

}


function loginpwd_function(){
  var email=$('#login_email').val();
  var mode=$("#login_mode").val();
  if(email==''){
    $("#flash_error_message1").show();
    $("#flash_error_message1").text('Please Enter Registered Email ID...!');
    return false;
  }
  
   $("#flash_error_message1").hide();
   var email=$('#login_email').val();
   var mode=$("#login_mode").val();

   $("#login_form_div").hide();
   $("#otp_final_div").show();

   //$("#login_country_code_hide").val(c_code);
   $("#login_email_hide").val(email);
   $("#login_mode_hide").val(mode);
   var csrf_token=$('#login_csrf').val();
}





function check_login_email(){
 
  var mode=$("#login_mode").val();
  var csrf_token=$('#login_csrf').val();
  if(mode==1){ //provider

   $.ajax({
     type: "POST",
     url: base_url+"user/login/check_otp",
     data:{  
       otp:otp,
       mobileno:mobile,
       country_code:c_code,
       category:'',
       subcategory:'',
       name:'',
       email:'',
       'csrf_token_name':csrf_token,

     }, 

     success: function (data) {

       var data=jQuery.parseJSON(data);

       if(data.response=='ok')
       {

         window.location.reload();
       }
       else if(data.response=='error')
       {
         $('#otp_error_msg_login').show();
         $('#otp_error_msg_login').text(data.msg).css('color','red');
         if(data.result=='otp_expired')
         {
           $('#registration_resend').show();
           $('#registration_final').addClass('invisible');

           $('#registration_resend').removeClass('invisible');


         }
       }

     }
   })

 }else{
  $.ajax({
   type: "POST",
   url: base_url+"user/login/check_otp_user",
   data:{  
     otp:otp,
     mobileno:mobile,
     country_code:c_code,
     csrf_token_name:csrf_token,
     name:'',
     email:'',


   }, 

   success: function (data) { 

     var data=jQuery.parseJSON(data);

     if(data.response=='ok')
     {

       window.location.reload();
     }
     else if(data.response=='error')
     {
       $('#otp_error_msg_login').show();
       $('#otp_error_msg_login').text(data.msg).css('color','red');
       if(data.result=='otp_expired')
       {
         $('#registration_resend').show();
         $('#registration_final').addClass('invisible');

         $('#registration_resend').removeClass('invisible');


       }
     }

   }
 })

} 


}

function check_login_otp(){
  var c_code=$("#login_country_code_hide").val();
  var mobile=$("#login_mobile_hide").val();
  var mode=$("#login_mode_hide").val();
  var otp=$("#login_otp").val();
  var csrf_token=$('#login_csrf').val();
  if(mode==1){ //provider

   $.ajax({
     type: "POST",
     url: base_url+"user/login/check_otp",
     data:{  
       otp:otp,
       mobileno:mobile,
       country_code:c_code,
       category:'',
       subcategory:'',
       name:'',
       email:'',
       'csrf_token_name':csrf_token,

     }, 

     success: function (data) {

       var data=jQuery.parseJSON(data);

       if(data.response=='ok')
       {

         window.location.reload();
       }
       else if(data.response=='error')
       {
         $('#otp_error_msg_login').show();
         $('#otp_error_msg_login').text(data.msg).css('color','red');
         if(data.result=='otp_expired')
         {
           $('#registration_resend').show();
           $('#registration_final').addClass('invisible');

           $('#registration_resend').removeClass('invisible');


         }
       }

     }
   })

 }else{
  $.ajax({
   type: "POST",
   url: base_url+"user/login/check_otp_user",
   data:{  
     otp:otp,
     mobileno:mobile,
     country_code:c_code,
     csrf_token_name:csrf_token,
     name:'',
     email:'',


   }, 

   success: function (data) { 

     var data=jQuery.parseJSON(data);

     if(data.response=='ok')
     {

       window.location.reload();
     }
     else if(data.response=='error')
     {
       $('#otp_error_msg_login').show();
       $('#otp_error_msg_login').text(data.msg).css('color','red');
       if(data.result=='otp_expired')
       {
         $('#registration_resend').show();
         $('#registration_final').addClass('invisible');

         $('#registration_resend').removeClass('invisible');


       }
     }

   }
 })

} 


}



function check_emaillogin_otp(){
 
  var email=$("#login_email_hide").val();
  var login_password=$("#login_password").val();
  var mode=$("#login_mode_hide").val();
  var csrf_token=$('#login_csrf').val();
  if(mode==1){ //provider

   $.ajax({
     type: "POST",
     url: base_url+"user/login/checkemaillogin",
     data:{  
       email:email,
       login_password:login_password,
       'csrf_token_name':csrf_token,

     }, 

     success: function (data) {

       var data=jQuery.parseJSON(data);

       if(data.response=='ok')
       {

         window.location.reload();
       }
       else if(data.response=='error')
       {
         $('#otp_error_msg_login').show();
         $('#otp_error_msg_login').text(data.msg).css('color','red');
         if(data.result=='otp_expired')
         {
           $('#registration_resend').show();
           $('#emailregistration_finals').addClass('invisible');

           $('#registration_resend').removeClass('invisible');


         }
       }

     }
   })

 }else{
  $.ajax({
   type: "POST",
   url: base_url+"user/login/checkemaillogin_user",
   data:{  
		email:email,
		login_password:login_password,
       'csrf_token_name':csrf_token,
	   }, 

   success: function (data) { 

     var data=jQuery.parseJSON(data);
	console.log(data);
     if(data.response=='ok')
     {

       window.location.reload();
     }
     else if(data.response=='error')
     {
       $('#otp_error_msg_login').show();
       $('#otp_error_msg_login').text(data.msg).css('color','red');
     }

   }
 })

} 


}



function login_resend_otp(){
  var c_code=$("#login_country_code_hide").val();
  var mobile=$("#login_mobile_hide").val();
  var mode=$("#login_mode_hide").val();

  if(mode==1 || mode=='1'){

   $.ajax({

     url: base_url+"user/login/re_send_otp_provider",
     data: {'mobile_no':mobile,'country_code':c_code,'csrf_token_name':csrf_token},
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
     console.log(response);
   }

   
 })

 }else{

   $.ajax({

     url: base_url+"user/login/re_send_otp_user",
     data: {'mobile_no':mobile,'country_code':c_code,'csrf_token_name':csrf_token},
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
      console.log(response);
    }

  })


 }
 
}






var sendEvent = function(sel, step) { 
  $(sel).trigger('next.m.' + step);
}
var checked = '';

$('#new_third_page').bootstrapValidator({
excluded: ':disabled',
 fields: {
   userName: {
     validators: {
       notEmpty: {
         message: 'Please enter your name'
       }
     }
   }, agreeCheckbox: {
     validators: {
       notEmpty: {
         message: 'Please Select Agreement'
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
           'csrf_token_name':$('#login_csrf').val(),
           checked: checked

         };
       },
       message: 'This email is already exist',
       type: 'POST'
     },
     notEmpty: {
       message: 'Please enter email address'
     },

     regexp: {
      regexp: '^[^@\\s]+@([^@\\s]+\\.)+[^@\\s]+$',
      message: 'The value is not a valid email address'
    },
  }
},
userPassword: {
     validators: {
       notEmpty: {
         message: 'Please enter your password'
       }
     }
   }, 
userMobile: {
 validators: {

  remote: {

   url: base_url + 'user/login/mobileno_chk',
   data: function(validator) {
     return {
       userMobile: validator.getFieldElements('userMobile').val(),
       'csrf_token_name':$('#login_csrf').val(),
       countryCode: validator.getFieldElements('countryCode').val(),
       checked: checked
     };
   },

   message: 'This mobile number is already exist or invalid',
   type: 'POST'
 },
 notEmpty: {
   message: 'Please Enter Mobile Number'
 },
 regexp: {
  regexp: /^[1-9][0-9]{0,15}$/,
  message: "Invalid Mobile Number"
	}, 
}
},

}
}).on('success.form.bv', function(e) {

  var categorys=    $('#categorys').val();
  var subcategorys= $('#subcategorys').val();
  var userName=     $('#userName').val();
  var userEmail=    $('#userEmail').val();
  var UserPassword=    $('#UserPassword').val();
  var userMobile=   $('#userMobile').val();
  var countryCode=   $('#countryCode').val();
  var user_logintype=   $('#user_logintype').val();
  var agree=   $('#agree_checkbox').val();
  
  if(user_logintype=='email')
  {
	  $.ajax({
   type: "POST",
   url: base_url+"user/login/emailregisterprovider",
   data:{ 
   'category':categorys,
	'subcategory':subcategorys,
     'name':userName,
     'email':userEmail,
     'password':UserPassword,
     'country_code':countryCode,
     'is_agree':agree,
     'csrf_token_name':csrf_token,
     'mobileno':userMobile
   },
   beforeSend: function() {
     button_loading();
   },
   success: function (data) { 
     button_unloading();

     var obj = JSON.parse(data);

     if(obj.response=='ok')
     { 
		window.location.reload();
     }
     else
     {   console.log(obj.response);
       swal({
         title: "Something Went Wrong",
         text: obj.msg,
         icon: "error",
         button: "okay",
         closeOnEsc: false,
         closeOnClickOutside: false
       }).then((result) => {
        location.reload();
      });
       $('#registration_submit').prop('disabled',false).text('Register');
     }
   }
 } ); 
  }
  else
  {
	   $.ajax({
	   type: "POST",
	   url: base_url+"user/login/send_otp_request",
	   data:{  'category':categorys,
	   'subcategory':subcategorys,
	   'username':userName,
	   'email':userEmail,
	   'countryCode':countryCode,
	   'agree':agree,
	   'mobileno':userMobile,
	   'csrf_token_name':$('#login_csrf').val()
	 },
	  beforeSend: function() {
		 button_loading();
	   },
	 success: function (data) { 

	button_unloading();
	   var obj = JSON.parse(data);


	   if(obj.response=='ok')
	   { 

		 sendEvent('#modal-wizard', 4);
	   }
	   else
	   {
		 $('#registration_submit').prop("disabled", false).text('Register'); 
	   }
	 }
	} );
  }
 
  return false;

});

   //user login

   
   sendEvent = function(sel, step) {  
     $(sel).trigger('next.m.' + step);
   }
   var checked = '';



$('#forgetpwddiv').bootstrapValidator({
     excluded: ':disabled',
     fields: {
     
       forgot_mailid: {
         validators: {
         notEmpty: {
           message: 'Please Enter Email Address...'
         },

         regexp: {
          regexp: '^[^@\\s]+@([^@\\s]+\\.)+[^@\\s]+$',
          message: 'The value is not a valid email address'
        },

      }
    }

}
}).on('success.form.bv', function(e) {

  var forgot_mailid=     $('#forgot_mailid').val();
  
  //console.log(user_logintype);//return false;
   $.ajax({
   type: "POST",
   url: base_url+"user/login/checkforgotmail",
   data:{ 
     'email':forgot_mailid},
   beforeSend: function() {
     button_loading();
   },
   success: function (data) { 
     button_unloading();

     var obj = JSON.parse(data);

     if(obj.response=='ok')
     { 
		window.location.reload();
     }
     else
     {   console.log(obj.response);
       swal({
         title: "Something Went Wrong",
         text: obj.msg,
         icon: "error",
         button: "okay",
         closeOnEsc: false,
         closeOnClickOutside: false
       }).then((result) => {
        location.reload();
      });
       $('#registration_submit_user').prop('disabled',false).text('Register');
     }
   }
 } ); 
 
  return false;

});




   $('#new_third_page_user').bootstrapValidator({
     excluded: ':disabled',
     fields: {
       userName: {
         validators: {
           notEmpty: {
             message: 'Please Enter your name ..'
           }
         }
       },
       agreeCheckboxUser: {
         validators: {
           notEmpty: {
             message: 'Please select Agreement'
           }
         }
       },
       userEmail: {
         validators: {
          remote: {
           url: base_url + 'user/login/email_chk_user',
           data: function(validator) {
             return {
               userEmail: validator.getFieldElements('userEmail').val(),
               csrf_token_name:csrf_token,
               checked: checked

             };
           },
           message: 'This email is already exist...',
           type: 'POST'
         },
         notEmpty: {
           message: 'Please Enter Email Address...'
         },

         regexp: {
          regexp: '^[^@\\s]+@([^@\\s]+\\.)+[^@\\s]+$',
          message: 'The value is not a valid email address'
        },

      }
    },
	userPassword: {
         validators: {
           notEmpty: {
             message: 'Please Enter your password ..'
           }
         }
       },
    userMobile: {
     validators: {

      remote: {

       url: base_url + 'user/login/mobileno_chk_user',
       data: function(validator) {
         return {
           userMobile: validator.getFieldElements('userMobile').val(),
           countryCode: validator.getFieldElements('countryCode').val(),
           csrf_token_name:csrf_token,
           checked: checked
         };
       },

       message: 'This mobile number is already exist or invalid..',
       type: 'POST'
     },
     notEmpty: {
       message: 'Please Enter Mobile Number'
     },
     regexp: {
      regexp: /^[1-9][0-9]{5,15}$/,
      message: "Invalid Mobile Number"
    },
  }
},

}
}).on('success.form.bv', function(e) {

  var userName=     $('#user_name').val();
  var userEmail=    $('#user_email').val();
  var userPassword=    $('#user_password').val();
  var userMobile=   $('#user_mobile').val();
  var countryCode=   $('#country_code').val();
  var user_logintype=   $('#user_logintype').val();
  var agree=$('#agree_checkbox_user').val();
  
  //console.log(user_logintype);//return false;
  
  if(user_logintype=='email')
  {
	  $.ajax({
   type: "POST",
   url: base_url+"user/login/emailregister",
   data:{ 
     'name':userName,
     'email':userEmail,
     'password':userPassword,
     'country_code':countryCode,
     'is_agree':agree,
     'csrf_token_name':csrf_token,
     'mobileno':userMobile
   },
   beforeSend: function() {
     button_loading();
   },
   success: function (data) { 
     button_unloading();

     var obj = JSON.parse(data);

     if(obj.response=='ok')
     { 
		window.location.reload();
     }
     else
     {   console.log(obj.response);
       swal({
         title: "Something Went Wrong",
         text: obj.msg,
         icon: "error",
         button: "okay",
         closeOnEsc: false,
         closeOnClickOutside: false
       }).then((result) => {
        location.reload();
      });
       $('#registration_submit_user').prop('disabled',false).text('Register');
     }
   }
 } ); 
  }
  else
  {
	  $.ajax({
	   type: "POST",
	   url: base_url+"user/login/send_otp_request_user",
	   data:{ 
		 'username':userName,
		 'email':userEmail,
		 'countryCode':countryCode,
		 'agree':agree,
		 'csrf_token_name':csrf_token,
		 'mobileno':userMobile
	   },
	   beforeSend: function() {
		 button_loading();
	   },
	   success: function (data) { 
	   
	   //console.log(data);
		 button_unloading();

		 var obj = JSON.parse(data);

		 if(obj.response=='ok')
		 { 

		   sendEvent('#modal-wizard1', 2);
		 }
		 else
		 {   console.log(obj.response);
		   swal({
			 title: "Something Went Wrong",
			 text: obj.msg,
			 icon: "error",
			 button: "okay",
			 closeOnEsc: false,
			 closeOnClickOutside: false
		   }).then((result) => {
			location.reload();
		  });
		   $('#registration_submit_user').prop('disabled',false).text('Register');
		 }
	   }
	 } ); 
  }
 
  return false;

});

$('#new_fourth_page_user').bootstrapValidator({
 fields: {
   otp_number: {
     validators: {
       notEmpty: {
         message: 'Please Enter OTP Numbers...!'
       }
     }
   },
 }

}).on('success.form.bv', function(e) {

 var otp =         $('#otp_number_user').val();
 var userMobile =  $('#user_mobile').val();
 var userName=     $('#user_name').val();
 var userEmail=    $('#user_email').val();
 var countryCode=  $('#country_code').val();
 var agree=$('#agree_checkbox_user').val();


 
 $.ajax({
   type: "POST",
   url: base_url+"user/login/check_otp_user",
   data:{  
     otp:otp,
     mobileno:userMobile,
     name:userName,
     email:userEmail,
     is_agree:agree,
     csrf_token_name:csrf_token,
     country_code:countryCode,

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

   console.log(data);


 }
});
 return false;
});
$('#registration_resend').on('click', function() {
 sendEvent('#modal-wizard1', 2);
 $('#otp_error_msg').text('');
 $('#registration_submit_user').prop('disabled',false).text('Register'); ;
 $('#otp_number_user').val('');
 $('#registration_resend').addClass('invisible');
 $('#registration_final').removeClass('invisible');

 

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