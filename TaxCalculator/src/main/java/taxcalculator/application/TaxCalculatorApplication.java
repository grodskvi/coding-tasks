package taxcalculator.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TaxCalculatorApplication {
	
	private ConfigurableApplicationContext applicationContext;
	
	public void start(String... args) {
		if(applicationContext != null) {
			throw new RuntimeException("TaxCalculator application is already running");
		}
		applicationContext = SpringApplication.run(new Object[] {TaxCalculatorApplication.class, TaxCalculatorApplicationConfiguration.class}, args);
	}
	
	public void stop() {
		if(applicationContext == null) {
			throw new RuntimeException("TaxCalculator application is already stopped");
		}
		SpringApplication.exit(applicationContext);
		applicationContext = null;
	}
	
	public static void main(String[] args) {
		new TaxCalculatorApplication().start(args);
	}

}
