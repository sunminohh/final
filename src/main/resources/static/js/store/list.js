$(document).ready(function() {
    // 이전에 클릭한 카테고리 정보를 로컬 스토리지에서 가져와서 적용
    const selectedCategoryId = localStorage.getItem("selectedCategoryId");
    if (selectedCategoryId) {
        $("#" + selectedCategoryId).addClass("on");
    }

    $(".tab-list ul li").on("click", function() {
        // 기존에 추가된 'on' 클래스를 모두 제거
        $(".tab-list ul li").removeClass("on");

        // 클릭한 카테고리의 li 태그에 'on' 클래스를 추가
        $(this).addClass("on");

        // 클릭한 카테고리의 id를 로컬 스토리지에 저장
        localStorage.setItem("selectedCategoryId", $(this).attr("id"));
    });

    function addCommasToPrice() {
        let priceElements = document.querySelectorAll('.price-detail', '.original');
        priceElements.forEach(function(element) {
            let priceValue = element.textContent;
            let formattedPrice = parseFloat(priceValue).toLocaleString('en-US');
            element.textContent = formattedPrice;
        });
    }

    window.onload = addCommasToPrice;
});




