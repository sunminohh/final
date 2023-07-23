// Login 관련 코드
$(() => {


    // 로그인 모달 탭
    $("#loginType>.btn-type").on("click", function (e) {
        $(this).parent().find(".btn-type").removeClass("active");
        $(this).addClass("active");

        if ($(this).hasClass("user")) {
            $(".user-login-form").show();
            $(".anonymous-login-form").hide();
        }else {
            $(".user-login-form").hide();
            $(".anonymous-login-form").show();
        }
    });

    // 로그인 모달 표출
   $(".btn-login").on("click", () => $(".login-modal").addClass("is-open"));

   // 로그인 엔터 Keypress
    $(".login-modal input").on("keypress", (e) => {
        if (e.keyCode === 13)
            $(".login-modal .input-button").trigger("click");
    });

    // 로그인 버튼 클릭
   $(".login-modal .input-button").on("click", function (e) {
       e.preventDefault();
       const $modal = $(this).closest(".login-modal");
       const username = $modal.find("input[name='username']").val();
       const password = $modal.find("input[name='password']").val();

       /*
       $.ajax({
           url: "/login",
           type: "POST",
           contentType:"application/json; charset=utf-8",
           data: JSON.stringify({username, password})
       }).catch(e => console.error("ERRR"));
       */

       axios.post("/login", {username, password})
           .then((res) => {
               location.reload();
           })
           .catch((e) => {
               Swal.fire({
                   icon: 'error',
                   text: '아이디 혹은 비밀번호가 일치하지 않습니다.',
                   footer: '<a href="#">비밀번호를 잊어버렸나요?</a>'
               })
           });
   });
});


// Login UI
// https://codepen.io/knyttneve/pen/dgoWyE
$(() => {
    const body = document.querySelector("body");
    const modal = document.querySelector(".modal");
    const modalButton = document.querySelector(".modal-button");
    const closeButton = document.querySelector(".close-button");
    const scrollDown = document.querySelector(".scroll-down");
    let isOpened = false;

    const openModal = () => {
        modal.classList.add("is-open");
        body.style.overflow = "hidden";
    };

    const closeModal = () => {
        modal.classList.remove("is-open");
        body.style.overflow = "initial";
    };

    window.addEventListener("scroll", () => {
        if (window.scrollY > window.innerHeight / 3 && !isOpened) {
            isOpened = true;
            scrollDown.style.display = "none";
            openModal();
        }
    });

    closeButton.addEventListener("click", closeModal);

    document.onkeydown = evt => {
        evt = evt || window.event;
        evt.keyCode === 27 ? closeModal() : false;
    };
});
