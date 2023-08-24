$(() => {
    const $pwd = $(".disable-form input[name='checkPassword']");
    const $email = $(".disable-form input[name='email']");
    const $authNumber = $("#userAuth"); // 인증번호 입력
    const $timerDisplay  = $("#schEmailtimer");
    const $confirmButton = $("#btnConfirm"); // 탈퇴
    const authErrMsg = $("#auth-error-text");
    const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    let timer;
    let timeLeft = 180;

    let pwdCheck = false;
    let emailCheck = false;
    let authCheck = false;

    $pwd.keyup(() => pwdCheck = false);
    $email.keyup(() => emailCheck = false);
    $authNumber.keyup(() => authCheck = false);

    $("#btnSendAuthMail").click(() => sendNumber());
    $("#btnResendAuthMail").click(() => resendNumber());
    $("#btnConfirm").click(() => checkNumber());

    $("#btnCancel").click(function () {
        history.back();
    })

    function startTimer() {
        timer = setInterval(() => {
            if (timeLeft > 0) {
                timeLeft--;
                $("#btnAuthMail").prop("disabled", true);
                $("#mail-number").show();
                updateTimerDisplay();
            } else {
                clearInterval(timer);
                updateTimerDisplay(0);
                Swal.fire({
                    icon: 'error',
                    text: '인증 시간이 초과되었습니다. 다시 인증해주세요.',
                }).then(() => {
                    $("#btnResendAuthMail").prop('disabled', false);
                });
            }
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

    $("#action-form").submit(function (e) {
        e.preventDefault();

        const pwdValue = $pwd.val();
        const emailValue = $email.val();
        const authValue = $authNumber.val();
        const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;


        const check = checkInput();
        if (!check) {
            console.log(check);
            return false;
        }
        console.log("pass");

        $.ajax({
            url: "/user/info/disabled",
            type: "POST",
            data: form.serialize(),
            success: function (res) {
                successAlert($pwd, "탈퇴처리 되었습니다.");
                $pwd.val("");
                $email.val("");
                $authNumber.val("");

                location.href = "/logout";
            },
            error: function (e) {
                errorAlert($pwd, e.reponseText);
            }
        })

        function checkInput() {
            if (!pwdCheck) {
                errorAlert($pwd, "비밀번호를 입력하세요.");
                return false;
            }

            if (!emailCheck) {
                errorAlert($email, "이메일을 입력하세요.");
                return false;
            }

            if (!authCheck) {
                errorAlert($authNumber, "이메일 인증 후 이용이 가능합니다.");
                return false;
            }
            return true;
        }
    })

    $("input[name='checkPassword']").keyup(() => {
        const pwdValue = $pwd.val();
        if (!$pwd.val()) {
            pwdCheck = false;
        } else {
            pwdErrMsg.text("");
        }
    })

    $("input[name='email']").keyup(() => {
        const emailValue = $email.val();
        if (!emailValue) {
            emailCheck = false;
        }
        if (!emailReg.test(emailValue)) {
            emailCheck = false;
        } else {

            authErrMsg.text("");
        }
    })

    // todo 멘토님 질문
    function checkEmailInput() {
        $.ajax({
            url: "/user/info/checkEmail",
            type: "POST",
            data: form.serialize(),
            success: function (res) {

            },
            error: function (e) {
                errorAlert($email, e.responseText);
            }
        })
    }

    // 인증번호 발송
    async function sendNumber() {
        console.log("사용자 이메일 -> ", $email.val());
        if (!$email.val()) {
            errorAlert($email, "이메일을 입력하세요.");
        }

        try {
            const response = await $.ajax({
                type: "POST",
                url: "/user/auth/mail",
                data: {"email": $email.val()}
            });
            if (response === "success") {
                sessionStorage.setItem("emailConfirmCode", response);
                successAlert($email, "해당 이메일로 인증번호가 전송되었습니다. \n 확인부탁드립니다.");

                $("#btnResendAuthMail").show();
                $("#btnSendAuthMail").hide();
                startTimer(); // 타이머 시작

            } else {
                errorAlert($email, "인증번호 요청 실패: " + response);
            }
        } catch (error) {
            // Ajax 요청 실패한 경우
            handleErrorMessage("서버와 통신 중 오류가 발생했습니다.", response);
        }


    }

    async function resendNumber() {
        clearInterval(timer);
        timeLeft = 180;

        await sendNumber();
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
    async function checkNumber() {
        const userAuthNumber = $("#userAuth").val();

        try {
            // 서버에서 인증번호를 확인하는 요청
            const checkResponse = await $.ajax({
                type: "POST",
                url: "/user/auth/check",
                data: {"code": userAuthNumber}, // 사용자가 입력한 인증번호를 보냄
                dataType: "text"
            });
            // 서버 성공메시지에 따른
            if (checkResponse === "SESSION_CODE_NULL") {
                errorAlert($authNumber, "세션에서 인증코드를 찾을 수 없습니다.");
                return false;
            } else if (checkResponse === "USER_CODE_NULL") {
                errorAlert($authNumber, "인증번호를 입력하세요.");
                authCheck = false;
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
            authCheck = false;
            errorAlert($authNumber, "이메일 인증 중 오류가 발생하였습니다. 잠시 후 이용해주세요.");
        }

    }

    function successCheck() {
        $("#mail-number").hide();
        $("#btnSuccess").prop("disabled", false);
        $("#userAuth").prop("readonly", true);
        $("#btnConfirm").prop("disabled", true);
        $("#btnResendAuthMail").prop("disabled", true);
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
            text: text,
        });
        $($el).focus();
    }

})