package com.example.swipelayout.adapter;

import java.util.ArrayList;

import com.example.swipelayout.R;
import com.example.swipelayout.ui.SwipeLayout;

import android.view.View;
import static com.example.swipelayout.utils.Cheeses.NAMES;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private ArrayList<SwipeLayout> openItems;//记录打开的item

	public MyAdapter()
	{
		super();
		
		openItems = new ArrayList<>();
	}

	@Override
	public int getCount()
	{
		return NAMES.length;
	}

	@Override
	public Object getItem(int position)
	{
		return NAMES[position];
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = View.inflate(parent.getContext(), R.layout.lv_item, null);
		}

		ViewHolder holder = ViewHolder.getHolder(convertView);

		holder.tv_name.setText(NAMES[position]);

		SwipeLayout swipeLayout = (SwipeLayout) convertView;

		swipeLayout.setSwipeLayouListener(new SwipeLayout.OnSwipeLayouListener() {

			@Override
			public void onStartOpen(SwipeLayout mSwipeLayout)
			{
				
				//要开启时先遍历所有已打开条目，逐个关闭
				for (SwipeLayout item : openItems)
				{
					item.close();
				}
				
				openItems.clear();
			}

			@Override
			public void onStartClose(SwipeLayout mSwipeLayout)
			{
				
			}

			@Override
			public void onOpen(SwipeLayout mSwipeLayout)
			{
				//添加进集合
				openItems.add(mSwipeLayout);
			}

			@Override
			public void onDraging(SwipeLayout mSwipeLayout)
			{

			}

			@Override
			public void onClose(SwipeLayout mSwipeLayout)
			{
				//移除集合
				openItems.remove(mSwipeLayout);
			}
		});

		return convertView;
	}

	private static class ViewHolder {

		public TextView tv_name;
		public TextView tv_call;
		public TextView tv_delete;

		public static ViewHolder getHolder(View convertView)
		{
			Object tag = convertView.getTag();
			if (tag != null)
			{
				return (ViewHolder) tag;
			} else
			{
				ViewHolder holder = new ViewHolder();

				holder.tv_call = (TextView) convertView.findViewById(R.id.tv_call);
				holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

				convertView.setTag(holder);
				return holder;
			}

		}
	}

}
