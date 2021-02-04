<div class="page-wrapper">
	<div class="content container-fluid">
		<div class="row">
			<div class="col-xl-8 offset-xl-2">

				<!-- Page Header -->
				<div class="page-header">
					<div class="row">
						<div class="col">
							<h3 class="page-title">Edit Sub Category</h3>
						</div>
					</div>
				</div>
				<!-- /Page Header -->
				
				<div class="card">
					<div class="card-body">
						<form id="update_subcategory" method="post" autocomplete="off" enctype="multipart/form-data">
							<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    
							<div class="form-group">
								<label>Category</label>
								<select class="form-control" name="category" id="category">
									<option value="">Select Category</option>
									<?php foreach ($categories as $rows) { ?>
									<option value="<?php echo $rows['id'];?>" <?php if($rows['id']==$subcategories['category']) echo 'selected';?>><?php echo $rows['category_name'];?></option>
									<?php } ?>
								</select>
							</div>
							<div class="form-group">
								<label>Subcategory Name</label>
								<input class="form-control" type="text" value="<?php echo $subcategories['subcategory_name'];?>"  name="subcategory_name" id="subcategory_name">
								<input class="form-control" type="hidden" value="<?php echo $subcategories['id'];?>"  name="subcategory_id" id="subcategory_id">
							</div>
							<div class="form-group">
								<label>Category Image</label>
								<input class="form-control" type="file"  name="subcategory_image" id="subcategory_image">
							</div>
							<div class="form-group">
								<div class="avatar">
									<img class="avatar-img rounded" alt="" src="<?php echo base_url().$subcategories['subcategory_image'];?>">
								</div>
							</div>
							<div class="mt-4">
								 <?php if($user_role==1){?>
								<button class="btn btn-primary" name="form_submit" value="submit" type="submit">Save Changes</button>
								 <?php }?>
	
								<a href="<?php echo $base_url; ?>subcategories" class="btn btn-link">Cancel</a>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>