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
		if(inputText === "") { // 입력창 내용이 비었을 때
		        Swal.fire({
		            icon: 'error',
		            text: '내용을 작성해주세요.',
		            
		        });
		    } else { // 입력창 내용이 비어있지 않을 때
			
				let selectedValue = "";
				let selectedTitle = "";
				$(".movie-search-list .movie-search-item").each(function() {
		            if ($(this).text() === inputText) {
		                selectedValue = parseInt($(this).attr("value"));
		               	selectedTitle = $(this).text();
		            }
	        	});
				
				let content =`	
					    <div id="selected-box" >
					    	<input type="hidden" name="movieNo" value="${selectedValue}" />
					        <div id="selected-movie-title" value="${selectedValue}">${selectedTitle}</div>
					        <div id="btn-x" role="button" tabindex="0" class="notion-token-remove-button">
					            <svg viewBox="0 0 8 8" class="closeThick" style="width: 8px; height: 8px; display: block; fill: inherit; flex-shrink: 0; backface-visibility: hidden; opacity: 0.5;">
					                <polygon points="8 1.01818182 6.98181818 0 4 2.98181818 1.01818182 0 0 1.01818182 2.98181818 4 0 6.98181818 1.01818182 8 4 5.01818182 6.98181818 8 8 6.98181818 5.01818182 4"></polygon>
					            </svg>
					        </div>
					    </div>
						`
				$("#selectd-movie-title-box").html(content);
				$(".movie-search-input").val('');		
			}
	});	
	


})
