$(() => {
    const $email = $("#userEmail");
    const $authNumber = $("#userAuth"); // 인증번호

    let authCheck = false;

    $authNumber.keyup(() => authCheck = false);

    $("#btnAuthMail").click(() => sendNumber());
    $("#btnConfirm").click(() => checkNumber());

    $("#action-form").submit(function (e) {
        e.preventDefault();

        if (!authCheck) {
            errorAlert($authNumber, "이메일 인증 후 이용이 가능합니다.");
            return false;
        }

        $(this)[0].submit();
    })

    // todo 인증번호 발송
    async function sendNumber() {
        console.log("사용자 이메일 -> ", $email.val());
        try {
            const response = await $.ajax({
                type: "POST",
                url: "/user/info/mail",
                data: {"email": $email.val()}
            });
            // 성공 메시지에 따른 조건문
            if (response === "success") {
                // 서버에서 보낸 인증코드 세션에 저장
                sessionStorage.setItem("emailConfirmCode", response);
                successAlert($email, "해당 이메일로 인증번호가 전송되었습니다. \n 확인부탁드립니다.");
                // todo timer 설정
                console.info("응답결과 -> ", response);
                $("#btnAuthMail").prop("disabled", true);
                $("#mail-number").show();
            } else {
                errorAlert($email, "인증번호 요청 실패: " + response);
            }
        } catch (error) {
            // Ajax 요청 실패한 경우
            console.error("Error sending email", error);
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

    // todo 인증번호 체크
    async function checkNumber() {
        const userAuthNumber = $("#userAuth").val();

        try {
            // 서버에서 인증번호를 확인하는 요청
            const checkResponse = await $.ajax({
                type: "POST",
                url: "/user/info/check",
                data: {"code": userAuthNumber}, // 사용자가 입력한 인증번호를 보냄
                dataType: "text"
            });
            // 서버 성공메시지에 따른
            if (checkResponse === "SESSION_CODE_NULL") {
                errorAlert($authNumber, "세션에서 인증코드를 찾을 수 없습니다.");
                return false;
            } else if (checkResponse === "USER_CODE_NULL") {
                errorAlert($authNumber, "인증번호를 입력하세요.");
                console.log("응답 메시지 -> ", checkResponse);
                authCheck = false;
            } else if (checkResponse === "인증실패") {
                console.log("응답 메시지 -> ", checkResponse);
                authCheck = false;
                errorAlert($authNumber, "인증번호가 일치하지 않습니다. 다시 확인해주세요.");
            } else {
                console.log("응답 메시지 -> ", checkResponse);
                authCheck = true;
                successAlert($authNumber, "인증되었습니다.");
                $("#btnSuccess").prop("disabled", false);
                $("#userAuth").prop("readonly", true);
                $("#btnConfirm").prop("disabled", true);
                // todo timer 멈추고 hide
            }
        } catch (error) {
            console.error("Error checkNumber", error);

            authCheck = false;
            errorAlert($authNumber, "이메일 인증 중 오류가 발생하였습니다. 잠시 후 이용해주세요.");
        }

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