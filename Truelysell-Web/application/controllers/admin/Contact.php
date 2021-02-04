<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Contact extends CI_Controller {

 public $data;

 public function __construct() {

  parent::__construct();
  error_reporting(0);
  $this->load->model('admin_model','admin');
  $this->data['theme'] = 'admin';
  $this->data['model'] = 'contact';
  $this->data['base_url'] = base_url();
  $this->session->keep_flashdata('error_message');
  $this->session->keep_flashdata('success_message');
  $this->load->helper('user_timezone_helper'); 
  $this->data['user_role']=$this->session->userdata('role');
}

public function index()
{
$this->data['page'] = 'contact';

  $this->data['list_filter'] = $this->admin->contact_list();

  if ($this->input->post('form_submit')) 
    {    extract($_POST);
     $name = $this->input->post('name');
     $email = $this->input->post('email');    
     $this->data['list'] = $this->admin->contact_list_filter($name,$email);
   }else{
    $this->data['list'] = $this->admin->contact_list();
  }
  
  
  $this->load->vars($this->data);
  $this->load->view($this->data['theme'].'/template');
}

public function contact_details($value='')
{
  $this->data['page'] = 'contact_details';
  $this->data['list'] = $this->admin->contactreply_list($value);
  $this->load->vars($this->data);
  $this->load->view($this->data['theme'].'/template');
}

 public function reply_contact() {
        $id = $this->input->post('contact_id');
        $name = $this->input->post('uname');
        $email = $this->input->post('umail');
        $reply = $this->input->post('replycont');
		$ins_data=array("contact_id"=>$id,"name"=>$name,"reply"=>$reply,"created_at"=>date('Y-m-d H:i:s'));
		//echo "<pre>";print_r($ins_data);exit;
		$result=$this->db->insert('contact_reply', $ins_data);
		$insid=$this->db->insert_id();
		 if ($result) {
            $this->data['user'] = $this->session->userdata();
            $this->data['reply_det'] = $this->db->where('id', $insid)->from('contact_reply')->get()->row_array();
            $body = $this->load->view('admin/email/contact_reply', $this->data, true);
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
                        ->to($email)
                        ->subject('Contact Reply')
                        ->message($body)
                        ->send();
						//print_r($mail);exit;
            }
			
            $message = 'Mail Sent Successfully';
            $this->session->set_flashdata('success_message', $message);
			  echo 1;
        } else {
            $message = 'Sorry, something went wrong';
            $this->session->set_flashdata('error_message', $message);
        }
		
    }



}
