package com.wodangjia.utils;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.wodangjia.bean.User;

public class Config {

	public static final String APP_ID = "com.example.wodangjialayout";
	
	
	public static final String CHARSET = "UTF-8";

//	public static final String SERVER_HOST = "http://192.168.191.1:8080";
//	public static final String PROJECT="/wdj";
	
	public static final String SERVER_HOST = "http://115.28.70.156:8080";
	public static final String PROJECT="";
	
	public static final String API_URL = SERVER_HOST+PROJECT+"/api";
	public static final String UPLOAD_URL = SERVER_HOST+PROJECT+"/upload";
	public static final String APP_DOWNLOAD_URL = "http://fir.im/nkr1";
//	public static final String API_URL_USER = API_URL + "/usermanager";
//	public static final String API_URL_GOODS = API_URL + "/goodsmanager";
	
	//smssdk 接口
	public static final String SMSSDK_APPKEY="ada9569df712";
	public static final String SMSSDK_APPSECRET="14eceb0fc51f9c80a384988ad105adbd";
	public static final String COUNTRY_CODE_CHINA="86";
	

	public static final String KEY_ACTION = "action";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_STATUS = "status";
	public static final String KEY_CODE = "code";
	public static final String KEY_PASSWORD_MD5="password_md5";
	public static final String KEY_USER="user";
	public static final String KEY_MSG="msg";
	public static final String KEY_TITLE="title";
	public static final String KEY_SUBTITLE="subtitle";
	public static final String KEY_PRICE="price";
	public static final String KEY_STOCK="stock";
	public static final String KEY_TYPE="type";
	public static final String KEY_ITEM="item";
	public static final String KEY_GOODSLIST="goodslist";
	public static final String KEY_STORELIST="storelist";
	public static final String KEY_SIZE="size";
	public static final String KEY_KEYWORD="keyword";
	public static final String KEY_PAGE="page";
	public static final String KEY_COMMENTLIST="commentlist";
	public static final String KEY_GOODS_ID="goods_id";
	public static final String KEY_USER_ID="user_id";
	public static final String KEY_SORT="sort";
	public static final String KEY_COUNT="count";
	public static final String KEY_STORE_ID="store_id";
	public static final String KEY_STORE="store";
	public static final String KEY_STORE_NAME="store_name";
	public static final String KEY_STRING="string";
	public static final String KEY_INT="int";
	public static final String KEY_NAME="name";
	public static final String KEY_ID_CARD="id_card";
	public static final String KEY_CERTIFICATION="certification";
	public static final String KEY_URL="url";
	public static final String KEY_SCHOOLLIST="schoollist";
	public static final String KEY_SCHOOL="school";
	public static final String KEY_GOODS_STATUS="goods_status";
	public static final String KEY_OPERATION="operation";
	public static final String KEY_CARTITEMLIST="cartitemlist";
	public static final String KEY_ADDRESSLIST="addresslist";
	public static final String KEY_ADDRESS_ID="address_id";
	public static final String KEY_ADDRESS="address";
	public static final String KEY_ORDER_INFO="order_info";
	public static final String KEY_ORDERIDS="orderids";
	public static final String KEY_ORDER_ID="order_id";
	public static final String KEY_REQUESTCODE="RequestCode";
	public static final String KEY_COMMENTS="comments";
	public static final String KEY_CONTACT="contact";
	public static final String KEY_FEEDBACK="feedback";
	
	
	
