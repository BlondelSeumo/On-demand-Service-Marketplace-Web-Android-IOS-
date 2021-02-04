<?php
if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Chat_model extends CI_Model
{

    public function __construct()
    {
        parent::__construct();
    }

    /*get information base on token*/
    public function get_token_info($token){
        
        $user_table=$this->db->select('*')->
                        from('users')->
                        where('token',$token)->
                        get()->row();
        $provider_table=$this->db->select('*')->
                        from('providers')->
                        where('token',$token)->
                        get()->row();
        if(!empty($user_table)){
            return $user_table;
        }else{
            return $provider_table;
        }                
        
    }

      public function get_book_info($book_service_id){


    $ret=$this->db->select('tab_1.provider_id,tab_1.user_id,tab_1.status,tab_2.service_title')->
                    from('book_service as tab_1')->
                    join('services as tab_2','tab_2.id=tab_1.service_id','LEFT')->
                    where('tab_1.id',$book_service_id)->limit(1)->
                    order_by('tab_1.id','DESC')->
                    get()->row_array();
    return $ret;

  }  

      public function get_user_info($user_id,$user_type){

    if($user_type ==2){
      $val=$this->db->select('*')->from('users')->where('id',$user_id)->where('type',$user_type)->get()->row_array();
    }else{
      $val=$this->db->select('*')->from('providers')->where('id',$user_id)->where('type',$user_type)->get()->row_array();
    }
        
    return $val;
  }

    /*get last msg*/
    public function get_last_msg($token){
      $val=$this->db->select('message,created_at')->
                      from('chat_table')->
                      where('sender_token',$token)->
                      or_where('receiver_token',$token)->
                      order_by('chat_id','DESC')->
                      limit(1)->get()->row();
      return $val;                

    }

    /*change to read status*/

    public function changeToRead($where,$data,$table){
         $this->db->where($where);
        $ret=$this->db->update($table,$data);
        return $ret;               

    }

    /*get badge count*/
    
    public function get_badge_count($send_token,$token){
      $val=$this->db->select('count(chat_id) as counts')->
                      from('chat_table')->
                      where('sender_token',$send_token)->
                      where('receiver_token',$token)->
                      where('status',1)->
                      where('read_status',0)->
                      get()->row();
      return $val;                

    }

  /*get chat list*/
  public function get_chat_list($token){

    $sent=[];
    $receive=[];
    $sent=$this->db->select('receiver_token as token')->
                    from('chat_table')->
                    where('sender_token',$token)->
                    get()->result_array();
    $receive=$this->db->select('sender_token as token')->
                    from('chat_table')->
                    where('receiver_token',$token)->
                    get()->result_array();

    $chat_tokens=(array_merge($sent,$receive));
    $test=[];
    foreach ($chat_tokens as $key => $value) {
       $test[]=$value['token'];
    }

    $token_detail=[];
    foreach (array_unique($test) as $key => $value) {

        $token_detail[]=$this->get_token_info($value);
        
    }
    return $token_detail;

  }

  /*get chat history*/

  public function get_conversation_info($self_token,$partner_token){
        $return=$this->db->select('*')->
                from('chat_table')->
                where("(`sender_token` = '".$self_token."' AND `receiver_token` = '".$partner_token."') OR (`sender_token` = '".$partner_token."' AND `receiver_token` = '".$self_token."')")->
                where('status',1)->
                group_by('chat_id')->
                order_by('chat_id','ASC')->
                get()->result();
        return $return;
    }
    /*insert msg*/
	
    public function insert_msg($data){
      $val=$this->db->insert("chat_table",$data);
    if($val){
      return true;
    }else{
      return false;
    }
    }

    /*update*/

    public function update_info($where,$data,$table){
         $this->db->where_in('chat_id',$where);
        $ret=$this->db->update($table,$data);
        return $ret;
    }
 
}

?>