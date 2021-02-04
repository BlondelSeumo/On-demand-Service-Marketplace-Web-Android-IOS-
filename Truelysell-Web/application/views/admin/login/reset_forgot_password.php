<?php
    $query = $this->db->query("select * from system_settings WHERE status = 1");
    $result = $query->result_array();
    $this->website_name = '';
    $website_logo_front ='assets/img/logo.png';
    
    if(!empty($result)) {
		foreach($result as $data){
			if($data['key'] == 'website_name'){
				$this->website_name = $data['value'];
			}
			if($data['key'] == 'logo_front'){
				$website_logo_front =  $data['value'];
			}
		}
    }
?>

<div class="login-page">
	<div class="login-body container">
		<div class="loginbox">
			<div class="login-right-wrap">
				<div class="account-header">
					<div class="account-logo text-center mb-4">
						<a href="<?php echo $base_url."admin"; ?>">
							<img src="<?php echo $base_url; ?>assets/img/logo-icon.png" alt="" class="img-fluid">
						</a>
					</div>
				</div>
				<div class="login-header">
					<h3>Reset Password <span><?php echo $this->website_name;?></span></h3>
				</div>

				<?php if($this->session->flashdata('error_message')) {  ?>
				<div class="alert alert-danger text-center " id="flash_error_message"><?php echo $this->session->flashdata('error_message');?></div>
				<?php $this->session->unset_userdata('error_message');
				} ?>
				<?php if($this->session->flashdata('success_message')) {  ?>
				<div class="alert alert-success text-center" id="flash_succ_message"><?php echo $this->session->flashdata('success_message');?></div>
				<?php $this->session->unset_userdata('success_message');
				} ?>
				
				
				<?php
				if(!empty($chk_data))
				{
				?>
				<form id="resetpwdadmin" action="<?php echo $base_url; ?>/forgot_password" method="POST">
                   <input type="hidden" name="<?php echo $csrf['name']; ?>" value="<?php echo $csrf['hash']; ?>">
                   <input type="hidden" name="user_id" value="<?php echo $euser_id; ?>">
					<div class="form-group">
						<label class="control-label">New Password</label>
						<input class="form-control" type="password" name="new_password" id="new_password" placeholder="New Password">
					</div>
					
					<div class="form-group">
						<label class="control-label">Confirm Password</label>
						<input class="form-control" type="password" name="confirm_password" id="confirm_password" placeholder="Confirm Password">
					</div>
					
					<span id="err_respwd" ></span><br>
					<div class="text-center">
						<button class="btn btn-primary btn-block account-btn" id="resetpwdSubmit" type="submit">Submit</button>
					</div>
				</form>
				
				<?php }else{?>
				<div class="form-group">
						<label class="control-label" style="color:red;">Reset Link mismatch , Check your mail.</label>
					</div>
				<?php } ?>
			</div>
		</div>
	</div>
</div>