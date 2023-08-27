$(function() {
    
    $("#regBox").on('click','#btn-comment' ,function(e) {
         e.preventDefault();
         let inputcontent = $("#commentArea").val();
      
          if(inputcontent === "") { // 댓글 내용이 비어있을 때
              Swal.fire({
                  icon: 'error',
                  text: '댓글 내용을 작성해주세요.',
              });
          } else { // 댓글 내용이 비어있지 않을 때
              // AJAX 요청을 보내고 새로운 댓글 목록을 받아옴
             $.ajax({
                 url: '/admin/support/one/addComment',
                 method: "POST",
                 data: $("#form-comment").serialize(),
                 success: function(comments) {
                     // 성공 시 새로운 댓글 목록을 업데이트
                     const html = comments.map(({content}, i) =>
                         `<div class="cont review">
                            <p>${content}</p>
                         </div>`
                     ).join("\n");
                     $(".review").remove();
                     $(".board-view").append(html);
                     $("#commentArea").val("");
                 }
             });
          }   
      });
      
})
