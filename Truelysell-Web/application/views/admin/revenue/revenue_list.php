<div class="page-wrapper">
	<div class="content container-fluid">
	
		<!-- Page Header -->
		<div class="page-header">
			<div class="row">
				<div class="col">
					<h3 class="page-title">Revenue</h3>
				</div>
				<div class="col-auto text-right">
					<a class="btn btn-white filter-btn mr-2" href="javascript: void(0);" id="filter_search">
						<i class="fas fa-filter"></i>
					</a>
				</div>
			</div>
		</div>
		<!-- /Page Header -->
		
		<!-- Search Filter -->
		
		<!-- /Search Filter -->
		
		
		
		<div class="row">
			<div class="col-md-12">
				<div class="card">
					<div class="card-body">
						<div class="table-responsive">
							<table class=" table table-hover table-center mb-0 payment_table">
								<thead>
									<tr>
										<th>#</th>
										<th>Date</th>
										<th>Provider Name</th>
										<th>User Name</th>
										<th>Amount</th>
										<th>Commission Amount</th>
										<th>Status</th>
										
										
									
									</tr>
								</thead>
								<tbody>
								<?php
								if(!empty($list)) {
									$i=1;
								foreach ($list as $rows) { 
                                                                    $amount=$rows['amount'];
                                                                    $comi=$rows['commission'];
                                                                    $comAount=$amount*$comi/100;
//                                                                    $compre=$amount-$comAount;
                                                                    ?>
								<tr>
									 
									<td><?php echo $i++; ?></td> 
									<td><?php echo ($rows['date']); ?></td> 
									<td><?php echo ($rows['provider']); ?></td> 
									<td><?php echo ($rows['user']); ?></td> 
									<td><?php echo currency_conversion($rows['currency_code']).($rows['amount']); ?></td> 
									<td><?php echo currency_conversion($rows['currency_code']).($comAount); ?></td> 
									<td><label class="badge badge-success">Completed</label></td> 
									
									<!--Compete Request Accept update_status_user-->

								</tr>
								<?php } } else {
								?>
								<tr>
									<td colspan="9">
										<div class="text-center text-muted">No records found</div>
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