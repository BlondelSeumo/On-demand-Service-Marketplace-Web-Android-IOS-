<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class payments extends CI_Controller {

   public $data;

   public function __construct() {

        parent::__construct();
        $this->load->model('payment_model','payment');
		$this->load->model('common_model','common_model');
        $this->data['theme'] = 'admin';
        $this->data['model'] = 'payments';
        $this->data['base_url'] = base_url();
        $this->session->keep_flashdata('error_message');
        $this->session->keep_flashdata('success_message');
        $this->load->helper('user_timezone_helper');

    }


	public function payment_list()
  {
	  $this->common_model->checkAdminUserPermission(6);
   extract($_POST);
      $this->data['page'] = 'payment_list';
       if ($this->input->post('form_submit')) 
      {  
         $provider_id = $this->input->post('provider_id');
         $status = $this->input->post('status');
         $from = $this->input->post('from');
         $to = $this->input->post('to');
 $this->data['list'] = $this->payment->payment_filter($provider_id,$status,$from,$to);
      }
      else
      {
      $this->data['list'] = $this->payment->payment_list();
      }
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
    
  }

    public function admin_payment()
  {
   
      $this->data['page'] = 'admin_payment';
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
    
  }
   public function add_payment()
  {
            $payment_details = $this->input->post();
          
            $result = $this->payment->add_payment($payment_details);                
            if($result)
            {
                 $this->session->set_flashdata('success_message','Updated successfully');    
                 
            }
            else
            {
                $this->session->set_flashdata('error_message','Something wrong, Please try again');
                 

             } 
               
           echo json_encode($result);
    
  }
	

}
