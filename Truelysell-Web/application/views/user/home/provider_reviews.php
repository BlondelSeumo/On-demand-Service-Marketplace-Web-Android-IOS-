
<div class="content">
	<div class="container">
		<div class="row">
			<?php $this->load->view('user/home/provider_sidemenu');?>
			<div class="col-xl-9 col-md-8">
				<h4 class="widget-title"><?php echo (!empty($user_language[$user_selected]['lg_Reviews'])) ? $user_language[$user_selected]['lg_Reviews'] : $default_language['en']['lg_Reviews']; ?></h4>
				<div class="card review-card">
					<div class="card-body">
						<?php if(!empty($reviews)) { 
							foreach ($reviews as $review_list) {
								$datetime = new DateTime($review_list['created']); 
								$avg_rating=round($review_list['rating'],1);


								 $service_image=$this->db->where('service_id',$review_list['service_id'])->get('services_image')->row_array();
						 if(!empty($service_image['service_image'])){
						 	 $service_images=$service_image['service_image'];
						 	}else{
						 		$service_images="";
						 	}
								
								?>
								<div class="review-list">
									<div class="review-img"> 
										<img class="rounded img-fluid" src="<?php echo base_url().$service_images;?>" alt=""> 
									</div>
									<div class="review-info">
										<h5><a href=""><?php echo $review_list['service_title']?></a></h5>
										<div class="review-date"><?php echo $datetime->format('F d, Y H:i a');?></div>
										<p class="mb-2"><?php echo $review_list['review']?></p>
										<div class="review-user">
											<?php if($review_list['profile_img'] == '') { ?>
												<img class="avatar-xs rounded-circle" src="<?php echo base_url();?>assets/img/user.jpg" alt="">  
											<?php } else {?>
												<img class="avatar-xs rounded-circle" src="<?php echo base_url().$review_list['profile_img']?>" alt=""> 
											<?php } ?>
											<?php echo $review_list['name']?>
										</div>
									</div>
									<div class="review-count">
										<div class="rating">
											<?php 
											for($x=1;$x<=$avg_rating;$x++) {
												echo '<i class="fas fa-star filled"></i>';
											}
											if (strpos($avg_rating,'.')) {
												echo '<i class="fas fa-star"></i>';
												$x++;
											}
											while ($x<=5) {
												echo '<i class="fas fa-star"></i>';
												$x++;
											}
											?>
											<span class="d-inline-block average-rating">(<?php echo $avg_rating?>)</span>
										</div>
									</div>
								</div>
								
							<?php } } else { ?>
								<div class="py-3"><?php echo (!empty($user_language[$user_selected]['lg_No_reviews'])) ? $user_language[$user_selected]['lg_No_reviews'] : $default_language['en']['lg_No_reviews']; ?></div>
							<?php } ?>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>