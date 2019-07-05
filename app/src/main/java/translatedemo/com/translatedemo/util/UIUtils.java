package translatedemo.com.translatedemo.util;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xiaweizi.cornerslibrary.CornersProperty;
import com.xiaweizi.cornerslibrary.RoundCornersTransformation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.activity.MainActivity;
import translatedemo.com.translatedemo.application.BaseApplication;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.contans.Contans;


public class UIUtils {


	public static  int[] image1 = {
			R.mipmap.lg_image2,
			R.mipmap.lg_image3,
			R.mipmap.lg_image1,
			R.mipmap.lg_image14,
			R.mipmap.image_my,
			R.mipmap.lg_image4,
			R.mipmap.lg_image11,
			R.mipmap.lg_image6,
			R.mipmap.lg_image7,
			R.mipmap.lg_image8,
			R.mipmap.lg_image5,
			R.mipmap.lg_image9,
			R.mipmap.lg_image10,
			R.mipmap.lg_image12,
			R.mipmap.lg_image13,
			R.mipmap.image_yl,
			R.mipmap.image_lfhly,
			R.mipmap.image_amhly,
			R.mipmap.image_alab,
			R.mipmap.image_asabj,
			R.mipmap.image_bsjer,
			R.mipmap.image_bols,
			R.mipmap.image_bjly,
			R.mipmap.image_mjly,
			R.mipmap.image_bslyy,
			R.mipmap.image_jtlnyy,
			R.mipmap.image_swy,
			R.mipmap.image_kxjy,
			R.mipmap.image_jacky,
			R.mipmap.image_wesy,
			R.mipmap.image_danmy,
			R.mipmap.image_xilayu,
			R.mipmap.image_wordy,
			R.mipmap.image_aslyy,
			R.mipmap.image_bakyy,
			R.mipmap.image_bsy,
			R.mipmap.image_fenlany,
			R.mipmap.image_flby,
			R.mipmap.image_feijiyu,
			R.mipmap.image_fulixiyu,
			R.mipmap.image_aerlany,
			R.mipmap.image_sglangaiery,
			R.mipmap.image_jialixiyay,
			R.mipmap.image_jjlty,
			R.mipmap.image_hsy,
			R.mipmap.image_xwyy,
			R.mipmap.image_xbolaiyu,
			R.mipmap.image_yindiyu,
			R.mipmap.image_kluodiyay,
			R.mipmap.image_haidikeliaoeryu,
			R.mipmap.image_xyaliyu,
			R.mipmap.image_yameiliyayu,
			R.mipmap.image_yiboyu,
			R.mipmap.image_bindaotyu,
			R.mipmap.image_yinliwazhuayu,
			R.mipmap.image_gelujiyayu,
			R.mipmap.image_hashakeyu,
			R.mipmap.image_hashakeyuxilier,
			R.mipmap.image_gaomianyu,
			R.mipmap.image_kaladayu,
			R.mipmap.image_hanyu,
			R.mipmap.image_kuerdeyu,
			R.mipmap.image_jierjisiyu,
			R.mipmap.image_ladingyu,
			R.mipmap.image_lshenbaiyu,
			R.mipmap.image_laowoyu,
			R.mipmap.image_litaowanyu,
			R.mipmap.image_latuoweiyayu,
			R.mipmap.image_maerjiasyu,
			R.mipmap.image_maliyu,
			R.mipmap.image_maoliyu,
			R.mipmap.image_maqidunyu,
			R.mipmap.image_malayalamuyu,
			R.mipmap.image_mengguyuxler,
			R.mipmap.image_maladiyu,
			R.mipmap.image_shandimaliyu,
			R.mipmap.image_maqitayu,
			R.mipmap.image_baimiaowen,
			R.mipmap.image_miandianyu,
			R.mipmap.image_niboeryu,
			R.mipmap.image_helanyu,
			R.mipmap.image_luoweiyu,
			R.mipmap.image_qiqiewayu,
			R.mipmap.image_kelegeluotuoaimiyu,
			R.mipmap.image_qiongzhepuyu,
			R.mipmap.image_papiawentuoyi,
			R.mipmap.image_bolanyu,
			R.mipmap.image_pushentuyu,
			R.mipmap.image_luomaniyayu,
			R.mipmap.image_xindeyu,
			R.mipmap.image_shenjialuoyu,
			R.mipmap.image_siluofuckyu,
			R.mipmap.unage_silewenniyayu,
			R.mipmap.image_saboyayu,
			R.mipmap.image_xiulayu,
			R.mipmap.image_suomaliyu,
			R.mipmap.image_aerbaliyayu,
			R.mipmap.image_saierweiyayu,
			R.mipmap.image_saisuotuoyu,
			R.mipmap.image_yinnizhuandiyu,
			R.mipmap.image_ruidianyu,
			R.mipmap.image_siwaxiliyu,
			R.mipmap.image_taimieryu,
			R.mipmap.image_tailuguyu,
			R.mipmap.image_tajikeyu,
			R.mipmap.image_taiyu,
			R.mipmap.image_tangjiayu,
			R.mipmap.image_tuerqiyu,
			R.mipmap.image_dadayu,
			R.mipmap.image_taxitiyu,
			R.mipmap.image_wudeerteyu,
			R.mipmap.image_wukelanyu,
			R.mipmap.image_wuerduyu,
			R.mipmap.image_wuzibiekeyu,
			R.mipmap.image_lanfeishakeyu,
			R.mipmap.image_yidixuyu,
			R.mipmap.image_yuelubayu,
			R.mipmap.image_youkatanmayayu,
			R.mipmap.image_yueyu,
			R.mipmap.image_lanfeizulu

	};

