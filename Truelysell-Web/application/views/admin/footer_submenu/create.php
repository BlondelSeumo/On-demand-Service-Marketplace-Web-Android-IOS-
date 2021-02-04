<div class="page-wrapper">
	<div class="content container-fluid">
		<div class="container">
			<div class="row">
				<div class="col-md-8 col-md-offset-2">
					<h4 class="page-title m-b-20 m-t-0">Create Footer Menu</h4>
				</div>
			</div>
			<div class="row">
				<div class="col-md-8 col-md-offset-2">
					<div class="card-box">
						<form class="form-horizontal" id="add_submenu" action="<?php echo base_url('admin/footer_submenu/create'); ?>" method="POST" enctype="multipart/form-data">
							<div class="form-group">
							<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>"/>
								<label class="col-sm-3 control-label"></label>
								<div class="radio radio-danger radio-inline">
									<input type="radio" id="menu_status" value="0" name="menu_status">
									<label for="menu_status">Menu</label>
								</div>
								<div class="radio radio-danger radio-inline">
									<input type="radio" id="menu_status_one" value="1" name="menu_status">
									<label for="menu_status">Widget</label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">Widget</label>
								<div class="col-sm-9">
									<select class="form-control" name="main_menu" id="main_menu">
										<?php
										foreach ($main_menu as $main) { ?>
											<option value="<?php echo $main['id']; ?>"><?php echo str_replace('_', ' ', $main['title']); ?></option>
										<?php  }
										?>
									</select>
								</div>
							</div>
							<div class="form-group sub_menu">
								<label class="col-sm-3 control-label">Title</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="sub_menu" id="sub_menu" value="">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">Page Content</label>
								<div class="col-sm-9">
									<textarea class="form-control" name="page_desc" id="ck_editor_textarea_id"></textarea>
									<?php echo display_ckeditor($ckeditor_editor1);  ?>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">Page Status</label>
								<div class="col-sm-9">
									<div class="radio radio-primary radio-inline">
										<input type="radio" id="academy_status1" value="1" name="status" checked="">
										<label for="academy_status1">Active</label>
									</div>
									<div class="radio radio-danger radio-inline">
										<input type="radio" id="academy_status2" value="0" name="status">
										<label for="academy_status2">Inactive</label>
									</div>
								</div>
							</div>
							<div class="m-t-30 text-center">
								<button name="form_submit" type="submit" class="btn btn-primary center-block" value="true">Save Changes</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>