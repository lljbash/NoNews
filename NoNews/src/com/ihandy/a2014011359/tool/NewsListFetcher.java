package com.ihandy.a2014011359.tool;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.ihandy.a2014011359.bean.CityEntity;
//import com.ihandy.a2014011359.bean.NewsClassify;
import com.ihandy.a2014011359.bean.NewsEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsListFetcher {
	/*
	 * 获取新闻列表
	 */
	public static ArrayList<NewsEntity> getNewsList(String text, int channel_id) {
		ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d("Thread_id", String.valueOf(channel_id));
				HttpRequest request = HttpRequest.get(
						"http://assignment.crazz.cn/news/query?locale=en&category="+text);
				String body = request.body();
				Log.i("QueryEnd", text);
				Log.i("QueryEnd", body);
				try {
					JSONObject jsonObject = new JSONObject(body);
					JSONObject result = jsonObject.getJSONObject("data");
					JSONArray dataArray = result.optJSONArray("news");
					Log.i("Json dataArray.length", String.valueOf(dataArray.length()));
					for (int i = 0; i < dataArray.length(); i++) {
						Log.i("Json img", dataArray.getJSONObject(i).optJSONArray("imgs").getJSONObject(0).getString("url"));
						NewsEntity news = new NewsEntity();
						news.setId(i);
						news.setNewsId(i);
						news.setCollectStatus(false);
						news.setCommentNum(0);
						news.setInterestedStatus(true);
						news.setLikeStatus(true);
						news.setReadStatus(false);
						news.setNewsCategory(text);
						news.setNewsCategoryId(channel_id);
						news.setSource(dataArray.getJSONObject(i).getString("origin"));
						news.setTitle(dataArray.getJSONObject(i).getString("title"));
						news.setSource_url(dataArray.getJSONObject(i).getJSONObject("source").getString("url"));
						List<String> url_list = new ArrayList<String>();
						String url = dataArray.getJSONObject(i).optJSONArray("imgs").getJSONObject(0).getString("url");
						news.setPicOne(url);
						url_list.add(url);
						news.setPicList(url_list);
						news.setPublishTime((long) i);
						news.setMark(5);
						news.setIsLarge(false);
						news.setPublishTime((long) 0);
						newsList.add(news);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		return newsList;
	}
	
	/** mark=0 ：推荐 */
	public final static int mark_recom = 0;
	/** mark=1 ：热门 */
	public final static int mark_hot = 1;
	/** mark=2 ：首发 */
	public final static int mark_frist = 2;
	/** mark=3 ：独家 */
	public final static int mark_exclusive = 3;
	/** mark=4 ：收藏 */
	public final static int mark_favor = 4;
	
	/*
	 * 获取城市列表
	 */
	public static ArrayList<CityEntity> getCityList(){
		ArrayList<CityEntity> cityList =new ArrayList<CityEntity>();
		CityEntity city1 = new CityEntity(1, "安吉", 'A');
		CityEntity city2 = new CityEntity(2, "北京", 'B');
		CityEntity city3 = new CityEntity(3, "长春", 'C');
		CityEntity city4 = new CityEntity(4, "长沙", 'C');
		CityEntity city5 = new CityEntity(5, "大连", 'D');
		CityEntity city6 = new CityEntity(6, "哈尔滨", 'H');
		CityEntity city7 = new CityEntity(7, "杭州", 'H');
		CityEntity city8 = new CityEntity(8, "金沙江", 'J');
		CityEntity city9 = new CityEntity(9, "江门", 'J');
		CityEntity city10 = new CityEntity(10, "山东", 'S');
		CityEntity city11 = new CityEntity(11, "三亚", 'S');
		CityEntity city12 = new CityEntity(12, "义乌", 'Y');
		CityEntity city13 = new CityEntity(13, "舟山", 'Z');
		cityList.add(city1);
		cityList.add(city2);
		cityList.add(city3);
		cityList.add(city4);
		cityList.add(city5);
		cityList.add(city6);
		cityList.add(city7);
		cityList.add(city8);
		cityList.add(city9);
		cityList.add(city10);
		cityList.add(city11);
		cityList.add(city12);
		cityList.add(city13);
		return cityList;
	}
	/* 频道中区域 如杭州 对应的栏目ID */
	public final static int CHANNEL_CITY = 3;
}
