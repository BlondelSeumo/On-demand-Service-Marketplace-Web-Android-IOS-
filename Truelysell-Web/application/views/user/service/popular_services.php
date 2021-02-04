<div class="content">
	<div class="container">
		<div class="row">
            <div class="col-lg-12">
				<div class="section-header text-center">
					<h2>Popular Services</h2>
				</div>
				<div class="row">
					<?php 
					if(!empty($services)){
						foreach ($services as $srows) {
						$mobile_image=explode(',', $srows['mobile_image']);
						$this->db->select("service_image");
						$this->db->from('services_image');
						$this->db->where("service_id",$srows['id']);
						$this->db->where("status",1);
						$image = $this->db->get()->row_array(); 
					?>
					<div class="col-lg-4 col-md-6">
						<div class="service-widget">
							<div class="service-img">
								<a href="<?php echo base_url().'service-preview/'.str_replace(' ', '-', $srows['service_title']).'?sid='.md5($srows['id']);?>">
									<img class="img-fluid serv-img" alt="Service Image" src="<?php echo base_url().$image['service_image'];?>">
								</a>
								<div class="item-info">
									<div class="service-user">
										<a href="#">
											<img src="<?php echo base_url();?>assets/img/user.jpg">
										</a>
										<span class="service-price">$11</span>
									</div>
									<div class="cate-list"> <a class="bg-yellow" href="#"><?php echo $srows['category_name'];?></a></div>
								</div>
							</div>
							<div class="service-content">
								<h3 class="title">
									<a href="<?php echo base_url().'service-preview/'.str_replace(' ', '-', $srows['service_title']).'?sid='.md5($srows['id']);?>"><?php echo $srows['service_title'];?></a>
								</h3>
								<div class="rating">
									<i class="fas fa-star"></i>
									<i class="fas fa-star"></i>
									<i class="fas fa-star"></i>
									<i class="fas fa-star"></i>
									<i class="fas fa-star"></i>
									<span class="d-inline-block average-rating">(0)</span>
								</div>
								<div class="user-info">
									<div class="row">
										<span class="col ser-contact"><i class="fas fa-phone mr-1"></i> <span>xxxxxxxx<?=rand(00,99)?></span></span>
										<span class="col ser-location"><span>Coimbatore, Tamil Nadu, India</span> <i class="fas fa-map-marker-alt ml-1"></i></span>
									</div>
								</div>
							</div>
						</div>
					</div>
					<?php } }
					else{
						echo '<h3>No Services Found</h3>';
					} ?>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="deleteConfirmModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h5 class="modal-title" id="acc_title"></h5>
			</div>
			<div class="modal-body">
				<p id="acc_msg"></p>
			</div>
			<div class="modal-footer">
				<a href="javascript:;" class="btn btn-success si_accept_confirm">Yes</a>
				<button type="button" class="btn btn-danger si_accept_cancel" data-dismiss="modal">Cancel</button>
			</div>
		</div>
	</div>
</div>
