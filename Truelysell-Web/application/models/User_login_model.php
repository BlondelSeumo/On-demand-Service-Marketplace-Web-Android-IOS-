<?php
if ( ! defined('BASEPATH')) exit('No direct script access allowed');
class User_login_model extends CI_Model
{

  public function __construct()
  {
    parent::__construct();
  }
  public function total_revenue(){
    $this->db->select('forgot');
    $this->db->where('user_id', 3);
    return $this->db->get('request_details')->row_array();
  }

  /*find user type*/
  public function get_user_type($mobile_no,$country_code){
    
     $user=$this->db->select('*')->from('users')->
                    where('country_code',$country_code)->
                    where('mobileno',$mobile_no)->
                    get()->row();

    $provider=$this->db->select('*')->from('providers')->
                    where('country_code',$country_code)->
                    where('mobileno',$mobile_no)->
                    get()->row();

    if(!empty($user)){
       return $user;
    }

    if(!empty($provider)){
       return $provider;
    }
    return '';
  }

  public function isset_mobile_otp($mobile_no,$country_code,$otp_data){
    $ret=$this->db->select('*')->from('mobile_otp')->
                    where('country_code',$country_code)->
                    where('mobile_number',$mobile_no)->
                    where('status',1)->
                    count_all_results();  
    if($ret > 0){
    /*update otp*/
      $this->db->where('country_code', $country_code);
      $this->db->where('mobile_number', $mobile_no);
      $this->db->where('status', 1);
      $save_otp=$this->db->update('mobile_otp', array('endtime'=>$otp_data['endtime'],'otp'=>$otp_data['otp'],'updated_on'=> utc_date_conversion(date('Y-m-d H:i:s'))));
    }else{
      $save_otp = $this->user_login->save_otp($otp_data);
    }

    if($save_otp){
    return 1;
    }else{
    return 2;
    
    }

  }

  public function is_valid_login($username,$password)
  {
    $password = md5($password);
    $this->db->select('username,user_id, latitude, longitude, profile_img, register_through');
    $this->db->from('users');
    $this->db->where("(username = '$username' OR email = '$username' OR mobile_no = '$username')");
    $this->db->where('password',$password);
    $this->db->where('register_through', 1);
    $this->db->where('role',2);
    $this->db->where('verified',1);
    $this->db->where('is_active',1);
    $result = $this->db->get()->row_array();
    return $result;
  }

    public function google_plus($inputs){

    $oauth_uid = $inputs['oauth_uid'];

    $this->db->select('username,user_id, latitude, longitude, profile_img, register_through');
    $this->db->from('users');
    $this->db->where('tokenid',$oauth_uid);
    $this->db->where('role',2);
    $this->db->where('verified',1);
    $this->db->where('is_active',1);
    $this->db->where('register_through',3);
    $result = $this->db->get()->row_array();
    return $result;
    }

    public function face_book($inputs){

    $oauth_uid = $inputs['oauth_uid'];

    $this->db->select('username,user_id, latitude, longitude, profile_img, register_through');
    $this->db->from('users');
    $this->db->where('tokenid',$oauth_uid);
    $this->db->where('role',2);
    $this->db->where('verified',1);
    $this->db->where('is_active',1);
    $this->db->where('register_through',2);
    $result = $this->db->get()->row_array();
    return $result;
    }

  public function check_username($username)
  {
    $this->db->select('*');
    $this->db->where('username', $username);
    $this->db->where('register_through', 1);
    $this->db->from('users');
    $result = $this->db->get()->num_rows();
    return $result;
  }

  public function check_email_address($email_addr)
  {
    $this->db->select('*');
    $this->db->where('email', $email_addr);
    $this->db->from('users');
    $result = $this->db->get()->num_rows();
    return $result;
  }

    public function check_phone($phone)
  {
    $this->db->select('*');
    $this->db->where('mobile_no', $phone);
    $this->db->from('users');
    $result = $this->db->get()->num_rows();
    return $result;
  }

  public function signup($inputs)
  {
      $result  = $this->db->insert('users',$inputs);
      $user_id = $this->db->insert_id();
      $unique_code = $this->getToken(14,$user_id);
      $this->db->where('user_id', $user_id);
      $this->db->update('users', array('unique_code'=>$unique_code));
      return $result;
  }

