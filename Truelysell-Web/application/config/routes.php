<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/*
| ----------------------------------------------------------------	---------
| URI ROUTING
| -------------------------------------------------------------------------
| This file lets you re-map URI requests to specific controller functions.
|
| Typically there is a one-to-one relationship between a URL string
| and its corresponding controller class/method. The segments in a
| URL normally follow this pattern:
|
|	example.com/class/method/id/
|
| In some instances, however, you may want to remap this relationship
| so that a different class/function is called than the one
| corresponding to the URL.
|
| Please see the user guide for complete details:
|
|	https://codeigniter.com/user_guide/general/routing.html
|
| -------------------------------------------------------------------------
| RESERVED ROUTES
| -------------------------------------------------------------------------
|
| There are three reserved routes:
|
|	$route['default_controller'] = 'welcome';
|
| This route indicates which controller class should be loaded if the
| URI contains no data. In the above example, the "welcome" class
| would be loaded.
|
|	$route['404_override'] = 'errors/page_missing';
|
| This route will tell the Router which controller/method to use if those
| provided in the URL cannot be matched to a valid route.
|
|	$route['translate_uri_dashes'] = FALSE;
|
| This is not exactly a route, but allows you to automatically route
| controller and method names that contain dashes. '-' isn't a valid
| class or method name character, so it requires translation.
| When you set this option to TRUE, it will replace ALL dashes in the
| controller and method URI segments.
|
| Examples:	my-controller/index	-> my_controller/index
|		my-controller/my-method	-> my_controller/my_method
*/
$route['default_controller'] = 'home';
$route['admin'] = 'admin/login';
$route['forgot_password'] = 'admin/login/forgot_password';
$route['dashboard'] = 'admin/dashboard';
$route['map'] = 'admin/dashboard/map_list';
$route['map_lists'] = 'admin/dashboard/service_map_list';
$route['admin-profile'] = 'admin/profile';
$route['admin/logout'] = 'admin/login/logout';
$route['admin/wallet'] = 'admin/wallet';
$route['admin/wallet-history'] = 'admin/wallet/wallet_history';
/*booking report*/
$route['admin/total-report'] = 'admin/Booking/total_bookings';
$route['admin/pending-report'] = 'admin/Booking/pending_bookings';
$route['admin/inprogress-report'] = 'admin/Booking/inprogress_bookings';
$route['admin/complete-report'] = 'admin/Booking/completed_bookings';
$route['admin/reject-report'] = 'admin/Booking/rejected_bookings';
$route['admin/cancel-report'] = 'admin/Booking/cancel_bookings';
$route['reject-payment/(:num)'] = 'admin/Booking/reject_booking_payment';
$route['pay-reject'] = 'admin/Booking/update_reject_payment';

$route['admin-notification'] = 'admin/Dashboard/admin_notification';


//admin users
$route['adminusers'] = 'admin/dashboard/adminusers';
$route['adminusers/edit']='admin/dashboard/edit_adminusers';
$route['adminusers/edit/(:num)']='admin/dashboard/edit_adminusers/$1';
$route['adminuser-details/(:num)'] = 'admin/dashboard/adminuser_details/$1';
$route['adminusers_list'] = 'admin/dashboard/adminusers_list';

//


//email template 
$route['emailtemplate'] = 'admin/emailtemplate';
$route['edit-emailtemplate/(:num)'] = 'admin/emailtemplate/edit/$1';

//

/* Settings*/
$route['admin/fb_social_media'] = 'admin/settings/fb_social_media';
$route['admin/googleplus_social_media'] = 'admin/settings/googleplus_social_media';
$route['admin/twit_social_media'] = 'admin/settings/twit_social_media';
$route['admin/emailsettings'] = 'admin/settings/emailsettings';
$route['admin/sms-settings'] = 'admin/settings/smssettings';
$route['admin/stripe_payment_gateway'] = 'admin/settings/stripe_payment_gateway';
$route['admin/razorpay_payment_gateway'] = 'admin/settings/razorpay_payment_gateway';
$route['admin/paypal_payment_gateway'] = 'admin/settings/paypal_payment_gateway';
$route['admin/paytabs_payment_gateway'] = 'admin/settings/paytabs_payment_gateway';


