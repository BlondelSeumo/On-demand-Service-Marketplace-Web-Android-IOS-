<?php
	defined('BASEPATH') OR exit('No direct script access allowed');

	class Booking_model extends CI_Model {

		public function __construct() {

	        parent::__construct();
	        $this->load->database();
	        date_default_timezone_set('UTC');
	    }

		public function get_subscription_list()
    {
      return $this->db->get_where('subscription_fee',array('status'=>1))->result_array();
    }
    public function get_service($id)
		{
			return $this->db->get_where('services',array('status'=>1,'id'=>$id))->row_array();
		}

		public function get_my_subscription()
		{
			$user_id = $this->session->userdata('id');
			return $this->db->get_where('subscription_details',array('subscriber_id'=>$user_id))->row_array();
		}

		public function booking_success($inputs)
     {

       
			 $stripe = array();

       
                 $stripe['tokenid'] = $inputs['token'];
                 $stripe['payment_details'] = $inputs['args'];
	         $stripe['service_id'] = $inputs['service_id'];
	         $stripe['provider_id'] = $inputs['provider_id'];
	         $stripe['user_id'] = $inputs['user_id'];
	         $stripe['amount'] = $inputs['amount'];
	         $stripe['currency_code'] = $inputs['currency_code'];
	         $stripe['service_date'] = $inputs['service_date'];
	         $stripe['location'] = $inputs['location'];
	         $stripe['latitude'] = $inputs['latitude'];
	         $stripe['longitude'] = $inputs['longitude'];
	         $stripe['notes'] = $inputs['notes'];
	         $stripe['from_time'] = $inputs['from_time'];
	         $stripe['to_time'] = $inputs['to_time'];
	         $stripe['request_date'] = (date('Y-m-d H:i:s'));
        	 $stripe['request_time'] =  (date('H:i:s'));
        	 $stripe['updated_on']  = (date('Y-m-d H:i:s'));
                
			  $this->db->insert('book_service', $stripe);
			  return $this->db->insert_id();

     

     }

}
