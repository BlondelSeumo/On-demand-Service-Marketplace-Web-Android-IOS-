<div class="page-wrapper">
    <div class="content container-fluid">
        <!-- Page Header -->
        <div class="page-header">
            <div class="row">
                <div class="col">
                    <h3 class="page-title"></h3>
                </div>
            </div>
        </div>
        <!-- /Page Header -->

        <ul class="nav nav-tabs menu-tabs">
            <li class="nav-item">
                <a class="nav-link" href="<?php echo base_url() . 'admin/settings'; ?>">General Settings</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<?php echo base_url() . 'admin/emailsettings'; ?>">Email Settings</a>
            </li>
            <li class="nav-item ">
                <a class="nav-link" href="<?php echo base_url() . 'admin/stripe_payment_gateway'; ?>">Payment Gateway</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<?php echo base_url() . 'admin/sms-settings'; ?>">SMS Gateway</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="<?php echo base_url() . 'admin/theme-color'; ?>">Theme Color Change</a>
            </li>
        </ul>



        <div class="row">
            <div class="col-lg-8">
                <?php if ($this->session->flashdata('error_message1')) { ?>
                    <div class="alert alert-danger text-center" id="flash_error_message"><?php echo $this->session->flashdata('error_message1'); ?></div>
                    <?php
                    $this->session->unset_userdata('error_message1');
                }
                ?>
                <?php if ($this->session->flashdata('success_message1')) { ?>
                    <div class="alert alert-success text-center" id="flash_succ_message"><?php echo $this->session->flashdata('success_message1'); ?></div>
                    <?php
                    $this->session->unset_userdata('success_message1');
                }
                ?>
                <div class="card">
                    <div class="card-header">
                        <h4 class="card-title">Theme Color Change</h4>
                    </div>

                    <div class="card-body">

                            <form action='<?php echo $base_url; ?>Change_color' method="POST" name='DatabaseForm' novalidate>
                                <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>"/>
                                <div class="row"> 
                                    <?php
                                    foreach ($Colorlist as $list) {

                                        $status = $list['status'];
                                        if ($status == 1) {
                                            $checked = 'checked';
                                        } else {
                                            $checked = '';
                                        }
                                        ?>
                                        <div class="col-sm-4">
                                            <!-- checkbox -->
                                            <div class="form-group">
                                                <label><input type="radio" name="color" value="<?php echo $list['id']; ?>" <?php echo $checked; ?>>
                                                    <span ><?php echo $list['color_name']; ?></span>
                                                </label>
                                            </div>
                                        </div> 

                                    <?php } ?>
                                </div> 


                                <div class="card-footer text-center">
                                    <button type="submit" class="btn btn-outline-primary" id="submitForm">Color Change</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
