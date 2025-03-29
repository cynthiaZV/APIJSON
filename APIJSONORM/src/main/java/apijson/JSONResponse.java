/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson;

import apijson.orm.exception.UnsupportedDataTypeException;

import java.util.*;

import static apijson.JSON.parseObject;

/**parser for response
 * @author Lemon
 * @see #getObject
 * @see #getList
 * @use JSONResponse response = new JSONResponse(json);
 * <br> User user = response.getObject(User.class);//not a must
 * <br> List<Comment> commenntList = response.getList("Comment[]", Comment.class);//not a must
 */
public class JSONResponse<M extends Map<String, Object>, L extends List<Object>> extends apijson.JSONObject {
	private static final long serialVersionUID = 1L;

	// 节约性能和减少 bug，除了关键词 @key ，一般都符合变量命名规范，不符合也原样返回便于调试
	/**格式化带 - 中横线的单词
	 */
	public static boolean IS_FORMAT_HYPHEN = false;
	/**格式化带 _ 下划线的单词
	 */
	public static boolean IS_FORMAT_UNDERLINE = false;
	/**格式化带 $ 美元符的单词
	 */
	public static boolean IS_FORMAT_DOLLAR = false;

	private static final String TAG = "JSONResponse";

	public JSONResponse() {
		super();
	}
	public JSONResponse(Object json) {
		this(parseObject(json));
	}
	public JSONResponse(Object json, JSONParser<M, L> parser) {
		this(parseObject(json, parser));
	}
	public JSONResponse(Map<String, Object> object) {
		super(format(object));
	}
	public JSONResponse(M object, JSONCreator<M, L> creator) {
		super(format(object, creator));
	}

	//状态信息，非GET请求获得的信息<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final int CODE_SUCCESS = 200; //成功
	public static final int CODE_UNSUPPORTED_ENCODING = 400; //编码错误
	public static final int CODE_ILLEGAL_ACCESS = 401; //权限错误
	public static final int CODE_UNSUPPORTED_OPERATION = 403; //禁止操作
	public static final int CODE_NOT_FOUND = 404; //未找到
	public static final int CODE_ILLEGAL_ARGUMENT = 406; //参数错误
	public static final int CODE_NOT_LOGGED_IN = 407; //未登录
	public static final int CODE_TIME_OUT = 408; //超时
	public static final int CODE_CONFLICT = 409; //重复，已存在
	public static final int CODE_CONDITION_ERROR = 412; //条件错误，如密码错误
	public static final int CODE_UNSUPPORTED_TYPE = 415; //类型错误
	public static final int CODE_OUT_OF_RANGE = 416; //超出范围
	public static final int CODE_NULL_POINTER = 417; //对象为空
	public static final int CODE_SERVER_ERROR = 500; //服务器内部错误


	public static final String MSG_SUCCEED = "success"; //成功
	public static final String MSG_SERVER_ERROR = "Internal Server Error!"; //服务器内部错误


	public static String KEY_OK = "ok";
	public static String KEY_CODE = "code";
	public static String KEY_MSG = "msg";
	public static final String KEY_COUNT = "count";
	public static final String KEY_TOTAL = "total";
	public static final String KEY_INFO = "info"; //详细的分页信息
	public static final String KEY_FIRST = "first"; //是否为首页
	public static final String KEY_LAST = "last"; //是否为尾页
	public static final String KEY_MAX = "max"; //最大页码
	public static final String KEY_MORE = "more"; //是否有更多

