$(() => {

    // 패스워드 입력 이벤트
    $("input[name=password]")

    // 중복 체크 클릭 이벤트
    $("#btnMbLoginIdDupCnfm").click(() => exists());

    // 가입하기 클릭 이벤트
    $("#btnJoin").click(async () => {

        const $id = $(".join-form input[name='id']");
        const $password = $(".join-form input[name='password']");
        const $password2 = $(".join-form input[name='password2']");
        const $email = $(".join-form input[name='email']");

        // 아이디 입력 및 중복 체크
        if (await exists()) {
            return false;
        }

        if (!$password.val()) {
            errorAlert($password, '패스워드를 입력해주세요!');
            return false;
        }

        if (!$password2.val()) {
            errorAlert($password2, '패스워드 확인하세요!');
            return false;
        }

        // TODO: password 체크 확인

        if (!$email.val()) {
            errorAlert($email, '메일을 입력해주세요!');
            return false;
        }

        // TODO 메일 인증

        const id = $id.val();
        const password = $password.val();
        const password2 = $password2.val();
        const email = $email.val();

        console.log(id, password, password2, email);

    });

    async function exists() {
        const $id = $(".join-form input[name='id']");
        if ($id.val() === "" || $id.val() === undefined) {
            errorAlert($id, '아이디를 입력해주세요!');
            return false;
        }
        const exists = await $.get("/user/auth/exists", {id: $id.val()});
        if (exists) {
            errorAlert($id, "중복된 아이디입니다!");
        } else {
            Swal.fire({
                icon: 'success',
                text: "사용할 수 있는 아이디입니다.",
            });
        }
        return exists;
    }

    function errorAlert($el, text) {
        Swal.fire({
            icon: 'error',
            text: text,
        });
        $el.focus();
    }

});