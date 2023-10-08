package cafegaza.cafegazaspring.config;
import cafegaza.cafegazaspring.dto.CafeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.*;

// job, step 정의
@Configuration
@RequiredArgsConstructor
public class FileReaderJobConfig extends DefaultBatchConfiguration {

    private final CsvCafeReader csvCafeReader;
    private final CsvCafeWriter csvCafeWriter;
    private final CsvMenuReader csvMenuReader;
    private final CsvMenuWriter csvMenuWriter;
    private final OpenHourWriteTasklet openHourWriteTasklet;
    private final PlatformTransactionManager transactionManager;

    private static final int chunkSize = 500; // 한번에 처리할 데이터 행 사이즈

    /**
        카페 정보 저장 job
        job 은 하나의 배치 작업 단위, 여러 step을 가짐
     */
    @Bean
    public Job csvFileCafeReaderJob(JobRepository jobRepository, Step csvFileCafeReaderStep, Step openHourWriteStep) {
        return new JobBuilder("csvFileCafeReaderJob", jobRepository)
                .start(csvFileCafeReaderStep) // step1: 카페 정보 엔티티 저장
                .next(openHourWriteStep) // step2: 영업 시간 엔티티 저장
                .build();
    }

    /**
        csv 파일에서 카페 정보 읽고 db 에 쓰는 step
     */
    @Bean
    public Step csvFileCafeReaderStep(JobRepository jobRepository) {
        return new StepBuilder("csvFileCafeReaderStep", jobRepository)
                .<CafeDto, CafeDto>chunk(chunkSize, transactionManager)
                .reader(csvCafeReader.multiResourceCafeReader())
                .writer(csvCafeWriter)
                //.allowStartIfComplete(true) // 이미 completed 된 step 도 재실행하기 위해서 추가
                .build();
    }

    /**
     * 카페 별 메뉴 정보 저장 job
     */
    @Bean
    public Job csvFileMenuReaderJob(JobRepository jobRepository, Step csvFileMenuReaderStep) {
        return new JobBuilder("csvFileMenuReaderJob", jobRepository)
                .start(csvFileMenuReaderStep)
                .build();
    }

    /**
     * 카페 별 메뉴 정보 저장 step
     */
    @Bean
    public Step csvFileMenuReaderStep(JobRepository jobRepository) {
        return new StepBuilder("csvFileMenuReaderStep", jobRepository)
                .<BatchMenu, BatchMenu>chunk(chunkSize, transactionManager)
                .reader(csvMenuReader.multiResourceMenuReader())
                .writer(csvMenuWriter)
                //.allowStartIfComplete(true) // 이미 completed 된 step 도 재실행하기 위해서 추가
                .build();
    }

    /**
     * 영업 시간 엔티티 저장 step
     */
    @Bean
    public Step openHourWriteStep(JobRepository jobRepository) {
         return new StepBuilder("openHourWriteStep", jobRepository)
                 .tasklet(openHourWriteTasklet, transactionManager)
                 //.allowStartIfComplete(true)
                 .build();
    }


}
