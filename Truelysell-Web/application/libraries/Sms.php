<?php 
defined('BASEPATH') OR exit('No direct script access allowed');
require_once(APPPATH . '../vendor/autoload.php');
use Twilio\Rest\Client;
	
Class Sms{

	public function send_message($mobileno,$message)
	{
		$sid = 'AC93c4b58383728efd4f4253b4c6023a5a';
		$token = 'dcd15d331617d445b92186390be6c2ad';
		$client = new Client($sid, $token);
		$result=$client->messages->create('+'.$mobileno,array('from' => '+13203357119','body' => $message));
		return $result;
	}

     
		

}