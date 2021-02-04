<?php
class Footer_submenu extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
        error_reporting(0);
        $this->data['theme']  = 'admin';
        $this->data['model'] = 'footer_submenu';
        $this->load->model('admin_model');
        $this->data['base_url'] = base_url();
        $this->data['admin_id']  = $this->session->userdata('id');
        $this->user_role         = !empty($this->session->userdata('user_role')) ? $this->session->userdata('user_role') : 0;
        $this->data['main_menu'] = $this->admin_model->get_all_footer_menu();
        $this->load->helper('ckeditor');
        $this->load->helper('common_helper');
        // Array with the settings for this instance of CKEditor (you can have more than one)
        $this->data['ckeditor_editor1'] = array(
            //id of the textarea being replaced by CKEditor
            'id' => 'ck_editor_textarea_id',
            // CKEditor path from the folder on the root folder of CodeIgniter
            'path' => 'assets/js/ckeditor',
            // optional settings
            'config' => array(
                'toolbar' => "Full",
                'filebrowserBrowseUrl' => base_url() . 'assets/js/ckfinder/ckfinder.html',
                'filebrowserImageBrowseUrl' => base_url() . 'assets/js/ckfinder/ckfinder.html?Type=Images',
                'filebrowserFlashBrowseUrl' => base_url() . 'assets/js/ckfinder/ckfinder.html?Type=Flash',
                'filebrowserUploadUrl' => base_url() . 'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Files',
                'filebrowserImageUploadUrl' => base_url() . 'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Images',
                'filebrowserFlashUploadUrl' => base_url() . 'assets/js/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Flash'
            )
        );
    }
    public function index($offset = 0)
    {
        $this->data['page']  = 'index';
        $this->data['lists'] = $this->admin_model->get_footer_submenu();
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
    public function create()
    {
        $this->data['page'] = 'create';
        if ($this->input->post('form_submit')) {
            if ($this->data['admin_id'] > 1) {
                $this->session->set_flashdata('message', '<p class="alert alert-danger">Permission Denied</p>');
                redirect(base_url() . 'admin/footer_submenu');
            } else {
                $data['footer_menu']    = $this->input->post('main_menu');
                $value                  = $this->input->post('sub_menu');
                $data['footer_submenu'] = str_replace(' ', '_', $value);
                $data['page_desc']      = $this->input->post('page_desc');
                $data['status']         = $this->input->post('status');
                $data['menu_status']    = $this->input->post('menu_status');
                if ($this->db->insert('footer_submenu', $data)) {
                    $message = "<div class='alert alert-success text-center fade in' id='flash_succ_message'>footer menu created successfully.</div>";
                }
                $this->session->set_flashdata('message', $message);
                redirect(base_url() . 'admin/footer_submenu');
            }
        }
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
    public function edit($id)
    {
        $this->data['page']     = 'edit';
        $this->data['datalist'] = $this->admin_model->edit_submenu($id);
        if ($this->data['admin_id'] > 1) {
            $this->session->set_flashdata('message', '<p class="alert alert-danger">Permission Denied</p>');
            redirect(base_url() . 'admin/footer_submenu');
        } else {
            if ($this->input->post('form_submit')) {
                $data['footer_menu']    = $this->input->post('main_menu');
                $value                  = $this->input->post('sub_menu');
                $data['footer_submenu'] = str_replace(' ', '_', $value);
                $data['page_desc']      = $this->input->post('page_desc');
                $data['status']         = $this->input->post('status');
                $data['menu_status']    = $this->input->post('menu_status');
                $this->db->where('id', $id);
                if ($this->db->update('footer_submenu', $data)) {
                    $message = "<div class='alert alert-success text-center fade in' id='flash_succ_message'>footer menu edited successfully.</div>";
                }
                $this->session->set_flashdata('message', $message);
                redirect(base_url() . 'admin/footer_submenu');
            }
        }
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
    public function delete_footer_submenu()
    {
        if ($this->data['admin_id'] > 1) {
            $this->session->set_flashdata('message', '<p class="alert alert-danger">Permission Denied</p>');
            redirect(base_url() . 'admin/footer_submenu');
        } else {
            $id = $this->input->post('tbl_id');
            if (!empty($id)) {
                $this->db->delete('footer_submenu', array(
                    'id' => $id
                ));
                $message = "<div class='alert alert-success text-center fade in' id='flash_succ_message'>footer menu deleted successfully.</div>";
                echo 1;
            }
            $this->session->set_flashdata('message', $message);
        }
    }
}
