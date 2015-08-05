package ch.vivates.tools.dsmlv2;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

/**
 * The Class LdapConnectionPoolFactoryBean contains the parameters for the LdapConnectionPool.
 * 
 * @author Federico Marmory, Post CH, major development
 * @author Kevin Tippenhauer, Berner Fachhochschule, javadoc
 */
public class LdapConnectionPoolFactoryBean {

	/** The max idle. */
	private int maxIdle = GenericObjectPool.DEFAULT_MAX_IDLE;

	/** The min idle. */
	private int minIdle = GenericObjectPool.DEFAULT_MIN_IDLE;

	/** The max active. */
	private int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;

	/** The max wait. */
	private long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;

	/** The when exhausted action. */
	private byte whenExhaustedAction = GenericObjectPool.DEFAULT_WHEN_EXHAUSTED_ACTION;

	/** The test on borrow. */
	private boolean testOnBorrow = GenericObjectPool.DEFAULT_TEST_ON_BORROW;

	/** The test on return. */
	private boolean testOnReturn = GenericObjectPool.DEFAULT_TEST_ON_RETURN;

	/** The test while idle. */
	private boolean testWhileIdle = GenericObjectPool.DEFAULT_TEST_WHILE_IDLE;

	/** The time between eviction runs millis. */
	private long timeBetweenEvictionRunsMillis = GenericObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;

	/** The num tests per eviction run. */
	private int numTestsPerEvictionRun = GenericObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;

	/** The min evictable idle time millis. */
	private long minEvictableIdleTimeMillis = GenericObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

	/** The soft min evictable idle time millis. */
	private long softMinEvictableIdleTimeMillis = GenericObjectPool.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

	/** The lifo. */
	private boolean lifo = GenericObjectPool.DEFAULT_LIFO;

	/**
	 * Gets the config.
	 *
	 * @return the config
	 */
	public Config getConfig() {
		Config c = new Config();
		c.maxIdle = this.maxIdle;
		c.minIdle = this.minIdle;
		c.maxActive = this.maxActive;
		c.maxWait = this.maxWait;
		c.whenExhaustedAction = this.whenExhaustedAction;
		c.testOnBorrow = this.testOnBorrow;
		c.testOnReturn = this.testOnReturn;
		c.testWhileIdle = this.testWhileIdle;
		c.timeBetweenEvictionRunsMillis = this.timeBetweenEvictionRunsMillis;
		c.numTestsPerEvictionRun = this.numTestsPerEvictionRun;
		c.minEvictableIdleTimeMillis = this.minEvictableIdleTimeMillis;
		c.softMinEvictableIdleTimeMillis = this.softMinEvictableIdleTimeMillis;
		c.lifo = this.lifo;
		return c;
	}

	/**
	 * Sets the max idle.
	 *
	 * @param maxIdle the new max idle
	 */
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	/**
	 * Sets the min idle.
	 *
	 * @param minIdle the new min idle
	 */
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	/**
	 * Sets the max active.
	 *
	 * @param maxActive the new max active
	 */
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	/**
	 * Sets the max wait.
	 *
	 * @param maxWait the new max wait
	 */
	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

	/**
	 * Sets the when exhausted action.
	 *
	 * @param whenExhaustedAction the new when exhausted action
	 */
	public void setWhenExhaustedAction(byte whenExhaustedAction) {
		this.whenExhaustedAction = whenExhaustedAction;
	}

	/**
	 * Sets the test on borrow.
	 *
	 * @param testOnBorrow the new test on borrow
	 */
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	/**
	 * Sets the test on return.
	 *
	 * @param testOnReturn the new test on return
	 */
	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	/**
	 * Sets the test while idle.
	 *
	 * @param testWhileIdle the new test while idle
	 */
	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	/**
	 * Sets the time between eviction runs millis.
	 *
	 * @param timeBetweenEvictionRunsMillis the new time between eviction runs millis
	 */
	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	/**
	 * Sets the num tests per eviction run.
	 *
	 * @param numTestsPerEvictionRun the new num tests per eviction run
	 */
	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	/**
	 * Sets the min evictable idle time millis.
	 *
	 * @param minEvictableIdleTimeMillis the new min evictable idle time millis
	 */
	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	/**
	 * Sets the soft min evictable idle time millis.
	 *
	 * @param softMinEvictableIdleTimeMillis the new soft min evictable idle time millis
	 */
	public void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
		this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
	}

	/**
	 * Sets the lifo.
	 *
	 * @param lifo the new lifo
	 */
	public void setLifo(boolean lifo) {
		this.lifo = lifo;
	}

}