	public static final String ACTION_REGISTER = "register";
	public static final String ACTION_LOGIN = "login";
	public static final String ACTION_LOGIN_FAST = "login_fast";
	public static final String ACTION_SEND_CODE = "send_code";
	public static final String ACTION_ADD_GOOD = "add_goods";
	public static final String ACTION_UPDATE_GOOD = "update_goods";
	public static final String ACTION_GET_TYPES = "get_types";
	public static final String ACTION_GET_GOODS = "get_goods";
	public static final String ACTION_USER_GOODS_LIST = "user_goods_list";
	public static final String ACTION_SEARCH_GOODS = "search_goods";
	public static final String ACTION_GET_GOODS_COMMENT = "get_goods_comment";
	public static final String ACTION_COLLECT_GOODS = "collect_goods";
	public static final String ACTION_CANCLE_COLLECT_GOODS = "cancle_collect_goods";
	public static final String ACTION_COLLECT_STORE = "collect_store";
	public static final String ACTION_CANCLE_COLLECT_STORE = "cancle_collect_store";
	public static final String ACTION_GET_GOODS_IS_COLLECT = "get_goods_is_collect";
	public static final String ACTION_GET_STORE_IS_COLLECT = "get_store_is_collect";
	public static final String ACTION_GET_SHOW_NAME = "get_show_name";
	public static final String ACTION_SHOPPING_CART = "shopping_cart";
	public static final String ACTION_UPDATE_STORE = "update_store";
	public static final String ACTION_UPDATE_USER = "update_user";
	public static final String ACTION_CERTIFICATION = "certification";
	public static final String ACTION_CERTIFICATION_INFO= "certification_info";
	public static final String ACTION_ADD_STORE_LOGO= "add_store_logo";
	public static final String ACTION_GET_SCHOOL_LIST= "get_school_list";
	public static final String ACTION_CATEGORY_GOODSLIST="category_goodslist";
	public static final String ACTION_WALLET_INFO="wallet_info";
	public static final String ACTION_STORE_INFO="store_info";
	public static final String ACTION_GET_STROE_GOODS="get_stroe_goods";
	public static final String ACTION_CAROUSEL_FIGURE="carousel_figure";
	public static final String ACTION_UPADE_SHOPPING_CART="upade_shopping_cart";
	public static final String ACTION_SHOPPING_CART_INFO="shopping_cart_info";
	public static final String ACTION_ADDRESS_LIST="address_list";
	public static final String ACTION_CREATE_ADDRESS="create_address";
	public static final String ACTION_DELETE_ADDRESS="delete_address";
	public static final String ACTION_UPDATE_ADDRESS="update_address";
	public static final String ACTION_SET_DEFAULT_ADDRESS="set_default_address";
	public static final String ACTION_GET_DEFAULT_ADDRESS="get_default_address";
	public static final String ACTION_SUBMIT_ORDER="submit_order";
	public static final String ACTION_PAY_ORDER="pay_order";
	public static final String ACTION_GET_ORDERLIST="get_orderlist";
	public static final String ACTION_GET_STORE_ORDERLIST="get_store_orderlist";
	public static final String ACTION_UPDATE_ORDER_STATUS="update_order_status";
	public static final String ACTION_SUBMIT_GOODS_COMMENTS="submit_goods_comments";
	public static final String ACTION_GET_TODAY_INFO="get_today_info";
	public static final String ACTION_SEARCH_STORE="search_store";
	public static final String ACTION_SUBMIT_FEEDBACK="submit_feedback";
	public static final String ACTION_GET_NEWEST_VERSION="get_newest_version";
	public static final String ACTION_GET_COLLECTION_GOODS="get_collection_goods";
	public static final String ACTION_GET_COLLECTION_STORE="get_collection_store";
	public static final String ACTION_CHANGE_PASSWORD="change_password";
	public static final String ACTION_CHECK_VERIFICATION_CODE="check_verification_code";
	public static final String ACTION_CHANGE_PHONE="change_phone";
	public static final String ACTION_GET_INDEX_STORE="get_index_store";
	public static final String ACTION_GET_ALL_INCOME="get_all_income";
	
	
	public static final String OPERATION__ONSALE="onsale";
	public static final String OPERATION__OFFSALE="offsale";
	public static final String OPERATION__EIDT="edit";
	public static final String OPERATION__DELETE="delete";
	

	public static final int RESULT_STATUS_SUCCESS = 0;
	public static final int RESULT_STATUS_FAIL_LOGIN = 1;
	public static final int RESULT_STATUS_FAIL = 1;
	public static final int RESULT_STATUS_INVALID_TOKEN = 2;
	public static final int RESULT_STATUS_NOTFOUND = 3;
	public static final int RESULT_STATUS_BAN = -1;//没有访问权限
	
	
	public static final int STATUS_NO_USER_SESSION=-1;//无权请求
	public static final int STATUS_COLLECTIONGOODS_ERR=1;//收藏商品失败
	

