<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Categories extends CI_Controller {

	public $data;

   public function __construct() {

        parent::__construct();
        error_reporting(0);
        $this->data['theme']     = 'user';
        $this->data['module']    = 'categories';
        $this->data['page']     = '';
        $this->data['base_url'] = base_url();
        $this->load->helper('user_timezone_helper');
        $this->load->model('service_model','service');
        
        
         $default_language_select = default_language();

        if ($this->session->userdata('user_select_language') == '') {
            $this->data['user_selected'] = $default_language_select['language_value'];
        } else {
            $this->data['user_selected'] = $this->session->userdata('user_select_language');
        }

        $this->data['active_language'] = $active_lang = active_language();

        $lg = custom_language($this->data['user_selected']);

        $this->data['default_language'] = $lg['default_lang'];

        $this->data['user_language'] = $lg['user_lang'];

        $this->user_selected = (!empty($this->data['user_selected'])) ? $this->data['user_selected'] : 'en';

        $this->default_language = (!empty($this->data['default_language'])) ? $this->data['default_language'] : '';

        $this->user_language = (!empty($this->data['user_language'])) ? $this->data['user_language'] : '';
        
        
    }

	
	public function index()
	{
		 $this->data['page'] = 'index';
	   $this->load->vars($this->data);
		 $this->load->view($this->data['theme'].'/template');
	}

	
  
  public function featured_categories()
  {
     $this->data['page'] = 'featured_categories';
     $this->data['featured_category'] = $this->service->featured_category();
     $this->load->vars($this->data);
     $this->load->view($this->data['theme'].'/template');
  }

   
 public function user_dashboard()
  {
     $this->data['page'] = 'user_dashboard';
     $this->load->vars($this->data);
     $this->load->view($this->data['theme'].'/template');
  }

  

}
