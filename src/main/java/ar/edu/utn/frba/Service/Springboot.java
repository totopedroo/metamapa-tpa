package ar.edu.utn.frba.Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"ar.edu.utn.frba"})
public class Springboot {
    public static void main(String[] args){
        SpringApplication.run(Springboot.class, args);
    }
}
