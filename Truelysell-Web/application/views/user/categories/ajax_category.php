
			<?php
			if(!empty($category)) {
				foreach ($category as $crows) {
					$category_name=strtolower($crows['category_name']);
				?>
			<div class="col-lg-4 col-md-6">
				<a href="<?php echo base_url();?>search/<?php echo str_replace(' ', '-', $category_name);?>">
					<div class="cate-widget">
						<img src="<?php echo base_url().$crows['category_image'];?>" alt="">
						<div class="cate-title">
							<h3><span><i class="fas fa-circle"></i> <?php echo ucfirst($crows['category_name']);?></span></h3>
						</div>
						<div class="cate-count">
							<i class="fas fa-clone"></i> <?php echo $crows['category_count'];?>
						</div>
					</div>
				</a>
			</div>
			<?php } }
			else { 

			echo '<div class="col-lg-12">
			<div class="category">
			No Categories Found
			</div>
			</div>';
			} 

			echo $this->ajax_pagination->create_links();
			?>
			<script src="<?php echo base_url();?>assets/js/functions.js"></script>	