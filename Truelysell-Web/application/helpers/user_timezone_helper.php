<?php 

  if(!function_exists('utc_date_conversion')){
 function utc_date_conversion($utc){
    $timezone =  'Asia/kolkata';
     
    $dt = new DateTime($utc . ' UTC' );
    $tz = new DateTimeZone($timezone); // or whatever zone you're after
    $dt->setTimezone($tz);
    return $dt->format('Y-m-d H:i:s');
  }
}




  if(!function_exists('time_differences_ago')){
	 function time_differences_ago($from,$to){

	 	$start_date = new DateTime($from);
		$since_start = $start_date->diff(new DateTime($to));
		
		$ago = 'Now';
		
		if($since_start->y!=0){
			$ago = $since_start->y.' years';
		}else if($since_start->m!=0){
		  $ago = $since_start->m.' months';	
		}else if($since_start->d!=0){
		  $ago = $since_start->d.' days';	
		}else if($since_start->h!=0){
		  $ago = $since_start->h.' hours';
		}else if($since_start->i!=0){
		  $ago = $since_start->i.' minutes';
		}else if($since_start->s!=0){
		  $ago = $since_start->s.' seconds';
		  $ago = 'Now';
		}
		return $ago; 
  }
}


 ?>