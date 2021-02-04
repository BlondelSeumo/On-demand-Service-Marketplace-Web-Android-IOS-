<?php
$type = $this->session->userdata('usertype');
$userId = $this->session->userdata('id');
$default_language = default_language();
$active_language = active_language();

if ($this->session->userdata('user_select_language') == '') {

    $lang = $default_language['language_value'];
} else {
    $lang = $this->session->userdata('user_select_language');
}
?>

<?php
$default_language_select = default_language();

if ($this->session->userdata('user_select_language') == '') {

    if ($default_language_select['tag'] == 'ltr' || $default_language_select['tag'] == '') {

        
    } elseif ($default_language_select['tag'] == 'rtl') {
        echo'<link href="' . base_url() . 'assets/css/bootstrap-rtl.min.css" media="screen" rel="stylesheet" type="text/css" />';
        echo'<link href="' . base_url() . 'assets/css/app-rtl.css" rel="stylesheet" />';
    }
} else {
    if ($this->session->userdata('tag') == 'ltr' || $this->session->userdata('tag') == '') {
        
    } elseif ($this->session->userdata('tag') == 'rtl') {

        echo'<link href="' . base_url() . 'assets/css/bootstrap-rtl.min.css" media="screen" rel="stylesheet" type="text/css" />';
        echo'<link href="' . base_url() . 'assets/css/app-rtl.css" rel="stylesheet" />';
    }
}
?>

