package com.github.andylke.demo.randomuser;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableConfigurationProperties({DownloadImportRandomUserProperties.class})
public class ImportRandomUserLocalPartitioningStepConfig {

  @Autowired private StepBuilderFactory stepBuilderFactory;

  @Autowired private TaskExecutor taskExecutor;

  @Autowired private Step importRandomUserStep;

  @Autowired private DownloadImportRandomUserProperties properties;

  @Bean
  public Step importRandomUserLocalPartitioningStep() {
    return stepBuilderFactory
        .get("importRandomUserLocalPartitioning")
        .partitioner(importRandomUserStep.getName(), importRandomUserPartitioner())
        .step(importRandomUserStep)
        .gridSize(properties.getGridSize())
        .taskExecutor(taskExecutor)
        .build();
  }

  @Bean
  public Partitioner importRandomUserPartitioner() {
    return gridSize -> {
      final Map<String, ExecutionContext> partitions = new HashMap<>();
      for (int partitionNumber = 1; partitionNumber <= gridSize; partitionNumber++) {
        final ExecutionContext executionContext = new ExecutionContext();
        executionContext.put("partitionNumber", partitionNumber);
        partitions.put("partition-" + partitionNumber, executionContext);
      }
      return partitions;
    };
  }
}