	public static MainActivity mainactivity = null;
	private static String Ppath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/translateimage/";

	public static Context getContext() {
		return BaseApplication.mContext;
	}

	/**
	 * 判断是否是6.0以上
	 *
	 * @return
	 */
	public static boolean isMarshmallow() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	/**
	 * 获取uuid
	 *
	 * @return
	 */
	public static String getUUidString() {
		return UUID.randomUUID().toString();
	}

	/**
	 * dip转换px
	 */
	public static int dip2px(int dip) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}


	public static String gettime(String time) {

		Date date1=null;
		SimpleDateFormat simdate1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str1="2015-1-25";
		try {
			date1=simdate1.parse(str1);
			SimpleDateFormat simdate=new SimpleDateFormat("yyyy-MM-dd");
			return simdate.format(date1);
		} catch (ParseException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(date1);


		return time;

	}


	/**
	 * pxz转换dip
	 */
	public static int px2dip(int px) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * inflate打气�?
	 */
	public static View inflate(int resId) {
		return LayoutInflater.from(getContext()).inflate(resId, null);
	}

	/**
	 * 当使用了自定义样式时使用此方法，context应传Activity.context
	 */
	public static View inflate(Context context, int resId) {
		return LayoutInflater.from(context).inflate(resId, null);
	}

	public static View inflate(Context context, int resId, ViewGroup root, boolean attachToRoot) {
		return LayoutInflater.from(context).inflate(resId, root, attachToRoot);
	}

	/**
	 * 获取资源
	 */
	public static Resources getResources() {

		return getContext().getResources();
	}

	/**
	 * 获取文字
	 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/**
	 * 获取文字数组
	 */
	public static String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	/**
	 * 获取dimen
	 */
	public static int getDimens(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}

	/**
	 * 获取drawable
	 */
	public static Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	/**
	 * 获取颜色
	 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/**
	 * 获取颜色选择�?
	 */
	public static ColorStateList getColorStateList(int resId) {
		return getResources().getColorStateList(resId);
	}


	/**
	 * 隐藏电话号码
	 */
	public static String getphone(String phone) {
		try {


			return phone.substring(0, 4) + "****" + phone.substring(phone.length() - 3, phone.length());
		} catch (Exception e) {
			return phone;
		}
	}



	/**
	 * view转bitmap
	 *
	 * @param addViewContent
	 * @return
	 */
	public static Bitmap getViewBitmap(View addViewContent) {

		addViewContent.setDrawingCacheEnabled(true);

		addViewContent.measure(
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		addViewContent.layout(0, 0,
				addViewContent.getMeasuredWidth(),
				addViewContent.getMeasuredHeight());

		addViewContent.buildDrawingCache();
		Bitmap cacheBitmap = addViewContent.getDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

		return bitmap;
	}
	/**
	 * 包名获取图标
	 * @param packageName
	 * @return
	 */
	public static Drawable getAppIcon(String packageName){
		//		Log.d(TAG, "-------------->packageName :"+packageName);
		PackageManager pm = UIUtils.getContext().getPackageManager();
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {

				if(packageInfo.packageName.equals(packageName)){

					return packageInfo.applicationInfo.loadIcon(pm);
				}
			}

		}
		return null;
	}

	// 判断当前的线程是不是在主线程
	public static boolean isRunInMainThread() {
		return android.os.Process.myTid() == BaseApplication.mMainThreadId;
	}
	/** 在主线程执行runnable */
	public static boolean post(Runnable runnable) {
		return BaseApplication.mHandler.post(runnable);
	}

	public static void runInMainThread(Runnable runnable) {
		// 在主线程运行
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}
	public static int getScreenWidth(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = wm.getDefaultDisplay().getWidth();//屏幕宽度
		return screenWidth;
	}

	public static int getScreenHeigh(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int screenHeight = wm.getDefaultDisplay().getHeight();
		return screenHeight;
	}

	/**
	 * 判断是否手机号
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneNumber){
		Pattern compileImage = Pattern.compile(Contans.PHONE_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = compileImage.matcher(phoneNumber);
		if (matcher.matches()) {
			//是手机号
			return true;
		}
		return false;
	}

	/**
	 * 判断密码是否规范
	 * @param password
	 * @return
	 */
	public static boolean isPasswordFormat(String password){

		Pattern compileImage = Pattern.compile(Contans.PASSWORD_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = compileImage.matcher(password);
		if (matcher.matches()) {
			//是手机号
			return true;
		}


			return false;
	}



	/**
	 * 获取当前本地apk的版本
	 *
	 * @param mContext
	 * @return
	 */
	public static int getVersionCode(Context mContext) {
		int versionCode = 0;
		try {
			//获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = mContext.getPackageManager().
					getPackageInfo(mContext.getPackageName(), 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 获取版本号名称
	 *
	 * @param context 上下文
	 * @return
	 */
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().
					getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}

		/*View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)。
		View.INVISIBLE：隐藏状态栏，同时Activity会伸展全屏显示。
		View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
		View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
		View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
		View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。*/
	public static void  showFullScreen (AppCompatActivity activity,boolean isFullScreen){
		if (isFullScreen){
			//得到当前界面的装饰视图
			if(Build.VERSION.SDK_INT >= 21) {
				View decorView = activity.getWindow().getDecorView();
				//让应用主题内容占用系统状态栏的空间,注意:下面两个参数必须一起使用 stable 牢固的
				int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
				decorView.setSystemUiVisibility(option);
				//设置状态栏颜色为透明
				activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
				//隐藏标题栏

			}else {
				activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}
		}else {
			//隐藏
			if (Build.VERSION.SDK_INT >= 21){
				View decorView = activity.getWindow().getDecorView();
				//让应用主题内容占用系统状态栏的空间,注意:下面两个参数必须一起使用 stable 牢固的
				int option = View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_VISIBLE;
				decorView.setSystemUiVisibility(option);
				//设置状态栏颜色为透明
				activity.getWindow().setStatusBarColor(UIUtils.getColor(R.color.colorPrimaryDark));

			}else {
			activity.getWindow().setFlags(View.SYSTEM_UI_FLAG_VISIBLE,View.SYSTEM_UI_FLAG_VISIBLE);

			}
		}

	}

	/**
	 * 保留两位小数
	 * @param money
	 * @return
	 */
	public static String getMoney(double money){
		DecimalFormat df   = new DecimalFormat("######0.00");

		return  df.format(money);
	}
	/**
	 * 加载图片
	 * @param context
	 * @param imagUrl
	 * @param imageView
	 */
	public static void loadImageView(Context context, String imagUrl, ImageView imageView){
		Glide.with(context).load(imagUrl).asBitmap().error(R.mipmap.buffer).placeholder(R.mipmap.buffer).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
	}

	/**
	 * 加载圆角图片
	 * * @param context
	 * @param imagUrl
	 * @param imageView
	 */
	public static void loadImageViewRoud(Context context, String imagUrl, ImageView imageView,int radius){
		CornersProperty cornersProperty=new CornersProperty();
		cornersProperty.setCornersType(CornersProperty.CornerType.ALL);
		cornersProperty.setCornersRadius(radius);
		RoundCornersTransformation roundCornersTransformation=new RoundCornersTransformation(context,cornersProperty);

		Glide.with(context).load(imagUrl).bitmapTransform(roundCornersTransformation).error(R.mipmap.buffer).placeholder(R.mipmap.buffer).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
	}

	/**
	 * 是否昵称
	 * @param nike
	 * @return
	 */
		public static boolean  isNick(String nike){
			// 仅仅同意字母、数字和汉字
			String   regEx  =  "^[a-zA-Z0-9	\\u4E00-\\u9FA5]+$";
			Pattern   p   =   Pattern.compile(regEx);
			Matcher m   =   p.matcher(nike);
		if (m.matches()){
			return true;
		}
			return false;

		}

	/**
	 * 是否收货地址
	 * @param nike
	 * @return
	 */
	public static boolean  isorderpath(String nike){
		// 仅仅同意字母、数字和汉字
		String   regEx  =  "^[\\u4E00-\\u9FA5A-Za-z0-9\\s\\$\\(\\)\\.\\-]+$";
		Pattern   p   =   Pattern.compile(regEx);
		Matcher m   =   p.matcher(nike);
		if (m.matches()){
			return true;
		}
		return false;

	}
//
//	/**
//	 * ios风格toast
//	 * @param type 2：成功   1：警告
//	 * @param tips 提示内容
//	 */
//	public static void showCarToast(Context context,int type,String tips){
//		ShopCarToastView shopCarToastView = new ShopCarToastView(context);
//		if (1 == type) {
//			shopCarToastView.setIcon(R.mipmap.address_error);
//		} else if (2 == type){
//			shopCarToastView.setIcon(R.mipmap.address_success);
//		}
//		shopCarToastView.setText(tips);
//		shopCarToastView.setDuration(Toast.LENGTH_LONG);
//		shopCarToastView.show();
//	}

	/**
	 * 楼盘显示价格
	 * @param houseType
	 * @param textView
	 * @param price
	 */
	public static void  showPrice(int houseType, TextView textView,String price){
		if (houseType==Contans.HOUSE_PRESELL_TYPE){

			textView.setText("预售");

		}else if (houseType==Contans.HOUSE_ONSALE_TYPE){
			textView.setText(price);

		}else if (houseType==Contans.HOUSE_SELLOUT_TYPE){
			textView.setText("售罄");
		}


	}

	/**
	 *
	 * @param mcontext 上下文
	 * @param view 显示的自定义viw
	 * @param view2 相对位置
	 * @return
	 */

	public static PopupWindow showBottomPopwindow( final Activity mcontext,View view ,View view2 ){

		lightoff(mcontext);


			// 参数2,3：指明popupwindow的宽度和高度
		PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.WRAP_CONTENT);

			popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
				@Override
				public void onDismiss() {
					UIUtils.lighton(mcontext);
				}
			});


			// 设置背景图片， 必须设置，不然动画没作用
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setFocusable(true);

			// 设置点击popupwindow外屏幕其它地方消失
			popupWindow.setOutsideTouchable(true);

			// 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
					Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
			animation.setInterpolator(new AccelerateInterpolator());
			animation.setDuration(200);
		// 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
		popupWindow.showAtLocation(view2, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		view.startAnimation(animation);
		return popupWindow;
	}

	/**
	 * 设置手机屏幕亮度灰色半透明
	 */
	public static void lightoff(Activity mcontext) {
		WindowManager.LayoutParams lp = mcontext.getWindow().getAttributes();
		lp.alpha = 0.3f;
		mcontext.getWindow().setAttributes(lp);
	}

	/**
	 * 设置手机屏幕亮度显示正常
	 */
	public static void lighton(Activity mcontext) {
		WindowManager.LayoutParams lp =mcontext. getWindow().getAttributes();
		lp.alpha = 1f;
		mcontext.getWindow().setAttributes(lp);
	}

	/**
	 * 非空判定
	 * @param velue 传入值
	 * @return 为空返回true 不为空返回false
	 */
	public static boolean isEntmy(String velue){
		if(velue.equals("")||velue==null||velue.length()==0){
			return true;
		}else{
			return false;
		}

	}

	/**
	 * 打开网络地址
	 * @param url
	 * @return
	 */
	public static void openWebUrl(Context mContext,String url){

		new LogUntil(mContext,"weburl__:",url);
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		mContext.startActivity(intent);
	}





	public static String chulishoujih(String phone){
		return phone.substring(0,3)+"****"+phone.substring(phone.length()-4,phone.length());
	}


	/***
	 * 使用缓存获取图片流
	 * @param imageepath
	 * @return
	 */
	public static Bitmap getImageBitmap(String imageepath){
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inTempStorage = new byte[200 * 1024];// 设置缓存
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;// 设置编码格式
		bitmapOptions.inPurgeable = true;// 回收
		return BitmapFactory.decodeFile(imageepath, bitmapOptions);
	}
	public static String saveMyBitmap(Bitmap mBitmap, String bitName) {
		String path = Ppath + bitName + ".png";
		File f = new File(path);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getRealFilePath( final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	/**
	 * 判定是否为英文
	 * @param charaString
	 * @return
	 */

	public static boolean isEnglish(String charaString){

		return charaString.matches("^[a-zA-Z]*");

	}


	public static String gettime(double motn,Context mContext){
		String year = "";
	   if(motn>0){

	     switch ((int)motn){
			 case 12:

					 year = mContext.getResources().getString(R.string.oneyear);
			 	break;
			 case 6:
				 year = mContext.getResources().getString(R.string.byear);
			 	break;
			 case 3:
				 year = mContext.getResources().getString(R.string.onejidu);
			 	break;
			 case 1:
				 year = mContext.getResources().getString(R.string.onemonth);
			 	break;
		 }

	    }

		return year;
	};


	public static List<String> getFilesAllName(String path) {
		File file=new File(path);
		File[] files=file.listFiles();
		if (files == null){
			Log.e("error","空目录");
			return null;}
		List<String> s = new ArrayList<>();
		for(int i =0;i<files.length;i++){
			if(files[i].getAbsolutePath().endsWith(".json")) {
				Log.e("path_info",files[i].getAbsolutePath());
				s.add(files[i].getAbsolutePath());
			}
		}
		return s;
	}

	public static String getJson(String fileName) {
		StringBuilder stringBuilder = new StringBuilder();
        Scanner sc = null;
		try {
			InputStream is = new FileInputStream(new File(fileName));


            sc = new Scanner(is,"UTF-8");
            while (sc.hasNextLine()) {
//                String line = ;
                stringBuilder.append(sc.nextLine());
                // System.out.println(line);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}


		if (sc != null) {
			sc.close();
		}

		return stringBuilder.toString();

	}

	public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
		Type type = new TypeToken<ArrayList<JsonObject>>() {
		}.getType();
		ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

		ArrayList<T> arrayList = new ArrayList<>();
		for (JsonObject jsonObject : jsonObjects) {
			arrayList.add(new Gson().fromJson(jsonObject, clazz));
		}
		return arrayList;

	}

	public static String getAppVersionName(Context context) {
		String versionName = "";
		int versionCode = 0;
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			versionCode = pi.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (versionName == null || versionName.length() <= 0) {
			versionName = "";
		}

		return versionName;
	}

	/**
	 * 设置img标签下的width为手机屏幕宽度，height自适应
	 *
	 * @param data html字符串
	 * @return 更新宽高属性后的html字符串
	 */
	public static  String getNewData(String data ,Context mCo) {
		Document document = Jsoup.parse(data);
		int screenWidth = getScreenWidth(mCo);
		Elements pElements = document.select("p:has(img)");
		for (Element pElement : pElements) {
			pElement.attr("style", "text-align:center");
			pElement.attr("max-width", String.valueOf(screenWidth + "px"))
					.attr("height", "auto");
			pElement.attr("width", String.valueOf(screenWidth + "px"));


		}
		Elements imgElements = document.select("img");
		for (Element imgElement : imgElements) {
			//重新设置宽高
			imgElement.attr("max-width", "100%")
					.attr("height", "auto");
			imgElement.attr("width", "100%");
			imgElement.attr("style", "max-width:100%;height:auto");
		}
		Log.i("newData:", document.toString());
		return document.toString();
	}

	/**
	 * 设置img标签下的width为手机屏幕宽度，height自适应
	 *
	 * @param data html字符串
	 * @return 更新宽高属性后的html字符串
	 */
	public static  String getNewData1(String data ,Context mCo) {
		Document document = Jsoup.parse(data);
		int screenWidth = getScreenWidth(mCo);
		Elements pElements = document.select("p:has(iframe)");
		for (Element pElement : pElements) {
			pElement.attr("style", "text-align:center");
			pElement.attr("width", String.valueOf(screenWidth + "px"));

		}
		Elements imgElements = document.select("iframe");
		for (Element imgElement : imgElements) {
			//重新设置宽高
			imgElement.attr("max-width", "100%");
//					.attr("height", "auto");

			imgElement.attr("width", "100%");
			imgElement.attr("style", "max-width:100%");
		}
		Log.i("newData:", document.toString());
		return document.toString();
	}


	public static String getNewMessageData(String data){

		if(!TextUtils.isEmpty(data)){
			if(data.endsWith("།")){
				data = data.substring(0,data.lastIndexOf("།"));
			}
			if(data.endsWith("་")){
				data = data.substring(0,data.lastIndexOf("་"));
			}
		}

		return  data;
	}



	public static  String getlangvagetype(String data,String[] choicedata1){
		String outputexttype = "";
		for(int i=0;i<choicedata1.length;i++){
			if(data.equals(choicedata1[i])){
				switch (i){
					case 0:
						outputexttype = "zh";
						break;
					case 1:
						outputexttype = "en";
						break;
					case 2:
						outputexttype = "ti";
						break;
					case 3:
						outputexttype = "uy";
						break;
					case 4:
						outputexttype = "mn";
						break;
					case 5:
						outputexttype = "cht";
						break;
					case 6:
						outputexttype = "ja";
						break;
					case 7:
						outputexttype = "fr";
						break;
					case 8 :
						outputexttype = "es";
						break;
					case 9:
						outputexttype = "pt";
						break;
					case 10:
						outputexttype = "de";
						break;
					case 11:
						outputexttype = "it";
						break;
					case 12:
						outputexttype = "ru";
						break;
					case 13:
						outputexttype = "id";
						break;
					case 14:
						outputexttype = "ms";
						break;
					case 15:
						outputexttype = "vi";
						break;

					case 16:
						outputexttype = "af";
						break;
					case 17:
						outputexttype = "am";
						break;
					case 18:
						outputexttype = "ar";
						break;
					case 19:
						outputexttype = "az";
						break;
					case 20:
						outputexttype = "ba";
						break;
					case 21:
						outputexttype = "be";
						break;
					case 22:
						outputexttype = "bg";
						break;



					case 23:
						outputexttype = "bn";
						break;
					case 24:
						outputexttype = "bs";
						break;
					case 25:
						outputexttype = "ca";
						break;
					case 26:
						outputexttype = "ceb";
						break;
					case 27:
						outputexttype = "co";
						break;
					case 28:
						outputexttype = "cs";
						break;
					case 29:
						outputexttype = "cy";
						break;



					case 30:
						outputexttype = "da";
						break;
					case 31:
						outputexttype = "el";
						break;
					case 32:
						outputexttype = "eo";
						break;
					case 33:
						outputexttype = "et";
						break;
					case 34:
						outputexttype = "eu";
						break;
					case 35:
						outputexttype = "fa";
						break;
					case 36:
						outputexttype = "fi";
						break;

					case 37:
						outputexttype = "fil";
						break;
					case 38:
						outputexttype = "fj";
						break;
					case 39:
						outputexttype = "fy";
						break;
					case 40:
						outputexttype = "ga";
						break;
					case 41:
						outputexttype = "gd";
						break;
					case 42:
						outputexttype = "gl";
						break;
					case 43:
						outputexttype = "gu";
						break;

					case 44:
						outputexttype = "ha";
						break;
					case 45:
						outputexttype = "haw";
						break;
					case 46:
						outputexttype = "he";
						break;
					case 47:
						outputexttype = "hi";
						break;
					case 48:
						outputexttype = "hr";
						break;
					case 49:
						outputexttype = "ht";
						break;
					case 50:
						outputexttype = "hu";
						break;


					case 51:
						outputexttype = "hy";
						break;
					case 52:
						outputexttype = "ig";
						break;
					case 53:
						outputexttype = "is";
						break;
					case 54:
						outputexttype = "jv";
						break;
					case 55:
						outputexttype = "jy";
						break;
					case 56:
						outputexttype = "ka";
						break;
					case 57:
						outputexttype = "kk";
						break;


					case 58:
						outputexttype = "km";
						break;
					case 59:
						outputexttype = "kn";
						break;
					case 60:
						outputexttype = "ko";
						break;
					case 61:
						outputexttype = "ku";
						break;
					case 62:
						outputexttype = "ky";
						break;
					case 63:
						outputexttype = "la";
						break;
					case 64:
						outputexttype = "lb";
						break;

					case 65:
						outputexttype = "lo";
						break;
					case 66:
						outputexttype = "lt";
						break;
					case 67:
						outputexttype = "lv";
						break;
					case 68:
						outputexttype = "mg";
						break;
					case 69:
						outputexttype = "mhr";
						break;
					case 70:
						outputexttype = "mi";
						break;
					case 71:
						outputexttype = "mk";
						break;



					case 72:
						outputexttype = "ml";
						break;
					case 73:
						outputexttype = "mn";
						break;
					case 74:
						outputexttype = "mr";
						break;
					case 75:
						outputexttype = "mrj";
						break;
					case 76:
						outputexttype = "mt";
						break;
					case 77:
						outputexttype = "mww";
						break;
					case 78:
						outputexttype = "my";
						break;


					case 79:
						outputexttype = "ne";
						break;
					case 80:
						outputexttype = "nl";
						break;
					case 81:
						outputexttype = "no";
						break;
					case 82:
						outputexttype = "ny";
						break;
					case 83:
						outputexttype = "otq";
						break;
					case 84:
						outputexttype = "pa";
						break;
					case 85:
						outputexttype = "pap";
						break;


					case 86:
						outputexttype = "pl";
						break;
					case 87:
						outputexttype = "ps";
						break;
					case 88:
						outputexttype = "ro";
						break;
					case 89:
						outputexttype = "sd";
						break;
					case 90:
						outputexttype = "si";
						break;
					case 91:
						outputexttype = "sk";
						break;
					case 92:
						outputexttype = "sl";
						break;




					case 93:
						outputexttype = "sm";
						break;
					case 94:
						outputexttype = "sn";
						break;
					case 95:
						outputexttype = "so";
						break;
					case 96:
						outputexttype = "sq";
						break;
					case 97:
						outputexttype = "sr";
						break;
					case 98:
						outputexttype = "st";
						break;
					case 99:
						outputexttype = "su";
						break;


					case 100:
						outputexttype = "sv";
						break;
					case 101:
						outputexttype = "sw";
						break;
					case 102:
						outputexttype = "ta";
						break;
					case 103:
						outputexttype = "te";
						break;
					case 104:
						outputexttype = "tg";
						break;
					case 105:
						outputexttype = "th";
						break;
					case 106:
						outputexttype = "to";
						break;


					case 107:
						outputexttype = "tr";
						break;
					case 108:
						outputexttype = "tt";
						break;
					case 109:
						outputexttype = "ty";
						break;
					case 110:
						outputexttype = "udm";
						break;
					case 111:
						outputexttype = "uk";
						break;
					case 112:
						outputexttype = "ur";
						break;
					case 113:
						outputexttype = "uz";
						break;

					case 114:
						outputexttype = "xh";
						break;
					case 115:
						outputexttype = "yi";
						break;
					case 116:
						outputexttype = "yo";
						break;
					case 117:
						outputexttype = "yua";
						break;
					case 118:
						outputexttype = "yue";
						break;
					case 119:
						outputexttype = "zu";
						break;





				}
			}else{
				continue;
			}
		}

		return outputexttype;
	}
}
