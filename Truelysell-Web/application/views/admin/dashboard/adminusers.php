<?php
   $user_details = $this->db->get('administrators')->result_array();
  // echo "<pre>";print_r($user_details);exit;
?>
<div class="page-wrapper">
	<div class="content container-fluid">
	
		<!-- Page Header -->
		<div class="page-header">
			<div class="row">
				<div class="col">
					<h3 class="page-title">Admin Users</h3>
				</div>
				<div class="col-auto text-right">
					<a class="btn btn-white filter-btn mr-3" href="javascript:void(0);" id="filter_search">
						<i class="fas fa-filter"></i>
					</a>
					<a href="<?=base_url().'adminusers/edit/';?>"><button class="btn btn-primary">Add</button></a>
					<!--<a href="<?=base_url().'admin/dashboard/adminusers_export';?>"><button class="btn btn-primary">Export</button></a>-->
				</div>
			</div>
		</div>
		<!-- /Page Header -->
		
		<!-- Search Filter -->
		<form action="<?php echo base_url()?>admin/dashboard/adminusers_list" method="post" id="filter_inputs">
			<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    
			<div class="card filter-card">
				<div class="card-body pb-0">
					<div class="row filter-row">
					
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>User Name</label>
								<select class="form-control" name="username">
									<option value="">Select user name</option>
									<?php foreach ($user_details as $user) { ?>
									<option value="<?=$user['username']?>"><?php echo $user['username']?></option>
									<?php } ?>
								</select>
							</div>
						</div>						
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<button class="btn btn-primary btn-block" name="form_submit" value="submit" type="submit">Submit</button>
							</div>
						</div>
					</div>

				</div>
			</div>
		</form>
		<!-- /Search Filter -->
		
		<div class="row">
			<div class="col-md-12">
				<div class="card">
					<div class="card-body">
                        <div class="table-responsive">
                            <table class="custom-table table table-hover table-center mb-0 w-100" id="adminusers_table">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Full Name</th>
                                        <th>User Name</th>
										<th>Email ID</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
									<?php
									if(!empty($lists)) {
										$i=1;
										foreach ($lists as $rows) {
										$profile_img = $rows['profile_img'];
										if(empty($profile_img)){
											$profile_img ='assets/img/user.jpg';
										}
										 $base_url=base_url()."adminusers/edit/".$rows['user_id'];

										$action="<a href='".$base_url."'' class='btn btn-sm bg-success-light mr-2'>
      <i class='far fa-edit mr-1'></i> Edit
      </a>
      <a class='btn btn-sm bg-info-light delete_show' data-id='".$rows['user_id']."'><i class='fa fa-trash' ></i> Delete</a>";
					echo'<tr>
					<td>'.$i++.'</td>
					<td>
					<h2 class="table-avatar">
					<a href="#" class="avatar avatar-sm mr-2">
					<img class="avatar-img rounded-circle" alt="" src="'.base_url().$profile_img.'">
						</a>
					<a href="'.base_url().'adminuser-details/'.$rows['user_id'].'">'.str_replace('-', ' ', $rows['full_name']).'</a>
											</h2>
					</td>
			<td>'.$rows['username'].'</td>
			<td>'.$rows['email'].'</td>
				<td>'.$action.'</td>
										</tr>';
										}
                                    }
                                    else {
										echo '<tr><td colspan="6"><div class="text-center text-muted">No records found</div></td></tr>';
                                    }
									?>
                                </tbody>
                            </table>
                        </div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal" id="delete_modal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5>Delete Confirmation</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <p>Are you confirm to Delete.</p>
      </div>
      <div class="modal-footer">
        <button type="button" id="confirm_btn_admin" data-id="" class="btn btn-primary">Confirm</button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
      </div>
    </div>
  </div>
</div>
