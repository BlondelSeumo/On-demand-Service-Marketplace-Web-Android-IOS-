<?php
$self_info=$this->session->userdata('chat_token');
foreach ($chat_history as $key => $msg_value) {
	if($self_info != $msg_value->sender_token){
	?>
	<div class="d-flex justify-content-start mb-4">
		<div class="img_cont_msg">
			<img src="https://static.turbosquid.com/Preview/001292/481/WV/_D.jpg" class="rounded-circle user_img_msg">
		</div>
		<div class="msg_cotainer">
			<?=$msg_value->message;?>
			<span class="msg_time">8:40 AM, Today</span>
		</div>
	</div>
	<?php

	}else{ ?>
	<div class="d-flex justify-content-end mb-4">
		<div class="msg_cotainer_send">
			<?=$msg_value->message;?>
			<span class="msg_time_send">8:55 AM, Today</span>
		</div>
		<div class="img_cont_msg">
			<img src="https://static.turbosquid.com/Preview/001292/481/WV/_D.jpg" class="rounded-circle user_img_msg">
		</div>
	</div>
<?php } } ?>