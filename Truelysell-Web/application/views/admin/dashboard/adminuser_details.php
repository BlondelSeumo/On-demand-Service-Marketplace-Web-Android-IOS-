<?php 
$user_id = $this->uri->segment('2');
$user_details = $this->db->where('user_id',$user_id)->get('administrators')->row_array();

$this->db->select('tab_2.module_name')->from('admin_access tab_1');
$access_result_data=$this->db->where('tab_1.admin_id',$user_id)->where('tab_1.access',1)->join('admin_modules tab_2','tab_2.id=tab_1.module_id','INNER')->get()->result_array();

$access_result_data_array = array_column($access_result_data, 'module_name');
$access_result_data_details = implode(", ",$access_result_data_array);

?>
<div class="page-wrapper">
	<div class="content container-fluid">
	
		<!-- Page Header -->
		<div class="page-header">
			<div class="row">
				<div class="col">
					<h3 class="page-title">AdminUsers Details</h3>
				</div>
			</div>
		</div>
		<!-- /Page Header -->
		
		<div class="row">
			<div class="col-lg-4">
				<div class="card">
					<div class="card-body text-center">
						<?php if($user_details['profile_img'] != '')
						{?>
						<img class="rounded-circle img-fluid mb-3" alt="User Image" src="<?php echo $base_url.$user_details['profile_img'] ?>">
						<?php } else { ?>
						<img class="rounded-circle img-fluid mb-3" alt="User Image" src="<?php echo $base_url?>assets/img/user.jpg">
						<?php } ?>
						<h5 class="card-title text-center">
							<span>Account Status</span>
						</h5>						
					</div>
				</div>
			</div>
			
			<div class="col-lg-8">
				<div class="card">
					<div class="card-body">
						<h5 class="card-title d-flex justify-content-between">
							<span>Personal Details</span>
						</h5>
						<div class="row">
							<p class="col-sm-3 text-muted text-sm-right mb-0 mb-sm-3">Name</p>
							<p class="col-sm-9"><?php echo $user_details['full_name']?></p>
						</div>
						<div class="row">
							<p class="col-sm-3 text-muted text-sm-right mb-0 mb-sm-3">Username</p>
							<p class="col-sm-9"><?php echo $user_details['username']?></p>
						</div>
						<div class="row">
							<p class="col-sm-3 text-muted text-sm-right mb-0 mb-sm-3">Access Modules</p>
							<p class="col-sm-9"><?php echo $access_result_data_details?></p>
						</div>
					</div>
				</div>                      
			</div>
		</div>
	</div>
</div>