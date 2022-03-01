package cn.zcs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.zcs.mapper")
public class ChenshuStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChenshuStudyApplication.class, args);
    }

}
