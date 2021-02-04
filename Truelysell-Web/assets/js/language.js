function change_status(id)
{
  var status= $('#lang_status'+id).attr('data-status');
   if(status==1)
   {
     update_language = '2';
   }
   if(status==2)
   {
     update_language = '1';
   }

   $.ajax({
          type:'POST',
          url: BASE_URL+'admin/language/update_language_status',
          data : {id:id,update_language:update_language},
          success:function(response)
            {      
              if(response==0)
              {
                  $('#flash_lang_message').html(" <div class='alert alert-danger fade in' id=''>Default Language cannot be inactive. <button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");

              }
              else if(response==1)
              {
                   if(status==1)
                   {
                     $('#lang_status'+id).attr('data-status',2);
                     $('#lang_status'+id).removeClass("label label-success").addClass("label label-danger");
                      $('#texts'+id).text('In Active');
                      $('#default_language'+id).hide();

                   }
                   if(status==2)
                   {
                     $('#lang_status'+id).attr('data-status',1);
                     $('#lang_status'+id).removeClass("label label-danger").addClass("label label-success");
                      $('#texts'+id).text('Active');
                      $('#default_language'+id).show();
                   }
              }
              else
              {
                
              }
            }                
            });

}


$(document).ready(function(){     
    $('.active_lang').on('change', function (e, data) { 
        var update_language = ''; 
        var sts_str   = '';
        var id = $(this).attr('data-id');
        if($(this).is(':checked')) { 
           update_language = '1'; 
           sts_str   = 'Active';
        } else { 
           update_language = '2';  
           sts_str   = 'InActive';
        }  
        if(update_language != '') {  
            
        } 
  })

    $('.default_lang').on('change', function (e, data) { 
      var id = $(this).attr('data-id');
                  $.ajax({
                type:'POST',
                url: BASE_URL+'admin/language/update_language_default',
                data : {id:id},
                success:function(response)
                  {  
                     if(response==0)
                    {    
                      $('#default_language'+id).attr('checked',false);
                    }
                    else
                    {
                      $('#default_language'+id).attr('checked',true);
                    }
                  }                
            });
        
  });
    });