package com.wodangjia.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.app.App;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.ImageVIewUtils;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.wodangjia.view.CircleImageView;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zf.iosdialog.widget.CustomAlertDialog;

public class UserInfoActivity extends BaseActivity implements OnClickListener {
	private BitmapUtils bitmapUtils;
	private CircleImageView civ_photo;
	private LayoutInflater mInflater;
	Dialog dialog;
	private TextView tv_nickname, tv_gender, tv_school, tv_buyer_credit,
			tv_sale_credit;
	private ImageView iv_buy_level, iv_sale_level;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		ActivityUtils.setActionBarLayout(getActionBar(), this,
				R.layout.title_bar_userinfo);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.actionbar_bg, this);
		overridePendingTransition(R.anim.anim_right_to_left_in,
				R.anim.anim_right_to_left_out);
		initData();
		initView();
		updateUI();
	}

	private void initData() {
		bitmapUtils = new BitmapUtils(this);
		mInflater = LayoutInflater.from(this);
	}

	private void updateUI() {
		if(App.user==null)
			return;
		bitmapUtils.display(civ_photo,
				Config.SERVER_HOST + App.user.getUser_photo());
		tv_nickname.setText(App.user.getUser_nickname());
		if (App.user.isUser_gender()) {
			tv_gender.setText("男");

		} else {
			tv_gender.setText("女");
		}
		tv_school.setText(App.user.getUser_school());
		tv_buyer_credit.setText("" + App.user.getUser_credit());
		tv_sale_credit.setText("" + App.user.getStore_credit());
		ImageVIewUtils.setLevel(App.user.getUser_credit(), iv_buy_level);
		ImageVIewUtils.setLevel(App.user.getStore_credit(), iv_sale_level);

	}

	// 初始化视图 绑定事件监听器
	private void initView() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		civ_photo = (CircleImageView) findViewById(R.id.userinfo_civ_photo);
		civ_photo.setOnClickListener(this);
		
		iv_buy_level=(ImageView) findViewById(R.id.iv_buyer_credit_enter);
		iv_sale_level=(ImageView) findViewById(R.id.iv_seller_credit_enter);
		
		findViewById(R.id.userinfo_rl_photo).setOnClickListener(this);
		findViewById(R.id.userinfo_rl_nickname).setOnClickListener(this);
		findViewById(R.id.userinfo_rl_gender).setOnClickListener(this);
		findViewById(R.id.userinfo_rl_school).setOnClickListener(this);
		findViewById(R.id.userinfo_rl_buyer_credit).setOnClickListener(this);
		findViewById(R.id.userinfo_rl_seller_credit).setOnClickListener(this);

		tv_nickname = (TextView) findViewById(R.id.nickname_value);
		tv_gender = (TextView) findViewById(R.id.gender_value);
		tv_school = (TextView) findViewById(R.id.school_value);
		tv_buyer_credit = (TextView) findViewById(R.id.buyer_credit_value);
		tv_sale_credit = (TextView) findViewById(R.id.seller_credit_value);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_back:// 点击了返回
			StringUtils.showToast(this, "返回");
			ActivityUtils.finish(this);
			break;
		case R.id.userinfo_civ_photo:// 点击了头像图片
			StringUtils.showToast(this, "点击了头像图片");
			showPhotoDialog();
			break;
		case R.id.userinfo_rl_photo:// 点击了头像rl
			StringUtils.showToast(this, "点击了头像布局");
			break;
		case R.id.userinfo_rl_nickname:// 点击了昵称布局
			StringUtils.showToast(this, "点击了昵称布局");
			showNicknameDialog();
			break;
		case R.id.userinfo_rl_gender:// 点击了性别布局
			StringUtils.showToast(this, "点击了性别布局");
			showGenderDialog();
			break;
		case R.id.userinfo_rl_school:// 点击了学校布局
			StringUtils.showToast(this, "点击了学校布局");
			showSchoolDialog();
			break;
		case R.id.userinfo_rl_buyer_credit:// 点击了买家信誉度
			StringUtils.showToast(this, "点击了买家信誉度");
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

	private void showNicknameDialog() {
		LinearLayout layout = (LinearLayout) mInflater.inflate(
				R.layout.dialog_change_stroename, null);
		final EditText et_nickname = (EditText) layout
				.findViewById(R.id.et_storename);
		et_nickname.setText(App.user.getUser_nickname());
		final CustomAlertDialog dialog = new CustomAlertDialog(this).builder()
				.setTitle("昵称").setView(layout)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		dialog.setPositiveButton("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!StringUtils.isNickname(UserInfoActivity.this, et_nickname)) {
					return;
				}
				uploadInfo(et_nickname.getText().toString(), 0,
						Config.UPDATE_CODE_NICKNAME, dialog);
			}
		});
		dialog.setCancelable(false);
		dialog.show();

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
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				UserInfoActivity.this.getExternalCacheDir(), "temp_photo.png")));
		startActivityForResult(intent, Config.REQUEST_CODE_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("resultCode:" + resultCode);
		StringUtils.showToast(this, ""+requestCode+"---"+resultCode);
		switch (requestCode) {
		// 如果是直接从相册获取
		case Config.REQUEST_CODE_OPENPICS:
			if (data != null)
				startPhotoZoom(data.getData());
			break;
		// 如果是调用相机拍照时
		case Config.REQUEST_CODE_CAMERA:
			System.out.println("----" + data);
			if (resultCode == RESULT_OK) {
				File file = new File(this.getExternalCacheDir(),
						"temp_photo.png");
				startPhotoZoom(Uri.fromFile(file));
			}
			break;
		// 取得裁剪后的图片
		case Config.REQUEST_CODE_ZOOM:
			if (resultCode == RESULT_OK) {
				if (data != null) {
					// dataIntent = data;
					setPicToView(data);
					uploadPhoto();
				}
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
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
		intent.putExtra("output",
				Uri.fromFile(new File(this.getExternalCacheDir(), "temp.png")));
		startActivityForResult(intent, Config.REQUEST_CODE_ZOOM);
	}

	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			civ_photo.setImageBitmap(photo);
		}
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
	public void onBackPressed() {
		ActivityUtils.finish(this);
	};

	void uploadPhoto() {
		String uploadHost = Config.UPLOAD_URL; // 服务器接收地址
		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_UPDATE_USER);
		params.addBodyParameter("img1", new File(this.getExternalCacheDir(),
				"temp.png")); // filePath是手机获取的图片地址
		sendImgToServer(params, uploadHost);
	}

	private void sendImgToServer(RequestParams params, String uploadHost) {
		App.httpclient.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						// 上传开始
						System.out.println("开始上传");
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
						int status = JsonUtils.getInt(responseInfo.result,
								Config.KEY_STATUS);
						if (status == 0) {
							String url = JsonUtils.getString(
									responseInfo.result, Config.KEY_URL);
							App.user.setUser_photo(url);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// 上传失败
						System.out.println("上传失败  ");
					}
				});

	}

	protected void uploadInfo(final String string, final int int0,
			final int uploadCode, final CustomAlertDialog dialog) {

		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_UPDATE_USER);
		params.addBodyParameter(Config.KEY_TYPE, "" + uploadCode);
		params.addBodyParameter(Config.KEY_STRING, string);
		params.addBodyParameter(Config.KEY_INT, "" + int0);

		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						StringUtils.showToast(UserInfoActivity.this,
								"提交失败，网络链接失败！");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						if(dialog!=null){
							dialog.dismiss();
						}
						
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status != 0) {
							StringUtils.showToast(UserInfoActivity.this,
									"修改失败,登录状态已过期!");
							return;
						}
						switch (uploadCode) {
						case Config.UPDATE_CODE_NICKNAME:
							App.user.setUser_nickname(string);
							break;
						case Config.UPDATE_CODE_GENDER:
							if (int0 == 0) {
								App.user.setUser_gender(false);
							} else {
								App.user.setUser_gender(true);
							}
							break;
						case Config.UPDATE_CODE_SCHOOL:
							App.user.setUser_school(string);
							break;
						default:
							break;
						}
						App.saveLoginStatus(App.user);
						updateUI();
					}
				});

	}

	private void showGenderDialog() {

		final RadioGroup radioGroup = (RadioGroup) mInflater.inflate(
				R.layout.dialog_user_gender, null);
		if (App.user.isUser_gender()) {
			radioGroup.check(R.id.delivery_1);
		} else {
			radioGroup.check(R.id.delivery_2);
		}

		final CustomAlertDialog dialog = new CustomAlertDialog(this).builder()
				.setTitle("请选择性别").setView(radioGroup)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		dialog.setPositiveButton("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (radioGroup.getCheckedRadioButtonId() == R.id.delivery_1) {
					uploadInfo("", 1, Config.UPDATE_CODE_GENDER, dialog);
				} else {
					uploadInfo("", 0, Config.UPDATE_CODE_GENDER, dialog);
				}

			}
		});
		dialog.setCancelable(false);
		dialog.show();

	}
	private void showSchoolDialog() {

		new ActionSheetDialog(UserInfoActivity.this)
		.builder()
		.setCancelable(true)
		.setCanceledOnTouchOutside(true)
		.addSheetItem("洛阳师范学院", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						uploadInfo("洛阳师范学院", 0, Config.UPDATE_CODE_SCHOOL, null);
					}
				})
		.addSheetItem("南阳师范学院", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						uploadInfo("南阳师范学院", 0, Config.UPDATE_CODE_SCHOOL, null);
					}
				}).show();

	}
}
