package com.wodangjia.activity;

import java.io.File;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wodangjialayout.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wodangjia.app.App;
import com.wodangjia.bean.Certification;
import com.wodangjia.utils.ActivityUtils;
import com.wodangjia.utils.Config;
import com.wodangjia.utils.ImageVIewUtils;
import com.wodangjia.utils.JsonUtils;
import com.wodangjia.utils.StringUtils;
import com.zf.iosdialog.widget.CustomAlertDialog;

public class StroeManager extends BaseActivity implements OnClickListener {

	private LayoutInflater mInflater;
	private TextView tv_name, tv_certification, tv_delivery, tv_credit,
			tv_creat_time;
	private Dialog dialog;
	private ImageView stroe_img, iv_level;
	private BitmapUtils bitmapUtils;

	@SuppressLint("ResourceAsColor") @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_manager);
		ActivityUtils.setTranslucentStatus(getWindow(), true);
		ActivityUtils.setStatusBarColor(R.color.statusbar_bg, this);
		initData();
		initView();
	}

	private void initData() {
		mInflater = LayoutInflater.from(this);
		bitmapUtils=new BitmapUtils(this);
	}

	private void initView() {
		
		findViewById(R.id.rl_certification).setOnClickListener(this);
		findViewById(R.id.stroe_logo).setOnClickListener(this);
		stroe_img = (ImageView) findViewById(R.id.stroe_img);
		iv_level = (ImageView) findViewById(R.id.iv_level);
		findViewById(R.id.btn_back).setOnClickListener(this);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		LayoutParams params = stroe_img.getLayoutParams();
		params.height = screenWidth / 16 * 9;
		stroe_img.setLayoutParams(params);
		findViewById(R.id.rl_stroe_name).setOnClickListener(this);
		findViewById(R.id.rl_delivery).setOnClickListener(this);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_certification = (TextView) findViewById(R.id.tv_certification);
		tv_delivery = (TextView) findViewById(R.id.tv_delivery);
		tv_credit = (TextView) findViewById(R.id.tv_credit);
		tv_creat_time = (TextView) findViewById(R.id.tv_creat_time);
		updateUI();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_stroe_name:
			showStorenameDialog();
			break;
		case R.id.rl_delivery:
			showDeliveryDialog();
			break;
		case R.id.rl_certification:
			showCertificationDialog(App.user.isIs_certification());
			break;
		case R.id.stroe_logo:// 点击了更换店标
			showPhotoDialog();
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
		case R.id.btn_back:// 返回按钮
			ActivityUtils.finish(this);
			break;
		default:
			break;
		}

	}

	private void showStorenameDialog() {

		LinearLayout layout = (LinearLayout) mInflater.inflate(
				R.layout.dialog_change_stroename, null);
		final EditText et_storename = (EditText) layout
				.findViewById(R.id.et_storename);
		et_storename.setText(App.user.getStore_name());
		final CustomAlertDialog dialog = new CustomAlertDialog(this).builder()
				.setTitle("店铺名称").setView(layout)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		dialog.setPositiveButton("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!StringUtils.isStorename(StroeManager.this, et_storename)) {
					return;
				}
				uploadInfo(et_storename.getText().toString(), 0,
						Config.UPDATE_CODE_STROENAME, dialog);
			}
		});
		dialog.setCancelable(false);
		dialog.show();

	}

	private void showDeliveryDialog() {

		final RadioGroup radioGroup = (RadioGroup) mInflater.inflate(
				R.layout.dialog_store_delivery, null);
		if (App.user.isStore_delivery()) {
			radioGroup.check(R.id.delivery_1);
		} else {
			radioGroup.check(R.id.delivery_2);
		}

		final CustomAlertDialog dialog = new CustomAlertDialog(this).builder()
				.setTitle("减库存方式").setView(radioGroup)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		dialog.setPositiveButton("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (radioGroup.getCheckedRadioButtonId() == R.id.delivery_1) {
					uploadInfo("", 1, Config.UPDATE_CODE_DELIVERY, dialog);
				} else {
					uploadInfo("", 0, Config.UPDATE_CODE_DELIVERY, dialog);
				}

			}
		});
		dialog.setCancelable(false);
		dialog.show();

	}

	private void showCertificationDialog(boolean isCertification) {
		View view =mInflater.inflate(R.layout.dialog_change_certification,null);
		final CustomAlertDialog dialog = new CustomAlertDialog(this).builder()
				.setTitle("实名认证").setView(view);
		final EditText et_name=(EditText)view.findViewById(R.id.et_name);
		final EditText et_id_card=(EditText)view.findViewById(R.id.et_id_card);
		if(isCertification){
			RequestParams params=new RequestParams();
			params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_CERTIFICATION_INFO);
			App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					dialog.dismiss();
					StringUtils.showToast(StroeManager.this, Config.ERROR_UNCONNECTION_INTERNET);
					
				}
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					int status=JsonUtils.getInt(arg0.result, Config.KEY_STATUS);
					if(status==Config.RESULT_STATUS_SUCCESS){
						Gson gson=new Gson();
						Certification certification = gson.fromJson(JsonUtils.getJsonObject(arg0.result, Config.KEY_CERTIFICATION).toString(), Certification.class);
						if(certification==null){
							dialog.dismiss();
							StringUtils.showToast(StroeManager.this,Config.KEY_CERTIFICATION);
						}else{
							System.out.println(certification);
							et_name.setText(certification.getRelname());
							et_id_card.setText(certification.getId_card());
							App.user.setIs_certification(true);
							App.saveLoginStatus(App.user);
						}
					}
					
				}
			});
			et_name.setEnabled(false);
			et_id_card.setEnabled(false);
			dialog.setNegativeButton("确认", new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});
		}else{
			dialog.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
			dialog.setPositiveButton("申请认证", new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!StringUtils.certificationCheck(StroeManager.this,et_name,et_id_card)){
						return;
					}
					sendCertification(et_name.getText().toString(),et_id_card.getText().toString(),dialog);
				}
			});
		}	
		
		dialog.setCancelable(false);
		dialog.show();

	}

	protected void sendCertification(String name, String id_card,final CustomAlertDialog dialog) {
		RequestParams params=new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_CERTIFICATION);
		params.addBodyParameter(Config.KEY_NAME, name);
		params.addBodyParameter(Config.KEY_ID_CARD, id_card);
		App.httpclient.send(HttpMethod.POST, Config.API_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				StringUtils.showToast(StroeManager.this,Config.ERROR_UNCONNECTION_INTERNET);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				
				int status= JsonUtils.getInt(arg0.result,Config.KEY_STATUS);
				if(status==0){
					dialog.dismiss();
					StringUtils.showToast(StroeManager.this, Config.SUCCESS_CERTIFICATION);
					App.user.setIs_certification(true);
					App.saveLoginStatus(App.user);
					tv_certification.setText("已认证");
				}else{
					StringUtils.showToast(StroeManager.this,Config.ERROR_CERTIFICATION );
				}
				
			}
		});
		
	}

	protected void uploadInfo(final String string, final int int0,
			final int uploadCode, final CustomAlertDialog dialog) {

		RequestParams params = new RequestParams();
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_UPDATE_STORE);
		params.addBodyParameter(Config.KEY_TYPE, "" + uploadCode);
		params.addBodyParameter(Config.KEY_STRING, string);
		params.addBodyParameter(Config.KEY_INT, "" + int0);

		App.httpclient.send(HttpMethod.POST, Config.API_URL, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						StringUtils
								.showToast(StroeManager.this, "提交失败，网络链接失败！");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						int status = JsonUtils.getInt(arg0.result,
								Config.KEY_STATUS);
						if (status != 0) {
							StringUtils.showToast(StroeManager.this,
									"修改失败,登录状态已过期!");
							return;
						}
						switch (uploadCode) {
						case Config.UPDATE_CODE_STROENAME:
							App.user.setStore_name(string);
							tv_name.setText(string);
							break;
						case Config.UPDATE_CODE_DELIVERY:
							if (int0 == 0) {
								App.user.setStore_delivery(false);
							} else {
								App.user.setStore_delivery(true);
							}
							break;

						default:
							break;
						}
						App.saveLoginStatus(App.user);
						updateUI();
					}
				});
	}

	void updateUI() {
		if(App.user==null){
			return;
		}
		bitmapUtils.display(stroe_img, Config.SERVER_HOST+App.user.getStore_img_path());
		tv_name.setText(App.user.getStore_name());
		if (App.user.isIs_certification()) {
			tv_certification.setText("已认证");
		} else {
			tv_certification.setText("未认证");
		}
		if (App.user.isStore_delivery()) {
			tv_delivery.setText("拍下减库存");
		} else {
			tv_delivery.setText("付款减库存");
		}
		tv_creat_time.setText(App.user.getUser_create_time());
		int credit = App.user.getStore_credit();
		tv_credit.setText("" + credit);
		ImageVIewUtils.setLevel(credit,iv_level);

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
				StroeManager.this.getExternalCacheDir(), "temp_photo.png")));
		startActivityForResult(intent, Config.REQUEST_CODE_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("resultCode:" + resultCode);
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
		intent.putExtra("aspectX", 16);
		intent.putExtra("aspectY", 9);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 450);
		intent.putExtra("return-data", true);
		intent.putExtra("output",
				Uri.fromFile(new File(this.getExternalCacheDir(), "temp.png")));
		startActivityForResult(intent, Config.REQUEST_CODE_ZOOM);
	}

	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			stroe_img.setImageBitmap(photo);
		}
	}

	private void showPhotoDialog() {
		View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog,null);
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
		params.addBodyParameter(Config.KEY_ACTION, Config.ACTION_ADD_STORE_LOGO);
		params.addBodyParameter("msg", "上传店铺图片");
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
						int status=JsonUtils.getInt(responseInfo.result, Config.KEY_STATUS);
						if(status==0){
							String url=JsonUtils.getString(responseInfo.result, Config.KEY_URL);
							App.user.setStore_img_path(url);
							App.saveLoginStatus(App.user);
						}
					}
					@Override
					public void onFailure(HttpException error, String msg) {
						// 上传失败
						System.out.println("上传失败  ");
					}
				});

	}
}
