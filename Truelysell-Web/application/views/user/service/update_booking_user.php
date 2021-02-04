<?php 
$booking_details  = $this->service->get_booking_details($this->uri->segment('2')); 

$re = strtotime($booking_details['from_time']);
$re1   = strtotime($booking_details['to_time']);
$st_time = date('g:i A',($re)); 
$end_time = date('g:i A',($re1)); 

$service_details  = $this->service->get_service_id($booking_details['service_id']);

?>

<div class="content">
	<div class="container">
		<div class="row justify-content-center">
            <div class="col-lg-10">
				<div class="section-header text-center">
					<h2>Approve/Reject Booking</h2>
				</div>
				
				<form method="post" enctype="multipart/form-data" autocomplete="off" id="book_service" action="<?php echo base_url()?>update_bookingstatus">
					<div class="row">
						<div class="col-lg-6">
							<div class="form-group">
								<label>Service Title</label>
								<input type="text" name="service_title" id="service_title" value="<?php echo $service_details['service_title']?>" readonly="">
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group">
								<label>Service Date <span class="text-center">*</span></label>
								<input type="date" name="booking_date" id="booking_date" value="<?php echo $booking_details['service_date']?>" readonly>
								<input type="hidden" name="provider_id" id="provider_id" value="<?php echo $booking_details['user_id']?>">
								<input type="hidden" name="booking_id" id="booking_id" value="<?php echo $booking_details['id']?>">
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group">
								<label>Service amount</label>
								<input type="text" name="service_amount" id="service_amount" value="<?php echo $booking_details['amount']?>" readonly="">
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group">
								<label>Service Location <span class="text-center">*</span></label>
								<input type="text" name="service_location" id="service_location" value="<?php echo $booking_details['location']?>" readonly>
								<input type="hidden" name="service_latitude" id="service_latitude">
								<input type="hidden" name="service_longitude" id="service_longitude">
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group">
								<label>From time <span class="text-center">*</span></label>
								<input type="text"  name="from_time" id="from_time" value="<?php echo $st_time?>" readonly>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group">
								<label>To Time <span class="text-center">*</span></label>
								<input type="text"  id="to_time" value="<?php echo $end_time?>" readonly>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="form-group">
								<label>Approve/Reject</label>
								<select class="form-control" type="text" name="status" id="status">
									<option <?php if($booking_details['status'] == '1') echo 'selected';?> value="1">Pending</option>
									<option <?php if ($booking_details['status'] == '2' ) echo 'selected' ; ?> value="2">Inprogress</option>
									<option <?php if ($booking_details['status'] == '3' ) echo 'selected' ; ?> value="3">Completed</option>
									<option <?php if ($booking_details['status'] == '7' ) echo 'selected' ; ?> value="7">Cancelled</option>
								</select>
							</div>
						</div>
						<div class="submit-section">
							<button type="submit" name="form_submit" value="submit">Submit</button>
						</div>
					</div>
				</form>
            </div>
		</div>
	</div>
</div>