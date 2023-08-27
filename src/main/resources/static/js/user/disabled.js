$(() => {
    const $pwd = $(".disable-form input[name='checkPassword']");
    const $email = $(".disable-form input[name='email']");
    const $inputAuth = $("#userAuth");
    const $etcText = $("#etcText");
    const timerDisplay = $("#schEmailtimer");

    const btnConfirm = $("#btnConfirm");
    const btnSendEmail = $("#btnSendAuthMail");
    const btnResendEmail = $("#btnResendAuthMail");

    const pwdErrMsg = $("#pwd-error-text");
    const emailErrMsg = $("#email-error-text");
    const authErrMsg = $("#auth-error-text");

    const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    let pwdCheck = false;
    let emailCheck = false;
    let authCheck = false;

    let timer;
    let timeLeft = 180;

    $pwd.keyup(() => pwdCheck = false);
    $email.keyup(() => emailCheck = false);
    $inputAuth.keyup(() => authCheck = false);

    btnSendEmail.click(() => sendNumber()); // 인증요청
    btnResendEmail.click(() => resendNumber()); // 재전송
    btnConfirm.click(() => checkNumber()); // 인증하기

    $("#btnCancel").click(function () {
        history.back();
    })

    // enter 방지
    $("#action-form").on("keydown", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            return false;
        }
    });

    function startTimer() {
        timer = setInterval(() => {
            if (timeLeft > 0) {
                timeLeft--;
                btnResendEmail.prop('disabled', true);
                $("#auth-number").show();
                updateTimerDisplay();
            } else {
                clearInterval(timer);
                updateTimerDisplay(0);
                Swal.fire({
                    icon: 'error',
                    text: '인증 시간이 초과되었습니다. 다시 인증해주세요.',
                }).then(() => {
                    $("#auth-number").hide();
                    btnResendEmail.prop('disabled', false);
                    $inputAuth.val("");
                    return false;
                });
            }
            return true;
        }, 1000); // 1초마다 타이머 업데이트
    }

    // 타이머 종료 후 동작
    function stopTimer() {
        clearInterval(timer);
        timerDisplay.text("0:00"); // 타이머 종료 표시 업데이트
        btnConfirm.prop("disabled", true); // 인증하기 버튼 비활성화
        $inputAuth.prop("readonly", true); // 인증번호 입력 필드 읽기 전
    }

    // 타이머 표시 업데이트
    function updateTimerDisplay() {
        const minutes = Math.floor(timeLeft / 60);
        const seconds = timeLeft % 60;
        const formattedTime = `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
        timerDisplay.text(formattedTime);
    }

    // todo 탈퇴 버튼 클릭
    $("#action-form").submit(function (e) {
        e.preventDefault();

        const formData = $("#action-form").serialize();

        const pwdValue = $pwd.val();
        const etcRadio = $("#userout07").prop('checked');
        const etcText = $etcText.val();
        const korReg = /^[가-힣]+$/;

        if (!pwdValue) {
            errorAlert($pwd, "비밀번호를 입력해주세요.");
            pwdErrMsg.text("")
            return false;
        }

        if (etcRadio == true && !etcText) {
            errorAlert($etcText, "기타 항목은 입력하셔야 합니다.");
            return false;
        }

        if (!korReg.test(etcText)) {
            Swal.fire({
                icon: 'error',
                title: "기타",
                text: "정확한 이유를 작성해주세요.",
                didClose: () => {
                    $etcText.focus();
                }
            });
            $etcText.val("");
            return false;
        }
        
        // 비밀번호 체크 및 탈퇴처리
        $.ajax({
            url: "/user/info/disabled",
            type: "POST",
            data: formData,
            success: function () {
                Swal.fire({
                    icon: 'warning',
                    title: "정말 탈퇴 하시겠습니까?",
                    text: "탈퇴 시 관리자에게 문의하셔야 합니다.",
                    showCancelButton: true,
                    confirmButtonText: '탈퇴',
                    cancelButtonText: '취소',
                }).then((result) => {
                    if (result.isConfirmed) {
                        Swal.fire({
                            icon: 'success',
                            title: "탈퇴 처리되었습니다.",
                            confirmButtonText: '확인',
                        }).then(() => {
                            location.href = "/logout";
                        });
                    }
                });
                return true;
            },
            error: function (e) {
                errorAlert($pwd, "비밀번호가 일치하지 않습니다.");
                pwdErrMsg.text("다시 입력해주세요.").css('color', 'red');
                return false;
            }
        });
    })

    $("input[name='checkPassword']").keyup(() => {
        const blank = $pwd.val().trim();
        const pwdValue = $pwd.val();
        if (!blank) {
            pwdCheck = false;
        } else {
            pwdErrMsg.text("");
        }
        if (!pwdValue) {
            pwdCheck = false;
        } else {
            pwdErrMsg.text("");
        }
    })

    $("input[name='email']").keyup(() => {
        const blank = $email.val().trim();
        const emailValue = $email.val();
        if (!blank) {
            emailCheck = false;
        } else {
            emailErrMsg.text("");
        }
        if (!emailValue) {
            emailCheck = false;
        } else {
            authErrMsg.text("");
        }
        if (!emailReg.test(emailValue)) {
            btnSendEmail.prop('disabled', true);
            emailCheck = false;
        } else {
            btnSendEmail.prop('disabled', false);
            authErrMsg.text("");
        }
    })

    // 인증번호 발송
    async function sendNumber() {
        const checkEmail = $email.val();

        const formData = $("#action-form").serialize();
        const inputEmail = await $.ajax({
            url: "/user/info/checkEmail",
            type: "post",
            data: formData,
            success: function () {
                $("input[name='email']").prop('disabled', true);
                return true;
            },
            error: function (e) {
                errorAlert($email, e.responseText);
                $("input[name='email']").prop('disabled', false);
                emailErrMsg.text("다시 입력해주세요.");
                return false;
            }
        });
        if (inputEmail === checkEmail) {
            try {
                const response = await $.ajax({
                    type: "POST",
                    url: "/user/auth/mail",
                    data: {"email": inputEmail}
                });
                if (response === "success") {
                    successAlert($inputAuth, "인증번호 발송 완료. 확인 후 인증해주세요.");
                    sessionStorage.setItem("emailConfirmCode", response);
                    btnSendEmail.hide();
                    btnResendEmail.show();
                    startTimer();

                } else {
                    errorAlert($email, "인증번호 요청 실패: " + response);
                }
            } catch (e) {
                console.error(e);
                errorAlert($email, e.responseText);
                return false;
            }
        }
    }

    // 재전송
    async function resendNumber() {
        btnResendEmail.prop('disabled', true);
        const inputEmail = $email.val();
        clearInterval(timer);
        timeLeft = 180;

        try {
            const response = await $.ajax({
                type: "POST",
                url: "/user/auth/mail",
                data: {"email": inputEmail}
            });
            if (response === "success") {
                successAlert($inputAuth, "인증번호 발송 완료. 확인 후 인증해주세요.");
                sessionStorage.setItem("emailConfirmCode", response);

                btnSendEmail.hide();
                btnResendEmail.show();

                startTimer();
            } else {
                errorAlert($email, "인증번호 요청 실패: " + response);
            }
        } catch (error) {
            errorAlert($authNumber, error.responseText);
            $authNumber.val("");
        }
    }

    // 인증번호 입력 시 이벤트
    $("#userAuth").keyup(() => {
        const number = $inputAuth.val().trim();
        if (!$inputAuth) {
            btnConfirm.prop("disabled", false);
            return false;
        } else {
            btnConfirm.prop("disabled", true);
            authErrMsg.text("");
        }
        if (number.length !== 8) {
            btnConfirm.prop("disabled", true);
            return false;
        } else {
            btnConfirm.prop("disabled", false);
            authErrMsg.text("");
        }

    })

    // 인증번호 체크
    async function checkNumber() {
        const inputAuthNumber = $inputAuth.val();
        try {
            // 서버에서 인증번호를 확인하는 요청
            await $.ajax({
                type: "POST",
                url: "/user/auth/check",
                data: {"code": inputAuthNumber},
                dataType: "text",
                success: function () {
                    authCheck = true;
                    successAlert($inputAuth, "인증되었습니다.");
                    stopTimer();
                    successCheck();
                },
                error: function (error) {
                    console.error(error);
                    Swal.fire({
                        icon: 'error',
                        text: error.responseText
                    })
                }
            });
        } catch (error) {
            errorAlert($inputAuth, error.responseText);
            $("#auth-number").prop('disabled', false);
            btnResendEmail.prop('disabled', false);
            authErrMsg.text("다시 입력해주세요.");
            $inputAuth.val("");
            return false;

        }
    }

    function successCheck() {
        $("#auth-number").hide();
        $("#btnDisable").prop("disabled", false);
        btnConfirm.prop("disabled", true);
    }


    function errorAlert($el, text) {
        Swal.fire({
            icon: 'error',
            text: text,
            didClose: () => {
                $el.focus();
            }
        });
    }

    function successAlert($el, text) {
        Swal.fire({
            icon: 'success',
            text: text,
            didClose: () => {
                $el.focus();
            }
        });
    }

})