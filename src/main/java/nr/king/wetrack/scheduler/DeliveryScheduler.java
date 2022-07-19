package nr.king.wetrack.scheduler;

import com.gofrugal.delivery_data_sync_service.config.JdbcTemplateProvider;
import com.gofrugal.delivery_data_sync_service.domain.SchedulerMaster;
import com.gofrugal.delivery_data_sync_service.repo.SchedulerMasterRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DeliveryScheduler {

    private final static Logger logger = LogManager.getLogger(DeliveryScheduler.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SchedulerMasterRepo schedulerMasterRepo;

    @Autowired
    private JdbcTemplateProvider jdbcTemplateProvider;

    @PostConstruct
    public void schedule() {
        jdbcTemplateProvider.getTemplate().update("update akka_master set status ='complete'");
        jdbcTemplateProvider.getTemplate().update("update sync_tracker set status ='Success',remarks='Sync Success'");
        schedulerMasterRepo.findSchedulerList(1, "push_data_to_pos").forEach(this::unschedule);
        schedulerMasterRepo.findSchedulerList(0, "push_data_to_pos").forEach(this::createScheduler);
    }

    public void createScheduler(SchedulerMaster schedulerMaster) {
        try {
            logger.info(String.format("\n\n************* Scheduler Starts for user Id - %s Task - %s ***************\n\n ", schedulerMaster.getUserId(), schedulerMaster.getTask()));
            scheduler.unscheduleJob(new TriggerKey(schedulerMaster.getUserId() + "_" + schedulerMaster.getTask()));
            JobDetail jobDetail = buildJobDetail(schedulerMaster.getTask(), schedulerMaster.getUserId());
            scheduler.scheduleJob(jobDetail, buildJobTrigger(jobDetail, schedulerMaster.getTask(), schedulerMaster.getUserId(), schedulerMaster.getCronString()));
//            auditUtils.syncTracking(schedulerMaster.getUserId(), -1, 0, "success", "schedule cron", "Cron Scheduling is Successful");
        } catch (SchedulerException e) {
//            auditUtils.syncTracking(schedulerMaster.getUserId(), -1, 0, "failed", "schedule cron", String.format("Error due to - %s Exception stack Trace is - %s", e.getMessage(), e));
            logger.error("[Data Sync] Exception while scheduling pos Data sync due to " + e.getMessage(), e);
        }
    }

    public void unschedule(SchedulerMaster schedulerMaster) {
        try {
            logger.info(String.format("\n\n************* unSchedule Starts for user Id - %s Task - %s *************** \n\n", schedulerMaster.getUserId(), schedulerMaster.getTask()));
            scheduler.unscheduleJob(new TriggerKey(schedulerMaster.getUserId() + "_" + schedulerMaster.getTask()));
//            auditUtils.syncTracking(schedulerMaster.getUserId(), -1, 0, "success", "unSchedule cron", "Cron unScheduling is Successful");
        } catch (SchedulerException e) {
//            auditUtils.syncTracking(schedulerMaster.getUserId(), -1, 0, "failed", "unSchedule cron", String.format("Error due to - %s Exception stack Trace is - %s", e.getMessage(), e));
            logger.error("[Data Sync] Exception while unScheduling pos Data sync due to " + e.getMessage(), e);
        }
    }

    private JobDetail buildJobDetail(String task, Long userId) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("task", task);
        jobDataMap.put("userId", userId);
        return JobBuilder.newJob(CronJob.class)
                .usingJobData(jobDataMap)
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, String task, Long userId, String cronString) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronString))
                .forJob(jobDetail)
                .withIdentity(new TriggerKey(userId + "_" + task))
                .build();
    }
}
