<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Login extends CI_Controller {

   public $data;

   public function __construct() {

        parent::__construct();
        $this->load->model('admin_model','admin');
        $this->data['theme'] = 'admin';
        $this->data['model'] = 'login';
        $this->data['base_url'] = base_url();
        $this->load->helper('form');
        $this->data['csrf'] = array(
	    'name' => $this->security->get_csrf_token_name(),
	    'hash' => $this->security->get_csrf_hash()
	    );
    }


	public function index()
	{
	    if (empty($this->session->userdata['admin_id']))
	    {
	    	$this->load->vars($this->data);
	    	$this->load->view($this->data['theme'] . '/include/header');
		    $this->load->view($this->data['theme'].'/'.$this->data['model'].'/login');
	  		$this->load->view($this->data['theme'] . '/include/footer');
	    }
	    else {
	      redirect(base_url()."dashboard");
	    }
	}

  public function is_valid_login()
	{ 
		$username = $this->input->post('username');
		$password = $this->input->post('password');
		$result = $this->admin->is_valid_login($username,$password);

		if(!empty($result))
		{
		$this->session->set_userdata('admin_id',$result['user_id']);
  		$this->session->set_userdata('admin_profile_img',$result['profile_img']);
  		$this->session->set_userdata('chat_token',$result['token']);
  		$this->session->set_userdata('role',$result['role']);
		$access_result_data = $this->db->where('admin_id',$result['user_id'])->where('access',1)->select('module_id')->get('admin_access')->result_array(); 
		$access_result_data_array = array_column($access_result_data, 'module_id');
		$this->session->set_userdata('access_module',$access_result_data_array);

			echo 1;
		}
	 else
		{
    $this->session->set_flashdata('error_message','Wrong login credentials.');
			echo 2;
		}
	}

	
 	public function logout()
	{
	    if (!empty($this->session->userdata['admin_id']))
	    {
	      $this->session->unset_userdata('admin_id');
	    }
	    $this->session->set_flashdata('success_message','Logged out successfully');
			redirect(base_url()."admin");
    }
	
	public function forgot_password()
	{
	  if (empty($this->session->userdata['admin_id']))
	    {
	    	$this->load->vars($this->data);
	    	$this->load->view($this->data['theme'] . '/include/header');
		    $this->load->view($this->data['theme'].'/'.$this->data['model'].'/forgot_password');
	  		$this->load->view($this->data['theme'] . '/include/footer');
	    }
	    else {
	      redirect(base_url()."dashboard");
	    }
    }
	
	public function check_forgot_pwd()
	{ 
		$email = $this->input->post('email');
		$result = $this->admin->check_admin_email($email);
		
		//print_r($result);exit;

		if(!empty($result))
		{
			$token=rand(1000,9999);
			$pwdlink=base_url()."admin/login/adminchangepwd/".base64_encode($result['user_id'])."/".base64_encode($token);
			$chk_forpawd=$this->db->where('user_id',$result['user_id'])->where('user_type','admin')->where('status','1')->select('*')->get('forget_password_det')->result_array(); 
			if(empty($chk_forpawd))
			{
				$pwdlink_data=array(
				  'endtime'=>time()+300,
				  'token'=> $token,
				  'user_id'=>$result['user_id'],
				  'email'=>$result['email'],
				  'pwdlink'=>$pwdlink,
				  'user_type'=>'admin',
				  'created_at'=>date('Y-m-d H:i:s')
				);
				$save_forpwd = $this->admin->save_pwdlink_data($pwdlink_data);
			}
			else
			{
				$pwdlink_data=array(
			  'endtime'=>time()+300,
			  'token'=>$token ,
			  'user_id'=>$result['user_id'],
			  'email'=>$result['email'],
			  'pwdlink'=>$pwdlink,
			  'user_type'=>'admin',
			  'updated_on'=>date('Y-m-d H:i:s')
				);
				$save_forpwd = $this->admin->update_pwdlink_data($pwdlink_data,$result['user_id']);
			}
			
			$message='Reset Link  '.$pwdlink.''; 
			$body = 'Hi '.$result["username"].',<br> '.$message;
			
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
                        ->subject('Admin Forget Password')
                        ->message($body)
                        ->send();
						
            }
			
			echo 1;
		}
	 else
		{
    $this->session->set_flashdata('error_message','Email ID Not Exist...!');
			echo 2;
		}
	}
	
	
	public function adminchangepwd($user_id,$token)
	{
		
	  if (empty($this->session->userdata['admin_id']))
	    {
			$data['euser_id']=base64_decode($user_id);
			$data['etoekn']=base64_decode($token);
			$data['chk_data']=$this->db->where('user_id',$data['euser_id'])->where('user_type','admin')->where('token',$data['etoekn'])->where('status',1)->select('*')->get('forget_password_det')->result_array(); 
			
	    	$this->load->vars($this->data);
	    	$this->load->view($this->data['theme'] . '/include/header');
		    $this->load->view($this->data['theme'].'/'.$this->data['model'].'/reset_forgot_password',$data);
	  		$this->load->view($this->data['theme'] . '/include/footer');
	    }
	    else {
	      redirect(base_url()."dashboard");
	    }
    }
	
	
	public function save_reset_password()
	{ 
		$user_id = $this->input->post('user_id');
		$confirm_password = array(
				  'password'=>md5($this->input->post('confirm_password'))
				);
		
		$chkdata =$this->db->where('user_id',$user_id)->select('*')->get('administrators')->result_array(); 
		
		//print_r($result);exit;

		if(!empty($chkdata))
		{
			 
			$save_pwd = $this->admin->update_res_pwd($confirm_password,$user_id);
			
			$change_sts=$this->admin->update_forpwd_status($status = array('status'=>0),$user_id);
			$this->session->set_flashdata('error_message','Password Changed Successfully...!');
			
			
			echo 1;
		}
	 else
		{
    $this->session->set_flashdata('error_message','Something Went wrong...!');
			echo 2;
		}
	}
	
	
	
	
	
	
	
	
	

}
