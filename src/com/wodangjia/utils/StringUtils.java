package com.wodangjia.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import android.R.string;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StringUtils {

	public static boolean checkAddress(Context context, EditText et_name,
			EditText et_phone, TextView county, EditText et_detail) {
		if (!isName(context, et_name)) {
			return false;
		} else if (!isPhone(context, et_phone)) {
			return false;
		} else if (!isCounty(context, county)) {
			return false;
 		} else if (!isDetail(context, et_detail)) {
			return false;
		}

		return true;
	}

	private static boolean isCounty(Context context, TextView county) {
		if(county.getText().toString().equals("未设定")){
			showToast(context, "请选择收货地区");
			return false;
		}
		return true;
	}

	private static boolean isDetail(Context context, EditText et_detail) {
		if (et_detail.length() == 0) {
			showToast(context, "请输入详细地址！");
			return false;
		} else if (et_detail.length() < 8) {
			showToast(context, "详细地址至少为8个字符！");
			return false;
		}
		return true;
	}

	public static boolean registerCheck(Context context, EditText et_phone,
			EditText et_code, EditText et_password) {// 验证手机号
		boolean isPass = true;
		if (!isPhone(context, et_phone)) {
			isPass = false;
		} else if (!isVerCode(context, et_code)) {
			isPass = false;
		} else if (!isPassword(context, et_password)) {
			isPass = false;
		}
		return isPass;
	}

	public static boolean loginCheck(Context context, EditText et_phone,
			EditText et_password) {// 验证手机号
		boolean isPass = true;
		if (!isPhone(context, et_phone)) {
			isPass = false;
		} else if (!isPassword(context, et_password)) {
			isPass = false;
		}
		return isPass;
	}

	public static boolean certificationCheck(Context context, EditText et_name,
			EditText et_id_card) {// 验证手机号
		boolean isPass = true;
		String name = et_name.getText().toString();
		String id_card = et_id_card.getText().toString();

		if (name.length() == 0) {
			showToast(context, Config.ERROR_NAME_NULL);
			isPass = false;
		} else if (name.length() < 2) {
			showToast(context, Config.ERROR_NAME_FORMAT);
			isPass = false;
		} else if (id_card.length() == 0) {
			showToast(context, Config.ERROR_ID_CARD_NULL);
			isPass = false;
		} else if (id_card.length() != 18) {
			showToast(context, Config.ERROR_ID_CARD_FORMAT);
			isPass = false;
		}
		return isPass;
	}

	public static boolean fastLoginCheck(Context context, EditText et_phone,
			EditText et_code) {// 验证手机号
		boolean isPass = true;
		if (!isPhone(context, et_phone)) {
			isPass = false;
		} else if (!isVerCode(context, et_code)) {
			isPass = false;
		}
		return isPass;
	}

	public static boolean isPhone(Context context, EditText et_phone) {
		boolean isPass = true;
		String phone = et_phone.getText().toString();
		if (phone.length() == 0) {
			showToast(context, Config.ERROR_PHONE_NULL);
			isPass = false;
		} else if (!phone.matches("^[1][3,5,7,8][0-9]{9}$")) {
			showToast(context, Config.ERROR_PHONE_FORMAT);
			isPass = false;
		}
		return isPass;
	}

	public static boolean isPassword(Context context, EditText et_password) {
		boolean isPass = true;
		String password = et_password.getText().toString();
		if (password.length() == 0) {
			showToast(context, Config.ERROR_PASSWORD_NULL);
			isPass = false;
		} else if (password.length() < 6 || password.length() > 16) {
			showToast(context, Config.ERROR_PASSWORD_FORMAT);
			isPass = false;
		}
		return isPass;
	}

	public static boolean isStorename(Context context, EditText et_storename) {
		boolean isPass = true;
		String password = et_storename.getText().toString();
		if (password.length() == 0) {
			showToast(context, Config.ERROR_STORENAME_NULL);
			isPass = false;
		} else if (password.length() < 3 || password.length() > 10) {
			showToast(context, Config.ERROR_STORENAME_FORMAT);
			isPass = false;
		}
		return isPass;
	}

	public static boolean isNickname(Context context, EditText et_nickname) {
		boolean isPass = true;
		String password = et_nickname.getText().toString();
		if (password.length() == 0) {
			showToast(context, Config.ERROR_STORENAME_NULL);
			isPass = false;
		}
		return isPass;
	}

	public static boolean isVerCode(Context context, EditText et_code) {
		boolean isPass = true;
		String code = et_code.getText().toString();
		if (code.length() == 0) {
			showToast(context, Config.ERROR_VERCODE_NULL);
			isPass = false;
		} else if (code.length() != 4) {
			showToast(context, Config.ERROR_VERCODE_FORMAT);
			isPass = false;
		}
		return isPass;
	}

	public static boolean VerGoodsInfo(Context context, EditText et_title,
			EditText et_subtitle, EditText et_price, TextView tv_type) {

		return false;

	}

	// 消息提示
	public static void showToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static String MD5(String string) { // 字符串MD5加密
		byte[] hash;

		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}

		return hex.toString();
	}

	public static String getImageSaveName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String name = sdf.format(new java.util.Date()) + ".png";
		return name;
	}

	public static boolean isName(Context context, EditText et_name) {
		if (et_name.length() == 0) {
			showToast(context, "收货人不能为空！");
			return false;
		} else if (et_name.length() > 10 || et_name.length() < 2) {
			showToast(context, "收货人姓名格式不能正确！");
			return false;
		}
		return true;
	}
}
