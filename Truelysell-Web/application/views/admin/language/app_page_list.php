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
            <li class="nav-item ">
                <a class="nav-link" href="<?php echo $base_url; ?>language">Language</a>
            </li>
            <li class="nav-item ">
                <a class="nav-link" href="<?php echo $base_url; ?>wep_language">Web Keywords</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="<?php echo $base_url; ?>app_page_list">App Keywords</a>
            </li>
        </ul>
        <div class="col-auto text-right">
            <a href="<?php echo $base_url; ?>add-app-keyword" class="btn btn-primary add-button"><i class="fas fa-plus"></i></a>
        </div>
        <br>

        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover table-center mb-0 language_table" id="language_table">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Page Title</th>
                                    </tr>
                                </thead>
                                <tbody>
                                  <?php
                                  $i = 0;
                                  foreach ($list as $page) {
                                    $i++;
                                    
                                    ?>
                                  <tr>
                                    <td><?php echo $i; ?></td>
                                                <td>
                                                    <div class="service-desc">
                                                        
                                                        <h2><a href="<?php echo base_url().'app_page_list/'.$page['page_key']; ?>"><?php echo $page['page_title']; ?></a></h2>
                                                    </div>
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
<script type="text/javascript">
    
    function update_multi_lang()
    {
        
        
        $("#form_id").submit();
    }

</script>



