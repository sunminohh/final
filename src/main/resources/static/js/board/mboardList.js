	function changeSort(){
		let sort = document.querySelector("select[name=sort]").value;
		document.querySelector("input[name=sort]").value = sort;
		document.querySelector("input[name=page]").value = 1;
		
		document.querySelector("#form-board-search").submit();
	}
	
	function changeRows(){
		let rows = document.querySelector("select[name=rows]").value;
		document.querySelector("input[name=rows]").value = rows;
		document.querySelector("input[name=page]").value = 1;
		
		document.querySelector("#form-board-search").submit();
	}
	
	function changePage(event, page){
		event.preventDefault();
		document.querySelector("input[name=page]").value = page;

		document.querySelector("#form-board-search").submit();
	}
	
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
	
	