	/**获取状态
	 * @return
	 */
	public int getCode() {
		try {
			return getIntValue(KEY_CODE);
		} catch (Exception e) {
			//empty
		}
		return 0;
	}
	/**获取状态
	 * @return
	 */
	public static int getCode(Map<String, Object> reponse) {
		try {
			return JSON.getIntValue(reponse, KEY_CODE);
		} catch (Exception e) {
			//empty
		}
		return 0;
	}
	/**获取状态描述
	 * @return
	 */
	public String getMsg() {
		return getString(KEY_MSG);
	}
	/**获取状态描述
	 * @param reponse
	 * @return
	 */
	public static String getMsg(Map<String, Object> reponse) {
		return reponse == null ? null : JSON.getString(reponse, KEY_MSG);
	}
	/**获取id
	 * @return
	 */
	public long getId() {
		try {
			return getLongValue(KEY_ID);
		} catch (Exception e) {
			//empty
		}
		return 0;
	}
	/**获取数量
	 * @return
	 */
	public int getCount() {
		try {
			return getIntValue(KEY_COUNT);
		} catch (Exception e) {
			//empty
		}
		return 0;
	}
	/**获取总数
	 * @return
	 */
	public int getTotal() {
		try {
			return getIntValue(KEY_TOTAL);
		} catch (Exception e) {
			//empty
		}
		return 0;
	}


	/**是否成功
	 * @return
	 */
	public boolean isSuccess() {
		return isSuccess(getCode());
	}
	/**是否成功
	 * @param code
	 * @return
	 */
	public static boolean isSuccess(int code) {
		return code == CODE_SUCCESS;
	}
	/**是否成功
	 * @param response
	 * @return
	 */
	public static boolean isSuccess(JSONResponse response) {
		return response != null && response.isSuccess();
	}
	/**是否成功
	 * @param response
	 * @return
	 */
	public static boolean isSuccess(Map<String, Object> response) {
        try {
            return response != null && isSuccess(JSON.getIntValue(response, KEY_CODE));
        } catch (UnsupportedDataTypeException e) {
            return false;
        }
    }

	/**校验服务端是否存在table
	 * @return
	 */
	public boolean isExist() {
		return isExist(getCount());
	}
	/**校验服务端是否存在table
	 * @param count
	 * @return
	 */
	public static boolean isExist(int count) {
		return count > 0;
	}
	/**校验服务端是否存在table
	 * @param response
	 * @return
	 */
	public static boolean isExist(JSONResponse response) {
		return response != null && response.isExist();
	}

	/**获取内部的JSONResponse
	 * @param key
	 * @return
	 */
	public JSONResponse<M, L> getJSONResponse(String key, JSONParser<M, L> parser) {
		return getObject(key, JSONResponse.class, parser);
	}
	//cannot get javaBeanDeserizer
	//	/**获取内部的JSONResponse
	//	 * @param response
	//	 * @param key
	//	 * @return
	//	 */
	//	public static JSONResponse getJSONResponse(M response, String key) {
	//		return response == null ? null : response.getObject(key, JSONResponse.class);
	//	}
	//状态信息，非GET请求获得的信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	/**
	 * key = clazz.getSimpleName()
	 * @param clazz
	 * @return
	 */
	public <T> T getObject(Class<T> clazz, JSONParser<M, L> parser) {
		return getObject(clazz == null ? "" : clazz.getSimpleName(), clazz, parser);
	}
	/**
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T getObject(String key, Class<T> clazz, JSONParser<M, L> parser) {
		return getObject(this, key, clazz, parser);
	}
	/**
	 * @param object
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T, M extends Map<String, Object>, L extends List<Object>> T getObject(
			Map<String, Object> object, String key, Class<T> clazz, JSONParser<M, L> parser) {
		return toObject(object == null ? null : JSON.get(object, formatObjectKey(key)), clazz, parser);
	}

	/**
	 * @param clazz
	 * @return
	 */
	public <T> T toObject(Class<T> clazz, JSONParser<M, L> parser) {
		return toObject(this, clazz, parser);
	}
	/**
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T, M extends Map<String, Object>, L extends List<Object>> T toObject(
			Map<String, Object> object, Class<T> clazz, JSONParser<M, L> parser) {
		return parseObject(JSON.toJSONString(object), clazz, parser);
	}




	/**
	 * key = KEY_ARRAY
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getList(Class<T> clazz, JSONParser<M, List<Object>> parser) {
		return getList(KEY_ARRAY, clazz, parser);
	}
	/**
	 * arrayObject = this
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getList(String key, Class<T> clazz, JSONParser<M, List<Object>> parser) {
		return getList(this, key, clazz, parser);
	}

	/**
	 * key = KEY_ARRAY
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T, M extends Map<String, Object>> List<T> getList(Map<String, Object> object, Class<T> clazz, JSONParser<M, List<Object>> parser) {
		return getList(object, KEY_ARRAY, clazz, parser);
	}
	/**
	 * @param object
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T, M extends Map<String, Object>> List<T> getList(Map<String, Object> object, String key, Class<T> clazz, JSONParser<M, List<Object>> parser) {
		return object == null ? null : JSON.parseArray(JSON.getString(object, formatArrayKey(key)), clazz, parser);
	}

	/**
	 * key = KEY_ARRAY
	 * @return
	 */
	public JSONArray getArray() {
		return getArray(KEY_ARRAY);
	}
	/**
	 * @param key
	 * @return
	 */
	public JSONArray getArray(String key) {
		return getArray(this, key);
	}
	/**
	 * @param object
	 * @return
	 */
	public static JSONArray getArray(Map<String, Object> object) {
		return getArray(object, KEY_ARRAY);
	}
	/**
	 * key = KEY_ARRAY
	 * @param object
	 * @param key
	 * @return
	 */
	public static JSONArray getArray(Map<String, Object> object, String key) {
		return object == null ? null : JSON.get(object, formatArrayKey(key));
	}


