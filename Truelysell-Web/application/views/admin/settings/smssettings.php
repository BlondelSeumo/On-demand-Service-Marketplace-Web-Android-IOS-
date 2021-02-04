<div class="page-wrapper">
    <div class="content container-fluid">

        <!-- Page Header -->
        <div class="page-header">
            <div class="row">
                <div class="col">
                    <h3 class="page-title">SMS Settings</h3>
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
            <li class="nav-item">
                <a class="nav-link" href="<?php echo base_url() . 'admin/stripe_payment_gateway'; ?>">Payment Gateway</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="<?php echo base_url() . 'admin/sms-settings'; ?>">SMS Gateway</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<?php echo base_url() . 'admin/theme-color'; ?>">Theme Color Change</a>
            </li>
        </ul>
        <form id="form_smssetting" action="" method="POST" enctype="multipart/form-data">
            <div class="row">

                <div class="col-xl-8 col-lg-12">
                    <div class="card">
                        <div class="card-body">
                            <div class="row align-items-center">
                                <div class="col">
                                    <h5>Default OTP</h5>
                                    <p class="mb-0">You can use default otp <strong>1234</strong> for demo purpose</p>
                                </div>
                                <div class="col-auto">
                                    <div class="status-toggle">
                                        <?php if ($user_role == 1) { ?>
                                            <input  id="default_otp" class="check" type="checkbox" name="default_otp" <?= ($default_otp == 1) ? 'checked' : ''; ?>>
                                        <?php } else { ?>
                                            <input  id="default_otp" class="check" type="checkbox" name="default_otp" <?= ($default_otp == 1) ? 'checked' : ''; ?> disabled>
                                        <?php } ?>
                                        <label for="default_otp" class="checktoggle">checkbox</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-body">
                            <h4 class="card-title">Nexmo</h4>


                            <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />

                            <div class="form-group">
                                <label>API Key</label>
                                <input type="text" class="form-control" name="sms_key" value="<?php if (isset($sms_key)) echo $sms_key; ?>">
                            </div>
                            <div class="form-group">
                                <label>API Secret Key</label>
                                <input type="text" class="form-control" name="sms_secret_key" value="<?php if (isset($sms_secret_key)) echo $sms_secret_key; ?>">
                            </div>
                            <div class="form-group">
                                <label>Sender ID</label>
                                <input type="text" class="form-control" name="sms_sender_id" value="<?php if (isset($sms_sender_id)) echo $sms_sender_id; ?>">
                            </div>
                            <div class="mt-4">
                                <?php if ($user_role == 1) { ?>
                                    <button name="form_submit" type="submit" class="btn btn-primary center-block" value="true">Save Changes</button>
                                <?php } ?>

                            </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
    </div>
</div>	