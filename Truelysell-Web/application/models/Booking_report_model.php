<?php
if ( ! defined('BASEPATH')) exit('No direct script access allowed');
class Booking_report_model extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
     date_default_timezone_set('Asia/Kolkata');
	}

 
 /*booking reports*/

 public function get_total_bookings(){

                            $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->order_by('tab_1.id','DESC');
                                     $returns=$this->db->get()->result_array();
                                return $returns;


 }


/*booking reports*/

 public function get_filter_total_bookings($service_id,$status,$user_id,$provider_id,$from,$to){

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


                                     $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->order_by('tab_1.id','DESC');
                                     if(!empty($service_id)){
                                      $this->db->where('tab_1.service_id',$service_id);
                                     }
                                     if(!empty($status)){
                                      $this->db->where('tab_1.status',$status);
                                     }
                                     if(!empty($user_id)){
                                      $this->db->where('tab_1.user_id',$user_id);
                                     }
                                     if(!empty($provider_id)){
                                      $this->db->where('tab_1.provider_id',$provider_id);
                                     }
                                     if(!empty($from_date)){
                                      $this->db->where('tab_1.service_date >=',$from_date);
                                     }
                                     if(!empty($to_date)){
                                      $this->db->where('tab_1.service_date <=',$to_date);
                                     }
                                     $returns=$this->db->get()->result_array();
                                return $returns;


 }
 /*total*/


  /*booking reports*/

 public function get_pending_bookings(){

                            $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->where('tab_1.status',1);
                                     $this->db->order_by('tab_1.id','DESC');
                                     $returns=$this->db->get()->result_array();
                                return $returns;


 }


/*booking reports*/

 public function get_filter_pending_bookings($service_id,$status,$user_id,$provider_id,$from,$to){

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


                                     $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->where('tab_1.status',1);
                                     $this->db->order_by('tab_1.id','DESC');
                                     if(!empty($service_id)){
                                      $this->db->where('tab_1.service_id',$service_id);
                                     }
                                     if(!empty($status)){
                                      $this->db->where('tab_1.status',$status);
                                     }
                                     if(!empty($user_id)){
                                      $this->db->where('tab_1.user_id',$user_id);
                                     }
                                     if(!empty($provider_id)){
                                      $this->db->where('tab_1.provider_id',$provider_id);
                                     }
                                     if(!empty($from_date)){
                                      $this->db->where('tab_1.service_date >=',$from_date);
                                     }
                                     if(!empty($to_date)){
                                      $this->db->where('tab_1.service_date <=',$to_date);
                                     }
                                     $returns=$this->db->get()->result_array();
                                return $returns;


 }
 /*total*/


  /*booking reports*/

 public function get_inprogress_bookings(){

                            $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->where('tab_1.status in (2,3,4)');
                                     $this->db->order_by('tab_1.id','DESC');
                                     $returns=$this->db->get()->result_array();
                                return $returns;


 }


/*booking reports*/

 public function get_filter_inprogress_bookings($service_id,$status,$user_id,$provider_id,$from,$to){

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


                                     $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->where('tab_1.status in (2,3,4)');
                                     if(!empty($service_id)){
                                      $this->db->where('tab_1.service_id',$service_id);
                                     }
                                     if(!empty($status)){
                                      $this->db->where('tab_1.status',$status);
                                     }
                                     if(!empty($user_id)){
                                      $this->db->where('tab_1.user_id',$user_id);
                                     }
                                     if(!empty($provider_id)){
                                      $this->db->where('tab_1.provider_id',$provider_id);
                                     }
                                     if(!empty($from_date)){
                                      $this->db->where('tab_1.service_date >=',$from_date);
                                     }
                                     if(!empty($to_date)){
                                      $this->db->where('tab_1.service_date <=',$to_date);
                                     }
                                     $this->db->order_by('tab_1.id','DESC');
                                     $returns=$this->db->get()->result_array();
                                return $returns;


 }
 /*total*/

  /*booking reports*/

 public function get_complete_bookings(){

                            $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->where('tab_1.status',6);
                                     $this->db->order_by('tab_1.id','DESC');
                                     $returns=$this->db->get()->result_array();
                                return $returns;


 }