$route['users'] = 'admin/dashboard/users';
$route['user-details/(:num)'] = 'admin/dashboard/user_details/$1';
$route['users_list'] = 'admin/dashboard/users_list';


$route['categories'] = 'admin/categories/categories';
$route['add-category'] = 'admin/categories/add_categories';
$route['categories/check_category_name'] = 'admin/categories/check_category_name';
$route['edit-category/(:num)'] = 'admin/categories/edit_categories/$1';

$route['subcategories'] = 'admin/categories/subcategories';
$route['add-subcategory'] = 'admin/categories/add_subcategories';
$route['categories/check_subcategory_name'] = 'admin/categories/check_subcategory_name';
$route['edit-subcategory/(:num)'] = 'admin/categories/edit_subcategories/$1';

$route['subscriptions'] = 'admin/service/subscriptions';

$route['add-subscription'] = 'admin/service/add_subscription';

$route['service/check_subscription_name'] = 'admin/service/check_subscription_name';

$route['service/save_subscription'] = 'admin/service/save_subscription';

$route['edit-subscription/(:num)'] = 'admin/service/edit_subscription/$1';

$route['service/update_subscription'] = 'admin/service/update_subscription';

$route['subscription-list'] = 'user/subscription/subscription_list';

$route['ratingstype'] = 'admin/ratingstype/ratingstype';
$route['review-reports'] = 'admin/ratingstype/review_report';

$route['add-ratingstype'] = 'admin/ratingstype/add_ratingstype';

$route['ratingstype/check_ratingstype_name'] = 'admin/ratingstype/check_ratingstype_name';

$route['edit-ratingstype/(:num)'] = 'admin/ratingstype/edit_ratingstype/$1';

$route['service-providers'] = 'admin/service/service_providers';

$route['provider_list'] = 'admin/service/provider_list';
$route['service-list'] = 'admin/service/service_list';
$route['provider-details/(:num)'] = 'admin/service/provider_details/$1';
$route['admin/provider_list'] = 'admin/service/provider_list';
$route['payment_list'] = 'admin/payments/payment_list';
$route['admin-payment/(:any)'] = 'admin/payments/admin_payment/$1';
$route['service-details/(:num)'] = 'admin/service/service_details/$1';
$route['contact-details/(:num)'] = 'admin/contact/contact_details/$1';

/*web*/

$route['all-categories'] = 'categories';
$route['featured-category'] = 'user/categories/featured_categories';
$route['service-preview/(:any)'] = 'home/service_preview/$1';
$route['all-services'] = 'home/services';
$route['featured-services'] = 'user/service/featured_services';
$route['popular-services'] = 'user/service/popular_services';
$route['search'] = 'home/services';
$route['about-us'] = 'user/about/about_us';
$route['terms-conditions'] = 'user/terms/terms';
$route['contact'] = 'user/contact/contact';
$route['pages/(:any)'] = 'home/pages/$1';
$route['search/(:any)'] = 'home/services/$1';
$route['privacy'] = 'user/privacy/privacy';
$route['faq'] = 'user/privacy/faq';
$route['help'] = 'user/privacy/help';

//my_service_pagination
$route['my-services']='user/myservice/index';
$route['my-services-inactive']='user/myservice/inactive_services';
//end

$route['add-service']='user/service/add_service';

$route['edit_service']='user/service/edit_service';
$route['notification-list']='user/service/notification_view';
$route['booking']='user/service/booking';
$route['update_bookingstatus']='user/service/update_bookingstatus';
$route['update_status_user']='user/service/update_status_user';
$route['update_booking/(:any)']='user/service/update_booking/$1';
$route['user_bookingstatus/(:any)']='user/service/user_bookingstatus/$1';
$route['book-service/(:any)']='user/service/book_service/$1';
$route['user-dashboard']='user/service/user_dashboard';
$route['provider-dashboard']='user/service/provider_dashboard';
$route['user-settings']='user/dashboard/user_settings';
$route['change-password']='user/dashboard/userchangepassword';
$route['provider-change-password']='user/dashboard/prochangepassword';
$route['user-wallet']='user/dashboard/user_wallet';
$route['paytab_payment']='user/dashboard/paytab_payment';//user/dashboard/paytab_payment
$route['user-payment']='user/dashboard/user_payment';
$route['user-accountdetails']='user/dashboard/user_accountdetails';
$route['user-reviews']='user/dashboard/user_reviews';
$route['provider-reviews']='user/dashboard/provider_reviews';
$route['booking-details/(:any)']='user/service/booking_details/$1';
$route['booking-details-user/(:any)']='user/service/booking_details_user/$1';

