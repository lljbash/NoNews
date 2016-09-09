package com.ihandy.a2014011359;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ihandy.a2014011359.adapter.NewsAdapter;
import com.ihandy.a2014011359.base.BaseActivity;
import com.ihandy.a2014011359.bean.FavoriteManager;
import com.ihandy.a2014011359.bean.NewsEntity;

public class FavoriteActivity extends BaseActivity implements View.OnClickListener {
    TextView title;
    ListView mListView;
    NewsAdapter mAdapter;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite);
        Log.i("create", "favor");
        initView();
        initData();
    }

    NewsEntity detailedNews;

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        mListView = (ListView) findViewById(R.id.favorite_list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(activity, DetailsActivity.class);
                detailedNews = mAdapter.getItem(position);
                intent.putExtra("news", detailedNews);
                startActivityForResult(intent, 1);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void initData() {
        title.setText("Favorite");
        mAdapter = new NewsAdapter(this, FavoriteManager.getNewsList());
        Log.i("ListSize", String.valueOf(FavoriteManager.getNewsList().size()));
        mListView.setAdapter(mAdapter);
    }

	/*@Override
    public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
//		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_text:
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("end", "code = " + resultCode);
        mAdapter.setNewsList(FavoriteManager.getNewsList());
        mAdapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }
}