  public function temp_subscription_success($inputs)
     {

       $new_details = array();

       $user_id = $inputs['user_id'];

       $subscription_id = $inputs['subscription_id'];


       $this->db->select('duration');
       $record = $this->db->get_where('subscription_fee',array('id'=>$subscription_id))->row_array();

       if(!empty($record)){


       $duration = $record['duration'];
       $days = 30;
       switch ($duration) {
         case 1:
           $days = 30;
           break;
         case 2:
           $days = 60;
           break;
         case 3:
           $days = 90;
           break;
         case 6:
           $days = 180;
           break;
         case 12:
           $days = 365;
           break;
         case 24:
           $days = 730;
           break;

         default:
           $days = 30;
           break;
       }

        $subscription_date = date('Y-m-d H:i:s');
        $expiry_date_time =  date('Y-m-d H:i:s',strtotime(date("Y-m-d  H:i:s", strtotime($subscription_date)) ." +".$days."days"));


       $new_details['subscriber_id'] = $user_id;
       $new_details['subscription_id'] = $subscription_id;
       $new_details['subscription_date'] = $subscription_date;
       $new_details['expiry_date_time'] = $expiry_date_time;
       $this->db->where('subscriber_id', $user_id);
       $count = $this->db->count_all_results('subscription_details');
       if($count == 0){

       return $this->db->insert('subscription_details', $new_details);

       }else{

         $this->db->where('subscriber_id', $user_id);
        $this->db->update('subscription_details', $new_details);
       }

     }else{

      return false;
     }

    }

      public function getToken($length,$user_id)
   {
       $token = $user_id;

       $codeAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
       $codeAlphabet.= "abcdefghijklmnopqrstuvwxyz";
       $codeAlphabet.= "0123456789";

       $max = strlen($codeAlphabet); // edited

    for ($i=0; $i < $length; $i++) {
         $token .= $codeAlphabet[$this->crypto_rand_secure(0, $max-1)];

    }

    return $token;
   }

   function crypto_rand_secure($min, $max) {
        $range = $max - $min;
        if ($range < 0) return $min; // not so random...
        $log = log($range, 2);
        $bytes = (int) ($log / 8) + 1; // length in bytes
        $bits = (int) $log + 1; // length in bits
        $filter = (int) (1 << $bits) - 1; // set all lower bits to 1
        do {
            $rnd = hexdec(bin2hex(openssl_random_pseudo_bytes($bytes)));
            $rnd = $rnd & $filter; // discard irrelevant bits
        } while ($rnd >= $range);
        return $min + $rnd;
  }

  public function forgot_count($user_id)
  {
    $query=$this->db->select('forgot');
    $this->db->where('forgot', $user_id);
    $num = $this->db->get('users')->num_rows();

    return $num;
  }

  public function forgot_password($email)
    {
      $this->db->where('email', $email);
      $record = $this->db->get('users')->row_array();
      if(!empty($record)){

        $id = $record['user_id'];
        $userid = md5($id);
        $this->db->where('user_id', $id);
        $this->db->update('users', array('forgot' => $userid ));

        $this->load->library('servpro');
        $to = $email;
        $subject = 'Forgot Password reset link for ';
        $message = 'Reset Password link is follows <a href="'.base_url().'login/change_password/'.$userid.'">Reset Link</a>';
        $this->servpro->send_mail($to,$subject,$message);
        return true;

      }else{

        return false;

      }
    }

  public function change_password($data, $userid)
  {
    $this->db->where('forgot',$userid);
    $result = $this->db->update('users',$data);
    return $result;
  }

  public function otp_check_email($inputs='')
  {
      $email = $inputs['email'];
      $this->db->where('email',$email);
      return $this->db->count_all_results('providers');
  }
   public function otp_check_uemail($inputs='')
  {
      $email = $inputs['email'];
      $this->db->where('email',$email);
      return $this->db->count_all_results('users');
  }
  public function otp_check_mobile_no($inputs='')
   {

      $mobileno = $inputs['mobileno'];
       $this->db->where(array('mobileno'=>$mobileno,'country_code'=>$inputs['countryCode']));
      return $this->db->count_all_results('providers');
   }

