<?php

if (!defined('BASEPATH'))
    exit('No direct script access allowed');

class Ajax extends CI_Controller {

    public function __construct() {

        parent::__construct();
        error_reporting(0);
        $this->load->model('Stripe_model');
    }

    public function index() {

        $version = 0;



        $query = $this->db->select('version')->order_by('version_id', 'desc')->get('version_updates');



        if ($query->num_rows() > 0) {



            $version = $query->row()->version;
        }

        if (isset($_REQUEST['timezone'])) {

            $array = array(
                'time_zone' => $_REQUEST['timezone'],
                'ip_city' => $_REQUEST['ip_city'],
                'version' => $version,
            );

            $this->check_updates();

            $this->session->set_userdata($array);

            echo json_encode($array);
        }
    }

    Public function check_updates() {

        $this->load->model('New_update_model', 'updates');

        $ch = curl_init();

        $options = array(
            CURLOPT_URL => 'https://www.dreamguys.co.in/gigs_updates/',
            CURLOPT_RETURNTRANSFER => true
        );



        if (!ini_get('safe_mode') && !ini_get('open_basedir')) {

            $options[CURLOPT_FOLLOWLOCATION] = true;
        }

        curl_setopt_array($ch, $options);

        $output = curl_exec($ch);

        curl_close($ch);



        $updates = json_decode($output, TRUE);

        $check_updates = $this->updates->check_updates($updates['build']);

        if ($check_updates == 0) {

            $this->session->set_userdata(array('updates' => 1));
        }
    }

    public function change_language() {



        $language = (!empty($this->input->post('lg'))) ? $this->input->post('lg') : 'en';

        $tag = (!empty($this->input->post('tag'))) ? $this->input->post('tag') : 'ltr';

        $lang_value = (!empty($this->input->post('lang_value'))) ? $this->input->post('lang_value') : 'English';

        $this->session->set_userdata(array('user_select_language' => $language));
        $this->session->set_userdata(array('user_lang_value' => $lang_value));

        $this->session->set_userdata(array('tag' => $tag));
    }

    public function change_email_language() {

        $query = $this->db->select('email_templates');
    }

    public function currency_rate() {
        $req_url = 'https://v6.exchangerate-api.com/v6/cc726126438b5513e5a41f69/latest/USD';
        $response_json = file_get_contents($req_url);

// Continuing if we got a result
        if (false !== $response_json) {

            // Try/catch for json_decode operation
            try {

                // Decoding
                $response = json_decode($response_json);

                // Check for success
                if ('success' === $response->result) {

                    foreach ($response->conversion_rates as $key => $value) {
                        $count = $this->db->where('currency_code', $key)->from('currency_rate')->count_all_results();
                        if ($count == 0) {
                            $data = array(
                                'currency_code' => $key,
                                'rate' => $value,
                                'created_at' => date('Y-m-d H:i:s')
                            );
                            $this->db->insert('currency_rate', $data);
                        } else {
                            $data = array(
                                'rate' => $value,
                                'updated_at' => date('Y-m-d H:i:s')
                            );
                            $this->db->where('currency_code', $key)->update('currency_rate', $data);
                        }
                    }
                    echo "success";
                    // YOUR APPLICATION CODE HERE, e.g.
                    // $base_price = 10; // Your price in USD
                    // $inr_price = round(($base_price * $response->conversion_rates->INR), 2);
                }
            } catch (Exception $e) {
                echo 'Caught exception: ', $e->getMessage();
            }
        }
    }

    public function add_user_currency() {
        $params = $this->input->post();
        $currrency = $params['code'];
        
        
        
        if (!empty($currrency)) {
           
            $user_id = $this->session->userdata('id');
            $token = $this->session->userdata('chat_token');
            $usertype = $this->session->userdata('usertype');
            
 
            if (!empty($usertype) && $usertype == 'user') {
                
                $user_wallet = $this->Stripe_model->get_wallet_new($token);
                $user_info = $this->Stripe_model->get_user_info($user_id);
                $wallet_history = $this->Stripe_model->get_wallet_history_info($token, $currrency);
            } else if (!empty($usertype) && $usertype == 'provider') {
                $user_wallet = $this->Stripe_model->get_wallet_pro($token);
                $user_info = $this->Stripe_model->get_provider($token);
                $wallet_history = $this->Stripe_model->get_wallet_history_info($token, $currrency);
            }
            $credit = $debit = 0;
            
            if (count($wallet_history) > 0) {
                foreach ($wallet_history as $key => $value) {

                    if ($value['credit_wallet'] != 0) {

                        $credit_amt = get_gigs_currency($value['credit_wallet'], $value['currency_code'], $currrency);
                        $credit += round($credit_amt, 2);
                    }
                    if ($value['debit_wallet'] != 0) {
                        $debit_amt = get_gigs_currency($value['debit_wallet'], $value['currency_code'], $currrency);
                        $debit += round($debit_amt, 2);
                        
                    }
                }
            }
//            $currency_rate = $credit - $debit;
            $currency_rate = get_gigs_currency($user_wallet['wallet_amt'], $user_wallet['currency_code'], $currrency);
            
            $this->db->where('token', $token)->update('wallet_table', ['currency_code' => $currrency, 'wallet_amt' => $currency_rate]);
           
            if($usertype =='user'){
                $result = $this->db->where('token', $token)->update('users', ['currency_code' => $currrency]);
            }else if($usertype =='provider'){
                $result = $this->db->where('token', $token)->update('providers', ['currency_code' => $currrency]);
            }
            
            if ($result == true) {
                echo json_encode(['success' => true]);
                
            } else {
                
                echo json_encode(['success' => false]);
                
            }
        }
    }

    public function currency_check() {
        $currency = get_gigs_currency(1, 'USD', 'INR');
        print_r($currency);
        exit;
    }

}

/* End of file  */

/* Location: ./application/controllers/ */