package com.batch.springboot.csvtomysql.config;

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
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.batch.springboot.csvtomysql.model.User;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    
    private final DataSource dataSource;
    @SuppressWarnings("removal")
	private final JobBuilderFactory jobBuilderFactory;
    @SuppressWarnings("removal")
	private final StepBuilderFactory stepBuilderFactory;

    @SuppressWarnings("removal")
	public BatchConfig(DataSource dataSource, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.dataSource = dataSource;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public FlatFileItemReader<User> reader() {
        // Read csv file.
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        // Read from location
        reader.setResource(new ClassPathResource("csv/sample.csv"));
        reader.setLineMapper(lineMapper());
        reader.setLinesToSkip(1);
        return reader;
    }

    @Bean
    public LineMapper<User> lineMapper() {
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[]{"Id","Address","Name", "Salary", "company", "Department"});
        lineTokenizer.setIncludedFields(new int[]{0,1,2,3,5,6});

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public UserItemProcessor processor() {
        return new UserItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<User> writer() {
        JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO user(id, address, name, salary, company, department) VALUES (:id, :address, :name, :salary, :company, :department)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("User-Import-Job")
            .incrementer(new RunIdIncrementer())
            .flow(step1())
            .end()
            .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("Step1")
            .<User, User>chunk(10)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build();
    }
}
