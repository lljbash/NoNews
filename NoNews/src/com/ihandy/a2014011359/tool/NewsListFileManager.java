package com.ihandy.a2014011359.tool;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ihandy.a2014011359.bean.NewsEntity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class NewsListFileManager {

    public static void saveNewsList(Activity activity, String filename, ArrayList<NewsEntity> newsList) {
        FileOutputStream outputStream;
        ObjectOutputStream objectOutputStream;
        try {
            outputStream = activity.openFileOutput(filename, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(newsList);
            Log.i("save to", filename);
            Log.i("size", String.valueOf(newsList.size()));
            objectOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<NewsEntity> loadNewsList(Activity activity, String filename) {
        ArrayList<NewsEntity> newsList = new ArrayList<>();
        FileInputStream inputStream;
        ObjectInputStream objectInputStream;
        try {
            inputStream = activity.openFileInput(filename);
            objectInputStream = new ObjectInputStream(inputStream);
            newsList = (ArrayList<NewsEntity>) objectInputStream.readObject();
            Log.i("load from", filename);
            Log.i("size", String.valueOf(newsList.size()));
            objectInputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }

}
