$(function () {
    // 폼 사진첨부
    document.getElementById("uploadBtn1").addEventListener("click", function () {
        document.getElementById("fileInput1").click(); // 파일 입력 요소를 클릭
    });
    
     document.getElementById("uploadBtn2").addEventListener("click", function () {
        document.getElementById("fileInput2").click(); // 파일 입력 요소를 클릭
    });

    document.getElementById("fileInput1").addEventListener("change", function () {
		uploadfile("fileInput1", "imgList1", "uploadBtn1");
    });
    document.getElementById("fileInput2").addEventListener("change", function () {
		uploadfile("fileInput2", "imgList2", "uploadBtn2");
    });
    
    function uploadfile(input, container, btn) {
		let selectedFiles = document.getElementById(input).files;
        let imgList = document.getElementById(container);
        let uploadBtn = document.getElementById(btn);
        
        // 기존에 업로드된 파일을 모두 삭제
        imgList.innerHTML = "";

        // 선택된 파일을 순회하면서 파일명과 삭제 버튼을 #imgList에 추가
        let fileNameElement = document.createElement("p");
        fileNameElement.textContent = "선택된 파일: " + selectedFiles[0].name;

        let deleteButton = document.createElement("button");
        deleteButton.textContent = "X";
        deleteButton.className = "btn-del";

        // 삭제 버튼을 클릭했을 때 해당 파일명과 삭제 버튼을 삭제
        deleteButton.addEventListener("click", function () {
            let parentDiv = this.parentElement;
            parentDiv.remove();
            // 파일 입력을 초기화하여 추가적인 파일 첨부 막기
            document.getElementById(input).value = "";
            // 파일 선택 버튼을 활성화
            uploadBtn.disabled = false;
        });

        // 파일명과 삭제 버튼을 함께 추가
        let fileContainer = document.createElement("div");
        fileContainer.appendChild(fileNameElement);
        fileContainer.appendChild(deleteButton);
        imgList.appendChild(fileContainer);

	}
	
	
});
