$(() => {
    const $email = $(".update-form input[name='email']");

    const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const emailErrMsg = $("#email-error-text");

    let emailCheck = false;

    $(".alert-popup").dialog({
        autoOpen: false,
        modal: true
    });

    $(".alert-popup .confirm").click(() => {
        $(".alert-popup").dialog("close");
    })

    $(".btn-layer-close").click(() => {
        $(".alert-popup").dialog("close");
    });

    // 취소 버튼
    $("#btnCancel").click(function () {
        history.back();
        return false;
    })

    // 이미지 등록
    $("#btnAddProfileImg").on('click', function () {
        $("#userImgFile").click();
    })

    $("#userImgFile").on('change', function () {
        const file = $("#userImgFile")[0].files[0];
        if (file.size > 10 * 1024 * 1024) { // 10MB를 초과하는 경우
            Swal.fire({
                icon: 'error',
                text: "파일 크기는 10MB를 초과할 수 없습니다."
            });
            return false;
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            Swal.fire({
                icon: 'success',
                text: '이미지 등록 완료'
            }).then(() => {
                $("#profileImage").attr("src", e.target.result);
                $("#btnAddProfileImg").hide();
                $("#btnDefaultProfileImg").show();
            })
        }
        reader.readAsDataURL(file);

        const formData = new FormData();
        formData.append("file", file);

    })

    // 이미지 삭제
    $("#btnDefaultProfileImg").click(function () {
        let defaultImagePath = $("#profileImage").attr("data-default-src");
        Swal.fire({
            icon: 'warning',
            title: '이미지 삭제',
            text: '이미지를 삭제하시겠습니까?',
            showCancelButton: true,
            confirmButtonText: '확인',
            cancelButtonText: '취소',
        }).then((result) => {
            if (result.isConfirmed){
                $("#profileImage").attr("src", defaultImagePath);
                $("btnDefaultProfileImg").hide();
                $("btnAddProfileImg").show();
                Swal.fire({
                    icon: 'success',
                    text: '이미지가 삭제되었습니다.'
                });
            }
        })
    })

    $("#action-form").submit(function (e) {
        e.preventDefault();

        const form = $(this);

        const emailValue = $email.val();

        if (!emailValue) {
            errorAlert($email, "E-mail을 입력해주세요.");
            emailErrMsg.text("필수 입력 값입니다.").css('color', 'red');
            return false;
        }

        if (!emailReg.test(emailValue)) {
            errorAlert($email, "E-mail 형식이 아닙니다.");
            emailErrMsg.text("name@examble.com").css('color', 'black');
            return false;
        }

        $.ajax({
            url: "/mypage/update",
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
                    return true;
                })
            },
            error: function (e) {
                errorAlert($email, e.responseText);
                return false;
            }
        });
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
            didClose: () => {
                $el.focus();
            }
        });
    }

})