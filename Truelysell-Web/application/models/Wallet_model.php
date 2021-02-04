<?php
if ( ! defined('BASEPATH')) exit('No direct script access allowed');
class Wallet_model extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
	}

 
 /*wallet info*/

  public function get_wallet_info(){
   $val=$this->db->select('*')->from('wallet_table')->get()->result_array();
   
   $wallet=[];
        if(!empty($val)){
          foreach ($val as $key => $value) {
            
               if($value['type']==1){
               $user_info=$this->get_user_info($value['user_provider_id'],$value['type']); 
             }else{
               $user_info=$this->get_user_info($value['user_provider_id'],$value['type']); 
             }
              $profile_img = $user_info['profile_img'];
              if(empty($profile_img)){
                $profile_img = 'assets/img/user.jpg';
              }
             
             $wallet[$key]['id']=$value['id'];
             $wallet[$key]['user_name']=$user_info['name'];
             $wallet[$key]['user_mobile']=$user_info['mobileno'];
             $wallet[$key]['user_email']=$user_info['email'];
             $wallet[$key]['user_image']=$profile_img;
             $wallet[$key]['wallet_amt']=$value['wallet_amt'];
			 $wallet[$key]['currency_code']=$value['currency_code'];
             $wallet[$key]['token']=$value['token'];
             $wallet[$key]['date']=$value['created_at'];
             $wallet[$key]['role']=$value['type'];

           
          }
        }
   return $wallet;
  }

  /*filter option*/

    public function get_wallet_info_filter($token,$from,$to){
              
              if(!empty($from)) {
              $from_date=date("Y-m-d 00:00:00", strtotime($from));
              }else{
              $from_date='';
              }
              if(!empty($to)) {
              $to_date=date("Y-m-d 23:59:00", strtotime($to));
              }else{
              $to_date='';
              }

             $val=$this->db->select('*');
             $this->db->from('wallet_table');
          if(!empty($token)){
            $this->db->where('token',$token);
          }
          if(!empty($from_date)){
            $this->db->where('created_at >=',$from_date);
          }
          if(!empty($to_date)){
            $this->db->where('created_at <=',$to_date);
          }  

          $val=$this->db->get()->result_array();
   
   $wallet=[];
        if(!empty($val)){
          foreach ($val as $key => $value) {
            
               if($value['type']==1){
               $user_info=$this->get_user_info($value['user_provider_id'],$value['type']); 
             }else{
               $user_info=$this->get_user_info($value['user_provider_id'],$value['type']); 
             }
              $profile_img = $user_info['profile_img'];
              if(empty($profile_img)){
                $profile_img = 'assets/img/user.jpg';
              }

             $wallet[$key]['id']=$value['id'];
             $wallet[$key]['user_name']=$user_info['name'];
             $wallet[$key]['user_mobile']=$user_info['mobileno'];
             $wallet[$key]['user_email']=$user_info['email'];
             $wallet[$key]['user_image']=$profile_img;
             $wallet[$key]['wallet_amt']=$value['wallet_amt'];
			 $wallet[$key]['currency_code']=$value['currency_code'];
             $wallet[$key]['token']=$value['token'];
             $wallet[$key]['date']=$value['created_at'];
             $wallet[$key]['role']=$value['type'];

           
          }
        }
   return $wallet;
  }

  public function get_wallet_history(){
    
    $val=$this->db->select('id,token,user_provider_id,type,currency_code,current_wallet,credit_wallet,debit_wallet,avail_wallet,total_amt,fee_amt,reason,created_at')->from('wallet_transaction_history')->order_by('id','DESC')->get()->result_array();

     $wallet=[];
        if(!empty($val)){
          foreach ($val as $key => $value) {
            
               if($value['type']==1){
               $user_info=$this->get_user_info($value['user_provider_id'],$value['type']); 
             }else{
               $user_info=$this->get_user_info($value['user_provider_id'],$value['type']); 
             }
              $profile_img = $user_info['profile_img'];
              if(empty($profile_img)){
                $profile_img = 'assets/img/user.jpg';
              }
              if(!empty($value['fee_amt'])){
                $fee=$value['fee_amt']/100;
              }else{
                $fee=0;

              }

             $wallet[$key]['id']=$value['id'];
             $wallet[$key]['user_name']=$user_info['name'];
             $wallet[$key]['user_mobile']=$user_info['mobileno'];
             $wallet[$key]['user_email']=$user_info['email'];
             $wallet[$key]['user_image']=$profile_img;
             $wallet[$key]['currency_code']=$value['currency_code'];
             $wallet[$key]['current_wallet']=$value['current_wallet'];
             $wallet[$key]['credit_wallet']=$value['credit_wallet'];
             $wallet[$key]['debit_wallet']=$value['debit_wallet'];
             $wallet[$key]['avail_wallet']=$value['avail_wallet'];
             $wallet[$key]['total_amt']=$value['total_amt'];
             $wallet[$key]['fee_amt']=$value['fee_amt'];
             $wallet[$key]['reason']=$value['reason'];
             $wallet[$key]['fee_amt']=$fee;
             $wallet[$key]['date']=$value['created_at'];
             $wallet[$key]['role']=$value['type'];

           
          }
        }
   return $wallet;
  }
