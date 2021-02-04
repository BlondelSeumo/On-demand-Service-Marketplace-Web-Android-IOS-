<?php 

if(!function_exists('get_languages')){
  
   function get_languages($lang){ 
     
     $ci =& get_instance();
     $ci->load->database();
     
    $ci->db->select('page_key,lang_key,lang_value');
    $ci->db->from('language_management');
    $ci->db->where('language', 'en');
    $ci->db->order_by('page_key', 'ASC');
    $ci->db->order_by('lang_key', 'ASC');
    $records = $ci->db->get()->result_array();


    $language = array();
    if(!empty($records)){
      foreach ($records as $record) {
        
			$ci->db->select('page_key,lang_key,lang_value');
		    $ci->db->from('language_management');
		    $ci->db->where('language', $lang);
		    $ci->db->where('page_key', $record['page_key']);
		    $ci->db->where('lang_key', $record['lang_key']);
		    $eng_records = $ci->db->get()->row_array();
						if(!empty($eng_records['lang_value'])){
            $language['language'][$record['page_key']][$record['lang_key']] = $eng_records['lang_value'];
					}
					else {
						$language['language'][$record['page_key']][$record['lang_key']] = $record['lang_value'];
					}
               }
    }
    return $language;
   }
}

 
?>