package com.ihandy.a2014011359.tool;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import com.ihandy.a2014011359.bean.NewsClassify;
import com.ihandy.a2014011359.bean.FavoriteManager;
import com.ihandy.a2014011359.bean.NewsEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsListFetcher {
    /*
     * 获取新闻列表
     */
    public static ArrayList<NewsEntity> getNewsList(String text, int channel_id, long news_id) {
        ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("Thread_id", String.valueOf(channel_id));
                    String str = "http://assignment.crazz.cn/news/query?locale=en&category=" + text;
                    if (news_id > 0L) {
                        str += "&max_news_id=" + news_id;
                    }
                    HttpRequest request = HttpRequest.get(str);
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
                            try {
                                news.setNewsId(Long.valueOf(dataArray.getJSONObject(i).getString("news_id")));
                            } catch (org.json.JSONException e) {
                                e.printStackTrace();
                            }
                            news.setCollectStatus(FavoriteManager.query(news));
                            news.setNewsCategory(text);
                            news.setNewsCategoryId(channel_id);
                            try {
                                news.setSource(dataArray.getJSONObject(i).getString("origin"));
                            } catch (org.json.JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                news.setTitle(dataArray.getJSONObject(i).getString("title"));
                            } catch (org.json.JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                news.setSource_url(dataArray.getJSONObject(i).getJSONObject("source").getString("url"));
                            } catch (org.json.JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                List<String> url_list = new ArrayList<String>();
                                String url = dataArray.getJSONObject(i).optJSONArray("imgs").getJSONObject(0).getString("url");
                                news.setPicOne(url);
                                url_list.add(url);
                                news.setPicList(url_list);
                            } catch (org.json.JSONException e) {
                                e.printStackTrace();
                            }
                            news.setIsLarge(false);
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
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Collections.sort(newsList);
        return newsList;
    }

    public static void moreNewsList(ArrayList<NewsEntity> currentList, String text, int channel_id) {
        long news_id = -1;
        if (!currentList.isEmpty()) {
            news_id = currentList.get(currentList.size() - 1).getNewsId();
        }
        ArrayList<NewsEntity> newsList = getNewsList(text, channel_id, news_id);
        for (NewsEntity e : newsList) {
            if (news_id == -1 || e.getNewsId() < news_id) {
                currentList.add(e);
            }
        }
        Collections.sort(currentList);
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