/*booking reports*/

 public function get_filter_complete_bookings($service_id,$status,$user_id,$provider_id,$from,$to){

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


                                     $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->where('tab_1.status',6);

                                     if(!empty($service_id)){
                                      $this->db->where('tab_1.service_id',$service_id);
                                     }
                                     if(!empty($status)){
                                      $this->db->where('tab_1.status',$status);
                                     }
                                     if(!empty($user_id)){
                                      $this->db->where('tab_1.user_id',$user_id);
                                     }
                                     if(!empty($provider_id)){
                                      $this->db->where('tab_1.provider_id',$provider_id);
                                     }
                                     if(!empty($from_date)){
                                      $this->db->where('tab_1.service_date >=',$from_date);
                                     }
                                     if(!empty($to_date)){
                                      $this->db->where('tab_1.service_date <=',$to_date);
                                     }
                                     $this->db->order_by('tab_1.id','DESC');

                                     $returns=$this->db->get()->result_array();
                                return $returns;


 }
 /*total*/

  /*booking reports*/

 public function get_reject_bookings(){

                            $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on,,tab_1.admin_change_status,tab_1.reject_paid_token,tab_1.admin_reject_comment');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->where('tab_1.status',5);

                                     $this->db->order_by('tab_1.id','DESC');
                                     $returns=$this->db->get()->result_array();
                                return $returns;


 }


/*booking reports*/

 public function get_filter_reject_bookings($service_id,$status,$user_id,$provider_id,$from,$to){

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


                                     $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on,tab_1.admin_change_status,tab_1.reject_paid_token,tab_1.admin_reject_comment');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->where('tab_1.status',5);

                                     if(!empty($service_id)){
                                      $this->db->where('tab_1.service_id',$service_id);
                                     }
                                     if(!empty($status)){
                                      $this->db->where('tab_1.status',$status);
                                     }
                                     if(!empty($user_id)){
                                      $this->db->where('tab_1.user_id',$user_id);
                                     }
                                     if(!empty($provider_id)){
                                      $this->db->where('tab_1.provider_id',$provider_id);
                                     }
                                     if(!empty($from_date)){
                                      $this->db->where('tab_1.service_date >=',$from_date);
                                     }
                                     if(!empty($to_date)){
                                      $this->db->where('tab_1.service_date <=',$to_date);
                                     }
                                     $this->db->order_by('tab_1.id','DESC');

                                     $returns=$this->db->get()->result_array();
                                return $returns;


 }
 /*total*/

  /*booking reports*/

 public function get_cancel_bookings(){

                            $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on,tab_1.admin_change_status,tab_1.reject_paid_token,tab_1.admin_reject_comment');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->where_in('tab_1.status',7);

                                     $this->db->order_by('tab_1.id','DESC');
                                     $returns=$this->db->get()->result_array();
                                return $returns;


 }


