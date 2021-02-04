<?php

if (!function_exists('currency_sign')) {

    function currency_sign($currency_code) {

        switch ($currency_code) {
            case 'USD':
                $sign = '$';
                break;
            case 'EUR':
                $sign = '€';
                break;
            case 'GBP':
                $sign = '£';
                break;
            default:
                $sign = '$';
                break;
        }
        return $sign;
    }

}

if (!function_exists('currency_code')) {

    function currency_code($currency_sign) {

        switch ($currency_sign) {
            case '$':
                $code = 'USD';
                break;
            case '€':
                $code = 'EUR';
                break;
            case '£':
                $code = 'GBP';
                break;
            default:
                $code = 'USD';
                break;
        }
        return $code;
    }

}
if (!function_exists('currency_code_sign')) {

    function currency_code_sign($val) {
        $currency_sign = array(
            "ALL" => 'Lek',
            "AFN" => '؋',
            "ARS" => '$',
            "AWG" => 'ƒ',
            "AUD" => '$',
            "AZN" => '₼',
            "BSD" => '$',
            "BBD" => '$',
            "BYN" => 'Br',
            "BZD" => 'BZ$',
            "BMD" => '$',
            "BOB" => '$b',
            "BAM" => 'KM',
            "BWP" => 'P',
            "BGN" => 'лв',
            "BRL" => 'R$',
            "BND" => '$',
            "KHR" => '៛',
            "CAD" => '$',
            "KYD" => '$',
            "CLP" => '$',
            "CNY" => '¥',
            "COP" => '$',
            "CRC" => '₡',
            "HRK" => 'kn',
            "CUP" => '₱',
            "CZK" => 'Kč',
            "DKK" => 'kr',
            "DOP" => 'RD$',
            "XCD" => '$',
            "EGP" => '£',
            "SVC" => '$',
            "EUR" => '€',
            "FKP" => '£',
            "FJD" => '$',
            "GHS" => '¢',
            "GIP" => '£',
            "GTQ" => 'Q',
            "GGP" => '£',
            "GYD" => '$',
            "HNL" => 'L',
            "HKD" => '$',
            "HUF" => 'Ft',
            "ISK" => 'kr',
            "INR" => '₹',
            "IDR" => 'Rp',
            "IRR" => '﷼',
            "IMP" => '£',
            "ILS" => '₪',
            "JMD" => 'J$',
            "JPY" => '¥',
            "JEP" => '£',
            "KZT" => 'лв',
            "KPW" => '₩',
            "KRW" => '₩',
            "KGS" => 'лв',
            "LAK" => '₭',
            "LBP" => '£',
            "LRD" => '$',
            "MKD" => 'ден',
            "MYR" => 'RM',
            "MUR" => '₨',
            "MXN" => '$',
            "MNT" => '₮',
            "MZN" => 'MT',
            "NAD" => '$',
            "NPR" => '₨',
            "ANG" => 'ƒ',
            "NZD" => '$',
            "NIO" => 'C$',
            "NGN" => '₦',
            "NOK" => 'kr',
            "OMR" => '﷼',
            "PKR" => '₨',
            "PAB" => 'B/.',
            "PYG" => 'Gs',
            "PEN" => 'S/.',
            "PHP" => '₱',
            "PLN" => 'zł',
            "QAR" => '﷼',
            "RON" => 'lei',
            "RUB" => '₽',
            "SHP" => '£',
            "SAR" => '﷼',
            "RSD" => 'Дин.',
            "SCR" => '₨',
            "SGD" => '$',
            "SBD" => '$',
            "SOS" => 'S',
            "ZAR" => 'R',
            "LKR" => '₨',
            "SEK" => 'kr',
            "CHF" => 'CHF',
            "SRD" => '$',
            "SYP" => '£',
            "TWD" => 'NT$',
            "THB" => '฿',
            "TTD" => 'TT$',
            "TRY" => '₺',
            "TVD" => '$',
            "UAH" => '₴',
            "GBP" => '£',
            "USD" => '$',
            "UYU" => '$U',
            "UZS" => 'лв',
            "VEF" => 'Bs',
            "VND" => '₫',
            "YER" => '﷼',
            "ZWD" => 'Z$'
        );

        if (array_key_exists($val, $currency_sign)) {
            return $currency_sign[$val];
        } else {
            return "$";
        }
    }

}

