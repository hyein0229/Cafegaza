const inputLeft = document.getElementById("openHour-left");
const inputRight = document.getElementById("openHour-right");

const thumbLeft = document.querySelector(".thumb.left");
const thumbRight = document.querySelector(".thumb.right");

const range = document.querySelector(".range");

const rangeText = document.getElementById("range_text");

const setLeftValue = e => {
    const _this = e.target;
    const { value, min, max } = _this;
    if (+inputRight.value - +value <= 1) {
        _this.value = +inputRight.value - 1;
    }
    const percent = ((+_this.value - +min) / (+max - +min)) * 100;

    // 왼쪽으로부터 떨어진 정도
    thumbLeft.style.left = `${percent}%`;
    range.style.left = `${percent}%`;
    isSetOpenHour = 1;
    changeRangeText(inputLeft.value, inputRight.value);
};

const setRightValue = e => {
    const _this = e.target;
    const { value, min, max } = _this;
    if (+value - +inputLeft.value <= 1) {
        _this.value = +inputLeft.value + 1;
    }
    const percent = ((+_this.value - +min) / (+max - +min)) * 100;

    // 오른쪽으로부터 떨어진 정도
    thumbRight.style.right = `${100 - percent}%`;
    range.style.right = `${100 - percent}%`;
    isSetOpenHour = 1;
    changeRangeText(inputLeft.value, inputRight.value);
};

function changeRangeText(leftValue, rightValue) {
    let text = '';
    // 영업 시작, 종료 시간
    text += getTime(leftValue);
    text += ' ~ '
    text += getTime(rightValue);
    // rangeText.innerHTML = text + '<button type="button" className="btn btn-light">Light</button>';
    rangeText.innerHTML = '<button type="button" onclick="removeOpenHourOption()" class="btn btn-light" style="padding: 4px; border-radius: 15px; margin-top: 5px;">' + text
        + '<span class="badge" style="padding: 4px;">x</span>' + '</button>'
}

function removeOpenHourOption() {
    if(isSetOpenHour) {
        // 초기화
        rangeText.innerHTML = '';
        isSetOpenHour = 0;
        startHour = 0;
        endHour = 0;

        inputLeft.value = 0;
        inputRight.value = 24;
        thumbRight.removeAttribute('style');
        thumbLeft.removeAttribute('style');
        range.removeAttribute('style');
    }
}

function openHourReset() {

}
function getTime(value) {
    if (value < 12) {
        return '오전 ' + value + '시';
    } else {
        if (value == 24){
            return '자정';
        }
        return '오후 ' + ((value > 12) ? (value % 12) : 12) + '시';
    }
}

if (inputLeft && inputRight) {
    inputLeft.addEventListener("input", setLeftValue);
    inputRight.addEventListener("input", setRightValue);
    inputLeft.addEventListener("click", setLeftValue);
    inputRight.addEventListener("click", setRightValue);

}