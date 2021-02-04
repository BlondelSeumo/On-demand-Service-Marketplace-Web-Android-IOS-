<?php
  if(!function_exists('settings')){
  
  function settings($val){
      $ci =& get_instance();
      $settings = $ci->db->query("select * from system_settings WHERE status = 1")->result_array();
      $result=array();

        
        if(!empty($settings)){
          foreach($settings as $datas){
            if($datas['key']=='currency_option'){
             $result['currency'] = $datas['value'];
            }
         }
        }

        if(!empty($result[$val]))
		  {
		     $results= $result[$val];
		  }
		else
		  {
		     $results= 'INR';
		  }

    return $results;

     }
function settingValue($key){
  if(!empty($key)){
     $ci =& get_instance();
     $settings = $ci->db->where('key=',$key)->get('system_settings')->row_array();
     if(!empty($settings)){
        return $settings['value'];
     }else{
        return "";
     }
  }
}
  
function currencyConverter($from, $to) {


    $url = 'https://free.currconv.com/api/v7/convert?q=' . $from . '_' . $to . ',' . $to . '_' . $from . '&compact=ultra&apiKey=de2f3dcf8b88d2d760d4';

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');


$headers = array();
$headers[] = 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36';
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);

$result = curl_exec($ch);
if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
}
curl_close($ch);
print_r($result);
}
}
function removeTag($data){
  
    foreach ($data as $key => $value) {
      if(!is_array($value)){
        $_POST[$key]=strip_tags($value);
      }
    }
   return $_POST;
}
?>