/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

import java.util.List;
import java.util.Map;

import apijson.NotNull;
import apijson.RequestMethod;
import apijson.StringUtil;

/**SQL配置
 * @author Lemon
 */
public interface SQLConfig<T extends Object> {

	String DATABASE_MYSQL = "MYSQL"; // https://www.mysql.com
	String DATABASE_POSTGRESQL = "POSTGRESQL"; // https://www.postgresql.org
	String DATABASE_SQLSERVER = "SQLSERVER"; // https://www.microsoft.com/en-us/sql-server
	String DATABASE_ORACLE = "ORACLE"; // https://www.oracle.com/database
	String DATABASE_DB2 = "DB2"; // https://www.ibm.com/products/db2
	String DATABASE_MARIADB = "MARIADB"; // https://mariadb.org
	String DATABASE_TIDB = "TIDB"; // https://www.pingcap.com/tidb
	String DATABASE_COCKROACHDB = "COCKROACHDB"; // https://www.cockroachlabs.com
	String DATABASE_DAMENG = "DAMENG"; // https://www.dameng.com
	String DATABASE_KINGBASE = "KINGBASE"; // https://www.kingbase.com.cn
	String DATABASE_ELASTICSEARCH = "ELASTICSEARCH"; // https://www.elastic.co/guide/en/elasticsearch/reference/7.4/xpack-sql.html
	String DATABASE_MANTICORE = "MANTICORE"; // https://manticoresearch.com
	String DATABASE_CLICKHOUSE = "CLICKHOUSE"; // https://clickhouse.com
	String DATABASE_HIVE = "HIVE"; // https://hive.apache.org
	String DATABASE_PRESTO = "PRESTO"; // Facebook PrestoDB  https://prestodb.io
	String DATABASE_TRINO = "TRINO"; // PrestoSQL  https://trino.io
	String DATABASE_SNOWFLAKE = "SNOWFLAKE"; // https://www.snowflake.com
	String DATABASE_DATABRICKS = "DATABRICKS"; // https://www.databricks.com
	String DATABASE_CASSANDRA = "CASSANDRA"; // https://cassandra.apache.org
	String DATABASE_MILVUS = "MILVUS"; // https://milvus.io
	String DATABASE_INFLUXDB = "INFLUXDB"; // https://www.influxdata.com/products/influxdb-overview
	String DATABASE_TDENGINE = "TDENGINE"; // https://tdengine.com
	String DATABASE_TIMESCALEDB = "TIMESCALEDB"; // https://www.timescale.com
	String DATABASE_QUESTDB = "QUESTDB"; // https://questdb.com
	String DATABASE_IOTDB = "IOTDB"; // https://iotdb.apache.org/zh/UserGuide/latest/API/Programming-JDBC.html

	String DATABASE_REDIS = "REDIS"; // https://redisql.com
	String DATABASE_MONGODB = "MONGODB"; // https://www.mongodb.com/docs/atlas/data-federation/query/query-with-sql
	String DATABASE_KAFKA = "KAFKA"; // https://github.com/APIJSON/APIJSON-Demo/tree/master/APIJSON-Java-Server/APIJSONDemo-MultiDataSource-Kafka
	String DATABASE_SQLITE = "SQLITE"; // https://www.sqlite.org
	String DATABASE_DUCKDB = "DUCKDB"; // https://duckdb.org
	String DATABASE_SURREALDB = "SURREALDB"; // https://surrealdb.com
	String DATABASE_OPENGAUSS = "OPENGAUSS"; // https://opengauss.org

	String DATABASE_MQ = "MQ"; //

	String SCHEMA_INFORMATION = "information_schema";  //MySQL, PostgreSQL, SQL Server 都有的系统模式
	String SCHEMA_SYS = "sys";  //SQL Server 系统模式
	String TABLE_SCHEMA = "table_schema";
	String TABLE_NAME = "table_name";

	int TYPE_CHILD = 0;
	int TYPE_ITEM = 1;
	int TYPE_ITEM_CHILD_0 = 2;

	Parser<T> getParser();

	SQLConfig<T> setParser(Parser<T> parser);

	ObjectParser getObjectParser();

	SQLConfig<T> setObjectParser(ObjectParser objectParser);

	int getVersion();

	SQLConfig<T> setVersion(int version);

	String getTag();

	SQLConfig<T> setTag(String tag);

	boolean isTSQL();
	boolean isMSQL();
	boolean isPSQL();

