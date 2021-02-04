<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Subscription extends CI_Controller {

   public $data;

   public function __construct() {

        parent::__construct();
        error_reporting(0);
                  if(empty($this->session->userdata('id'))){
          redirect(base_url());
          }
        $this->data['theme'] = 'user';
        $this->data['model'] = 'home';
        $this->data['base_url'] = base_url();
		
		 $this->load->library('paypal_lib');
		 

			  $this->load->helper('custom_language');
        $this->load->model('subscription_model','subscription');
        $this->load->helper('user_timezone_helper');$user_id = $this->session->userdata('id');
        $this->data['user_id'] = $user_id;
				$this->load->helper('subscription_helper');
        $this->data['subscription_details'] = get_subscription_details(md5($user_id));
       


         $this->data['secret_key'] = '';

         $this->data['publishable_key'] = '';

         $this->data['website_logo_front'] ='assets/img/logo.png';

         $publishable_key='';
         $secret_key='';
         $live_publishable_key='';
         $live_secret_key='';
         $stripe_option='';


          $query = $this->db->query("select * from system_settings WHERE status = 1");
          $result = $query->result_array();
          if(!empty($result))
          {
              foreach($result as $data){

                  if($data['key'] == 'website_name'){
                  $this->website_name = $data['value'];
                  }


                  if($data['key'] == 'secret_key'){

                    $secret_key = $data['value'];

                  }

                  if($data['key'] == 'publishable_key'){

                    $publishable_key = $data['value'];

                  }

                  if($data['key'] == 'live_secret_key'){

                    $live_secret_key = $data['value'];

                  }

                  if($data['key'] == 'live_publishable_key'){

                    $live_publishable_key = $data['value'];

                  }

                  if($data['key'] == 'stripe_option'){

                    $stripe_option = $data['value'];

                   } 
                  
                  if($data['key'] == 'logo_front'){
                      $this->data['website_logo_front'] =  $data['value'];
                  }

              }
          }


          if(@$stripe_option == 1){

          $this->data['publishable_key'] = $publishable_key;

          $this->data['secret_key']      = $secret_key;

        }

        if(@$stripe_option == 2){

          $this->data['publishable_key'] = $live_publishable_key;

          $this->data['secret_key']      = $live_secret_key;

        }


          $config['publishable_key'] =  $this->data['publishable_key'];

          $config['secret_key'] = $this->data['secret_key'];

          $this->load->library('stripe',$config);
          

           if(!$this->session->userdata('id'))
          {
            redirect(base_url());
          }

         $this->load->model('Api_model','api');

    }

	public function index()
	{
		redirect(base_url('provider-settings'));
	}
	public function subscription_list()
	{
  	$this->data['page'] = 'index';
  	$this->data['model'] = 'subscription';
    $this->data['publishable_key'] = $this->data['publishable_key'];
  	$this->data['list'] = $this->subscription->get_subscription_list();
  	$this->data['my_subscribe'] = $this->subscription->get_my_subscription();
  	$this->load->vars($this->data);
  	$this->load->view($this->data['theme'].'/template');
	}
	
	
	
	
	public function razorpay_payment(){
    $inputs = array();
    $sub_id = $this->input->post('sub_id'); // Package ID
    //$records = $this->subscription->get_subscription($sub_id);
    $inputs['subscription_id'] = $sub_id;
    $inputs['user_id'] = $this->session->userdata('id');
	$inputs['token']=$this->session->userdata('chat_token');
	$inputs['payment_details']='Razorpay';
	//echo json_encode($inputs);exit;
	//echo "<pre>";print_r($inputs);exit;
    // $inputs['token'] = $this->input->post('tokenid');

     // $charges_array = array();
     // $amount = (!empty($records['fee']))?$records['fee']:2;
     // $amount = ($amount *100);
     // $charges_array['amount']       = $amount;
     // $charges_array['currency']     = settings('currency');
     // $charges_array['description']  = (!empty($records['subscription_name']))?$records['subscription_name']:'Subscription';
     // $charges_array['source']       = 'tok_visa';


     // $result = $this->stripe->stripe_charges($charges_array);

     
     // $result = json_decode($result,true);
     
        // $inputs['token'] = $result['id'];
        // $inputs['args'] = json_encode($result);
    $result = $this->subscription->razorpay_subscription_success($inputs);
    if($result){
        $data=array('tab_ctrl'=>2,'success_message'=>'You have been subscribed successfully');


                   $this->session->set_flashdata($data);
                    $token=$this->session->userdata('chat_token');
       $this->send_push_notification($token,$this->session->userdata('id'),1,' have been subscribed'); 
          }else{
             $data=array('tab_ctrl'=>2,'success_message'=>'Sorry, something went wrong');

                   $this->session->set_flashdata($data);
    }
      
    echo json_encode($result);
  }
  
  
  	public function paypal_payment($sub_id){
		
		// $input = $this->input->post();
		// $paypalInfo = $this->input->get();
		// 
		// echo "<pre>";print_r($paypalInfo);exit;
		
		$inputs = array();
    //$sub_id = $this->input->get('sub_id'); // Package ID
    //$records = $this->subscription->get_subscription($sub_id);
    $inputs['subscription_id'] = $sub_id;
    $inputs['user_id'] = $this->session->userdata('id');
	$inputs['token']=$this->session->userdata('chat_token');
	$inputs['payment_details']='Paypal';
	//echo "<pre>";print_r($inputs);exit;
	 $result = $this->subscription->paypal_subscription_success($inputs);
    if($result){
        $data=array('tab_ctrl'=>2,'success_message'=>'You have been subscribed successfully');


                   $this->session->set_flashdata($data);
                    $token=$this->session->userdata('chat_token');
       $this->send_push_notification($token,$this->session->userdata('id'),1,' have been subscribed'); 
	   redirect(base_url('provider-subscription'));
          }else{
             $data=array('tab_ctrl'=>2,'success_message'=>'Sorry, something went wrong');

                   $this->session->set_flashdata($data);
				   redirect(base_url('provider-subscription'));
    }
      
    echo json_encode($result);
	
  }

  public function stripe_payment(){
    $inputs = array();
    $sub_id = $this->input->post('sub_id'); // Package ID
    $records = $this->subscription->get_subscription($sub_id);
    $inputs['subscription_id'] = $sub_id;
    $inputs['user_id'] = $this->session->userdata('id');
    $inputs['token'] = $this->input->post('tokenid');

     $charges_array = array();
     $amount = (!empty($records['fee']))?$records['fee']:2;
     $amount = ($amount *100);
     $charges_array['amount']       = $amount;
     $charges_array['currency']     = settings('currency');
     $charges_array['description']  = (!empty($records['subscription_name']))?$records['subscription_name']:'Subscription';
     $charges_array['source']       = 'tok_visa';


     $result = $this->stripe->stripe_charges($charges_array);

     
     $result = json_decode($result,true);
      if(empty($result['error'])){
        $inputs['token'] = $result['id'];
        $inputs['args'] = json_encode($result);
    $result = $this->subscription->subscription_success($inputs);
    if($result){
        $data=array('tab_ctrl'=>2,'success_message'=>'You have been subscribed successfully');


                   $this->session->set_flashdata($data);
                    $token=$this->session->userdata('chat_token');
       $this->send_push_notification($token,$this->session->userdata('id'),1,' have been subscribed'); 
          }else{
             $data=array('tab_ctrl'=>2,'success_message'=>'Sorry, something went wrong');

                   $this->session->set_flashdata($data);
    }
      }else{
        $inputs['token'] = 'Issue - token_already_used';
         $data=array('tab_ctrl'=>2,'success_message'=>'Sorry, something went wrong');

                   $this->session->set_flashdata($data);
      }

    echo json_encode($result);
  }

  public function stripe_payments(){
    $inputs = array();
    $sub_id = $this->input->post('sub_id'); // Package ID
    $records = $this->subscription->get_subscription($sub_id);
    $inputs['subscription_id'] = $sub_id;
    $inputs['user_id'] = $this->session->userdata('id');
   
      
        $inputs['token'] = 'Free subscription';
        $inputs['args'] = '';
    $result = $this->subscription->subscription_success($inputs);
    if($result){
      $message= 'You have been subscribed';
      $this->session->set_flashdata('success_message',$message);
       $token=$this->session->userdata('chat_token');
       $this->send_push_notification($token,$this->session->userdata('id'),1,' have been subscribed'); 
          }else{
      $message= 'Sorry, something went wrong';
      $this->session->set_flashdata('error_message',$message);
    }
     
    echo json_encode($result);
  }

  public function getToken($length,$user_id)
   {
       $token = $user_id;

       $codeAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
       $codeAlphabet.= "abcdefghijklmnopqrstuvwxyz";
       $codeAlphabet.= "0123456789";

       $max = strlen($codeAlphabet); // edited

    for ($i=0; $i < $length; $i++) {
         $token .= $codeAlphabet[$this->crypto_rand_secure(0, $max-1)];

    }

    return $token;
   }

   function crypto_rand_secure($min, $max) {
        $range = $max - $min;
        if ($range < 0) return $min; // not so random...
        $log = log($range, 2);
        $bytes = (int) ($log / 8) + 1; // length in bytes
        $bits = (int) $log + 1; // length in bits
        $filter = (int) (1 << $bits) - 1; // set all lower bits to 1
        do {
            $rnd = hexdec(bin2hex(openssl_random_pseudo_bytes($bytes)));
            $rnd = $rnd & $filter; // discard irrelevant bits
        } while ($rnd >= $range);
        return $min + $rnd;
  }

  /*push notification*/

  public function send_push_notification($token,$provider_id,$type,$msg=''){

            $data=array();
            $data['provider_id']=$this->session->userdata('id');      
              if(!empty($data)){
            if($type==1){
               $device_tokens=$this->api->get_device_info_multiple($data['provider_id'],1); 
             }
            /*insert notification*/
            $msg=ucfirst($this->session->userdata('name')).' '.strtolower($msg);

            $receiver='';
            $admin=$this->db->where('user_id',1)->from('administrators')->get()->row_array();
            if(empty($admin['token'])){
             
              $receiver=$this->getToken(14,$admin['user_id']);
              $receiver_token_update=$this->db->where('role',1)->update('administrators',['token'=>$receiver]);
            }else{
              $receiver=$admin['token'];
            }

             if(!empty($receiver)){
                  $this->api->insert_notification($token,$receiver,$msg);
             }
             

            $title='Subscription';
           

            if (!empty($device_tokens)) {
              foreach ($device_tokens as $key => $device) {
                          if(!empty($device['device_type']) && !empty($device['device_id'])){
                          
                          if(strtolower($device['device_type'])=='android'){
                          
                          $notify_structure=array(
                          'title' => $title,
                          'message' => $msg,
                          'image' => 'test22',
                          'action' => 'test222',
                          'action_destination' => 'test222',
                          );
                          
                          sendFCMMessage($notify_structure,$device['device_id']);  
                          
                          }
                          
                          if(strtolower($device['device_type']=='ios')){
                          $notify_structure= array(
                          'alert' => $msg,
                          'sound' => 'default',
                          'badge' => 0,
                          );
                          
                          
                          sendApnsMessage($notify_structure,$device['device_id']);  
                          
                          }
                          }
              }
             
            }


/*apns push notification*/
}else{
     $this->token_error();
}
}
}
?>
