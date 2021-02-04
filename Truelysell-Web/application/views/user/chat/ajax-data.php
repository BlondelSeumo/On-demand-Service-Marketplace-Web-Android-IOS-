<?php
					if(!empty($notification_list)){
						foreach ($notification_list as $key => $value) {
							$full_date =date('Y-m-d H:i:s', strtotime($value['utc_date_time']));
							$date=date('Y-m-d',strtotime($full_date));
							$date_f=date('d-m-Y',strtotime($full_date));
							$yes_date=date('Y-m-d',(strtotime ( '-1 day' , strtotime (date('Y-m-d')) ) ));
							$time=date('H:i',strtotime($full_date));
							$session = date('h:i A', strtotime($time));
							if($date == date('Y-m-d')){
							$timeBase ="Today ".$session;
							}elseif($date == $yes_date){
							$timeBase ="Yester day ".$session;
							}else{
							$timeBase =$date_f." ".$session;
							}
							 
               
               
						?>
						<div class="notificationlist">
							<div class="inner-content-blk position-relative">
								<div class="d-flex text-dark">
									<?php
									if(!empty($value['profile_img'])){
										$image=base_url().$value['profile_img'];
									}else{
										$image=base_url().'assets/img/user.jpg';
									}
									?>
									<img class="rounded" src="<?php echo $image;?>" width="50" alt="">
									<div class="noti-contents">
										<h3><strong><?=strtolower($value['message']);?></strong></h3>
										<span><?=$timeBase;?></span>
									</div>
								</div>

							</div>
						</div>

					<?php } }else{ ?>
					<div class="notificationlist">
						<p class="text-center text-danger mt-3"><?php echo (!empty($user_language[$user_selected]['lg_notification_empty'])) ? $user_language[$user_selected]['lg_notification_empty'] : $default_language['en']['lg_notification_empty']; ?></p>
					</div>
				   <?php } ?>
				   <?php 
					if(!empty($notification_list)){
						echo $this->ajax_pagination->create_links();
					} ?>
					<script src="<?php echo base_url();?>assets/js/functions.js"></script>