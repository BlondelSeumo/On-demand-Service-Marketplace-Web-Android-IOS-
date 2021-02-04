<?php

if (!defined('BASEPATH'))
    exit('No direct script access allowed');

class Api_model extends CI_Model {

    public function __construct() {
        parent::__construct();
        $this->load->database();
        $this->load->helper('user_timezone');
        date_default_timezone_set('Asia/kolkata');
        $this->date = date('Y-m-d');
        $this->date = utc_date_conversion($this->date);
        $this->date = date('Y-m-d', strtotime($this->date));
        $this->base_url = base_url();
    }

    public function languages_list() {
        $this->db->select('language,language_value,tag');
        $this->db->from('language');
        $this->db->where('status', '1');
        $records = $this->db->get()->result_array();
        return $records;
    }

    public function language_list($key) {
        $this->db->select('lang_key,lang_value,language,placeholder,validation1,validation2,validation3,type,page_key');
        $this->db->from('app_language_management');
        $this->db->where('language', 'en');
        $this->db->where('type', 'App');
        $records = $this->db->get()->result_array();


        $language = array();
        if (!empty($records)) {
            foreach ($records as $record) {
                $this->db->select('lang_key,lang_value,language,placeholder,validation1,validation2,validation3,type,page_key');
                $this->db->from('app_language_management');
                $this->db->where('language', $key);
                $this->db->where('type', 'App');
                $this->db->where('page_key', $record['page_key']);
                $this->db->where('lang_key', $record['lang_key']);
                $eng_records = $this->db->get()->row_array();
                if (!empty($eng_records['lang_value'])) {

                    $language['language'][$record['page_key']][$record['lang_key']]['name'] = $eng_records['lang_value'];
                    $language['language'][$record['page_key']][$record['lang_key']]['placeholder'] = $eng_records['placeholder'];
                    $language['language'][$record['page_key']][$record['lang_key']]['validation1'] = $eng_records['validation1'];
                    $language['language'][$record['page_key']][$record['lang_key']]['validation2'] = $eng_records['validation2'];
                    $language['language'][$record['page_key']][$record['lang_key']]['validation3'] = $eng_records['validation3'];
                } else {
                    $language['language'][$record['page_key']][$record['lang_key']]['name'] = $record['lang_value'];
                    $language['language'][$record['page_key']][$record['lang_key']]['placeholder'] = $record['placeholder'];
                    $language['language'][$record['page_key']][$record['lang_key']]['validation1'] = $record['validation1'];
                    $language['language'][$record['page_key']][$record['lang_key']]['validation2'] = $record['validation2'];
                    $language['language'][$record['page_key']][$record['lang_key']]['validation3'] = $record['validation3'];
                }
            }
        }
        return $language;
    }

    public function get_user_id_using_token($token) {
        if ($token != '') {
            $this->db->select('*');
            $records = $this->db->get_where('providers', array('token' => $token))->row_array();
            if (!empty($records)) {
                return $records['id'];
            } else {
                return 0;
            }
        }
        return 0;
    }

    public function get_users_id_using_token($token) {
        if ($token != '') {
            $this->db->select('*');
            $records = $this->db->get_where('users', array('token' => $token))->row_array();
            if (!empty($records)) {
                return $records['id'];
            } else {
                return 0;
            }
        }
        return 0;
    }

    public function get_category() {
        $this->db->select('c.id,c.category_name,c.category_image, (SELECT COUNT(s.id) FROM services AS s WHERE s.category=c.id AND s.status=1 ) AS category_count');
        $this->db->from('categories c');
        $this->db->where('c.status', 1);
        $this->db->join('subcategories s', 'c.id = s.category', 'INNER');
        $this->db->group_by('c.id');
        $this->db->order_by('category_count', 'DESC');

        $this->db->limit(6);
        $result = $this->db->get()->result_array();
        return $result;
    }

    public function get_categories() {
        $this->db->select('c.id,c.category_name,c.category_image');
        $this->db->from('categories c');
        $this->db->join('subcategories s', 'c.id = s.category', 'INNER');
        $this->db->where('c.status', 1);

        $this->db->group_by('c.id');
        $result = $this->db->get()->result_array();
        return $result;
    }

    public function get_subcategories($category) {
        $this->db->select('id,subcategory_name,subcategory_image');
        $this->db->from('subcategories');
        $this->db->where('status', 1);
        $this->db->where('category', $category);
        $result = $this->db->get()->result_array();
        return $result;
    }

    public function check_email($inputs = '') {
        $email = $inputs['email'];
        $this->db->where('email', $email);
        return $this->db->count_all_results('providers');
    }

    public function check_mobile_no($inputs = '') {

        $mobileno = $inputs['mobileno'];
        $this->db->where(array('country_code' => $inputs['country_code'], 'mobileno' => $mobileno));
        return $this->db->count_all_results('providers');
    }

    public function check_user_email($inputs = '') {
        $email = $inputs['email'];
        $this->db->where('email', $email);
        return $this->db->count_all_results('users');
    }

    public function check_user_mobileno($inputs = '') {

        $mobileno = $inputs['mobileno'];
        $this->db->where(array('country_code' => $inputs['country_code'], 'mobileno' => $mobileno));
        return $this->db->count_all_results('users');
    }

    public function provider_signup($user_details, $device_data) {

        $user_details['created_at'] = date('Y-m-d H:i:s');
        $result = $this->db->insert('providers', $user_details);
        $records = array();
        if ($result) {
            $user_id = $this->db->insert_id();
            $token = $this->getToken(14, $user_id);
            /* insert wallet */
            $data = array(
                "token" => $token,
                'currency_code' => settings('currency'),
                "user_provider_id" => $user_id,
                "type" => 1,
                "wallet_amt" => 0,
                "created_at" => utc_date_conversion(date('Y-m-d H:i:s'))
            );
            $wallet_result = $this->db->insert('wallet_table', $data);
            /* insert wallet */

            $this->db->where('id', $user_id);
            $this->db->update('providers', array('token' => $token));
            $profile_img = base_url() . 'assets/img/professional.png';


            $device_type = $device_data['device_type'];
            $device_id = $device_data['device_id'];
            $date = date('Y-m-d H:i:s');
            $devicetype = strtolower($device_type);

            $deviceid = $device_id;
            $type = '1';

            $this->db->insert('device_details', array('user_id' => $user_id, 'device_type' => $devicetype, 'device_id' => $deviceid, 'created' => $date, 'type' => $type));

            $this->db->select('name,email,country_code,mobileno,category,subcategory,IF(profile_img IS NULL or profile_img = "", "' . $profile_img . '", profile_img) as profile_img,token', 'type', 'user_type');
            $this->db->where('id', $user_id);
            $records = $this->db->get('providers')->row_array();
        }
        return $records;
    }

