(function($) {
	 "use strict";
	 $(".pay_for").on('change',function(){
		var user_token=$('#user_token').val();
		var provider_token=$('#provider_token').val();
		var val=$('input[name="pay_for"]:checked').val();
		if(val==1 || val=='1'){
			$("#fav_com").text('This service amount favour for Provider');
			$("#token").val(provider_token);
		}else{
			$("#fav_com").text('This service amount favour for User');
			$("#token").val(user_token);
		}
	});
 })(jQuery);