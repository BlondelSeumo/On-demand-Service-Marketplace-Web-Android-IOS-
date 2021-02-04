<div class="page-wrapper">
	<div class="content container-fluid">
        <div class="container">
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <h4 class="page-title m-b-20 m-t-0">Create New Widget</h4>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div class="card-box">
                        <form id="add_footer_menu" action="<?php echo base_url().'admin/footer_menu/create'; ?>" method="POST" enctype="multipart/form-data">
                            <div class="form-group">
                            <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>"/>
                                <label>Widget Name</label>
                                <input type="text" class="form-control" name="menu_name" id="menu_name" required>
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