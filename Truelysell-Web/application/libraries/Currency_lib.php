<?php  
if ( ! defined('BASEPATH')) exit('No direct script access allowed');
class Currency_lib{
    private $CI;
    private $rate;
    private $fromCurrency;
    private $toCurrency;
    private $amount;
    private $hourDifference;
    function __construct()
    {
        $this->CI =& get_instance();
        $this->CI->load->database(); 
        
    }
    function currencyConverter($from_Currency,$to_Currency,$amount)
    {
          $from_Currency = urlencode($from_Currency);
          $to_Currency = urlencode($to_Currency);
          $encode_amount = $amount;
          $get=file_get_contents("https://finance.google.com/finance/converter?a=$encode_amount&from=$from_Currency&to=$to_Currency");
          $get = explode("<span class=bld>",$get);
          $get = explode("</span>",$get[1]);
          $converted_currency = preg_replace("/[^0-9\.]/", null, $get[0]);
          return $converted_currency;
    }
  //Return Currency Name
  function GetCurrencyName($currency_code){
    $query=$this->CI->db->select('currency_name')->where('currency_code',$currency_code)->get('currency');
    $res=$query->row();
    if (!empty($res->currency_name))
      return (string) $res->currency_name;
    else
      return '';
  }
  //Return Currency Symbol
  function getCurrencySymbol($currency_code){
  
    $query=$this->CI->db->select('symbol')->where('currency_code',$currency_code)->get('currency');
    $res=$query->row();
    if (!empty($res->symbol))
      return (string) $res->symbol;
    else
      return '';
  }
  //Return Currency Symbol in HEX
  function getCurrencySymbolHex($currency_code){
      $query=$this->CI->db->select('hex')->where('currency_code',$currency_code)->get('currency');
    $res=$query->row();
    if (!empty($res->hex))
      return (string) $res->hex;
    else
      return '';
  }
}
?>