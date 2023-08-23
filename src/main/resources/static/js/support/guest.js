$(function() {
	// 폼 알림창
	$("#btn-submit").on("click", function(event) {
		let guestname = $("input[name='guestName']").val();
		let guestemail = $("input[name='guestEmail']").val();
        
        if  (guestname === '') {
			event.preventDefault();
			 Swal.fire({
                icon: 'error',
                text: '이름을 입력 해주세요.'
            });
        } else if (guestemail === '') {
			event.preventDefault();
			 Swal.fire({
                icon: 'error',
                text: '이메일을 입력 해주세요.'
            });
        } else {
            $(".guestform").submit();
        }
    });
    
})