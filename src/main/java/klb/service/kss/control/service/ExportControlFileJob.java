package klb.service.kss.control.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ExportControlFileJob extends QuartzJobBean {
	@Autowired
	private ExportControlFileService exportService;

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		exportService.exportControlFile();
	}
}
