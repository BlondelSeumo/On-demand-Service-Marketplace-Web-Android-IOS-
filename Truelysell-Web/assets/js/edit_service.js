(function($) {
   "use strict";
   var names = [];
   var service_id=$('#service_id').val();


function file_browse() {
	var service_id=$('#service_id').val();
	var formData = new FormData();
	for (var i = 0, len = document.getElementById('images').files.length; i < len; i++) {
		formData.append("images" + i, document.getElementById('images').files[i]);
	}
	formData.append('service_id',service_id);
	console.log(formData);
	ajax_data(formData);
}
function ajax_data(formData) {
	$.ajax({
		url: base_url+'user/service/sevice_images', // our php file
		type: 'post',
		data: formData,
		dataType: 'html', // we return html from our php file
		async: true,
		processData: false,  // tell jQuery not to process the data
		contentType: false,   // tell jQuery not to set contentType
		success : function(data) {
			console.log(data);
		},
		error : function(request) {
			console.log(request.responseText);
		}
	});
}

})(jQuery);