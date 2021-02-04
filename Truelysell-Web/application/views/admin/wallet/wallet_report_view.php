<?php
$category = $this->db->get('categories')->result_array();
$subcategory = $this->db->get('subcategories')->result_array();
$services = $this->db->get('services')->result_array();
$user_list = $this->db->select('id,name,type,token')->get('users')->result_array();
$provider_list = $this->db->select('id,name,type,token')->get('providers')->result_array();
$all_member=array_merge($user_list,$provider_list);
?>
<div class="page-wrapper">
	<div class="content container-fluid">
	
		<!-- Page Header -->
		<div class="page-header">
			<div class="row">
				<div class="col">
					<h3 class="page-title">Wallet</h3>
				</div>
				<div class="col-auto text-right">
					<a class="btn btn-white filter-btn mr-2" href="javascript: void(0);" id="filter_search">
						<i class="fas fa-filter"></i>
					</a>
				</div>

			</div>
		</div>
		<!-- /Page Header -->
		  
		<!-- Search Filter -->
		<form action="<?php echo base_url()?>admin/wallet" method="post" id="filter_inputs">
			<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    
			<div class="card filter-card">
				<div class="card-body pb-0">
					<div class="row filter-row">
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>User</label>
								<select class="form-control" id="user_id_f" name="token">
									<option value="">Select User</option>
									<?php foreach ($all_member as $pro) 
									{
									if($pro['type']==1){
										$type_name='Provider';
									}else{
										$type_name='User';
									}
									if($filter['token_f']==$pro['token']){
										$select='selected';
									}else{
										$select='';
									}
									?>
									<option <?=$select;?> value="<?=$pro['token']?>"><?php echo ucfirst($pro['name']).'-'.$type_name;?></option>
									<?php } ?>
								</select>
							</div>
						</div>

						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>From Date</label>
								<?php
								if(!empty($filter['service_from'])){
									$fr_date=$filter['service_from'];
								}else{
									$fr_date='';
								}
								if(!empty($filter['service_to'])){
									$to_date=$filter['service_to'];
								}else{
									$to_date='';
								}
								?>
								<div class="cal-icon">
									<input class="form-control datetimepicker" type="text" id="from_f" name="from" value="<?=$fr_date;?>">
								</div>
							</div>
						</div>
						
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>To Date</label>
								<div class="cal-icon">
									<input class="form-control datetimepicker" type="text" id="to_f" name="to" value="<?=$to_date;?>">
								</div>
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

		<ul class="nav nav-tabs menu-tabs">
			<li class="nav-item active">
				<a class="nav-link" href="<?php echo base_url().'admin/wallet'; ?>">Wallet Report</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="<?php echo base_url().'admin/wallet-history'; ?>">Wallet History</a>
			</li>
		</ul>
		
		<div class="row">
			<div class="col-md-12">
				<div class="card">
					<div class="card-body">
						<div class="table-responsive">
                              <table class="table table-hover table-center mb-0 service_table" >
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Date</th>
                                        <th>Name</th>
                                        <th>Mobile</th>
                                        <th>Wallet Amt</th>
                                       <th>Role</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <?php
									if(!empty($list)) {
                                    $i=1;
									
                                    foreach ($list as $rows) {
									if(!empty($rows['date'])){
										$date=date('d M Y');
									}else{
										$date='-';
									}
									if($rows['role']==1){
										$role=' Provider ';
										$color='success';
									}else{
										$role='User';
										$color='primary';
									}
									echo '<tr>
										<td>'.$i++.'</td>
										<td>'.$date.'</td>
										<td>
											<h2 class="table-avatar">
												<a href="#" class="avatar avatar-sm mr-2">
													<img class="avatar-img rounded-circle" alt="" src="'.base_url().$rows['user_image'].'">
												</a>
												<a href="javascript:void(0);">'.str_replace('-', ' ', $rows['user_name']).'</a>
											</h2>
										</td>
										<td>'.$rows['user_mobile'].'</td>
										<td>'.currency_conversion($rows['currency_code']).$rows['wallet_amt'].'</td>
										<td><label class="badge badge-'.$color.'">'.ucfirst($role).'</lable></td>
									</tr>';
									} 
									} else {
									?>
									<tr>
										<td colspan="6">
											<div class="text-center text-muted">No records found</div>
										</td>
									</tr>
									<?php } ?>
                                </tbody>
							</table>
						</div> 
					</div> 
				</div>
			</div>
		</div>
	</div>
</div>