<div class="page-wrapper">
	<div class="content container-fluid">
        <div class="container">
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <h4 class="page-title m-b-20 m-t-0">Footer Widget Edit</h4>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div class="card-box">
                        <?php
                        foreach ($datalist as $value) {
                        ?>
                            <form action="<?php echo base_url().'admin/footer_menu/edit/' . $value['id']; ?>" method="POST" enctype="multipart/form-data">
                            <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>"/>
                                <div class="form-group">
                                    <label>Widget Name</label>
                                    <input type="text" class="form-control" name="menu_name" id="menu_name" value="<?php if ($value['title']) echo str_replace('_', ' ', $value['title']) ?>">
                                </div>
                                <div class="m-t-30 text-center">
                                    <button name="form_submit" type="submit" class="btn btn-primary" value="true">Save Changes</button>
                                </div>
                            </form>
                        <?php } ?>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>