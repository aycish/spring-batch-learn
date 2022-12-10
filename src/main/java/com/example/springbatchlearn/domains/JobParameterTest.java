package com.example.springbatchlearn.domains;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JobParameterTest implements ApplicationRunner {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Map<String,Job> jobMap;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        /* JobParametersBuilder를 통해 parameter를 전달하는 방법 */
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user1")
                .addLong("seq", 2L)
                .addDate("date", new Date())
                .addDouble("age", 16.5)
                .toJobParameters();

        jobLauncher.run(jobMap.get("batchJob"), jobParameters);
    }
}
