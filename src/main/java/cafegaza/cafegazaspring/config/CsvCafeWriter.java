package cafegaza.cafegazaspring.config;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CsvCafeWriter implements ItemWriter<CafeDto> {

    private final CafeRepository cafeRepository;

    /**
        DB 에 쓰기
     */
    @Override
    public void write(Chunk<? extends CafeDto> chunk) throws Exception {

        List<Cafe> cafeList = new ArrayList<>(); // 카페 엔티티 리스트

        // cafe Dto -> Cafe Entity 변환하여 리스트에 삽입
        chunk.forEach(cafeDto -> {

            // Cafe Entity builder
            Cafe cafe = Cafe.builder()
                    .name(cafeDto.getName())
                    .roadAddress(cafeDto.getRoadAddress())
                    .address(cafeDto.getAddress())
                    .openHours(cafeDto.getOpenHours())
                    .telephone(cafeDto.getTelephone())
                    .cafeImageUrl(cafeDto.getCafeImageUrl())
                    .detailUrl(cafeDto.getDetailUrl())
                    .x(cafeDto.getX())
                    .y(cafeDto.getY())
                    .build();

            cafeList.add(cafe);
        });
        cafeRepository.saveAll(new ArrayList<Cafe>(cafeList)); // DB 저장
    }
}
