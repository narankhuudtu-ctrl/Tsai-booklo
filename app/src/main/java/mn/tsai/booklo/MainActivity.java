package mn.tsai.booklo;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
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
                    web.postDelayed(new Runnable() {
                        public void run() { fill(); }
                    }, 1200);
                }
            }
        });

        readSource(getIntent());
        web.loadUrl(URL);
    }

    @Override
    protected void onNewIntent(Intent i) {
        super.onNewIntent(i);
        setIntent(i);
        readSource(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        readClipboard();
        if (!orderText.isEmpty() && web.getProgress() == 100) {
            fill();
        }
    }

    void readSource(Intent i) {
        if (i != null && Intent.ACTION_SEND.equals(i.getAction())) {
            String t = i.getStringExtra(Intent.EXTRA_TEXT);
            if (t != null && !t.trim().isEmpty()) {
                orderText = t;
                return;
            }
        }
        readClipboard();
    }

    void readClipboard() {
        try {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null && cm.hasPrimaryClip() && cm.getPrimaryClip().getItemCount() > 0) {
                CharSequence cs = cm.getPrimaryClip().getItemAt(0).getText();
                if (cs != null && cs.length() > 5) {
                    orderText = cs.toString();
                    Toast.makeText(this, "Захиалга уншлаа", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) { }
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