	//	/**
	//	 * @return
	//	 */
	//	public M format() {
	//		return format(this);
	//	}
	/**格式化key名称
	 * @param object
	 * @return
	 */
	public static Map<String, Object> format(final Map<String, Object> object) {
//		return format(object, JSON.DEFAULT_JSON_CREATOR);
		return format(object, new JSONCreator<>() {
			@Override
			public Map<String, Object> createJSONObject() {
				return new LinkedHashMap<>();
			}

			@Override
			public List<Object> createJSONArray() {
				return new ArrayList<>();
			}
		});
	}
	/**格式化key名称
	 * @param object
	 * @return
	 */
	public static <M extends Map<String, Object>, L extends List<Object>> M format(final M object, @NotNull JSONCreator<M, L> creator) {
		//太长查看不方便，不如debug	 Log.i(TAG, "format  object = \n" + JSON.toJSONString(object));
		if (object == null || object.isEmpty()) {
			Log.i(TAG, "format  object == null || object.isEmpty() >> return object;");
			return object;
		}

		M formatedObject = creator.createJSONObject();

		Set<String> set = object.keySet();
		if (set != null) {

			Object value;
			for (String key : set) {
				value = object.get(key);

				if (value instanceof List<?>) {//JSONArray，遍历来format内部项
					formatedObject.put(formatArrayKey(key), format((L) value, creator));
				}
				else if (value instanceof Map<?, ?>) {//M，往下一级提取
					formatedObject.put(formatObjectKey(key), format((M) value, creator));
				}
				else {//其它Object，直接填充
					formatedObject.put(formatOtherKey(key), value);
				}
			}
		}

		//太长查看不方便，不如debug	 Log.i(TAG, "format  return formatedObject = " + JSON.toJSONString(formatedObject));
		return formatedObject;
	}

