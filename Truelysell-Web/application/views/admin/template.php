 <?php
if (isset($this->session->userdata['admin_id'])) {
    $this->load->view($theme . '/include/header');
    $this->load->view($theme . '/include/navbar');
    $this->load->view($theme . '/include/sidebar');
    $this->load->view($theme . '/' . $model . '/' . $page);
    $this->load->view($theme . '/include/footer');
} else {
   redirect(base_url().'admin/login');
}


       
       