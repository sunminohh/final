$(function () {
	
	// 탭컬러 바꾸기
	$('li.tab-link').click(function() {

	      $('li.tab-link').removeClass('current');
	      $('button.btn').removeClass('current');
	
	      $(this).addClass('current');
	      $(this).find('button.btn').addClass('current');
    });
    

	
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
	
	function searchCBoard(){	
		let keyword = document.querySelector("input[name=keyword]").value;
		if(keyword.trim() === ""){
			alert("키워드를 입력하세요")			
			document.querySelector("input[name=page]").value = 1;
			return
		}
		
		document.querySelector("input[name=page]").value = 1;
		
		document.querySelector("#form-board-search").submit();
	}
	
	function changeCBoards(){
		const boards = document.querySelector("select[name=boards]").value;
		document.querySelector("input[name=boards]").value = boards;
		document.querySelector("input[name=page]").value = 1;
		
		document.querySelector("#form-board-search").submit();
	}
	
	function changePage(event, page){
		event.preventDefault();
		document.querySelector("input[name=page]").value = page;

		document.querySelector("#form-board-search").submit();
	}
	
	