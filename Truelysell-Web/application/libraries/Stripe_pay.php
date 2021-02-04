<?php 
Class Stripe_pay 
{
	public function __construct($config = array())
	{
		$this->config = $config;
		$this->validation();
	}
}
?>