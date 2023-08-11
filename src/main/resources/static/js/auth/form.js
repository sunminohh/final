$(() => {

    const $id = $(".join-form input[name='id']");
    const $name = $(".join-form input[name='name']");
    const $password = $(".join-form input[name='password']");
    const $repassword = $(".join-form input[name='repassword']");
    const $birth = $(".join-form input[name='birth']");
    const $email = $(".join-form input[name='email']");
    const $auth = $(".join-form input[name='authnumber']");

    // 각 input 값 검증 결과 변수
    let idCheck = false;
    let passwordCheck = false;
    let nameCheck = false;
    let birthCheck = false;
    let emailCheck = false;
    let authCheck = false;

    // input 새로운 값 입력 시 기존 검증 결과 초기화
    $id.keyup(() => idCheck = false);
    $email.keyup(() => emailCheck = false);
    $auth.keyup(() => authCheck = false);

    // 아이디, 이메일 중복 확인 버튼 클릭 이벤트
    $("#btnUserIdConfirm").click(() => checkId());
    $("#btnCheckMail").click(() => checkEmail());

    // 이메일 인증번호 요청 버튼 클릭 이벤트
    $("#btnAuthMail").click(() => checkAuth());

    // 아이디 입력 이벤트
    $("input[name=id]").keyup(() => {
        const $id = $(".join-form input[name='id']");
        const idReg = /^[a-zA-Z0-9]{3,10}$/;
        if (!idReg.test($id.val())) {
            $("#id-error-text").text("아이디는 영문, 숫자의 조합으로 3자~10자여야 합니다.").css('color', 'red');
            return false;
        } else {
            $("#id-error-text").text("올바른 아이디 형식입니다.").css('color', 'lightgreen');
            return true;
        }
    });

    // 이름 입력 이벤트
    $("input[name=name]").keyup(() => {
        const namevalue = $name.val().trim();
        const korReg = /^[가-힣]+$/;

        if (namevalue.length < 2 || namevalue.includes(" ") || !korReg.test(namevalue)) {
            $("#name-error-text").text("이름은 두글자 이상 공백을 포함할 수 없고, 한글만 가능합니다.").css('color', 'red');
            nameCheck = false;
        } else {
            $("#name-error-text").text("");
            nameCheck = true;
        }
    });

    // 비밀번호 입력 시 값 검증 이벤트
    $("input[name=password],input[name=repassword]").keyup(() => {
        const $password = $(".join-form input[name='password']");
        const $repassword = $(".join-form input[name='repassword']");
        const pwdReg = /(?=.*[0-9])(?=.*[a-zA-Z])(?=\S+$).{8,16}/;

        // 비밀번호랑 비밀번호 확인 둘다 따로따로 체크해서 해당하는 오류 메세지
        // Validation check (길이, 특수 문자 포함 여부)
        if (!$password.val()) {
            $("#password-error-text").text("비밀번호를 입력해주세요.").css('color', 'red');
            passwordCheck = false;
            return;
        } else if (!pwdReg.test($password.val())) {
            $("#password-error-text").text("비밀번호는 8자~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.").css('color', 'red');
            passwordCheck = false;
            return;
        } else {
            $("#password-error-text").text("올바른 비밀번호 형식입니다.").css('color', 'lightgreen');
        }

        if (!$repassword.val()) {
            $("#re-password-error-text").text("비밀번호를 확인해주세요.").css('color', 'red');
            passwordCheck = false;
        }

        if ($repassword.val() !== $password.val()) {
            $("#re-password-error-text").text("입력한 비밀번호가 일치하지 않습니다.").css('color', 'red');
            passwordCheck = false;
        } else {
            $("#re-password-error-text").text("비밀번호가 일치합니다.").css('color', 'lightgreen');
        }
    });

    // 이메일 입력 시 검증 이벤트
    $("input[name=email]").keyup(() => {
        const $email = $(".join-form input[name='email']");
        const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!$email.val()) {
            $("#email-error-text").text("이메일을 입력하세요.").css('color', 'red');
            emailCheck = false;
        }
        if (!emailReg.test($email.val())) {
            $("#email-error-text").text("이메일 형식에 올바르지 않습니다.").css('color', 'red');
            emailCheck = false;
        } else {
            $("#email-error-text").text("올바른 이메일 형식입니다.").css('color', 'lightgreen');
            emailCheck = true;
        }

    });

    // 가입하기 클릭 이벤트
    $("#action-form").submit(function (e) {
        e.preventDefault();

        const $id = $(".join-form input[name='id']");
        const $name = $(".join-form input[name='name']");
        const $password = $(".join-form input[name='password']");
        const $repassword = $(".join-form input[name='repassword']");
        const $email = $(".join-form input[name='email']");
        const $birth = $(".join-form input[name='birth']");
        const birth = $("#datepicker").val();

        // 아이디 입력 및 중복 체크
        if (!idCheck) {
            errorAlert($id, "아이디를 확인하세요.");
            return false;
        }

        // 이름 입력 확인
        if (!nameCheck) {
            errorAlert($name, "이름은 필수 입력 값입니다.");
            return false;
        }

        // 비밀번호 입력 및 확인
        if (!checkPwd()) {
            errorAlert($password, "비밀번호를 확인해주세요.");
            return false;
        }

        // 생년월일 체크
        if (!birth) {
            errorAlert($birth, "생년월일을 선택하세요.");
            return false;
        } else if (!birthCheck) {
            errorAlert($birth, "12세 이상만 가입 가능합니다.");
            return false;
        }

        // 메일 체크
        if (!emailCheck) {
            errorAlert($email, "이메일을 확인해주세요.");
            return false;
        }

        // 인증번호 체크
        if (!authCheck) {
            errorAlert($auth, "이메일인증을 확인 후 가입이 가능합니다.");
            return false;
        }

        // 검증 완료 후 form 전송
        $(this)[0].submit();
    });

    // id 중복 체크
    async function checkId() {
        const $id = $(".join-form input[name='id']");
        const idReg = /^[a-zA-Z0-9]{3,10}$/;
        if (!$id.val()) {
            errorAlert($id, '아이디를 입력해주세요!');
            idCheck = false;
            return;
        }
        if (!idReg.test($id.val())) {
            errorAlert($id, '아이디 형식에 맞지 않습니다.');
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

    // 비밀번호, 비밀번호 확인 체크
    function checkPwd() {
        const $password = $(".join-form input[name='password']");
        const $repassword = $(".join-form input[name='repassword']");
        const pwdReg = /(?=.*[0-9])(?=.*[a-zA-Z])(?=\S+$).{8,16}/;

        if (!pwdReg.test($password.val())) {
            errorAlert($password, '비밀번호 형식에 맞지 않습니다. 다시 입력해주세요.');
            return false;
        }

        if (!$password.val()) {
            errorAlert($password, '비밀번호를 입력해주세요.');
            return false;
        }

        if (!$repassword.val()) {
            errorAlert($repassword, '비밀번호를 확인해주세요');
            return false;
        }

        // password 체크 확인
        if ($repassword.val() !== $password.val()) {
            errorAlert($repassword, '입력하신 비밀번호가 일치하지 않습니다. 다시 확인해주세요.');
            return false;
        }
        return true;
    }

    // email 중복 체크
    /*async function checkEmail() {
        const $email = $(".join-form input[name='email']");
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!$email.val()) {
            errorAlert($email, '이메일을 입력해주세요.');
            emailCheck = false;
            return;
        }
        if (!emailRegex.test($email.val())) {
            errorAlert($email, "올바른 이메일 형식이 아닙니다. 다시 입력해주세요.");
            emailCheck = false;
            return;
        }
        const checkEmail = await $.get("/user/auth/checkEmail", {email: $email.val()});
        if (checkEmail) {
            errorAlert($email, "중복된 이메일 주소입니다.");
            emailCheck = false;
        } else {
            successAlert($email, "사용할 수 있는 이메일 주소입니다.");
            emailCheck = true;
        }
    }*/

    // todo 이메일 인증하기
    function checkAuth() {
        /*const checkEmail = await $.get("/user/auth/checkEmail", {email: $email.val()});
        if (checkEmail) {
            errorAlert($email, "중복된 이메일 주소입니다.");
            emailCheck = false;
        } else {
            successAlert($email, "사용할 수 있는 이메일 주소입니다.");
            emailCheck = true;
        }*/
        const $email = $(".join-form input[name='email']");
        const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!$email.val()) {
            errorAlert($email, "이메일을 입력하세요.");
            return false;
        }
        if (!emailReg.test($email.val())) {
            errorAlert($email, "이메일 형식에 올바르지 않습니다. 다시 입력해주세요.")
            return false;
        }
        $.ajax({
            type: "POST",
            url: "/user/auth/mailConfirm",
            data: {
                "email": $email.val()
            },
            success: function (data) {
                successAlert($email,"해당 이메일로 인증번호가 발송이 완료되었습니다. \n 확인부탁드립니다.")
                console.log("data : " + data);
                $("#mail-number").show();
                chkEmailConfirm(data, $auth);
                emailCheck = true;
            }
        });
    }

    // 이메일 인증번호 체크 함수
    function chkEmailConfirm(data, $auth){
        $("#confirmBtn").on("keyup", function(){
            if (data != $auth.val()) { //
                errorAlert($auth, "인증번호가 일치하지 않습니다. 다시 확인해주세요.");
                authCheck = false;
                //console.log("중복아이디");
            } else { // 아니면 중복아님
                successAlert($auth, "인증번호가 일치합니다.");
                authCheck = true;
            }
        })
    }

    // 만 12세 이상 가입 제한
    $("#datepicker").on("change", function () {
        const birthDate = new Date($(this).val());
        const currentDate = new Date();

        let age = currentDate.getFullYear() - birthDate.getFullYear();
        const monthDiff = currentDate.getMonth() - birthDate.getMonth();

        if (monthDiff < 0 || (monthDiff === 0 && currentDate.getDate() < birthDate.getDate())) {
            age--;
        }

        if (age < 12) {
            $("#birth-error-text").text(`${age}세입니다. 만 12세이상 부터 가입할 수 있습니다.`).css('color', 'red');
            birthCheck = false;
        } else {
            $("#birth-error-text").text(`${age}세입니다. 회원가입이 가능합니다.`).css('color', 'lightgreen');
            birthCheck = true;
        }
    });

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