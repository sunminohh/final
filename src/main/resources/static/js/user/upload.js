$(() => {
    // 이미지 등록
    $("#btnAddProfileImg").on('click', function () {
        $("#userImgFile").click();
    })

    const $profileImg = $("#profileImage");
    if ($profileImg.attr("src") !== "/images/user/profile/default.png") {
        $("#btnAddProfileImg").hide();
        $("#btnDefaultProfileImg").show();
    } else {
        $("#btnAddProfileImg").show();
        $("#btnDefaultProfileImg").hide();
    }

    $("#userImgFile").on('change', function () {
        const file = $("#userImgFile")[0].files[0];
        if (file.size > 10 * 1024 * 1024) { // 10MB를 초과하는 경우
            Swal.fire({
                icon: 'error',
                text: "파일 크기는 10MB를 초과할 수 없습니다."
            });
            return false;
        }

        const formData = new FormData();
        formData.append('file', file);

        for (let pair of formData.entries()) {
            console.log(pair[0] + ',' + pair[1]);
        }
        $.ajax({
            url: "/mypage/upload",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (newImgPath) {
                Swal.fire({
                    icon: 'success',
                    text: "이미지 등록이 완료되었습니다.",
                    confirmButtonText: '확인',
                }).then((result) => {
                    if (result.isConfirmed) {
                        console.log("filename -> ", newImgPath)
                        $("#profileImage").attr("src", "/images/user/profile/" + newImgPath);

                        location.reload();
                    }
                })
            },
            error: function (e) {
                errorAlert("이미지 등록 중 오류 발생", e);
                return false;
            }
        });
    });

    // 이미지 삭제
    $("#btnDefaultProfileImg").click(function () {
        let profileImageUrl = $('#profileImage').attr('src');
        console.log("등록된 이미지 -> ", profileImageUrl);

        Swal.fire({
            icon: 'warning',
            title: '이미지 삭제',
            text: '이미지를 삭제하시겠습니까?',
            showCancelButton: true,
            confirmButtonText: '확인',
            cancelButtonText: '취소',
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: '/mypage/deleteImg',
                    type: 'POST',
                    data: {"file": profileImageUrl},
                    success: function () {
                        let defaultImagePath = $("#profileImage").attr("data-default-src");
                        $("#profileImage").attr("src", defaultImagePath);
                        Swal.fire({
                            icon: 'success',
                            text: "이미지가 삭제되었습니다.",
                        }).then((result) => {
                            if (result.isConfirmed) {
                                location.reload();
                            }
                        });
                    },
                    error: function (error) {
                        errorAlert(null, "이미지 삭제 중 오류가 발생하였습니다.");
                        return false;
                    }
                })
            }
        })
    })

    function errorAlert(text) {
        Swal.fire({
            icon: 'error',
            text: text
        });
    }
})