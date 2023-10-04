package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.dto.CafeDto;
import cafegaza.cafegazaspring.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeService {
     private final CafeRepository cafeRepository;

     public CafeDto findById(Long cafeId) {
         Cafe findCafe = cafeRepository.findById(cafeId).get();
         return CafeDto.toDto(findCafe);
     }
}
