<?php
$category = $this->service->get_category();
$subcategory = $this->service->get_subcategory();
$service_image = $this->service->service_image($services['id']);
$service_id = $services['id'];


$user_currency_code = '';
$userId = $this->session->userdata('id');
If (!empty($userId)) {
    $service_amount = $services['service_amount'];
    $type = $this->session->userdata('usertype');
    if ($type == 'user') {
        $user_currency = get_user_currency();
    } else if ($type == 'provider') {
        $user_currency = get_provider_currency();
    }
    $user_currency_code = $user_currency['user_currency_code'];

    $service_amount = get_gigs_currency($services['service_amount'], $services['currency_code'], $user_currency_code);
} else {
    $user_currency_code = settings('currency');
    $service_amount = $services['service_amount'];
}
?>
<div class="content">
    <div class="container">

        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="section-header text-center">
                    <h2>Edit Service</h2>
                </div>

                <form method="post" enctype="multipart/form-data" autocomplete="off" id="update_service" action="<?php echo base_url() ?>user/service/update_service">
                    <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>">
                    <input class="form-control" type="hidden" name="currency_code" value="<?php echo $user_currency_code; ?>">
                    <div class="service-fields mb-3">
                        <h3 class="heading-2">Service Information</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="form-group">
                                    <label>Service Title <span class="text-danger">*</span></label>
                                    <input type="hidden" name="service_id" id="service_id" value="<?php echo $services['id']; ?>">
                                    <input class="form-control" type="text" name="service_title" id="service_title" value="<?php echo $services['service_title']; ?>" required>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="form-group">
                                    <label>Service Amount <span class="text-danger">*</span></label>
                                    <input class="form-control" type="text" name="service_amount" id="service_amount" value="<?php echo $service_amount; ?>" required>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="form-group">
                                    <label>Service Location <span class="text-danger">*</span></label>
                                    <input class="form-control" type="text" name="service_location" id="service_location" value="<?php echo $services['service_location'] ?>" required> 
                                    <input type="hidden" name="service_latitude" id="service_latitude" value="<?php echo $services['service_latitude'] ?>">
                                    <input type="hidden" name="service_longitude" id="service_longitude" value="<?php echo $services['service_longitude'] ?>">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="service-fields mb-3">
                        <h3 class="heading-2">Service Category</h3>
                        <div class="row">
                            <div class="col-lg-6">
                                <div class="form-group">
                                    <label>Category <span class="text-danger">*</span></label>
                                    <select class="form-control" name="category" required> 
<?php foreach ($category as $cat) { ?>
                                            <option value="<?= $cat['id'] ?>"  <?php if ($cat['id'] == $services['category']) { ?> selected = "selected" <?php } ?>><?php echo $cat['category_name'] ?>
                                            </option>
                                                <?php } ?>
                                    </select>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="form-group">
                                    <label>Sub Category <span class="text-danger">*</span></label>
                                    <select class="form-control" name="subcategory"> 
<?php foreach ($subcategory as $sub_category) { ?>
                                            <option value="<?= $sub_category['id'] ?>"  <?php if ($sub_category['id'] == $services['subcategory']) { ?> selected = "selected" <?php } ?>><?php echo $sub_category['subcategory_name'] ?>
                                            </option>
                                        <?php } ?>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="service-fields mb-3">
                        <h3 class="heading-2">Service Offer</h3>

                        <div class="membership-info">
<?php
if (!empty($serv_offered) && $serv_offered != 'null') {
    foreach ($serv_offered as $key => $value) {
        ?>

                                    <div class="row form-row membership-cont">
                                        <div class="col-lg-6">
                                            <div class="form-group">
                                                <label>Service Offered <span class="text-danger">*</span></label>
                                                <input type="text" class="form-control" name="service_offered[]"  value="<?php echo $value['service_offered'] ?>">
                                            </div>
                                        </div>
                                        <div class="col-12 col-md-2 col-lg-2">
                                            <label>&nbsp;</label>
                                            <a href="#" class="btn btn-danger trash"><i class="far fa-times-circle"></i></a>
                                        </div>
                                    </div>
    <?php
    }
} else {
    ?>
                                <div class="row form-row membership-cont">
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <input type="text" class="form-control" name="service_offered[]"  value="" >
                                        </div>
                                    </div>
                                    <div class="col-12 col-md-2 col-lg-2">
                                        <label class="d-md-block d-sm-none d-none">&nbsp;</label>
                                        <a href="#" class="btn btn-danger trash"><i class="far fa-times-circle"></i></a>
                                    </div>
                                </div>
<?php }
?>
                        </div>
                        <div class="add-more form-group">
                            <a href="javascript:void(0);" class="add-membership"><i class="fas fa-plus-circle"></i> Add More</a>
                        </div>
                    </div>

                    <div class="service-fields mb-3">
                        <h3 class="heading-2">Details Information</h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="form-group">
                                    <label>Descriptions <span class="text-danger">*</span></label>
                                    <textarea id="about" class="form-control service-desc" name="about"><?php echo $services['about'] ?></textarea>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="service-fields mb-3">
                        <h3 class="heading-2">Service Gallery </h3>
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="service-upload">
                                    <i class="fas fa-cloud-upload-alt"></i>
                                    <span>Upload Service Images *</span>
                                    <input type="file"  name="images[]" id="images" multiple accept="image/jpeg, image/png, image/gif,">

                                </div>	
                                <div id="uploadPreview">
                                    <ul class="upload-wrap">
<?php
$service_img = array();
for ($i = 0; $i < count($service_image); $i++) {
    ?>
                                            <li>
                                                <div class=" upload-images">

                                                    <img alt="Service Image" src="<?php echo base_url() . $service_image[$i]['service_image']; ?>">
                                                </div>
                                            </li>
                                        <?php } ?>
                                    </ul>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div class="submit-section">
                        <button class="btn btn-primary submit-btn" type="submit" name="form_submit" value="submit">Submit</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

