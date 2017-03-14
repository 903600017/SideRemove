package com.example.swipelayout;

import com.example.swipelayout.adapter.MyAdapter;
import com.example.swipelayout.ui.SwipeLayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends Activity {

	protected String TAG = "tag";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ListView listView = (ListView) findViewById(R.id.lv);
		
		listView.setAdapter(new MyAdapter());

		/*
		 * SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.sl);
		 * 
		 * swipeLayout.setSwipeLayouListener(new
		 * SwipeLayout.OnSwipeLayouListener() {
		 * 
		 * @Override public void onStartOpen(SwipeLayout mSwipeLayout) {
		 * Log.d(TAG, "onStartOpen"); }
		 * 
		 * @Override public void onStartClose(SwipeLayout mSwipeLayout) {
		 * Log.d(TAG, "onStartClose");
		 * 
		 * }
		 * 
		 * @Override public void onOpen(SwipeLayout mSwipeLayout) { Log.d(TAG,
		 * "onOpen");
		 * 
		 * }
		 * 
		 * @Override public void onDraging(SwipeLayout mSwipeLayout) {
		 * 
		 * }
		 * 
		 * @Override public void onClose(SwipeLayout mSwipeLayout) { Log.d(TAG,
		 * "onClose");
		 * 
		 * } }); 
		 */
	}
}
