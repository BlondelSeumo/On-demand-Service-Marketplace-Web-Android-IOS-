<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Ratingstype extends CI_Controller {

   public $data;

   public function __construct() {

        parent::__construct();
        $this->load->model('service_model','service');
		$this->load->model('common_model','common_model');
        $this->data['theme'] = 'admin';
        $this->data['model'] = 'ratingstype';
        $this->data['base_url'] = base_url();
        $this->session->keep_flashdata('error_message');
        $this->session->keep_flashdata('success_message');
        $this->load->helper('user_timezone_helper');
        $this->data['user_role']=$this->session->userdata('role');
    }

	public function index()
	{
		redirect(base_url('ratingstype'));
	}

  /*delete comments*/

  public function delete_comment(){
	  $this->common_model->checkAdminUserPermission(8);
    $id=$this->input->post('id');

            
            $table_data['review'] = '...';
            $this->db->where('id',$id);                
            if($this->db->update('rating_review', $table_data))
            {
                 $this->session->set_flashdata('success_message','Ratings type updated successfully');    
                 redirect(base_url()."ratingstype");   
            }
            else
            {
                $this->session->set_flashdata('error_message','Something wrong, Please try again');
                redirect(base_url()."ratingstype");   

             } 
  }
	public function ratingstype()
	{
		$this->common_model->checkAdminUserPermission(7);
    if($this->session->userdata('admin_id'))
		{
  		$this->data['page'] = 'ratingstype';
  		$this->data['model'] = 'ratingstype';
  		$this->data['list'] = $this->service->ratingstype_list();
  		$this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');

		}
		else {
			redirect(base_url()."admin");
		}
	}

  /*review Ratings*/

    public function review_report()
  {
	  $this->common_model->checkAdminUserPermission(8);
    if($this->session->userdata('admin_id'))
    {

      $this->data['page'] = 'review_report';
      $this->data['model'] = 'review';

      $this->data['rating_type'] = $this->service->ratingstype_list();
      $this->data['user_list'] = $this->service->get_user_list();
      $this->data['provider_list'] = $this->service->get_provider_list();
      $this->data['service_list'] = $this->service->get_service_list();
      

      if(!empty($this->input->post())){
       
            removeTag($this->input->post());
  
      $service=$this->input->post('service_id');
      $provider=$this->input->post('provider_id');
      $user=$this->input->post('user_id');
      $type=$this->input->post('type_id');
      $from=$this->input->post('from');
      $to=$this->input->post('to');
      $this->data['list'] = $this->service->get_review_filter($service,$provider,$user,$type,$from,$to);
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
      }else{
        
      $this->data['page'] = 'review_report';
      $this->data['model'] = 'review';
      $this->data['list'] = $this->service->get_review();
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
      }


    }
    else {
      redirect(base_url()."admin");
    }
  }


	public function add_ratingstype()
	{
		$this->common_model->checkAdminUserPermission(7);
    if($this->session->userdata('admin_id'))
		{

      if ($this->input->post('form_submit')) {  

           removeTag($this->input->post());
            $table_data['name'] = $this->input->post('name');
            $table_data['status'] = 0;                
            if($this->db->insert('rating_type', $table_data))
            {
                 $this->session->set_flashdata('success_message','Ratings type added successfully');    
                 redirect(base_url()."add-ratingstype");   
            }
            else
            {
                $this->session->set_flashdata('error_message','Something wrong, Please try again');
                redirect(base_url()."add-ratingstype");   

             } 
               
         
    }


  		$this->data['page'] = 'add_ratingstype';
  		$this->data['model'] = 'ratingstype';
  		$this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
  		
		}
		else {
			redirect(base_url()."admin");
		}
            

	}


  public function edit_ratingstype($id)
  {
	  $this->common_model->checkAdminUserPermission(7);
    if($this->session->userdata('admin_id'))
    {


      if ($this->input->post('form_submit')) {  
 removeTag($this->input->post());
            $id=$this->input->post('id');
            $table_data['name'] = $this->input->post('name');
            $table_data['status'] = 0;
            $this->db->where('id',$id);                
            if($this->db->update('rating_type', $table_data))
            {
                 $this->session->set_flashdata('success_message','Ratings type updated successfully');    
                 redirect(base_url()."ratingstype");   
            }
            else
            {
                $this->session->set_flashdata('error_message','Something wrong, Please try again');
                redirect(base_url()."ratingstype");   

             } 
               
         
    }


      $this->data['page'] = 'edit_ratingstype';
      $this->data['model'] = 'ratingstype';
      $this->data['ratingstype'] = $this->service->ratingstype_details($id);
      $this->load->vars($this->data);
      $this->load->view($this->data['theme'].'/template');
      
    }
    else {
      redirect(base_url()."admin");
    }

  }

  
  public function check_ratingstype_name()
  {
    $name = $this->input->post('name');
    $id = $this->input->post('id');
    if(!empty($id))
    {
      $this->db->select('*');
      $this->db->where('name', $name);
      $this->db->where('id !=', $id);
      $this->db->from('rating_type');
      $result = $this->db->get()->num_rows();
    }
    else
    {
      $this->db->select('*');
      $this->db->where('name', $name);
      $this->db->from('rating_type');
      $result = $this->db->get()->num_rows();
    }
    if ($result > 0) {
      $isAvailable = FALSE;
    } else {
      $isAvailable = TRUE;
    }
    echo json_encode(
      array(
        'valid' => $isAvailable
     ));
  }


  public function delete_ratingstype()
  {
	  $this->common_model->checkAdminUserPermission(7);
    $id=$this->input->post('id');
    $this->db->where('id',$id);
    if($this->db->delete('rating_type'))
   {
           $this->session->set_flashdata('success_message','Ratings type deleted successfully');    
           redirect(base_url()."ratingstype");   
    }
    else
    {
        $this->session->set_flashdata('error_message','Something wrong, Please try again');
        redirect(base_url()."ratingstype");   

     } 
  }  
}
