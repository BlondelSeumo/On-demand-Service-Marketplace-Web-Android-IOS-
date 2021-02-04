<?php
   $module_details = $this->db->where('status',1)->get('admin_modules')->result_array();
?>
<div class="page-wrapper">
	<div class="content container-fluid">
		<div class="row">
			<div class="col-xl-8 offset-xl-2">
			
				<!-- Page Header -->
				<div class="page-header">
					<div class="row">
						<div class="col">
							<h3 class="page-title"><?=$title;?></h3>
						</div>
					</div>
				</div>
				<!-- /Page Header -->
				
				<div class="card">
					<div class="card-body">
						<form id="add_adminuser" method="get" autocomplete="off" enctype="multipart/form-data">
							<input type="hidden" name="id" value="<?=(!empty($user['user_id']))?$user['user_id']:''?>" id="user_id">
							<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>"/>
    
							<div class="form-group">
								<label>Name</label>
								<input class="form-control" type="text"  name="full_name" id="full_name" value="<?=(!empty($user['full_name']))?$user['full_name']:''?>">
							</div>
							<div class="form-group">
								<label>Username</label>
								<input class="form-control" type="text"  name="username" id="username" value="<?=(!empty($user['username']))?$user['username']:''?>">
							</div>
							<?php if(empty($user['password'])){ ?>
							<div class="form-group">
								<label>Password</label>
								<input class="form-control" type="password"  name="password" id="password" >
							</div>
							<?php }else{ ?>
							<input class="form-control" type="hidden"  name="password" id="password" value="<?php echo $user['password'];?>">
							<?php } ?>
							
							<div class="form-group">
								<label>Email ID</label>
								<input class="form-control" type="text"  name="email" id="email" value="<?=(!empty($user['email']))?$user['email']:''?>">
							</div>
							<div class="form-group">
								<label>Profile Image</label>
								<input class="form-control" type="file"  name="profile_img" id="profile_img">
							</div>
							<div class="form-group">
								<label>Set Access</label>
								<div class="example1">
									<div><input type="checkbox" name="selectall1" id="selectall1" class="all" value="1"> <label for="selectall1"><strong>Select all</strong></label></div>
									<?php foreach ($module_details as $module) {
										$checkcondition  = "";
										if(!empty($user['user_id'])){
											$access_result = $this->db->where('admin_id',$user['user_id'])->where('module_id',$module['id'])->where('access',1)->select('id')->get('admin_access')->result_array();
											if(!empty($access_result)){
												$checkcondition  = "checked='checked'";
											}
										}
									?>
									<div><input type="checkbox" <?php echo $checkcondition; ?> name="accesscheck[]" id="check<?php echo $module['id'];?>" value="<?php echo $module['id'];?>"> <label for="check1"><?php echo $module['module_name'];?></label></div>
									<?php } ?>									
								</div>
							</div>
							<div class="mt-4">
								<?php if($user_role==1){?>
								<button class="btn btn-primary " name="form_submit" value="submit" type="submit">Submit</button>
							<?php }?>

								<a href="<?php echo $base_url; ?>adminusers"  class="btn btn-link">Cancel</a>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

