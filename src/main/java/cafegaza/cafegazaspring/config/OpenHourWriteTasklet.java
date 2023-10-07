package cafegaza.cafegazaspring.config;

import cafegaza.cafegazaspring.domain.Cafe;
import cafegaza.cafegazaspring.domain.OpenHour;
import cafegaza.cafegazaspring.repository.CafeRepository;
import cafegaza.cafegazaspring.repository.OpenHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class OpenHourWriteTasklet implements Tasklet {

    private final CafeRepository cafeRepository;
    private final OpenHourRepository openHourRepository;
    private Map<String, OpenHour> openHourMap = new HashMap<>();
    private List<String> days = Arrays.asList("월", "화", "수", "목", "금", "토", "일");

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        List<OpenHour> openHourList = new ArrayList<>(); // 생성된 영업 시간 엔티티 리스트
        List<Cafe> cafeList = cafeRepository.findAll(); // 저장한 카페 가져오기
        System.out.println(cafeList.size());

        List<String> range = new ArrayList<>();

        cafeList.forEach(cafe -> {
            String strOpenHours = cafe.getOpenHours().replaceAll(" ", ""); // 문자열 형태의 영업 시간 형식
            if(strOpenHours.contains("휴무")) {
                return;
            }

            int startTime = -1;
            int endTime = -1;
            boolean breakTime = false;

            int i = 0;
            while(i < strOpenHours.length()) {

                try{
                    if(days.contains(strOpenHours.substring(i, i+1))) { // 특정 요일
                        range.add(strOpenHours.substring(i, i+1));
                        i++;

                    } else if(strOpenHours.substring(i, i+2).equals("매일")) { // 모든 요일
                        range.addAll(days);
                        i += 2;

                    } else if(Character.isDigit(strOpenHours.charAt(i)) && strOpenHours.substring(i, i+11).contains("~")) { // 숫자 -> 시작 시간 ~ 종료 시간
                        String[] startAndEnd = strOpenHours.substring(i, i+11).split("~");
                        if(startAndEnd.length == 2) {
                            startTime = getIntegerTime(startAndEnd[0]);
                            endTime = getIntegerTime(startAndEnd[1]);
                        }
                        if(startTime != -1 && endTime != -1 && !range.isEmpty()) {
                            if (!breakTime) {
                                createEntity(cafe, range, startTime, endTime);
                            } else {
                                setBreakTime(range, startTime, endTime);
                                breakTime = false;
                            }
                            range.clear();
                            i += 11;
                        }

                    } else if(strOpenHours.charAt(i) == '~' && !range.isEmpty()) { // 시작 요일 ~ 끝 요일
                        int startIndex = days.indexOf(range.get(range.size()-1));
                        int endIndex = days.indexOf(strOpenHours.substring(i+1, i+2));
                        for (int j = startIndex+1; j <= endIndex; j++) {
                            range.add(days.get(j));
                        }
                        i += 2;

                    } else if(strOpenHours.substring(i, i+4).equals("휴게시간")) { //브레이크 타임 안내
                        breakTime = true;
                        i += 4;

                    } else {
                        i++;
                    }
                }catch (Exception ex) {
                    System.out.println(cafe.getCafeId() + " " + strOpenHours +" 예외 발생");
                    i++;
                }
            }

            for (String day : openHourMap.keySet()) {
                openHourList.add(openHourMap.get(day));
            }
            openHourMap.clear();
        });
        openHourRepository.saveAll(openHourList); // 엔티티 저장
        return RepeatStatus.FINISHED;
    }

    // 엔티티 생성
    private void createEntity(Cafe cafe, List<String> range, int startTime, int endTime) {
        for(String day : range) {
            OpenHour openHour = OpenHour.setOpenHour(cafe, day, startTime, endTime);
            openHourMap.put(day, openHour);
        }
    }

    // 이미 생성된 엔티티에 브레이크 타임 지정
    private void setBreakTime(List<String> range, int startTime, int endTime) {
        for (String day : range) {
            if (openHourMap.containsKey(day)) {
                OpenHour openHour = openHourMap.get(day);
                openHour.setBreakStart(startTime);
                openHour.setBreakEnd(endTime);
            }
        }
    }

    // 문자열 시간을 분단위의 integer 타입으로 변환
    private int getIntegerTime(String time) {
        int result = -1;
        try{
            String[] hourAndMinute = time.split(":");
            if(hourAndMinute.length == 2) {
                int h = Integer.parseInt(hourAndMinute[0]);
                int m = Integer.parseInt(hourAndMinute[1]);
                result = 60 * h + m;
            }
            return result;
        }catch (Exception ex){
            return -1;
        }
    }
}
