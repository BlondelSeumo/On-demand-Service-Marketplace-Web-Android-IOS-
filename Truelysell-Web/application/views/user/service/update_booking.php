<?php 
$booking_details  = $this->service->get_booking_details($this->uri->segment('2')); 
$re = strtotime($booking_details['from_time']);
$re1 = strtotime($booking_details['to_time']);
$st_time = date('g:i A',($re)); 
$end_time = date('g:i A',($re1)); 
$service_details  = $this->service->get_service_id($booking_details['service_id']);
?>

<div class="content">
	<div class="container">
		<div class="row justify-content-center">
            <div class="col-lg-10">
				<div class="section-header text-center">
					<h2>Edit Booking</h2>
				</div>
				<form method="post" enctype="multipart/form-data" autocomplete="off" id="book_service" action="<?php echo base_url()?>update_bookingstatus">
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group">
								<label>Service Title</label>
								<input class="form-control" type="text" name="service_title" id="service_title" value="<?php echo $service_details['service_title']?>" readonly="">
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group">
								<label>Service Date <span class="text-center">*</span></label>
								<input class="form-control" type="date" name="booking_date" id="booking_date" value="<?php echo $booking_details['service_date']?>" readonly>
								<input type="hidden" name="provider_id" id="provider_id" value="<?php echo $booking_details['user_id']?>">
								<input type="hidden" name="booking_id" id="booking_id" value="<?php echo $booking_details['id']?>">
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group">
								<label>Service amount</label>
								<input class="form-control" type="text" name="service_amount" id="service_amount" value="<?php echo $booking_details['amount']?>" readonly="">
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group">
								<label>Service Location <span class="text-center">*</span></label>
								<input class="form-control" type="text" name="service_location" id="service_location" value="<?php echo $booking_details['location']?>" readonly>
								<input type="hidden" name="service_latitude" id="service_latitude">
								<input type="hidden" name="service_longitude" id="service_longitude">
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group">
								<label>From time <span class="text-center">*</span></label>
								<input class="form-control" type="text"  name="from_time" id="from_time" value="<?php echo $st_time?>" readonly>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group">
								<label>To Time <span class="text-center">*</span></label>
								<input class="form-control" type="text"  id="to_time" value="<?php echo $end_time?>" readonly>
							</div>
						</div>

						<?php
						
						if ($booking_details['status']==1) { ?>
						<div class="col-lg-6">
							<div class="form-group">
								<label>Approve/Reject</label>
								<select class="form-control" type="text" name="status" id="status">
									<option value=""> - select -</option>
									<option value="2">Accept</option>
									<option value="7">Reject</option>
								</select>
							</div>
						</div>
						<?php } 

						if ($booking_details['status']==2 ) { ?>
						<div class="col-lg-6">
							<div class="form-group">
								<label>Complete Request</label>
								<select class="form-control" type="text" name="status" id="status">
									<option value=""> - select -</option>
									<option value="3">Complete</option>
									<option value="7">Cancel</option>
								</select>
							</div>
						</div>
						<?php } 

						if ($booking_details['status']==3) { ?>
						<div class="col-lg-6">
							<div class="form-group">
								<label>Complete Request</label>
								<select class="form-control" type="text" name="status" id="status">
									<option value=""> - select -</option>
									<option value="4">Complete Accept</option>
									<option value="5">Complete Reject</option>
								</select>
							</div>
						</div>
					   <?php } ?>
					 <?php  if ($booking_details['status']==4) { ?>
						<div class="col-lg-6">
							<div class="form-group">
								<label>Complete Request</label>
								<select class="form-control" type="text" name="status" id="status">
									<option value=""> - select -</option>
									<option value="6">Complete Accept</option>
									<option value="7">Complete Reject</option>
								</select>
							</div>
						</div>
					   <?php } ?>
					</div>
					<div class="submit-section">
						<button class="btn btn-primary submit-btn" type="submit" name="form_submit" value="submit">Submit</button>
					</div>
				</form>
            </div>
		</div>
	</div>
</div>