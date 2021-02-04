(function($) {
	"use strict";
	
	var base_url=$('#base_url').val();
	var BASE_URL=$('#base_url').val();
	var csrf_token=$('#csrf_token').val();
	var csrfName=$('#csrfName').val();
	var csrfHash=$('#csrfHash').val();
	
	$( document ).ready(function() {
		$('#history_page').hide();
		$('#home_page').show();
		$('.history_append_fun').on('click',function(){
			var id=$(this).attr('data-token');
			history_append_fun(id);
		});  
		$('.btn_send').on('click',function(){
			btn_send();
		});  
		$('.empty_check').on('keyup',function(){
			empty_check();
		}); 
		$('.search-chat').on('keyup',function(){
			filter(this);
		}); 
	});
	
	var self_token = $('#self_token').val();
	var server_name=$('#server_name').val();
	var img=$('#img').val();

	window.filter = function(element) {
		var value = $(element).val().toUpperCase();
		$(".left_message > li").each(function() {
			if ($(this).text().toUpperCase().search(value) > -1){
				$(this).show();
			}
			else {
				$(this).hide();
			}
		});
	}

	function empty_check(){
		var text=$("#chat-message").val();
		if(text==''){
			$('#submit').attr('disabled',true);
		}else{
			$('#submit').attr('disabled',false);
		}
	}
  
  function chat_clear(){
   var fromToken=$("#fromToken").val();     
   var toToken=$("#toToken").val(); 
   
   $.ajax({

    url:base_url+"user/Chat_ctrl/clear_history",
    type:"post",
    data:{'partner_token':toToken,'self_token':fromToken,'csrf_token_name':csrf_token},
    async:false,
    success:function(data){
      console.log(data);  
      return false;
      if(data==1){
        history_append_fun(toToken);
      }else{
        alert("Please Try Some TIme....");
        console.log(data);  
      }
      
    }
  })
 }

 function get_user_chat_lists(){
   $.ajax({
    url:base_url+"user/Chat_ctrl/get_user_chat_lists",
    type:"post",
    data:{'csrf_token_name':csrf_token},
    async:false,
    success:function(data){
      if(data!=''){
        var res=JSON.parse(data);
        $('.left_message li').remove();
        var add='';
        var path='';
        var badge='';
        $(res.chat_list).each(function(index, value) {

          if(value.profile_img!=''){
            path=base_url+value.profile_img;
          }else{
            path=base_url+'assets/img/user.jpg';
          }
          if(value.badge!=0){
            badge="<span  class='position-absolute badge badge-theme '>"+value.badge+"</span>";
          }else{
           badge="<span  class='position-absolute badge badge-theme '></span>";
         }
         add+='<li class="active history_append_fun" data-token="'+ value.token+'"> <a href="javascript:void(0);"><div class="d-flex bd-highlight">';
         add +='<div class="img_cont">'+badge+'<img src="'+path+'" class="rounded-circle user_img"></div>';
         add +='<div class="user_info"><span class="user-name">'+value.name+'</span><span class="float-right text-muted"></span></div></div></a></li>';
       });
        $('.left_message').append(add);
      }
    }
  });
 }

 function history_append_fun(token){
   $('#home_page').hide();
   $('.badge_count'+token).hide();
   var img=$('#img').val();
   $('#history_page').show();
   $('#load_div').html('<img src='+img+' alt="" />');
   $('#load_div').show();
   var self_token = $('#self_token').val();

   /*change to read status*/
   $.ajax({

    url:base_url+"user/Chat_ctrl/changeToRead_ctrl",
    type:"post",
    data:{'partner_token':token,'self_token':self_token,'csrf_token_name':csrf_token},
    async:false,
    success:function(data){
      if(data==1){
        console.log('updated');
      }else{
        console.log('update not updated');

      }
      
    }
  })
   $.ajax({

    url:base_url+"user/Chat_ctrl/get_chat_history",
    type:"post",
    data:{'partner_token':token,'self_token':self_token,'csrf_token_name':csrf_token},
    async:false,
    success:function(data){

     $.ajax({

       url:base_url+"user/Chat_ctrl/get_token_informations",
       type:"post",
       data:{'partner_token':token,'self_token':self_token,'csrf_token_name':csrf_token},
       async:false,
       success:function(fetch){
        var Data = JSON.parse(fetch);

        $('#from_name').val(Data.self_info.name);
        $('#fromToken').val(Data.self_info.token);
        $('#to_name').val(Data.partner_info.name);
        $('#toToken').val(Data.partner_info.token);
        
        $('#receiver_name').text(Data.partner_info.name);
        
        $("#receiver_image").removeAttr("src");
        
        if(Data.partner_info.profile_img.length>0){
         var img=("src", base_url+Data.partner_info.profile_img);
       }else{
         var img=("src", base_url+'assets/img/user.jpg');
       }

       
       $("#receiver_image").attr("src", img);
     }
   })

     
     $("#chat_box").empty().append(data);
     
   },
   complete: function(){
    $('#load_div').show();
  }
});
 }


 function formatAMPM(date) {
  var hours = date.getHours();
  var minutes = date.getMinutes();
  var ampm = hours >= 12 ? 'pm' : 'am';
  hours = hours % 12;
  hours = hours ? hours : 12; // the hour '0' should be '12'
  minutes = minutes < 10 ? '0'+minutes : minutes;
  var strTime = hours + ':' + minutes + ' ' + ampm;
  return strTime;
}

function showMessage(messageHTML) {

  $('#chat_box').append(messageHTML);
  console.log(messageHTML);
}

function btn_send(){
  var messageJSON = {
    'toToken': $('#toToken').val(),
    'fromToken': $('#fromToken').val(),
    'content': $('#chat-message').val(),
    'fromName': $('#from_name').val(),
    'toName': $('#to_name').val(),

  };

  $.post(base_url+'user-chat/insert_chat',{fromToken: $('#fromToken').val(),toToken:$('#toToken').val(),content:$('#chat-message').val(),csrf_token_name:csrf_token},
    function(response){
      var res=JSON.parse(response);

      if(res.success){ 
       history_append_fun($('#toToken').val());
       $('#chat-message').val('');
       $('#submit').prop('disabled',false);
     }
   });



  /*disabled submit*/
  $('#submit').attr('disabled',true);
}


const chatAppTarget = $('.pbox');
setInterval(function(){ 
  get_user_chat_lists();
}, 30000);
if ($(window).width() > 991)
  chatAppTarget.removeClass('chat-slide');

$(document).on("click",".pbox .left-message li",function () {
  if ($(window).width() <= 991) {
    chatAppTarget.addClass('chat-slide');
  }
  return false;
});
$(document).on("click","#back_user_list",function () {
  if ($(window).width() <= 991) {
    chatAppTarget.removeClass('chat-slide');
  } 
  return false;
});

})(jQuery);