package com.edgareldy.bootstrap;

import org.springframework.boot.SpringApplication;

public class TestHexagonalDddTutorialApplication {

	public static void main(String[] args) {
		SpringApplication.from(HexagonalDddTutorialApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
