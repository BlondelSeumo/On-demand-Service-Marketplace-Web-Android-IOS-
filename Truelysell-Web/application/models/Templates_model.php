<?php

class Templates_model extends CI_Model {

    public function __construct() {
        parent::__construct();
    }

    public function get_templates_list($limit, $offset) {
        $data = array();
        $stmt = "SELECT a.*"
                . " FROM email_templates AS a"
                . " ORDER BY a.`template_id` DESC"
                . " LIMIT " . (int) $offset . ", " . (int) $limit;
        $query = $this->db->query($stmt);
        if ($query->num_rows()) {
            $data = $query->result_array();
        }
        return $data;
    }

    public function get_template_data($template_id) {
        $data = array();
        $stmt = "SELECT a.*"
                . " FROM email_templates AS a"
                . " WHERE a.`template_id` = " . (int) $template_id;
        $query = $this->db->query($stmt);
        if ($query->num_rows()) {
            $data = $query->row_array();
        }
        return $data;
    }
	 public function get_usertemplate_data($template) {
        $data = array();
        $stmt = "SELECT a.template_title, a.template_content"
                . " FROM email_templates AS a"
                . " WHERE a.`template_id` = '" .$template."'";
				
        $query = $this->db->query($stmt);
        if ($query->num_rows()) {
            $data = $query->row_array();
        }
        return $data;
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
	

    public function get_template_list()
    {
        $query = $this->db->query("SELECT * FROM  `email_templates` ");
        $result = $query->result_array();
        return $result;          
    }
    public function edit_template($id)
    {
        $query = $this->db->query(" SELECT * FROM `email_templates` WHERE `template_id` = $id ");
        $result = $query->row_array();
        return $result;
    }
}