$route['provider-bookings']='user/dashboard/provider_bookings';
$route['provider-settings']='user/dashboard/provider_settings';
$route['provider-wallet']='user/dashboard/provider_wallet';
$route['provider-payment']='user/dashboard/provider_payment';
$route['provider-subscription']='user/dashboard/provider_subscription';
$route['provider-availability']='user/dashboard/provider_availability';
$route['provider-accountdetails']='user/dashboard/provider_accountdetails';
$route['create_availability']='user/dashboard/create_availability';
$route['user-bookings']='user/dashboard/user_bookings';
$route['logout']='user/login/logout';

/*
 * Multiple Languages
 */
$route['language']='admin/language';

/*api*/

/*chat api*/

$route['user-chat'] = 'user/Chat_ctrl';
$route['user-chat/booking-new-chat']='user/Chat_ctrl/booking_new_chat';
$route['user-chat/insert_chat']='user/Chat_ctrl/insert_message';
$route['user-chat/get_user_chat_lists']='user/Chat_ctrl/get_user_chat_lists';

$route['api/country_details'] = 'api/api/country_details';
$route['api/chat_details'] = 'api/api/chat_details';
$route['api/chat'] = 'api/api/chat';
$route['api/chat_storage'] = 'api/api/insert_message';
$route['api/get-chat-list'] = 'api/api/get_chat_list';
$route['api/get-chat-history'] = 'api/api/get_chat_history';
$route['api/flash-device-token'] = 'api/api/flash_device_token';
$route['api/get-notification-list'] = 'api/api/get_notification_list';
$route['api/home'] = 'api/api/home';
$route['api/demo-home'] = 'api/api/demo_home';
$route['api/service-details'] = 'api/api/service_details';
$route['api/all-services'] = 'api/api/all_services';
$route['api/category'] = 'api/api/category';
$route['api/subcategory'] = 'api/api/subcategory';
$route['api/generate_otp'] = 'api/api/generate_otp';
$route['api/provider_signin'] = 'api/api/provider_signin';
$route['api/subcategory_services'] = 'api/api/subcategory_services';
$route['api/profile'] = 'api/api/profile';
$route['api/subscription'] = 'api/api/subscription';
$route['api/subscription_success'] = 'api/api/subscription_success';
$route['api/add_service'] = 'api/api/add_service';
$route['api/update_service'] = 'api/api/update_service';
$route['api/delete_service'] = 'api/api/delete_service';
$route['api/update_provider'] = 'api/api/update_provider';
$route['api/my_service'] = 'api/api/my_service';
$route['api/edit_service'] = 'api/api/edit_service';
$route['api/existing_user'] = 'api/api/existing_user';
$route['api/delete_serviceimage'] = 'api/api/delete_serviceimage';
$route['api/add_availability'] = 'api/api/add_availability';
$route['api/update_availability'] = 'api/api/update_availability';
$route['api/availability'] = 'api/api/availability';
$route['api/user_signin'] = 'api/api/user_signin';
$route['api/generate_userotp'] = 'api/api/generate_userotp';
$route['api/logout'] = 'api/api/logout';
$route['api/logout_provider'] = 'api/api/logout_provider';
$route['api/update_user'] = 'api/api/update_user';
$route['api/user_profile'] = 'api/api/user_profile';
$route['api/service_availability'] = 'api/api/service_availability';
$route['api/book_service'] = 'api/api/book_service';
$route['api/search_services'] = 'api/api/search_services';
$route['api/bookingdetail'] = 'api/api/bookingdetail';
$route['api/bookinglist_provider'] = 'api/api/bookinglist_provider';
$route['api/requestlist_provider'] = 'api/api/requestlist_provider';
$route['api/bookinglist_users'] = 'api/api/bookinglist_users';
$route['api/bookingdetail_user'] = 'api/api/bookingdetail_user';
$route['api/views'] = 'api/api/views';
$route['api/update_bookingstatus'] = 'api/api/update_bookingstatus';
$route['api/service_statususer'] = 'api/api/service_statususer';
$route['api/bookinglist'] = 'api/api/bookinglist';
$route['api/get_services_from_subid'] = 'api/api/get_services_from_subid';#get services belongs to sub category id
$route['api/get_provider_dashboard_infos'] = 'api/api/get_provider_dashboard_infos';#get provider dashboar infos
$route['api/delete_account'] = 'api/api/delete_account';
$route['api/rate_review'] = 'api/api/rate_review';
$route['api/review_type'] = 'api/api/review_type'; 
$route['api/update_booking'] = 'api/api/update_booking';
$route['api/generate_otp_provider'] = 'api/api/generate_otp_provider';
$route['api/check_provider_email'] = 'api/api/check_provider_email';
$route['api/check_user_emailid'] = 'api/api/check_user_emailid';
$route['api/forget_password'] = 'api/api/forget_password';
$route['api/userchangepassword'] = 'api/api/userchangepassword';
$route['api/generate_otp_user'] = 'api/api/generate_otp_user';
$route['api/stripe_account_details'] = 'api/api/stripe_account_details';
$route['api/details'] = 'api/api/details';
$route['api/account_details'] = 'api/api/account_details';
$route['api/update-myservice-status'] = 'api/api/update_myservice_status';


