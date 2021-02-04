<?php
	$providers = $this->db->get('providers')->result_array();
?>
<div class="page-wrapper">
	<div class="content container-fluid">

		<!-- Page Header -->
		<div class="page-header">
			<div class="row">
				<div class="col">
					<h3 class="page-title">Payments</h3>
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
		<form action="<?php echo base_url()?>admin/payments/payment_list" method="post" id="filter_inputs">
			<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    

			<div class="card filter-card">
				<div class="card-body pb-0">
					<div class="row filter-row">
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>Provider</label>
								<select class="form-control" name="provider_id">
									<option value="">Select Provider</option>
									<?php foreach ($providers as $pro) { ?>
									<option value="<?=$pro['id']?>"><?php echo $pro['name']?></option>
									<?php } ?>
								</select>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>Status</label>
								<select class="form-control" name="status">
									<option value="">Select Status</option>
									<option value="1">Pending</option>
									<option value="2">Inprogress</option>
									<option value="3">Completed (Provider)</option>
									<option value="6">Accepted</option>
									<option value="5">Rejected</option>
									<option value="7">Cancelled (Provider)</option>
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
							<table class="table table-hover table-center mb-0 payment_table">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Date</th>
                                        <th>Provider</th>
										<th>User</th>
                                        <th>Service</th>
                                        <th>Amount</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <?php
                                    if(!empty($list)) {
										$i=1;
										foreach ($list as $rows) {
                                        $amount_refund=''; 
									 	if(!empty($rows['reject_paid_token'])){
									 	if($rows['admin_reject_comment']=="This service amount favour for User"){
									 		$status="Amount refund to User";
									 	}else{
                                          $status="Amount refund to Provider";
									 	}
									 }

										$provider_name = $this->db->where('id',$rows['provider_id'])->get('providers')->row_array();
										$user_name = $this->db->where('id',$rows['user_id'])->get('users')->row_array();
										$service = $this->db->where('id',$rows['service_id'])->get('services')->row_array();
										$service = $this->db->where('id',$rows['service_id'])->get('services')->row_array();
										$admin_payment = $this->db->where('booking_id',$rows['id'])->get('admin_payment')->row_array();
										
										if($rows['status'] == 1) {
                                            $status = 'Pending';
										}
										elseif($rows['status'] == 2) {
                                            $status = 'Inprogress';
										}
										elseif($rows['status'] == 3) {
                                            $status = 'Complete Request to User';
										}
										elseif($rows['status'] == 5&&empty($rows['reject_paid_token'])) {
                                            $status = 'Rejected by User';
										}
										elseif($rows['status'] == 6) {
                                            $status = 'Payment Completed';
										}
										elseif($rows['status'] == 7&&empty($rows['reject_paid_token'])) {
                                            $status = 'Cancelled by Provider';
										}
										?>
                                        <tr>
											<td><?php echo $i++ ?></td> 
											<td><?=date('d-m-Y',strtotime($rows['service_date']));?></td>
											<td><?php echo $provider_name['name'] ?></td>
											<td><?php echo $user_name['name'] ?></td>
											<td><?php echo $service['service_title']?></td>
											<td>$<?php echo $rows['amount']?></td>
											<td><?php echo $status?></td>
										</tr>
                                    <?php } } else {
                                    ?>
									<tr>
										<td colspan="7">
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