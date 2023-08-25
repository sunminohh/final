$(() => {
    const $pwd = $(".update-form input[name='checkPassword']");
    const $pwdnew = $(".update-form input[name='newPassword']");
    const $repwdnew = $(".update-form input[name='repwdnew']");
    const pwdReg = /(?=.*[0-9])(?=.*[a-zA-Z])(?=\S+$).{8,16}/;

    const pwdtooltip = $("#pwdtooltip");
    const pwdErrMsg = $("#pwd-error-text");
    const pwdnewErrMsg = $("#pwdnew-error-text");
    const repwdnewErrMsg = $("#repwdnew-error-text");

    let pwdCheck = false;
    let pwdnewCheck = false;
    let repwdnewCheck = false;

    // 재입력 시 검증 결과
    $pwd.keyup(() => pwdCheck = false);
    $pwdnew.keyup(() => pwdnewCheck = false);
    $repwdnew.keyup(() => repwdnewCheck = false);

    $("#btnCancel").click(function () {
        history.back();
    })

    // 비밀번호 변경 이벤트
    $("#action-form").submit(function (e) {
        e.preventDefault();

        const form = $(this);

        const pwdValue = $pwd.val();
        const pwdnewValue = $pwdnew.val();
        const repwdnewValue = $repwdnew.val();


        const check = checkInput();
        if (!check) {
            console.log(check);
            return false;
        }
        console.log("pass");

        $.ajax({
            url: "/user/info/update/password",
            type: "POST",
            data: form.serialize(),
            success: function () {
                successAlert($pwd, "비밀번호가 변경되었습니다.", function () {
                    $pwd.val("");
                    $pwdnew.val("");
                    $repwdnew.val("");

                    // 만약 다른 페이지로 이동 필요할 시
                    location.href = "/user/info/form";

                });

            },
            error: function (e) {
                errorAlert($pwd, e.responseText);
                pwdErrMsg.text("현재 비밀번호를 입력하세요.");
            }
        });

        function checkInput() {
            if (!pwdnewValue) {
                errorAlert($pwdnew, "새 비밀번호를 입력하세요.");
                return false;
            }
            if (pwdValue && pwdnewValue === pwdValue) {
                errorAlert($pwdnew, "비밀번호가 동일합니다.")
                pwdtooltip.hide();
                pwdnewErrMsg.text("현재 비밀번호와 다르게 입력해주세요.");
                return false;
            }

            if (!pwdReg.test(pwdnewValue)) {
                errorAlert($pwdnew, "비밀번호 형식이 아닙니다.");
                return false;
            }

            if (!repwdnewValue) {
                errorAlert($repwdnew, "비밀번호를 확인해주세요");
                repwdnewErrMsg.text("비밀번호 확인을 위해 한 번 더 입력해 주시기 바랍니다.");
                return false;
            }

            if ($repwdnew.val() !== $pwdnew.val()) {
                errorAlert($repwdnew, "비밀번호가 일치하지 않습니다.");
                repwdnewErrMsg.text("다시 입력해주세요.");
                return false;
            }
            return true;
        }
    })

    // 입력 이벤트
    $("input[name='checkPassword']").keyup(() => {
        const pwdValue = $pwd.val();
        if (pwdValue) {
            pwdCheck = false;
        } else {
            pwdErrMsg.text("");
        }
    })

    $("input[name='newPassword']").keyup(() => {
        const pwdValue = $pwd.val();
        const pwdnewValue = $pwdnew.val();
        const pwdReg = /(?=.*[0-9])(?=.*[a-zA-Z])(?=\S+$).{8,16}/;

        if (!pwdnewValue) {
            pwdnewCheck = false;
        } else {
            pwdnewErrMsg.text("");
            pwdtooltip.show();
        }

        if (pwdValue && pwdnewValue === pwdValue) {
            pwdnewCheck = false;
        } else {
            pwdnewErrMsg.text("");
            pwdtooltip.show();
        }
        if (!pwdReg.test(pwdnewValue)) {
            pwdnewCheck = false;
        } else {
            pwdnewErrMsg.text("");
            pwdtooltip.show();
        }


    })

    $("input[name='repwdnew']").keyup(() => {
        const pwdnewValue = $pwdnew.val();
        const repwdnewValue = $repwdnew.val();

        if (!repwdnewValue) {
            repwdnewCheck = false;
        } else {
            repwdnewErrMsg.text("");
            repwdnewCheck = true;
        }
        if (repwdnewValue !== pwdnewValue) {
            repwdnewCheck = false;
        } else {
            repwdnewErrMsg.text("");
            repwdnewCheck = true;
        }
    })

    function handleAjaxError() {
        Swal.fire({
            icon: 'error',
            text: "현재 비밀번호가 일치하지 않습니다.",
        });
    }

    // 경고창
    function errorAlert($el, text) {
        Swal.fire({
            icon: 'error',
            text: text,
        });
        $($el).focus();
    }

    function successAlert($el, text, callback) {
        Swal.fire({
            icon: 'success',
            text: text,
            confirmButtonText: '확인',
            allowOutsideClick: false,
        }).then((result) => {
            if (result.isConfirmed) {
                if (typeof callback === 'function') {
                    callback();
                }
            }
        });
    }
})