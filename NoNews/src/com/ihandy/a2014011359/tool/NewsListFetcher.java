package com.ihandy.a2014011359.tool;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
                try {
                    Log.d("Thread_id", String.valueOf(channel_id));
                    HttpRequest request = HttpRequest.get(
                            "http://assignment.crazz.cn/news/query?locale=en&category=" + text);
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
                } catch (HttpRequest.HttpRequestException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        return newsList;
    }

    /**
     * mark=0 ：推荐
     */
    public final static int mark_recom = 0;
    /**
     * mark=1 ：热门
     */
    public final static int mark_hot = 1;
    /**
     * mark=2 ：首发
     */
    public final static int mark_frist = 2;
    /**
     * mark=3 ：独家
     */
    public final static int mark_exclusive = 3;
    /**
     * mark=4 ：收藏
     */
    public final static int mark_favor = 4;

}
