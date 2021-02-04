<?php
$this->db->select_min('service_amount');
$this->db->from('services');
$min_price = $this->db->get()->row_array();

$this->db->select_max('service_amount');
$this->db->from('services');
$max_price = $this->db->get()->row_array();

$currency = currency_conversion(settings('currency'));

$query = $this->db->query("select * from system_settings WHERE status = 1");
$result = $query->result_array();
if (!empty($result)) {
    foreach ($result as $data) {
        if ($data['key'] == 'currency_option') {
            $currency_option = $data['value'];
        }
    }
}
?>

<div class="breadcrumb-bar">
    <div class="container-fluid">
        <div class="row">
            <div class="col">
                <div class="breadcrumb-title">
                    <h2><?php echo (!empty($user_language[$user_selected]['lg_Find_Professional'])) ? $user_language[$user_selected]['lg_Find_Professional'] : $default_language['en']['lg_Find_Professional']; ?></h2>
                </div>
            </div>
            <div class="col-auto float-right ml-auto breadcrumb-menu">
                <nav aria-label="breadcrumb" class="page-breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="<?php echo base_url(); ?>"><?php echo (!empty($user_language[$user_selected]['lg_home'])) ? $user_language[$user_selected]['lg_home'] : $default_language['en']['lg_home']; ?></a></li>
                        <li class="breadcrumb-item active" aria-current="page"><?php echo (!empty($user_language[$user_selected]['lg_Find_Professional'])) ? $user_language[$user_selected]['lg_Find_Professional'] : $default_language['en']['lg_Find_Professional']; ?></li>
                    </ol>
                </nav>
            </div>
        </div>
    </div>
</div>

