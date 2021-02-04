<body>
	<h5>Hai <?php echo $user['name'];?>,</h5>
	<p><?= $service['service_title'];?> <?php echo (!empty($user_language[$user_selected]['lg_booking_success'])) ? $user_language[$user_selected]['lg_booking_success'] : $default_language['en']['lg_booking_success']; ?>.</p>
</body>