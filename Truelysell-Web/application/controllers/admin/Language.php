<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Language extends CI_Controller {

    public $data;

    public function __construct() {

        parent::__construct();
        $this->load->helper('form');
        $this->load->model('language_model', 'language');
        $this->load->model('language_web_model', 'web_language');
        $this->load->model('admin_model', 'admin');
        $this->data['theme'] = 'admin';
        $this->data['model'] = 'language';
        $this->data['base_url'] = base_url();
        $this->session->keep_flashdata('error_message');
        $this->session->keep_flashdata('success_message');
        $this->load->helper('user_timezone_helper');
        $this->data['user_role'] = $this->session->userdata('role');
    }

    public function index() {

        $this->data['page'] = 'language';
        $this->data['list'] = $this->admin->language_list();
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function AddLanguages() {
        $this->data['page'] = 'add_languages';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function InsertLanguage() {
        $postdata = $this->input->post();

        $data = [
            'language' => $postdata['language_name'],
            'language_value' => $postdata['language_value'],
            'tag' => $postdata['language_type'],
        ];
        $insert = $this->db->insert('language', $data);
        if (!empty($insert) && $insert != '') {
            $this->session->set_flashdata('success_message', 'Language added successfully');
            redirect(base_url() . "language");
        } else {
            $this->session->set_flashdata('error_message', 'Language added not successfully');
            redirect(base_url() . "add-language");
        }
    }

    public function Wep_Language() {
        $this->data['page'] = 'wep_language';
        $this->data['active_language'] = $this->language->active_language();
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function AddWepKeyword() {
        $this->data['page'] = 'add_wep_keyword';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function AppPageList() {
        $this->data['list'] = $this->language->page_list();
    //print_r($this->data['list']);exit;
        $this->data['page'] = 'app_page_list';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function AddAPPKeyword() {

        $this->data['page'] = 'add_app_keyword';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function pages_language() {
        $this->data['page'] = 'pages_language';
        $this->data['active_language'] = $this->language->active_language();
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }
    
    public function AllAPPKeyword() {
        
        

        $this->data['page'] = 'all_app_keyword';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function InsertWepKeyword() {
        $postdata = $this->input->post();
        $data = [
            'lang_value' => $postdata['filed_name'],
            'lang_key' => trim($postdata['key_name']),
            'language' => 'en'
        ];
        $insert = $this->db->insert('language_management', $data);
        if (!empty($insert) && $insert != '') {
            $this->session->set_flashdata('success_message', 'Language added successfully');
            redirect(base_url() . "wep_language");
        } else {
            $this->session->set_flashdata('error_message', 'Language added not successfully');
            redirect(base_url() . "add-wep-keyword");
        }
    }

    public function InsertAppKeyword() {
        $postdata = $this->input->post();
        $page_name = $postdata['page_name'];
        $page_key = str_replace(array(' ', '!', '&'), '_', strtolower($page_name));
        $PageInsert = [
            'page_title' => $postdata['page_name'],
            'page_key' => $page_key,
            'status' => 1
        ];
        $insertPage = $this->db->insert('pages', $PageInsert);
        if (!empty($insertPage) && $insertPage != '') {
            $this->session->set_flashdata('success_message', 'Page added successfully');
            redirect(base_url() . "app_page_list");
        } else {
            $this->session->set_flashdata('error_message', 'Page added not successfully');
            redirect(base_url() . "add-app-keyword");
        }
    }

    public function language_web_list() {
        $lists = $this->web_language->language_list();
        $data = array();
        $no = $_POST['start'];
        $active_language = $this->web_language->active_language();
        foreach ($lists as $keyword) {
            // $no++;
            $row = array();
            // $row[] = $no;
            if (!empty($active_language)) {
                foreach ($active_language as $rows) {
                    $lg_language_name = $keyword['lang_key'];
                    $language_key = $rows['language_value'];
                    $key = $keyword['language'];
                    $value = ($language_key == $key) ? $keyword['lang_value'] : '';
                    $key = $keyword['language'];
                    $currenct_page_key_value = $this->web_language->currenct_page_key_value($lists);
                    $name = (!empty($currenct_page_key_value[$lg_language_name][$language_key]['name'])) ? $currenct_page_key_value[$lg_language_name][$language_key]['name'] : '';
                    $lang_key = (!empty($currenct_page_key_value[$lg_language_name][$language_key]['lang_key'])) ? $currenct_page_key_value[$lg_language_name][$language_key]['lang_key'] : '';
                    $readonly = '';

                    $row[] = '<input type="text" class="form-control" placeholder="Name" name="' . $lg_language_name . '[' . $language_key . '][lang_value]" value="' . $name . '" ' . $readonly . ' ><br>
                            <input type="text" class="form-control" value="' . $lang_key . '" readonly >
                         ';
                }
            }

            $data[] = $row;
        }
        $output = array(
            "draw" => $_POST['draw'],
            "recordsTotal" => $this->web_language->language_list_all(),
            "recordsFiltered" => $this->web_language->language_list_filtered(),
            "data" => $data,
        );
        echo json_encode($output);
    }

    public function update_multi_web_language() {

        if ($this->input->post()) {
            if ($this->data['admin_id'] > 1) {
                $this->session->set_flashdata('error_message', '<p class="alert alert-danger">Permission Denied</p>');
                redirect(base_url() . 'wep_language');
            } else {
                $data = $this->input->post();

                foreach ($data as $row => $object) {
                    if (!empty($object)) {
                        foreach ($object as $key => $value) {
                            $this->db->where('language', $key);
                            $this->db->where('lang_key', $row);
                            $record = $this->db->count_all_results('language_management');
                            if ($record == 0) {
                                $array = array(
                                    'language' => $key,
                                    'lang_key' => $row,
                                    'lang_value' => $value['lang_value'],
                                );

                                $this->db->insert('language_management', $array);
                            } else {

                                $this->db->where('language', $key);
                                $this->db->where('lang_key', $row);


                                $array = array(
                                    'lang_value' => $value['lang_value'],
                                );

                                $this->db->update('language_management', $array);
                            }
                        }
                    }
                }
            }
        }
        redirect(base_url() . 'wep_language');
    }

    public function language_list() {
        $page_key = $this->input->post('page_key');
        
        
        
        $lists = $this->language->language_list($page_key);

        $data = array();
        $no = $_POST['start'];
        $active_language = $this->language->active_language();
        foreach ($lists as $keyword) {
            // $no++;
            $row = array();
            // $row[] = $no;
            if (!empty($active_language)) {
                foreach ($active_language as $rows) {

                    $lg_language_name = $keyword['lang_key'];
                    $language_key = $rows['language_value'];


                    $key = $keyword['language'];
                    $value = ($language_key == $key) ? $keyword['lang_value'] : '';
                    $key = $keyword['language'];
                    $currenct_page_key_value = $this->language->currenct_page_key_value($lists);



                    $name = (!empty($currenct_page_key_value[$lg_language_name][$language_key]['name'])) ? $currenct_page_key_value[$lg_language_name][$language_key]['name'] : '';
                    $placeholder = (!empty($currenct_page_key_value[$lg_language_name][$language_key]['placeholder'])) ? $currenct_page_key_value[$lg_language_name][$language_key]['placeholder'] : '';
                    $validation1 = (!empty($currenct_page_key_value[$lg_language_name][$language_key]['validation1'])) ? $currenct_page_key_value[$lg_language_name][$language_key]['validation1'] : '';
                    $validation2 = (!empty($currenct_page_key_value[$lg_language_name][$language_key]['validation2'])) ? $currenct_page_key_value[$lg_language_name][$language_key]['validation2'] : '';
                    $validation3 = (!empty($currenct_page_key_value[$lg_language_name][$language_key]['validation3'])) ? $currenct_page_key_value[$lg_language_name][$language_key]['validation3'] : '';
                    $lang_key = (!empty($currenct_page_key_value[$lg_language_name][$language_key]['lang_key'])) ? $currenct_page_key_value[$lg_language_name][$language_key]['lang_key'] : '';


                    $type = $currenct_page_key_value[$lg_language_name]['en']['type'];


                    //$readonly = ($language_key=='en')?'readonly':'';

                    $readonly = '';


                    $row[] = '<input type="text" class="form-control" placeholder="Name" name="' . $lg_language_name . '[' . $language_key . '][lang_value]" value="' . $name . '" ' . $readonly . ' ><br>
                          <input type="text" class="form-control" placeholder="Placeholder" name="' . $lg_language_name . '[' . $language_key . '][placeholder]" value="' . $placeholder . '" ' . $readonly . ' ><br>
                          <input type="text" class="form-control" placeholder="Validation 1" name="' . $lg_language_name . '[' . $language_key . '][validation1]" value="' . $validation1 . '" ' . $readonly . ' ><br>
                          <input type="text" class="form-control" placeholder="Validation 2" name="' . $lg_language_name . '[' . $language_key . '][validation2]" value="' . $validation2 . '" ' . $readonly . ' ><br>
                          <input type="text" class="form-control" placeholder="Validation 3" name="' . $lg_language_name . '[' . $language_key . '][validation3]" value="' . $validation3 . '" ' . $readonly . ' ><br>
                          <input type="text" class="form-control" value="' . $lang_key . '" readonly >
                          <input type="hidden" class="form-control" name="' . $lg_language_name . '[' . $language_key . '][type]" value="' . $type . '" ' . $readonly . ' >';
                }
            }

            $data[] = $row;
        }
        $output = array(
            "draw" => $_POST['draw'],
            "recordsTotal" => $this->language->language_list_all($page_key),
            "recordsFiltered" => $this->language->language_list_filtered($page_key),
            "data" => $data,
        );

        //output to json format
        echo json_encode($output);
    }

    public function update_language_status() {

        $id = $this->input->post('id');
        $status = $this->input->post('update_language');

        if ($status == 2) {
            $this->db->where('id', $id);
            $this->db->where('default_language', 1);
            $data = $this->db->get('language')->result_array();

            if (!empty($data)) {
                echo "0";
            } else {
                $this->db->query(" UPDATE `language` SET `status` = " . $status . " WHERE `id` = " . $id . " ");
                echo "1";
            }
        } else {
            $this->db->query(" UPDATE `language` SET `status` = " . $status . " WHERE `id` = " . $id . " ");
            echo "1";
        }
    }

    public function update_language_default() {

        $id = $this->input->post('id');

        $this->db->where('id', $id);
        $this->db->where('status', 1);
        $data = $this->db->get('language')->result_array();

        if (!empty($data)) {
            $this->db->query("UPDATE language SET default_language = ''");
            $this->db->query(" UPDATE `language` SET `default_language` = 1 WHERE `id` = " . $id . " ");
            echo "1";
        } else {
            echo "0";
        }
    }

    public function change_language() {





        $language = (!empty($this->input->post('lg'))) ? $this->input->post('lg') : 'en';

        $tag = (!empty($this->input->post('tag'))) ? $this->input->post('tag') : 'ltr';



        $this->session->set_userdata(array('user_select_language' => $language));

        $this->session->set_userdata(array('tag' => $tag));
    }

    public function update_multi_language() {

        if ($this->input->post()) {
            

            $page_key = $this->input->post('page_key');
            

            if ($this->data['admin_id'] > 1) {
                $this->session->set_flashdata('message', '<p class="alert alert-danger">Permission Denied</p>');
                redirect(base_url() . 'app_page_list/' . $page_key . '');
            } else {


                $data = $this->input->post();




                foreach ($data as $row => $object) {
                    if (!empty($object)) {

                        foreach ($object as $key => $value) {

                            $this->db->where('language', $key);
                            $this->db->where('lang_key', $row);
                            $this->db->where('type', $value['type']);
                            $this->db->where('page_key', $page_key);

                            $record = $this->db->count_all_results('app_language_management');
                            


                            if ($record == 0) {
                                $array = array(
                                    'language' => $key,
                                    'lang_key' => $row,
                                    'lang_value' => $value['lang_value'],
                                    'placeholder' => $value['placeholder'],
                                    'validation1' => $value['validation1'],
                                    'validation2' => $value['validation2'],
                                    'validation3' => $value['validation3'],
                                    'type' => $value['type'],
                                    'page_key' => $page_key,
                                );

                                $this->db->insert('app_language_management', $array);
                            } else {

                                $this->db->where('language', $key);
                                $this->db->where('lang_key', $row);
                                $this->db->where('type', $value['type']);
                                $this->db->where('page_key', $page_key);


                                $array = array(
                                    'lang_value' => $value['lang_value'],
                                    'placeholder' => $value['placeholder'],
                                    'validation1' => $value['validation1'],
                                    'validation2' => $value['validation2'],
                                    'validation3' => $value['validation3'],
                                    'type' => $value['type'],
                                    'page_key' => $page_key,
                                );

                                $this->db->update('app_language_management', $array);
                            }
                        }
                    }
                }
            }
        }
        redirect(base_url() . 'app_page_list/' . $page_key . '');
    }
    
    public function AppKeyword() {
        $postdata = $this->input->post();
        $page_key = $this->input->post('page_key');
        
        $data=[
            'page_key' => $page_key,
            'lang_key' => $postdata['filed_name'],
            'lang_value' => $postdata['name'],
            'placeholder' => $postdata['placeholder'],
            'validation1' => $postdata['valide_1'],
            'validation2' => $postdata['valide_2'],
            'validation3' =>$postdata['valide_3'],
            'type' => 'App',
            'language' => 'en',
        ];
        
        $insertPage = $this->db->insert('app_language_management', $data);
        if (!empty($insertPage) && $insertPage != '') {
            $this->session->set_flashdata('success_message', 'Keyword added successfully');
            redirect(base_url() . "app_page_list");
        } else {
            $this->session->set_flashdata('error_message', 'Keyword added not successfully');
            redirect(base_url() . 'app_page_list/' . $page_key . '');
        }
    }

    
    


}
