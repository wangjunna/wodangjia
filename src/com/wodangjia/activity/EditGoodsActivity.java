package com.wodangjia.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.app.App;
import com.wodangjia.bean.Goods_Type;
import com.wodangjia.bean.View_Goods;
import com.wodangjia.dialog.CustomProgressDialog;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;

public class EditGoodsActivity extends Activity implements OnClickListener {

	private Dialog dialog;// 获取图片的弹出框
	File file;
	LinearLayout ll_imgs;
	List<String> imgs_path;// 上传图片的路径
	ImageView addGoodsPic;
	String tempImgName;
	int index = -1;// 记录要替换的图片 当为-1时 为新增
	private int imgmaxCount=9;
	TextView tv_goods_type;
	EditText et_title, et_subtitle, et_price, et_stock;
	View_Goods item;
	ArrayList<Goods_Type> typeList;
	BitmapUtils bitmapUtils;
	private LayoutInflater mInflater;
	

	int select_type_id = -1;// 记录选择的类别id

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_goods);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_add_goods);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,
				R.anim.anim_right_to_left_out);
		initData();
		initView();
	}

	private void initData() {
		mInflater = LayoutInflater.from(this);
		imgs_path = new ArrayList<String>();
		bitmapUtils = new BitmapUtils(this);
		Intent intent = getIntent();
		item = (View_Goods) intent.getSerializableExtra(Config.KEY_ITEM);

	}

	private void initView() {
		ll_imgs = (LinearLayout) findViewById(R.id.ll_imgs);
		addGoodsPic = (ImageView) findViewById(R.id.iv_add_goods);
		addGoodsPic.setOnClickListener(this);
		findViewById(R.id.action_bar_to).setOnClickListener(this);
		findViewById(R.id.rl_goods_type).setOnClickListener(this);
		tv_goods_type = (TextView) findViewById(R.id.spinner_goods_type);
		tv_goods_type.setOnClickListener(this);
		et_price = (EditText) findViewById(R.id.et_price);
		et_price.setText("" + item.getGoods_price());
		et_title = (EditText) findViewById(R.id.et_title);
		et_title.setText(item.getGoods_title());
		et_stock = (EditText) findViewById(R.id.et_stock);
		et_stock.setText("" + item.getGoods_stock());
		et_subtitle = (EditText) findViewById(R.id.et_subtitle);
		et_subtitle.setText(item.getGoods_subtitle());
		et_price.addTextChangedListener(new PriceEditListener());
		getType();
		System.out.println("item.getGoods_imgs().get(i).getPath()");
		System.out.println(item.getGoods_imgs().size());

		for (int i = 0; i < item.getGoods_imgs().size(); i++) {
			System.out.println("item.getGoods_imgs().get(i).getPath()"
					+ item.getGoods_imgs().get(i).getPath());
			addImageView(item.getGoods_imgs().get(i).getPath());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.action_bar_to:
			// 信息完整性验证后调用提交
			if (checkGoodsParameter())
				uploadGoods();
			break;
		case R.id.iv_add_goods:// 点击添加商品图片
			// addImageView(v);
			if(imgs_path.size()>imgmaxCount){
				StringUtils.showToast(this, "最多上传9张商品图片");
				return;
			}
			index = -1;
			showPhotoDialog();
			break;
		case R.id.spinner_goods_type:
		case R.id.rl_goods_type:
			showTypeDialog();
			// getType();
			break;
		case R.id.openCamera:// 打开相机
			openCamera();
			break;
		case R.id.openPhones:// 打开图库
			openPhones();
			break;
		case R.id.cancel:// 取消
			dialog.cancel();
			break;

		default:
			break;
		}

	}

	private void uploadGoods() {

		final CustomProgressDialog pd = new CustomProgressDialog(
				EditGoodsActivity.this, "正在上传商品信息,请稍等！", R.anim.frame);
		pd.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_UPDATE_GOOD);
		params.addBodyParameter(Config.KEY_GOODS_ID, ""+item.getGoods_id());
		params.addBodyParameter(Config.KEY_TITLE, et_title.getText().toString());
		params.addBodyParameter(Config.KEY_SUBTITLE, et_subtitle.getText().toString());
		params.addBodyParameter(Config.KEY_STATUS, ""+item.getGoods_status());
		params.addBodyParameter(Config.KEY_PRICE, et_price.getText().toString());
		params.addBodyParameter(Config.KEY_STOCK, et_stock.getText().toString());
		params.addBodyParameter(Config.KEY_TYPE, "" + select_type_id);

		for (int i = 0; i < imgs_path.size(); i++) {
			System.out.println(imgs_path.get(0));
			if(imgs_path.get(i).startsWith("/imgs/")){
				params.addBodyParameter("" + i,imgs_path.get(i));
			}else {
				params.addBodyParameter("" + i, new File(imgs_path.get(i)));
			}
		}
		App.httpclient.configSoTimeout(5000);
		App.httpclient.send(HttpRequest.HttpMethod.POST, Config.UPLOAD_URL,
				params, new RequestCallBack<String>() {
					@Override
					public void onStart() {

					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// 上传中
						System.out.println("上传中  ");
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// 上传成功，这里面的返回值，就是服务器返回的数据
						// 使用 String result = responseInfo.result 获取返回值
						System.out.println("上传成功  ");
						StringUtils
								.showToast(EditGoodsActivity.this, "商品上传成功！");
						System.out.println(responseInfo.result);
						pd.dismiss();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// 上传失败
						pd.dismiss();
						StringUtils.showToast(EditGoodsActivity.this,
								"商品上传失败，请检查网络！");
						System.out.println("上传失败  ");
					}
				});
	}

	private void openPhones() {
		dialog.dismiss();
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, Config.REQUEST_CODE_OPENPICS);
	}

	private void openCamera() {
		dialog.dismiss();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		tempImgName = StringUtils.getImageSaveName();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				EditGoodsActivity.this.getExternalCacheDir(), tempImgName)));

		startActivityForResult(intent, Config.REQUEST_CODE_CAMERA);
	}

	private void showPhotoDialog() {
		View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog,
				null);
		view.findViewById(R.id.openCamera).setOnClickListener(this);
		view.findViewById(R.id.openPhones).setOnClickListener(this);
		view.findViewById(R.id.cancel).setOnClickListener(this);
		dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Config.REQUEST_CODE_CAMERA:
				File file = new File(this.getExternalCacheDir(), tempImgName);
				startPhotoZoom(Uri.fromFile(file));
				break;
			case Config.REQUEST_CODE_OPENPICS:
				Uri uri = data.getData();
				startPhotoZoom(uri);
				break;

			case Config.REQUEST_CODE_ZOOM:
				File file2 = new File(getExternalCacheDir(), tempImgName);
				addImageView(Uri.fromFile(file2));
				break;

			default:
				break;
			}
		}
	}

	void addImageView(String url) {
		System.out.println("---------------------");

		final View view = mInflater.inflate(R.layout.goods_img, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);

		bitmapUtils.display(imageView, Config.SERVER_HOST + url);
		final int n = imgs_path.size();// 当前商品图片数量
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				new AlertDialog.Builder(EditGoodsActivity.this)
						.setNegativeButton(R.string.cancle,
								new DialogInterface.OnClickListener() {

									// 取消
									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								})
						.setPositiveButton(R.string.delete,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
										// 删除
										dialog.dismiss();
										removeImageView((View) v.getParent());
									}
								}).show();

			}
		});
		ll_imgs.addView(view, n);
		imgs_path.add(url);

	}

	void addImageView(Uri uri) {
		String img_path = getExternalCacheDir() + File.separator + tempImgName;
		if (index == -1) {
			final ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					addGoodsPic.getWidth(), addGoodsPic.getHeight());
			params.setMargins(20, 0, 0, 0);
			imageView.setLayoutParams(params);
			imageView.setImageURI(uri);
			final int n = imgs_path.size();// 当前商品图片数量
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					new AlertDialog.Builder(EditGoodsActivity.this)
							.setNegativeButton(R.string.cancle,
									new DialogInterface.OnClickListener() {

										// 取消
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									})
							.setNeutralButton(
									getResources().getString(R.string.edit),
									new DialogInterface.OnClickListener() {
										// 编辑
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
											showPhotoDialog();
											index = ll_imgs.indexOfChild(v);

										}
									})
							.setPositiveButton(R.string.delete,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 删除
											dialog.dismiss();
											removeImageView(v);
										}
									}).show();

				}
			});
			ll_imgs.addView(imageView, n);
			imgs_path.add(img_path);
		} else {
			ImageView imageView = (ImageView) ll_imgs.getChildAt(index);
			imageView.setImageURI(uri);
			imgs_path.set(index, img_path);
		}

	}

	void removeImageView(View v) {
		imgs_path.remove(ll_imgs.indexOfChild(v));
		StringUtils.showToast(EditGoodsActivity.this,
				"" + ll_imgs.indexOfChild(v));
		ll_imgs.removeView(v);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 500);
		intent.putExtra("outputY", 500);
		intent.putExtra("return-data", true);
		tempImgName = StringUtils.getImageSaveName();
		intent.putExtra("output",
				Uri.fromFile(new File(this.getExternalCacheDir(), tempImgName)));
		startActivityForResult(intent, Config.REQUEST_CODE_ZOOM);
	}

	void showTypeDialog() {
		ActionSheetDialog dialog;
		if (typeList.size() > 0) {
			dialog = new ActionSheetDialog(EditGoodsActivity.this).builder()
					.setTitle("请选择商品类别").setCancelable(true)
					.setCanceledOnTouchOutside(true);
			for (int i = 0; i < typeList.size(); i++) {
				final String title = typeList.get(i).getType_title();
				final int type_id = typeList.get(i).getType_id();
				dialog.addSheetItem(title, SheetItemColor.Gray,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								System.out.println(which);
								System.out.println(title);
								tv_goods_type.setText(title);
								select_type_id = type_id;
							}
						});
			}
			dialog.show();
		}

	}

	// 验证商品的各个参数是否合法
	boolean checkGoodsParameter() {
		// 没有选择商品图片的
		if (imgs_path.size() == 0) {
			StringUtils.showToast(this, Config.ERROR_NO_IMGS);
			return false;
		} else if (et_title.getText().length() == 0) {
			StringUtils.showToast(this, Config.ERROR_NO_TITLE);
			et_title.requestFocus();
			return false;
		} else if (et_title.getText().length() < 3) {
			StringUtils.showToast(this, Config.ERROR_TITLE_TOSHORT);
			et_title.requestFocus();
			return false;
		} else if (et_subtitle.getText().length() == 0) {
			StringUtils.showToast(this, Config.ERROR_NO_SUBTITLE);
			et_subtitle.requestFocus();
			return false;
		} else if (et_subtitle.getText().length() < 10) {
			StringUtils.showToast(this, Config.ERROR_SUBTITLE_TOSHORT);
			et_subtitle.requestFocus();
			return false;
		} else if ("0.00".equals(et_price.getText().toString())) {
			StringUtils.showToast(this, Config.ERROR_NO_PRICE);
			et_price.requestFocus();
			return false;
		} else if (et_stock.getText().length() == 0) {
			StringUtils.showToast(this, Config.ERROR_NO_STOCK);
			et_stock.requestFocus();
			return false;
		} else if (select_type_id == -1) {
			StringUtils.showToast(this, Config.ERROR_NO_TYPE);
			return false;
		}
		return true;
	}

	void getType() {
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_GET_TYPES);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						StringUtils.showToast(EditGoodsActivity.this,
								Config.ERROR_GET_TYPES);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						System.out.println(arg0.result);
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status == Config.RESULT_STATUS_SUCCESS) {
							Gson gson = new Gson();
							Type type = new TypeToken<List<Goods_Type>>() {
							}.getType();
							typeList = gson.fromJson(
									JsonUtils.getJsonArray(arg0.result,
											"type_list").toString(), type);
							System.out.println("size---" + typeList.size());
							// showTypeDialog();
							tv_goods_type.setText(typeList.get(
									item.getGoods_type() - 1).getType_title());
							select_type_id=item.getGoods_type();

						}
					}
				});
	}

	class PriceEditListener implements TextWatcher {
		private boolean isChanged = false;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (isChanged) {// ----->如果字符未改变则返回
				return;
			}
			String str = s.toString();

			isChanged = true;
			String cuttedStr = str;
			/* 删除字符串中的dot */
			for (int i = str.length() - 1; i >= 0; i--) {
				char c = str.charAt(i);
				if ('.' == c) {
					cuttedStr = str.substring(0, i) + str.substring(i + 1);
					break;
				}
			}
			/* 删除前面多余的0 */
			int NUM = cuttedStr.length();
			int zeroIndex = -1;
			for (int i = 0; i < NUM - 2; i++) {
				char c = cuttedStr.charAt(i);
				if (c != '0') {
					zeroIndex = i;
					break;
				} else if (i == NUM - 3) {
					zeroIndex = i;
					break;
				}
			}
			if (zeroIndex != -1) {
				cuttedStr = cuttedStr.substring(zeroIndex);
			}
			/* 不足3位补0 */
			if (cuttedStr.length() < 3) {
				cuttedStr = "0" + cuttedStr;
			}
			/* 加上dot，以显示小数点后两位 */
			cuttedStr = cuttedStr.substring(0, cuttedStr.length() - 2) + "."
					+ cuttedStr.substring(cuttedStr.length() - 2);

			et_price.setText(cuttedStr);

			et_price.setSelection(et_price.length());
			isChanged = false;
		}
	}
}
