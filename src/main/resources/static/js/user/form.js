$(() => {

    // 취소 버튼
    $("#btnCancel").click(function () {
        history.back();
        return false;
    })
})