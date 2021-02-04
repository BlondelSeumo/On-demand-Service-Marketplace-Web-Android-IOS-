<?php

defined('BASEPATH') OR exit('No direct script access allowed');
require_once 'vendor/autoload.php';
require_once 'vendor/braintree/braintree_php/lib/Braintree.php';

class Paypal extends CI_Controller {
	public function __construct() { 
		parent::__construct(); 
                error_reporting(0);
		$this->data['base_url'] = base_url();
		$this->session->keep_flashdata('error_message');
		$this->session->keep_flashdata('success_message');
		$this->load->helper('user_timezone_helper');
		$this->load->model('api_model','api');

	}
	public function braintree(){

		  $query = $this->db->query("select * from system_settings WHERE status = 1");
          $result = $query->result_array();
          if(!empty($result))
          {
            foreach($result as $data){
              
            if($data['key'] == 'braintree_merchant'){
               $braintree_merchant = $data['value'];
            }

            if($data['key'] == 'braintree_publickey'){
              $braintree_publickey = $data['value'];
            }

            if($data['key'] == 'braintree_privatekey'){
              $braintree_privatekey = $data['value'];
            }
            if($data['key'] == 'live_braintree_merchant'){
               $live_braintree_merchant = $data['value'];
            }

            if($data['key'] == 'live_braintree_publickey'){
              $live_braintree_publickey = $data['value'];
            }

            if($data['key'] == 'live_braintree_privatekey'){
              $live_braintree_privatekey = $data['value'];
            }

            if($data['key'] == 'paypal_gateway'){
             $environment = $data['value'];
           }
		   
         }
       }
	   
       if($environment == "sandbox"){
		$merchantId = $braintree_merchant;
		$publicKey = $braintree_publickey;
		$privateKey = $braintree_privatekey;
      }else{
		$merchantId = $live_braintree_merchant;
		$publicKey = $live_braintree_publickey;
		$privateKey = $live_braintree_privatekey;		  
	  }

		$gateway = new Braintree\Gateway([
			'environment' => $environment,
			'merchantId' => $merchantId,
			'publicKey' => $publicKey,
			'privateKey' => $privateKey
		]);
		$amount = $_POST['amount'];
		$payload_nonce = $_POST['payload_nonce'];
		$orderId = $_POST['orderID'];

		$result = $gateway->transaction()->sale([
			'amount' => $amount,
			'paymentMethodNonce' => $payload_nonce,
			'orderId' => $orderId,
			'options' => [
				'submitForSettlement' => True
			],
		]);
                
		if ($result->success) {
			$transaction_id=$result->transaction->id;

			$res=$this->paypal_success($amount,$orderId,$transaction_id);
			if($res==true){
				
				$this->session->set_flashdata('success_message','Amount Added to Wallet Successfully');
				redirect(base_url().'user-wallet');
			}
		} else {
			
			$message= 'Something Went in Server End';
			$this->session->set_flashdata('error_message',$message);
		}
	}
	public function paypal_success($amt,$orderId,$transaction_id){
		$token=$this->session->userdata('chat_token');

		$user_info=$this->api->get_token_info($token);
		$wallet=$this->api->get_wallet($token);
		$curren_wallet=$wallet['wallet_amt'];
		/*wallet infos*/
		$pay_data=array(
			'transaction_id'=>$transaction_id,
			'order_id'=>$orderId,
			'amount'=>$amt,
			'user_id'=>$user_info->id,
			'created_at'=>date('Y-m-d H:i:s')
		);
		$paypal=$this->db->insert('paypal_transaction',$pay_data);
		$pay_transaction=$this->db->insert_id();
		$history_pay['token']=$token;
		$history_pay['currency_code'] = $wallet['currency_code'];
		$history_pay['user_provider_id']=$user_info->id;
		$history_pay['type']=$user_info->type;
		$history_pay['tokenid']=$token;
		$history_pay['payment_detail']="paypal";
		$history_pay['charge_id']=1;
		$history_pay['transaction_id']=$pay_transaction;
		$history_pay['exchange_rate']=0;
		$history_pay['paid_status']="pass";
		$history_pay['cust_id']="self";
		$history_pay['card_id']="self";
		$history_pay['total_amt']=$amt;
		$history_pay['fee_amt']=0;
		$history_pay['net_amt']=$amt;
		$history_pay['amount_refund']=0;
		$history_pay['current_wallet']=$curren_wallet;
		$history_pay['credit_wallet']=$amt;
		$history_pay['debit_wallet']=0;
		$history_pay['avail_wallet']=$amt+$curren_wallet;
		$history_pay['reason']=TOPUP;
		$history_pay['created_at']=date('Y-m-d H:i:s');

		if($this->db->insert('wallet_transaction_history',$history_pay)){
			/*update wallet table*/
			$wallet_dat['wallet_amt']=$curren_wallet+$history_pay['credit_wallet'];
			$wallet_dat['updated_on']=date('Y-m-d H:i:s');
			$WHERE =array('token'=> $token);
			$result=$this->api->update_wallet($wallet_dat,$WHERE);

			/*payment on stripe*/
			
			return true;
		}
	}
}