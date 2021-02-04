<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Booking extends CI_Controller {

   public $data;

   public function __construct() {

        parent::__construct();
		
        $this->load->model('service_model','service');
        $this->load->model('Api_model','api');
        $this->load->model('wallet_model','wallet');
        $this->load->model('Booking_report_model','book');
		$this->load->model('common_model','common_model');
		$this->load->model('templates_model');
		$this->site_name ='Truelysell';

        $this->data['theme'] = 'admin';
        $this->data['model'] = 'bookings';
        $this->data['base_url'] = base_url();
        $this->session->keep_flashdata('error_message');
        $this->session->keep_flashdata('success_message');
        $this->load->helper('user_timezone_helper');
        $this->data['user_role']=$this->session->userdata('role');

    }

	public function index()
	{

    if(!empty($this->input->post())){
      
    }else{
      $this->data['page'] = 'wallet_report_view';
      $this->data['model'] = 'wallet';
      $this->data['list'] = $this->wallet->get_wallet_info();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
    }

		
	}

  public function total_bookings() {
	$this->common_model->checkAdminUserPermission(5);
    if(!empty($this->input->post())){
      extract($_POST);
         
          $service_id =$service_title;
          $status     =$service_status;
          $user_id    =$user_id;
          $provider_id=$provider_id;
          $from       =$from;
          $to         =$to;
      $this->data['page'] = 'total_booking_view';
      $this->data['model'] = 'bookings';
      $this->data['list'] = $this->book->get_filter_total_bookings($service_id,$status,$user_id,$provider_id,$from,$to);

      $this->data['filter']=array(
                                  'service_t'=>$service_title,
                                  'service_s'=>$service_status,
                                  'user_i'=>$user_id,
                                  'provider_i'=>$provider_id,
                                  'service_from'=>$from,
                                  'service_to'=>$to,
                                );
      $this->data['all_booking']=$this->db->from('book_service')->count_all_results();
      $this->data['pending']=$this->db->from('book_service')->where('status',1)->count_all_results();
      $this->data['inprogress']=$this->db->from('book_service')->where('status',2)->count_all_results();
      $this->data['completed']=$this->db->from('book_service')->where('status',6)->count_all_results();
      $this->data['rejected']=$this->db->from('book_service')->where('status',5)->count_all_results();
      $this->data['cancelled']=$this->db->from('book_service')->where('status',7)->count_all_results();

      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');

    }else{
      $this->data['page'] = 'total_booking_view';
      $this->data['model'] = 'bookings';
      $this->data['list'] = $this->book->get_total_bookings();
      $this->data['all_booking']=$this->db->from('book_service')->count_all_results();
      $this->data['pending']=$this->db->from('book_service')->where('status',1)->count_all_results();
      $this->data['inprogress']=$this->db->from('book_service')->where('status',2)->count_all_results();
      $this->data['completed']=$this->db->from('book_service')->where('status',6)->count_all_results();
      $this->data['rejected']=$this->db->from('book_service')->where('status',5)->count_all_results();
      $this->data['cancelled']=$this->db->from('book_service')->where('status',7)->count_all_results();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
    }
  }

  /*pending report*/
    public function pending_bookings() {
		$this->common_model->checkAdminUserPermission(5);
    if(!empty($this->input->post())){
      extract($_POST);
         
          $service_id =$service_title;
          $status     =$service_status;
          $user_id    =$user_id;
          $provider_id=$provider_id;
          $from       =$from;
          $to         =$to;
      $this->data['page'] = 'pending_booking_view';
      $this->data['model'] = 'bookings';
      $this->data['list'] = $this->book->get_filter_pending_bookings($service_id,$status,$user_id,$provider_id,$from,$to);

      $this->data['filter']=array(
                                  'service_t'=>$service_title,
                                  'service_s'=>$service_status,
                                  'user_i'=>$user_id,
                                  'provider_i'=>$provider_id,
                                  'service_from'=>$from,
                                  'service_to'=>$to,
                                );
       $this->data['all_booking']=$this->db->from('book_service')->count_all_results();
      $this->data['pending']=$this->db->from('book_service')->where('status',1)->count_all_results();
      $this->data['inprogress']=$this->db->from('book_service')->where('status',2)->count_all_results();
      $this->data['completed']=$this->db->from('book_service')->where('status',6)->count_all_results();
      $this->data['rejected']=$this->db->from('book_service')->where('status',5)->count_all_results();
      $this->data['cancelled']=$this->db->from('book_service')->where('status',7)->count_all_results();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');

    }else{
      $this->data['page'] = 'pending_booking_view';
      $this->data['model'] = 'bookings';
      $this->data['list'] = $this->book->get_pending_bookings();
       $this->data['all_booking']=$this->db->from('book_service')->count_all_results();
      $this->data['pending']=$this->db->from('book_service')->where('status',1)->count_all_results();
      $this->data['inprogress']=$this->db->from('book_service')->where('status',2)->count_all_results();
      $this->data['completed']=$this->db->from('book_service')->where('status',6)->count_all_results();
      $this->data['rejected']=$this->db->from('book_service')->where('status',5)->count_all_results();
      $this->data['cancelled']=$this->db->from('book_service')->where('status',7)->count_all_results();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
    }
  }
	
/*Inprogress*/

  public function inprogress_bookings() {
	  $this->common_model->checkAdminUserPermission(5);
    if(!empty($this->input->post())){
      extract($_POST);
         
          $service_id =$service_title;
          $status     =$service_status;
          $user_id    =$user_id;
          $provider_id=$provider_id;
          $from       =$from;
          $to         =$to;
      $this->data['page'] = 'inprogress_booking_view';
      $this->data['model'] = 'bookings';
      $this->data['list'] = $this->book->get_filter_inprogress_bookings($service_id,$status,$user_id,$provider_id,$from,$to);

      $this->data['filter']=array(
                                  'service_t'=>$service_title,
                                  'service_s'=>$service_status,
                                  'user_i'=>$user_id,
                                  'provider_i'=>$provider_id,
                                  'service_from'=>$from,
                                  'service_to'=>$to,
                                );
       $this->data['all_booking']=$this->db->from('book_service')->count_all_results();
      $this->data['pending']=$this->db->from('book_service')->where('status',1)->count_all_results();
      $this->data['inprogress']=$this->db->from('book_service')->where('status',2)->count_all_results();
      $this->data['completed']=$this->db->from('book_service')->where('status',6)->count_all_results();
      $this->data['rejected']=$this->db->from('book_service')->where('status',5)->count_all_results();
      $this->data['cancelled']=$this->db->from('book_service')->where('status',7)->count_all_results();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');

    }else{
      $this->data['page'] = 'inprogress_booking_view';
      $this->data['model'] = 'bookings';
       $this->data['all_booking']=$this->db->from('book_service')->count_all_results();
      $this->data['pending']=$this->db->from('book_service')->where('status',1)->count_all_results();
      $this->data['inprogress']=$this->db->from('book_service')->where('status',2)->count_all_results();
      $this->data['completed']=$this->db->from('book_service')->where('status',6)->count_all_results();
      $this->data['rejected']=$this->db->from('book_service')->where('status',5)->count_all_results();
      $this->data['cancelled']=$this->db->from('book_service')->where('status',7)->count_all_results();
      $this->data['list'] = $this->book->get_inprogress_bookings();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
    }
  }

  /*Completed*/

    public function completed_bookings() {
		$this->common_model->checkAdminUserPermission(5);
    if(!empty($this->input->post())){
      extract($_POST);
         
          $service_id =$service_title;
          $status     =$service_status;
          $user_id    =$user_id;
          $provider_id=$provider_id;
          $from       =$from;
          $to         =$to;
      $this->data['page'] = 'complete_booking_view';
      $this->data['model'] = 'bookings';
      $this->data['list'] = $this->book->get_filter_complete_bookings($service_id,$status,$user_id,$provider_id,$from,$to);

      $this->data['filter']=array(
                                  'service_t'=>$service_title,
                                  'service_s'=>$service_status,
                                  'user_i'=>$user_id,
                                  'provider_i'=>$provider_id,
                                  'service_from'=>$from,
                                  'service_to'=>$to,
                                );
       $this->data['all_booking']=$this->db->from('book_service')->count_all_results();
      $this->data['pending']=$this->db->from('book_service')->where('status',1)->count_all_results();
      $this->data['inprogress']=$this->db->from('book_service')->where('status',2)->count_all_results();
      $this->data['completed']=$this->db->from('book_service')->where('status',6)->count_all_results();
      $this->data['rejected']=$this->db->from('book_service')->where('status',5)->count_all_results();
      $this->data['cancelled']=$this->db->from('book_service')->where('status',7)->count_all_results();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');

    }else{
      $this->data['page'] = 'complete_booking_view';
      $this->data['model'] = 'bookings';
       $this->data['all_booking']=$this->db->from('book_service')->count_all_results();
      $this->data['pending']=$this->db->from('book_service')->where('status',1)->count_all_results();
      $this->data['inprogress']=$this->db->from('book_service')->where('status',2)->count_all_results();
      $this->data['completed']=$this->db->from('book_service')->where('status',6)->count_all_results();
      $this->data['rejected']=$this->db->from('book_service')->where('status',5)->count_all_results();
      $this->data['cancelled']=$this->db->from('book_service')->where('status',7)->count_all_results();
      $this->data['list'] = $this->book->get_complete_bookings();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
    }
  }

  /*Rejected*/

    public function rejected_bookings() { 
	$this->common_model->checkAdminUserPermission(5);
    if(!empty($this->input->post())){
      extract($_POST);
         
          $service_id =$service_title;
          $status     =$service_status;
          $user_id    =$user_id;
          $provider_id=$provider_id;
          $from       =$from;
          $to         =$to;
      $this->data['page'] = 'reject_booking_view';
      $this->data['model'] = 'bookings';
      $this->data['list'] = $this->book->get_filter_reject_bookings($service_id,$status,$user_id,$provider_id,$from,$to);

      $this->data['filter']=array(
                                  'service_t'=>$service_title,
                                  'service_s'=>$service_status,
                                  'user_i'=>$user_id,
                                  'provider_i'=>$provider_id,
                                  'service_from'=>$from,
                                  'service_to'=>$to,
                                );
       $this->data['all_booking']=$this->db->from('book_service')->count_all_results();
      $this->data['pending']=$this->db->from('book_service')->where('status',1)->count_all_results();
      $this->data['inprogress']=$this->db->from('book_service')->where('status',2)->count_all_results();
      $this->data['completed']=$this->db->from('book_service')->where('status',6)->count_all_results();
      $this->data['rejected']=$this->db->from('book_service')->where('status',5)->count_all_results();
      $this->data['cancelled']=$this->db->from('book_service')->where('status',7)->count_all_results();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');

    }else{
      $this->data['page'] = 'reject_booking_view';
      $this->data['model'] = 'bookings';
 $this->data['all_booking']=$this->db->from('book_service')->count_all_results();
      $this->data['pending']=$this->db->from('book_service')->where('status',1)->count_all_results();
      $this->data['inprogress']=$this->db->from('book_service')->where('status',2)->count_all_results();
      $this->data['completed']=$this->db->from('book_service')->where('status',6)->count_all_results();
      $this->data['rejected']=$this->db->from('book_service')->where('status',5)->count_all_results();
      $this->data['cancelled']=$this->db->from('book_service')->where('status',7)->count_all_results();
      $this->data['list'] = $this->book->get_reject_bookings();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
    }
  }

/*Cancelled booking*/

  public function cancel_bookings() {
	  $this->common_model->checkAdminUserPermission(5);
    if(!empty($this->input->post())){
      extract($_POST);
         
          $service_id =$service_title;
          $status     =$service_status;
          $user_id    =$user_id;
          $provider_id=$provider_id;
          $from       =$from;
          $to         =$to;
      $this->data['page'] = 'cancel_booking_view';
      $this->data['model'] = 'bookings';
      $this->data['list'] = $this->book->get_filter_cancel_bookings($service_id,$status,$user_id,$provider_id,$from,$to);

      $this->data['filter']=array(
                                  'service_t'=>$service_title,
                                  'service_s'=>$service_status,
                                  'user_i'=>$user_id,
                                  'provider_i'=>$provider_id,
                                  'service_from'=>$from,
                                  'service_to'=>$to,
                                );
       $this->data['all_booking']=$this->db->from('book_service')->count_all_results();
      $this->data['pending']=$this->db->from('book_service')->where('status',1)->count_all_results();
      $this->data['inprogress']=$this->db->from('book_service')->where('status',2)->count_all_results();
      $this->data['completed']=$this->db->from('book_service')->where('status',6)->count_all_results();
      $this->data['rejected']=$this->db->from('book_service')->where('status',5)->count_all_results();
      $this->data['cancelled']=$this->db->from('book_service')->where('status',7)->count_all_results();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');

    }else{
      $this->data['page'] = 'cancel_booking_view';
      $this->data['model'] = 'bookings';
      $this->data['list'] = $this->book->get_cancel_bookings();
       $this->data['all_booking']=$this->db->from('book_service')->count_all_results();
      $this->data['pending']=$this->db->from('book_service')->where('status',1)->count_all_results();
      $this->data['inprogress']=$this->db->from('book_service')->where('status',2)->count_all_results();
      $this->data['completed']=$this->db->from('book_service')->where('status',6)->count_all_results();
      $this->data['rejected']=$this->db->from('book_service')->where('status',5)->count_all_results();
      $this->data['cancelled']=$this->db->from('book_service')->where('status',7)->count_all_results();
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



/* rejected payments */

public function reject_booking_payment(){
	$this->common_model->checkAdminUserPermission(5);
      $id=$this->uri->segment('2');

      if(!empty($this->uri->segment('2'))){
      $this->data['page'] = 'edit_reject_booking_view';
      $this->data['model'] = 'bookings';
      $this->data['list'] = $this->book->get_reject_bookings_by_id($this->uri->segment('2'));
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template'); 
      }else{
        redirect(base_url('admin/reject-report'));
      }


    
}

/* rejected function */

public function update_reject_payment(){
	$this->common_model->checkAdminUserPermission(5);
  if(!empty($_POST['booking_id']))
      
    $pay= $this->book->get_reject_bookings_by_id($_POST['booking_id']);

  if(!empty($pay['id'])){
            $paid_token='';
			$tomailid = '';
            if($_POST['token']==$pay['user_token']){
            $paid_token=$pay['user_token'];
			$this->data['uname'] = $pay['user_name'];
			$tomailid = $pay['user_email'];
            }
            if($_POST['token']==$pay['provider_token']){
            $paid_token=$pay['provider_token'];
			$this->data['uname'] = $pay['provider_name'];
			$tomailid = $pay['provider_email'];
            }

            $data['book_id']=$pay['id'];
            $data['service_title']=$_POST['service_name'];
            $data['amount']=$pay['amount'];
            $data['token']=$paid_token;
            $data['favour_comment']=$_POST['favour_comment'];

            $ret=$this->book->reject_pay_proccess($data);
            if($ret){
               $this->session->set_flashdata('success_message','Reject Payment added successfully');
			   
			   $phpmail_config=settingValue('mail_config');
              if(isset($phpmail_config)&&!empty($phpmail_config)){
                if($phpmail_config=="phpmail"){
                  $from_email=settingValue('email_address');
                }else{
                  $from_email=settingValue('smtp_email_address');
                }
              }
			  $this->data['service_amount']= $pay['amount'];
			  $this->data['service_date']= $pay['service_date'];
			  $this->data['service_title']= $data['service_title'];
              $this->data['comments'] = $data['favour_comment'];
			 // $providerbody=$this->load->view('user/email/service_email_admin',$this->data,true);
			  $bodyid = 5;
			  $tempbody_details= $this->templates_model->get_usertemplate_data($bodyid);
			  $body = $tempbody_details['template_content'];
			  $body = str_replace('{user_name}', $this->data['uname'], $body);
			  $body = str_replace('{service_amount}', $pay['amount'], $body);
			  $body = str_replace('{service_title}', $pay['service_title'], $body);
			  $body = str_replace('{service_date}', $pay['service_date'], $body);
			  $body = str_replace('{admin_comments}', $data['favour_comment'], $body);
			  $body = str_replace('{sitetitle}',$this->site_name, $body);
			  $preview_link = base_url();
			  $body = str_replace('{preview_link}',$preview_link, $body);
			  
              $this->load->library('email');
			  //$this->load->library('sms');
              //Send mail to provider
              if(!empty($from_email)&&isset($from_email)){
	        	$mail = $this->email
	            	->from($from_email)
	            	->to($tomailid)
	            	->subject('Service Booking Refund')
                    ->message($body)
	            	->send();
	         }
			  //send sms to provider
			  // $smsmessage = "Hi ".$this->data['uname'].", Admin has refunded your amount(". $pay['amount'] .") of this service (". $data['service_title'].") booked on ". $pay['service_date'];	  
			  // $this->sms->send_message($tomobile,$smsmessage);
			  
      redirect(base_url('admin/reject-report'));

            }else{
               $this->session->set_flashdata('error_message','Something wrong, Please try again');
      redirect(base_url('admin/reject-report'));

            }
          


  }else{
               $this->session->set_flashdata('error_message','Something wrong, Please try again');
      redirect(base_url('admin/reject-report'));


  
  }
}


  
}

?>
