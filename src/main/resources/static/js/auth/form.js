$(() => {

    const $id = $(".join-form input[name='id']");
    const $password = $(".join-form input[name='password']");
    const $repassword = $(".join-form input[name='repassword']");
    const $email = $(".join-form input[name='email']");

    // 각 input 값 검증 결과 변수
    let idCheck = false;
    let emailCheck = false;
    let passwordCheck = false;

    // input 새로운 값 입력 시 기존 검증 결과 초기화
    $id.keyup(() => idCheck = false);
    $email.keyup(() => emailCheck = false);

    // 아이디, 이메일 중복 확인 버튼 클릭 이벤트
    $("#btnUserIdConfirm").click(() => checkId());
    $("#btnCheckMail").click(() => checkEmail());

    // 패스워드 입력 시 값 검증 이벤트
    $("input[name=password],input[name=repassword]").keyup(() => {
        // TODO Validation check (길이, 특수 문자 포함 여부)

        // 비밀번호랑 비밀번호 확인 둘다 따로따로 체크해서 해당하는 오류 메세지 표출 할 것
        if (!$password.val() || !$repassword.val()) {
            passwordCheck = false;
            $("#password-error-text").text("패스워드를 입력해주세요.");
        }
        if ($password.val() === $repassword.val()) {
            passwordCheck = true;
            $("#password-error-text").text("사용 가능한 패스워드입니다.");
        }
    });



    // TODO 패스워드 입력 이벤트

    // 중복 체크 클릭 이벤트

    // 이메일 중복 체크 클릭 이벤트

    // 가입하기 클릭 이벤트
    $("#action-form").submit(function (e) {
        e.preventDefault();

        const $id = $(".join-form input[name='id']");
        const $password = $(".join-form input[name='password']");
        const $repassword = $(".join-form input[name='repassword']");
        const $email = $(".join-form input[name='email']");

        // 아이디 입력 및 중복 체크
        if (!idCheck) {
            errorAlert($id, "아이디를 확인해주세요.");
            return false;
        }

        // // 패스워드 입력 및 확인
        if (!passwordCheck) {
            errorAlert($password, "패스워드를 확인해주세요.");
            return false;
        }

        // TODO 메일 체크
        if (!emailCheck) {
            errorAlert($email, "이메일을 확인해주세요.");
            return false;
        }

        // 검증 완료 후 form 전송
        $(this)[0].submit();
    });

    // id 중복 체크
    async function checkId() {
        const $id = $(".join-form input[name='id']");
        if ($id.val() === "" || $id.val() === undefined) {
            errorAlert($id, '아이디를 입력해주세요!');
            idCheck = false;
            return;
        }
        // ES6 문법
        const checkId = await $.get("/user/auth/checkId", {id: $id.val()});
        if (checkId) {
            errorAlert($id, "중복된 아이디입니다!");
            idCheck = false;
        } else {
            successAlert($id, "사용할 수 있는 아이디입니다.");
            idCheck = true;
        }
        return checkId;
    }

    function checkPwd() {
        const $password = $(".join-form input[name='password']");
        const $repassword = $(".join-form input[name='repassword']");
        if (!$password.val()) {
            errorAlert($password, '비밀번호를 입력해주세요!');
            return false;
        }

        if (!$repassword.val()) {
            errorAlert($repassword, '비밀번호를 확인하세요!');
            return false;
        }

        // password 체크 확인
        if ($repassword.val() !== $password.val()) {
            errorAlert($repassword, '입력하신 비밀번호가 일치하지 않습니다. 다시 확인해주세요!');
            return false;
        }
        return true;
    }

    // email 중복 체크
    async function checkEmail() {
        const $email = $(".join-form input[name='email']");
        if (!$email.val()) {
            errorAlert($email, '이메일을 입력해주세요!');
            emailCheck = false;
        }
        const checkEmail = await $.get("/user/auth/checkEmail", {email: $email.val()});
        if (checkEmail) {
            errorAlert($email, "중복된 이메일 주소입니다!");
            emailCheck = false;
        } else {
            successAlert($email, "사용할 수 있는 이메일 주소입니다.");
            emailCheck = true;
        }
    }

    // 경고창 라이브러리 함수
    function errorAlert($el, text) {
        Swal.fire({
            icon: 'error',
            text: text,
        });
        $el.focus();
    }
    function successAlert($el, text) {
        Swal.fire({
            icon: 'success',
            text: text,
        });
        $el.focus();
    }

});