package nr.king.wetrack;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import nr.king.wetrack.extensions.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Configuration
@EnableScheduling
public class WetrackApplication  {
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SpringExtension springExtension;
	public static void main(String[] args) {
		SpringApplication.run(WetrackApplication.class, args);
	}

	@Bean
	@ConditionalOnProperty(name = "mode", prefix = "test", havingValue = "false", matchIfMissing = true)
	public ActorSystem actorSystem() {
		ActorSystem system = ActorSystem.create("AkkaTaskProcessing", akkaConfiguration());
		springExtension.initialize(applicationContext);
		return system;
	}

	@Bean
	public Config akkaConfiguration() {
		return ConfigFactory.load();
	}




}
