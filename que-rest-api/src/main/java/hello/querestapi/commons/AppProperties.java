package hello.querestapi.commons;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "my-app")
@Getter @Setter
public class AppProperties {

    private String adminUsername;
    private String adminPassword;
    private String userUsername;
    private String userPassword;
    private String clientId;
    private String clientSecret;
}
