<div class="page-wrapper">
	<div class="content container-fluid">
	
		<div class="row">
			<div class="col-xl-8 offset-xl-2">

				<!-- Page Header -->
				<div class="page-header">
					<div class="row">
						<div class="col">
							<h3 class="page-title">Edit Cancel Booking</h3>
						</div>
					</div>
				</div>
				<!-- /Page Header -->
				
				<div class="card">
					<div class="card-body">
                        <form id="edit_cancel_form" method="post" autocomplete="off" enctype="multipart/form-data">
                        	<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    

                            <div class="form-group">
                                <label>Service Name : </label>
                              <label><?=$list->service_title;?></label>
                            </div>
                          
                            <div class="form-group">
                                <label>User Name : </label>
                              <label><?=$list->user_name;?></label>
                            </div>
                              <div class="form-group">
                                <label>Provider Name : </label>
                              <label><?=$list->provider_name;?></label>
                            </div>
                             <div class="form-group">
                                <label>Date : </label>
                              <label><?=date('d M Y',strtotime($list->service_date));?></label>
                            </div>
                            <div class="form-group">
                                <label>Total : </label>
                              <label><?=$list->amount;?></label>
                            </div>
                             <div class="form-group">
                                <label>Amount Refund To : </label>
                                <div class="col-md-5">
                              <select class="form-control" name="amount_refund">
                              	<option value="">Select One</option>
                              	<option value="1">Provider</option>
                              	<option value="2">User</option>
                              </select>
                              <input type="hidden" name="booking_id" value="<?=$list->id;?>">
                          </div>
                            </div>
                            <div class="mt-4">
                            	 <?php if($user_role==1){?>
                                <button class="btn btn-primary" name="form_submit" value="submit" type="submit">Save Changes</button>
                               <?php }?>
								<a href="<?php echo $base_url; ?>admin/cancel-report"  class="btn btn-link">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
			</div>
		</div>
	</div>
</div>