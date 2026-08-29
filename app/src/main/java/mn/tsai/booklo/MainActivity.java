package mn.tsai.booklo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import android.widget.Toast;

public class MainActivity extends Activity {

    WebView web;
    String orderText = "";
    static final String URL = "https://delivery.booklo.mn/";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        web = new WebView(this);
        setContentView(web);

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView v, String url) {
                if (!orderText.isEmpty()) {
                    fill();
                }
            }
        });

        handleIntent(getIntent());
        web.loadUrl(URL);
    }

    @Override
    protected void onNewIntent(Intent i) {
        super.onNewIntent(i);
        handleIntent(i);
        web.reload();
    }

    void handleIntent(Intent i) {
        if (i != null && Intent.ACTION_SEND.equals(i.getAction())) {
            String t = i.getStringExtra(Intent.EXTRA_TEXT);
            if (t != null) {
                orderText = t;
                Toast.makeText(this, "Захиалга хүлээн авлаа", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void fill() {
        String safe = orderText
                .replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\n", "\\n")
                .replace("\r", "");
        web.evaluateJavascript("window.__ORDER__='" + safe + "';" + Filler.JS, null);
    }
}
