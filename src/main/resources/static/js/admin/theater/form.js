$(() => {
    const $floorBox = $("#floorBox")
    const $btnAddFloorInput = $("#btnAddFloorInput")
    
    $btnAddFloorInput.click(handlerBtnAddFloorInput);
    $floorBox.on("click","button", handlerBtnDelInput);
    function handlerBtnAddFloorInput(){
		let htmlContent=`<div class="d-flex py-1">
							<input type="text" class="form-control" style="width: 20%"
							placeholder="층수" aria-label="floor" name="floor"> 
							<input type="text"
							class="form-control" placeholder="안내사항"
							aria-label="floorinfo" name="floorinfo">
							
							<button type="button" class=" ms-1 btn btn-danger text-nowrap">삭제</button>
						</div>
						`
		$floorBox.append(htmlContent);
	}
	
	function handlerBtnDelInput(){
		$(this).closest("div").remove();
	}
});