<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Profile extends CI_Controller {

    public $data;

    public function __construct() {

        parent::__construct();
        $this->load->model('admin_model', 'admin');
        $this->data['theme'] = 'admin';
        $this->data['model'] = 'profile';
        $this->data['base_url'] = base_url();
        $this->data['user_role'] = $this->session->userdata('role');
    }

    public function index() {
        $this->data['page'] = 'profile';
        $this->data['details'] = $this->admin->admin_details($this->session->userdata['admin_id']);
        $this->load->vars($this->data);
        $this->load->view($this->data['theme'] . '/template');
    }

    public function check_password() {
        $isAvailable = 0;
        $current_password = $this->input->post('current_password');
        $admin_id = $this->session->userdata('admin_id');
        $query = $this->db->query("SELECT `password` FROM `administrators` WHERE `user_id` = $admin_id");
        $result = $query->row_array();

        if (!empty($result)) {
            if ($result['password'] == md5($current_password)) {
                $isAvailable = TRUE;
            } else {
                $isAvailable = FALSE;
            }
        } else {
            $isAvailable = FALSE;
        }
        echo json_encode(
                array(
                    'valid' => $isAvailable
        ));
    }

	public function check_admin_mail()
	{ 
		$email = $this->input->post('email');
		$admin_id = $this->session->userdata('admin_id');
		$result = $this->admin->check_admin_emailbyid($email,$admin_id);
		
		//print_r($result);exit;

		if(!empty($result))
		{
			echo 1;
		}
	 else
		{
    //$this->session->set_flashdata('error_message','Email ID Not Exist...!');
			echo 2;
		}
	}
	
	

    public function update_profile() {
        $profile_image = $this->input->post('profile_img');
        //print_r($profile_image);
        //exit;
        if (!empty($profile_image)) {
            $data['profile_img'] = "uploads/profile_img/" . $profile_image;
		}
		
		
		$email = $this->input->post('adminmail');
		$admin_id = $this->session->userdata('admin_id');
		$result = $this->admin->check_admin_emailbyid($email,$admin_id);
		if(empty($result))
		{
		if($this->input->post('adminmail')!=''){
            $data['email'] = $this->input->post('adminmail');
            $result = $this->admin->update_profile($data);
            if ($result) {
				 if (!empty($profile_image)) {
                $this->session->set_userdata('admin_profile_img', $data['profile_img']);
				 }
                $this->session->set_flashdata('success_message', 'Your profile updated successfully.');
                echo 1;
            } else {
                $this->session->set_flashdata('error_message', 'Something went wrong, Please try again.');
                echo 2;
            }
        } else {
            $this->session->set_flashdata('error_message', 'Something went wrong, Please try again.');
            echo 2;
        }
		}
		else
		{
			 $this->session->set_flashdata('error_message', 'Email ID already exist...!');
            echo 2;
		}
    }

    public function change_password() {
        if ($this->input->post()) {
            removeTag($this->input->post());
            $user_id = $this->session->userdata('admin_id');
            $confirm_password = $this->input->post('confirm_password');
            $current_password = $this->input->post('current_password');

            $result = $this->admin->change_password($user_id, $confirm_password, $current_password);

            if ($result == 1) {
                $this->session->set_flashdata('success_message', 'Password changed successfully...');
            } else {
                $this->session->set_flashdata('message', 'Something is wrong, please try again later...');
            }
        }
        redirect(base_url('admin-profile'));
    }

    public function crop_profile_img($prev_img = '') {

        ini_set('max_execution_time', 3000);

        ini_set('memory_limit', '-1');

        if (!empty($prev_img)) {

            $file_path = FCPATH . $prev_img;

            if (!file_exists($file_path)) {

                unlink(FCPATH . $prev_img);
            }
        }

        $error_msg = '';

        $av_src = $this->input->post('avatar_src');

        $av_data = json_decode($this->input->post('avatar_data'), true);

        $av_file = $_FILES['avatar_file'];

        $src = 'uploads/profile_img/' . $av_file['name'];

        $imageFileType = pathinfo($src, PATHINFO_EXTENSION);

        $image_name = time() . '.' . $imageFileType;

        $src2 = 'uploads/profile_img/temp/' . $image_name;

        move_uploaded_file($av_file['tmp_name'], $src2);
        $admin_updates = $this->db->where('user_id=', $this->session->userdata('admin_id'))->update('administrators', ['profile_img' => $src2]);

        $ref_path = '/uploads/profile_img/temp/';

        $image1 = $this->crop_images($image_name, $av_data, 200, 200, "/uploads/profile_img/", $ref_path);



        $rand = rand(100, 999);

        $response = array(
            'state' => 200,
            'message' => $error_msg,
            'result' => 'uploads/profile_img/' . $image_name,
            'success' => 'Y',
            'img_name1' => $image_name
        );

        echo json_encode($response);
    }

    public function crop_images($image_name, $av_data, $t_width, $t_height, $path, $ref_path) {

        $w = $av_data['width'];

        $h = $av_data['height'];

        $x1 = $av_data['x'];

        $y1 = $av_data['y'];

        list($imagewidth, $imageheight, $imageType) = getimagesize(FCPATH . $ref_path . $image_name);

        $imageType = image_type_to_mime_type($imageType);

        $ratio = ($t_width / $w);

        $nw = ceil($w * $ratio);

        $nh = ceil($h * $ratio);

        $newImage = imagecreatetruecolor($nw, $nh);



        $backgroundColor = imagecolorallocate($newImage, 0, 0, 0);

        imagefill($newImage, 0, 0, $backgroundColor);

        $black = imagecolorallocate($newImage, 0, 0, 0);



        // Make the background transparent

        imagecolortransparent($newImage, $black);







        switch ($imageType) {

            case "image/gif" : $source = imagecreatefromgif(FCPATH . $ref_path . $image_name);

                break;

            case "image/pjpeg":

            case "image/jpeg" :

            case "image/jpg" : $source = imagecreatefromjpeg(FCPATH . $ref_path . $image_name);

                break;

            case "image/png" :

            case "image/x-png": $source = imagecreatefrompng(FCPATH . $ref_path . $image_name);

                break;
        }

        imagecopyresampled($newImage, $source, 0, 0, $x1, $y1, $nw, $nh, $w, $h);

        switch ($imageType) {

            case "image/gif" : imagegif($newImage, FCPATH . $path . $image_name);

                break;

            case "image/pjpeg":

            case "image/jpeg" :

            case "image/jpg" : imagejpeg($newImage, FCPATH . $path . $image_name, 100);

                break;

            case "image/png" :

            case "image/x-png": imagepng($newImage, FCPATH . $path . $image_name);

                break;
        }
    }

}
