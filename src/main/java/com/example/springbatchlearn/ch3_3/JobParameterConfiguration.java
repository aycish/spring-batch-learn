package com.example.springbatchlearn.ch3_3;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class JobParameterConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /*
    * Job Parameter를 다양한 방법으로 전달하는 것을 테스트한다.
    * 1. jar 실행 시 전달
    * 2. JobParameterBuilder로 전달
    * 3. SpEl로 전달
    * */
    @Bean
    public Job batchJob() {
        return this.jobBuilderFactory.get("Job")
                .start(jpStep1())
                .next(jpStep2())
                .build();
    }

    @Bean
    public Step jpStep1() {
        return stepBuilderFactory.get("jpStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();

                        /* contribution에서는 JobParameters라는 wrapping된 Class로 job parameter에 접근할 수 있다. */
                        jobParameters.getString("name");
                        jobParameters.getLong("seq");
                        jobParameters.getDate("date");
                        jobParameters.getDouble("age");

                        /* chunkContext에서 가져올 때는 각각의 JobParameter를 까서 Map 형태로 가져온다. */
                        Map<String, Object> jobParameters1 = chunkContext.getStepContext().getJobParameters();

                        System.out.println("jpStep1 has executed");

                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step jpStep2() {
        return stepBuilderFactory.get("jpStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("jpStep2 has executed");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
}
