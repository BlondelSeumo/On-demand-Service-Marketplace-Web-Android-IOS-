<div class="page-wrapper">
	<div class="content container-fluid">
        <div class="container">
            <div class="row">
                <div class="col-sm-8">
                    <h4 class="page-title m-b-20 m-t-0">Footer Sub Menu</h4>
                </div>
                <div class="col-sm-4 text-right m-b-20">
                    <a href="<?php echo base_url($theme . '/' . $model . '/create'); ?>" class="btn btn-primary add-button"><i class="fas fa-plus"></i></a>
                </div>
            </div>
            <?php
            if ($this->session->userdata('message')) {
                echo $this->session->userdata('message');
            }
            ?>
            <div class="panel">
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-actions-bar categories_table">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Widget</th>
                                    <th>Menu Title</th>
                                    <th>Page Description</th>
                                    <th>Create Date</th>
                                    <th>Status</th>
                                    <th class="text-right">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <?php
                                if (!empty($lists)) {
                                    $sno = 1;
                                    foreach ($lists as $row) {
                                        $_id = isset($row['id']) ? $row['id'] : '';
                                        if (!empty($_id)) {
                                            $main_menu = $row['title'];
                                            $sub_menu = $row['footer_submenu'];
                                            $seo_title = $row['seo_title'];
                                            $seo_desc = $row['seo_desc'];
                                            $seo_keyword = $row['seo_keyword'];
                                            $page_title = isset($row['page_title']) ? $row['page_title'] : '';
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
                                            if (isset($row['page_desc'])) {
                                                $page_content = $row['page_desc'];
                                            }
                                ?>
                                            <tr>
                                                <td> <?php echo $sno ?></td>
                                                <td> <?php echo str_replace('_', ' ', $main_menu); ?></td>
                                                <td> <?php echo $sub_menu ?></td>
                                                <td> <?php echo substr(strip_tags($page_content), 0, 20);
                                                        if (strlen($page_content) > 20) { ?> ...<?php } ?> </td>
                                                <td> <?php echo $created_on ?></td>
                                                <td> <?php echo $user_status; ?></td>
                                                <td class="text-right">
                                                <a href="<?php echo base_url().'admin/footer_submenu/edit/' . $_id; ?>" class="btn btn-sm bg-success-light mr-2"><i class="far fa-edit mr-1"></i> Edit</a>&nbsp;
													<a href="javascript:void(0)" class="on-default remove-row btn btn-sm bg-danger-light mr-2 delete_footer_submenu" id="Onremove_<?php echo $_id; ?>" data-id="<?php echo $_id; ?>"><i class="far fa-trash-alt mr-1"></i> Delete</a>
                                                   
                                                </td>
                                            </tr>
                                    <?php
                                        }
                                        $sno = $sno + 1;
                                    }
                                } else {
                                    ?>
                                    <tr>
                                        <td colspan="11">
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