package bssm.bsmauth.global;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:env.properties"),
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:mail/mail.properties")
})
public class PropertyConfig {}