   public function otp_check_mobile_no_user($inputs='')
   {

      $mobileno = $inputs['mobileno'];
      $this->db->where('mobileno',$mobileno);
      return $this->db->count_all_results('users');
   }

   public function check_mobile_no($inputs)
   {
      $this->db->where(array('country_code'=>$inputs['countryCode'],'status'=>1))->like('mobileno',$inputs['mobileno'],'match','after');
      return $this->db->count_all_results('providers');
   }
   
    public function check_emailid($inputs)
   {
	  $this->db->where('email',$inputs['email']);
	  $this->db->where('status',1);
      return $this->db->count_all_results('providers');
   }
   
   
    public function check_provider_email($inputs)
   {
      $this->db->where('email',$inputs);
      return $this->db->count_all_results('providers');
   }

    public function check_user_mobileno($inputs)
   {
      $this->db->where(array('country_code'=>$inputs['countryCode'],'status'=>1))->like('mobileno',$inputs['mobileno'],'match','after');
      return $this->db->count_all_results('users');
   }
   
   public function check_user_emailidlogin($inputs)
   {
      $this->db->where('email',$inputs['email']);
	  $this->db->where('status',1); 
      return $this->db->count_all_results('users');
   }
   
   public function check_user_emailid($inputs)
   {

      $this->db->where('email',$inputs);
      return $this->db->count_all_results('users');
   }
   public function save_otp($user_data)
   {
      $result  = $this->db->insert('mobile_otp',$user_data);
      $insert_id = $this->db->insert_id();
      return $insert_id;
   }
   public function otp_validation($user_data,$input_data)
   {
        $this->db->select('id,mobile_number,otp,endtime');
        $this->db->where($user_data);
        $this->db->where('status',1);
        
        $query= $this->db->get('mobile_otp');
       
        $count = $query->num_rows();

    if($count!=0)
      {
        $this->db->select('id,mobile_number,otp,endtime');
        $this->db->where($user_data);
        $this->db->where('status',1);
        $this->db->where('endtime >=',time());
        $query= $this->db->get('mobile_otp');
        $time_count = $query->num_rows();
      }
      else
      {
        return 'invalid_otp';
      }
        
        if( $time_count ==1)
        {

         $this->db->where($user_data);
         $this->db->update('mobile_otp', array('status'=>0));




          $input_data['created_at'] = date('Y-m-d H:i:s');
          $result  = $this->db->insert('providers',$input_data);
          $records=array();
          if($result)
          {
              $user_id = $this->db->insert_id();
              $token = $this->getToken(14,$user_id);
              /*insert wallet*/
              $data=array(
                          "token"=>$token,
                          "currency_code" => $input_data['currency_code'],
                          "user_provider_id"=>$user_id,
                          "type"=>1,
                          "wallet_amt"=>0,
                          "created_at"=>utc_date_conversion(date('Y-m-d H:i:s'))
                        );
              $wallet_result  = $this->db->insert('wallet_table',$data);
              /*insert wallet*/

              $this->db->where('id', $user_id);
              $this->db->update('providers', array('token'=>$token));
              $profile_img=base_url().'assets/img/professional.png';

               $this->db->select('id,name,email,mobileno,category,subcategory,IF(profile_img IS NULL or profile_img = "", "'.$profile_img.'", profile_img) as profile_img,token');
               $this->db->where('id',$user_id);
              $records=$this->db->get('providers')->row_array();

          }
          return array('data'=>$records,'msg'=>'ok');
          }
          else
          {
            $this->db->where($user_data);
            $this->db->update('mobile_otp', array('status'=>0));
            return 'otp_expired';
          }
      

   }
	
	
	public function insertemailusers($input_data)
	{
        if($input_data)
        {
          $input_data['created_at'] = date('Y-m-d H:i:s');
          $result  = $this->db->insert('users',$input_data);
		  $last_query=$this->db->last_query();
		 // echo $last_query;exit;
          $records=array();
          if($result)
          {
              $user_id = $this->db->insert_id();
              $token = $this->getToken(14,$user_id);

                            /*insert wallet*/
              $data=array(
                          "token"=>$token,
                          "currency_code" => $input_data['currency_code'],
                          "user_provider_id"=>$user_id,
                          "type"=>2,
                          "wallet_amt"=>0,
                          "created_at"=>utc_date_conversion(date('Y-m-d H:i:s'))
                        );
              $wallet_result  = $this->db->insert('wallet_table',$data);
             
              $this->db->where('id', $user_id);
              $this->db->update('users', array('token'=>$token));
              $profile_img=base_url().'assets/img/professional.png';

               $this->db->select('id,name,email,mobileno,IF(profile_img IS NULL or profile_img = "", "'.$profile_img.'", profile_img) as profile_img,token');
               $this->db->where('id',$user_id);
               $records=$this->db->get('users')->row_array();

          }
          return array('data'=>$records,'msg'=>'ok');
		}
   }
   
   
   	public function insertemailproviders($input_data)
	{
        if($input_data)
        {
          $input_data['created_at'] = date('Y-m-d H:i:s');
          $result  = $this->db->insert('providers',$input_data);
		  $last_query=$this->db->last_query();
		 // echo $last_query;exit;
          $records=array();
          if($result)
          {
              $user_id = $this->db->insert_id();
              $token = $this->getToken(14,$user_id);
              /*insert wallet*/
              $data=array(
                          "token"=>$token,
                          "currency_code" => $input_data['currency_code'],
                          "user_provider_id"=>$user_id,
                          "type"=>1,
                          "wallet_amt"=>0,
                          "created_at"=>utc_date_conversion(date('Y-m-d H:i:s'))
                        );
              $wallet_result  = $this->db->insert('wallet_table',$data);
              /*insert wallet*/

              $this->db->where('id', $user_id);
              $this->db->update('providers', array('token'=>$token));
              $profile_img=base_url().'assets/img/professional.png';

               $this->db->select('id,name,email,mobileno,category,subcategory,IF(profile_img IS NULL or profile_img = "", "'.$profile_img.'", profile_img) as profile_img,token');
               $this->db->where('id',$user_id);
              $records=$this->db->get('providers')->row_array();

          }
          return array('data'=>$records,'msg'=>'ok');
		}
   }
   
	


