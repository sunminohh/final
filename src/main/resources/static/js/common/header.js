$(() => {
    const $username = $("input[name='username']");

    $(".btn-my-anonymous").on("click", function (e) {
        e.preventDefault();
        Swal.fire({
            icon: 'warning',
            title: "이용 제한",
            text: "로그인 후 이용가능합니다. 로그인 하시겠습니까?",
            showCancelButton: true,
            confirmButtonText: '네',
            cancelButtonText: '아니오',
            didClose: () => {
                $username.focus();
            }
        }).then((result) => {
            if (result.isConfirmed) {
                $(".btn-login").trigger("click");
            }
        });
    });
    $("#vip").click(function () {
        Swal.fire({
            icon: 'warning',
            text: '서비스 준비 중 입니다.'
        })
    });
});