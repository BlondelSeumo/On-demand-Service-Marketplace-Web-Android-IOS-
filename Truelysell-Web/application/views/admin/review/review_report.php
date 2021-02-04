<div class="page-wrapper">
	<div class="content container-fluid">
	
		<!-- Page Header -->
		<div class="page-header">
			<div class="row">
				<div class="col">
					<h3 class="page-title">Ratings</h3>
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
		<form action="<?php echo base_url()?>admin/Ratingstype/review_report" method="post" id="filter_inputs">
			<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
    

			<div class="card filter-card">
				<div class="card-body pb-0">
					<div class="row filter-row">
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>Service</label>
								<select class="form-control" name="service_id">
									<option value="">Select Service</option>
									<?php foreach ($service_list as $pro) { ?>
									<option value="<?=$pro['id']?>"><?php echo $pro['service_title']?></option>
									<?php } ?>
								</select>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>Provider Name</label>
								<select class="form-control" name="provider_id">
									<option value="">Select provider</option>
									<?php foreach ($provider_list as $pro) { ?>
									<option value="<?=$pro['id']?>"><?php echo $pro['name']?></option>
									<?php } ?>
								</select>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>User Name</label>
								<select class="form-control" name="user_id">
									<option value="">Select User</option>
									<?php foreach ($user_list as $pro) { ?>
									<option value="<?=$pro['id']?>"><?php echo $pro['name']?></option>
									<?php } ?>
								</select>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>Type</label>
								<select class="form-control" name="type_id">
									<option value="">Select Type</option>
									<?php foreach ($rating_type as $pro) { ?>
									<option value="<?=$pro['id']?>"><?php echo $pro['name']?></option>
									<?php } ?>
								</select>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>From Date</label>
								<div class="cal-icon">
									<input class="form-control datetimepicker" type="text" name="from">
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<label>To Date</label>
								<div class="cal-icon">
									<input class="form-control datetimepicker" type="text" name="to">
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="form-group">
								<button class="btn btn-primary btn-block" name="form_submit" value="submit" type="submit">Submit</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
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
										<th>Service Name</th>
										<th>Type Name</th>
										<th>Ratings</th>
										<th>Comments</th>
										
										<th class="text-right">Action</th>
									
									</tr>
								</thead>
								<tbody>
								<?php
								if(!empty($list)) {
									$i=1;
								foreach ($list as $rows) { ?>
								<tr>
									<td><?php echo $i++ ?></td> 
									<td><?=date('d-m-Y',strtotime($rows['created']));?></td>
									<td><?php echo $rows['user_name'] ?></td>
									<td><?php echo $rows['provider_name'] ?></td>
									<td><?php echo $rows['service_title']?></td>
									<td><?php echo $rows['type_name']?></td>
									<td><?php echo $rows['rating']?></td>
									<td><?php echo $rows['review']?></td>
									<?php if($user_role==1){?>
									<td class="text-right">
										<a data-id="<?php echo $rows['id']?>"  href="javascript:void(0);" class="btn btn-sm bg-danger-light mr-2 delete_review_comment">
											<i class="far fa-trash-alt mr-1"></i> Delete
										</a>
									</td>
								<?php }else{?>
									<td class="text-right">
										<a href="javascript:void(0);" class="btn btn-sm bg-danger-light mr-2">
											<i class="far fa-trash-alt mr-1"></i> Delete
										</a>
									</td>
							<?php }?>
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