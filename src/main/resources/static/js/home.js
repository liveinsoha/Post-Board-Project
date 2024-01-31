$(document).ready(function () {
    var urlParams = new URLSearchParams(window.location.search);
    var selectedSortOrder = urlParams.get("sort");

    if (selectedSortOrder) {
        if (selectedSortOrder === 'createdDate,asc') {
            $("#sortOrder").val("asc").prop('selected', true);
        } else {
            $("#sortOrder").val("desc").prop('selected', true);
        }
    }
});

function changeSortOrder() {
    var selectedSearchBy = document.getElementById("searchBy").value;
    var searchQuery = document.getElementById("searchQuery").value;

    // 현재 셀렉트 박스의 값과 검색어를 URL에 추가
    var urlParams = `?searchBy=${selectedSearchBy}&searchQuery=${searchQuery}`;
    var selectedSortOrder = $("#sortOrder").val();

    // 동적으로 href 생성
    var dynamicHref = urlParams + "&sort=" + selectedSortOrder;

    // 페이지 이동
    window.location.href = dynamicHref;
}