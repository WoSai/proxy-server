package com.wosai.upay.proxy.timer;

import java.util.TimerTask;

public abstract class BaseTimerTask extends TimerTask implements IBaseTimerTask {
	
	private Integer interval = 86400000;

	private Integer startHour;
	
	private Integer startMinuter;
	
	private Integer startSecond;

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Integer getStartHour() {
		return startHour;
	}

	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
	}

	public Integer getStartMinuter() {
		return startMinuter;
	}

	public void setStartMinuter(Integer startMinuter) {
		this.startMinuter = startMinuter;
	}

	public Integer getStartSecond() {
		return startSecond;
	}

	public void setStartSecond(Integer startSecond) {
		this.startSecond = startSecond;
	}

}
