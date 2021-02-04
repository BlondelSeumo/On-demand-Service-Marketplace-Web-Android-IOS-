<div class="page-wrapper">
	<div class="content container-fluid">
	
		<!-- Page Header -->
		<div class="page-header">
			<div class="row">
				<div class="col">
					<h3 class="page-title">Email template</h3>
				</div>
			</div>
		</div>
		<!-- /Page Header -->		

				
		<div class="row">
			<div class="col-md-12">
				<div class="card">
					<div class="card-body">
						<div class="table-responsive">
							 <table class="table table-hover table-center mb-0 emailtemplate_table">
                            <thead>
                                <tr>
                                    <th>Reference ID</th>
                                    <th>Email Title</th>
                                    <th class="text-right">Action</th>
                                </tr>
                            </thead>
                            <tbody >
                                <?php                                
                                
                                if (!empty($list)) {
                                    $sno = 1;
                                    foreach ($list as $row) {                          
                                            ?>
                                            <tr>
                                                <td><?php echo $row['template_type'] ?></td>  
                                                <td> <?php echo $row['template_title'] ?></td>                                                
                                                <td class="text-right">
                                                    <a href="<?php echo base_url('edit-emailtemplate/' . $row['template_id']); ?>" class="btn btn-sm bg-success-light mr-2" title="Edit"><i class="far fa-edit mr-1"></i> Edit</a>
                                                </td>
                                            </tr>
                                            <?php
                                        
                                   $sno = $sno +1;
                                        }
                                } else {
                                    ?>
                                    <tr>
                                        <td colspan="11"><p class="text-danger text-center m-b-0">No Records Found</p></td>
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