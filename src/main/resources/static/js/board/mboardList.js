
    // 모든 리스트 공통
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
	
	function searchBoard(event) {
	    if (event.key !== null && event.key === "Enter") {
	        event.preventDefault(); // 기본 엔터 동작을 취소
	        let keyword = document.querySelector("input[name=keyword]").value;
	        if (keyword.trim() === "") {
	            alert("키워드를 입력하세요");
	            document.querySelector("input[name=page]").value = 1;
	        } else {
	            document.querySelector("input[name=page]").value = 1;
	            document.querySelector("#form-board-search").submit();
	        }
	    }
	}

	function searchBoardBtn(){	
		let keyword = document.querySelector("input[name=keyword]").value;
		if(keyword.trim() === ""){
			alert("키워드를 입력하세요")			
			document.querySelector("input[name=page]").value = 1;
			return
		}
		document.querySelector("input[name=page]").value = 1;
		document.querySelector("#form-board-search").submit();
	}
	

	
	function changeCom() {
    let complete = document.querySelector("select[name=complete]").value;
    
    // Set the value in the hidden input field
    document.querySelector("input[name=complete]").value = complete;
    
    document.querySelector("input[name=page]").value = 1;
    document.querySelector("#form-board-search").submit();
	}

	
	// 극장리스트
	function changeLoc() {
		let el = document.querySelector("select[name=theaterNo]");
		el.innerHTML = "";  // select박스의 내부 컨텐츠를 전부 지운다.
		el.disabled =false; // select박스를 활성화상태로 바꾼다.
		let locationNo = document.querySelector("select[name=locationNo]").value
		let theaterNo = document.querySelector("select[name=theaterNo]").value
			
		let xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
			    let text = xhr.responseText;
			    let theaters = JSON.parse(text);
			    if (theaters.length === 0) {
			        el.disabled = true;
			    } else {
			        let options = `<option value="" selected disabled>극장선택</option>`;
			        theaters.forEach(function(theater) {
			            options += `<option value="${theater.no}" id="option-theater">${theater.name}</option>`;
			        });
			        el.innerHTML = options;
			    }
			}

		}
		xhr.open("GET", "theaterByLocationNo?locationNo=" + locationNo );
		xhr.send();
	}
	
	function changeTboard() {
		let theaterNo = document.querySelector("select[name=theaterNo]").value;
		let locationNo = document.querySelector("select[name=locationNo]").value;
		document.querySelector("input[name=theaterNo]").value = theaterNo;
		document.querySelector("input[name=locationNo]").value = locationNo;
		document.querySelector("input[name=page]").value = 1;
		
		document.querySelector("#form-board-search").submit();
	}
	
	// 스토어리스트
	function changeCat() {
		let el = document.querySelector("select[name=productNo]");
		el.innerHTML = "";  // select박스의 내부 컨텐츠를 전부 지운다.
		el.disabled =false; // select박스를 활성화상태로 바꾼다.
		let catNo = document.querySelector("select[name=catNo]").value
			
		let xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200){
				let text = xhr.responseText;
				let products = JSON.parse(text);
				if (products.length === 0){
					el.disabled = true;
				}else {
					let options = `<option value="" selected disabled>상품선택</option>`;
					products.forEach(function(product) {
						options += `<option value="${product.no}" >${product.name}</option>`;
	
					});
					el.innerHTML = options;
				}
			}
		}
		xhr.open("GET", "productByCatNo?catNo=" + catNo );
		xhr.send();
	}
	
	function changeSboard() {
		let productNo = document.querySelector("select[name=productNo]").value;
		let catNo = document.querySelector("select[name=catNo]").value;
		document.querySelector("input[name=productNo]").value = productNo;
		document.querySelector("input[name=catNo]").value = catNo;
		document.querySelector("input[name=page]").value = 1;
		
		document.querySelector("#form-board-search").submit();
	}
	
	$(function() {
		
		$("#write-board-anonymous").click(function(event) {
			event.preventDefault();
			Swal.fire({
				icon: 'warning',
				title: "이용 제한",
				text: "로그인 후 이용 가능한 서비스입니다."
			})
		});
		
        let bgColor = $("#search-btn").css("background-color");
        let bColor = $("#search-btn").css("border-color");
		$("#search-btn").hover(
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
	
