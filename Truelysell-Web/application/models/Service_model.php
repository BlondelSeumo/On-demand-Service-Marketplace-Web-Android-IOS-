<?php
if ( ! defined('BASEPATH')) exit('No direct script access allowed');
class Service_model extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
                
	}
 

    public function create_service($inputs)
	{
		$this->db->insert('services',$inputs);
		return $this->db->insert_id(); 
	}

	public function insert_serviceimage($image)
	  {
	    $this->db->insert('services_image',$image);
	    $this->db->where(array('service_id' => $image['service_id']));
	    return $this->db->affected_rows() != 0 ? true : false; 
	  }

	public function get_service()
	{
	
          $this->db->select("s.id,s.user_id,s.service_title,s.service_amount,s.mobile_image,c.category_name,sc.subcategory_name,s.rating_count,s.service_location");
	      $this->db->from('services s');
	      $this->db->join('categories c', 'c.id = s.category', 'LEFT');
	      $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
	      $this->db->where("s.status = 1");
	      $this->db->where('s.user_id',$this->session->userdata('id'));
	      $this->db->order_by('s.id','DESC');
	     $result = $this->db->get()->result_array();
          return $result;
    }

    public function get_service_id($inputs)
    {
      return $this->db->where('id',$inputs)->get('services')->row_array();
    }

    public function get_category()
	{
	     return $this->db->get('categories')->result_array();
	}

	public function get_subcategory()
	{
	     return $this->db->get('subcategories')->result_array();
	}

	public function service_image($service_id)
	{
		$this->db->select("service_image");
		$this->db->from('services_image');
		$this->db->where("service_id",$service_id);
		$this->db->where("status",1);
		$this->db->order_by('id','ASC');
		return $this->db->get()->result_array();

	}
	 public function update_service($inputs,$where)
	  {
	    $this->db->set($inputs);
	    $this->db->where($where);
	    $this->db->update('services');
	    return $this->db->affected_rows() != 0 ? true : false; 
	  }
    

       public function update_service_image($inputs,$where)
    {
      
      $this->db->set($inputs);
      $this->db->where($where);
      $this->db->update('services_image');
      return $this->db->affected_rows() != 0 ? true : false; 
    }
	
  	public function get_availability($provider_id)
    {
      return $this->db->where('provider_id',$provider_id)->get('business_hours')->row_array();
    }

    public function provider_hours($user_id)
     {
        return $this->db->where('provider_id',$user_id)->get('business_hours')->row_array();

     }

     public function get_bookings($service_date,$service_id)
     {
        return $this->db->where(array('service_date'=>$service_date,'service_id'=>$service_id))->get('book_service')->result_array();

     } 

     public function insert_booking($user_post_data)
     {
       
        $result = $this->db->insert('book_service',$user_post_data);
       

        return $result;
     }

     public function get_booking_details($booking_id)
     {
        return $this->db->where('id',$booking_id)->get('book_service')->row_array();

     } 

     public function update_bookingstatus($book_details,$where)
    {
      
      $this->db->set($book_details);
      $this->db->where($where);
      $this->db->update('book_service');
      return $this->db->affected_rows() != 0 ? true : false;
      
         
    }

     public function featured_service()
     {
       $this->db->select("s.id,s.user_id,s.service_title,s.service_amount,s.mobile_image,c.category_name,c.category_image,r.rating,sc.subcategory_name");
        $this->db->from('services s');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('rating_review r', 'r.service_id = s.id', 'LEFT');
        $this->db->where("s.status = 1");
        $this->db->order_by('r.rating','DESC');
        return $result = $this->db->get()->result_array();


        
     }

     public function popular_service()
     {
       $this->db->select("s.id,s.user_id,s.service_title,s.service_amount,s.mobile_image,s.currency,c.category_name,c.category_image,r.rating,sc.subcategory_name");
        $this->db->from('services s');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('rating_review r', 'r.service_id = s.id', 'LEFT');
        $this->db->where("s.status = 1");
        $this->db->order_by('s.total_views','DESC');
        return $result = $this->db->get()->result_array();


        
     }

      public function get_profile($user_id)
     {
        return $this->db->where('id',$user_id)->get('users')->row_array();

     }

     public function subscription_list()
        {
            return $this->db->where("status",1)->get('subscription_fee')->result_array();
        }

    public function subscription_details($id)
        {
            return $this->db->get_where('subscription_fee',array('id'=>$id))->row_array();
        }

        public function ratingstype_list()
        {
            return $this->db->get('rating_type')->result_array();
        }

        /*user_list*/
        public function get_user_list(){
            return $this->db->where("status",1)->get('users')->result_array();

        }

        /*Provider_list*/
        public function get_provider_list(){
            return $this->db->where("status",1)->get('providers')->result_array();

        }

        /*get service list*/

        public function get_service_list(){
            return $this->db->where("status",1)->get('services')->result_array();

        }




