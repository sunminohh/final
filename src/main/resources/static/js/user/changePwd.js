$(() => {
    const $pwd = $(".update-form input[name='pwdnow']");
    const $pwdnew = $(".update-form input[name='pwdnew']");
    const $repwdnew = $(".update-form input[name='repwdnew']");

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
        return false;
    })

    // 비밀번호 변경 이벤트
    $("#action-form").submit(function (e) {
        e.preventDefault();

        const $pwd = $(".update-form input[name=pwdnow]");
        const $pwdnew = $(".update-form input[name=pwdnew]");
        const $repwdnew = $(".update-form input[name=repwdnew]");
        const pwdValue = $pwd.val();
        const pwdnewValue = $pwdnew.val();
        const repwdnewValue = $repwdnew.val();
        const pwdReg = /(?=.*[0-9])(?=.*[a-zA-Z])(?=\S+$).{8,16}/;

        if (!pwdValue) {
            errorAlert($pwd, "비밀번호를 입력하세요.");
            pwdErrMsg.text("현재 비밀번호를 입력하세요.");
            return false;
        }
        // todo 비밀번호 db비밀번호 비교
        $.ajax({
            url: "/user/info/checkPwd",
            type: "POST",
            data: {pwd: pwdValue},
            dataType: "text",
            success: function (res) {
                if (res === "yes") {
                    console.log("비밀번호 일치 여부 ->", res);
                    pwdCheck = true;
                } else {
                    console.log("비밀번호 일치 여부 ->", res);
                    pwdCheck = false;
                }
            },
            error: function () {
                console.error("Ajax request fail");
            }
        })

        if (!pwdnewValue) {
            errorAlert($pwdnew, "새 비밀번호를 입력하세요.");
            pwdtooltip.show();
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

        $(this)[0].submit();
    })

    // 입력 이벤트
    $("input[name='pwdnow']").keyup(() => {
        if (!$pwd.val()) {
            pwdCheck = false;
        } else {
            pwdErrMsg.text("");
            pwdCheck = true;
        }
    })

    $("input[name='pwdnew']").keyup(() => {
        const pwdValue = $pwd.val();
        const pwdnewValue = $pwdnew.val();
        const pwdReg = /(?=.*[0-9])(?=.*[a-zA-Z])(?=\S+$).{8,16}/;

        if (pwdValue && pwdnewValue === pwdValue) {
            pwdnewCheck = false;
        } else if (!pwdReg.test(pwdnewValue)) {
            pwdnewCheck = false;
        } else {
            pwdnewErrMsg.text("");
            pwdtooltip.show();
        }

        if (!pwdnewValue) {
            pwdnewCheck = false;
        } else if (!pwdReg.test(pwdnewValue)) {
            pwdnewCheck = false;
        } else {
            pwdnewCheck = true;
            pwdnewErrMsg.text("");
            pwdtooltip.show();
        }

    })

    $("input[name='repwdnew']").keyup(() => {
        const pwdnewValue = $pwdnew.val();
        const repwdnewValue = $repwdnew.val();

        if (!repwdnewValue) {
            repwdnewCheck = false;
        } else if (repwdnewValue !== pwdnewValue) {
            repwdnewCheck = false;
        } else {
            repwdnewErrMsg.text("");
            repwdnewCheck = true;
        }
    })

    // 경고창
    function errorAlert($el, text) {
        Swal.fire({
            icon: 'error',
            text: text,
        });
        $($el).focus();
    }

    function successAlert($el, text) {
        Swal.fire({
            icon: 'success',
            text: text,
        });
        $($el).focus();
    }
})