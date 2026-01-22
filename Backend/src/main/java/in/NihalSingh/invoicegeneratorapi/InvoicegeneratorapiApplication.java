package in.NihalSingh.invoicegeneratorapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InvoicegeneratorapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoicegeneratorapiApplication.class, args);
	}
}
