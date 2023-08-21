/**
 * 
 */
$(document).ready(function() {
	//여기 아래 부분
	$('#summernote').summernote({
		  height: 500,                 // 에디터 높이
		  minHeight: null,             // 최소 높이
		  maxHeight: null,             // 최대 높이
		  focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
		  lang: "ko-KR",					// 한글 설정
		  placeholder: '내용을 입력하세요.',	//placeholder 설정
		  
		  toolbar: [
	       // 글꼴 설정
	       ['fontname', ['fontname']],
	       // 글자 크기 설정
	       ['fontsize', ['fontsize']],
	       // 굵기, 기울임꼴, 밑줄,취소 선, 서식지우기
	       ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
	        // 글자색
	       ['color', ['forecolor','color']],
	       // 표만들기
	       ['table', ['table']],
	       // 글머리 기호, 번호매기기, 문단정렬
	       ['para', ['ul', 'ol', 'paragraph']],
	       // 줄간격
	       ['height', ['height']],
	       ['insert', ['picture']], // 이미지 업로드 버튼 추가
	        ],
	        // 추가한 글꼴
	        fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체'],
	        // 추가한 폰트사이즈
	        fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72']
	});
});

