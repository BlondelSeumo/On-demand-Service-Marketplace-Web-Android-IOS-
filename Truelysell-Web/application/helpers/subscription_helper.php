<?php
  if(!function_exists('subscription_details')){

    function get_subscription_details($subscriber_id){
        $ci =& get_instance();
        $ci->db->where('md5(subscriber_id)',$subscriber_id);
        $ci->db->order_by('id', 'DESC');
        return $ci->db->get('subscription_details')->row_array();
       }
  }
?>
