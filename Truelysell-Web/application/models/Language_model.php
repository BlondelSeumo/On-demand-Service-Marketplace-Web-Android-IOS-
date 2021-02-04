<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Language_model extends CI_Model {

	public function __construct() {

        parent::__construct();
        $this->load->database();
    }
	public function page_list()
	{
    
    $this->db->select('*');
    $this->db->from('pages');
    $query=$this->db->get();
    $result = $query->result_array();
    if(!empty($result)){
    return $result;
    }
    
    
    
		//return $this->db->get('pages')->result_array();
	}

	var $column_order = array(null, 'L.lang_key','L.lang_value','L.language');
	var $column_search = array('L.lang_key','L.lang_value','L.language');
	var $order = array('L.sno' => 'DESC'); // default order
	var $request_details  = 'app_language_management L';
	var $users  = 'pages P';
	private function p_get_datatables_query()
	{


		$this->db->select('L.*, P.page_title');
		$this->db->from($this->request_details);
		$this->db->join($this->users, 'P.page_key = L.page_key', 'left');
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

    public function language_list($page_key){
      $this->p_get_datatables_query();
        if($_POST['length'] != -1)
				$this->db->where('L.page_key', $page_key);
        $this->db->limit($_POST['length'], $_POST['start']);
        $this->db->group_by(array('L.page_key','L.lang_key'));
        $query = $this->db->get();
        return $query->result_array();
    }

    public function language_list_all($page_key){
			$this->db->where('L.page_key', $page_key);
      $this->db->from($this->request_details);
    	return $this->db->count_all_results();
    }

    public function language_list_filtered($page_key){

          $this->p_get_datatables_query();
					$this->db->where('L.page_key', $page_key);
          $this->db->group_by(array('L.page_key','L.lang_key'));
          $query = $this->db->get();
          return $query->num_rows();
    }
		public function currenct_page_key_value($inputs){ 
    	 
    	$my_keys = array();
    	if(!empty($inputs)){
    		foreach ($inputs as $input) {
    			$my_keys[] = $input['lang_key'];
    			$mypage_keys[] = $input['page_key'];
    		}
    	}
     

    	$my_final_values = array();
    	if(!empty($my_keys)){
    		
    		$this->db->select('sno,lang_key,lang_value,language,placeholder,validation1,validation2,validation3,type,page_key');
       		$this->db->from('app_language_management');
          	$this->db->where_in('lang_key',$my_keys);
            $this->db->where_in('page_key',$mypage_keys);
          	$this->db->order_by('lang_key');
          	
          	$my_final = $this->db->get()->result_array();

         
    		 if(!empty($my_final)){
    		 	foreach ($my_final as $keyvalue) {
    		 		//$my_final_values[$keyvalue['lang_key']][$keyvalue['language']] = $keyvalue['lang_value'];

    		 		$my_final_values[$keyvalue['lang_key']][$keyvalue['language']]['name'] = $keyvalue['lang_value'];
				    $my_final_values[$keyvalue['lang_key']][$keyvalue['language']]['placeholder'] = $keyvalue['placeholder'];
				    $my_final_values[$keyvalue['lang_key']][$keyvalue['language']]['validation1'] = $keyvalue['validation1'];
				    $my_final_values[$keyvalue['lang_key']][$keyvalue['language']]['validation2'] = $keyvalue['validation2'];
				    $my_final_values[$keyvalue['lang_key']][$keyvalue['language']]['validation3'] = $keyvalue['validation3'];
				    $my_final_values[$keyvalue['lang_key']][$keyvalue['language']]['type'] = $keyvalue['type'];
				    $my_final_values[$keyvalue['lang_key']][$keyvalue['language']]['lang_key'] = $keyvalue['lang_key'];

    		 		  		 	    
    		 	}
    		 }

    	}
    	// print_r($my_final_values);
    	// exit;
    		
     return $my_final_values;  
    } 

		public function get_language_id($page_key){
				$this->db->select('p_id');
				$this->db->where('page_key', $page_key);
        $query = $this->db->get('pages');
        return $query->row_array();
    }

		public function get_keyword_id($page_key){
				$this->db->select('sno');
				$this->db->where('page_key', $page_key);
        $query = $this->db->get('pages');
        return $query->row_array();
    }

    public function active_language()

    {

        $query = $this->db->query("SELECT language, language_value,tag FROM `language` WHERE status = 1");
        $result = $query->result_array();
        return $result;

    }

	//     public function keyword_model()

	//     { 

	//         if (!empty($_POST['keyword'])) {

	            

	//         $data = array();

	//         $lang =trim($_POST['keyword']);

	//         $language = 'lg_'.str_replace(array(' ','!','&'),'_',strtolower($lang));

	//         $data['lang_key'] = $language;

	//         $data['page_key'] = $_POST['page_key'];

	//         $data['lang_value'] = $lang;

	//         $data['language'] = 'en';

	//         $this->db->where($data);

	//         $record = $this->db->count_all_results('language_management');

	//         if($record == 1)

	//         {

	//             return false;

	//         }else{

	//             $result = $this->db->insert('language_management', $data);

	//             return $result;

	//        }

	//     }

	    

	//     $already_exits = array();

	//         if (!empty($_POST['multiple'])) {

	                

	//             $data = array();

	            

	//             $multiple = $_POST['multiple'];

	            

	//             $multiple_keyword = explode(',',$_POST['multiple']);

	            

	//             $multiple_keyword = array_filter($multiple_keyword);



	//             if (!empty($multiple_keyword)) {

	//                 foreach ($multiple_keyword as $lang) {

	//                     $lang = trim($lang);

	//                     if ($lang != null) {

	                    

	//                     $language = 'lg_'.str_replace(array(' ','!','&'),'_',strtolower($lang));

	//                     $data['lang_key'] = $language;

	//                     $data['lang_value'] = $lang;

	//                     $data['page_key'] = $_POST['page_key'];

	//                     $data['language'] = 'en';

	//                     $this->db->where($data);

	//                     $record = $this->db->count_all_results('language_management');

	//                     if($record == 1)

	//                         {

	//                             $already_exits[] = $lang;

	//                         }else{

	                        

	//                             $result = $this->db->insert('language_management', $data);  

	//                        }

	//                     }



	//                     }

	//             }



	//             return $already_exits;

	//         }

	// }


	public function keyword_model()

	    { 

	        if (!empty($_POST['field_name'])) {

	            

	        $data = array();
	        $datas = array();

	        $field_name =trim($_POST['field_name']);

	        $data['lang_key'] = $field_name;
	        $data['page_key'] = $_POST['page_key'];
	        $data['language'] = 'en';

	        $this->db->where($data);

	        $record = $this->db->count_all_results('app_language_management');

	        if($record >= 1)

	        {

	            return false;

	        }else{

	        	$datas['lang_key'] = $field_name;
	        	$datas['lang_value'] = trim($_POST['name']);
	        	$datas['page_key'] = $_POST['page_key'];
	        	$datas['type'] = $_POST['type'];
	        	$datas['placeholder'] = !empty($_POST['placeholder'])?$_POST['placeholder']:'';
	        	$datas['validation1'] = !empty($_POST['validation1'])?$_POST['validation1']:'';
	        	$datas['validation2'] = !empty($_POST['validation2'])?$_POST['validation2']:'';
	        	$datas['validation3'] = !empty($_POST['validation3'])?$_POST['validation3']:'';
	            $datas['language'] = 'en';

	            $result = $this->db->insert('app_language_management', $datas);

	            return $result;

	       }

	    }

	    


	}


	public function page_model()

	    { 

	        if (!empty($_POST['page_name'])) {

		        $data = array();

		        $page_name =trim($_POST['page_name']);

		        $page_key = str_replace(array(' ','!','&'),'_',strtolower($page_name));

		        $data['page_title'] = $page_name;

		        $data['page_key'] = $page_key;

		        $data['status'] = 1;

		         $this->db->where($data);

		        $record = $this->db->count_all_results('pages');

		        if($record >= 1)

		        {

		            return false;

		        }else{

		            $result = $this->db->insert('pages', $data);

		            return $result;

		       }

	    }
   

	}



	public function language_model()

    { 



        $where = array();

        $where['tag'] = $this->input->post('tag');

        $where['language'] = $this->input->post('language');

        $where['language_value'] = $this->input->post('value');

        $this->db->where($where);

        $record = $this->db->count_all_results('language');

        

        if($record == 1)

        {

            return false;

        }else{  



        $data = array(

        'language_value' => trim($this->input->post('value')),

        'language' => trim($this->input->post('language')),

        'tag' => trim($this->input->post('tag')),

        'status' => 2

        );



       $result = $this->db->insert('language',$data);

        return $result;

        }

    }

    public function lang_data()

    {

        $query = $this->db->query(" SELECT * FROM language");

        return $query->result_array();

    }
}
