<?php

if (!defined('BASEPATH'))
    exit('No direct script access allowed');

class Common {

    public function __construct() {
        $this->ci = & get_instance();
    }

    public function global_file_upload($directory, $fieldname, $filename = '') {
        $config['upload_path'] = realpath($directory);
        $config['allowed_types'] = '*';
        $config['max_size'] = '0';
      
        if (!empty($filename)) {
            $config['file_name'] = $filename;
        }

        $this->ci->load->library('upload', $config);
        $data = array();
        if (!$this->ci->upload->do_upload($fieldname)) {
            $data['success'] = 'n';
            $data['data'] = $this->ci->upload->display_errors();
        } else {
            $data['success'] = 'y';
            $data['data'] = $this->ci->upload->data();
            return $data;
        }
        return $data;
    }

}

/* End of file Common.php */
