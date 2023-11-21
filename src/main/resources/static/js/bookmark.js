// 북마크 버튼 클릭 시
function bookMarkToggle(cafeId) {
    var bookMarkEl = document.getElementById('bookmark_' + cafeId);
    if(bookMarkEl.getAttribute('name') == 'checked_bookmark') {
        deleteBookmark(cafeId, bookMarkEl);
    } else {
        addBookMark(cafeId, bookMarkEl);
    }
}

// 북마크 제거 요청
function deleteBookmark(cafeId, bookMarkEl) {

    $.ajax({

        url: '/bookmark/'+ cafeId + '/delete',
        type: 'post',
        contentType: "application/json; charset=utf-8",
        success : function() {
            // 북마크 제거도 필요
            bookMarkEl.name = "bookmark";
            let svgEl = $('#'+ bookMarkEl.id + " svg");
            console.log(svgEl);
            svgEl.attr('class', 'bi bi-heart');
            svgEl.html('<path d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01L8 2.748zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143c.06.055.119.112.176.171a3.12 3.12 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z"/>');
        },
        error:function(request){
            // 권한 없음 에러일 경우 로그인 모달창 보여주기
            $('#loginModal').modal("show");
            console.log("code:"+request.status);
        }
    });

}

// 북마크 추가 요청
function addBookMark(cafeId, bookMarkEl) {

    $.ajax({

        url: '/bookmark/'+ cafeId,
        type: 'post',
        contentType: "application/json; charset=utf-8",
        success : function() {
            // 성공적으로 북마크 추가되면 하트를 눌러진 상태로 변경
            bookMarkEl.name = "checked_bookmark";
            let svgEl = $('#'+ bookMarkEl.id + " svg");
            console.log(svgEl);
            svgEl.attr('class','bi bi-heart-fill');
            svgEl.html('<path fill-rule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"/>');

        },
        error:function(request){
            // 권한 없음 에러일 경우 로그인 모달창 보여주기
            $('#loginModal').modal("show");
            console.log("code:"+request.status);
        }
    });
}