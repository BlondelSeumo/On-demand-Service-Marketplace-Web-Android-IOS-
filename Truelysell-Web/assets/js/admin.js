(function($) {
  "use strict";
  var csrf_token=$('#admin_csrf').val();
  var base_url=$('#base_url').val();


	// Variables declarations
	
	var $wrapper = $('.main-change_languagewrapper');
	var $pageWrapper = $('.page-wrapper');
	var $slimScrolls = $('.slimscroll');
	$( document ).ready(function() {
   $('#save_profile_change').on('click',function(){
    changeAdminProfile();
  });
  
     $('#adminmail').on('blur',function(){
    
	 var email = $('#adminmail').val();//alert(email);
    $.ajax({
     type:'POST',
     url: base_url+'admin/profile/check_admin_mail',
     data :  {email:email,csrf_token_name:csrf_token},
     success:function(response)
     {
       if(response==1)
       {
         //window.location = base_url+'dashboard';
		 
		 $("#email_error").html("Email ID already exist...!");
		 $("#save_profile_change").prop("disabled",true);
       }
       else {
        //location.reload();
		$("#email_error").html("");
		$("#save_profile_change").prop("disabled",false);
      }
    }
  });
  
  });
  
  
   $('#upload_images').on('click',function(){
    upload_images();
  }); 
 });
	// Sidebar
	
	var Sidemenu = function() {
		this.$menuItem = $('#sidebar-menu a');
	};
	
	function init() {
		var $this = Sidemenu;
		$('#sidebar-menu a').on('click', function(e) {
			if($(this).parent().hasClass('submenu')) {
				e.preventDefault();
			}
			if(!$(this).hasClass('subdrop')) {
				$('ul', $(this).parents('ul:first')).slideUp(350);
				$('a', $(this).parents('ul:first')).removeClass('subdrop');
				$(this).next('ul').slideDown(350);
				$(this).addClass('subdrop');
			} else if($(this).hasClass('subdrop')) {
				$(this).removeClass('subdrop');
				$(this).next('ul').slideUp(350);
			}
		});
		$('#sidebar-menu ul li.submenu a.active').parents('li:last').children('a:first').addClass('active').trigger('click');
	}
	
	// Sidebar Initiate
	init();
	
	// Mobile menu sidebar overlay
	
	$('body').append('<div class="sidebar-overlay"></div>');
	$(document).on('click', '#mobile_btn', function() {
		$wrapper.toggleClass('slide-nav');
		$('.sidebar-overlay').toggleClass('opened');
		$('html').addClass('menu-opened');
		return false;
	});
	
	// Sidebar overlay
	
	$(".sidebar-overlay").on("click", function () {
		$wrapper.removeClass('slide-nav');
		$(".sidebar-overlay").removeClass("opened");
		$('html').removeClass('menu-opened');
	});	
	
	// Select 2
	
	if ($('.select').length > 0) {
		$('.select').select2({
			minimumResultsForSearch: -1,
			width: '100%'
		});
	}

	$(document).on('click', '#filter_search', function() {
		$('#filter_inputs').slideToggle("slow");
	});

	// Datetimepicker
	
	if($('.datetimepicker').length > 0 ){
		$('.datetimepicker').datetimepicker({
			format: 'DD-MM-YYYY',
			icons: {
				up: "fas fa-angle-up",
				down: "fas fa-angle-down",
				next: 'fas fa-angle-right',
				previous: 'fas fa-angle-left'
			}
		});
		$('.datetimepicker').on('dp.show',function() {
			$(this).closest('.table-responsive').removeClass('table-responsive').addClass('temp');
		}).on('dp.hide',function() {
			$(this).closest('.temp').addClass('table-responsive').removeClass('temp')
		});
	}

	// Tooltip
	
	if($('[data-toggle="tooltip"]').length > 0 ){
		$('[data-toggle="tooltip"]').tooltip();
	}
	
    // Datatable

    if ($('.datatable').length > 0) {
      $('.datatable').DataTable({
        "bFilter": false,
      });
    }
    $('.revenue_table').DataTable();
    $('.language_table').DataTable();
    $('.categories_table').DataTable();
    $('.ratingstype_table').DataTable();
    $('.service_table').DataTable();
    $('.payment_table').DataTable();

    // Owl Carousel

    if ($('.images-carousel').length > 0) {
      $('.images-carousel').owlCarousel({
       loop: true,
       center: true,
       margin: 10,
       responsiveClass: true,
       responsive: {
        0: {
         items: 1
       },
       600: {
         items: 1
       },
       1000: {
         items: 1,
         loop: false,
         margin: 20
       }
     }
   });
    }

	// Sidebar Slimscroll

	if($slimScrolls.length > 0) {
		$slimScrolls.slimScroll({
			height: 'auto',
			width: '100%',
			position: 'right',
			size: '7px',
			color: '#ccc',
			allowPageScroll: false,
			wheelStep: 10,
			touchScrollStep: 100
		});
		var wHeight = $(window).height() - 60;
		$slimScrolls.height(wHeight);
		$('.sidebar .slimScrollDiv').height(wHeight);
		$(window).resize(function() {
			var rHeight = $(window).height() - 60;
			$slimScrolls.height(rHeight);
			$('.sidebar .slimScrollDiv').height(rHeight);
		});
	}
	
	// Small Sidebar

	$(document).on('click', '#toggle_btn', function() {
		if($('body').hasClass('mini-sidebar')) {
			$('body').removeClass('mini-sidebar');
			$('.subdrop + ul').slideDown();
		} else {
			$('body').addClass('mini-sidebar');
			$('.subdrop + ul').slideUp();
		}
		setTimeout(function(){ 
			mA.redraw();
			mL.redraw();
		}, 300);
		return false;
	});
	
	$(document).on('mouseover', function(e) {
		e.stopPropagation();
		if($('body').hasClass('mini-sidebar') && $('#toggle_btn').is(':visible')) {
			var targ = $(e.target).closest('.sidebar').length;
			if(targ) {
				$('body').addClass('expand-menu');
				$('.subdrop + ul').slideDown();
			} else {
				$('body').removeClass('expand-menu');
				$('.subdrop + ul').slideUp();
			}
			return false;
		}
		
		$(window).scroll(function() {
      if ($(window).scrollTop() >= 30) {
        $('.header').addClass('fixed-header');
      } else {
        $('.header').removeClass('fixed-header');
      }
    });
		
		$(document).on('click', '#loginSubmit', function() {
			$("#adminSignIn").submit();
		});
		
	});



  $('#adminSignIn').bootstrapValidator({
    fields: {
      username:   {
        validators:          {
          notEmpty:              {
            message: 'Please enter your Username'
          }
        }
      },
      password:           {
        validators:           {
          notEmpty:               {
            message: 'Please enter your Password'
          }
        }
      }
    }
  }).on('success.form.bv', function(e) {

    var username = $('#username').val();
    var password = $('#password').val();
    $.ajax({
     type:'POST',
     url: base_url+'admin/login/is_valid_login',
     data :  $('#adminSignIn').serialize(),
     success:function(response)
     {
       if(response==1)
       {
         window.location = base_url+'dashboard';
       }
       else {
        location.reload();
      }
    }
  });
    return false;
}); 


  $('#forgotpwdadmin').bootstrapValidator({
    fields: {
      email:   {
        validators:          {
          notEmpty:              {
            message: 'Please enter your Email ID'
          }
        }
      }
    }
  }).on('success.form.bv', function(e) {

    var email = $('#email').val();
    $.ajax({
     type:'POST',
     url: base_url+'admin/login/check_forgot_pwd',
     data :  $('#forgotpwdadmin').serialize(),
     success:function(response)
     {
       if(response==1)
       {
         //window.location = base_url+'dashboard';
		 $("#err_frpwd").html("Reset link has been sent to your mail ID, Check your mail.").css("color","green");
       }
       else {
        //location.reload();
		$("#err_frpwd").html("Email ID Not Exist...!").css("color","red");
      }
    }
  });
    return false;
}); 



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
		 url: base_url+'admin/login/save_reset_password',
		 data :  $('#resetpwdadmin').serialize(),
		 success:function(response)
		 {
		   if(response==1)
		   {
			 //window.location = base_url+'dashboard';
			 $("#err_respwd").html("Password Changed SuccessFully...!").css("color","green");
			 window.location = base_url+'admin';
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


  $('#addSubscription').bootstrapValidator({
    fields: {
      subscription_name:   {
        validators: {
          remote: {
            url: base_url + 'service/check_subscription_name',
            data: function(validator) {
              return {
                subscription_name: validator.getFieldElements('subscription_name').val(),
                csrf_token_name:csrf_token
              };
            },
            message: 'This subscription name is already exist',
            type: 'POST'
          },
          notEmpty: {
            message: 'Please enter subscription name'

          }
        }
      },
      amount:           {
        validators:           {
          notEmpty:               {
            message: 'Please enter subscription amount'
          }
        }
      },
      duration:           {
        validators:           {
          notEmpty:               {
            message: 'Please select subscription duration'
          }
        }
      }
    }
  }).on('success.form.bv', function(e) {

    var subscription_name = $('#subscription_name').val();
    var fee_description = $('#subscription_description').val();
    var amount = $('#amount').val();
    var duration = $('#duration').val();
    var status = $('input[name="status"]:checked').val();
    $.ajax({
     type:'POST',
     url: base_url+'service/save_subscription',
     data : {subscription_name:subscription_name,fee_description:fee_description,subscription_amount:amount,subscription_duration:duration,status:status,csrf_token_name:csrf_token},
     success:function(response)
     {
       if(response==1)
       {
         window.location = base_url+'subscriptions';
       }
       else
       {
         window.location = base_url+'subscriptions';
       }
     }
   });
    return false;
        }); 
        
  $('#add_app_keywords').bootstrapValidator({
    fields: {
      page_name:   {
        validators: {
          notEmpty: {
            message: 'Please enter page name'

          }
        }
      },                
  }
}).on('success.form.bv', function(e) {


  return true;
});

  $('#add_language').bootstrapValidator({
    fields: {
      language_name:   {
        validators: {
          notEmpty: {
            message: 'Please enter language name'

          }
        }
      },
      language_value:   {
        validators: {
          notEmpty: {
            message: 'Please enter language value'

          }
        }
      },
      language_type:   {
        validators: {
          notEmpty: {
            message: 'Please enter language type'

          }
        }
      },                  
  }
}).on('success.form.bv', function(e) {


  return true;
});

  $('#admin_settings').bootstrapValidator({
    fields: {
      website_name:   {
        validators: {
          notEmpty: {
            message: 'Please enter website name'

          }
        }
      },
      contact_details:   {
        validators: {
          notEmpty: {
            message: 'Please enter contact details'

          }
        }
      },
      mobile_number:   {
        validators: {
          notEmpty: {
            message: 'Please enter mobile number'

          }
        }
      },
	currency_option:   {
        validators: {
          notEmpty: {
            message: 'Please select currency'

          }
        }
      },
	commission:   {
        validators: {
          notEmpty: {
            message: 'Please enter commission amount'

          }
        }
      },
	  
	  login_type:   {
        validators: {
          notEmpty: {
            message: 'Please select Login type'

          }
        }
      },
	paypal_gateway:   {
        validators: {
          notEmpty: {
            message: 'Please enter paypal gateway'

          }
        }
      },
	braintree_key:   {
        validators: {
          notEmpty: {
            message: 'Please enter braintree key'

          }
        }
      },
	site_logo:           {
		   validators:           {
			file: {
			  extension: 'jpeg,png,jpg',
			  type: 'image/jpeg,image/png,image/jpg',
			  message: 'The selected file is not valid. Only allowed jpeg,jpg,png files'
			}
		  }
		},
	favicon:           {
		   validators:           {
			file: {
			  extension: 'png,ico',
			  type: 'image/png,image/ico',
			  message: 'The selected file is not valid. Only allowed ico,png files'
			}
			
		  }
		},	
  }
}).on('success.form.bv', function(e) {


  return true;
});

  $('#add_category').bootstrapValidator({
    fields: {
      category_name:   {
        validators: {
          remote: {
            url: base_url + 'categories/check_category_name',
            data: function(validator) {
              return {
                category_name: validator.getFieldElements('category_name').val(),
                csrf_token_name:csrf_token
              };
            },
            message: 'This category name is already exist',
            type: 'POST'
          },
          notEmpty: {
            message: 'Please enter category name'

          }
        }
      },
      category_image:           {
       validators:           {
        file: {
          extension: 'jpeg,png,jpg',
          type: 'image/jpeg,image/png,image/jpg',
          message: 'The selected file is not valid. Only allowed jpeg,jpg,png files'
        },
        notEmpty:               {
          message: 'Please upload category image'
        }
      }
    },
    category_mobile_icon:           {
      validators:           {
        file: {
          extension: 'jpeg,png',
          type: 'image/jpeg,image/png',
          message: 'The selected file is not valid. Only allowed jpeg,png files'
        },

        notEmpty:               {
          message: 'Please upload category mobile icon'
        }
      }
    }                    
  }
}).on('success.form.bv', function(e) {


  return true;
});  

$('#update_category').bootstrapValidator({
  fields: {
    category_name:   {
      validators: {
        remote: {
          url: base_url + 'categories/check_category_name',
          data: function(validator) {
            return {
              category_name: validator.getFieldElements('category_name').val(),
              csrf_token_name:csrf_token,
              category_id: validator.getFieldElements('category_id').val()
            };
          },
          message: 'This category name is already exist',
          type: 'POST'
        },
        notEmpty: {
          message: 'Please enter category name'

        }
      }
    },
     category_image:           {
       validators:           {
        file: {
          extension: 'jpeg,png,jpg',
          type: 'image/jpeg,image/png,image/jpg',
          message: 'The selected file is not valid. Only allowed jpeg,jpg,png files'
        }
      }
    },

  }
}).on('success.form.bv', function(e) {


  return true;
        });   



$('#add_subcategory').bootstrapValidator({
  fields: {
    subcategory_name:   {
      validators: {
        remote: {
          url: base_url + 'categories/check_subcategory_name',
          data: function(validator) {
            return {
              category: validator.getFieldElements('category').val(),
              csrf_token_name:csrf_token,
              subcategory_name: validator.getFieldElements('subcategory_name').val()
            };
          },
          message: 'This sub category name is already exist',
          type: 'POST'
        },
        notEmpty: {
          message: 'Please enter sub category name'

        }
      }
    },

    subcategory_image:           {
       validators:           {
        file: {
          extension: 'jpeg,png,jpg',
          type: 'image/jpeg,image/png,image/jpg',
          message: 'The selected file is not valid. Only allowed jpeg,jpg,png files'
        },
        notEmpty:               {
          message: 'Please upload category image'
        }
      }
    },
    category:           {
      validators:           {
        notEmpty:               {
          message: 'Please select category'
        }
      }
    }                  
  }
}).on('success.form.bv', function(e) {


  return true;
});  



$('#update_subcategory').bootstrapValidator({
  fields: {
    subcategory_name:   {
      validators: {
        remote: {
          url: base_url + 'categories/check_subcategory_name',
          data: function(validator) {
            return {
              category: validator.getFieldElements('category').val(),
              subcategory_name: validator.getFieldElements('subcategory_name').val(),
              csrf_token_name:csrf_token,
              subcategory_id: validator.getFieldElements('subcategory_id').val()
            };
          },
          message: 'This sub category name is already exist',
          type: 'POST'
        },
        notEmpty: {
          message: 'Please enter sub category name'

        }
      }
    },
     subcategory_image:           {
       validators:           {
        file: {
          extension: 'jpeg,png,jpg',
          type: 'image/jpeg,image/png,image/jpg',
          message: 'The selected file is not valid. Only allowed jpeg,jpg,png files'
        }
      }
    },
    category:           {
      validators:           {
        notEmpty:               {
          message: 'Please select category'
        }
      }
    } 

  }
}).on('success.form.bv', function(e) {


  return true;
        });   

$('#add_ratingstype').bootstrapValidator({
  fields: {
    name:   {
      validators: {
        remote: {
          url: base_url + 'ratingstype/check_ratingstype_name',
          data: function(validator) {
            return {
              category_name: validator.getFieldElements('name').val(),
              csrf_token_name:csrf_token
            };
          },
          message: 'This Rating type name is already exist',
          type: 'POST'
        },
        notEmpty: {
          message: 'Please enter rating type name'

        }
      }
    },
  }
}).on('success.form.bv', function(e) {


  return true;
});

$('#update_ratingstype').bootstrapValidator({
  fields: {
    name:   {
      validators: {
        remote: {
          url: base_url + 'ratingstype/check_ratingstype_name',
          data: function(validator) {
            return {
              name: validator.getFieldElements('name').val(),
              csrf_token_name:csrf_token,
              id: validator.getFieldElements('id').val()
            };
          },
          message: 'This rating type name is already exist',
          type: 'POST'
        },
        notEmpty: {
          message: 'Please enter rating type name'

        }
      }
    },

  }
}).on('success.form.bv', function(e) {


  return true;
        });   




$("#duration").on("change", function(){
  var description = $("#duration option:selected").text();
  $("#subscription_description").val(description);
})

$('#editSubscription').bootstrapValidator({
  fields: {
    subscription_name:   {
      validators: {
        remote: {
          url: base_url + 'service/check_subscription_name',
          data: function(validator) {
            return {
              subscription_name: validator.getFieldElements('subscription_name').val(),
              csrf_token_name:csrf_token,
              subscription_id: validator.getFieldElements('subscription_id').val()
            };
          },
          message: 'This subscription name is already exist',
          type: 'POST'
        },
        notEmpty: {
          message: 'Please enter subscription name'

        }
      }
    },
    amount:           {
      validators:           {
        notEmpty:               {
          message: 'Please enter subscription amount'
        }
      }
    },
    duration:           {
      validators:           {
        notEmpty:               {
          message: 'Please select subscription duration'
        }
      }
    }
  }
}).on('success.form.bv', function(e) {

  var subscription_id = $('#subscription_id').val();
  var subscription_name = $('#subscription_name').val();
  var fee_description = $('#subscription_description').val();
  var amount = $('#amount').val();
  var duration = $('#duration').val();
  var status = $('input[name="status"]:checked').val();
  $.ajax({
   type:'POST',
   url: base_url+'service/update_subscription',
   data : {subscription_id:subscription_id,subscription_name:subscription_name,fee_description:fee_description,subscription_amount:amount,subscription_duration:duration,status:status,csrf_token_name:csrf_token,},
   success:function(response)
   {
     if(response==1)
     {
       window.location = base_url+'subscriptions';
     }
     else
     {
       window.location = base_url+'subscriptions';
     }
   }
 });
  return false;
        }); 

$('#addKeyword').bootstrapValidator({
  fields: {
    multiple_key:           {
      validators:           {
        notEmpty:               {
          message: 'Please enter keyword'
        }
      }
    }
  }
}).on('success.form.bv', function(e) {

  var page_key = $('#page_key').val();
  var multiple_key = $('#multiple_key').val();
  $.ajax({
   type:'POST',
   url: base_url+'admin/language/save_keywords',
   data : {page_key:page_key,multiple_key:multiple_key},
   success:function(response)
   {
     if(response==1)
     {
       window.location = base_url+'language/'+page_key;
     }
   }
 });
  return false;
        }); 

$('#image_upload_error').hide();
$('#image_error').hide();


var csrf_toiken=$('#admin_csrf').val();
var url = base_url+'admin/profile/check_password';

$('#change_password_form').bootstrapValidator({
  fields: {
    current_password: {
      validators: {
        remote: {
         url: url,
         data: function(validator) {
           return {
             current_password: validator.getFieldElements('current_password').val(),
             'csrf_token_name':csrf_token
           };
         },
         message: 'Current Password is Not Valid',
         type: 'POST'
       },
       notEmpty: {
        message: 'Please Enter Current Password'
      }
    }
  },

  new_password: {
    validators: {
     stringLength: {
      min: 4,
      message: 'The full name must be less than 4 characters'
    },
    different: {
      field: 'current_password',
      message: 'The username and password cannot be the same as each other'
    },
    notEmpty: {
      message: 'Please Enter Password...'
    }
  }
},
confirm_password: {
  validators: {
   identical: {
    field: 'new_password',
    message: 'The password and its confirm are not the same'
  },
  notEmpty: {
    message: 'Please Enter Password...'
  }
}
}                    
}
}).on('success.form.bv', function(e) {
  e.preventDefault();
  $.ajax({
    url: base_url+'admin/profile/change_password',
    type: "post",
    data: $('#change_password_form').serialize(),
    success: function(response) {
      swal({
        title: "Password Updated..!",
        text: "Password Updated SuccessFully..",
        icon: "success",
        button: "okay",
        closeOnEsc: false,
        closeOnClickOutside: false
      }).then(function(){
        location.reload();
      });
    }
  });

});    



function update_language(lang_key, lang, page_key) {
	var cur_val = $('input[name="'+lang_key+'['+lang+']"]').val();
	var prev_val = $('input[name="prev_'+lang_key+'['+lang+']"]').val();

	$.post(base_url+'admin/language/update_language',{lang_key:lang_key, lang:lang, cur_val:cur_val, page_key:page_key},function(data){
		if(data == 1) {
			$("#flash_success_message").show();
		}
		else if(data == 0) {
			$('input[name="'+lang_key+'['+lang+']"]').val(prev_val);
			$("#flash_error_message").html('Sorry, This keyword already exist!');
			$("#flash_error_message").show();
		}
		else if(data == 2) {
			$('input[name="'+lang_key+'['+lang+']"]').val(prev_val);
			$("#flash_error_message").html('Sorry, This field should not be empty!');
			$("#flash_error_message").show();
		}
	});
}

function upload_images(){
	var img= $('.avatar-input').val();
	if(img!=''){
		$('#image_upload_error').hide();
		return true;
	}else{
		$('#image_upload_error').text('Please Upload an Image . ');
		$('#image_upload_error').show();
		return false;
	}
}

function changeAdminProfile(){
	$('#image_error').hide();
	var profile_img = $('#crop_prof_img').val();
	var adminmail = $('#adminmail').val();
	
	var error = 0;
	
	
	// if(profile_img==""){
		// error =1;
		// $('#image_error').text('Please select an image.');
		// $('#image_error').show();
	// }else{
		// $('#image_error').hide();
	// }
	
	
	if(error==0){
		var url = base_url+'admin/profile/update_profile';
		//fetch file
		var formData = new FormData();
		formData.append('profile_img', profile_img);
		formData.append('adminmail', adminmail);
		formData.append('csrf_token_name', csrf_token);
		$.ajax({
			url: url,
			type: "POST",
			data: formData,
			cache: false,
			processData: false,
			contentType: false,
			context: this,
			success:function(res)
			{
       window.location.href=base_url+'admin-profile';
     }
   });
	}
}

function delete_category(id) {
	$('#delete_category').modal('show');
	$('#category_id').val(id);
}

function delete_subcategory(id) {
	$('#delete_subcategory').modal('show');
	$('#subcategory_id').val(id);
}

function delete_ratings_type(id) {
	$('#delete_ratings_type').modal('show');
	$('#id').val(id);
}


 $(document).on("click", ".delete_show", function () {
    var id=$(this).attr('data-id');
    delete_modal_show(id);
  });
  
  function delete_modal_show(id){
    $('#delete_modal').modal('show');
    $('#confirm_btn').attr('data-id',id);
    $('#confirm_delete_pro').attr('data-id',id);
	$('#confirm_btn_admin').attr('data-id',id);
  }
    $('#confirm_btn_admin').on('click',function(){ 
    var id=$(this).attr('data-id');
    var url=base_url+"admin/dashboard/adminuser_delete";
    delete_confirm(id,url);
  });
   function delete_confirm(id,url){
    if(id!=''){
      $('#delete_modal').modal('hide');
       $.ajax({
     type:'POST',
     url: url,
     data : {id:id,csrf_token_name:csrf_token},
     dataType:'json',
     success:function(response)
     {
       if(response.status)
       {
        swal({
          title: "Success..!",
          text: response.msg,
          icon: "success",
          button: "okay",
          closeOnEsc: false,
          closeOnClickOutside: false
        }).then(function(){
          location.reload();
        });

      }
      else {
       swal({
        title: "Error..!",
        text: response.msg,
        icon: "error",
        button: "okay",
        closeOnEsc: false,
        closeOnClickOutside: false
      }).then(function(){
        location.reload();
      });
    }
  }
});
    }
  }

})(jQuery);