if (!function_exists('currency_conversion')) {

    function currency_conversion($val) {
        $currency_symbols = array(
            'AED' => '&#1583;',
            'AFN' => '&#65;',
            'ALL' => '&#76;',
            'AMD' => '',
            'ANG' => '&#402;',
            'AOA' => '&#75;&#122;', // ?
            'ARS' => '&#36;',
            'AUD' => '&#36;',
            'AWG' => '&#402;',
            'AZN' => '&#1084;',
            'BAM' => '&#75;',
            'BBD' => '&#36;',
            'BDT' => '&#2547;', // ?
            'BGN' => '&#1083;',
            'BHD' => '.&#1583;',
            'BIF' => '&#70;',
            'BMD' => '&#36;',
            'BND' => '&#36;',
            'BOB' => '&#36;',
            'BRL' => '&#82;&#36;',
            'BSD' => '&#36;',
            'BTN' => '&#78;',
            'BWP' => '&#80;',
            'BYR' => '&#112;',
            'BZD' => '&#66;',
            'CAD' => '&#36;',
            'CDF' => '&#70;',
            'CHF' => '&#67;',
            'CLF' => '', // ?
            'CLP' => '&#36;',
            'CNY' => '&#165;',
            'COP' => '&#36;',
            'CRC' => '&#8353;',
            'CUP' => '&#8396;',
            'CVE' => '&#36;', // ?
            'CZK' => '&#75;',
            'DJF' => '&#70;',
            'DKK' => '&#107;',
            'DOP' => '&#82;',
            'DZD' => '&#1583;',
            'EGP' => '&#163;',
            'ETB' => '&#66;',
            'EUR' => '&#8364;',
            'FJD' => '&#36;',
            'FKP' => '&#163;',
            'GBP' => '&#163;',
            'GEL' => '&#4314;', // ?
            'GHS' => '&#162;',
            'GIP' => '&#163;',
            'GMD' => '&#68;', // ?
            'GNF' => '&#70;',
            'GTQ' => '&#81;',
            'GYD' => '&#36;',
            'HKD' => '&#36;',
            'HNL' => '&#76;',
            'HRK' => '&#107;',
            'HTG' => '&#71;', // ?
            'HUF' => '&#70;',
            'IDR' => '&#82;',
            'ILS' => '&#8362;',
            'INR' => '&#8377;',
            'IQD' => '&#1593;',
            'IRR' => '&#65020;',
            'ISK' => '&#107;',
            'JEP' => '&#163;',
            'JMD' => '&#74;',
            'JOD' => '&#74;',
            'JPY' => '&#165;',
            'KES' => '&#75;',
            'KGS' => '&#1083;',
            'KHR' => '&#6107;',
            'KMF' => '&#67;',
            'KPW' => '&#8361;',
            'KRW' => '&#8361;',
            'KWD' => '&#1583;',
            'KYD' => '&#36;',
            'KZT' => '&#1083;&#1074;',
            'LAK' => '&#8365;',
            'LBP' => '&#163;',
            'LKR' => '&#8360;',
            'LRD' => '&#36;',
            'LSL' => '&#76;', // ?
            'LTL' => '&#76;',
            'LVL' => '&#76;',
            'LYD' => '&#1604;',
            'MAD' => '&#1583;',
            'MDL' => '&#76;',
            'MGA' => '&#65;',
            'MKD' => '&#1076;',
            'MMK' => '&#75;',
            'MNT' => '&#8366;',
            'MOP' => '&#77;',
            'MRO' => '&#85;',
            'MUR' => '&#8360;', // ?
            'MVR' => '.&#1923;', // ?
            'MWK' => '&#77;',
            'MXN' => '&#36;',
            'MYR' => '&#82;',
            'MZN' => '&#77;',
            'NAD' => '&#36;',
            'NGN' => '&#8358;',
            'NIO' => '&#67;',
            'NOK' => '&#107;',
            'NPR' => '&#8360;',
            'NZD' => '&#36;',
            'OMR' => '&#65020;',
            'PAB' => '&#66;',
            'PEN' => '&#83;',
            'PGK' => '&#75;', // ?
            'PHP' => '&#8369;',
            'PKR' => '&#8360;',
            'PLN' => '&#122;',
            'PYG' => '&#71;',
            'QAR' => '&#65020;',
            'RON' => '&#108;',
            'RSD' => '&#1044;',
            'RUB' => '&#1088;',
            'RWF' => '&#1585;',
            'SAR' => '&#65020;',
            'SBD' => '&#36;',
            'SCR' => '&#8360;',
            'SDG' => '&#163;', // ?
            'SEK' => '&#107;',
            'SGD' => '&#36;',
            'SHP' => '&#163;',
            'SLL' => '&#76;',
            'SOS' => '&#83;',
            'SRD' => '&#36;',
            'STD' => '&#68;',
            'SVC' => '&#36;',
            'SYP' => '&#163;',
            'SZL' => '&#76;', // ?
            'THB' => '&#3647;',
            'TJS' => '&#84;',
            'TMT' => '&#109;',
            'TND' => '&#1583;',
            'TOP' => '&#84;',
            'TRY' => '&#8356;', // New Turkey Lira (old symbol used)
            'TTD' => '&#36;',
            'TWD' => '&#78;',
            'TZS' => '',
            'UAH' => '&#8372;',
            'UGX' => '&#85;',
            'USD' => '&#36;',
            'UYU' => '&#36;',
            'UZS' => '&#1083;',
            'VEF' => '&#66;',
            'VND' => '&#8363;',
            'VUV' => '&#86;',
            'WST' => '&#87;',
            'XAF' => '&#70;',
            'XCD' => '&#36;',
            'XDR' => '',
            'XOF' => '',
            'XPF' => '&#70;',
            'YER' => '&#65020;',
            'ZAR' => '&#82;',
            'ZMK' => '&#90;',
            'ZWL' => '&#90;',
        );
        if (array_key_exists($val, $currency_symbols)) {
            $symbols = $currency_symbols[$val];
        } else {
            $symbols = '&#36;';
        }


        return $symbols;
    }

}
if (!function_exists('settings')) {

    function settings($val) {
        $ci = & get_instance();
        $settings = $ci->db->query("select * from system_settings WHERE status = 1")->result_array();
        $result = array();


        if (!empty($settings)) {
            foreach ($settings as $datas) {
                if ($datas['key'] == 'currency_option') {
                    $result['currency_option'] = $datas['value'];
                }
            }
        }

        if (!empty($result[$val])) {
            $results = $result[$val];
        }

        return $results;
    }

}
if (!function_exists('get_currency')) {

    function get_currency() {
        $ci = & get_instance();
        $currency = $ci->db->select('id,currency_code')->get('currency_rate')->result_array();
        return $currency;
    }

}
if (!function_exists('get_user_currency')) {

    function get_user_currency() {
        $ci = & get_instance();
        $ci->load->library('session');
        $user_id = $ci->session->userdata('id');
        $currency = $ci->db->where('id', $user_id)->select('currency_code')->get('users')->row_array();
        $currency_rate = $ci->db->where('currency_code', $currency['currency_code'])->get('currency_rate')->row_array();
        $data['user_currency_code'] = $currency['currency_code'];
        $data['user_currency_rate'] = $currency_rate['rate'];
        $data['user_currency_sign'] = currency_code_sign($currency['currency_code']);
        return $data;
    }

}
if (!function_exists('get_provider_currency')) {

    function get_provider_currency() {
        $ci = & get_instance();
        $ci->load->library('session');
        $user_id = $ci->session->userdata('id');
        $currency = $ci->db->where('id', $user_id)->select('currency_code')->get('providers')->row_array();
        $currency_rate = $ci->db->where('currency_code', $currency['currency_code'])->get('currency_rate')->row_array();

        $data['user_currency_code'] = $currency['currency_code'];
        $data['user_currency_rate'] = $currency_rate['rate'];
        $data['user_currency_sign'] = currency_code_sign($currency['currency_code']);
        return $data;
    }

}
if (!function_exists('get_gigs_currency')) {

    function get_gigs_currency($gig_price, $gig_currency, $user_currency) {
        $ci = & get_instance();

        $gigs_currency_rate = $ci->db->where('currency_code', $gig_currency)->select('rate')->get('currency_rate')->row()->rate;

        $user_currency_rate = $ci->db->where('currency_code', $user_currency)->select('rate')->get('currency_rate')->row()->rate;
        $rates = $user_currency_rate / $gigs_currency_rate;
        $rate = $rates * $gig_price;
        return round($rate, 2);
    }

}

