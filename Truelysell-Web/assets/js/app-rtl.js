	if($('.service-slider').length > 0 ){
		$('.service-slider').owlCarousel({
			rtl:true,
			loop: true,
			center: true,
			margin: 30,
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
		})	
	}
