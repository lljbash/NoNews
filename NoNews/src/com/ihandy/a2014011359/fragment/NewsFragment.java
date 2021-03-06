package com.ihandy.a2014011359.fragment;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AbsListView;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import com.ihandy.a2014011359.DetailsActivity;
        import com.ihandy.a2014011359.R;
        import com.ihandy.a2014011359.adapter.NewsAdapter;
        import com.ihandy.a2014011359.bean.FavoriteManager;
        import com.ihandy.a2014011359.bean.NewsEntity;
        import com.ihandy.a2014011359.tool.NewsListFetcher;
        import com.ihandy.a2014011359.tool.NewsListFileManager;
        import com.ihandy.a2014011359.view.XListView;

        import java.util.ArrayList;

public class NewsFragment extends Fragment implements XListView.IXListViewListener {
    private final static String TAG = "NewsFragment";
    Activity activity;
    ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
    XListView mListView;
    NewsAdapter mAdapter;
    String text;
    int channel_id;
    ImageView detail_loading;
    public final static int SET_NEWSLIST = 0;
    public final static int UPDATE_NEWSLIST = 1;
    //Toast提示框
    private RelativeLayout notify_view;
    private TextView notify_view_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Bundle args = getArguments();
        text = args != null ? args.getString("text") : "";
        channel_id = args != null ? args.getInt("id", 0) : 0;
        initData();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        this.activity = activity;
        super.onAttach(activity);
    }

    /**
     * 此方法意思为fragment是否可见 ,可见时候加载数据
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //fragment可见时加载数据
            if (newsList != null && newsList.size() != 0) {
                handler.obtainMessage(SET_NEWSLIST).sendToTarget();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        handler.obtainMessage(SET_NEWSLIST).sendToTarget();
                    }
                }).start();
            }
        } else {
            //fragment不可见时不执行操作
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
        mListView = (XListView) view.findViewById(R.id.mListView);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime("从未");
        TextView item_textview = (TextView) view.findViewById(R.id.item_textview);
        detail_loading = (ImageView) view.findViewById(R.id.detail_loading);
        //Toast提示框
        notify_view = (RelativeLayout) view.findViewById(R.id.notify_view);
        notify_view_text = (TextView) view.findViewById(R.id.notify_view_text);
        item_textview.setText(text);
        return view;
    }

    private void initData() {
        newsList = NewsListFileManager.loadNewsList(activity, text + ".dat");
        updateData();
    }

    volatile boolean updating = false;

    public void updateData() {
        if (updating) {
            return;
        }
        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                updating = true;
                ArrayList<NewsEntity> freshList = NewsListFetcher.getNewsList(text, channel_id, -1);
                if (!freshList.isEmpty()) {
                    newsList = freshList;
                    handler.obtainMessage(SET_NEWSLIST).sendToTarget();
                    NewsListFileManager.saveNewsList(activity, text + ".dat", newsList);
                }
                updating = false;
            }
        });
        updateThread.start();
    }

    volatile boolean moring = false;

    public void moreData() {
        if (moring) {
            return;
        }
        Thread moreThread = new Thread(new Runnable() {
            @Override
            public void run() {
                moring = true;
                NewsListFetcher.moreNewsList(newsList, text, channel_id);
                if (!newsList.isEmpty()) {
                    handler.obtainMessage(UPDATE_NEWSLIST).sendToTarget();
                    NewsListFileManager.saveNewsList(activity, text + ".dat", newsList);
                }
                moring = false;
            }
        });
        moreThread.start();
    }

    NewsEntity detailedNews;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case SET_NEWSLIST:
                    detail_loading.setVisibility(View.GONE);
                    //if (mAdapter == null) {
                    //    mAdapter = new NewsAdapter(activity, newsList);
                    //}
                    mAdapter = new NewsAdapter(activity, newsList);
                    mListView.setAdapter(mAdapter);
                    mListView.setOnScrollListener(mAdapter);

                    mListView.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Intent intent = new Intent(activity, DetailsActivity.class);
                            detailedNews = mAdapter.getItem(position - 1);
                            intent.putExtra("news", detailedNews);
                            startActivityForResult(intent, 1);
                            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });
                    break;
                case UPDATE_NEWSLIST:
                    mAdapter.setNewsList(newsList);
                    mAdapter.notifyDataSetChanged();
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /* 初始化通知栏目*/
    private void initNotify() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                notify_view_text.setText(String.format(getString(R.string.ss_pattern_update), 10));
                notify_view.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        notify_view.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        }, 1000);
    }

    /* 摧毁视图 */
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        Log.d("onDestroyView", "channel_id = " + channel_id);
        mAdapter = null;
    }

    /* 摧毁该Fragment，一般是FragmentActivity 被摧毁的时候伴随着摧毁 */
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG, "channel_id = " + channel_id);
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateData();
                onLoad();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moreData();
                onLoad();
            }
        }, 500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("end", "code = " + resultCode);
        detailedNews.setCollectStatus(FavoriteManager.query(detailedNews));
        mAdapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
