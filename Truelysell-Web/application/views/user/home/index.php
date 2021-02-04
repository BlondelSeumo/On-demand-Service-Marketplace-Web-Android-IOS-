<?php
$this->db->from('services');
$services_count = $this->db->count_all_results();
$this->db->from('services');
$this->db->order_by('total_views', 'DESC');
$this->db->limit(3);
$popular = $this->db->get()->result_array();

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
<section class="hero-section">
    <div class="layer">
        <div class="home-banner"></div>	
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-12">
                    <div class="section-search">
                        <h3><?php echo (!empty($user_language[$user_selected]['lg_world_largest'])) ? $user_language[$user_selected]['lg_world_largest'] : $default_language['en']['lg_world_largest']; ?> <span><?php echo (!empty($user_language[$user_selected]['lg_marget_place'])) ? $user_language[$user_selected]['lg_marget_place'] : $default_language['en']['lg_marget_place']; ?></span></h3>
                        <p><?php echo (!empty($user_language[$user_selected]['lg_search_from'])) ? $user_language[$user_selected]['lg_search_from'] : $default_language['en']['lg_search_from']; ?> <?php echo $services_count ?> <?php echo (!empty($user_language[$user_selected]['lg_awesome_verified'])) ? $user_language[$user_selected]['lg_awesome_verified'] : $default_language['en']['lg_awesome_verified']; ?> </p>
                        <div class="search-box">
                            <form action="<?php echo base_url(); ?>search" id="search_service" method="post">
                                <input type="hidden" name="<?php echo $this->security->get_csrf_token_name(); ?>" value="<?php echo $this->security->get_csrf_hash(); ?>" />
                                <div class="search-input line">
                                    <i class="fas fa-tv bficon"></i>
                                    <div class="form-group mb-0">
                                        <input type="text" class="form-control common_search" name="common_search" id="search-blk" placeholder="What are you looking for?" >
                                    </div>
                                </div>
                                <div class="search-input">
                                    <i class="fas fa-location-arrow bficon"></i>
                                    <div class="form-group mb-0">
                                        <input type="text" class="form-control" value="" name="user_address" id="user_address" placeholder="Your Location">
                                        <input type="hidden" value="" name="user_latitude" id="user_latitude">
                                        <input type="hidden" value="" name="user_longitude" id="user_longitude">
                                        <a class="current-loc-icon current_location" data-id="1" href="javascript:void(0);"><i class="fas fa-crosshairs"></i></a>
                                    </div>
                                </div>
                                <div class="search-btn">
                                    <button class="btn search_service" name="search" value="search"  type="button"><?php echo (!empty($user_language[$user_selected]['lg_search'])) ? $user_language[$user_selected]['lg_search'] : $default_language['en']['lg_search']; ?></button>
                                </div>
                            </form>
                        </div>
                        <div class="search-cat">
                            <i class="fas fa-circle"></i>
                            <span><?php echo (!empty($user_language[$user_selected]['lg_popular_search'])) ? $user_language[$user_selected]['lg_popular_search'] : $default_language['en']['lg_popular_search']; ?></span>
                            <?php foreach ($popular as $popular_services) { ?>
                                <a href="<?php echo base_url() . 'service-preview/' . str_replace(' ', '-', $popular_services['service_title']) . '?sid=' . md5($popular_services['id']); ?>">
                                    <?php echo $popular_services['service_title'] ?>
                                </a>
                            <?php } ?>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<section class="category-section">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="row">
                    <div class="col-md-6">
                        <div class="heading">
                            <h2><?php echo (!empty($user_language[$user_selected]['lg_Featured_Categories'])) ? $user_language[$user_selected]['lg_Featured_Categories'] : $default_language['en']['lg_Featured_Categories']; ?></h2>
                            <span><?php echo (!empty($user_language[$user_selected]['lg_What_do_you'])) ? $user_language[$user_selected]['lg_What_do_you'] : $default_language['en']['lg_What_do_you']; ?></span>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="viewall">
                            <h4><a href="<?php echo base_url(); ?>all-categories"><?php echo (!empty($user_language[$user_selected]['lg_View_All'])) ? $user_language[$user_selected]['lg_View_All'] : $default_language['en']['lg_View_All']; ?> <i class="fas fa-angle-right"></i></a></h4>
                            <span><?php echo (!empty($user_language[$user_selected]['lg_Featured_Categories'])) ? $user_language[$user_selected]['lg_Featured_Categories'] : $default_language['en']['lg_Featured_Categories']; ?></span>
                        </div>
                    </div>
                </div>						
                <div class="catsec">
                    <div class="row">
                        <?php
                        if (!empty($category)) {
                            foreach ($category as $crows) {
                                ?>
                                <div class="col-lg-4 col-md-6">
                                    <a href="<?php echo base_url(); ?>search/<?php echo str_replace(' ', '-', strtolower($crows['category_name'])); ?>">
                                        <div class="cate-widget">
                                            <img src="<?php echo base_url() . $crows['category_image']; ?>" alt="">
                                            <div class="cate-title">
                                                <h3><span><i class="fas fa-circle"></i> <?php echo ucfirst($crows['category_name']); ?></span></h3>
                                            </div>
                                            <div class="cate-count">
                                                <i class="fas fa-clone"></i> <?php echo $crows['category_count']; ?>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                                <?php
                            }
                        } else {
                            echo '<div class="col-lg-12">
								<div class="category">
									No Categories Found
								</div>
							</div>';
                        }
                        ?>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<section class="popular-services">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="row">
                    <div class="col-md-6">
                        <div class="heading">
                            <h2><?php echo (!empty($user_language[$user_selected]['lg_Most_Popular_Services'])) ? $user_language[$user_selected]['lg_Most_Popular_Services'] : $default_language['en']['lg_Most_Popular_Services']; ?></h2>
                            <span><?php echo (!empty($user_language[$user_selected]['lg_exlore_greates'])) ? $user_language[$user_selected]['lg_exlore_greates'] : $default_language['en']['lg_exlore_greates']; ?></span>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="viewall">
                            <h4><a href="<?php echo base_url(); ?>all-services"><?php echo (!empty($user_language[$user_selected]['lg_View_All'])) ? $user_language[$user_selected]['lg_View_All'] : $default_language['en']['lg_View_All']; ?> <i class="fas fa-angle-right"></i></a></h4>
                            <span><?php echo (!empty($user_language[$user_selected]['lg_Most_Popular'])) ? $user_language[$user_selected]['lg_Most_Popular'] : $default_language['en']['lg_Most_Popular']; ?></span>
                        </div>
                    </div>
                </div>
                <div class="service-carousel">
                    <div class="service-slider owl-carousel owl-theme">

                        <?php
                        if (!empty($services)) {
                            foreach ($services as $srows) {

//                                print_r($srows);exit;



                                $this->db->select("service_image");
                                $this->db->from('services_image');
                                $this->db->where("service_id", $srows['id']);
                                $this->db->where("status", 1);
                                $image = $this->db->get()->row_array();

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
                                    } $user_currency_code = $user_currency['user_currency_code'];

                                    $service_amount = get_gigs_currency($srows['service_amount'], $srows['currency_code'], $user_currency_code);
                                } else {
                                    $user_currency_code = settings('currency');
                                    $service_currency_code = $srows['currency_code'];
                                    $service_amount = get_gigs_currency($srows['service_amount'], $srows['currency_code'], $user_currency_code);
                                }
