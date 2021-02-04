<div class="page-wrapper">
	<div class="content container-fluid">
	
		<!-- Page Header -->
		<div class="page-header">
			<div class="row">
				<div class="col">
					<h3 class="page-title">Contact Details</h3>
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
		<form action="<?php echo base_url()?>admin/contact" method="post" id="filter_inputs">
			<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    
			<div class="card filter-card">
				<div class="card-body pb-0">
					<div class="row filter-row">
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>Name</label>
								<input type="text" class="form-control" name="name" />
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>Email</label>								
								<input type="text" class="form-control" name="email" />
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
							<table class="table table-hover table-center mb-0 categories_table" >
								<thead>
									<tr>
										<th>#</th>
										<th>Name</th>
										<th>Eamil</th>
										<th>Message</th>
										<th>Date</th>										
										<th>Action</th>									
									  
									</tr>
								</thead>
								<tbody>
								<?php
								$i=1;
								if(!empty($list)){
								foreach ($list as $rows) {
								
								if(!empty($rows['created_at'])){
									$date=date('d-m-Y',strtotime($rows['created_at']));
								}else{
									$date='-';
								}
								
								// strip tags to avoid breaking any html
								$message= strip_tags($rows['message']);
								if (strlen($message) > 50) {

									// truncate string
									$stringCut = substr($message, 0, 50);
									$endPoint = strrpos($stringCut, ' ');

									//if the string doesn't contain any space then it will cut without word basis.
									$message = $endPoint? substr($stringCut, 0, $endPoint) : substr($stringCut, 0);
									$message .= '...';
								}
								echo'<tr>
								<td>'.$i++.'</td>
								<td>'.$rows['name'].'</td>
								<td>'.$rows['email'].'</td>
								<td>'.$message.'</td>
								<td>'.$date.'</td>
								<td> 
									<a href="'.base_url().'contact-details/'.$rows['id'].'" class="btn btn-sm bg-info-light">
										<i class="far fa-eye mr-1"></i> View
									</a>
								</td>
								</tr>';
							
								}
								}
								else {
								echo '<tr><td colspan="4"><div class="text-center text-muted">No records found</div></td></tr>';
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