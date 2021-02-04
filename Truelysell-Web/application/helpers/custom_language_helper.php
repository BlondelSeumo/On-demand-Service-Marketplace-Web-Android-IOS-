<?php

if (!function_exists('custom_language')) {



    function custom_language($u_lng) {



        $ci = & get_instance();

        $ci->load->database();

        $user_lang = array();

        $default_lang = array();

        $active_language = array();





        $sql = "SELECT lang_value, lang_key, language from language_management Where language = 'en'";

        $query = $ci->db->query($sql);

        $row = $query->result_array();



        foreach ($row as $key => $value) {

            $lang = $value['language'];

            $lang_key = $value['lang_key'];

            $default_lang[$lang][$lang_key] = $value['lang_value'];
        }







        if (!empty($u_lng)) {



            $id = $u_lng;

            $sql = "SELECT lang_value, lang_key, language from language_management Where language = '" . $id . "'";

            $query = $ci->db->query($sql);

            $row = $query->result_array();

            $user_lang = array();

            foreach ($row as $key => $value) {

                $lang = $value['language'];

                $lang_key = $value['lang_key'];

                $user_lang[$lang][$lang_key] = $value['lang_value'];
            }
        }


        $row = array(
            'default_lang' => $default_lang,
            'user_lang' => $user_lang
        );


        return $row;
    }

}



if (!function_exists('active_language')) {



    function active_language() {

        $ci = & get_instance();

        $ci->load->database();

        $sql = "SELECT  language, language_value, tag from language where status = 1";

        $query = $ci->db->query($sql);

        return $query->result_array();
    }

}


if (!function_exists('default_language')) {



    function default_language() {

        $ci = & get_instance();

        $ci->load->database();

        $sql = "SELECT  language, language_value, tag from language where status = 1 AND default_language =1";

        $query = $ci->db->query($sql);

        return $query->row_array();
    }

}



if (!function_exists('get_languages')) {

    function get_languages($lang) {

        $ci = & get_instance();
        $ci->load->database();

        $ci->db->select('page_key,lang_key,lang_value');
        $ci->db->from('app_language_management');
        $ci->db->where('language', 'en');
        $ci->db->where('type', 'App');
        $ci->db->order_by('page_key', 'ASC');
        $ci->db->order_by('lang_key', 'ASC');
        $records = $ci->db->get()->result_array();




        $language = array();
        if (!empty($records)) {
            foreach ($records as $record) {
                $ci->db->select('page_key,lang_key,lang_value');
                $ci->db->from('app_language_management');
                $ci->db->where('language', $lang);
                $ci->db->where('type', 'App');
                $ci->db->where('page_key', $record['page_key']);
                $ci->db->where('lang_key', $record['lang_key']);
                $eng_records = $ci->db->get()->row_array();
                if (!empty($eng_records['lang_value'])) {
                    $language['language'][$record['page_key']][$record['lang_key']] = $eng_records['lang_value'];
                } else {
                    $language['language'][$record['page_key']][$record['lang_key']] = $record['lang_value'];
                }
            }
        }
        return $language;
    }

}
?>