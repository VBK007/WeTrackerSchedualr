package nr.king.wetrack.scheduler;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import nr.king.wetrack.extensions.SpringExtension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;

@Service
public class CronJob extends QuartzJobBean {

    @Autowired
    private SpringExtension springExtension;

    @Autowired
    private ApplicationContext applicationContext;

    private ActorSystem actorSystem;

    private final static Logger logger = LogManager.getLogger(CronJob.class);

    @PostConstruct
    public void postConstruct() throws URISyntaxException, InstantiationException {
        actorSystem = applicationContext.getBean(ActorSystem.class);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
        Long userId = mergedJobDataMap.getLong("userId");
        String task = mergedJobDataMap.getString("task");
        logger.info("************* Scheduler Execution for - " + userId + "_" + task + " started! *************");
      /*  try {
            if (!akkaMasterRepo.findByUserIdAndStatusAndTask(userId, "inprogress", task).isPresent()) {
                logger.info(String.format("************* Akka queue Execution for userId -> %s and task -> %s started! *************", userId, task));
                akkaMasterRepo.akkaAudit(userId, "inprogress", task, "GoDeliver", "Akka Queue Started successfully.!");
                if (actorSystem.actorFor("/user/" + userId).isTerminated()) {
                    ActorRef actor = actorSystem.actorOf(springExtension.props("AsyncJob"), String.valueOf(userId));
                    actor.tell(new User(userId, task), ActorRef.noSender());
                } else {
                    actorSystem.actorSelection("/user/" + userId).tell(new User(userId, task), ActorRef.noSender());
                }
            } else {
                logger.info(String.format("************* Akka queue Execution for  userId -> %ds and task -> %s skipped because already in running state! *************", userId, task));
            }
        } catch (Exception e) {
            akkaMasterRepo.akkaAudit(userId, "completed", task, "GoDeliver", String.format("Akka Queue execution failed for userId -> %s and task -> %s due to - %s", userId, task, e.getMessage()));
            logger.error(String.format("[Akka] [pos data sync] Exception while executing the akka queue due to - %s", e.getMessage()), e);
        }*/
        logger.info("************* Scheduler Execution for - " + userId + "_" + task + " ended! *************");
    }
}