	/**格式化key名称
	 * @param array
	 * @return
	 */
	public static List<Object> format(final List<Object> array) {
		//		return format(array, JSON.DEFAULT_JSON_CREATOR);
		return format(array, new JSONCreator<>() {
			@Override
			public Map<String, Object> createJSONObject() {
				return new LinkedHashMap<>();
			}

			@Override
			public List<Object> createJSONArray() {
				return new ArrayList<>();
			}
		});
	}
	public static <M extends Map<String, Object>, L extends List<Object>> L format(final L array, @NotNull JSONCreator<M, L> creator) {
		//太长查看不方便，不如debug	 Log.i(TAG, "format  array = \n" + JSON.toJSONString(array));
		if (array == null || array.isEmpty()) {
			Log.i(TAG, "format  array == null || array.isEmpty() >> return array;");
			return array;
		}
		L formatedArray = creator.createJSONArray();

		Object value;
		for (int i = 0; i < array.size(); i++) {
			value = array.get(i);
			if (value instanceof List<?>) {//JSONArray，遍历来format内部项
				formatedArray.add(format((L) value, creator));
			}
			else if (value instanceof Map<?, ?>) {//M，往下一级提取
				formatedArray.add(format((M) value, creator));
			}
			else {//其它Object，直接填充
				formatedArray.add(value);
			}
		}

		//太长查看不方便，不如debug	 Log.i(TAG, "format  return formatedArray = " + JSON.toJSONString(formatedArray));
		return formatedArray;
	}


	/**获取表名称
	 * @param fullName name 或 name:alias
	 * @return name => name; name:alias => alias
	 */
	public static String getTableName(String fullName) {
		//key:alias  -> alias; key:alias[] -> alias[]
		int index = fullName == null ? -1 : fullName.indexOf(":");
		return index < 0 ? fullName : fullName.substring(0, index);
	}

	/**获取变量名
	 * @param fullName
	 * @return {@link #formatKey(String, boolean, boolean, boolean, boolean, boolean, Boolean)} formatColon = true, formatAt = true, formatHyphen = true, firstCase = true
	 */
	public static String getVariableName(String fullName) {
		if (isArrayKey(fullName)) {
			fullName = StringUtil.addSuffix(fullName.substring(0, fullName.length() - 2), "list");
		}
		return formatKey(fullName, true, true, true, true, false, true);
	}

	/**格式化数组的名称 key[] => keyList; key:alias[] => aliasList; Table-column[] => tableColumnList
	 * @param key empty ? "list" : key + "List" 且首字母小写
	 * @return {@link #formatKey(String, boolean, boolean, boolean, boolean, boolean, Boolean)} formatColon = false, formatAt = true, formatHyphen = true, firstCase = true
	 */
	public static String formatArrayKey(String key) {
		if (isArrayKey(key)) {
			key = StringUtil.addSuffix(key.substring(0, key.length() - 2), "list");
		}
		int index = key == null ? -1 : key.indexOf(":");
		if (index >= 0) {
			return key.substring(index + 1); //不处理自定义的
		}

		return formatKey(key, false, true, true, IS_FORMAT_UNDERLINE, IS_FORMAT_DOLLAR, false); //节约性能，除了数组对象 Table-column:alias[] ，一般都符合变量命名规范
	}

	/**格式化对象的名称 name => name; name:alias => alias
	 * @param key name 或 name:alias
	 * @return {@link #formatKey(String, boolean, boolean, boolean, boolean, boolean, Boolean)} formatColon = false, formatAt = true, formatHyphen = false, firstCase = true
	 */
	public static String formatObjectKey(String key) {
		int index = key == null ? -1 : key.indexOf(":");
		if (index >= 0) {
			return key.substring(index + 1); // 不处理自定义的
		}

		return formatKey(key, false, true, IS_FORMAT_HYPHEN, IS_FORMAT_UNDERLINE, IS_FORMAT_DOLLAR, false); //节约性能，除了表对象 Table:alias ，一般都符合变量命名规范
	}

	/**格式化普通值的名称 name => name; name:alias => alias
	 * @param fullName name 或 name:alias
	 * @return {@link #formatKey(String, boolean, boolean, boolean, boolean, boolean, Boolean)} formatColon = false, formatAt = true, formatHyphen = false, firstCase = false
	 */
	public static String formatOtherKey(String fullName) {
		return formatKey(fullName, false, true, IS_FORMAT_HYPHEN, IS_FORMAT_UNDERLINE, IS_FORMAT_DOLLAR
				, IS_FORMAT_HYPHEN || IS_FORMAT_UNDERLINE || IS_FORMAT_DOLLAR ? false : null);
	}


