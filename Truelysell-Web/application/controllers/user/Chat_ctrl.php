<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Chat_ctrl extends CI_Controller {

   public $data;
   public $chat_token;

   public function __construct() {

        parent::__construct();
        error_reporting(0);
        $this->data['theme'] = 'user';
        $this->data['module'] = 'chat';
        $this->data['base_url'] = base_url();

			  $this->load->helper('custom_language');

        $this->load->model('booking_model','booking');
        $this->load->model('Chat_model');
        $this->load->helper('user_timezone_helper');
        
        $user_id = $this->session->userdata('id');
        $this->chat_token=$this->session->userdata('chat_token');
        $this->data['user_id'] = $user_id;
				$this->load->helper('subscription_helper');
       


         $this->data['secret_key'] = '';

         $this->data['publishable_key'] = '';

         $this->data['website_logo_front'] ='assets/img/logo.png';

         $publishable_key='';
         $secret_key='';
         $live_publishable_key='';
         $live_secret_key='';
         $stripe_option='';


          $query = $this->db->query("select * from system_settings WHERE status = 1");
          $result = $query->result_array();
          if(!empty($result))
          {
              foreach($result as $data){

                  if($data['key'] == 'website_name'){
                  $this->website_name = $data['value'];
                  }


                  if($data['key'] == 'secret_key'){

                    $secret_key = $data['value'];

                  }

                  if($data['key'] == 'publishable_key'){

                    $publishable_key = $data['value'];

                  }

                  if($data['key'] == 'live_secret_key'){

                    $live_secret_key = $data['value'];

                  }

                  if($data['key'] == 'live_publishable_key'){

                    $live_publishable_key = $data['value'];

                  }

                  if($data['key'] == 'stripe_option'){

                    $stripe_option = $data['value'];

                   } 
                  
                  if($data['key'] == 'logo_front'){
                      $this->data['website_logo_front'] =  $data['value'];
                  }

              }
          }


          if(@$stripe_option == 1){

          $this->data['publishable_key'] = $publishable_key;

          $this->data['secret_key']      = $secret_key;

        }

        if(@$stripe_option == 2){

          $this->data['publishable_key'] = $live_publishable_key;

          $this->data['secret_key']      = $live_secret_key;

        }


          $config['publishable_key'] =  $this->data['publishable_key'];

          $config['secret_key'] = $this->data['secret_key'];

          $this->load->library('stripe',$config);
          

           if(!$this->session->userdata('id'))
          {
            redirect(base_url());
          }
          
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
         $this->data['page'] = 'user_chats';
         $chat_lists=$this->Chat_model->get_chat_list($this->chat_token);
         $final=[];
         foreach ($chat_lists as $key => $value) {
          if(!empty($value->name)){
          $final[$key]['profile_img']=$value->profile_img;
          $final[$key]['token']=$value->token;
          $final[$key]['name']=$value->name;
          $final[$key]['last_msg']=$this->Chat_model->get_last_msg($value->token)->message;
          $final[$key]['badge']=$this->Chat_model->get_badge_count($value->token,$this->chat_token)->counts;
        }
         }

         $this->data['chat_list']=$final;
         $this->data['server_name']=settingValue('server_name');
         $this->data['port_no']=settingValue('port_no');

         $this->load->vars($this->data);
         $this->load->view($this->data['theme'].'/template');
	}
  public function get_user_chat_lists(){
    $chat_lists=$this->Chat_model->get_chat_list($this->chat_token);
         $final=[];
         foreach ($chat_lists as $key => $value) {
          $final[$key]['profile_img']=$value->profile_img;
          $final[$key]['token']=$value->token;
          $final[$key]['name']=$value->name;
          $final[$key]['last_msg']=$this->Chat_model->get_last_msg($value->token)->message;
          $final[$key]['badge']=$this->Chat_model->get_badge_count($value->token,$this->chat_token)->counts;
         }

         $this->data['chat_list']=$final;
         echo json_encode($this->data);exit;
  }
  /*new Chat App*/

  public function booking_new_chat()
  {     
      extract($_GET);
       
        $data=$this->Chat_model->get_book_info($book_id);

             if(!empty($data)){
              $self_info=$this->Chat_model->get_token_info($this->session->userdata('chat_token'));
              if($self_info->type==2){
              $user_token=$this->Chat_model->get_user_info($data['provider_id'],1);
              }else{
              $user_token=$this->Chat_model->get_user_info($data['user_id'],2);
              }
         
        }

        

         $this->data['page'] = 'user_chats';
          $chat_lists=$this->Chat_model->get_token_info($user_token['token']);
       
          $final['profile_img']=$chat_lists->profile_img;
          $final['token']=$chat_lists->token;
          $final['name']=$chat_lists->name;
          $final['last_msg']='';
          $final['badge']=$this->Chat_model->get_badge_count($chat_lists->token,$this->chat_token)->counts;
        $this->data['server_name']=settingValue('server_name');
        $this->data['port_no']=settingValue('port_no');
        
        $this->data['chat_list']=array($final);

         $this->load->vars($this->data);
         $this->load->view($this->data['theme'].'/template');
  }

  /*appnd chat history*/

  public function get_chat_history(){
    extract($_POST);
          $self_token= $this->session->userdata('chat_token');
         $data['chat_history']=$this->Chat_model->get_conversation_info($self_token,$partner_token);
         $data['partner_info']=$this->Chat_model->get_token_info($partner_token);
         $data['self_info']=$this->Chat_model->get_token_info($self_token);
         $this->load->view('user/chat/ajax_page/chat_history',$data);
  }
	

  /*get token info*/
  public function get_token_informations(){
    extract($_POST);
    $self_token= $this->session->userdata('chat_token');
         $data['partner_info']=$this->Chat_model->get_token_info($partner_token);

         $data['self_info']=$this->Chat_model->get_token_info($self_token); 
         echo json_encode($data);
  }
  /*insert_message*/

  public function insert_message(){
    extract($_POST);
  date_default_timezone_set('UTC');
     $date_time = date('Y-m-d H:i:s');
   date_default_timezone_set('Asia/Kolkata');
      $data=array(
        "sender_token"=>$fromToken,
        "receiver_token"=>$toToken,
        "message"=>$content,
        "status"=>1,
        "read_status"=>0,
        "utc_date_time"=>$date_time,
        "created_at"=>date('Y-m-d H:i:s'),
      ); 
      
      $val=$this->Chat_model->insert_msg($data);
      if($val){
        echo json_encode(['success'=>true,'msg'=>"success"]);exit;
      }else{
        echo json_encode(['success'=>false,'msg'=>"not insert"]);exit;
      }
  }

    /*clear screen*/

  public function clear_history(){

    extract($_POST);

 $data=$this->Chat_model->get_conversation_info($self_token,$partner_token);
 $where=[];
 foreach ($data as $key => $value) {
   $where[]=$value->chat_id;
 }
  $data=array('status'=>0);
  $table='chat_table';
 
 $ret=$this->Chat_model->update_info($where,$data,$table);
 if($ret){
  $ret=1;
 }else{
  $ret=2;
 }
 echo $ret;

  }

  /*change to read staus*/
  public function changeToRead_ctrl(){

extract($_POST);
    $data=array('read_status'=>1);
    $table='chat_table';
    $where=array('receiver_token'=>$self_token,'sender_token'=>$partner_token);
    $ret=$this->Chat_model->changeToRead($where,$data,$table);
    if($ret){
      echo 1;
      }else{
        echo 2;
      }
  }


  }

  ?>