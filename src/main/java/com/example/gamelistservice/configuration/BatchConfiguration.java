package com.example.gamelistservice.configuration;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.gamelistservice.batch.GameItemProcessor;
import com.example.gamelistservice.model.Game;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

 @Autowired
 public JobBuilderFactory jobBuilderFactory;
 
 @Autowired
 public StepBuilderFactory stepBuilderFactory;
 
 @Autowired
 public DataSource dataSource;
 
 
 @Bean
 public FlatFileItemReader<Game> reader(){
  FlatFileItemReader<Game> reader = new FlatFileItemReader<Game>();
  reader.setResource(new ClassPathResource("games.csv"));
  reader.setLineMapper(new DefaultLineMapper<Game>() {{
   setLineTokenizer(new DelimitedLineTokenizer() {{
    setNames(new String[] {"title", "platform", "score", "genre", "editors_choice"});
   }});
   setFieldSetMapper(new BeanWrapperFieldSetMapper<Game>() {{
    setTargetType(Game.class);
   }});
   
  }});
  
  return reader;
 }
 
 @Bean
 public GameItemProcessor processor(){
  return new GameItemProcessor();
 }
 
 @Bean
 public JdbcBatchItemWriter<Game> writer(){
  JdbcBatchItemWriter<Game> writer = new JdbcBatchItemWriter<Game>();
  writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Game>());
  writer.setSql("INSERT INTO game(title, platform, score, genre, editors_choice) VALUES (:title, :platform, :score, :genre, :editors_choice )");
  writer.setDataSource(dataSource);
  
  return writer;
 }
 
 @Bean
 public Step step1() {
  return stepBuilderFactory.get("step1").<Game, Game> chunk(3)
    .reader(reader())
    .processor(processor())
    .writer(writer())
    .build();
 }
 
 @Bean
 public Job importUserJob() {
  return jobBuilderFactory.get("importGameJob")
    .incrementer(new RunIdIncrementer())
    .flow(step1())
    .end()
    .build();
 }
 
}