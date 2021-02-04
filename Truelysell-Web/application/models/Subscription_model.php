<?php
	defined('BASEPATH') OR exit('No direct script access allowed');

	class Subscription_model extends CI_Model {

		public function __construct() {

	        parent::__construct();
	        $this->load->database();
	        date_default_timezone_set('Asia/Kolkata');
	    }

		public function get_subscription_list()
    {
      return $this->db->get_where('subscription_fee',array('status'=>1))->result_array();
    }
    public function get_subscription($id)
		{
			return $this->db->get_where('subscription_fee',array('status'=>1,'id'=>$id))->row_array();
		}

		public function get_my_subscription()
		{
			$user_id = $this->session->userdata('id');
			return $this->db->get_where('subscription_details',array('subscriber_id'=>$user_id))->row_array();
		}

		public function subscription_success($inputs)
     {

       $new_details = array();
			 $stripe = array();

       $user_id = $inputs['user_id'];

       $subscription_id = $inputs['subscription_id'];


       $this->db->select('duration');
       $record = $this->db->get_where('subscription_fee',array('id'=>$subscription_id))->row_array();
       
       if(!empty($record)){


       $duration = $record['duration'];
       $days = 30;
       switch ($duration) {
         case 1:
           $days = 30;
           break;
         case 2:
           $days = 60;
           break;
         case 3:
           $days = 90;
           break;
         case 6:
           $days = 180;
           break;
         case 12:
           $days = 365;
           break;
         case 24:
           $days = 730;
           break;

         default:
           $days = 30;
           break;
       }

        $subscription_date = date('Y-m-d H:i:s');
        $expiry_date_time =  date('Y-m-d H:i:s',strtotime(date("Y-m-d  H:i:s", strtotime($subscription_date)) ." +".$days."days"));


       $new_details['subscriber_id'] = $stripe['subscriber_id'] = $user_id;
       $new_details['subscription_id'] = $stripe['subscription_id'] = $subscription_id;
       $new_details['subscription_date'] = $stripe['subscription_date'] = $subscription_date;
       $new_details['expiry_date_time'] = $expiry_date_time;
       $new_details['type']=1;  
			 $this->db->where('subscriber_id', $user_id);
       $count = $this->db->count_all_results('subscription_details');
       if($count == 0){
       $this->db->insert('subscription_details', $new_details);
       $this->db->insert('subscription_details_history', $new_details);
			 $stripe['sub_id'] = $this->db->insert_id();

       }else{

         $this->db->where('subscriber_id', $user_id);
        $this->db->update('subscription_details', $new_details);
          $this->db->insert('subscription_details_history', $new_details);
				 $this->db->where('subscriber_id', $user_id);
       $details_sub = $this->db->get('subscription_details')->row_array();
 			 $stripe['sub_id'] = $details_sub['id'];
       }
			 $stripe['tokenid'] = $inputs['token'];
			 $stripe['payment_details'] = $inputs['args'];
				return $this->db->insert('subscription_payment', $stripe);

     }else{

      return false;
     }

     }
	 
	public function razorpay_subscription_success($inputs)
     {

       $new_details = array();
			 $razorpay = array();

       $user_id = $inputs['user_id'];

       $subscription_id = $inputs['subscription_id'];


       $this->db->select('duration');
       $record = $this->db->get_where('subscription_fee',array('id'=>$subscription_id))->row_array();
       
       if(!empty($record)){


       $duration = $record['duration'];
       $days = 30;
       switch ($duration) {
         case 1:
           $days = 30;
           break;
         case 2:
           $days = 60;
           break;
         case 3:
           $days = 90;
           break;
         case 6:
           $days = 180;
           break;
         case 12:
           $days = 365;
           break;
         case 24:
           $days = 730;
           break;

         default:
           $days = 30;
           break;
       }

        $subscription_date = date('Y-m-d H:i:s');
        $expiry_date_time =  date('Y-m-d H:i:s',strtotime(date("Y-m-d  H:i:s", strtotime($subscription_date)) ." +".$days."days"));


       $new_details['subscriber_id'] = $razorpay['subscriber_id'] = $user_id;
       $new_details['subscription_id'] = $razorpay['subscription_id'] = $subscription_id;
       $new_details['subscription_date'] = $razorpay['subscription_date'] = $subscription_date;
       $new_details['expiry_date_time'] = $expiry_date_time;
       $new_details['type']=1;  
			 $this->db->where('subscriber_id', $user_id);
       $count = $this->db->count_all_results('subscription_details');
       if($count == 0){
       $this->db->insert('subscription_details', $new_details);
       $this->db->insert('subscription_details_history', $new_details);
			 $razorpay['sub_id'] = $this->db->insert_id();

       }else{

         $this->db->where('subscriber_id', $user_id);
        $this->db->update('subscription_details', $new_details);
          $this->db->insert('subscription_details_history', $new_details);
				 $this->db->where('subscriber_id', $user_id);
       $details_sub = $this->db->get('subscription_details')->row_array();
 			 $razorpay['sub_id'] = $details_sub['id'];
       }
			 $razorpay['tokenid'] = $inputs['token'];
			 $razorpay['payment_details'] = $inputs['payment_details'];
				return $this->db->insert('subscription_payment', $razorpay);

     }else{

      return false;
     }

     }
	 
	 
	 
	public function paypal_subscription_success($inputs)
     {

       $new_details = array();
			 $razorpay = array();

       $user_id = $inputs['user_id'];

       $subscription_id = $inputs['subscription_id'];


       $this->db->select('duration');
       $record = $this->db->get_where('subscription_fee',array('id'=>$subscription_id))->row_array();
       
       if(!empty($record)){


       $duration = $record['duration'];
       $days = 30;
       switch ($duration) {
         case 1:
           $days = 30;
           break;
         case 2:
           $days = 60;
           break;
         case 3:
           $days = 90;
           break;
         case 6:
           $days = 180;
           break;
         case 12:
           $days = 365;
           break;
         case 24:
           $days = 730;
           break;

         default:
           $days = 30;
           break;
       }

        $subscription_date = date('Y-m-d H:i:s');
        $expiry_date_time =  date('Y-m-d H:i:s',strtotime(date("Y-m-d  H:i:s", strtotime($subscription_date)) ." +".$days."days"));


       $new_details['subscriber_id'] = $paypal['subscriber_id'] = $user_id;
       $new_details['subscription_id'] = $paypal['subscription_id'] = $subscription_id;
       $new_details['subscription_date'] = $paypal['subscription_date'] = $subscription_date;
       $new_details['expiry_date_time'] = $expiry_date_time;
       $new_details['type']=1;  
			 $this->db->where('subscriber_id', $user_id);
       $count = $this->db->count_all_results('subscription_details');
       if($count == 0){
       $this->db->insert('subscription_details', $new_details);
       $this->db->insert('subscription_details_history', $new_details);
			 $paypal['sub_id'] = $this->db->insert_id();

       }else{

         $this->db->where('subscriber_id', $user_id);
        $this->db->update('subscription_details', $new_details);
          $this->db->insert('subscription_details_history', $new_details);
				 $this->db->where('subscriber_id', $user_id);
       $details_sub = $this->db->get('subscription_details')->row_array();
 			 $paypal['sub_id'] = $details_sub['id'];
       }
			 $paypal['tokenid'] = $inputs['token'];
			 $paypal['payment_details'] = $inputs['payment_details'];
				return $this->db->insert('subscription_payment', $paypal);

     }else{

      return false;
     }

     }
	 
	 
	 
	 
	 

}
