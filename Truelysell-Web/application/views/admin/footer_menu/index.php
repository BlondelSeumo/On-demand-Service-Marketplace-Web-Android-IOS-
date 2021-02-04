<div class="page-wrapper">
	<div class="content container-fluid">
	
		<!-- Page Header -->
		<div class="page-header">
			<div class="row">
				<div class="col">
					<h3 class="page-title">Footer Menu</h3>
					<p class="m-t-5">Maximum 4 footer Menu only!</p>
				</div>
				<div class="col-auto text-right">
					<?php
				if ($footercount <= 3) {
				?>
					<div class="col-sm-4 text-right m-b-20">
						<a href="<?php echo base_url().$theme . '/' . $model . '/create'; ?>" class="btn btn-primary add-button"><i class="fas fa-plus"></i></a>
					</div>
				<?php
				}
				?>
				
				</div>
			</div>
		</div>
		<!-- /Page Header -->
		<?php
			if ($this->session->userdata('message')) {
				echo $this->session->userdata('message');
			}
			?>
		<div class="panel">
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-striped table-actions-bar m-b-0 categories_table">
							<thead>
								<tr>
									<th>#</th>
									<th>Widget Name</th>
									<th>Create Date</th>
									<th class="text-right">Action</th>
								</tr>
							</thead>
							<tbody>
								<?php
								if (!empty($lists)) {
									$sno = 0;
									foreach ($lists as $row) {
										$_id = isset($row['id']) ? $row['id'] : '';
										if (!empty($_id)) {
											$page_name = isset($row['title']) ? $row['title'] : '';
											$user_status = 'Inactive';
											if (isset($row['status']) && $row['status'] == 1) {
												$user_status = 'Active';
											}
											$created_on = '-';
											if (isset($row['created_date'])) {
												if (!empty($row['created_date']) && $row['created_date'] != "0000-00-00 00:00:00") {
													$created_on = '<span >' . date('d M Y', strtotime($row['created_date'])) . '</span>';
												}
											}
								?>
											<tr>
												<td> <?php echo ++$sno; ?></td>
												<td> <?php echo str_replace('_', ' ', $page_name); ?></td>
												<td> <?php echo $created_on ?></td>
												<td class="text-right">
													<a href="<?php echo base_url().'admin/footer_menu/edit/' . $_id; ?>" class="btn btn-sm bg-success-light mr-2"><i class="far fa-edit mr-1"></i> Edit</a>&nbsp;
													<a href="javascript:;" class="on-default remove-row btn btn-sm bg-danger-light mr-2 delete_footer_menu" id="Onremove_<?php echo $_id; ?>" data-id="<?php echo $_id; ?>"><i class="far fa-trash-alt mr-1"></i> Delete</a>
												</td>
											</tr>
									<?php
										}
									}
								} else {
									?>
									<tr>
										<td colspan="5">
											<p class="text-danger text-center m-b-0">No Records Found</p>
										</td>
									</tr>
								<?php } ?>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
	</div>
</div>