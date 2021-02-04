<?php
if ( ! defined('BASEPATH')) exit('No direct script access allowed');
class Payment_model extends CI_Model
{

  public function __construct()
  {
    parent::__construct();
  }
 
 

    private function p_get_datatables_query()
    {

  

      $this->db->select('B.id','B.service_id','B.provider_id','B.user_id','B.amount','B.tokenid','U.name','S.service_title','P.id','P.name');
      $this->db->from($this->book_service);
      $this->db->join($this->users,'U.id=B.user_id','left');
      $this->db->join($this->providers,'P.id=B.provider_id','left');
      $this->db->join($this->services,'S.id=B.service_id','left');
      $this->db->where('tokenid !=', '');
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

    public function payment_list(){

      $this->db->select('*');
      $this->db->from('book_service');
      $this->db->where('tokenid !=', '');
      $this->db->order_by('id','desc');
      $query = $this->db->get();
      return $query->result_array();
    }

    public function payment_list_all(){

      $this->db->from('book_service');
      $this->db->where('tokenid !=', '');
          return $this->db->count_all_results();
    }

    public function payment_list_filtered(){

          $this->p_get_datatables_query();
          $query = $this->db->get();
          return $query->num_rows();
    }

    public function add_payment($user_post_data)
     {
       
        $result = $this->db->insert('admin_payment',$user_post_data);
       

        return $result;
     }

     /*payment filter*/
     public function payment_filter($provider_id,$status,$from,$to){
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
                $this->db->select('*');
                $this->db->from('book_service');

                if(!empty($provider_id)){
                $this->db->where('provider_id',$provider_id);
                }
                if(!empty($status)){
                $this->db->where('status',$status);
                }
                
                if(!empty($from_date)){
                $this->db->where('service_date >=',$from_date);
                }
                if(!empty($to_date)){
                $this->db->where('service_date <=',$to_date);
                }
                return $this->db->get()->result_array();
                }

  

     public function search_all($provider_id,$status)
    {
      $this->db->select('*');
      $this->db->from('book_service');
      return $this->db->where(array('provider_id'=>$provider_id,'status'=>$status))->get()->result_array();
    }

     public function search_provider($provider_id)
    {
      $this->db->select('*');
      $this->db->from('book_service');
      return $this->db->where('provider_id',$provider_id)->get()->result_array();
    }

    public function search_status($status)
    {
      $this->db->select('*');
      $this->db->from('book_service');
      return $this->db->where('status',$status)->get()->result_array();
    }
    
     
  
    
}
?>
