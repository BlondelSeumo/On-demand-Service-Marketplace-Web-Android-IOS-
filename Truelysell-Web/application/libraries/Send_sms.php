<?php 
defined('BASEPATH') OR exit('No direct script access allowed');
require_once(APPPATH . '../vendor/autoload.php');
use Nexmo\Client;

Class Send_sms{

	public function send_message($mobileno,$message)
	{
		$client = new NexmoClient(new NexmoClientCredentialsBasic('67c452d6', 'f2xUR7JbjTrqJKPz')); 
		$text = new NexmoMessageText('+'.$mobileno,array('from' => '+442086388690','body' => $message)); 
		$response = $client->message()->send($text);
		print_r($response->getResponseData());
	}
}	
?>

