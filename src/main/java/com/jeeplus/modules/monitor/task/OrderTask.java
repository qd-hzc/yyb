package com.jeeplus.modules.monitor.task;

import com.jeeplus.modules.api.order.service.YybOrderApiService;
import org.quartz.DisallowConcurrentExecution;

import com.jeeplus.modules.monitor.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;

@DisallowConcurrentExecution  
public class OrderTask extends Task{

	@Autowired
	private YybOrderApiService yybOrderApiService;

	@Override
	public void run() {
		//三天
		yybOrderApiService.cancelOvertimeOrder();
	}

}
