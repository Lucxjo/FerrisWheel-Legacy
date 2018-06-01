package me.turtlecode.ferriswheel;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class PolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        WebView browser = (WebView) findViewById(R.id.webView);

        browser.loadUrl("https://www.iubenda.com/api/privacy-policy/89605138");
    }
}
