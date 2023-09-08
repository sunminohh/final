$(function () {
	
	// 탭컬러 바꾸기
	$('li.tab-link').click(function() {

	      $('li.tab-link').removeClass('current');
	      $('button.btn').removeClass('current');
	
	      $(this).addClass('current');
	      $(this).find('button.btn').addClass('current');
    });
    
        
 	let bgColor = $("#searchBtn").css("background-color");
    let bColor = $("#searchBtn").css("border-color");
	$("#searchBtn").hover(
	    function() {
	        // 마우스를 버튼 위로 올렸을 때 배경색과 테두리색 변경
	        $(this).css("background-color", "#058eab38").css("border-color", "#058eab38"); 
	    },
	    function() {
	        // 마우스가 버튼을 벗어났을 때 원래의 배경색과 테두리색 복원
	        $(this).css("background-color", bgColor).css("border-color", bColor); 
	    }
	);
	
})
	function searchBoard(){	
		let keyword = document.querySelector("input[name=keyword]").value;
		if(keyword.trim() === ""){
			alert("키워드를 입력하세요")			
			document.querySelector("input[name=page]").value = 1;
			return
		}
		
		document.querySelector("input[name=page]").value = 1;
		
		document.querySelector("#form-board-search").submit();
	}
	
	function changeBoards(){
		const boards = document.querySelector("select[name=boards]").value;
		document.querySelector("input[name=boards]").value = boards;
		document.querySelector("input[name=page]").value = 1;
		
		document.querySelector("#form-board-search").submit();
	}
	
	function changeJoinBoards(){
		const com = document.querySelector("select[name=com]").value;
		document.querySelector("input[name=com]").value = com;
		document.querySelector("input[name=page]").value = 1;
		
		document.querySelector("#form-board-search").submit();
	}
	
	
	function changePage(event, page){
		event.preventDefault();
		document.querySelector("input[name=page]").value = page;

		document.querySelector("#form-board-search").submit();
	}
	
	