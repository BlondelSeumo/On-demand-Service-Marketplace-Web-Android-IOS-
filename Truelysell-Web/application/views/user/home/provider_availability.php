<?php
	$provider_availability = $this->home->get_availability($this->session->userdata('id'));
	$get_details = $this->db->where('id',$this->session->userdata('id'))->get('providers')->row_array();
	
	if(is_array($provider_availability) && !empty($provider_availability)) {
		$ava = '1';
	}
	else {
		$ava  = '0';
	}

	?>
<div class="content">
	<div class="container">
		<div class="row">
			<?php $this->load->view('user/home/provider_sidemenu');?>
			<div class="col-xl-9 col-md-8">
				<div class="card">
					<div class="card-body">
						<form method="post" enctype="multipart/form-data" autocomplete="off" id="update_service" action="<?php echo base_url()?>create_availability">
							
<input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
                            <div class="form-group">
								<p><?php echo (!empty($user_language[$user_selected]['lg_Availability'])) ? $user_language[$user_selected]['lg_Availability'] : $default_language['en']['lg_Availability']; ?> <span class="text-danger">*</span></p>
          <div class="row">
            <div class="col-md-12">
            <div class="table-responsive">
            <table class="table availability-table">
            <tbody>
            <?php
            
            $availability_details = (!empty($provider_availability['availability']))?$provider_availability['availability']:'';
            if(!empty($availability_details)){
            $availability_details = json_decode($availability_details,true);
            }

           

            $days = array();
            $Monday_from_time = '';
            $Monday_to_time = '';
            $Tuesday_from_time = '';
            $Tuesday_to_time = '';
            $Wednesday_from_time = '';
            $Wednesday_to_time = '';
            $Thursday_from_time = '';
            $Thursday_to_time = '';
            $Friday_from_time = '';
            $Friday_to_time = '';
            $Saturday_from_time = '';
            $Saturday_to_time = '';
            $Sunday_from_time = '';
            $Sunday_to_time = '';
            $Monday_checked = '';
            $Tuesday_checked = '';
            $Wednesday_checked = '';
            $Thursday_checked = '';
            $Friday_checked = '';
            $Saturday_checked = '';
            $Sunday_checked = '';
            if($availability_details != '') {
            foreach ($availability_details as $availability) {
            $days[] = $availability['day'];
            if($availability['day'] == 1){
            $Monday = '';
            $Monday_checked = 'checked';
            $Monday_from_time = $availability['from_time'];
            if(isset($availability['to_time'])){
            $Monday_to_time = $availability['to_time'];    
            }else{
                $Monday_to_time ='11:30 AM';
            }
            }
            if($availability['day'] == 2){
            $Tuesday = '';
            $Tuesday_checked = 'checked';
            $Tuesday_from_time = $availability['from_time'];
            $Tuesday_to_time = $availability['to_time'];
            }
            if($availability['day'] == 3){
            $Wednesday = '';
            $Wednesday_checked = 'checked';
            $Wednesday_from_time = $availability['from_time'];
            $Wednesday_to_time = $availability['to_time'];
            }
            if($availability['day'] == 4){
            $Thursday = '';
            $Thursday_checked = 'checked';
            $Thursday_from_time = $availability['from_time'];
            if(isset($availability['to_time'])){
              $Thursday_to_time = $availability['to_time'];
            }else{
              $Thursday_to_time ="11:30 AM";
            }
           
            }
            if($availability['day'] == 5){
            $Friday = '';
            $Friday_checked = 'checked';
            $Friday_from_time = $availability['from_time'];
            $Friday_to_time = $availability['to_time'];
            }
            if($availability['day'] == 6){
            $Saturday = '';
            $Saturday_checked = 'checked';
            $Saturday_from_time = $availability['from_time'];
            $Saturday_to_time = $availability['to_time'];
            }
            if($availability['day'] == 7){
            $Sunday = '';
            $Sunday_checked = 'checked';
            $Sunday_from_time = $availability['from_time'];
            $Sunday_to_time = $availability['to_time'];
            }
            }
            }
            $day_count = count($days);
            if(!empty($provider_availability['all_days'])){
                $checked = ($provider_availability['all_days']=='1')?'checked':'';
            }else{
                $checked='';
            }
            
            $Monday = '';
            $Tuesday = '';
            $Wednesday = '';
            $Thursday = '';
            $Friday = '';
            $Saturday = '';
            $Sunday = '';
            if($day_count == 7)
            {
                 if(!empty($provider_availability['all_days'])){
            $checked = ($provider_availability['all_days']=='1')?'checked':'';
                }else{
                    $checked ='';
                }
            $Monday = 'disabled';
            $Tuesday = 'disabled';
            $Wednesday = 'disabled';
            $Thursday = 'disabled';
            $Friday = 'disabled';
            $Saturday = 'disabled';
            $Sunday = 'disabled';
            if(!empty($provider_availability['all_days'])){
            if($provider_availability['all_days']=='1'){
                $All_from_time = $Monday_from_time;
               $All_to_time = $Monday_to_time;
            }else{
                     $All_from_time = '-';
                    $All_to_time = '-';
            }
            }else{
                   $All_from_time = '-';
                        $All_to_time = '-';
            }
            
            $Monday_checked = '';
            $Tuesday_checked = '';
            $Wednesday_checked = '';
            $Thursday_checked = '';
            $Friday_checked = '';
            $Saturday_checked = '';
            $Sunday_checked = '';
            }
            else {
                if(!empty($provider_availability['all_days'])){
            $checked = ($provider_availability['all_days']=='1')?'checked':'';
            }else{
                $checked ='';
            }
            $All_from_time = '-';
            $All_to_time = '-';
            }


            ?>

            <tr>
            <td>
            <input type="checkbox" class="err_check days_check mr-1 validate_time" data-id="0" name="availability[0][day]" value="1" <?php echo $checked; ?>><?php echo (!empty($user_language[$user_selected]['lg_All_Days'])) ? $user_language[$user_selected]['lg_All_Days'] : $default_language['en']['lg_All_Days']; ?>
            </td>
            <td class="">
            <?php echo (!empty($user_language[$user_selected]['lg_From_time'])) ? $user_language[$user_selected]['lg_From_time'] : $default_language['en']['lg_From_time']; ?> <span class="time-select mb-1">
            <select class="form-control daysfromtime_check"  name="availability[0][from_time]">
            <?php echo from_time(1, $All_from_time); ?>
            </select></span>
            </td>
            <td class="">
            <?php echo (!empty($user_language[$user_selected]['lg_To_time'])) ? $user_language[$user_selected]['lg_To_time'] : $default_language['en']['lg_To_time']; ?> <span class="time-select">
            <select class="form-control daystotime_check" name="availability[0][to_time]">
            <?php echo to_time(1, $All_to_time); ?>s
            </select></span>
            </td>
            </tr>
            <?php
            $day_name = array();
            $day_name[1] = 'Monday';
            $day_name[2] = 'Tuesday';
            $day_name[3] = 'Wednesday';
            $day_name[4] = 'Thursday';
            $day_name[5] = 'Friday';
            $day_name[6] = 'Saturday';
            $day_name[7] = 'Sunday';

            $day_name_checked = array();
            $day_name_checked[1] = 'Monday_checked';
            $day_name_checked[2] = 'Tuesday_checked';
            $day_name_checked[3] = 'Wednesday_checked';
            $day_name_checked[4] = 'Thursday_checked';
            $day_name_checked[5] = 'Friday_checked';
            $day_name_checked[6] = 'Saturday_checked';
            $day_name_checked[7] = 'Sunday_checked';

            $var_from_time = array();
            $var_from_time[1] = 'Monday_from_time';
            $var_from_time[2] = 'Tuesday_from_time';
            $var_from_time[3] = 'Wednesday_from_time';
            $var_from_time[4] = 'Thursday_from_time';
            $var_from_time[5] = 'Friday_from_time';
            $var_from_time[6] = 'Saturday_from_time';
            $var_from_time[7] = 'Sunday_from_time';

            $var_to_time = array();
            $var_to_time[1] = 'Monday_to_time';
            $var_to_time[2] = 'Tuesday_to_time';
            $var_to_time[3] = 'Wednesday_to_time';
            $var_to_time[4] = 'Thursday_to_time';
            $var_to_time[5] = 'Friday_to_time';
            $var_to_time[6] = 'Saturday_to_time';
            $var_to_time[7] = 'Sunday_to_time';
              
            
            
            
            
              ?>
			  
              <!-- monday -->

              <?php
              if(@$availability_details[0]['day']==1){
                $monday_check='checked';
                $mon_from_time=$availability_details[0]['from_time'];
                if(isset($availability_details[0]['to_time'])){
 $mon_to_time=$availability_details[0]['to_time'];
                }else{
                     $mon_to_time="11:30 AM";
                }
               
              }else{
                $monday_check='';
                $mon_from_time='';
                $mon_to_time='';
              }
              ?>
            <tr>
            <td>
            <input type="checkbox" data-id="1" class="eachdays err_check eachdays1 validate_time"  name="availability[1][day]" value="1" <?=$monday_check;?> <?php echo $day_name_checked[1]; ?> <?php echo $day_name[1]; ?>> <?php echo $day_name[1]; ?>
            </td>
            <td class="">
            From time <span class="time-select mb-1">
            <select class="form-control eachdayfromtime eachdayfromtime1 select_from_time" data-id="1"  name="availability[1][from_time]" <?php echo $day_name[1]; ?>>
              <?php
              if(!empty($mon_from_time)){ ?>
<option value="<?=$mon_from_time?>"><?=$mon_from_time?></option>
              <?php }
              ?>
              
            <?php echo from_time(1, $var_from_time[1]); ?>
            </select></span>
            </td>
            <td class="">
            To time <span class="time-select" >
            <select class="form-control eachdaytotime eachdaytotime1" name="availability[1][to_time]" <?php echo $day_name[1]; ?>>
                             <?php
              if(!empty($mon_to_time)){ ?>
<option value="<?=$mon_to_time?>"><?=$mon_to_time?></option>
              <?php }
              ?>
               
            <?php echo to_time(1, $var_to_time[1]); ?>
            </select>
            </span>
            </td>
            </tr>


            <!-- tuesday -->

            <?php

              if(@$availability_details[0]['day']==2){
                $tues_check='checked';
                $tue_from_time=$availability_details[0]['from_time'];
                $tue_to_time=$availability_details[0]['to_time'];
              }elseif(@$availability_details[1]['day']==2){
                $tues_check='checked';
                $tue_from_time=$availability_details[1]['from_time'];
                $tue_to_time=$availability_details[1]['to_time'];
              }else{
                $tues_check='';
                $tue_from_time='';
                $tue_to_time='';
              }
               
             
              

            ?>
            <tr>
            <td>
            <input type="checkbox"  data-id="2" class="eachdays err_check eachdays2 validate_time"   name="availability[2][day]" value="2" <?=$tues_check;?> <?php echo $day_name_checked[2]; ?> <?php echo $day_name[2]; ?>> <?php echo $day_name[2]; ?>
            </td>
            <td class="">
            From time <span class="time-select mb-1">
            <select class="form-control eachdayfromtime eachdayfromtime2 select_from_time" data-id="2" name="availability[2][from_time]" <?php echo $day_name[2]; ?>>
                                           <?php
              if(!empty($tue_from_time)){ ?>
<option value="<?=$tue_from_time?>"><?=$tue_from_time?></option>
              <?php }
              ?>
            
            <?php echo from_time(1, $var_from_time[2]); ?>
            </select></span>
            </td>
            <td class="">
            To time <span class="time-select" >
            <select class="form-control eachdaytotime eachdaytotime2" name="availability[2][to_time]" <?php echo $day_name[2]; ?>>
               
        <?php
              if(!empty($tue_to_time)){ ?>
<option value="<?=$tue_to_time?>"><?=$tue_to_time?></option>
              <?php }
              ?>
            <?php echo to_time(1, $var_to_time[2]); ?>
            </select>
            </span>
            </td>
            </tr>


                        <!-- wednesday -->


            <?php

              if(@$availability_details[0]['day']==3){
                $wed_check='checked';
                $wed_from_time=$availability_details[0]['from_time'];
                $wed_to_time=$availability_details[0]['to_time'];
              }elseif(@$availability_details[1]['day']==3){
                $wed_check='checked';
                $wed_from_time=$availability_details[1]['from_time'];
                $wed_to_time=$availability_details[1]['to_time'];
              }elseif(@$availability_details[2]['day']==3){
                $wed_check='checked';
                $wed_from_time=$availability_details[2]['from_time'];
                $wed_to_time=$availability_details[2]['to_time'];
              }else{
                $wed_check='';
                $wed_from_time='';
                $wed_to_time='';
              }
               
             
              

            ?>
            <tr>
            <td>
            <input type="checkbox"  data-id="3" class="eachdays err_check eachdays3 validate_time"  name="availability[3][day]" value="3" <?=$wed_check;?> <?php echo $day_name_checked[3]; ?> <?php echo $day_name[3]; ?>> <?php echo $day_name[3]; ?>
            </td>
            <td class="">
            <?php echo (!empty($user_language[$user_selected]['lg_From_time'])) ? $user_language[$user_selected]['lg_From_time'] : $default_language['en']['lg_From_time']; ?> <span class="time-select mb-1">
            <select class="form-control eachdayfromtime eachdayfromtime3 select_from_time" data-id="3"  name="availability[3][from_time]" <?php echo $day_name[3]; ?>>
                      <?php
              if(!empty($wed_from_time)){ ?>
<option value="<?=$wed_from_time?>"><?=$wed_from_time?></option>
              <?php }
              ?>
            <?php echo from_time(1, $var_from_time[3]); ?>
            </select></span>
            </td>
            <td class="">
            <?php echo (!empty($user_language[$user_selected]['lg_To_time'])) ? $user_language[$user_selected]['lg_To_time'] : $default_language['en']['lg_To_time']; ?> <span class="time-select" >
            <select class="form-control eachdaytotime eachdaytotime3" name="availability[3][to_time]" <?php echo $day_name[3]; ?>>
                      <?php
              if(!empty($wed_to_time)){ ?>
<option value="<?=$wed_to_time?>"><?=$wed_to_time?></option>
              <?php }
              ?>
            <?php echo to_time(1, $var_to_time[3]); ?>
            </select>
            </span>
            </td>
            </tr>


                        <!-- thursday -->

                          <?php

              if(@$availability_details[0]['day']==4){
                $thur_check='checked';
                $thur_from_time=$availability_details[0]['from_time'];
                $thur_to_time=$availability_details[0]['to_time'];
              }elseif(@$availability_details[1]['day']==4){
                $thur_check='checked';
                $thur_from_time=$availability_details[1]['from_time'];
                $thur_to_time=$availability_details[1]['to_time'];
              }elseif(@$availability_details[2]['day']==4){
                $thur_check='checked';
                $thur_from_time=$availability_details[2]['from_time'];
                $thur_to_time=$availability_details[2]['to_time'];
              }elseif(@$availability_details[3]['day']==4){
                $thur_check='checked';
                $thur_from_time=$availability_details[3]['from_time'];
                if(isset($availability_details[3]['to_time'])){
                 $thur_to_time=$availability_details[3]['to_time'];
                }else{
                    $thur_to_time="11:30 AM";
                }
                
           
              }else{
                $thur_check='';
                $thur_from_time='';
                $thur_to_time='';
              }
               
             
              

            ?>
            <tr>
            <td>
            <input type="checkbox"  data-id="4" class="eachdays err_check eachdays4 validate_time"  name="availability[4][day]" value="4" <?=$thur_check;?> <?php echo $day_name_checked[4]; ?> <?php echo $day_name[4]; ?>> <?php echo $day_name[4]; ?>
            </td>
            <td class="">
            <?php echo (!empty($user_language[$user_selected]['lg_From_time'])) ? $user_language[$user_selected]['lg_From_time'] : $default_language['en']['lg_From_time']; ?> <span class="time-select mb-1">
            <select class="form-control eachdayfromtime eachdayfromtime4 select_from_time" data-id="4" name="availability[4][from_time]" <?php echo $day_name[4]; ?>>
                                    <?php
              if(!empty($thur_from_time)){ ?>
<option value="<?=$thur_from_time?>"><?=$thur_from_time?></option>
              <?php }
              ?>
            <?php echo from_time(1, $var_from_time[4]); ?>
            </select></span>
            </td>
            <td class="">
            <?php echo (!empty($user_language[$user_selected]['lg_To_time'])) ? $user_language[$user_selected]['lg_To_time'] : $default_language['en']['lg_To_time']; ?> <span class="time-select" >
            <select class="form-control eachdaytotime eachdaytotime4" name="availability[4][to_time]" <?php echo $day_name[4]; ?>>
                                     <?php
              if(!empty($thur_to_time)){ ?>
<option value="<?=$thur_to_time?>"><?=$thur_to_time?></option>
              <?php }
              ?>
            <?php echo to_time(1, $var_to_time[4]); ?>
            </select>
            </span>
            </td>
            </tr>


                        <!-- friday -->

                                      <?php

              if(@$availability_details[0]['day']==5){
                $fri_check='checked';
                $fri_from_time=$availability_details[0]['from_time'];
                $fri_to_time=$availability_details[0]['to_time'];
              }elseif(@$availability_details[1]['day']==5){
                $fri_check='checked';
                $fri_from_time=$availability_details[1]['from_time'];
                $fri_to_time=$availability_details[1]['to_time'];
              }elseif(@$availability_details[2]['day']==5){
                $fri_check='checked';
                $fri_from_time=$availability_details[2]['from_time'];
                $fri_to_time=$availability_details[2]['to_time'];
              }elseif(@$availability_details[3]['day']==5){
                $fri_check='checked';
                $fri_from_time=$availability_details[3]['from_time'];
                $fri_to_time=$availability_details[3]['to_time'];
              }elseif(@$availability_details[4]['day']==5){
                $fri_check='checked';
                $fri_from_time=$availability_details[4]['from_time'];
                $fri_to_time=$availability_details[4]['to_time'];
              }else{
                $fri_check='';
                $fri_from_time='';
                $fri_to_time='';
              }
               
             
              

            ?>
            <tr>
            <td>
            <input type="checkbox"  data-id="5" class="eachdays err_check eachdays5 validate_time"  name="availability[5][day]" value="5" <?=$fri_check;?> <?php echo $day_name_checked[5]; ?> <?php echo $day_name[5]; ?>> <?php echo $day_name[5]; ?>
            </td>
            <td class="">
            <?php echo (!empty($user_language[$user_selected]['lg_From_time'])) ? $user_language[$user_selected]['lg_From_time'] : $default_language['en']['lg_From_time']; ?> <span class="time-select mb-1">
            <select class="form-control eachdayfromtime eachdayfromtime5 select_from_time" data-id="5" name="availability[5][from_time]" <?php echo $day_name[5]; ?>>
                                                 <?php
              if(!empty($fri_from_time)){ ?>
<option value="<?=$fri_from_time?>"><?=$fri_from_time?></option>
              <?php }
              ?>
            <?php echo from_time(1, $var_from_time[5]); ?>
            </select></span>
            </td>
            <td class="">
            <?php echo (!empty($user_language[$user_selected]['lg_To_time'])) ? $user_language[$user_selected]['lg_To_time'] : $default_language['en']['lg_To_time']; ?> <span class="time-select" >
            <select class="form-control  custom-height eachdaytotime eachdaytotime5" name="availability[5][to_time]" <?php echo $day_name[5]; ?>>
                                                  <?php
              if(!empty($fri_to_time)){ ?>
<option value="<?=$fri_to_time?>"><?=$fri_to_time?></option>
              <?php }
              ?>
            <?php echo to_time(1, $var_to_time[5]); ?>
            </select>
            </span>
            </td>
            </tr>

                        <!-- saturday -->

                                                  <?php

              if(@$availability_details[0]['day']==6){
                $sat_check='checked';
                $sat_from_time=$availability_details[0]['from_time'];
                $sat_to_time=$availability_details[0]['to_time'];
              }elseif(@$availability_details[1]['day']==6){
                $sat_check='checked';
                $sat_from_time=$availability_details[1]['from_time'];
                $sat_to_time=$availability_details[1]['to_time'];
              }elseif(@$availability_details[2]['day']==6){
                $sat_check='checked';
                $sat_from_time=$availability_details[2]['from_time'];
                $sat_to_time=$availability_details[2]['to_time'];
              }elseif(@$availability_details[3]['day']==6){
                $sat_check='checked';
                $sat_from_time=$availability_details[3]['from_time'];
                $sat_to_time=$availability_details[3]['to_time'];
              }elseif(@$availability_details[4]['day']==6){
                $sat_check='checked';
                $sat_from_time=$availability_details[4]['from_time'];
                $sat_to_time=$availability_details[4]['to_time'];
              }elseif(@$availability_details[5]['day']==6){
                $sat_check='checked';
                $sat_from_time=$availability_details[5]['from_time'];
                $sat_to_time=$availability_details[5]['to_time'];
              }else{
                $sat_check='';
                $sat_from_time='';
                $sat_to_time='';
              }
               
             
              

            ?>
            <tr>
            <td>
            <input type="checkbox"  data-id="6" class="eachdays err_check eachdays6 validate_time"  name="availability[6][day]" value="6" <?=$sat_check;?> <?php echo $day_name_checked[6]; ?> <?php echo $day_name[6]; ?>> <?php echo $day_name[6]; ?>
            </td>
            <td class="">
            <?php echo (!empty($user_language[$user_selected]['lg_From_time'])) ? $user_language[$user_selected]['lg_From_time'] : $default_language['en']['lg_From_time']; ?> <span class="time-select mb-1">
            <select class="form-control eachdayfromtime eachdayfromtime6 select_from_time" data-id="6" name="availability[6][from_time]" <?php echo $day_name[6]; ?>>
                  <?php
              if(!empty($sat_from_time)){ ?>
<option value="<?=$sat_from_time?>"><?=$sat_from_time?></option>
              <?php }
              ?>
            <?php echo from_time(1, $var_from_time[6]); ?>
            </select></span>
            </td>
            <td class="">
            <?php echo (!empty($user_language[$user_selected]['lg_To_time'])) ? $user_language[$user_selected]['lg_To_time'] : $default_language['en']['lg_To_time']; ?> <span class="time-select" >
            <select class="form-control eachdaytotime eachdaytotime6" name="availability[6][to_time]" <?php echo $day_name[6]; ?>>
                                 <?php
              if(!empty($sat_to_time)){ ?>
<option value="<?=$sat_to_time?>"><?=$sat_to_time?></option>
              <?php }
              ?>
            <?php echo to_time(1, $var_to_time[6]); ?>
            </select>
            </span>
            </td>
            </tr>

                        <!-- sunday -->
                                                                <?php

              if(@$availability_details[0]['day']==7){
                $sun_check='checked';
                $sun_from_time=$availability_details[0]['from_time'];
                $sun_to_time=$availability_details[0]['to_time'];
              }elseif(@$availability_details[1]['day']==7){
                $sun_check='checked';
                $sun_from_time=$availability_details[1]['from_time'];
                $sun_to_time=$availability_details[1]['to_time'];
              }elseif(@$availability_details[2]['day']==7){
                $sun_check='checked';
                $sun_from_time=$availability_details[2]['from_time'];
                $sun_to_time=$availability_details[2]['to_time'];
              }elseif(@$availability_details[3]['day']==7){
                $sun_check='checked';
                $sun_from_time=$availability_details[3]['from_time'];
                $sun_to_time=$availability_details[3]['to_time'];
              }elseif(@$availability_details[4]['day']==7){
                $sun_check='checked';
                $sun_from_time=$availability_details[4]['from_time'];
                $sun_to_time=$availability_details[4]['to_time'];
              }elseif(@$availability_details[5]['day']==7){
                $sun_check='checked';
                $sun_from_time=$availability_details[5]['from_time'];
                $sun_to_time=$availability_details[5]['to_time'];
              }elseif(@$availability_details[6]['day']==7){
                $sun_check='checked';
                $sun_from_time=$availability_details[6]['from_time'];
                $sun_to_time=$availability_details[6]['to_time'];
              }else{
                $sun_check='';
                $sun_from_time='';
                $sun_to_time='';
              }
               
             
              

            ?>
            <tr>
            <td>
            <input type="checkbox"  data-id="7" class="eachdays err_check eachdays7 validate_time"  name="availability[7][day]" value="7" <?=$sun_check;?> <?php echo $day_name_checked[7]; ?> <?php echo $day_name[7]; ?>> <?php echo $day_name[7]; ?>
            </td>
            <td class="">
            <?php echo (!empty($user_language[$user_selected]['lg_From_time'])) ? $user_language[$user_selected]['lg_From_time'] : $default_language['en']['lg_From_time']; ?> <span class="time-select mb-1">
            <select class="form-control eachdayfromtime eachdayfromtime7 select_from_time" data-id="7" name="availability[7][from_time]" <?php echo $day_name[7]; ?>>
                                               <?php
              if(!empty($sun_from_time)){ ?>
<option value="<?=$sun_from_time?>"><?=$sun_from_time?></option>
              <?php }
              ?>
            <?php echo from_time(1, $var_from_time[7]); ?>
            </select></span>
            </td>
            <td class="">
            <?php echo (!empty($user_language[$user_selected]['lg_To_time'])) ? $user_language[$user_selected]['lg_To_time'] : $default_language['en']['lg_To_time']; ?> <span class="time-select" >
            <select class="form-control eachdaytotime eachdaytotime7" name="availability[7][to_time]" <?php echo $day_name[7]; ?>>
                                                <?php
              if(!empty($sun_to_time)){ ?>
<option value="<?=$sun_to_time?>"><?=$sun_to_time?></option>
              <?php }
              ?>
            <?php echo to_time(1, $var_to_time[7]); ?>
            </select>
            </span>
            </td>
            </tr>


            <?php

            function from_time($selected='', $my_from_time)
            {
            if($my_from_time == '-'){
            $time_from = '<option value="" selected>Select Time</option>';
            }
            else{
            $time_from = '<option value="">Select Time</option>';
            }
            for ($j=0; $j <=23 ; $j++) {
            if($j <10){
            $k = '0'.$j;
            }else{
            $k = $j;
            }
            if($j <12){
            if($j <10){
            $label1 = '0'.$j.':00 AM';
            $label2 = '0'.$j.':30 AM';
            }else{
            $label1 = $j.':00 AM';
            $label2 = $j.':30 AM';
            }

            }else{
            $s = ($j-12);

            if($s ==0){ $s = 12; }

            if($s <10){
            $label1 = '0'.$s.':00 PM';
            $label2 = '0'.$s.':30 PM';
            }else{
            $label1 = $s.':00 PM';
            $label2 = $s.':30 PM';
            }
            }
            if($my_from_time == $label1){
            $time_from .= '<option value="'.$label1.'" selected>'.$label1.'</option>';
            }else{
            $time_from .= '<option value="'.$label1.'">'.$label1.'</option>';
            }

            if($my_from_time == $label2){
            $time_from .= '<option value="'.$label2.'" selected>'.$label2.'</option>';
            }else{
            $time_from .= '<option value="'.$label2.'">'.$label2.'</option>';
            }
            }
            return $time_from;
            }

            function to_time($selected='', $my_to_time)
            {  
            if($my_to_time == '-'){
            $to_time = '<option value="" selected>Select Time</option>';
            }
            else{
            $to_time = '<option value="">Select Time</option>';
            }
            for ($j=0; $j <=23 ; $j++) {

            if($j <10){
            $k = '0'.$j;
            }else{
            $k = $j;
            }

            if($j <12){
            if($j <10){
            $label1 = '0'.$j.':00 AM';
            $label2 = '0'.$j.':30 AM';
            }else{
            $label1 = $j.':00 AM';
            $label2 = $j.':30 AM';
            }

            }else{
            $s = ($j-12);

            if($s ==0){ $s = 12; }

            if($s <10){
            $label1 = '0'.$s.':00 PM';
            $label2 = '0'.$s.':30 PM';
            }else{
            $label1 = $s.':00 PM';
            $label2 = $s.':30 PM';
            }
            }
            
            if($my_to_time == $label1){
            $to_time .= '<option value="'.$label1.'" selected>'.$label1.'</option>';
            }else{
            $to_time .= '<option value="'.$label1.'">'.$label1.'</option>';
            }

            if($my_to_time == $label2){
            $to_time .= '<option value="'.$label2.'" selected>'.$label2.'</option>';
            }else{
            $to_time .= '<option value="'.$label2.'">'.$label2.'</option>';
            }
            }
            if(!empty($my_to_time)&&strpos($my_to_time, 'AM')||strpos($my_to_time, 'PM')){
               $to_time .= '<option value="'.$my_to_time.'" selected>'.$my_to_time.'</option>';
            }
            return $to_time;
            } 
            ?>


            </tbody>
            </table>
            </div>
            </div>
          </div>
            </div>
         <div class="submit-section text-center">
               <button class="btn btn-primary submit-btn" type="submit" name="form_submit" id="time_submit" value="submit"><?php echo (!empty($user_language[$user_selected]['lg_Submit'])) ? $user_language[$user_selected]['lg_Submit'] : $default_language['en']['lg_Submit']; ?></button>
         </div>
    </form>
      

                     </div>
                     </div>
								
								
                    </div>
                </div>
	  
   </div>

</body>
</html>
 