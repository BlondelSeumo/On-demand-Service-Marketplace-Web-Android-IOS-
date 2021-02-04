<?php 
 $admin_notification=$this->db->where('n.receiver',$this->session->userdata('chat_token'))->where('n.status',1)->from('notification_table as n')->join('providers as p ','p.token=n.sender','left')->select('n.notification_id,n.message,n.created_at,p.name,p.profile_img,n.utc_date_time')->get()->result_array();?>
<div class="header">
	<div class="header-left">
		<a href="<?php echo $base_url; ?>dashboard" class="logo">
			<img src="<?php echo $base_url.$this->website_logo_front; ?>" width="140" height="46" alt="">
		</a>
		<a href="<?php echo $base_url; ?>dashboard" class="logo logo-small">
			<img src="<?php echo $base_url.$this->website_logo_front; ?>" alt="Logo" width="30" height="30">
		</a>
	</div>
	<a href="javascript:void(0);" id="toggle_btn">
		<i class="fas fa-align-left"></i>
	</a>
	<a class="mobile_btn" id="mobile_btn" href="javascript:void(0);">
		<i class="fas fa-align-left"></i>
	</a>
	
	<ul class="nav user-menu">
		<!-- Notifications -->
		<li class="nav-item dropdown noti-dropdown">
			<a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown">
				<i class="far fa-bell"></i> <span class="badge badge-pill"></span>
			</a>
			<div class="dropdown-menu dropdown-menu-right notifications">
				<div class="topnav-dropdown-header">
					<span class="notification-title">Notifications</span>
					<a href="javascript:void(0)" class="clear-noti"> Clear All </a>
				</div>
				<div class="noti-content">
					<ul class="notification-list">
						<?php foreach($admin_notification as $value){
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
						<li class="notification-message">
							<a href="<?= base_url().'admin-notification';?>">
								<div class="media">
									<span class="avatar avatar-sm">
										<?php
										if(!empty($value['profile_img'])){
											$image=base_url().$value['profile_img'];
										}else{
											$image=base_url().'assets/img/user.jpg';
										}
										?>
										<img class="avatar-img rounded-circle" alt="User Image" src="<?php echo $image;?>">
									</span>
									<div class="media-body">
										<p class="noti-details"><span class="noti-title"></span>  <span class="noti-title"><?= $value['message'];?></span></p>
										<p class="noti-time"><span class="notification-time"><?= $timeBase;?></span></p>
									</div>
								</div>
							</a>
						</li>
					<?php }?>
						
					</ul>
				</div>
				<div class="topnav-dropdown-footer">
					<a href="<?= base_url().'admin-notification'?>">View all Notifications</a>
				</div>
			</div>
		</li>
		<!-- /Notifications -->
		
		<li class="nav-item dropdown">
			<a href="javascript:void(0)" class="dropdown-toggle user-link  nav-link" data-toggle="dropdown">
				<span class="user-img">
				  <?php
				 $admin_id=$this->session->userdata('admin_id');
				 $admin_profile=$this->db->where('user_id',$admin_id)->get('administrators')->row_array();
				   if(!empty($admin_profile['profile_img'])){
				   	$prof_img = $admin_profile['profile_img'];
				   }else{
				   	$prof_img = "";
				   }
				   
				   $navprofile_img = (!empty($prof_img))?$prof_img:'assets/img/user.jpg';?>
					<img class="rounded-circle" src="<?php echo $base_url.$navprofile_img; ?>" width="40" alt="Admin">
				</span>
			</a>
			<div class="dropdown-menu dropdown-menu-right">
				<a class="dropdown-item" href="<?php echo $base_url; ?>admin-profile">Profile</a>
				<a class="dropdown-item" href="<?php echo $base_url; ?>admin/logout">Logout</a>
			</div>
		</li>
	</ul>
</div>

                <?php if($this->session->flashdata('error_message')) {  ?>
		<div class="alert alert-danger text-center" id="flash_error_message"><?php echo $this->session->flashdata('error_message');?></div>
		<?php $this->session->unset_userdata('error_message');
		} ?>
		<?php if($this->session->flashdata('success_message')) {  ?>
		<div class="alert alert-success text-center" id="flash_succ_message"><?php echo $this->session->flashdata('success_message');?></div>
		<?php $this->session->unset_userdata('success_message');
		} ?>