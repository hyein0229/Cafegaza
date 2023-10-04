package cafegaza.cafegazaspring.service;

import cafegaza.cafegazaspring.dto.KakaoSearchApiResDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;

@Component
public class KakaoOpenApiManager {

    @Value("${kakaomap-restapikey}")
    private String restApiKey;
    private String API_SERVER_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    /**
        카카오맵 키워드로 장소 검색하기 rest api 호출
     */
    public KakaoSearchApiResDto kakaomapSearchApi(String query) throws Exception {
        String uriQuery = "?page=1&size=15&sort=accuracy&query="+ URLEncoder.encode(query, "UTF-8");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "KakaoAK " + restApiKey);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE); // 응답 받는 타입 지정
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8"); // 요청 보내는 타입 지정

        URI uri = URI.create(API_SERVER_URL + uriQuery);
        RequestEntity<KakaoSearchApiResDto> request = new RequestEntity<>(headers, HttpMethod.GET, uri);
        ResponseEntity<KakaoSearchApiResDto> response = restTemplate.exchange(request, KakaoSearchApiResDto.class); // api 호출하여 응답 받음
        return response.getBody();

    }
}
