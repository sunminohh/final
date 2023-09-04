
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
	
	