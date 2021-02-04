<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Language extends CI_Controller {

   public $data;

   public function __construct() {

        parent::__construct();
//        $this->load->model('language_model','language');
        $this->data['view'] = 'admin';
        $this->data['base_url'] = base_url();
        $this->session->keep_flashdata('error_message');
        $this->session->keep_flashdata('success_message');
        $this->load->helper('user_timezone_helper');

    }

  	public function index()
  	{
  		redirect(base_url('language'));
  	}
  	public function pages()
  	{
      if($this->session->userdata('admin_id'))
  		{
    		$this->data['page'] = 'page_list';
    		$this->data['model'] = 'language';
                $this->data['pages'] = $this->language->page_list();
    		$this->load->vars($this->data);
    		$this->load->view('template');
  		}
  		else {
  			redirect(base_url()."admin");
  		}
  	}
    public function language()
  	{
      if($this->session->userdata('admin_id'))
  		{
    		$this->data['page'] = '';
    		//$this->data['model'] = 'language';
    		$this->load->vars($this->data);
    		$this->load->view('template');
  		}
  		else {
  			redirect(base_url()."admin");
  		}
  	}
    public function language_list()
  	{
        $page_key = $this->input->post('page_key');
  			$lists = $this->language->language_list($page_key);
  	        $data = array();
  	        $no = $_POST['start'];
            $active_language = array('en','ma','ar');
  	        foreach ($lists as $keyword) {
              $no++;
            $row    = array();
            $row[] = $no;
            $exist_key = array();
              if (!empty($active_language))
               {
              $l = 0;
               foreach ($active_language as $lang)
                                                {
               $lg_language_name = $keyword['lang_key'];
                $language_key = $lang;



          $key = $keyword['language'];
         $value = ($language_key == $key)?$keyword['lang_value']:'';
       $page_key = $keyword['page_key'];
         $key = $keyword['language'];
        $this->data['currenct_page_key_value'] = $this->language->currenct_page_key_value($lists);
      $value = (!empty($this->data['currenct_page_key_value'][$lg_language_name][$language_key]))?$this->data['currenct_page_key_value'][$lg_language_name][$language_key]:'';


      $row[] = '<input type="text" class="form-control update_language" name="'.$lg_language_name.'['.$language_key.']" value="'.$value.'" data-lang-key="'.$lg_language_name.'" data-lang="'.$language_key.'" data-page="'.$page_key.'" >
       <input type="hidden" class="form-control" name="prev_'.$lg_language_name.'['.$language_key.']" value="'.$value.'">';

         $l++;
                                                   }

                                               }
  	            $data[] = $row;
          }

          $output = array(
      "draw" => $_POST['draw'],
                          "recordsTotal" => $this->language->language_list_all($page_key),
                          "recordsFiltered" => $this->language->language_list_filtered($page_key),
                          "data" => $data,
                  );

          //output to json format
          echo json_encode($output);

  	}


  	public function add_keyword()
  	{
      if($this->session->userdata('admin_id'))
  		{
    		$this->data['page'] = 'add_keyword';
    		$this->data['model'] = 'language';
    		$this->load->vars($this->data);
    		$this->load->view('template');
  		}
  		else {
  			redirect(base_url()."admin");
  		}

  	}

    public function save_keywords()
    {
	    	$data = array();
    	  $pdata = array();
	    	$page_key = $this->input->post('page_key');
	    	$multiple = $this->input->post('multiple_key');
	    	$multiple_keyword = explode(',',$multiple);
	    	$multiple_keyword = array_filter($multiple_keyword);

	    	if(!empty($multiple_keyword)) {
	    		foreach($multiple_keyword as $lang) {
	    			$lang = trim($lang);
	    			if($lang != null) {
          $lang_for_key = preg_replace("/[^ \w]+/", "", $lang);
          $count = strlen($lang_for_key);
          if($count > 15)
          {
            $lang_for_key = substr($lang_for_key, 0, 15);
          }
          $lang_det = $this->language->get_language_id($page_key);
          $lang_id = $lang_det['p_id'];

	    		$language = 'lg'.$lang_id.'_'.str_replace(array(' ','!','&'),'_',strtolower($lang_for_key));
	    		$data['page_key'] = $pdata['page_key'] = $page_key;
	    		$data['lang_key'] = $language;
					$data['lang_value'] = $pdata['lang_value'] = $lang;
					$data['language'] = $pdata['language'] = 'en';
					$this->db->where($pdata);
	    			$record = $this->db->count_all_results('language_management');
	    			if($record > 0)
				    	{
				    		$already_exits[] = $lang;
				    	}else{
						$cdata['page_key'] = $page_key;
  	    		$cdata['lang_key'] = $language;
  					$cdata['language'] = 'en';
  					$this->db->where("page_key = '".$page_key."' AND lang_key LIKE '".$language."%' AND language = 'en'");
	    			$chk_record = $this->db->count_all_results('language_management');
            if($chk_record > 0){
              $data['lang_key'] = $language.$chk_record;
            }
							$result = $this->db->insert('language_management', $data);
					   }
					}

	    		}
	    	}
       if(!empty($already_exits))
       {
        $this->session->set_flashdata('success_message','Keywords added successfully, But some keywords already exist');
       }
       else
       {
         $this->session->set_flashdata('success_message','Keywords added successfully');
       }
      echo 1;
    }


		public function update_language()
		{
			$lang_key = $insert['lang_key'] = $this->input->post('lang_key');
			$lang = $insert['language'] = $this->input->post('lang');
      $page_key = $insert['page_key'] = $this->input->post('page_key');
      $data['lang_value'] = $insert['lang_value'] = $this->input->post('cur_val');
      $this->db->where('lang_key',$lang_key);
			$this->db->where('language',$lang);
			$this->db->from('language_management');
      $ext = $this->db->count_all_results();
      if($ext >0 ){
        if($lang == 'en')
        {
          if(!empty($data['lang_value']))
          {
           
            $lang_det = $this->language->get_language_id($page_key);
            $lang_id = $lang_det['p_id'];

            $check['page_key'] = $page_key;
            $check['lang_value'] = $data['lang_value'];
  					$check['language'] = 'en';
  					$this->db->where($check);
  	    		$record = $this->db->count_all_results('language_management');
  	    		if($record == 0)
  				  {
        			$this->db->where('page_key',$page_key);
        			$this->db->where('lang_key',$lang_key);
        			$this->db->where('language',$lang);
        			$result = $this->db->update('language_management',$data);
             
            }
            else {
              $result = 0;
            }
          }
          else {
            $result = 2;
          }
        }
        else{
    			$this->db->where('page_key',$page_key);
    			$this->db->where('lang_key',$lang_key);
    			$this->db->where('language',$lang);
    			$result = $this->db->update('language_management',$data);
        }
      }
      else {
        $result = $this->db->insert('language_management',$insert);
      }
			echo $result;
			die();
		}

  }