   public function otp_validation_user($user_data,$input_data)
   {
        $this->db->select('id,mobile_number,otp,endtime');
        $this->db->where($user_data);
        $this->db->where('status',1);
        
        $query= $this->db->get('mobile_otp');

        $count = $query->num_rows();

    if($count!=0)
      {
        $this->db->select('id,mobile_number,otp,endtime');
        $this->db->where($user_data);
        $this->db->where('status',1);
        $this->db->where('endtime >=',time());
        $query= $this->db->get('mobile_otp');
        $time_count = $query->num_rows();
      }
      else
      {
        return 'invalid_otp';
      }
        
        if( $time_count ==1)
        {

         $this->db->where($user_data);
         $this->db->update('mobile_otp', array('status'=>0));




          $input_data['created_at'] = date('Y-m-d H:i:s');
          $result  = $this->db->insert('users',$input_data);
          $records=array();
          if($result)
          {
              $user_id = $this->db->insert_id();
              $token = $this->getToken(14,$user_id);

                            /*insert wallet*/
              $data=array(
                          "token"=>$token,
                          "currency_code" => $input_data['currency_code'],
                          "user_provider_id"=>$user_id,
                          "type"=>2,
                          "wallet_amt"=>0,
                          "created_at"=>utc_date_conversion(date('Y-m-d H:i:s'))
                        );
              $wallet_result  = $this->db->insert('wallet_table',$data);
              /*insert wallet*/

              $this->db->where('id', $user_id);
              $this->db->update('users', array('token'=>$token));
              $profile_img=base_url().'assets/img/professional.png';

               $this->db->select('id,name,email,mobileno,IF(profile_img IS NULL or profile_img = "", "'.$profile_img.'", profile_img) as profile_img,token');
               $this->db->where('id',$user_id);
              $records=$this->db->get('users')->row_array();

          }
          return array('data'=>$records,'msg'=>'ok');
          }
          else
          {
            $this->db->where($user_data);
            $this->db->update('mobile_otp', array('status'=>0));
            return 'otp_expired';
          }
      

   }
   
   
   

 public function check_emaillogin($check_data)
   {
         $this->db->select('*');
         $this->db->where($check_data);
         $res = $this->db->get('providers')->row_array();  
         return array('data'=>$res,'msg'=>'ok');       
   }
   
