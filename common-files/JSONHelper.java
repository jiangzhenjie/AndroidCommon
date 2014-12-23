/**
 * JSON解析工具类
 * 利用反射机制对JOSN字符串进行解析
 * 
 */
public class JSONHelper {

	private static final String TAG = "mydebug";

	/**
	 * 解析JSON，注意以下几点： 1.该方法使用反射机制解析JSON。 2.模型类的字段名称必须和返回JSON的键名称完全一致。
	 * 3.模型类的字段类型只能使用String，List或者模型类型。
	 * 
	 * @param json
	 *            data字段的JSON对象，可以是JSONObject或者JSONArray
	 * @param cls
	 *            模型类的Class值
	 * @return 正常情况返回模型类
	 * @throws Exception
	 */

	public static Object parse(Object json, Class<?> cls) throws Exception {
		if (json instanceof JSONObject) {
			return parse1(json, cls);
		} else if (json instanceof JSONArray) {
			return parse2(json, cls);
		}
		return json;
	}

	/**
	 * 第一次传入JSONObject
	 * @param json
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	private static Object parse1(Object json, Class<?> cls) throws Exception {
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;

		// Log.d(TAG, json.toString());
		// Log.d(TAG, cls.toString());

		Field[] fields = cls.getDeclaredFields();

		Object bean = cls.newInstance();

		if (json instanceof JSONObject) {
			jsonObject = (JSONObject) json;
			for (Field f : fields) {
				f.setAccessible(true);
				String name = f.getName();
				Class<?> type = f.getType();
				// Log.d(TAG, name);
				// Log.d(TAG, type.toString());
				if (type == List.class) {
					String genTypeStr = f.getGenericType().toString();
					int s = genTypeStr.indexOf("<");
					int e = genTypeStr.indexOf(">");
					String genType = genTypeStr.substring(s + 1, e);
					// Log.d(TAG, genType);
					Object obj = jsonObject.get(name);
					Object result = parse1(obj, Class.forName(genType));
					f.set(bean, result);
				} else if (type == String.class) {
					String value = jsonObject.getString(name);
					f.set(bean, value);
				} else {
					Object obj = jsonObject.get(name);
					Object result = parse1(obj, type);
					f.set(bean, result);
				}
			}
		} else if (json instanceof JSONArray) {
			jsonArray = (JSONArray) json;
			ArrayList<Object> arrayResult = new ArrayList<Object>();
			for (int i = 0; i < jsonArray.length(); i++) {
				// Log.d(TAG, jsonArray.length() + "");
				Object obj = jsonArray.get(i);
				Object result = parse1(obj, cls);
				arrayResult.add(result);
			}
			return arrayResult;

		} else {
			return json;
		}
		return bean;
	}

	/**
	 * 第一次传入的是JSONArray
	 * 
	 * @param json
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	private static Object parse2(Object json, Class<?> cls) throws Exception {
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;

		Log.d(TAG, json.toString());
		Log.d(TAG, cls.toString());

		Field[] fields = cls.getDeclaredFields();

		Object bean = cls.newInstance();

		for (Field f : fields) {
			f.setAccessible(true);
			String name = f.getName();
			Class<?> type = f.getType();
			// Log.d(TAG, name);
			// Log.d(TAG, type.toString());
			if (type == List.class) {
				String genTypeStr = f.getGenericType().toString();
				int s = genTypeStr.indexOf("<");
				int e = genTypeStr.indexOf(">");
				String genType = genTypeStr.substring(s + 1, e);
				Log.d(TAG, genType);

				if (json instanceof JSONArray) {
					jsonArray = (JSONArray) json;
					ArrayList<Object> arrayResult = new ArrayList<Object>();
					for (int i = 0; i < jsonArray.length(); i++) {
						Log.d(TAG, jsonArray.length() + "");
						Object obj = jsonArray.get(i);
						Object result = parse2(obj, Class.forName(genType));
						arrayResult.add(result);
					}
					f.set(bean, arrayResult);
				}
			} else if (type == String.class) {
				if (json instanceof JSONObject) {
					jsonObject = (JSONObject) json;
					String value = jsonObject.getString(name);
					f.set(bean, value);
				}

			} else {
				Object obj = jsonObject.get(name);
				Object result = parse2(obj, type);
				f.set(bean, result);
			}
		}
		return bean;
	}
}
