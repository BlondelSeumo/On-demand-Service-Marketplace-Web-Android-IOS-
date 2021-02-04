<div class="page-wrapper">
    <div class="content container-fluid">

        <!-- Page Header -->
        <div class="page-header">
            <div class="row">
                <div class="col">
                    <h3 class="page-title">Languages</h3>
                </div>



            </div>

        </div>

        <ul class="nav nav-tabs menu-tabs">
            <li class="nav-item active">
                <a class="nav-link" href="<?php echo $base_url; ?>language">Language</a>
            </li>
            <li class="nav-item ">
                <a class="nav-link" href="<?php echo $base_url; ?>wep_language">Web Keywords</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<?php echo $base_url; ?>app_page_list">App Keywords</a>
            </li>
        </ul>
        <div class="col-auto text-right">
            <a href="<?php echo $base_url; ?>add-language" class="btn btn-primary add-button"><i class="fas fa-plus"></i></a>
        </div>
        <br>
        <div id="flash_lang_message"></div>

        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover table-center mb-0 language_table" >
                                <form  method="post" id="form_id">
                                    <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" id="active_csrf"/>

                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Languages</th>
                                            <th>Languages Value</th>
                                            <th>Action</th>
                                            <th>Default Language</th>

                                        </tr>
                                    </thead>
                                    <tbody >
                                        <?php
                                        if (!empty($list)) {
                                            $i=1;
                                            foreach ($list as $row) {
                                                $new = '';
                                                $status = 'Active';
                                                if ($row['status'] == 2) {
                                                    $status = 'Inactive';
                                                } else {
                                                    
                                                }
                                                ?>
                                        
                                                <tr>
                                                    <td> <?php echo $i++; ?></td>
                                                    <td> <?php echo $row['language'] ?></td>
                                                    <td> <?php echo $row['language_value'] ?></td>
                                                    <td>
                                                        <?php
                                                        $status = '';
                                                        if ($row['status'] == 1) {
                                                            $status = 'success';
                                                            $stst = 'Active';
                                                            $style = 'style="display:block;"';
                                                        } else {
                                                            $status = 'danger';
                                                            $stst = 'In Active';
                                                            $style = 'style="display:none;"';
                                                        }
                                                        if ($row['language_value'] == 'en') {
                                                            echo '';
                                                        } else {
                                                            ?>

                                                            <span id="lang_status<?php echo $row['id']; ?>" data-status="<?php echo $row['status']; ?>" style="cursor: pointer;" onclick="change_status(<?php echo $row['id']; ?>)" class="badge badge-<?php echo $status; ?>"><span id="texts<?php echo $row['id']; ?>"><?php echo $stst; ?></span></span>



                                                        <?php } ?>

                                                    </td>
                                                    <td><input type="radio" <?php echo $style; ?> class="default_lang" value="1" <?php if ($row['default_language'] == '1') echo 'checked'; ?> name="default_language" data-id="<?php echo $row['id']; ?>" id="default_language<?php echo $row['id']; ?>"></td>

                                                </tr>

                                                <?php
                                            }
                                        }else {
                                            ?>

                                            <tr>

                                                <td colspan="6"><p class="text-danger text-center m-b-0">No Records Found</p></td>

                                            </tr>

                                        <?php } ?>

                                    </tbody>
                                </form>
                            </table>
                        </div> 
                    </div> 
                </div>
            </div>
        </div>
    </div>
</div>
<script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.min.js"></script>

<script type="text/javascript">
                                                    function change_status(id)
                                                    {
                                                        var status = $('#lang_status' + id).attr('data-status');
                                                        var csrf_token = $('#active_csrf').val();

                                                        if (status == 1)
                                                        {
                                                            update_language = '2';
                                                        }
                                                        if (status == 2)
                                                        {
                                                            update_language = '1';
                                                        }
                                                        $.ajax({
                                                            type: 'POST',
                                                            url: '<?php echo base_url('admin/language/update_language_status'); ?>',
                                                            data: {id: id, update_language: update_language, csrf_token_name: csrf_token},
                                                            success: function (response)
                                                            {

                                                                if (response == 0)
                                                                {
                                                                    $('#flash_lang_message').html(" <div class='alert alert-danger fade in' id=''>Default Language cannot be inactive. <button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");

                                                                } else if (response == 1)
                                                                {
                                                                    if (status == 1)
                                                                    {
                                                                        $('#lang_status' + id).attr('data-status', 2);
                                                                        $('#lang_status' + id).removeClass("badge badge-success").addClass("badge badge-danger");
                                                                        $('#texts' + id).text('In Active');
                                                                        $('#default_language' + id).hide();

                                                                    }
                                                                    if (status == 2)
                                                                    {
                                                                        $('#lang_status' + id).attr('data-status', 1);
                                                                        $('#lang_status' + id).removeClass("badge badge-danger").addClass("badge badge-success");
                                                                        $('#texts' + id).text('Active');
                                                                        $('#default_language' + id).show();
                                                                    }
                                                                } else
                                                                {

                                                                }
                                                            }
                                                        });

                                                    }




                                                    $(document).ready(function () {
                                                        $('.default_lang').on('change', function (e, data) {
                                                            var id = $(this).attr('data-id');
                                                            var csrf_token = $('#active_csrf').val();
                                                            $.ajax({
                                                                type: 'POST',
                                                                url: '<?php echo base_url('admin/language/update_language_default'); ?>',
//                                                                url: base_url + 'admin/language/update_language_default',
                                                                data: {id: id, csrf_token_name: csrf_token},
                                                                success: function (response)
                                                                {
                                                                    if (response == 0)
                                                                    {
                                                                        $('#default_language' + id).attr('checked', false);
                                                                    } else
                                                                    {
                                                                        $('#default_language' + id).attr('checked', true);
                                                                    }
                                                                }
                                                            });

                                                        });
                                                    });




                                                    $('.active_lang').on('change', function (e, data) {
                                                        var update_language = '';
                                                        var sts_str = '';
                                                        var id = $(this).attr('data-id');
                                                        if ($(this).is(':checked')) {
                                                            update_language = '1';
                                                            sts_str = 'Active';
                                                        } else {
                                                            update_language = '2';
                                                            sts_str = 'InActive';
                                                        }
                                                        if (update_language != '') {

                                                        }
                                                    })




</script>

