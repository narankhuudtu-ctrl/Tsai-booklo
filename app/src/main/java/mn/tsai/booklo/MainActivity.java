package mn.tsai.booklo;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    WebView web;
    StringBuilder buf = new StringBuilder();
    String last = "";
    static final String URL = "https://delivery.booklo.mn/order/create";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);

        Button add = new Button(this);
        add.setText("Нэмэх +");
        add.setBackgroundColor(Color.parseColor("#1a9e6f"));
        add.setTextColor(Color.WHITE);
        bar.addView(add, new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 2f));

        Button clr = new Button(this);
        clr.setText("Цэвэр");
        clr.setBackgroundColor(Color.parseColor("#888888"));
        clr.setTextColor(Color.WHITE);
        bar.addView(clr, new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        root.addView(bar, new LinearLayout.LayoutParams(
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

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String t = clip();
                if (t.isEmpty()) {
                    toast("Хуулсан текст алга");
                    return;
                }
                if (t.equals(last)) {
                    toast("Энэ текстийг аль хэдийн нэмсэн");
                    return;
                }
                last = t;
                buf.append(t).append("\n");
                fill();
                toast("Нэмлээ");
            }
        });

        clr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buf.setLength(0);
                last = "";
                web.loadUrl(URL);
                toast("Шинэ захиалга");
            }
        });
    }

    void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    String clip() {
        try {
            ClipboardManager cm = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null && cm.hasPrimaryClip()
                    && cm.getPrimaryClip().getItemCount() > 0) {
                CharSequence cs = cm.getPrimaryClip().getItemAt(0).getText();
                if (cs != null) return cs.toString().trim();
            }
        } catch (Exception e) { }
        return "";
    }

    void fill() {
        String safe = buf.toString()
                .replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\n", "\\n")
                .replace("\r", "");
        web.evaluateJavascript(
            "window.__ORDER__='" + safe + "';" + Filler.JS, null);
    }
}
