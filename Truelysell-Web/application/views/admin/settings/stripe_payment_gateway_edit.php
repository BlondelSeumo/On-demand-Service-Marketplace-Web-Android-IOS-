<div class="page-wrapper">
    <div class="content container-fluid">
        <!-- Page Header -->
        <div class="page-header">
            <div class="row">
                <div class="col">
                    <h3 class="page-title">Edit Stripe Gateway</h3>
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
            <li class="nav-item active">
                <a class="nav-link" href="<?php echo base_url() . 'admin/stripe_payment_gateway'; ?>">Payment Gateway</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<?php echo base_url() . 'admin/sms-settings'; ?>">SMS Gateway</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<?php echo base_url() . 'admin/theme-color'; ?>">Theme Color Change</a>
            </li>
        </ul>



        <div class="row">
            <div class="col-lg-8">
                <div class="card">
                    <div class="card-body">
                        <form action="<?php echo base_url() . 'admin/settings/edit/' . $list['id']; ?>" method="post">
                            <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />

                            <div class="form-group">
                                <label>Option</label>
                                <div>
                                    <?php if ($list['gateway_type'] == 'sandbox') { ?>
                                        <div class="radio radio-inline">
                                            <input id="sandbox" name="gateway_type" value="sandbox" type="radio" checked >
                                            <label for="sandbox"> Sandbox </label>
                                        </div>
                                    <?php } ?>	
                                    <?php if ($list['gateway_type'] == 'live') { ?>	
                                        <div class="radio radio-inline">
                                            <input id="livepaypal" name="gateway_type" value='live' type="radio"  checked>
                                            <label for="livepaypal"> Live </label>
                                        </div>
                                    <?php } ?>	
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Gateway Name</label>
                                <input  type="text" id="gateway_name" name="gateway_name"  value="<?php if (!empty($list['gateway_name'])) {
                                        echo $list['gateway_name'];
                                    } ?>" required class="form-control" placeholder="Gateway Name">
                            </div>
                            <div class="form-group">
                                <label>API Key</label>
                                <input type="text" id="api_key" name="api_key" value="<?php if (!empty($list['api_key'])) {
                                        echo $list['api_key'];
                                    } ?>" required class="form-control">
                            </div>
                            <div class="form-group">
                                <label>Rest Key</label>
                                <input type="text" id="value" name="value" value="<?php if (!empty($list['value'])) {
                                        echo $list['value'];
                                    } ?>" required class="form-control">
                            </div>
                            <div class="mt-4">
                                <button class="btn btn-primary" name="form_submit" value="submit" type="submit">Submit</button>
                                <a href="<?php echo base_url() . 'admin/stripe_payment_gateway' ?>" class="btn btn-link m-l-5">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>