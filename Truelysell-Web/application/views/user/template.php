<?php
    $this->load->view($theme . '/includes/header');
    $this->load->view($theme . '/includes/navbar');
    $this->load->view($theme . '/'.$module . '/' . $page);
    $this->load->view($theme . '/includes/footer');
