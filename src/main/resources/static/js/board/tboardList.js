/*
	다중셀렉트 관련 코드
*/
	// 지역을 변경할 때 마다 실행되는 이벤트 핸들러 함수
	function changeLoc() {
		refreshManagerField();
		
	}

	// 현재 선택된 지역에 소속된 극장목록을 조회해서 극장 셀렉터박스에 표시한다.
	function refreshTheaterField() {
		let el = document.querySelector("select[name=theaterNo]");
		// select 박스 내부컨텐츠를 초기화하고 활성화 상태로 바꾼다.
		el.innerHTML = "";
		el.disabled = false;
		
		// 지역번호 조회
		let locNo = document.querySelector("select[name=locationNo]").value
		
		let xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200){
				let text = xhr.responseText;
				let theaters = JSON.parse(text);
				if (theaters.length == 0) {
					el.disabled = true;
				} else {
					let options = "";
					theaters.forEach(function(theater) {
						options += `
							<option th:value="${theater.no}">${theater.name}</option>
						`;
					});
					el.innerHTML = options;
				}
			}
		}
		xhr.open("get", "theater/theaterByLocation?locNo="+ locNo);
		xhr.send();
	}
	
	function changeTBoard() {
		
	}
	
	
		