<?php 

defined('BASEPATH') OR exit('No direct script access allowed.');
$CI =& get_instance();
$email_config=$CI->db->where('key','mail_config')->from('system_settings')->get()->row_array();
$email_address=$CI->db->where('key','email_address')->from('system_settings')->get()->row_array();
$email_password=$CI->db->where('key','email_password')->from('system_settings')->get()->row_array();

$smtp_email_address=$CI->db->where('key','smtp_email_address')->from('system_settings')->get()->row_array();
$smtp_email_password=$CI->db->where('key','smtp_email_password')->from('system_settings')->get()->row_array();
$smtp_email_host=$CI->db->where('key','smtp_email_host')->from('system_settings')->get()->row_array();
$smtp_email_port=$CI->db->where('key','smtp_email_port')->from('system_settings')->get()->row_array();

// $email_config=$this->db->where('key','mail_config')->from('system_settings')->get()->row_array();
// $email_address=$this->db->where('key','email_address')->from('system_settings')->get()->row_array();
// $email_password=$this->db->where('key','email_password')->from('system_settings')->get()->row_array();

// $smtp_email_address=$this->db->where('key','smtp_email_address')->from('system_settings')->get()->row_array();
// $smtp_email_password=$this->db->where('key','smtp_email_password')->from('system_settings')->get()->row_array();
// $smtp_email_host=$this->db->where('key','smtp_email_host')->from('system_settings')->get()->row_array();
// $smtp_email_port=$this->db->where('key','smtp_email_port')->from('system_settings')->get()->row_array();
if(!isset($email_config)){
	$email_config="phpmail";
}

$config['useragent']        = 'PHPMailer';              // Mail engine switcher: 'CodeIgniter' or 'PHPMailer'
$config['protocol']         = 'smtp';                   // 'mail', 'sendmail', or 'smtp'
$config['mailpath']         = '/usr/sbin/sendmail';

$config['smtp_auth']        = true;                     // Whether to use SMTP authentication, boolean TRUE/FALSE. If this option is omited or if it is NULL, then SMTP authentication is used when both $config['smtp_user'] and $config['smtp_pass'] are non-empty strings.
if($email_config['value']=="phpmail"){
	$config['smtp_host']        = 'smtp.gmail.com';
	$config['smtp_user']        = $email_address['value'];
	$config['smtp_pass']        = $email_password['value'];
	$config['smtp_port']        = 587;
}else{
    $config['smtp_host']        = $smtp_email_host['value'];
	$config['smtp_user']        = $smtp_email_address['value'];
	$config['smtp_pass']        = $smtp_email_password['value'];
	$config['smtp_port']        = $smtp_email_port['value'];
}


$config['smtp_timeout']     = 30;                       // (in seconds)
$config['smtp_crypto']      = 'tls';                    // '' or 'tls' or 'ssl'
$config['smtp_debug']       = 0;                        // PHPMailer's SMTP debug info level: 0 = off, 1 = commands, 2 = commands and data, 3 = as 2 plus connection status, 4 = low level data output.
$config['debug_output']     = '';                       // PHPMailer's SMTP debug output: 'html', 'echo', 'error_log' or user defined function with parameter $str and $level. NULL or '' means 'echo' on CLI, 'html' otherwise.
$config['smtp_auto_tls']    = false;                    // Whether to enable TLS encryption automatically if a server supports it, even if `smtp_crypto` is not set to 'tls'.
$config['smtp_conn_options'] = array();                 // SMTP connection options, an array passed to the function stream_context_create() when connecting via SMTP.
$config['wordwrap']         = true;
$config['wrapchars']        = 76;
$config['mailtype']         = 'html';                   // 'text' or 'html'
$config['charset']          = null;                     // 'UTF-8', 'ISO-8859-15', ...; NULL (preferable) means config_item('charset'), i.e. the character set of the site.
$config['validate']         = true;
$config['priority']         = 3;                        // 1, 2, 3, 4, 5; on PHPMailer useragent NULL is a possible option, it means that X-priority header is not set at all, see https://github.com/PHPMailer/PHPMailer/issues/449
$config['crlf']             = "\n";                     // "\r\n" or "\n" or "\r"
$config['newline']          = "\n";                     // "\r\n" or "\n" or "\r"
$config['bcc_batch_mode']   = false;
$config['bcc_batch_size']   = 200;
$config['encoding']         = '8bit';                   // The body encoding. For CodeIgniter: '8bit' or '7bit'. For PHPMailer: '8bit', '7bit', 'binary', 'base64', or 'quoted-printable'.

// DKIM Signing
$config['dkim_domain']      = '';                       // DKIM signing domain name, for exmple 'example.com'.
$config['dkim_private']     = '';                       // DKIM private key, set as a file path.
$config['dkim_private_string'] = '';                    // DKIM private key, set directly from a string.
$config['dkim_selector']    = '';                       // DKIM selector.
$config['dkim_passphrase']  = '';                       // DKIM passphrase, used if your key is encrypted.
$config['dkim_identity']    = '';    