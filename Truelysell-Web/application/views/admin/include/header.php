<?php
    $query = $this->db->query("select * from system_settings WHERE status = 1");
    $result = $query->result_array();
    $this->website_name = '';
    $this->website_logo_front ='assets/img/logo.png';
     $fav=base_url().'assets/img/favicon.png';
    if(!empty($result)) {
		foreach($result as $data){
			if($data['key'] == 'website_name'){
				$this->website_name = $data['value'];
			}
			if($data['key'] == 'favicon'){
				$favicon = $data['value'];
			}
			if($data['key'] == 'logo_front'){
				$this->website_logo_front =  $data['value'];
			}
			if($data['key'] == 'meta_title'){
				$this->meta_title =  $data['value'];
			}
			if($data['key'] == 'meta_description'){
				$this->meta_description =  $data['value'];
			}
			if($data['key'] == 'meta_keywords'){
				$this->meta_keywords =  $data['value'];
			}
		}
    }
    if(!empty($favicon)) {
		$fav = base_url().'uploads/logo/'.$favicon;
    }
?>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
	<meta name="description" content="<?php echo $this->meta_description; ?>">
    <meta name="keywords" content="<?php echo $this->meta_keywords; ?>">
    <link rel="shortcut icon" type="image/x-icon" href="<?php echo $fav;?>">
    <title><?php echo $this->meta_title;?></title>
    <?php
    $base_url = base_url();
    $page = $this->uri->segment(1); ?>
    
	<?php if($page == 'admin-profile'){ ?>
	<link rel="stylesheet" href="<?php echo base_url(); ?>assets/css/cropper.min.css">
	<?php } ?>
	
	<link rel="stylesheet" href="<?php echo base_url(); ?>assets/css/bootstrap-datetimepicker.min.css">
	<link rel="stylesheet" href="<?php echo base_url();?>assets/plugins/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="<?php echo base_url(); ?>assets/plugins/fontawesome/css/fontawesome.min.css">
	<link rel="stylesheet" href="<?php echo base_url(); ?>assets/plugins/fontawesome/css/all.min.css">
	<!--<link rel="stylesheet" href="<?php echo base_url();?>assets/plugins/jvectormap/jquery-jvectormap-2.0.3.css">-->
	<link rel="stylesheet" href="<?php echo base_url();?>assets/plugins/datatables/datatables.min.css">
	<link rel="stylesheet" href="<?php echo base_url(); ?>assets/css/animate.min.css">
	<link rel="stylesheet" href="<?php echo $base_url; ?>assets/css/select2.min.css">
	<link rel="stylesheet" href="<?php echo base_url();?>assets/plugins/owlcarousel/owl.carousel.min.css">
	<link rel="stylesheet" href="<?php echo base_url();?>assets/plugins/owlcarousel/owl.theme.default.min.css">
	<link rel="stylesheet" href="<?php echo base_url(); ?>assets/css/admin.css">

   
</head>

<body>
    <div class="main-wrapper">