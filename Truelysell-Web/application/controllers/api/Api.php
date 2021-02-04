<?php

defined('BASEPATH') OR exit('No direct script access allowed');

//include Rest Controller library
require APPPATH . '/libraries/REST_Controller.php';

class Api extends REST_Controller {

    public function __construct() {

        parent::__construct();
        error_reporting(0);
        $this->load->helper('push_notifications');
        $this->load->helper('user_timezone');
        $this->load->model('api_model', 'api');
        $this->load->model('admin_model', 'admin');
        $this->load->model('Stripe_model');
        $this->load->model('user_login_model', 'user_login');

        $header = getallheaders(); // Get Header Data
        $token = (!empty($header['token'])) ? $header['token'] : '';
        if (empty($token)) {
            $token = (!empty($header['Token'])) ? $header['Token'] : '';
        }
        $this->default_token = md5('Dreams99');
        $this->api_token = $token;
        $this->user_id = $this->api->get_user_id_using_token($token); /* provider */
        $this->users_id = $this->api->get_users_id_using_token($token); /* user */

        /* language API */

        $lang = (!empty($header['language'])) ? $header['language'] : '';
        if (empty($lang)) {
            $lang = (!empty($header['Language'])) ? $header['Language'] : 'en';
        }
        $language = get_languages($lang);
        $language = (!empty($language['language']['api'])) ? $language['language']['api'] : '';
        $this->language_content = $language;


        $this->website_name = '';
        $this->data['secret_key'] = '';
        $this->data['publishable_key'] = '';
        $this->data['website_logo_front'] = 'assets/img/logo.png';

        $publishable_key = '';
        $secret_key = '';
        $live_publishable_key = '';
        $live_secret_key = '';
        $stripe_option = '';
        $login_type = '';



        $query = $this->db->query("select * from system_settings WHERE status = 1");
        $result = $query->result_array();
        if (!empty($result)) {
            foreach ($result as $data) {
                if ($data['key'] == 'website_name') {
                    $this->website_name = $data['value'];
                }
                if ($data['key'] == 'secret_key') {
                    $secret_key = $data['value'];
                }

                if ($data['key'] == 'publishable_key') {
                    $publishable_key = $data['value'];
                }

                if ($data['key'] == 'live_secret_key') {
                    $live_secret_key = $data['value'];
                }

                if ($data['key'] == 'live_publishable_key') {
                    $live_publishable_key = $data['value'];
                }

                if ($data['key'] == 'stripe_option') {
                    $stripe_option = $data['value'];
                }
				
				if ($data['key'] == 'login_type') {
                    $login_type = $data['value'];
                }
            }
        }



        if (@$stripe_option == 1) {
            $this->data['publishable_key'] = $publishable_key;
            $this->data['secret_key'] = $secret_key;
        }

        if (@$stripe_option == 2) {
            $this->data['publishable_key'] = $live_publishable_key;
            $this->data['secret_key'] = $live_secret_key;
        }


        $config['publishable_key'] = $this->data['publishable_key'];
        $config['secret_key'] = $this->data['secret_key'];

        $this->load->library('stripe', $config);
    }
	
	
	public function getlogin_type_get(){
		$loginquery = $this->db->query("select * from system_settings WHERE status = 1");
			   $loginresult = $loginquery->result_array();
			   if (!empty($loginresult)) {
				   foreach ($loginresult as $logindata) {
		if($logindata['key'] == 'login_type'){
		$data['login_type'] = $logindata['value'];
		}
		}
		}
		$response_code = 200;
			   $response_message = "Get Login type";
		$result = $this->data_format($response_code, $response_message, $data);
			   $this->response($result, REST_Controller::HTTP_OK);
		}
		
		
		public function check_provider_email_post(){
			
			$user_data = array();
			$user_data = $this->post();
			$is_available_email = $this->api->check_email($user_data);
			if($is_available_email > 0)
			{
				$response_code = '200';
				$response_message = "Email ID Exist";
			}
			else
			{
				$response_code = '200';
				$response_message = "Email ID Not Exist";
			}
			$result = $this->data_format($response_code, $response_message, $data);
			$this->response($result, REST_Controller::HTTP_OK);
		}
		
		public function check_user_emailid_post(){
			
			$user_data = array();
			$user_data = $this->post();
			$is_available_email = $this->api->check_user_email($user_data);
			if($is_available_email > 0)
			{
				$response_code = '200';
				$response_message = "Email ID Exist";
			}
			else
			{
				$response_code = '200';
				$response_message = "Email ID Not Exist";
			}
			$result = $this->data_format($response_code, $response_message, $data);
			$this->response($result, REST_Controller::HTTP_OK);
		}
		
		public function forget_password_post(){
			
			// $email = $this->input->post('email');
			// $mode = $this->input->post('mode');
			
			$user_data = $this->post();
			$email=$user_data['email'];
			$mode=$user_data['mode'];
			
			if($mode==2)
			{
				$result = $this->user_login->check_user_emaildet($email);
				$user_type='user';
			}
			else
			{
				$result = $this->user_login->check_provider_emaildet($email);
				$user_type='provider';
			}
			//echo "<pre>";print_r($result);exit;
			if(!empty($result))
			{
				$token=rand(1000,9999);
				$pwdlink=base_url()."user/login/userchangepwd/".base64_encode($result['id'])."/".base64_encode($token)."/".base64_encode($mode);
				$chk_forpawd=$this->db->where('user_id',$result['id'])->where('user_type',$user_type)->where('status','1')->select('*')->get('forget_password_det')->result_array(); 
				if(empty($chk_forpawd))
				{
					$pwdlink_data=array(
					  'endtime'=>time()+300,
					  'token'=> $token,
					  'user_id'=>$result['id'],
					  'email'=>$result['email'],
					  'pwdlink'=>$pwdlink,
					  'user_type'=>$user_type,
					  'created_at'=>date('Y-m-d H:i:s')
					);
					$save_forpwd = $this->admin->save_pwdlink_data($pwdlink_data);
				}
				else
				{
					$pwdlink_data=array(
				  'endtime'=>time()+300,
				  'token'=>$token ,
				  'user_id'=>$result['id'],
				  'email'=>$result['email'],
				  'pwdlink'=>$pwdlink,
				  'user_type'=>$user_type,
				  'updated_on'=>date('Y-m-d H:i:s')
					);
					$save_forpwd = $this->admin->update_pwdlink_data($pwdlink_data,$result['user_id']);
				}
				
				$message='Reset Link  '.$pwdlink.''; 
				$body = 'Hi '.$result["name"].',<br> '.$message;
				
				$phpmail_config = settingValue('mail_config');
				
				if (isset($phpmail_config) && !empty($phpmail_config)) {
					if ($phpmail_config == "phpmail") {
						$from_email = settingValue('email_address');
					} else {
						$from_email = settingValue('smtp_email_address');
					}
				}
				
				$this->load->library('email');
				if (!empty($from_email) && isset($from_email)) {
					$mail = $this->email
							->from($from_email)
							->to($result["email"])
							->subject('User Forget Password Link')
							->message($body)
							->send();
							
				}
				
				$response_code = '200';
				$response_message = "Reset link sent to your Email";
				
			}
		 else
			{
				
				$response_code = '201';
				$response_message = "Email ID Not Exist";
			}
			
			$result = $this->data_format($response_code, $response_message, $data);
			$this->response($result, REST_Controller::HTTP_OK);
		}
		
		public function check_pro_newpassword_post(){
			
			$user_data = array();
			$user_data = $this->post();
			
			$user_id       = $user_data['user_id'];
			$user_type       = $user_data['user_type'];
			if($user_type=='provider')
			{
				$user = $this->db->where('id', $user_id)->where('password', md5($user_data('current_password')))->get('providers')->row_array();
			}
			else
			{
				$user = $this->db->where('id', $user_id)->where('password', md5($user_data('current_password')))->get('users')->row_array();
			}
			
			if(!empty($user))
			{
				$response_code = '200';
				$response_message = "Password Matched";
			}
			else
			{
				$response_code = '200';
				$response_message = "Password Mismatched";
			}
			
			$result = $this->data_format($response_code, $response_message, $data);
			$this->response($result, REST_Controller::HTTP_OK);
		}
		
		
		public function userchangepassword_post(){
			
			$user_data = array();
			$user_data = $this->post();
			
			$user_id       = $user_data['user_id'];
			$user_type       = $user_data['user_type'];
			$current_password       = md5($user_data['current_password']);
			$confirm_password       = $user_data['confirm_password'];
			
			$table_data=array("password"=>md5($confirm_password));
			$this->db->where('id',$user_id);
			if($user_type=='provider')
			{
				//$prodet = $this->db->where('id', $user_id)->where('password', md5($user_data('current_password')))->get('providers')->row_array();
				$prodet = $this->api->checkpropwd($user_id,$current_password);
				if($prodet > 0)
				{
					$this->db->where('id',$user_id);  
					if($this->db->update('providers', $table_data))
					{
						$response_code = '200';
						$response_message = "Password updated successfully";
					}
					else
					{
						$response_code = '201';
						$response_message = "Something went wrong";
					}
				}
				else
				{
					$response_code = '201';
					$response_message = "Current Password Mismatched";
				}
				
				
			}
			else
			{
				//$user = $this->db->where('id', $user_id)->where('password', md5($user_data('current_password')))->get('users')->row_array();
				$user = $this->api->checkuserpwd($user_id,$current_password);
				if($user > 0)
				{
					$this->db->where('id',$user_id);  
					if($this->db->update('users', $table_data))
					{
						$response_code = '200';
						$response_message = "User password updated successfully";
					}
					else
					{
						$response_code = '201';
						$response_message = "Something went wrong";
					}
				}
				else
				{
					$response_code = '201';
					$response_message = "Current Password Mismatched";
				}
			}	
			$result = $this->data_format($response_code, $response_message, $data);
			$this->response($result, REST_Controller::HTTP_OK);
		}
		
		

    public function home_post() {
		if($this->api_token){
        $data = new stdClass();
        $user_data = array();
        $user_data = $this->post();
        if (!empty($user_data['latitude']) && !empty($user_data['longitude'])) {
			$user_data['user_id']=$this->api->get_users_id_using_token($this->api_token);
            $category_list = $this->api->get_category();
            $popular_services = $this->api->get_service(1, $user_data);
            $new_services = $this->api->get_service(2, $user_data);


            if (!empty($category_list) || !empty($popular_services) || !empty($new_services)) {
                $response_code = '200';
                $response_message = "Home Page";
                $res['category_list'] = $category_list;
                $res['popular_services'] = $popular_services;
                $res['new_services'] = $new_services;
                $data = $res;
            } else {
                $response_code = '200';
                $response_message = "No Results found";
                $res['category_list'] = [];
                $res['popular_services'] = [];
                $res['new_services'] = [];
                $data = $res;
            }
        } else {
            $response_code = '200';
            $response_message = "Input field missing";
            $res['category_list'] = [];
            $res['popular_services'] = [];
            $res['new_services'] = [];
            $data = $res;
        }


        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }else{
        $this->token_error();
        }
    }

