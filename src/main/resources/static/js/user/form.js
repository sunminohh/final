$(() => {
    const $email = $(".update-form input[name='email']");

    const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const emailErrMsg = $("#email-error-text");

    let emailCheck = false;

    // 취소 버튼
    $("#btnCancel").click(function () {
        history.back();
        return false;
    })

    // todo 프로필 사진
    $("#btnAddProfileImg").click(function () {
        Swal.fire({
            icon: 'warning',
            text: "서비스 준비 중입니다."
        })
    })

    $("#action-form").submit(function (e) {
        e.preventDefault();

        const form = $(this);

        const emailValue = $email.val();

        let check = checkInput();
        if (!check) {
            return false;
        }
        console.log("pass");

        $.ajax({
            url: "/user/info/update",
            type: "POST",
            data: form.serialize(),
            success: function () {
                Swal.fire({
                    icon: 'success',
                    text: "회원정보 수정이 완료되었습니다. 다시 로그인해주세요.",
                    confirmButtonText: '확인',
                }).then((result) => {
                    if (result.isConfirmed) {
                        location.href = "/logout";
                    }
                })
            },
            error: function (e) {
                errorAlert(emailValue, e.responseText);
            }
        });

        function checkInput() {

            if (!emailValue) {
                errorAlert(emailValue, "E-mail을 입력해주세요.");
                emailErrMsg.text("필수 입력 값입니다.").css('color', 'red');
                return false;
            }

            if (!emailReg.test(emailValue)) {
                errorAlert(emailValue, "E-mail 형식이 아닙니다.");
                emailErrMsg.text("name@examble.com").css('color', 'red');
                return false;
            }
            return true;
        }
    });

    $(".update-form input[name='email']").keyup(() => {
        const emailValue = $email.val();

        if (!emailValue) {
            emailCheck = false;
        } else {
            emailErrMsg.text("");
        }
        if (!emailReg.test(emailValue)) {
            emailCheck = false;
        } else {
            emailErrMsg.text("올바른 이메일 형식입니다.").css('color', 'lightgreen');
            emailCheck = true;
        }
    });

    function errorAlert($el, text) {
        Swal.fire({
            icon: 'error',
            text: text,
            didClose: $el.focus()
        });
    }

})