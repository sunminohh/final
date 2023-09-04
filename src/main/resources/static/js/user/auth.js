$(() => {
    const $email = $("#userEmail");
    const $authNumber = $("#userAuth"); // 인증번호 입력
    const $timerDisplay = $("#schEmailtimer"); // 타이머 표시
    const $confirmButton = $("#btnConfirm"); // 인증하기

    const btnConfirm = $("#btnConfirm");
    const btnSendEmail = $("#btnSendAuthMail");
    const btnResendEmail = $("#btnResendAuthMail");

    let authCheck = false;
    let timer;
    let timeLeft = 180;

    $authNumber.keyup(() => authCheck = false);

    btnSendEmail.click(() => sendNumber());
    btnResendEmail.click(() => resendNumber());
    btnConfirm.click(() => checkNumber());

    $("#btnCancel").click(function () {
        history.back();
    })

    // 타이머 시작
    function startTimer() {
        timer = setInterval(() => {
            if (timeLeft > 0) {
                timeLeft--;
                btnResendEmail.prop('disabled', true);
                $("#mail-number").show();
                updateTimerDisplay();
            } else {
                clearInterval(timer);
                updateTimerDisplay(0);
                Swal.fire({
                    icon: 'error',
                    text: '인증 시간이 초과되었습니다. 다시 인증해주세요.',
                }).then(() => {
                    $("#mail-number").hide();
                    btnResendEmail.prop('disabled', false);
                    $authNumber.val("");
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
        $authNumber.prop("readonly", true);
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

        if (!authCheck) {
            errorAlert($authNumber, "이메일 인증 후 이용이 가능합니다.");
            return false;
        }

        $(this)[0].submit();
    })

    // 인증번호 발송
    async function sendNumber() {
        const inputEmail = $email.val();

        try {
            const response = await $.ajax({
                type: "POST",
                url: "/user/auth/mail",
                data: {"email": inputEmail}
            });
            if (response === "success") {
                sessionStorage.setItem("emailConfirmCode", response);
                successAlert($authNumber, "해당 이메일로 인증번호가 전송되었습니다. \n 확인부탁드립니다.");

                btnResendEmail.show();
                btnSendEmail.hide();
                startTimer(); // 타이머 시작

            } else {
                console.error(response);
                Swal.fire({
                    icon: 'error',
                    text: response
                })
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
        // Clear any existing timer and reset timeLeft
        clearInterval(timer);
        timeLeft = 180;

        await sendNumber();
    }

    // 인증번호 입력 시 이벤트
    $("#userAuth").keyup(() => {
        const number = $authNumber.val().trim();
        if (number.length === 6) {
            btnConfirm.prop("disabled", false);
            return true;
        } else {
            btnConfirm.prop("disabled", true);
            return false;
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
                        text: "인증 완료",
                    })
                    stopTimer();
                    successCheck();
                },
                error: function (e) {
                    console.error(e);
                    errorAlert($authNumber, e.responseText);
                }
            });
        } catch (error) {
            errorAlert($authNumber, error.responseText);
            btnConfirm.prop('disabled', true);
            $authNumber.val("");
        }
    }

    function successCheck() {
        $("#mail-number").hide();
        $("#btnSuccess").prop("disabled", false);
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