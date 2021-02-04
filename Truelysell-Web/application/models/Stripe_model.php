<?php

class Stripe_model extends CI_Model {
    
    
    public function get_provider($id) {
        $user_table = $this->db->select('*')->
                        from('providers')->
                        where('token', $id)->
                        get()->row();
        return $user_table;
    }
    
    
    public function get_wallet_pro($id) {
        $user_currency = '';

        if (!empty($id)) {
            $user_currency = get_provider_currency_code($id);
        }
        $val = $this->db->select('*')->from('wallet_table')->where('token', $id)->get()->row();
        $wallet = [];
        $setting_currency = '';
        $user_info = $this->get_provider($id);

        $UserId = $user_info->id;

        $query = $this->db->query("select * from system_settings WHERE status = 1");
        $result = $query->result_array();
        if (!empty($result)) {
            foreach ($result as $data) {
                if ($data['key'] == 'currency_option') {
                    $setting_currency = $data['value'];
                }
            }
        }

        /* sum of totAL wallet */

        $wallet_tot = $this->db->select('sum(credit_wallet)as total_credit,sum(debit_wallet)as total_debit')->from('wallet_transaction_history')->
                        where('token', $id)->order_by('id', 'DESC')->
                        get()->row_array();

        if (!empty($val)) {

            $wallet['id'] = $val->id;
            $wallet['token'] = $val->token;
            $wallet['type'] = $val->type;
            $wallet['wallet_amt'] = strval(abs($val->wallet_amt));
            if (!empty($user_info->currency_code)) {
                $wallet['currency'] = currency_conversion($user_info->currency_code);
                $wallet['currency_code'] = $user_info->currency_code;
            } else {
                $wallet['currency'] = currency_conversion(settings('currency'));
                $wallet['currency_code'] = $setting_currency;
            }

            $wallet['total_credit'] = (!empty($wallet_tot['total_credit'])) ? strval($wallet_tot['total_credit']) : 0;
            $wallet['total_debit'] = (!empty($wallet_tot['total_debit'])) ? strval($wallet_tot['total_debit']) : 0;
        }
        $bank_check = 0;
        if (!empty($id)) {

            $bank_check = $this->db->where('user_id', $UserId)->get('stripe_bank_details')->num_rows();
            if ($bank_check > 0) {
                $bank_check = 1;
            }
        }
        if (!empty($wallet)) {
            $wallet['bank_details'] = (string) $bank_check;
            return $wallet;
        } else {
            $wallet['id'] = '';
            $wallet['token'] = $user_info->unique_code;
            $wallet['type'] = '';
            $wallet['wallet_amt'] = '0';
            if (!empty($user_info->currency_code)) {
                $wallet['currency'] = currency_conversion($user_info->currency_code);
                $wallet['currency_code'] = $user_info->currency_code;
            } else {
                $wallet['currency'] = currency_conversion(settings('currency'));
                $wallet['currency_code'] = $setting_currency;
            }
            $wallet['total_credit'] = '0';
            $wallet['total_debit'] = '0';
            $wallet['bank_details'] = (string) $bank_check;
            return $wallet;
        }
    }


    public function get_user($id) {
        $user_table = $this->db->select('*')->
                        from('users')->
                        where('token', $id)->
                        get()->row();
        return $user_table;
    }

