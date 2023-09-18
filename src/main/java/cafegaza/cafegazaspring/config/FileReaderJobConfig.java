package cafegaza.cafegazaspring.config;
import cafegaza.cafegazaspring.dto.CafeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.*;

import java.io.IOException;

// job, step 정의
@Configuration
@RequiredArgsConstructor
public class FileReaderJobConfig extends DefaultBatchConfiguration {

    private final CsvReader csvReader;
    private final CsvWriter csvWriter;
    private final PlatformTransactionManager transactionManager;

    private static final int chunkSize = 500; // 한번에 처리할 데이터 행 사이즈
    /*
        카페 정보 저장 job
        job 은 하나의 배치 작업 단위, 여러 step을 가짐
     */
    @Bean
    public Job csvFileItemReaderJob(JobRepository jobRepository, Step csvFileItemReaderStep) {
        return new JobBuilder("csvFileItemReaderJob", jobRepository)
                .start(csvFileItemReaderStep) // step 지정
                .build();
    }

    /*
        csv 파일 읽고 db 에 쓰는 step
     */
    @Bean
    public Step csvFileItemReaderStep(JobRepository jobRepository) {
        return new StepBuilder("csvFileItemReaderStep", jobRepository)
                .<CafeDto, CafeDto>chunk(chunkSize, transactionManager)
                .reader(csvReader.multiResourceItemReader())
                .writer(csvWriter)
                //.allowStartIfComplete(true) // 이미 completed 된 step 도 재실행하기 위해서 추가
                .build();
    }

}
