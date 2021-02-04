package com.dreamguys.truelysell;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.ProgressDlg;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webview;
    String url = "", title = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        url = getIntent().getStringExtra(AppConstants.URL);
        title = getIntent().getStringExtra(AppConstants.TbTitle);
        setToolBarTitle(title);
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        ProgressDlg.showProgressDialog(WebViewActivity.this, null, null);

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    uri = request.getUrl();
                }
                view.loadUrl(uri.toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ProgressDlg.dismissProgressDialog();
            }
        });
        webview.loadUrl(url);

    }
}
