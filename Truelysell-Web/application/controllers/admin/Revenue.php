<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Revenue extends CI_Controller {

    public $data;

    public function __construct() {

        parent::__construct();
        $this->load->helper('form');
        
        $this->load->model('admin_model', 'admin');
		$this->load->model('common_model','common_model');
        $this->data['theme'] = 'admin';
        $this->data['model'] = 'revenue';
        $this->data['base_url'] = base_url();
        $this->session->keep_flashdata('error_message');
        $this->session->keep_flashdata('success_message');
        $this->load->helper('user_timezone_helper');
        $this->data['user_role'] = $this->session->userdata('role');
    }

    public function index() {
		
		$this->common_model->checkAdminUserPermission(11);

        $this->data['page'] = 'revenue_list';
        $this->data['list'] = $this->admin->Revenue_list();
//        echo '<pre>';
//        print_r($this->data['list']);exit;
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    

}
