package klb.service.kss.control.service;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class JobConfig {

	@Value("${cronExportExp}")
	private String cronExportExp;

	private String ek = "exportControlFile";

	@Value("${cronBatchProcess}")
	private String cronBatchProcess;

	private String bk = "batchProcess";

	@Bean
	public JobDetail exportFileJobDetail() {
		return JobBuilder.newJob(ExportControlFileJob.class).withIdentity(JobKey.jobKey(ek)).storeDurably().build();
	}

	@Bean
	public Trigger exportFileTrigger() {
		return TriggerBuilder.newTrigger().forJob(exportFileJobDetail()).withIdentity(TriggerKey.triggerKey(ek))
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExportExp)).build();

	}

	@Bean
	public JobDetail batchProcessDetail() {
		return JobBuilder.newJob(BatchAccountingJob.class).withIdentity(JobKey.jobKey(bk)).storeDurably().build();
	}

	@Bean
	public Trigger batchProcessTrigger() {
		return TriggerBuilder.newTrigger().forJob(batchProcessDetail()).withIdentity(TriggerKey.triggerKey(bk))
				.withSchedule(CronScheduleBuilder.cronSchedule(cronBatchProcess)).build();

	}
}
