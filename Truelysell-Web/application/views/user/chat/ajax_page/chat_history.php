<?php
$self_info=$this->session->userdata('chat_token');
foreach ($chat_history as $key => $msg_value) {
	$full_date =date('Y-m-d H:i:s', strtotime($msg_value->created_at));
	$date=date('Y-m-d',strtotime($full_date));
	$date_f=date('d-m-Y',strtotime($full_date));
	$yes_date=date('Y-m-d',(strtotime ( '-1 day' , strtotime (date('Y-m-d')) ) ));
	$time=date('H:i',strtotime($full_date));
	$session = date('h:i A', strtotime($time));
	if($date == date('Y-m-d')){
		$timeBase ="Today ".$session;
	}elseif($date == $yes_date){
		$timeBase ="Yester day ".$session;
	}else{
		$timeBase =$date_f." ".$session;
	}
	if($self_info != $msg_value->sender_token){
?>
<div class="d-flex justify-content-start mb-4">
	<div class="img_cont_msg">
	</div>
	<div class="msg_cotainer">
		<?=$msg_value->message;?>
		<span class="msg_time"> <?=$timeBase;?></span>
	</div>
</div>
<?php
}else{ ?>
<div class="d-flex justify-content-end mb-4">
	<div class="msg_cotainer_send">
		<?=$msg_value->message;?>
		<span class="msg_time_send"> <?=$timeBase;?></span>
	</div>
	<div class="img_cont_msg">
	</div>
</div>
<?php } } ?>