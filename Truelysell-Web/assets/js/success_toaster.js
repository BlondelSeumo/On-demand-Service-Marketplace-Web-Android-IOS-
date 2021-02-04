(function($) {
	"use strict";
	$( document ).ready(function() {
		var success_message=$('#success_message').text();
		toaster_msg('success',success_message);
	});
	
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
})(jQuery);