package com.github.andylke.demo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class DemoSpringBatchLocalPartitioning {

  public static void main(String[] args) {
    SpringApplication.run(DemoSpringBatchLocalPartitioning.class, args);
  }
}
