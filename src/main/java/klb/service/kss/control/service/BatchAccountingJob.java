package klb.service.kss.control.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class BatchAccountingJob extends QuartzJobBean {
	@Autowired
	private BatchAccountingService batchProcess;

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		batchProcess.batchProcess();
	}
}