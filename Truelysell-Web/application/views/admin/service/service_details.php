<?php 
$service_id = $this->uri->segment('2');
$this->db->select('*');
$this->db->select('s.status as check_status');
$this->db->from('services s');
$this->db->join('categories c', 'c.id = s.category', 'LEFT');
$this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
$this->db->where('s.id',$service_id);
$service_details = $this->db->get()->row_array();
$this->db->select("service_image");
$this->db->from('services_image');
$this->db->where("service_id",$service_id);
$this->db->where("status",1);
$services_image = $this->db->get()->row_array();
$this->db->select('AVG(rating)');
$this->db->where(array('service_id'=>$service_id,'status'=>1));
$this->db->from('rating_review');
$rating = $this->db->get()->row_array();
$avg_rating = round($rating['AVG(rating)'],2);
if(!empty($service_details['user_id'])){
		$provider_online=$this->db->where('id',$service_details['user_id'])->from('providers')->get()->row_array();
		$datetime1 = new DateTime();
$datetime2 = new DateTime($provider_online['last_logout']);
$interval = $datetime1->diff($datetime2);
$days = $interval->format('%a');
$hours = $interval->format('%h');
$minutes = $interval->format('%i');
$seconds = $interval->format('%s');
	}else{
$days=$hours=$minutes=$seconds=0;
	}
	$business_hours = $this->db->where('provider_id',$service_details['user_id'])->get('business_hours')->row_array();
$availability_details = json_decode($business_hours['availability'],true);
 $service_image = $this->service->service_image($service_id);
?>
<div class="page-wrapper">
	<div class="content container-fluid">
		<div class="row">
			<div class="col-lg-8">
				<div class="card">
					<div class="card-body">
						<div class="service-header">
							<div class="service-inner">
								<h2><?php echo $service_details['service_title']?></h2>
								<address class="service-location"><i class="fas fa-location-arrow"></i> <?php echo $service_details['service_location']?></address>
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
							<span class="d-inline-block average-rating">(<?php echo $avg_rating;?>)</span>
								</div>
								<div class="service-cate">
									<a href="javascript:void(0);"><?php echo $service_details['category_name']?></a>
								</div>
							</div>
							<div class="service-amount">
								<span>$<?php echo $service_details['service_amount']?></span>
							</div>
						</div>
						<div class="service-description">
							<div class="service-images service-carousel">
								<div class="images-carousel owl-carousel owl-theme">
								<?php
								if(!empty($service_image))
								{
									for ($i=0; $i < count($service_image) ; $i++) { 
										echo'<div class="item"><img src="'.base_url().$service_image[$i]['service_image'].'" alt="" class="img-fluid"></div>';
									}
								}
								?>
								</div>
							</div>
							<h5 class="card-title">Service Details</h5>
							<p class="mb-0"><?php echo $service_details['about']?></p>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-4">
				<div class="card provider-widget clearfix">
					<div class="card-body">
						<h5 class="card-title">Service Provider</h5>
						<?php
										if(!empty($service_details['user_id'])){
											$provider=$this->db->select('*')->
											from('providers')->
											where('id',$service_details['user_id'])->
											get()->row_array();
										?>
						<div class="about-author">
							<div class="about-provider-img">
								<div class="provider-img-wrap">
									<?php
													if(!empty($provider['profile_img'])){
														$image=base_url().$provider['profile_img'];
													}else{
														$image=base_url().'assets/img/user.jpg';
													}
													?>
													<a href="javascript:void(0);"><img class="img-fluid rounded-circle" alt="" src="<?php echo $image;?>"></a>
								</div>
							</div>
							<div class="provider-details">
									<a href="javascript:void(0);" class="ser-provider-name"><?=!empty($provider['name'])?$provider['name']:'-';?></a>
												<p class="last-seen"> 
												<?php if($provider_online['is_online']==2){ ?>
												<i class="fas fa-circle"></i> Last seen: &nbsp;
												<?= (!empty($days))?$days.' days':'';?> 
												<?php if($days==0){?>
												<?= (!empty($hours))?$hours.' hours':''; ?>
												<?php }?>
												 <?php if($days==0&&$hours==0){?>
												<?= (!empty($minutes))?$minutes.' min':'';?>
											     <?php }?>
												 ago
												</p>
												<?php }elseif($provider_online['is_online']==1){?>
													<i class="fas fa-circle online"></i> Online</p>
												<?php }?>
												<p class="text-muted mb-1">Member Since <?= date('M Y',strtotime($provider['created_at']));?></p>
							</div>
						</div>
						<hr>
						<div class="provider-info">
							<p class="mb-1"><i class="far fa-envelope"></i> <?=$provider['email']?></p>
											<p class="mb-0"><i class="fas fa-phone-alt"></i> xxxxxxxx<?=rand(00,99)?></p>
						</div>
							<?php } ?>
					</div>
				</div>
				<div class="card available-widget">
					<div class="card-body">
						<h5 class="card-title">Service Availability</h5>
						<ul>
							<?php
					if(!empty($availability_details))
					{
					foreach ($availability_details as $availability) {

					$day = $availability['day'];
					$from_time = $availability['from_time'];
					$to_time = $availability['to_time'];

					  if($day == '1')
					  {
						$weekday = 'Monday';
					  }
					  elseif($day == '2')
					  {
						$weekday = 'Tuesday';
					  }
					  elseif($day == '3')
					  {
						$weekday = 'Wednesday';
					  }
					  elseif($day == '4')
					  {
						$weekday = 'Thursday';
					  }
					  elseif($day == '5')
					  {
						$weekday = 'Friday';
					  }
					  elseif($day == '6')
					  {
						$weekday = 'Saturday';
					  }
					  elseif($day == '7')
					  {
						$weekday = 'Sunday';
					  }
					  elseif($day == '0')
					  {
						$weekday = 'Sunday';
					  }
					 
					echo '<li><span>'.$weekday.'</span>'.$from_time.' - '.$to_time.'</li>'; 
					}
					}
					else
					{
						echo '<li class="text-center">No Details found</li>';
					}
					
					?>
						</ul>
					</div>				
				</div>
			</div>
		</div>
	</div>
</div>