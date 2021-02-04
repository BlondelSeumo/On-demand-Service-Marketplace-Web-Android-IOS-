<div class="content">
    <div class="container">
        <div class="row">
            <?php $this->load->view('user/home/provider_sidemenu'); ?>
            <div class="col-xl-9 col-md-8">
                <div class="row">


                    <?php
                    
                    $user_currency_code = '';
                    $userId = $this->session->userdata('id');
                    If (!empty($userId)) {
                        $service_amount1 = $wallet['wallet_amt'];
                        $user_currency = get_provider_currency();
                        $user_currency_code = $user_currency['user_currency_code'];
                        $service_amount1 = get_gigs_currency($wallet['wallet_amt'], $wallet['currency_code'], $user_currency_code);
                    } else {
                        $user_currency_code = settings('currency');
                        $service_amount1 = $wallet['wallet_amt'];
                        
                    }
                    ?>

                    <div class="col-xl-6 col-lg-6 col-md-6">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">Wallet</h4>

                                <div class="wallet-details">
                                    <span>Wallet Balance</span>
                                    <h3><?php echo currency_conversion($user_currency_code) . $service_amount1; ?></h3>

                                    <div class="d-flex justify-content-between my-4">
                                        <?php
                                        $total_cr = 0;
                                        $total_dr = 0;
                                        if (!empty($wallet_history)) {
                                            foreach ($wallet_history as $key => $value) {
												$total_cr += get_gigs_currency($value['credit_wallet'], $value['currency_code'], $user_currency_code);
												$total_dr += abs(get_gigs_currency($value['debit_wallet'], $value['currency_code'], $user_currency_code));
                                            }
                                        }
                                        ?>
                                        <div>
                                            <p class="mb-1">Total Credit</p>
                                            <h4><?php echo currency_conversion($user_currency_code) . number_format($total_cr, 2); ?></h4>
                                        </div>
                                        <div>
                                            <p class="mb-1">Total Debit</p>
                                            <h4><?php echo currency_conversion($user_currency_code) . number_format($total_dr, 2); ?></h4>
                                        </div>
                                    </div>
                                    <div class="wallet-progress-chart">
                                         <div class="d-flex justify-content-between">
											<?php
                                            if (!empty($wallet['total_credit'])) {
                                                $wallet['total_credit'] = $total_cr;
												$wallet['total_debit'] = $total_dr;
                                            } else {
                                                $wallet['total_credit'] = 0;
												$wallet['total_debit'] = $total_dr;
                                            }
                                            ?>
                                            <span><?= $wallet['currency'] . '' . abs($wallet['total_debit']); ?></span>
                                            <span><?= $wallet['currency'] . '' . number_format($wallet['total_credit'], 2); ?></span>
                                        </div>

                                        <?php
                                        $total_per = 0;
                                        if (!empty($wallet['total_debit']) && !empty($wallet['total_credit'])) {
                                            $total_per = ($wallet['total_debit'] / $wallet['total_credit']) * 100;
                                        }
                                        ?>
                                        <div class="progress mt-1">
                                            <div class="progress-bar bg-theme" role="progressbar" aria-valuenow="41" aria-valuemin="0" aria-valuemax="100" style="width:<?= round($total_per); ?>%">
                                                <?= number_format(abs($total_per), 2); ?>%
                                            </div>
                                        </div>                                     
                                    </div> 
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xl-6 col-lg-6 col-md-6">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">Withdraw</h4>
                                <form action="#">
                                    <div class="form-group">
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <label class="input-group-text display-5"><?= currency_conversion($user_currency_code); ?></label>
                                            </div>
                                        <input type="hidden"  id="currency_val" name="currency_val" value="<?php echo $user_currency_code;?>">
										<input type="hidden" id="wallet_amount" value="<?php echo (int)$service_amount1; ?>">
                                            <input type="text" maxlength="10"  class="form-control isNumber" name="wallet_withdraw_amt" id="wallet_withdraw_amt" placeholder="00.00">
                                        </div>
                                    </div>
                                </form>
                                <div class="text-center mb-3">
                                    <h5 class="mb-3">OR</h5>                                       
                                    <ul class="list-inline mb-0">
                                        <li class="line-inline-item mb-0 d-inline-block">
                                            <a href="javascript:;" data-amount="50" class="updatebtn withdraw_wallet_value"><?= currency_conversion($user_currency_code); ?>50</a>
                                        </li>
                                        <li class="line-inline-item mb-0 d-inline-block">
                                            <a href="javascript:;" data-amount="100" class="updatebtn withdraw_wallet_value"><?= currency_conversion($user_currency_code); ?>100</a>
                                        </li>
                                        <li class="line-inline-item mb-0 d-inline-block">
                                            <a href="javascript:;" data-amount="150" class="updatebtn withdraw_wallet_value"><?= currency_conversion($user_currency_code); ?>150</a>
                                        </li>
                                    </ul>
                                </div>
								
								<form action="<?= base_url() ?>user/wallet/payment" method="post" id="paypal_payment">
									<div class="form-group">
										<div id="payment-method">
											<div class="row">
												<div class="col-4">
													<span>
													<input type="radio" id="gigs_payment_radio3" name="group2" value="Direct" checked>
													<label for="gigs_payment_radio3"> <img src="<?php echo base_url(); ?>assets/img/paypal.png" alt="stripe"></label>
												</span>
												</div>
												<div class="col-4">
													<span>
													<input type="radio" id="gigs_payment_radio6" name="group2" value="RazorPay">
													<label for="gigs_payment_radio6"> <img src="<?php echo base_url(); ?>assets/img/razorpay.png" alt="RazorPay"></label>
													</span>
												</div>
												
												<div class="col-4">
													<span>
													<input type="radio" id="gigs_payment_radio5" name="group2" value="stripe" >
													<label for="gigs_payment_radio5"> <img src="<?php echo base_url(); ?>assets/img/stripe.png" alt="stripe"></label>
													</span>
												</div>
												
												<!--<div class="col-3">
													<input class="form-check-input" type="radio" name="payment_type" id="paytabs"  value="paytabs">
													<img src="<?= base_url() . "assets/img/paytabs.png"; ?>">
												</div>-->
											</div>
											<!--<div class="gigs-form-01">
												
												<span>
													<input type="radio" id="gigs_payment_radio3" name="group2" value="Direct" checked>
													<label for="gigs_payment_radio3"> <img src="<?php echo base_url(); ?>assets/img/paypal.png" alt="stripe"></label>
												</span>
												
												<span>
													<input type="radio" id="gigs_payment_radio5" name="group2" value="stripe" >
													<label for="gigs_payment_radio5"> <img src="<?php echo base_url(); ?>assets/img/stripe.png" alt="stripe"></label>
												</span>
												
												<span>
													<input type="radio" id="gigs_payment_radio6" name="group2" value="RazorPay">
													<label for="gigs_payment_radio6"> <img src="<?php echo base_url(); ?>assets/img/razorpay.png" alt="RazorPay"></label>
												</span>
											
											</div>-->
										</div>
										<div class="input-group mb-3">
										</div>
										<!--<input type="text" maxlength="4" class="form-control isNumber" name="wallet_amt" id="wallet_amt" placeholder="<?= $rate_symbol; ?> 00.00">-->
									</div>
								</form>
                                <a href="javascript:void(0);" id="stripe_withdraw_wallet" class="btn btn-primary btn-block withdraw-btn">Withdraw</a>
                                <div id="card_form_div" >
                                    <div class="payment-card">
                                        <span>Withdraw Amount is</span>
                                        <h3 class="mb-3"><?php echo currency_conversion($user_currency_code); ?><span id="remember_withdraw_wallet"></span></h3>
                                        <h5 class="mb-3">Please Fill Debit Card Detail</h5>
                                        <div id="card-element">
                                            <!-- Stripe Element will be inserted here -->
                                        </div>

                                        <!-- Used to display form errors. -->
                                        <div id="card-errors" role="alert"></div>
                                        <div class="text-center"><div id="load_div"></div></div>
                                        <hr>
                                        <div class="text-center">
                                            <button class="btn btn-success" id="pay_btn">Withdraw</button>
                                            <button class="btn btn-secondary" id="cancel_card_btn">Cancel</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <h4 class="mb-4">Recent Transactions</h4>
                <div class="card transaction-table mb-0">
                    <div class="card-body">
                        <div class="table-responsive">
                            <?php if (!empty($wallet_history)) { ?>
                                <table id="order-summary" class="table table-center mb-0">
                                <?php } else { ?>
                                    <table class="table table-center mb-0">
                                    <?php } ?>
                                    <thead>
                                        <tr>
                                            <th>S.No</th>
                                            <th>Date</th>
                                            <th>Wallet</th>
                                            <th>Credit</th>
                                            <th>Debit</th>
                                            <th>Available</th>
                                            <th>Reason</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <?php
                                        $total_cr = 0;
                                        $total_dr = 0;
                                        if (!empty($wallet_history)) {

                                            foreach ($wallet_history as $key => $value) {
                                                if (!empty($value['credit_wallet'])) {
                                                    $color = 'success';
                                                    $message = 'Credit';
                                                } else {
                                                    $color = 'danger';
                                                    $message = 'Debit';
                                                }
                                                $total_cr += (int) $value['credit_wallet'];
                                                $total_dr += (int) abs($value['debit_wallet']);

                                                $user_currency_code = '';
                                                $userId = $this->session->userdata('id');
                                                $user_details = $this->db->where('id', $userId)->get('providers')->row_array();
                                                If (!empty($userId)) {
                                                    $service_amount1 = $value["current_wallet"];
                                                    $service_amount2 = $value["credit_wallet"];
                                                    $service_amount3 = $value["debit_wallet"];
                                                    $service_amount4 = $txt_amt;
                                                    $service_amount5 = $value["avail_wallet"];
                                                    $get_currency = get_currency();
                                                    $user_currency = get_provider_currency();
                                                    $user_currency_code = $user_currency['user_currency_code'];

                                                    $service_amount1 = get_gigs_currency($value["current_wallet"], $value["currency_code"], $user_details['currency_code']);
                                                    $service_amount2 = get_gigs_currency($value["credit_wallet"], $value["currency_code"], $user_details['currency_code']);
                                                    $service_amount3 = get_gigs_currency($value["debit_wallet"], $value["currency_code"], $user_details['currency_code']);
                                                    $service_amount4 = get_gigs_currency($txt_amt, $value["currency_code"], $user_details['currency_code']);
                                                    $service_amount5 = get_gigs_currency($value["avail_wallet"], $value["currency_code"], $user_details['currency_code']);
                                                } else {
                                                    $user_currency_code = settings('currency');
                                                    $service_amount1 = $value["current_wallet"];
                                                    $service_amount2 = $value["credit_wallet"];
                                                    $service_amount3 = $value["debit_wallet"];
                                                    $service_amount4 = $txt_amt;
                                                    $service_amount5 = $value["avail_wallet"];
                                                }




                                                echo '<tr>
									<td>' . ($key + 1) . '</td>
									<td>' . date("d M Y H:i:s", strtotime($value["created_at"])) . '</td>
									<td>' . currency_conversion($user_currency_code) . '' . $service_amount1 . '</td>
									<td>' . currency_conversion($user_currency_code) . '' . $service_amount2 . '</td>
									<td>' . currency_conversion($user_currency_code) . '' . $service_amount3 . '</td>
									<td>' . currency_conversion($user_currency_code) . '' . $service_amount5 . '</td>
									<td><lable>' . $value["reason"] . '</lable></td>
									<td><span class="badge bg-' . $color . '-light">' . $message . '</span></td> 
									</tr>';
                                            }
                                        } else {
                                            echo '<tr> <td colspan="8"> <div class="text-center">No data found</div></td> </tr>';
                                        }
                                        ?>
                                    </tbody>
                                </table>
                        </div>
                    </div>
                </div>			

            </div>
        </div>

    </div>


    <?php
    $query = $this->db->query("select * from system_settings WHERE status = 1");
    $result = $query->result_array();
    $stripe_option = '1';
    $publishable_key = '';
    $live_publishable_key = '';
    $logo_front = '';
    foreach ($result as $res) {
        if ($res['key'] == 'stripe_option') {
            $stripe_option = $res['value'];
        }
        if ($res['key'] == 'publishable_key') {
            $publishable_key = $res['value'];
        }
        if ($res['key'] == 'live_publishable_key') {
            $live_publishable_key = $res['value'];
        }

        if ($res['key'] == 'logo_front') {
            $logo_front = $res['value'];
        }
    }

    if ($stripe_option == 1) {
        $stripe_key = $publishable_key;
    } else {
        $stripe_key = $live_publishable_key;
    }

    if (!empty($logo_front)) {
        $web_log = base_url() . $logo_front;
    } else {
        $web_log = base_url() . 'assets/img/logo.png';
    }
    ?>

    <input type="hidden" id="stripe_key" value="<?= $stripe_key; ?>">
    <input type="hidden" id="logo_front" value="<?= $web_log; ?>">
    <input type="hidden" id="token" value="<?= $this->session->userdata('chat_token'); ?>">



  <!--- Withdraw details modal--->
    <div class="modal" id="withdraw_modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h2 class="text-center"><?php echo (!empty($user_language[$user_selected]['lg_withdraw_amount'])) ? $user_language[$user_selected]['lg_withdraw_amount'] : $default_language['en']['lg_withdraw_amount']; ?></h2>
                <div class="modal-body">
                    <form id="bank_details" method="post" action="#">
                        <div class="paypal_details">
                            <div class="form-group">
                                <label><?php echo (!empty($user_language[$user_selected]['lg_paypal_id'])) ? $user_language[$user_selected]['lg_paypal_id'] : $default_language['en']['lg_paypal_id']; ?></label>
                                <input class="form-control" type="text" name="paypal_id" value="<?= (!empty($bank_account['paypal_account'])) ? $bank_account['paypal_account'] : ''; ?>" id="paypal_id">
                                <span class="paypal_id_error"></span>
                            </div>
                            <div class="form-group">
                                <label><?php echo (!empty($user_language[$user_selected]['lg_paypal_email_id'])) ? $user_language[$user_selected]['lg_paypal_email_id'] : $default_language['en']['lg_paypal_email_id']; ?></label>
                                <input class="form-control" type="text" name="paypal_email_id" value="<?= (!empty($bank_account['paypal_email_id'])) ? $bank_account['paypal_email_id'] : ''; ?>" id="paypal_email_id">
                                <span class="paypal_email_id_error"></span>
                            </div>
                        </div>
                        <div class="bank_details">
                            <div class="form-group">
                                <label>
                                   Bank Name
                                </label>
                                <input class="form-control" type="text" name="bank_name" value="<?= (!empty($bank_account['bank_name'])) ? $bank_account['bank_name'] : ''; ?>">
                            </div>
                            <div class="form-group">
                                <label>Bank Address</label>
                                <input class="form-control" type="text" name="bank_address" value="<?= (!empty($bank_account['bank_address'])) ? $bank_account['bank_address'] : ''; ?>">
                            </div>
                            <div class="form-group">
                                <label>Account No</label>
                                <input class="form-control" type="text" name="account_no" value="<?= (!empty($bank_account['account_number'])) ? $bank_account['account_number'] : ''; ?>" id="account_no">
                                <span class="account_no_error"></span>
                            </div>
                            <div class="form-group">
                                <label>IFSC Code</label>
                                <input class="form-control" type="text" name="ifsc_code" value="<?= (!empty($bank_account['account_ifsc'])) ? $bank_account['account_ifsc'] : ''; ?>">
                            </div>
                            <div class="form-group">
                                <label>Sort Code</label>
                                <input class="form-control" type="text" name="sort_code" value="<?= (!empty($bank_account['sort_code'])) ? $bank_account['sort_code'] : ''; ?>">
                            </div>
                            <div class="form-group">
                                <label>Routing No</label>
                                <input class="form-control" type="text" name="routing_number" value="<?= (!empty($bank_account['routing_number'])) ? $bank_account['routing_number'] : ''; ?>">
                            </div>
                            <div class="form-group">
                                <label>Pan No</label>
                                <input class="form-control" type="text" name="pancard_no" value="<?= (!empty($bank_account['pancard_no'])) ? $bank_account['pancard_no'] : ''; ?>">
                            </div>
                        </div>
						<div class="razorpay_details">
                            <div class="form-group">
                                <label>
                                   Name
							   </label>
                                <input class="form-control" type="text" name="name" value="<?= (!empty($bank_account['name'])) ? $bank_account['name'] : ''; ?>">
                            </div>
                            <div class="form-group">
                                <label>
                                 Email ID
								 </label>
                                <input class="form-control" type="text" name="email" value="<?= (!empty($bank_account['email'])) ? $bank_account['email'] : ''; ?>">
                            </div>
							<div class="form-group">
                                <label>
                                    Contact No
                                </label>
                                <input class="form-control" type="text" name="contact" value="<?= (!empty($bank_account['contact'])) ? $bank_account['contact'] : ''; ?>">
                            </div>
							
							<div class="form-group">
                                <label>
                                  Card No
                                </label>
                                <input class="form-control" type="text" name="cardno" value="<?= (!empty($bank_account['cardno'])) ? $bank_account['cardno'] : ''; ?>">
                            </div>
							<div class="form-group">
                                <label>
                                  Card Name
                                </label>
                                <input class="form-control" type="text" name="cardname" value="<?= (!empty($bank_account['cardname'])) ? $bank_account['cardname'] : ''; ?>">
                            </div>	
							<div class="form-group">
                                <label>
                                   Bank Name
                                </label>
                                <input class="form-control" type="text" name="bank_name" value="">
                            </div>	
							<div class="form-group">
                                <label>
                                   IFSC Code
                                </label>
                                <input class="form-control" type="text" name="ifsc" value="<?= (!empty($bank_account['ifsc'])) ? $bank_account['ifsc'] : ''; ?>">
                            </div>
							<div class="form-group">
                                <label>
                                   Account No
                                </label>
                                <input class="form-control" type="text" name="accountnumber" value="<?= (!empty($bank_account['accountnumber'])) ? $bank_account['accountnumber'] : ''; ?>">
                            </div>
							<div class="form-group">
                                <label>
                                  Payment Mode
                                </label>
                                <select class="form-control" name="mode">
									<option value="">Select Payment Mode</option>
									<option value="NEFT">NEFT</option>
                                    <option value="RTGS">RTGS</option>
                                    <option value="IMPS">IMPS</option>
                                    <option value="UPI">UPI</option>
								</select>
                            </div>
							<div class="form-group">
                                <label>
                                   Payment Purpose
                                </label>
                                <select class="form-control" name="purpose">
									<option value="">Select Payment Purpose</option>
                                    <option value="refund">refund</option>
                                    <option value="cashback">cashback</option>
                                    <option value="payout" selected="">payout</option>
								</select>
                            </div>
                        </div>
                        <input type="hidden" name="amount" id="stripe_amount">
                        <input type="hidden" name="payment_type" id="payment_types">
                        <!--<input type="hidden" id="wallet_amount" value="<?php echo (int)$total_amount; ?>">-->
                        <button type="submit" class="btn btn-primary btn-block withdraw-btn1">Save changes</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

