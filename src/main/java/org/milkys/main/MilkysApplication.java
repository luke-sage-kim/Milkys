package org.milkys.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "org.milkys")
@EntityScan(basePackages = "org.milkys.domain")
@EnableJpaRepositories(basePackages = "org.milkys.domain")  // 리포지토리 경로 명시
public class MilkysApplication {


    public static void main(String[] args) {
        SpringApplication.run(MilkysApplication.class,args);
    }
}
