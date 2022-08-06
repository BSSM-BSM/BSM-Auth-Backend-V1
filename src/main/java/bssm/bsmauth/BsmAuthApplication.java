package bssm.bsmauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BsmAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(BsmAuthApplication.class, args);
	}

}
