function searchPlaces() {

    keyword = document.getElementById('keyword').value;
    menuOption = document.getElementById('menuOption_btn').innerHTML.trim(); // 값이 없으면 빈문자열 상태
    if (menuOption == '메뉴 종류') {
        menuOption = '';
    }
    // 영업 시간 옵션을 지정했다면
    if (isSetOpenHour){
        startHour = document.getElementById('openHour-left').value; // 영업 시작 시간
        endHour = document.getElementById('openHour-right').value; // 영업 종료 시간
    }

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }
    // api 는 0페이지가 첫페이지
    places = {}; // 딕셔너리 초기화
    searchApi(0);
}

// api 호출하여 현재 페이지에 해당하는 카페 데이터 요청
function searchApi(pageNum) {

    $.ajax({

        url: '/search?page=' + pageNum + '&size=15', // 요쳥을 보낼 서버의 URL 주소 + parameter
        data: JSON.stringify({ // 질의어
            'keyword' : keyword,
            'menuOption' : menuOption,
            'startHour' : startHour,
            'endHour' : endHour,
            'currentOpen': currentOpen,
            'dawnOpen' : dawnOpen,
            'sortType' : sortType
        }),
        type: 'post',
        async : false, // 응답받은 결과를 전역변수에 저장하기 위해
        dataType: 'json', // 요청 시 응답 결과 타입
        contentType: "application/json; charset=utf-8",
        success : function(data) {

            if (data['totalElements'] == 0) { // 검색 결과가 0건이면
                alert('검색 결과가 존재하지 않습니다.');
                return;

            } else{

                places[pageNum+1] = data['content']; // 검색된 카페
                // 검색된 카페가 있을 때
                // 현재 페이지 검색 결과 출력
                // 검색 목록과 마커를 표출합니다
                displayPlaces(pageNum+1);
                // 페이지 번호를 표출합니다
                displayPagination(pageNum+1, data['totalPages']);
            }

            console.log(data);
        },
        error:function(request,status,error){
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }

    });

}