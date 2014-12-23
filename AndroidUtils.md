# Android Utils #

------------------------------------
## Android评价打分功能的实现（2014-12-23） ##
	/**
    * @Description 跳转到本机安装的应用市场中进行评价和打分
    */ 
	 public void evaluate() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "未安装任何应用市场App", Toast.LENGTH_SHORT).show();
        }
    }

## 使整个Android背景透明（2014-12-23） ##
>首先，在style中定义：

    <style name="Theme.Transparent" parent="AppTheme">
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="android:windowNoTitle">true</item>
        <item name="android:windowIsFloating">true</item>
        <item name="android:backgroundDimEnabled">false</item>
    </style>
> 然后在AndroidManifest中为Activity使用该Theme

## android dip 和 pix 单位的互换（2014-12-23） ##
	/**
    * 
    * @Description  将输入的DIP值转换成PIX值
    * @param dp  输入要转换的DIP值
    * @return PIX值
    */  
	public  int dpChangePix(Context c, float dp) 	{
		float DENSITY = c.getResources().getDisplayMetrics().density;
		// DP*本机的屏幕密度，再加上0.5来四舍五入到最接近的数。
		return (int) (dp * DENSITY + 0.5f);
	}
	
    /**
    * 
    * @Description  将输入的DIP值转换成PIX值
    * @param dp  输入要转换的DIP值
    * @return PIX值
    */  
	public  int pixChangeDp(Context c, float pix) {
		float DENSITY = c.getResources().getDisplayMetrics().density;
		return (int) (pix / DENSITY + 0.5f);
	}