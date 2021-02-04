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
						<form id="add_user" method="post" autocomplete="off" enctype="multipart/form-data">
							<input type="hidden" name="id" value="<?=(!empty($user['id']))?$user['id']:''?>" id="user_id">
							<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>"/>
    
							<div class="form-group">
								<label>Name</label>
								<input class="form-control" type="text"  name="name" id="name" value="<?=(!empty($user['name']))?$user['name']:''?>">
							</div>
							<div class="form-group">
								<label>Country Code</label>
								<select class="form-control" name="country_code" id="country_code">
									<option value="">select Country Code</option>
									<?php foreach ($country as $key => $value) {?>
									<option value="<?=$value['country_id'];?>" <?php echo (!empty($user['country_code'])&&$user['country_code']==$value['country_id'])?'selected':''?>> +<?=$value['country_id'];?></option>
								<?php } ?>
								</select>
							</div>
							<div class="form-group">
								<label>Mobile No</label>
								<input class="form-control no_only" type="text"  name="mobileno" id="mobileno" value="<?=(!empty($user['mobileno']))?$user['mobileno']:''?>">
							</div>
							<div class="form-group">
								<label>Email</label>
								<input class="form-control" type="text"  name="email" id="email" value="<?=(!empty($user['email']))?$user['email']:''?>">
							</div>
							<div class="form-group">
								<label>DOB</label>
								<input class="form-control datepicker dob" type="text"  name="dob" id="dob" value="<?=(!empty($user['dob']))?date('d-m-Y',strtotime($user['dob'])):''?>" >
							</div>

							<div class="form-group">
								<label>Profile Image</label>
								<input class="form-control" type="file"  name="profile_img" id="profile_img">
							</div>
							<div class="form-group">
								<label>Status</label>
								 <label><input type="radio" name="status" value="1" <?=(!empty($user['status'])&&$user['status']==1)?'checked':'';?>>Active</label>
								 <label><input type="radio" name="status" <?=(!empty($user['status'])&&$user['status']==2)?'checked':'';?> value="2">InActive</label>
							</div>
							<div class="mt-4">
								<?php if($user_role==1){?>
								<button class="btn btn-primary " name="form_submit" value="submit" type="submit">Submit</button>
							<?php }?>

								<a href="<?php echo $base_url; ?>categories"  class="btn btn-link">Cancel</a>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

