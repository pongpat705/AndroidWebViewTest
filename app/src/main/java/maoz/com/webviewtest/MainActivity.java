package maoz.com.webviewtest;

import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.http.HttpConnection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient()
            {
                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                    try {
                        OkHttpClient httpClient = new OkHttpClient();

                        Request.Builder reqBuilder = new Request.Builder();
                        reqBuilder.url(request.getUrl().toString().trim());
                        Log.i("url", request.getUrl().toString());
                        for (Map.Entry<String, String> e : request.getRequestHeaders().entrySet()) {

                            Log.i(e.getKey(), e.getValue());
                            reqBuilder.addHeader(e.getKey(), e.getValue());
                        }
                        reqBuilder.addHeader("mobile-token", "prefix xxxxxxxxx");
                        Request myReq = reqBuilder.build();
                        Response response = httpClient.newCall(myReq).execute();

                        return new WebResourceResponse(
//                                getMimeType(request.getUrl().toString()), // set content-type
                                response.headers().get("content-type"),
                                response.header("content-encoding", response.headers().get("content-encoding")),
                                response.body().byteStream()
                        );
                    }  catch (IOException e) {
                        return null;
                    } catch (Exception e) {
                        return null;
                    }
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {


                    handler.proceed();
                }

            }
        );

        myWebView.loadUrl("http://www.w3schools.com/html/html_responsive.asp");


    }

    public String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            if (extension.equals("js")) {
                return "text/javascript";
            }
            else if (extension.equals("woff")) {
                return "application/font-woff";
            }
            else if (extension.equals("woff2")) {
                return "application/font-woff2";
            }
            else if (extension.equals("ttf")) {
                return "application/x-font-ttf";
            }
            else if (extension.equals("eot")) {
                return "application/vnd.ms-fontobject";
            }
            else if (extension.equals("svg")) {
                return "image/svg+xml";
            }
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }


}
