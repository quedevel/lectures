package hello.querestapi;

import hello.querestapi.commons.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QueRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueRestApiApplication.class, args);
	}

}
