<div class="page-wrapper">
	<div class="content container-fluid">
		<div class="row">
			<div class="col-xl-8 offset-xl-2">
			
			  <!-- Page Header -->
			  <div class="page-header">
					<div class="row">
						<div class="col-sm-12">
							<h3 class="page-title">Add Rating Type</h3>
						</div>
					</div>
				</div>
				<!-- /Page Header -->
				
				
				<div class="card">
					<div class="card-body">
						<form id="add_ratingstype" method="post" autocomplete="off" enctype="multipart/form-data">
							<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    

							<div class="form-group">
								<label>Ratings Type</label>
								<input class="form-control" type="text"  name="name" id="name">
							</div>
							<div class="mt-4">
								<?php if($user_role==1){?>
								<button class="btn btn-primary" name="form_submit" value="submit" type="submit">Submit</button>
								<?php }?>
									
								<a href="<?php echo $base_url; ?>ratingstype" class="btn btn-link">Cancel</a>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>