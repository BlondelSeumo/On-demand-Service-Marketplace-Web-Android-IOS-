<?php 
$contact_id = $this->uri->segment('2');
$this->db->select('*');
$this->db->from('contact_form_details');
$this->db->where('id',$contact_id);
$contact_details = $this->db->get()->row_array();
if(!empty($contact_details['created_at'])){
	$date=date('d-m-Y',strtotime($contact_details['created_at']));
}else{
	$date='-';
}
?>

<div class="page-wrapper">
	<div class="content container-fluid">
		<div class="row">
			<div class="col-xl-8 offset-xl-2">
			
				<!-- Page Header -->
				<div class="page-header">
					<div class="row">
						<div class="col-sm-12">
							<h3 class="page-title">Contact Details</h3>
						</div>
					</div>
				</div>
				<!-- /Page Header -->
				
				<div class="card">
					<div class="card-body">
                        <div class="form-group">
							<h5>Name</h5>
							<label><?=$contact_details['name']?></label>
							
						</div>
						
						<div class="form-group">
							<h5>Email ID</h5>
							<label><?=$contact_details['email']?></label>
							
						</div>
						
						<div class="form-group">
							<h5>Date</h5>
							<label><?=$date?></label>
							
						</div>
						
						<div class="form-group">
							<h5>Message</h5>
							<label><?=$contact_details['message']?></label>
							
						</div>
						<div class="mt-4">
								<a href="javascript:;" class="on-default remove-row btn btn-primary mr-2 reply_contact" id="Onremove_<?=$contact_details['id']?>" data-uname="<?=$contact_details['name']?>" data-mail="<?=$contact_details['email']?>" data-id="<?=$contact_details['id']?>"><i class="far fa-envelope"></i> Reply</a>
                            </div>
						
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="content container-fluid">
	
		<!-- Page Header -->
		<div class="page-header">
			<div class="row">
				<div class="col">
					<h3 class="page-title">Admin Replies</h3>
				</div>
		
			</div>
		</div>
		<!-- /Page Header -->
				
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
										<th>Email ID</th>
										<th>Date</th>
										<th>Reply</th>
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
						   
								echo'<tr>
								<td>'.$i++.'</td>
								<td>'.$rows['name'].'</td>
								<td>'.$rows['email'].'</td>
								<td>'.$date.'</td>
								<td>'.$rows['reply'].'</td>
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