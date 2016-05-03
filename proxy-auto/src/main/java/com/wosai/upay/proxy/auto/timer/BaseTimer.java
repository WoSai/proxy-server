package com.wosai.upay.proxy.auto.timer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseTimer implements InitializingBean {
	
	private Timer timer=new Timer(true);
	
	private List<IBaseTimerTask> baseTimerTaskList=new ArrayList<IBaseTimerTask>();

	@Autowired(required=false)
	public void setBaseTimerTaskList(List<IBaseTimerTask> baseTimerTaskList) {
		this.baseTimerTaskList = baseTimerTaskList;
	}

	@Autowired(required=false)
	public void setBaseTimerTask(IBaseTimerTask baseTimerTask) {
		baseTimerTaskList.add(baseTimerTask);
	}

	public List<IBaseTimerTask> getBaseTimerTaskList() {
		return baseTimerTaskList;
	}

	public void init() {
		if(baseTimerTaskList!=null){
			for(final IBaseTimerTask baseTimerTask:baseTimerTaskList){
				long delay=0;
				if(baseTimerTask.getStartHour()!=null||baseTimerTask.getStartMinuter()!=null||baseTimerTask.getStartSecond()!=null){
					Calendar now=Calendar.getInstance();
					Calendar firstTime=Calendar.getInstance();
					if(baseTimerTask.getStartHour()!=null){
						firstTime.set(Calendar.HOUR_OF_DAY, baseTimerTask.getStartHour());
					}
					if(baseTimerTask.getStartMinuter()!=null){
						firstTime.set(Calendar.MINUTE, baseTimerTask.getStartMinuter());
					}
					if(baseTimerTask.getStartSecond()!=null){
						firstTime.set(Calendar.SECOND, baseTimerTask.getStartSecond());
					}
					while(baseTimerTask.getInterval()!=null&&baseTimerTask.getInterval()>0&&firstTime.getTimeInMillis()<now.getTimeInMillis()){
						firstTime.setTimeInMillis(firstTime.getTimeInMillis()+baseTimerTask.getInterval());
					}
					delay=firstTime.getTimeInMillis()-now.getTimeInMillis();
				}
				timer.schedule(new TimerTask(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try{
							baseTimerTask.run();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}, 0, 0);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.init();
		
	}

}