    public function check_emailloginuser($check_data)
   {
         $this->db->select('*');
         $this->db->where($check_data);
         $res = $this->db->get('users')->row_array();  
         return array('data'=>$res,'msg'=>'ok');       
   }
   
   

   public function check_otp($check_data)
   {
        $this->db->select('id,mobile_number,otp,endtime');
        $this->db->where($check_data);
        $this->db->where('status',1);
        $query= $this->db->get('mobile_otp');
        $time_count = $query->num_rows();
        if($time_count ==1)
        {

         $this->db->where($check_data);
         $this->db->update('mobile_otp', array('status'=>0));

         $this->db->select('*');
         $this->db->where($check_data);
         $res = $this->db->get('mobile_otp')->row_array();  
         return array('data'=>$res,'msg'=>'ok');
       
         }         
   }

   public function check_otp_user($check_data)
   {
        $this->db->select('id,mobile_number,otp,endtime');
        $this->db->where($check_data);
        $this->db->where('status',1);
        $query= $this->db->get('mobile_otp');
        $time_count = $query->num_rows();
        if($time_count ==1)
        {

         $this->db->where($check_data);
         $this->db->update('mobile_otp', array('status'=>0));

         $this->db->select('*');
         $this->db->where($check_data);
         $res = $this->db->get('mobile_otp')->row_array();  
         return array('data'=>$res,'msg'=>'ok');
       
         }         
   }


   public function get_provider_details($mobile_number)
    {

      
       $record = $this->db->where('mobileno',$mobile_number)->get('providers')->row_array(); 
       
       return $record;    

      }
	  
	public function get_provider_detailsbymail($email)
    {

      
       $record = $this->db->where('email',$email)->get('providers')->row_array(); 
       
       return $record;    

      }
	  
	  	public function get_provider_detailsbymailuser($email)
    {

      
       $record = $this->db->where('email',$email)->get('users')->row_array(); 
       
       return $record;    

      }
	  
	  
	  
	  

    public function get_user_details($mobile_number)
    {

      
       $record = $this->db->select('*')->from('users')->where('mobileno',$mobile_number)->get()->row_array(); 
       
       return $record;    

      }

    public function check_user_email($inputs='')
    {
        $email = $inputs['email'];
        $this->db->where('email',$email);
        return $this->db->count_all_results('users');
    }
  

   public function user_signup($user_details)
   {
        $user_details['created_at'] = date('Y-m-d H:i:s');
        $result  = $this->db->insert('users',$user_details);
        $records=array();
        if($result)
        {
            $user_id = $this->db->insert_id();
            $token = $this->getToken(14,$user_id);
            $this->db->where('id', $user_id);
            $this->db->update('users', array('token'=>$token));

           
           
            $this->db->select('*');
            $this->db->where('id',$user_id);
            $records=$this->db->get('users')->row_array();

        }
        return $records;
 
   }
   
   public function ShareCode($length,$user_id)
   {
       $token = $user_id;

//       $codeAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
       $codeAlphabet= "abcdefghijklmnopqrstuvwxyz";
       $codeAlphabet.= "0123456789";

       $max = strlen($codeAlphabet); // edited

    for ($i=0; $i < $length; $i++) {
         $token .= $codeAlphabet[$this->crypto_rand_secure(0, $max-1)];

    }

    return $token;
   }
   
   public function check_user_emaildet($email)
	  {
		$this->db->select('*');
		$this->db->from('users');
			$this->db->where('email',$email);
			//$this->db->where_in('type',2);
		  $result = $this->db->get()->row_array();
		return $result;
	  }
   
      public function check_provider_emaildet($email)
	  {
		$this->db->select('*');
		$this->db->from('providers');
			$this->db->where('email',$email);
			//$this->db->where_in('type',2);
		  $result = $this->db->get()->row_array();
		return $result;
	  }
	  
	  public function update_res_userpwd($data, $id) {
		$this->db->where('id',$id);
		$status = $this->db->update('users', $data);
		return $status;

		}
		
		public function update_res_providerpwd($data, $id) {
		$this->db->where('id',$id);
		$status = $this->db->update('providers', $data);
		return $status;

		}
   
   
   
   


      

}
