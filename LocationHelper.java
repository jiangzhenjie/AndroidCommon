/**
 * 位置工具类，获取用户当前的经纬度
 * 
 */
public class LocationHelper {

	private Context mContext;
	private LocationManager mManager;
	private GetLocationListener mGetListener;
	private Timer mTimer;
	private TimerTask mTimerTask;
	private boolean isLocationOk ;
	
	private static final int GPS_TIME = 10000;

	public LocationHelper(Context context) {
		mContext = context;
		mTimer = new Timer();
		mTimerTask = new ChangeProviderTask();
		isLocationOk = false;
	}

	private LocationListener listener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			mManager.removeUpdates(this);
			mGetListener.location(latitude, longitude);
			isLocationOk = true;
			mTimer.cancel();
		}
	};

	public void getLocation(GetLocationListener getListener) {
		mGetListener = getListener;
		if (mManager == null) {
			mManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);
		}
		boolean gpsEnabled = mManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (gpsEnabled) {
			mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
					100, listener);
			mTimer.schedule(mTimerTask, GPS_TIME);
		} else {
			mManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					1000, 100, listener);
		}
	}

	private class ChangeProviderTask extends TimerTask {

		@Override
		public void run() {
			Looper.prepare();
			if (!isLocationOk) {
				Log.d("mydebug", "change provider");
				mManager.removeUpdates(listener);
				mManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 1000, 100, listener);
			}
			Looper.loop();
		}

	}

	public interface GetLocationListener {
		public void location(double latitude, double longitude);
	}

}
