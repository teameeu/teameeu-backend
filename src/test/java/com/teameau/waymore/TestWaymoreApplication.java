package com.teameau.waymore;

import org.springframework.boot.SpringApplication;

public class TestWaymoreApplication {

	public static void main(String[] args) {
		SpringApplication.from(WaymoreApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