/*booking reports*/

 public function get_filter_cancel_bookings($service_id,$status,$user_id,$provider_id,$from,$to){

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


                                     $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on,tab_1.admin_change_status');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->where('tab_1.status',7);

                                     if(!empty($service_id)){
                                      $this->db->where('tab_1.service_id',$service_id);
                                     }
                                     if(!empty($status)){
                                      $this->db->where('tab_1.status',$status);
                                     }
                                     if(!empty($user_id)){
                                      $this->db->where('tab_1.user_id',$user_id);
                                     }
                                     if(!empty($provider_id)){
                                      $this->db->where('tab_1.provider_id',$provider_id);
                                     }
                                     if(!empty($from_date)){
                                      $this->db->where('tab_1.service_date >=',$from_date);
                                     }
                                     if(!empty($to_date)){
                                      $this->db->where('tab_1.service_date <=',$to_date);
                                     }
                                     $this->db->order_by('tab_1.id','DESC');
                                     
                                     $returns=$this->db->get()->result_array();
                                return $returns;


 }
 /*total*/

  public function get_reject_bookings_by_id($id){

                            $this->db->select('tab_1.id,tab_1.location,tab_1.service_date,tab_1.currency_code,tab_1.amount,tab_1.currency_code,tab_1.from_time,tab_1.to_time,tab_1.notes,tab_1.latitude,tab_1.longitude,tab_1.request_date,tab_1.request_time,tab_1.status,tab_1.reason,tab_1.updated_on,tab_1.status as payment_status');
                                     $this->db->select('tab_2.service_title');
                                     $this->db->select('tab_3.name as user_name,tab_3.mobileno as user_mobile,tab_3.email as user_email,tab_3.profile_img as user_profile_img,tab_3.token as user_token');
                                     $this->db->select('tab_4.name as provider_name,tab_4.mobileno as provider_mobile,tab_4.email as provider_email,tab_4.profile_img as provider_profile_img,tab_4.token as provider_token');
                                     $this->db->from('book_service tab_1');
                                     $this->db->join('services tab_2','tab_2.id=tab_1.service_id','LEFT');
                                     $this->db->join('users tab_3','tab_3.id=tab_1.user_id','LEFT');
                                     $this->db->join('providers tab_4','tab_4.id=tab_1.provider_id','LEFT');
                                     $this->db->where_in('tab_1.status',[5,7]);
                                     $this->db->where('tab_1.id',$id);

                                     $this->db->order_by('tab_1.id','DESC');
                                     $returns=$this->db->get()->row_array();
                                return $returns;


 }

  /*User accept flow and history*/
   public function reject_pay_proccess($data){
  if(!empty($data['book_id'])){
     $booking=$this->get_book_info($data['book_id']);

          if(!empty($booking)){
                      
  
                          $user_info=$this->get_token_info($data['token']);

                          $provider=$this->get_user_info($user_info->id,$user_info->type);

                          if($user_info->type==1){
							$query = $this->db->query('select * from admin_commission where admin_id=1');
							$amount = $query->row();
							$pertage = $amount->commission;	
                            $cash_text="Rejected by Provider and refunded by Admin.";
							$commission = ($booking['amount']) * $pertage / 100;
                            $ComAmount = $booking['amount'] - $commission;
                          }else{
                            $cash_text="Rejected by user and refunded by Admin.";
							$ComAmount = $booking['amount'];
                          }
                          
                          $wallet=$this->get_wallet($provider['token']);
                          
                          //$curren_wallet=$wallet['wallet_amt'];
						  $curren_wallet = get_gigs_currency($wallet['wallet_amt'], $wallet['currency_code'], $booking['currency_code']);
                          
                          /*wallet infos*/
                          
                          $history_pay['token']=$provider['token'];
						  $history_pay['currency_code']=$booking['currency_code'];
                          $history_pay['user_provider_id']=$provider['id'];
                          $history_pay['type']=$provider['type'];
                          $history_pay['tokenid']=$data['book_id'];
                          $history_pay['payment_detail']= json_encode($booking);//response
                          $history_pay['charge_id']=$booking['provider_id'];
                          $history_pay['transaction_id']=$data['book_id'];
                          $history_pay['exchange_rate']=0;
                          $history_pay['paid_status']='pass';
                          $history_pay['cust_id']='Self';
                          $history_pay['card_id']='Self';
                          $history_pay['total_amt']=$booking['amount']*100;
                          $history_pay['fee_amt']=0;
                          $history_pay['net_amt']=$booking['amount']*100;
                          $history_pay['amount_refund']=0;
                          $history_pay['current_wallet']=$curren_wallet;
                          $history_pay['credit_wallet']=($ComAmount);
                          $history_pay['debit_wallet']=0;
                          $history_pay['avail_wallet']=($ComAmount)+$curren_wallet;
                          $history_pay['reason']= $cash_text;
                          $history_pay['created_at']=date('Y-m-d H:i:s');
                          
                          if($this->db->insert('wallet_transaction_history',$history_pay)){
							  if($user_info->type==1){
								  //Detect commision refund to provider
									$commissionInsert = [
											'date' => date('Y:m:d'),
											'provider' => $booking['provider_id'],
											'user' => $booking['user_id'],
											'currency_code' => $booking['currency_code'],
											'amount' => $booking['amount'],
											'commission' => $pertage,
										];
									$commInsert = $this->db->insert('revenue', $commissionInsert);
							  }
                          /*update wallet table*/
						  
                          $wallet_amount = $ComAmount+$curren_wallet;
                          $wallet_data['wallet_amt']=get_gigs_currency($wallet_amount, $booking['currency_code'], $wallet['currency_code']);
                          $wallet_data['updated_on']=(date('Y-m-d H:i:s'));
                          $WHERE =array('token'=> $provider['token']);
                          $result=$this->update_wallet($wallet_data,$WHERE);

                          $book_h['admin_change_status']=1;
                          $book_h['updated_on']=(date('Y-m-d H:i:s'));
                          $book_h['reject_paid_token']=$data['token'];
                          $book_h['admin_reject_comment']=$data['favour_comment'];
                          $b_where =array('id'=> $data['book_id']);
                          $results=$this->book_update_admin($book_h,$b_where);
                           /*payment on stripe*/
                        return true;
                          }

    }



  }
                      
  }

  /*wallet update*/

   public function update_wallet($inputs,$where)
  {
    
    $this->db->set($inputs);
    $this->db->where($where);
    $this->db->update('wallet_table');
    return $this->db->affected_rows() != 0 ? true : false; 
  }

     public function book_update_admin($inputs,$where)
  {
    
    $this->db->set($inputs);
    $this->db->where($where);
    $this->db->update('book_service');
    return $this->db->affected_rows() != 0 ? true : false; 
  }



    /*get user infermation*/

  public function get_user_info($user_id,$user_type){

    if($user_type ==2){
      $val=$this->db->select('*')->from('users')->where('id',$user_id)->where('type',$user_type)->get()->row_array();
    }else{
      $val=$this->db->select('*')->from('providers')->where('id',$user_id)->where('type',$user_type)->get()->row_array();
    }
        
    return $val;
  }

  public function get_book_info($book_service_id){


    $ret=$this->db->select('tab_1.provider_id,tab_1.user_id,tab_1.status,tab_1.currency_code,tab_1.amount,tab_2.service_title')->
                    from('book_service as tab_1')->
                    join('services as tab_2','tab_2.id=tab_1.service_id','LEFT')->
                    where('tab_1.id',$book_service_id)->limit(1)->
                    order_by('tab_1.id','DESC')->
                    get()->row_array();
    return $ret;

  }

        /*get information base on token*/
    public function get_token_info($token){
        
        $user_table=$this->db->select('id,name,profile_img,token,type,email')->
                        from('users')->
                        where('token',$token)->
                        get()->row();
        $provider_table=$this->db->select('id,name,profile_img,token,type,email')->
                        from('providers')->
                        where('token',$token)->
                        get()->row();
        if(!empty($user_table)){
            return $user_table;
        }else{
            return $provider_table;
        }                
        
    }

		