    public function country_details_get() {
        $data = $this->db->select('country_code,country_id,country_name')->order_by('country_name', 'asc')->get('country_table')->result_array();
        $response_code = 200;
        $response_message = "Fetched Successfully...";
        $result = $this->data_format($response_code, $response_message, $data);
        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function demo_home_post() {
       
        $data = new stdClass();
        $user_data = array();
        $user_data = $this->post();
		$user_data['user_id'] = $this->user_id;
		$user_data['users_id'] = $this->users_id;

        if (!empty($user_data['latitude']) && !empty($user_data['longitude'])) {

            $category_list = $this->api->get_category();
            $popular_services = $this->api->get_service(1, $user_data);

            $new_services = $this->api->get_service(2, $user_data);


            if (!empty($category_list) && !empty($popular_services) && !empty($new_services)) {
                $response_code = '200';
                $response_message = "Home Page";
                $res['category_list'] = $category_list;
                $res['popular_services'] = $popular_services;
                $res['new_services'] = $new_services;
                $data = $res;
            } else {
                $response_code = '200';
                $response_message = "Home Page";
                $res['category_list'] = $this->api->get_category();
                $res['popular_services'] = $this->api->get_demo_service(1, $user_data);
                $res['new_services'] = $this->api->get_demo_service(2, $user_data);
                $data = $res;
            }
        } else {
            $response_code = '200';
            $response_message = "Home Page";
            $res['category_list'] = $this->api->get_category();
            $res['popular_services'] = $this->api->get_demo_service(1, $user_data);
            $res['new_services'] = $this->api->get_demo_service(2, $user_data);
            $data = $res;
        }


        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function my_service_post() {
        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {
            $user_data['token'] = $this->api_token;
            $user_data['user_id'] = $this->user_id;

            if (!empty($this->post('type'))) {
                $user_data['type'] = $this->post('type');
            }

            $result = $this->api->get_my_service($user_data);

            if (!empty($result)) {
                $response_code = '200';
                $response_message = "Service list";
                $data = $result;
            } else {
                $response_code = '200';
                $response_message = "No Results found";
                $data = array();
            }


            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function service_details_get() {

        if ($this->user_id != 0 || $this->users_id != 0 || ($this->default_token == $this->api_token)) {

            if (isset($_GET['id']) && !empty($_GET['id'])) {
                $token = $this->api_token;
                $user_id = $this->api->get_users_id_using_token($token);
                if($user_id == ''){
                    $user_id = $this->api->get_user_id_using_token($token);
                    $pro=$this->api->getSingleData('providers',['type'],['id' => $user_id]);
                    $type=$pro->type;
                }
                

                $inputs = array();
                $inputs['id'] = $this->get('id');
                $service_details = $this->api->get_service_details($inputs, $user_id, $type);
            	

                if (!empty($service_details)) {



                    $this->db->select("r.*,u.*");
                    $this->db->from('rating_review r');
                    $this->db->join('users u', 'r.user_id = u.id', 'LEFT');
                    $this->db->where("r.service_id", $inputs['id']);
                    $review_details = $this->db->get()->result_array();
                    $review_list = array();
                    foreach ($review_details as $review) {

                        $reviews['name'] = $review['name'];
                        $reviews['profile_img'] = $review['profile_img'];
                        $reviews['rating'] = $review['rating'];
                        $reviews['review'] = $review['review'];
                        $reviews['created'] = $review['created'];
                        $review_list[] = $reviews;
                    }

                    $response_code = '200';
                    $response_message = "Service Details";
                    $data['service_overview'] = $service_details['service_overview'];
                    $data['seller_overview'] = $service_details['seller_overview'];
                    $data['seller_overview']['services'] = $service_details['seller_services'];

                    $data['reviews'] = $review_list;
                } else {
                    $response_code = '500';
                    $response_message = "No Details found";
                    $data = new stdClass();
                }
            } else {
                $response_code = '500';
                $response_message = "Service id missing";
                $data = new stdClass();
            }
        } else {
            $this->token_error();
        }

        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function all_services_post() {
        if($this->api_token){
        $user_data = array();
        $data = array();
        $user_data = $this->post();
        $inputs['page'] = (!empty($inputs['page'])) ? $inputs['page'] : 1;
        if (!empty($user_data['type']) && !empty($user_data['latitude']) && !empty($user_data['longitude'])) {
            
            $user_id = $this->api->get_users_id_using_token($this->api_token);
            $response = $this->api->all_services($user_data,$user_id);

            if (!empty($response['service_list'])) {

                $response_code = '200';
                $response_message = 'All Service';
                $data = $response;
            } else {
                $response_code = '200';
                $response_message = 'No Services Found';
                $data['service_list'] = array();
            }
        } else {
            $response_code = '200';
            $response_message = 'Input field missing';
            $data = (object) array();
        }

        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    
    
    }else{
         $this->token_error();
        }
    }
    public function category_get() {



        $data = array();
        $category_list = $this->api->get_categories();
        if (!empty($category_list)) {
            $response_code = '200';
            $response_message = "Category List";
            $data['category_list'] = $category_list;
        } else {
            $response_code = '200';
            $response_message = "No Results found";
        }


        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function subcategory_post() {

        $data['subcategory_list'] = array();
        $user_post_data = $this->post();
        $subcategory_list = $this->api->get_subcategories($user_post_data['category']);
        if (!empty($subcategory_list)) {
            $response_code = '200';
            $response_message = "Subcategory List";
            $data['subcategory_list'] = $subcategory_list;
        } else {
            $response_code = '200';
            $response_message = "No Results found";
        }


        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

     public function provider_signin_post() {

        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {
            $data = array();
            $user_data = array();
            $user_data = $this->post();


            if (!empty($user_data['mobileno']) && !empty($user_data['otp']) && !empty($user_data['country_code'])) {
                $is_available_mobile = $this->api->check_mobile_no($user_data);
                $is_available_user = $this->api->check_user_mobileno($user_data);

                if ($is_available_user == 0) {

                    if ($is_available_mobile == 1) {


                        $check_data['mobile_number'] = $user_data['mobileno'];
                        $check_data['otp'] = $user_data['otp'];
                        $check_data['country_code'] = $user_data['country_code'];

                        $check = $this->api->check_otp($check_data);

                        if (is_array($check) && !empty($check)) {


                            $mobile_number = $user_data['mobileno'];
                            $user_details = $this->api->get_provider_details($mobile_number, $user_data);
                        }
                        if (!empty($user_details)) {
                            $response_code = '200';
                            $response_message = 'LoggedIn Successfully';
                            $data['provider_details'] = $user_details;
                        } else {
                            $response_code = '202';
                            $response_message = 'Login failed, Invalid OTP or mobile number';
                        }
                    } else {
                        $response_code = '500';
                        $response_message = 'Mobile number does not exits';
                    }
                } else {
                    $response_code = '500';
                    $response_message = 'This number is already registered as User.';
                }
            } else if(!empty($user_data['email']) && !empty($user_data['password'])){
				
                $is_available_email = $this->api->check_email($user_data);
				
				if ($is_available_email == 1) {
					$check = $this->api->check_email($user_data);
					
                        if ($check  == 1) {
                            $email = $user_data['email'];
							$password = md5($user_data['password']);
                            $user_details = $this->api->get_provider_details_by_email($email,$password,$user_data);
							
                        }
                        if (!empty($user_details)) {
                            $response_code = '200';
                            $response_message = 'LoggedIn Successfully';
                            $data['provider_details'] = $user_details;
                        } else {
                            $response_code = '202';
                            $response_message = 'Login failed, Invalid Email Id';
                        }
				}else{
					$response_code = '500';
					$response_message = 'Email id does not exists';
				}
				
			} else {
                $response_code = '500';
                $response_message = 'Inputs field missing';
            }


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function update_provider_post() {

        ini_set('post_max_size', '100M');
        ini_set('upload_max_filesize', '100M');
        ini_set('max_execution_time', -1);
        ini_set('memory_limit', '128M');

        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {
            $data = array();
            $user_data = array();
            $user_data = $this->post();


            /*
             * Currency Update Start
             */

            if (!empty($this->post('user_currency'))) {
                $currency = $this->post('user_currency');
                $user_data['currency_code'] = $user_data['user_currency'];
                $user_id = $this->user_id;
                $token = $this->api_token;

                $user_wallet = $this->Stripe_model->get_wallet_pro($token);
                $user_info = $this->Stripe_model->get_provider($token);
                $wallet_history = $this->Stripe_model->get_wallet_history_info($token, $currency);
                $credit = $debit = 0;


                if (count($wallet_history) > 0) {
                    foreach ($wallet_history as $key => $value) {




                        if ($value['credit_wallet'] != 0) {

                            $credit_amt = get_gigs_currency($value['credit_wallet'], $value['currency_code'], $this->post('user_currency'));


                            $credit += round($credit_amt, 2);
                        }
                        if ($value['debit_wallet'] != 0) {
                            $debit_amt = get_gigs_currency($value['debit_wallet'], $value['currency_code'], $this->post('user_currency'));
                            $debit += round($debit_amt, 2);
                        }
                    }
                }

//                $currency_rate = $credit - $debit;
                $currency_rate = get_gigs_currency($user_wallet['wallet_amt'], $user_info->currency_code, $this->post('user_currency'));
            
                $this->db->where('token', $token)->update('wallet_table', ['currency_code' => $this->post('user_currency'), 'wallet_amt' => $currency_rate]);
            }

            /*
             * Currency Update End
             */



            if (!empty($user_data['name']) || !empty($user_data['category']) || !empty($user_data['subcategory']) || !empty($user_data['profile_img'])) {

                if (!empty($_FILES['profile_img'])) {

                    $config['upload_path'] = FCPATH . 'uploads/profile_img';
                    $config['allowed_types'] = 'jpeg|jpg|png|gif|JPEG|JPG|PNG|GIF';
                    $new_name = time() . 'user';
                    $config['file_name'] = $new_name;
                    $this->load->library('upload', $config);
                    $this->upload->initialize($config);


                    if (!$this->upload->do_upload('profile_img')) {
                        $upload_data = $this->upload->display_errors();
                        $user_data['profile_img'] = '';
                        $profile_img = $upload_data;
                    } else {
                        $upload_data = $this->upload->data();
                        $upload_url = 'uploads/profile_img/';
                        $user_data['profile_img'] = 'uploads/profile_img/' . $upload_data['file_name'];
                        $this->image_resize(200, 200, $user_data['profile_img'], $upload_data['file_name'], $upload_url);
                    }
                } else {
                    
                }
                $WHERE = array('id' => $this->user_id);
                unset($this->user_id);
                unset($user_data['user_currency']);

//                print_r($user_data);exit;

                $result = $this->admin->update_data('providers', $user_data, $WHERE);
//                echo $this->db->last_query();exit;
                if ($result) {
                    $response_code = '200';
                    $response_message = 'Profile updated successfully';
                    $data = $this->api->profile(array('user_id' => $WHERE['id']));
                } else {
                    $response_code = '200';
                    $response_message = 'Provider service failed';
                    $data = new stdClass();
                }

                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            } else {
                $response_code = '500';
                $response_message = 'Inputs field missing';
            }


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function subcategory_services_post() {

        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {

            $inputs = array();
            $data = array();
            $inputs = $this->post();
            $inputs['page'] = (!empty($inputs['page'])) ? $inputs['page'] : 1;

            $response = $this->api->subcategory_services($inputs);

            if (!empty($response['total_pages'])) {

                $response_code = '200';
                $response_message = 'All Service';

                if ($response['total_pages'] < $response['current_page']) {
                    $response_code = '500';
                    $response_message = 'Invalid Page';
                }

                $data = $response;
            } else {

                $response_code = '200';
                $response_message = 'No Records found';
            }

            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function subscription_get() {

        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {

            $result = $this->api->subscription();
//            print_r($result);exit;
            if (!empty($result)) {
                $provider_currency = get_api_provider_currency($this->user_id);
                $ProviderCurrency = $provider_currency['user_currency_code'];
                foreach ($result as $details) {
                
                $fee=(!empty($ProviderCurrency) && $details['currency_code'] != '') ? get_gigs_currency($details['fee'], $details['currency_code'], $ProviderCurrency) : $details['fee'];

                    $res['id'] = $details['id'];
                    $res['subscription_name'] = $details['subscription_name'];
                    $res['fee'] =  (string) $fee;
                    $res['currency_code'] = $ProviderCurrency;
                    $res['currency'] = (!empty($ProviderCurrency)) ? currency_code_sign($ProviderCurrency) : currency_code_sign(settings('currency'));
                    $res['duration'] = $details['duration'];
                    $res['fee_description'] = $details['fee_description'];
                    $res['status'] = $details['status'];

                    $results[] = $res;
                }
            }

            if ($result) {
                $response_code = '200';
                $response_message = 'Subscription listed successfully';
                $data['subscription_list'] = $results;
            } else {
                $response_code = '200';
                $response_message = 'No Records found';
            }

            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function subscription_success_post() {

        $user_data = array();
        $user_data = getallheaders(); // Get Header Data
        $user_post_data = $this->post();
        $user_data = array_merge($user_data, $user_post_data);

        $token = (!empty($user_data['token'])) ? $user_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_data['Token'])) ? $user_data['Token'] : '';
        }

        $data = array();
        $response_code = '500';
        $response_message = 'Validation error';

        if (!empty($token)) {

            $result = $this->api->token_is_valid_provider($token);

            if ($result) {

                if (!empty($user_data['subscription_id']) && !empty($user_data['transaction_id'])) {
                    $user_data['token'] = $token;
                    $result = $this->api->subscription_success($user_data);

                    $user_id = $this->api->get_user_id_using_token($token);

                    $this->db->select('subscription_id');

                    $this->db->where('subscriber_id', $user_id);

                    $subscription = $this->db->get('subscription_details')->row_array();

                    if (!empty($subscription)) {

                        $id = $subscription['subscription_id'];

                        $this->db->select('id,subscription_name');

                        $this->db->where('id', $id);

                        $subscription = $this->db->get('subscription_fee')->row_array();

                        $subscribed_user = 1;

                        $subscribed_msg = $subscription['subscription_name'];
                    } else {

                        $subscribed_user = 0;

                        $subscribed_msg = 'Free';
                    }


                    if (!empty($result)) {

                        $res['id'] = $result['id'];
                        $res['subscription_id'] = $result['subscription_id'];
                        $res['subscriber_id'] = $result['subscriber_id'];
                        $res['subscription_date'] = $result['subscription_date'];
                        $res['expiry_date_time'] = $result['expiry_date_time'];
                        $res['type'] = $result['type'];
                        $res['is_subscribed'] = "$subscribed_user";




                        $response_code = '200';
                        $response_message = 'Subscribed Successfully';
                        $data = $res;
                    } else {
                        $response_code = '201';
                        $response_message = 'Something went wrong. Please try again later.';
                    }
                } else {

                    $response_code = '201';
                    $response_message = 'Input field missing';
                }
            } else {

                $response_code = '202';
                $response_message = 'Invalid user';
            }
        } else {

            $response_code = '201';
            $response_message = 'User token missing';
        }

        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function profile_get() {

        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {
            $user_data['token'] = $this->api_token;
            $user_data['user_id'] = $this->user_id;
            $result = $this->api->profile($user_data);

            if ($result) {

                if (!empty($result['currency_code'])) {
                    $result['currency_id'] = $this->db->where('currency_code', $result['currency_code'])->get('currency_rate')->row()->id;
                } else {
                    $result['currency_id'] = "";
                }

                $response_code = '200';
                $response_message = 'Profile found';
            } else {
                $response_code = '200';
                $response_message = 'No Records found';
            }
            $data = $result;
            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function add_service_post() {



        ini_set('post_max_size', '100M');
        ini_set('upload_max_filesize', '100M');
        ini_set('max_execution_time', -1);
        ini_set('memory_limit', '128M');


        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {

            $user_data = $this->input->post();
            // print_r($user_data);exit;

            if (!empty($user_data['service_title']) && !empty($user_data['service_location']) && !empty($user_data['category']) && !empty($user_data['subcategory']) && !empty($user_data['service_latitude']) && !empty($user_data['service_longitude']) && !empty($user_data['service_amount']) && !empty($user_data['service_offered']) && !empty($user_data['about'])) {



                $inputs = array();

                $config["upload_path"] = './uploads/services/';
                $config["allowed_types"] = '*';
                $this->load->library('upload', $config);
                $this->upload->initialize($config);

                $service_image = array();
                $thumb_image = array();
                $mobile_image = array();



                if (isset($_FILES["images"]["name"])) {
                    $count = count($_FILES["images"]['name']);


                    if ($count >= 3) {
                        for ($i = 0; $i < $count; $i++) {
                            $_FILES["file"]["name"] = 'full_' . time() . $_FILES["images"]["name"][$i];
                            $_FILES["file"]["type"] = $_FILES["images"]["type"][$i];
                            $_FILES["file"]["tmp_name"] = $_FILES["images"]["tmp_name"][$i];
                            $_FILES["file"]["error"] = $_FILES["images"]["error"][$i];
                            $_FILES["file"]["size"] = $_FILES["images"]["size"][$i];
                            if ($this->upload->do_upload('file')) {
                                $data = $this->upload->data();
                                $image_url = 'uploads/services/' . $data["file_name"];
                                $upload_url = 'uploads/services/';
                                $service_image[] = $this->image_resize(360, 220, $image_url, 'se_' . $data["file_name"], $upload_url);
                                $service_details_image[] = $this->image_resize(820, 440, $image_url, 'de_' . $data["file_name"], $upload_url);
                                $thumb_image[] = $this->image_resize(60, 60, $image_url, 'th_' . $data["file_name"], $upload_url);
                                $mobile_image[] = $this->image_resize(280, 160, $image_url, 'mo_' . $data["file_name"], $upload_url);
                            }
                        }
                    } else {
                        $data = new stdClass();
                        $response_code = '500';
                        $response_message = 'Minimum 3 images required';

                        $result = $this->data_format($response_code, $response_message, $data);
                        $this->response($result, REST_Controller::HTTP_OK);
                    }
                }
            
            $provider_currency = get_api_provider_currency($this->user_id);
            $ProviderCurrency = $provider_currency['user_currency_code'];
			
                $inputs['user_id'] = $this->user_id;

                $inputs['service_title'] = $this->input->post('service_title');
            	$inputs['currency_code'] = $ProviderCurrency;
                $inputs['category'] = $this->input->post('category');
                $inputs['subcategory'] = $this->input->post('subcategory');
                $inputs['service_location'] = $this->input->post('service_location');
                $inputs['service_latitude'] = $this->input->post('service_latitude');
                $inputs['service_longitude'] = $this->input->post('service_longitude');
                $inputs['service_amount'] = $this->input->post('service_amount');
                $inputs['service_image'] = (!empty($service_image)) ? $service_image[0] : '';
                $inputs['service_offered'] = $this->input->post('service_offered');
                $inputs['about'] = $this->input->post('about');

                $inputs['created_at'] = date('Y-m-d H:i:s');
                $inputs['updated_at'] = date('Y-m-d H:i:s');

                $result = $this->api->create_service($inputs);


                $temp = count($service_image); //counting number of row's
                $service_image = $service_image;
                $service_details_image = (!empty($service_details_image)) ? $service_details_image : '';
                $thumb_image = $thumb_image;
                $mobile_image = $mobile_image;
                $service_id = $result;



                for ($j = 0; $j < $temp; $j++) {
                    $image = array(
                        'service_id' => $service_id,
                        'service_image' => $service_image[$j],
                        'service_details_image' => $service_details_image[$j],
                        'thumb_image' => $thumb_image[$j],
                        'mobile_image' => $mobile_image[$j]);


                    $serviceimage = $this->api->insert_serviceimage($image);
                }



                if ($serviceimage) {
                    $response_code = '200';
                    $response_message = 'Service added successfully';
                } else {
                    $response_code = '500';
                    $response_message = 'Add service failed';
                }
                $data = new stdClass();
                $res = $this->data_format($response_code, $response_message, $data);

                $this->response($res, REST_Controller::HTTP_OK);
            } else {
                $response_code = '500';
                $response_message = 'Add service failed, required fields empty';
                $data = new stdClass();
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            }
        } else {
            $this->token_error();
        }
    }

    public function update_service_post() {

        ini_set('post_max_size', '100M');
        ini_set('upload_max_filesize', '100M');
        ini_set('max_execution_time', -1);
        ini_set('memory_limit', '128M');

        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {
			 $provider_id = $this->user_id;
			 $provider_currency = get_api_provider_currency($provider_id);
			 $ProviderCurrency = $provider_currency['user_currency_code'];
            $user_data = $this->post();

            if (!empty($user_data['service_title']) && !empty($user_data['service_location']) && !empty($user_data['category']) && !empty($user_data['subcategory']) && !empty($user_data['service_latitude']) && !empty($user_data['service_longitude']) && !empty($user_data['service_amount']) && !empty($user_data['service_offered']) && !empty($user_data['about']) && !empty($user_data['id'])) {

                $inputs = array();

                $config["upload_path"] = './uploads/services/';
                $config["allowed_types"] = '*';
                $this->load->library('upload', $config);
                $this->upload->initialize($config);

                $service_image = array();
                $thumb_image = array();
                $mobile_image = array();

                if (isset($_FILES["images"])) {
                    $count = count($_FILES["images"]);
                    if ($count >= 3) {
                        for ($count = 0; $count < count($_FILES["images"]["name"]); $count++) {
                            $_FILES["file"]["name"] = 'full_' . time() . $_FILES["images"]["name"][$count];
                            $_FILES["file"]["type"] = $_FILES["images"]["type"][$count];
                            $_FILES["file"]["tmp_name"] = $_FILES["images"]["tmp_name"][$count];
                            $_FILES["file"]["error"] = $_FILES["images"]["error"][$count];
                            $_FILES["file"]["size"] = $_FILES["images"]["size"][$count];
                            if ($this->upload->do_upload('file')) {
                                $data = $this->upload->data();
                                $image_url = 'uploads/services/' . $data["file_name"];
                                $upload_url = 'uploads/services/';
                                $service_image[] = $this->image_resize(360, 220, $image_url, 'se_' . $data["file_name"], $upload_url);
                                $service_details_image[] = $this->image_resize(820, 440, $image_url, 'de_' . $data["file_name"], $upload_url);
                                $thumb_image[] = $this->image_resize(60, 60, $image_url, 'th_' . $data["file_name"], $upload_url);
                                $mobile_image[] = $this->image_resize(280, 160, $image_url, 'mo_' . $data["file_name"], $upload_url);
                            }
                        }
                        $inputs['service_image'] = implode(',', $service_image);
                        $inputs['service_details_image'] = implode(',', $service_details_image);
                        $inputs['thumb_image'] = implode(',', $thumb_image);
                        $inputs['mobile_image'] = implode(',', $mobile_image);
                    } else {
                        $data = new stdClass();

                        $response_code = '200';
                        $response_message = 'Minimum 3 images required';

                        $result = $this->data_format($response_code, $response_message, $data);
                        $this->response($result, REST_Controller::HTTP_OK);
                    }
                }


                $inputs['service_title'] = $this->input->post('service_title');
                $inputs['category'] = $this->input->post('category');
                $inputs['subcategory'] = $this->input->post('subcategory');
                $inputs['currency_code'] = $ProviderCurrency;
                $inputs['service_location'] = $this->input->post('service_location');
                $inputs['service_latitude'] = $this->input->post('service_latitude');
                $inputs['service_longitude'] = $this->input->post('service_longitude');
                $inputs['service_amount'] = $this->input->post('service_amount');

                $inputs['service_offered'] = $this->input->post('service_offered');
                $inputs['about'] = $this->input->post('about');

                $inputs['updated_at'] = date('Y-m-d H:i:s');
                $WHERE = array('id' => $user_data['id']);
                $result = $this->api->update_service($inputs, $WHERE);


                if (is_array($service_image) && count($service_image) > 0) {
                    $temp = count($service_image);
                    $service_image = $service_image;
                    $service_details_image = $service_details_image;
                    $thumb_image = $thumb_image;
                    $mobile_image = $mobile_image;
                    $service_id = $user_data['id'];



                    for ($i = 0; $i < $temp; $i++) {
                        $image = array(
                            'service_id' => $service_id,
                            'service_image' => $service_image[$i],
                            'service_details_image' => $service_details_image[$i],
                            'thumb_image' => $thumb_image[$i],
                            'mobile_image' => $mobile_image[$i]
                        );
                        $serviceimage = $this->api->insert_serviceimage($image);
                    }
                }

                if ($result) {
                    $response_code = '200';
                    $response_message = 'Service updated successfully';
                } else {
                    $response_code = '200';
                    $response_message = 'Update service failed';
                }
                $data = new stdClass();
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            } else {
                $response_code = '200';
                $response_message = 'Update service failed, required fields value missing';
                $data = new stdClass();
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            }
        } else {
            $this->token_error();
        }
    }

    public function delete_service_post() {
        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {

            $user_data = $this->post();
            if (!empty($user_data['id'])) {
                $inputs['status'] = '0';
                $WHERE = array('id' => $user_data['id']);
                $result = $this->api->update_service($inputs, $WHERE);

                if ($result) {
                    $response_code = '200';
                    $response_message = 'Service deleted successfully';
                } else {
                    $response_code = '200';
                    $response_message = 'Service delete failed';
                }
                $data = new stdClass();
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            } else {
                $response_code = '200';
                $response_message = 'Service delete failed';
                $data = new stdClass();
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            }
        } else {
            $this->token_error();
        }
    }

    public function delete_serviceimage_post() {
        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {

            $user_data = $this->post();

            if (!empty($user_data['id']) && !empty($user_data['service_id'])) {
                $service_id = $user_data['service_id'];
                $get_serviceimg = $this->api->get_serviceimg($service_id);

                if ($get_serviceimg > 1) {
                    $inputs['status'] = '0';
                    $WHERE = array('id' => $user_data['id'], 'service_id' => $user_data['service_id']);
                    $result = $this->api->delete_service($inputs, $WHERE);

                    if ($result) {
                        $response_code = '200';
                        $response_message = 'Service image deleted successfully';
                    } else {
                        $response_code = '500';
                        $response_message = 'Service image deletion failed';
                    }
                } else {
                    $response_code = '500';
                    $response_message = 'You have only one service image, So you cant delete';
                }
                $data = new stdClass();
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            } else {
                $response_code = '500';
                $response_message = 'Input field missing';
                $data = new stdClass();
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            }
        } else {
            $this->token_error();
        }
    }

    public function image_resize($width = 0, $height = 0, $image_url, $filename, $upload_url) {

        $source_path = base_url() . $image_url;
        list($source_width, $source_height, $source_type) = getimagesize($source_path);
        switch ($source_type) {
            case IMAGETYPE_GIF:
                $source_gdim = imagecreatefromgif($source_path);
                break;
            case IMAGETYPE_JPEG:
                $source_gdim = imagecreatefromjpeg($source_path);
                break;
            case IMAGETYPE_PNG:
                $source_gdim = imagecreatefrompng($source_path);
                break;
        }

        $source_aspect_ratio = $source_width / $source_height;
        $desired_aspect_ratio = $width / $height;

        if ($source_aspect_ratio > $desired_aspect_ratio) {
            /*
             * Triggered when source image is wider
             */
            $temp_height = $height;
            $temp_width = (int) ($height * $source_aspect_ratio);
        } else {
            /*
             * Triggered otherwise (i.e. source image is similar or taller)
             */
            $temp_width = $width;
            $temp_height = (int) ($width / $source_aspect_ratio);
        }

        /*
         * Resize the image into a temporary GD image
         */

        $temp_gdim = imagecreatetruecolor($temp_width, $temp_height);
        imagecopyresampled(
                $temp_gdim, $source_gdim, 0, 0, 0, 0, $temp_width, $temp_height, $source_width, $source_height
        );

        /*
         * Copy cropped region from temporary image into the desired GD image
         */

        $x0 = ($temp_width - $width) / 2;
        $y0 = ($temp_height - $height) / 2;
        $desired_gdim = imagecreatetruecolor($width, $height);
        imagecopy(
                $desired_gdim, $temp_gdim, 0, 0, $x0, $y0, $width, $height
        );

        /*
         * Render the image
         * Alternatively, you can save the image in file-system or database
         */

        $image_url = $upload_url . $filename;

        imagepng($desired_gdim, $image_url);

        return $image_url;

        /*
         * Add clean-up code here
         */
    }

    public function subscription_payment_post() {

        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {
            $params = $this->post();
            if (!empty($params['amount']) && !empty($params['tokenid']) && !empty($params['description']) && !empty($params['subscription_id']) && $params['amount'] > 0) {
                $charges_array = array();
                $amount = $params['amount'];
                $amount = ($amount * 100);
                $charges_array['amount'] = $amount;
                $charges_array['currency'] = settings('currency');
                $charges_array['description'] = $params['description'];
                $charges_array['source'] = $params['tokenid'];

                $result = $this->stripe->stripe_charges($charges_array);

                $result = json_decode($result, true);

                if (empty($result['error'])) {
                    $data['transaction_id'] = $result['id'];
                    $data['payment_details'] = json_encode($result);

                    $response_code = '200';
                    $response_message = 'Stripe payment success';
                } else {
                    $response_code = '200';
                    $response_message = 'Stripe payment issue';
                    $data['error'] = $result['error'];
                }
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            } else {
                $response_code = '200';
                $response_message = 'Stripe payment issue';
                $data['error'] = $result['error'];

                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            }
        } else {
            $this->token_error();
        }
    }

    public function data_format($response_code, $response_message, $data) {
        $final_result = array();
        $response = array();
        $response['response_code'] = $response_code;
        $response['response_message'] = $response_message;

        if (!empty($data)) {

            $data = $data;
        } else {

            $data = $data;
        }

        $final_result['response'] = $response;
        $final_result['data'] = $data;

        return $final_result;
    }

    public function edit_service_get() {

        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {

            if (isset($_GET['id']) && !empty($_GET['id'])) {
                $inputs = array();
                $inputs['id'] = $this->get('id');
                $token=$this->api_token;
                $user_id = $this->api->get_user_id_using_token($token);
                $inputs['user_id']=$user_id;
                

                $service_id = $this->api->get_service_id($inputs['id']);

                if (!empty($service_id)) {
                    $service_details = $this->api->get_service_info($inputs);

                    if (!empty($service_details)) {
                        $response_code = '200';
                        $response_message = "Service Details";
                        $data = $service_details;
                    }
                } else {
                    $response_code = '200';
                    $response_message = "No Details found";

                    $data = [];
                }
            } else {
                $response_code = '500';
                $response_message = "Service id missing";
                $data = [];
            }



            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function add_availability_post() {
        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {

            $user_data = $this->post();

            if (!empty($user_data['availability'])) {

                $user_data['provider_id'] = $this->user_id;
                $check_provider = $this->api->provider_hours($this->user_id);

                if (empty($check_provider)) {
                    $result = $this->api->insert_businesshours($user_data);

                    if ($result) {
                        $response_code = '200';
                        $response_message = 'Business hours added successfully';
                    } else {
                        $response_code = '200';
                        $response_message = 'Business hours added failed';
                    }
                    $data = new stdClass();
                    $result = $this->data_format($response_code, $response_message, $data);
                    $this->response($result, REST_Controller::HTTP_OK);
                } else {
                    $response_code = '200';
                    $response_message = 'Business hours already exists';
                }
                $data = new stdClass();
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            } else {
                $response_code = '500';
                $response_message = 'Input field missing';
                $data = new stdClass();
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            }
        } else {
            $this->token_error();
        }
    }

    public function update_availability_post() {
        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {

            $user_data = $this->post();

            if (!empty($user_data['availability'])) {
                $provider_id = $this->user_id;
                $check_provider = $this->api->provider_hours($this->user_id);


                if (empty($check_provider)) {

                    $user_data['provider_id'] = $this->user_id;
                    $result = $this->api->insert_businesshours($user_data);

                    if ($result) {
                        $response_code = '200';
                        $response_message = 'Business hours added successfully';
                    } else {
                        $response_code = '200';
                        $response_message = 'Business hours added failed';
                    }
                    $data = new stdClass();
                    $result = $this->data_format($response_code, $response_message, $data);
                    $this->response($result, REST_Controller::HTTP_OK);
                } else {

                    $provider_id = $this->user_id;

                    $WHERE = array('provider_id' => $provider_id);
                    $result = $this->api->update_availability($user_data, $WHERE);

                    if ($result) {
                        $response_code = '200';
                        $response_message = 'Availability updated successfully';
                    } else {
                        $response_code = '200';
                        $response_message = 'Availability updation failed';
                    }
                    $data = new stdClass();
                    $result = $this->data_format($response_code, $response_message, $data);
                    $this->response($result, REST_Controller::HTTP_OK);
                }
            } else {
                $response_code = '200';
                $response_message = 'Input field missing';
                $data = new stdClass();
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            }
        } else {
            $this->token_error();
        }
    }

    public function availability_get() {

        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {


            $inputs = array();
            $provider_id = $this->user_id;
            $result = $this->api->get_availability($provider_id);

            if (!empty($result)) {

                $response_code = '200';
                $response_message = "Availability Details";
                $data = $result;
            } else {
                $res['id'] = "";
                $res['provider_id'] = "";
                $res['availability'] = "";

                $response_code = '200';
                $response_message = "No Details found";
                $data = $res;
            }




            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function logout_provider_post() {
        $user_data = array();
        $user_data = getallheaders();
        $user_post_data = $this->post();
        $user_data = array_merge($user_data, $user_post_data);
        $token = $this->api_token;

        if (empty($token)) {
            $token = (!empty($header['Token'])) ? $header['Token'] : '';
        }
        $user_data['token'] = $token;


        $data = array();
        $response_code = '-1';
        $response_message = 'validation error';

        if (!empty($user_data['token'])) {
            if (!empty($user_data['device_type']) && !empty($user_data['device_id'])) {



                $result = $this->api->logout_provider($user_data['token'], $user_data['device_type'], $user_data['device_id']);

                if ($result) {

                    $response_code = '200';
                    $response_message = 'Logout successfully';
                } else {

                    $response_code = '202';
                    $response_message = 'Invalid user token';
                }
            } else {

                $response_code = '201';
                $response_message = 'Required input is missing';
            }
        } else {

            $response_code = '201';
            $response_message = 'user token is missing';
        }

        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function user_signin_post() {
        if ($this->users_id != 0 || ($this->default_token == $this->api_token)) {
            $data = array();
            $user_data = array();
            $user_data = $this->post();


            if (!empty($user_data['mobileno']) && !empty($user_data['otp']) && !empty($user_data['country_code'])) {


                $is_available_mobile = $this->api->check_user_mobileno($user_data);
                $is_available_provider = $this->api->check_mobile_no($user_data);
                if ($is_available_provider == 0) {
                    if ($is_available_mobile == 1) {

                        $check_data['mobile_number'] = $user_data['mobileno'];
                        $check_data['otp'] = $user_data['otp'];
                        $check_data['country_code'] = $user_data['country_code'];

                        $check = $this->api->check_otp($check_data);

                        if (is_array($check) && !empty($check)) {
                            $mobile_number = $user_data['mobileno'];
                            $user_details = $this->api->get_user_details($mobile_number, $user_data);
                        }

                        if (!empty($user_details)) {
                            $response_code = '200';
                            $response_message = 'LoggedIn Successfully';
                            $data['user_details'] = $user_details;
                        } else {
                            $response_code = '202';
                            $response_message = 'Login failed, Invalid OTP or mobile number';
                        }
                    } else {
                        $response_code = '500';
                        $response_message = 'Mobile number does not exits';
                    }
                } else {
                    $response_code = '500';
                    $response_message = 'This number is already registered as Provider.';
                }
            }else if (!empty($user_data['email']) && !empty($user_data['password'])) {
                $is_available_email = $this->api->check_user_email($user_data);
				
				if ($is_available_email == 1) {
					$check = $this->api->check_user_email($user_data);
					
                        if ($check  == 1) {
                            $email = $user_data['email'];
                            $password = md5($user_data['password']);
                            $user_details = $this->api->get_user_detailsby_email($email,$password,$user_data);
							
                        }
                        if (!empty($user_details)) {
							
                            $response_code = '200';
                            $response_message = 'LoggedIn Successfully';
                            $data['user_details'] = $user_details;
                        } else {
                            $response_code = '202';
                            $response_message = 'Login failed, Invalid Email Id';
                        }
				}else{
					$response_code = '500';
					$response_message = 'Email id does not exists';
				}
            } else {
                $response_code = '500';
                $response_message = 'Inputs field missing';
            }


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function generate_userotp_post() {
        if ($this->users_id != 0 || ($this->default_token == $this->api_token)) {
            $data = array();
            $user_data = array();
            $user_data = $this->post();

            if ($user_data['usertype'] == 2) {
                if (!empty($user_data['mobileno']) && !empty($user_data['email']) && !empty($user_data['country_code']) && !empty($user_data['usertype'])) {

                    $is_available = $this->api->check_user_email($user_data);
                    $is_available_email = $this->api->check_email($user_data);
                    $is_available_mobile = $this->api->check_mobile_no($user_data);
                    $is_available_mobileno = $this->api->check_user_mobileno($user_data);


                    if ($is_available == 0 && $is_available_email == 0) {
                        if ($is_available_mobile == 0 && $is_available_mobileno == 0) {
                            $default_otp = settingValue('default_otp');
                            if ($default_otp == 1) {
                                $otp = '1234';
                            } else {
                                $otp = rand(1000, 9999);
                            }



                            $message = 'Your OTP for ' . $this->website_name . ' is ' . $otp . '';
                            $this->load->library('sms');
                            $result = $this->sms->send_message($user_data['country_code'] . $user_data['mobileno'], $message);
                            $otp_data = array(
                                'endtime' => time() + 300,
                                'mobile_number' => $user_data['mobileno'],
                                'country_code' => $user_data['country_code'],
                                'otp' => $otp
                            );
                            $save_otp = $this->api->save_otp($otp_data);
                            $response_code = '200';
                            $response_message = 'OTP send successfully';
                            $data['usertype'] = $user_data['usertype'];
                        } else {
                            $response_code = '500';
                            $response_message = 'Mobile no already exits';
                        }
                    } else {
                        $response_code = '500';
                        $response_message = 'Email id already exits';
                    }
                } else {
                    $response_code = '500';
                    $response_message = 'Inputs field missing';
                }
            } elseif ($user_data['usertype'] == 1) {
                if (!empty($user_data['mobileno']) && !empty($user_data['country_code'])) {

                    $is_available_mobile = $this->api->check_user_mobileno($user_data);


                    if ($is_available_mobile == 1) {
                        $default_otp = settingValue('default_otp');
                        if ($default_otp == 1) {
                            $otp = '1234';
                        } else {
                            $otp = rand(1000, 9999);
                        }



                        $message = 'Your OTP for ' . $this->website_name . ' is ' . $otp . '';
                        $this->load->library('sms');
                        $result = $this->sms->send_message($user_data['country_code'] . $user_data['mobileno'], $message);
                        $otp_data = array(
                            'endtime' => time() + 300,
                            'mobile_number' => $user_data['mobileno'],
                            'country_code' => $user_data['country_code'],
                            'otp' => $otp,
                            'status' => 1
                        );
                        $save_otp = $this->api->save_otp($otp_data);
                        $response_code = '200';
                        $response_message = 'OTP send successfully';
                        $data['usertype'] = $user_data['usertype'];
                    } elseif ($is_available_mobile == 0) {

                        $data['usertype'] = '2';
                        $response_code = '500';
                        $response_message = 'Mobile number does not exists';
                    }
                } else {
                    $response_code = '500';
                    $response_message = 'Inputs field missing';
                }
            }


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function logout_post() {
        $user_data = array();
        $user_data = getallheaders();
        $user_post_data = $this->post();
        $user_data = array_merge($user_data, $user_post_data);
        $token = $this->api_token;

        if (empty($token)) {
            $token = (!empty($header['Token'])) ? $header['Token'] : '';
        }
        $user_data['token'] = $token;


        $data = new stdClass();
        $response_code = '-1';
        $response_message = 'validation error';

        if (!empty($user_data['token']) && !empty($user_data['device_type']) && !empty($user_data['deviceid'])) {


            $result = $this->api->logout($user_data['token'], $user_data['device_type'], $user_data['deviceid']);

            if ($result) {

                $response_code = '200';
                $response_message = 'Logout successfully';
            } else {

                $response_code = '202';
                $response_message = 'Invalid user token';
            }
        } else {

            $response_code = '201';
            $response_message = 'Input Field is missing';
        }

        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function update_user_post() {

        if ($this->users_id != 0 || ($this->default_token == $this->api_token)) {
            $data = array();
            $user_data = array();
            $user_data = $this->post();

            /*
             * Currency Update Start
             */

            if (!empty($this->post('user_currency'))) {
                $currency = $this->post('user_currency');
                $user_data['currency_code'] = $user_data['user_currency'];
                $user_id = $this->user_id;
                $token = $this->api_token;

                $user_wallet = $this->Stripe_model->get_wallet_new($token);
                $user_info = $this->Stripe_model->get_user($token);
                $wallet_history = $this->Stripe_model->get_wallet_history_info($token, $currency);
                $credit = $debit = 0;


                if (count($wallet_history) > 0) {
                    foreach ($wallet_history as $key => $value) {

                        if ($value['credit_wallet'] != 0) {

                            $credit_amt = get_gigs_currency($value['credit_wallet'], $value['currency_code'], $currency);

                            $credit += round($credit_amt, 2);
                        }
                        if ($value['debit_wallet'] != 0) {
                            $debit_amt = get_gigs_currency($value['debit_wallet'], $value['currency_code'], $currency);
                            $debit += round($debit_amt, 2);
                        }
                    }
                }

                // $currency_rate = $credit - $debit;
                $currency_rate = get_gigs_currency($user_wallet['wallet_amt'], $user_info->currency_code, $currency);
                $this->db->where('token', $token)->update('wallet_table', ['currency_code' => $currency, 'wallet_amt' => $currency_rate]);
            }

            /*
             * Currency Update End
             */


            if (!empty($user_data['name']) || !empty($user_data['email']) || !empty($user_data['mobileno']) || !empty($user_data['country_code']) || !empty($_FILES['profile_img'])) {
                if (!empty($_FILES['profile_img'])) {



                    $config['upload_path'] = FCPATH . 'uploads/profile_img';
                    $config['allowed_types'] = 'jpeg|jpg|png|gif|JPEG|JPG|PNG|GIF';
                    $new_name = time() . 'user';
                    $config['file_name'] = $new_name;
                    $this->load->library('upload', $config);
                    $this->upload->initialize($config);


                    if (!$this->upload->do_upload('profile_img')) {
                        $upload_data = $this->upload->display_errors();
                        $user_data['profile_img'] = '';
                        $profile_img = $upload_data;
                    } else {
                        $upload_data = $this->upload->data();
                        $upload_url = 'uploads/profile_img/';
                        $user_data['profile_img'] = 'uploads/profile_img/' . $upload_data['file_name'];
                        $this->image_resize(200, 200, $user_data['profile_img'], $upload_data['file_name'], $upload_url);
                    }
                } else {
                    
                }
                if (!empty($user_data['type'])) {
                    $WHERE = array('id' => $this->users_id);
                    unset($this->users_id);
                    unset($user_data['user_currency']);

                    $result = $this->admin->update_data('users', $user_data, $WHERE);
                    if ($result) {
                        $response_code = '200';
                        $response_message = 'Profile updated successfully';
                        $data = $this->api->user_profile(array('user_id' => $WHERE['id']));
                    } else {
                        $response_code = '200';
                        $response_message = 'Provider service failed';
                        $data = new stdClass();
                    }

                    $result = $this->data_format($response_code, $response_message, $data);
                    $this->response($result, REST_Controller::HTTP_OK);
                } else {
                    $response_code = '500';
                    $response_message = 'Inputs field missing';
                }
            } else {
                $response_code = '500';
                $response_message = 'Inputs field missing';
            }


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function user_profile_get() {

        if ($this->users_id != 0 || ($this->default_token == $this->api_token)) {
            $user_data['token'] = $this->api_token;
            $user_data['user_id'] = $this->users_id;
            $result = $this->api->user_profile($user_data);

            if ($result) {

                if (!empty($result['currency_code'])) {
                    $result['currency_id'] = $this->db->where('currency_code', $result['currency_code'])->get('currency_rate')->row()->id;
                } else {
                    $result['currency_id'] = "";
                }


                $response_code = '200';
                $response_message = 'Profile found';
            } else {
                $response_code = '200';
                $response_message = 'No Records found';
            }
            $data = $result;
            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function service_availability_post() {


        if ($this->users_id != 0 || ($this->default_token == $this->api_token)) {
            $data = array();
            $user_data = array();
            $user_data = $this->post();

            if (!empty($user_data['date']) && !empty($user_data['service_id'])) {

                $timestamp = strtotime($user_data['date']);
                $day = date('D', $timestamp);


                $inputs = $user_data['service_id'];

                $result = $this->api->get_service_id($inputs);
                $provider_details = $this->api->provider_hours($result['user_id']);
                $availability_details = json_decode($provider_details['availability'], true);
                $alldays = false;
                if ($availability_details != '') {
                    foreach ($availability_details as $details) {

                        if (isset($details['day']) && $details['day'] == 0) {

                            if (isset($details['from_time']) && !empty($details['from_time'])) {

                                if (isset($details['to_time']) && !empty($details['to_time'])) {
                                    $from_time = $details['from_time'];
                                    $to_time = $details['to_time'];
                                    $alldays = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                if ($alldays == false) {


                    if ($day == 'Mon') {
                        $weekday = '1';
                    } elseif ($day == 'Tue') {
                        $weekday = '2';
                    } elseif ($day == 'Wed') {
                        $weekday = '3';
                    } elseif ($day == 'Thu') {
                        $weekday = '4';
                    } elseif ($day == 'Fri') {
                        $weekday = '5';
                    } elseif ($day == 'Sat') {
                        $weekday = '6';
                    } elseif ($day == 'Sun') {
                        $weekday = '7';
                    } elseif ($day == '0') {
                        $weekday = '0';
                    }

                    if ($availability_details != '') {
                        foreach ($availability_details as $availability) {

                            if ($weekday == $availability['day'] && $availability['day'] != 0) {

                                $availability_day = $availability['day'];
                                $from_time = $availability['from_time'];
                                $to_time = $availability['to_time'];
                            }
                        }
                    }
                }

                if (!empty($from_time)) {
                    $temp_start_time = $from_time;
                    $temp_end_time = $to_time;
                } else {
                    $response_code = '500';
                    $response_message = 'Availability not found';
                    $data = new stdClass();
                    $res = $this->data_format($response_code, $response_message, $data);

                    $this->response($res, REST_Controller::HTTP_OK);
                }


                $start_time_array = '';
                $end_time_array = '';


                $timestamp_start = strtotime($temp_start_time);
                $timestamp_end = strtotime($temp_end_time);

                $timing_array = array();

                $counter = 1;

                $from_time_railwayhrs = date('G:i:s', ($timestamp_start));
                $to_time_railwayhrs = date('G:i:s', ($timestamp_end));

                $timestamp_start_railwayhrs = strtotime($from_time_railwayhrs);
                $timestamp_end_railwayhrs = strtotime($to_time_railwayhrs);


                $i = 1;
                while ($timestamp_start_railwayhrs < $timestamp_end_railwayhrs) {

                    $temp_start_time_ampm = date('G:i:s', ($timestamp_start_railwayhrs));
                    $temp_end_time_ampm = date('G:i:s', (($timestamp_start_railwayhrs) + 60 * 60 * 1));

                    $timestamp_start_railwayhrs = strtotime($temp_end_time_ampm);

                    $timing_array[] = array('id' => $i, 'start_time' => $temp_start_time_ampm, 'end_time' => $temp_end_time_ampm);

                    if ($counter > 24) {
                        break;
                    }

                    $counter += 1;
                    $i++;
                }


                // Booking availability


                $service_date = $user_data['date'];
                $service_id = $user_data['service_id'];


                $booking_count = $this->api->get_bookings($service_date, $service_id);


                $new_timingarray = array();

                if (is_array($booking_count) && empty($booking_count)) {
                    $new_timingarray = $timing_array;
                } elseif (is_array($booking_count) && $booking_count != '') {
                    foreach ($timing_array as $timing) {
                        $match_found = false;

                        $explode_st_time = explode(':', $timing['start_time']);
                        $explode_value = $explode_st_time[0];

                        $explode_endtime = explode(':', $timing['end_time']);
                        $explode_endval = $explode_endtime[0];


                        if (strlen($explode_value) == 1) {
                            $timing['start_time'] = "0" . $explode_st_time[0] . ":" . $explode_st_time[1] . ":" . $explode_st_time[2];
                        }

                        if (strlen($explode_endval) == 1) {
                            $timing['end_time'] = "0" . $explode_endtime[0] . ":" . $explode_endtime[1] . ":" . $explode_endtime[2];
                        }

                        foreach ($booking_count as $bookings) {


                            if ($timing['start_time'] == $bookings['from_time'] && $timing['end_time'] == $bookings['to_time']) {


                                $match_found = true;
                                break;
                            }
                        }

                        if ($match_found == false) {
                            $new_timingarray[] = array('start_time' => $timing['start_time'], 'end_time' => $timing['end_time']);
                        }
                    }
                }

                $new_timingarray = array_filter($new_timingarray);

                if (!empty($new_timingarray)) {
                    $i = 1;
                    foreach ($new_timingarray as $booked_time) {
                        $re = strtotime($booked_time['start_time']);
                        $re1 = strtotime($booked_time['end_time']);
                        $st_time = date('g:i A', ($re));
                        $end_time = date('g:i A', ($re1));

                        $time['id'] = "$i";
                        $time['start_time'] = $st_time;
                        $time['end_time'] = $end_time;
                        $time['is_selected'] = '0';
                        $service_availability[] = $time;
                        $i++;
                    }
                } else {
                    $service_availability = '';
                }

                $data['service_availability'] = $service_availability;



                if ($service_availability != '') {
                    $response_code = '200';
                    $response_message = 'Availability details';
                } else {
                    $response_code = '200';
                    $response_message = 'Availability not found';
                    $data = new stdClass();
                }

                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            } else {
                $response_code = '500';
                $response_message = 'Inputs field missing';
            }




            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function book_service_post() {

        $user_data = array();
        $user_data = getallheaders(); // Get Header Data
        $user_post_data = $this->post();


        $token = (!empty($user_data['token'])) ? $user_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_data['Token'])) ? $user_data['Token'] : '';
        }


        $data = array();
        $response_code = '-1';
        $response_message = 'Validation error';

        if (!empty($token)) {

            $result = $this->api->token_is_valid($token);
            $results = '';

            if ($result) {



                if (!empty($user_post_data['from_time']) && !empty($user_post_data['to_time']) && !empty($user_post_data['service_date']) && !empty($user_post_data['service_id']) && !empty($user_post_data['latitude']) && !empty($user_post_data['longitude']) && !empty('location') && !empty($user_post_data['notes']) && !empty($user_post_data['amount'])) {
                    $user_post_data['tokenid'] = 'flow changed';

                    $wallet = $this->api->get_wallet($token);

                    $curren_wallet = $wallet['wallet_amt'];

                    /* check wallet amount */

                    if ($user_post_data['amount'] > $curren_wallet) {

                        $response_code = '201';
                        $response_message = 'You do not have sufficient balance in your wallet account. Please Topup to book the service.';
                        $data = new stdClass();

                        $res = $this->data_format($response_code, $response_message, $data);

                        $this->response($res, REST_Controller::HTTP_OK);

                        return false;
                    }
                    /* check wallet amount */


                    $timestamp = strtotime($user_post_data['service_date']);
                    $day = date('D', $timestamp);


                    $inputs = $user_post_data['service_id'];

                    $result = $this->api->get_service_id($inputs);
                    $provider_details = $this->api->provider_hours($result['user_id']);
                    $availability_details = json_decode($provider_details['availability'], true);

                    $alldays = false;
                    foreach ($availability_details as $details) {

                        if (isset($details['day']) && $details['day'] == 0) {

                            if (isset($details['from_time']) && !empty($details['from_time'])) {

                                if (isset($details['to_time']) && !empty($details['to_time'])) {
                                    $from_time = $details['from_time'];
                                    $to_time = $details['to_time'];
                                    $alldays = true;
                                    break;
                                }
                            }
                        }
                    }

                    if ($alldays == false) {


                        if ($day == 'Mon') {
                            $weekday = '1';
                        } elseif ($day == 'Tue') {
                            $weekday = '2';
                        } elseif ($day == 'Wed') {
                            $weekday = '3';
                        } elseif ($day == 'Thu') {
                            $weekday = '4';
                        } elseif ($day == 'Fri') {
                            $weekday = '5';
                        } elseif ($day == 'Sat') {
                            $weekday = '6';
                        } elseif ($day == 'Sun') {
                            $weekday = '7';
                        } elseif ($day == '0') {
                            $weekday = '0';
                        }


                        foreach ($availability_details as $availability) {

                            if ($weekday == $availability['day'] && $availability['day'] != 0) {

                                $availability_day = $availability['day'];
                                $from_time = $availability['from_time'];
                                $to_time = $availability['to_time'];
                            }
                        }
                    }

                    if (!empty($from_time)) {
                        $temp_start_time = $from_time;
                        $temp_end_time = $to_time;
                    } else {
                        $response_code = '500';
                        $response_message = 'Booking not available';
                        $data = new stdClass();
                        $res = $this->data_format($response_code, $response_message, $data);

                        $this->response($res, REST_Controller::HTTP_OK);
                    }



                    $start_time_array = '';
                    $end_time_array = '';


                    $timestamp_start = strtotime($temp_start_time);
                    $timestamp_end = strtotime($temp_end_time);

                    $timing_array = array();

                    $counter = 1;

                    $from_time_railwayhrs = date('G:i:s', ($timestamp_start));
                    $to_time_railwayhrs = date('G:i:s', ($timestamp_end));

                    $timestamp_start_railwayhrs = strtotime($from_time_railwayhrs);
                    $timestamp_end_railwayhrs = strtotime($to_time_railwayhrs);


                    $i = 1;
                    while ($timestamp_start_railwayhrs < $timestamp_end_railwayhrs) {

                        $temp_start_time_ampm = date('G:i:s', ($timestamp_start_railwayhrs));
                        $temp_end_time_ampm = date('G:i:s', (($timestamp_start_railwayhrs) + 60 * 60 * 1));

                        $timestamp_start_railwayhrs = strtotime($temp_end_time_ampm);

                        $timing_array[] = array('id' => $i, 'start_time' => $temp_start_time_ampm, 'end_time' => $temp_end_time_ampm);

                        if ($counter > 24) {
                            break;
                        }

                        $counter += 1;
                        $i++;
                    }

                    $data['availability'] = $timing_array;



                    // Booking availability


                    $booking_from_time = $user_post_data['from_time'];
                    $booking_end_time = $user_post_data['to_time'];

                    $timestamp_from = strtotime($booking_from_time);
                    $timestamp_to = strtotime($booking_end_time);

                    $from_time_railwayhrs = date('G:i:s', ($timestamp_from));
                    $to_time_railwayhrs = date('G:i:s', ($timestamp_to));

                    $service_date = $user_post_data['service_date'];
                    $service_id = $user_post_data['service_id'];


                    $booking_count = $this->api->get_bookings($service_date, $service_id);


                    $new_timingarray = array();

                    if (is_array($booking_count) && empty($booking_count)) {
                        $new_timingarray = $timing_array;
                    } elseif (is_array($booking_count) && $booking_count != '') {
                        foreach ($timing_array as $timing) {
                            $match_found = false;

                            $explode_st_time = explode(':', $timing['start_time']);
                            $explode_value = $explode_st_time[0];

                            $explode_endtime = explode(':', $timing['end_time']);
                            $explode_endval = $explode_endtime[0];


                            if (strlen($explode_value) == 1) {
                                $timing['start_time'] = "0" . $explode_st_time[0] . ":" . $explode_st_time[1] . ":" . $explode_st_time[2];
                            }

                            if (strlen($explode_endval) == 1) {
                                $timing['end_time'] = "0" . $explode_endtime[0] . ":" . $explode_endtime[1] . ":" . $explode_endtime[2];
                            }

                            foreach ($booking_count as $bookings) {


                                if ($timing['start_time'] == $bookings['from_time'] && $timing['end_time'] == $bookings['to_time']) {

                                    $match_found = true;
                                    break;
                                }
                            }

                            if ($match_found == false) {
                                $new_timingarray[] = array('start_time' => $timing['start_time'], 'end_time' => $timing['end_time']);
                            }
                        }
                    }

                    $new_timingarray = array_filter($new_timingarray);



                    $booking = false;

                    // Booking code
                    $user_currency = get_api_user_currency($this->users_id);
                    $UserCurrency = $user_currency['user_currency_code'];
                    foreach ($new_timingarray as $booked_time) {

                        if ($booked_time['start_time'] == $from_time_railwayhrs && $booked_time['end_time'] == $to_time_railwayhrs) {
                            $booking = true;

                            $amt=(!empty($UserCurrency)) ? get_gigs_currency($user_post_data['amount'], $booked_time['currency_code'], $UserCurrency) : $user_post_data['amount'];
                            $charges_array = array();
                            $amount = (string) $amt;
                            $amount = $user_post_data['amount'];
                            $amount = ($amount * 100);
                            $charges_array['amount'] = $amount;
                            $charges_array['currency'] = (!empty($UserCurrency)) ? currency_code_sign($UserCurrency) : currency_code_sign(settings('currency'));
                            $charges_array['description'] = $user_post_data['notes'];
                            $charges_array['source'] = $user_post_data['tokenid'];
                            $charges_array['source'] = $user_post_data['tokenid'];
                            $provider_id = $result['user_id'];



                            $user_post_data['currency_code'] = $UserCurrency;
                            $user_post_data['provider_id'] = $provider_id;
                            $user_post_data['user_id'] = $this->users_id;
                            $user_post_data['request_date'] = date('Y-m-d H:i:s');
                            $user_post_data['request_time'] = time();
                            $user_post_data['from_time'] = date('G:i:s', ($timestamp_from));
                            $user_post_data['to_time'] = date('G:i:s', ($timestamp_to));
                            $user_post_data['updated_on'] = utc_date_conversion(date('Y-m-d H:i:s'));

                            $insert_booking = $this->api->insert_booking($user_post_data);

                            if ($insert_booking != '') {

                                /* create history */

                                $this->api->booking_wallet_history_flow($insert_booking, $token);
                                /* create history */

                                /* apns */
                                $data = $this->api->get_book_info_b($insert_booking);


                                $device_token = $this->api->get_device_info_multiple($data['provider_id'], 1);

                                $user_name = $this->api->get_user_info($data['user_id'], 2);

                                $provider_token = $this->api->get_user_info($user_post_data['provider_id'], 1);

                                if (!empty($user_name['name'])) {
                                    $u_name = $user_name['name'];
                                } else {
                                    $u_name = 'user';
                                }

                                $msg = $u_name . 'has booked your Service';

                                $this->api->insert_notification($token, $provider_token['token'], $msg);

                                if (!empty($device_token)) {

                                    foreach ($device_token as $key => $device) { /* loop */

                                        if (!empty($device['device_type']) && !empty($device['device_id'])) {

                                            if ($device['device_type'] == 'Android' || $device['device_type'] == 'android') {

                                                $notify_structure = array(
                                                    'title' => $data['service_title'],
                                                    'message' => $msg,
                                                    'image' => 'test22',
                                                    'action' => 'test222',
                                                    'action_destination' => 'test222',
                                                );


                                                sendFCMMessage($notify_structure, $device['device_id']);
                                            }
                                            if ($device['device_type'] == 'ios') {
                                                $notify_structure = array(
                                                    'alert' => $msg,
                                                    'sound' => 'default',
                                                    'badge' => 0,
                                                );

                                                sendApnsMessage($notify_structure, $device['device_id']);
                                            }
                                        }
                                    }/* loop */
                                }
                                /* apns */



                                $response_code = '200';
                                $response_message = 'Booked successfully';
                                $data = new stdClass();

                                $res = $this->data_format($response_code, $response_message, $data);

                                $this->response($res, REST_Controller::HTTP_OK);
                                break;
                            }
                        }
                    }

                    if ($booking == false) {

                        $response_code = '500';
                        $response_message = 'Booking not available';
                        $data = new stdClass();

                        $res = $this->data_format($response_code, $response_message, $data);

                        $this->response($res, REST_Controller::HTTP_OK);
                    }
                } else {
                    $response_code = '201';
                    $response_message = 'Input field missing';
                }
            } else {

                $response_code = '202';
                $response_message = 'Invalid user or token';
            }
        } else {

            $response_code = '200';
            $response_message = 'Token missing';
        }

        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function search_services_post() {
        if ($this->users_id != 0 || ($this->default_token == $this->api_token)) {

            $data = array();
            $user_data = $this->post();
            if (!empty($user_data['text']) && !empty($user_data['latitude']) && !empty($user_data['longitude'])) {

                $result = $this->api->search_request_list($user_data);

                if (is_array($result) && !empty($result)) {

                    $user_currency = get_api_user_currency($this->users_id);
                    $UserCurrency = $user_currency['user_currency_code'];

                    foreach ($result as $details) {


                        $this->db->select("service_image");
                        $this->db->from('services_image');
                        $this->db->where("service_id", $details['id']);
                        $this->db->where("status", 1);
                        $image = $this->db->get()->result_array();

                        $serv_image = '';
                        foreach ($image as $key => $i) {
                            $serv_image = $i['service_image'];
                        }


                        $serviceAmt = (!empty($UserCurrency && $details['currency_code'] != '')) ? get_gigs_currency($details['service_amount'], $details['currency_code'], $UserCurrency) : $details['service_amount'];
                        $res['service_id'] = $details['id'];
                        $res['service_title'] = $details['service_title'];
                        $res['service_amount'] = (string) $serviceAmt;
                        $res['service_location'] = $details['service_location'];
                        $res['service_image'] = $serv_image;
                        $res['category_name'] = $details['category_name'];
                        $res['subcategory_name'] = $details['subcategory_name'];
                        $res['rating'] = $details['rating'];
                        $res['rating_count'] = $details['rating_count'];
                        if ($details['profile_img'] == null) {
                            $res['profile_img'] = "";
                        } else {
                            $res['profile_img'] = $details['profile_img'];
                        }
                        $res['currency'] = (!empty($UserCurrency)) ? currency_code_sign($UserCurrency) : currency_code_sign(settings('currency'));
                        $response[] = $res;
                    }

                    $data = $response;
                    $response_code = '200';
                    $response_message = 'Service search result';
                } else {
                    $response_code = '200';
                    $response_message = 'No results found';
                    $data = array();
                }
            } else {
                $response_code = '500';
                $response_message = 'Input field missing';
            }

            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function bookinglist_post() {

        $user_data = array();
        $user_data = $this->post(); // Get Header Data
        $user_post_data = getallheaders();


        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }


        $data = array();
        $response_code = '201';
        $response_message = 'Invalid token or token missing';



        if (!empty($user_data['type'] && !empty($user_data['status']))) {

            if ($user_data['type'] == 1) {

                $result = $this->api->token_is_valid_provider($token);
                $results = '';


                if ($result) {

                    $inputs = array();
                    $provider_id = $this->user_id;


                    $result = $this->api->get_bookinglist($provider_id, $user_data['status']);

                    if (!empty($result)) {
                        $provider_currency = get_api_provider_currency($provider_id);
                        $ProviderCurrency = $provider_currency['user_currency_code'];
                        foreach ($result as $details) {

                            $this->db->select("service_image");
                            $this->db->from('services_image');
                            $this->db->where("service_id", $details['service_id']);
                            $this->db->where("status", 1);
                            $image = $this->db->get()->result_array();

                            $this->db->select("*");
                            $this->db->from('users');
                            $this->db->where("id", $details['user_id']);

                            $user_mble = $this->db->get()->row_array();

                            $rating_count = $this->db->where(array("service_id" => $details['service_id'], 'status' => 1))->count_all_results('rating_review');


                            $this->db->select('AVG(rating)');
                            $this->db->where(array('service_id' => $details['service_id'], 'status' => 1));
                            $this->db->from('rating_review');
                            $rating = $this->db->get()->row_array();
                            $avg_rating = round($rating['AVG(rating)'], 2);


                            $serv_image = array();
                            foreach ($image as $key => $i) {
                                $serv_image[] = $i['service_image'];
                            }

                            $res['id'] = $details['id'];
                            $res['user_id'] = $details['user_id'];
                            $res['token'] = $user_mble['token'];
                            $res['name'] = $user_mble['name'];
                            $res['profile_img'] = $details['profile_img'];
                            $res['provider_id'] = $details['provider_id'];
                            $res['location'] = $details['location'];
                            $res['service_date'] = $details['service_date'];
                            if (!empty($user_mble['mobileno'])) {
                                $res['mobileno'] = $user_mble['mobileno'];
                            } else {
                                $res['mobileno'] = '';
                            }
                            $res['country_code'] = $details['country_code'];

                            $service_amt = (!empty($ProviderCurrency) && $ProviderCurrency != '') ? get_gigs_currency($details['service_amount'], $details['currency_code'], $ProviderCurrency) : $details['service_amount'];
                            $res['service_title'] = $details['service_title'];
                            $res['service_amount'] = (string) $service_amt;
                            $res['category_name'] = $details['category_name'];
                            $res['subcategory_name'] = $details['subcategory_name'];


                            $res['service_image'] = $serv_image[0];
                            $res['rating_count'] = "$rating_count";
                            $res['rating'] = "$avg_rating";
                            $res['notes'] = $details['notes'];
                            $res['latitude'] = $details['latitude'];
                            $res['longitude'] = $details['longitude'];
                            $res['currency'] = (!empty($ProviderCurrency)) ? currency_code_sign($ProviderCurrency) : currency_code_sign(settings('currency'));
                            $res['status'] = $details['status'];
                            $response[] = $res;
                        }

                        $data = $response;
                        $response_code = '200';
                        $response_message = "Booking list";
                    } else {
                        $response_code = '200';
                        $response_message = "No Records found";
                        $data = array();
                    }
                }
            } elseif ($user_data['type'] == 2) {

                $result = $this->api->token_is_valid($token);
                $results = '';

                if ($result) {


                    $inputs = array();
                    $user_id = $this->users_id;


                    $result = $this->api->get_bookinglist_user($user_id, $user_data['status']);
                   
                    if (!empty($result)) {

                        $user_currency = get_api_user_currency($user_id);
                        $UserCurrency = $user_currency['user_currency_code'];

                        foreach ($result as $details) {

                            $this->db->select("service_image");
                            $this->db->from('services_image');
                            $this->db->where("service_id", $details['service_id']);
                            $this->db->where("status", 1);
                            $image = $this->db->get()->result_array();

                            $this->db->select("*");
                            $this->db->from('providers');
                            $this->db->where("id", $details['provider_id']);

                            $provider_mble = $this->db->get()->row_array();



                            $rating_count = $this->db->where(array("service_id" => $details['service_id'], 'status' => 1))->count_all_results('rating_review');


                            $this->db->select('AVG(rating)');
                            $this->db->where(array('service_id' => $details['service_id'], 'status' => 1));
                            $this->db->from('rating_review');
                            $rating = $this->db->get()->row_array();
                            $avg_rating = round($rating['AVG(rating)'], 2);


                            $serv_image = array();
                            foreach ($image as $key => $i) {
                                $serv_image[] = $i['service_image'];
                            }


                            $res['id'] = $details['id'];
                            $res['user_id'] = $details['user_id'];
                            $res['token'] = $provider_mble['token'];
                            $res['name'] = $provider_mble['name'];
                            $res['profile_img'] = $details['provider_profile'];
                            $res['provider_id'] = $details['provider_id'];
                            $res['location'] = $details['location'];
                            $res['service_date'] = $details['service_date'];
                            $res['from_time'] = $details['from_time'];
                            $res['to_time'] = $details['to_time'];

                            $amt_detail= (!empty($UserCurrency)) ? get_gigs_currency($details['service_amount'], $details['currency_code'], $UserCurrency) : $details['service_amount'];
                            $res['service_title'] = $details['service_title'];
                            $res['service_amount'] = (string) $amt_detail;
                            $res['category_name'] = $details['category_name'];
                            $res['subcategory_name'] = $details['subcategory_name'];


                            $res['service_title'] = $details['service_title'];
                            $res['service_image'] = $serv_image[0];
                            $res['rating_count'] = "$rating_count";
                            $res['rating'] = "$avg_rating";

                            if (!empty($provider_mble['mobileno'])) {
                                $res['mobileno'] = $provider_mble['mobileno'];
                            } else {
                                $res['mobileno'] = '';
                            }
                            $res['country_code'] = $details['country_code'];
                            $res['notes'] = $details['notes'];
                            $res['latitude'] = $details['latitude'];
                            $res['longitude'] = $details['longitude'];
                            $res['currency'] = (!empty($UserCurrency)) ? currency_code_sign($UserCurrency) : currency_code_sign(settings('currency'));
                            $res['status'] = $details['status'];
                            $res['provider_profile'] = $details['provider_profile'];
//                            $res['amount'] = get_gigs_currency($details['amount'],$details['currency_code'],$user_currency['user_currency_code']);
                            $response[] = $res;
                        }

                        $data = $response;
                        $response_code = '200';
                        $response_message = "Booking service list";
                    } else {
                        $response_code = '200';
                        $response_message = "No Records found";
                        $data = array();
                    }
                }
            }
        } else {
            $response_code = '200';
            $response_message = "Input field missing";
            $data = array();
        }

        $result = $this->data_format($response_code, $response_message, $data);
        
        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function bookingdetail_post() {


        $user_data = array();
        $user_data = $this->post(); // Get Header Data
        $user_post_data = getallheaders();


        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }


        $data = array();
        $response_code = '201';
        $response_message = 'Invalid token or token missing';



        $data = array();
        $user_data = $this->post();
        $provider_id = $this->user_id;

        if (!empty($user_data['booking_id']) && !empty($user_data['type'])) {
            if ($user_data['type'] == 1) {


                $result = $this->api->token_is_valid_provider($token);
                $results = '';


                if ($result) {
                    $details = $this->api->get_bookingdetails($provider_id, $user_data['booking_id']);

                    if (!empty($details)) {
                        $provider_currency = get_api_provider_currency($provider_id);
                        $ProviderCurrency = $provider_currency['user_currency_code'];

                        foreach ($details as $result) {

                            $this->db->select("service_image");
                            $this->db->from('services_image');
                            $this->db->where("service_id", $result['service_id']);
                            $this->db->where("status", 1);
                            $image = $this->db->get()->result_array();

                            $rating_count = $this->db->where(array("service_id" => $result['service_id'], 'status' => 1))->count_all_results('rating_review');

                            $is_rated = $this->db->where(array("service_id" => $result['service_id'], 'user_id' => $result['user_id']))->count_all_results('rating_review');



                            $this->db->select('AVG(rating)');
                            $this->db->where(array('service_id' => $result['service_id'], 'status' => 1));
                            $this->db->from('rating_review');
                            $rating = $this->db->get()->row_array();
                            $avg_rating = round($rating['AVG(rating)'], 2);

                            $serv_image = array();
                            foreach ($image as $key => $i) {
                                $serv_image[] = $i['service_image'];
                            }

//                            print_r($result);exit;
                            $res_amt= (!empty($ProviderCurrency) && $ProviderCurrency != '') ? get_gigs_currency($result['service_amount'], $result['currency_code'], $ProviderCurrency) : $result['service_amount'];
                            $services['service_id'] = $result['service_id'];
                            $services['service_title'] = $result['service_title'];
                            $services['service_amount'] = (string) $res_amt;
                            ;
                            $services['about'] = $result['about'];
                            $services['service_offered'] = $result['service_offered'];
                            $services['service_location'] = $result['service_location'];
                            $services['service_latitude'] = $result['service_latitude'];
                            $services['service_longitude'] = $result['service_longitude'];
                            $services['category_name'] = $result['category_name'];
                            $services['subcategory_name'] = $result['subcategory_name'];
                            $services['currency_code'] = (!empty($ProviderCurrency)) ? currency_code_sign($ProviderCurrency) : currency_code_sign(settings('currency'));
                            $services['service_image'] = $serv_image;
                            $services['total_views'] = $result['total_views'];
                            $services['rating'] = "$avg_rating";
                            $services['rating_count'] = "$rating_count";
                            if ($is_rated != 0) {
                                $services['is_rated'] = "1";
                            } else {
                                $services['is_rated'] = "0";
                            }

                            $service_details = $services;

                            $res['booking_id'] = $user_data['booking_id'];
                            $res['user_id'] = $result['user_id'];
                            $res['provider_id'] = $result['provider_id'];
                            $res['service_date'] = $result['service_date'];
                            $res['from_time'] = date('g:i A', strtotime($result['from_time']));
                            $res['to_time'] = date('g:i A', strtotime($result['to_time']));
                            $res['currency_code'] = currency_code_sign(settings('currency'));
                            $res['notes'] = $result['notes'];
                            $res['user_rejected_reason'] = !empty($result['reason']) ? $result['reason'] : '';
                            $res['admin_comments'] = !empty($result['admin_reject_comment']) ? $result['admin_reject_comment'] : '';
                            $res['status'] = $result['status'];

                            $booking_details = $res;

                            $users['name'] = $result['name'];
                            $users['token'] = $result['token'];
                            $users['mobileno'] = $result['mobileno'];
                            $users['country_code'] = $result['country_code'];
                            $users['email'] = $result['email'];
                            $users['profile_img'] = $result['profile_img'];
                            $users['latitude'] = $result['latitude'];
                            $users['longitude'] = $result['longitude'];
                            $users['location'] = $result['location'];

                            $user_details = $users;



                            $response['booking_details'] = $booking_details;
                            $response['service_details'] = $service_details;
                            $response['personal_details'] = $user_details;
                        }
                        $data = $response;
                        $response_code = '200';
                        $response_message = "Service Booking list";
                    } else {
                        $response_code = '200';
                        $response_message = "No Records found";
                        $data = new stdClass();
                    }
                }
            } elseif ($user_data['type'] == 2) {

                $result = $this->api->token_is_valid($token);
                $results = '';

                if ($result) {
                    $data = array();
                    $user_data = $this->post();
                    $user_id = $this->users_id;

                    $details = $this->api->bookingdetail_user($user_id, $user_data['booking_id']);

                    if (!empty($details)) {
                        $user_currency = get_api_user_currency($user_id);
                        $UserCurrency = $user_currency['user_currency_code'];

                        foreach ($details as $result) {

                            


                            $this->db->select("service_image");
                            $this->db->from('services_image');
                            $this->db->where("service_id", $result['service_id']);
                            $this->db->where("status", 1);
                            $image = $this->db->get()->result_array();

                            $rating_count = $this->db->where(array("service_id" => $result['service_id'], 'status' => 1))->count_all_results('rating_review');

                            $is_rated = $this->db->where(array("service_id" => $result['service_id'], 'user_id' => $result['user_id']))->count_all_results('rating_review');

                            $this->db->select('AVG(rating)');
                            $this->db->where(array('service_id' => $result['service_id'], 'status' => 1));
                            $this->db->from('rating_review');
                            $rating = $this->db->get()->row_array();
                            $avg_rating = round($rating['AVG(rating)'], 2);


                            foreach ($image as $key => $i) {
                                $serv_image[] = $i['service_image'];
                            }
                            $rest_amt = (!empty($UserCurrency)) ? get_gigs_currency($result['service_amount'], $result['currency_code'], $UserCurrency) : $result['service_amount'];
                            $services['service_id'] = $result['service_id'];
                            $services['service_title'] = $result['service_title'];
                            $services['service_amount'] = (string) $rest_amt;
                            ;
                            $services['about'] = $result['about'];
                            $services['service_offered'] = $result['service_offered'];
                            $services['category_name'] = $result['category_name'];
                            $services['subcategory_name'] = $result['subcategory_name'];
                            $services['service_image'] = $serv_image;
                            $services['service_location'] = $result['service_location'];
                            $services['service_latitude'] = $result['service_latitude'];
                            $services['service_longitude'] = $result['service_longitude'];
                            $services['total_views'] = $result['total_views'];
                            $services['currency_code'] = (!empty($UserCurrency)) ? currency_code_sign($UserCurrency) : currency_code_sign(settings('currency'));
                            $services['rating'] = "$avg_rating";
                            $services['rating_count'] = "$rating_count";
                            if ($is_rated != 0) {
                                $services['is_rated'] = "1";
                            } else {
                                $services['is_rated'] = "0";
                            }


                            $service_details = $services;

                            $res['booking_id'] = $user_data['booking_id'];
                            $res['user_id'] = $result['user_id'];
                            $res['provider_id'] = $result['provider_id'];
                            $res['service_date'] = $result['service_date'];
                            $res['from_time'] = date('g:i A', strtotime($result['from_time']));
                            $res['to_time'] = date('g:i A', strtotime($result['to_time']));
                            $res['currency_code'] = currency_code_sign(settings('currency'));
                            $res['notes'] = $result['notes'];
                            $res['request_date'] = $result['request_date'];
                            $res['request_time'] = $result['request_time'];
                            $res['user_rejected_reason'] = !empty($result['reason']) ? $result['reason'] : '';
                            $res['admin_comments'] = !empty($result['admin_reject_comment']) ? $result['admin_reject_comment'] : '';
                            $res['status'] = $result['status'];

                            $booking_details = $res;

                            $provider['name'] = $result['name'];
                            $provider['token'] = $result['token'];
                            $provider['mobileno'] = $result['mobileno'];
                            $provider['country_code'] = $result['country_code'];
                            $provider['email'] = $result['email'];
                            $provider['profile_img'] = $result['profile_img'];
                            $provider['location'] = $result['location'];
                            $provider['latitude'] = $result['latitude'];
                            $provider['longitude'] = $result['longitude'];

                            $provider_details = $provider;



                            $response['booking_details'] = $booking_details;
                            $response['service_details'] = $service_details;
                            $response['personal_details'] = $provider_details;
                        }

                        $data = $response;
                        $response_code = '200';
                        $response_message = "Service Booking list";
                    } else {
                        $response_code = '200';
                        $response_message = "No Records found";
                        $data = new stdClass();
                    }
                }
            }
        } else {
            $response_code = '500';
            $response_message = 'Input field missing';
        }

        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function requestlist_provider_get() {

        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {


            $inputs = array();
            $provider_id = $this->user_id;


            $result = $this->api->get_requestlist($provider_id);

            if (!empty($result)) {

                $provider_currency = get_api_provider_currency($provider_id);
                $ProviderCurrency = $provider_currency['user_currency_code'];

                foreach ($result as $details) {

                    $this->db->select("service_image");
                    $this->db->from('services_image');
                    $this->db->where("service_id", $details['service_id']);
                    $this->db->where("status", 1);
                    $image = $this->db->get()->result_array();

                    $rating_count = $this->db->where(array("service_id" => $details['service_id'], 'status' => 1))->count_all_results('rating_review');


                    $this->db->select('AVG(rating)');
                    $this->db->where(array('service_id' => $details['service_id'], 'status' => 1));
                    $this->db->from('rating_review');
                    $rating = $this->db->get()->row_array();
                    $avg_rating = round($rating['AVG(rating)'], 2);

                    $serv_image = array();
                    foreach ($image as $key => $i) {
                        $serv_image[] = $i['service_image'];
                    }
                    $det_amt = (!empty($ProviderCurrency) && $ProviderCurrency != '') ? get_gigs_currency($details['service_amount'], $details['currency_code'], $ProviderCurrency) : $details['service_amount'];
                    $res['id'] = $details['id'];
                    $res['user_id'] = $details['user_id'];
                    $res['profile_img'] = $details['profile_img'];
                    $res['provider_id'] = $details['provider_id'];
                    $res['location'] = $details['location'];
                    $res['service_date'] = $details['service_date'];
                    $res['from_time'] = $details['from_time'];
                    $res['to_time'] = $details['to_time'];
                    $res['service_title'] = $details['service_title'];
                    $res['service_amount'] = (string) $det_amt;
                    $res['category_name'] = $details['category_name'];
                    $res['subcategory_name'] = $details['subcategory_name'];
                    $res['service_title'] = $details['service_title'];
                    $res['service_image'] = $serv_image[0];
                    $res['rating'] = "$avg_rating";
                    $res['rating_count'] = "$rating_count";
                    $res['notes'] = $details['notes'];
                    $res['latitude'] = $details['latitude'];
                    $res['longitude'] = $details['longitude'];
                    $res['currency_code'] = (!empty($ProviderCurrency)) ? currency_code_sign($ProviderCurrency) : currency_code_sign(settings('currency'));
                    $res['status'] = $details['status'];
                    $response[] = $res;
                }

                $data = $response;
                $response_code = '200';
                $response_message = "Request service list";
            } else {
                $response_code = '200';
                $response_message = "No Records found";
                $data = array();
            }




            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function bookinglist_users_get() {

        if ($this->users_id != 0 || ($this->default_token == $this->api_token)) {


            $inputs = array();
            $user_id = $this->users_id;


            $result = $this->api->get_bookinglist_user($user_id);

            if (!empty($result)) {

                foreach ($result as $details) {

                    $this->db->select("service_image");
                    $this->db->from('services_image');
                    $this->db->where("service_id", $details['service_id']);
                    $this->db->where("status", 1);
                    $image = $this->db->get()->result_array();

                    $serv_image = array();
                    foreach ($image as $key => $i) {
                        $serv_image[] = $i['service_image'];
                    }


                    $res['user_id'] = $details['user_id'];
                    $res['profile_img'] = $details['profile_img'];
                    $res['provider_id'] = $details['provider_id'];
                    $res['location'] = $details['location'];
                    $res['service_date'] = $details['service_date'];
                    $res['from_time'] = $details['from_time'];
                    $res['to_time'] = $details['to_time'];
                    $res['service_title'] = $details['service_title'];
                    $res['service_amount'] = $details['service_amount'];
                    $res['category_name'] = $details['category_name'];
                    $res['subcategory_name'] = $details['subcategory_name'];
                    $res['service_title'] = $details['service_title'];
                    $res['service_image'] = $serv_image[0];
                    $res['notes'] = $details['notes'];
                    $res['latitude'] = $details['latitude'];
                    $res['longitude'] = $details['longitude'];
                    $res['status'] = $details['status'];
                    $response[] = $res;
                }

                $data = $response;
                $response_code = '200';
                $response_message = "Booking service list";
            } else {
                $response_code = '200';
                $response_message = "No Records found";
                $data = new stdClass();
            }




            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function bookingdetail_user_post() {

        if ($this->users_id != 0 || ($this->default_token == $this->api_token)) {

            $data = array();
            $user_data = $this->post();
            $user_id = $this->users_id;

            if (!empty($user_data['booking_id'])) {

                $details = $this->api->bookingdetail_user($user_id, $user_data['booking_id']);

                if (!empty($details)) {


                    foreach ($details as $result) {


                        $this->db->select("service_image");
                        $this->db->from('services_image');
                        $this->db->where("service_id", $result['service_id']);
                        $this->db->where("status", 1);
                        $image = $this->db->get()->result_array();

                        foreach ($image as $key => $i) {
                            $serv_image[] = $i['service_image'];
                        }

                        $services['service_title'] = $result['service_title'];
                        $services['service_amount'] = $result['service_amount'];
                        $services['about'] = $result['about'];
                        $services['service_offered'] = $result['service_offered'];
                        $services['category_name'] = $result['category_name'];
                        $services['subcategory_name'] = $result['subcategory_name'];
                        $services['service_location'] = $result['service_location'];
                        $services['service_latitude'] = $result['service_latitude'];
                        $services['service_longitude'] = $result['service_longitude'];
                        $services['service_image'] = $serv_image[0];
                        $services['total_views'] = $result['total_views'];
                        $services['created_at'] = $result['created_at'];
                        $services['updated_at'] = $result['updated_at'];

                        $service_details[] = $services;

                        $res['booking_id'] = $result['id'];
                        $res['user_id'] = $result['user_id'];
                        $res['provider_id'] = $result['provider_id'];
                        $res['location'] = $result['location'];
                        $res['service_date'] = $result['service_date'];
                        $res['from_time'] = $result['from_time'];
                        $res['to_time'] = $result['to_time'];
                        $res['amount'] = $result['amount'];
                        $res['currency_code'] = $result['currency_code'];
                        $res['notes'] = $result['notes'];
                        $res['latitude'] = $result['latitude'];
                        $res['longitude'] = $result['longitude'];
                        $res['request_date'] = $result['request_date'];
                        $res['request_time'] = $result['request_time'];
                        $res['status'] = $result['status'];

                        $booking_details[] = $res;

                        $provider['name'] = $result['name'];
                        $provider['mobileno'] = $result['mobileno'];
                        $provider['email'] = $result['email'];
                        $provider['profile_img'] = $result['profile_img'];

                        $provider_details[] = $provider;



                        $response['booking_details'] = $booking_details;
                        $response['service_details'] = $service_details;
                        $response['provider_details'] = $provider_details;
                    }

                    $data = $response;
                    $response_code = '200';
                    $response_message = "Service Booking list";
                } else {
                    $response_code = '200';
                    $response_message = "No Records found";
                    $data = new stdClass();
                }
            } else {
                $response_code = '500';
                $response_message = 'Input field missing';
            }

            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function views_post() {


        $user_data = array();
        $user_data = getallheaders(); // Get Header Data
        $user_post_data = $this->post();


        $token = (!empty($user_data['token'])) ? $user_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_data['Token'])) ? $user_data['Token'] : '';
        }


        $data = array();
        $response_code = '-1';
        $response_message = 'validation error';

        if (!empty($token) && !empty($user_post_data['service_id'])) {

            $result = $this->api->token_is_valid($token);
            $results = '';
            $user_id = $this->users_id;

            if ($result) {

                $this->db->select('id');
                $this->db->from('views');
                $this->db->where(array('user_id' => $user_id, 'service_id' => $user_post_data['service_id']));
                $check_views = $this->db->count_all_results();


                if ($check_views == 0) {
                    $this->db->insert('views', array('user_id' => $user_id, 'service_id' => $user_post_data['service_id']));

                    $this->db->set('total_views', 'total_views+1', FALSE);
                    $this->db->where('id', $user_post_data['service_id']);
                    $results = $this->db->update('services');
                }

                if ($results == 1) {

                    $response_code = '200';
                    $response_message = 'Views added successfully';
                } else {

                    $response_code = '200';
                    $response_message = 'Views already added for this user';
                }
            } else {

                $response_code = '202';
                $response_message = 'Invalid user token';
            }
        } else {

            $response_code = '201';
            $response_message = 'User token is missing';
        }

        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function update_bookingstatus_post() {


        $user_data = $this->post();

        if (!empty($user_data['id']) && !empty($user_data['status'])) {




            if ($user_data['status'] == '1') {
                $book_details['status'] = '2';
                $book_details['id'] = $user_data['id'];
            } elseif ($user_data['status'] == '2') {
                $book_details['status'] = '7';
                $book_details['id'] = $user_data['id'];
            } elseif ($user_data['status'] == '3') {
                $book_details['status'] = '3';
                $book_details['id'] = $user_data['id'];
            } elseif ($user_data['status'] == '6') {
                $book_details['status'] = '4';
                $book_details['id'] = $user_data['id'];
            } elseif ($user_data['status'] == '5') {
                $book_details['status'] = '5';
                $book_details['id'] = $user_data['id'];
            } elseif ($user_data['status'] == '6') {
                $book_details['status'] = '6';
                $book_details['id'] = $user_data['id'];
            }


            $booking_status = $this->api->booking_status($user_data['id']);

            if ($booking_status['status'] == '1' && $user_data['status'] == '1') {
                $WHERE = array('id' => $user_data['id']);

                $result = $this->api->update_bookingstatus($book_details, $WHERE);

                if ($result) {
                    $response_code = '200';
                    $response_message = 'Booking status updated successfully';
                }
            } elseif ($booking_status['status'] == '2' && $user_data['status'] == '3') {
                $WHERE = array('id' => $user_data['id']);

                $result = $this->api->update_bookingstatus($book_details, $WHERE);

                if ($result) {
                    $response_code = '200';
                    $response_message = 'Booking status updated successfully';
                }
            } elseif ($booking_status['status'] == '3' && $user_data['status'] == '4') {
                $WHERE = array('id' => $user_data['id']);

                $result = $this->api->update_bookingstatus($book_details, $WHERE);

                if ($result) {
                    $response_code = '200';
                    $response_message = 'Booking status updated successfully';
                }
            } elseif ($booking_status['status'] == '4' && $user_data['status'] == '5') {
                $WHERE = array('id' => $user_data['id']);

                $result = $this->api->update_bookingstatus($book_details, $WHERE);

                if ($result) {
                    $response_code = '200';
                    $response_message = 'Booking status updated successfully';
                }
            } elseif ($booking_status['status'] == '5' && $user_data['status'] == '6') {
                $WHERE = array('id' => $user_data['id']);

                $result = $this->api->update_bookingstatus($book_details, $WHERE);

                if ($result) {
                    $response_code = '200';
                    $response_message = 'Booking status updated successfully';
                }
            } else {
                $response_code = '200';
                $response_message = 'Booking status already updated';
            }
            $data = new stdClass();
            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $response_code = '200';
            $response_message = 'Input field missing';
            $data = new stdClass();
            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        }
    }

    public function service_statususer_post() {
        if ($this->users_id != 0 || ($this->default_token == $this->api_token)) {

            $user_data = $this->post();

            if (!empty($user_data['id']) && !empty($user_data['service_status'])) {

                $WHERE = array('id' => $user_data['id']);

                $result = $this->api->service_statususer($user_data, $WHERE);

                if ($result) {
                    $response_code = '200';
                    $response_message = 'Service status updated successfully';
                } else {
                    $response_code = '200';
                    $response_message = 'Service status updation failed';
                }
                $data = new stdClass();
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            } else {
                $response_code = '200';
                $response_message = 'Input field missing';
                $data = new stdClass();
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            }
        } else {
            $this->token_error();
        }
    }

    public function update_booking_post() {


        $user_data = array();
        $user_data = $this->post(); // Get Header Data
        $user_post_data = getallheaders();


        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }

        if (!empty($user_data['id']) && !empty($user_data['status']) && !empty($user_data['type'])) {
            if ($user_data['type'] == 1) {


                $result = $this->api->token_is_valid_provider($token);


                $results = '';
                $user_id = $this->user_id;

                if ($result) {
                    if ($user_data['status'] == '1') {
                        $book_details['status'] = '2';
                        $book_details['id'] = $user_data['id'];
                    } elseif ($user_data['status'] == '2')/* cancel provider */ {
                        $book_details['status'] = '7';
                        $book_details['id'] = $user_data['id'];
                    } elseif ($user_data['status'] == '3')/* service completed */ {
                        $book_details['status'] = '3';
                        $book_details['id'] = $user_data['id'];
                    } elseif ($user_data['status'] == '4') {
                        $book_details['status'] = '6';
                        $book_details['id'] = $user_data['id'];
                    } elseif ($user_data['status'] == '5') {
                        $book_details['status'] = '5';
                        $book_details['id'] = $user_data['id'];
                    } elseif ($user_data['status'] == '6') {
                        $book_details['status'] = '6';
                        $book_details['id'] = $user_data['id'];
                    }


                    $booking_status = $this->api->booking_status($user_data['id']);

                    if ($booking_status['status'] == '1' && $user_data['status'] == '1') {

                        $WHERE = array('id' => $user_data['id']);

                        $result = $this->api->update_bookingstatus($book_details, $WHERE);

                        if ($result) {
                            /* provider accepted */
                            $this->send_push_notification($token, $user_data['id'], 2, ' Have Accepted The Service');

                            $response_code = '200';
                            $response_message = 'Booking status updated successfully';
                        }
                    } elseif ($booking_status['status'] == '1' && $user_data['status'] == '2') {
                        $WHERE = array('id' => $user_data['id']);

                        $result = $this->api->update_bookingstatus($book_details, $WHERE);

                        if ($result) {

                            /* wallet history */
                            $this->api->provider_reject_history_flow($user_data['id']);
                            /* wallet history */

                            /* Provider Rejected */
                            $this->send_push_notification($token, $user_data['id'], 2, ' Has Rejected The Service');

                            $response_code = '200';
                            $response_message = 'Booking status updated successfully';
                        }
                    } elseif ($booking_status['status'] == '2' && $user_data['status'] == '3') {
                        $WHERE = array('id' => $user_data['id']);

                        $result = $this->api->update_bookingstatus($book_details, $WHERE);

                        if ($result) {
                            /* provider completed */
                            $this->send_push_notification($token, $user_data['id'], 2, ' Have Completed Ther Service');

                            $response_code = '200';
                            $response_message = 'Booking status updated successfully';
                        }
                    } else {


                        $response_code = '200';
                        $response_message = 'Booking status already updated';
                    }
                    $data = new stdClass();
                    $result = $this->data_format($response_code, $response_message, $data);
                    $this->response($result, REST_Controller::HTTP_OK);
                } else {
                    $response_code = "500";
                    $response_message = "Token is Invalid";
                    $data = [];
                }
            } elseif ($user_data['type'] == 2) {

                $result = $this->api->token_is_valid($token);
                $results = '';
                $users_id = $this->users_id;

                if ($result) {

                    if ($user_data['status'] == '4') {

                        if (!empty($user_data['id']) && !empty($user_data['status']) && !empty($user_data['type'])) {


                            $book_details['status'] = '6';
                            $book_details['id'] = $user_data['id'];
                            /* apns push notification */



                            $booking_status = $this->api->booking_status($user_data['id']);

                            if ($booking_status['status'] == '3' && $user_data['status'] == '4') {
                                $WHERE = array('id' => $user_data['id']);

                                $result = $this->api->update_bookingstatus($book_details, $WHERE);

                                if ($result) {
                                    /* wallet history */
                                    $this->api->user_accept_history_flow($user_data['id']);

                                    /* completed user site */
                                    $this->send_push_notification($token, $user_data['id'], 1, ' Has Accepted The Completed Service');
                                    $response_code = '200';
                                    $response_message = 'Booking status updated successfully';
                                }
                            } else {
                                $response_code = '200';
                                $response_message = 'Booking status already updated';
                            }
                            $data = new stdClass();
                            $result = $this->data_format($response_code, $response_message, $data);
                            $this->response($result, REST_Controller::HTTP_OK);
                        } else {
                            $response_code = '200';
                            $response_message = 'Input field missing';
                        }
                    } else if ($user_data['status'] == '2') {

                        if (!empty($user_data['id']) && !empty($user_data['status']) && !empty($user_data['type'])) {


                            $book_details['status'] = '7';
                            $book_details['id'] = $user_data['id'];
                            /* apns push notification */



                            $booking_status = $this->api->booking_status($user_data['id']);

                            if ($booking_status['status'] == '1' && $user_data['status'] == '2') {
                                $WHERE = array('id' => $user_data['id']);

                                $result = $this->api->update_bookingstatus($book_details, $WHERE);

                                if ($result) {
                                    /* wallet history */
                              //      $this->api->booking_wallet_history_flow($user_data['id'], $token);

                                    /* completed user site */
                                    $this->send_push_notification($token, $user_data['id'], 1, ' Has Cancelled The Service');
                                    $response_code = '200';
                                    $response_message = 'Booking status updated successfully';
                                }
                            } else {
                                $response_code = '200';
                                $response_message = 'Booking status already updated';
                            }
                            $data = new stdClass();
                            $result = $this->data_format($response_code, $response_message, $data);
                            $this->response($result, REST_Controller::HTTP_OK);
                        } else {
                            $response_code = '200';
                            $response_message = 'Input field missing';
                        }
                    } elseif ($user_data['status'] == '5') {

                        if (!empty($user_data['id']) && !empty($user_data['status']) && !empty($user_data['reason'])) {


                            $book_details['status'] = '5';
                            $book_details['id'] = $user_data['id'];
                            $book_details['reason'] = $user_data['reason'];


                            $booking_status = $this->api->booking_status($user_data['id']);


                            if ($booking_status['status'] == '3' && $user_data['status'] == '5') {
                                $WHERE = array('id' => $user_data['id']);

                                $result = $this->api->update_bookingstatus($book_details, $WHERE);

                                if ($result) {
                                    /* user rejected */

                                    $this->send_push_notification($token, $user_data['id'], 1, ' Has Rejected The Completed Service');
                                    $response_code = '200';
                                    $response_message = 'Booking status updated successfully';
                                }
                            } else {
                                $response_code = '200';
                                $response_message = 'Booking status already updated';
                            }
                            $data = new stdClass();
                            $result = $this->data_format($response_code, $response_message, $data);
                            $this->response($result, REST_Controller::HTTP_OK);
                        } else {
                            $response_code = '200';
                            $response_message = 'Input field missing';
                        }
                    }
                } else {
                    $response_code = "500";
                    $response_message = "Token is Invalid";
                    $data = [];
                }
            }
        } else {

            $response_code = '500';
            $response_message = 'Input field missing';
        }

        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function rate_review_post() {
        if ($this->users_id != 0 || ($this->default_token == $this->api_token)) {
            $data = array();
            $user_data = $this->post();

            if (!empty($user_data['rating']) && !empty($user_data['review']) && !empty($user_data['booking_id']) && !empty($user_data['service_id']) && !empty($user_data['type'])) {

                $check_service_status = $this->api->check_booking_status($user_data['booking_id']);

                if ($check_service_status != '') {

                    $result = $this->api->rate_review_for_service($user_data);

                    if ($result == 1) {
                        $response_code = '200';
                        $response_message = 'Thank you for your review';
                    } elseif ($result == 2) {

                        $response_code = '200';
                        $response_message = 'You have already reviwed this service';
                    }
                } else {
                    $response_code = '500';
                    $response_message = 'Service not completed';
                }
            } else {

                $response_code = '500';
                $response_message = 'Input field missing';
            }
        } else {
            $this->token_error();
        }

        $data = new stdClass();
        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function delete_account_post() {


        $user_data = array();
        $user_data = $this->post(); // Get Header Data
        $user_post_data = getallheaders();


        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }


        $data = array();
        $response_code = '201';
        $response_message = 'Invalid token or token missing';

        if (!empty($user_data['type'])) {

            if ($user_data['type'] == 1) {
                $result = $this->api->token_is_valid_provider($token);
                $results = '';
                $user_id = $this->user_id;

                if ($result) {
                    $WHERE = array('id' => $user_id);
                    $details = $this->api->delete_account_provider($user_data, $WHERE);
                    if ($details) {
                        $response_code = '200';
                        $response_message = 'Account deleted successfully';
                    } else {
                        $response_code = '200';
                        $response_message = 'Something went wrong. Please try again later';
                    }
                }
            } elseif ($user_data['type'] == 2) {

                $result = $this->api->token_is_valid($token);
                $results = '';
                $user_id = $this->users_id;

                if ($result) {
                    $WHERE = array('id' => $user_id);
                    $details = $this->api->delete_account_user($user_data, $WHERE);
                    if ($details) {
                        $response_code = '200';
                        $response_message = 'Account deleted successfully';
                    } else {
                        $response_code = '200';
                        $response_message = 'Something went wrong. Please try again later';
                    }
                }
            }
        } else {
            $data = new stdClass();
            $response_code = '500';
            $response_message = 'Input field missing';
        }

        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    //get service belong to sub category id
    public function get_services_from_subid_post() {

        if ($this->users_id != 0 || ($this->default_token == $this->api_token)) {

            $user_data = array();
            $user_data = $this->post();

            if (!empty($user_data['latitude']) && !empty($user_data['longitude']) && !empty($user_data['subcategory_id'])) {
                $val = $this->api->get_services_from_sub_service_id($user_data);
                $data = [];

                if (!empty($val)) {
                    $response_code = '200';
                    $response_message = "Successfully fetched...!";

                    $user_currency = get_api_user_currency($this->users_id);
                    $UserCurrency = $user_currency['user_currency_code'];


                    foreach ($val as $key => $value) {

                        $service_image = $this->api->get_common_service_image($value['id'], 1);
                        $rating_count = $this->db->where(array("service_id" => $value['id'], 'status' => 1))->count_all_results
                                ('rating_review');

                        $this->db->select('AVG(rating)');
                        $this->db->where(array('service_id' => $value['id'], 'status' => 1));
                        $this->db->from('rating_review');
                        $rating = $this->db->get()->row_array();
                        $avg_rating = round($rating['AVG(rating)'], 2);

                        $val_amt= (!empty($UserCurrency) && $value['currency_code'] != '') ? get_gigs_currency($value['service_amount'], $value['currency_code'], $UserCurrency) : $value['service_amount'];
                        $data[$key]['id'] = $value['id'];
                        $data[$key]['service_title'] = $value['service_title'];
                        $data[$key]['service_amount'] = (string) $val_amt;
                        $data[$key]['service_location'] = $value['service_location'];
                        $data[$key]['rating'] = "$avg_rating";
                        $data[$key]['rating_count'] = "$rating_count";



                        if (!empty($value['profile_img'])) {
                            $data[$key]['profile_img'] = $value['profile_img'];
                        } else {
                            $data[$key]['profile_img'] = '';
                        }
                        $data[$key]['category_name'] = $value['category_name'];
                        $data[$key]['subcategory_name'] = $value['subcategory_name'];

                        if (!empty($service_image['service_image'])) {
                            $data[$key]['service_image'] = $service_image['service_image'];
                        } else {
                            $data[$key]['service_image'] = '';
                        }
                        $data[$key]['currency'] = (!empty($UserCurrency)) ? currency_code_sign($UserCurrency) : currency_code_sign(settings('currency'));
                    }
                } else {

                    $response_code = '200';
                    $response_message = "No Records found";
                    $data = [];
                }
            } else {
                $response_code = '200';
                $response_message = "Input field missing";
                $data = [];
            }


            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    //get dashboard counts

    public function get_provider_dashboard_infos_get() {

        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {

            $counts = $this->api->get_provider_dashboard_count($this->user_id);

            $response_code = 200;
            $response_message = "successfully fetched...!";
            $data = $counts;


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function generate_otp_provider_post() {
        if ($this->user_id != 0 || ($this->default_token == $this->api_token)) {
            $data = new stdClass();
            $user_data = array();
            $user_data = $this->post();
			
			$login_type = settingValue('login_type');
            //print_r($login_type);exit;
            

            if (!empty($user_data['mobileno']) && !empty($user_data['country_code']) && !empty($user_data['device_type'])) {

                $is_available_mobile = $this->api->check_mobile_no($user_data);
                $is_available_mobileno = $this->api->check_user_mobileno($user_data);
                $is_available_user = $this->api->check_user_mobileno($user_data);

                if ($is_available_user == 0) {
					
					$is_available_email = $this->api->check_email($user_data);
					if ($is_available_email == 0) {
						
						if ($is_available_mobile == 0 && $is_available_mobileno == 0) {


                        if (!empty($user_data['name']) && !empty($user_data['email']) && !empty($user_data['mobileno']) && !empty($user_data['category']) && !empty($user_data['subcategory']) && !empty($user_data['country_code']) && !empty($user_data['device_type'])) {

                            $user_details['name'] = $user_data['name'];
                            $user_details['email'] = $user_data['email'];
							
							if(!empty($user_data['password']))
							{
								$user_details['password'] =md5($user_data['password']);
							}
							else
							{
								$user_details['password']='';
							}
                            $user_details['mobileno'] = $user_data['mobileno'];
                            $user_details['country_code'] = $user_data['country_code'];
                            $user_details['currency_code'] = settings('currency');
                            $user_details['category'] = $user_data['category'];
                            $user_details['subcategory'] = $user_data['subcategory'];
                            $device_data['device_type'] = $user_data['device_type'];
                            $device_data['device_id'] = $user_data['device_id'];
                            $username = strlen($user_data['name']);
                            $user_details['share_code'] = $this->user_login->ShareCode(6, $username);

                            $share_code = $user_data['get_code'];

//                            if ($share_code) {
                                $updateAmount = $this->api->ProviderShareCode($share_code);
                                if ($updateAmount == 'Empty code') {
									
									
									if($login_type=='email' && empty($user_details['password']))
									{
										$response_code = '201';
										$response_message = 'Please enter the password';
										$result = $this->data_format($response_code, $response_message, $data);
										$this->response($result, REST_Controller::HTTP_OK);
										exit;
									}
                                    
									$result = $this->api->provider_signup($user_details, $device_data);
                                    if ($result != '') {
										
										if($login_type=='mobile' && empty($user_details['password']))
										{
											$default_otp = settingValue('default_otp');
											if ($default_otp == 1) {
												$otp = '1234';
											} else {
												$otp = rand(1000, 9999);
											}

											$message = 'Your OTP is ' . $otp . '';
											$user_data['otp'] = $otp;

											error_reporting(0);
											$key = settingValue('sms_key');
											$secret_key = settingValue('sms_secret_key');
											$sender_id = settingValue('sms_sender_id');
											require_once('vendor/nexmo/src/NexmoMessage.php');
											$nexmo_sms = new NexmoMessage($key, $secret_key);
											$result = $nexmo_sms->sendText($user_data['country_code'] . $user_data['mobileno'], $sender_id, $message);
											$this->session->set_tempdata('otp', '$user_data', 300);


											$otp_data = array(
												'endtime' => time() + 300,
												'mobile_number' => $user_data['mobileno'],
												'country_code' => $user_data['country_code'],
												'otp' => $otp
											);

											$ret = $this->db->select('*')->from('mobile_otp')->
													where('country_code', $user_data['country_code'])->
													where('mobile_number', $user_data['mobileno'])->
													where('status', 1)->
													count_all_results();
											if ($ret > 0) {
												/* update otp */
												$this->db->where('country_code', $country_code);
												$this->db->where('mobile_number', $mobile_no);
												$this->db->where('status', 1);
												$save_otp = $this->db->update('mobile_otp', array('endtime' => $otp_data['endtime'], 'otp' => $otp_data['otp'], 'updated_on' => utc_date_conversion(date('Y-m-d H:i:s'))));
											} else {
												$save_otp = $this->api->save_otp($otp_data);
											}


											$response_code = '200';
											$response_message = 'OTP send successfully';
										}
										else
										{
											$response_code = '200';
											$response_message = 'Provider registered successfully';
										}
                                        
                                    } else {
                                        $response_code = '200';
                                        $response_message = 'Something went wrong. Please try again later.';
                                    }
                                } else {
                                    $response_code = '201';
                                    $response_message = 'Share Code Invalid';
                                }
//                            }
                        } else {
                            $response_code = '201';
                            $response_message = 'Please enter the required fields to register';
                        }
                    } elseif ($is_available_mobile == 1) {
                        if (!empty($user_data['name']) && !empty($user_data['email'])) {
                            $response_code = '201';
                            $response_message = 'Mobile number already exists as user. Please use another mobile number';
                        } else {

                            $default_otp = settingValue('default_otp');
                            if ($default_otp == 1) {
                                $otp = '1234';
                            } else {
                                $otp = rand(1000, 9999);
                            }

                            $message = 'Your OTP is ' . $otp . '';
                            $user_data['otp'] = $otp;

                            error_reporting(0);
                            $key = settingValue('sms_key');
                            $secret_key = settingValue('sms_secret_key');
                            $sender_id = settingValue('sms_sender_id');
                            require_once('vendor/nexmo/src/NexmoMessage.php');
                            $nexmo_sms = new NexmoMessage($key, $secret_key);
                            $result = $nexmo_sms->sendText($user_data['country_code'] . $user_data['mobileno'], $sender_id, $message);
                            $this->session->set_tempdata('otp', '$user_data', 300);

                            $otp_data = array(
                                'endtime' => time() + 300,
                                'mobile_number' => $user_data['mobileno'],
                                'country_code' => $user_data['country_code'],
                                'otp' => $otp,
                                'status' => 1
                            );

                            $ret = $this->db->select('*')->from('mobile_otp')->
                                    where('country_code', $user_data['country_code'])->
                                    where('mobile_number', $user_data['mobileno'])->
                                    where('status', 1)->
                                    count_all_results();
                            if ($ret > 0) {
                                /* update otp */
                                $this->db->where('country_code', $country_code);
                                $this->db->where('mobile_number', $mobile_no);
                                $this->db->where('status', 1);
                                $save_otp = $this->db->update('mobile_otp', array('endtime' => $otp_data['endtime'], 'otp' => $otp_data['otp'], 'updated_on' => utc_date_conversion(date('Y-m-d H:i:s'))));
                            } else {
                                $save_otp = $this->api->save_otp($otp_data);
                            }




                            $update_check = $this->api->update_device_details($user_data);

                            $response_code = '200';
                            $response_message = 'OTP send successfully';
                        }
                    }
					else {
                        $response_code = '500';
                        $response_message = 'Please fillin the required fields.';
                    }
						
					}
					else {
                    $response_code = '500';
                    $response_message = 'This Email ID is already registered.';
					}

                    
                } else {
                    $response_code = '500';
                    $response_message = 'This number is already registered as User.';
                }
            }
			if (!empty($user_data['email']) && !empty($user_data['password']) && !empty($user_data['device_type']) && empty($user_data['mobileno'])) {
				
				$update_check = $this->api->update_device_details($user_data);
				$response_code = '200';
				$response_message = 'OTP send successfully ';
			}
				
			
			



            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function generate_otp_user_post() {
        if ($this->users_id != 0 || ($this->default_token == $this->api_token)) {
            $data = array();
            $user_data = array();
            $user_data = $this->post();
			$login_type = settingValue('login_type');
            
            if (!empty($user_data['mobileno']) && !empty($user_data['country_code']) && !empty($user_data['device_type'])) {


                $is_available_mobile = $this->api->check_mobile_no($user_data);
                $is_available_mobileno = $this->api->check_user_mobileno($user_data);
                $is_available_provider = $this->api->check_mobile_no($user_data);
                if ($is_available_provider == 0) {
				 $is_available_email = $this->api->check_user_email($user_data);
				 if ($is_available_provider == 0) {
					 
					 if ($is_available_email == 0) {
							 if ($is_available_mobile == 0 && $is_available_mobileno == 0) {


							if (!empty($user_data['name']) && !empty($user_data['email']) && !empty($user_data['mobileno']) && !empty($user_data['country_code']) && !empty($user_data['device_type'])) {

								$user_details['name'] = $user_data['name'];
								$user_details['email'] = $user_data['email'];
								if(!empty($user_data['password']))
								{
									$user_details['password'] =md5($user_data['password']);
								}
								else
								{
									$user_details['password']='';
								}
								$user_details['mobileno'] = $user_data['mobileno'];
								$user_details['country_code'] = $user_data['country_code'];
								$user_details['currency_code'] = settings('currency');
								$device_data['device_type'] = $user_data['device_type'];
								$device_data['device_id'] = $user_data['device_id'];
								$username = strlen($user_data['name']);
								$user_details['share_code'] = $this->user_login->ShareCode(6, $username);
								$share_code = $user_data['get_code'];

	//                            if ($share_code) {
									$updateAmount = $this->api->UserShareCode($share_code);
									if ($updateAmount =='Empty code') {
										
										if($login_type=='email' && empty($user_details['password']))
										{
											$response_code = '201';
											$response_message = 'Please enter the password';
											$result = $this->data_format($response_code, $response_message, $data);
											$this->response($result, REST_Controller::HTTP_OK);
											exit;
										}

										$result = $this->api->user_signup($user_details, $device_data);
										
										

										if ($result != '') {
												if($login_type=='mobile' && empty($user_details['password']))
												{
													$default_otp = settingValue('default_otp');
													if ($default_otp == 1) {
														$otp = '1234';
													} else {
														$otp = rand(1000, 9999);
													}

													$message = 'Your OTP is' . $otp . '';
													$user_data['otp'] = $otp;

													error_reporting(0);
													$key = settingValue('sms_key');
													$secret_key = settingValue('sms_secret_key');
													$sender_id = settingValue('sms_sender_id');
													require_once('vendor/nexmo/src/NexmoMessage.php');
													$nexmo_sms = new NexmoMessage($key, $secret_key);
													$result = $nexmo_sms->sendText($user_data['country_code'] . $user_data['mobileno'], $sender_id, $message);
													$this->session->set_tempdata('otp', '$user_data', 300);

													$otp_data = array(
														'endtime' => time() + 300,
														'mobile_number' => $user_data['mobileno'],
														'country_code' => $user_data['country_code'],
														'otp' => $otp
													);

													$ret = $this->db->select('*')->from('mobile_otp')->
															where('country_code', $user_data['country_code'])->
															where('mobile_number', $user_data['mobileno'])->
															where('status', 1)->
															count_all_results();
													if ($ret > 0) {
														/* update otp */
														$this->db->where('country_code', $country_code);
														$this->db->where('mobile_number', $mobile_no);
														$this->db->where('status', 1);
														$save_otp = $this->db->update('mobile_otp', array('endtime' => $otp_data['endtime'], 'otp' => $otp_data['otp'], 'updated_on' => utc_date_conversion(date('Y-m-d H:i:s'))));
													} else {
														$save_otp = $this->api->save_otp($otp_data);
													}



													$response_code = '200';
													$response_message = 'OTP send successfully';
													
												}
												else
												{
													$response_code = '200';
													$response_message = 'User registered successfully';
												}
											
										} else {
											$response_code = '200';
											$response_message = 'Something went wrong. Please try again later.';
										}
									} else {
										$response_code = '201';
										$response_message = 'Share Code Invalid';
									}
	//                            }
							} else {
								$response_code = '201';
								$response_message = 'Please enter the required fields to register';
							}
						} elseif ($is_available_mobileno == 1) {

							if (!empty($user_data['name']) && !empty($user_data['email'])) {
								$response_code = '201';
								$response_message = 'Mobile number already exists as provider. Please use another mobile number';
							} else {


								$default_otp = settingValue('default_otp');
								if ($default_otp == 1) {
									$otp = '1234';
								} else {
									$otp = rand(1000, 9999);
								}

								$message = 'Your OTP is ' . $otp . '';
								$user_data['otp'] = $otp;

								error_reporting(0);
								$key = settingValue('sms_key');
								$secret_key = settingValue('sms_secret_key');
								$sender_id = settingValue('sms_sender_id');
								require_once('vendor/nexmo/src/NexmoMessage.php');
								$nexmo_sms = new NexmoMessage($key, $secret_key);
								$result = $nexmo_sms->sendText($user_data['country_code'] . $user_data['mobileno'], $sender_id, $message);
								$this->session->set_tempdata('otp', '$user_data', 300);

								$otp_data = array(
									'endtime' => time() + 300,
									'mobile_number' => $user_data['mobileno'],
									'country_code' => $user_data['country_code'],
									'otp' => $otp,
									'status' => 1
								);

								$ret = $this->db->select('*')->from('mobile_otp')->
										where('country_code', $user_data['country_code'])->
										where('mobile_number', $user_data['mobileno'])->
										where('status', 1)->
										count_all_results();
								if ($ret > 0) {
									/* update otp */
									$this->db->where('country_code', $country_code);
									$this->db->where('mobile_number', $mobile_no);
									$this->db->where('status', 1);
									$save_otp = $this->db->update('mobile_otp', array('endtime' => $otp_data['endtime'], 'otp' => $otp_data['otp'], 'updated_on' => utc_date_conversion(date('Y-m-d H:i:s'))));
								} else {
									$save_otp = $this->api->save_otp($otp_data);
								}



								$update_device = $this->api->update_device_user($user_data);

								$response_code = '200';
								$response_message = 'OTP send successfully';
							}
						} else {
							$response_code = '500';
							$response_message = 'Mobile number does not exit';
						}
						 
					 }
					 else {
						$response_code = '500';
						$response_message = 'This Email ID is already registered.';
					}
				 }
				 else {
					$response_code = '500';
					$response_message = 'This Email ID is already registered';
                }

                } else {
                    $response_code = '500';
                    $response_message = 'This number is already registered as Provider.';
                }
            }
			if (!empty($user_data['email']) && !empty($user_data['password']) && !empty($user_data['device_type']) && empty($user_data['mobileno'])) {
				
				$update_check = $this->api->update_device_user($user_data);
				$response_code = '200';
				$response_message = 'OTP send successfully ';
			}



            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function review_type_get() {

        $data = array();

        $rating_type = $this->api->get_rating_type();

        if (!empty($rating_type)) {
            $response_code = '200';
            $response_message = "Review type List";
            $data['review_type'] = $rating_type;
        } else {
            $response_code = '200';
            $response_message = "No Results found";
        }


        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function details_get() {

        $data = new stdClass();

        $user_post_data = getallheaders();

        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }

        $data = array();
        $response_code = '500';
        $response_message = 'Validation error';

        if (!empty($token)) {



            if (isset($_GET['type']) && !empty($_GET['type'])) {

                $type = $this->get('type');

                if ($type == 1) {
                    $user_id = $this->api->get_user_id_using_token($token);

                    $detail['id'] = $user_id;
                    $detail['type'] = $type;

                    $account_details = $this->api->accdetails_provider($detail);
                    $availability_details = $this->api->get_availability($detail['id']);


                    if ($account_details['account_number'] == '') {
                        $account_details = "0";
                    } elseif ($account_details['account_number'] != '') {
                        $account_details = "1";
                    }

                    if ($availability_details == '') {

                        $availability_details = "0";
                    } elseif ($availability_details != '') {

                        $availability_details = "1";
                    }



                    $response_code = '200';
                    $response_message = "Account status";
                    $data['account_details'] = $account_details;
                    $data['availability_details'] = $availability_details;
                } elseif ($type == 2) {

                    $user_id = $this->api->get_users_id_using_token($token);

                    $detail['id'] = $user_id;


                    $user_details = $this->api->accdetails_user($detail);
                    if ($user_details['account_number'] != '') {
                        $acc_detail = "1";
                    } else {
                        $acc_detail = "0";
                    }

                    $response_code = '200';
                    $response_message = "Account status";
                    $data['account_details'] = $acc_detail;
                }
            } else {
                $response_code = '200';
                $response_message = "Input field missing";
            }


            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function account_details_get() {

        $data = new stdClass();

        $user_post_data = getallheaders();

        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }

        $data = array();
        $response_code = '500';
        $response_message = 'Validation error';

        if (!empty($token)) {



            if (isset($_GET['type']) && !empty($_GET['type'])) {

                $type = $this->get('type');

                if ($type == 1) {
                    $user_id = $this->api->get_user_id_using_token($token);

                    $detail['id'] = $user_id;
                    $detail['type'] = $type;

                    $account_details = $this->api->accdetails_provider($detail);
                    if ($account_details != '') {

                        $response_code = '200';
                        $response_message = "Account details";
                        $data = $account_details;
                    } else {
                        $response_code = '200';
                        $response_message = "No records found";
                        $data = new stdClass();
                    }
                } elseif ($type == 2) {

                    $user_id = $this->api->get_users_id_using_token($token);

                    $detail['id'] = $user_id;


                    $user_details = $this->api->accdetails_user($detail);
                    if ($user_details != '') {
                        $response_code = '200';
                        $response_message = "Account details";
                        $data = $user_details;
                    } else {
                        $response_code = '200';
                        $response_message = "No records found";
                        $data = new stdClass();
                    }
                }
            } else {
                $response_code = '200';
                $response_message = "Input field missing";
            }


            $result = $this->data_format($response_code, $response_message, $data);

            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function token_error() {
        $response_code = '498';
        $response_message = "Invalid token or token missing";
        $data = [];
        $result = $this->data_format($response_code, $response_message, $data);

        $this->response($result, REST_Controller::HTTP_OK);
    }

    /* chat stored */


    /* chat stored */

    public function chat_post() {
        $data = array();
        $history = array();
        $response_code = '-1';
        $response_message = '';

        $params = $this->post();
        $user_post_data = getallheaders();

        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }
        if (!empty($token)) {
            if (!empty($params['to']) && !empty($params['content'])) {
                $user_data = array();


                $array = array();
                date_default_timezone_set('UTC');
                $date_time = date('Y-m-d H:i:s');
                $array['sender_token'] = $token;
                $array['receiver_token'] = $params['to'];
                $array['message'] = $params['content'];
                $array['utc_date_time'] = $date_time;
                $array['status'] = 1;
                $array['read_status'] = 0;
                date_default_timezone_set('Asia/Kolkata');
                $array['created_at'] = date('Y-m-d H:i:s');

                $history = $this->api->chat_conversation($array);
                $utctime = $history['utc_date_time'];
                $time = utc_date_conversion($history['utc_date_time']);
                $time = date('h:i A', strtotime($time));
                $history['date'] = date('Y-m-d', strtotime($time));
                $history['time'] = $time;

                $to_user_id = $history['receiver_token'];
                $from_user_id = $history['sender_token'];
                $message = $history['message'];

                $name = $this->api->username($from_user_id);
                $title = $name['name'];
                $from_userid = $name['id'];

                $name1 = $this->api->username($to_user_id);
                $to_username = $name1['name'];
                $to_userid = $name1['id'];

                $body = array(
                    'notification_type' => 'chat',
                    'title' => $title,
                    'message' => $message,
                    'from_username' => $title,
                    'to_username' => $to_username,
                    'from_userid' => $from_userid,
                    'to_userid' => $to_userid,
                    'date' => $history['created_at'],
                    'time' => $time,
                    'utctime' => $utctime
                );

                $is_provider = $this->api->get_user_id_using_token($params['to']);
                $is_user = $this->api->get_users_id_using_token($params['to']);
                if (!empty($is_user)) {
                    $device_tokens = $this->api->get_device_info_multiple($to_userid, 2);
                }
                if (!empty($is_provider)) {
                    $device_tokens = $this->api->get_device_info_multiple($to_userid, 1);
                }

                $deviceid = 0;


                $notificationdata = array();
                $notificationdata['body'] = $body;

                if (!empty($device_tokens)) {
                    foreach ($device_tokens as $key => $device) {
                        if (!empty($device['device_type']) && !empty($device['device_id'])) {

                            if (strtolower($device['device_type']) == 'android') {
                                $notify_structure = array(
                                    'title' => $title,
                                    'message' => $message,
                                    'image' => 'test22',
                                    'action' => 'test222',
                                    'action_destination' => 'test222',
                                );
                                sendFCMMessage($notify_structure, $device['device_id']);
                            }

                            if (strtolower($device['device_type'] == 'ios')) {

                                $notify_structure = array(
                                    'title' => $title,
                                    'alert' => $message,
                                    'badge' => 0,
                                    'sound' => 'default',
                                );
                                sendApnsMessage($notify_structure, $device['device_id']);
                            }
                        }
                    }
                }

                if (!empty($history)) {
                    $response_code = '200';
                    $response_message = 'Chats Fetched Successfully...';
                    $history = $history;
                } else {
                    $response_code = '500';
                    $response_message = 'Chats are Empty...';
                    $history = [];
                }
            } else {
                $response_code = '500';
                $response_message = 'Some Fields are Missing..';
                $history = [];
            }

            $result = $this->data_format($response_code, $response_message, $history);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function chat_details_post() {
        $user_post_data = getallheaders();

        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }


        if (!empty($token)) {
            $data = array();
            $response_code = '-1';

            $params = $this->post();

            if (!empty($params['chat_id']) && !empty($params['page'])) {
                $user_data = array();
                $user_data['token'] = $token;

                $id = $params['chat_id'];
                $page = $params['page'];
                $user_id = $this->api->get_user_id_using_token($user_data['token']);
                $history = $this->api->conversations($id, $user_id, $page);

                if (!empty($history)) {
                    $response_code = '200';
                    $response_message = 'Successfully Fetched....';
                    $data = $history;
                }
            } else {
                $response_code = '500';
                $response_message = "Field is Missing...!";
                $data = [];
            }

            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function insert_message_post() {
        extract($_POST);

        $data = array(
            "sender_token" => $fromToken,
            "receiver_token" => $toToken,
            "message" => $content,
            "status" => 1,
            "read_status" => 0,
            "created_at" => date('Y-m-d H:i:s'),
        );
        $val = $this->api->insert_msg($data);
        if ($val) {
            echo 1;
        } else {
            echo 0;
        }
    }

    /* get chat list */

    public function get_chat_list_post() {

        $user_post_data = getallheaders();

        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }


        if (!empty($token)) {
            $sent = [];
            $receive = [];
            $sent = $this->db->select('receiver_token as token')->
                            from('chat_table')->
                            where('sender_token', $token)->
                            get()->result_array();
            $receive = $this->db->select('sender_token as token')->
                            from('chat_table')->
                            where('receiver_token', $token)->
                            get()->result_array();

            $chat_tokens = (array_merge($sent, $receive));
            $test = [];
            foreach ($chat_tokens as $key => $value) {
                $test[] = $value['token'];
            }

            $token_detail = [];
            foreach (array_unique($test) as $key => $value) {

                $token_detail[] = $this->api->get_chat_list_info($value);
            }

            $response_code = '200';
            $response_message = "successfully fetched...!";
            $data = $token_detail;


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    /* get chat history */

    public function get_chat_history_post() {



        $user_post_data = getallheaders();

        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }


        $user_data = $this->post();
        if (!empty($token) && !empty($user_data['to_token'])) {
            $data['chat_history'] = $this->api->get_conversation_info($token, $user_data['to_token']);

            $response_code = '200';
            $response_message = "successfully fetched...!";
            $data = $data;


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    /* get flash device token */

    public function flash_device_token_post() {
        $user_post_data = getallheaders();

        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }
        $user_data = $this->post();
        if ($token != '8338d6ff4f0878b222f312494c1621a9') {
            if (!empty($token) && !empty($user_data['device_token']) && !empty($user_data['device_type'])) {
                $ret = $this->api->is_check_divesToken($user_data['device_token']);
                if ($ret) {
                    $user_id = $this->api->get_token_info($token)->id;

                    if (!empty($user_id) && !empty($user_data['device_token']) && !empty($user_data['device_type'])) {
                        $data = array('user_id' => $user_id, 'user_token' => $token, 'device_token' => $user_data['device_token'], 'device_type' => $user_data['device_type']);
                        $get_user_id = $this->api->insert_device_info($data); /* base on user_type */

                        $response_code = '200';
                        $response_message = "successfully fetched...!";
                        $data = [];
                    } else {
                        $response_code = '200';
                        $response_message = "User & Provider Empty...!";
                        $data = [];
                    }



                    $result = $this->data_format($response_code, $response_message, $data);
                    $this->response($result, REST_Controller::HTTP_OK);
                } else {
                    $response_code = '200';
                    $response_message = "already Inserted...!";
                    $data = [];
                    $result = $this->data_format($response_code, $response_message, $data);
                    $this->response($result, REST_Controller::HTTP_OK);
                }
            } else {
                $this->token_error();
            }
        } else {
            $response_code = '201';
            $response_message = "This is Static Key";
            $data = [];

            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        }
    }

    /* get notification list */

    public function get_notification_list_get() {

        $user_post_data = getallheaders();
        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }

        if (!empty($token)) {
            $data['notification_list'] = $this->api->get_notification_info($token);
            $response_code = '200';
            $response_message = "successfully fetched...!";


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function send_push_notification($token, $service_id, $type, $msg = '') {
        $data = $this->api->get_book_info($service_id);
        if (!empty($data)) {
            if ($type == 1) {
                $device_tokens = $this->api->get_device_info_multiple($data['provider_id'], 1);
            } else {
                $device_tokens = $this->api->get_device_info_multiple($data['user_id'], 2);
            }
            if ($type == 2) {
                $user_info = $this->api->get_user_info($data['user_id'], $type);
            } else {
                $user_info = $this->api->get_user_info($data['provider_id'], $type);
            }

            /* insert notification */

            $msg = ucfirst($user_info['name']) . ' ' . strtolower($msg);
            if (!empty($user_info['token'])) {
                $this->api->insert_notification($token, $user_info['token'], $msg);
            }

            $title = $data['service_title'];


            if (!empty($device_tokens)) {
                foreach ($device_tokens as $key => $device) {
                    if (!empty($device['device_type']) && !empty($device['device_id'])) {

                        if (strtolower($device['device_type']) == 'android') {

                            $notify_structure = array(
                                'title' => $title,
                                'message' => $msg,
                                'image' => 'test22',
                                'action' => 'test222',
                                'action_destination' => 'test222',
                            );
                            sendFCMMessage($notify_structure, $device['device_id']);
                        }

                        if (strtolower($device['device_type'] == 'ios')) {
                            $notify_structure = array(
                                'alert' => $msg,
                                'sound' => 'default',
                                'badge' => 1
                            );


                            sendApnsMessage($notify_structure, $device['device_id']);
                        }
                    }
                }
            }


            /* apns push notification */
        } else {
            $this->token_error();
        }
    }

    /* get wallet amount */

    public function get_wallet_amt_post() {

        $user_post_data = getallheaders();
        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }
        if (empty($token)) {
            $token = (!empty($_POST['token'])) ? $_POST['token'] : '';
        }

        /* get wallet */


        if (!empty($token)) {
            $data['wallet_info'] = $this->api->get_wallet($token);
            $response_code = '200';
            $response_message = "successfully fetched...!";


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    /* end with Wallet Info */



    /* push notification */

    public function send_push_notification_wallet($token, $type, $msg = '') {

        $data = $this->api->get_token_info($token);
        if (!empty($data)) {
            if ($type == 1) {
                $device_tokens = $this->api->get_device_info_multiple($data['provider_id'], 1);
            } else {
                $device_tokens = $this->api->get_device_info_multiple($data['user_id'], 2);
            }
            if ($type == 2) {
                $user_info = $this->api->get_user_info($data['user_id'], $type);
            } else {
                $user_info = $this->api->get_user_info($data['provider_id'], $type);
            }

            /* insert notification */

            $msg = ucfirst($user_info['name']) . ' ' . strtolower($msg);
            if (!empty($user_info['token'])) {
                $this->api->insert_notification($token, $user_info['token'], $msg);
            }

            $title = $data['service_title'];


            if (!empty($device_tokens)) {
                foreach ($device_tokens as $key => $device) {
                    if (!empty($device['device_type']) && !empty($device['device_id'])) {

                        if (strtolower($device['device_type']) == 'android') {

                            $notify_structure = array(
                                'title' => $title,
                                'message' => $msg,
                                'image' => 'test22',
                                'action' => 'test222',
                                'action_destination' => 'test222',
                            );

                            sendFCMMessage($notify_structure, $device['device_id']);
                        }

                        if (strtolower($device['device_type'] == 'ios')) {
                            $notify_structure = array(
                                'alert' => $msg,
                                'sound' => 'default',
                                'badge' => 0,
                            );


                            sendApnsMessage($notify_structure, $device['device_id']);
                        }
                    }
                }
            }


            /* apns push notification */
        } else {
            $this->token_error();
        }
    }

    /* Wallet */

    /* get wallet history */

    public function wallet_history_post() {
        $user_post_data = getallheaders();
        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }
        if (!empty($token)) {
            $params = $this->post();
            $history = $this->api->get_wallet_history_info($token);
            $user_id=$this->api->get_user_id_using_token($token);
        
            if($user_id){
                $stripe=$this->api->getSingleData('stripe_bank_details',['count(*) as id'],['user_id'=>$user_id]);
            }
        
       
            $his = [];
            if (!empty($history)) {
                $val = $this->db->select('*')->from('wallet_table')->where('token', $token)->get()->row();
				if($val->type == '1'){
				$provider_currency = get_api_provider_currency($val->user_provider_id);
				$UserCurrency = $provider_currency['user_currency_code'];
				}else{
				$provider_currency = get_api_user_currency($val->user_provider_id);
				$UserCurrency = $provider_currency['user_currency_code'];
				}
                foreach ($history as $key => $value) {

                    $his[$key]['id'] = $value['id'];
                    $his[$key]['token'] = $value['token'];
                    $his[$key]['user_provider_id'] = $value['user_provider_id'];
                    $his[$key]['type'] = $value['type'];
                    $his[$key]['currency'] = currency_code_sign($UserCurrency);
                    $his[$key]['current_wallet'] = get_gigs_currency($value['current_wallet'], $value['currency_code'], $UserCurrency);
                    $his[$key]['credit_wallet'] = get_gigs_currency($value['credit_wallet'], $value['currency_code'], $UserCurrency);
                    $his[$key]['debit_wallet'] = get_gigs_currency(strval(abs($value['debit_wallet'])), $value['currency_code'], $UserCurrency);
                    $his[$key]['avail_wallet'] = get_gigs_currency($value['avail_wallet'], $value['currency_code'], $UserCurrency);
					if($value['payment_detail'] == 'paypal' || $value['payment_detail'] == 'razorpay'){
						$his[$key]['total_amt'] = get_gigs_currency(strval(abs($value['total_amt'])), $value['currency_code'], $UserCurrency);
						$his[$key]['txt_amt'] = get_gigs_currency(strval(abs($value['fee_amt'])), $value['currency_code'], $UserCurrency);						
					}else{
						$his[$key]['total_amt'] = get_gigs_currency(strval(abs($value['total_amt'] / 100)), $value['currency_code'], $UserCurrency);
						$his[$key]['txt_amt'] = get_gigs_currency(strval(abs($value['fee_amt'] / 100)), $value['currency_code'], $UserCurrency);						
					}
                    $his[$key]['reason'] = $value['reason'];
                    $his[$key]['created_at'] = $value['created_at'];
                }
                $response_code = '200';
                $response_message = 'Fetched successfully...';
                $data['wallet_info'] = array();
                $data['wallet_info']['wallet'] = (object) $this->api->get_wallet($token);
                $data['wallet_info']['wallet_history'] = $his;
                $data['stripe_bank'] = (string) ($stripe->id) ? $stripe->id : "0";
            } else {
                $response_code = '200';
                $response_message = 'Fetched successfully...';
                $data['wallet_info'] = array();
                $data['wallet_info']['wallet'] = (object) $this->api->get_wallet($token);
                $data['wallet_info']['wallet_history'] = [];
            }

            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    /* Add wallet amount */

    public function add_user_wallet_post() {

        $user_post_data = getallheaders();
        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }
        $params = $this->post();
        if (empty($token)) {
            $token = (!empty($params['Token'])) ? $params['Token'] : '';
        }
        if (!empty($token)) {
            $params = $this->post();

			if($params['paytype'] == 'stripe'){

				if (!empty($params['amount']) && !empty($params['tokenid']) && $params['amount'] > 0) {

					$check_card = $this->db->get_where('stripe_customer_table', array('user_token' => $token))->row();

					if (!empty($check_card->user_token) && $check_card->user_token == $token) {
						/* create card info based on customer */

						$cust_info = $this->stripe->retrieve_customer($check_card->cust_id, $this->data['secret_key']);

						if (!empty($cust_info)) {

							$data['source'] = $params['tokenid']; /* The type of payment source. Should be card. */


							/* create customern in stripe */
							$create_cust = $this->stripe->create_card($data, $check_card->cust_id);
							$card_data = json_decode($create_cust);
							if (!empty($card_data) && !empty($card_data->id)) {
								$card_info['user_token'] = $token;
								$card_info['stripe_token'] = $params['tokenid'];
								$card_info['cust_id'] = $check_card->cust_id;
								$card_info['card_id'] = $card_data->id;
								$card_info['pay_type'] = $card_data->object;
								$card_info['brand'] = $card_data->brand;
								$card_info['cvc_check'] = $card_data->cvc_check;
								$card_info['card_number '] = $card_data->last4;
								$card_info['card_exp_month'] = $card_data->exp_month;
								$card_info['card_exp_year'] = $card_data->exp_year;
								$card_info['status'] = 1;
								$card_info['created_at  '] = date('Y-m-d H:i:s');

								$vals = $this->db->insert('stripe_customer_card_details', $card_info);

								/* payment on stripe */
								$charges_array = array();
								$amount = $params['amount'];
								$amount = ($amount * 100);
								$charges_array['amount'] = $amount;
								$charges_array['currency'] = $params['currency'];
								$charges_array['customer'] = $card_info['cust_id'];
								$charges_array['source'] = $card_info['card_id'];
								$charges_array['expand'] = array('balance_transaction');

								$result = $this->stripe->stripe_charges($charges_array);


								$pay_info = json_decode($result);
								if ($vals) {
									$deleted = $this->stripe->delete_card($card_info['cust_id'], $card_info['card_id']);

									$delete_card = json_decode($deleted);

									if (empty($delete_card->error)) {
										$wallet_data['status'] = 0;
										$wallet_data['updated_on'] = date('Y-m-d H:i:s');
										$WHERE = array('cust_id' => $card_info['cust_id'], 'card_id' => $card_info['card_id']);
										$result = $this->api->update_customer_card($wallet_data, $WHERE);
									}
								}

								if (empty($pay_info->error)) {
									/* wallet infos */

									$user_info = $this->api->get_token_info($token);

									$wallet = $this->api->get_wallet($token);

									$curren_wallet = get_gigs_currency($wallet['wallet_amt'], $wallet['currency_code'], "USD");

									/* wallet infos */


									$history_pay['token'] = $token;
									$history_pay['currency_code']="USD";
									$history_pay['user_provider_id'] = $user_info->id;
									$history_pay['type'] = $user_info->type;
									$history_pay['tokenid'] = $params['tokenid'];
									$history_pay['payment_detail'] = $result;
									$history_pay['charge_id'] = $pay_info->id;
									$history_pay['transaction_id'] = $pay_info->balance_transaction->id;
									$history_pay['exchange_rate'] = $pay_info->balance_transaction->exchange_rate;
									$history_pay['paid_status'] = $pay_info->paid;
									$history_pay['cust_id'] = $pay_info->source->customer;
									$history_pay['card_id'] = $pay_info->source->id;
									$history_pay['total_amt'] = $pay_info->balance_transaction->amount;
									$history_pay['fee_amt'] = $pay_info->balance_transaction->fee;
									$history_pay['net_amt'] = $pay_info->balance_transaction->net;
									$history_pay['amount_refund'] = $pay_info->amount_refunded;
									$history_pay['current_wallet'] = $curren_wallet;
									$history_pay['credit_wallet'] = (($pay_info->balance_transaction->net) / 100);
									$history_pay['debit_wallet'] = 0;
									$history_pay['avail_wallet'] = (($pay_info->balance_transaction->net) / 100) + $curren_wallet;
									$history_pay['reason'] = TOPUP;
									$history_pay['created_at'] = date('Y-m-d H:i:s');

									if ($this->db->insert('wallet_transaction_history', $history_pay)) {
										/* update wallet table */
										$wallet_dat['currency_code']=$wallet['currency_code'];
										$wallet_dat['wallet_amt'] = get_gigs_currency(($curren_wallet + $history_pay['credit_wallet']),"USD",$wallet['currency_code']);
										$wallet_dat['updated_on'] = date('Y-m-d H:i:s');
										$WHERE = array('token' => $token);
										$result = $this->api->update_wallet($wallet_dat, $WHERE);
										/* payment on stripe */
										$response_code = '200';
										$response_message = 'Amount added to wallet successfully';
										$data['data'] = 'Successfully added to wallet...';
									} else {
										$response_code = '200';
										$response_message = 'Stripe payment issue';
										$data['data'] = 'history issues';
									}
								} else {
									$response_code = '200';
									$response_message = 'Stripe payment issue';
									$data['data'] = [];
								}
							} else {
								$response_code = '200';
								$response_message = 'card not created by customer...';
								$data['data'] = $card_data->error;
							}
						} else {
							$response_code = '200';
							$response_message = 'Stripe payment issue';
							$data['error'] = 'Not stored in card info';
						}

						$result = $this->data_format($response_code, $response_message, $data);
						$this->response($result, REST_Controller::HTTP_OK);
					} else {

						/* create new customer and card info */
						$user_info = $this->api->get_token_info($token);

						$data['email'] = $user_info->email;
						$data['source'] = $params['tokenid'];
						$create_cust = $this->stripe->customer_create($data);

						$cust = json_decode($create_cust);
						if (empty($cust->error)) {
							$cr_stripe_cust['cust_id'] = $cust->id;
							$cr_stripe_cust['user_token'] = $token;
							$cr_stripe_cust['created_at'] = date('Y-m-d H:i:s');

							if ($this->db->insert('stripe_customer_table', $cr_stripe_cust)) {
								if (!empty($cust->sources)) {
									foreach ($cust->sources->data as $key => $value) {
										$card_info['user_token'] = $token;
										$card_info['stripe_token'] = $params['tokenid'];
										$card_info['cust_id'] = $value->customer;
										$card_info['card_id'] = $value->id;
										$card_info['pay_type'] = $value->object;
										$card_info['brand'] = $value->brand;
										$card_info['cvc_check'] = $value->cvc_check;
										$card_info['card_number '] = $value->last4;
										$card_info['card_exp_month'] = $value->exp_month;
										$card_info['card_exp_year'] = $value->exp_year;
										$card_info['status'] = 1;
										$card_info['created_at  '] = date('Y-m-d H:i:s');

										$vals = $this->db->insert('stripe_customer_card_details', $card_info);
									}
								}
							}
							/* create payment in stripe in stripe */

							/* payment on stripe */
							$charges_array = array();
							$amount = $params['amount'];
							$amount = ($amount * 100);
							$charges_array['amount'] = $amount;
							$charges_array['currency'] = $params['currency'];
							$charges_array['customer'] = $card_info['cust_id'];
							$charges_array['source'] = $card_info['card_id'];
							$charges_array['expand'] = array('balance_transaction');

							$result = $this->stripe->stripe_charges($charges_array);


							$pay_info = json_decode($result);

							if ($vals) {
								/* delete card */
								$deleted = $this->stripe->delete_card($card_info['cust_id'], $card_info['card_id']); //remove card
								$delete_card = json_decode($deleted);

								if (empty($delete_card->error)) {
									$wallet_data['status'] = 0;
									$wallet_data['updated_on'] = date('Y-m-d H:i:s');
									$WHERE = array('cust_id' => $card_info['cust_id'], 'card_id' => $card_info['card_id']);
									$result = $this->api->update_customer_card($wallet_data, $WHERE);
								}
							}
							 if (empty($pay_info->error)) {
									/* wallet infos */

									$user_info = $this->api->get_token_info($token);

									$wallet = $this->api->get_wallet($token);

									$curren_wallet = get_gigs_currency($wallet['wallet_amt'], $wallet['currency_code'], "USD");

									/* wallet infos */


									$history_pay['token'] = $token;
									$history_pay['currency_code']="USD";
									$history_pay['user_provider_id'] = $user_info->id;
									$history_pay['type'] = $user_info->type;
									$history_pay['tokenid'] = $params['tokenid'];
									$history_pay['payment_detail'] = $result;
									$history_pay['charge_id'] = $pay_info->id;
									$history_pay['transaction_id'] = $pay_info->balance_transaction->id;
									$history_pay['exchange_rate'] = $pay_info->balance_transaction->exchange_rate;
									$history_pay['paid_status'] = $pay_info->paid;
									$history_pay['cust_id'] = $pay_info->source->customer;
									$history_pay['card_id'] = $pay_info->source->id;
									$history_pay['total_amt'] = $pay_info->balance_transaction->amount;
									$history_pay['fee_amt'] = $pay_info->balance_transaction->fee;
									$history_pay['net_amt'] = $pay_info->balance_transaction->net;
									$history_pay['amount_refund'] = $pay_info->amount_refunded;
									$history_pay['current_wallet'] = $curren_wallet;
									$history_pay['credit_wallet'] = (($pay_info->balance_transaction->net) / 100);
									$history_pay['debit_wallet'] = 0;
									$history_pay['avail_wallet'] = (($pay_info->balance_transaction->net) / 100) + $curren_wallet;
									$history_pay['reason'] = TOPUP;
									$history_pay['created_at'] = date('Y-m-d H:i:s');

									if ($this->db->insert('wallet_transaction_history', $history_pay)) {
										/* update wallet table */
										$wallet_dat['currency_code']=$wallet['currency_code'];
										$wallet_dat['wallet_amt'] = get_gigs_currency(($curren_wallet + $history_pay['credit_wallet']),"USD",$wallet['currency_code']);
										$wallet_dat['updated_on'] = date('Y-m-d H:i:s');
										$WHERE = array('token' => $token);
										$result = $this->api->update_wallet($wallet_dat, $WHERE);
										/* payment on stripe */
										$response_code = '200';
										$response_message = 'Amount added to wallet successfully';
										$data['data'] = 'Successfully added to wallet...';
									} else {
										$response_code = '200';
										$response_message = 'Stripe payment issue';
										$data['data'] = 'history issues';
									}
								} else {
								$response_code = '200';
								$response_message = 'Stripe payment issue';
								$data['data'] = 'history issues';
							}
						} else {
							$response_code = '400';
							$response_message = 'This token Already Used...';
							$data['data'] = 'token already used...';
						}
					}


					$result = $this->data_format($response_code, $response_message, $data);
					$this->response($result, REST_Controller::HTTP_OK);
				} else {
					$response_code = '200';
					$response_message = 'Stripe payment issue';
					$data['error'] = $result['error'];

					$result = $this->data_format($response_code, $response_message, $data);
					$this->response($result, REST_Controller::HTTP_OK);
				}
			}
			else if($params['paytype'] == 'razorpay'){				
				$usertoken     = $params['token'];
				$user          = $this->db->where('token', $token)->get('users')->row_array();
				//print_r($params['amount']);
				//$params        = $this->input->get();
				$token         = $this->session->userdata('chat_token');
				$user_id       = $user['id'];
				$user_name     = $user['name'];
				$user_token    = $user['token'];
				$currency_type = $user['currency_code'];
				$amt = $params['amount'];
				$wallet = $this->db->where('user_provider_id', $user_id)->limit(1)->order_by('id',"DESC")->get('wallet_table')->row_array();
				
				$wallet_amt = $wallet['wallet_amt'];
				if($wallet_amt){
					$current_wallet = $wallet_amt;
				}else{
					$current_wallet = $amt;
				}	 				
				$history_pay['token']=$user_token;
				$history_pay['currency_code'] = $currency_type;
				$history_pay['user_provider_id']=$user_id;
				$history_pay['type']='2';
				$history_pay['tokenid']=$user_post_data['tokenid'];
				$history_pay['payment_detail']="Razorpay";
				$history_pay['charge_id']=1;
				$history_pay['exchange_rate']=0;
				$history_pay['paid_status']="pass";
				$history_pay['cust_id']="self";
				$history_pay['card_id']="self";
				$history_pay['total_amt']=$amt;
				$history_pay['fee_amt']=0;
				$history_pay['net_amt']=$amt*100;
				$history_pay['amount_refund']=0;
				$history_pay['current_wallet']=$current_wallet;
				$history_pay['credit_wallet']=$amt;
				$history_pay['debit_wallet']=0;
				$history_pay['avail_wallet']=$amt + $current_wallet;
				$history_pay['reason']=TOPUP;
				$history_pay['created_at']=date('Y-m-d H:i:s');
				if($this->db->insert('wallet_transaction_history',$history_pay)){
					
					$this->db->where('user_provider_id', $user_id)->update('wallet_table', array(
						'currency_code' => 'INR',
						'wallet_amt' => $amt+$current_wallet
					));
					$response_code = '200';
					$response_message = 'Amount added to wallet successfully';
					$data['data'] = 'Successfully added to wallet...';               
				}else{
					$response_code = '200';
					$response_message = 'RazorPay payment issue';
					$data['data'] = 'history issues';                
				}
				$result = $this->data_format($response_code, $response_message, $data);
				$this->response($result, REST_Controller::HTTP_OK);
			}
        } else {
            $this->token_error();
        }
    }

    /* get card based on customer */

    public function get_customer_saved_card_post() {
        $user_post_data = getallheaders();
        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }
        if (!empty($token)) { //main loop
            $ret_val = $this->api->get_customer_based_card_list($token);

            if (!empty($ret_val)) {
                $response_code = '200';
                $response_message = 'fetched successfully';
                $data['data'] = $ret_val;
            } else {
                $response_code = '200';
                $response_message = 'data was empty...';
                $data['data'] = [];
            }


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    /*provider withdrawal*/

    public function provider_wallet_withdrawal_post() {
        $params = $this->post();
        $user_post_data = getallheaders();
        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }
        if (empty($token)) {
            $token = (!empty($params['Token'])) ? $params['Token'] : '';
        }
        if (!empty($token)) { //main loop
            if (!empty($params['amount']) && !empty($params['tokenid']) && $params['amount'] > 0) {
            $user_info = $this->api->get_token_info($token);
             $amount= get_gigs_currency($params['amount'],$params['currency_val'],"USD");
            $paypal_response = $this->api->paypal_payout($user_info->email,$amount) ;
            $trans_info = json_decode($paypal_response);
    
            if (!empty($trans_info->batch_header->payout_batch_id)) {
                $user_info = $this->api->get_token_info($token);
                $wallet = $this->api->get_wallet($token);
                $curren_wallet = get_gigs_currency($wallet['wallet_amt'], $wallet['currency_code'],$params['currency_val']);
                /*wallet infos*/
                $history_pay['token'] = $token;
                $history_pay['user_provider_id'] = $user_info->id;
                $history_pay['currency_code']=$params['currency_val'];
                $history_pay['type'] = $user_info->type;
                $history_pay['tokenid'] = $params['tokenid'];
                $history_pay['payment_detail'] = $paypal_response; //response
                $history_pay['charge_id'] = $trans_info->batch_header->payout_batch_id;
                $history_pay['transaction_id'] = 0;
                $history_pay['exchange_rate'] = 0;
                $history_pay['paid_status'] = "Completed";
                $history_pay['cust_id'] = $trans_info->batch_header->payout_batch_id;
                $history_pay['card_id'] = 0;
                $history_pay['total_amt'] = $params['amount'];
                $history_pay['fee_amt'] = 0;
                $history_pay['net_amt'] = $params['amount']*100;
                $history_pay['amount_refund'] = 0;
                $history_pay['current_wallet'] = $curren_wallet;
                $history_pay['credit_wallet'] = 0;
                $history_pay['debit_wallet'] = (($params['amount']));
                $history_pay['avail_wallet'] = $curren_wallet - $params['amount'] ;
                $history_pay['reason'] = WITHDRAW;
                $history_pay['created_at'] = date('Y-m-d H:i:s');
                if ($this->db->insert('wallet_transaction_history', $history_pay)) {
                    /*update wallet table*/
                    $wallet_data['currency_code']=$wallet['currency_code'];
                    $wallet_data['wallet_amt'] = get_gigs_currency(($curren_wallet - $history_pay['debit_wallet']),$params['currency_val'],$wallet['currency_code']);
                    $wallet_data['updated_on'] = date('Y-m-d H:i:s');
                    $WHERE = array('token' => $token);
                    $result = $this->api->update_wallet($wallet_data, $WHERE);
                    /*payment on stripe*/
                    $response_code = '200';
                    $response_message = 'Amount transfered  successfully';
                    $data['data'] = 'Successfully added to wallet...';
                } else {
                    $response_code = '400';
                    $response_message = 'Stripe payment issue';
                    $data['data'] = 'history issues';
                }
            } else {
                $response_code = '400';
                $response_message = 'Wallet transaction not succeed...!';
                $data['data'] = 'payout failed';
            }
            /*transfer account via card*/
                        
                    /*transfer amount via card*/
                    $result = $this->data_format($response_code, $response_message, $data);
                    $this->response($result, REST_Controller::HTTP_OK);
            } else {
                $response_code = '400';
                $response_message = 'Invalid Email or information...';
                $data['data'] = [];
                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            }
        } else {
            $this->token_error();
        }
    }
    /* provider card info */

    public function provider_card_info_post() {
        $user_post_data = getallheaders();
        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }
        if (!empty($token)) { //main loop
            $ret_val = $this->api->get_provider_based_card_list($token);

            if (!empty($ret_val)) {
                $response_code = '200';
                $response_message = 'fetched successfully';
                $data['data'] = $ret_val;
            } else {
                $response_code = '200';
                $response_message = 'data was empty...';
                $data['data'] = [];
            }


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function stripe_details_get() {

        $user_post_data = getallheaders();
        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }
        if (!empty($token)) {

            $result = $this->api->token_is_valid_provider($token);
            if (empty($result)) {
                $result = $this->api->token_is_valid($token);
            }

            if ($result) {

                $publishable_key = '';
                $secret_key = '';
                $live_publishable_key = '';
                $live_secret_key = '';
                $stripe_option = '';
				$razorpay_apikey = '';
				$razorpay_secret_key = '';
				$live_razorpay_apikey = '';
				$live_razorpay_secret_key = '';

                $query = $this->db->query("select * from system_settings WHERE status = 1");
                $result = $query->result_array();
                if (!empty($result)) {
                    foreach ($result as $datas) {


                        if ($datas['key'] == 'secret_key') {

                            $secret_key = $datas['value'];
                        }

                        if ($datas['key'] == 'publishable_key') {

                            $publishable_key = $datas['value'];
                        }

                        if ($datas['key'] == 'live_secret_key') {

                            $live_secret_key = $datas['value'];
                        }

                        if ($datas['key'] == 'live_publishable_key') {

                            $live_publishable_key = $datas['value'];
                        }

                        if ($datas['key'] == 'stripe_option') {

                            $stripe_option = $datas['value'];
                        }
						
						if ($datas['key'] == 'razor_option') {

                            $razor_option = $datas['value'];
                        }
						
						if($datas['key'] == 'razorpay_apikey'){
							$razorpay_apikey = $datas['value'];
						}
						
						if($datas['key'] == 'razorpay_secret_key'){
							$razorpay_secret_key = $datas['value'];
						}
						
						if($datas['key'] == 'live_razorpay_apikey'){
							$live_razorpay_apikey = $datas['value'];
						}
						if($datas['key'] == 'live_razorpay_secret_key'){
							$live_razorpay_secret_key = $datas['value'];
						}
                    }
                }
                $stripedetails = array();

                if (@$stripe_option == 1) {

                    $stripedetails['publishable_key'] = $publishable_key;

                    $stripedetails['secret_key'] = $secret_key;
                }

                if (@$stripe_option == 2) {

                    $stripedetails['publishable_key'] = $live_publishable_key;

                    $stripedetails['secret_key'] = $live_secret_key;
                }

				if(@$stripe_option == 1){
					$stripedetails['razorpay_apikey'] = $razorpay_apikey;
					$stripedetails['razorpay_secret_key'] = $razorpay_secret_key;
				}
				
				if(@$stripe_option == 2){
					$stripedetails['live_razorpay_apikey'] = $live_razorpay_apikey;
					$stripedetails['live_razorpay_secret_key'] = $live_razorpay_secret_key;
				}

				$stripedetails['braintree_key'] = settingValue('braintree_key');

                if (!empty($stripedetails)) {
                    $response_code = '200';
                    $response_message = 'Fetched successfully...';
                    $data = $stripedetails;
                } else {
                    $response_code = '200';
                    $response_message = 'Fetched successfully...';
                    $data = [];
                }
            } else {

                $response_code = "500";
                $response_message = "Token is Invalid";
                $data = [];
            }
        } else {
            $this->token_error();
            $data = [];
        }
        $result = $this->data_format($response_code, $response_message, $data);
        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function update_myservice_status_post() {
        $user_data = array();
        $user_data = $this->post();

        $user_post_data = getallheaders();
        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }
        //check status
        if ($user_data['status'] != 1 && $user_data['status'] != 2) {
            $response_code = "200";
            $response_message = "Service Status is Invalid.";
            $data = [];
            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        }
        //end
        if (!empty($token)) {
            $result = $this->api->token_is_valid_provider($token);
            if ($result) {

                if (!empty($user_data['service_id']) && !empty($user_data['status'])) {

                    $provider = $this->db->where('token=', $token)->get('providers')->row_array();

                    $service_booking = $this->api->get_service_bookingdetails($provider['id'], $user_data['service_id']);
                    if ($service_booking == 0) {

                        $service_update = $this->db->where('id', $user_data['service_id'])->update('services', ['status' => $user_data['status']]);

                        if ($service_update == true) {
                            $response_code = "200";
                            $response_message = "Service Status Updated Successfully.";
                            $data = [];
                        } else {
                            $response_code = "500";
                            $response_message = "Service Status Not Update.";
                            $data = [];
                        }
                    } else {
                        $response_code = "500";
                        $response_message = "This Service is already Booked and status not changed.";
                        $data = [];
                    }
                } else {
                    $response_code = "500";
                    $response_message = "Some fields are Missing";
                    $data = [];
                }
            } else {
                $response_code = "500";
                $response_message = "Token is Invalid";
                $data = [];
            }
        } else {
            $this->token_error();
            $data = [];
        }
        $result = $this->data_format($response_code, $response_message, $data);
        $this->response($result, REST_Controller::HTTP_OK);
    }

    public function language_list_get() {
        $user_data = array();
        $user_data = $this->post();

        $data = array();

        $result = $this->api->languages_list();

        if (!empty($result)) {

            $response_code = '200';
            $response_message = $this->language_content['lg_success'];
            $data = $result;

            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $response_code = '404';
            $status = FALSE;
            $response_message = $this->language_content['lg_no_language_were_found'];
            $data = [];

            $result = $this->data_format($response_code, $status, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        }
    }

    public function language_post() {
        $user_data = array();
        $user_data = $this->post();


        $data = array();


        if (!empty($user_data['language'])) {

            $result = $this->api->language_list($user_data['language']);

            if (!empty($result)) {

                $response_code = '200';
                $response_message = $this->language_content['lg_success'];
                $data = $result;

                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            } else {

                $response_code = '404';
                $response_message = $this->language_content['lg_no_language_were_found'];
                $data = [];

                $result = $this->data_format($response_code, $response_message, $data);
                $this->response($result, REST_Controller::HTTP_OK);
            }
        } else {
            $response_code = '404';
            $response_message = $this->language_content['lg_no_language_were_found'];
            $data = [];

            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        }
    }

    public function currency_list_get() {

        $user_data = array();
        $user_data = $this->post();

        $data = array();

        $result = $this->db->where('status', 1)->select('id,currency_code')->get('currency_rate')->result_array();
        // print_r($results);exit;

        if (!empty($result)) {


            $response_code = '200';
            $response_message = $this->language_content['lg_success'];
            $data = $result;

            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {

            $response_code = '404';
            $response_message = $this->language_content['lg_no_language_were_found'];
            $data = [];

            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        }
    }

    public function stripe_account_details_post() {

        $data = new stdClass();
        $user_data = array();
        $user_data = $this->post();
        $user_post_data = getallheaders();

//        print_r($user_data);exit;

        $token = (!empty($user_post_data['token'])) ? $user_post_data['token'] : '';
        if (empty($token)) {
            $token = (!empty($user_post_data['Token'])) ? $user_post_data['Token'] : '';
        }

        $data = array();
        $response_code = '500';
        $response_message = 'Validation error';

        if (!empty($token)) {

            if (!empty($user_data['account_holder_name']) && !empty($user_data['account_number']) && !empty($user_data['account_iban']) && !empty($user_data['bank_name']) && !empty($user_data['bank_address']) && (!empty($user_data['sort_code']) || !empty($user_data['routing_number']) || !empty($user_data['account_ifsc']))) {

                $user_id = $this->api->get_user_id_using_token($token);
                $WHERE = array('id' => $user_id);
                $whr = array('user_id' => $user_id);

                $result = $this->api->update_data('providers', $user_data, $WHERE);
                $count = $this->api->CountRows('stripe_bank_details', $whr);
                if (!empty($count)) {
                    $stripe = $this->api->update_data('stripe_bank_details', $user_data, $whr);
                } else {
                    $user_data['user_id'] = $user_id;
                    $stripe = $this->api->update_data('stripe_bank_details', $user_data);
                }

                if ($result) {
                    $response_code = '200';
                    $response_message = "Account details updated Successfully";
                } else {
                    $response_code = '200';
                    $response_message = "No Results found";
                }
            } else {
                $response_code = '200';
                $response_message = "Input field missing";
            }


            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function wallet_withdraw_post() {
        if ($this->user_id != 0 || ($this->default_toke == $this->api_token)) {
            $user_data = $this->post();
            $user_details = $this->db->where('token', $this->api_token)->get('providers')->row_array();
            $user_currency = get_api_provider_currency($this->user_id);
            if (!empty($user_data['amount'])) {
                if (empty($user_data['currency_code'])) {
                    $user_data['currency_code'] = $user_details['currency_code'];
                }
                if (!empty($user_data['amount']) && !empty($user_data['currency_code'])) {
                    $wallet_data = array(
                        'user_id' => $this->user_id,
                        'amount' => $user_data['amount'],
                        'currency_code' => $user_details['currency_code'],
                        'status' => 1,
                        'transaction_status' => 0,
                        'request_payment' => 'stripe',
                        'created_by' => $this->user_id
                    );
                    $amount = $this->db->insert('wallet_withdraw', $wallet_data);
//                                    print_r($amount);exit;
                    if ($amount == true) {
                        $amount_withdraw = $this->Stripe_model->wallet_withdraw_flow($user_data['amount'], $user_currency['user_currency_code'], $this->user_id, '');
                    }
                    if ($amount == true) {
                        $response_code = 200;
                        $response_message = 'SUCCESS';
                        $data = 'Successfully withdraw in wallet...';
                    } else {
                        $response_code = 404;
                        $response_message = $this->language_content['lg_something_is_wrong_please_try_again_later'];
                        $data = [];
                    }
                }
            } else {
                $response_code = 404;
                $response_message = $this->language_content['lg_input_params_missing'];
                $data = [];
            }
            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function BraintreePaypal_post() {
        if ($this->api_token !='') {
            $user_data = array();
            $user_data = $this->post();			
            $data = array();
            if (!empty($user_data['amount']) && !empty($user_data['payload_nonce']) && !empty($user_data['orderID'])) {

                $amount = $user_data['amount'];
                $orderId = $user_data['orderID'];
                $payload_nonce = $user_data['payload_nonce'];
                require_once 'vendor/autoload.php';	
                require_once 'vendor/braintree/braintree_php/lib/Braintree.php';
                $gateway = new Braintree\Gateway([
                    'environment' => 'sandbox',
                    'merchantId' => 'pd6gznv7zbrx9hb8',
                    'publicKey' => 'h8bydrz7gcjkp7d4',
                    'privateKey' => '47b83ae8fdcf23342f71b21c1a9a6223'
                ]);

                if ($gateway) {

                    $result = $gateway->transaction()->sale([
                        'amount' => $amount,
                        'paymentMethodNonce' => $payload_nonce,
                        'orderId' => $orderId,
                        'options' => [
                        'submitForSettlement' => True
                        ],
                    ]);

                    if ($result->success) {
                        $transaction_id = $result->transaction->id;

                        $res = $this->paypal_success($amount, $orderId, $transaction_id, $this->api_token);
                        if (!empty($res)) {

                            $response_code = '200';
                            $response_message = $this->language_content['lg_success'];
                            $data = $res;
                        } else {

                            $response_code = '404';
                            $response_message = $this->language_content['lg_no_language_were_found'];
                            $data = [];
                        }
                    } else {
                        $response_code = '404';
                        $response_message = $this->language_content['lg_no_language_were_found'];
                        $data = [];
                    }
                } else {
                    $response_code = '404';
                    $response_message = $this->language_content['lg_no_language_were_found'];
                    $data = [];
                }
            }
            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $this->token_error();
        }
    }

    public function paypal_success($amt, $orderId, $transaction_id, $token_id) {
        $token = $token_id;

        $user_info = $this->api->get_token_info($token);
        $wallet = $this->api->get_wallet($token);
        $curren_wallet = $wallet['wallet_amt'];
        /* wallet infos */
        $pay_data = array(
            'transaction_id' => $transaction_id,
            'order_id' => $orderId,
            'amount' => $amt,
            'user_id' => $user_info->id,
            'created_at' => date('Y-m-d H:i:s')
        );
        $paypal = $this->db->insert('paypal_transaction', $pay_data);
        $pay_transaction = $this->db->insert_id();
        $history_pay['token'] = $token;
		$history_pay['currency_code'] = $wallet['currency_code'];
        $history_pay['user_provider_id'] = $user_info->id;
        $history_pay['type'] = $user_info->type;
        $history_pay['tokenid'] = $token;
        $history_pay['payment_detail'] = "paypal";
        $history_pay['charge_id'] = 1;
        $history_pay['transaction_id'] = $pay_transaction;
        $history_pay['exchange_rate'] = 0;
        $history_pay['paid_status'] = "pass";
        $history_pay['cust_id'] = "self";
        $history_pay['card_id'] = "self";
        $history_pay['total_amt'] = $amt;
        $history_pay['fee_amt'] = 0;
        $history_pay['net_amt'] = $amt;
        $history_pay['amount_refund'] = 0;
        $history_pay['current_wallet'] = $curren_wallet;
        $history_pay['credit_wallet'] = $amt;
        $history_pay['debit_wallet'] = 0;
        $history_pay['avail_wallet'] = $amt + $curren_wallet;
        $history_pay['reason'] = 'TOPUP';
        $history_pay['created_at'] = date('Y-m-d H:i:s');

        if ($this->db->insert('wallet_transaction_history', $history_pay)) {
            /* update wallet table */
            $wallet_amt = $curren_wallet + $amt;
            $amt_wallent=get_gigs_currency($wallet_amt,$history_pay['currency_code'],$wallet['currency_code']);
            $wallet_dat['wallet_amt']= $amt_wallent;
            $wallet_dat['updated_on'] = date('Y-m-d H:i:s');
            $WHERE = array('token' => $token);
            $result = $this->api->update_wallet($wallet_dat, $WHERE);

            /* payment on stripe */

            return true;
        }
    }
    
    
    public function braintreeKey_get() {
        
        $data = array();
        
        $braintree_key=settingValue('braintree_key');
        
        if (!empty($braintree_key)) {

            $response_code = '200';
            $response_message = $this->language_content['lg_success'];
            $data['braintree_key'] = $braintree_key;

            $result = $this->data_format($response_code, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        } else {
            $response_code = '404';
            $status = FALSE;
            $response_message = $this->language_content['lg_no_language_were_found'];
            $data = [];

            $result = $this->data_format($response_code, $status, $response_message, $data);
            $this->response($result, REST_Controller::HTTP_OK);
        }
    }
	
	
	
	public function razorpay_details()
    {
        removeTag($this->input->post());
        $params        = $this->input->post();
        $user_id       = $this->session->userdata('id');
		$razor_option = settingValue('razor_option');
		if($razorpay_option == 1){			
			$apikey = settingValue('razorpay_apikey');
			$apisecret = settingValue('razorpay_secret_key');
		}else if($razorpay_option == 2){
			$apikey = settingValue('live_razorpay_apikey');
			$apisecret = settingValue('live_razorpay_secret_key');
		}
        $user_currency = 'INR';
        if (!empty($params)) { 
			$url = "https://api.razorpay.com/v1/contacts";
			$unique = strtoupper(uniqid());
			$data   = ' {
			  "name":"'.$params['name'].'",
			  "email":"'.$params['email'].'",
			  "contact":"'.$params['contact'].'",
			  "type":"employee",
			  "reference_id":"'.$unique.'",
			  "notes":{}
			}';
			$ch     = curl_init();
			curl_setopt($ch, CURLOPT_URL, $url);
			curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
			curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
			curl_setopt($ch, CURLOPT_POST, 1);
			curl_setopt($ch, CURLOPT_USERPWD, $apikey . ":" . $apisecret);
			$headers = array(
				'Content-Type:application/json'
			);
			curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
			$result = curl_exec($ch);
			
			if (curl_errno($ch)) {
				$result = 'Error:' . curl_error($ch);
				echo json_encode(array(
                    'status' => false,
                    'msg' => $result
                ));
			}
			$results = json_decode($result);
			$user_id       = $this->session->userdata('id');
			$cnotes = $results->notes;
			$serializedcnotes = serialize($cnotes);
			$contact_data = array(
				'user_id' => $user_id,
				'rp_contactid' => $results->id,
				'entity' => $results->entity,
				'name' => $results->name,
				'contact' => $results->contact,
				'email' => $results->email,
				'type' => $results->type,
				'reference_id' => $results->reference_id,
				'batch_id' => $results->batch_id,
				'active' => $results->active,
				'accountnumber' => $params['accountnumber'],
				'mode' => $params['mode'],
				'purpose' => $params['purpose'],
				'notes' => $serializedcnotes,
				'created_at' => $results->created_at
			);
			$createcontact = $this->db->insert('razorpay_contact', $contact_data);
			if(!empty($createcontact)){
				$faurl = "https://api.razorpay.com/v1/fund_accounts";
				$faunique = strtoupper(uniqid());
				$fadata   = ' {
				  "contact_id": "'.$results->id.'",
				  "account_type": "bank_account",
				  "bank_account": {
					"name": "'.$params['bank_name'].'",
					"ifsc": "'.$params['ifsc'].'",
					"account_number":"'.$params['accountnumber'].'"
				  }
				}';
								
				$fach     = curl_init();
				curl_setopt($fach, CURLOPT_URL, $faurl);
				curl_setopt($fach, CURLOPT_RETURNTRANSFER, 1);
				curl_setopt($fach, CURLOPT_POSTFIELDS, $fadata);
				curl_setopt($fach, CURLOPT_POST, 1);
				curl_setopt($fach, CURLOPT_USERPWD, $apikey . ":" . $apisecret);
				$faheaders = array(
					'Content-Type:application/json'
				);
				curl_setopt($fach, CURLOPT_HTTPHEADER, $faheaders);
				$faresult = curl_exec($fach);
				
				if (curl_errno($fach)) {
					$faresult = 'Error:' . curl_error($fach);
					echo json_encode(array(
						'status' => false,
						'msg' => $faresult
					));
				}
				$faresults = json_decode($faresult);
				
				$fa_data = array(
					'fund_account_id' => $faresults->id,
					'entity' => $faresults->entity,
					'contact_id' => $faresults->contact_id,
					'account_type' => $faresults->account_type,
					'ifsc' => $faresults->bank_account->ifsc,
					'bank_name' => $faresults->bank_account->bank_name,
					'name' => $faresults->bank_account->name,
					'account_number' => $faresults->bank_account->account_number,
					'active' => $faresults->active,
					'batch_id' => $faresults->batch_id,
					'created_at' => $faresults->created_at
				);
				$facreatecontact = $this->db->insert('razorpay_fund_account', $fa_data);
				
				if($facreatecontact){
					$purl = "https://api.razorpay.com/v1/payouts";
					$punique = strtoupper(uniqid());
					$pdata   = ' {
					  "account_number": "2323230032510196",
					  "fund_account_id": "'.$faresults->id.'",
					  "amount": "'.$params['amount'].'",
					  "currency": "INR",
					  "mode": "'.$params['mode'].'",
					  "purpose": "'.$params['purpose'].'",
					  "queue_if_low_balance": true,
					  "reference_id": "'.$punique.'",
					  "narration": "",
					  "notes": {}
					}';
					
					$pch     = curl_init();
					curl_setopt($pch, CURLOPT_URL, $purl);
					curl_setopt($pch, CURLOPT_RETURNTRANSFER, 1);
					curl_setopt($pch, CURLOPT_POSTFIELDS, $pdata);
					curl_setopt($pch, CURLOPT_POST, 1);
					curl_setopt($pch, CURLOPT_USERPWD, $apikey . ":" . $apisecret);
					$pheaders = array(
						'Content-Type:application/json'
					);
					curl_setopt($pch, CURLOPT_HTTPHEADER, $pheaders);
					$presult = curl_exec($pch);
					
					if (curl_errno($pch)) {
						$presult = 'Error:' . curl_error($pch);						
						echo json_encode(array(
							'status' => false,
							'msg' => $presult
						));
					}
					$presults = json_decode($presult);
					
					$pydata = array(
						'payout_id' => $presults->id,
						'entity' => $presults->entity,
						'fund_account_id' => $presults->fund_account_id,
						'amount' => $presults->amount,
						'currency' => $presults->currency,
						'fees' => $presults->fees,
						'tax' => $presults->tax,
						'status' => $presults->status,
						'utr' => $presults->utr,
						'mode' => $presults->mode,
						'purpose' => $presults->purpose,						
						'reference_id' => $presults->reference_id,
						'narration' => $presults->narration,
						'batch_id' => $presults->batch_id,
						'failure_reason' => $presults->failure_reason,
						'created_at' => $presults->created_at
					);
					$payouts = $this->db->insert('razorpay_payouts', $pydata);
					if($payouts){
						$wdata = array(
							'user_id' => $user_id,
							'amount' => $presults->amount,
							'currency_code' => $presults->currency,
							'transaction_status' => 1,
							'transaction_date' => date('Y-m-d'),
							'request_payment' => 'RazorPay',
							'status' => 1,
							'created_by' => $user_id,
							'created_at' => $presults->created_at
						);
						$payoutins = $this->db->insert('wallet_withdraw', $wdata);
						if($payoutins){
							$amount        = $presults->amount;
							$user_id       = $this->session->userdata('id');
							$user          = $this->db->where('id', $user_id)->get('providers')->row_array();
							$user_name     = $user['name'];
							$user_token    = $user['token'];
							$currency_type = $user['currency_code'];
							$wallet = $this->db->where('user_provider_id', $user_id)->get('wallet_table')->row_array();
							$wallet_amt = $wallet['wallet_amt'];
							$history_pay['token']=$user_token;
							$history_pay['user_provider_id']=$user_id;
							$history_pay['currency_code']='INR';
							$history_pay['transaction_id']=$presults->id;
							$history_pay['paid_status']='1';
							$history_pay['total_amt']=$presults->amount;
							if($wallet_amt){
								$current_wallet = $wallet_amt-$amount;
							}else{
								$current_wallet = $amount;
							}
							$history_pay['current_wallet']=$current_wallet;
							$history_pay['reason']='Withdrawn Wallet Amt';
							$history_pay['created_at']=date('Y-m-d H:i:s');
							if($this->db->insert('wallet_transaction_history',$history_pay)){								
								$this->db->where('user_provider_id', $user_id)->update('wallet_table', array(
									'currency_code' => 'INR',
									'wallet_amt' => $current_wallet
								));								               
							}
							$message = "Amount Withdrawn Successfully";
							echo json_encode(array(
								'status' => true,
								'msg' => $message
							));
						}else{
							$message = "Payout details not Inserted";
							echo json_encode(array(
								'status' => false,
								'msg' => $message
							));
						}
					}else{
						$message = "Payout details not Inserted";
						echo json_encode(array(
							'status' => false,
							'msg' => $message
						));
					}
				} 
			}
        }else{
			$message = (!empty($this->user_language[$this->user_selected]['lg_something_went_wrong'])) ? $this->user_language[$this->user_selected]['lg_something_went_wrong'] : $this->default_language['en']['lg_something_went_wrong'];
            echo json_encode(array(
                    'status' => false,
                    'msg' => $message
                ));
		}
    }

    /* END */
}

?>