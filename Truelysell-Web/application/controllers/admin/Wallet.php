<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Wallet extends CI_Controller {

   public $data;

   public function __construct() {

        parent::__construct();
        $this->load->model('service_model','service');
        $this->load->model('wallet_model','wallet');
		  $this->load->model('common_model','common_model');
        $this->data['theme'] = 'admin';
        $this->data['model'] = 'wallet';
        $this->data['base_url'] = base_url();
        $this->session->keep_flashdata('error_message');
        $this->session->keep_flashdata('success_message');
        $this->load->helper('user_timezone_helper');

    }

	public function index()
	{
  $this->common_model->checkAdminUserPermission(10);
    if(!empty($this->input->post())){
      $token=$this->input->post('token');
      $from=$this->input->post('from');
      $to=$this->input->post('to');
      $this->data['page'] = 'wallet_report_view';
      $this->data['model'] = 'wallet';
      $this->data['list'] = $this->wallet->get_wallet_info_filter($token,$from,$to); 
      $this->data['filter'] = array('token_f'=>$token, 
                                    'service_from'=>$from,
                                    'service_to'=>$to 
                                  );
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
     
    }else{
      $this->data['page'] = 'wallet_report_view';
      $this->data['model'] = 'wallet';
      $this->data['list'] = $this->wallet->get_wallet_info();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
    }

		
	}

  public function wallet_history() {
	    $this->common_model->checkAdminUserPermission(10);
    if(!empty($this->input->post())){
         $token=$this->input->post('token');
      $from=$this->input->post('from');
      $to=$this->input->post('to');
      $this->data['page'] = 'wallet_trans_report_view';
      $this->data['model'] = 'wallet';
      $this->data['list'] = $this->wallet->get_wallet_history_filter($token,$from,$to); 
      $this->data['filter'] = array('token_f'=>$token, 
                                    'service_from'=>$from,
                                    'service_to'=>$to 
                                  );
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
    }else{
      $this->data['page'] = 'wallet_trans_report_view';
      $this->data['model'] = 'wallet';
      $this->data['list'] = $this->wallet->get_wallet_history();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
    }
  }
	
  public function request_list()
	{
			$lists = $this->service->request_list();
	        $data = array();
	        $no = $_POST['start'];
	        foreach ($lists as $template) {
	            $no++;
	            $row    = array();
	            $row[]  = $no;
              $profile_img = $template['profile_img'];
              if(empty($profile_img)){
                $profile_img = 'assets/img/user.jpg';
              }
	            $row[]  = '<a href="#" class="avatar"> <img alt="" src="'.$profile_img.'"></a><h2><a href="#">'.$template['username'].'</a></h2>';
	            $row[]  = $template['contact_number'];
              $row[]  = $template['title'];
              $row[]  = '<p class="price-sup"><sup>RM</sup>'.$template['proposed_fee'].'</p>';
              $row[]  = '<span class="service-date">'.date("d M Y", strtotime($template['request_date'])).'<span class="service-time">'.date("H.i A", strtotime($template['request_time'])).'</span></span>';
              $row[]  = date("d M Y", strtotime($template['created']));
              $val = '';
              $status = $template['status'];
              if($status == -1)
              {
                $val = '<span class="label label-danger-border">Expired</span>';
              }
              if($status == 0)
              {
                $val = '<span class="label label-warning-border">Pending</span>';
              }
              elseif($status == 1)
              {
                $val = '<span class="label label-info-border">Accepted</span>';
              }
              elseif($status == 2)
              {
                $val = '<span class="label label-success-border">Completed</span>';
              }
              elseif($status == 3)
              {
                $val = '<span class="label label-danger-border">Declined</span>';
              }
              elseif($status == 4)
              {
                $val = '<span class="label label-danger-border">Deleted</span>';
              }
	            $row[]  = $val;
	            $data[] = $row;
        }

        $output = array(
                        "draw" => $_POST['draw'],
                        "recordsTotal" => $this->service->request_list_all(),
                        "recordsFiltered" => $this->service->request_list_filtered(),
                        "data" => $data,
                );

        //output to json format
        echo json_encode($output);

	}

   public function delete_service()
  {
    $id=$this->input->post('service_id');

    $inputs['status']= '0';
    $WHERE =array('id' => $id);
    $result=$this->service->update_service($inputs,$WHERE);


    if($result)
   {
           $this->session->set_flashdata('success_message','Service deleted successfully');    
           redirect(base_url()."service-list");   
    }
    else
    {
        $this->session->set_flashdata('error_message','Something wrong, Please try again');
        redirect(base_url()."service-list");   

     } 
  }



  
}
