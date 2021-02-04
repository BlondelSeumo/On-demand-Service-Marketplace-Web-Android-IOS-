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
            <?php $page_key = $this->uri->segment(2); ?>
            <a href="<?php echo base_url().'app-keyword-add/'.$page_key; ?>" class="btn btn-primary add-button"><i class="fas fa-plus"></i></a>
        </div>
       
        <br>

        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-body">
                        <div class="table-responsive">
                            <form action="<?php echo base_url().'admin/language/update_multi_language/';?>" onsubmit="update_multi_lang();" method="post" id="form_id">
                                <input type="hidden" name="page_key" value="<?php echo $this->uri->segment(2);?>">
                                <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" id="app_csrf"/>

                            <table class="table" id="language_app_table">
                                <thead>
                                    <tr>
                                        <?php
                                        
                                        if (!empty($active_language))
                                        {
                                            foreach ($active_language as $row)
                                            {  
                                                ?>
                                                <th><?php echo ucfirst($row['language'])?></th>
                                                <?php
                                            }
                                        }
                                        ?>
                                    </tr>
                                </thead>
                                <tbody>
                               
                            </tbody>
                        </table>
                        <div class="m-t-30 text-center">
                            <button name="form_submit"  type="submit" class="btn btn-primary center-block" value="true">Save</button>
                        </div>
                        </form>
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



