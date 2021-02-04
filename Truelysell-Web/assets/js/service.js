(function($) {
  "use strict";

  var base_url=$('#base_url').val();
  var csrf_token=$('#csrf_token').val();
  var csrfName=$('#csrfName').val();
  var csrfHash=$('#csrfHash').val();
  var placeSearch, autocomplete;

  $( document ).ready(function() {
   $('.select').selectpicker();

     //location

     
     $.ajax({
      type: "GET",
      url: base_url+"user/service/get_category",
      data:{id:$(this).val(),'csrf_token_name':csrf_token}, 
      beforeSend :function(){
        $("#category option:gt(0)").remove(); 
        $('#category').selectpicker('refresh');
        $("#category").selectpicker();
        $('#category').find("option:eq(0)").html("Please wait..");
        $('#category').selectpicker('refresh');
        $("#category").selectpicker();
      },                         
      success: function (data) {   
        $('#category').selectpicker('refresh'); 
        $("#category").selectpicker();      
        $('#category').find("option:eq(0)").html("Select Category");
        $('#category').selectpicker('refresh');
        $("#category").selectpicker();
        var obj=jQuery.parseJSON(data);       
        $('#category').selectpicker('refresh');
        $("#category").selectpicker();
        $(obj).each(function(){
          var option = $('<option />');
          option.attr('value', this.value).text(this.label);           
          $('#category').append(option);
        });       
        $('#category').selectpicker('refresh');
        $("#category").selectpicker();
      }
    });


     $('#category').change(function(){

      $("#subcategory").val('default');
      $("#subcategory").selectpicker("refresh");


      $.ajax({
        type: "POST",
        url: base_url+"user/service/get_subcategory",
        data:{id:$(this).val(),csrf_token_name:csrf_token}, 
        beforeSend :function(){
          $("#subcategory option:gt(0)").remove(); 
          $('#subcategory').selectpicker('refresh');
          $("#subcategory").selectpicker();
          $('#subcategory').find("option:eq(0)").html("Please wait..");
          $('#subcategory').selectpicker('refresh');
          $("#subcategory").selectpicker();
        },                         
        success: function (data) {   
          $('#subcategory').selectpicker('refresh'); 
          $("#subcategory").selectpicker();      
          $('#subcategory').find("option:eq(0)").html("Select SubCategory");
          $('#subcategory').selectpicker('refresh');
          var obj=jQuery.parseJSON(data);       
          $('#subcategory').selectpicker('refresh');
          $("#subcategory").selectpicker();
          $(obj).each(function(){
            var option = $('<option />');
            option.attr('value', this.value).text(this.label);           
            $('#subcategory').append(option);
          });       
          $('#subcategory').selectpicker('refresh');
          $("#subcategory").selectpicker();
        }
      });

    }); 

     $('#add_service').bootstrapValidator({
      fields: {
        service_title: {
          validators: {
            remote: {
             url: base_url + 'user/service/check_service_title',
             data: function(validator) {
               return {
                 service_title: validator.getFieldElements('service_title').val(),
                 'csrf_token_name':$('#login_csrf').val()
               };
             },
             message: 'This Service is already exist',
             type: 'POST'
           },
           notEmpty: {
            message: 'Please Enter your service title'
          }
        }
      },
      service_sub_title: {
        validators: {
          notEmpty: {
            message: 'Please Enter service sub title'
          }
        }
      },
      category: {
        validators: {
          notEmpty: {
            message: 'Please select category...'
          }
        }
      },
      subcategory: {
        validators: {
          notEmpty: {
            message: 'Please select subcategory...'
          }
        }
      },
      service_location: {
        validators: {
          notEmpty: {
            message: 'Please Enter service location...'
          }
        }
      },
      service_amount: {
        validators: {
          digits: {
            message: 'Please Enter valid service amount and not user in special characters...'
          },
          notEmpty: {
            message: 'Please Enter service amount...'
          }
        }
      },
      'service_offered[]': {
        validators: {
          notEmpty: {
            message: 'Please Enter service offered'
          }
        }
      }, 
      about: {
        validators: {
          notEmpty: {
            message: 'Please Enter About Informations...'
          }
        }
      },
      'images[]': {
        validators: {
          file: {
            extension: 'jpeg,png,jpg',
            type: 'image/jpeg,image/png,image/jpg',
            message: 'The selected file is not valid. Only allowed jpeg,png files'
          },
          notEmpty:               {
            message: 'Please upload category image...'
          }
        }
      }                     
    }
  }).on('success.form.bv', function(e) {

   return true;
 });    

 });
//document end


  function initialize() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    autocomplete = new google.maps.places.Autocomplete(
      /** @type {HTMLInputElement} */
      (document.getElementById('service_location')), {
        types: ['geocode']
      });
	  
	 

    google.maps.event.addDomListener(document.getElementById('service_location'), 'focus', geolocate);//alert('sdfds');
    autocomplete.addListener('place_changed', get_latitude_longitude);
	
  }

  function get_latitude_longitude() {
        // Get the place details from the autocomplete object.
        var place = autocomplete.getPlace();
		 var key = $("#map_key").val();//alert(map_key);
        //var key = "AIzaSyAKuTyrOQ7v-xjPXQUcP7pOJn6vSnDCCF8";
        $.get('https://maps.googleapis.com/maps/api/geocode/json',{address:place.formatted_address,key:key},function(data, status){

          $(data.results).each(function(key,value){

            $('#service_address').val(place.formatted_address);
            $('#service_latitude').val(value.geometry.location.lat);
            $('#service_longitude').val(value.geometry.location.lng);


          });    
        });
      }

      function geolocate() {

        if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(function (position) {

            var geolocation = new google.maps.LatLng(
              position.coords.latitude, position.coords.longitude);
            var circle = new google.maps.Circle({
              center: geolocation,
              radius: position.coords.accuracy
            });
            autocomplete.setBounds(circle.getBounds());

          });
        }
      }

      initialize();

      /* Image Upload */

      if($('#add_service, #update_service').length > 0 ){

        document.addEventListener("DOMContentLoaded", init, false);

        //To save an array of attachments 
        var AttachmentArray = [];

        //counter for attachment array
        var arrCounter = 0;

        //to make sure the error message for number of files will be shown only one time.
        var filesCounterAlertStatus = false;

        //un ordered list to keep attachments thumbnails
        var ul = document.createElement('ul');
        ul.className = ("upload-wrap");
        ul.id = "imgList";

        function init() {
            //add javascript handlers for the file upload event
            document.querySelector('#images').addEventListener('change', handleFileSelect, false);
          }

        //the handler for file upload event
        function handleFileSelect(e) {
            //to make sure the user select file/files
            if (!e.target.files) return;

            //To obtaine a File reference
            var files = e.target.files;

            // Loop through the FileList and then to render image files as thumbnails.
            for (var i = 0, f; f = files[i]; i++) {

                //instantiate a FileReader object to read its contents into memory
                var fileReader = new FileReader();

                // Closure to capture the file information and apply validation.
                fileReader.onload = (function (readerEvt) {
                  return function (e) {

                        //Apply the validation rules for attachments upload
                        ApplyFileValidationRules(readerEvt)

                        //Render attachments thumbnails.
                        RenderThumbnail(e, readerEvt);

                        //Fill the array of attachment
                        FillAttachmentArray(e, readerEvt)
                      };
                    })(f);

                // Read in the image file as a data URL.
                // readAsDataURL: The result property will contain the file/blob's data encoded as a data URL.
                // More info about Data URI scheme https://en.wikipedia.org/wiki/Data_URI_scheme
                fileReader.readAsDataURL(f);
              }
              document.getElementById('images').addEventListener('change', handleFileSelect, false);
            }

        //To remove attachment once user click on x button
        jQuery(function ($) {
          $('div').on('click', '.upload-images .file_close', function () {
            var id = $(this).closest('.upload-images').find('img').data('id');

                //to remove the deleted item from array
                var elementPos = AttachmentArray.map(function (x) { return x.FileName; }).indexOf(id);
                if (elementPos !== -1) {
                  AttachmentArray.splice(elementPos, 1);
                }

                //to remove image tag
                $(this).parent().find('img').not().remove();

                //to remove div tag that contain the image
                $(this).parent().find('div').not().remove();

                //to remove div tag that contain caption name
                $(this).parent().parent().find('div').not().remove();

                //to remove li tag
                var lis = document.querySelectorAll('#imgList li');
                for (var i = 0; li = lis[i]; i++) {
                  if (li.innerHTML == "") {
                    li.parentNode.removeChild(li);
                  }
                }

              });
        }
        )

        //Apply the validation rules for attachments upload
        function ApplyFileValidationRules(readerEvt)
        {
            //To check file type according to upload conditions
            if (CheckFileType(readerEvt.type) == false) {
              alert("The file (" + readerEvt.name + ") does not match the upload conditions, You can only upload jpg/png/gif files");
              e.preventDefault();
              return;
            }

            //To check files count according to upload conditions
            if (CheckFilesCount(AttachmentArray) == false) {
              if (!filesCounterAlertStatus) {
                filesCounterAlertStatus = true;
                alert("You have added more than 10 files. According to upload conditions you can upload 10 files maximum");
              }
              e.preventDefault();
              return;
            }
          }

        //To check file type according to upload conditions
        function CheckFileType(fileType) {
          if (fileType == "image/jpeg") {
            return true;
          }
          else if (fileType == "image/png") {
            return true;
          }
          else if (fileType == "image/gif") {
            return true;
          }
          else {
            return false;
          }
          return true;
        }

        //To check file Size according to upload conditions
        function CheckFileSize(fileSize) {
          if (fileSize < 300000) {
            return true;
          }
          else {
            return false;
          }
          return true;
        }

        //To check files count according to upload conditions
        function CheckFilesCount(AttachmentArray) {
            //Since AttachmentArray.length return the next available index in the array, 
            //I have used the loop to get the real length
            var len = 0;
            for (var i = 0; i < AttachmentArray.length; i++) {
              if (AttachmentArray[i] !== undefined) {
                len++;
              }
            }
            //To check the length does not exceed 10 files maximum
            if (len > 9) {
              return false;
            }
            else
            {
              return true;
            }
          }

        //Render attachments thumbnails.
        function RenderThumbnail(e, readerEvt)
        {
          var li = document.createElement('li');
          ul.appendChild(li);
          li.innerHTML = ['<div class=" upload-images"> ' +
          '<a style="display:none;" href="javascript:void(0);" class="file_close btn btn-icon btn-danger btn-sm"><i class="far fa-trash-alt"></i></a><img class="thumb" src="', e.target.result, '" title="', escape(readerEvt.name), '" data-id="',
          readerEvt.name, '"/>' + '</div>'].join('');

          var div = document.createElement('div');
          div.className = "FileNameCaptionStyle d-none";
          li.appendChild(div);
          div.innerHTML = [readerEvt.name].join('');
          document.getElementById('uploadPreview').insertBefore(ul, null);
        }

        //Fill the array of attachment
        function FillAttachmentArray(e, readerEvt)
        {
          AttachmentArray[arrCounter] =
          {
            AttachmentType: 1,
            ObjectType: 1,
            FileName: readerEvt.name,
            FileDescription: "Attachment",
            NoteText: "",
            MimeType: readerEvt.type,
            Content: e.target.result.split("base64,")[1],
            FileSizeInBytes: readerEvt.size,
          };
          arrCounter = arrCounter + 1;
        }
      }



    })(jQuery);
