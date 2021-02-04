<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Service extends CI_Controller {

    public $data;

    public function __construct() {

        parent::__construct();
        error_reporting(0);
        $this->data['theme'] = 'user';
        $this->data['module'] = 'service';
        $this->data['page'] = '';
        $this->data['base_url'] = base_url();
        $this->session->keep_flashdata('error_message');
        $this->session->keep_flashdata('success_message');
        $this->load->helper('user_timezone_helper');
        $this->load->helper('push_notifications');
        $this->load->model('api_model', 'api');

        $this->load->model('service_model', 'service');
        $this->load->model('home_model', 'home');
        // Load pagination library 
        $this->load->library('ajax_pagination');
        // Per page limit 
        $this->perPage = 10;
        
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

    public function index() {
        $this->data['page'] = 'index';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function mail($value = '') {
        $this->load->library('email');
        $result = $this->email
                ->from('vignesh.s@dreamguys.co.in')
                ->reply_to('vignesh.s@dreamguys.co.in')    // Optional, an account where a human being reads.
                ->to('vignesh.s@dreamguys.co.in')
                ->subject('hai')
                ->message('asf')
                ->send();
    }

    public function add_service() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }

        if ($this->input->post('form_submit')) {
            $inputs = array();

            removeTag($this->input->post());
            $query = $this->db->query("select * from system_settings WHERE status = 1");
            $result = $query->result_array();
            if (!empty($result)) {
                foreach ($result as $data) {
                    if ($data['key'] == 'currency_option') {
                        $currency_option = $data['value'];
                    }
					 if ($data['key'] == 'map_key') {
                        $map_key = $data['map_key'];
                    }
                }
            }
            $config["upload_path"] = './uploads/services/';
            $config["allowed_types"] = '*';
            $this->load->library('upload', $config);
            $this->upload->initialize($config);

            $service_image = array();
            $thumb_image = array();
            $mobile_image = array();

            if ($_FILES["images"]["name"] != '') {
                for ($count = 0; $count < count($_FILES["images"]["name"]); $count++) {
                    $_FILES["file"]["name"] = 'full_' . time() . $_FILES["images"]["name"][$count];
                    $_FILES["file"]["type"] = $_FILES["images"]["type"][$count];
                    $_FILES["file"]["tmp_name"] = $_FILES["images"]["tmp_name"][$count];
                    $_FILES["file"]["error"] = $_FILES["images"]["error"][$count];
                    $_FILES["file"]["size"] = $_FILES["images"]["size"][$count];
                    if ($this->upload->do_upload('file')) {
                        $data = $this->upload->data();
                        $image_url = 'uploads/services/' . $data["file_name"];
                        $upload_url = 'uploads/services/';
                        $service_image[] = $this->image_resize(360, 220, $image_url, 'se_' . $data["file_name"], $upload_url);
                        $service_details_image[] = $this->image_resize(820, 440, $image_url, 'de_' . $data["file_name"], $upload_url);
                        $thumb_image[] = $this->image_resize(60, 60, $image_url, 'th_' . $data["file_name"], $upload_url);
                        $mobile_image[] = $this->image_resize(280, 160, $image_url, 'mo_' . $data["file_name"], $upload_url);
                    }
                }
            }
            
            

            $inputs['user_id'] = $this->session->userdata('id');
            $inputs['service_title'] = $this->input->post('service_title');
            $inputs['currency_code'] = $this->input->post('currency_code');;
            $inputs['service_sub_title'] = $this->input->post('service_sub_title');
            $inputs['category'] = $this->input->post('category');
            $inputs['subcategory'] = $this->input->post('subcategory');
            $inputs['service_location'] = $this->input->post('service_location');
            $inputs['service_latitude'] = $this->input->post('service_latitude');
            $inputs['service_longitude'] = $this->input->post('service_longitude');
            $inputs['service_amount'] = $this->input->post('service_amount');
            $inputs['about'] = $this->input->post('about');
            $inputs['service_image'] = implode(',', $service_image);
            $inputs['service_details_image'] = implode(',', $service_details_image);
            $inputs['thumb_image'] = implode(',', $thumb_image);
            $inputs['mobile_image'] = implode(',', $mobile_image);
            $inputs['created_at'] = date('Y-m-d H:i:s');
            $inputs['updated_at'] = date('Y-m-d H:i:s');
            
            
            $result = $this->service->create_service($inputs);

            if (count($_POST['service_offered']) > 0) {
                foreach ($_POST['service_offered'] as $key => $value) {
                    $service_data = array(
                        'service_id' => $result,
                        'service_offered' => $value);
                    $this->db->insert('service_offered', $service_data);
                }
            }
            $temp = count($service_image); //counting number of row's
            $service_image = $service_image;
            $service_details_image = $service_details_image;
            $thumb_image = $thumb_image;
            $mobile_image = $mobile_image;
            $service_id = $result;



            for ($i = 0; $i < $temp; $i++) {
                $image = array(
                    'service_id' => $service_id,
                    'service_image' => $service_image[$i],
                    'service_details_image' => $service_details_image[$i],
                    'thumb_image' => $thumb_image[$i],
                    'mobile_image' => $mobile_image[$i]
                );
                $serviceimage = $this->service->insert_serviceimage($image);
            }

            if ($serviceimage == true) {
                $this->session->set_flashdata('success_message', 'Service created successfully');
                redirect(base_url() . "my-services");
            } else {
                $this->session->set_flashdata('error_message', 'Service created failed');
                redirect(base_url() . "add-service");
            }
        }
        
        $this->data['map_key'] = $map_key;
		$this->data['page'] = 'add_service';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function edit_service() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }

        $service_id = $this->uri->segment('4');
        $this->data['page'] = 'edit_service';
        $this->data['model'] = 'service';
        $this->data['services'] = $services = $this->service->get_service_id($service_id);
        $this->data['serv_offered'] = $this->db->from('service_offered')->where('service_id', $services['id'])->get()->result_array();

        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function notification_view() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }
        $data = array();

        // Get record count 
        $conditions['returnType'] = 'count';

        $totalRec = $this->service->get_full_notification($conditions);
        // Pagination configuration 
        $config['target'] = '#dataList';
        $config['base_url'] = base_url('user/service/notificaitonAjaxPagination');
        $config['total_rows'] = $totalRec;
        $config['per_page'] = $this->perPage;

        // Initialize pagination library 
        $this->ajax_pagination->initialize($config);

        // Get records 
        $conditions = array(
            'limit' => $this->perPage
        );
        $this->data['notification_list'] = $notification_list = $this->service->get_full_notification($conditions);
        $this->data['page'] = 'user_notifications';
        $this->data['module'] = 'chat';
        $values = array();
        foreach ($notification_list as $key => $value) {
            $values[$key] = $value;
            $user_table = $this->db->select('id,name,profile_img,token,type')->
                            from('users')->
                            where('token', $value['sender'])->
                            get()->row();

            $provider_table = $this->db->select('id,name,profile_img,token,type')->
                            from('providers')->
                            where('token', $value['sender'])->
                            get()->row();

            if (!empty($user_table)) {
                $user_info = $user_table;
            } else {
                $user_info = $provider_table;
            }
            if (!empty($user_info) && isset($user_info)) {
                $values[$key]['profile_img'] = $user_info->profile_img;
            }
        }
        $token = $this->session->userdata('chat_token');
        $this->db->where_in('receiver', $token);
        $this->db->set('status', 0);
        $this->db->update('notification_table');
        $this->data['notification_list'] = $values;
        // Load the list page view 
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function notificaitonAjaxPagination() {
        $page = $this->input->post('page');
        if (!$page) {
            $offset = 0;
        } else {
            $offset = $page;
        }

        // Get record count 
        $conditions['returnType'] = 'count';
        $totalRec = $this->service->get_full_notification($conditions);

        // Pagination configuration 
        $config['target'] = '#dataList';
        $config['base_url'] = base_url('user/service/notificaitonAjaxPagination');
        $config['total_rows'] = $totalRec;
        $config['per_page'] = $this->perPage;

        // Initialize pagination library 
        $this->ajax_pagination->initialize($config);

        // Get records 
        $conditions = array(
            'start' => $offset,
            'limit' => $this->perPage
        );
        $this->data['notification_list'] = $notification_list = $this->service->get_full_notification($conditions);
        $values = array();
        foreach ($notification_list as $key => $value) {
            $values[$key] = $value;
            $user_table = $this->db->select('id,name,profile_img,token,type')->
                            from('users')->
                            where('token', $value['sender'])->
                            get()->row();

            $provider_table = $this->db->select('id,name,profile_img,token,type')->
                            from('providers')->
                            where('token', $value['sender'])->
                            get()->row();

            if (!empty($user_table)) {
                $user_info = $user_table;
            } else {
                $user_info = $provider_table;
            }
            if (!empty($user_info->profile_img)) {
                $values[$key]['profile_img'] = $user_info->profile_img;
            } else {
                $values[$key]['profile_img'] = '';
            }
        }
        $this->data['notification_list'] = $values;
        // Load the data list view 
        $this->load->view('user/chat/ajax-data', $this->data, false);
    }

    public function update_service() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }
        removeTag($this->input->post());
        $service = implode(',', $this->input->post('service_offered'));
        $service_offered = json_encode(array($service));

        $inputs = array();

        $config["upload_path"] = './uploads/services/';
        $config["allowed_types"] = '*';
        $this->load->library('upload', $config);
        $this->upload->initialize($config);

        $service_image = array();
        $thumb_image = array();
        $mobile_image = array();

        if (isset($_FILES["images"]) && !empty($_FILES["images"]['name'][0])) {
            $count = count($_FILES["images"]);

            for ($count = 0; $count < count($_FILES["images"]["name"]); $count++) {
                $profile_count = $this->db->where('service_id', $this->input->post('service_id'))->from('services_image')->count_all_results();
                if ($profile_count < 10) {
                    $_FILES["file"]["name"] = 'full_' . time() . $_FILES["images"]["name"][$count];
                    $_FILES["file"]["type"] = $_FILES["images"]["type"][$count];
                    $_FILES["file"]["tmp_name"] = $_FILES["images"]["tmp_name"][$count];
                    $_FILES["file"]["error"] = $_FILES["images"]["error"][$count];
                    $_FILES["file"]["size"] = $_FILES["images"]["size"][$count];
                    if ($this->upload->do_upload('file')) {
                        $data = $this->upload->data();
                        $image_url = 'uploads/services/' . $data["file_name"];
                        $upload_url = 'uploads/services/';
                        $service_image[] = $this->image_resize(360, 220, $image_url, 'se_' . $data["file_name"], $upload_url);
                        $service_details_image[] = $this->image_resize(820, 440, $image_url, 'de_' . $data["file_name"], $upload_url);
                        $thumb_image[] = $this->image_resize(60, 60, $image_url, 'th_' . $data["file_name"], $upload_url);
                        $mobile_image[] = $this->image_resize(280, 160, $image_url, 'mo_' . $data["file_name"], $upload_url);
                    }
                }
            }
        }

        $inputs['service_image'] = implode(',', $service_image);
        $inputs['service_details_image'] = implode(',', $service_details_image);
        $inputs['thumb_image'] = implode(',', $thumb_image);
        $inputs['mobile_image'] = implode(',', $mobile_image);

        $inputs['service_title'] = $this->input->post('service_title');
        $inputs['service_sub_title'] = $this->input->post('service_sub_title');
        $inputs['category'] = $this->input->post('category');
        $inputs['subcategory'] = $this->input->post('subcategory');
        $inputs['service_location'] = $this->input->post('service_location');
        $inputs['service_latitude'] = $this->input->post('service_latitude');
        $inputs['service_longitude'] = $this->input->post('service_longitude');
        $inputs['service_amount'] = $this->input->post('service_amount');

        $inputs['about'] = $this->input->post('about');
        $inputs['currency_code'] = $this->input->post('currency_code');


        $inputs['updated_at'] = date('Y-m-d H:i:s');
        $service_image = implode(',', $service_image);
        $service_details_image = implode(',', $service_details_image);
        $thumb_image = implode(',', $thumb_image);
        $mobile_image = implode(',', $mobile_image);

        $sql = "update services set service_image='" . $service_image . "',service_details_image='" . $service_details_image . "',thumb_image='" . $thumb_image . "',mobile_image='" . $mobile_image . "', service_title='" . $this->input->post('service_title') . "',service_sub_title='" . $this->input->post('service_sub_title') . "',currency_code='".$this->input->post('currency_code')."',category='" . $this->input->post('category') . "',subcategory='" . $this->input->post('subcategory') . "',service_location='" . $this->input->post('service_location') . "',service_latitude='" . $this->input->post('service_latitude') . "',service_longitude='" . $this->input->post('service_longitude') . "',service_amount='" . $this->input->post('service_amount') . "',service_offered= '" . $service_offered . "',about='" . $this->input->post('about') . "',updated_at='" . date('Y-m-d H:i:s') . "' where id='" . $_POST['service_id'] . "'";
        $result = $this->db->query($sql);
        if (count($_POST['service_offered']) > 0) {
            $this->db->where('service_id', $this->input->post('service_id'))->delete('service_offered');
            foreach ($_POST['service_offered'] as $key => $value) {
                $service_data = array(
                    'service_id' => $this->input->post('service_id'),
                    'service_offered' => $value);
                $this->db->insert('service_offered', $service_data);
            }
        }

        if (!empty($service_image)) {
            $temp = count(explode(',', $service_image));
            $service_image = explode(',', $service_image);
            $service_details_image = explode(',', $service_details_image);
            $thumb_image = explode(',', $thumb_image);
            $mobile_image = explode(',', $mobile_image);
            $service_id = $this->input->post('service_id');



            for ($i = 0; $i < $temp; $i++) {
                $image = array(
                    'service_id' => $service_id,
                    'service_image' => $service_image[$i],
                    'service_details_image' => $service_details_image[$i],
                    'thumb_image' => $thumb_image[$i],
                    'mobile_image' => $mobile_image[$i]
                );
                $serviceimage = $this->service->insert_serviceimage($image);
            }
        }

        if ($result) {
            $this->session->set_flashdata('success_message', 'Service Updated successfully');
            redirect(base_url() . 'my-services');
        } else {
            $this->session->set_flashdata('error_message', 'Something Wents to Wrong...!');
            redirect(base_url() . 'my-services');
        }
    }

    public function sevice_images($value = '') {
        if (!empty($_FILES)) {

            $config["upload_path"] = './uploads/services_dummy/';
            $config["allowed_types"] = '*';
            $this->load->library('upload', $config);
            $this->upload->initialize($config);

            $service_image = array();
            $thumb_image = array();
            $mobile_image = array();

            if (isset($_FILES["images0"]) && !empty($_FILES["images0"]['name'][0])) {
                $count = count($_FILES);
                $i = 0;
                for ($count = 0; $count < count($_FILES); $count++) {
                    $j = $i++;
                    $_FILES["file"]["name"] = 'full_' . time() . $_FILES["images" . $j]["name"][$count];
                    $_FILES["file"]["type"] = $_FILES["images" . $j]["type"][$count];
                    $_FILES["file"]["tmp_name"] = $_FILES["images" . $j]["tmp_name"][$count];
                    $_FILES["file"]["error"] = $_FILES["images" . $j]["error"][$count];
                    $_FILES["file"]["size"] = $_FILES["images" . $j]["size"][$count];
                    if ($this->upload->do_upload('file')) {
                        $data = array('service_id' => $_POST['service_id'],
                            'user_id' => $this->session->userdata('id'),
                            'image_url' => $config["upload_path"] . $_FILES["file"]
                        );
                        $this->db->insert('service_dummy_images', $data);
                    }
                }
            }
        }
    }

    public function delete_service() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }
        $s_id = $this->input->post('s_id');

        $inputs['status'] = '2';
        $WHERE = array('id' => $s_id);
        $result = $this->service->update_service($inputs, $WHERE);
        if ($result) {
            $message = 'Service InActivate successfully';
            $this->session->set_flashdata('success_message', $message);
            echo 1;
        } else {
            $message = 'Something went wrong.Please try again later.';
            $this->session->set_flashdata('error_message', $message);
            echo 2;
        }
    }

    public function delete_inactive_service() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }
        $s_id = $this->input->post('s_id');

        $inputs['status'] = '0';
        $WHERE = array('id' => $s_id);
        $result = $this->service->update_service($inputs, $WHERE);
        if ($result) {
            $message = 'Service deleted successfully';
            $this->session->set_flashdata('success_message', $message);
            echo 1;
        } else {
            $message = 'Something went wrong.Please try again later.';
            $this->session->set_flashdata('error_message', $message);
            echo 2;
        }
    }

    public function delete_active_service() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }
        $s_id = $this->input->post('s_id');

        $inputs['status'] = '1';
        $WHERE = array('id' => $s_id);
        $result = $this->service->update_service($inputs, $WHERE);
        if ($result) {
            $message = 'Service Activate successfully';
            $this->session->set_flashdata('success_message', $message);
            echo 1;
        } else {
            $message = 'Something went wrong.Please try again later.';
            $this->session->set_flashdata('error_message', $message);
            echo 2;
        }
    }

    public function my_services() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }
        $this->data['page'] = 'my_service';
        $this->data['services'] = $this->service->get_service();
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function featured_services() {
        $this->data['page'] = 'featured_services';
        $this->data['services'] = $this->service->featured_service();
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function popular_services() {
        $this->data['page'] = 'popular_services';
        $this->data['services'] = $this->service->popular_service();
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function update_booking() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }
        $this->data['page'] = 'update_booking';
        $this->data['services'] = $this->service->get_service();
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function user_bookingstatus() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }
        $this->data['page'] = 'user_bookingstatus';
        $this->data['services'] = $this->service->get_service();
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function update_bookingstatus() {
        extract($_POST);
        if (empty($this->session->userdata('id'))) {
            echo "3";
            return false;
        }
        if (!empty($_POST['review'])) {
            $book_details['reason'] = $_POST['review'];
        }
        $book_details['status'] = $this->input->post('status');
        $book_details['id'] = $this->input->post('booking_id');
        $book_details['updated_on'] = (date('Y-m-d H:i:s'));
        if (!empty($this->input->post('booking_id'))) {
            $old_booking_status = $this->db->where('id', $this->input->post('booking_id'))->get('book_service')->row();
            if ($old_booking_status->status == 5 || $old_booking_status->status == 7) {
                $message = 'Something went wrong.User Cancel the Service.';
                echo "2";
                exit;
            }
        }
        $WHERE = array('id' => $this->input->post('booking_id'));

        $result = $this->service->update_bookingstatus($book_details, $WHERE);
        if ($result) {
            $message = 'Booking updated successfully';

            if ($book_details['status'] == 2) {

                $token = $this->session->userdata('chat_token');


                $this->send_push_notification($token, $book_details['id'], 2, ' Have Inprogress The Service');
            }

            if ($book_details['status'] == 3) {
                $token = $this->session->userdata('chat_token');
                $this->send_push_notification($token, $book_details['id'], 2, ' Have Completed The Service');
            }


            if ($book_details['status'] == 7) {
                $token = $this->session->userdata('chat_token');
                $this->send_push_notification($token, $book_details['id'], 2, ' Have Cancelled The Service');
            }
            echo "1";
        } else {
            $message = 'Something went wrong.Please try again later.';
            echo "2";
        }
    }

    public function update_status_user() {
        extract($_POST);
        if (empty($this->session->userdata('id'))) {
            echo "3";
        }

        $book_details['reason'] = $this->input->post('review');
        $book_details['status'] = $this->input->post('status');
        $book_details['id'] = $this->input->post('booking_id');
        $book_details['updated_on'] = (date('Y-m-d H:i:s'));

        if (!empty($this->input->post('booking_id'))) {
            $old_booking_status = $this->db->where('id', $this->input->post('booking_id'))->get('book_service')->row();

            if ($old_booking_status->status == 5 || $old_booking_status->status == 7) {
                echo '2';
                exit;
            }
        }

        $WHERE = array('id' => $this->input->post('booking_id'));

        $result = $this->service->update_bookingstatus($book_details, $WHERE);
        if ($result) {
            $message = 'Booking updated successfully';

            if ($book_details['status'] == 6) {

                $token = $this->session->userdata('chat_token');

                $history = $this->api->user_accept_history_flow($this->input->post('booking_id'));
                

                $this->send_push_notification($token, $book_details['id'], 1, ' Have Accepted The Service');
            }

            if ($book_details['status'] == 5) {

                $token = $this->session->userdata('chat_token');
                $this->send_push_notification($token, $book_details['id'], 1, ' Have Rejected The Service');
            }

            if ($book_details['status'] == 7) {

                $token = $this->session->userdata('chat_token');

                $this->send_push_notification($token, $book_details['id'], 1, ' Has Rejected The Service');
            }

            echo "1";
        } else {
            echo '2';
        }
    }

    public function book_service() {
		
		$query = $this->db->query("select * from system_settings WHERE status = 1");
		$result = $query->result_array();
		if (!empty($result)) {
			foreach ($result as $data) {
				 if ($data['key'] == 'map_key') {
					$map_key = $data['map_key'];
				}
			}
		}
        $this->data['map_key'] = $map_key;
        $this->data['page'] = 'book_service';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function service_availability() {

        $booking_date = $this->input->post('date');
        $provider_id = $this->input->post('provider_id');
        $service_id = $this->input->post('service_id');

        $timestamp = strtotime($booking_date);
        $day = date('D', $timestamp);
        $provider_details = $this->service->provider_hours($provider_id);


        $availability_details = json_decode($provider_details['availability'], true);


        $alldays = false;
        foreach ($availability_details as $details) {

            if (isset($details['day']) && $details['day'] == 0) {

                if (isset($details['from_time']) && !empty($details['from_time'])) {

                    if (isset($details['to_time']) && !empty($details['to_time'])) {
                        $from_time = $details['from_time'];
                        $to_time = $details['to_time'];
                        $alldays = true;
                        break;
                    }
                }
            }
        }

        if ($alldays == false) {


            if ($day == 'Mon') {
                $weekday = '1';
            } elseif ($day == 'Tue') {
                $weekday = '2';
            } elseif ($day == 'Wed') {
                $weekday = '3';
            } elseif ($day == 'Thu') {
                $weekday = '4';
            } elseif ($day == 'Fri') {
                $weekday = '5';
            } elseif ($day == 'Sat') {
                $weekday = '6';
            } elseif ($day == 'Sun') {
                $weekday = '7';
            } elseif ($day == '0') {
                $weekday = '0';
            }


            foreach ($availability_details as $availability) {

                if ($weekday == $availability['day'] && $availability['day'] != 0) {

                    $availability_day = $availability['day'];
                    $from_time = $availability['from_time'];
                    $to_time = $availability['to_time'];
                }
            }
        }

        if (!empty($from_time)) {
            $temp_start_time = $from_time;
            $temp_end_time = $to_time;
        } else {
            $temp_start_time = '';
            $temp_end_time = '';
        }


        $start_time_array = '';
        $end_time_array = '';


        $timestamp_start = strtotime($temp_start_time);
        $timestamp_end = strtotime($temp_end_time);

        $timing_array = array();

        $counter = 1;

        $from_time_railwayhrs = date('G:i:s', ($timestamp_start));
        $to_time_railwayhrs = date('G:i:s', ($timestamp_end));

        $timestamp_start_railwayhrs = strtotime($from_time_railwayhrs);
        $timestamp_end_railwayhrs = strtotime($to_time_railwayhrs);


        $i = 1;
        while ($timestamp_start_railwayhrs < $timestamp_end_railwayhrs) {

            $temp_start_time_ampm = date('G:i:s', ($timestamp_start_railwayhrs));
            $temp_end_time_ampm = date('G:i:s', (($timestamp_start_railwayhrs) + 60 * 60 * 1));

            $timestamp_start_railwayhrs = strtotime($temp_end_time_ampm);

            $timing_array[] = array('id' => $i, 'start_time' => $temp_start_time_ampm, 'end_time' => $temp_end_time_ampm);

            if ($counter > 24) {
                break;
            }

            $counter += 1;
            $i++;
        }


        // Booking availability


        $service_date = $booking_date;



        $booking_count = $this->service->get_bookings($service_date, $service_id);



        $new_timingarray = array();

        if (is_array($booking_count) && empty($booking_count)) {
            $new_timingarray = $timing_array;
        } elseif (is_array($booking_count) && $booking_count != '') {
            foreach ($timing_array as $timing) {
                $match_found = false;

                $explode_st_time = explode(':', $timing['start_time']);
                $explode_value = $explode_st_time[0];

                $explode_endtime = explode(':', $timing['end_time']);
                $explode_endval = $explode_endtime[0];


                if (strlen($explode_value) == 1) {
                    $timing['start_time'] = "0" . $explode_st_time[0] . ":" . $explode_st_time[1] . ":" . $explode_st_time[2];
                }

                if (strlen($explode_endval) == 1) {
                    $timing['end_time'] = "0" . $explode_endtime[0] . ":" . $explode_endtime[1] . ":" . $explode_endtime[2];
                }

                foreach ($booking_count as $bookings) {


                    if ($timing['start_time'] == $bookings['from_time'] && $timing['end_time'] == $bookings['to_time']) {


                        $match_found = true;
                        break;
                    }
                }

                if ($match_found == false) {
                    $new_timingarray[] = array('start_time' => $timing['start_time'], 'end_time' => $timing['end_time']);
                }
            }
        }

        $new_timingarray = array_filter($new_timingarray);

        if (!empty($new_timingarray)) {
            $i = 1;
            foreach ($new_timingarray as $booked_time) {

                $re = strtotime($booked_time['start_time']);
                $re1 = strtotime($booked_time['end_time']);
                date_default_timezone_set('Asia/Kolkata');
                if (date('Y-m-d', strtotime($_POST['date'])) == date('Y-m-d')) {
                    $current_time = strtotime(date('H:i:s'));
                    if (strtotime($booked_time['start_time']) > $current_time) {

                        $st_time = date('h:i A', strtotime($booked_time['start_time']));
                        $end_time = date('h:i A', strtotime($booked_time['end_time']));
                    } else {
                        $st_time = '';
                        $end_time = '';
                    }
                } else {

                    $st_time = date('h:i A', strtotime($booked_time['start_time']));
                    $end_time = date('h:i A', strtotime($booked_time['end_time']));
                }


                if (!empty($st_time)) {
                    $time['start_time'] = $st_time;
                    $time['end_time'] = $end_time;
                    $service_availability[] = $time;
                    $i++;
                }
            }
        } else {
            $service_availability = '';
        }
        if (!isset($service_availability)) {
            $service_availability = '';
        }

        echo json_encode($service_availability);
    }

    public function booking() {
        removeTag($this->input->post());
        $timestamp_from = strtotime($this->input->post('from_time'));
        $timestamp_to = strtotime($this->input->post('to_time'));

        $charges_array = array();

        $amount = $this->input->post['service_amount'];
        $amount = ($amount * 100);
        $charges_array['amount'] = $amount;
        $charges_array['currency'] = 'USD';
        $charges_array['description'] = $this->input->post['notes'];



        $user_post_data['currency_code'] = $this->input->post('currency_code');;
        $user_post_data['service_id'] = $this->input->post('service_id');
        $user_post_data['provider_id'] = $this->input->post('provider_id');
        $user_post_data['service_date'] = date('Y-m-d', strtotime($this->input->post('booking_date')));
        $user_post_data['user_id'] = $this->session->userdata('id');
        $user_post_data['amount'] = $this->input->post('service_amount');
        $user_post_data['request_date'] = date('Y-m-d H:i:s');
        $user_post_data['request_time'] = time();
        $user_post_data['from_time'] = date('G:i:s', ($timestamp_from));
        $user_post_data['to_time'] = date('G:i:s', ($timestamp_to));
        $user_post_data['location'] = $this->input->post('service_location');
        $user_post_data['latitude'] = $this->input->post('service_latitude');
        $user_post_data['longitude'] = $this->input->post('service_longitude');
        $user_post_data['notes'] = $this->input->post('notes');


        
        $insert_booking = $this->service->insert_booking($user_post_data);
    }

    public function user_dashboard() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }

        $user_id = $this->session->userdata('id');
        $this->data['all_bookings'] = $this->home->get_bookinglist_user($user_id);
        $this->data['completed_bookings'] = $this->home->completed_bookinglist_user($user_id);
        $this->data['inprogress_bookings'] = $this->home->inprogress_bookinglist_user($user_id);
        $this->data['accepted_bookings'] = $this->home->accepted_bookinglist_user($user_id);
        $this->data['cancelled_bookings'] = $this->home->cancelled_bookinglist_user($user_id);
        $this->data['cancelled_bookings'] = $this->home->cancelled_bookinglist_user($user_id);
        $this->data['rejected_bookings'] = $this->home->rejected_bookinglist_user($user_id);
        $this->data['page'] = 'user_dashboard';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function provider_dashboard() {

        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }

        $this->data['page'] = 'provider_dashboard';
        $provider_id = $this->session->userdata('id');
        $this->data['all_bookings'] = $this->home->get_bookinglist($provider_id);
        $this->data['completed_bookings'] = $this->home->completed_bookinglist($provider_id);
        $this->data['inprogress_bookings'] = $this->home->inprogress_bookinglist($provider_id);
        $this->data['cancelled_bookings'] = $this->home->cancelled_bookinglist($provider_id);
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function booking_details() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }
        $this->data['page'] = 'booking_details';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function booking_details_user() {
        if (empty($this->session->userdata('id'))) {
            redirect(base_url());
        }

        $this->data['page'] = 'booking_details_user';
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function get_category() {
        $this->db->where('status', 1);
        $query = $this->db->get('categories');
        $result = $query->result();
        $data = array();
        foreach ($result as $r) {
            $data['value'] = $r->id;
            $data['label'] = $r->category_name;
            $json[] = $data;
        }
        echo json_encode($json);
    }

    public function get_subcategory() {
        $this->db->where('status', 1);
        $this->db->where('category', $_POST['id']);
        $query = $this->db->get('subcategories');
        $result = $query->result();
        $data = array();
        if (!empty($result)) {
            foreach ($result as $r) {
                $data['value'] = $r->id;
                $data['label'] = $r->subcategory_name;
                $json[] = $data;
            }
        } else {
            $this->db->insert('subcategories', ['category' => $_POST['id'], 'subcategory_name' => "Others", 'status' => 1]);
            $data['value'] = $this->db->insert_id();
            $data['label'] = 'Others';
            $json[] = $data;
        }
        echo json_encode($json);
    }

    public function image_resize($width = 0, $height = 0, $image_url, $filename, $upload_url) {

        $source_path = base_url() . $image_url;
        list($source_width, $source_height, $source_type) = getimagesize($source_path);
        switch ($source_type) {
            case IMAGETYPE_GIF:
                $source_gdim = imagecreatefromgif($source_path);
                break;
            case IMAGETYPE_JPEG:
                $source_gdim = imagecreatefromjpeg($source_path);
                break;
            case IMAGETYPE_PNG:
                $source_gdim = imagecreatefrompng($source_path);
                break;
        }

        $source_aspect_ratio = $source_width / $source_height;
        $desired_aspect_ratio = $width / $height;

        if ($source_aspect_ratio > $desired_aspect_ratio) {
            /*
             * Triggered when source image is wider
             */
            $temp_height = $height;
            $temp_width = (int) ($height * $source_aspect_ratio);
        } else {
            /*
             * Triggered otherwise (i.e. source image is similar or taller)
             */
            $temp_width = $width;
            $temp_height = (int) ($width / $source_aspect_ratio);
        }

        /*
         * Resize the image into a temporary GD image
         */

        $temp_gdim = imagecreatetruecolor($temp_width, $temp_height);
        imagecopyresampled(
                $temp_gdim, $source_gdim, 0, 0, 0, 0, $temp_width, $temp_height, $source_width, $source_height
        );

        /*
         * Copy cropped region from temporary image into the desired GD image
         */

        $x0 = ($temp_width - $width) / 2;
        $y0 = ($temp_height - $height) / 2;
        $desired_gdim = imagecreatetruecolor($width, $height);
        imagecopy(
                $desired_gdim, $temp_gdim, 0, 0, $x0, $y0, $width, $height
        );

        /*
         * Render the image
         * Alternatively, you can save the image in file-system or database
         */

        $image_url = $upload_url . $filename;

        imagepng($desired_gdim, $image_url);

        return $image_url;

        /*
         * Add clean-up code here
         */
    }

    /* push notification */

    public function send_push_notification($token, $service_id, $type, $msg = '') {


        $data = $this->api->get_book_info($service_id);
        if (!empty($data)) {
            if ($type == 1) {
                $device_tokens = $this->api->get_device_info_multiple($data['provider_id'], 1);
            } else {
                $device_tokens = $this->api->get_device_info_multiple($data['user_id'], 2);
            }

            if ($type == 2) {
                $user_info = $this->api->get_user_info($data['user_id'], $type);

                $name = $this->api->get_user_info($data['provider_id'], 1);
            } else {
                $name = $this->api->get_user_info($data['user_id'], 2);

                $user_info = $this->api->get_user_info($data['provider_id'], $type);
            }



            /* insert notification */
            $msg = ucfirst($name['name']) . ' ' . strtolower($msg);

            if (!empty($user_info['token'])) {
                $this->api->insert_notification($token, $user_info['token'], $msg);
            }

            $title = $data['service_title'];


            if (!empty($device_tokens)) {
                foreach ($device_tokens as $key => $device) {
                    if (!empty($device['device_type']) && !empty($device['device_id'])) {

                        if (strtolower($device['device_type']) == 'android') {

                            $notify_structure = array(
                                'title' => $title,
                                'message' => $msg,
                                'image' => 'test22',
                                'action' => 'test222',
                                'action_destination' => 'test222',
                            );

                            sendFCMMessage($notify_structure, $device['device_id']);
                        }

                        if (strtolower($device['device_type'] == 'ios')) {
                            $notify_structure = array(
                                'alert' => $msg,
                                'sound' => 'default',
                                'badge' => 0,
                            );


                            sendApnsMessage($notify_structure, $device['device_id']);
                        }
                    }
                }
            }


            /* apns push notification */
        } else {
            $this->token_error();
        }
    }

    public function get_state_details() {
        if (!empty($_POST['id'])) {
            $state = $this->db->where('country_id', $_POST['id'])->from('state')->get()->result_array();
            if (!empty($state)) {
                echo json_encode($state);
                exit;
            } else {
                $state = [];
                echo json_encode($state);
                exit;
            }
        }
    }

    public function get_city_details() {
        if (!empty($_POST['id'])) {
            $state = $this->db->where('state_id', $_POST['id'])->from('city')->get()->result_array();
            if (!empty($state)) {
                echo json_encode($state);
                exit;
            } else {
                $state = [];
                echo json_encode($state);
                exit;
            }
        }
    }

    public function check_service_title() {
        $user_data = $this->input->post();
        $input['service_title'] = $user_data['service_title'];
        $service_count = $this->db->where('service_title', $input['service_title'])->count_all_results('services');
        if ($service_count > 0) {
            $isAvailable = FALSE;
        } else {
            $isAvailable = TRUE;
        }
        echo json_encode(
                array(
                    'valid' => $isAvailable
        ));
    }

}
