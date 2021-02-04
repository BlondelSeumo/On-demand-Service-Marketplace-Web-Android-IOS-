<?php
	$category = $this->db->get('categories')->result_array();
	$subcategory = $this->db->get('subcategories')->result_array();
	$services = $this->db->get('services')->result_array();
?>
<div class="page-wrapper">
	<div class="content container-fluid">
	
		<!-- Page Header -->
		<div class="page-header">
			<div class="row">
				<div class="col">
					<h3 class="page-title">Services</h3>
				</div>
				<div class="col-auto text-right">
					<a class="btn btn-white filter-btn mr-3" href="javascript:void(0);" id="filter_search">
						<i class="fas fa-filter"></i>
					</a>
				</div>
			</div>
		</div>
		<!-- /Page Header -->
		
		<!-- Search Filter -->
		<form action="<?php echo base_url()?>admin/service/service_list" method="post" id="filter_inputs">
			<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    
			<div class="card filter-card">
				<div class="card-body pb-0">
					<div class="row filter-row">
					
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>Service Title</label>
								<select class="form-control" name="service_title">
									<option value="">Select Service</option>
									<?php foreach ($services as $pro) { ?>
									<option value="<?=$pro['id']?>"><?php echo $pro['service_title']?></option>
									<?php } ?>
								</select>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>Category</label>
								<select class="form-control" name="category">
									<option value="">Select Category</option>
									<?php foreach ($category as $cat) { ?>
									<option value="<?=$cat['id']?>"><?php echo $cat['category_name']?></option>
									<?php } ?>
								</select>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>Sub Category</label>
								<select class="form-control" name="subcategory">
									<option value="">Select subcategory</option>
									<?php foreach ($subcategory as $sub_category) { ?>
									<option value="<?=$sub_category['id']?>"><?php echo $sub_category['subcategory_name']?></option>
									<?php } ?>
								</select>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>From Date</label>
								<div class="cal-icon">
									<input class="form-control datetimepicker" type="text" name="from">
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>To Date</label>
								<div class="cal-icon">
									<input class="form-control datetimepicker" type="text" name="to">
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
		
		<div class="row">
			<div class="col-md-12">
				<div class="card">
					<div class="card-body">
						<div class="table-responsive">
							<table class="table table-hover table-center mb-0 service_table">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Service</th>
                                        <th>Location</th>
                                        <th>Category</th>
                                        <th>Subcategory</th>
                                        <th>Amount</th>
                                        <th>Date</th>
                                        
                                        <th>Status</th>
                                  
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <?php
                                    if(!empty($list)) {
									$i=1;

									foreach ($list as $rows) {
										
									$ser_image='';
									$service_img=$this->db->where('service_id',$rows['id'])->get('services_image')->row();
									if(!empty($service_img->service_image)){
										$ser_image=$service_img->service_image;
									}
									
									$avail=$this->service->check_booking_list($rows['id']);
									if($avail==0){
                                        $attr='';
                                        $tag='';
									}else{
                                        $attr='disabled';
                                        $tag='data-toggle="tooltip" title="Some One was Booked The Service So You Cannot Modified It ..!"';
									}
									if(!empty($rows['created_at'])){
                                        $date=date('d-m-Y',strtotime($rows['created_at']));
									}else{
                                        $date='-';
									}
									if($rows['status']==1) {
										$val='checked';
									}
									else {
										$val='';
									}
									if($user_role==1){
									echo'<tr>
                                        <td>'.$i++.'</td>
                                        <td><a href="'.base_url().'service-details/'.$rows['id'].'"><img class="rounded service-img mr-1" src="'.base_url().$ser_image.'" alt=""> '.$rows['service_title'].'</a></td>
                                        <td>'.$rows['service_location'].'</td>
                                        <td>'.$rows['category_name'].'</td>
                                        <td>'.$rows['subcategory_name'].'</td>
                                        <td>$'.$rows['service_amount'].'</td>
                                        <td>'.$date.'</td>
                                        <td>
											<div '.$tag.'>
												<div class="status-toggle">
													<input '.$attr.' id="status_'.$rows['id'].'" class="check change_Status_Service" data-id="'.$rows['id'].'" type="checkbox" '.$val.'>
													<label for="status_'.$rows['id'].'" class="checktoggle">checkbox</label>
												</div>
											</div>
                                        </td>
										<td> 
											<a href="'.base_url().'service-details/'.$rows['id'].'" class="btn btn-sm bg-info-light">
												<i class="far fa-eye mr-1"></i> View
											</a>
										</td>
									</tr>';
								}else{
										echo'<tr>
                                        <td>'.$i++.'</td>
                                        <td><a href="'.base_url().'service-details/'.$rows['id'].'"><img class="rounded service-img mr-1" src="'.base_url().$ser_image.'" alt=""> '.$rows['service_title'].'</a></td>
                                        <td>'.$rows['service_location'].'</td>
                                        <td>'.$rows['category_name'].'</td>
                                        <td>'.$rows['subcategory_name'].'</td>
                                        <td>$'.$rows['service_amount'].'</td>
                                        <td>'.$date.'</td>
                                        <td>
											<div '.$tag.'>
												<div class="status-toggle">
													<input '.$attr.' disabled id="status_'.$rows['id'].'" class="check change_Status_Service" data-id="'.$rows['id'].' type="checkbox" '.$val.'>
													<label for="status_'.$rows['id'].'" class="checktoggle">checkbox</label>
												</div>
											</div>
                                        </td>
										<td> 
											<a href="'.base_url().'service-details/'.$rows['id'].'" class="btn btn-sm bg-info-light">
												<i class="far fa-eye mr-1"></i> View
											</a>
										</td>
									</tr>';
								}
									} } else {
                                    ?>
									<tr>
										<td colspan="9">
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