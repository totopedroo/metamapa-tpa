package ar.edu.utn.frba.server.fuente.proxy.services;

import lombok.Getter; import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "proxy.desastres")
public class DesastresProps {
    private String baseUrl;
    private String email;
    private String password;
    private int perPage = 50;
}
