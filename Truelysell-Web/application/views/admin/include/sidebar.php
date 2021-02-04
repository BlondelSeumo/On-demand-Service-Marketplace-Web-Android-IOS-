<?php
    $page = $this->uri->segment(1);
    $active =$this->uri->segment(2);
	$access_result_data_array = $this->session->userdata('access_module');	
	$admin_id=$this->session->userdata('admin_id');
	//echo "<pre>";print_r($this->session->userdata('admin_id'));exit;
 ?>
 <div class="sidebar" id="sidebar">
	<div class="sidebar-logo">
		<a href="<?php echo $base_url; ?>dashboard">
			<img src="<?php echo $base_url; ?>assets/img/logo-icon.png" class="img-fluid" alt="">
		</a>
	</div>
	<div class="sidebar-inner slimscroll">
		<div id="sidebar-menu" class="sidebar-menu">
			<ul>
				<li class="<?php echo ($page == 'dashboard')?'active':'';?>">
					<a href="<?php echo $base_url; ?>dashboard"><i class="fas fa-columns"></i> <span>Dashboard</span></a>
				</li>
				<?php if(in_array(1,$access_result_data_array)) { ?>
				<li class="<?php echo ($page == 'adminusers')?'active':''; echo ($page == 'edit_adminuser')?'active':''; echo ($page == 'adminuser_details')?'active':'';?>" >
					<a href="<?php echo $base_url; ?>adminusers"><i class="fas fa-user"></i> <span>Admin Users</span></a>
				</li>
                <?php }if(in_array(2,$access_result_data_array)) { ?>                
				<li class="<?php echo ($page == 'categories')?'active':''; echo ($page == 'add-category')?'active':''; echo ($page == 'edit-category')?'active':'';?>">
					<a href="<?php echo $base_url; ?>categories"><i class="fas fa-layer-group"></i> <span>Categories</span></a>
				</li>
				<?php }if(in_array(3,$access_result_data_array)) { ?>
				<li class="<?php echo ($page == 'subcategories')?'active':''; echo ($page == 'add-subcategory')?'active':''; echo ($page == 'edit-subcategory')?'active':'';?>">
					<a href="<?php echo $base_url; ?>subcategories"><i class="fab fa-buffer"></i> <span>Sub Categories</span></a>
				</li>
				<?php }if(in_array(4,$access_result_data_array)) { ?>
				<li class="<?php echo ($page == 'service-list')?'active':''; echo ($page == 'service-details')?'active':''; ?>">
					<a href="<?php echo $base_url; ?>service-list"><i class="fas fa-bullhorn"></i> <span> Services</span></a>
				</li>
				<?php }if(in_array(5,$access_result_data_array)) { ?>
				<li class="<?php echo ($active =='total-report' || $active =='pending-report' || $active == 'inprogress-report' || $active == 'complete-report' || $active == 'reject-report' || $active == 'cancel-report' ||$active == 'reject-payment/(:num)')? 'active':''; ?>">
					<a href="<?php echo $base_url; ?>admin/total-report"><i class="far fa-calendar-check"></i> <span> Booking List</span></a>
				</li>
				<?php }if(in_array(6,$access_result_data_array)) { ?>
				<li class="<?php echo ($page == 'payment_list')?'active':''; echo ($page == 'admin-payment')?'active':'';?>">
					<a href="<?php echo $base_url; ?>payment_list"><i class="fas fa-hashtag"></i> <span>Payments</span></a>
				</li>
				<?php }if($admin_id==1){ ?>
				<li class="<?php echo ($page == 'contact')?'active':''; ?>">
					<a href="<?php echo $base_url; ?>admin/contact"><i class="fas fa-layer-group"></i> <span>Contact Details</span></a>
				</li>
				<?php } ?>
				<?php if(in_array(7,$access_result_data_array)) { ?>
				<li class="<?php echo ($page == 'ratingstype')?'active':''; echo ($page == 'add-ratingstype')?'active':''; echo ($page == 'edit-ratingstype')?'active':'';?>">
					 <a href="<?php echo $base_url; ?>ratingstype"><i class="fas fa-star-half-alt"></i> <span>Rating Type</span></a>
				</li> 
				<?php }if(in_array(8,$access_result_data_array)) { ?>
				<li class="<?php echo ($page == 'review-reports')?'active':''; echo ($page == 'add-review-reports')?'active':''; echo ($page == 'edit-review-reports')?'active':'';?>">
					 <a href="<?php echo $base_url; ?>review-reports"><i class="fas fa-star"></i> <span>Ratings</span></a>
				</li>
				<?php }if(in_array(9,$access_result_data_array)) { ?>				
				<li class="<?php echo ($page == 'subscriptions')?'active':''; echo ($page == 'add-subscription')?'active':''; echo ($page == 'edit-subscription')?'active':'';?>">
					<a href="<?php echo $base_url; ?>subscriptions"><i class="far fa-calendar-alt"></i> <span>Subscriptions</span></a>
				</li>
				<?php }if(in_array(10,$access_result_data_array)) { ?>
				<li class="<?php echo ($active =='wallet' || $active =='wallet-history')? 'active':''; ?>">
					 <a href="<?php echo $base_url; ?>admin/wallet"><i class="fas fa-wallet"></i> <span> Wallet</span></a>
				</li>
				<?php }if(in_array(12,$access_result_data_array)) { ?>
				<li class="<?php echo ($page == 'service-providers')?'active':'';?>" >
					<a href="<?php echo $base_url; ?>service-providers"><i class="fas fa-user-tie"></i> <span> Service Providers</span></a>
				</li>
				<?php }if(in_array(11,$access_result_data_array)) { ?>
				<li class="<?php echo ($page == 'Revenue')?'active':'';?>" >
					<a href="<?php echo $base_url; ?>Revenue"><i class="fas fa-percent"></i> <span>Revenue</span></a>
				</li>
                                
                                <!--Add Multiple Languages-->
                                
                                <li class="<?php echo ($page == 'language')?'active':'';?>" >
					<a href="<?php echo $base_url; ?>language"><i class="fas fa-flag"></i> <span>Language</span></a>
				</li>
				<li class="<?php echo ($page == 'country_code_config')?'active':'';?>">
					<a href="<?php echo $base_url; ?>admin/country_code_config"><i class="fas fa-columns"></i> <span>Country Code</span></a>
				</li>
				<?php }if(in_array(13,$access_result_data_array)) { ?>
				<li class="<?php echo ($page == 'users')?'active':'';?>" >
					<a href="<?php echo $base_url; ?>users"><i class="fas fa-user"></i> <span>Users</span></a>
				</li>
				<?php }if(in_array(14,$access_result_data_array)) { ?>
				<li class="<?php echo ($active == 'settings' || $active =='emailsettings' || $active =='stripe_payment_gateway')? 'active':''; ?>">
					<a href="<?php echo $base_url; ?>admin/settings"><i class="fas fa-cog"></i> <span> Settings</span></a>
				</li> 
<?php }if(in_array(15,$access_result_data_array)) { ?>
				<li class="<?php echo ($active == 'emailtemplate' || $active =='edit-emailtemplate')? 'active':''; ?>">
					<a href="<?php echo $base_url; ?>admin/emailtemplate"><i class="fas fa-envelope"></i> <span> Email Templates</span></a>
				</li>
				<?php }?>	

<?php if($admin_id==1){ ?>				
				<li class="<?php echo ($page == 'footer_menu')?'active':'';?>">
					<a href="<?php echo $base_url; ?>admin/footer_menu"><i class="fas fa-columns"></i> <span>Footer Menu</span></a>
				</li>
                                
				<li class="<?php echo ($page == 'footer_submenu')?'active':'';?>">
					<a href="<?php echo $base_url; ?>admin/footer_submenu"><i class="fas fa-layer-group"></i> <span>Footer Sub menu</span></a>
				</li>
				
<?php } ?>
			</ul>
		</div>
	</div>
</div>