$route['api/chat_storage'] = 'api/api/insert_message';
$route['api/get-chat-list'] = 'api/api/get_chat_list';
$route['api/get-chat-history'] = 'api/api/get_chat_history';
$route['api/get-wallet'] = 'api/api/get_wallet_amt';
$route['api/add-user-wallet'] = 'api/api/add_user_wallet';
$route['api/withdraw-provider'] = 'api/api/provider_wallet_withdrawal';
$route['api/customer-card-list'] = 'api/api/get_customer_saved_card';
$route['api/wallet-history'] = 'api/api/wallet_history';
$route['api/stripe_details'] = 'api/api/stripe_details';
$route['api/provider-card-info'] = 'api/api/provider_card_info';


$route['404_override'] = '';
$route['translate_uri_dashes'] = FALSE;

//Add Multiple Language
$route['language'] = 'admin/language';
$route['add-language'] = 'admin/language/AddLanguages';
$route['insert-language'] = 'admin/language/InsertLanguage';
$route['update_language'] = 'admin/language/update_language_status';
//Add Wep Keywords
$route['wep_language'] = 'admin/language/wep_language';
$route['add-wep-keyword'] = 'admin/language/AddWepKeyword';
$route['insert_web_keyword'] = 'admin/language/InsertWepKeyword';
$route['update_multi_web_language'] = 'admin/language/update_multi_web_language/';
$route['language_web_list'] = 'admin/language/language_web_list';
//App Keyword
$route['app_page_list'] = 'admin/language/AppPageList';
$route['app_page_list/(:any)'] = 'admin/language/pages_language';
$route['add-app-keyword'] = 'admin/language/AddAPPKeyword';
$route['insert_app_keyword'] = 'admin/language/InsertAppKeyword';
$route['language_list'] = 'admin/language/language_list';
//$route['app-keyword-add'] = 'admin/language/AllAPPKeyword';
$route['insertApp'] = 'admin/language/AppKeyword';
$route['app-keyword-add/(:any)'] = 'admin/language/AllAPPKeyword';


$route['Revenue'] = 'admin/Revenue';

$route['paypal_braintree'] = 'user/paypal/braintree';


$route['admin/theme-color'] = 'admin/Settings/ThemeColorChange';
$route['Change_color'] = 'admin/Settings/ChangeColor';







