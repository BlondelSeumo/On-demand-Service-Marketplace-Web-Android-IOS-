

<div class="page-wrapper">
    <div class="content container-fluid">
        <!-- Page Header -->
        <div class="page-header">
            <div class="row">
                <div class="col">
                    <h3 class="page-title">Paytab Gateway</h3>
					<?php
//echo "<pre>";print_r($list);exit;
?>
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
					
							<ul class="nav nav-tabs menu-tabs">
								<li class="nav-item ">
									<a class="nav-link" href="<?php echo base_url() . 'admin/stripe_payment_gateway'; ?>">Stripe</a>
								</li>
								<li class="nav-item ">
									<a class="nav-link" href="<?php echo base_url() . 'admin/razorpay_payment_gateway'; ?>">Razorpay </a>
								</li>
								<li class="nav-item">
									<a class="nav-link" href="<?php echo base_url() . 'admin/paypal_payment_gateway'; ?>">PayPal</a>
								</li>
								<li class="nav-item active">
									<a class="nav-link" href="<?php echo base_url() . 'admin/paytabs_payment_gateway'; ?>">PayTabs</a>
								</li>
							</ul>
							
							
                        <form action="<?php echo base_url() . 'admin/settings/paytab_edit/' . $list['id']; ?>" method="post">
                            <h4 class="text-primary">Paytab Sandbox</h4>
                            <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />

                            <!--<div class="form-group">
									<label>Paytab Gateway</label>
								<input type="radio" required="" class="paypal_payment" name="gateway_type"  value="sandbox" <?= ($list['gateway_type'] == "sandbox") ? 'checked' : '' ?>>
								Sandbox &nbsp;	
								<input type="radio" required="" class="paypal_payment" name="gateway_type"  value="live" <?= ($list['gateway_type'] == "live") ? 'checked' : '' ?>>
								Live
							</div>-->
							<div class="form-group">
									<label>Email ID</label>
								<input class="form-control" type="text" name="sandbox_email" id="sandbox_email" value="<?php if (!empty($list['sandbox_email'])) {
    echo $list['sandbox_email'];
} ?>" >
							</div>
							<div class="form-group">
									<label>Secret Key</label>
								<input class="form-control" type="text" name="sandbox_secretkey" id="sandbox_secretkey" value="<?php if (!empty($list['sandbox_secretkey'])) {
    echo $list['sandbox_secretkey'];
} ?>" >
							</div>
							
							 <h4 class="text-primary">Paytab Live</h4>
							 
							 <div class="form-group">
									<label>Email ID</label>
								<input class="form-control" type="text" name="email" id="email" value="<?php if (!empty($list['email'])) {
    echo $list['email'];
} ?>" >
							</div>
							<div class="form-group">
									<label>Secret Key</label>
								<input class="form-control" type="text" name="secretkey" id="secretkey" value="<?php if (!empty($list['secretkey'])) {
    echo $list['secretkey'];
} ?>" >
							</div>
							
                            <div class="mt-4">
<?php if ($user_role == 1) { ?>
                                    <button class="btn btn-primary" name="form_submit" value="submit" type="submit">Submit</button>
<?php } ?>

                                <a href="<?php echo base_url() . 'admin/paypal_payment_gateway' ?>" class="btn btn-link m-l-5">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
