package com.github.andylke.demo.randomuser;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableConfigurationProperties({DownloadImportRandomUserProperties.class})
public class DownloadRandomUserStepConfig {

  public static final String RANDOM_USER_FILE_PATH_FORMAT = "target/random-user-%d.csv";

  public static final String[] RANDOM_USER_FIELD_NAMES =
      new String[] {
        "gender",
        "name.title",
        "name.first",
        "name.last",
        "email",
        "login.uuid",
        "login.username",
        "login.password",
        "login.salt",
        "login.md5",
        "login.sha1",
        "login.sha256",
        "nat"
      };

  @Autowired private StepBuilderFactory stepBuilderFactory;

  @Autowired private DownloadImportRandomUserProperties properties;

  @Bean
  public Step downloadRandomUserStep() {
    return stepBuilderFactory
        .get("downloadRandomUser")
        .<RandomUser, RandomUser>chunk(properties.getChunkSize())
        .reader(randomUserRestServiceReader(null, null))
        .writer(randomUserFileWriter(null))
        .build();
  }

  @Bean
  @StepScope
  public RandomUserRestServiceReader randomUserRestServiceReader(
      @Value("#{stepExecutionContext['startPagePerPartition']}") Integer startPagePerPartition,
      @Value("#{stepExecutionContext['totalPagePerPartition']}") Integer totalPagePerPartition) {
    return new RandomUserRestServiceReader(
        startPagePerPartition, totalPagePerPartition, properties.getPageSize());
  }

  @Bean
  @StepScope
  public FlatFileItemWriter<? super RandomUser> randomUserFileWriter(
      @Value("#{stepExecutionContext['partitionNumber']}") Integer partitionNumber) {
    return new FlatFileItemWriterBuilder<RandomUser>()
        .name("randomUserFileWriter")
        .resource(
            new FileSystemResource(String.format(RANDOM_USER_FILE_PATH_FORMAT, partitionNumber)))
        .encoding("UTF-8")
        .shouldDeleteIfExists(true)
        .delimited()
        .names(RANDOM_USER_FIELD_NAMES)
        .headerCallback(callback -> callback.write(StringUtils.join(RANDOM_USER_FIELD_NAMES, ",")))
        .build();
  }
}
