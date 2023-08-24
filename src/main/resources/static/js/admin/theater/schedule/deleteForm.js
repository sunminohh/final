$(() => {
	const $table=$("#table-schedule");
	const $btnDel=$("#btnDel");
    
    $table.on("click","tbody tr",function(){
		console.log(this)
		$(this).toggleClass("on");
	})
    
    $btnDel.click(async function(){
		let idList =[]; 
		$table.find(".on").each(function(){
		idList.push($(this).attr("data-id"));
		})
		console.log(idList)
		if(idList.length <1 ||idList[0] == 0){
			callMessage(1);
		}
		if(await callMessage(10) == 1){
			$.ajax({
            url: "/schedule/admin/delete",
            type: "POST",
            data: JSON.stringify(idList), // JSON 형태로 변환하여 보냄
		    contentType: "application/json", // JSON 데이터임을 명시
		    success:function(response){
				callMessage(11);
				}
		    })
		}
	})
	
	function callMessage(messageNo){
		let message = [
			
		]
		
		if(messageNo<10){
			Swal.fire({
				icon:"error",
				text:"실패",
			})
		}
		if(messageNo>10){
			Swal.fire({
				icon:"success",
				text:"성공",
			})
		}
		if (messageNo == 10) {
			return new Promise((resolve) => {
				Swal.fire({
					title: "정말 삭제 하시겠습니까?",
					text: "삭제 하시면 복구는 불가능 합니다.",
					icon: "warning",
					showCancelButton: true,
					confirmButtonColor: "#3085d6",
					cancelButtonColor: "#d33",
					confirmButtonText: "삭제",
					cancelButtonText: "취소",
				}).then((result) => {
					if (result.isConfirmed) {
						resolve(1);
					} else if (result.dismiss === Swal.DismissReason.cancel) {
						resolve(0);
					}
				});
			});
		}
	}
});