<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Settings extends CI_Controller {

    public $data;

    public function __construct() {

        parent::__construct();
        error_reporting(0);
        $this->load->model('admin_model', 'admin');
		$this->load->model('common_model','common_model');
        $this->data['theme'] = 'admin';
        $this->data['model'] = 'settings';
        $this->data['base_url'] = base_url();
        $this->load->helper('user_timezone');
        $this->data['user_role'] = $this->session->userdata('role');
        $this->data['csrf'] = array(
            'name' => $this->security->get_csrf_token_name(),
            'hash' => $this->security->get_csrf_hash()
        );
        $this->load->helper('ckeditor');
		$this->data['ckeditor_editor1'] = array(
			'id'   => 'ck_editor_textarea_id',
			'path' => 'assets/js/ckeditor',
			'config' => array(
				'toolbar' 					=> "Full",
				'filebrowserBrowseUrl'      => base_url() . 'assets/js/ckfinder/ckfinder.html',
				'filebrowserImageBrowseUrl' => base_url() . 'assets/js/ckfinder/ckfinder.html?Type=Images',
				'filebrowserFlashBrowseUrl' => base_url() . 'assets/js/ckfinder/ckfinder.html?Type=Flash',
				'filebrowserUploadUrl'      => base_url() . 'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Files',
				'filebrowserImageUploadUrl' => base_url() . 'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Images',
				'filebrowserFlashUploadUrl' => base_url() . 'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Flash'
			)
        );
        $this->data['ckeditor_editor2'] = array(
			'id'   => 'ck_editor_textarea_id1',
			'path' => 'assets/js/ckeditor',
			'config' => array(
				'toolbar' 					=> "Full",
				'filebrowserBrowseUrl'      => base_url() . 'assets/js/ckfinder/ckfinder.html',
				'filebrowserImageBrowseUrl' => base_url() . 'assets/js/ckfinder/ckfinder.html?Type=Images',
				'filebrowserFlashBrowseUrl' => base_url() . 'assets/js/ckfinder/ckfinder.html?Type=Flash',
				'filebrowserUploadUrl'      => base_url() . 'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Files',
				'filebrowserImageUploadUrl' => base_url() . 'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Images',
				'filebrowserFlashUploadUrl' => base_url() . 'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Flash'
			)
        );
        $this->data['ckeditor_editor3'] = array(
			'id'   => 'ck_editor_textarea_id2',
			'path' => 'assets/js/ckeditor',
			'config' => array(
				'toolbar' 					=> "Full",
				'filebrowserBrowseUrl'      => base_url() . 'assets/js/ckfinder/ckfinder.html',
				'filebrowserImageBrowseUrl' => base_url() . 'assets/js/ckfinder/ckfinder.html?Type=Images',
				'filebrowserFlashBrowseUrl' => base_url() . 'assets/js/ckfinder/ckfinder.html?Type=Flash',
				'filebrowserUploadUrl'      => base_url() . 'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Files',
				'filebrowserImageUploadUrl' => base_url() . 'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Images',
				'filebrowserFlashUploadUrl' => base_url() . 'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Flash'
			)
		);
    }

    public function index() {
		$this->common_model->checkAdminUserPermission(14);
	//echo "<pre>";
	//print_r($this->data['theme']);exit;
        if ($this->input->post('form_submit')) {
            $this->load->library('upload');
            $data = $this->input->post();

            /*
             *  commision insert start vasanth
             */

            $admin_id = $this->session->userdata('admin_id');
            $commission = $this->input->post('commission');
            $CommInsert = [
                'admin_id' => $admin_id,
                'commission' => $commission,
            ];
            $where = [
                'status' => 1,
                'admin_id' => $admin_id,
            ];

            $AdminData = $this->admin->getSingleData('admin_commission', $where);

            if ($admin_id === $AdminData->admin_id) {

                $where = ['admin_id' => $admin_id];
                $this->admin->update_data('admin_commission', $CommInsert, $where);
            } else {
                $this->admin->update_data('admin_commission', $CommInsert);
            }

            /*
             *  commision insert end vasanth
             */

            if ($_FILES['site_logo']['name']) {
                $table_data1 = [];
                $configfile['upload_path'] = FCPATH . 'uploads/logo';
                $configfile['allowed_types'] = 'gif|jpg|jpeg|png';
                $configfile['overwrite'] = FALSE;
                $configfile['remove_spaces'] = TRUE;
                $file_name = $_FILES['site_logo']['name'];
                $configfile['file_name'] = time() . '_' . $file_name;
                $image_name = $configfile['file_name'];
                $image_url = 'uploads/logo/' . $image_name;
                $this->upload->initialize($configfile);
                if ($this->upload->do_upload('site_logo')) {
                    $img_uploadurl = 'uploads/logo' . $_FILES['site_logo']['name'];
                    $key = 'logo_front';
                    $val = 'uploads/logo/' . $image_name;
                    $this->db->where('key', $key);
                }
                $this->db->delete('system_settings');
                $table_data1['key'] = $key;
                $table_data1['value'] = $val;
                $table_data1['system'] = 1;
                $table_data1['groups'] = 'config';
                $table_data1['update_date'] = date('Y-m-d');
                $table_data1['status'] = 1;
                $this->db->insert('system_settings', $table_data1);
            }
            if ($_FILES['favicon']['name']) {
                $img_uploadurl1 = '';
                $table_data2 = '';
                $table_data = [];
                $configfile['upload_path'] = FCPATH . 'uploads/logo';
                $configfile['allowed_types'] = 'png|ico';
                $configfile['overwrite'] = FALSE;
                $configfile['remove_spaces'] = TRUE;
				//$configfile['max_width'] = 50;
				//$configfile['max_height'] = 50;
                $file_name = $_FILES['favicon']['name'];
                $configfile['file_name'] = $file_name;
                $this->upload->initialize($configfile);
                if ($this->upload->do_upload('favicon')) {

                    $img_uploadurl1 = $_FILES['favicon']['name'];
                    $key = 'favicon';
                    $val = $img_uploadurl1;
                    $select_fav_icon = $this->db->query("SELECT * FROM `system_settings` WHERE `key` = '$key' ");
                    $fav_icon_result = $select_fav_icon->row_array();

                    if (count($fav_icon_result) > 0) {
                        $this->db->where('key', $key);
                        $this->db->update('system_settings', array('value' => $val));
                    } else {
                        $table_data['key'] = $key;
                        $table_data['value'] = $val;
                        $this->db->insert('system_settings', $table_data);
                    }
                    $error = '';
                } else {
                    $error = $this->upload->display_errors();
                }
            }
            if ($data) {
                $table_data = array();

                # stripe_option // 1 SandBox, 2 Live 
                # stripe_allow  // 1 Active, 2 Inactive  

                $live_publishable_key = $live_secret_key = $secret_key = $publishable_key = '';

                $query = $this->db->query("SELECT * FROM payment_gateways WHERE status = 1");
                $stripe_details = $query->result_array();
                if (!empty($stripe_details)) {
                    foreach ($stripe_details as $details) {
                        if (strtolower($details['gateway_name']) == 'stripe') {
                            if (strtolower($details['gateway_type']) == 'sandbox') {

                                $publishable_key = $details['api_key'];
                                $secret_key = $details['value'];
                            }
                            if (strtolower($details['gateway_type']) == 'live') {
                                $live_publishable_key = $details['api_key'];
                                $live_secret_key = $details['value'];
                            }
                        }
                    }
                }
                
                $braintree_merchant = $braintree_key = $braintree_publickey = $braintree_privatekey = $paypal_appid = $paypal_appkey = '';
$live_braintree_merchant = $live_braintree_key = $live_braintree_publickey = $live_braintree_privatekey = $live_paypal_appid = $live_paypal_appkey = '';
    $pdata['braintree_key'] = $this->input->post('braintree_key');
    $pdata['braintree_merchant'] = $this->input->post('braintree_merchant');
    $pdata['braintree_publickey'] = $this->input->post('braintree_publickey');
    $pdata['braintree_privatekey'] = $this->input->post('braintree_privatekey');
    $pdata['paypal_appid'] = $this->input->post('paypal_appid');
    $pdata['paypal_appkey'] = $this->input->post('paypal_appkey');
    $pdata['gateway_type'] = $this->input->post('paypal_gateway');
    if($_POST['paypal_gateway']=="sandbox"){
        $pid=1;
    }else{
        $pid=2;
    }
	$this->db->where('id',$pid);
	$this->db->update('paypal_payment_gateways',$pdata); 
	
  $query = $this->db->query("SELECT * FROM paypal_payment_gateways");
  $paypal_details = $query->result_array();
  if(!empty($paypal_details)){
    foreach ($paypal_details as $details) {      
        if(strtolower($details['gateway_type'])=='sandbox'){
		  $braintree_key = $details['braintree_key'];
		  $braintree_merchant = $details['braintree_merchant'];
		  $braintree_publickey = $details['braintree_publickey'];
		  $braintree_privatekey = $details['braintree_privatekey'];
		  $paypal_appid = $details['paypal_appid'];
		  $paypal_appkey = $details['paypal_appkey'];
        }else{
		  $live_braintree_key = $details['braintree_key'];
		  $live_braintree_merchant = $details['braintree_merchant'];
		  $live_braintree_publickey = $details['braintree_publickey'];
		  $live_braintree_privatekey = $details['braintree_privatekey'];
		  $live_paypal_appid = $details['paypal_appid'];
		  $live_paypal_appkey = $details['paypal_appkey'];
        }
    }
  } 
  $data['braintree_key']    = $braintree_key;
  $data['braintree_merchant']       = $braintree_merchant;
  $data['braintree_publickey'] = $braintree_publickey;
  $data['braintree_privatekey']    = $braintree_privatekey;
  $data['paypal_appid'] = $paypal_appid;
  $data['paypal_appkey']    = $paypal_appkey;
  
  $data['live_braintree_key']    = $live_braintree_key;
  $data['live_braintree_merchant']       = $live_braintree_merchant;
  $data['live_braintree_publickey'] = $live_braintree_publickey;
  $data['live_braintree_privatekey']    = $live_braintree_privatekey; 
  $data['live_paypal_appid'] = $live_paypal_appid;
  $data['live_paypal_appkey']    = $live_paypal_appkey;

                $data['publishable_key'] = $publishable_key;
                $data['secret_key'] = $secret_key;
                $data['live_publishable_key'] = $live_publishable_key;
                $data['live_secret_key'] = $live_secret_key;

                foreach ($data AS $key => $val) {

                    if ($key != 'form_submit') {
                        $this->db->where('key', $key);
                        $this->db->delete('system_settings');
                        $table_data['key'] = $key;
                        $table_data['value'] = $val;
                        $table_data['system'] = 1;
                        $table_data['groups'] = 'config';
                        $table_data['update_date'] = date('Y-m-d');
                        $table_data['status'] = 1;
                        $this->db->insert('system_settings', $table_data);
                    }
                }
            }
            $message = '';
            if (!empty($error)) {
                $this->session->set_flashdata('error_message', 'Something wrong, Please try again');
            } else {
                $this->session->set_flashdata('success_message', 'Settings updated successfully');
            }
            redirect(base_url('admin/settings'));
        }

        $results = $this->admin->get_setting_list();

        foreach ($results AS $config) {
            $this->data[$config['key']] = $config['value'];
        }



        $this->data['page'] = 'index';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function emailsettings() {
		$this->common_model->checkAdminUserPermission(14);
        if ($this->input->post('form_submit')) {


            $this->load->library('upload');
            $data = $this->input->post();
            if ($data) {
                $table_data = array();
                foreach ($data AS $key => $val) {
                    if ($key != 'form_submit') {
                        $this->db->where('key', $key);
                        $this->db->delete('system_settings');
                        $table_data['key'] = $key;
                        $table_data['value'] = $val;
                        $table_data['system'] = 1;
                        $table_data['groups'] = 'config';
                        $table_data['update_date'] = date('Y-m-d');
                        $table_data['status'] = 1;
                        $this->db->insert('system_settings', $table_data);
                    }
                }
            }


            $message = 'Settings saved successfully';
            $this->session->set_flashdata('success_message', $message);
            redirect(base_url('admin/emailsettings'));
        }

        $results = $this->admin->get_setting_list();
        foreach ($results AS $config) {
            $this->data[$config['key']] = $config['value'];
        }

        $this->data['page'] = 'emailsettings';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
	
	   public function googleplus_social_media() {
		$this->common_model->checkAdminUserPermission(14);
        if ($this->input->post('form_submit')) {


            $this->load->library('upload');
            $data = $this->input->post();
            if ($data) {
                $table_data = array();
                foreach ($data AS $key => $val) {
                    if ($key != 'form_submit') {
                        $this->db->where('key', $key);
                        $this->db->delete('system_settings');
                        $table_data['key'] = $key;
                        $table_data['value'] = $val;
                        $table_data['system'] = 1;
                        $table_data['groups'] = 'config';
                        $table_data['update_date'] = date('Y-m-d');
                        $table_data['status'] = 1;
                        $this->db->insert('system_settings', $table_data);
                    }
                }
            }


            $message = 'Settings saved successfully';
            $this->session->set_flashdata('success_message', $message);
            redirect(base_url('admin/googleplus_social_media'));
        }

        $results = $this->admin->get_setting_list();
        foreach ($results AS $config) {
            $this->data[$config['key']] = $config['value'];
        }

        $this->data['page'] = 'googleplus_social_media';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
	
	
	public function twit_social_media() {
		$this->common_model->checkAdminUserPermission(14);
        if ($this->input->post('form_submit')) {


            $this->load->library('upload');
            $data = $this->input->post();
            if ($data) {
                $table_data = array();
                foreach ($data AS $key => $val) {
                    if ($key != 'form_submit') {
                        $this->db->where('key', $key);
                        $this->db->delete('system_settings');
                        $table_data['key'] = $key;
                        $table_data['value'] = $val;
                        $table_data['system'] = 1;
                        $table_data['groups'] = 'config';
                        $table_data['update_date'] = date('Y-m-d');
                        $table_data['status'] = 1;
                        $this->db->insert('system_settings', $table_data);
                    }
                }
            }


            $message = 'Settings saved successfully';
            $this->session->set_flashdata('success_message', $message);
            redirect(base_url('admin/twit_social_media'));
        }

        $results = $this->admin->get_setting_list();
        foreach ($results AS $config) {
            $this->data[$config['key']] = $config['value'];
        }

        $this->data['page'] = 'twit_social_media';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
	
	
		public function fb_social_media() {
		$this->common_model->checkAdminUserPermission(14);
        if ($this->input->post('form_submit')) {


            $this->load->library('upload');
            $data = $this->input->post();
            if ($data) {
                $table_data = array();
                foreach ($data AS $key => $val) {
                    if ($key != 'form_submit') {
                        $this->db->where('key', $key);
                        $this->db->delete('system_settings');
                        $table_data['key'] = $key;
                        $table_data['value'] = $val;
                        $table_data['system'] = 1;
                        $table_data['groups'] = 'config';
                        $table_data['update_date'] = date('Y-m-d');
                        $table_data['status'] = 1;
                        $this->db->insert('system_settings', $table_data);
                    }
                }
            }


            $message = 'Settings saved successfully';
            $this->session->set_flashdata('success_message', $message);
            redirect(base_url('admin/fb_social_media'));
        }

        $results = $this->admin->get_setting_list();
        foreach ($results AS $config) {
            $this->data[$config['key']] = $config['value'];
        }

        $this->data['page'] = 'fb_social_media';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
	
	
	
	
	
	
	

    public function smssettings() {
		$this->common_model->checkAdminUserPermission(14);
        if ($this->input->post('form_submit')) {
            $this->load->library('upload');
            $data = $this->input->post();
            if ($data) {
                $table_data = array();
                if (isset($_POST['default_otp'])) {
                    $data['default_otp'] = 1;
                } else {
                    $data['default_otp'] = 0;
                }

                foreach ($data AS $key => $val) {
                    if ($key != 'form_submit') {
                        $this->db->where('key', $key);
                        $this->db->delete('system_settings');
                        $table_data['key'] = $key;
                        $table_data['value'] = $val;
                        $table_data['system'] = 1;
                        $table_data['groups'] = 'config';
                        $table_data['update_date'] = date('Y-m-d');
                        $table_data['status'] = 1;
                        $this->db->insert('system_settings', $table_data);
                    }
                }
            }


            $message = 'Settings saved successfully';
            $this->session->set_flashdata('success_message', $message);
            redirect(base_url('admin/sms-settings'));
        }
        $results = $this->admin->get_setting_list();
        foreach ($results AS $config) {
            $this->data[$config['key']] = $config['value'];
        }
        if (empty($this->data['default_otp'])) {
            $this->data['default_otp'] = '';
        }
        $this->data['page'] = 'smssettings';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function stripe_payment_gateway() {
		$this->common_model->checkAdminUserPermission(14);  
        $id = settingValue('stripe_option');
	//echo $id;exit;
        if ($this->input->post('form_submit')) {
            if ($_POST['gateway_type'] == "sandbox") {
                $id = 1;
            } else {
                $id = 2;
            }
            $data['gateway_name'] = $this->input->post('gateway_name');
            $data['gateway_type'] = $this->input->post('gateway_type');
            $data['api_key'] = $this->input->post('api_key');
            $data['value'] = $this->input->post('value');
            $data['status'] = '1';
            $this->db->where('id', $id);
            if ($this->db->update('payment_gateways', $data)) {
                if ($this->input->post('gateway_type') == 'sandbox') {
                    $datass['publishable_key'] = $this->input->post('api_key');
                    $datass['secret_key'] = $this->input->post('value');
                } else {
                    $datass['live_publishable_key'] = $this->input->post('api_key');
                    $datass['live_secret_key'] = $this->input->post('value');
                }

                foreach ($datass AS $key => $val) {
                    $this->db->where('key', $key);
                    $this->db->delete('system_settings');
                    $table_data['key'] = $key;
                    $table_data['value'] = $val;
                    $table_data['system'] = 1;
                    $table_data['groups'] = 'config';
                    $table_data['update_date'] = date('Y-m-d');
                    $table_data['status'] = 1;
                    $this->db->insert('system_settings', $table_data);
                }

                $message = 'Payment gateway edit successfully';
            }
            $this->session->set_flashdata('success_message', $message);
            redirect(base_url() . 'admin/stripe_payment_gateway');
        }
        if (!empty($id)) {
            $this->data['list'] = $this->admin->edit_payment_gateway($id);
        } else {
            $this->data['list'] = [];
            $this->data['list']['id'] = '';
            $this->data['list']['gateway_type'] = '';
            $this->data['gateway_type'] = '';
        }
//echo "<pre>";print_r($this->data['list']);exit;
        $this->data['page'] = 'stripe_payment_gateway';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
	
	 public function razorpay_payment_gateway() {
		 $this->common_model->checkAdminUserPermission(14);  
        $id = settingValue('razor_option');

        if ($this->input->post('form_submit')) {
            if ($_POST['gateway_type'] == "sandbox") {
                $id = 1;
            } else {
                $id = 2;
            }
            $data['gateway_name'] = $this->input->post('gateway_name');
            $data['gateway_type'] = $this->input->post('gateway_type');
            $data['api_key'] = $this->input->post('api_key');
            $data['api_secret'] = $this->input->post('value');
            $data['status'] = '1';
            $this->db->where('id', $id);
            if ($this->db->update('razorpay_gateway', $data)) {
                if ($this->input->post('gateway_type') == 'sandbox') {
                    $datass['publishable_key'] = $this->input->post('api_key');
                    $datass['secret_key'] = $this->input->post('value');
                } else {
                    $datass['live_publishable_key'] = $this->input->post('api_key');
                    $datass['live_secret_key'] = $this->input->post('value');
                }

                foreach ($datass AS $key => $val) {
                    $this->db->where('key', $key);
                    $this->db->delete('system_settings');
                    $table_data['key'] = $key;
                    $table_data['value'] = $val;
                    $table_data['system'] = 1;
                    $table_data['groups'] = 'config';
                    $table_data['update_date'] = date('Y-m-d');
                    $table_data['status'] = 1;
                    $this->db->insert('system_settings', $table_data);
                }

                $message = 'Payment gateway edit successfully';
            }
            $this->session->set_flashdata('success_message', $message);
            redirect(base_url() . 'admin/razorpay_payment_gateway');
        }
        if (!empty($id)) {
            $this->data['list'] = $this->admin->edit_razor_payment_gateway($id);
        } else {
            $this->data['list'] = [];
            $this->data['list']['id'] = '';
            $this->data['list']['gateway_type'] = '';
            $this->data['gateway_type'] = '';
        }
		//echo "<pre>";print_r($this->data['list']);exit;
		//print_r($this->data['list']);exit;

        $this->data['page'] = 'razorpay_payment_gateway';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
	
	public function paypal_payment_gateway() {
        $id = settingValue('paypal_option');
		//echo $id;exit;

		if (!empty($id)) {
            $this->data['list'] = $this->admin->edit_paypal_payment_gateway($id);
        } else {
            $this->data['list'] = [];
            $this->data['list']['id'] = '';
            $this->data['list']['gateway_type'] = '';
            $this->data['gateway_type'] = '';
        }
		
		//print_r($this->data['list']);exit;

        $this->data['page'] = 'paypal_payment_gateway';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
	
	public function paytabs_payment_gateway() {
       // $id = settingValue('paytab_option');
		//echo $id;exit;

		// if (!empty($id)) {
            // $this->data['list'] = $this->admin->edit_paytab_payment_gateway($id);
        // } else {
            // $this->data['list'] = [];
            // $this->data['list']['id'] = '';
            // $this->data['list']['gateway_type'] = '';
            // $this->data['gateway_type'] = '';
        // }
		
		$this->data['list'] = $this->admin->edit_paytab_payment_gateway();
		
		//print_r($this->data['list']);exit;

        $this->data['page'] = 'paytab_payment_gateway';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function payment_type() {
        if (!empty($_POST['type'])) {
            $result = $this->db->where('gateway_type=', $_POST['type'])->get('payment_gateways')->row_array();
            echo json_encode($result);
            exit;
        }
    }
	
	public function razor_payment_type() {
	if (!empty($_POST['type'])) {
		$result = $this->db->where('gateway_type=', $_POST['type'])->get('razorpay_gateway')->row_array();
		echo json_encode($result);
		exit;
	}
	}
	
	
	public function paypal_payment_type(){ 
	  if(!empty($_POST['type'])){
		$result=$this->db->where('gateway_type=',$_POST['type'])->get('paypal_payment_gateways')->row_array();
		echo json_encode($result);exit;
	  }
	}
    public function edit($id = NULL) {
		$this->common_model->checkAdminUserPermission(14);
        if ($this->input->post('form_submit')) {
            if ($_POST['gateway_type'] == "sandbox") {
                $id = 1;
            } else {
                $id = 2;
            }
            $data['gateway_name'] = $this->input->post('gateway_name');
            $data['gateway_type'] = $this->input->post('gateway_type');
            $data['api_key'] = $this->input->post('api_key');
            $data['value'] = $this->input->post('value');
            $data['status'] = '1';
            $this->db->where('id', $id);
            if ($this->db->update('payment_gateways', $data)) {
                if ($this->input->post('gateway_type') == 'sandbox') {
                    $datass['publishable_key'] = $this->input->post('api_key');
                    $datass['secret_key'] = $this->input->post('value');
                } else {
                    $datass['live_publishable_key'] = $this->input->post('api_key');
                    $datass['live_secret_key'] = $this->input->post('value');
                }
                $stripe_option = settingValue('stripe_option');
                if (!empty($stripe_option)) {
                    $this->db->where('key', 'stripe_option')->update('system_settings', ['value' => $id]);
                } else {
                    $this->db->insert('system_settings', ['key' => 'stripe_option', 'value' => $id]);
                }

                foreach ($datass AS $key => $val) {
                    $this->db->where('key', $key);
                    $this->db->delete('system_settings');
                    $table_data['key'] = $key;
                    $table_data['value'] = $val;
                    $table_data['system'] = 1;
                    $table_data['groups'] = 'config';
                    $table_data['update_date'] = date('Y-m-d');
                    $table_data['status'] = 1;
                    $this->db->insert('system_settings', $table_data);
                }

                $message = 'Payment gateway edit successfully';
            }
            $this->session->set_flashdata('success_message', $message);
            redirect(base_url() . 'admin/stripe_payment_gateway');
        }

        $this->data['list'] = $this->admin->edit_payment_gateway($id);
        $this->data['page'] = 'stripe_payment_gateway_edit';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
	
	
	    public function razor_edit($id = NULL) {
			$this->common_model->checkAdminUserPermission(14);
        if ($this->input->post('form_submit')) {
            if ($_POST['gateway_type'] == "sandbox") {
                $id = 1;
            } else {
                $id = 2;
            }
            $data['gateway_name'] = $this->input->post('gateway_name');
            $data['gateway_type'] = $this->input->post('gateway_type');
            $data['api_key'] = $this->input->post('api_key');
            $data['api_secret'] = $this->input->post('value');
            $data['status'] = '1';
            $this->db->where('id', $id);
            if ($this->db->update('razorpay_gateway', $data)) {
                if ($this->input->post('gateway_type') == 'sandbox') {
                    $datass['razorpay_apikey'] = $this->input->post('api_key');
                    $datass['razorpay_secret_key'] = $this->input->post('value');
                } else {
                    $datass['live_razorpay_apikey'] = $this->input->post('api_key');
                    $datass['live_razorpay_secret_key'] = $this->input->post('value');
                }
                $razor_option = settingValue('razor_option');
				
				//print_r($razor_option);exit;
                if (!empty($razor_option)) {
                    $this->db->where('key', 'razor_option')->update('system_settings', ['value' => $id]);
                } else {
                    $this->db->insert('system_settings', ['key' => 'razor_option', 'value' => $id]);
                }

                foreach ($datass AS $key => $val) {
                    $this->db->where('key', $key);
                    $this->db->delete('system_settings');
                    $table_data['key'] = $key;
                    $table_data['value'] = $val;
                    $table_data['system'] = 1;
                    $table_data['groups'] = 'config';
                    $table_data['update_date'] = date('Y-m-d');
                    $table_data['status'] = 1;
                    $this->db->insert('system_settings', $table_data);
                }

                $message = 'Payment gateway edit successfully';
            }
            $this->session->set_flashdata('success_message', $message);
            redirect(base_url() . 'admin/razorpay_payment_gateway');
        }

        $this->data['list'] = $this->admin->edit_payment_gateway($id);
        $this->data['page'] = 'razorpay_payment_gateway';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
	
	   public function paypal_edit($id = NULL) {
		   $this->common_model->checkAdminUserPermission(14);
        if ($this->input->post('form_submit')) {
            if ($_POST['paypal_gateway'] == "sandbox") {
                $id = 1;
            } else {
                $id = 2;
            }
			
		//	echo "<pre>";print_r($this->input->post());exit;
            $data['braintree_key'] = $this->input->post('braintree_key');
            $data['gateway_type'] = $this->input->post('paypal_gateway');
            $data['braintree_merchant'] = $this->input->post('braintree_merchant');
            $data['braintree_publickey'] = $this->input->post('braintree_publickey');
            $data['braintree_privatekey'] = $this->input->post('braintree_privatekey');
            $data['paypal_appid'] = $this->input->post('paypal_appid');
            $data['paypal_appkey'] = $this->input->post('paypal_appkey');
           // $data['status'] = '1';
            $this->db->where('id', $id);
            if ($this->db->update('paypal_payment_gateways', $data)) {
                if ($this->input->post('paypal_gateway') == 'sandbox') {
                    $datass['braintree_key'] = $this->input->post('braintree_key');
					$datass['gateway_type'] = $this->input->post('paypal_gateway');
					$datass['braintree_merchant'] = $this->input->post('braintree_merchant');
					$datass['braintree_publickey'] = $this->input->post('braintree_publickey');
					$datass['braintree_privatekey'] = $this->input->post('braintree_privatekey');
					$datass['paypal_appid'] = $this->input->post('paypal_appid');
					$datass['paypal_appkey'] = $this->input->post('paypal_appkey');
                } else {
                    $datass['braintree_key'] = $this->input->post('braintree_key');
					$datass['gateway_type'] = $this->input->post('paypal_gateway');
					$datass['braintree_merchant'] = $this->input->post('braintree_merchant');
					$datass['braintree_publickey'] = $this->input->post('braintree_publickey');
					$datass['braintree_privatekey'] = $this->input->post('braintree_privatekey');
					$datass['paypal_appid'] = $this->input->post('paypal_appid');
					$datass['paypal_appkey'] = $this->input->post('paypal_appkey');
                }
                $paypal_option = settingValue('paypal_option');
				
				//print_r($razor_option);exit;
                if (!empty($paypal_option)) {
                    $this->db->where('key', 'paypal_option')->update('system_settings', ['value' => $id]);
                } else {
                    $this->db->insert('system_settings', ['key' => 'paypal_option', 'value' => $id]);
                }

                foreach ($datass AS $key => $val) {
                    $this->db->where('key', $key);
                    $this->db->delete('system_settings');
                    $table_data['key'] = $key;
                    $table_data['value'] = $val;
                    $table_data['system'] = 1;
                    $table_data['groups'] = 'config';
                    $table_data['update_date'] = date('Y-m-d');
                    $table_data['status'] = 1;
                    $this->db->insert('system_settings', $table_data);
                }

                $message = 'Payment gateway edit successfully';
            }
            $this->session->set_flashdata('success_message', $message);
            redirect(base_url() . 'admin/paypal_payment_gateway');
        }

        $this->data['list'] = $this->admin->edit_payment_gateway($id);
        $this->data['page'] = 'paypal_payment_gateway';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
	
	 public function paytab_edit($id = NULL) {
		 $this->common_model->checkAdminUserPermission(14);
        if ($this->input->post('form_submit')) {
            
			$id=1;
		//	echo "<pre>";print_r($this->input->post());exit;
            $data['sandbox_email'] = $this->input->post('sandbox_email');
            $data['sandbox_secretkey'] = $this->input->post('sandbox_secretkey');
            $data['email'] = $this->input->post('email');
            $data['secretkey'] = $this->input->post('secretkey');
           // $data['status'] = '1';
            $this->db->where('id', $id);
            if ($this->db->update('paytabs_details', $data)) {
               
                    $datass['sandbox_email'] = $this->input->post('sandbox_email');
					$datass['sandbox_secretkey'] = $this->input->post('sandbox_secretkey');
					$datass['email'] = $this->input->post('email');
					$datass['secretkey'] = $this->input->post('secretkey');
                
                $paytab_option = settingValue('paytab_option');
				
				//print_r($razor_option);exit;
                if (!empty($paytab_option)) {
                    $this->db->where('key', 'paytab_option')->update('system_settings', ['value' => $id]);
                } else {
                    $this->db->insert('system_settings', ['key' => 'paytab_option', 'value' => $id]);
                }

                foreach ($datass AS $key => $val) {
                    $this->db->where('key', $key);
                    $this->db->delete('system_settings');
                    $table_data['key'] = $key;
                    $table_data['value'] = $val;
                    $table_data['system'] = 1;
                    $table_data['groups'] = 'config';
                    $table_data['update_date'] = date('Y-m-d');
                    $table_data['status'] = 1;
                    $this->db->insert('system_settings', $table_data);
                }

                $message = 'Payment gateway edit successfully';
            }
            $this->session->set_flashdata('success_message', $message);
            redirect(base_url() . 'admin/paytabs_payment_gateway');
        }

        $this->data['list'] = $this->admin->edit_payment_gateway($id);
        $this->data['page'] = 'paytab_payment_gateway';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
	
	
	

    public function ThemeColorChange() {

        $this->data['page'] = 'theme_color';
        $this->data['Colorlist'] = $this->admin->ColorList();
//        echo '<pre>';
//        print_r($this->data['Colorlist']);exit;
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function ChangeColor() {
        $Postdata = $this->input->post();
        $ChangeColor = $Postdata['color'];

        if ($ChangeColor) {

            $whr = [
                'id' => $ChangeColor
            ];
            $color = [
                'status' => 1
            ];
            $query=$this->db->query("UPDATE theme_color_change SET status='0'");
            $updateColor = $this->admin->update_data('theme_color_change', $color, $whr);

            if ($updateColor) {
                $this->session->set_flashdata('success_message1', 'Color Change Suceessfully');
                redirect(base_url() . 'admin/theme-color');
            }
        } else {
            $this->session->set_flashdata('error_message1', 'Choose the Color');
            redirect(base_url() . 'admin/theme-color');
        }
    }

}
