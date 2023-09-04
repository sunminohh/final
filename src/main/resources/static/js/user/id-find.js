$(() => {

    const $name = $(".user-form input[name='name']");
    const $birth = $(".user-form input[name='birth']");
    const $email = $(".user-form input[name='email']");
    const numReg = /[^0-9]/g;
    const korReg = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g;
    const engReg = /[a-zA-Z]/g;
    const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    let nameCheck = false;
    let birthCheck = false;
    let emailCheck = false;

    $(".alert-popup").dialog({
        autoOpen: false,
        modal: true
    });

    $("#action-form").submit(function (e) {
        e.preventDefault();

        const name = $name.val();
        const email = $email.val();
        const birthValue = $birth.val();
        const birth = `${birthValue.substring(0, 4)}-${birthValue.substring(4, 6)}-${birthValue.substring(6, 8)}`;

        const check = checkInput();
        if (!check) {
            return false;
        }
        console.log("pass");

        $.ajax({
            url: "/user/user-find",
            type: "POST",
            data: {
                name: name,
                email: email,
                birth: birth,
            },
            success: function (user) {
                const infoText = `회원님의 아이디는 [${user.id}] 입니다. \n 가입일 : ${moment(user.createDate).format('YYYY-MM-DD')}`;
                $('#userInfo').text(infoText);
                $('.alert-popup').dialog('open');
            },
            error: function (error) {
                console.error(error);
                Swal.fire({
                    icon: 'error',
                    text: error.responseText
                })
                return false;
            }
        })

        function checkInput() {
            if (!name) {
                errorAlert($name, "이름을 입력해주세요.");
                return false;
            }

            if (!birth) {
                errorAlert($birth, "생년월일을 선택해주세요.");
                return false;
            }
            if (!email) {
                errorAlert($email, "이메일을 입력해주세요.");
                return false;
            }
            return true;
        }

    });

    $(".alert-popup .confirm").click(() => {
        location.href = "/";
    })

    $(".btn-layer-close").click(() => {
        $(".alert-popup").dialog("close");
    });

    // 입력 체크
    $("input[name='name']").keyup(() => {
        const nameValue = $name.val();

        if (!nameValue) {
            nameCheck = false;
        } else if (engReg.test(nameValue)) {
            nameCheck = false;
            $name.val(nameValue.replace(engReg, ''));
        }
    })

    $("input[name=birth]").keyup(() => {
        const birthValue = $birth.val();

        if (!birthValue) {
            birthCheck = false;
        } else if (numReg.test(birthValue)) {
            birthCheck = false;
            $birth.val(birthValue.replace(numReg, ''));
        } else if (!isValidBirtDate(birthValue)) {
            birthCheck = false;
            $("#birth-error-text").text("생년월일을 정확히 입력해주세요.").show();
        } else {
            $("#birth-error-text").text("").hide();
        }
    })

    $("input[name=email]").keyup(() => {
        const emailValue = $email.val();

        if (!emailValue) {
            emailCheck = false;
        } else if (korReg.test(emailValue)) {
            emailCheck = false;
            $email.val(emailValue.replace(korReg, ''));
        } else if (!emailReg.test(emailValue)) {
            emailCheck = false;
            $("#email-error-text").text("이메일 형식이 아닙니다.").show();
        } else {
            $("#email-error-text").text("").hide();
        }
    })

    // 생년월일 유효성
    function isValidBirtDate(d) {
        if (!/^\d{4}\d{2}\d{2}$/.test(d)) return false;

        const year = parseInt(d.substring(0, 4), 10);
        const month = parseInt(d.substring(4, 6), 10);
        const day = parseInt(d.substring(6, 8), 10);

        if (year < 1900 || year > new Date().getFullYear()) return false;

        if (month < 1 || month > 12) return false;

        const daysInMonth = [0, 31, (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0 ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
        return !(day < 1 || day > daysInMonth[month]);

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
})