	/**格式化名称
	 * @param fullName name 或 name:alias
	 * @param formatAt 去除前缀 @ ， @a => a
	 * @param formatColon 去除分隔符 : ， A:b => b
	 * @param formatHyphen 去除分隔符 - ， A-b-cd-Efg => aBCdEfg
	 * @param formatUnderline 去除分隔符 _ ， A_b_cd_Efg => aBCdEfg
	 * @param formatDollar 去除分隔符 $ ， A$b$cd$Efg => aBCdEfg
	 * @param firstCase 第一个单词首字母小写，后面的首字母大写， Ab => ab ; A-b-Cd => aBCd
	 * @return name => name; name:alias => alias
	 */
	public static String formatKey(String fullName, boolean formatColon, boolean formatAt, boolean formatHyphen
			, boolean formatUnderline, boolean formatDollar, Boolean firstCase) {
		if (fullName == null) {
			Log.w(TAG, "formatKey  fullName == null >> return null;");
			return null;
		}

		if (formatColon) {
			fullName = formatColon(fullName);
		}
		if (formatAt) { //关键词只去掉前缀，不格式化单词，例如 @a-b 返回 a-b ，最后不会调用 setter
			fullName = formatAt(fullName);
		}
		if (formatHyphen && fullName.contains("-")) {
			fullName = formatHyphen(fullName, true);
		}
		if (formatUnderline && fullName.contains("_")) {
			fullName = formatUnderline(fullName, true);
		}
		if (formatDollar && fullName.contains("$")) {
			fullName = formatDollar(fullName, true);
		}

		// 默认不格式化普通 key:value (value 不为 [], {}) 的 key
		return firstCase == null ? fullName : StringUtil.firstCase(fullName, firstCase);
	}

	/**"@key" => "key"
	 * @param key
	 * @return
	 */
	public static String formatAt(@NotNull String key) {
		return key.startsWith("@") ? key.substring(1) : key;
	}

	/**key:alias => alias
	 * @param key
	 * @return
	 */
	public static String formatColon(@NotNull String key) {
		int index = key.indexOf(":");
		return index < 0 ? key : key.substring(index + 1);
	}

	/**A-b-cd-Efg => ABCdEfg
	 * @param key
	 * @return
	 */
	public static String formatHyphen(@NotNull String key) {
		return StringUtil.firstCase(formatHyphen(key, true), false);
	}
	/**A-b-cd-Efg => ABCdEfg
	 * @param key
	 * @param firstCase 首字符的大小写，true-大写，false-小写，null-不处理
	 * @return
	 */
	public static String formatHyphen(@NotNull String key, Boolean firstCase) {
		return formatHyphen(key, firstCase, false);
	}
	/**A-b-cd-Efg => ABCdEfg
	 * @param key
	 * @param firstCase 首字符的大小写，true-大写，false-小写，null-不处理
	 * @param otherCase 非首字符的大小写，true-大写，false-小写，null-不处理
	 * @return
	 */
	public static String formatHyphen(@NotNull String key, Boolean firstCase, Boolean otherCase) {
		return formatDivider(key, "-", firstCase, otherCase);
	}

	/**A_b_cd_Efg => ABCdEfg
	 * @param key
	 * @return
	 */
	public static String formatUnderline(@NotNull String key) {
		return StringUtil.firstCase(formatUnderline(key, true), false);
	}
	/**A_b_cd_Efg => ABCdEfg
	 * @param key
	 * @param firstCase 首字符的大小写，true-大写，false-小写，null-不处理
	 * @return
	 */
	public static String formatUnderline(@NotNull String key, Boolean firstCase) {
		return formatUnderline(key, firstCase, false);
	}
	/**A_b_cd_Efg => ABCdEfg
	 * @param key
	 * @param firstCase 首字符的大小写，true-大写，false-小写，null-不处理
	 * @param otherCase 非首字符的大小写，true-大写，false-小写，null-不处理
	 * @return
	 */
	public static String formatUnderline(@NotNull String key, Boolean firstCase, Boolean otherCase) {
		return formatDivider(key, "_", firstCase, otherCase);
	}

