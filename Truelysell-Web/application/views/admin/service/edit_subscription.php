<div class="page-wrapper">
	<div class="content container-fluid">
		<div class="row">
			<div class="col-xl-8 offset-xl-2">
			
				<!-- Page Header -->
				<div class="page-header">
					<div class="row">
						<div class="col-sm-12">
							<h3 class="page-title">Add Subscription</h3>
						</div>
					</div>
				</div>
				<!-- /Page Header -->
				
				<div class="card">
					<div class="card-body">
                        <form id="editSubscription" method="post">
                        	
<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    
                            <div class="form-group">
                                <label>Subscription Name</label>
                                <input class="form-control" type="text" value="<?php echo $subscription['subscription_name']; ?>" name="subscription_name" id="subscription_name">
                                <input class="form-control" type="hidden" value="<?php echo $subscription['id']; ?>" name="subscription_id" id="subscription_id">
                            </div>
                            <div class="form-group">
                                <label>Subscription Amount</label>
                                <input class="form-control" type="number" step="0.01" min="0" value="<?php echo $subscription['fee']; ?>" name="amount" id="amount">
                            </div>
                            <div class="form-group">
                                <label>Subscription Duration</label>
								<select class="form-control" name="duration" id="duration">
									<option value="">Select Duration</option>
									<option value="1" <?php echo $subscription['duration']==1 ? "selected":""; ?>>One Month</option>
									<option value="3" <?php echo $subscription['duration']==3 ? "selected":""; ?>>3 Months</option>
									<option value="6" <?php echo $subscription['duration']==6 ? "selected":""; ?>>6 Months</option>
									<option value="12" <?php echo $subscription['duration']==12 ? "selected":""; ?>>One Year</option>
									<option value="24" <?php echo $subscription['duration']==24 ? "selected":""; ?>>2 Years</option>
								</select>
                                <input type="hidden" name="subscription_description" id="subscription_description" value="<?php echo $subscription['fee_description']; ?>">
                            </div>
                            <?php
								$value=$this->db->select('count(id) as counts')->from('subscription_details')->where('subscription_id',$subscription['id'])->get()->row();
                            ?>
                            <div class="form-group">
                                <label class="d-block">Subscription Status</label>
                                <label class="radio-inline">
                                    <input name="status" checked="checked" name="status" id="status1" value="1" type="radio" <?php echo $subscription['status']==1 ? "checked":""; ?>> Active
                                </label>
                                <?php
                                if ($value->counts==0 || $value->counts=='') { ?>
									<label class="radio-inline">
                                    <input name="status" type="radio" name="status" id="status2" value="0" <?php echo $subscription['status']==0 ? "checked":""; ?>> Inactive
                                </label> 
                                <?php } ?>
                            </div>
                            <div class="mt-4">
								<?php if($user_role==1){?>
                                <button class="btn btn-primary" type="submit">Save Changes</button>
								<?php }?>
									
                                <a href="<?php echo $base_url; ?>subscriptions" class="btn btn-link">Cancel</a>
                            </div>
                        </form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>