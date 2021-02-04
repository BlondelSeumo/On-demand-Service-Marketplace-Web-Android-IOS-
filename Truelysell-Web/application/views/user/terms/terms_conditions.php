<?php
$query = $this->db->query("select * from system_settings WHERE status = 1");
$result = $query->result_array();
?>
<div class="breadcrumb-bar">
	<div class="container">
		<div class="row">
			<div class="col">
				<div class="breadcrumb-title">
					<h2><?php echo (!empty($user_language[$user_selected]['lg_Terms'])) ? $user_language[$user_selected]['lg_Terms'] : $default_language['en']['lg_Terms']; ?></h2>
				</div>
			</div>
			<div class="col-auto float-right ml-auto breadcrumb-menu">
				<nav aria-label="breadcrumb" class="page-breadcrumb">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="<?php echo base_url();?>"><?php echo (!empty($user_language[$user_selected]['lg_home'])) ? $user_language[$user_selected]['lg_home'] : $default_language['en']['lg_home']; ?></a></li>
						<li class="breadcrumb-item active" aria-current="page"><?php echo (!empty($user_language[$user_selected]['lg_Terms'])) ? $user_language[$user_selected]['lg_Terms'] : $default_language['en']['lg_Terms']; ?></li>
					</ol>
				</nav>
			</div>
		</div>
	</div>
</div>
<div class="content">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="about-blk-content">
					<?php if(!empty($result)) {
						foreach($result as $data){
							if($data['key'] == 'terms'){
								$this->terms = $data['value'];
							}
						  }
						}
					 ?>
					<p><?php echo $this->terms?></p>
					
				</div>
			</div>
		</div>
	</div>
</div>