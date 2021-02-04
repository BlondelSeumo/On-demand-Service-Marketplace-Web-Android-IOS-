<div class="breadcrumb-bar">
	<div class="container">
		<div class="row">
			<div class="col">
				<div class="breadcrumb-title">
					<h2><?php echo ucfirst(str_replace('_', ' ', $list['footer_submenu'])); ?></h2>
				</div>
			</div>
			<div class="col-auto float-right ml-auto breadcrumb-menu">
				<nav aria-label="breadcrumb" class="page-breadcrumb">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="<?php echo base_url();?>"><?php echo (!empty($user_language[$user_selected]['lg_home'])) ? $user_language[$user_selected]['lg_home'] : $default_language['en']['lg_home']; ?></a></li>
						<li class="breadcrumb-item active" aria-current="page"><?php echo ucfirst(str_replace('_', ' ', $list['footer_submenu'])); ?></li>
					</ol>
				</nav>
			</div>
		</div>
	</div>
</div>
<div class="container">
	<div class="pages-content">
		<p><?php echo str_replace('_', ' ', $list['page_desc']); ?></p>
	</div>
</div>