	boolean isMySQL();
	boolean isPostgreSQL();
	boolean isSQLServer();
	boolean isOracle();
	boolean isDb2();
	boolean isMariaDB();
	boolean isTiDB();
	boolean isCockroachDB();
	boolean isDameng();
	boolean isKingBase();
	boolean isElasticsearch();
	boolean isManticore();
	boolean isClickHouse();
	boolean isHive();
	boolean isPresto();
	boolean isTrino();
	boolean isSnowflake();
	boolean isDatabricks();
	boolean isCassandra();
	boolean isMilvus();
	boolean isInfluxDB();
	boolean isTDengine();
	boolean isTimescaleDB();
	boolean isQuestDB();
	boolean isIoTDB();
	boolean isRedis();
	boolean isMongoDB();
	boolean isKafka();
	boolean isMQ();
	boolean isSQLite();
	boolean isDuckDB();
	boolean isSurrealDB();
	boolean isOpenGauss();


	// 暂时只兼容以上几种
	//	boolean isSQL();
	//	boolean isTSQL();
	//	boolean isPLSQL();
	//	boolean isAnsiSQL();

	/**用来给 Table, Column 等系统属性表来绕过 MAX_SQL_COUNT 等限制
	 * @return
	 */
	boolean limitSQLCount();

	/**是否开启 WITH AS 表达式来简化 SQL 和提升性能
	 * @return
	 */
	boolean isWithAsEnable();
	/**允许增删改部分失败
	 * @return
	 */
	boolean allowPartialUpdateFailed();

	@NotNull
	String getIdKey();
	@NotNull
	String getUserIdKey();


	/**获取数据库版本号，可通过判断版本号解决一些 JDBC 驱动连接数据库的兼容问题
	 * MYSQL: 8.0, 5.7, 5.6 等； PostgreSQL: 11, 10, 9.6 等
	 * @return
	 */
	String getDBVersion();

	@NotNull
	default int[] getDBVersionNums() {
		String dbVersion = StringUtil.noBlank(getDBVersion());
		if (dbVersion.isEmpty()) {
			return new int[]{0};
		}

		int index = dbVersion.indexOf("-");
		if (index > 0) {
			dbVersion = dbVersion.substring(0, index);
		}

		String[] ss = dbVersion.split("[.]");
		int[] nums = new int[Math.max(1, ss.length)];
		for (int i = 0; i < ss.length; i++) {
			nums[i] = Integer.valueOf(ss[i]);
		}

		return nums;
	}

	/**获取数据库地址
	 * @return
	 */
	String getDBUri();

	/**获取数据库账号
	 * @return
	 */
	String getDBAccount();

	/**获取数据库密码
	 * @return
	 */
	String getDBPassword();

	/**获取SQL语句
	 * @return
	 * @throws Exception
	 */
	String getSQL(boolean prepared) throws Exception;



	boolean isTest();
	SQLConfig<T> setTest(boolean test);

	int getType();
	SQLConfig<T> setType(int type);

	int getCount();
	SQLConfig<T> setCount(int count);

	int getPage();
	SQLConfig<T> setPage(int page);

	int getQuery();
	SQLConfig<T> setQuery(int query);

	Boolean getCompat();
	SQLConfig<T> setCompat(Boolean compat);

	int getPosition();
	SQLConfig<T> setPosition(int position);

	int getCache();
	SQLConfig<T> setCache(int cache);

	boolean isExplain();
	SQLConfig<T> setExplain(boolean explain);


	RequestMethod getMethod();
	SQLConfig<T> setMethod(RequestMethod method);

	Object getId();
	SQLConfig<T> setId(Object id);

	Object getIdIn();
	SQLConfig<T> setIdIn(Object idIn);

	Object getUserId();
	SQLConfig<T> setUserId(Object userId);

	Object getUserIdIn();
	SQLConfig<T> setUserIdIn(Object userIdIn);

	String getRole();
	SQLConfig<T> setRole(String role);

	public boolean isDistinct();
	public SQLConfig<T> setDistinct(boolean distinct);

	String getDatabase();
	SQLConfig<T> setDatabase(String database);

	String getSQLNamespace();
	String getNamespace();
	SQLConfig<T> setNamespace(String namespace);

	String getSQLCatalog();
	String getCatalog();
	SQLConfig<T> setCatalog(String catalog);

	String getSQLSchema();
	String getSchema();
	SQLConfig<T> setSchema(String schema);

