package com.wosai.upay.proxy.timer;

public interface IBaseTimerTask {

	/**
	 * 执行间隔
	 * @return
	 */
	public Integer getInterval();

	/**
	 * 开始执行小时
	 * @return
	 */
	public Integer getStartHour();

	/**
	 * 开始执行分钟
	 * @return
	 */
	public Integer getStartMinuter();
	
	/**
	 * 开始执行秒
	 * @return
	 */
	public Integer getStartSecond();

	/**
	 * 定时执行任务
	 */
	public void run();

}
