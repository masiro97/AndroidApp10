package com.android.masiro.proj10;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Button b;
    EditText et;
    WebView wv;
    ProgressDialog dlg;
    Animation ani;
    LinearLayout linear;

    public void OnButton(View v){

        if(v.getId() == R.id.button2){
            wv.loadUrl("javascript:changeImage()");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //b = (Button)findViewById(R.id.button);
        et = (EditText)findViewById(R.id.editText);
        wv = (WebView)findViewById(R.id.webview);
        wv.addJavascriptInterface(new JavaSriptMethods(),"MyApp");
        dlg = new ProgressDialog(this);
        ani = AnimationUtils.loadAnimation(this,R.anim.translate_top);
        linear = (LinearLayout)findViewById(R.id.linearlayout);

        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linear.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        wv.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dlg.setMessage("Loading...");
                dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dlg.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                et.setText(url);
            }
        });
        wv.loadUrl("http://m.blog.naver.com/kscr37");

        WebSettings webSettings = wv.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(webSettings.LOAD_NO_CACHE);

        wv.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress >= 100) dlg.dismiss();
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0,1,0,"즐겨찾기");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){

        if(menuItem.getItemId() == 1) {
            wv.loadUrl("file://android_assets/www/index.html");
            linear.setAnimation(ani);
            ani.start();
        }
        //wv.loadUrl("file://android.asset/www/index.html");
        return super.onContextItemSelected(menuItem);
    }

    Handler handler =  new Handler();
    class JavaSriptMethods{

        @JavascriptInterface
        public void displayToast(){
           handler.post(new Runnable() {
               @Override
               public void run() {
                   //원하는 기능
                   AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                   dlg.setTitle("그림 변경");
                   dlg.setMessage("그림을 변경하시겠습니까?");
                   dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           wv.loadUrl("javascript:changeImage()");
                       }
                   });
                   dlg.setNegativeButton("Cancel",null);
                   dlg.show();
               }
           });

        }
    }
}
