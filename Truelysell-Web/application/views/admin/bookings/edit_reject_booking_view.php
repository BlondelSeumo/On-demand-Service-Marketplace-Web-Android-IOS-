<div class="page-wrapper">
	<div class="content container-fluid">
		<div class="row">
			<div class="col-xl-8 offset-xl-2">
					
				<!-- Page Header -->
				<div class="page-header">
					<div class="row">
						<div class="col">
							<?php if($list['payment_status']==5){?>
							<h3 class="page-title">Edit Reject Payment</h3>
							<?php }else{ ?>
								<h3 class="page-title">Edit Cancel Payment</h3>
							<?php }?>
						</div>
					</div>
				</div>
				<!-- /Page Header -->
				
				<div class="card">
					<div class="card-body">
						<form method="post" method="post" action="<?=base_url('pay-reject')?>" id="reject_payment_submit" autocomplete="off" enctype="multipart/form-data">
                            <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    

							<input type="hidden" name="booking_id" value="<?=$list['id'];?>">
                            <div class="form-group">
                                <label>Service Title</label>
                                <input class="form-control" type="text" name="service_name" id="service_name" value="<?=!empty($list['service_title'])?$list['service_title']:'';?>" readonly>
                            </div>
							<div class="form-group">
                                <label>Service Amount</label>
                                <input class="form-control" type="text" name="service_amount" id="service_amount" value="<?=!empty($list['amount'])?$list['amount']:'';?>" readonly>
                            </div>
                            <div class="form-group">
                                <label>Rejection Comments.</label>
                                <textarea class="form-control" readonly=""><?=!empty($list['reason'])?$list['reason']:'not mentioned...';?></textarea>
                            </div>
                            <div class="form-group">
                                <label>Service Amount Favour for</label>
								<div>
									<label class="radio-inline"><input class="pay_for"  type="radio" checked="checked" name="pay_for" value="1"> Provider </label>
									<label class="radio-inline"><input class="pay_for"  type="radio" name="pay_for" value="2"> User </label>
								</div>
                            </div>
                            <input type="hidden" name="token" id="token" value="<?=$list['provider_token'];?>">
							<div class="form-group">
                                <label>Favour comments</label>
                                <textarea name="favour_comment" id="fav_com" class="form-control">This service amount favour for Provider</textarea>
                            </div>
                            <div class="mt-4">
                            	<?php if($user_role==1){?>
                                <button class="btn btn-primary" name="form_submit" value="submit" type="submit">Submit</button>
                                <?php }?>

								<a href="<?php echo $base_url; ?>admin/reject-report"  class="btn btn-link">Cancel</a>
                            </div>
                            <input type="hidden" id="user_token" value="<?=$list['user_token'];?>">
                            <input type="hidden" id="provider_token" value="<?=$list['provider_token'];?>">
                        </form>
                    </div>
				</div>
			</div>
		</div>
	</div>
</div>
