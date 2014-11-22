     /**
	  *  Android dip单位和pix单位的相互转化
	  *  last-motify:2014-11-22
	  */
	public class AndroidSizeUtils{
		/**
	 	 * 
	 	 * @Method: dpChangePix
	 	 * @Description  将输入的DP值转换成PIX值
	 	 * @param dp  输入要转换的DP值
	 	 * @return
	 	*/
		public static int dpChangePix(Context c, float dp) 	{
				float DENSITY = c.getResources().getDisplayMetrics().density;
				// DP*本机的屏幕密度，再加上0.5来四舍五入到最接近的数。
				return (int) (dp * DENSITY + 0.5f);
		}

		/**
	 	* 
	 	* @Method: pixChangeDp
	 	* @Description  将输入的PIX值转换成DP值
	 	* @param pix  输入要转换的PIX值
	 	* @return
	 	*/
		public static int pixChangeDp(Context c, float pix) {
			float DENSITY = c.getResources().getDisplayMetrics().density;
			return (int) (pix / DENSITY + 0.5f);
		}
	}