<div class="page-wrapper">
	<div class="content container-fluid">
		<div class="row">
			<div class="col-xl-8 offset-xl-2">

				<!-- Page Header -->
				<div class="page-header">
					<div class="row">
						<div class="col-sm-12">
							<h3 class="page-title">Profile</h3>
						</div>
					</div>
				</div>
				<!-- /Page Header -->
	
				
				
				<div class="card">
					<div class="card-body profile-menu">
						<ul class="nav nav-tabs nav-tabs-solid" role="tablist">
							<li class="nav-item home_tab"> <a class="nav-link active" data-toggle="tab" href="#profile" role="tab" aria-selected="false"><span class="hidden-sm-up"><i class="ti-home"></i></span> <span class="hidden-xs-down">Profile Settings</span></a> </li>
							<li class="nav-item home_add"> <a class="nav-link" data-toggle="tab" href="#pass" role="tab" aria-selected="false"><span class="hidden-sm-up"><i class="ti-user"></i></span> <span class="hidden-xs-down">Change password</span></a> </li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane fade show active" id="profile" role="tabpanel">
								<form id="profile_settings_form" class="settings-form" action="javascript:void(0);" method="POST" enctype="multipart/form-data">
									<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    
									<div class="form-group">
										<label>Username</label>
										<input type="text" class="form-control" value="<?php echo $details['username']; ?>" disabled>
									</div>
									
									<div class="form-group">
										<label>Email ID</label>
										<input type="text" class="form-control" id="adminmail" name="email" value="<?php echo $details['email']; ?>" >
										<span id="email_error" class="text-danger" ></span>
									</div>
									<div class="form-group">
										<label>Profile Image</label>
										<div class="media align-items-center">
											<div class="media-left">
												<?php
												if (!empty($details['profile_img'])) { ?>
													<img class="rounded-circle" src="<?php echo base_url().$details['profile_img'];?>" width="100" height="100" class="profile-img avatar-view-img">
												<?php  } else {?>
													<img class="rounded-circle" src="<?php echo base_url('assets/img/user.jpg');?>" width="100" height="100" class="profile-img avatar-view-img">
													
												<?php }
												?>
											
											</div>
											<div class="media-body">
												<div class="uploader"><button class="btn btn-secondary btn-sm ml-2 avatar-view-btn">Change profile picture</button>
												<input type="hidden" id="crop_prof_img" name="profile_img">
												</div>
												<span id="image_error" class="text-danger" ></span>
											</div>
										</div>
									</div>
									<div class="mt-4 save-form">
										<?php if($user_role==1){?>
										<button name="save_profile_change" id="save_profile_change"  class="btn btn-primary save-btn" type="button">Save</button>
										<?php }?>
									</div>
								</form>
							</div>
							
							<div class="tab-pane fade" id="pass" role="tabpanel">
								<form id="change_password_form" class="settings-form" action="<?php echo $base_url; ?>admin/profile/change_password" method="POST" enctype="multipart/form-data">
									<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    
									<div class="form-group">
										<label>Current Password</label>
										<input type="password" class="form-control" id="current_password" name="current_password" value="">
										
									</div>
									<div class="form-group">
										<label>New Password</label>
										<input type="password" id="new_password" name="new_password" class="form-control">
									
									</div>
									<div class="form-group">
										<label>Repeat Password</label>
										<input type="password" id="confirm_password" name="confirm_password" class="form-control" value="">
										
									</div>
									<div class="mt-4 save-form">
										<?php if($user_role==1){?>
										<button name="save_profile_change" id="cform_submit" class="btn save-btn btn-primary" type="submit">Save</button>
										<?php }?>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="avatar-modal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Profile Image</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">

				<?php $curprofile_img = (!empty($profile['profile_img']))?$profile['profile_img']:''; ?>
				<form class="avatar-form" action="<?=base_url('admin/profile/crop_profile_img/'.$curprofile_img)?>" enctype="multipart/form-data" method="post">
					<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    
					<div class="avatar-body">
						<!-- Upload image and data -->
						<div class="avatar-upload">
							<input class="avatar-src" name="avatar_src" type="hidden">
							<input class="avatar-data" name="avatar_data" type="hidden">
							<label for="avatarInput">Select Image</label>
							<input class="avatar-input" id="avatarInput" name="avatar_file" type="file">
							<span id="image_upload_error" class="error" ></span>
						</div>
						<!-- Crop and preview -->
						<div class="row">
							<div class="col-md-12">
								<div class="avatar-wrapper"></div>
							</div>
						</div>
						<div class="mt-4 text-center">
							<button class="btn btn-primary avatar-save upload_images" id="upload_images" type="submit" >Yes, Save Changes</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>