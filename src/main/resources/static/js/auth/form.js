$(() => {

    // TODO 패스워드 입력 이벤트
    // $("input[name=password]")

    // 중복 체크 클릭 이벤트
    $("#btnUserIdConfirm").click(() => checkId());

    // 이메일 중복 체크 클릭 이벤트
    $("#btnCheckMail").click(() => checkEmail());

    // 가입하기 클릭 이벤트
    $("#btnJoin").click(async () => {

        const $id = $(".join-form input[name='id']");
        const $password = $(".join-form input[name='password']");
        const $repassword = $(".join-form input[name='repassword']");
        const $email = $(".join-form input[name='email']");

        // 아이디 입력 및 중복 체크
        if (!await checkId()) {
            return false;
        }
            console.log("아이디 패스");

        // // 패스워드 입력 및 확인
        if (checkPwd()) {
            console.log("패스워드 일치하지 않음");
            return false;
        }

        // TODO 메일 체크
        if (!await checkEmail()) {
            console.log("이메일 일치하지 않음");
            return false;
        }

        // TODO 메일 인증 (로그인 완료 후 구현)

        const id = $id.val();
        const password = $password.val();
        const repassword = $repassword.val();
        const email = $email.val();

        console.log(id, password, repassword, email);

        // TODO form 전송

    });

    // id 중복 체크
    async function checkId() {
        const $id = $(".join-form input[name='id']");
        console.log($id.val());
        if ($id.val() === "" || $id.val() === undefined) {
            errorAlert($id, '아이디를 입력해주세요!');
            return false;
        }
        // ES6 문법
        const checkId = await $.get("/user/auth/checkId", {id: $id.val()});
        if (checkId) {
            errorAlert($id, "중복된 아이디입니다!");
        } else {
            // successAlert($id, "사용할 수 있는 아이디입니다.");
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
            console.log("이메일 확인바랍니다.");
            return false;
        }
        const checkEmail = await $.get("/user/auth/checkEmail", {email: $email.val()});
        if (checkEmail) {
            errorAlert($email, "중복된 이메일 주소입니다!");
        } else {
            successAlert($email, "사용할 수 있는 이메일 주소입니다.");
        }
        return checkEmail;

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