	String getDatasource();
	SQLConfig<T> setDatasource(String datasource);

	String getQuote();

	List<String> getJson();
	SQLConfig<T> setJson(List<String> json);

	/**请求传进来的Table名
	 * @return
	 * @see {@link #getSQLTable()}
	 */
	String getTable();

	SQLConfig<T> setTable(String table);

	/**数据库里的真实Table名
	 * 通过 {@link AbstractSQLConfig.TABLE_KEY_MAP} 映射
	 * @return
	 */
	String getSQLTable();

	String getTablePath();

	Map<String, String> getKeyMap();
	SQLConfig<T> setKeyMap(Map<String, String> keyMap);

	List<String> getRaw();
	SQLConfig<T> setRaw(List<String> raw);

	Subquery getFrom();
	SQLConfig<T> setFrom(Subquery from);

	List<String> getColumn();
	SQLConfig<T> setColumn(List<String> column);

	List<List<Object>> getValues();
	SQLConfig<T> setValues(List<List<Object>> values);

	Map<String, Object> getContent();
	SQLConfig<T> setContent(Map<String, Object> content);

	Map<String, List<String>> getCombineMap();
	SQLConfig<T> setCombineMap(Map<String, List<String>> combineMap);

	String getCombine();
	SQLConfig<T> setCombine(String combine);

	Map<String, String> getCast();
	SQLConfig<T> setCast(Map<String, String> cast);

	List<String> getNull();
	SQLConfig<T> setNull(List<String> nulls);

	Map<String, Object> getWhere();
	SQLConfig<T> setWhere(Map<String, Object> where);

	String getGroup();
	SQLConfig<T> setGroup(String group);

	Map<String, Object> getHaving();
	SQLConfig<T> setHaving(Map<String, Object> having);

	String getHavingCombine();
	SQLConfig<T> setHavingCombine(String havingCombine);

	String getSample();
	SQLConfig<T> setSample(String order);

	String getLatest();
	SQLConfig<T> setLatest(String latest);

	String getPartition();
	SQLConfig<T> setPartition(String partition);

	String getFill();
	SQLConfig<T> setFill(String fill);

	String getOrder();
	SQLConfig<T> setOrder(String order);

	/**
	 * exactMatch = false
	 * @param key
	 * @return
	 */
	Object getWhere(String key);
	/**
	 * @param key
	 * @param exactMatch
	 * @return
	 */
	Object getWhere(String key, boolean exactMatch);
	/**
	 * @param key
	 * @param value
	 * @return
	 */
	SQLConfig<T> putWhere(String key, Object value, boolean prior);


	boolean isPrepared();

	SQLConfig<T> setPrepared(boolean prepared);

	boolean isMain();

	SQLConfig<T> setMain(boolean main);


	List<Object> getPreparedValueList();
	SQLConfig<T> setPreparedValueList(List<Object> preparedValueList);


	String getAlias();

	SQLConfig<T> setAlias(String alias);

	default String getTableKey() {
		String alias = getAlias();
		return getTable() + (StringUtil.isEmpty(alias) ? "" : ":" + alias);
	}

	default String getSQLAlias() {
		return getSQLAlias(getTable(), getAlias());
	}
	static String getSQLAlias(@NotNull String table, String alias) {
		// 这里不用 : $ 等符号，因为部分数据库/引擎似乎不支持 `key`, "key", [key] 等避免关键词冲突的方式，只能使用符合变量命名的表别名
		return StringUtil.isEmpty(alias) ? table : table + "__" + alias; // 带上原表名，避免 alias 和其它表名/字段名冲突
	}


	String getWhereString(boolean hasPrefix) throws Exception;

	String getRawSQL(String key, Object value) throws Exception;
	String getRawSQL(String key, Object value, boolean throwWhenMissing) throws Exception;

	boolean isKeyPrefix();

	SQLConfig<T> setKeyPrefix(boolean keyPrefix);


	List<Join> getJoinList();

	SQLConfig<T> setJoinList(List<Join> joinList);

	boolean hasJoin();


	String getSubqueryString(Subquery subquery) throws Exception;

	SQLConfig<T> setProcedure(String procedure);



	List<Object> getWithAsExprPreparedValueList();
	SQLConfig<T> setWithAsExprPreparedValueList(List<Object> withAsExprePreparedValueList);

	boolean isFakeDelete();

	Map<String, Object> onFakeDelete(Map<String, Object> map);

}
