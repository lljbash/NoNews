package com.ihandy.a2014011359;

import java.util.ArrayList;

import com.ihandy.a2014011359.bean.FavoriteManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ihandy.a2014011359.adapter.NewsFragmentPagerAdapter;
import com.ihandy.a2014011359.app.AppApplication;
import com.ihandy.a2014011359.bean.ChannelItem;
import com.ihandy.a2014011359.bean.ChannelManage;
import com.ihandy.a2014011359.fragment.NewsFragment;
import com.ihandy.a2014011359.tool.BaseTools;
import com.ihandy.a2014011359.view.ColumnHorizontalScrollView;
import com.ihandy.a2014011359.view.DrawerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * （android高仿系列）今日头条 --新闻阅读器  
 * author:RA
 * blog : http://blog.csdn.net/vipzjyno1/
 */
public class MainActivity extends FragmentActivity {
	/** 自定义HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	LinearLayout mRadioGroup_content;
	RelativeLayout rl_column;
	private ViewPager mViewPager;
	/** 用户选择的新闻分类列表*/
	private ArrayList<ChannelItem> userChannelList=new ArrayList<ChannelItem>();
	/** 当前选中的栏目*/
	private int columnSelectIndex = 0;
	/** 左阴影部分*/
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	
	protected SlidingMenu side_drawer;
	
	/** head 头部 的中间的loading*/
	private ProgressBar top_progress;
	/** head 头部 的左侧菜单 按钮*/
	private ImageView top_head;
	/** 请求CODE */
	public final static int CHANNEL_REQUEST = 1;
	/** 调整返回的RESULT_CODE */
	public final static int CHANNEL_RESULT = 10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
		FavoriteManager.init(this);
		initView();
		initSlidingMenu();
	}
	/** 初始化layout控件*/
	private void initView() {
		mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		top_head = (ImageView) findViewById(R.id.top_head);
		//top_more = (ImageView) findViewById(R.id.top_more);
		top_progress = (ProgressBar) findViewById(R.id.top_progress);
		top_head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(side_drawer.isMenuShowing()){
					side_drawer.showContent();
				}else{
					side_drawer.showMenu();
				}
			}
		});
		setChangeView();
	}
	/** 
	 *  当栏目项发生变化时候调用
	 * */
	private void setChangeView() {
		initColumnData();
		initTabColumn();
		initFragment();
	}
	/** 获取Column栏目 数据*/
	private void initColumnData() {
		userChannelList = ((ArrayList<ChannelItem>)ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel());
	}

	/** 
	 *  初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count =  userChannelList.size();
		mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, rl_column);
		for(int i = 0; i< count; i++){
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(userChannelList.get(i).getName());
			columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
			columnTextView.setSingleLine();
			if(columnSelectIndex == i){
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
			          for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
				          View localView = mRadioGroup_content.getChildAt(i);
				          if (localView != v)
				        	  localView.setSelected(false);
				          else{
				        	  localView.setSelected(true);
				        	  mViewPager.setCurrentItem(i);
				          }
			          }
			          Toast.makeText(getApplicationContext(), userChannelList.get(v.getId()).getName(), Toast.LENGTH_SHORT).show();
				}
			});
			mRadioGroup_content.addView(columnTextView, i ,params);
		}
	}
	/** 
	 *  选择的Column里面的Tab
	 * */
	private void selectTab(int tab_position) {
		columnSelectIndex = tab_position;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_position);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			// rg_nav_content.getParent()).smoothScrollTo(i2, 0);
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
			// mItemWidth , 0);
		}
		//判断是否选中
		for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean is_checked;
			if (j == tab_position) {
				is_checked = true;
			} else {
				is_checked = false;
			}
			checkView.setSelected(is_checked);
		}
		((NewsFragment)fragments.get(tab_position)).updateData();
	}
	/** 
	 *  初始化Fragment
	 * */
	private void initFragment() {
		fragments.clear();//清空
		int count =  userChannelList.size();
		for(int i = 0; i< count;i++){
			Bundle data = new Bundle();
    		data.putString("text", userChannelList.get(i).getName());
    		data.putInt("id", userChannelList.get(i).getId());
			NewsFragment newsfragment = new NewsFragment();
			newsfragment.setArguments(data);
			fragments.add(newsfragment);
		}
		NewsFragmentPagerAdapter mAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
//		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(pageListener);
	}
	/** 
	 *  ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener= new OnPageChangeListener(){

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			mViewPager.setCurrentItem(position);
			selectTab(position);
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void initSlidingMenu() {
		side_drawer = new DrawerView(this).initSlidingMenu();
	}
	
	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(side_drawer.isMenuShowing() ||side_drawer.isSecondaryMenuShowing()){
				side_drawer.showContent();
			}else {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "再按一次退出",
							Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
				}
			}
			return true;
		}
		//拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case CHANNEL_REQUEST:
			if(resultCode == CHANNEL_RESULT){
				setChangeView();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
