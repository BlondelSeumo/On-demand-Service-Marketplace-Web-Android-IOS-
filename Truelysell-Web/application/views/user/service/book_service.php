<?php
$service_details = $this->service->get_service_id($this->uri->segment('2'));

$user_currency_code = '';
$userId = $this->session->userdata('id');
If (!empty($userId)) {
    $service_amount = $service_details['service_amount'];
    $get_currency = get_currency();
    $user_currency = get_user_currency();
    $user_currency_code = $user_currency['user_currency_code'];

    $service_amount = get_gigs_currency($service_details['service_amount'], $service_details['currency_code'], $user_currency_code);
} else {
    $user_currency_code = settings('currency');
    $service_amount = $service_details['service_amount'];
}    
?>
<div class="content">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-10">

                <div class="section-header text-center">
                    <h2>Book Service</h2>
                </div>


                <form method="post" enctype="multipart/form-data" autocomplete="off" id="book_services" >
                    <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
                    <input type="hidden" name="currency_code" value="<?php echo $user_currency_code; ?>">
                    <div class="row">
                        <div class="col-lg-6">
						    <div class="form-group">
                                <label>Service Location <span class="text-danger">*</span></label>
                                <input class="form-control" type="text" name="service_location" id="service_location" required>
<input type="hidden" class="form-control" id="map_key" value="<?=$map_key?>" >
                                <input type="hidden" name="service_latitude" id="service_latitude">
                                <input type="hidden" name="service_longitude" id="service_longitude">
                            </div>                            
                        </div>

                        <div class="col-lg-6">
                            <div class="form-group">
                                <label>Service amount</label>
                                <input class="form-control" type="text" name="service_amount" id="service_amount" value="<?php echo currency_conversion($user_currency_code) . $service_amount; ?>" readonly="">
                            </div>
                        </div>

                        <div class="col-lg-6">
                           <div class="form-group">
                                <label>Date <span class="text-danger">*</span></label>
                                <input class="form-control" type="text" name="booking_date" id="booking_date" />

                                <input type="hidden" name="provider_id" id="provider_id" value="<?php echo $service_details['user_id'] ?>">
                                <input type="hidden" name="service_id" id="service_id" value="<?php echo $service_details['id'] ?>">
                            </div>
                        </div>

                        <div class="col-lg-6">
                            <div class="form-group">
                                <label>Time slot <span class="text-danger">*</span></label>
                                <select class="form-control from_time" name="from_time" id="from_time" required>
                                </select>

                            </div>
                        </div>
                        <div class="col-lg-12">
                            <div class="form-group">
                                <div class="text-center">
                                    <div id="load_div"></div>
                                </div>
                                <label>Notes</label>
                                <textarea class="form-control" name="notes" id="notes" rows="5"></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="submit-section">
                        <button class="btn btn-primary submit-btn submit_service_book" data-loading-text="<i class='fa fa-spinner fa-spin '></i> Processing Order" data-id="<?php echo $service_details['id']; ?>" data-provider="<?php echo $service_details['user_id'] ?>" data-amount="<?php echo $service_details['service_amount']; ?>" type="submit" id="submit">Continue to Book</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
