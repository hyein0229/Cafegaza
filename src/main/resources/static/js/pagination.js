// 데이터를 한 번에 가져와서 페이지별로 화면 출력 구현
// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(page, totalPages) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment();

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild (paginationEl.lastChild);
    }

    let showPage = 5; // 화면에 보일 페이지 번호 개수
    let totalPage = totalPages; // 전체 페이지 수
    if(totalPage > 34) {
        totalPage = 34; // 전체 페이지는 34까지 제한 -> 최대 15x34 = 510 개의 카페 목록 제공
    }

    let pageGroupNum = Math.ceil(page / showPage); // 현재 페이지 그룹
    let lastPage = pageGroupNum * showPage; // 화면에 보여주는 마지막 페이지의 번호
    if (lastPage > totalPage) {
        lastPage = totalPage;
    }
    let firstPage = (pageGroupNum - 1) * showPage + 1

    let prev = firstPage - 1;
    let next = lastPage + 1;

    if (prev != 0) { // 이전 페이지가 있으면
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = '<';
        el.onclick = gotoPage(prev, totalPages);
        fragment.appendChild(el);
    }

    for (let i=firstPage; i<=lastPage; i++) { // 페이지 번호 생성
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i===page){
            el.className = 'on';
        } else {
            el.onclick = gotoPage(i, totalPages);
        }
        fragment.appendChild(el);
    }

    if (next <= totalPage) { // 다음 페이지가 있으면
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = '>';
        el.onclick = gotoPage(next, totalPages);
        fragment.appendChild(el);
    }

    paginationEl.appendChild(fragment);
}

// 페이지 번호 클릭 시 해당 페이지 목록으로 이동시키는 함수
function gotoPage(nextPage, totalPages) {
    return function() {
        if(nextPage in places) { // 이미 조회했던 페이지라면
            displayPlaces(nextPage);
            displayPagination(nextPage, totalPages);
        } else{
            searchApi(nextPage-1); // api는 0페이지부터 검색하므로 -1하여 호출
        }
    }
}