    public function provider_update($inputs, $where) {
        $inputs['updated_at'] = date('Y-m-d H:i:s');
        $this->db->set($inputs);
        $this->db->where($where);
        $this->db->update('providers');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    public function user_update($inputs, $where) {
        $inputs['updated_at'] = date('Y-m-d H:i:s');
        $this->db->set($inputs);
        $this->db->where($where);
        $this->db->update('users');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    public function user_signup($user_details, $device_data) {
        $user_details['created_at'] = date('Y-m-d H:i:s');
        $result = $this->db->insert('users', $user_details);
        $records = array();
        if ($result) {
            $user_id = $this->db->insert_id();
            $token = $this->getToken(14, $user_id);

            /* insert wallet */
            $data = array(
                "token" => $token,
                'currency_code' => settings('currency'),
                "user_provider_id" => $user_id,
                "type" => 2,
                "wallet_amt" => 0,
                "created_at" => utc_date_conversion(date('Y-m-d H:i:s'))
            );
            $wallet_result = $this->db->insert('wallet_table', $data);
            /* insert wallet */
            $this->db->where('id', $user_id);
            $this->db->update('users', array('token' => $token));

            $device_type = $device_data['device_type'];

            $device_id = $device_data['device_id'];
            $date = date('Y-m-d H:i:s');
            $devicetype = strtolower($device_type);

            $deviceid = $device_id;
            $type = '2';

            $this->db->insert('device_details', array('user_id' => $user_id, 'device_type' => $devicetype, 'device_id' => $deviceid, 'created' => $date, 'type' => $type));

            $this->db->select('*');
            $this->db->where('id', $user_id);
            $records = $this->db->get('users')->row_array();
        }
        return $records;
    }

    public function get_service($type, $inputs) {


        $latitude = $inputs['latitude'];

        $longitude = $inputs['longitude'];

        $radius = 10;


        $longitude_min = $longitude - 100 / abs(cos(deg2rad($longitude)) * 69);

        $longitude_max = $longitude + 100 / abs(cos(deg2rad($longitude)) * 69);

        $latitude_min = $latitude - (100 / 69);

        $latitude_max = $latitude + (100 / 69);



        $this->db->select("u.id as prd,s.id,s.currency_code,s.service_title,s.currency_code,s.service_amount,s.service_location,s.service_image,s.service_latitude,s.service_longitude,c.category_name,u.profile_img,1.609344 * 3956 * 2 * ASIN(SQRT( POWER(SIN((" . $latitude . " - s.service_latitude) *  pi()/180 / 2), 2) +COS(" . $latitude . " * pi()/180) * COS(s.service_latitude * pi()/180) * POWER(SIN((" . $longitude . " - s.service_longitude) * pi()/180 / 2), 2) )) AS distance");
        $this->db->from('services s');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('providers u', 'u.id = s.user_id', 'LEFT');
        $this->db->where("s.status = 1");
        $this->db->having('distance <=', $radius);


        if ($type == '1') {
            $this->db->order_by('s.total_views', 'DESC');
        } elseif ($type == '2') {
            $this->db->order_by('s.id', 'DESC');
        }

        $this->db->limit(10);
        $query = $this->db->get();

        if ($query) {
            $result = $query->result_array();
        }



        if (count($result) > 0) {

            $response = array();
            $data = array();
            foreach ($result as $r) {

                
                $rating_count = $this->db->where(array("service_id" => $r['id'], 'status' => 1))->count_all_results('rating_review');


                $this->db->select('AVG(rating)');
                $this->db->where(array('service_id' => $r['id'], 'status' => 1));
                $this->db->from('rating_review');

                $rating = $this->db->get()->row_array();

                $avg_rating = round($rating['AVG(rating)'], 2);

                if($inputs['user_id']){
					$data_currency_info = get_api_provider_currency($inputs['user_id']);
					$data_currency = $data_currency_info['user_currency_code'];
				}else if($inputs['users_id']){
					$data_currency_info = get_api_user_currency($inputs['users_id']);
					$data_currency = $data_currency_info['user_currency_code'];
				}else{
					$data_currency = settings('currency');
				}


                $serviceimage = explode(',', $r['service_image']);
                $data['service_id'] = $r['id'];
                $data['service_title'] = $r['service_title'];
                $data['service_amount'] = get_gigs_currency($r['service_amount'], $r['currency_code'], $data_currency);
				$data['currency_code'] = $data_currency; 
                $data['service_image'] = $serviceimage[0];
                $data['category_name'] = $r['category_name'];
                $data['ratings'] = "$avg_rating";
                $data['rating_count'] = "$rating_count";
                if (is_null($r['profile_img'])) {
                    $data['user_image'] = "";
                } else {
                    $data['user_image'] = $r['profile_img'];
                }

                $data['currency'] =  currency_code_sign($data_currency);
                $response[] = $data;
            }

            return $response;
        } else {

            return array();
        }
    }

    public function get_demo_service($type, $inputs) {



        $this->db->select("s.id,s.service_title,s.service_amount,s.currency_code,s.service_location,s.service_image,s.service_latitude,s.service_longitude,c.category_name,u.profile_img");
        $this->db->from('services s');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('providers u', 'u.id = s.user_id', 'LEFT');
        $this->db->where("s.status = 1");
        $this->db->where('u.name=', 'demo');


        if ($type == '1') {
            $this->db->order_by('s.total_views', 'DESC');
        } elseif ($type == '2') {
            $this->db->order_by('s.id', 'DESC');
        }

        $this->db->limit(10);
        $result = $this->db->get()->result_array();

        if (count($result) > 0) {

            $response = array();
            $data = array();
            foreach ($result as $r) {

                $rating_count = $this->db->where(array("service_id" => $r['id'], 'status' => 1))->count_all_results('rating_review');


                $this->db->select('AVG(rating)');
                $this->db->where(array('service_id' => $r['id'], 'status' => 1));
                $this->db->from('rating_review');

                $rating = $this->db->get()->row_array();

                $avg_rating = round($rating['AVG(rating)'], 2);
				if($inputs['user_id']){
					$data_currency_info = get_api_provider_currency($inputs['user_id']);
					$data_currency = $data_currency_info['user_currency_code'];
				}else if($inputs['users_id']){
					$data_currency_info = get_api_user_currency($inputs['users_id']);
					$data_currency = $data_currency_info['user_currency_code'];
				}else{
					$data_currency = settings('currency');
				}

                $serviceimage = explode(',', $r['service_image']);
                $data['service_id'] = $r['id'];
                $data['service_title'] = $r['service_title'];
                $data['service_amount'] = get_gigs_currency($r['service_amount'], $r['currency_code'], $data_currency);
                $data['currency_code'] = $data_currency;
                $data['service_image'] = $serviceimage[0];
                $data['category_name'] = $r['category_name'];
                $data['ratings'] = "$avg_rating";
                $data['rating_count'] = "$rating_count";
                $data['user_image'] = $r['profile_img'];
                $data['currency'] = currency_code_sign($data_currency);
                $response[] = $data;
            }

            return $response;
        } else {

            return array();
        }
    }

    public function get_my_service($inputs) {
        $user_id = $inputs['user_id'] ? $inputs['user_id'] : $this->get_user_id_using_token($inputs['token']);
        $this->db->select("s.id,s.service_title,s.service_amount,s.service_location,s.service_image,c.category_name,s.status,s.currency_code");
        $this->db->from('services s');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->where("s.status != 0");
        $this->db->where("s.user_id", $user_id);

        $this->db->order_by('s.id', 'DESC');
        if (!empty($inputs['type']) && isset($inputs['type'])) {
            $this->db->where('s.status=', $inputs['type']);
        }


        $query = $this->db->get();

        if ($query) {
            $result = $query->result_array();
        }



        if (count($result) > 0) {

            $provider_currency = get_api_provider_currency($user_id);
            $ProviderCurrency = $provider_currency['user_currency_code'];

            $response = array();
            $data = array();
            foreach ($result as $r) {

                $rating_count = $this->db->where(array("service_id" => $r['id'], 'status' => 1))->count_all_results('rating_review');


                $this->db->select('AVG(rating)');
                $this->db->where(array('service_id' => $r['id'], 'status' => 1));
                $this->db->from('rating_review');
                $rating = $this->db->get()->row_array();
                $avg_rating = round($rating['AVG(rating)'], 2);

                $ServiceAmount = (!empty($ProviderCurrency) && $r['currency_code'] != '') ? get_gigs_currency($r['service_amount'], $r['currency_code'], $ProviderCurrency) : $r['service_amount'];
                $serviceimage = explode(',', $r['service_image']);
                $data['service_id'] = $r['id'];
                $data['service_title'] = $r['service_title'];
                $data['service_amount'] = (string) $ServiceAmount;
                $data['service_image'] = $serviceimage[0];
                $data['category_name'] = $r['category_name'];
                $data['ratings'] = "$avg_rating";
                $data['rating_count'] = "$rating_count";
                $data['user_image'] = $serviceimage[0];
                $data['currency_code'] = $r['currency_code'];
                if ($r['status'] == 1) {
                    $data['is_active'] = $r['status'];
                } else if ($r['status'] == 2) {
                    $data['is_active'] = "0";
                }

                $data['currency'] = (!empty($ProviderCurrency)) ? currency_code_sign($ProviderCurrency) : currency_code_sign(settings('currency'));
                $response[] = $data;
            }

            return $response;
        } else {

            return array();
        }
    }

    public function get_service_details($inputs, $user_id, $type = '') {

        $this->db->select("s.*,c.category_name");
        $this->db->from('services s');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->where("s.status = 1 AND s.id='" . $inputs['id'] . "'");
        $result = $this->db->get()->row_array();

        $rating_count = $this->db->where(array("service_id" => $inputs['id'], 'status' => 1))->count_all_results('rating_review');


        $this->db->select('AVG(rating)');
        $this->db->where(array('service_id' => $inputs['id'], 'status' => 1));
        $this->db->from('rating_review');
        $rating = $this->db->get()->row_array();
        $avg_rating = round($rating['AVG(rating)'], 2);

        $this->db->select("service_image");
        $this->db->from('services_image');
        $this->db->where("service_id", $inputs['id']);
        $this->db->where("status", 1);
        $services_image = $this->db->get()->result_array();

        $service_img = array();
        foreach ($services_image as $key => $i) {
            $service_img[] = $i['service_image'];
        }
       $ProviderCurrency = settings('currency');
        if ($type == 1) {
            $provider_currency = get_api_provider_currency($user_id);
            $ProviderCurrency = $provider_currency['user_currency_code'];
        } else {
            $provider_currency = get_api_user_currency($user_id);
            $ProviderCurrency = $provider_currency['user_currency_code'];
        }
    if(empty($ProviderCurrency)){
    $ProviderCurrency = settings('currency');
    }
        
        if (!empty($result)) {
            $service_amt=get_gigs_currency($result['service_amount'], $result['currency_code'], $ProviderCurrency);
            $service['service_id'] = $result['id'];
            $service['service_title'] = $result['service_title'];
            $service['service_amount'] = (string) $service_amt;
            $service['service_image'] = $service_img;
            $service['category_name'] = $result['category_name'];
            $service['service_offered'] = $result['service_offered'];
            $service['service_latitude'] = $result['service_latitude'];
            $service['service_longitude'] = $result['service_longitude'];
            $service['about'] = $result['about'];
            $service['ratings'] = "$avg_rating";
            $service['rating_count'] = "$rating_count";
            $service['views'] = $result['total_views'];
            $service['currency'] = currency_code_sign($ProviderCurrency);




            $seller_overview = $this->db->where('id', $result['user_id'])->get('providers')->row_array();

            $this->db->select("s.*,c.category_name");
            $this->db->from('services s');
            $this->db->join('categories c', 'c.id = s.category', 'LEFT');
            $this->db->where('s.user_id', $seller_overview['id']);
            $this->db->where('s.status', 1);
            $this->db->where_not_in('s.id', $inputs['id']);
            $get_services = $this->db->get()->result_array();

            $get_bookings = $this->db->where("user_id", $user_id)->count_all_results('book_service');


            $seller['name'] = $seller_overview['name'];
            $seller['email'] = $seller_overview['email'];
            if ($get_bookings > 0) {
                $seller['mobileno'] = $seller_overview['mobileno'];
            } else {
                $seller['mobileno'] = "xxxxxxxxxx";
            }
            $seller['profile_img'] = $seller_overview['profile_img'];
            $seller['location'] = $result['service_location'];
            $seller['latitude'] = $result['service_latitude'];
            $seller['longitude'] = $result['service_longitude'];
            $seller['location'] = $result['service_location'];
            $seller['country_code'] = $seller_overview['country_code'];



            if (is_array($get_services) && !empty($get_services)) {
                foreach ($get_services as $key => $c) {


                    $this->db->select("service_image");
                    $this->db->from('services_image');
                    $this->db->where("service_id", $c['id']);
                    $this->db->where("status", 1);
                    $image = $this->db->get()->result_array();

                    $this->db->select("*");
                    $this->db->from('providers');
                    $this->db->where("id", $c['user_id']);
                    $provider_details = $this->db->get()->row_array();


                    $serv_image = array();
                    foreach ($image as $key => $i) {
                        $serv_image = $i['service_image'];
                    }

                    $rating_count = $this->db->where("service_id", $c['id'])->count_all_results('rating_review');

                    $this->db->select('AVG(rating)');
                    $this->db->where(array('service_id' => $c['id'], 'status' => 1));
                    $this->db->from('rating_review');
                    $rating = $this->db->get()->row_array();
                    $avg_rating = round($rating['AVG(rating)'], 2);

                    $c_amt=get_gigs_currency($c['service_amount'], $c['currency_code'], $ProviderCurrency);
                    $seller_services['service_id'] = $c['id'];
                    $seller_services['service_title'] = $c['service_title'];
                    $seller_services['service_amount'] = (string) $c_amt;
                    $seller_services['service_image'] = $serv_image;
                    $seller_services['name'] = $provider_details['name'];
                    $seller_services['profile_img'] = $provider_details['profile_img'];
                    $seller_services['category'] = $c['category_name'];
                    $seller_services['service_offered'] = $c['service_offered'];
                    $seller_services['service_latitude'] = $c['service_latitude'];
                    $seller_services['service_longitude'] = $c['service_longitude'];
                    $seller_services['about'] = $c['about'];
                    $seller_services['ratings'] = "$avg_rating";
                    $seller_services['rating_count'] = "$rating_count";
                    $seller_services['views'] = $c['total_views'];
                    $seller_services['currency'] = currency_code_sign($ProviderCurrency);
                    $service_details[] = $seller_services;




                    $response['service_overview'] = $service;
                    $response['seller_overview'] = $seller;
                    $response['seller_services'] = $service_details;
                }


                return $response;
            } elseif (is_array($get_services) && empty($get_services)) {
                $response['service_overview'] = $service;
                $response['seller_overview'] = $seller;
                $response['seller_services'] = [];

                return $response;
            }
        } else {
            $response['service_overview'] = [];
            $response['seller_overview'] = [];
            $response['seller_services'] = '';

            return array();
        }
    }

    public function get_service_info($inputs) {
        $this->db->select("s.*,c.category_name,sc.subcategory_name");
        $this->db->from('services s');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->where("s.status = 1 AND s.id='" . $inputs['id'] . "'");
        $result = $this->db->get()->row_array();

        $provider_currency = get_api_provider_currency($inputs['user_id']);
        $ProviderCurrency = $provider_currency['user_currency_code'];
        
        $res_amt=get_gigs_currency($result['service_amount'], $result['currency_code'], $ProviderCurrency);
        $service['service_id'] = $result['id'];
        $service['service_title'] = $result['service_title'];
        $service['service_amount'] = (string) $res_amt;
        $service['category'] = $result['category'];
        $service['subcategory'] = $result['subcategory'];
        $service['service_offered'] = $result['service_offered'];
        $service['service_location'] = $result['service_location'];
        $service['service_latitude'] = $result['service_latitude'];
        $service['service_longitude'] = $result['service_longitude'];
        $service['category_name'] = $result['category_name'];
        $service['subcategory_name'] = $result['subcategory_name'];
        $service['about'] = $result['about'];
        $service['ratings'] = '0';
        $service['views'] = $result['total_views'];
        $service['currency'] = currency_code_sign($ProviderCurrency);

        $image_details = $this->db->where(array('service_id' => $result['id'], 'status' => 1))->get('services_image')->result_array();



        foreach ($image_details as $r) {

            $data['id'] = $r['id'];
            $data['service_image'] = $r['service_image'];
            $data['service_details_image'] = $r['service_details_image'];
            $data['thumb_image'] = $r['thumb_image'];
            $data['mobile_image'] = $r['mobile_image'];
            $data['is_url'] = $r['is_url'];
            $service_image[] = $data;

            $response['service_overview'] = (object) $service;
            $response['service_image'] = $service_image;
        }
        return $response;
    }

    public function all_services($inputs,$user_id='') {



        $count = $this->db->where("status", 1)->count_all_results('services');


        $latitude = $inputs['latitude'];

        $longitude = $inputs['longitude'];

        $radius = 10;


        $longitude_min = $longitude - 100 / abs(cos(deg2rad($longitude)) * 69);

        $longitude_max = $longitude + 100 / abs(cos(deg2rad($longitude)) * 69);

        $latitude_min = $latitude - (100 / 69);

        $latitude_max = $latitude + (100 / 69);


        $this->db->select("s.currency_code,u.id as pro,s.id,s.service_title,s.service_amount,s.service_location,s.service_image,s.service_latitude,s.service_longitude,c.category_name,u.profile_img,r.rating,1.609344 * 3956 * 2 * ASIN(SQRT( POWER(SIN((" . $latitude . " - s.service_latitude) *  pi()/180 / 2), 2) +COS(" . $latitude . " * pi()/180) * COS(s.service_latitude * pi()/180) * POWER(SIN((" . $longitude . " - s.service_longitude) * pi()/180 / 2), 2) )) AS distance");
        $this->db->from('services s');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('rating_review r', 'r.service_id = s.id', 'LEFT');
        $this->db->join('providers u', 's.user_id = u.id', 'LEFT');
        $this->db->where("s.status = 1");
        $this->db->having('distance <=', $radius);

        if ($inputs['type'] == 'Popular') {
            $this->db->order_by('s.total_views', 'DESC');
        } else if ($inputs['type'] == 'Feature') {
            $this->db->order_by('r.rating', 'DESC');
        } else {
            $this->db->order_by('s.id', 'DESC');
        }



        $query = $this->db->get();

        if ($query) {
            $result = $query->result_array();
        }


        if (count($result) > 0) {

            if($user_id){
            $provider_currency = get_api_user_currency($user_id);
            $ProviderCurrency = $provider_currency['user_currency_code'];
            }else{
                $ProviderCurrency='';
            }
            
           

            $details = array();
            $new_details = array();
            $data = array();
            foreach ($result as $r) {



                $rating_count = $this->db->where(array('service_id' => $r['id'], 'status' => 1))->count_all_results('rating_review');
                $r_amt=(!empty($ProviderCurrency) && $r['currency_code'] != '') ? get_gigs_currency($r['service_amount'], $r['currency_code'], $ProviderCurrency) : $r['service_amount'];
                $serviceimage = explode(',', $r['service_image']);
                $data['service_id'] = $r['id'];
                $data['service_title'] = $r['service_title'];
                $data['service_amount'] = (string) $r_amt;
                $data['service_latitude'] = $r['service_latitude'];
                $data['service_longitude'] = $r['service_longitude'];
                $data['service_image'] = $serviceimage[0];
                $data['category_name'] = $r['category_name'];
                if (!empty($r['rating'])) {
                    $data['ratings'] = $r['rating'];
                } else {
                    $data['ratings'] = '';
                }
                $data['rating_count'] = "$rating_count";
                $data['user_image'] = $r['profile_img'];
                $data['currency'] = (!empty($ProviderCurrency)) ? currency_code_sign($ProviderCurrency) : currency_code_sign(settings('currency'));
                $details[] = $data;
            }


            if (!empty($details)) {
                $new_details['service_list'] = $details;
            } else {

                $new_details['service_list'] = array();
            }


            return $new_details;
        } else {
            $new_details = array();
            $new_details['service_list'] = array();
            return $new_details;
        }
    }

    public function getToken($length, $user_id) {
        $token = $user_id;
        $codeAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        $codeAlphabet .= "abcdefghijklmnopqrstuvwxyz";
        $codeAlphabet .= "0123456789";
        $max = strlen($codeAlphabet); // edited
        for ($i = 0; $i < $length; $i++) {
            $token .= $codeAlphabet[$this->crypto_rand_secure(0, $max - 1)];
        }
        return $token;
    }

    function crypto_rand_secure($min, $max) {

        $range = $max - $min;
        if ($range < 0)
            return $min; // not so random...
        $log = log($range, 2);
        $bytes = (int) ($log / 8) + 1; // length in bytes
        $bits = (int) $log + 1; // length in bits
        $filter = (int) (1 << $bits) - 1; // set all lower bits to 1
        do {
            $rnd = hexdec(bin2hex(openssl_random_pseudo_bytes($bytes)));
            $rnd = $rnd & $filter; // discard irrelevant bits
        } while ($rnd >= $range);
        return $min + $rnd;
    }

    public function subcategory_services($inputs) {
        $offset = ($inputs['page'] > 1) ? (($inputs['page'] - 1) * 10) : 0;

        $count = $this->db->where("status", 1)->count_all_results('services');

        $this->db->select
                ("s.id,s.service_title,s.service_amount,s.service_location,s.service_image,c.subcategory_name");
        $this->db->from('services s');
        $this->db->join('subcategories c', 'c.id = s.subcategory', 'LEFT');
        $this->db->where("s.status = 1");
        $this->db->where("c.id ", $inputs['category']);
        $this->db->order_by('s.id', 'DESC');


        $this->db->limit(10, $offset);
        $result = $this->db->get()->result_array();

        if (count($result) > 0) {

            $details = array();
            $new_details = array();
            $data = array();
            foreach ($result as $r) {
                $serviceimage = explode(',', $r['service_image']);
                $data['service_id'] = $r['id'];
                $data['service_title'] = $r['service_title'];
                $data['service_amount'] = $r['service_amount'];
                $data['service_image'] = $serviceimage[0];
                $data['subcategory_name'] = $r['subcategory_name'];
                $data['ratings'] = '0';
                $data['rating_count'] = '0';
                $data['user_image'] = $serviceimage[0];
                $data['currency'] = currency_code_sign(settings('currency'));
                $details[] = $data;
            }

            $total_pages = 0;
            $next_page = -1;
            $page = $inputs['page'];

            if ($count > 0 && $page > 0) {
                $total_pages = ceil($count / 10);
                $page = $inputs['page'];
                if ($page < $total_pages) {
                    $next_page = ($page + 1);
                } else {
                    $next_page = -1;
                }
            }

            $new_details['next_page'] = $next_page;
            $new_details['current_page'] = $page;
            $new_details['total_pages'] = $total_pages;
            $new_details['service_list'] = $details;

            return $new_details;
        } else {

            return array();
        }
    }

    public function subscription() {
        return $this->db->where('status', 1)->get('subscription_fee')->result_array();
    }

    public function subscription_success($inputs) {



        $new_details = array();



        $user_id = $this->get_user_id_using_token($inputs['token']);



        $subscription_id = $inputs['subscription_id'];

        $transaction_id = $inputs['transaction_id'];

        $payment_details = !empty($inputs['payment_details']) ? $inputs['payment_details'] : '';





        $this->db->select('duration');

        $record = $this->db->get_where('subscription_fee', array('id' => $subscription_id))->row_array();



        if (!empty($record)) {





            $duration = $record['duration'];

            $days = 30;

            switch ($duration) {

                case 1:

                    $days = 30;

                    break;

                case 2:

                    $days = 60;

                    break;

                case 3:

                    $days = 90;

                    break;

                case 6:

                    $days = 180;

                    break;

                case 12:

                    $days = 365;

                    break;

                case 24:

                    $days = 730;

                    break;



                default:

                    $days = 30;

                    break;
            }



            $subscription_date = date('Y-m-d H:i:s');

            $expiry_date_time = date('Y-m-d H:i:s', strtotime(date("Y-m-d  H:i:s", strtotime($subscription_date)) . " +" . $days . "days"));





            $new_details['subscriber_id'] = $user_id;

            $new_details['subscription_id'] = $subscription_id;

            $new_details['subscription_date'] = $subscription_date;

            $new_details['expiry_date_time'] = $expiry_date_time;



            $this->db->where('subscriber_id', $user_id);

            $count = $this->db->count_all_results('subscription_details');

            if ($count == 0) {



                $this->db->insert('subscription_details', $new_details);

                $id = $this->db->insert_id();
            } else {



                $this->db->where('subscriber_id', $user_id);

                $this->db->update('subscription_details', $new_details);

                $details = $this->db->get('subscription_details', array('subscriber_id' => $user_id))->row_array();

                $id = $details['id'];
            }


            $array['sub_id'] = $id;

            $array['subscription_id'] = $subscription_id;

            $array['subscriber_id'] = $user_id;

            $array['subscription_date'] = date('Y-m-d');


            $array['tokenid'] = $transaction_id;

            $array['payment_details'] = $payment_details;

            $this->db->insert('subscription_payment', $array);



            $this->db->where('subscriber_id', $user_id);

            return $this->db->get('subscription_details')->row_array();
        } else {



            return false;
        }
    }

    public function profile($inputs) {

        $user_id = $inputs['user_id'] ? $inputs['user_id'] : $this->get_user_id_using_token($inputs['token']);
        $results = array();


        $this->db->select("p.*,c.category_name,sc.subcategory_name");
        $this->db->from('providers p');
        $this->db->join('categories c', 'c.id = p.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = p.subcategory', 'LEFT');
        $this->db->where(array('p.id' => $user_id));
        $results = $this->db->get()->row_array();
        $results['subscription_details'] = $this->get_subscription_details_using_user_id($user_id);

        $results['stripe_details'] = $this->Stripe_Details($user_id);
        ;
        $data['id'] = $results['id'];
        $data['name'] = $results['name'];
        $data['email'] = $results['email'];
        $data['country_code'] = $results['country_code'];
        $data['currency_code'] = $results['currency_code'];
        $data['mobileno'] = $results['mobileno'];
        $data['category'] = $results['category'];
        $data['subcategory'] = $results['subcategory'];
        $data['category_name'] = $results['category_name'];
        $data['subcategory_name'] = $results['subcategory_name'];
        $data['profile_img'] = $results['profile_img'];
        $data['created_at'] = $results['created_at'];
        $data['updated_at'] = $results['updated_at'];
        $data['status'] = $results['status'];
        $data['subscription_details'] = $results['subscription_details'];
        $data['stripe_details'] = $results['stripe_details'];

        return $data;
    }

    public function Stripe_Details($user_id) {
        $this->db->select('*');
        $this->db->from('stripe_bank_details');
        $this->db->where('user_id', $user_id);
        $query = $this->db->get();
        $result = $query->row_array();
        if (!empty($result)) {
            return $result;
        } else {
            return (object) array();
        }
    }

    public function user_profile($inputs) {

        $user_id = $inputs['user_id'] ? $inputs['user_id'] : $this->get_users_id_using_token($inputs['token']);
        $results = array();


        $this->db->select("*");
        $this->db->from('users');
        $this->db->where(array('id' => $user_id));
        $results = $this->db->get()->row_array();
        $results['subscription_details'] = $this->get_subscription_details_using_user_id($user_id);

        $data['id'] = $results['id'];
        $data['name'] = $results['name'];
        $data['email'] = $results['email'];
        $data['country_code'] = $results['country_code'];
        $data['currency_code'] = $results['currency_code'];
        $data['mobileno'] = $results['mobileno'];
        $data['profile_img'] = $results['profile_img'];
        $data['created_at'] = $results['created_at'];
        $data['updated_at'] = $results['updated_at'];
        $data['status'] = $results['status'];
        $data['type'] = $results['type'];
        $data['subscription_details'] = $results['subscription_details'];

        return $data;
    }

    public function profile_update($inputs) {

        $user_id = $inputs['token'];
        $id = $this->get_user_id_using_token($inputs['token']);
        $results = array();


        $this->db->select("p.*,c.category_name,sc.subcategory_name");
        $this->db->from('providers p');
        $this->db->join('categories c', 'c.id = p.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = p.subcategory', 'LEFT');
        $this->db->where(array('p.token' => $user_id));
        $results = $this->db->get()->row_array();
        $results['subscription_details'] = $this->get_subscription_details_using_user_id($id);

        $data['id'] = $results['id'];
        $data['name'] = $results['name'];
        $data['email'] = $results['email'];
        $data['country_code'] = $results['country_code'];
        $data['mobileno'] = $results['mobileno'];
        $data['category'] = $results['category'];
        $data['subcategory'] = $results['subcategory'];
        $data['category_name'] = $results['category_name'];
        $data['subcategory_name'] = $results['subcategory_name'];
        $data['profile_img'] = $results['profile_img'];
        $data['created_at'] = $results['created_at'];
        $data['updated_at'] = $results['updated_at'];
        $data['status'] = $results['status'];
        $data['subscription_details'] = $results['subscription_details'];

        return $data;
    }

    public function get_subscription_details_using_user_id($id = '') {
        $records = array();
        if (!empty($id)) {
            $this->db->select('SD.expiry_date_time, SF.subscription_name');
            $this->db->from('subscription_details SD');
            $this->db->join('subscription_fee SF', 'SF.id = SD.subscription_id', 'left');
            $this->db->where('subscriber_id', $id);
            $records = $this->db->get()->row_array();
            if (!empty($records)) {
                $records['expiry_date_time'] = utc_date_conversion($records['expiry_date_time']);
            } else {
                $records = (object) array();
            }
        }
        return $records;
    }

    public function create_service($inputs) {
        $this->db->insert('services', $inputs);
        return $this->db->insert_id();
    }

    public function insert_serviceimage($image) {
        $this->db->insert('services_image', $image);
        $this->db->where(array('service_id' => $image['service_id']));
        return $this->db->affected_rows() != 0 ? true : false;
    }

    public function update_service($inputs, $where) {

        $this->db->set($inputs);
        $this->db->where($where);
        $this->db->update('services');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    public function delete_service($inputs, $where) {

        $this->db->set($inputs);
        $this->db->where($where);
        $this->db->update('services_image');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    public function get_provider_details($mobile_number, $user_data) {


        $record = $this->db->where('mobileno', $mobile_number)->get('providers')->row_array();

        $records = array();

        if (!empty($record)) {

            $user_id = $record['id'];

            $count = 0;

            if (!empty($user_data['device_id'])) {

                $device_id = $user_data['device_id'];

                $this->db->where('user_id', $user_id);

                $this->db->where('device_id', $device_id);

                $count = $this->db->count_all_results('device_details');
            }



            if (!empty($user_data['device_type']) && !empty($user_data['device_id'])) {

                $device_type = strtolower($user_data['device_type']);

                $device_id = $user_data['device_id'];

                $date = date('Y-m-d H:i:s');
                $type = '1';


                if ($count == 0) {

                    $this->db->insert('device_details', array('user_id' => $user_id, 'device_type' => $device_type, 'device_id' => $device_id, 'created' => $date, 'type' => $type));
                } else {

                    $this->db->where('user_id', $user_id);

                    $this->db->update('device_details', array('device_type' => $device_type, 'device_id' => $device_id, 'created' => $date, 'type' => $type));
                }
            }


            $this->db->select('subscription_id');

            $this->db->where('subscriber_id', $user_id);

            $subscription = $this->db->get('subscription_details')->row_array();


            if (!empty($subscription)) {

                $id = $subscription['subscription_id'];

                $this->db->select('id,subscription_name');

                $this->db->where('id', $id);

                $subscription = $this->db->get('subscription_fee')->row_array();

                $subscribed_user = 1;

                $subscribed_msg = $subscription['subscription_name'];
            } else {

                $subscribed_user = 0;

                $subscribed_msg = 'Free';
            }




            $records = array('id' => $record['id'], 'token' => $record['token'], 'name' => $record['name'], 'mobileno' => $record['mobileno'], 'country_code' => $record['country_code'], 'currency_code' => $record['currency_code'], 'share_code' => $record['share_code'], 'otp' => $record['otp'], 'email' => $record['email'], 'profile_img' => $record['profile_img'], 'status' => $record['status'], 'created_at' => $record['created_at'], 'updated_at' => $record['updated_at'], 'type' => $record['type']);

            $records['id'] = $record['id'];
            $records['name'] = $record['name'];
            $records['mobileno'] = $record['mobileno'];
            $records['country_code'] = $record['country_code'];
            $records['otp'] = $record['otp'];
            $records['profile_img'] = $record['profile_img'];
            $records['status'] = $record['status'];
            $records['created_at'] = $record['created_at'];
            $records['updated_at'] = $record['updated_at'];
            $records['type'] = $record['type'];
            $records['is_subscribed'] = "$subscribed_user";
            $records['subscription_details'] = $this->get_subscription_details_using_user_id($user_id);
            $records['share_code'] = $record['share_code'];
        }

        return $records;
    }

    public function get_service_id($inputs) {
        return $this->db->where('id', $inputs)->get('services')->row_array();
    }

    public function check_otp($check_data) {
        $this->db->select('id,mobile_number,otp,endtime');
        $this->db->from('mobile_otp');
        $this->db->where($check_data);
        $this->db->where('status', 1);
        $query = $this->db->get();
        $time_count = $query->num_rows();
        if ($time_count == 1) {

            $this->db->where($check_data);
            $this->db->update('mobile_otp', array('status' => 0));

            $this->db->select('*');
            $this->db->where($check_data);
            return $this->db->get('mobile_otp')->row_array();
        }
    }

    public function get_serviceimg($service_id) {

        $this->db->select('*');
        $this->db->where('service_id', $service_id);
        $this->db->where('status', 1);
        $query = $this->db->get('services_image');
        return $time_count = $query->num_rows();
    }

    public function insert_businesshours($user_data) {
        $this->db->insert('business_hours', $user_data);
        return $this->db->affected_rows() != 0 ? true : false;
    }

    public function update_availability($user_data, $where) {

        $this->db->set($user_data);
        $this->db->where($where);
        $this->db->update('business_hours');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    public function get_availability($provider_id) {
        return $this->db->where('provider_id', $provider_id)->get('business_hours')->row_array();
    }

    public function save_otp($user_data) {
        $result = $this->db->insert('mobile_otp', $user_data);
        $insert_id = $this->db->insert_id();
        return $insert_id;
    }

    public function get_user_details($mobile_number, $user_data) {
        $record = $this->db->where('mobileno', $mobile_number)->get('users')->row_array();

        $records = array();

        if (!empty($record)) {

            $user_id = $record['id'];

            $count = 0;

            if (!empty($user_data['device_id'])) {

                $device_id = $user_data['device_id'];

                $this->db->where('user_id', $user_id);

                $this->db->where('device_id', $device_id);

                $count = $this->db->count_all_results('device_details');
            }



            if (!empty($user_data['device_type']) && !empty($user_data['device_id'])) {

                $device_type = strtolower($user_data['device_type']);

                $device_id = $user_data['device_id'];

                $date = date('Y-m-d H:i:s');
                $type = '2';


                if ($count == 0) {

                    $this->db->insert('device_details', array('user_id' => $user_id, 'device_type' => $device_type, 'device_id' => $device_id, 'created' => $date, 'type' => $type));
                } else {

                    $this->db->where('user_id', $user_id);

                    $this->db->update('device_details', array('device_type' => $device_type, 'device_id' => $device_id, 'created' => $date, 'type' => $type));
                }
            }




            $subscribed_user = 1;

            $records = array('id' => $record['id'], 'token' => $record['token'], 'name' => $record['name'], 'mobileno' => $record['mobileno'], 'country_code' => $record['country_code'], 'otp' => $record['otp'], 'email' => $record['email'], 'profile_img' => $record['profile_img'], 'status' => $record['status'], 'created_at' => $record['created_at'], 'updated_at' => $record['updated_at'], 'type' => $record['type']);

            $records['id'] = $record['id'];
            $records['name'] = $record['name'];
            $records['mobileno'] = $record['mobileno'];
            $records['profile_img'] = $record['profile_img'];
            $records['country_code'] = $record['country_code'];
            $records['otp'] = $record['otp'];
            $records['status'] = $record['status'];
            $records['created_at'] = $record['created_at'];
            $records['updated_at'] = $record['updated_at'];
            $records['type'] = $record['type'];
            $records['subscription_details'] = $this->get_subscription_details_using_user_id($user_id);
            $records['share_code'] = $record['share_code'];
        }


        return $records;
    }

    public function logout_provider($token = '', $device_type, $device_id) {

        $result = 0;


        $device_id = $device_id;

        $device_type = strtolower($device_type);

        $user_id = $this->get_user_id_using_token($token);

        $this->db->where(array('device_id' => $device_id, 'device_type' => $device_type, 'user_id' => $user_id));

        $this->db->delete('device_details');



        if (!empty($token)) {

            $last_login = date('Y-m-d H:i:s');

            $this->db->where('token', $token);

            $this->db->update('providers', array('last_login' => $last_login));

            $result = $this->db->affected_rows();
        }



        return $result;
    }

    public function provider_hours($user_id) {
        return $this->db->where('provider_id', $user_id)->get('business_hours')->row_array();
    }

    public function token_is_valid($token) {


        $where = array();

        $where['token'] = $token;

        $this->db->where($where);

        $count = $this->db->count_all_results('users');

        return $count;
    }

    public function get_bookings($service_date, $service_id) {
        return $this->db->where(array('service_date' => $service_date, 'service_id' => $service_id))->get('book_service')->result_array();
    }

    public function insert_booking($user_post_data) {

        $this->db->insert('book_service', $user_post_data);
        return $this->db->insert_id();
    }

    public function search_services($text) {
        $this->db->select("s.*,c.category_name,sc.subcategory_name,u.profile_img");
        $this->db->from('services s');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('users u', 'u.id = s.user_id', 'LEFT');
        $this->db->like('s.service_title', $text);
        $this->db->or_like('c.category_name', $text);
        $this->db->or_like('sc.subcategory_name', $text);

        $result = $this->db->get()->result_array();
        return $result;
    }

    public function get_bookinglist($provider_id, $status) {

        if (!empty($status)) {

            if ((int) $status == 1) {
                $query = $this->db->query("SELECT  `b` . * ,  `s`.`service_title` ,  `s`.`service_image` ,  `s`.`service_amount` ,  `s`.`rating` ,  `s`.`service_image` ,  `c`.`category_name` ,  `sc`.`subcategory_name` ,  `p`.`token` ,`p`.`profile_img` ,  `p`.`mobileno` ,  `p`.`country_code` FROM  `book_service`  `b` LEFT JOIN  `services`  `s` ON  `b`.`service_id` =  `s`.`id` LEFT JOIN  `categories`  `c` ON  `c`.`id` =  `s`.`category` LEFT JOIN  `subcategories`  `sc` ON  `sc`.`id` =  `s`.`subcategory` LEFT JOIN  `users`  `p` ON  `b`.`user_id` =  `p`.`id` WHERE  `b`.`provider_id` =  $provider_id ORDER BY `b`.`id` DESC ");
                $result = $query->result_array();
            }

            if ((int) $status == 2) {
                $query = $this->db->query("SELECT  `b` . * ,  `s`.`service_title` ,  `s`.`service_image` ,  `s`.`service_amount` ,  `s`.`rating` ,  `s`.`service_image` ,  `c`.`category_name` ,  `sc`.`subcategory_name` ,  `p`.`token`,`p`.`profile_img` ,  `p`.`mobileno` ,  `p`.`country_code` FROM  `book_service`  `b` LEFT JOIN  `services`  `s` ON  `b`.`service_id` =  `s`.`id` LEFT JOIN  `categories`  `c` ON  `c`.`id` =  `s`.`category` LEFT JOIN  `subcategories`  `sc` ON  `sc`.`id` =  `s`.`subcategory` LEFT JOIN  `users`  `p` ON  `b`.`user_id` =  `p`.`id` WHERE  `b`.`provider_id` =  $provider_id AND (`b`.`status` =2 OR  `b`.`status` =3 OR `b`.`status` =5) ORDER BY `b`.`id` DESC ");
                $result = $query->result_array();
            }
            if ((int) $status == 3) {
                $query = $this->db->query("SELECT  `b` . * ,  `s`.`service_title` ,  `s`.`service_image` ,  `s`.`service_amount` ,  `s`.`rating` ,  `s`.`service_image` ,  `c`.`category_name` ,  `sc`.`subcategory_name` , `p`.`token`, `p`.`profile_img` ,  `p`.`mobileno` ,  `p`.`country_code` FROM  `book_service`  `b` LEFT JOIN  `services`  `s` ON  `b`.`service_id` =  `s`.`id` LEFT JOIN  `categories`  `c` ON  `c`.`id` =  `s`.`category` LEFT JOIN  `subcategories`  `sc` ON  `sc`.`id` =  `s`.`subcategory` LEFT JOIN  `users`  `p` ON  `b`.`user_id` =  `p`.`id` WHERE  `b`.`provider_id` =  $provider_id AND (`b`.`status` =6) ORDER BY `b`.`id` DESC");
                $result = $query->result_array();
            }
            if ((int) $status == 4) {
                $query = $this->db->query("SELECT  `b` . * ,  `s`.`service_title` ,  `s`.`service_image` ,  `s`.`service_amount` ,  `s`.`rating` ,  `s`.`service_image` ,  `c`.`category_name` ,  `sc`.`subcategory_name` ,`p`.`token`,  `p`.`profile_img` ,  `p`.`mobileno` ,  `p`.`country_code` FROM  `book_service`  `b` LEFT JOIN  `services`  `s` ON  `b`.`service_id` =  `s`.`id` LEFT JOIN  `categories`  `c` ON  `c`.`id` =  `s`.`category` LEFT JOIN  `subcategories`  `sc` ON  `sc`.`id` =  `s`.`subcategory` LEFT JOIN  `users`  `p` ON  `b`.`user_id` =  `p`.`id` WHERE  `b`.`provider_id` =  $provider_id AND (`b`.`status` =7) ORDER BY `b`.`id` DESC");
                $result = $query->result_array();
            }
        }

        return $result;
    }

    public function get_requestlist($provider_id) {
        $this->db->select("b.*,s.service_title,s.service_image,s.service_amount,s.rating,s.service_image,
        c.category_name,sc.subcategory_name,p.profile_img");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('providers p', 'b.provider_id = p.id', 'LEFT');
        $this->db->where("b.status", 1);
        $this->db->where("b.provider_id", $provider_id);
        $this->db->order_by("b.id", 'DESC');
        $result = $this->db->get()->result_array();
        return $result;
    }

    public function get_bookinglist_user($user_id, $status) {

        if (!empty($status)) {

            if ((int) $status == 1) {
                $query = $this->db->query("SELECT  `b` . * ,  `s`.`service_title` ,  `s`.`service_image` ,  `s`.`service_amount` ,  `s`.`rating` ,  `s`.`service_image` ,  `c`.`category_name` ,  `sc`.`subcategory_name` , `p`.`token` , `p`.`profile_img` ,  `p`.`mobileno` ,  `p`.`country_code` ,`pro`.`profile_img` as `provider_profile`  FROM  `book_service`  `b`  LEFT JOIN  `providers`  `pro` ON  `b`.`provider_id` =  `pro`.`id`  LEFT JOIN  `services`  `s` ON  `b`.`service_id` =  `s`.`id` LEFT JOIN  `categories`  `c` ON  `c`.`id` =  `s`.`category` LEFT JOIN  `subcategories`  `sc` ON  `sc`.`id` =  `s`.`subcategory` LEFT JOIN  `users`  `p` ON  `b`.`user_id` =  `p`.`id` WHERE  `b`.`user_id` =  $user_id ORDER BY `b`.`id` DESC");
                $result = $query->result_array();
            }

            if ((int) $status == 2) {
                $query = $this->db->query("SELECT  `b` . * ,  `s`.`service_title` ,  `s`.`service_image` ,  `s`.`service_amount` ,  `s`.`rating` ,  `s`.`service_image` ,  `c`.`category_name` ,  `sc`.`subcategory_name` ,  `p`.`token` , `p`.`profile_img` ,  `p`.`mobileno` ,  `p`.`country_code` ,`pro`.`profile_img` as `provider_profile` FROM  `book_service`  `b` LEFT JOIN  `providers`  `pro` ON  `b`.`provider_id` =  `pro`.`id` LEFT JOIN  `services`  `s` ON  `b`.`service_id` =  `s`.`id` LEFT JOIN  `categories`  `c` ON  `c`.`id` =  `s`.`category` LEFT JOIN  `subcategories`  `sc` ON  `sc`.`id` =  `s`.`subcategory` LEFT JOIN  `users`  `p` ON  `b`.`user_id` =  `p`.`id` WHERE  `b`.`user_id` =  $user_id AND (`b`.`status` =2 OR  `b`.`status` =3 OR `b`.`status` =5) ORDER BY `b`.`id` DESC");
                $result = $query->result_array();
            }
            if ((int) $status == 3) {
                $query = $this->db->query("SELECT  `b` . * ,  `s`.`service_title` ,  `s`.`service_image` ,  `s`.`service_amount` ,  `s`.`rating` ,  `s`.`service_image` ,  `c`.`category_name` ,  `sc`.`subcategory_name` ,   `p`.`token` ,`p`.`profile_img` ,  `p`.`mobileno` ,  `p`.`country_code` ,`pro`.`profile_img` as `provider_profile` FROM  `book_service`  `b` LEFT JOIN  `providers`  `pro` ON  `b`.`provider_id` =  `pro`.`id` LEFT JOIN  `services`  `s` ON  `b`.`service_id` =  `s`.`id` LEFT JOIN  `categories`  `c` ON  `c`.`id` =  `s`.`category` LEFT JOIN  `subcategories`  `sc` ON  `sc`.`id` =  `s`.`subcategory` LEFT JOIN  `users`  `p` ON  `b`.`user_id` =  `p`.`id` WHERE  `b`.`user_id` =  $user_id AND (`b`.`status` =6) ORDER BY `b`.`id` DESC");
                $result = $query->result_array();
            }
            if ((int) $status == 4) {
                $query = $this->db->query("SELECT  `b` . * ,  `s`.`service_title` ,  `s`.`service_image` ,  `s`.`service_amount` ,  `s`.`rating` ,  `s`.`service_image` ,  `c`.`category_name` ,  `sc`.`subcategory_name` ,  `p`.`token` , `p`.`profile_img` ,  `p`.`mobileno` ,  `p`.`country_code` ,`pro`.`profile_img` as `provider_profile` FROM  `book_service`  `b` LEFT JOIN  `providers`  `pro` ON  `b`.`provider_id` =  `pro`.`id` LEFT JOIN  `services`  `s` ON  `b`.`service_id` =  `s`.`id` LEFT JOIN  `categories`  `c` ON  `c`.`id` =  `s`.`category` LEFT JOIN  `subcategories`  `sc` ON  `sc`.`id` =  `s`.`subcategory` LEFT JOIN  `users`  `p` ON  `b`.`user_id` =  `p`.`id` WHERE  `b`.`user_id` =  $user_id AND (`b`.`status` =7) ORDER BY `b`.`id` DESC");
                $result = $query->result_array();
            }
        }

        return $result;
    }

    public function bookingdetail_user($user_id, $booking_id) {


        $this->db->select("b.*,s.id,s.service_title,s.service_amount,s.about,s.service_offered,s.service_location,s.service_latitude,s.service_longitude,s.total_views,p.name,p.mobileno,p.country_code,p.email,p.profile_img,p.token,c.category_name,sc.subcategory_name");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('providers p', 'b.provider_id = p.id', 'LEFT');
        $this->db->where("b.user_id", $user_id);
        $this->db->where("b.id", $booking_id);
        $result = $this->db->get()->result_array();
        return $result;
    }

    public function get_bookingdetails($provider_id, $booking_id) {

        $this->db->select("b.*,s.id,s.service_title,s.service_amount,s.about,s.service_offered,s.service_location,s.service_latitude,s.service_longitude,s.total_views,u.name,u.mobileno,u.country_code,u.email,u.profile_img,u.token,c.category_name,sc.subcategory_name");
        $this->db->from('book_service b');
        $this->db->join('services s', 'b.service_id = s.id', 'LEFT');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('users u', 'b.user_id = u.id', 'LEFT');
        $this->db->where("b.provider_id", $provider_id);
        $this->db->where("b.id", $booking_id);
        $result = $this->db->get()->result_array();
        return $result;
    }

    public function get_service_bookingdetails($provider_id, $service_id) {
        $result = $this->db->where('service_id', $service_id)
                        ->where_not_in('status', [7, 5, 6])->from('book_service')->count_all_results();
        return $result;
    }

    public function update_bookingstatus($book_details, $where) {

        $this->db->set($book_details);
        $this->db->where($where);
        $this->db->update('book_service');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    public function service_statususer($user_data, $where) {

        $this->db->set($user_data);
        $this->db->where($where);
        $this->db->update('book_service');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    public function booking_status($user_data) {
        return $this->db->where('id', $user_data)->get('book_service')->row_array();
    }

    public function token_is_valid_provider($token) {


        $where = array();

        $where['token'] = $token;

        $this->db->where($where);

        $count = $this->db->count_all_results('providers');

        return $count;
    }

    public function rate_review_for_service($inputs) {


        $get_provider = $this->db->where('id', $inputs['service_id'])->get('services')->row_array();

        $new_details = array();

        $user_id = $this->users_id;

        $new_details['user_id'] = $user_id;

        $new_details['service_id'] = $inputs['service_id'];

        $new_details['booking_id'] = $inputs['booking_id'];

        $new_details['provider_id'] = $get_provider['user_id'];

        $new_details['rating'] = $inputs['rating'];

        $new_details['review'] = $inputs['review'];

        $new_details['type'] = $inputs['type'];

        $new_details['created'] = date('Y-m-d H:i:s');

        $this->db->where('status', 1);

        $this->db->where('booking_id', $inputs['booking_id']);

        $this->db->where('user_id', $user_id);

        $count = $this->db->count_all_results('rating_review');

        if ($count == 0) {

            return $this->db->insert('rating_review', $new_details);
        } else {

            return $result = 2;
        }
    }

    public function check_booking_status($user_data) {
        return $this->db->where(array('id' => $user_data, 'status' => 6))->get('book_service')->row_array();
    }

    public function rate_review_list($inputs) {


        $this->db->select("r.*,u.*");
        $this->db->from('rating_review r');
        $this->db->join('users u', 'r.user_id = u.id', 'LEFT');
        $this->db->where("r.service_id", $inputs);
        $result = $this->db->get()->result_array();
        return $result;
    }

    public function delete_account_provider($user_data, $where) {

        $this->db->set('status', 2);
        $this->db->where($where);
        $this->db->update('providers');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    public function delete_account_user($user_data, $where) {

        $this->db->set('status', 2);
        $this->db->where($where);
        $this->db->update('users');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    //get_services_from_sub_service_id
    public function get_services_from_sub_service_id($inputs) {


        $latitude = $inputs['latitude'];

        $longitude = $inputs['longitude'];

        $radius = 10;


        $longitude_min = $longitude - 100 / abs(cos(deg2rad($longitude)) * 69);

        $longitude_max = $longitude + 100 / abs(cos(deg2rad($longitude)) * 69);

        $latitude_min = $latitude - (100 / 69);

        $latitude_max = $latitude + (100 / 69);



        $this->db->select("s.*,c.category_name,sc.subcategory_name,u.profile_img,1.609344 * 3956 * 2 * ASIN(SQRT( POWER(SIN((" . $latitude . " - s.service_latitude) *  pi()/180 / 2), 2) +COS(" . $latitude . " * pi()/180) * COS(s.service_latitude * pi()/180) * POWER(SIN((" . $longitude . " - s.service_longitude) * pi()/180 / 2), 2) )) AS distance");

        $this->db->from('services s');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('users u', 'u.id = s.user_id', 'LEFT');
        $this->db->where('s.subcategory', $inputs['subcategory_id']);
        $this->db->where('s.status', 1);

        $this->db->having('distance <=', $radius);

        $result = $this->db->get()->result_array();



        return $result;
    }

    //common get service image
    public function get_common_service_image($id, $type) {

        $this->db->select("service_image");
        $this->db->from('services_image');
        $this->db->where("service_id", $id);
        $this->db->where("status", 1);
        if ($type == '1' || $type == 1) {
            $val = $this->db->get()->row_array();
        } else {
            $val = $this->db->get()->result();
        }
        return $val;
    }

    //get get_provider_dashboard_count

    public function get_provider_dashboard_count($provider_id) {

        $count = [];

        $service_count = $this->db->query(" SELECT * FROM `services` WHERE `user_id` =                            $provider_id AND (`status` =1)");
        $serv_count = $service_count->result_array();

        $pending_booking_count = $this->db->query(" SELECT * FROM `book_service` WHERE `provider_id` =                            $provider_id AND (`status` =1)");
        $pending_service_count = $pending_booking_count->result_array();

        $inprogress_booking_count = $this->db->query(" SELECT * FROM `book_service` WHERE `provider_id` =                            $provider_id AND (`status` =2)");
        $inprogress_service_count = $inprogress_booking_count->result_array();

        $completed_booking_count = $this->db->query(" SELECT * FROM `book_service` WHERE `provider_id` =                           $provider_id AND (`status` =6)");
        $complete_service_count = $completed_booking_count->result_array();

        $p = count($pending_service_count);
        $i = count($inprogress_service_count);
        $c = count($complete_service_count);
        $s = count($serv_count);

        $count['service_count'] = "$s";
        $count['pending_service_count'] = "$p";
        $count['inprogress_service_count'] = "$i";
        $count['complete_service_count'] = "$c";

        return $count;
    }

    public function search_request_list($inputs) {


        $search_title = (!empty($inputs['text'])) ? $inputs['text'] : '';

        $new_details = array();

        $latitude = $inputs['latitude'];

        $longitude = $inputs['longitude'];

        $radius = 10;


        $longitude_min = $longitude - 100 / abs(cos(deg2rad($longitude)) * 69);

        $longitude_max = $longitude + 100 / abs(cos(deg2rad($longitude)) * 69);

        $latitude_min = $latitude - (100 / 69);

        $latitude_max = $latitude + (100 / 69);



        $this->db->select("s.*,c.category_name,sc.subcategory_name,u.profile_img,1.609344 * 3956 * 2 * ASIN(SQRT( POWER(SIN((" . $latitude . " - 

s.service_latitude) *  pi()/180 / 2), 2) +COS(" . $latitude . " * pi()/180) * COS

(s.service_latitude * pi()/180) * POWER(SIN((" . $longitude . " - s.service_longitude) * pi()/180 

/ 2), 2) )) AS distance");

        $this->db->from('services s');
        $this->db->join('categories c', 'c.id = s.category', 'LEFT');
        $this->db->join('subcategories sc', 'sc.id = s.subcategory', 'LEFT');
        $this->db->join('users u', 'u.id = s.user_id', 'LEFT');
        $this->db->like('s.service_title', $inputs['text']);
        $this->db->or_like('c.category_name', $inputs['text']);
        $this->db->or_like('sc.subcategory_name', $inputs['text']);
        $this->db->having('distance <=', $radius);


        $result = $this->db->get()->result_array();
        return $result;
    }

    public function get_rating_type() {
        return $this->db->get('rating_type')->result_array();
    }

    public function accdetails_provider($detail) {
        return $this->db->where('id', $detail['id'])->get('providers')->row_array();
    }

    public function accdetails_user($detail) {
        return $this->db->where('id', $detail['id'])->get('users')->row_array();
    }

    // Chat queries
    public function chat_conversation($array) {
        $this->db->insert('chat_table', $array);

        $id = $this->db->insert_id();

        $this->db->select('C.*');

        $this->db->from('chat_table C');

        $this->db->where(array('chat_id' => $id));

        return $this->db->get()->row_array();
    }

    public function username($id) {
        $this->db->select('*');
        $users = $this->db->get_where('users', array('token' => $id))->row_array();

        $this->db->select('*');
        $providers = $this->db->get_where('providers', array('token' => $id))->row_array();
        if (!empty($users)) {
            $users['type'] = 2;
            return $users;
        } else {
            $users['type'] = 1;
            return $providers;
        }
    }

    /* insert msg */

    public function insert_msg($data) {
        $val = $this->db->insert("chat_table", $data);
        if ($val) {
            return true;
        } else {
            return false;
        }
    }

    /* get chat list */
    /* get information base on token */

    public function get_chat_list_info($token) {

        $user_table = $this->db->select('id,name,profile_img,token')->
                        from('users')->
                        where('token', $token)->
                        get()->row_array();
        $provider_table = $this->db->select('id,name,profile_img,token')->
                        from('providers')->
                        where('token', $token)->
                        get()->row_array();
        if (!empty($user_table)) {
            $user = $this->get_last_msg($user_table['token']);

            $user_table['message'] = $user['message'];
            $user_table['datetime'] = $user['created_at'];
            $user_table['date'] = date('d-m-Y', strtotime($user['created_at']));
            $user_table['time'] = date('H:i a', strtotime($user['created_at']));
            return $user_table;
        } else {
            $provider = $this->get_last_msg($provider_table['token']);
            $provider_table['message'] = $provider['message'];
            $provider_table['datetime'] = $provider['created_at'];
            $provider_table['date'] = date('d-m-Y', strtotime($provider['created_at']));
            $provider_table['time'] = date('H:i a', strtotime($provider['created_at']));

            return $provider_table;
        }
    }

    /* get last msg get token based */

    public function get_last_msg($token) {
        $val = $this->db->select('message,created_at')->
                        from('chat_table')->
                        where('sender_token', $token)->
                        or_where('receiver_token', $token)->
                        order_by('chat_id', 'DESC')->
                        limit(1)->get()->row_array();
        return $val;
    }

    /* get information base on token */

    public function get_token_info($token) {

        $user_table = $this->db->select('id,name,profile_img,token,type,email')->
                        from('users')->
                        where('token', $token)->
                        get()->row();
        $provider_table = $this->db->select('id,name,profile_img,token,type,email')->
                        from('providers')->
                        where('token', $token)->
                        get()->row();
        if (!empty($user_table)) {
            return $user_table;
        } else {
            return $provider_table;
        }
    }

    /* history */

    public function get_conversation_info($self_token, $partner_token) {
        $return = $this->db->select('chat_id,sender_token,receiver_token,message,status,read_status,utc_date_time,created_at')->
                        from('chat_table')->
                        where("(`sender_token` = '" . $self_token . "' AND `receiver_token` = '" . $partner_token . "') OR (`sender_token` = '" . $partner_token . "' AND `receiver_token` = '" . $self_token . "')")->
                        group_by('chat_id')->
                        order_by('chat_id', 'ASC')->
                        get()->result();
        return $return;
    }

    /* get book service ingo */

    public function get_book_info($book_service_id) {


        $ret = $this->db->select('tab_1.provider_id,tab_1.user_id,tab_1.status,tab_2.service_title')->
                        from('book_service as tab_1')->
                        join('services as tab_2', 'tab_2.id=tab_1.service_id', 'LEFT')->
                        where('tab_1.id', $book_service_id)->limit(1)->
                        order_by('tab_1.id', 'DESC')->
                        get()->row_array();
        return $ret;
    }

    public function get_book_info_b($book_service_id) {


        $ret = $this->db->select('tab_1.provider_id,tab_1.user_id,tab_1.status,tab_1.currency_code,tab_1.amount,tab_2.service_title')->
                        from('book_service as tab_1')->
                        join('services as tab_2', 'tab_2.id=tab_1.service_id', 'LEFT')->
                        where('tab_1.id', $book_service_id)->limit(1)->
                        order_by('tab_1.id', 'DESC')->
                        get()->row_array();
        return $ret;
    }

    /* get device info */

    public function get_device_info($user_id, $user_type) {

        $val = $this->db->select('*')->from('device_details')->where('user_id', $user_id)->where('type', $user_type)->get()->row_array();
        return $val;
    }

    public function get_device_info_multiple($user_id, $user_type) {
        $val = $this->db->select('*')->from('device_details')->where('user_id', $user_id)->where('type', $user_type)->get()->result_array();
        return $val;
    }

    /* get user infermation */

    public function get_user_info($user_id, $user_type) {

        if ($user_type == 2) {
            $val = $this->db->select('*')->from('users')->where('id', $user_id)->where('type', $user_type)->get()->row_array();
        } else {
            $val = $this->db->select('*')->from('providers')->where('id', $user_id)->where('type', $user_type)->get()->row_array();
        }

        return $val;
    }

    /* insert Notification infos */

    public function insert_notification($sender, $receiver, $message) {
        $data = array(
            'sender' => $sender,
            'receiver' => $receiver,
            'message' => $message,
            'status' => 1,
            'utc_date_time' => utc_date_conversion(date('Y-m-d H:i:s')),
            'created_at' => date('Y-m-d H:i:s')
        );

        $ret = $this->db->insert('notification_table', $data);
    }

    public function get_notification_info($token) {
        $ret = $this->db->select('*')->
                        from('notification_table')->
                        where('receiver', $token)->
                        where('status', 1)->
                        order_by('notification_id', 'DESC')->
                        get()->result_array();
        $user_info = $this->get_token_info($token);
        $notification = [];
        if (!empty($ret)) {
            foreach ($ret as $key => $value) {
                $notification[$key]['name'] = !empty($user_info->name) ? $user_info->name : '';
                $notification[$key]['message'] = !empty($value['message']) ? $value['message'] : '';
                $notification[$key]['profile_img'] = !empty($user_info->profile_img) ? $user_info->profile_img : '';
                $notification[$key]['utc_date_time'] = !empty($value['utc_date_time']) ? $value['utc_date_time'] : '';
            }
        }
        return $notification;
    }

    /* check device token */

    public function is_check_divesToken($device_token) {
        $ret = $this->db->select('*')->
                        from('device_details')->
                        where('device_id', $device_token)->
                        get()->row();

        if (empty($ret)) {
            return true;
        } else {
            return false;
        }
    }

    /* get user type and insert */

    public function insert_device_info($data) {

        $user_info = $this->get_token_info($data['user_token']);

        $data = array(
            'user_id' => $user_info->id,
            'device_type' => $data['device_type'],
            'device_id' => $data['device_token'],
            'created' => date('Y-m-d H:i:s'),
            'type' => $user_info->type
        );
        $val = $this->db->insert('device_details', $data);
        return $val;
    }

    public function update_device_details($user_data) {


        $record = $this->db->where('mobileno', $user_data['mobileno'])->get('providers')->row_array();

        $records = array();

        if (!empty($record)) {

            $user_id = $record['id'];

            $count = 0;

            if (!empty($user_data['device_id'])) {

                $device_id = $user_data['device_id'];

                $this->db->where('user_id', $user_id);

                $this->db->where('device_id', $device_id);

                $this->db->where('type', 1);

                $count = $this->db->count_all_results('device_details');
            }



            if (!empty($user_data['device_type']) && !empty($user_data['device_id'])) {

                $device_type = strtolower($user_data['device_type']);

                $device_id = $user_data['device_id'];

                $date = date('Y-m-d H:i:s');
                $type = '1';


                if ($count == 0) {

                    $this->db->insert('device_details', array('user_id' => $user_id, 'device_type' => $device_type, 'device_id' => $device_id, 'created' => $date, 'type' => $type));
                } else {

                    $this->db->where(array('user_id' => $user_id, 'type' => 1));

                    $this->db->update('device_details', array('device_type' => $device_type, 'device_id' => $device_id, 'created' => $date, 'type' => $type));
                }
            }
        }
    }

    public function update_device_user($user_data) {


        $record = $this->db->where('mobileno', $user_data['mobileno'])->get('users')->row_array();

        $records = array();

        if (!empty($record)) {

            $user_id = $record['id'];

            $count = 0;

            if (!empty($user_data['device_id'])) {

                $device_id = $user_data['device_id'];

                $this->db->where('user_id', $user_id);

                $this->db->where('device_id', $device_id);

                $this->db->where('type', 2);

                $count = $this->db->count_all_results('device_details');
            }



            if (!empty($user_data['device_type']) && !empty($user_data['device_id'])) {

                $device_type = strtolower($user_data['device_type']);

                $device_id = $user_data['device_id'];

                $date = date('Y-m-d H:i:s');
                $type = '2';


                if ($count == 0) {

                    $this->db->insert('device_details', array('user_id' => $user_id, 'device_type' => $device_type, 'device_id' => $device_id, 'created' => $date, 'type' => $type));
                } else {

                    $this->db->where(array('user_id' => $user_id, 'type' => 2));

                    $this->db->update('device_details', array('device_type' => $device_type, 'device_id' => $device_id, 'created' => $date, 'type' => $type));
                }
            }
        }
    }

    public function logout($token = '', $device_type, $deviceid) {

        $result = 0;


        if (!empty($token)) {

            $device_id = $deviceid;

            $device_type = strtolower($device_type);

            $user_id = $this->get_user_id_using_token($token);



            $this->db->where(array('device_id' => $device_id, 'device_type' => $device_type));

            $this->db->delete('device_details');

            $last_login = date('Y-m-d H:i:s');

            $this->db->where('token', $token);

            $this->db->update('users', array('last_login' => $last_login));
            $this->db->update('providers', array('last_login' => $last_login));
            $result = $this->db->affected_rows();
        }




        return $result;
    }

    /* wallet information */

    public function get_wallet($token) {
        $val = $this->db->select('*')->from('wallet_table')->where('token', $token)->get()->row();
        $wallet = [];
        $setting_currency = '';
        $query = $this->db->query("select * from system_settings WHERE status = 1");
        $result = $query->result_array();
        if (!empty($result)) {
            foreach ($result as $data) {
                if ($data['key'] == 'currency_option') {
                    $setting_currency = $data['value'];
                }
            }
        }
    

        /* sum of totAL wallet */
            if($val->type == '1'){
            $provider_currency = get_api_provider_currency($val->user_provider_id);
            $UserCurrency = $provider_currency['user_currency_code'];
            }else{
            $provider_currency = get_api_user_currency($val->user_provider_id);
            $UserCurrency = $provider_currency['user_currency_code'];
            }
    

        $wallet_tot = $this->db->select('sum(credit_wallet)as total_credit,sum(debit_wallet)as total_debit')->from('wallet_transaction_history')->
                        where('token', $token)->order_by('id', 'DESC')->
                        get()->row_array();
    

        if (!empty($val)) {

            $wallet['id'] = $val->id;
            $wallet['token'] = $val->token;
            $wallet['type'] = $val->type;
            $wallet['wallet_amt'] = strval(abs($val->wallet_amt));
            $wallet['currency'] = currency_code_sign($UserCurrency);
            $wallet['currency_code'] = $val->currency_code;
            $wallet['total_credit'] = (!empty($wallet_tot['total_credit'])) ? strval($wallet_tot['total_credit']) : 0;
            $wallet['total_debit'] = (!empty($wallet_tot['total_debit'])) ? strval($wallet_tot['total_debit']) : 0;
        }
        if (!empty($wallet)) {
            return $wallet;
        } else {
            $wallet['id'] = '';
            $wallet['token'] = '';
            $wallet['type'] = '';
            $wallet['wallet_amt'] = '';
            $wallet['currency'] = '';
            $wallet['currency_code'] = '';
            $wallet['total_credit'] = '';
            $wallet['total_debit'] = '';
            return $wallet;
        }
    }

    /* wallet update */

    public function update_wallet($inputs, $where) {

        $this->db->set($inputs);
        $this->db->where($where);
        $this->db->update('wallet_table');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    public function update_customer_card($inputs, $where) {

        $this->db->set($inputs);
        $this->db->where($where);
        $this->db->update('stripe_customer_card_details');
        return $this->db->affected_rows() != 0 ? true : false;
    }

    /* get wallet history */

    public function get_wallet_history_info($token) {
        $wallet = $this->db->select('id,token,payment_detail,user_provider_id,type,current_wallet,currency_code,credit_wallet,debit_wallet,avail_wallet,total_amt,fee_amt,reason,created_at')->from('wallet_transaction_history')->
                        where('token', $token)->order_by('id', 'DESC')->
                        get()->result_array();
        return $wallet;
    }

    /* get customer based savedcard */

    public function get_customer_based_card_list($token) {

        return $this->db->get_where('stripe_customer_card_details', array('status' => 1, 'user_token' => $token))->result_array();
    }

    /* get provider based savedcard */

    public function get_provider_based_card_list($token) {

        return $this->db->get_where('stripe_provider_card_details', array('status' => 1, 'user_token' => $token))->result_array();
    }

    /* history updated */

    public function booking_wallet_history_flow($booking_id, $token) {
        if (!empty($booking_id)) {
            $booking = $this->get_book_info_b($booking_id);

            if (!empty($booking)) {

                $user_info = $this->api->get_token_info($token);

                $wallet = $this->api->get_wallet($token);

                $curren_wallet = get_gigs_currency($wallet['wallet_amt'], $wallet['currency_code'], $booking['currency_code']);

                /* wallet infos */

                $history_pay['token'] = $token;
				$history_pay['currency_code']=$booking['currency_code'];
                $history_pay['user_provider_id'] = $user_info->id;
                $history_pay['type'] = $user_info->type;
                $history_pay['tokenid'] = $booking_id;
                $history_pay['payment_detail'] = json_encode($booking); //response
                $history_pay['charge_id'] = $booking['provider_id'];
                $history_pay['transaction_id'] = $booking_id;
                $history_pay['exchange_rate'] = 0;
                $history_pay['paid_status'] = 'pass';
                $history_pay['cust_id'] = 'Self';
                $history_pay['card_id'] = 'Self';
                $history_pay['total_amt'] = $booking['amount'] * 100;
                $history_pay['fee_amt'] = 0;
                $history_pay['net_amt'] = $booking['amount'] * 100;
                $history_pay['amount_refund'] = 0;
                $history_pay['current_wallet'] = $curren_wallet;
                $history_pay['credit_wallet'] = 0;
                $history_pay['debit_wallet'] = ($booking['amount']);
                $history_pay['avail_wallet'] = $curren_wallet - ($booking['amount']);
                $history_pay['reason'] = BOOKED;
                $history_pay['created_at'] = date('Y-m-d H:i:s');

                if ($this->db->insert('wallet_transaction_history', $history_pay)) {
                    /* update wallet table */
					$wallet_data['wallet_amt'] = get_gigs_currency($history_pay['avail_wallet'], $history_pay['currency_code'], $wallet['currency_code']);
                    $wallet_data['updated_on'] = date('Y-m-d H:i:s');
                    $WHERE = array('token' => $token);
                    $result = $this->api->update_wallet($wallet_data, $WHERE);
                    /* payment on stripe */
                }
            }
        }
    }

    /* User accept flow and history */

    public function user_accept_history_flow($booking_id) {
        if (!empty($booking_id)) {
            $booking = $this->get_book_info_b($booking_id);

            if (!empty($booking)) {
                $provider = $this->get_user_info($booking['provider_id'], 1);

                $wallet = $this->api->get_wallet($provider['token']);

                $curren_wallet = get_gigs_currency($wallet['wallet_amt'], $wallet['currency_code'], $booking['currency_code']);

                $query = $this->db->query('select * from admin_commission where admin_id=1');
                $amount = $query->row();
                $pertage = $amount->commission;

                $commission = ($booking['amount']) * $pertage / 100;
                $ComAmount = $booking['amount'] - $commission;

                /* wallet infos */

                $history_pay['token'] = $provider['token'];
				$history_pay['currency_code']=$booking['currency_code'];
                $history_pay['user_provider_id'] = $provider['id'];
                $history_pay['type'] = $provider['type'];
                $history_pay['tokenid'] = $booking_id;
                $history_pay['payment_detail'] = json_encode($booking); //response
                $history_pay['charge_id'] = $booking['provider_id'];
                $history_pay['transaction_id'] = $booking_id;
                $history_pay['exchange_rate'] = 0;
                $history_pay['paid_status'] = 'pass';
                $history_pay['cust_id'] = 'Self';
                $history_pay['card_id'] = 'Self';
                $history_pay['total_amt'] = $booking['amount'] * 100;
                $history_pay['fee_amt'] = 0;
                $history_pay['net_amt'] = $booking['amount'] * 100;
                $history_pay['amount_refund'] = 0;
                $history_pay['current_wallet'] = $curren_wallet;
                $history_pay['credit_wallet'] = $ComAmount;
                $history_pay['debit_wallet'] = 0;
                $history_pay['avail_wallet'] = ($ComAmount) + $curren_wallet;
                $history_pay['reason'] = COMPLETE_PROVIDER;
                $history_pay['created_at'] = date('Y-m-d H:i:s');

                $walletHistory = $this->db->insert('wallet_transaction_history', $history_pay);

                if ($walletHistory) {
                    /* update wallet table */
                    $wallet_data['wallet_amt'] = get_gigs_currency($history_pay['avail_wallet'], $history_pay['currency_code'], $wallet['currency_code']);					
                    $wallet_data['updated_on'] = date('Y-m-d H:i:s');
                    $WHERE = array('token' => $provider['token']);
                    $result = $this->api->update_wallet($wallet_data, $WHERE);


                    /* payment on stripe */

                    $commissionInsert = [
                        'date' => date('Y:m:d'),
                        'provider' => $booking['provider_id'],
                        'user' => $booking['user_id'],
						'currency_code' => $booking['currency_code'],
                        'amount' => $booking['amount'],
                        'commission' => $pertage,
                    ];
                    $commInsert = $this->db->insert('revenue', $commissionInsert);


                    return $result;
                }
            }
        }
    }

    /* provider reject */

    public function provider_reject_history_flow($booking_id) {
        if (!empty($booking_id)) {
            $booking = $this->get_book_info_b($booking_id);

            if (!empty($booking)) {
                $user = $this->get_user_info($booking['user_id'], 2);

                $wallet = $this->api->get_wallet($user['token']);

                $curren_wallet = get_gigs_currency($wallet['wallet_amt'], $wallet['currency_code'], $booking['currency_code']);

                /* wallet infos */

                $history_pay['token'] = $user['token'];
				$history_pay['currency_code'] = $booking['currency_code'];
                $history_pay['user_provider_id'] = $user['id'];
                $history_pay['type'] = $user['type'];
                $history_pay['tokenid'] = $booking_id;
                $history_pay['payment_detail'] = json_encode($booking); //response
                $history_pay['charge_id'] = $booking['provider_id'];
                $history_pay['transaction_id'] = $booking_id;
                $history_pay['exchange_rate'] = 0;
                $history_pay['paid_status'] = 'pass';
                $history_pay['cust_id'] = 'Self';
                $history_pay['card_id'] = 'Self';
                $history_pay['total_amt'] = $booking['amount'] * 100;
                $history_pay['fee_amt'] = 0;
                $history_pay['net_amt'] = $booking['amount'] * 100;
                $history_pay['amount_refund'] = 0;
                $history_pay['current_wallet'] = $curren_wallet;
                $history_pay['credit_wallet'] = ($booking['amount']);
                $history_pay['debit_wallet'] = 0;
                $history_pay['avail_wallet'] = ($booking['amount']) + $curren_wallet;
                $history_pay['reason'] = PROVIDER_REJECT;
                $history_pay['created_at'] = date('Y-m-d H:i:s');

                if ($this->db->insert('wallet_transaction_history', $history_pay)) {
                    /* update wallet table */
                    $wallet_data['wallet_amt'] = get_gigs_currency($history_pay['avail_wallet'], $history_pay['currency_code'], $wallet['currency_code']);
					
                    $wallet_data['updated_on'] = date('Y-m-d H:i:s');
                    $WHERE = array('token' => $user['token']);
                    $result = $this->api->update_wallet($wallet_data, $WHERE);
                    /* payment on stripe */
                    return $result;
                }
            }
        }
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

    public function UserShareCode($share_code) {
        if ($share_code) {

            $where = [
                'share_code' => $share_code
            ];
            $GetUsers = $this->db->where($where)->get('users')->row_array();

            if ($GetUsers) {
                $whr = [
                    'token' => $GetUsers['token'],
                    'user_provider_id' => $GetUsers['id']
                ];
                $GetWallet = $this->db->where($whr)->get('wallet_table')->row_array();
                $AddAmt = $GetWallet['wallet_amt'] + 1;

                $amtup = [
                    'wallet_amt' => $AddAmt,
                    'currency_code' => settings('currency')
                ];

                $updateAmount = $this->update_data('wallet_table', $amtup, $whr);
                if ($updateAmount) {

                    return $updateAmount;
                }
            } else {

                return false;
            }
        } else {
            $empty = 'Empty code';
            return $empty;
        }
    }

    public function ProviderShareCode($share_code) {
        if ($share_code) {

            $where = [
                'share_code' => $share_code
            ];
            $GetUsers = $this->db->where($where)->get('providers')->row_array();

            if ($GetUsers) {
                $whr = [
                    'token' => $GetUsers['token'],
                    'user_provider_id' => $GetUsers['id']
                ];
                $GetWallet = $this->db->where($whr)->get('wallet_table')->row_array();
                $AddAmt = $GetWallet['wallet_amt'] + 1;

                $amtup = [
                    'wallet_amt' => $AddAmt,
                    'currency_code' => settings('currency')
                ];

                $updateAmount = $this->update_data('wallet_table', $amtup, $whr);
                if ($updateAmount) {

                    return $updateAmount;
                }
            } else {

                return false;
            }
        } else {
            $empty = 'Empty code';
            return $empty;
        }
    }

    public function CountRows($table, $where = []) {
        $this->db->select('count(*) as count');
        $this->db->from($table);
        $this->db->where($where);
        $query = $this->db->get();
        $result = $query->row();
        if ($result) {
            return $result->count;
        } else {
            return FALSE;
        }
    }

    public function getSingleData($table, $select = [], $where = []) {
        $this->db->select($select);
        $this->db->from($table);
        $this->db->where($where);
        $query = $this->db->get();
        $result = $query->row();
        if ($result) {
            return $result;
        } else {
            return FALSE;
        }
    }
    public function paypal_payout($email,$amount){
		$token_request = $this->get_paypal_access_token();
		$query = $this->db->query("select * from system_settings WHERE status = 1");
        $result = $query->result_array();
        if(!empty($result))
        { 
            foreach($result as $data1){
                if($data1['key'] == 'paypal_gateway'){
                 $environment = $data1['value'];
               }
		   
           }
        }
	   
        if($environment == "sandbox"){
            $payout_url = "https://api.sandbox.paypal.com/v1/payments/payouts";
        }else{
            $payout_url = "https://api.paypal.com/v1/payments/payouts";
	    }
		 
		$headers = $data = [];
		//--- Headers for payout request
		$headers[] = "Content-Type: application/json";
		$headers[] = "Authorization: Bearer $token_request->access_token";

		$time = time();
		//--- Prepare sender batch header
		$sender_batch_header["sender_batch_id"] = $time;
		$sender_batch_header["email_subject"]   = "Payout Received";
		$sender_batch_header["email_message"]   = "You have received a payout, Thank you for using our services";

		//--- First receiver
		$receiver["recipient_type"] = "EMAIL";
		$receiver["note"] = "Thank you for your services";
		$receiver["sender_item_id"] = $time++;
		$receiver["receiver"] = $email;
		$receiver["amount"]["value"] = $amount;
		$receiver["amount"]["currency"] = "USD";
		$items[] = $receiver;
		
		$data["sender_batch_header"] = $sender_batch_header;
		$data["items"] = $items;

		//--- Send payout request
		$payout = $this->paypal_curl_request($payout_url, "POST", $headers, json_encode($data));
		return $payout;
    } 
    
    public function paypal_curl_request($url, $method, $headers = [], $data = [], $curl_options = []){

		$curl = curl_init();

		curl_setopt($curl, CURLOPT_URL, $url);
		curl_setopt($curl, CURLOPT_TIMEOUT, 0);
		curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);

		//--- If any headers set add them to curl request
		if(!empty($headers)){
			curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
		}

		//--- Set the request type , GET, POST, PUT or DELETE
		switch($method){
			case "POST":
			curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
			break;
		case "PUT":
			curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "PUT");
			break;
		case "DELETE":
			curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "DELETE");
			break;
		default:
			curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "GET");
			break;
		}

		//--- If any data is supposed to be send along with request add it to curl request
		if($data){
			curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
		}
		//--- Any extra curl options to add in curl object
		if($curl_options){
			foreach($curl_options as $option_key => $option_value){
				curl_setopt($curl, $option_key, $option_value);
			}
		}

		$response = curl_exec($curl);
		$error = curl_error($curl);
		curl_close($curl);

		//--- If curl request returned any error return the error
		if ($error) {
			//return "CURL Error: $error";
		}
		//--- Return response received from call
		return $response;
    }
    public function get_paypal_access_token() {
        $query = $this->db->query("select * from system_settings WHERE status = 1");
        $result = $query->result_array();
        if(!empty($result))
        { 
            foreach($result as $data1){

    
                if($data1['key'] == 'paypal_appid'){
                  $paypal_appid = $data1['value'];
                }
    
                if($data1['key'] == 'paypal_appkey'){
                  $paypal_appkey = $data1['value'];
                }

    
                if($data1['key'] == 'live_paypal_appid'){
                  $live_paypal_appid = $data1['value'];
                }
    
                if($data1['key'] == 'live_paypal_appkey'){
                  $live_paypal_appkey = $data1['value'];
                }
                if($data1['key'] == 'paypal_gateway'){
                 $environment = $data1['value'];
               }
		   
           }
        }
	   
        if($environment == "sandbox"){
    		$clientId = $paypal_appid;
    		$clientSecret = $paypal_appkey;
    		$token_url = "https://api.sandbox.paypal.com/v1/oauth2/token";
        }else{
    		$clientId = $live_paypal_appid;
    		$clientSecret = $live_paypal_appkey;
    		$token_url = "https://api.paypal.com/v1/oauth2/token";
	    }
		
		//--- Headers for our token request
		$headers[] = "Accept: application/json";
		$headers[] = "Content-Type: application/x-www-form-urlencoded";

		//--- Data field for our token request
		$data = "grant_type=client_credentials";

		//--- Pass client id & client secrent for authorization
		$curl_options[CURLOPT_USERPWD] = $clientId . ":" . $clientSecret;

		$token_request = $this->paypal_curl_request($token_url, "POST", $headers, $data, $curl_options);
		$token_request1 = json_decode($token_request);
		if(isset($token_request1->error)){
			die("Paypal Token Error: ". $token_request1->error_description);
		}
		return $token_request1;
	}
	
	
	
	public function checkuserpwd($user_id,$current_password) {
        $this->db->where('id', $user_id);
        $this->db->where('password', $current_password);
        return $this->db->count_all_results('users');
    }
	
	public function checkpropwd($user_id,$current_password) {
	$this->db->where('id', $user_id);
	$this->db->where('password', $current_password);
	return $this->db->count_all_results('providers');
	}
	
	
	public function get_user_detailsby_email($email,$password,$user_data) {
        $record = $this->db->where('email', $email)->where('password', $password)->get('users')->row_array();

        $records = array();

        if (!empty($record)) {

            $user_id = $record['id'];

            $count = 0;

            if (!empty($user_data['device_id'])) {

                $device_id = $user_data['device_id'];

                $this->db->where('user_id', $user_id);

                $this->db->where('device_id', $device_id);

                $count = $this->db->count_all_results('device_details');
            }



            if (!empty($user_data['device_type']) && !empty($user_data['device_id'])) {

                $device_type = strtolower($user_data['device_type']);

                $device_id = $user_data['device_id'];

                $date = date('Y-m-d H:i:s');
                $type = '2';


                if ($count == 0) {

                    $this->db->insert('device_details', array('user_id' => $user_id, 'device_type' => $device_type, 'device_id' => $device_id, 'created' => $date, 'type' => $type));
                } else {

                    $this->db->where('user_id', $user_id);

                    $this->db->update('device_details', array('device_type' => $device_type, 'device_id' => $device_id, 'created' => $date, 'type' => $type));
                }
            }




            $subscribed_user = 1;

            $records = array('id' => $record['id'], 'token' => $record['token'], 'name' => $record['name'], 'mobileno' => $record['mobileno'], 'country_code' => $record['country_code'], 'otp' => $record['otp'], 'email' => $record['email'], 'profile_img' => $record['profile_img'], 'status' => $record['status'], 'created_at' => $record['created_at'], 'updated_at' => $record['updated_at'], 'type' => $record['type']);

            $records['id'] = $record['id'];
            $records['name'] = $record['name'];
            $records['mobileno'] = $record['mobileno'];
            $records['profile_img'] = $record['profile_img'];
            $records['country_code'] = $record['country_code'];
            $records['otp'] = $record['otp'];
            $records['status'] = $record['status'];
            $records['created_at'] = $record['created_at'];
            $records['updated_at'] = $record['updated_at'];
            $records['type'] = $record['type'];
            $records['subscription_details'] = $this->get_subscription_details_using_user_id($user_id);
            $records['share_code'] = $record['share_code'];
        }


        return $records;
    }
	
	
	public function get_provider_details_by_email($email,$password,$user_data) {


        $record = $this->db->where('email', $email)->where('password', $password)->get('providers')->row_array();

        $records = array();

        if (!empty($record)) {

            $user_id = $record['id'];

            $count = 0;

            if (!empty($user_data['device_id'])) {

                $device_id = $user_data['device_id'];

                $this->db->where('user_id', $user_id);

                $this->db->where('device_id', $device_id);

                $count = $this->db->count_all_results('device_details');
            }



            if (!empty($user_data['device_type']) && !empty($user_data['device_id'])) {

                $device_type = strtolower($user_data['device_type']);

                $device_id = $user_data['device_id'];

                $date = date('Y-m-d H:i:s');
                $type = '1';


                if ($count == 0) {

                    $this->db->insert('device_details', array('user_id' => $user_id, 'device_type' => $device_type, 'device_id' => $device_id, 'created' => $date, 'type' => $type));
                } else {

                    $this->db->where('user_id', $user_id);

                    $this->db->update('device_details', array('device_type' => $device_type, 'device_id' => $device_id, 'created' => $date, 'type' => $type));
                }
            }


            $this->db->select('subscription_id');

            $this->db->where('subscriber_id', $user_id);

            $subscription = $this->db->get('subscription_details')->row_array();


            if (!empty($subscription)) {

                $id = $subscription['subscription_id'];

                $this->db->select('id,subscription_name');

                $this->db->where('id', $id);

                $subscription = $this->db->get('subscription_fee')->row_array();

                $subscribed_user = 1;

                $subscribed_msg = $subscription['subscription_name'];
            } else {

                $subscribed_user = 0;

                $subscribed_msg = 'Free';
            }




            $records = array('id' => $record['id'], 'token' => $record['token'], 'name' => $record['name'], 'mobileno' => $record['mobileno'], 'country_code' => $record['country_code'], 'currency_code' => $record['currency_code'], 'share_code' => $record['share_code'], 'otp' => $record['otp'], 'email' => $record['email'], 'profile_img' => $record['profile_img'], 'status' => $record['status'], 'created_at' => $record['created_at'], 'updated_at' => $record['updated_at'], 'type' => $record['type']);

            $records['id'] = $record['id'];
            $records['name'] = $record['name'];
            $records['mobileno'] = $record['mobileno'];
            $records['country_code'] = $record['country_code'];
            $records['otp'] = $record['otp'];
            $records['profile_img'] = $record['profile_img'];
            $records['status'] = $record['status'];
            $records['created_at'] = $record['created_at'];
            $records['updated_at'] = $record['updated_at'];
            $records['type'] = $record['type'];
            $records['is_subscribed'] = "$subscribed_user";
            $records['subscription_details'] = $this->get_subscription_details_using_user_id($user_id);
            $records['share_code'] = $record['share_code'];
        }

        return $records;
    }
	
    /* END */
}
?>

