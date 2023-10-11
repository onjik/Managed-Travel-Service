package click.porito.modular_travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
public class ModularTravelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModularTravelApplication.class, args);
	}

}
