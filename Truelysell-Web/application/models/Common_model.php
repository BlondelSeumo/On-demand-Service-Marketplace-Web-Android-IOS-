<?php defined('BASEPATH') OR exit('No direct script access allowed'); 
 
class Common_model extends CI_Model{ 
     
	function checkAdminUserPermission($permission_id,$no_return = true)
	{
		if(in_array($permission_id, $this->session->userdata('access_module'))){
			return true;
		}else{
			if($no_return){
				if($this->input->is_ajax_request()){
					$data['success'] = false;
					$data['message_title'] = 'Permissions Denied';
					$data['message'] = 'Sorry You are not allowed to access this feature';
					$data['error_type'] = 'auth';
					echo json_encode($data); die;
				}else{
					echo '<h1 align="center">Permissions Denied - Sorry You are not allowed to access this feature</h1>';die;
				}
			}else{
				return false;
			}
		}
	}
    
}