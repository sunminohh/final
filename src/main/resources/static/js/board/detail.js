/**
 * 좋아요 버튼 관련 코드
 */
$(function() {
	let likeButtonClicked = false;
	$("#like-button").click(function(event) {
		event.preventDefault();
		if (!likeButtonClicked) {
			$(this).removeClass("btn-outline-secondary").addClass("btn-secondary");
			likeButtonClicked = true;
		} else {
			$(this).removeClass("btn-secondary").addClass("btn-outline-secondary");
			likeButtonClicked = false;
		}
	})
	


	
})