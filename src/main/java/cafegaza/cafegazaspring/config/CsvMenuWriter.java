package cafegaza.cafegazaspring.config;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.Menu;
import cafegaza.cafegazaspring.repository.CafeRepository;
import cafegaza.cafegazaspring.repository.MenuRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CsvMenuWriter implements ItemWriter<BatchMenu> {

    private final MenuRepository menuRepository;
    private final CafeRepository cafeRepository;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class MenuInfo {
        private int price;
        private String menuImage;
        private int popularity;
    }

    /**
     * DB 에 쓰기
     */
    @Override
    public void write(Chunk<? extends BatchMenu> chunk) throws Exception {

        // menuDto -> entity 변환 후 db 저장
        // menuDto -> 하나의 카페에 대한 메뉴 정보가 들어가 있음
        chunk.forEach(batchMenu -> {

            String menuStrDic = batchMenu.getMenuStrDic(); // 메뉴 가져오기
            if(menuStrDic.isBlank()) {
                return; // continue 와 같이 다음으로 넘어감
            }

            List<Menu> menuList = new ArrayList<>(); // db 에 저장할 메뉴 엔티티 리스트
            Cafe findCafe = cafeRepository.findByDetailUrl(batchMenu.getDetailUrl()).get(0); // 대상 카페 찾기
            Map<String, MenuInfo> menuDic = getMenuDict(menuStrDic); // string to dictionary

            for (String menuName : menuDic.keySet()) {
                MenuInfo menuInfo = menuDic.get(menuName);
                Menu menu = Menu.createMenu(findCafe, menuName, menuInfo.getPrice(), menuInfo.getMenuImage(), menuInfo.getPopularity()); // 메뉴 엔티티 생성
                menuList.add(menu);
            }
            menuRepository.saveAll(menuList);
        });
    }

    public Map<String, MenuInfo> getMenuDict(String menuStrDic) {
        Map<String, MenuInfo> result = new HashMap<>();

        menuStrDic = menuStrDic.substring(1, menuStrDic.length()-1);; // { }
        String[] menuArr = menuStrDic.split("],");
        for(int i=0; i<menuArr.length; i++) {
            String[] keyValue = menuArr[i].split(": "); // key: 메뉴이름 value: [가격, 이미지주소]
            MenuInfo menuInfo = new MenuInfo();

            // 메뉴 이름 파싱
            String menuName = keyValue[0].replaceAll("'", "").trim();
            if (menuName.contains("추천")) {
                menuName = menuName.replace("추천", "").trim();
                menuInfo.setPopularity(1); // 추천 여부
            }

            // 메뉴 가격, 이미지 주소 파싱
            String[] values = keyValue[1].split(", ");
            if(values.length == 1){
                continue;
            }
            String price = values[0].replace("[", "").replaceAll("[^0-9]", "").trim(); // 숫자 추출
            String imageUrl = values[1].replaceAll("'", "").replace("]", "").trim();

            if(!price.isEmpty()) { // 가격 정보가 있으면
                menuInfo.setPrice(Integer.parseInt(price));
            }
            menuInfo.setMenuImage(imageUrl);
            // 딕셔너리 삽입
            result.put(menuName, menuInfo);
        }
        return result;
    }
}
