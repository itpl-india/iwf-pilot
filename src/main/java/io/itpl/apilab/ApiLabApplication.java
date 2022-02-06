package io.itpl.apilab;

import io.itpl.apilab.services.SudoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ApiLabApplication {
	private static final Logger logger = LoggerFactory.getLogger(ApiLabApplication.class);
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(ApiLabApplication.class);
		ApplicationContext context = application.run();
		SudoService service = context.getBean(SudoService.class);
		int res = service.execute();
		logger.info("[{}]ms Api Executed!",res);

	}

}