/*wallet information*/

public function get_wallet($token){
  $val=$this->db->select('id,token,currency_code,wallet_amt,type')->from('wallet_table')->where('token',$token)->get()->row();
  $wallet=[];
  $setting_currency='';
     $query = $this->db->query("select * from system_settings WHERE status = 1");
          $result = $query->result_array();
          if(!empty($result))
          {
              foreach($result as $data){
                  if($data['key']=='currency_option'){
                    $setting_currency=$data['value'];
                  }
              }
          }

          /*sum of totAL wallet*/

              $wallet_tot=$this->db->select('sum(credit_wallet)as total_credit,sum(debit_wallet)as total_debit')->from('wallet_transaction_history')->
                    where('token',$token)->order_by('id','DESC')->
                    get()->row_array();
   
  if (!empty($val)) {
   
      $wallet['id']=$val->id;
      $wallet['token']=$val->token;
      $wallet['type']=$val->type;
      $wallet['wallet_amt']=strval(abs($val->wallet_amt));
      $wallet['currency']=currency_conversion($val->currency_code);
      $wallet['currency_code']=$val->currency_code;
      $wallet['total_credit']=strval($wallet_tot['total_credit']);
      $wallet['total_debit']=strval($wallet_tot['total_debit']);
   
  }
  if(!empty($wallet)){
    return $wallet;
  }else{
    return 0;
  }

}

}
?>