/*wallet*/
 public function get_wallet_history_filter($token,$from,$to){

    
              if(!empty($from)) {
              $from_date=date("Y-m-d 00:00:00", strtotime($from));
              }else{
              $from_date='';
              }
              if(!empty($to)) {
              $to_date=date("Y-m-d 23:59:00", strtotime($to));
              }else{
              $to_date='';
              }
    
    $this->db->select('id,token,user_provider_id,type,currency_code,current_wallet,credit_wallet,debit_wallet,avail_wallet,total_amt,fee_amt,reason,created_at');
    $this->db->from('wallet_transaction_history');


          if(!empty($token)){
            $this->db->where('token',$token);
          }
          if(!empty($from_date)){
            $this->db->where('created_at >=',$from_date);
          }
          if(!empty($to_date)){
            $this->db->where('created_at <=',$to_date);
          }  

          $val=$this->db->order_by('id','DESC')->get()->result_array();

     $wallet=[];
        if(!empty($val)){
          foreach ($val as $key => $value) {
            
               if($value['type']==1){
               $user_info=$this->get_user_info($value['user_provider_id'],$value['type']); 
             }else{
               $user_info=$this->get_user_info($value['user_provider_id'],$value['type']); 
             }
              $profile_img = $user_info['profile_img'];
              if(empty($profile_img)){
                $profile_img = 'assets/img/user.jpg';
              }
              if(!empty($value['fee_amt'])){
                $fee=$value['fee_amt']/100;
              }else{
                $fee=0;

              }

             $wallet[$key]['id']=$value['id'];
             $wallet[$key]['user_name']=$user_info['name'];
             $wallet[$key]['user_mobile']=$user_info['mobileno'];
             $wallet[$key]['user_email']=$user_info['email'];
             $wallet[$key]['user_image']=$profile_img;
             $wallet[$key]['currency_code']=$value['currency_code'];
             $wallet[$key]['current_wallet']=$value['current_wallet'];
             $wallet[$key]['credit_wallet']=$value['credit_wallet'];
             $wallet[$key]['debit_wallet']=$value['debit_wallet'];
             $wallet[$key]['avail_wallet']=$value['avail_wallet'];
             $wallet[$key]['total_amt']=$value['total_amt'];
             $wallet[$key]['fee_amt']=$value['fee_amt'];
             $wallet[$key]['reason']=$value['reason'];
             $wallet[$key]['fee_amt']=$fee;
             $wallet[$key]['date']=$value['created_at'];
             $wallet[$key]['role']=$value['type'];

           
          }
        }
   return $wallet;
  }
/*wallet*/
/*user_info*/


    public function get_user_info($user_id,$user_type){

    if($user_type ==2){
      $val=$this->db->select('*')->from('users')->where('id',$user_id)->where('type',$user_type)->where('status',1)->get()->row_array();
    }else{
      $val=$this->db->select('*')->from('providers')->where('id',$user_id)->where('type',$user_type)->where('status',1)->get()->row_array();
    }
        
    return $val;
  }

		
}
?>