if (!function_exists('get_user_currency_code')) {

    function get_user_currency_code($id) {
        $ci = & get_instance();
        $user_id = $id;
       
		$q = $ci->db->query("SELECT currency_code from users  where token = '$user_id' ");
        $currency = $q->row_array();
		
        $data['user_currency_code'] = $currency['currency_code'];
        $data['user_currency_sign'] = currency_code_sign($currency['currency_code']);
        return $data;
    }

}

if (!function_exists('get_provider_currency_code')) {

    function get_provider_currency_code($id) {
        $ci = & get_instance();
        $user_id = $id;

		$q = $ci->db->query("SELECT currency_code from providers  where token = '$user_id' ");
        $currency = $q->row_array();
		
        $data['user_currency_code'] = $currency['currency_code'];
        $data['user_currency_sign'] = currency_code_sign($currency['currency_code']);
        return $data;
    }

}

if (!function_exists('get_api_user_currency')) {

    function get_api_user_currency($id) {
        $ci = & get_instance();

        $user_id = $id;
		$q = $ci->db->query("SELECT currency_code from users  where id = '$user_id' ");
        $currency = $q->row_array();

        $data['user_currency_code'] = $currency['currency_code'];
        $data['user_currency_sign'] = currency_code_sign($currency['currency_code']);
        return $data;
    }

}
if (!function_exists('get_api_provider_currency')) {

    function get_api_provider_currency($id) {
        $ci = & get_instance();

        $provider_id = $id;
       
        $q = $ci->db->query("SELECT currency_code from providers  where id = '$provider_id' ");
        $currency = $q->row_array();

        $data['user_currency_code'] = $currency['currency_code'];
        $data['user_currency_sign'] = currency_code_sign($currency['currency_code']);
        return $data;
    }

}

if (!function_exists('get_flag')) {

    function get_flag($lang) {
        $ci = & get_instance();
        $flag = $ci->db->query("select * from language WHERE status = 1 and deleted_status=0 and language_value='" . $lang . "'")->row_array();
        if (!empty($flag['flag_img'])) {
            return $flag['flag_img'];
        }
    }

}
?>