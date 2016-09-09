package com.ihandy.a2014011359.bean;

import android.app.Activity;
import android.util.Log;

import com.ihandy.a2014011359.tool.NewsListFileManager;

import java.util.ArrayList;
import java.util.HashSet;

public class FavoriteManager {

    static ArrayList<NewsEntity> newsList = new ArrayList<>();
    static HashSet<Long> newsSet = new HashSet<>();
    static final String filename = "favorite.fdat";
    static Activity mActivity;

    static void save() {
        NewsListFileManager.saveNewsList(mActivity, filename, newsList);
    }

    static void load() {
        newsList = NewsListFileManager.loadNewsList(mActivity, filename);
    }

    public static void init(Activity activity) {
        mActivity = activity;
        load();
        newsSet.clear();
        for (NewsEntity e : newsList) {
            newsSet.add(e.getNewsId());
        }
    }

    public static boolean query(NewsEntity e) {
        return newsSet.contains(e.getNewsId());
    }

    public static boolean add(NewsEntity e) {
        if (!query(e)) {
            newsList.add(0, e);
            newsSet.add(e.getNewsId());
            save();
            Log.i("add", "success");
            return true;
        }
        return false;
    }

    public static boolean remove(NewsEntity e) {
        if (query(e)) {
            for (int i = 0; i < newsList.size(); ++i) {
                if (newsList.get(i).getNewsId().equals(e.getNewsId())) {
                    newsList.remove(i);
                    break;
                }
            }
            newsSet.remove(e.getNewsId());
            Log.i("remove", "success");
            save();
            return true;
        }
        return false;
    }

    public static ArrayList<NewsEntity> getNewsList() {
        return newsList;
    }

}
