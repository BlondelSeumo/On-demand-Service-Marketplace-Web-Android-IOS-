<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Login extends CI_Controller {



   public $data;



   public function __construct() {



        parent::__construct();
        error_reporting(0);

        $this->load->helper('custom_language');

        $lang = !empty($this->session->userdata('lang'))?$this->session->userdata('lang'):'en';

        $this->data['language_content'] = get_languages($lang);

        $this->web_validation_array = $this->data['language_content']['language']['web_validation'];

        $this->load->model('user_login_model','user_login');
        $this->data['view'] = 'user';

        $this->data['base_url'] = base_url();

        $user_id = $this->session->userdata('user_id');

        $this->data['user_id'] = $user_id;

				$this->load->helper('subscription_helper');

        $this->data['subscription_details'] = get_subscription_details(md5($user_id));


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



     if(empty($this->session->userdata('user_id')))

    {

  		$this->data['page'] = 'index';

  		$this->data['model'] = 'login';

      $this->load->vars($this->data);

  		$this->load->view('template');



    }else{

      redirect(base_url()."home");

    }

	}





  public function sociallogin()

  {

            $socialloginProfile=$this->input->post();



            $userData=array();



            if($socialloginProfile['auth']=='Google')

            {

                $userData['oauth_provider'] = $socialloginProfile['auth'];

                $userData['oauth_uid'] = $socialloginProfile['profileid'];

                $userData['first_name'] = $socialloginProfile['firstname'];

                $userData['last_name'] = $socialloginProfile['lastname'];

                $userData['email'] = $socialloginProfile['email'];

                $userData['picture'] = $socialloginProfile['profileurl'];

                $this->session->set_userdata('google_plus_data',$userData);

                $result = $this->user_login->google_plus($userData);

                 

               

            }



            if($socialloginProfile['auth']=='Facebook')

            {

                $userData['oauth_provider'] = $socialloginProfile['auth'];

                $userData['oauth_uid'] = $socialloginProfile['profileid'];

                $userData['first_name'] = $socialloginProfile['firstname'];

                $userData['last_name'] = $socialloginProfile['lastname'];

                $userData['email'] = $socialloginProfile['email'];

                $userData['picture'] = $socialloginProfile['profileurl'];

                $this->session->set_userdata('facebook_data',$userData);

                $result = $this->user_login->face_book($userData);

            }



           



         if(!empty($result)){

            $this->session->set_userdata('user_id',$result['user_id']);

            $this->session->set_userdata('latitude',$result['latitude']);

            $this->session->set_userdata('longitude',$result['longitude']);

            $this->session->set_userdata('profile_img',$result['profile_img']);

            $this->session->set_userdata('username',$result['username']);

            $this->session->set_userdata('register_through',$result['register_through']);

            

            $response=1;

          }else{

             $response=2;

          }



          echo $response;

  }



  public function signup()

	{

    if (empty($this->session->userdata['user_id']))

    {

  		$this->data['page'] = 'index';

  		$this->data['model'] = 'signup';

  		$this->load->vars($this->data);

  		$this->load->view('template');

    }

    else {

      redirect(base_url()."home");

    }

	}



  public function forgot_password()

	{

		$this->data['page'] = 'index';

		$this->data['model'] = 'forgot_password';

		$this->load->vars($this->data);

		$this->load->view('template');

	}





  public function check_forgot_username()

	{

		$isAvailable = 0;

	  $username = $this->input->post('username');

      $this->db->where("username = '" .$username."' OR email = '".$username."'");

	    $result = $this->db->count_all_results('users');



	    if($result > 0){

	        $isAvailable = 1;

	      }

	      else

	      {

	       $isAvailable = 0;

	     }

	   echo $isAvailable;

	}



  public function send_forgot_password(){

    $username = $this->input->post('username');

    $this->db->where("username = '" .$username."' OR email = '".$username."'");

	  $result = $this->db->get('users')->row_array();

    if(!empty($result)){

      $email = $result['email'];

      $upresult = $this->user_login->forgot_password($email);

			if($upresult == true){

        $message=$this->web_validation_array['lg12_password_reset_'];

        $this->session->set_flashdata('success_message',$message);

        redirect(base_url());

      }else{

        $message=$this->web_validation_array['lg12_invalid_usernam'];

        $this->session->set_flashdata('error_message',$message);

        redirect(base_url());

      }



    }



  }



	  public function change_password()

	  {

	        if ($this->uri->segment(3))

					$user_id = trim($this->uri->segment(3));

					$num = $this->user_login->forgot_count($user_id);

					if($num != 0)

					{

	        $userid = $user_id;

					if($this->input->post())

					{

						 $data['password'] = md5($this->input->post('new_password'));

						 $data['forgot'] = '';

						 $userid = $this->input->post('user_id');

             $this->user_login->change_password($data, $userid);

						 $message=$this->web_validation_array['lg12_the_password_up'].', '.$this->web_validation_array['lg12_please_login_ag'];

						 $this->session->set_flashdata('success_message',$message);

						 redirect(base_url());

					}

					}else{

						$message=$this->web_validation_array['lg12_the_email_has_b'];

						$this->session->set_flashdata('error_message',$message);

						 redirect(base_url());

					}

					if(!empty($userid))

	        {

						$this->data['user_id'] = $userid;

						$this->data['page'] = 'forgot_password';

						$this->data['model'] = 'forgot_password';

						$this->load->vars($this->data);

						$this->load->view('template');

					}

	    }



    public function crop_profile_img($prev_img='') {

		ini_set('max_execution_time', 3000);

		ini_set('memory_limit', '-1');

    if(!empty($prev_img))

    {

      $file_path = FCPATH.$prev_img;

					if(!file_exists($file_path)){

						unlink(FCPATH.$prev_img);

					}

    }

    $error_msg       = '';

		$av_src          = $this->input->post('avatar_src');

		$av_data         = json_decode($this->input->post('avatar_data'),true);

		$av_file         = $_FILES['avatar_file'];

		$src             = 'uploads/profile_img/'.$av_file['name'];

		$imageFileType   = pathinfo($src,PATHINFO_EXTENSION);

		$image_name 	= time().'.'.$imageFileType;

		$src2            = 'uploads/profile_img/temp/'.$image_name;

		move_uploaded_file($av_file['tmp_name'], $src2);



    $ref_path = '/uploads/profile_img/temp/';

		$image1          = $this->crop_images($image_name,$av_data,200,200,"/uploads/profile_img/",$ref_path);



		$rand = rand(100,999);

		$response = array(

			'state'  => 200,

			'message' => $error_msg,

			'result' => 'uploads/profile_img/'.$image_name,

			'img_name1' => $image_name

		);

		echo json_encode($response);

	}

  public function crop_ic_img($prev_img='') {

		ini_set('max_execution_time', 3000);

		ini_set('memory_limit', '-1');

    if(!empty($prev_img))

    {

      $file_path = FCPATH.$prev_img;

					if(!file_exists($file_path)){

						unlink(FCPATH.$prev_img);

					}

    }

    $error_msg       = '';

		$av_src          = $this->input->post('avatar_src');

		$av_data         = json_decode($this->input->post('avatar_data'),true);

		$av_file         = $_FILES['avatar_file'];

		$src             = 'uploads/ic_card_image/'.$av_file['name'];

		$imageFileType   = pathinfo($src,PATHINFO_EXTENSION);

		$image_name 	= time().'.'.$imageFileType;

		$src2            = 'uploads/ic_card_image/temp/'.$image_name;

		move_uploaded_file($av_file['tmp_name'], $src2);

    $ref_path = '/uploads/ic_card_image/temp/';

		$image1          = $this->crop_images($image_name,$av_data,300,300,"/uploads/ic_card_image/",$ref_path);



		$rand = rand(100,999);

		$response = array(

			'state'  => 200,

			'message' => $error_msg,

			'result' => 'uploads/ic_card_image/'.$image_name,

			'img_name1' => $image_name

		);

		echo json_encode($response);

	}



  public function crop_images($image_name,$av_data,$t_width,$t_height,$path,$ref_path) {

		$w                 = $av_data['width'];

		$h                 = $av_data['height'];

		$x1                = $av_data['x'];

		$y1                = $av_data['y'];

		list($imagewidth, $imageheight, $imageType) = getimagesize(FCPATH.$ref_path.$image_name);

		$imageType                                  = image_type_to_mime_type($imageType);

		$ratio             = ($t_width/$w);

		$nw                = ceil($w * $ratio);

		$nh                = ceil($h * $ratio);

		$newImage          = imagecreatetruecolor($nw,$nh);



		$backgroundColor = imagecolorallocate($newImage, 0, 0, 0);

		imagefill($newImage, 0, 0, $backgroundColor);

		$black = imagecolorallocate($newImage, 0, 0, 0);



		// Make the background transparent

		imagecolortransparent($newImage, $black);







		switch($imageType) {

			case "image/gif"  : $source = imagecreatefromgif(FCPATH.$ref_path.$image_name);

			break;

			case "image/pjpeg":

			case "image/jpeg" :

			case "image/jpg"  : $source = imagecreatefromjpeg(FCPATH.$ref_path.$image_name);

			break;

			case "image/png"  :

			case "image/x-png": $source = imagecreatefrompng(FCPATH.$ref_path.$image_name);

			break;

		}

		imagecopyresampled($newImage,$source,0,0,$x1,$y1,$nw,$nh,$w,$h);

			switch($imageType) {

			case "image/gif"  : imagegif($newImage,FCPATH.$path.$image_name);

			break;

			case "image/pjpeg":

			case "image/jpeg" :

			case "image/jpg"  : imagejpeg($newImage,FCPATH.$path.$image_name,100);

			break;

			case "image/png"  :

			case "image/x-png": imagepng($newImage,FCPATH.$path.$image_name);

			break;

		}

	}



  public function check_username()

  {

    $username = $this->input->post('username');

    $result = $this->user_login->check_username($username);

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



  public function check_email_address()

  {

    $email_addr = $this->input->post('email_addr');

    $result = $this->user_login->check_email_address($email_addr);

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


   public function check_phone()

  {

    $phone = $this->input->post('phone');

    $result = $this->user_login->check_phone($phone);

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



  public function create_new_user()

  {

    date_default_timezone_set('UTC');

    $data['username'] = $data['full_name'] = $this->input->post('username');

    $data['email'] = $this->input->post('email_addr');

    $data['mobile_no'] = $this->input->post('phone');

    $data['profile_img'] = (!empty($this->input->post('profile_image')))?"uploads/profile_img/" . $this->input->post('profile_image'):'';

    $data['ic_card_image'] = (!empty($this->input->post('ic_card_image')))?"uploads/ic_card_image/" . $this->input->post('ic_card_image'):'';

    $data['password'] = md5($this->input->post('password'));

    $data['role'] = 2;

    $data['is_active'] = 1;

    $data['verified'] = 1;

    $data['register_through'] = 1;

    $data['created'] = date('Y-m-d H:i:s');

    $result = $this->user_login->signup($data);

    if(!empty($result))

    {

      $this->session->set_flashdata('success_message',$this->web_validation_array['lg12_you_have_been_r']);

      echo 1;

    }

    else

    {

      $this->session->set_flashdata('error_message',$this->web_validation_array['lg12_something_wrong'].', '.$this->web_validation_array['lg12_please_try_agai']);

      echo 2;

    }

  }

   public function google_new_user()

  {

    date_default_timezone_set('UTC');

  	$data['username'] = $data['full_name'] = $this->input->post('username');

    $data['email'] = $this->input->post('email_addr');

    $data['mobile_no'] = $this->input->post('phone');

    $data['profile_img'] = (!empty($this->input->post('profile_image')))?"uploads/profile_img/" . $this->input->post('profile_image'):'';

    $data['role'] = 2;

    $data['is_active'] = 1;

    $data['verified'] = 1;

    $data['tokenid'] = $this->input->post('tokenid');

    $data['register_through'] = 3;

    $data['created'] = date('Y-m-d H:i:s');

  	$result = $this->user_login->signup($data);

  	if(!empty($result))

  	{

  		$this->session->set_flashdata('success_message',$this->web_validation_array['lg12_you_have_been_r']);

  		echo 1;

  	}

  	else

  	{

      $this->session->set_flashdata('error_message',$this->web_validation_array['lg12_something_wrong'].', '.$this->web_validation_array['lg12_please_try_agai']);

  		echo 2;

  	}

  }





  public function facebook_new_user()

  {

    date_default_timezone_set('UTC');

    $data['username'] = $data['full_name'] = $this->input->post('username');

    $data['email'] = $this->input->post('email_addr');

    $data['mobile_no'] = $this->input->post('phone');

    $data['profile_img'] = (!empty($this->input->post('profile_image')))?"uploads/profile_img/" . $this->input->post('profile_image'):'';

    $data['role'] = 2;

    $data['is_active'] = 1;

    $data['verified'] = 1;

    $data['tokenid'] = $this->input->post('tokenid');

    $data['register_through'] = 2;

    $data['created'] = date('Y-m-d H:i:s');

    $result = $this->user_login->signup($data);

    if(!empty($result))

    {

      $this->session->set_flashdata('success_message',$this->web_validation_array['lg12_you_have_been_r']);

      echo 1;

    }

    else

    {

      $this->session->set_flashdata('error_message',$this->web_validation_array['lg12_something_wrong'].', '.$this->web_validation_array['lg12_please_try_agai']);

      echo 2;

    }

  }



  public function is_valid_login()

	{

		$username = $this->input->post('username');

		$password = $this->input->post('password');

		$result = $this->user_login->is_valid_login($username,$password);

		if(!empty($result))

		{

			$this->session->set_userdata('user_id',$result['user_id']);

  		$this->session->set_userdata('latitude',$result['latitude']);

    	$this->session->set_userdata('longitude',$result['longitude']);

      $this->session->set_userdata('profile_img',$result['profile_img']);

    	$this->session->set_userdata('username',$result['username']);

    	$this->session->set_userdata('register_through',$result['register_through']);

			echo 1;

		}

	 else

		{

    $this->session->set_flashdata('error_message',$this->web_validation_array['lg12_wrong_login_cre']);

			echo 2;

		}

	}



 	public function logout()

	{

    if (!empty($this->session->userdata['user_id']))

    {

      $this->session->unset_userdata('user_id');

    }

    $this->session->set_flashdata('success_message',$this->web_validation_array['lg12_logged_out_succ']);

    		redirect(base_url());

  }

   public function signup_web()

  {

  }

}

