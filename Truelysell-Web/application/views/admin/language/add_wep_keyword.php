<div class="page-wrapper">
    <div class="content container-fluid">
        <div class="row">
            <div class="col-xl-8 offset-xl-2">

                <!-- Page Header -->
                <div class="page-header">
                    <div class="row">
                        <div class="col">
                            <h3 class="page-title">Add Web Keywords</h3>
                        </div>
                    </div>
                </div>
                <!-- /Page Header -->

                <div class="card">
                    <div class="card-body">
                        <form action="<?php echo $base_url; ?>insert_web_keyword" id="add_category" method="post" autocomplete="off" enctype="multipart/form-data">
                            <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>"/>

                            <div class="form-group">
                                <label>Filed Name</label>
                                <input class="form-control" type="text"  name="filed_name" id="category_name">
                            </div>
                            <div class="form-group">
                                <label>Key Name</label>
                                <input class="form-control check_key_name" type="text"  name="key_name" id="category_name">
                            </div>
                            

                            <div class="mt-4">
                                <?php if ($user_role == 1) { ?>
                                    <button class="btn btn-primary " name="form_submit" value="submit" type="submit">Add Language</button>
                                <?php } ?>

                                <a href="<?php echo $base_url; ?>wep_language"  class="btn btn-link">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

