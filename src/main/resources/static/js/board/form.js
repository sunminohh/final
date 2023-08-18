$(document).ready(function () {
  $(".movie-search-input").on("input", function () {
    const inputText = $(this).val();
    const searchItems = $(".movie-search-item");

    if (inputText.length > 0) {
      $(".movie-search-list").show();
      searchItems.each(function () {
        const itemText = $(this).text().toLowerCase();
        if (itemText.includes(inputText)) {
          $(this).show();
        } else {
          $(this).hide();
        }
      });
    } else {
      $(".movie-search-list").hide();
      searchItems.hide();
    }
  });

  $(".movie-search-item").on("click", function () {
    const selectedItem = $(this).text();
    $(".movie-search-input").val(selectedItem);
    $(".movie-search-list").hide();
  });

  $(document).on("click", function (e) {
    if (!$(e.target).closest(".movie-search-select").length) {
      $(".movie-search-list").hide();
    }
  });
});

$(function() {
    $(".movie-search-button").on("click", function () {
        let inputText = $(".movie-search-input").val();
        
        if (inputText === "") { // 입력창 내용이 비었을 때
            Swal.fire({
                icon: 'error',
                text: '내용을 작성해주세요.',
            });
        } else {
            let selectedValue = -1; // 기본값을 -1로 설정
            let selectedTitle = "";
            let found = false; // 영화 제목을 찾았는지 여부를 나타내는 변수

            $(".movie-search-list .movie-search-item").each(function () {
                if ($(this).text() === inputText) {
                    selectedValue = parseInt($(this).attr("value"));
                    selectedTitle = $(this).text();
                    found = true; // 영화 제목을 찾았음을 표시
                    return false; // 반복문 종료
                }
            });

            if (!found) { // 리스트에 없는 영화 제목일 경우
                Swal.fire({
                    icon: 'error',
                    text: '영화제목을 입력해주세요.',
                });
            } else { // 리스트에 있는 영화 제목일 경우
                let content = `
                    <div id="selected-box" >
                        <input type="hidden" name="movieNo" value="${selectedValue}" />
                        <div id="selected-movie-title" value="${selectedValue}">${selectedTitle}</div>
                        <div id="btn-x" role="button" tabindex="0" class="notion-token-remove-button">
                            <svg viewBox="0 0 8 8" class="closeThick" style="width: 8px; height: 8px; display: block; fill: inherit; flex-shrink: 0; backface-visibility: hidden; opacity: 0.5;">
                                <polygon points="8 1.01818182 6.98181818 0 4 2.98181818 1.01818182 0 0 1.01818182 2.98181818 4 0 6.98181818 1.01818182 8 4 5.01818182 6.98181818 8 8 6.98181818 5.01818182 4"></polygon>
                            </svg>
                        </div>
                    </div>
                `;

                $("#selectd-movie-title-box").html(content);
                $(".movie-search-input").val('');
            }
        }
    });
    
    $("#selectd-movie-title-box").on("click", "#btn-x", function() {
		$("#selectd-movie-title-box").empty();
	});
    
	$("#btn-submit").on("click", function () {
	    let movieTitle = $("#selectd-movie-title-box").find('#selected-movie-title').text();
	    let title = $("input[name='name']").val();
	    let content = $("#summernote").val();
	
	    if (movieTitle === '') {
	        Swal.fire({
	            icon: 'error',
	            text: '영화 제목을 선택해주세요.',
	        });
	    } else if (title === '') {
	        Swal.fire({
	            icon: 'error',
	            text: '게시글의 제목을 입력해주세요.',
	        });
	    } else if (content === '') {
	        Swal.fire({
	            icon: 'error',
	            text: '게시글의 내용을 입력해주세요.',
	        });
	    } else {
		   Swal.fire({
           icon: 'warning',
           title: '게시글을 등록 하시겠습니까?',
           showCancelButton: true,
           confirmButtonText: '네',
           cancelButtonText: '아니오',
	       }).then((result) => {
	           if (result.isConfirmed) {
					 $(".board-insert-form").submit();
	           } else if (result.dismiss === Swal.DismissReason.cancel) {
	               
	           }
	       });
	    }
	});
	
	$("#tboard-btn-submit").on("click", function () {
	    let theaterNo = document.querySelector("select[name=theaterNo]").value;
	    let title = $("input[name='name']").val();
	    let content = $("#summernote").val();
	
	    if (theaterNo === '0') {
	        Swal.fire({
	            icon: 'error',
	            text: '극장을 선택해주세요.',
	        });
	    } else if (title === '') {
	        Swal.fire({
	            icon: 'error',
	            text: '게시글의 제목을 입력해주세요.',
	        });
	    } else if (content === '') {
	        Swal.fire({
	            icon: 'error',
	            text: '게시글의 내용을 입력해주세요.',
	        });
	    } else {
		   Swal.fire({
           icon: 'warning',
           title: '게시글을 등록 하시겠습니까?',
           showCancelButton: true,
           confirmButtonText: '네',
           cancelButtonText: '아니오',
	       }).then((result) => {
	           if (result.isConfirmed) {
					 $(".board-insert-form").submit();
	           } else if (result.dismiss === Swal.DismissReason.cancel) {
	               
	           }
	       });
	    }
	});
	
});

/*
	다중셀렉트 관련 코드
*/
	function changeLoc() {
		let el = document.querySelector("select[name=theaterNo]");
		el.innerHTML = "";  // select박스의 내부 컨텐츠를 전부 지운다.
		el.disabled =false; // select박스를 활성화상태로 바꾼다.
		let locationNo = document.querySelector("select[name=locationNo]").value
		let xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200){
				let text = xhr.responseText;
				let theaters = JSON.parse(text);
				if (theaters.length === 0){
					el.disabled = true;
				}else {
					let options = `<option id="theater-option" value="0" selected disabled>극장선택</option>`;
					theaters.forEach(function(theater) {
						options += `<option id="theater-option" value="${theater.no}" >${theater.name}</option>`;

					});
					el.innerHTML = options;
					
					
				}
			}
		}
		xhr.open("GET", "theaterByLocationNo?locationNo=" + locationNo );
		xhr.send();
	}