	public static final String SUCCESS_SEND_CODE = "验证码已发送至您的手机,有效时间5分钟，请及时填写！";
	public static final String SUCCESS_COLLECT_GOODS = "收藏商品成功！";
	public static final String SUCCESS_CANCLE_COLLECT_GOODS = "已取消收藏商品！";
	public static final String SUCCESS_COLLECT_STORE = "收藏商品成功！";
	public static final String SUCCESS_CANCLE_COLLECT_STORE = "已取消收藏商品！";
	public static final String SUCCESS_CERTIFICATION = "认证成功！";
	public static final String SUCCESS_FEEDBACK = "您的反馈已提交成功！";
	public static final String ERROR_CERTIFICATION = "认证失败！";
	public static final String ERROR_SEND_CODE = "验证码发送失败，请检查网络稍候重试！";
	public static final String ERROR_CODE_ERR = "验证码无效！";
	public static final String ERROR_LOGIN_FAIL = "登录失败，手机号或密码错误！";
	public static final String ERROR_UNCONNECTION_INTERNET = "连接服务器失败，请检查网络！";
	public static final String ERROR_PHONE_FORMAT = "手机号码格式错误！";
	public static final String ERROR_PHONE_NULL = "手机号码不能为空！";
	public static final String ERROR_PASSWORD_NULL = "密码不能为空！";
	public static final String ERROR_PASSWORD_FORMAT = "密码长度应为6-16位！";
	public static final String ERROR_VERCODE_NULL = "验证码不能为空！";
	public static final String ERROR_VERCODE_FORMAT = "验证码为4位数字！";
	public static final String ERROR_SERVER_EXCEPTION = "服务器异常，请联系管理员！";
	public static final String ERROR_GET_TYPES = "获取类别数据异常，请稍候重试！";
	public static final String ERROR_NO_IMGS = "至少选择一张商品图片！";
	public static final String ERROR_NO_TITLE = "商品标题不能为空！";
	public static final String ERROR_TITLE_TOSHORT = "商品标题长度至少为三个字符！";
	public static final String ERROR_NO_SUBTITLE = "商品描述不能为空！";
	public static final String ERROR_SUBTITLE_TOSHORT = "商品描述长度至少为十个字符！";
	public static final String ERROR_NO_PRICE = "商品价格不能为0.00！";
	public static final String ERROR_NO_STOCK = "商品库存不能为空！";
	public static final String ERROR_NO_TYPE = "请选择商品的分类！";
	public static final String ERROR_COLLECT_GOODS = "收藏商品失败！";
	public static final String ERROR_CANCLE_COLLECT_GOODS = "取消收藏商品失败！";
	public static final String ERROR_LOGIN_TIMEOUT = "登录身份过期！";
	public static final String ERROR_STORENAME_NULL = "店铺名称不能为空！";
	public static final String ERROR_STORENAME_FORMAT = "店铺名称应为3-10个字符！";
	public static final String ERROR_NAME_NULL = "姓名不能为空";
	public static final String ERROR_NAME_FORMAT = "姓名格式不正确";
	public static final String ERROR_ID_CARD_NULL = "身份证号不能为空";
	public static final String ERROR_ID_CARD_FORMAT = "身份证号格式不正确";
	public static final String ERROR_GET_CERTIFICATION_INFO = "获取认证信息失败！";
	
	
	
	
	public static final String MSG_NO_MORE = "已全部加载";

	public static final int MSG_WHAT_SHOWTOAST=0;


	public static final int REQUEST_CODE_OPENPICS=11;
	public static final int REQUEST_CODE_CAMERA=12;
	public static final int REQUEST_CODE_ZOOM=13;
	public static final int REQUEST_CODE_LOGIN=14; //登录
	public static final int REQUEST_CODE_EDIT=15;//编辑商品
	public static final int REQUEST_CODE_COMMENT=16;//提交评论
	public static final int REQUEST_CODE_BUY_ORDER=17;//已买到商品跳转首页


	

	
	public static final int UPDATE_CODE_STROENAME=1;//修改店铺名称
	public static final int UPDATE_CODE_STROEIMG=2;//修改店铺图片
	public static final int UPDATE_CODE_CERTIFICATION=3;//修改店铺认证状态
	public static final int UPDATE_CODE_DELIVERY=4;//修改店铺减库存方式
	public static final int UPDATE_CODE_CREDIT=5;//修改店铺信誉度

	public static final int UPDATE_CODE_NICKNAME=1;//修改昵称
	public static final int UPDATE_CODE_PASSWORD=2;//修改密码
	public static final int UPDATE_CODE_GENDER=3;//修改性别
	public static final int UPDATE_CODE_SCHOOL=4;//修改学校
	public static final int UPDATE_CODE_PHOTO=5;//修改头像
	public static final int UPDATE_CODE_PHONE=6;//修改手机号码
	public static final int UPDATE_CODE_BUY_CREDIT=7;//修改手机号码
	
	
	public static final int ORDER_BY_GOODS_COLLECT_DESC=0;//人气优先
	public static final int ORDER_BY_GOODS_PRICE_DESC=4;//按价格排序
	public static final int ORDER_BY_GOODS_PRICE_ASC=3;//价格最低
	public static final int ORDER_BY_GOODS_SALES_DESC=2;//按销量排序
	public static final int ORDER_BY_GOODS_STAR_DESC=1;//好评优先
	
	public static final int ORDER_BY_GOODS_CREAT_TIME_DESC=5;//时间降序
	public static final int ORDER_BY_GOODS_CREAT_TIME_ASC=6;//时间升序
	
	
	
	public static final int SHOPPING_CART_SUB=3;//增加购物车
	public static final int SHOPPING_CART_ADD=1;//增加购物车
	public static final int SHOPPING_CART_UPDATE=1;//更新购物车
	public static final int SHOPPING_CART_DELETE=2;//删除购物车

}
