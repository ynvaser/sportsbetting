package systems.bdev.sportsbetting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients(basePackages = "systems.bdev.sportsbetting.client.*")
@Slf4j
public class SportsbettingApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SportsbettingApplication.class, args);
    }

}
