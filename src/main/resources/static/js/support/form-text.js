$(function() {
	
	// textarea 요소와 글자 수 표시 요소를 가져옴
	const textarea = document.getElementById('textarea');
	const textareaCnt = document.getElementById('textareaCnt');
	
	// 최대 글자 수를 정의
	const maxChars = 2000;
	
	// textarea 입력 이벤트를 감지하고 글자 수 업데이트 및 글자 수 체크를 수행
	textarea.addEventListener('input', function () {
	    const textLength = textarea.value.length;
	    
	    // 현재 글자 수를 업데이트
	    textareaCnt.textContent = textLength;
	    
	    // 최대 글자 수를 초과하면 입력을 막음
	    if (textLength > maxChars) {
	        textarea.value = textarea.value.substring(0, maxChars); // 초과한 부분 잘라냄
	        textareaCnt.textContent = maxChars; // 최대 글자 수로 설정
	    }
	});

	
})
