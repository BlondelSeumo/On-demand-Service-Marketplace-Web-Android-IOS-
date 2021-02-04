<div class="page-wrapper">
    <div class="content container-fluid">
        <div class="row">
            <div class="col-xl-8 offset-xl-2">

                <!-- Page Header -->
                <div class="page-header">
                    <div class="row">
                        <div class="col">
                            <h3 class="page-title">Add Languages</h3>
                        </div>
                    </div>
                </div>
                <!-- /Page Header -->

                <div class="card">
                    <div class="card-body">
                        <form action="<?php echo $base_url; ?>insert-language" id="add_language" method="post" autocomplete="off" enctype="multipart/form-data">
                            <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>"/>

                            <div class="form-group">
                                <label>Language Name</label>
                                <input class="form-control" type="text" required  name="language_name" id="language_name">
                            </div>
                            <div class="form-group">
                                <label>Language Value</label>
                                <input class="form-control" type="text"  required name="language_value" id="language_value">
                            </div>
                            <div class="form-group">
                                <label>RTL or LTR (optional)</label>
                                <select class="form-control select" name="language_type" id="language_type">
                                    <option value="">Select Tag</option>
                                    <option value="rtl">RTL</option>
                                    <option value="ltr">LTR</option>
                                    
                                </select>
                            </div>

                            <div class="mt-4">
                                <?php if ($user_role == 1) { ?>
                                    <button class="btn btn-primary " name="form_submit" value="submit" type="submit">Add Language</button>
                                <?php } ?>

                                <a href="<?php echo $base_url; ?>language"  class="btn btn-link">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

