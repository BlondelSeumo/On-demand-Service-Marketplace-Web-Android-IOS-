<div class="page-wrapper">
	<div class="content container-fluid">
		<div class="row">
			<div class="col-xl-8 offset-xl-2">
			
				<!-- Page Header -->
				<div class="page-header">
					<div class="row">
						<div class="col">
							<h3 class="page-title">Add Sub Category</h3>
						</div>
					</div>
				</div>
				<!-- /Page Header -->
					
				<div class="card">
					<div class="card-body">
                        <form id="add_subcategory" method="post" autocomplete="off" enctype="multipart/form-data">
                            <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    
                            <div class="form-group">
                                <label>Category</label>
                                <select class="form-control select" name="category" id="category">
                                    <option value="">Select Category</option>
                                    <?php foreach ($categories as $rows) { ?>
                                    <option value="<?php echo $rows['id'];?>"><?php echo $rows['category_name'];?></option>
                                   <?php } ?>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Sub Category Name</label>
                                <input class="form-control" type="text"  name="subcategory_name" id="subcategory_name">
                            </div>
                            <div class="form-group">
                                <label>Sub Category Image</label>
                                <input class="form-control" type="file"  name="subcategory_image" id="subcategory_image">
                            </div>
                            <div class="mt-4">
                                <?php if($user_role==1){?>
                                <button class="btn btn-primary" name="form_submit" value="submit" type="submit">Add Subcategory</button>
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