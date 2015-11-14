package krelve.view;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.example.wodangjialayout.R;
import com.lidroid.xutils.BitmapUtils;
import com.wodangjia.bean.CarouselFigure;
import com.wodangjia.utils.Config;

public class Kanner extends FrameLayout {
	private int count;
	private BitmapUtils bitmapUtils;
	private List<ImageView> imageViews;
	private Context context;
	private ViewPager vp;
	private boolean isAutoPlay;
	private int currentItem;
	private int delayTime;
	private KannerPagerAdapter adapter;
	private LinearLayout ll_dot;
	private List<ImageView> iv_dots;
	private Handler handler = new Handler();

	public Kanner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initImageLoader(context);
		initData();
	}

	public Kanner(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Kanner(Context context) {
		this(context, null);
	}

	public int getCurrentItem() {
		return currentItem;
	}

	private void initData() {
		adapter=new KannerPagerAdapter();
		imageViews = new ArrayList<ImageView>();
		iv_dots = new ArrayList<ImageView>();
		delayTime = 2000;
	}

	public void setImagesUrl(String[] imagesUrl) {
		initLayout();
		initImgFromNet(imagesUrl);
		showTime();
	}
	public void setImagesUrl(ArrayList<CarouselFigure> cflist) {
		initLayout();
		initImgFromNetWithClick(cflist);
		showTime();
	}

	public void setImagesRes(int[] imagesRes) {
		initLayout();
		initImgFromRes(imagesRes);
		showTime();
	}

	private void initLayout() {
		imageViews.clear();
		View view = LayoutInflater.from(context).inflate(
				R.layout.kanner_layout, this, true);
		vp = (ViewPager) view.findViewById(R.id.vp);
		ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
		ll_dot.removeAllViews();
	}

	private void initImgFromRes(int[] imagesRes) {
		count = imagesRes.length;
		for (int i = 0; i < count; i++) {
			ImageView iv_dot = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			iv_dot.setImageResource(R.drawable.dot_blur);
			ll_dot.addView(iv_dot, params);
			iv_dots.add(iv_dot);
		}
		iv_dots.get(0).setImageResource(R.drawable.dot_focus);

		for (int i = 0; i <= count + 1; i++) {
			ImageView iv = new ImageView(context);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setBackgroundResource(R.drawable.loading);
			if (i == 0) {
				iv.setImageResource(imagesRes[count - 1]);
			} else if (i == count + 1) {
				iv.setImageResource(imagesRes[0]);
			} else {
				iv.setImageResource(imagesRes[i - 1]);
			}

			imageViews.add(iv);
		}
	}

	private void initImgFromNet(String[] imagesUrl) {
		count = imagesUrl.length;
		for (int i = 0; i < count; i++) {
			ImageView iv_dot = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			iv_dot.setImageResource(R.drawable.dot_blur);
			ll_dot.addView(iv_dot, params);
			iv_dots.add(iv_dot);
		}

		iv_dots.get(0).setImageResource(R.drawable.dot_focus);

		for (int i = 0; i <= count + 1; i++) {
			ImageView iv = new ImageView(context);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setBackgroundResource(R.drawable.loading);
			if (i == 0) {
				bitmapUtils.display(iv, imagesUrl[count - 1]);
			} else if (i == count + 1) {
				bitmapUtils.display(iv, imagesUrl[0]);
			} else {
				bitmapUtils.display(iv, imagesUrl[i - 1]);
			}
			imageViews.add(iv);
		}
	}
	private void initImgFromNetWithClick(final ArrayList<CarouselFigure> cflist) {
		imageViews.clear();
		ll_dot.removeAllViews();
		count = cflist.size();
		for (int i = 0; i < count; i++) {
			ImageView iv_dot = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			iv_dot.setImageResource(R.drawable.dot_blur);
			ll_dot.addView(iv_dot, params);
			iv_dots.add(iv_dot);
		}

		iv_dots.get(0).setImageResource(R.drawable.dot_focus);

		for (int i = 0; i <= count + 1; i++) {
			final int position=i;
			ImageView iv = new ImageView(context);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setBackgroundResource(R.drawable.loading);
			if (i == 0) {
				bitmapUtils.display(iv, Config.SERVER_HOST+ cflist.get(count - 1).getImgUrl());
			} else if (i == count + 1) {
				bitmapUtils.display(iv,  Config.SERVER_HOST+cflist.get(0).getImgUrl());
			} else {
				bitmapUtils.display(iv, Config.SERVER_HOST+cflist.get(i - 1).getImgUrl());
			}
			iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String url ;
					if (position == 0) {
						url = cflist.get(count - 1).getUrl();
					} else if (position == count + 1) {
						url =  cflist.get(0).getUrl();
					} else {
						url =  cflist.get(position - 1).getUrl();
					}
					System.out.println(url);
					Uri uri = Uri.parse(url);  
				    context.startActivity(new Intent(Intent.ACTION_VIEW,uri));  
				}
			});
			imageViews.add(iv);
		}
//		adapter.notifyDataSetChanged();
	}

	private void showTime() {
		
		vp.setAdapter(adapter);
		vp.setFocusable(true);
		vp.setCurrentItem(1);
		currentItem = 1;
		vp.addOnPageChangeListener(new MyOnPageChangeListener());
		startPlay();
	}

	private void startPlay() {
		if(!isAutoPlay){
			isAutoPlay = true;
			handler.postDelayed(task, 2000);
		}
		
	}

	public void initImageLoader(Context context) {
		bitmapUtils=new BitmapUtils(context);
	}

	private final Runnable task = new Runnable() {

		@Override
		public void run() {
			if (isAutoPlay) {
				currentItem = currentItem % (count + 1) + 1;
				if (currentItem == 1) {
					vp.setCurrentItem(currentItem, false);
					handler.post(task);
				} else {
					vp.setCurrentItem(currentItem);
					handler.postDelayed(task, 3000);
				}
			} else {
				handler.postDelayed(task, 5000);
			}
		}
	};

	class KannerPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(imageViews.get(position));
			return imageViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageViews.get(position));
		}

	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:
				isAutoPlay = false;
				break;
			case 2:
				isAutoPlay = true;
				break;
			case 0:
				if (vp.getCurrentItem() == 0) {
					vp.setCurrentItem(count, false);
				} else if (vp.getCurrentItem() == count + 1) {
					vp.setCurrentItem(1, false);
				}
				currentItem = vp.getCurrentItem();
				isAutoPlay = true;
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < iv_dots.size(); i++) {
				if (i == arg0 - 1) {
					iv_dots.get(i).setImageResource(R.drawable.dot_focus);
				} else {
					iv_dots.get(i).setImageResource(R.drawable.dot_blur);
				}
			}
		}

	}

}
