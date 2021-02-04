<div class="page-wrapper">
	<div class="content container-fluid">
        <div class="container">
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <h4 class="page-title m-b-20 m-t-0">Create New Country code</h4>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div class="card-box">
                        <form id="add_country_code_config" action="<?php echo base_url().'admin/country_code_config/create'; ?>" method="POST" enctype="multipart/form-data">
                            <div class="form-group">
                            <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>"/>
                                <label>Country code</label>
                                <input type="text" class="form-control" name="country_code" id="country_code" required>
                            </div>
                            <div class="form-group">
                                <label>Country id</label>
                                <input type="text" class="form-control" name="country_id" id="country_id" required>
                            </div>
                            <div class="form-group">
                                <label>Country name</label>
                                <input type="text" class="form-control" name="country_name" id="country_name" required>
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