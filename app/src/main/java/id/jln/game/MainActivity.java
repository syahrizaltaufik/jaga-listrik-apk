package id.jln.game;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
  // Halaman artifact di claude.ai (mode online, perlu login akun Claude)
  static final String ONLINE_URL = "https://claude.ai/artifact/196ERJsKkj1hEmXWHEKGdd";
  static final String LOCAL_URL = "file:///android_asset/index.html";
  // Alamat file game terbaru (HTML tunggal) untuk pembaruan mode offline.
  // GANTI dengan alamat milikmu, mis. GitHub Pages: https://USERNAME.github.io/REPO/index.html
  static final String UPDATE_URL = "https://syahrizaltaufik.github.io/jaga-listrik/index.html";
  WebView w;
  boolean login=false;

  File upd() { return new File(getFilesDir(), "index-update.html"); }

  /** Muat game lokal: pakai salinan hasil unduhan bila lebih baru dari APK, selain itu bawaan APK. */
  void loadLocal() {
    try {
      File f = upd();
      long apk = getPackageManager().getPackageInfo(getPackageName(), 0).lastUpdateTime;
      if (f.exists() && f.length() > 100000 && f.lastModified() > apk) {
        byte[] bb = new byte[(int) f.length()];
        try (DataInputStream in = new DataInputStream(new FileInputStream(f))) { in.readFully(bb); }
        w.loadDataWithBaseURL("file:///android_asset/", new String(bb, "UTF-8"), "text/html", "UTF-8", null);
        return;
      }
    } catch (Exception e) {}
    w.loadUrl(LOCAL_URL);
  }

  /** Unduh versi terbaru di latar belakang; dipakai pada pembukaan berikutnya. */
  void checkUpdate() {
    
    new Thread(() -> {
      try {
        HttpURLConnection c = (HttpURLConnection) new URL(UPDATE_URL).openConnection();
        c.setConnectTimeout(8000); c.setReadTimeout(20000);
        if (c.getResponseCode() != 200) return;
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        try (InputStream in = c.getInputStream()) { byte[] b = new byte[16384]; int n; while ((n = in.read(b)) > 0) o.write(b, 0, n); }
        byte[] d = o.toByteArray();
        String s = new String(d, "UTF-8");
        if (d.length < 100000 || !s.contains("Jaga Listrik")) return;
        File f = upd(), tmp = new File(getFilesDir(), "index-update.tmp");
        try (FileOutputStream fo = new FileOutputStream(tmp)) { fo.write(d); }
        if (!tmp.renameTo(f)) { f.delete(); tmp.renameTo(f); }
      } catch (Exception e) {}
    }).start();
  }

  @Override protected void onCreate(Bundle b) {
    super.onCreate(b);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    w = new WebView(this);
    WebSettings s = w.getSettings();
    s.setJavaScriptEnabled(true);
    s.setDomStorageEnabled(true);
    s.setJavaScriptCanOpenWindowsAutomatically(true);
    s.setMediaPlaybackRequiresUserGesture(false);
    // Hilangkan penanda "wv" agar login tidak ditolak sebagai WebView
    s.setUserAgentString(s.getUserAgentString().replace("; wv", "").replace("Version/4.0 ", ""));
    CookieManager cm = CookieManager.getInstance();
    cm.setAcceptCookie(true);
    cm.setAcceptThirdPartyCookies(w, true);
    w.setWebViewClient(new WebViewClient() {
      @Override public void onPageFinished(WebView v, String url) {
        // Setelah masuk akun Claude di dalam aplikasi, otomatis kembali ke game online
        if (login && url != null && url.startsWith("https://claude.ai/") && !url.contains("login") && !url.contains("magic") && !url.contains("oauth") && !url.contains("verify") && !url.contains("onboarding")) {
          login = false; v.loadUrl(ONLINE_URL);
        }
      }
      @Override public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
        Uri u = r.getUrl();
        String sc = u.getScheme() == null ? "" : u.getScheme();
        String h = u.getHost() == null ? "" : u.getHost();
        if (sc.equals("file")) return false;
        boolean inApp = h.endsWith("claude.ai") || h.endsWith("claude.com") || h.endsWith("anthropic.com")
            || h.endsWith("googleusercontent.com") || h.endsWith("claudeusercontent.com");
        if (inApp) return false;
        try { startActivity(new Intent(Intent.ACTION_VIEW, u)); } catch (Exception e) {}
        return true;
      }
    });
    setContentView(w);
    if (!online()) { loadLocal(); return; }
    checkUpdate();
    loadLocal(); // game lokal; mode online memakai server Firebase dari dalam game
  }

  boolean online() {
    try {
      ConnectivityManager c = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo n = c.getActiveNetworkInfo();
      return n != null && n.isConnected();
    } catch (Exception e) { return false; }
  }

  @Override public void onBackPressed() { if (w.canGoBack()) w.goBack(); else super.onBackPressed(); }
}
