<div class="page-wrapper">
	<div class="content container-fluid">
	
		<!-- Page Header -->
		<div class="page-header">
			<div class="row">
				<div class="col">
					<h3 class="page-title">Rating Type</h3>
				</div>
				<div class="col-auto text-right">
					<a href="<?php echo $base_url; ?>add-ratingstype" class="btn btn-primary add-button">
						<i class="fas fa-plus"></i>
					</a>
				</div>
			</div>
		</div>
		<!-- /Page Header -->
		
		
		
		<div class="row">
			<div class="col-md-12">
				<div class="card">
					<div class="card-body">
                        <div class="table-responsive">
                            <table class="table custom-table m-b-0 ratingstype_table" >
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Rating Type</th>  
                                      
                                        <th>Status</th>

                                        <th class="text-right">Action</th>
                                    	
                                    </tr>
                                </thead>
                                <tbody>
                                    <?php
                                    $i=1;
                                    foreach ($list as $rows) {
									if($rows['status']==1) {
										$val='checked';
									}
									else {
										$val='';
									}
	if($user_role==1){
	echo'<tr>
    <td>'.$i++.'</td>
	<td>'.$rows['name'].'</td>
	<td><div class="status-toggle">
	<input id="status_'.$rows['id'].'" class="check change_Status_rating" data-id="'.$rows['id'].'" type="checkbox" '.$val.'>
		<label for="status_'.$rows['id'].'" class="checktoggle">checkbox</label>
		</div> </td>
		<td class="text-right"><a href="'.base_url().'edit-ratingstype/'.$rows['id'].'" class="table-action-btn btn btn-sm bg-success-light mr-2"><i class="far fa-edit mr-1"></i> Edit</a>
									</td>
									</tr>';
	}else{
		echo'<tr>
    <td>'.$i++.'</td>
	<td>'.$rows['name'].'</td>
	<td><div class="status-toggle">
	<input id="status_'.$rows['id'].'" class="check change_Status_rating" data-id="'.$rows['id'].'" type="checkbox" '.$val.' disabled>
		<label for="status_'.$rows['id'].'" class="checktoggle">checkbox</label>
		</div> </td>
		<td class="text-right"><a href="'.base_url().'edit-ratingstype/'.$rows['id'].'" class="table-action-btn btn btn-sm bg-success-light mr-2"><i class="far fa-edit mr-1"></i> Edit</a>
									</td>
									</tr>';
								}
									}
									?>
                                </tbody>
                            </table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>