<div class="content">
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-3 theiaStickySidebar">
                <div class="card filter-card">
                    <div class="card-body">
                        <h4 class="card-title mb-4"><?php echo (!empty($user_language[$user_selected]['lg_Search_Filter'])) ? $user_language[$user_selected]['lg_Search_Filter'] : $default_language['en']['lg_Search_Filter']; ?></h4>
                        <form id="search_form">

                            <div class="filter-widget">
                                <div class="filter-list">
                                    <h4 class="filter-title"><?php echo (!empty($user_language[$user_selected]['lg_Keyword'])) ? $user_language[$user_selected]['lg_Keyword'] : $default_language['en']['lg_Keyword']; ?></h4>
                                    <input type="text" id="common_search" value="<?php if (isset($_POST["common_search"]) && !empty($_POST["common_search"])) echo $_POST["common_search"]; ?>" class="form-control common_search" placeholder="What are you looking for?" />
                                </div>
                                <div class="filter-list">
                                    <h4 class="filter-title"><?php echo (!empty($user_language[$user_selected]['lg_Sort_By'])) ? $user_language[$user_selected]['lg_Sort_By'] : $default_language['en']['lg_Sort_By']; ?></h4>
                                    <select id="sort_by" class="form-control selectbox select">
                                        <option value=""><?php echo (!empty($user_language[$user_selected]['lg_Sort_By'])) ? $user_language[$user_selected]['lg_Sort_By'] : $default_language['en']['lg_Sort_By']; ?></option>
                                        <option value="1"><?php echo (!empty($user_language[$user_selected]['lg_Price_Low_High'])) ? $user_language[$user_selected]['lg_Price_Low_High'] : $default_language['en']['lg_Price_Low_High']; ?></option>
                                        <option value="2"><?php echo (!empty($user_language[$user_selected]['lg_Price_High_Low'])) ? $user_language[$user_selected]['lg_Price_High_Low'] : $default_language['en']['lg_Price_High_Low']; ?></option>
                                        <option value="3"><?php echo (!empty($user_language[$user_selected]['lg_Newest'])) ? $user_language[$user_selected]['lg_Newest'] : $default_language['en']['lg_Newest']; ?></option>
                                    </select>
                                </div>
                                <div class="filter-list">
                                    <h4 class="filter-title"><?php echo (!empty($user_language[$user_selected]['lg_category_name'])) ? $user_language[$user_selected]['lg_category_name'] : $default_language['en']['lg_category_name']; ?></h4>
                                    <select id="categories" class="form-control form-control selectbox select">
                                        <option value=""><?php echo (!empty($user_language[$user_selected]['lg_all_categories'])) ? $user_language[$user_selected]['lg_all_categories'] : $default_language['en']['lg_all_categories']; ?></option>
                                        <?php
                                        foreach ($category as $crows) {
                                            $selected = '';
                                            if (isset($category_id) && !empty($category_id)) {
                                                if ($crows['id'] == $category_id) {
                                                    $selected = 'selected';
                                                }
                                            }
                                            echo'<option value="' . $crows['id'] . '" ' . $selected . '>' . $crows['category_name'] . '</option>';
                                        }
                                        ?>
                                    </select>
                                </div>
                                <div class="filter-list">
                                    <h4 class="filter-title">Location</h4>
                                    <input class="form-control" type="text" id="service_location" value="<?php if (isset($_POST["user_address"]) && !empty($_POST["user_address"])) echo $_POST["user_address"]; ?>" placeholder="Search Location" name="user_address" >
                                    <input type="hidden" value="<?php if (isset($_POST["user_latitude"]) && !empty($_POST["user_latitude"])) echo $_POST["user_latitude"]; ?>" id="service_latitude">
                                    <input type="hidden" value="<?php if (isset($_POST["user_longitude"]) && !empty($_POST["user_longitude"])) echo $_POST["user_longitude"]; ?>" id="service_longitude">
                                </div>
                                <div class="filter-list">
                                    <h4 class="filter-title"><?php echo (!empty($user_language[$user_selected]['lg_Price_Range'])) ? $user_language[$user_selected]['lg_Price_Range'] : $default_language['en']['lg_Price_Range']; ?></h4>
                                    <div class="price-ranges">
                                        <?php echo currency_conversion(settings('currency')); ?><span class="from d-inline-block" id="min_price"><?php echo $min_price['service_amount'] ?></span> -
                                        <?php echo currency_conversion(settings('currency')); ?><span class="to d-inline-block" id="max_price"><?php echo $max_price['service_amount'] ?></span>
                                    </div>	
                                    <div class="range-slider price-range"></div>										
                                </div>
                            </div>
                            <button class="btn btn-primary pl-5 pr-5 btn-block get_services" type="button"><?php echo (!empty($user_language[$user_selected]['lg_search'])) ? $user_language[$user_selected]['lg_search'] : $default_language['en']['lg_search']; ?></button>
                        </form>	
                    </div>
                </div>
            </div>
            <div class="col-lg-9">

                <div class="row align-items-center mb-4">
                    <div class="col-md-6 col">
                        <h4><span id="service_count"><?php echo $count; ?></span> <?php echo (!empty($user_language[$user_selected]['lg_Services_Found'])) ? $user_language[$user_selected]['lg_Services_Found'] : $default_language['en']['lg_Services_Found']; ?></h4>
                    </div>
                    <div class="col-md-6 col-auto">
                        <div class="view-icons ">
                            <a href="javascript:void(0);" class="grid-view active"><i class="fas fa-th-large"></i></a>
                        </div>

                    </div>
                </div>
                <div>
                    <div class="row" id="dataList">

                        <?php
                        if (!empty($service)) {

                            foreach ($service as $srows) {

                                $serviceimage = explode(',', $srows['service_image']);

                                $serviceimages = $this->db->where('service_id', $srows['id'])->get('services_image')->row_array();

                                $provider_details = $this->db->where('id', $srows['user_id'])->get('providers')->row_array();

                                $this->db->select('AVG(rating)');
                                $this->db->where(array('service_id' => $srows['id'], 'status' => 1));
                                $this->db->from('rating_review');
                                $rating = $this->db->get()->row_array();
                                $avg_rating = round($rating['AVG(rating)'], 1);


                                $user_currency_code = '';
                                $userId = $this->session->userdata('id');
                                If (!empty($userId)) {
                                    $service_amount = $srows['service_amount'];
                                    $type = $this->session->userdata('usertype');
                                    if ($type == 'user') {
                                        $user_currency = get_user_currency();
                                    } else if ($type == 'provider') {
                                        $user_currency = get_provider_currency();
                                    }
                                    $user_currency_code = $user_currency['user_currency_code'];

                                    $service_amount = get_gigs_currency($srows['service_amount'], $srows['currency_code'], $user_currency_code);
                                } else {
                                    $user_currency_code = settings('currency');
                                    $service_currency_code = $srows['currency_code'];
                                    $service_amount = get_gigs_currency($srows['service_amount'], $srows['currency_code'], $user_currency_code);
                                }
                                ?>
                                <div class="col-lg-4 col-md-6">
                                    <div class="service-widget">
                                        <div class="service-img">
                                            <a href="<?php echo base_url() . 'service-preview/' . str_replace(' ', '-', strtolower($srows['service_title'])) . '?sid=' . md5($srows['id']); ?>">
                                                <?php if (!empty($serviceimages['service_image'])) { ?>
                                                    <img class="img-fluid serv-img" alt="Service Image" src="<?php echo base_url() . $serviceimages['service_image']; ?>">
                                                <?php } else { ?>
                                                    <img class="img-fluid serv-img" alt="Service Image" src="">
                                                <?php } ?>
                                            </a>
                                            <div class="item-info">
                                                <div class="service-user">
                                                    <a href="#">
                                                        <?php if ($provider_details['profile_img'] == '') { ?>
                                                            <img src="<?php echo base_url(); ?>assets/img/user.jpg">
                                                        <?php } else { ?>
                                                            <img src="<?php echo base_url() . $provider_details['profile_img'] ?>">
                                                        <?php } ?>
                                                    </a>
                                                    <span class="service-price"><?php echo currency_conversion($user_currency_code) . $service_amount; ?></span>
                                                </div>
                                                <div class="cate-list"> <a class="bg-yellow" href="<?php echo base_url() . 'search/' . str_replace(' ', '-', strtolower($srows['category_name'])); ?>"><?php echo ucfirst($srows['category_name']); ?></a></div>
                                            </div>
                                        </div>
                                        <div class="service-content">
                                            <h3 class="title">
                                                <a href="<?php echo base_url() . 'service-preview/' . str_replace(' ', '-', strtolower($srows['service_title'])) . '?sid=' . md5($srows['id']); ?>"><?php echo ucfirst($srows['service_title']); ?></a>
                                            </h3>
                                            <div class="rating">
                                                <?php
                                                for ($x = 1; $x <= $avg_rating; $x++) {
                                                    echo '<i class="fas fa-star filled"></i>';
                                                }
                                                if (strpos($avg_rating, '.')) {
                                                    echo '<i class="fas fa-star"></i>';
                                                    $x++;
                                                }
                                                while ($x <= 5) {
                                                    echo '<i class="fas fa-star"></i>';
                                                    $x++;
                                                }
                                                ?>
                                                <span class="d-inline-block average-rating">(<?php echo $avg_rating ?>)</span>
                                            </div>
                                            <div class="user-info">

                                                <div class="row">
                                                    <?php if ($this->session->userdata('id') != '') {
                                                        ?>
                                                        <span class="col ser-contact"><i class="fas fa-phone mr-1"></i> <span>xxxxxxxx<?= rand(00, 99) ?></span></span>
                                                    <?php } else { ?>
                                                        <span class="col ser-contact"><i class="fas fa-phone mr-1"></i> <span>xxxxxxxx<?= rand(00, 99) ?></span></span>
                                                    <?php } ?>

                                                    <span class="col ser-location"><span><?php echo ucfirst($srows['service_location']); ?></span> <i class="fas fa-map-marker-alt ml-1"></i></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                                <?php
                            }
                        } else {

                            echo '<div class="col-lg-12">
									<p class="mb-0">
										No Services Found
									</p>
								</div>';
                        }

                        echo $this->ajax_pagination->create_links();
                        ?>



                    </div>
                </div>

            </div>					
        </div>
    </div>
</div>

