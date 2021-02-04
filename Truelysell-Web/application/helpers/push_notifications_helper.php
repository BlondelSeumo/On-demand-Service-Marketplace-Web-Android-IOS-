<?php 

if(!function_exists('sendFCMMessage')){
  function sendFCMMessage($data,$target){ 
    $ci =& get_instance();
    $val=$ci->db->where('key','firebase_server_key')->where('status',1)->get('system_settings')->row();


    $firebase_api = trim($val->value);
    $value[]=$target;
    $fields = array(
        'registration_ids' => $value,
        'data' => $data,
    );

            // Set POST variables
    $url = 'https://fcm.googleapis.com/fcm/send';

    $headers = array(
      'Authorization: key=' . $firebase_api,
      'Content-Type: application/json'
  );

            // Open connection
    $ch = curl_init();

            // Set the url, number of POST vars, POST data
    curl_setopt($ch, CURLOPT_URL, $url);

    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

            // Disabling SSL Certificate support temporarily
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

            // Execute post
    $result = curl_exec($ch);  
    if($result === FALSE){ 
      die('Curl failed: ' . curl_error($ch));
  }
            // Close connection
  curl_close($ch);
}


}

/*APNS*/

if(!function_exists('sendApnsMessage'))
{
    function sendApnsMessage($data,$target){

        $deviceToken = $target;

        $passphrase = 'Dreams99';

        $message = $data;

        if (!preg_match('/[\'^£$%&*()}{@#~?><>:,|=_+¬-]/', $deviceToken)){
        $ctx = stream_context_create();
        stream_context_set_option($ctx, 'ssl', 'local_cert', 'truleysell.pem');
        stream_context_set_option($ctx, 'ssl', 'passphrase', $passphrase);

// Open a connection to the APNS server
        $fp = stream_socket_client('ssl://gateway.sandbox.push.apple.com:2195', $err, $errstr, 60, STREAM_CLIENT_CONNECT|STREAM_CLIENT_PERSISTENT, $ctx);

   

        $body['aps']=$message;
        $payload = json_encode($body);

// Build the binary notification
        $msg = chr(0) . pack('n', 32) . pack('H*', $deviceToken) . pack('n', strlen($payload)) . $payload;


        $result = fwrite($fp, $msg, strlen($msg));

        
        fclose($fp);
     }
    }

  
}

?>