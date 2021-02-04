<div class="page-wrapper">
	<div class="content container-fluid">
	
		<div class="row">
			<div class="col-xl-8 offset-xl-2">

				<!-- Page Header -->
				<div class="page-header">
					<div class="row">
						<div class="col">
							<h3 class="page-title">Edit Email Template</h3>
						</div>
					</div>
				</div>
				<!-- /Page Header -->
				
				<div class="card">
					<div class="card-body">
                       <form class="form-horizontal"  method="POST" enctype="multipart/form-data" >
					     <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
							<div class="form-group">
								<label >Email Title : </label>								
								<?php if($edit_data['template_title']) echo $edit_data['template_title']; ?> 
							</div>
							<div class="form-group">
								<label>Reference ID : </label>
								#<?php if($edit_data['template_type']) echo $edit_data['template_type']; ?> 
							</div>
							<div class="form-group">
								<label>Template Content</label>
								<?php
								if (!empty($edit_data['template_content'])) {
									echo  "<textarea class='form-control' id='ck_editor_textarea_id' rows='6' name='template_content'>" . $edit_data['template_content'] ."</textarea>";
									echo display_ckeditor($ckeditor_editor1);
								}
								else {
									echo "<textarea class='form-control' id='ck_editor_textarea_id' rows='6' name='template_content'> </textarea>";
									echo display_ckeditor($ckeditor_editor1);
								}
								?>								
							</div>
							<div class="mt-4">
                                <button class="btn btn-primary" name="form_submit" value="submit" type="submit">Save Changes</button>

								<a href="<?php echo $base_url; ?>/emailtemplate"  class="btn btn-link">Cancel</a>

							</div>
						</form>                          
                    </div>
                </div>
			</div>
		</div>
	</div>
</div>