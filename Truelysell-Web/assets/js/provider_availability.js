(function($) {
	"use strict";

	var base_url=$('#base_url').val();
	var BASE_URL=$('#base_url').val();
	var csrf_token=$('#csrf_token').val();
	var csrfName=$('#csrfName').val();
	var csrfHash=$('#csrfHash').val();
	
	$( document ).ready(function() {
		$('.select_from_time').on('change',function(){
			var id=$(this).attr('data-id');
			select_from_time(id);
		}); 
		$('.validate_time').on('click',function(){
			var id=$(this).attr('data-id');
			validate_time(id);
		}); 
		$('#update_service').on('submit',function(){
			var result=subCheck();
			return result;
		}); 
		
	});
	$(document).on('change','.daysfromtime_check',function(){
		var time = $(this).val();
		var time_digit = parseInt(time);

		var select_html  = '<option value="">Select Time</option>';

		for(var i=1; i<=23; i++){

			var nexttime =  parseInt(i);
			if(nexttime.toString().length < 2){
				nexttime = '0'+ parseInt(nexttime);
			}

			var timeval = nexttime+':00:00';
			var timeString = nexttime+':00:00';
			var H = +timeString.substr(0, 2);
			var h = H % 12 || 12;
			var ampm = H < 12 ? " AM" : " PM";
			timeString = h + timeString.substr(2, 3) + ampm;

			if(time_digit != i && time_digit < i){
				select_html += '<option value="'+timeString+'">'+timeString+'</option>';
			}
		}
		select_html += '<option value="12:00 AM">12:00 AM</option>';
		$('.daystotime_check').html(select_html);
	});


	function select_from_time(id){
		var time = $(".eachdayfromtime"+id).val();
		var time_digit = parseInt(time);
		var select_html  = '<option value="">Select Time</option>';

		for(var i=1; i<=23; i++){
			var nexttime =  parseInt(i);
			if(nexttime.toString().length < 2){
				nexttime = '0'+ parseInt(nexttime);
			}

			var timeval = nexttime+':00:00';
			var timeString = nexttime+':00:00';
			var H = +timeString.substr(0, 2);
			var h = H % 12 || 12;
			var ampm = H < 12 ? " AM" : " PM";
			timeString = h + timeString.substr(2, 3) + ampm;

			if(time_digit != i && time_digit < i){
				select_html += '<option value="'+timeString+'">'+timeString+'</option>';
			}
		}
		select_html += '<option value="12:00 AM">12:00 AM</option>';
		$('.eachdaytotime'+id).html(select_html);
	}
       
	function subCheck() {
		var test =true; 
		if ($(".days_check").prop('checked')==true){
			var all_from=$(".daysfromtime_check").val();
			var all_to=$(".daystotime_check").val();

			if(all_from=='' || all_to==''){
				swal({
					title: "Wrong Selection !",
					text: "Please Select Day Relevant From & To Time....!",
					icon: "error",
					button: "okay",
				});
				test=false;

			}

		}else{
			var row=1;
			$('.eachdays').each(function(){
				if ($(".eachdays"+row).prop('checked')==true){
					var from_time=$('.eachdayfromtime'+row).val();
					var to_time=$('.eachdaytotime'+row).val();
					if(from_time=='' || to_time==''){
						swal({
							title: "Wrong Selection...!",
							text: "Please Select Day Relevant From & To Time....!",
							icon: "error",
							button: "okay",
						});

						test=false;
					}

				}

				/*from time validate*/

				if($('.eachdayfromtime'+row).val() !=''){


					var to_time=$('.eachdaytotime'+row).val();

					if($(".eachdays"+row).prop('checked')==false || to_time ==''){
						swal({
							title: "Wrong Selection...!",
							text: "Please Select All Day Relevant From & To Time....!",
							icon: "error",
							button: "okay",
						});

						test=false;
					}

				}

				/*to time Validate*/

				if($('.eachdaytotime'+row).val()!=''){
					var from_time=$('.eachdaytotime'+row).val();
					if($(".eachdays"+row).prop('checked')==false || from_time ==''){
						swal({
							title: "Wrong Selection...!",
							text: "Please Select Day Relevant From & To Time....!",
							icon: "error",
							button: "okay",
						});

						test=false;
					}

				}
				row=row+1;   
			})

		}

		return test;

	}

	function validate_time(id){
		if($('.eachdays'+id).prop('checked')==true){
			$('.eachdayfromtime'+id).val('');
			$('.eachdaytotime'+id).val('');

			var t_val=0;
			$(".err_check").each(function(){
				if ($(this).prop('checked')==true){ 
					t_val+=Number($(this).val());
					$('.eachdayfromtime'+id).val('');
					$('.eachdaytotime'+id).val('');
				}

				if(t_val==0){
					$("#time_submit").attr("disabled", true);
				}else{
					$("#time_submit").removeAttr("disabled");
				}
			})
		}else{ 
			$(".err_check").each(function(){
				if ($(this).prop('checked')==false){  
					t_val+=Number($(this).val());
					$('.eachdayfromtime'+id+'[value=""]').attr('selected', 'selected');
					$('.eachdayfromtime'+id).val("");
					select_from_time(id);
				}


			})
		}
	}

})(jQuery);