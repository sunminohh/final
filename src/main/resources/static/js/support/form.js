$(function() {
	//폼 사진첨부
	document.getElementById("uploadBtn").addEventListener("click", function() {
	    document.getElementById("fileInput").click(); // 파일 입력 요소를 클릭
	});
	
	document.getElementById("fileInput").addEventListener("change", function() {
	    let selectedFiles = this.files;
	    let imgList = document.getElementById("imgList");
	    let uploadBtn = document.getElementById("uploadBtn");
	
	    // 이미지 첨부 개수가 5개를 초과하는지 확인
	    if (selectedFiles.length + imgList.children.length > 5) {
	        alert("최대 5개의 이미지만 첨부 가능합니다.");
	        this.value = "";
	        return;
	    }
	
	    // 선택된 파일을 순회하면서 파일명과 삭제 버튼을 #imgList에 추가
	    for (let i = 0; i < selectedFiles.length; i++) {
	        let fileNameElement = document.createElement("p");
	        fileNameElement.textContent = "선택된 파일: " + selectedFiles[i].name;
	
	        let deleteButton = document.createElement("button");
	        deleteButton.textContent = "X";
	        deleteButton.className = "btn-del";
	
	        // 삭제 버튼을 클릭했을 때 해당 파일명과 삭제 버튼을 삭제
	        deleteButton.addEventListener("click", function() {
	            let parentDiv = this.parentElement;
	            parentDiv.remove();
	            // 파일 입력을 초기화하여 추가적인 파일 첨부막기
	            document.getElementById("fileInput").value = "";
	            // 파일 선택 버튼을 활성화
	            uploadBtn.disabled = false;
	        });
	
	        // 파일명과 삭제 버튼을 함께 추가
	        let fileContainer = document.createElement("div");
	        fileContainer.appendChild(fileNameElement);
	        fileContainer.appendChild(deleteButton);
	        imgList.appendChild(fileContainer);
	    }
	
	    // 이미지 첨부 개수가 5개 이상일 경우 파일 선택 버튼을 비활성화
	    if (imgList.children.length >= 5) {
	        uploadBtn.disabled = true;
	    }
	});
	
})