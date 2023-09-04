$(() => {
    const $id = $(".pwd-form input[name='id']");
    const $name = $(".pwd-form input[name='name']");
    const $email = $(".pwd-form input[name='email']");
    const $inputAuth = $("#userAuth");
    const timerDisplay = $("#schEmailtimer");
    const btnConfirm = $("#btnConfirm");
    const btnSendEmail = $("#btnSendAuthMail");
    const btnResendEmail = $("#btnResendAuthMail");

    const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const engReg = /[a-zA-Z]/g;
    const korReg = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g;

    const emailErrMsg = $("#email-error-text");
    const authErrMsg = $("#auth-error-text");

    let idCheck = false;
    let nameCheck = false;
    let emailCheck = false;
    let authCheck = false;

    let timer;
    let timeLeft = 180;

    $inputAuth.keyup(() => authCheck = false);

    btnSendEmail.click(() => sendNumber());
    btnResendEmail.click(() => resendNumber());
    btnConfirm.click(() => checkNumber());

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

    $("#action-form").submit(function (e) {
        e.preventDefault();

        const form = $(this).serialize();

        const idValue = $id.val();
        const nameValue = $name.val();
        const emailValue = $email.val();



    })

    $("input[name='id']").keyup(() => {
        const idValue = $id.val()
        if (!idValue) {
            idCheck = false;
        } else if (korReg.test(idValue)) {
            idCheck = false;
            $id.val(idValue.replace(korReg, ''));
        }
    })

    $("input[name='name']").keyup(() => {
        const nameValue = $name.val();

        if (!nameValue) {
            nameCheck = false;
        } else if (engReg.test(nameValue)) {
            nameCheck = false;
            $name.val(nameValue.replace(engReg, ''));
        }
    })

    $("input[name='email']").keyup(() => {
        const emailValue = $email.val();

        if (!emailValue) {
            emailCheck = false;
        } else {
            $("#email-error-text").text("").hide();
        }
        if (!emailReg.test(emailValue)) {
            emailCheck = false;
            $("#email-error-text").text("이메일 형식이 아닙니다.").show();
            btnSendEmail.prop('disabled', true);
        } else {
            $("#email-error-text").text("").hide();
            btnSendEmail.prop('disabled', false);
        }
        if (korReg.test(emailValue)) {
            emailCheck = false;
            $email.val(emailValue.replace(korReg, ''));
        }
    })

    $("#userAuth").keyup(() => {
        const number = $inputAuth.val().trim();
        if (number.length === 6) {
            btnConfirm.prop("disabled", false);
            return true;
        } else {
            btnConfirm.prop("disabled", true);
            return false;
        }
    })

    // 인증번호 발송
    async function sendNumber() {
        const inputEmail = $email.val();
        const formData = $("#action-form").serialize();

        try {
            const checkResponse = await $.ajax({
                type: "POST",
                url: "/user/check",
                data: formData
            });
            if (checkResponse === "ok") {
                const response = await $.ajax({
                    type: "POST",
                    url: "/user/auth/mail",
                    data: {"email": inputEmail}
                });
                if (response === "success") {
                    sessionStorage.setItem("emailConfirmCode", response);
                    successAlert($inputAuth, "해당 이메일로 인증번호가 전송되었습니다. \n 확인부탁드립니다.");

                    btnResendEmail.show();
                    btnSendEmail.hide();
                    startTimer(); // 타이머 시작
                } else {
                    console.error(error);
                    Swal.fire({
                        icon: 'error',
                        text: error.responseText
                    });
                }
            } else {
                console.error(checkResponse);
                Swal.fire({
                    icon: 'error',
                    text: checkResponse
                });
            }
        } catch (error) {
            // Ajax 요청 실패한 경우
            console.error("server", error);
            Swal.fire({
                icon: 'error',
                text: error.responseText
            })
        }
    }

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
        if (number.length !== 6) {
            btnConfirm.prop("disabled", true);
            return false;
        } else {
            btnConfirm.prop("disabled", false);
            authErrMsg.text("");
        }
    })

    // 인증번호 체크
    async function checkNumber() {
        const inputAuthNumber = $("#userAuth").val();
        try {
            // 서버에서 인증번호를 확인하는 요청
            await $.ajax({
                type: "POST",
                url: "/user/auth/check",
                data: {"code": inputAuthNumber},
                dataType: "text",
                success: function () {
                    authCheck = true;
                    Swal.fire({
                        icon: 'success',
                        text: "인증완료"
                    })
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
            authError();
            return false;
        }
    }

    function successCheck() {
        $("#auth-number").hide();
        $("#btnPwdFind").prop("disabled", false);
        $("#userAuth").prop("readonly", true);
        btnConfirm.prop("disabled", true);
        btnResendEmail.prop("disabled", true);
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

    function authError() {
        $("#auth-number").prop('disabled', false);
        btnResendEmail.prop('disabled', false);
        btnConfirm.prop('disabled', true);
        authErrMsg.text("다시 입력해주세요.");
        $inputAuth.val("");
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