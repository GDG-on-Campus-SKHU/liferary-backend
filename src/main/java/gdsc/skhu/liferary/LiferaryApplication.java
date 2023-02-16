package gdsc.skhu.liferary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LiferaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiferaryApplication.class, args);
	}

}
