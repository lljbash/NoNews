package com.ihandy.a2014011359;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import com.ihandy.a2014011359.base.BaseActivity;
import com.ihandy.a2014011359.bean.FavoriteManager;
import com.ihandy.a2014011359.bean.NewsEntity;
import com.ihandy.a2014011359.service.NewsDetailsService;
import com.ihandy.a2014011359.tool.DateTools;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

@SuppressLint("JavascriptInterface")
public class DetailsActivity extends BaseActivity {
	private TextView title;
	private ProgressBar progressBar;
	private FrameLayout customview_layout;
	private String news_url;
	private String news_title;
	private String news_source;
	private String news_date;
	private String img_source;
	private NewsEntity news;
	private ImageView action_favor;
	private ImageView action_repost;
	WebView webView;
	private Tencent mTencent;

	private class BaseUiListener implements IUiListener {
		public void onComplete(Object response) {
			Log.d("---","share");
			try {
				Log.d("---share","success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("---share","error");
				e.printStackTrace();
			}

		}
		@Override
		public void onError(UiError e) {
			Log.d("---:", "onError code:" + e.errorCode);
		}
		@Override
		public void onCancel() {
			Log.d("---", "onCancel");
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		setNeedBackGesture(true);//设置需要手势监听
		getData();
		initView();
		initWebView();
		mTencent = Tencent.createInstance(String.valueOf(1103432372), this.getApplicationContext());
	}
	/* 获取传递过来的数据 */
	private void getData() {
		news = (NewsEntity) getIntent().getSerializableExtra("news");
		news_url = news.getSource_url();
		news_title = news.getTitle();
		news_source = news.getSource();
		img_source = news.getPicOne();
		news_date = DateTools.getNewsDetailsDate(String.valueOf(0));
	}

	private void initWebView() {
		webView = (WebView)findViewById(R.id.wb_details);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		if (!TextUtils.isEmpty(news_url)) {
			WebSettings settings = webView.getSettings();
			settings.setJavaScriptEnabled(true);//设置可以运行JS脚本
//			settings.setTextZoom(120);//Sets the text zoom of the page in percent. The default is 100.
			settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//			settings.setUseWideViewPort(true); //打开页面时， 自适应屏幕 
//			settings.setLoadWithOverviewMode(true);//打开页面时， 自适应屏幕 
			settings.setSupportZoom(false);// 用于设置webview放大
			settings.setBuiltInZoomControls(false);
			webView.setBackgroundResource(R.color.transparent);
			// 添加js交互接口类，并起别名 imagelistner
			webView.addJavascriptInterface(new JavascriptInterface(getApplicationContext()),"imagelistner");
			webView.setWebChromeClient(new MyWebChromeClient());
			webView.setWebViewClient(new MyWebViewClient());
			new MyAsnycTask().execute(news_url, news_title, news_source + " " +news_date);
		}
	}

	private void initView() {
		title = (TextView) findViewById(R.id.title);
		progressBar = (ProgressBar) findViewById(R.id.ss_htmlprogessbar);
		customview_layout = (FrameLayout) findViewById(R.id.customview_layout);
		//底部栏目
		action_favor = (ImageView) findViewById(R.id.action_favor);
		if (news.getCollectStatus()) {
			action_favor.setImageResource(R.drawable.ic_action_favor_on_normal);
		}
		else {
			action_favor.setImageResource(R.drawable.ic_action_favor_normal);
		}

		progressBar.setVisibility(View.VISIBLE);
		title.setTextSize(13);
		title.setVisibility(View.VISIBLE);
		title.setText(news_url);
		Activity activity = this;

		action_favor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (news.getCollectStatus()) {
					FavoriteManager.remove(news);
					news.setCollectStatus(false);
					Toast.makeText(activity, "remove favor", Toast.LENGTH_SHORT).show();
					action_favor.setImageResource(R.drawable.ic_action_favor_normal);
				}
				else {
					FavoriteManager.add(news);
					news.setCollectStatus(true);
					Toast.makeText(activity, "add favor", Toast.LENGTH_SHORT).show();
					action_favor.setImageResource(R.drawable.ic_action_favor_on_normal);
				}
			}
		});
		action_repost = (ImageView) findViewById(R.id.action_repost);
		action_repost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Bundle params = new Bundle();
				params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
				params.putString(QQShare.SHARE_TO_QQ_TITLE, news_title);
				//params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "要分享的摘要");
				params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, news_url);
				params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,img_source);
				params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "app");
				mTencent.shareToQQ(DetailsActivity.this, params, new BaseUiListener());
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		int resultCode = news.getCollectStatus() ? 1 : 0;
		Log.i("on exit", String.valueOf(resultCode));
		setResult(0);
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	private class MyAsnycTask extends AsyncTask<String, String,String>{

		@Override
		protected String doInBackground(String... urls) {
			String data=NewsDetailsService.getNewsDetails(urls[0],urls[1],urls[2]);
			return data;
		}

		@Override
		protected void onPostExecute(String data) {
			webView.loadDataWithBaseURL (null, data, "text/html", "utf-8",null);
		}
	}

	// 注入js函数监听
	private void addImageClickListner() {
		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
		webView.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\");"
				+ "var imgurl=''; " + "for(var i=0;i<objs.length;i++)  " + "{"
				+ "imgurl+=objs[i].src+',';"
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.imagelistner.openImage(imgurl);  "
				+ "    }  " + "}" + "})()");
	}

	// js通信接口
	public class JavascriptInterface {

		private Context context;

		public JavascriptInterface(Context context) {
			this.context = context;
		}

		public void openImage(String img) {

			//
			String[] imgs = img.split(",");
			ArrayList<String> imgsUrl = new ArrayList<String>();
			for (String s : imgs) {
				imgsUrl.add(s);
			}
			Intent intent = new Intent();
			intent.putStringArrayListExtra("infos", imgsUrl);
			intent.setClass(context, ImageShowActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	// 监听
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageFinished(view, url);
			// html加载完成之后，添加监听图片的点击js函数
			addImageClickListner();
			progressBar.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			progressBar.setVisibility(View.GONE);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}
	
	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			if(newProgress != 100){
				progressBar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}
	}
}
