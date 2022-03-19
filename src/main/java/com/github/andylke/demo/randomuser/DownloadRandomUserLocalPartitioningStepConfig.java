package com.github.andylke.demo.randomuser;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableConfigurationProperties({DownloadImportRandomUserProperties.class})
public class DownloadRandomUserLocalPartitioningStepConfig {

  @Autowired private StepBuilderFactory stepBuilderFactory;

  @Autowired private TaskExecutor taskExecutor;

  @Autowired private Step downloadRandomUserStep;

  @Autowired private DownloadImportRandomUserProperties properties;

  @Bean
  public Step downloadRandomUserLocalPartitioningStep() {
    return stepBuilderFactory
        .get("downloadRandomUserLocalPartitioning")
        .partitioner(downloadRandomUserStep.getName(), downloadRandomUserPartitioner())
        .step(downloadRandomUserStep)
        .gridSize(properties.getGridSize())
        .taskExecutor(taskExecutor)
        .build();
  }

  @Bean
  public DownloadRandomUserPartitioner downloadRandomUserPartitioner() {
    return new DownloadRandomUserPartitioner(properties.getTotalPage());
  }
}
