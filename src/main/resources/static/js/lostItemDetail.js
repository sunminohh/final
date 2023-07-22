$(() => {
    const editor = new FroalaEditor('#inputComment', {language: 'ko'});

    $(".btn-comment-insert").on("click", function () {
        const content = editor.html.get();
        if (!content) {
            Swal.fire({
                icon: 'error',
                text: '내용을 입력해주세요.',
            });
            return;
        }
        $.ajax({
            url: "/center/lost/comment",
            type: "POST",
            data: JSON.stringify({ content, lostItemId: $(this).data("id") }),
            contentType: "application/json; charset=utf-8",
            success: async () => {
                await refreshComments();
                Swal.fire({
                    icon: 'success',
                    text: '댓글이 작성되었습니다.',
                });
                editor.html.set('');
            },
            error: (error) => {
                console.error(error);
                Swal.fire({
                    icon: 'error',
                    text: '오류가 발생했습니다. 관련 기관에 문의 해주세요.',
                });
            }
        })
    });

    async function refreshComments() {
        const lostItemId = $("#lostItemId").val();
        try {
            const {data: comments} = await $.get(`/center/lost/comment/${lostItemId}`);
            $("#cnt1").text(comments.length);

            const hasAdmin = (comment) => comment.writer.roleNames.includes('ROLE_ADMIN');
            const html =
                comments.map((comment) =>
                    `<li class="${hasAdmin(comment) ? 'type01 oneContentTag' : 'type03'}">
                    <div class="story-area">
                        <div class="user-prof">
                            <div class="prof-img">
                                <img src="${hasAdmin(comment) ?
                                'https://img.megabox.co.kr/static/pc/images/common/ico/ico-mega-profile.png' :
                                'https://img.megabox.co.kr/static/pc/images/mypage/bg-profile.png'}" alt="MEGABOX">
                            </div>
                            <p class="user-id">${comment.writer.username}</p>
                        </div>
                        <div class="story-box">
                            <div
                                class="${hasAdmin(comment) ? 'story-wrap review' : 'story-wrap'}">
                                ${hasAdmin(comment) ? '<div class="tit">답변</div>' : ''}
                                <div class="story-cont">
                                    <div class="${hasAdmin(comment) ? 'story-txt' : ''}">${comment.content}</div>
                                    <div class="story-util">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="story-date">
                            <div class="review on">
                                <span>${moment(comment.createDate).format('YYYY.MM.DD HH:mm').replace(" ", "&nbsp;")}</span>
                            </div>
                        </div>
                    </div>
                </li>`
                ).join("");

            $("#comments li").not(".input-write").remove();
            $("#comments").append(html);
        } catch(e) {
            console.error(e);
        }
    }

});