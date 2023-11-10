// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, place) {

    var el = document.createElement('li'),
        fragment = document.createDocumentFragment();

    var a_El = document.createElement('a');
    a_El.href = "/cafe/view/" + place.id;
    a_El.target = "_blank";
    // 대표 이미지
    var imgEl = document.createElement('img');
    imgEl.src = 'https:' + place.cafeImageUrl;
    imgEl.id = "place_image";
    imgEl.className = "imagebg";
    fragment.appendChild(imgEl);

    var infoEl = document.createElement('div'),
        infoFragment = document.createDocumentFragment();
    // 이름
    var nameEl = document.createElement('h5');
    nameEl.id = "place_name";
    nameEl.innerHTML = place.name;
    infoFragment.appendChild(nameEl);

    // 도로명 주소
    if (place.roadAddress) {
        var roadAddressEl = document.createElement('span');
        roadAddressEl.id = "place_roadAddress";
        roadAddressEl.innerHTML = place.roadAddress;
        roadAddressEl.className = "d-block"
        infoFragment.appendChild(roadAddressEl);
    } else if (place.address) { // 지번 주소
        var jibunEl = document.createElement('span');
        jibunEl.id = "place_jibun";
        jibunEl.innerHTML = place.address;
        jibunEl.className = "jibun gray d-block";
        infoFragment.appendChild(jibunEl);
    }

    var starEl = document.createElement('div');
    starEl.id = 'place_rate';
    starEl.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-star-fill" viewBox="0 0 16 16">\n' +
        '  <path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z"/>\n' +
        '</svg>\n' + '<span style="display: inline-block; margin-left: 5px;">별점 ' + place.rate + '</span>\n';

    var reviewEl = document.createElement('div');
    reviewEl.id = 'place_review';
    reviewEl.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-fill" viewBox="0 0 16 16">\n' +
        '  <path d="M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708l-3-3zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207l6.5-6.5zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.499.499 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11l.178-.178z"/>\n' +
        '</svg>\n' + '<span style="display: inline-block; margin-left: 5px;">리뷰 ' + place.reviewCount + '</span>\n';

    infoFragment.appendChild(starEl);
    infoFragment.appendChild(reviewEl);

    infoEl.appendChild(infoFragment);
    infoEl.id = "place_info";
    infoEl.className = "info d-inline-block";
    fragment.appendChild(infoEl);

    var bookMarkEl = document.createElement('button');
    bookMarkEl.type = "button";
    bookMarkEl.className = "btn float-right shadow-none";
    bookMarkEl.style.padding = "0px";
    bookMarkEl.id = "bookmark_" + place.id;
    bookMarkEl.addEventListener('click', () => bookMarkToggle(place.id));
    // 북마크 여부
    if(place.isBookmark) {
        bookMarkEl.name = "checked_bookmark";
        bookMarkEl.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" className="bi bi-heart-fill" viewBox="0 0 16 16">' +
            '<path fill-rule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"/>' + '</svg>'
    } else {
        bookMarkEl.name = "bookmark";
        bookMarkEl.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-heart" viewBox="0 0 16 16">' +
            '  <path d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01L8 2.748zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143c.06.055.119.112.176.171a3.12 3.12 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z"/>' +
            '</svg>'
    }

    a_El.appendChild(fragment);
    el.appendChild(a_El);
    el.appendChild(bookMarkEl);
    el.className = 'item list_overlay';

    return el;
}

