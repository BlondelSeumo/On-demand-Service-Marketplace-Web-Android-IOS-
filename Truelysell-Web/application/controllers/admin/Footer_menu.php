<?php
class Footer_menu extends CI_Controller
{
    public $data;
    public function __construct()
    {
        parent::__construct();
        error_reporting(0);
        $this->data['theme']  = 'admin';
        $this->data['model'] = 'footer_menu';
        $this->load->model('admin_model');
        $this->data['base_url'] = base_url();
        $this->data['admin_id'] = $this->session->userdata('id');
        $this->user_role        = !empty($this->session->userdata('user_role')) ? $this->session->userdata('user_role') : 0;
        $this->load->helper('ckeditor');
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
        $this->data['page']        = 'index';
        $this->data['lists']       = $this->admin_model->get_all_footer_menu();
        $this->data['footercount'] = $this->admin_model->footercount();
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
    public function create()
    {
        if ($this->input->post('form_submit')) {
            if ($this->data['admin_id'] > 1) {
                $this->session->set_flashdata('message', '<p class="alert alert-danger">Permission Denied</p>');
                redirect(base_url() . 'admin/footer_menu');
            } else {
                str_replace("world", "Peter", "Hello world!");
                $value               = $this->input->post('menu_name');
                $table_data['title'] = str_replace(' ', '_', $value);
                if ($this->db->insert('footer_menu', $table_data)) {
                    $message = '<div class="alert alert-success text-center fade in" id="flash_succ_message">footer widget added successfully. </div>';
                    $this->session->set_flashdata('message', $message);
                    redirect(base_url('admin/' . $this->data['model']));
                }
            }
        }
        $this->data['page'] = 'create';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
    public function edit($cls_id)
    {
        $current_date = date('Y-m-d H:i:s');
        if ($this->data['admin_id'] > 1) {
            $this->session->set_flashdata('message', '<p class="alert alert-danger">Permission Denied</p>');
            redirect(base_url() . 'admin/footer_menu');
        } else {
            if (!empty($cls_id)) {
                if ($this->input->post('form_submit')) {
                    $value               = $this->input->post('menu_name');
                    $table_data['title'] = str_replace(' ', '_', $value);
                    $this->db->update('footer_menu', $table_data, "id = " . $cls_id);
                    $message = '<div class="alert alert-success text-center fade in" id="flash_succ_message">footer widget edited successfully. </div>';
                    $this->session->set_flashdata('message', $message);
                    redirect(base_url('admin/' . $this->data['model']));
                }
                $this->data['datalist'] = $this->admin_model->edit_footer_menu($cls_id);
                $this->data['page']     = 'edit';
                $this->load->vars($this->data);
                $this->load->view($this->data['theme'] . '/template');
            } else {
                redirect(base_url('admin/' . $this->data['model']));
            }
        }
    }
    public function delete_footer_menu()
    {
        if ($this->data['admin_id'] > 1) {
            $this->session->set_flashdata('message', '<p class="alert alert-danger">Permission Denied</p>');
            redirect(base_url() . 'admin/footer_menu');
        } else {
            $id = $this->input->post('tbl_id');
            if (!empty($id)) {
                $this->db->delete('footer_menu', array(
                    'id' => $id
                ));
                $message = '<div class="alert alert-success text-center fade in" id="flash_succ_message">footer widget delete successfully. </div>';
                echo 1;
            }
            $this->session->set_flashdata('message', $message);
        }
    }
    public function notification($pag_id)
    {
        $page_id = $pag_id;
        $this->db->set('notify_status', '1', FALSE);
        $this->db->where('page_id', $pag_id);
        $this->db->update('page');
        redirect(base_url("admin/page/edit/" . $page_id));
    }
}