/*get Ratings and Reviews*/

    public function get_review()
        {
          $value=$this->db->select('tab_1.id,tab_1.rating,tab_1.review,tab_1.created')->
                            select('tab_2.name as user_name,tab_2.profile_img as user_image,tab_3.name as provider_name,tab_3.profile_img as provider_image,tab_4.service_title,tab_5.name as type_name')->
                            from('rating_review tab_1')->
                            join("users tab_2","tab_1.user_id=tab_2.id","LEFT")->
                            join("providers tab_3","tab_1.provider_id=tab_3.id","LEFT")->
                            join("services tab_4","tab_1.service_id=tab_4.id","LEFT")->
                            join("rating_type tab_5","tab_1.type=tab_5.id","LEFT")->
                            where('tab_1.status',1)->
                            order_by('tab_1.id','DESC')->
                            get()->result_array();
            return $value;


            
        }

        /*rating filter*/

    public function get_review_filter($service,$provider,$user,$type,$from,$to){
          if(!empty($from)) {
          $from_date=date("Y-m-d 00:00:00", strtotime($from));
          }else{
          $from_date='';
          }
          if(!empty($to)) {
          $to_date=date("Y-m-d 23:59:00", strtotime($to));
          }else{
          $to_date='';
          }
          
          $this->db->select('tab_1.id,tab_1.rating,tab_1.review,tab_1.created');
          $this->db->select('tab_2.name as user_name,tab_2.profile_img as user_image,tab_3.name as provider_name,tab_3.profile_img as provider_image,tab_4.service_title,tab_5.name as type_name');
          $this->db->from('rating_review tab_1');
          $this->db->join("users tab_2","tab_1.user_id=tab_2.id","LEFT");
          $this->db->join("providers tab_3","tab_1.provider_id=tab_3.id","LEFT");
          $this->db->join("services tab_4","tab_1.service_id=tab_4.id","LEFT");
          $this->db->join("rating_type tab_5","tab_1.type=tab_5.id","LEFT");
          if(!empty($service)){
            $this->db->where('tab_1.service_id',$service);
          } 
          if(!empty($provider)){
            $this->db->where('tab_1.provider_id',$provider);
          }
          if(!empty($user)){
            $this->db->where('tab_1.user_id',$user);
          }
          if(!empty($type)){
            $this->db->where('tab_1.type',$type);
          }
          if(!empty($from_date)){
            $this->db->where('tab_1.created >=',$from_date);
          }
          if(!empty($to_date)){
            $this->db->where('tab_1.created <=',$to_date);
          }          

          $this->db->where('tab_1.status',1);
          $this->db->order_by('tab_1.id','DESC'); 
          return $this->db->get()->result_array();

    }


    public function ratingstype_details($id)
        {
            return $this->db->get_where('rating_type',array('id'=>$id))->row_array();
        }


        var $column_order = array(null, 'U.name','U.mobileno','U.email','U.created_at','S.subscription_name');
        var $column_search = array( 'U.name','U.mobileno','U.email','U.created_at','S.subscription_name');
        var $order = array('U.id' => 'DESC'); // default order
        var $providers  = 'providers U';
        var $subscription_details  = 'subscription_details SD';
        var $subscription  = 'subscription_fee S';

    private function p_get_datatables_query()
        {

          $this->db->select('U.*,S.subscription_name,SD.subscriber_id');
          $this->db->from($this->providers);
          $this->db->join($this->subscription_details,'SD.subscriber_id=U.id','left');
          $this->db->join($this->subscription,'S.id=SD.subscription_id','left');


                $i = 0;

                foreach ($this->column_search as $item) // loop column
                {
                        if($_POST['search']['value']) // if datatable send POST for search
                        {

                                if($i===0) // first loop
                                {
                                        $this->db->group_start(); // open bracket. query Where with OR clause better with bracket. because maybe can combine with other WHERE with AND.
                                        $this->db->like($item, $_POST['search']['value']);
                                }
                                else
                                {

                                    if($item == 'status'){
                                        if(strtolower($_POST['search']['value'])=='active'){
                                            $search_val = 1;
                                            $this->db->or_like($item, $search_val);
                                        }
                                        if(strtolower($_POST['search']['value'])=='inactive'){
                                            $search_val = 0;
                                            $this->db->or_like($item, $search_val);
                                        }


                                        }else{
                                            $search_val = $_POST['search']['value'];
                                            $this->db->or_like($item, $search_val);
                                        }

                                }

                                if(count($this->column_search) - 1 == $i) //last loop
                                        $this->db->group_end(); //close bracket
                        }
                        $i++;
                }

                if(isset($_POST['order'])) // here order processing
                {
                        $this->db->order_by($this->column_order[$_POST['order']['0']['column']], $_POST['order']['0']['dir']);
                }
                else if(isset($this->order))
                {
                        $order = $this->order;
                        $this->db->order_by(key($order), $order[key($order)]);
                }
        }

        /*provider filter*/

        public function provider_filter($username,$email,$from,$to,$subcategory){
          if(!empty($from)) {
          $from_date=date("Y-m-d 00:00:00", strtotime($from));
          }else{
          $from_date='';
          }
          if(!empty($to)) {
          $to_date=date("Y-m-d 23:59:00", strtotime($to));
          }else{
          $to_date='';
          }

      $this->db->select('U.*,S.subscription_name,SD.subscriber_id');
      $this->db->from('providers U');
      $this->db->join('subscription_details SD','SD.subscriber_id=U.id','left');
      $this->db->join('subscription_fee S','S.id=SD.subscription_id','left');
      if(!empty($username)){
        $this->db->where('U.name',$username);
      }
      if(!empty($email)){
        $this->db->where('U.email',$email);
      }
      if(!empty($subcategory)){
        $this->db->where('U.subcategory',$subcategory);
      }
        if(!empty($from_date)){
        $this->db->where('U.created_at >=',$from_date);
      }
      if(!empty($to_date)){
        $this->db->where('U.created_at <=',$to_date);
      }
      $this->db->where('U.delete_status',0);
      return $this->db->get()->result_array();
        }

      public function provider_list(){
          $this->p_get_datatables_query();
            if($_POST['length'] != -1)
              $this->db->where('delete_status',0);
            $this->db->limit($_POST['length'], $_POST['start']);
            $this->db->group_by('id');
            $query = $this->db->get();
            return $query->result();
      }

      public function provider_list_all(){

        $this->db->from('providers');
        $this->db->where('delete_status', 0);
            return $this->db->count_all_results();
      }

      public function provider_list_filtered(){

            $this->p_get_datatables_query();
            $this->db->group_by('id');
            $query = $this->db->get();
            return $query->num_rows();
      }
       public function booking_count($provider_id){

      $this->db->where('provider_id',$provider_id);
        return $this->db->count_all_results('book_service');
        
    }
     public function services_count($user_id){

        $this->db->where('user_id',$user_id);
        return $this->db->count_all_results('services');
        
    }
     public function completed_booking($provider_id){

        $this->db->where(array('provider_id'=> $provider_id,'status'=>6));
        return $this->db->count_all_results('book_service');
        
    }

      public function inprogress_booking($provider_id){

        
        $query = $this->db->query(" SELECT * FROM `book_service` WHERE `provider_id` = $provider_id AND (`status` =2 OR  `status` =1 OR `status` = 3)");
        $result = $query->result_array();
        return count($result); 
        
    }

    public function cancelled_booking($provider_id){

        $query = $this->db->query(" SELECT * FROM `book_service` WHERE `provider_id` = $provider_id AND (`status` =5 OR  `status` =7)");
        $result = $query->result_array();
        return count($result); 
        
    }

     public function users_name($username,$email)
    {
      $this->db->select('U.*,S.subscription_name,SD.subscriber_id');
      $this->db->from('providers U');
      $this->db->join('subscription_details SD','SD.subscriber_id=U.id','left');
      $this->db->join('subscription_fee S','S.id=SD.subscription_id','left');
      return $this->db->where(array('U.name'=>$username,'U.email'=>$email))->get()->result_array();
    }

    public function users_email($email)
    {
      $this->db->select('U.*,S.subscription_name,SD.subscriber_id');
      $this->db->from('providers U');
      $this->db->join('subscription_details SD','SD.subscriber_id=U.id','left');
      $this->db->join('subscription_fee S','S.id=SD.subscription_id','left');
      return $this->db->where('U.email',$email)->get()->result_array();
    }

    public function username($name)
    {
      $this->db->select('U.*,S.subscription_name,SD.subscriber_id');
      $this->db->from('providers U');
      $this->db->join('subscription_details SD','SD.subscriber_id=U.id','left');
      $this->db->join('subscription_fee S','S.id=SD.subscription_id','left');
      return $this->db->where('U.name',$name)->get()->result_array();
    }
    /*service filter*/
    public function service_filter($service_title,$category,$subcategory,$from,$to){
          if(!empty($from)) {
          $from_date=date("Y-m-d 00:00:00", strtotime($from));
          }else{
          $from_date='';
          }
          if(!empty($to)) {
          $to_date=date("Y-m-d 23:59:00", strtotime($to));
          }else{
          $to_date='';
          }

          $this->db->select('S.*,C.category_name,Sc.subcategory_name');
          $this->db->from('services S');
          $this->db->join('categories C','C.id=S.category','left');
          $this->db->join('subcategories Sc','Sc.id=S.subcategory','left');

          if(!empty($service_title)){
          $this->db->where('S.id',$service_title);
          }
          if(!empty($category)){
          $this->db->where('S.category',$category);
          }
          if(!empty($subcategory)){
          $this->db->where('S.subcategory',$subcategory);
          }
          
          if(!empty($from_date)){
          $this->db->where('S.created_at >=',$from_date);
          }
          if(!empty($to_date)){
          $this->db->where('S.created_at <=',$to_date);
          }
          $this->db->where('S.status',1);
          return $this->db->get()->result_array();


    }

    public function service_list()
    {
      $this->db->select('S.*,C.category_name,Sc.subcategory_name');
      $this->db->from('services S');
      $this->db->join('categories C','C.id=S.category','left');
      $this->db->join('subcategories Sc','Sc.id=S.subcategory','left');
      $this->db->where('S.status',1);
      return $this->db->get()->result_array();   
    }

    public function service_list_all(){

      $this->db->from('services');
      $this->db->where('status',1);
      return $this->db->count_all_results();
    }

    public function payment_list_filtered(){

          $this->p_get_datatables_query();
          $query = $this->db->get();
          return $query->num_rows();
    }


     public function search_all($service_title,$category,$subcategory)
    {
      $this->db->select('S.*,C.category_name,Sc.subcategory_name');
      $this->db->from('services S');
      $this->db->join('categories C','C.id=S.category','left');
      $this->db->join('subcategories Sc','Sc.id=S.subcategory','left');
      return $this->db->where(array('S.id'=>$service_title,'C.category'=>$category,'Sc.subcategory'=>$subcategory,'status'=>1))->get()->result_array();
    }

     public function search_scat($service_title,$category)
    {
      $this->db->select('S.*,C.category_name,Sc.subcategory_name');
      $this->db->from('services S');
      $this->db->join('categories C','C.id=S.category','left');
      $this->db->join('subcategories Sc','Sc.id=S.subcategory','left');
      return $this->db->where(array('S.id'=>$service_title,'S.category'=>$category,'S.status'=>1))->get()->result_array();
    }

    public function search_title($service_title)
    {
      $this->db->select('S.*,C.category_name,Sc.subcategory_name');
      $this->db->from('services S');
      $this->db->join('categories C','C.id=S.category','left');
      $this->db->join('subcategories Sc','Sc.id=S.subcategory','left');
      return $this->db->where(array('S.id'=>$service_title,'S.status'=>1))->get()->result_array();
    }

    public function check_booking_list($id){
      $ret=$this->db->select('*')->
                      from('book_service')->
                      where('service_id',$id)->where_not_in('status',[5,7,6])->
                      get()->result();

      if(count($ret)>0){
        $status=1;
      }else{
        $status=0;
      }
      return $status;
     
    }

    public function get_full_notification($params = array()){
       if(!empty($this->session->userdata('chat_token'))){
             $ses_token=$this->session->userdata('chat_token');
          }else{
               $ses_token='';
          }
       $this->db->select("notification_table.*"); 
        $this->db->from('notification_table'); 
           $this->db->where("notification_table.receiver = ",$ses_token);
        if(array_key_exists("where", $params)){ 
            foreach($params['where'] as $key => $val){ 
                $this->db->where($key, $val); 
            } 
        } 
         
        if(array_key_exists("returnType",$params) && $params['returnType'] == 'count'){  
            $result = $this->db->count_all_results(); 
        }else{ 
            if(array_key_exists("id", $params) || (array_key_exists("returnType", $params) && $params['returnType'] == 'single')){
                if(!empty($params['id'])){ 
                    $this->db->where('id', $params['id']); 
                } 
                $query = $this->db->get(); 
                $result = $query->row_array(); 
            }else{ 
                $this->db->order_by('notification_id', 'desc'); 
                if(array_key_exists("start",$params) && array_key_exists("limit",$params)){ 
                    $this->db->limit($params['limit'],$params['start']); 
                }elseif(!array_key_exists("start",$params) && array_key_exists("limit",$params)){ 
                    $this->db->limit($params['limit']); 
                } 
                
                $query = $this->db->get(); 

                $result = ($query->num_rows() > 0)?$query->result_array():FALSE; 
            } 
        } 
           /*update status*/
          
          if(!empty($result)&&is_array($result)){
            $notification_id=[];
            foreach ($result as $key => $value) {
              $notification_id[]=$value['notification_id'];
            }
            $data=array('status',0);
            
            $this->db->where_in('notification_id',$notification_id);
            $this->db->set('status',0);
            $this->db->update('notification_table');

          }
        // Return fetched data 
        return $result; 
    }

		
}
?>
