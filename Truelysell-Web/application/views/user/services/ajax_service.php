<?php
	$query = $this->db->query("select * from system_settings WHERE status = 1");
	$result = $query->result_array();
	if(!empty($result))
		{
		foreach($result as $data){
			if($data['key'] == 'currency_option'){
				$currency_option = $data['value'];
			}
		}
	}

	if(!empty($service)) {
		foreach ($service as $srows) {
			$provider_details = $this->db->where('id',$srows['user_id'])->get('providers')->row_array();
			$serviceimage=explode(',', $srows['service_image']);

			$this->db->select('AVG(rating)');
			$this->db->where(array('service_id'=>$srows['id'],'status'=>1));
			$this->db->from('rating_review');
			$rating = $this->db->get()->row_array();
			$avg_rating = round($rating['AVG(rating)'],1);
			$service_amount = $srows['service_amount'];
			$userId = $this->session->userdata('id');
			If (empty($userId)) {
			$user_currency_code = settings('currency');
			$service_currency_code = $srows['currency_code'];
			$service_amount = get_gigs_currency($srows['service_amount'], $srows['currency_code'], $user_currency_code);
			}
			$serviceimages=$this->db->where('service_id',$srows['id'])->get('services_image')->row_array();
			
			
		?>
		
<div class="col-lg-4 col-md-6">
	<div class="service-widget">
		<div class="service-img">
			<a href="<?php echo base_url().'service-preview/'.str_replace(' ', '-', $srows['service_title']).'?sid='.md5($srows['id']);?>">
				<img class="img-fluid serv-img" alt="Service Image" src="<?php echo base_url().$serviceimages['service_image'];?>">
			</a>
			<div class="item-info">
				<div class="service-user">
					<a href="#">
						<?php if($provider_details['profile_img'] == '') { ?>
						<img src="<?php echo base_url();?>assets/img/user.jpg" alt="">
						<?php } else { ?>
						<img src="<?php echo base_url().$provider_details['profile_img']?>" alt="">
						<?php } ?>
					</a>
					<span class="service-price"><?php echo currency_conversion(settings('currency')).$service_amount;?></span>
				</div>
				<div class="cate-list"> <a class="bg-yellow" href="#"><?php echo ucfirst($srows['category_name']);?></a></div>
			</div>
		</div>
		<div class="service-content">
			<h3 class="title">
				<a href="<?php echo base_url().'service-preview/'.str_replace(' ', '-', $srows['service_title']).'?sid='.md5($srows['id']);?>"><?php echo ucfirst($srows['service_title']);?></a>
			</h3>
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
			<div class="user-info">
				<div class="row">
					<?php if($this->session->userdata('id') != '')
					{ ?>
					<span class="col ser-contact"><i class="fas fa-phone mr-1"></i> <span>xxxxxxxx<?=rand(00,99)?></span></span>
					<?php } else { ?>
					<span class="col ser-contact"><i class="fas fa-phone mr-1"></i> <span>xxxxxxxx<?=rand(00,99)?></span></span>
					<?php } ?>
					<span class="col ser-location"><span><?php echo ucfirst($srows['service_location']);?></span> <i class="fas fa-map-marker-alt ml-1"></i></span>
				</div>
			</div>
		</div>
	</div>
</div>
<?php } }
else { 
	echo '<div class="col-lg-12">
		<p class="mb-0">No Services Found</p>
	</div>';
} 
echo $this->ajax_pagination->create_links();
?>
		<script src="<?php echo base_url();?>assets/js/functions.js"></script>