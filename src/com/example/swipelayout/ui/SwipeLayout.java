package com.example.swipelayout.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SwipeLayout extends FrameLayout {

	private ViewDragHelper mDragHelper;

	private Status mStatus = Status.Close;

	private OnSwipeLayouListener mSwipeLayouListener;

	private OnItemClickListener mOnItemClickListener;

	public OnSwipeLayouListener getSwipeLayouListener()
	{
		return mSwipeLayouListener;
	}

	public void setSwipeLayouListener(OnSwipeLayouListener mSwipeLayouListener)
	{
		this.mSwipeLayouListener = mSwipeLayouListener;
	}

	public static enum Status
	{
		Close, Open, Draing;
	}

	public static interface OnSwipeLayouListener {

		void onClose(SwipeLayout mSwipeLayout);

		void onOpen(SwipeLayout mSwipeLayout);

		void onDraging(SwipeLayout mSwipeLayout);

		void onStartClose(SwipeLayout mSwipeLayout);

		void onStartOpen(SwipeLayout mSwipeLayout);
	}

	/**
	 * 设置点击监听
	 * @param onItemClickListener
	 */
	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
		this.mOnItemClickListener = onItemClickListener;
	}

	/**
	 * item点击事件,防止冲突
	 *
	 *  SwipeLayout.java
	 *  Created on: 2015-12-8
	 *  @author 漆可
	 *
	 */
	public static interface OnItemClickListener {

		/**
		 *  点击事件
		 */
		void onClick();
	}

	public SwipeLayout(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public SwipeLayout(Context context)
	{
		this(context, null);
	}

	public SwipeLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		mDragHelper = ViewDragHelper.create(this, 1.0f, mCallback);
	}

	ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

		@Override
		public boolean tryCaptureView(View arg0, int arg1)
		{
			return true;
		}

		// 限定移动范围
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx)
		{
			// 修正移动
			if (child == mFrontView)
			{
				if (left > 0)
				{
					return 0;
				} else if (left < -mRang)
				{
					return -mRang;
				}
			} else if (child == mBackView)
			{
				if (left > mWidth)
				{
					return mWidth;
				} else if (left < mWidth - mRang)
				{
					return mWidth - mRang;
				}
			}

			return left;
		};

		@Override
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy)
		{
			// 传递事件
			if (changedView == mFrontView)
			{
				mBackView.offsetLeftAndRight(dx);
			} else if (changedView == mBackView)
			{
				mFrontView.offsetLeftAndRight(dx);
			}

			// 分发事件
			dispatchSwipeEvent();

			// 兼容老版本
			invalidate();
		};

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel)
		{

			if (xvel == 0 && releasedChild.getLeft() < -mRang / 2.0f)
			{
				open();
			} else if (xvel < 0)
			{
				open();
			} else
			{
				close();
			}
		}

	};
	private View mBackView;
	private View mFrontView;
	private int mWidth;
	private int mHeight;
	private int mRang;

	@Override
	public boolean onInterceptTouchEvent(android.view.MotionEvent ev)
	{
		return mDragHelper.shouldInterceptTouchEvent(ev);
	};

	// 分发事件
	protected void dispatchSwipeEvent()
	{
		if (mSwipeLayouListener != null)
		{
			mSwipeLayouListener.onDraging(this);
		}

		// 记录上一次的状态
		Status perStatus = mStatus;

		// 更新当前状态
		mStatus = updateStatus();
		if (perStatus != mStatus && mSwipeLayouListener != null)
		{
			if (mStatus == Status.Close)
			{
				mSwipeLayouListener.onClose(this);
			} else if (mStatus == Status.Open)
			{
				mSwipeLayouListener.onOpen(this);
			} else if (mStatus == Status.Draing)
			{
				if (perStatus == Status.Close)
				{
					mSwipeLayouListener.onStartOpen(this);
				} else if (perStatus == Status.Open)
				{
					mSwipeLayouListener.onStartClose(this);
				}
			}
		}
	}

	private Status updateStatus()
	{
		int left = mFrontView.getLeft();
		if (left == 0)
		{
			return Status.Close;
		} else if (left == -mRang)
		{
			return Status.Open;
		} else
		{
			return Status.Draing;
		}
	}

	public void open()
	{
		open(true);
	};

	public void close()
	{
		close(true);
	}

	public void open(boolean isSmooth)
	{
		int finalLeft = -mRang;
		if (isSmooth)
		{
			// 开始动画
			if (mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0))
			{
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else
		{
			layoutContent(true);
		}
	};

	public void close(boolean isSmooth)
	{
		if (isSmooth)
		{
			int finalLeft = 0;
			if (mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0))
			{
				ViewCompat.postInvalidateOnAnimation(this);
			}

		} else
		{

			layoutContent(false);
		}
	}

	@Override
	public void computeScroll()
	{
		super.computeScroll();

		if (mDragHelper.continueSettling(true))
		{
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	float x;

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// 点击
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			x = event.getX();
		} else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			float daltX = Math.abs(x - event.getX());
			if (daltX < 10 && mOnItemClickListener != null)
			{
				mOnItemClickListener.onClick();
			}
		}

		try
		{
			mDragHelper.processTouchEvent(event);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return true;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);

		// 摆放位置
		layoutContent(false);
	}

	private void layoutContent(boolean isOpen)
	{
		// 摆放前view
		Rect frontRect = compueFrontViewRect(isOpen);
		mFrontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);

		// 摆放后View
		Rect backRect = computeBackViewViaFront(frontRect);
		mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);

		// 调整顺序,把mFrontView前置
		bringChildToFront(mFrontView);
	}

	private Rect computeBackViewViaFront(Rect frontRect)
	{
		int left = frontRect.right;

		return new Rect(left, 0, left + mRang, 0 + mHeight);
	}

	private Rect compueFrontViewRect(boolean isOpen)
	{
		int left = 0;
		if (isOpen)
		{
			left = -mRang;
		}
		return new Rect(left, 0, left + mWidth, 0 + mHeight);
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();

		if (getChildCount() != 2)
		{
			return;
		}

		mBackView = getChildAt(0);
		mFrontView = getChildAt(1);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);

		mHeight = mFrontView.getMeasuredHeight();
		mWidth = mFrontView.getMeasuredWidth();

		mRang = mBackView.getMeasuredWidth();
	}
}
