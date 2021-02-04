(function(factory) {
    $("#avatar-modal").modal('hide');
    $("#crop-action").hide();
  
    $(".result-details").hide();
    if (typeof define === 'function' && define.amd) {
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        // Node / CommonJS
        factory(require('jquery'));
    } else {
        factory(jQuery);
    }

});

(function($) {

    'use strict';


    var console = window.console || {
        log: function() {}
    };

    function CropAvatar($element) {
        
        this.$container = $element;
        this.$avatarView = this.$container.find('.avatar-view');
        this.$avatar = this.$avatarView.find('img');
        this.$avatarModal = this.$container.find('#avatar-modal');
        this.$loading = this.$container.find('.loading');

        this.$avatarForm = this.$avatarModal.find('.avatar-form');
        this.$avatarUpload = this.$avatarForm.find('.avatar-upload');
        this.$avatarSrc = this.$avatarForm.find('.avatar-src');
        this.$avatarData = this.$avatarForm.find('.avatar-data');
        this.$avatarInput = this.$avatarForm.find('.avatar-input');
        this.$avatarSave = this.$avatarForm.find('.avatar-save');
        this.$avatarBtns = this.$avatarForm.find('.avatar-btns');

        this.$avatarWrapper = this.$avatarModal.find('.avatar-wrapper');
        this.$avatarPreview = this.$avatarModal.find('.avatar-preview');
	

        this.init();
    }

    CropAvatar.prototype = {
        constructor: CropAvatar,

        support: {
            fileList: !!$('<input type="file">').prop('files'),
            blobURLs: !!window.URL && URL.createObjectURL,
            formData: !!window.FormData
        },

        init: function() {
            this.support.datauri = this.support.fileList && this.support.blobURLs;

            if (!this.support.formData) {
                this.initIframe();
            }

            this.initTooltip();
            this.initModal();
            this.addListener();
        },

        addListener: function() {
            this.$avatarView.on('click', $.proxy(this.click, this));
            this.$avatarInput.on('change', $.proxy(this.change, this));
            this.$avatarForm.on('submit', $.proxy(this.submit, this));
            this.$avatarBtns.on('click', $.proxy(this.rotate, this));
        },

        initTooltip: function() {
            this.$avatarView.tooltip({
                placement: 'bottom'
            });
        },

        initModal: function() {
            this.$avatarModal.modal({
                show: false
            });
        },

        initPreview: function() {
            var url = this.$avatar.attr('src');
            this.$avatarPreview.html('<img src="' + url + '">');
        },

        initIframe: function() {
            var target = 'upload-iframe-' + (new Date()).getTime(),
                $iframe = $('<iframe>').attr({
                    name: target,
                    src: ''
                }),
                _this = this;

            // Ready ifrmae
            $iframe.one('load', function() {

                // respond response
                $iframe.on('load', function() {
                    var data;

                    try {
                        data = $(this).contents().find('body').text();
                    } catch (e) {
                        console.log(e.message);
                    }

                    if (data) {
                        try {
                            data = $.parseJSON(data);
                        } catch (e) {
                            console.log(e.message);
                        }

                        _this.submitDone(data);
                    } else {
                        _this.submitFail('Image upload failed!');
                    }

                    _this.submitEnd();

                });
            });

            this.$iframe = $iframe;
            this.$avatarForm.attr('target', target).after($iframe.hide());
        },

        click: function() {
            this.$avatarModal.modal('show');
            
        },

        change: function() {
            var files, file;

            if (this.support.datauri) {
                files = this.$avatarInput.prop('files');

                if (files.length > 0) {
                    file = files[0];

                    if (this.isImageFile(file)) {
                        if (this.url) {
                            URL.revokeObjectURL(this.url); // Revoke the old one
                        }

                        this.url = URL.createObjectURL(file);
                        this.startCropper();
                    }
                }
            } else {
                file = this.$avatarInput.val();

                if (this.isImageFile(file)) {
                    this.syncUpload();
                }
            }
        },

        submit: function() {
            if (!this.$avatarSrc.val() && !this.$avatarInput.val()) {
                return false;
            }

            if (this.support.formData) {
                this.ajaxUpload();
                return false;
            }
        },

        rotate: function(e) {
            var data;

            if (this.active) {
                data = $(e.target).data();

                if (data.method) {
                    this.$img.cropper(data.method, data.option);
                }
            }
        },

        isImageFile: function(file) {
            if (file.type) {
                return /^image\/\w+$/.test(file.type);
            } else {
                return /\.(jpg|jpeg|png|gif)$/.test(file);
            }
        },

        startCropper: function() {
            var _this = this;
            var aspectRatiostr = $('input[name=ratio]:checked').val();
            
            if (this.active) {
                this.$img.cropper('replace', this.url);
            } else {
                this.$img = $('<img src="' + this.url + '">');
                this.$avatarWrapper.empty().html(this.$img);
                this.$img.cropper({
                    aspectRatio: aspectRatiostr,
                    preview: this.$avatarPreview.selector,
                    strict: false,
                    height:10,
                    crop: function(data) {
                        var json = [
              '{"x":' + data.detail.x,
              '"y":' + data.detail.y,
              '"height":' + data.detail.height,
              '"width":' + data.detail.width,
              '"rotate":' + data.detail.rotate + '}'
                ].join();

                        _this.$avatarData.val(json);
                    }
                });

                this.active = true;
            }

            this.$avatarModal.one('hidden.bs.modal', function() {
                _this.$avatarPreview.empty();
                _this.stopCropper();
            });
        },

        stopCropper: function() {
            if (this.active) {
                this.$img.cropper('destroy');
                this.$img.remove();
                this.active = false;
            }
        },

        ajaxUpload: function() {
            var url = this.$avatarForm.attr('action'),
                data = new FormData(this.$avatarForm[0]),
                _this = this;

            $.ajax(url, {
                type: 'post',
                data: data,
                dataType: 'json',
                processData: false,
                contentType: false,

                beforeSend: function() {

                    $(".avatar-save").html('<img width="24" height="24" src="' + BASE_URL + 'images/btn-loader.svg" alt="loading">');
                    _this.submitStart();
                },

                success: function(data) {
                    $(".no_image").hide();
                    _this.submitDone(data);
                },

                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    _this.submitFail(textStatus || errorThrown);
                },

                complete: function() {
                    $(".avatar-save").html('Done');
                    _this.submitEnd();
                }
            });
        },

        syncUpload: function() {
            this.$avatarSave.click();
        },

        submitStart: function() {
            this.$loading.fadeIn();
        },

        submitDone: function(data) { 
            if (data.success == 'Y') {
                $('#avatar-modal').modal('hide');
                $(".crop-result").html ('<img width="250" class="upload_image_btn" height="250" src="' + BASE_URL + data.cropped_fliepath + '"  alt="loading">');

                $('#avatarInput').val('');
                $("#delete_img").show();
                $(".result-details").show();
                $(".crop-action").show();
                var imagepath = data.cropped_fliepath;
                $(".crop-action").html('<a class="crop-delete" id="delete_img" title="Delete" type="button" onclick="deleteimage(\'' + imagepath + '\')">Delete</a><a href="' + BASE_URL+data.cropped_fliepath+ '" class="crop-download" download  title="Download">Download</a>');
                var htmltable = '<div class="table-responsive">' +
                    '<table class="table table-striped table-bordered result-table">' +
                    '<thead>' +
                    '<tr>' +
                    '<th>Property</th>  ' +
                    '<th>Value</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>' +
                    '<tr>' +
                    '<td class="bold">Name</td>' +
                    '<td>' +
                    '<p>' + data.full_fliename + '</p>' +
                    '</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td class="bold">Type</td>' +
                    '<td>' +
                    '<p>' + data.image_extension + '</p>' +
                    '</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td class="bold">Date</td>' +
                    '<td>' +
                    '<p>' + data.Date + '</p>' +
                    '</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td class="bold">File path</td>' +
                    '<td>' +
                    '<p>' + data.cropped_fliepath + '</p>' +
                    '</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td class="bold">Success</td>' +
                    '<td>' +
                    '<p>' + data.success + '</p>' +
                    '</td>' +
                    '</tr>' +
                    '</tbody>' +
                    '</table>' +
                    '</div>';
                $(".result-details").html(htmltable);

            } else {
                this.alert('Failed to response');
            }
        },

        submitFail: function(msg) {
            this.alert(msg);
        },

        submitEnd: function() {
            this.$loading.fadeOut();
        },

        cropDone: function() {
            this.$avatarForm.get(0).reset();
            $("#new_image_attaches").append(this.result);
            if (this.orginalimg) {
                $("#event_img").val(this.orginalimg);
            }
            if (this.fullnameimg) {
                $("#event_img_thumb").val(this.fullnameimg);
                $("#uploadimg").attr('src', BASE_URL + this.fullnameimg);
            }
            this.stopCropper();

            $("#nomoredata").remove();
            this.$avatarModal.modal('hide');
            $(".modal-backdrop").remove();
        },

        alert: function(msg) {
            var $alert = [
                '<div class="alert alert-danger avatar-alert alert-dismissable">',
                '<button type="button" class="close" data-dismiss="alert">&times;</button>',
                msg,
                '</div>'
            ].join('');

            this.$avatarUpload.after($alert);
        }
    };

    $(function() {
        return new CropAvatar($('#crop-avatar'));
    });

});

$(document).on('click','.imgRatio',function(){
    
    var $image = $("#crop-avatar");
     $image.cropper("destroy");
    $('#avatarInput').change();   
});



function deleteimage(videolink) {
    var url = BASE_URL ;
    $.ajax({
        type: 'POST',
        url: url,
        data: {
            videolink: videolink
        },
        success: function(response) {

            if (response == 1) {
             
				 $('.crop-action').hide();
				 $('#avatarInput').val('');
                $(".crop-result").html('');
                $(".result-details").hide();
				location.reload();
            }
        }
    });
}
$('#avatarInput').change(function() {

    $("#avatar-modal").css('display', 'block');
    $("#avatar-modal").modal('show');
    $(".avatar-upload").hide();

})
function openfile() {
    $('#avatarInput').click();
}