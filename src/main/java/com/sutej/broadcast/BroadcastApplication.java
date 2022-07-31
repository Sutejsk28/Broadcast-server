package com.sutej.broadcast;

import com.sutej.broadcast.config.SwaggerConfig;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfig.class)
public class BroadcastApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


	public static void main(String[] args) {
		try {
			SpringApplication.run(BroadcastApplication.class, args);
		}catch(Exception e){
			System.out.println(e.getClass().getName());
		}

	}

}
