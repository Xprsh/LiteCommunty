package top.youmunan.communty;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("top.youmunan.communty.mapper")
public class CommuntyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommuntyApplication.class, args);
    }

}
