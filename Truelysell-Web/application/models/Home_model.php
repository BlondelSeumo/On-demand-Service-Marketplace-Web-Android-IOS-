<?php
if ( ! defined('BASEPATH')) exit('No direct script access allowed');
class Home_model extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
    	 date_default_timezone_set('Asia/kolkata');

	}
        
        
 

    public function get_category()
	{

			
		       $this->db->select('c.id,c.category_name,c.category_image, (SELECT COUNT(s.id) FROM services AS s WHERE s.category=c.id AND s.status=1 ) AS category_count');
		       $this->db->from('categories c');
		       $this->db->where('c.status',1);
		       $this->db->order_by('category_count','DESC');
		       $this->db->limit(6);
	           $result = $this->db->get()->result_array();
	           return $result;

		
	}

	public function get_category_id($category_name)
	{
		return $this->db->select('id')->where('category_name',rawurldecode(utf8_decode($category_name)))->get('categories')->row()->id;
	}

	public function get_service()
	{
	
          $this->db->select("s.id,s.user_id,s.service_title,s.service_amount,s.service_location,s.service_image,c.category_name,s.currency_code");
	      $this->db->from('services s');
	      $this->db->join('categories c', 'c.id = s.category', 'LEFT');
	      $this->db->where("s.status = 1");
          $this->db->join('subscription_details as sd','sd.subscriber_id=s.user_id','LEFT');
          $this->db->where('sd.expiry_date_time>=',date('Y-m-d'));
        if(!empty($this->session->userdata('user_address'))){
          
           $this->db->like("s.service_location",$this->session->userdata('user_address'));
           
        }
        if(!empty($this->session->userdata('current_location'))){
          $this->db->like("s.service_location",$this->session->userdata('current_location'));
        }
     
	      $this->db->order_by('s.total_views','DESC');
	      $this->db->limit(10);
	      $query = $this->db->get(); $result = array(); if($query !== FALSE && $query->num_rows() > 0){ $result = $query->result_array(); }

        if(count($result)==0){
            $this->db->select("s.id,s.user_id,s.service_title,s.service_amount,s.service_location,s.service_image,c.category_name,s.currency_code");
          $this->db->from('services s');
          $this->db->join('categories c', 'c.id = s.category', 'LEFT');
          $this->db->where("s.status = 1");
          $this->db->join('subscription_details as sd','sd.subscriber_id=s.user_id','LEFT');
          $this->db->where('sd.expiry_date_time>=',date('Y-m-d'));
          if(!empty($this->session->userdata('current_location'))){
            $this->db->like("s.service_location",$this->session->userdata('current_location'));
          }
       
          $this->db->order_by('s.total_views','DESC');
          $this->db->limit(10);
          $result = $this->db->get()->result_array();
          return $result;
        }else{
          return $result;
        }
          
          
    }

    public function get_service_details($inputs)
	{
	
        $this->db->select("s.*,c.category_name");
	      $this->db->from('services s');
	      $this->db->join('categories c', 'c.id = s.category', 'LEFT');
	      $this->db->where("s.status = 1 AND md5(s.id)='".$inputs['id']."'");
	      $result = $this->db->get()->row_array();
          return $result;
    }


    function get_all_service($params = array(),$inputs=array()){ 

          $this->db->select("s.id,s.user_id,s.service_title,s.service_amount,s.service_location,s.service_image,c.category_name,s.currency_code");
	      $this->db->from('services s');
	      $this->db->join('categories c', 'c.id = s.category', 'LEFT');
	      $this->db->where("s.status = 1");
        $this->db->join('subscription_details as sd','sd.subscriber_id=s.user_id','LEFT');
        $this->db->where('sd.expiry_date_time>=',date('Y-m-d')); 

	     if(isset($inputs['min_price']) && !empty($inputs['min_price']) && isset($inputs['max_price']) && !empty($inputs['max_price']))
	     {
	     $this->db->where("(s.service_amount BETWEEN " . $inputs['min_price'] . " AND " . $inputs['max_price'] . ")");
	     }
     
	     if(isset($inputs['common_search']) && !empty($inputs['common_search']))
	     {  
	     	$this->db->group_start();
            $this->db->like('s.service_title', $inputs['common_search'],'match');
            $this->db->or_like('s.service_location', $inputs['common_search'],'match');
            $this->db->or_like('s.service_offered', $inputs['common_search'],'match');
            $this->db->or_like('c.category_name', $inputs['common_search'],'match');
            $this->db->group_end();
	     }

   if(isset($inputs['user_address']) && !empty($inputs['user_address']))
       {    
          $this->db->like('s.service_location', $inputs['user_address']);
           
       }
      
	     if(isset($inputs['categories']) && !empty($inputs['categories']))
	     {
	     	$this->db->where('s.category',$inputs['categories']);
	     }
     
	     if(isset($inputs['service_latitude']) && !empty($inputs['service_latitude']) && isset($inputs['service_longitude']) && !empty($inputs['service_longitude']))
	     {
	     	 	 $latitude   = $inputs['service_latitude'];
				 $longitude  = $inputs['service_longitude'];
				 $radius     = 10;
		         $longitude_min = $longitude - 10 / abs(cos(deg2rad($longitude)) * 69);
		         $longitude_max = $longitude + 10 / abs(cos(deg2rad($longitude)) * 69);
		         $latitude_min  = $latitude - (10 / 69);
		         $latitude_max  = $latitude + (10 / 69);
		      $this->db->where("(s.service_longitude BETWEEN " . $longitude_min . " AND " . $longitude_max . ") AND (s.service_latitude BETWEEN " . $latitude_min . " AND " . $latitude_max . ")");
	     }

	     if(isset($inputs['sort_by']) && !empty($inputs['sort_by']))
	     {
	     	if($inputs['sort_by']==1)
	     	{
	     		$this->db->order_by('s.service_amount','ASC');
	     	}
	     	if($inputs['sort_by']==2)
	     	{
	     		$this->db->order_by('s.service_amount','DESC');
	     	}
	     	if($inputs['sort_by']==3)
	     	{
	     		$this->db->order_by('s.id','DESC');
	     	}

	     }
	     else
	     {
	     	$this->db->order_by('s.total_views','DESC');
	     }
         
        
         
        if(array_key_exists("returnType",$params) && $params['returnType'] == 'count'){ 
            $result = ($this->db->get())?$this->db->count_all_results():FALSE;
        }else{ 
              
                if(array_key_exists("start",$params) && array_key_exists("limit",$params)){ 
                    $this->db->limit($params['limit'],$params['start']); 
                }elseif(!array_key_exists("start",$params) && array_key_exists("limit",$params)){ 
                    $this->db->limit($params['limit']); 
                } 
                 
                $query = $this->db->get(); 
               $result = ($query)?$query->result_array():FALSE;  
            
        } 
         
        // Return fetched data 
        return $result; 
    } 



       public function get_pending_bookinglist($provider_id)
     {
        $this->db->select("b.*,s.service_title,s.service_image,s.service_amount,s.rating,s.service_image,c.category_name,sc.subcategory_name,p.profile_img,p.mobileno,p.country_code");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('providers p', 'b.provider_id = p.id', 'LEFT');
        $this->db->where("b.provider_id",$provider_id);
        $this->db->where("b.status",1);
        $this->db->order_by("b.id","DESC");
       
        $result = $this->db->get()->result_array();
        return $result;

     }


      public function get_reject_bookinglist($provider_id)
     {
        $this->db->select("b.*,s.service_title,s.service_image,s.service_amount,s.rating,s.service_image,c.category_name,sc.subcategory_name,p.profile_img,p.mobileno,p.country_code");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('providers p', 'b.provider_id = p.id', 'LEFT');
        $this->db->where("b.provider_id",$provider_id);
        $this->db->where("b.status",5);
        $this->db->order_by("b.id","DESC");
       
        $result = $this->db->get()->result_array();
        return $result;

     }



	 public function get_bookinglist($provider_id)
     {
        $this->db->select("b.*,s.service_title,s.service_image,s.service_amount,s.rating,s.service_image,c.category_name,sc.subcategory_name,p.profile_img,p.mobileno,p.country_code");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('providers p', 'b.provider_id = p.id', 'LEFT');
        $this->db->where("b.provider_id",$provider_id);
        $this->db->order_by("b.id","DESC");
       
        $result = $this->db->get()->result_array();
        return $result;

     }

      public function completed_bookinglist($provider_id)
     {
        $this->db->select("b.*,s.service_title,s.service_image,s.service_amount,s.rating,s.service_image,c.category_name,sc.subcategory_name,p.profile_img,p.mobileno,p.country_code");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('providers p', 'b.provider_id = p.id', 'LEFT');
        $this->db->where("b.provider_id",$provider_id);
        $this->db->where("b.status",6);
        $this->db->order_by("b.id","DESC");
        $result = $this->db->get()->result_array();
        return $result;

     }
     public function inprogress_bookinglist($provider_id)
     {
        $query = $this->db->query("SELECT  `b` . * ,  `s`.`service_title` ,  `s`.`service_image` ,  `s`.`service_amount` ,  `s`.`rating` ,  `s`.`service_image` ,  `c`.`category_name` ,  `sc`.`subcategory_name` ,  `p`.`profile_img` ,  `p`.`mobileno` ,  `p`.`country_code` FROM  `book_service`  `b` LEFT JOIN  `services`  `s` ON  `b`.`service_id` =  `s`.`id` LEFT JOIN  `categories`  `c` ON  `c`.`id` =  `s`.`category` LEFT JOIN  `subcategories`  `sc` ON  `sc`.`id` =  `s`.`subcategory` LEFT JOIN  `users`  `p` ON  `b`.`user_id` =  `p`.`id` WHERE  `b`.`provider_id` =  $provider_id AND (`b`.`status` =2) order by b.id DESC");
        $result = $query->result_array();
        return $result;

     }
      public function cancelled_bookinglist($provider_id)
     {
        $this->db->select("b.*,s.service_title,s.service_image,s.service_amount,s.rating,s.service_image,c.category_name,sc.subcategory_name,p.profile_img,p.mobileno,p.country_code");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('providers p', 'b.provider_id = p.id', 'LEFT');
        $this->db->where("b.provider_id",$provider_id);
        $this->db->where("b.status",7);
         $this->db->order_by("b.id","DESC");
        $result = $this->db->get()->result_array();
        return $result;

     }

      public function create_availability($inputs)
    {



        $new_details = array();

      $user_id = $this->session->userdata('id');
     
      $this->db->where('provider_id', $user_id);
      $count = $this->db->count_all_results('business_hours');
      if($count == 0){
      	$array = array();

      	if(!empty($inputs['availability'][0]['day'])){
      		$from = $inputs['availability'][0]['from_time'];
      		$to = $inputs['availability'][0]['to_time'];
      		for ($i=1; $i <= 7; $i++) {
      			$array[$i] = array('day'=>$i,'from_time'=>$from,'to_time'=>$to);
      		}

      	}else{
      		if(!empty($inputs['availability'][0])){
      			unset($inputs['availability'][0]);
      		}
      		$array = array_map('array_filter', $inputs['availability']);
			$array = array_filter($array);
      	}
      	if(!empty($array)){
      		$array = array_values($array);
      	}

      $new_details['provider_id'] = $user_id;
      if(empty($inputs['availability'][0]['from_time'])&&empty($inputs['availability'][0]['to_time'])){
        $new_details['all_days'] = 0;
      }else{
        $new_details['all_days']=1;
      }
      $new_details['availability'] = json_encode($array);
      

      return   $this->db->insert('business_hours', $new_details);
      }else{
        return 2; // Already Exists
      }
    }
     public function get_availability($user_id)
     { 
        return $this->db->where('provider_id',$user_id)->get('business_hours')->row_array();

     }

      public function get_subscription()
     {
        return $this->db->where('status',1)->get('subscription_fee')->result_array();

     }

      public function popular_service($service=NULL)
     { 
      if($this->session->userdata('usertype')=="provider"){

        $user=$this->db->where('provider_id',$this->session->userdata('id'))->from('provider_address as p')->join('city as c','c.id=p.city_id')->select('c.name as city_name')->get()->row_array();
      }else{  
          $user=$this->db->where('user_id',$this->session->userdata('id'))->from('user_address as p')->join('city as c','c.id=p.city_id')->select('c.name as city_name')->get()->row_array();
      }
       if(isset($user)&&!empty($user)){
        $city_name=$user['city_name'];  
      }else{
        $city_name='';
      }
       $this->db->select("s.id,s.user_id,s.service_title,s.service_amount,s.mobile_image,s.about,c.category_name,c.category_image,r.rating,sc.subcategory_name,s.currency_code");
        $this->db->from('services s');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('rating_review r', 'r.service_id = s.id', 'LEFT');
        $this->db->where("s.status = 1");
        $this->db->join('subscription_details as sd','sd.subscriber_id=s.user_id');
        $this->db->where('sd.expiry_date_time>=',date('Y-m-d'));

        if(!empty($service['category'])){
          $this->db->where('s.id!=',$service['id']);
          $this->db->where('s.category=',$service['category'])->or_where('s.subcategory=',$service['subcategory']);
          $this->db->where("s.status = 1");
        }
        if(!empty($this->session->userdata('current_location'))){
          $this->db->like('s.service_location',$this->session->userdata('current_location'),'after');
        }
        $this->db->group_by('s.id');
        $this->db->order_by('s.total_views','DESC');
        $this->db->limit(10);
        $query = $this->db->get(); $data = array(); if($query !== FALSE && $query->num_rows() > 0){ $data = $query->result_array(); } return $data;


        
     }

      public function completed_bookinglist_user($user_id)
     {
        $this->db->select("b.*,s.service_title,s.service_image,s.service_amount,s.rating,s.service_image,c.category_name,sc.subcategory_name,p.profile_img,p.mobileno,p.country_code");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('users p', 'b.user_id = p.id', 'LEFT');
        $this->db->where("b.user_id",$user_id);
        $this->db->where("b.status",3);
        $this->db->order_by("b.id",'DESC');
        $result = $this->db->get()->result_array();
        return $result;

     }

      public function accepted_bookinglist_user($user_id)
     {
        $this->db->select("b.*,s.service_title,s.service_image,s.service_amount,s.rating,s.service_image,c.category_name,sc.subcategory_name,p.profile_img,p.mobileno,p.country_code");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('users p', 'b.user_id = p.id', 'LEFT');
        $this->db->where("b.user_id",$user_id);
        $this->db->where("b.status",6);
        $this->db->order_by("b.id",'DESC');
        $result = $this->db->get()->result_array();
        return $result;

     }
     public function inprogress_bookinglist_user($user_id)
     {
      
        
        $query = $this->db->query("SELECT  `b` . * ,  `s`.`service_title` ,  `s`.`service_image` ,  `s`.`service_amount` ,  `s`.`rating` ,  `s`.`service_image` ,  `c`.`category_name` ,  `sc`.`subcategory_name` ,  `p`.`profile_img` ,  `p`.`mobileno` ,  `p`.`country_code` FROM  `book_service`  `b` LEFT JOIN  `services`  `s` ON  `b`.`service_id` =  `s`.`id` LEFT JOIN  `categories`  `c` ON  `c`.`id` =  `s`.`category` LEFT JOIN  `subcategories`  `sc` ON  `sc`.`id` =  `s`.`subcategory` LEFT JOIN  `users`  `p` ON  `b`.`user_id` =  `p`.`id` WHERE  `b`.`user_id` =  $user_id AND (`b`.`status` =2 OR  `b`.`status` =1) order by b.id DESC");
        $result = $query->result_array();
        return $result;

     }
      public function cancelled_bookinglist_user($user_id)
     {
        $this->db->select("b.*,s.service_title,s.service_image,s.service_amount,s.rating,s.service_image,c.category_name,sc.subcategory_name,p.profile_img,p.mobileno,p.country_code");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('users p', 'b.user_id = p.id', 'LEFT');
        $this->db->where("b.user_id",$user_id);
        $this->db->where("b.status",7);
        $this->db->order_by("b.id",'DESC');
        $result = $this->db->get()->result_array();
        return $result;

     }

      public function rejected_bookinglist_user($user_id)
     {
        $this->db->select("b.*,s.service_title,s.service_image,s.service_amount,s.rating,s.service_image,c.category_name,sc.subcategory_name,p.profile_img,p.mobileno,p.country_code");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('users p', 'b.user_id = p.id', 'LEFT');
        $this->db->where("b.user_id",$user_id);
        $this->db->where("b.status",5);
        $this->db->order_by("b.id",'DESC');
        $result = $this->db->get()->result_array();
        return $result;

     }

     public function get_bookinglist_user($user_id)
     {
        $this->db->select("b.*,s.service_title,s.service_image,s.service_amount,s.rating,s.service_image,c.category_name,sc.subcategory_name,p.name,p.profile_img,p.mobileno,p.country_code");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('users p', 'b.user_id = p.id', 'LEFT');
        $this->db->where("b.user_id",$user_id);
        $this->db->order_by("b.id",'DESC');

       
        $result = $this->db->get()->result_array();
        return $result;

     }

    public function update_profile($data)
    {
      $user_id = $this->session->userdata('id');
      $results = $this->db->update('users', $data, array('user_id'=>$user_id));
      return $results;
    }
    public function get_my_subscription()
    {
      $user_id = $this->session->userdata('id');  
      return $this->db->order_by('id','desc')->get_where('subscription_details',array('subscriber_id'=>$user_id,'type'=>1))->row_array();
    }
    public function get_my_subscription_list()
    {
     $user_id = $this->session->userdata('id');  
      return $this->db->from('subscription_details_history')->join('subscription_fee','subscription_fee.id=subscription_details_history.subscription_id')->where('subscription_details_history.subscriber_id',$user_id)->where('type',1)->get()->result_array();
    }
    public function update_user($data)
    {
      $user_id = $this->session->userdata('id');
      $results = $this->db->update('users', $data, array('id'=>$user_id));
      return $results;
    }

     public function provider_hours($user_id)
     {
        return $this->db->where('provider_id',$user_id)->get('business_hours')->row_array();

     }

     public function update_availability($input)
    {
      

      $new_details = array();

      $user_id = $this->session->userdata('id');
     
     
      $this->db->where('provider_id', $user_id);
      $count = $this->db->count_all_results('business_hours');
      if($count == 1){
        $array = array();

        if(!empty($input['availability'][0]['day'])){
          $from = $input['availability'][0]['from_time'];
          $to = $input['availability'][0]['to_time'];

          for ($i=1; $i <= 7; $i++) {
            $array[$i] = array('day'=>$i,'from_time'=>$from,'to_time'=>$to);
          }
        
        }else{
          if(!empty($input['availability'][0])){
            unset($input['availability'][0]);
          }
          $array = array_map('array_filter', $input['availability']);
      $array = array_filter($array);
        }
        if(!empty($array)){
          $array = array_values($array);
        }
      $new_details['provider_id'] = $user_id;
      if(empty($input['availability'][0]['from_time'])&&empty($input['availability'][0]['to_time'])){
        $new_details['all_days'] = 0;
      }else{
        $new_details['all_days']=1;
      }
      $new_details['availability'] = json_encode($array);
      
      
      return   $this->db->update('business_hours', $new_details, array('provider_id' => $user_id));
      }else{
        return 2; // Already Exists
      }
    }

     public function check_booking_status($user_data)
     {
        return $this->db->where(array('id'=> $user_data,'status'=>6))->get('book_service')->row_array();

     }
      
     public function rate_review_list($inputs)

     {


        $this->db->select("r.*,u.*");
        $this->db->from('rating_review r');
        $this->db->join('users u', 'r.user_id = u.id', 'LEFT');
        $this->db->where("r.service_id",$inputs);
        $result = $this->db->get()->result_array();
        return $result;

         
     }  

      public function rate_review_for_service($inputs)

   {

        $get_provider = $this->db->where('id',$inputs['service_id'])->get('services')->row_array();

        $new_details = array();

        $user_id = $inputs['user_id'];

        $new_details['user_id'] = $user_id;

        $new_details['service_id'] = $inputs['service_id'];

        $new_details['booking_id'] = $inputs['booking_id'];

        $new_details['provider_id'] = $get_provider['user_id'];

        $new_details['rating'] = $inputs['rating'];

        $new_details['review'] = $inputs['review'];

        $new_details['type'] = $inputs['type'];

        $new_details['created'] =  date('Y-m-d H:i:s');
         
        $this->db->where('status',1);

        $this->db->where('booking_id',$inputs['booking_id']);

        $this->db->where('user_id', $user_id);

        $count = $this->db->count_all_results('rating_review');

        if($count == 0)

        {

            return   $this->db->insert('rating_review', $new_details);
        }

        else

        {

          return $result = 2;

        }

    }
}
?>
