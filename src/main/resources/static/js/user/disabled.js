$(() => {
    const $pwd = $(".disable-form input[name='checkPassword']");
    const $email = $(".disable-form input[name='email']");
    const $authNumber = $("#userAuth"); // 인증번호 입력
    const $timerDisplay = $("#schEmailtimer");
    const authErrMsg = $("#auth-error-text");
    const pwdErrMsg = $("#pwd-error-text");
    const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    let pwdCheck = false;
    let emailCheck = false;
    let authCheck = false;

    let timer;
    let timeLeft = 5;

    $pwd.keyup(() => pwdCheck = false);
    $email.keyup(() => emailCheck = false);
    $authNumber.keyup(() => authCheck = false);

    $("#btnSendAuthMail").click(() => sendNumber()); // 인증요청
    $("#btnResendAuthMail").click(() => resendNumber()); // 재전송
    $("#btnConfirm").click(() => checkNumber()); // 인증하기

    $("#btnCancel").click(function () {
        history.back();
    })

    function startTimer() {
        timer = setInterval(() => {
            if (timeLeft > 0) {
                timeLeft--;
                $("#btnResendAuthMail").prop('disabled', true);
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
                    $("#btnResendAuthMail").prop('disabled', false);
                    return false;
                });
            }
            return true;
        }, 1000); // 1초마다 타이머 업데이트
    }

    // 타이머 종료 후 동작
    function stopTimer() {
        clearInterval(timer);
        $timerDisplay.text("0:00"); // 타이머 종료 표시 업데이트
        $confirmButton.prop("disabled", true); // 인증하기 버튼 비활성화
        $authNumber.prop("readonly", true); // 인증번호 입력 필드 읽기 전
    }

    // 타이머 표시 업데이트
    function updateTimerDisplay() {
        const minutes = Math.floor(timeLeft / 60);
        const seconds = timeLeft % 60;
        const formattedTime = `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
        $timerDisplay.text(formattedTime);
    }

    // todo 탈퇴 버튼 클릭
    $("#action-form").submit(function (e) {
        e.preventDefault();

        const pwdValue = $pwd.val();
        const emailValue = $email.val();
        const authValue = $authNumber.val();
        const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        const check = checkInput();
        if (!check) {
            return false;
        }
        console.log("pass");

        // 비밀번호 체크 및 탈퇴처리
        $.ajax({
            url: "/user/info/disabled",
            type: "POST",
            data: form.serialize(),
            success: function () {
                Swal.fire({
                    icon: 'success',
                    text: "탈퇴 처리 되었습니다.",
                    confirmButtonText: '확인',
                }).then((result) => {
                    if (result.isConfirmed) {
                        location.href = "/logout";
                    }
                })
            },
            error: function (e) {
                errorAlert($pwd, e.reponseText);
                authErrMsg.text("다시 입력해주세요.").css('color', 'red');
            }
        })

        /*function checkInput() {
            if (!authCheck) {
                errorAlert($authNumber, "인증확인 후 이용할 수 있습니다.");
                authErrMsg.text("인증해주세요.");
                return false;
            }

            return true;
        }*/
        $(this)[0].submit();
    })

    $("input[name='checkPassword']").keyup(() => {
        const pwdValue = $pwd.val();
        if (!pwdValue) {
            pwdCheck = false;
        } else {
            pwdErrMsg.text("");
        }
    })

    $("input[name='email']").keyup(() => {
        const emailValue = $email.val();
        if (!emailValue) {
            $("#btnSendAuthMail").prop('disabled', true);
            emailCheck = false;
        } else {
            $("#btnSendAuthMail").prop('disabled', false);
            authErrMsg.text("");
        }
        if (!emailReg.test(emailValue)) {
            $("#btnSendAuthMail").prop('disabled', true);
            emailCheck = false;
        } else {
            $("#btnSendAuthMail").prop('disabled', false);
            authErrMsg.text("");
        }
    })

    // 인증번호 발송
    async function sendNumber() {
        console.log("사용자 이메일 -> ", $email.val());
        const formData = $("#action-form").serialize();
        $.ajax({
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
                return false;
            }
        });
        try {
            const response = await $.ajax({
                type: "POST",
                url: "/user/auth/mail",
                data: {"email": $email.val()}
            });
            if (response === "success") {
                sessionStorage.setItem("emailConfirmCode", response);
                $("#btnResendAuthMail").show();
                $("#btnSendAuthMail").hide();
                Swal.fire({
                    icon: 'success',
                    text: "해당 이메일로 인증번호가 전송되었습니다.",
                    confirmButtonText: '확인',
                }).then(() => {
                    startTimer(); // 타이머 시작
                });
            } else {
                errorAlert($email, "인증번호 요청 실패: " + response);
            }
        } catch (error) {
            // Ajax 요청 실패한 경우
            handleErrorMessage("서버와 통신 중 오류가 발생했습니다.", response);
        }
    }

    // 재전송
    async function resendNumber() {
        console.log("재전송 -> ", $email.val());
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
                sessionStorage.setItem("emailConfirmCode", response);
                $("#btnResendAuthMail").show();
                $("#btnSendAuthMail").hide();
                Swal.fire({
                    icon: 'success',
                    text: "해당 이메일로 인증번호가 전송되었습니다.",
                    confirmButtonText: '확인',
                }).then(() => {
                    startTimer(); // 타이머 시작
                    console.log("사용자 입력 인증코드 -> ", inputEmail);
                });
            } else {
                errorAlert($email, "인증번호 요청 실패: " + response);
            }
        } catch (error) {
            // Ajax 요청 실패한 경우
            handleErrorMessage("서버와 통신 중 오류가 발생했습니다.", response);
        }
    }

    // 인증번호 입력 시 이벤트
    $("#userAuth").keyup(() => {
        const number = $authNumber.val().trim();
        if (number.length === 8) {
            $("#btnConfirm").prop("disabled", false);
            return true;
        } else {
            $("#btnConfirm").prop("disabled", true);
            return false;
        }
    })

    // 인증번호 체크
    // todo 멘토님 검증
    async function checkNumber() {
        const inputAuthNumber = $("#userAuth").val();
        try {
            // 서버에서 인증번호를 확인하는 요청
            const checkResponse = await $.ajax({
                type: "POST",
                url: "/user/auth/check",
                data: {"code": inputAuthNumber},
                dataType: "text"
            });
            console.log("pass")
            // 서버 성공메시지에 따른 처리
            if (checkResponse === "SESSION_CODE_NULL") {
                errorAlert($authNumber, "세션에서 인증코드를 찾을 수 없습니다.");
                return false;
            } else if (checkResponse === "USER_CODE_NULL") {
                authCheck = false;
                errorAlert($authNumber, "인증번호를 입력하세요.");
            } else if (checkResponse === "인증실패") {
                authCheck = false;
                errorAlert($authNumber, "인증번호가 일치하지 않습니다. 다시 확인해주세요.");
            } else {
                authCheck = true;
                successAlert($authNumber, "인증되었습니다.");
                stopTimer();
                successCheck();
            }
        } catch (error) {
            errorAlert($authNumber, "이메일 인증 중 오류가 발생하였습니다. 잠시 후 이용해주세요.");
            console.log("사용자 입력 값 -> ", inputAuthNumber)
            console.log("인증결과 -> ", checkResponse);
            location.reload();
        }
    }

    function successCheck() {
        $("#auth-number").hide();
        $("#btnDisable").prop("disabled", false);
        // $("#userAuth").prop("readonly", true);
        $("#btnConfirm").prop("disabled", true);
        // $("#btnResendAuthMail").prop("disabled", true);
    }


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
            text: text
        });
    }

})