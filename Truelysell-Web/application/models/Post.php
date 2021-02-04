<?php defined('BASEPATH') OR exit('No direct script access allowed'); 
 
class Post extends CI_Model{ 
     
    function __construct() { 
        // Set table name 
        $this->table = 'services'; 
    } 
     
    /* 
     * Fetch records from the database 
     * @param array filter data based on the passed parameters 
     */ 
    function getRows($params = array()){ 
        $this->db->select("services.currency_code,services.id,services.user_id,services.service_title,services.service_amount,services.mobile_image,c.category_name,sc.subcategory_name,services.rating_count,services.service_location"); 
        $this->db->from($this->table); 
           $this->db->join('categories c', 'c.id = services.category', 'LEFT');
          $this->db->join('subcategories sc', 'sc.id = services.subcategory', 'LEFT');
           $this->db->where("services.status = 1");
          $this->db->where('services.user_id',$this->session->userdata('id'));
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
                $this->db->order_by('id', 'desc'); 
                if(array_key_exists("start",$params) && array_key_exists("limit",$params)){ 
                    $this->db->limit($params['limit'],$params['start']); 
                }elseif(!array_key_exists("start",$params) && array_key_exists("limit",$params)){ 
                    $this->db->limit($params['limit']); 
                } 
          
                $query = $this->db->get(); 
                $result = ($query->num_rows() > 0)?$query->result_array():FALSE; 
            } 
        } 
         
        // Return fetched data 
        return $result; 
    } 
     function getInactiveRows($params = array()){ 
        $this->db->select("services.currency_code,services.id,services.user_id,services.service_title,services.service_amount,services.mobile_image,c.category_name,sc.subcategory_name,services.rating_count,services.service_location"); 
        $this->db->from($this->table); 
           $this->db->join('categories c', 'c.id = services.category', 'LEFT');
          $this->db->join('subcategories sc', 'sc.id = services.subcategory', 'LEFT');
           $this->db->where("services.status = 2");
          $this->db->where('services.user_id',$this->session->userdata('id'));
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
                $this->db->order_by('id', 'desc'); 
                if(array_key_exists("start",$params) && array_key_exists("limit",$params)){ 
                    $this->db->limit($params['limit'],$params['start']); 
                }elseif(!array_key_exists("start",$params) && array_key_exists("limit",$params)){ 
                    $this->db->limit($params['limit']); 
                } 
          
                $query = $this->db->get(); 
                $result = ($query->num_rows() > 0)?$query->result_array():FALSE; 
            } 
        } 
         
        // Return fetched data 
        return $result; 
    } 
}