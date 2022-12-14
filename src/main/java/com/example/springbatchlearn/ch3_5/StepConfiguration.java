package com.example.springbatchlearn.ch3_5;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
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
public class StepConfiguration {

    /* start, next, 각 Step 별 return문 등에 break point를 걸어놓고 디버그하며 Step이 어떻게 생성되서 우리가 참조할 수 있는지 분석해보자 */
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job StepJob() {
        return this.jobBuilderFactory.get("StepJob")
                .start(SJstep1())
                .next(SJstep2())
                .build();
    }

    /*
    * 1. [AbstractTaskletStepBuilder] 사용자가 지정한 이름으로 TaskletStep 생성
    * 2. [StepBuilder] TaskletStepBuilder를 지정한 tasklet과 함께 생성
    * 3. [TaskletStepBuilder] this.tasklet 멤버에 지정한 tasklet을 참조하게 한다.
    * 4. [AbstractTaskletStepBuilder] 이후 build가 되면, step에 필요한 설정 (Chunk, Repeat, Operation등)을 셋팅
    *     1. createTasklet() 호출하여 필요한 Tasklet 생성 → Simple … 등 여러 Taskletbuilder 중 적절한 것을 선택하여 수행
    *     2. [TaskletStepBuilder] 멤버인 tasklet 반환
    * */
    @Bean
    public Step SJstep1() {
        return stepBuilderFactory.get("SJstep1")
                .tasklet(new CustomTasklet())
                .build();
    }

    @Bean
    public Step SJstep2() {
        return stepBuilderFactory.get("SJstep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("SJstep2 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}