package mn.tsai.booklo;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    WebView web;
    String orderText = "";
    static final String URL = "https://delivery.booklo.mn/order/create";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        Button btn = new Button(this);
        btn.setText("Бөглөх");
        btn.setBackgroundColor(Color.parseColor("#1a9e6f"));
        btn.setTextColor(Color.WHITE);
        root.addView(btn, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        web = new WebView(this);
        root.addView(web, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

        setContentView(root);

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl(URL);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                readClipboard();
                if (orderText.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                        "Хуулсан текст олдсонгүй", Toast.LENGTH_LONG).show();
                } else {
                    String head = orderText.length() > 60
                        ? orderText.substring(0, 60) : orderText;
                    Toast.makeText(MainActivity.this,
                        "Уншсан: " + head, Toast.LENGTH_LONG).show();
                    fill();
                }
            }
        });
    }

    void readClipboard() {
        try {
            ClipboardManager cm = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null && cm.hasPrimaryClip()
                    && cm.getPrimaryClip().getItemCount() > 0) {
                CharSequence cs = cm.getPrimaryClip().getItemAt(0).getText();
                if (cs != null && cs.length() > 3) {
                    orderText = cs.toString();
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
        web.evaluateJavascript(
            "window.__ORDER__='" + safe + "';" + Filler.JS, null);
    }
}
