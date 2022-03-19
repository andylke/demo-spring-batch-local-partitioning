package com.github.andylke.demo.randomuser;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DownloadImportRandomUserJobConfig {

  @Autowired private JobBuilderFactory jobBuilderFactory;

  @Autowired private Step downloadRandomUserLocalPartitioningStep;

  @Autowired private Step importRandomUserLocalPartitioningStep;

  @Bean
  public Job downloadImportRandomUserJob() {
    return jobBuilderFactory
        .get("downloadImportRandomUser")
        .incrementer(new RunIdIncrementer())
        .start(downloadRandomUserLocalPartitioningStep)
        .next(importRandomUserLocalPartitioningStep)
        .build();
  }
}
