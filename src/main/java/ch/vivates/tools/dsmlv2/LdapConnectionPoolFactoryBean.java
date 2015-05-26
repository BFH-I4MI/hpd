package ch.vivates.tools.dsmlv2;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

public class LdapConnectionPoolFactoryBean {

	private int maxIdle = GenericObjectPool.DEFAULT_MAX_IDLE;

	private int minIdle = GenericObjectPool.DEFAULT_MIN_IDLE;

	private int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;

	private long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;

	private byte whenExhaustedAction = GenericObjectPool.DEFAULT_WHEN_EXHAUSTED_ACTION;

	private boolean testOnBorrow = GenericObjectPool.DEFAULT_TEST_ON_BORROW;

	private boolean testOnReturn = GenericObjectPool.DEFAULT_TEST_ON_RETURN;

	private boolean testWhileIdle = GenericObjectPool.DEFAULT_TEST_WHILE_IDLE;

	private long timeBetweenEvictionRunsMillis = GenericObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;

	private int numTestsPerEvictionRun = GenericObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;

	private long minEvictableIdleTimeMillis = GenericObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

	private long softMinEvictableIdleTimeMillis = GenericObjectPool.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

	private boolean lifo = GenericObjectPool.DEFAULT_LIFO;

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

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

	public void setWhenExhaustedAction(byte whenExhaustedAction) {
		this.whenExhaustedAction = whenExhaustedAction;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
		this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
	}

	public void setLifo(boolean lifo) {
		this.lifo = lifo;
	}

}