//                               
                                ?>
                                <div class="service-widget">
                                    <div class="service-img">
                                        <a href="<?php echo base_url() . 'service-preview/' . str_replace(' ', '-', $srows['service_title']) . '?sid=' . md5($srows['id']); ?>">
                                            <img class="img-fluid serv-img" alt="Service Image" src="<?php echo base_url() . $image['service_image']; ?>">
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
                                            <div class="cate-list">
                                                <a class="bg-yellow" href="<?php echo base_url() . 'search/' . str_replace(' ', '-', strtolower($srows['category_name'])); ?>"><?php echo ucfirst($srows['category_name']); ?></a></div>
                                        </div>
                                    </div>
                                    <div class="service-content">
                                        <h3 class="title">
                                            <a href="<?php echo base_url() . 'service-preview/' . str_replace(' ', '-', $srows['service_title']) . '?sid=' . md5($srows['id']); ?>"><?php echo ucfirst($srows['service_title']); ?></a>
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
                                <?php
                            }
                        } else {

                            echo '<div>	
									<p class="mb-0">
										No Services Found
									</p>
								</div>';
                        }
                        ?>
                    </div> 
                </div>
            </div>
        </div>
    </div>
</section>

<section class="how-work">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="heading howitworks">
                    <h2><?php echo (!empty($user_language[$user_selected]['lg_How_Works'])) ? $user_language[$user_selected]['lg_How_Works'] : $default_language['en']['lg_How_Works']; ?></h2>
                    <span><?php echo (!empty($user_language[$user_selected]['lg_aliquam'])) ? $user_language[$user_selected]['lg_aliquam'] : $default_language['en']['lg_aliquam']; ?></span>
                </div>
                <div class="howworksec">
                    <div class="row">
                        <div class="col-lg-4">
                            <div class="howwork">
                                <div class="iconround">
                                    <div class="steps">01</div>
                                    <img src="<?php echo base_url(); ?>assets/img/icon-1.png">
                                </div>
                                <h3><?php echo (!empty($user_language[$user_selected]['lg_choose_what'])) ? $user_language[$user_selected]['lg_choose_what'] : $default_language['en']['lg_choose_what']; ?></h3>
                                <p><?php echo (!empty($user_language[$user_selected]['lg_dapibus'])) ? $user_language[$user_selected]['lg_dapibus'] : $default_language['en']['lg_dapibus']; ?></p>
                            </div>
                        </div>
                        <div class="col-lg-4">
                            <div class="howwork">
                                <div class="iconround">
                                    <div class="steps">02</div>
                                    <img src="<?php echo base_url(); ?>assets/img/icon-2.png">
                                </div>
                                <h3><?php echo (!empty($user_language[$user_selected]['lg_find_what'])) ? $user_language[$user_selected]['lg_find_what'] : $default_language['en']['lg_find_what']; ?></h3>
                                <p><?php echo (!empty($user_language[$user_selected]['lg_dapibus'])) ? $user_language[$user_selected]['lg_dapibus'] : $default_language['en']['lg_dapibus']; ?></p>
                            </div>
                        </div>
                        <div class="col-lg-4">
                            <div class="howwork">
                                <div class="iconround">
                                    <div class="steps">03</div>
                                    <img src="<?php echo base_url(); ?>assets/img/icon-3.png">
                                </div>
                                <h3><?php echo (!empty($user_language[$user_selected]['lg_Amazing_Places'])) ? $user_language[$user_selected]['lg_Amazing_Places'] : $default_language['en']['lg_Amazing_Places']; ?></h3>
                                <p><?php echo (!empty($user_language[$user_selected]['lg_amesing_3'])) ? $user_language[$user_selected]['lg_amesing_3'] : $default_language['en']['lg_amesing_3']; ?></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>