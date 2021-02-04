<?php
if ( ! defined('BASEPATH')) exit('No direct script access allowed');
class Admin_model extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
	}
  public function is_valid_login($username,$password)
  {
    $password = md5($password);
    $this->db->select('user_id, profile_img,token,role');
    $this->db->from('administrators');
		$this->db->where('username',$username);
		$this->db->where('password',$password);
		$this->db->where_in('role',[1,2]);
	  $result = $this->db->get()->row_array();
    return $result;
  }
  
  public function update_data($table, $data, $where = []) {
        if (count($where) > 0) {
            $this->db->where($where);
            $status = $this->db->update($table, $data);
            return $status;
        } else {
            $this->db->insert($table, $data);
            return $this->db->insert_id();
        }
    }
  
   public function getSingleData($table,$where=array()) {

        $this->db->select('*');
        $this->db->from($table);
        $this->db->where($where);
        $query = $this->db->get();
        $result = $query->row();
        return $result;
    }

    public function admin_details($user_id)
	{
		$results = array();
		$results = $this->db->get_where('administrators',array('user_id'=>$user_id))->row_array();
		return $results;
	}

	public function update_profile($data)
	  {
			$user_id = $this->session->userdata('admin_id');
	    $results = $this->db->update('administrators', $data, array('user_id'=>$user_id));
	    return $results;
	  }

		public function change_password($user_id,$confirm_password,$current_password)
		{

	        $current_password = md5($current_password);
	        $this->db->where('user_id', $user_id);
	        $this->db->where(array('password'=>$current_password));
	        $record = $this->db->count_all_results('administrators');

	        if($record > 0){

	          $confirm_password = md5($confirm_password);
	          $this->db->where('user_id', $user_id);
	          return $this->db->update('administrators',array('password'=>$confirm_password));
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
	
	public function edit_razor_payment_gateway($id)
    {
        $query = $this->db->query(" SELECT * FROM `razorpay_gateway` WHERE `id` = $id ");
        $result = $query->row_array();
        return $result;
    }
	
	public function edit_paypal_payment_gateway($id)
    {
        $query = $this->db->query(" SELECT * FROM `paypal_payment_gateways` WHERE `id` = $id ");
        $result = $query->row_array();
        return $result;
    }
	
	public function edit_paytab_payment_gateway()
    {
        $query = $this->db->query(" SELECT * FROM `paytabs_details`");
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

        public function categories_list()
		{
			$query = $this->db->query(" SELECT * FROM `categories` WHERE `status` = 1 ")->result_array();
			return $query;
		}

		public function categories_list_filter($category,$from_date,$to_date){

			        if(!empty($from_date)) {
					$from_date=date("Y-m-d", strtotime($from_date));
					}else{
					$from_date='';
					}
					if(!empty($to_date)) {
					$to_date=date("Y-m-d", strtotime($to_date));
					}else{
					$to_date='';
					}
					$this->db->select('*');
					$this->db->from('categories');
					if(!empty($from_date)){
						$this->db->where('date(created_at) >=',$from_date);
					}
					if(!empty($to_date)){
						$this->db->where('date(created_at) <=',$to_date);
					}
					if(!empty($category)){
					$this->db->where('id',$category);
					}
					$this->db->where('status',1);
					return $this->db->get()->result_array();

		}

		/*subcategory filter*/
		public function subcategory_filter($category,$subcategory,$from,$to){
				
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

			        $this->db->select('s.*,c.category_name');
					$this->db->from('subcategories s');
					$this->db->join('categories c', 'c.id = s.category', 'left');
					if(!empty($from_date)){
						$this->db->where('date(s.created_at) >=',$from_date);
					}
					if(!empty($to_date)){
						$this->db->where('date(s.created_at) <=',$to_date);
					}
					if(!empty($category)){
						$this->db->where('s.category',$category);
					}
					if(!empty($subcategory)){
						$this->db->where('s.id',$subcategory);
					}
					$this->db->where('status',1);
					return $this->db->get()->result_array();

		}

		public function subcategories_list()
		{
					$this->db->select('s.*,c.category_name');
					$this->db->from('subcategories s');
					$this->db->join('categories c', 'c.id = s.category', 'left');
					$this->db->where('s.status',1);
			return $this->db->get()->result_array();
		}
		public function search_catsuball($category,$subcategory)
		{
			$this->db->select('s.*,c.category_name');
			$this->db->from('subcategories s');
			$this->db->join('categories c', 'c.id = s.category', 'left');
			return $this->db->where(array('s.id'=>$category,'c.id'=>$subcategory,'s.status'=>1))->get()->result_array();
		}

		public function search_subcategory($subcategory)
		{
			$this->db->select('s.*,c.category_name');
			$this->db->from('subcategories s');
			$this->db->join('categories c', 'c.id = s.category', 'left');
			return $this->db->where(array('c.id'=>$subcategory,'s.status'=>1))->get()->result_array();
		}

		public function search_category($category)
		{
			$this->db->select('s.*,c.category_name');
			$this->db->from('subcategories s');
			$this->db->join('categories c', 'c.id = s.category', 'left');
			return $this->db->where(array('c.id'=>$category,'s.status'=>1))->get()->result_array();
		}
		
        public function categories_details($id)
		{
			return $this->db->get_where('categories',array('id'=>$id))->row_array();
		}

		public function subcategories_details($id)
		{
			return $this->db->get_where('subcategories',array('id'=>$id))->row_array();
		}
                
                public function language_list()
		{
			return $this->db->get('language')->result_array();
		}
                public function Revenue_list()
		{
                    
                    $this->db->select('ren.date,ren.currency_code,ren.amount,ren.commission,ur.name as user,pro.name as provider');
                    $this->db->from('revenue ren');
                    $this->db->join('users ur', 'ur.id = ren.user', 'left');
                    $this->db->join('providers pro', 'pro.id = ren.provider', 'left');
                    $query=$this->db->get();
                    $result=$query->result_array();
                    return $result;
//			return $this->db->get('revenue')->result_array();
		}
                public function ColorList()
		{
			return $this->db->get('theme_color_change')->result_array();
		}
		public function contact_list()
		{
			return $this->db->get('contact_form_details')->result_array();
		}

		public function contact_list_filter($name,$email){
					$this->db->select('*');
					$this->db->from('contact_form_details');
					if(!empty($name)){
						$this->db->where('name like ',$name);
					}
					if(!empty($email)){
						$this->db->where('email like',$email);
					}					
					return $this->db->get()->result_array();

		}
		public function footercount()
    {
        $query  = $this->db->query("SELECT id FROM  `footer_menu` WHERE STATUS =1");
        $result = $query->num_rows();
        return $result;
	}
	public function is_valid_menu_name($menu_name)
    {
        $query  = $this->db->query("SELECT * FROM `footer_menu` WHERE `title` =  '$menu_name';");
        $result = $query->num_rows();
        return $result;
	}
	public function is_valid_submenu($menu_name)
    {
        $query  = $this->db->query("SELECT * FROM `footer_submenu` WHERE `title` =  '$menu_name';");
        $result = $query->num_rows();
        return $result;
    }
    public function edit_footer_menu($id)
    {
        $query  = $this->db->query("SELECT * FROM `footer_menu` WHERE `id` =  $id;");
        $result = $query->result_array();
        return $result;
	}
	public function get_footer_menu($end, $start)
    {
        $query  = $this->db->query("SELECT * FROM  `footer_menu` LIMIT $start , $end ");
        $result = $query->result_array();
        return $result;
    }
    public function get_footer_submenu()
    {
        $query  = $this->db->query("SELECT footer_submenu.*,footer_menu.title FROM `footer_submenu`
                                    INNER JOIN footer_menu ON footer_menu.id = footer_submenu.`footer_menu`");
        $result = $query->result_array();
        return $result;
    }
    public function get_all_footer_menu()
    {
        $query  = $this->db->query("SELECT * FROM  `footer_menu` ");
        $result = $query->result_array();
        return $result;
    }
    public function get_all_footer_submenu()
    {
        $query  = $this->db->query("SELECT footer_submenu.*,footer_menu.title FROM `footer_submenu`
                                    INNER JOIN footer_menu ON footer_menu.id = footer_submenu.`footer_menu` ");
        $result = $query->num_rows();
        return $result;
	}
	public function edit_submenu($id)
    {
        $query  = $this->db->query("SELECT footer_submenu . * , footer_menu.title
                                    FROM  `footer_submenu` 
                                    INNER JOIN footer_menu ON footer_menu.id = footer_submenu.`footer_menu` 
                                    WHERE footer_submenu.id = $id ");
        $result = $query->result_array();
        return $result;
	}
	public function edit_country_code_config($id)
    {
        $query  = $this->db->query("SELECT * FROM `country_table` WHERE `id` =  $id;");
        $result = $query->result_array();
        return $result;
	}
	public function get_country_code_config()
    {
        $query  = $this->db->query("SELECT * FROM  `country_table`");
        $result = $query->result_array();
        return $result;
    }
	
	public function contactreply_list($id)
    {
        $query  = $this->db->query("SELECT cr.*,c.email,c.message FROM  `contact_reply` as cr left join contact_form_details as c on cr.contact_id = c.id where cr.contact_id = $id");
        $result = $query->result_array();
        return $result;
    }
	
	
	public function check_admin_email($email)
	  {
		$this->db->select('*');
		$this->db->from('administrators');
			$this->db->where('email',$email);
			$this->db->where_in('role',[1,2]);
		  $result = $this->db->get()->row_array();
		return $result;
	  }
	  
	public function check_admin_emailbyid($email,$admin_id)
	  {
		$this->db->select('*');
		$this->db->from('administrators');
			$this->db->where('email',$email);
			$this->db->where('user_id !=',$admin_id);
			$this->db->where_in('role',[1,2]);
		  $result = $this->db->get()->row_array();
		return $result;
	  }
	  
	  
	  
	public function save_pwdlink_data($user_data)
   {
      $result  = $this->db->insert('forget_password_det',$user_data);
      $insert_id = $this->db->insert_id();
      return $insert_id;
   }
   
   
    public function update_pwdlink_data($data, $id) {
		$this->db->where('user_id',$id);
		$status = $this->db->update('forget_password_det', $data);
		return $status;
   
    }
	
	public function update_res_pwd($data, $id) {
	$this->db->where('user_id',$id);
	$status = $this->db->update('administrators', $data);
	return $status;

	}
	
	public function update_forpwd_status($data, $id) {
		$this->db->where('user_id',$id);
		$status = $this->db->update('forget_password_det', $data);
		return $status;
   
    }
	
	
   
	  
	
}
?>
