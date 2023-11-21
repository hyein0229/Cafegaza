// 정렬 옵션 설정
function setSortOption(option) {
    var optionGroup = document.getElementById('sort_option').children;
    var index = option.id.split('_')[1];

    // 이미 선택되어 있는 옵션을 눌렀다면
    if(sortType == index) {
        return;
    }
    // 옵션 설정 후 검색
    sortType = index;
    for (var i=0; i < optionGroup.length; i++) {
        if (optionGroup[i].id.split('_')[1] != index) {
            optionGroup[i].removeAttribute('style');
            // } else {
            option.style.backgroundColor = "#0755eb";
            option.style.color = "white";
        }
    }
    searchPlaces();
}

// 영업 중, 새벽운영 옵션 설정
function toggleActive(num) {
    var status; // 기존 버튼의 상태
    let target;
    if (num == 1) {
        target = document.getElementById('currentOpen_btn');
        status = currentOpen;
        currentOpen = (currentOpen == 0) ? 1 : 0
    } else if(num == 2){
        target = document.getElementById('dawnOpen_btn');
        status = dawnOpen;
        dawnOpen = (dawnOpen == 0) ? 1 : 0
    }

    if (status) {
        target.classList.remove('option_active');
    } else {
        target.classList.add('option_active');
    }
}

// 메뉴 검색 옵션 설정
function setMenuOption(menuItem) {

    var menuOptEl = document.getElementById("menuOption_btn");
    if (menuItem == '메뉴 전체'){
        menuOptEl.innerHTML = ' 메뉴 종류 ';
        menuOptEl.classList.remove('selected');
    } else {
        menuOptEl.innerHTML = ' ' + menuItem + ' ';
        menuOptEl.classList.add('selected');
    }
}

// 모든 검색 옵션 설정을 초기화하기
function optionReset() {
    // 메뉴 검색 옵션 설정 제거
    menuOption = '';
    setMenuOption('메뉴 전체');
    // 영업 시간 옵션 설정 제거
    removeOpenHourOption();
    // 영업 중, 새벽 운영 옵션 설정 제거
    if(currentOpen == 1){
        document.getElementById('currentOpen_btn').classList.remove('option_active');
        currentOpen = 0;
    }
    if(dawnOpen == 1){
        document.getElementById('dawnOpen_btn').classList.remove('option_active');
        dawnOpen = 0;
    }
}
