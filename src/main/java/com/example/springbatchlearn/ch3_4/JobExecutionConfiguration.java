package com.example.springbatchlearn.ch3_4;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class JobExecutionConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobExecutionTestJob() {
        return this.jobBuilderFactory.get("jobExecutionTestJob")
                .start(JEstep1())
                .next(JEstep2())
                .build();
    }

    @Bean
    public Step JEstep1() {
        return stepBuilderFactory.get("JEStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                        JobExecution jobExecution = contribution.getStepExecution().getJobExecution();
                        System.out.println("jobExecution = " + jobExecution);

                        System.out.println("JEstep1 has executed");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
    @Bean
    public Step JEstep2() {
        return stepBuilderFactory.get("JEStep2")
                .tasklet((contribution, chunkContext) -> {
//                    throw new RuntimeException("JobExecution has failed");
                   System.out.println("JEStep2 has executed");
                   return RepeatStatus.FINISHED;
                })
                .build();
    }
}