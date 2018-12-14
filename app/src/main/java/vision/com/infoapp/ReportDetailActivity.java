package vision.com.infoapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ReportDetailActivity extends Activity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.cut_image_show);
        webView = (WebView) this.findViewById(R.id.webView);
        String _id = this.getIntent().getExtras().getString("id");
        String _title = this.getIntent().getExtras().getString("title");
        this.setTitle(_title);
//		webView.getSettings().setCacheMode(1);
        String url = Globals.HTTP_CONTENT_URL + "/detail/content?id=" + _id;
        this.initWebView();
        webView.loadUrl(url);
    }

    private void initWebView() {
        WebViewClient webviewClient = new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //6.0以下执行
//                Log.i(TAG, "onReceivedError: ------->errorCode" + errorCode + ":" + description);
                //网络未连接
//                showErrorPage();
            }

            //处理网页加载失败时
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //6.0以上执行
//                Log.i(TAG, "onReceivedError: ");
//                showErrorPage();//显示错误页面
            }
        };

        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);    //允许加载javascript

        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAppCacheEnabled(true);

        webView.setWebViewClient(webviewClient);
        webView.setWebChromeClient(new WebChromeClient() {
            //            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
////                Log.i(TAG, "onProgressChanged:----------->" + newProgress);
////                if (newProgress == 100) {
////                    loadingLayout.setVisibility(View.GONE);
////                }
//            }
//
//
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
////                Log.i(TAG, "onReceivedTitle:title ------>" + title);
////                if (title.contains("404")){
////                    showErrorPage();
////                }
//            }
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
    }
}
