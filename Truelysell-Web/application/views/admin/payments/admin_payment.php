<?php $provider = $this->db->where('id',$this->uri->segment('2'))->get('book_service')->row_array(); 
	$provider_details = $this->db->where('id',$provider['provider_id'])->get('providers')->row_array(); 
	$booking_id = $this->uri->segment('2');
?>
<div class="page-wrapper">
	<div class="content container-fluid">
		<div class="row">
			<div class="col-xl-8 offset-xl-2">
			
				<!-- Page Header -->
				<div class="page-header">
					<div class="row">
						<div class="col-sm-12">
							<h3 class="page-title">Admin Payment</h3>
						</div>
					</div>
				</div>
				<!-- /Page Header -->
				
				<div class="card">
					<div class="card-body">
						<form method="post" autocomplete="off" enctype="multipart/form-data" id="admin_payment">
							<div class="row">
								<div class="form-group col-xl-6 col-xl-6">
									<label>Account holder name</label>
									<input class="form-control" type="text"  name="account_holder_name" id="account_holder_name" value="<?php echo $provider_details['account_holder_name']?>" readonly>
									<input class="form-control" type="hidden"  name="booking_id" id="booking_id" value="<?php echo $booking_id?>">
								</div>
								<div class="form-group col-xl-6">
									<label>Account Number</label>
								   <input class="form-control" type="text"  name="account_number" id="account_number" value="<?php echo $provider_details['account_number']?>" readonly>
								</div>
								<div class="form-group col-xl-6">
									<label>IBAN</label>
									<input class="form-control" type="text"  name="account_iban" id="account_iban" value="<?php echo $provider_details['account_iban']?>" readonly>
								</div>
								<div class="form-group col-xl-6">
									<label>Bank Name</label>
									<input class="form-control" type="text"  name="bank_name" id="bank_name" value="<?php echo $provider_details['bank_name']?>" readonly>
								</div>
								 <div class="form-group col-xl-6">
									<label>Bank Address</label>
									<input class="form-control" type="text"  name="bank_address" id="bank_address" value="<?php echo $provider_details['bank_address']?>" readonly>
								</div>
								 <div class="form-group col-xl-6">
									<label>Sort Code</label>
									<input class="form-control" type="text"  name="sort_code" id="sort_code" value="<?php echo $provider_details['sort_code']?>" readonly>
								</div>
								<div class="form-group col-xl-6">
									<label>SWIFT Code</label>
									<input class="form-control" type="text"  name="routing_number" id="routing_number" value="<?php echo $provider_details['routing_number']?>" readonly>
								</div>
								<div class="form-group col-xl-6">
									<label>IFSC Code</label>
									<input class="form-control" type="text"  name="account_ifsc" id="account_ifsc" value="<?php echo $provider_details['account_ifsc']?>" readonly>
								</div>
								<div class="form-group col-xl-6">
									<label>Transaction ID</label>
									<input class="form-control" type="text"  name="transaction_id" id="transaction_id">
									<p class="error_transaction error"></p>
								</div>
								<div class="mt-4 col-xl-12">
									<button class="btn btn-primary" type="Submit">Submit</button>
									<a href="<?php echo $base_url; ?>payment_list" class="btn btn-link">Cancel</a>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>