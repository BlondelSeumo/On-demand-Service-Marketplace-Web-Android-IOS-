/*
Author       : Dreamguys
Template Name: Truelysell - Service Marketplace
Version      : 1.0
*/

(function($) {
	"use strict";
	var current_page=$('#current_page').val();
	// Stick Sidebar
	var base_url=$('#base_url').val();
  var BASE_URL=$('#base_url').val();
  var csrf_token=$('#csrf_token').val();
  var csrfName=$('#csrfName').val();
  var csrfHash=$('#csrfHash').val();
  var modules=$('#modules_page').val();
	if ($(window).width() > 767) {
		if($('.theiaStickySidebar').length > 0) {
			$('.theiaStickySidebar').theiaStickySidebar({
			  // Settings
			  additionalMarginTop: 125
			});
		}
	}
	
	// Sidebar
	
	if($(window).width() <= 991){
		var Sidemenu = function() {
			this.$menuItem = $('.main-nav a');
		};

		function init() {
			var $this = Sidemenu;
			$('.main-nav a').on('click', function(e) {
				if($(this).parent().hasClass('has-submenu')) {
					e.preventDefault();
				}
				if(!$(this).hasClass('submenu')) {
					$('ul', $(this).parents('ul:first')).slideUp(350);
					$('a', $(this).parents('ul:first')).removeClass('submenu');
					$(this).next('ul').slideDown(350);
					$(this).addClass('submenu');
				} else if($(this).hasClass('submenu')) {
					$(this).removeClass('submenu');
					$(this).next('ul').slideUp(350);
				}
			});
		}

	// Sidebar Initiate
	init();
}

	// Mobile menu sidebar overlay
	
	$('body').append('<div class="sidebar-overlay"></div>');
	$(document).on('click', '#mobile_btn', function() {
		$('main-wrapper').toggleClass('slide-nav');
		$('.sidebar-overlay').toggleClass('opened');
		$('html').addClass('menu-opened');
		$('.header').removeClass('navbar-fixed');
		return false;
	});
	
	$(document).on('click', '.sidebar-overlay', function() {
		$('html').removeClass('menu-opened');
		$(this).removeClass('opened');
		$('main-wrapper').removeClass('slide-nav');
	});
	
	$(document).on('click', '#menu_close', function() {
		$('html').removeClass('menu-opened');
		$('.sidebar-overlay').removeClass('opened');
		$('main-wrapper').removeClass('slide-nav');
	});
	
	// Content div min height set
	
	function resizeInnerDiv() {
		var height = $(window).height();	
		var header_height = $(".header").height();
		var footer_height = $(".footer").height();
		var breadcrumb_height = $(".breadcrumb-bar").height();
		var setheight = height - header_height;
		var trueheight = setheight - footer_height;
		var trueheight2 = trueheight - breadcrumb_height;
		$(".content").css("min-height", trueheight2);
	}
	
	if($('.content').length > 0 ){
		resizeInnerDiv();
	}

	$(window).resize(function(){
		if($('.content').length > 0 ){
			resizeInnerDiv();
		}
	});
	
	$(window).on('scroll', function(){
		if ( $(window).scrollTop() > 100 ) {
			$('.sticktop').addClass('navbar-fixed');
		} else {
			$('.sticktop').removeClass('navbar-fixed');
		}
	});	
	
	if($('.service-slider').length > 0 ){
		$('.service-slider').owlCarousel({
			items:3,
			margin:30,
			dots:true,
			responsiveClass:true,
			responsive:{
				0:{
					items:1
				},
				768:{
					items:2
				},
				1170:{
					items:3	,
					loop:false
				}
			}
		});	
	}
	
	if($('.popular-slider').length > 0 ){
		$('.popular-slider').owlCarousel({
			items:3,
			margin:30,
			dots:true,
			responsiveClass:true,
			responsive:{
				0:{
					items:1
				},
				768:{
					items:2
				},
				1170:{
					items:2
				}
			}
		});	
	}
	
	if($('.images-carousel').length > 0 ){
		$('.images-carousel').owlCarousel({
			rtl: true,
			loop: true,
			center: true,
			margin: 10,
			responsiveClass: true,
			responsive: {
				0: {
					items: 1
				},
				600: {
					items: 1
				},
				1000: {
					items: 1,
					margin: 20
				}
			}
		})
	}
	
    // Membership Add More

    $(".membership-info").on('click','.trash', function () {
    	$(this).closest('.membership-cont').remove();
    	return false;
    });

    $(".add-membership").on('click', function () {
    	var membershipcontent = '<div class="row form-row membership-cont">' +
    	'<div class="col-12 col-md-10 col-lg-6">' +
    	'<div class="form-group">' +
    	'<input type="text" class="form-control" name="service_offered[]" id="field1">' +
    	'</div>' +
    	'</div>' +
    	'<div class="col-12 col-md-2 col-lg-2">' +
    	'<a href="#" class="btn btn-danger trash"><i class="far fa-times-circle"></i></a>' +
    	'</div>' +
    	'</div>';
    	$(".membership-info").append(membershipcontent);
    	return false;
    });

    if ($(window).scrollTop() > 200) {
    	$('.sticktop').addClass('menu-bg');
    } else {
    	$('.sticktop').removeClass('menu-bg');
    }

    $('.datetimepicker').datetimepicker({
    	format: 'DD-MM-YYYY',
    	minDate: new Date(),

    	icons: {
    		up: "fas fa-angle-up",
    		down: "fas fa-angle-down",
    		next: 'fas fa-angle-right',
    		previous: 'fas fa-angle-left'

    	}

    });

    $('.datepicker').datepicker({
    	dateFormat: 'dd-mm-yy',
    	minDate: new Date(),	
    	icons: {
    		up: "fas fa-angle-up",
    		down: "fas fa-angle-down",
    		next: 'fas fa-angle-right',
    		previous: 'fas fa-angle-left'
    	}, onSelect: function(dateText) {
    	}

    });

    $('.datepicker').on('change', function(e){
    	$(e.target).valid();
    });

    $('#book_service').bootstrapValidator('revalidateField', 'booking_date');

	// chat action button toggle
	
	$(document).on('click', '#action_menu_btn', function() {
		$('.action_menu').toggle();
	});
	
	// Delete service
	if(current_page=="my-services"){
		var delete_title = "Inactive Service";
		var delete_msg = "Are you sure want to inactive this service?";
		var delete_text = "Your service has been Inactive";

	}
	if(current_page=="my-services-inactive"){
		var delete_title = "Delete Service";
		var delete_msg = "Are you sure want to delete this service?";
		var delete_text = "Your service has been deleted";
		var delete_active_title = "Active Service";
		var delete_active_msg = "Are you sure want to Active this service?";
		var delete_active_text = "Your service has been Actived";
	}
	if(current_page=="featured-services"){
		var delete_title = "Delete Service";
		var delete_msg = "Are you sure want to delete this service?";
		var delete_text = "Your service has been deleted";
	}
	$(document).on('click','.si-delete-service',function() {
		var s_id = $(this).attr("data-id");
		$('#deleteConfirmModal').modal('toggle');
		$('#acc_title').html('<i>'+delete_title+'</i>');
		$('#acc_msg').html(delete_msg);
		
		$(document).on('click','.si_accept_confirm',function(){
			var dataString="s_id="+s_id+"&csrf_token_name="+csrf_token;
			var url = base_url+'user/service/delete_service';
			$.ajax({
				url:url,
				data:{s_id:s_id,csrf_token_name:csrf_token},
				type:"POST",
				beforeSend:function(){
					$('#deleteConfirmModal').modal('toggle');
				},
				success: function(res){
					if(res==1) {
						window.location = base_url+'my-services';
					}else if(res==2){
						window.location = base_url+'my-services';
					}
				}
			});
		});
		$(document).on('click','.si_accept_cancel',function(){
		});
	});

	$(document).on('click','.si-delete-inactive-service',function() {
		var s_id = $(this).attr("data-id");
		$('#deleteConfirmModal').modal('toggle');
		$('#acc_title').html('<i>'+delete_title+'</i>');
		$('#acc_msg').html(delete_msg);
		
		$(document).on('click','.si_accept_confirm',function(){
			var dataString="s_id="+s_id;
			var url = base_url+'user/service/delete_inactive_service';
			$.ajax({
				url:url,
				data:{s_id:s_id,csrf_token_name:csrf_token},
				type:"POST",
				beforeSend:function(){
					$('#deleteConfirmModal').modal('toggle');
				},
				success: function(res){
					if(res==1) {
						window.location = base_url+'my-services-inactive';
					}else if(res==2){
						window.location = base_url+'my-services-inactive';
					}
				}
			});
		});
		$(document).on('click','.si_accept_cancel',function(){
		});
	});
	
	$(document).on('click','.si-delete-active-service',function() {
		var s_id = $(this).attr("data-id");
		$('#deleteConfirmModal').modal('toggle');
		$('#acc_title').html('<i>'+delete_active_title+'</i>');
		$('#acc_msg').html(delete_active_msg);
		
		$(document).on('click','.si_accept_confirm',function(){
			var dataString="s_id="+s_id;
			var url    =  base_url+'user/service/delete_active_service';
			$.ajax({
				url:url,
				data:{s_id:s_id,csrf_token_name:csrf_token},
				type:"POST",
				beforeSend:function(){
					$('#deleteConfirmModal').modal('toggle');
				},
				success: function(res){
					if(res==1) {
						window.location = base_url+'my-services-inactive';
					}else if(res==2){
						window.location = base_url+'my-services-inactive';
					}
				}
			});
		});
	});

	$(window).on('load',function(){
		$('.page-loading').fadeOut();
	});
	
})(jQuery);