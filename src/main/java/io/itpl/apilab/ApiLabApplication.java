package io.itpl.apilab;

import io.itpl.apilab.jmx.CounterView;
import io.itpl.apilab.services.SudoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.management.*;
import java.lang.management.ManagementFactory;

@SpringBootApplication
public class ApiLabApplication {
	private static final Logger logger = LoggerFactory.getLogger(ApiLabApplication.class);
	private static ApplicationContext context;
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(ApiLabApplication.class);
		context = application.run();
		SudoService service = context.getBean(SudoService.class);
		int res = service.execute();
		logger.info("[{}]ms Api Executed!",res);
		try{
			ObjectName objectName = new ObjectName("io.itpl.apilab.jmx:type=CounterView");
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			server.registerMBean(new CounterView(),objectName);

		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			e.printStackTrace();
		}
	}
	public static ApplicationContext getContext() throws Exception {
		if(context==null) throw new Exception("Context Not Available");
		return context;
	}

}
