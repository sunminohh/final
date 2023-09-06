$(() => {
    $("#btnAddProfileImg").on('click', function () {
        $("#userImgFile").click();
    })

    $("#userImgFile").on('change', function () {
        const file = $("#userImgFile")[0].files[0];
        if (file.size > 10 * 1024 * 1024) { // 10MB를 초과하는 경우
            Swal.fire({
                icon: 'error',
                text: "파일 크기는 10MB를 초과할 수 없습니다."
            });
            return false;
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            $(".profile-img img").attr("src", e.target.result);
        }
        reader.readAsDataURL(file);
        
        const formData = new FormData();
        formData.append("file", file);
        
        $.ajax({
            ulr: "mypage/upload",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                console.log("이미지 등록");
                
            },
            error: function (error) {
                console.error(error);
                Swal.fire({
                    icon: 'warning',
                    text: "이미지 등록 실패"
                })
            }
        })
    });
})