	/**A$b$cd$Efg => ABCdEfg
	 * @param key
	 * @return
	 */
	public static String formatDollar(@NotNull String key) {
		return StringUtil.firstCase(formatDollar(key, true), false);
	}
	/**A$b$cd$Efg => ABCdEfg
	 * @param key
	 * @param firstCase 首字符的大小写，true-大写，false-小写，null-不处理
	 * @return
	 */
	public static String formatDollar(@NotNull String key, Boolean firstCase) {
		return formatDollar(key, firstCase, false);
	}
	/**A$b$cd$Efg => ABCdEfg
	 * @param key
	 * @param firstCase 首字符的大小写，true-大写，false-小写，null-不处理
	 * @param otherCase 非首字符的大小写，true-大写，false-小写，null-不处理
	 * @return
	 */
	public static String formatDollar(@NotNull String key, Boolean firstCase, Boolean otherCase) {
		return formatDivider(key, "$", firstCase, otherCase);
	}

	/**A.b.cd.Efg => ABCdEfg
	 * @param key
	 * @return
	 */
	public static String formatDot(@NotNull String key) {
		return StringUtil.firstCase(formatDot(key, true), false);
	}
	/**A.b.cd.Efg => ABCdEfg
	 * @param key
	 * @param firstCase 首字符的大小写，true-大写，false-小写，null-不处理
	 * @return
	 */
	public static String formatDot(@NotNull String key, Boolean firstCase) {
		return formatDot(key, firstCase, false);
	}
	/**A.b.cd.Efg => ABCdEfg
	 * @param key
	 * @param firstCase 首字符的大小写，true-大写，false-小写，null-不处理
	 * @param otherCase 非首字符的大小写，true-大写，false-小写，null-不处理
	 * @return
	 */
	public static String formatDot(@NotNull String key, Boolean firstCase, Boolean otherCase) {
		return formatDivider(key, ".", firstCase, otherCase);
	}

	/**A/b/cd/Efg => ABCdEfg
	 * @param key
	 * @return
	 */
	public static String formatDivider(@NotNull String key, Boolean firstCase) {
		return formatDivider(key, "/", firstCase);
	}

	/**去除分割符，返回驼峰格式
	 * @param key
	 * @param divider
	 * @return
	 */
	public static String formatDivider(@NotNull String key, @NotNull String divider) {
		return StringUtil.firstCase(formatDivider(key, divider, true), false);
	}
	/**去除分割符，返回驼峰格式
	 * @param key
	 * @param divider
	 * @param firstCase 首字符的大小写，true-大写，false-小写，null-不处理
	 * @return
	 */
	public static String formatDivider(@NotNull String key, @NotNull String divider, Boolean firstCase) {
		return formatDivider(key, divider, firstCase, false);
	}

	/**去除分割符，返回驼峰格式
	 * @param key
	 * @param divider
	 * @param firstCase 首字符的大小写，true-大写，false-小写，null-不处理
	 * @param otherCase 非首字符的大小写，true-大写，false-小写，null-不处理
	 * @return
	 */
	public static String formatDivider(@NotNull String key, @NotNull String divider, Boolean firstCase, Boolean otherCase) {
		String[] parts = StringUtil.split(key, divider);
		StringBuilder name = new StringBuilder();
		for (String part : parts) {
			if (otherCase != null) {
				part = otherCase ? part.toUpperCase() : part.toLowerCase();
			}
			if (firstCase != null) {
				part = StringUtil.firstCase(part, firstCase);
			}
			name.append(part);
		}
		return name.toString();
	}

}