    public function get_wallet_new($id) {
        $user_currency = '';

        if (!empty($id)) {
            $user_currency = get_user_currency_code($id);
        }
        $val = $this->db->select('*')->from('wallet_table')->where('token', $id)->get()->row();
        $wallet = [];
        $setting_currency = '';
        $user_info = $this->get_user($id);

        $UserId = $user_info->id;

        $query = $this->db->query("select * from system_settings WHERE status = 1");
        $result = $query->result_array();
        if (!empty($result)) {
            foreach ($result as $data) {
                if ($data['key'] == 'currency_option') {
                    $setting_currency = $data['value'];
                }
            }
        }

        /* sum of totAL wallet */

        $wallet_tot = $this->db->select('sum(credit_wallet)as total_credit,sum(debit_wallet)as total_debit')->from('wallet_transaction_history')->
                        where('token', $id)->order_by('id', 'DESC')->
                        get()->row_array();

        if (!empty($val)) {

            $wallet['id'] = $val->id;
            $wallet['token'] = $val->token;
            $wallet['type'] = $val->type;
            $wallet['wallet_amt'] = strval(abs($val->wallet_amt));
            if (!empty($user_info->currency_code)) {
                $wallet['currency'] = currency_conversion($user_info->currency_code);
                $wallet['currency_code'] = $user_info->currency_code;
            } else {
                $wallet['currency'] = currency_conversion(settings('currency'));
                $wallet['currency_code'] = $setting_currency;
            }

            $wallet['total_credit'] = (!empty($wallet_tot['total_credit'])) ? strval($wallet_tot['total_credit']) : 0;
            $wallet['total_debit'] = (!empty($wallet_tot['total_debit'])) ? strval($wallet_tot['total_debit']) : 0;
        }
        $bank_check = 0;
        if (!empty($id)) {

            $bank_check = $this->db->where('user_id', $UserId)->get('stripe_bank_details')->num_rows();
            if ($bank_check > 0) {
                $bank_check = 1;
            }
        }
        if (!empty($wallet)) {
            $wallet['bank_details'] = (string) $bank_check;
            return $wallet;
        } else {
            $wallet['id'] = '';
            $wallet['token'] = $user_info->unique_code;
            $wallet['type'] = '';
            $wallet['wallet_amt'] = '0';
            if (!empty($user_info->currency_code)) {
                $wallet['currency'] = currency_conversion($user_info->currency_code);
                $wallet['currency_code'] = $user_info->currency_code;
            } else {
                $wallet['currency'] = currency_conversion(settings('currency'));
                $wallet['currency_code'] = $setting_currency;
            }
            $wallet['total_credit'] = '0';
            $wallet['total_debit'] = '0';
            $wallet['bank_details'] = (string) $bank_check;
            return $wallet;
        }
    }

    /* wallet information */

    public function get_wallet($id) {
        $user_currency = '';

        if (!empty($id)) {
            $user_currency = get_api_user_currency($id);
        }
        $val = $this->db->select('id,token,wallet_amt,type')->from('wallet_table')->where('user_provider_id', $id)->get()->row();
        $wallet = [];
        $setting_currency = '';
        $user_info = $this->get_user_info($id);

        $query = $this->db->query("select * from system_settings WHERE status = 1");
        $result = $query->result_array();
        if (!empty($result)) {
            foreach ($result as $data) {
                if ($data['key'] == 'currency_option') {
                    $setting_currency = $data['value'];
                }
            }
        }

        /* sum of totAL wallet */

        $wallet_tot = $this->db->select('sum(credit_wallet)as total_credit,sum(debit_wallet)as total_debit')->from('wallet_transaction_history')->
                        where('user_provider_id', $id)->order_by('id', 'DESC')->
                        get()->row_array();

        if (!empty($val)) {

            $wallet['id'] = $val->id;
            $wallet['token'] = $val->token;
            $wallet['type'] = $val->type;
            $wallet['wallet_amt'] = strval(abs($val->wallet_amt));
            if (!empty($user_info->currency_code)) {
                $wallet['currency'] = currency_conversion($user_info->currency_code);
                $wallet['currency_code'] = $user_info->currency_code;
            } else {
                $wallet['currency'] = currency_conversion(settings('currency'));
                $wallet['currency_code'] = $setting_currency;
            }

            $wallet['total_credit'] = (!empty($wallet_tot['total_credit'])) ? strval($wallet_tot['total_credit']) : 0;
            $wallet['total_debit'] = (!empty($wallet_tot['total_debit'])) ? strval($wallet_tot['total_debit']) : 0;
        }
        $bank_check = 0;
        if (!empty($id)) {

            $bank_check = $this->db->where('user_id', $id)->get('stripe_bank_details')->num_rows();
            if ($bank_check > 0) {
                $bank_check = 1;
            }
        }
        if (!empty($wallet)) {
            $wallet['bank_details'] = (string) $bank_check;
            return $wallet;
        } else {
            $wallet['id'] = '';
            $wallet['token'] = $user_info->unique_code;
            $wallet['type'] = '';
            $wallet['wallet_amt'] = '0';
            if (!empty($user_info->currency_code)) {
                $wallet['currency'] = currency_conversion($user_info->currency_code);
                $wallet['currency_code'] = $user_info->currency_code;
            } else {
                $wallet['currency'] = currency_conversion(settings('currency'));
                $wallet['currency_code'] = $setting_currency;
            }
            $wallet['total_credit'] = '0';
            $wallet['total_debit'] = '0';
            $wallet['bank_details'] = (string) $bank_check;
            return $wallet;
        }
    }

