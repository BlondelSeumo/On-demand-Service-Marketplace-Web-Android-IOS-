<?php 
class Emailtemplate extends CI_Controller{
    public function __construct() {
        parent::__construct();
        error_reporting(0);
		  $this->load->helper('ckeditor');
		  $this->load->model('common_model','common_model');
 		// Array with the settings for this instance of CKEditor (you can have more than one)
		$this->data['ckeditor_editor1'] = array
		(
			//id of the textarea being replaced by CKEditor
			'id'   => 'ck_editor_textarea_id',
 			// CKEditor path from the folder on the root folder of CodeIgniter
			'path' => 'assets/js/ckeditor',
 			// optional settings
			'config' => array
			(
				'toolbar' => "Full",
				'filebrowserBrowseUrl'      => base_url().'assets/js/ckfinder/ckfinder.html',
				'filebrowserImageBrowseUrl' => base_url().'assets/js/ckfinder/ckfinder.html?Type=Images',
				'filebrowserFlashBrowseUrl' => base_url().'assets/js/ckfinder/ckfinder.html?Type=Flash',
				'filebrowserUploadUrl'      => base_url().'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Files',
				'filebrowserImageUploadUrl' => base_url().'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Images',
				'filebrowserFlashUploadUrl' => base_url().'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Flash'
			)
		);  
        $this->data['theme'] = 'admin';
		$this->data['base_url'] = base_url();
		$this->data['model'] = 'emailtemplate';
        $this->load->model('Templates_model');
        $this->session->keep_flashdata('error_message');
        $this->session->keep_flashdata('success_message');
        $this->load->helper('user_timezone_helper');
        $this->data['admin_id'] = $this->session->userdata('id');
        $this->user_role = !empty($this->session->userdata('user_role'))?$this->session->userdata('user_role'):0;
    }
    public function index()
    {
		$this->common_model->checkAdminUserPermission(15);
        $this->data['page'] = 'index';
        $this->data['list'] = $this->Templates_model->get_template_list();
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'].'/template');
        
    }    
	    public function create()
    {
		$this->common_model->checkAdminUserPermission(15);
       
        if($this->input->post('form_submit'))
        {
                $data['template_content'] = $this->input->post('template_content');
                $data['template_title'] = $this->input->post('template_title');
                if($this->db->insert('email_templates',$data))
                {
                    $insert_id = $this->db->insert_id();
                    $this->db->where('template_id',$insert_id);
                    $this->db->update('email_templates',array('template_type'=>$insert_id)); 
                    $message='<div class="alert alert-success text-center fade in" id="flash_succ_message">Created successfully.</div>';
                    
                }
           $this->session->set_flashdata('message',$message);
            redirect(base_url().'admin/emailtemplate');
        }
        $this->data['page'] = 'create';
       // $this->data['list'] = $this->admin_panel_model->emailtemplate();
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'].'/template');
    }    
    public function edit($id) 
    {
        $this->common_model->checkAdminUserPermission(15);

        if($this->input->post('form_submit'))
        {
            $data['template_content'] = $this->input->post('template_content');       
            $this->db->where('template_id',$id);
            if($this->db->update('email_templates',$data))
            {
        		$message='<div class="alert alert-success text-center fade in" id="flash_succ_message">Edited successfully.</div>';
        			
            }
		   $this->session->set_flashdata('message',$message);
            redirect(base_url().'admin/emailtemplate');
        }
        $this->data['page']='edit';
        $this->data['edit_data'] = $this->Templates_model->edit_template($id);
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'].'/template');
        
    }
}
?>