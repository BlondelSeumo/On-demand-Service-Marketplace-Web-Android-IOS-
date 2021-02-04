<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Dashboard_model extends CI_Model {

	public function __construct() {

        parent::__construct();
        $this->load->database();
        date_default_timezone_set('Asia/Kolkata');
    }

		public function total_requests(){
			return $this->db->count_all_results('request_details');
		}

		public function total_providers(){
			return $this->db->count_all_results('provider_details');
		}

		public function total_revenue(){
			$this->db->select_sum('proposed_fee');
			$this->db->where('status', 3);
			return $this->db->get('request_details')->row_array();
		}

		public function total_pending_providers(){
			$this->db->where('status', 3);
			return $this->db->count_all_results('request_details');
		}

		public function request_details($month, $year){
			$this->db->select("count(r_id) AS tot");
			$this->db->where("MONTH(created)", $month);
			$this->db->where("YEAR(created)", $year);
			return $this->db->get('request_details')->row_array();
		}

		public function provider_details($month, $year){
			$this->db->select("count(p_id) AS tot");
			$this->db->where("MONTH(created)", $month);
			$this->db->where("YEAR(created)", $year);
			return $this->db->get('provider_details')->row_array();
		}

		public function last_request_details(){
			$query = $this->db->query("select max(created) from (select max(created) as created from request_details union select max(created) as created from provider_details) AS created");
			return $query->row_array();
		}

		public function colour_settings(){
			return $this->db->get('colour_settings')->row_array();
		}


		var $column_order = array(null, 'U.name','U.mobileno','U.email','U.last_login','U.created_at','U.type','S.subscription_name');
		var $column_search = array( 'U.name','U.mobileno','U.email','U.last_login','U.type','S.subscription_name');
		var $order = array('U.id' => 'DESC'); // default order
		var $users  = 'users U';
		var $subscription_details  = 'subscription_details SD';
		var $subscription  = 'subscription_fee S';
		var $admincolumn_order = array(null, 'U.username','U.full_name');
		var $admincolumn_search = array( 'U.username','U.full_name');
		var $adminorder = array('U.user_id' => 'DESC'); // default order
		var $administrators  = 'administrators U';
		
		private function p_get_datatables_query()
		{


			$this->db->select('U.id,U.name,U.profile_img,U.mobileno,U.email,created_at as created_at,U.last_login,U.status,U.type,S.subscription_name,SD.subscriber_id');
			$this->db->from($this->users);
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

		/*user filter*/

		public function user_filter($username,$email,$from,$to){
					if(!empty($from)) {
					$from_date=date("Y-m-d", strtotime($from));
					}else{
					$from_date='';
					}
					if(!empty($to)) {
					$to_date=date("Y-m-d", strtotime($to));
					}else{
					$to_date='';
					}

			$this->db->select('U.*,S.subscription_name,SD.subscriber_id');
			$this->db->from('users U');
			$this->db->join('subscription_details SD','SD.subscriber_id=U.id','left');
			$this->db->join('subscription_fee S','S.id=SD.subscription_id','left');
			if(!empty($username)){
				$this->db->where('U.name',$username);
			}
			if(!empty($email)){
				$this->db->where('U.email',$email);
			}

		    if(!empty($from_date)){
				$this->db->where('date(U.created_at) >=',$from_date);
			}
			if(!empty($to_date)){
				$this->db->where('date(U.created_at) <=',$to_date);
			}
			return $this->db->get()->result_array();

		}

	  public function users_list(){
	      $this->p_get_datatables_query();
	        if($_POST['length'] != -1)
	        $this->db->limit($_POST['length'], $_POST['start']);
	        $query = $this->db->get();
	        return $query->result();
	  }

	  public function users_list_all(){

	    $this->db->from($this->users);
	        return $this->db->count_all_results();
	  }

	  public function users_list_filtered(){

	        $this->p_get_datatables_query();
	        $query = $this->db->get();
	        return $query->num_rows();
	  }

	  public function users_name($username,$email)
		{
			$this->db->select('U.*,S.subscription_name,SD.subscriber_id');
			$this->db->from('users U');
			$this->db->join('subscription_details SD','SD.subscriber_id=U.id','left');
			$this->db->join('subscription_fee S','S.id=SD.subscription_id','left');
			return $this->db->where(array('U.name'=>$username,'U.email'=>$email))->get()->result_array();
		}

		public function users_email($email)
		{
			$this->db->select('U.*,S.subscription_name,SD.subscriber_id');
			$this->db->from('users U');
			$this->db->join('subscription_details SD','SD.subscriber_id=U.id','left');
			$this->db->join('subscription_fee S','S.id=SD.subscription_id','left');
			return $this->db->where('U.email',$email)->get()->result_array();
		}

		public function username($name)
		{
			$this->db->select('U.*,S.subscription_name,SD.subscriber_id');
			$this->db->from('users U');
			$this->db->join('subscription_details SD','SD.subscriber_id=U.id','left');
			$this->db->join('subscription_fee S','S.id=SD.subscription_id','left');
			return $this->db->where('U.name',$name)->get()->result_array();
		}

		
		

	  public function providers_list_all(){

	    $this->db->where('status',1);
        return $this->db->count_all_results('providers');

	  }

	   public function services_list_all(){

	    $this->db->where('status',1);
        return $this->db->count_all_results('services');
        
	  }

	 
		public function admin_details($user_id)
		{
			$results = array();
			$results = $this->db->get_where('users',array('user_id'=>$user_id))->row_array();
			return $results;
		}

		public function update_profile($data)
	  {
			$user_id = $this->session->userdata('admin_id');
	    $results = $this->db->update('users', $data, array('user_id'=>$user_id));
	    return $results;
	  }

		public function change_password($user_id,$confirm_password,$current_password)
		{

	        $current_password = md5($current_password);
	        $this->db->where('user_id', $user_id);
	        $this->db->where(array('password'=>$current_password));
	        $record = $this->db->count_all_results('users');

	        if($record > 0){

	          $confirm_password = md5($confirm_password);
	          $this->db->where('user_id', $user_id);
	          return $this->db->update('users',array('password'=>$confirm_password));
	        }else{
	          return 2;
	        }
		}


		public function get_setting_list() {
        $data = array();
        $stmt = "SELECT a.*"
                . " FROM system_settings AS a"
                . " ORDER BY a.`id` ASC";
        $query = $this->db->query($stmt);
        if ($query->num_rows()) {
            $data = $query->result_array();
        }
        return $data;
    }


     public function edit_payment_gateway($id)
    {
        $query = $this->db->query(" SELECT * FROM `payment_gateways` WHERE `id` = $id ");
        $result = $query->row_array();
        return $result;
    }

     public function all_payment_gateway()
    {
      $this->db->select('*');
        $this->db->from('payment_gateways');
        $query = $this->db->get();
        return $query->result_array();         
    }

     public function get_bookinglist()
     {
        $this->db->select("b.*,b.currency_code as currency_code1,s.service_title,s.service_image,s.service_amount,s.rating,s.service_image,c.category_name,sc.subcategory_name,p.profile_img,p.mobileno,p.name");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('users p', 'b.provider_id = p.id', 'LEFT');
        $this->db->order_by('id','DESC');
        $this->db->limit(5);
       
        $result = $this->db->get()->result_array();

        return $result;

     }


     /*get payment informations*/

     public function get_payments_info(){
     	$ret=$this->db->select('sum(fee) as paid_amt')->from('subscription_details_history as s')->join('subscription_fee as f','f.id=s.subscription_id')->get()->row()->paid_amt;
     	return $ret;
     }

     /*admin dashboard*/
	 
	 private function p_get_datatables_adminquery()
		{


			$this->db->select('user_id,username,email,full_name,profile_img');
			$this->db->from($this->administrators);			
			$i = 0;
			foreach ($this->admincolumn_search as $item) // loop column
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
									$search_val = $_POST['search']['value'];
									$this->db->or_like($item, $search_val);
								}

								if(count($this->admincolumn_search) - 1 == $i) //last loop
										$this->db->group_end(); //close bracket
						}
						$i++;
				}

				if(isset($_POST['order'])) // here order processing
				{
						$this->db->order_by($this->admincolumn_order[$_POST['order']['0']['column']], $_POST['order']['0']['dir']);
				}
				else if(isset($this->adminorder))
				{
						$order = $this->adminorder;
						$this->db->order_by(key($order), $order[key($order)]);
				}
		}

    public function adminuser_filter($username){
		$this->db->select('U.*');
		$this->db->from('administrators U');
		if(!empty($username)){
			$this->db->where('U.username',$username);
		}
		return $this->db->get()->result_array();

	}
	
	public function adminusers_list(){
	      $this->p_get_datatables_adminquery();
	        if($_POST['length'] != -1)
	        $this->db->limit($_POST['length'], $_POST['start']);
	        $query = $this->db->get();
	        return $query->result();
	  }
	  
	  public function get_adminusers_list(){
	  	return $this->db->get('administrators')->result_array();
	  }
	    public function get_adminusers_filter($username){         

          if(!empty($username)){
            $this->db->where('username=',$username);
          }           
          $result=$this->db->get('administrators')->result_array();
         return $result;

        }
		
		 public function adminusers_list_all(){
	    $this->db->from($this->administrators);
	        return $this->db->count_all_results();
	  }

	  public function adminusers_list_filtered(){

	        $this->p_get_datatables_adminquery();
	        $query = $this->db->get();
	        return $query->num_rows();
	  }
	  public function get_adminuser_details($id){
	$result=$this->db->where('user_id',$id)->get('administrators')->row_array();
	return $result;
}


}

/* End of file Api_model.php */
/* Location: ./application/models/Api_model.php */
