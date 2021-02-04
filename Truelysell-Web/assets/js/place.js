(function($) {
	"use strict";
	var placeSearch, autocomplete;
	initialize();
	current_location(0);
	
	var base_url=$('#base_url').val();
	var BASE_URL=$('#base_url').val();
	var csrf_token=$('#csrf_token').val();
	var csrfName=$('#csrfName').val();
	var csrfHash=$('#csrfHash').val();
	$( document ).ready(function() {
		$('.current_location').on('click',function(){
			var id=$(this).attr('data-id');
			current_location(id);
		}); 
	});
	function initialize() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    autocomplete = new google.maps.places.Autocomplete(
    	/** @type {HTMLInputElement} */
    	(document.getElementById('user_address')), {
    		types: ['geocode']
    	});

    google.maps.event.addDomListener(document.getElementById('user_address'), 'focus', geolocate);
    autocomplete.addListener('place_changed', get_latitude_longitude);
}

function get_latitude_longitude() {
	// Get the place details from the autocomplete object.
	var place = autocomplete.getPlace();
	 var key = $("#map_key").val();//alert(map_key);
	//var key = "AIzaSyDYHKDyN1YX7hWTGtYdLb1F0UAOVwK1MKc";
	$.get('https://maps.googleapis.com/maps/api/geocode/json',{address:place.formatted_address,key:key},function(data, status){

		$(data.results).each(function(key,value){
			 $('#user_address').val(place.formatted_address);
             $('#user_latitude').val(value.geometry.location.lat);
             $('#user_longitude').val(value.geometry.location.lng);
			$.post(base_url+'home/set_location',{address:place.formatted_address,latitude:value.geometry.location.lat,longitude:value.geometry.location.lng,csrf_token_name:csrf_token});
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

function current_location(session) {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(showPosition);
	}
	else {
		alert("Geolocation is not supported by this browser.");
	}
	
	function showPosition(position) {
		var user_address=$('#user_address_values').val();
		var user_latitude=$('#user_latitude_values').val();
		var user_longitude=$('#user_longitude_values').val();
		var latlng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
		var geocoder = geocoder = new google.maps.Geocoder();
		geocoder.geocode({ 'latLng': latlng }, function (results, status) {

			if (status == google.maps.GeocoderStatus.OK) {
				if (results[3]) { 
					if(session==1) {
						$('#user_address').val(results[3].formatted_address);
						$('#user_latitude').val(position.coords.latitude);
						$('#user_longitude').val(position.coords.longitude);

						$.post(base_url+'home/set_location',{address:results[3].formatted_address,latitude:position.coords.latitude,longitude:position.coords.longitude,csrf_token_name:csrf_token})
					}
					else {
						if(user_address=='' && user_latitude=='' && user_longitude=='') {
							$('#user_address').val(results[3].formatted_address);
							$('#user_latitude').val(position.coords.latitude);
							$('#user_longitude').val(position.coords.longitude);
							$.post(base_url+'home/set_location',{address:results[3].formatted_address,latitude:position.coords.latitude,longitude:position.coords.longitude,csrf_token_name:csrf_token})
						}
					}
				}
			}
		});
	}
}

})(jQuery);