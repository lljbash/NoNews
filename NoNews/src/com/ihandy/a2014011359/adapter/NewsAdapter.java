package com.ihandy.a2014011359.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ihandy.a2014011359.R;
import com.ihandy.a2014011359.bean.NewsEntity;
import com.ihandy.a2014011359.tool.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends BaseAdapter implements OnScrollListener {
    ArrayList<NewsEntity> newsList;
    Activity activity;
    LayoutInflater inflater = null;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public NewsAdapter(Activity activity, ArrayList<NewsEntity> newsList) {
        this.activity = activity;
        this.newsList = newsList;
        inflater = LayoutInflater.from(activity);
        options = Options.getListOptions();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public NewsEntity getItem(int position) {
        // TODO Auto-generated method stub
        if (newsList != null && newsList.size() != 0) {
            return newsList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mHolder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, null);
            mHolder = new ViewHolder();
            mHolder.item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
            mHolder.item_title = (TextView) view.findViewById(R.id.item_title);
            mHolder.item_source = (TextView) view.findViewById(R.id.item_source);
            mHolder.alt_mark = (ImageView) view.findViewById(R.id.alt_mark);
            mHolder.right_image = (ImageView) view.findViewById(R.id.right_image);
            mHolder.item_image_layout = (LinearLayout) view.findViewById(R.id.item_image_layout);
            mHolder.item_image_0 = (ImageView) view.findViewById(R.id.item_image_0);
            mHolder.item_image_1 = (ImageView) view.findViewById(R.id.item_image_1);
            mHolder.item_image_2 = (ImageView) view.findViewById(R.id.item_image_2);
            mHolder.large_image = (ImageView) view.findViewById(R.id.large_image);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        //获取position对应的数据
        NewsEntity news = getItem(position);
        mHolder.item_title.setText(news.getTitle());
        mHolder.item_source.setText(news.getSource());
        List<String> imgUrlList = news.getPicList();
        if (imgUrlList != null && imgUrlList.size() != 0) {
            if (imgUrlList.size() == 1) {
                mHolder.item_image_layout.setVisibility(View.GONE);
                //是否是大图
                if (news.getIsLarge()) {
                    mHolder.large_image.setVisibility(View.VISIBLE);
                    mHolder.right_image.setVisibility(View.GONE);
                    imageLoader.displayImage(imgUrlList.get(0), mHolder.large_image, options);
                } else {
                    mHolder.large_image.setVisibility(View.GONE);
                    mHolder.right_image.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(imgUrlList.get(0), mHolder.right_image, options);
                }
            } else {
                mHolder.large_image.setVisibility(View.GONE);
                mHolder.right_image.setVisibility(View.GONE);
                mHolder.item_image_layout.setVisibility(View.VISIBLE);
                imageLoader.displayImage(imgUrlList.get(0), mHolder.item_image_0, options);
                imageLoader.displayImage(imgUrlList.get(1), mHolder.item_image_1, options);
                imageLoader.displayImage(imgUrlList.get(2), mHolder.item_image_2, options);
            }
        } else {
            mHolder.right_image.setVisibility(View.GONE);
            mHolder.item_image_layout.setVisibility(View.GONE);
        }
        int markResID = getAltMarkResID(news.getCollectStatus());
        if (markResID != -1) {
            mHolder.alt_mark.setVisibility(View.VISIBLE);
            mHolder.alt_mark.setImageResource(markResID);
        } else {
            mHolder.alt_mark.setVisibility(View.GONE);
        }
        return view;
    }

    static class ViewHolder {
        LinearLayout item_layout;
        //title
        TextView item_title;
        //图片源
        TextView item_source;
        //右上方TAG标记图片
        ImageView alt_mark;
        //右边图片
        ImageView right_image;
        //3张图片布局
        LinearLayout item_image_layout; //3张图片时候的布局
        ImageView item_image_0;
        ImageView item_image_1;
        ImageView item_image_2;
        //大图的图片的话布局
        ImageView large_image;
    }

    /**
     * 根据属性获取对应的资源ID
     */
    public int getAltMarkResID(boolean isfavor) {
        if (isfavor) {
            return R.drawable.ic_mark_favor;
        }
        return -1;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    //滑动监听
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }

}
