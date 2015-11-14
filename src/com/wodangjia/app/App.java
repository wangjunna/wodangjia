package com.wodangjia.app;

import io.rong.app.message.ContactNotificationMessageProvider;
import io.rong.app.message.DeAgreedFriendRequestMessage;
import io.rong.app.message.DemoCommandNotificationMessage;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.loopj.android.http.PersistentCookieStore;

import com.wodangjia.bean.View_UserStore;
import com.wodangjia.utils.ACache;
import com.wodangjia.utils.StringUtils;

public class App extends Application {
	public static HttpUtils httpclient;// 异步网络请求
	public static View_UserStore user;
	public static ACache mCache;
	public static String  packageName;
	public static Context context;
	static PersistentCookieStore myCookieStore;

	@Override
	public void onCreate() {
		super.onCreate();
		packageName=getApplicationInfo().packageName;
		context=getApplicationContext();
		/**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
		
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
//            RongCloudEvent.init(this);
            try {
                RongIM.registerMessageType(DemoCommandNotificationMessage.class);
                RongIM.registerMessageType(DeAgreedFriendRequestMessage.class);
                RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());

            } catch (Exception e) {
                e.printStackTrace();
            }
          
        }
		mCache = ACache.get(this);
		httpclient = new HttpUtils();
		// PreferencesCookieStore myCookieStore =new
		// PreferencesCookieStore(this);
		myCookieStore = new PersistentCookieStore(this);
		
		httpclient.configCookieStore(myCookieStore);

		System.out.println("=========" + mCache.getAsObject("user"));
		user = (View_UserStore) mCache.getAsObject("user");
		// user=(User) mCache.getAsObject("user");
		// System.out.println(mCache.getAsString("user1"));
	}

	public static void saveLoginStatus(View_UserStore user) {
		App.user = user;
		mCache.put("user", user);
		System.out.println(user);
	}

	/**
	 * 获得当前进程的名字
	 * 
	 * @param context
	 * @return 进程号
	 */
	public static String getCurProcessName(Context context) {

		int pid = android.os.Process.myPid();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {

			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}
	
	 /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public  static void connect(String token) {

        if (packageName.equals(App.getCurProcessName(context))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {

                    Log.d("LoginActivity", "--onSuccess" + userid);
//                    startActivity(new Intent(IndexActivity.this, ConversationListActivity.class));
//                    finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }
    public static void logout() {
		user=null;
		saveLoginStatus(user);
		myCookieStore.clear();
		httpclient.configCookieStore(myCookieStore);
		RongIM.getInstance().logout();
		
	}
    public String getVersion() {
    	// 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
    	 PackageInfo packInfo = null;
    	 String version = null;
    	try {
    		packInfo = packageManager.getPackageInfo(getPackageName(),0);
    		 version = packInfo.versionName;
             StringUtils.showToast(this, version);
    	} catch (NameNotFoundException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return version;
	}
}
