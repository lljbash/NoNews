package com.ihandy.a2014011359;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ihandy.a2014011359.base.BaseActivity;

public class FavoriteActivity extends BaseActivity implements View.OnClickListener {
    TextView title;
    TextView right_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initView();
        initData();
    }
    private void initView() {
        title = (TextView)findViewById(R.id.title);
    }

    private void initData() {
        title.setText("Favorite");
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
}