<body>
    <div class="page-loading">
        <div class="preloader-inner">
            <div class="preloader-square-swapping">
                <div class="cssload-square-part cssload-square-green"></div>
                <div class="cssload-square-part cssload-square-pink"></div>
                <div class="cssload-square-blend"></div>
            </div>
        </div>
    </div>

    <div class="main-wrapper">

        <header class="header sticktop">
            <nav class="navbar navbar-expand-lg header-nav">
                <div class="navbar-header">
                    <a id="mobile_btn" href="javascript:void(0);">
                        <span class="bar-icon">
                            <span></span>
                            <span></span>
                            <span></span>
                        </span>
                    </a>
                    <a href="<?php echo base_url(); ?>" class="navbar-brand logo">
                        <img src="<?php echo base_url() . $this->website_logo_front; ?>" class="img-fluid" alt="Logo">
                    </a>
                    <a href="<?php echo base_url(); ?>" class="navbar-brand logo-small">
                        <img src="<?php echo base_url(); ?>assets/img/logo-icon.png" class="img-fluid" alt="Logo">
                    </a>
                </div>
                <div class="main-menu-wrapper">
                    <div class="menu-header">
                        <a href="<?php echo base_url(); ?>" class="menu-logo">
                            <img src="<?php echo base_url() . $this->website_logo_front; ?>" class="img-fluid" alt="Logo">
                        </a>
                        <a id="menu_close" class="menu-close" href="javascript:void(0);">
                            <i class="fas fa-times"></i>
                        </a>
                    </div>
                    <ul class="main-nav">



                       <!--  <li><a href="<?php echo base_url(); ?>"><?php echo (!empty($user_language[$user_selected]['lg_home'])) ? $user_language[$user_selected]['lg_home'] : $default_language['en']['lg_home']; ?></a></li> -->
                        <li class="has-submenu">
                            <?php
                            $this->db->select('*');
                            $this->db->from('categories');
                            $this->db->where('status', 1);
                            $this->db->order_by('id', 'DESC');
                            $result = $this->db->get()->result_array();
                            ?>
                            <a href="<?php echo base_url(); ?>all-categories"><?php echo (!empty($user_language[$user_selected]['lg_category_name'])) ? $user_language[$user_selected]['lg_category_name'] : $default_language['en']['lg_category_name']; ?> <i class="fas fa-chevron-down"></i></a>
                            <ul class="submenu">
                                <?php foreach ($result as $res) { ?>
                                    <li><a href="<?php echo base_url(); ?>search/<?php echo str_replace(' ', '-', strtolower($res['category_name'])); ?>"><?php echo ucfirst($res['category_name']); ?></a></li>
                                <?php } ?>
                            </ul>
                        </li>

                        <li><a href="<?php echo base_url(); ?>about-us"><?php echo (!empty($user_language[$user_selected]['lg_about'])) ? $user_language[$user_selected]['lg_about'] : $default_language['en']['lg_about']; ?></a></li>
                        <?php
                        $usertype = $this->session->userdata('usertype');
                        if ($usertype != 'provider') {
                            ?>    
                            <li><a href="<?php echo base_url(); ?>contact"><?php echo (!empty($user_language[$user_selected]['lg_contact'])) ? $user_language[$user_selected]['lg_contact'] : $default_language['en']['lg_contact']; ?></a></li>
                        <?php } ?>
                        <?php if ($this->session->userdata('id') == '') { ?>
                            <li><a href="javascript:void(0);" data-toggle="modal" data-target="#modal-wizard"><?php echo (!empty($user_language[$user_selected]['lg_become_prof'])) ? $user_language[$user_selected]['lg_become_prof'] : $default_language['en']['lg_become_prof']; ?></a></li>

                            <?php
                            ?>
                            <li><a href="javascript:void(0);" data-toggle="modal" data-target="#modal-wizard1"><?php echo (!empty($user_language[$user_selected]['lg_become_user'])) ? $user_language[$user_selected]['lg_become_user'] : $default_language['en']['lg_become_user']; ?></a></li>

                            <li class="login-link">
                                <a href="javascript:void(0);" data-toggle="modal" data-target="#tab_login_modal"><?php echo (!empty($user_language[$user_selected]['lg_login'])) ? $user_language[$user_selected]['lg_login'] : $default_language['en']['lg_login']; ?></a>
                            </li>
                        <?php } ?> 

                        <li class="has-submenu">
                            <a href="javascript:;"><?php echo $lang; ?><i class="fas fa-chevron-down"></i></a>
                            <ul class="submenu lang-blk">

                                <?php foreach ($active_language as $active) { ?>
                                    <li>

                                        <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" id="csrf_lang"/>
                                        <a href="javascript:;" onclick="change_language(this)" lang_tag="<?php echo $active['tag']; ?>" lang="<?php echo $active['language_value']; ?>"<?php
                                        if ($active['language_value'] == $lang) {
                                            echo "selected";
                                        }
                                        ?>>
                                            <?php echo ($active['language']); ?></a></li>
                                <?php } ?>
                            </ul>
                        </li>
                        <?php
                        if ($userId != '') {
                            $get_currency = get_currency();
                            if ($type == 'user') {
                                $user_currency = get_user_currency();
                            } else if ($type == 'provider') {
                                $user_currency = get_provider_currency();
                            }
                            $user_currency_code = $user_currency['user_currency_code'];
                            ?>
                            <li class="has-submenu" style=" margin-top: 28px;">
                                <span class="currency-blk">
                                <select class="form-control-sm" onchange="user_currency(this.value)">
                                    <?php foreach ($get_currency as $row) { ?>
                                        <option value="<?= $row['currency_code']; ?>" <?= ($row['currency_code'] == $user_currency_code) ? 'selected' : ''; ?>><?= $row['currency_code']; ?></option>
                                    <?php } ?> 
                                </select> 
                                </span>     
                            </li>

                        <?php } ?>

                        <?php
                        if (($this->session->userdata('id') != '') && ($this->session->userdata('usertype') == 'provider')) {


                            $get_details = $this->db->where('id', $this->session->userdata('id'))->get('providers')->row_array();
                            $get_availability = $this->db->where('provider_id', $this->session->userdata('id'))->get('business_hours')->row_array();
                            if (!empty($get_availability['availability'])) {
                                $check_avail = strlen($get_availability['availability']);
                            } else {
                                $check_avail = 2;
                            }

                            $get_subscriptions = $this->db->select('*')->from('subscription_details')->where('subscriber_id', $this->session->userdata('id'))->where('expiry_date_time >=', date('Y-m-d 00:00:59'))->get()->row_array();
                            if (!isset($get_subscriptions)) {
                                $get_subscriptions['id'] = '';
                            }
                            if (!empty($get_availability) && !empty($get_subscriptions['id']) && $check_avail > 5) {
                                ?>
                                <li class="mobile-list">
                                    <a href="<?php echo base_url(); ?>add-service"><?php echo (!empty($user_language[$user_selected]['lg_post_service'])) ? $user_language[$user_selected]['lg_post_service'] : $default_language['en']['lg_post_service']; ?></a>
                                </li>
                                <?php
                            } elseif ($get_subscriptions['id'] == '') {
                                ?>
                                <li class="mobile-list">
                                    <span class="post-service-blk">
                                    <a href="javascript:;" class="get_pro_subscription"><i class="fas fa-plus-circle mr-1"></i><?php echo (!empty($user_language[$user_selected]['lg_post_service'])) ? $user_language[$user_selected]['lg_post_service'] : $default_language['en']['lg_post_service']; ?></a>
                                    </span>
                                </li>
                                <?php
                            } elseif ($get_availability == '' || $get_availability['availability'] == '' || $check_avail < 5) {
                                ?>
                                <li class="mobile-list">
                                    <a href="javascript:;" class="get_pro_availabilty"><span><?php echo (!empty($user_language[$user_selected]['lg_post_service'])) ? $user_language[$user_selected]['lg_post_service'] : $default_language['en']['lg_post_service']; ?></span></a>
                                </li>
                                <?php
                            }
                        }
                        ?>
                    </ul>		 
                </div>		 
                <ul class="nav header-navbar-rht">
                    <?php if ($this->session->userdata('id') == '') { ?>
                        <li class="nav-item">
                            <a class="nav-link header-login" href="javascript:void(0);" data-toggle="modal" data-target="#tab_login_modal"><?php echo (!empty($user_language[$user_selected]['lg_login'])) ? $user_language[$user_selected]['lg_login'] : $default_language['en']['lg_login']; ?></a>
                        </li>
                        <?php
                    }
                    $wallet = 0;
                    $token = '';
                    if ($this->session->userdata('id') != '') {
                        if (!empty($token = $this->session->userdata('chat_token'))) {
                            $wallet_sql = $this->db->select('*')->from('wallet_table')->where('token', $this->session->userdata('chat_token'))->get()->row();
                            if (!empty($wallet_sql)) {
                                $wallet = $wallet_sql->wallet_amt;
                                $user_currency_code = '';
                                If (!empty($userId)) {
                                   
                                    $wallet = $wallet_sql->wallet_amt;
                                    if ($type == 'user') {
                                        $user_currency = get_user_currency();
                                    } else if ($type == 'provider') {
                                        $user_currency = get_provider_currency();
                                    }
                                    $user_currency_code = $user_currency['user_currency_code'];

                                    $wallet = get_gigs_currency($wallet_sql->wallet_amt, $wallet_sql->currency_code, $user_currency_code);
                                } else {
                                    $user_currency_code = settings('currency');
                                    $wallet = $wallet_sql->wallet_amt;
                                }
                            }
                        }

                        if ($this->session->userdata('usertype') == 'provider') {
                            ?>
                            <li class="nav-item desc-list wallet-menu">
                                <a href="<?= base_url() . 'provider-wallet' ?>" class="nav-link header-login">
                                    <img src="<?php echo $base_url ?>assets/img/wallet.png" alt="" class="mr-2 wallet-img"><span><?php echo (!empty($user_language[$user_selected]['lg_wallet'])) ? $user_language[$user_selected]['lg_wallet'] : $default_language['en']['lg_wallet']; ?>:</span> <?php echo currency_conversion($user_currency_code) . $wallet; ?>
                                </a>
                            </li>
                        <?php } else {
                            ?>
                            <li class="nav-item desc-list wallet-menu">
                                <a href="<?= base_url() . 'user-wallet' ?>" class="nav-link header-login">
                                    <img src="<?php echo $base_url ?>assets/img/wallet.png" alt="" class="mr-2 wallet-img"><span><?php echo (!empty($user_language[$user_selected]['lg_wallet'])) ? $user_language[$user_selected]['lg_wallet'] : $default_language['en']['lg_wallet']; ?>:</span> <?php echo currency_conversion($user_currency_code) . $wallet; ?>
                                </a>
                            </li>
                            <?php
                        }
                    }
                    ?>

                    <?php
                    if (($this->session->userdata('id') != '') && ($this->session->userdata('usertype') == 'provider')) {

                        $get_details = $this->db->where('id', $this->session->userdata('id'))->get('providers')->row_array();
                        $get_availability = $this->db->where('provider_id', $this->session->userdata('id'))->get('business_hours')->row_array();
                        if (!empty($get_availability['availability'])) {
                            $check_avail = strlen($get_availability['availability']);
                        } else {
                            $check_avail = 2;
                        }

                        $get_subscriptions = $this->db->select('*')->from('subscription_details')->where('subscriber_id', $this->session->userdata('id'))->where('expiry_date_time >=', date('Y-m-d 00:00:59'))->get()->row_array();
                        if (!isset($get_subscriptions)) {
                            $get_subscriptions['id'] = '';
                        }
                        if (!empty($get_availability) && !empty($get_subscriptions['id']) && $check_avail > 5) {
                            ?>
                            <li class="nav-item desc-list">
                                <a href="<?php echo base_url(); ?>add-service" class="nav-link header-login"><i class="fas fa-plus-circle mr-1"></i> <span><?php echo (!empty($user_language[$user_selected]['lg_post_service'])) ? $user_language[$user_selected]['lg_post_service'] : $default_language['en']['lg_post_service']; ?></span></a>
                            </li>
                            <?php
                        } elseif ($get_subscriptions['id'] == '') {
                            ?>
                            <li class="nav-item desc-list">
                                <a href="javascript:;" class="nav-link header-login get_pro_subscription"><i class="fas fa-plus-circle mr-1"></i> <span><?php echo (!empty($user_language[$user_selected]['lg_post_service'])) ? $user_language[$user_selected]['lg_post_service'] : $default_language['en']['lg_post_service']; ?></span></a>
                            </li>
                            <?php
                        } elseif ($get_availability == '' || $get_availability['availability'] == '' || $check_avail < 5) {
                            ?>
                            <li class="nav-item desc-list">
                                <a href="javascript:;" class="nav-link header-login get_pro_availabilty"><i class="fas fa-plus-circle mr-1"></i> <span><?php echo (!empty($user_language[$user_selected]['lg_post_service'])) ? $user_language[$user_selected]['lg_post_service'] : $default_language['en']['lg_post_service']; ?></span></a>
                            </li>
                            <?php
                        }
                    }
                    ?>

                    <?php
                    if ($this->session->userdata('id')) {
                        if ($this->session->userdata('usertype') == 'user') {
                            $user_details = $this->db->where('id', $this->session->userdata('id'))->get('users')->row_array();
                        } elseif ($this->session->userdata('usertype') == 'provider') {
                            $user_details = $this->db->where('id', $this->session->userdata('id'))->get('providers')->row_array();
                        }
                        ?>
                        <?php if ($this->session->userdata('usertype') == 'provider') { ?>
                            <!-- Notifications -->
                            <li class="nav-item dropdown logged-item">
                                <?php
                                if (!empty($this->session->userdata('chat_token'))) {
                                    $ses_token = $this->session->userdata('chat_token');
                                } else {
                                    $ses_token = '';
                                }

                                if (!empty($ses_token)) {
                                    $ret = $this->db->select('*')->
                                                    from('notification_table')->
                                                    where('receiver', $ses_token)->
                                                    where('status', 1)->
                                                    order_by('notification_id', 'DESC')->
                                                    get()->result_array();

                                    $notification = [];
                                    if (!empty($ret)) {
                                        foreach ($ret as $key => $value) {
                                            $user_table = $this->db->select('id,name,profile_img,token,type')->
                                                            from('users')->
                                                            where('token', $value['sender'])->
                                                            get()->row();
                                            $provider_table = $this->db->select('id,name,profile_img,token,type')->
                                                            from('providers')->
                                                            where('token', $value['sender'])->
                                                            get()->row();
                                            if (!empty($user_table)) {
                                                $user_info = $user_table;
                                            } else {
                                                $user_info = $provider_table;
                                            }
                                            $notification[$key]['name'] = !empty($user_info->name) ? $user_info->name : '';
                                            $notification[$key]['message'] = !empty($value['message']) ? $value['message'] : '';
                                            $notification[$key]['profile_img'] = !empty($user_info->profile_img) ? $user_info->profile_img : '';
                                            $notification[$key]['utc_date_time'] = !empty($value['utc_date_time']) ? $value['utc_date_time'] : '';
                                        }
                                    }
                                    $n_count = count($notification);
                                } else {
                                    $n_count = 0;
                                    $notification = [];
                                }

                                /* Notification Count */
                                if (!empty($n_count) && $n_count != 0) {
                                    $notify = "<span class='badge badge-pill bg-yellow'>" . $n_count . "</span>";
                                } else {
                                    $notify = "";
                                }
                                ?>

                                <a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown">
                                    <i class="fas fa-bell"></i> <?= $notify; ?>
                                </a>
                                <div class="dropdown-menu notify-blk dropdown-menu-right notifications">
                                    <div class="topnav-dropdown-header">
                                        <span class="notification-title">Notifications</span>
                                        <a href="javascript:void(0)" class="clear-noti noty_clear" data-token="<?php echo $this->session->userdata('chat_token'); ?>"><?php echo (!empty($user_language[$user_selected]['lg_clear_all'])) ? $user_language[$user_selected]['lg_clear_all'] : $default_language['en']['lg_clear_all']; ?>  </a>
                                    </div>
                                    <div class="noti-content">
                                        <ul class="notification-list">
                                            <?php
                                            if (!empty($notification)) {
                                                foreach ($notification as $key => $notify) {
                                                    $full_date = date('Y-m-d H:i:s', strtotime($notify['utc_date_time']));
                                                    $date = date('Y-m-d', strtotime($full_date));
                                                    $date_f = date('d-m-Y', strtotime($full_date));
                                                    $yes_date = date('Y-m-d', (strtotime('-1 day', strtotime(date('Y-m-d')))));
                                                    $time = date('H:i', strtotime($full_date));
                                                    $session = date('h:i A', strtotime($time));
                                                    if ($date == date('Y-m-d')) {
                                                        $timeBase = "Today " . $session;
                                                    } elseif ($date == $yes_date) {
                                                        $timeBase = "Yester day " . $session;
                                                    } else {
                                                        $timeBase = $date_f . " " . $session;
                                                    }
                                                    $profile_img = $notify['profile_img'];
                                                    if (empty($profile_img)) {
                                                        $profile_img = 'assets/img/user.jpg';
                                                    }
                                                    ?>
                                                    <li class="notification-message">
                                                        <a href="<?= base_url(); ?>notification-list">
                                                            <div class="media">
                                                                <span class="avatar avatar-sm">
                                                                    <img class="avatar-img rounded-circle" alt="User Image" src="<?= base_url() . $profile_img; ?>">
                                                                </span>
                                                                <div class="media-body">
                                                                    <p class="noti-details"> <span class="noti-title"><?= ucfirst($notify['message']); ?></span></p>
                                                                    <p class="noti-time"><span class="notification-time"><?= $timeBase; ?></span></p>
                                                                </div>
                                                            </div>
                                                        </a>
                                                    </li>
                                                    <?php
                                                }
                                            } else {
                                                ?>
                                                <li class="notification-message">
                                                    <p class="text-center text-danger mt-3"><?php echo (!empty($user_language[$user_selected]['lg_notification_empty'])) ? $user_language[$user_selected]['lg_notification_empty'] : $default_language['en']['lg_notification_empty']; ?></p>
                                                </li>
                                            <?php } ?>

                                        </ul>
                                    </div>
                                    <div class="topnav-dropdown-footer">
                                        <a href="<?= base_url(); ?>notification-list"><?php echo (!empty($user_language[$user_selected]['lg_view_notification'])) ? $user_language[$user_selected]['lg_view_notification'] : $default_language['en']['lg_view_notification']; ?></a>
                                    </div>
                                </div>
                            </li>
                            <!-- /Notifications -->

                            <?php if (!empty($this->session->userdata('id'))) { ?>
                                <!-- chat -->
                                <?php
                                $chat_token = $this->session->userdata('chat_token');
                                if (!empty($chat_token)) {
                                    $chat_detail = $this->db->where('receiver_token', $chat_token)->where('read_status=', 0)->get('chat_table')->result_array();
                                }
                                ?>
                                <li class="nav-item dropdown logged-item">

                                    <a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown">
                                        <i class="fa fa-comments" aria-hidden="true"></i>
                                        <?php if (count($chat_detail) != 0) { ?>
                                            <span class="badge badge-pill bg-yellow chat-bg-yellow"><?= count($chat_detail); ?></span>
                                        <?php } ?>
                                    </a>

                                    <div class="dropdown-menu comments-blk dropdown-menu-right notifications">
                                        <div class="topnav-dropdown-header">
                                            <span class="notification-title"><?php echo (!empty($user_language[$user_selected]['lg_chats'])) ? $user_language[$user_selected]['lg_chats'] : $default_language['en']['lg_chats']; ?></span>
                                            <a href="javascript:void(0)" class="clear-noti chat_clear_all" data-token="<?php echo $this->session->userdata('chat_token'); ?>" > <?php echo (!empty($user_language[$user_selected]['lg_clear_all'])) ? $user_language[$user_selected]['lg_clear_all'] : $default_language['en']['lg_clear_all']; ?> </a>
                                        </div>

                                        <div class="noti-content">
                                            <ul class="chat-list notification-list">
                                                <?php
                                                if (count($chat_detail) > 0) {
                                                    $sender = '';
                                                    foreach ($chat_detail as $row) {

                                                        $user_table = $this->db->select('id,name,profile_img,token,type')->
                                                                        from('users')->
                                                                        where('token', $row['sender_token'])->
                                                                        get()->row();
                                                        $provider_table = $this->db->select('id,name,profile_img,token,type')->
                                                                        from('providers')->
                                                                        where('token', $row['sender_token'])->
                                                                        get()->row();
                                                        if (!empty($user_table)) {
                                                            $user_info = $user_table;
                                                        } else {
                                                            $user_info = $provider_table;
                                                        }

                                                        $full_date = date('Y-m-d H:i:s', strtotime($row['utc_date_time']));
                                                        $date = date('Y-m-d', strtotime($full_date));
                                                        $date_f = date('d-m-Y', strtotime($full_date));
                                                        $yes_date = date('Y-m-d', (strtotime('-1 day', strtotime(date('Y-m-d')))));
                                                        $time = date('H:i', strtotime($full_date));
                                                        $session = date('h:i A', strtotime($time));
                                                        if ($date == date('Y-m-d')) {
                                                            $timeBase = "Today " . $session;
                                                        } elseif ($date == $yes_date) {
                                                            $timeBase = "Yester day " . $session;
                                                        } else {
                                                            $timeBase = $date_f . " " . $session;
                                                        }
                                                        $profile_img = $user_info->profile_img;
                                                        if (empty($profile_img)) {
                                                            $profile_img = 'assets/img/user.jpg';
                                                        }
                                                        ?>

                                                        <li class="notification-message">
                                                            <a href="<?= base_url(); ?>user-chat">
                                                                <div class="media">
                                                                    <span class="avatar avatar-sm">

                                                                        <img class="avatar-img rounded-circle" alt="User Image" src="<?= base_url() . $profile_img; ?>">
                                                                    </span>
                                                                    <div class="media-body">
                                                                        <p class="noti-details"> <span class="noti-title"><?= $user_info->name . " send a message as " . $row['message']; ?></span></p>
                                                                        <p class="noti-time"><span class="notification-time"><?= $timeBase; ?></span></p>
                                                                    </div>
                                                                </div>
                                                            </a>
                                                        </li>
                                                        <?php
                                                    }
                                                }
                                                if (count($chat_detail) == 0) {
                                                    ?>

                                                    <li class="notification-message">
                                                        <p class="text-center text-danger mt-3"><?php echo (!empty($user_language[$user_selected]['lg_empty_chats'])) ? $user_language[$user_selected]['lg_empty_chats'] : $default_language['en']['lg_empty_chats']; ?></p>
                                                    </li>
                                                <?php } ?>

                                            </ul>
                                        </div>
                                        <div class="topnav-dropdown-footer">
                                            <a href="<?= base_url(); ?>user-chat">View all Chat</a>
                                        </div>
                                    </div>
                                </li>
                                <!-- /chat -->
                            <?php } ?>
                            <!-- User Menu -->
                            <li class="nav-item dropdown has-arrow logged-item">
                                <a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown">
                                    <span class="user-img">
                                        <?php if ($user_details['profile_img'] != '') { ?>
                                            <img class="rounded-circle" src="<?php echo $base_url . $user_details['profile_img'] ?>" width="31" alt="">
                                        <?php } else { ?>
                                            <img class="rounded-circle" src="<?php echo $base_url ?>assets/img/user.jpg" alt="">
                                        <?php } ?>
                                    </span>
                                </a>
                                <div class="dropdown-menu dropdown-menu-right">
                                    <div class="user-header">
                                        <div class="avatar avatar-sm">
                                            <?php if ($user_details['profile_img'] != '') { ?>
                                                <img class="avatar-img rounded-circle" src="<?php echo $base_url . $user_details['profile_img'] ?>" alt="">
                                            <?php } else { ?>
                                                <img class="avatar-img rounded-circle" src="<?php echo $base_url ?>assets/img/user.jpg" alt="">
                                            <?php } ?>
                                        </div>
                                        <div class="user-text">
                                            <h6><?php echo $user_details['name']; ?></h6>
                                            <p class="text-muted mb-0">Provider</p>
                                        </div>
                                    </div>
                                    <a class="dropdown-item" href="<?php echo base_url(); ?>provider-dashboard">Dashboard</a>
                                    <a class="dropdown-item" href="<?php echo base_url(); ?>my-services">My Services</a>
                                    <a class="dropdown-item" href="<?php echo base_url(); ?>provider-bookings">Booking List</a>
                                    <a class="dropdown-item" href="<?php echo base_url(); ?>provider-settings">Profile Settings</a>
                                    <a class="dropdown-item" href="<?php echo base_url(); ?>provider-wallet">Wallet</a>
                                    <a class="dropdown-item" href="<?php echo base_url() ?>provider-subscription">Subscription</a>
                                    <a class="dropdown-item" href="<?php echo base_url() ?>provider-availability">Availability</a>
									<?php 
									$query = $this->db->query("select * from system_settings WHERE status = 1");
									$result = $query->result_array();
									
									$login_type='';
									foreach ($result as $res) {
										
										if($res['key'] == 'login_type'){
											$login_type = $res['value'];
										}
										
										if($res['key'] == 'login_type'){
											$login_type = $res['value'];
										}

									}
										if($login_type=='email'){
										?>
                                    <a class="dropdown-item" href="<?php echo base_url() ?>provider-change-password">Change Password</a>
									
										<?php } ?>
                                    <a class="dropdown-item" href="<?php echo base_url() ?>user-chat">Chat</a>
                                    <a class="dropdown-item" href="<?php echo base_url() ?>logout">Logout</a>
                                </div>
                            </li>
                            <!-- /User Menu -->

                        <?php } elseif ($this->session->userdata('usertype') == 'user') { ?>
                            <!-- Notifications -->
                            <li class="nav-item dropdown logged-item">
                                <?php
                                if (!empty($this->session->userdata('chat_token'))) {
                                    $ses_token = $this->session->userdata('chat_token');
                                } else {
                                    $ses_token = '';
                                }
                                if (!empty($ses_token)) {
                                    $ret = $this->db->select('*')->
                                                    from('notification_table')->
                                                    where('receiver', $ses_token)->
                                                    where('status', 1)->
                                                    order_by('notification_id', 'DESC')->
                                                    get()->result_array();
                                    $notification = [];
                                    if (!empty($ret)) {
                                        foreach ($ret as $key => $value) {
                                            $user_table = $this->db->select('id,name,profile_img,token,type')->
                                                            from('users')->
                                                            where('token', $value['sender'])->
                                                            get()->row();
                                            $provider_table = $this->db->select('id,name,profile_img,token,type')->
                                                            from('providers')->
                                                            where('token', $value['sender'])->
                                                            get()->row();
                                            if (!empty($user_table)) {
                                                $user_info = $user_table;
                                            } else {
                                                $user_info = $provider_table;
                                            }
                                            $notification[$key]['name'] = !empty($user_info->name) ? $user_info->name : '';
                                            $notification[$key]['message'] = !empty($value['message']) ? $value['message'] : '';
                                            $notification[$key]['profile_img'] = !empty($user_info->profile_img) ? $user_info->profile_img : '';
                                            $notification[$key]['utc_date_time'] = !empty($value['utc_date_time']) ? $value['utc_date_time'] : '';
                                        }
                                    }
                                    $n_count = count($notification);
                                } else {
                                    $n_count = 0;
                                    $notification = [];
                                }

                                /* notification Count */
                                if (!empty($n_count) && $n_count != 0) {
                                    $notify = "<span class='badge badge-pill bg-yellow'>" . $n_count . "</span>";
                                } else {
                                    $notify = "";
                                }
                                ?>

                                <a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown">
                                    <i class="fas fa-bell"></i> <?= $notify; ?>
                                </a>
                                <div class="dropdown-menu dropdown-menu-right notifications">
                                    <div class="topnav-dropdown-header">
                                        <span class="notification-title">Notifications</span>
                                        <a href="javascript:void(0)" class="clear-noti noty_clear" data-token="<?php echo $this->session->userdata('chat_token'); ?>" > Clear All </a>
                                    </div>
                                    <div class="noti-content">
                                        <ul class="notification-list">
                                            <?php
                                            if (!empty($notification)) {
                                                foreach ($notification as $key => $notify) {
                                                    $full_date = date('Y-m-d H:i:s', strtotime($notify['utc_date_time']));
                                                    $date = date('Y-m-d', strtotime($full_date));
                                                    $date_f = date('d-m-Y', strtotime($full_date));
                                                    $yes_date = date('Y-m-d', (strtotime('-1 day', strtotime(date('Y-m-d')))));
                                                    $time = date('H:i', strtotime($full_date));
                                                    $session = date('h:i A', strtotime($time));
                                                    if ($date == date('Y-m-d')) {
                                                        $timeBase = "Today " . $session;
                                                    } elseif ($date == $yes_date) {
                                                        $timeBase = "Yester day " . $session;
                                                    } else {
                                                        $timeBase = $date_f . " " . $session;
                                                    }
                                                    $profile_img = $notify['profile_img'];
                                                    if (empty($profile_img)) {
                                                        $profile_img = 'assets/img/user.jpg';
                                                    }
                                                    ?>

                                                    <li class="notification-message">
                                                        <a href="<?= base_url(); ?>notification-list">
                                                            <div class="media">
                                                                <span class="avatar avatar-sm">
                                                                    <img class="avatar-img rounded-circle" alt="User Image" src="<?= base_url() . $profile_img; ?>">
                                                                </span>
                                                                <div class="media-body">
                                                                    <p class="noti-details"> <span class="noti-title"><?= ucfirst($notify['message']); ?></span></p>
                                                                    <p class="noti-time"><span class="notification-time"><?= $timeBase; ?></span></p>
                                                                </div>
                                                            </div>
                                                        </a>
                                                    </li>
                                                    <?php
                                                }
                                            } else {
                                                ?>
                                                <li class="notification-message">
                                                    <p class="text-center text-danger mt-3">Notification Empty</p>
                                                </li>
                                            <?php } ?>
                                        </ul>
                                    </div>
                                    <div class="topnav-dropdown-footer">
                                        <a href="<?= base_url(); ?>notification-list">View all Notifications</a>
                                    </div>
                                </div>
                            </li>
                            <!-- /Notifications -->

                            <?php if (!empty($this->session->userdata('id'))) { ?>
                                <!-- chat -->
                                <?php
                                $chat_token = $this->session->userdata('chat_token');
                                if (!empty($chat_token)) {
                                    $chat_detail = $this->db->where('receiver_token', $chat_token)->where('read_status=', 0)->get('chat_table')->result_array();
                                }
                                ?>
                                <li class="nav-item dropdown logged-item">

                                    <a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown">
                                        <i class="fa fa-comments" aria-hidden="true"></i>
                                        <?php if (count($chat_detail) != 0) { ?>
                                            <span class="badge badge-pill bg-yellow chat-bg-yellow"><?= count($chat_detail); ?></span>
                                        <?php } ?>
                                    </a>

                                    <div class="dropdown-menu comments-blk dropdown-menu-right notifications">
                                        <div class="topnav-dropdown-header">
                                            <span class="notification-title">Chats</span>
                                            <a href="javascript:void(0)" class="clear-noti chat_clear_all" data-token="<?php echo $this->session->userdata('chat_token'); ?>" > Clear All </a>
                                        </div>

                                        <div class="noti-content">
                                            <ul class="chat-list notification-list">
                                                <?php
                                                if (count($chat_detail) > 0) {
                                                    $sender = '';
                                                    foreach ($chat_detail as $row) {

                                                        $user_table = $this->db->select('id,name,profile_img,token,type')->
                                                                        from('users')->
                                                                        where('token', $row['sender_token'])->
                                                                        get()->row();
                                                        $provider_table = $this->db->select('id,name,profile_img,token,type')->
                                                                        from('providers')->
                                                                        where('token', $row['sender_token'])->
                                                                        get()->row();
                                                        if (!empty($user_table)) {
                                                            $user_info = $user_table;
                                                        } else {
                                                            $user_info = $provider_table;
                                                        }

                                                        $full_date = date('Y-m-d H:i:s', strtotime($row['utc_date_time']));
                                                        $date = date('Y-m-d', strtotime($full_date));
                                                        $date_f = date('d-m-Y', strtotime($full_date));
                                                        $yes_date = date('Y-m-d', (strtotime('-1 day', strtotime(date('Y-m-d')))));
                                                        $time = date('H:i', strtotime($full_date));
                                                        $session = date('h:i A', strtotime($time));
                                                        if ($date == date('Y-m-d')) {
                                                            $timeBase = "Today " . $session;
                                                        } elseif ($date == $yes_date) {
                                                            $timeBase = "Yester day " . $session;
                                                        } else {
                                                            $timeBase = $date_f . " " . $session;
                                                        }
                                                        $profile_img = $user_info->profile_img;
                                                        if (empty($profile_img)) {
                                                            $profile_img = 'assets/img/user.jpg';
                                                        }
                                                        ?>

                                                        <li class="notification-message">
                                                            <a href="<?= base_url(); ?>user-chat">
                                                                <div class="media">
                                                                    <span class="avatar avatar-sm">

                                                                        <img class="avatar-img rounded-circle" alt="User Image" src="<?= base_url() . $profile_img; ?>">
                                                                    </span>
                                                                    <div class="media-body">
                                                                        <p class="noti-details"> <span class="noti-title"><?= $user_info->name . " send a message as " . $row['message']; ?></span></p>
                                                                        <p class="noti-time"><span class="notification-time"><?= $timeBase; ?></span></p>
                                                                    </div>
                                                                </div>
                                                            </a>
                                                        </li>
                                                        <?php
                                                    }
                                                }
                                                if (count($chat_detail) == 0) {
                                                    ?>

                                                    <li class="notification-message">
                                                        <p class="text-center text-danger mt-3">Chat Empty</p>
                                                    </li>
                                                <?php } ?>

                                            </ul>
                                        </div>
                                        <div class="topnav-dropdown-footer">
                                            <a href="<?= base_url(); ?>user-chat">View all Chat</a>
                                        </div>
                                    </div>
                                </li>
                                <!-- /chat -->
                            <?php } ?>
                            <li class="nav-item dropdown has-arrow logged-item">
                                <a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown">
                                    <span class="user-img">
                                        <?php if ($user_details['profile_img'] != '') { ?>
                                            <img class="rounded-circle" src="<?php echo $base_url . $user_details['profile_img'] ?>" alt="">
                                        <?php } else { ?>
                                            <img class="rounded-circle" src="<?php echo $base_url ?>assets/img/user.jpg" alt="">
                                        <?php } ?>
                                    </span>
                                </a>
                                <div class="dropdown-menu dropdown-menu-right">
                                    <div class="user-header">
                                        <div class="avatar avatar-sm">
                                            <?php if ($user_details['profile_img'] != '') { ?>
                                                <img class="avatar-img rounded-circle" src="<?php echo $base_url . $user_details['profile_img'] ?>" alt="">
                                            <?php } else { ?>
                                                <img class="avatar-img rounded-circle" src="<?php echo $base_url ?>assets/img/user.jpg" alt="">
                                            <?php } ?>
                                        </div>
                                        <div class="user-text">
                                            <h6><?php echo $user_details['name']; ?></h6>
                                            <p class="text-muted mb-0">User</p>
                                        </div>
                                    </div>
                                    <a class="dropdown-item" href="<?php echo base_url(); ?>user-dashboard">Dashboard</a>
                                    <a class="dropdown-item" href="<?php echo base_url(); ?>user-bookings">My Bookings</a>
                                    <a class="dropdown-item" href="<?php echo base_url(); ?>user-settings">Profile Settings</a>
                                    <a class="dropdown-item" href="<?php echo base_url() ?>all-services">Book Services</a>
									
									<?php 
									$query = $this->db->query("select * from system_settings WHERE status = 1");
									$result = $query->result_array();
									
									$login_type='';
									foreach ($result as $res) {
										
										if($res['key'] == 'login_type'){
											$login_type = $res['value'];
										}
										
										if($res['key'] == 'login_type'){
											$login_type = $res['value'];
										}

									}
										if($login_type=='email'){
										?>
                                    <a class="dropdown-item" href="<?php echo base_url() ?>change-password">Change Password</a>
									
										<?php } ?>
                                    <a class="dropdown-item" href="<?php echo base_url() ?>user-chat">Chat</a>
                                    <a class="dropdown-item" href="<?php echo base_url() ?>logout">Logout</a>
                                </div>
                            </li>
                            <?php
                        }
                    }
                    ?>
                </ul>
            </nav>
        </header>

        <script>

        </script>

