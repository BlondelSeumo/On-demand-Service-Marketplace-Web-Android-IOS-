<div class="content">
	<div class="container">
		<div class="row">
			 <?php $this->load->view('user/home/user_sidemenu');?>
		 
			<div class="col-xl-9 col-md-8">
			
				<h4 class="widget-title"><?php echo (!empty($user_language[$user_selected]['lg_Payment_History'])) ? $user_language[$user_selected]['lg_Payment_History'] : $default_language['en']['lg_Payment_History']; ?></h4>
				<div class="card transaction-table mb-0">
					<div class="card-body">
						<div class="table-responsive">
							<?php
									if(count($services)>0){?>
							<table class="table mb-0" id="order-summary">
								<?php } else{?>
									<table class="table mb-0" >
								<?php }?>
								<thead>
									<tr>
										<th><?php echo (!empty($user_language[$user_selected]['lg_Service'])) ? $user_language[$user_selected]['lg_Service'] : $default_language['en']['lg_Service']; ?></th>
										<th><?php echo (!empty($user_language[$user_selected]['lg_Customer'])) ? $user_language[$user_selected]['lg_Customer'] : $default_language['en']['lg_Customer']; ?></th>
										<th><?php echo (!empty($user_language[$user_selected]['lg_Date'])) ? $user_language[$user_selected]['lg_Date'] : $default_language['en']['lg_Date']; ?></th>
										<th><?php echo (!empty($user_language[$user_selected]['lg_Amount'])) ? $user_language[$user_selected]['lg_Amount'] : $default_language['en']['lg_Amount']; ?></th>
										<th><?php echo (!empty($user_language[$user_selected]['lg_Status'])) ? $user_language[$user_selected]['lg_Status'] : $default_language['en']['lg_Status']; ?></th>
									</tr>
								</thead>
								<tbody>

									<?php
									if(count($services)>0){
										$user_details = $this->db->where('id', $this->session->userdata('id'))->get('users')->row_array();
										$service_images='';
									 foreach($services as $row){ 
									   $amount_refund=''; 
									 	if(!empty($row['reject_paid_token'])){
									 	if($row['admin_reject_comment']=="This service amount favour for User"){
									 		$amount_refund="Amount refund to User";
									 	}else{
                                          $amount_refund="Amount refund to Provider";
									 	}
									 }
									 $user_currency = get_user_currency();
                                     $user_currency_code = $user_currency['user_currency_code'];
									$service_amount = get_gigs_currency($row["amount"], $row["currency_code1"], $user_details['currency_code']);

									 	if(!empty($row['service_image'])){

									 		
						 $service_image=$this->db->where('service_id',$row['service_id'])->get('services_image')->row_array();
						 if(!empty($service_image['service_image'])){
						 	 $service_images=$service_image['service_image'];
						 	}else{
						 		$service_images="";
						 	}
						 
									 	
									 	}

									 	?>
									
									<tr>
										<td>
											<a href="javascript:void(0);">
												<img src="<?php echo base_url().$service_images;?>" class="pro-avatar" alt=""> <?=$row['service_title'];?>
											</a>
										</td>
										<td>
											<img class="avatar-xs rounded-circle" src="<?php echo base_url().$row['profile_img'];?>" alt=""> <?=$row['name'];?>
										</td>
										<td><?=date('d M Y',strtotime($row['service_date']));?></td>
										<td><strong><?=currency_conversion($user_currency_code).$service_amount;?></strong></td>
										<td>
											<?php if(!empty($row['reject_paid_token'])){ ?>
												<span class="badge bg-success-light"><?=$amount_refund;?></span>

										<?php } if($row['booking_status']==6){?>
											<span class="badge bg-success-light"><?php echo (!empty($user_language[$user_selected]['lg_Payment_Completed'])) ? $user_language[$user_selected]['lg_Payment_Completed'] : $default_language['en']['lg_Payment_Completed']; ?></span>
										<?php }else if($row['booking_status']==5&&empty($row['reject_paid_token'])){?>
											<span class="badge bg-danger-light"><?php echo (!empty($user_language[$user_selected]['lg_Use_Rejected'])) ? $user_language[$user_selected]['lg_Use_Rejected'] : $default_language['en']['lg_Use_Rejected']; ?></span>
										<?php }else if($row['booking_status']==7&&empty($row['reject_paid_token'])){?>
                                            <span class="badge bg-danger-light"><?php echo (!empty($user_language[$user_selected]['lg_Provider_Rejected'])) ? $user_language[$user_selected]['lg_Provider_Rejected'] : $default_language['en']['lg_Provider_Rejected']; ?></span>
										<?php }?>
										</td>
									</tr>
								<?php } }else{?>
									<tr> <td colspan="5"> <div class="text-center text-muted">No data found</div></td> </tr>
								<?php }?>
								</tbody>
							</table>
						</div>
					</div>
				</div>			
			</div>
		</div>
	</div>
</div>