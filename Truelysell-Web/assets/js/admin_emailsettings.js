(function($) {

	"use strict";
    $( document ).ready(function() {
       $('.smtpMail').hide();
            var mail_config=$('#mail_config').val();

            if(mail_config=="phpmail"){
                $('.phpMail').show();
                $('.smtpMail').hide();
                $("#phpmail").prop("checked", true);
            }else{
               $("#smtpmail").prop("checked", true);
               $('.smtpMail').show();
            $('.phpMail').hide();
            }
            $('input[type=radio][name=mail_config]').on('change',function() {
           var mail_config=$(this).val();
           if(mail_config=="smtp"){
            $('.smtpMail').show();
            $('.phpMail').hide();
           }else{
                $('.phpMail').show();
                $('.smtpMail').hide();
           }
        });
    });
    $('#form_emailsetting').bootstrapValidator({
        container: '#messages',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
           
            email_address: {
                validators: {
                    notEmpty: {
                        message: 'The email address is required and cannot be empty'
                    }
                }
            }
        }
    });
})(jQuery);