    /* wallet update */

    public function get_wallet_info($user_id) {
        return $this->db->where('user_provider_id', $user_id)->from('wallet_table')->count_all_results();
    }

    public function insert_wallet_table($user_id) {
        $this->db->insert('wallet_table', ['token' => '', 'user_provider_id' => $user_id, 'type' => 1, 'reason' => 'user_wallet', 'wallet_amt' => 0, 'created_at' => date('Y-m-d H:i:s')]);
    }

    public function update_wallet($inputs, $where) {

        $this->db->set($inputs);
        $this->db->where($where);
        $this->db->update('wallet_table');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    public function update_customer_card($inputs, $where) {

        $this->db->set($inputs);
        $this->db->where($where);
        $this->db->update('stripe_customer_card_details');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    /* get information base on token */

    public function get_token_info($token) {

        $user_table = $this->db->select('*')->
                        from('members')->
                        where('unique_code', $token)->
                        get()->row();
        return $user_table;
    }

    public function get_wallet_history_info($id,$params='') {
        
        
        
        if($params !=''){
      //  $update=$this->db->where('token', $id)->update('wallet_transaction_history', ['currency_code' => $params]);
        }
        $wallet = $this->db->select('*')->from('wallet_transaction_history')->
                        where('token', $id)->order_by('id', 'DESC')->
                        get()->result_array();
        if ($wallet) {
            return $wallet;
        }
    }

    /* get information base on id */

    public function get_user_info($id) {
        $user_table = $this->db->select('*')->
                        from('users')->
                        where('id', $id)->
                        get()->row();
        return $user_table;
    }

    /* get payment base on id */

    public function get_provider_info($id) {
        $user_table = $this->db->select('*')->
                        from('providers')->
                        where('id', $id)->
                        get()->row();
        return $user_table;
    }

    public function get_payment_info($id) {
        $user_table = $this->db->select('*')->
                        from('payments')->
                        where('id', $id)->
                        get()->row_array();
        return $user_table;
    }

    /* User wallet flow and history */

    public function user_wallet_history_flow($user_id, $txn = NULL, $amt, $comission_amt = NULL) {
        if (!empty($user_id)) {
            $user_info = $this->get_user_info($user_id);
            $provider = $this->get_user_info($user_id);

            $wallet = $this->get_wallet($user_id);

            $curren_wallet = $wallet['wallet_amt'];

            /* wallet infos */

            $history_pay['token'] = $provider->unique_code;
            $history_pay['currency_code'] = $provider->currency_code;
            $history_pay['user_provider_id'] = $provider->USERID;

            $history_pay['tokenid'] = '';
            $history_pay['payment_detail'] = json_encode($booking); //response
            $history_pay['charge_id'] = $provider->USERID;
            $history_pay['txn_id'] = $txn;
            $history_pay['transaction_id'] = '';

            $history_pay['exchange_rate'] = 0;
            $history_pay['paid_status'] = '1';
            $history_pay['cust_id'] = $provider->USERID;
            $history_pay['card_id'] = 'Self';
            $history_pay['total_amt'] = $amt * 100;

            $history_pay['fee_amt'] = 0;
            if (!empty($comission_amt)) {
                $comission_amt = round($comission_amt, 2) * 100;
                $history_pay['fee_amt'] = $comission_amt;
            }
            $history_pay['net_amt'] = $amt * 100;
            $history_pay['amount_refund'] = 0;
            $history_pay['current_wallet'] = $curren_wallet;
            $history_pay['credit_wallet'] = ($amt);
            $history_pay['debit_wallet'] = 0;
            $history_pay['avail_wallet'] = ($amt) + $curren_wallet;
            $history_pay['reason'] = 'Add Wallet Amt';
            $history_pay['created_at'] = date('Y-m-d H:i:s');

            if ($this->db->insert('wallet_transaction_history', $history_pay)) {
                /* update wallet table */
                $wallet_data['wallet_amt'] = ($amt) + $curren_wallet;
                $wallet_data['updated_on'] = date('Y-m-d H:i:s');
                $WHERE = array('user_provider_id' => $user_id);
                $result = $this->update_wallet($wallet_data, $WHERE);
                /* payment on stripe */
                return $result;
            }
        }
    }

//Buying gig wallet amt
    public function buying_wallet_history_flow($payment_id, $user_id) {
        if (!empty($payment_id)) {

            $booking = $this->get_payment_info($payment_id);

            if (!empty($booking)) {

                $user_info = $this->get_user_info($user_id);
                $booking['gig_price'] = get_gigs_currency($booking['gig_price'], $booking['currency_type'], $user_info->currency_code);
                $token = $user_info->unique_code;

                $wallet = $this->get_wallet($user_id);

                $curren_wallet = $wallet['wallet_amt'];

                /* wallet infos */

                $history_pay['token'] = $token;
                $history_pay['currency_code'] = $user_info->currency_code;
                $history_pay['user_provider_id'] = $user_info->USERID;
                $history_pay['type'] = '';
                $history_pay['tokenid'] = $payment_id;
                $history_pay['payment_detail'] = json_encode($booking); //response
                $history_pay['charge_id'] = $booking['USERID'];
                $history_pay['transaction_id'] = $payment_id;
                $history_pay['exchange_rate'] = 0;
                $history_pay['paid_status'] = 'pass';
                $history_pay['cust_id'] = 'Self';
                $history_pay['card_id'] = 'Self';
                $history_pay['total_amt'] = $booking['gig_price'] * 100;
                $history_pay['fee_amt'] = 0;
                $history_pay['net_amt'] = $booking['gig_price'] * 100;
                $history_pay['amount_refund'] = 0;
                $history_pay['current_wallet'] = $curren_wallet;
                $history_pay['credit_wallet'] = 0;
                $history_pay['debit_wallet'] = ($booking['gig_price']);
                $history_pay['avail_wallet'] = $curren_wallet - ($booking['gig_price']);
                $history_pay['reason'] = 'Buying gigs';
                $history_pay['created_at'] = date('Y-m-d H:i:s');

                if ($this->db->insert('wallet_transaction_history', $history_pay)) {
                    /* update wallet table */
                    $wallet_data['wallet_amt'] = $curren_wallet - ($booking['gig_price']);
                    $wallet_data['updated_on'] = date('Y-m-d H:i:s');
                    $WHERE = array('user_provider_id' => $user_id);
                    $result = $this->update_wallet($wallet_data, $WHERE);
                    /* payment on stripe */

                    return $result;
                }
            }
        }
    }

    //Buying gig wallet amt function end
    //PAYPAL AMOUTN WITHDRAWAL
    public function wallet_withdraw_history_flow($amount, $currency, $user_id, $batch_id) {

        if (!empty($amount)) {
            $booking['gig_price'] = $amount;
            $user_info = $this->get_user_info($user_id);

            $booking['gig_price'] = get_gigs_currency($booking['gig_price'], $currency, $user_info->currency_code);
            $token = $user_info->unique_code;

            $wallet = $this->get_wallet($user_id);

            $curren_wallet = $wallet['wallet_amt'];

            /* wallet infos */

            $history_pay['token'] = $token;
            $history_pay['currency_code'] = $user_info->currency_code;
            $history_pay['user_provider_id'] = $user_info->USERID;
            $history_pay['type'] = '';
            $history_pay['tokenid'] = $user_id;
            $history_pay['payment_detail'] = json_encode($booking); //response
            $history_pay['charge_id'] = $user_id;
            $history_pay['transaction_id'] = $batch_id;
            $history_pay['exchange_rate'] = 0;
            $history_pay['paid_status'] = 'pass';
            $history_pay['cust_id'] = 'Self';
            $history_pay['card_id'] = 'Self';
            $history_pay['total_amt'] = $booking['gig_price'] * 100;
            $history_pay['fee_amt'] = 0;
            $history_pay['net_amt'] = $booking['gig_price'] * 100;
            $history_pay['amount_refund'] = 0;
            $history_pay['current_wallet'] = $curren_wallet;
            $history_pay['credit_wallet'] = 0;
            $history_pay['debit_wallet'] = ($booking['gig_price']);
            $history_pay['avail_wallet'] = $curren_wallet - ($booking['gig_price']);
            $history_pay['reason'] = 'WITHDRAWAL';
            $history_pay['created_at'] = date('Y-m-d H:i:s');

            if ($this->db->insert('wallet_transaction_history', $history_pay)) {
                /* update wallet table */
                $wallet_data['wallet_amt'] = $curren_wallet - ($booking['gig_price']);

                $wallet_data['updated_on'] = date('Y-m-d H:i:s');
                $WHERE = array('user_provider_id' => $user_id);
                $result = $this->update_wallet($wallet_data, $WHERE);
                /* payment on stripe */
                return $result;
            }
        }
    }

    //PAYPAL AMOUNT WITHDRAWAL END
    //Wallet withdraw
    public function wallet_withdraw_flow($amount, $currency, $user_id, $batch_id) {

        if (!empty($amount)) {
            $booking['gig_price'] = $amount;
            $user_info = $this->get_provider_info($user_id);

            $booking['gig_price'] = get_gigs_currency($booking['gig_price'], $currency, $user_info->currency_code);
            $token = $user_info->token;

            $wallet = $this->get_wallet_pro($token);
            $curren_wallet = $wallet['wallet_amt'];

            /* wallet infos */

            $history_pay['token'] = $token;
            $history_pay['currency_code'] = $user_info->currency_code;
            $history_pay['user_provider_id'] = $user_info->id;
            $history_pay['type'] = '';
            $history_pay['tokenid'] = $user_id;
            $history_pay['payment_detail'] = json_encode($booking); //response
            $history_pay['charge_id'] = $user_id;
            $history_pay['transaction_id'] = $batch_id;
            $history_pay['exchange_rate'] = 0;
            $history_pay['paid_status'] = 'pass';
            $history_pay['cust_id'] = 'Self';
            $history_pay['card_id'] = 'Self';
            $history_pay['total_amt'] = $booking['gig_price'] * 100;
            $history_pay['fee_amt'] = 0;
            $history_pay['net_amt'] = $booking['gig_price'] * 100;
            $history_pay['amount_refund'] = 0;
            $history_pay['current_wallet'] = $curren_wallet;
            $history_pay['credit_wallet'] = 0;
            $history_pay['debit_wallet'] = ($booking['gig_price']);
            $history_pay['avail_wallet'] = $curren_wallet - ($booking['gig_price']);
            $history_pay['reason'] = 'WITHDRAW REQUEST AMOUNT';
            $history_pay['created_at'] = date('Y-m-d H:i:s');
//            print_r($history_pay);exit;
            if ($this->db->insert('wallet_transaction_history', $history_pay)) {
                /* update wallet table */
                $wallet_amt = $curren_wallet - ($booking['gig_price']);
                $wallet_data['wallet_amt']=get_gigs_currency($wallet_amt,$history_pay['currency_code'],$user_info->currency_code);
                $wallet_data['updated_on'] = date('Y-m-d H:i:s');
                $WHERE = array('token' => $token);
                $result = $this->update_wallet($wallet_data, $WHERE);
                /* payment on stripe */
                return $result;
            }
        }
    }

    //wallet withdraw end
}

?>