<?php 
$get_details = $this->db->where('id',$this->session->userdata('id'))->get('providers')->row_array();
?>
<div class="content">
	<div class="container">
		<div class="row">
		 	<?php
			if(!empty($_GET['tbs'])){
				$val=$_GET['tbs'];
			}else{
				$val=1;
			}
			?>
			<input type="hidden" name="tab_ctrl" id="tab_ctrl" value="<?=$val;?>">
			<?php $this->load->view('user/home/user_sidemenu');?>
		 
            <div class="col-xl-9 col-md-8">
				<div class="tab-content pt-0">
					<div class="tab-pane show active" id="user_profile_settings" >
						<div class="widget">
							<h4 class="widget-title">Change Password</h4>
							<form id="update_user_pwd" action="<?php echo base_url()?>user/dashboard/update_provider_password" method="POST" onsubmit="return updatepassword();" enctype="multipart/form-data">
								<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
   
								<div class="row">
									<div class="form-group col-xl-12">
										<label class="mr-sm-2">Current Password</label>
										<input class="form-control" onBlur="checkcurpwd();" id="current_password" type="password"   >
										<span id="errchkcp"></span>
									</div>
									<div class="form-group col-xl-12">
										<label class="mr-sm-2">New Password</label>
										<input class="form-control" id="new_password" type="password"   >
									</div>
									<div class="form-group col-xl-12">
										<label class="mr-sm-2">Confirm Password</label>
										<input class="form-control" id="confirm_password" type="password" name="confirm_password"  >
										
										<span id="errchk"></span>
									</div>
									<div class="form-group col-xl-12">
										<button name="form_submit" id="form_submit" class="btn btn-primary pl-5 pr-5" type="submit">Change</button>
									</div>
								
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
var base_url="<?php echo base_url()?>";
function checkcurpwd()
{
	
	var current_password=$("#current_password").val();
	$.ajax({
		type: "POST",
		url: base_url+"user/dashboard/checkproviderpwd",
		data:{current_password:current_password,csrf_token_name:"<?php echo $this->security->get_csrf_hash(); ?>"}, 
		dataType:'json',
		beforeSend :function(){
			//$('#city_id').find("option:eq(0)").html("Please wait..");
		}, 
		success: function (data) {
			
			//alert(data);
			if(data==0)
			{
				$("#form_submit").prop("disabled",true);
				$("#errchkcp").html('Current Password is Not Valid').css("color","red");
			}
			else
			{
				$("#form_submit").prop("disabled",false);
				$("#errchkcp").html('').css("color","");
			}
				
		}
	});
}

function updatepassword()
{
	
	var new_password=$("#new_password").val();
	var confirm_password=$("#confirm_password").val();
	var current_password=$("#current_password").val();
	if(current_password.length==0)
	{
		$("#errchk").html('Type your current password').css("color","red");return false;
	}
	if(new_password.length==0)
	{
		$("#errchk").html('New password is required').css("color","red");return false;
	}
	if(confirm_password.length==0)
	{
		$("#errchk").html('New password is required').css("color","red");return false;
	}
	if(new_password == confirm_password)
	{
	$.ajax({
		type: "POST",
		url: base_url+"user/dashboard/update_provider_password",
		data:{confirm_password:confirm_password,csrf_token_name:"<?php echo $this->security->get_csrf_hash(); ?>"}, 
		dataType:'json',
		beforeSend :function(){
			//$('#city_id').find("option:eq(0)").html("Please wait..");
		}, 
		success: function (data) {
			
			//alert(data);
			if(data==1)
			{
				//$("#form_submit").prop("disabled",false);
				$("#errchkcp").html('').css("color","");
				$("#errchk").html('Password Changed Successfully...!').css("color","green");return true;
				window.location.reload();
				//$("#errchkcp").html('Current Password is Not Valid').css("color","red");
			}
			else
			{
				$("#errchk").html('Something Went wrong try again...!').css("color","red");return false;
			}
				
		}
	});
	}
	else
	{
		$("#errchk").html('Password Mismatch').css("color","red");return false;
		
	}
}


</script>