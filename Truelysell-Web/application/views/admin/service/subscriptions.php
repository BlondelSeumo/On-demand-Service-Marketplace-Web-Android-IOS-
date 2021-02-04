<div class="page-wrapper">
    <div class="content container-fluid">

        <!-- Page Header -->
        <div class="page-header">
            <div class="row">
                <div class="col">
                    <h3 class="page-title">Subscriptions</h3>
                </div>
                <div class="col-auto text-right">
                    <a href="<?php echo $base_url; ?>add-subscription" class="btn btn-primary add-button">
                        <i class="fas fa-plus"></i>
                    </a>
                </div>
            </div>
        </div>
        <!-- /Page Header -->


        <div class="row pricing-box">

            <?php
            if (!empty($list)) {


                foreach ($list as $subscription) {

                    $str = $subscription['fee_description'];
                    $description = (explode(" ", $str));
                    $description = $description[1];

                    switch ($description) {
                        case "Month":
                            $drt= "Monthly";
                            break;
                        case "Months":
                            $drt= "Monthly";
                            break;
                        case "Year":
                            $drt= "Yearly";
                            break;
                        case "Years":
                            $drt= "Yearly";
                            break;
                        default:
                            $drt= "Monthly";
                    }
                    ?>
                    <div class="col-md-6 col-lg-4 col-xl-3">
                        <div class="card">
                            <div class="card-body">
                                <div class="pricing-header">
                                    <h2><?php echo $subscription['subscription_name']; ?></h2>
                                    <p><?php echo $drt; ?> Price</p>
                                </div>              
                                <div class="pricing-card-price">
                                    <h3 class="heading2 price"><?php echo currency_code_sign(settings('currency')).$subscription['fee']; ?></h3>
                                    <p>Duration: <span><?php echo $subscription['fee_description']; ?></span></p>
                                </div>
                                <ul class="pricing-options">
                                    <li><i class="far fa-check-circle"></i> One listing submission</li>
                                    <li><i class="far fa-check-circle"></i> <?php echo $subscription['fee_description']; ?> expiration</li>
                                </ul>

                                <a href="<?php echo $base_url . 'edit-subscription/' . $subscription['id']; ?>" class="btn btn-primary btn-block">Edit</a>

                            </div>
                        </div>
                    </div>
                    <?php
                }
            }
            ?>
        </div>
    </div>
</div>