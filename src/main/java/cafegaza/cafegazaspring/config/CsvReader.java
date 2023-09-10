package cafegaza.cafegazaspring.config;

import cafegaza.cafegazaspring.dto.CafeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


@Configuration
@RequiredArgsConstructor
public class CsvReader {

    // 파일 대상
    @Value("classpath*:/dataset/*.csv")
    private Resource[] inputFiles;

    /*
       카페 정보 파일 읽기
     */
    @Bean
    public FlatFileItemReader<CafeDto> csvCafeInfoReader() {

        // 파일 읽기
        FlatFileItemReader<CafeDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setEncoding("UTF-8");

        // 읽어들일 데이터를 내부적으로 LineMapper 을 통해 Dto 로 매핑해줌
        DefaultLineMapper<CafeDto> defaultLineMapper = new DefaultLineMapper<>();

        // 각각의 데이터 이름 설정 - 매핑할 엔티티 또는 Dto 필드 이름과 동일하게 설정하면 됨
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(",");
        delimitedLineTokenizer.setNames("name", "roadAddress", "address", "openHours", "telephone", "cafeImageUrl", "detailUrl", "x", "y"); //행으로 읽은 데이터에 매칭될 이름
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        // 읽은 데이터를 매칭할 class 타입 지정
        BeanWrapperFieldSetMapper<CafeDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(CafeDto.class);

        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper); // lineMapper 지정

        return flatFileItemReader;

    }

    /*
        여러개의 파일 읽어들이기 위한 함수
     */
    @Bean
    public MultiResourceItemReader<CafeDto> multiResourceItemReader() {
        MultiResourceItemReader<CafeDto> reader = new MultiResourceItemReader<>();
        reader.setDelegate(csvCafeInfoReader()); // 읽기 수행할 reader 지정
        reader.setResources(inputFiles);
        return reader;
    }

}
