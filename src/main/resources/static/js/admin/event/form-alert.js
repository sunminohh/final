$(function() {
	
	// 인서트 알람 띄우기
	$('#btn-submit').on("click", function(event) {
		let faqCat = $('#eventCat').val();
		let title = $('input[name=title]').val();
		let eventStartDate = $('input[name=startDate]').val();
		let eventEndDate = $('input[name=endDate]').val();
		let file1 = $('input[name=file1]').val();
		let file2 = $('input[name=file2]').val();
		
		if (faqCat === null) {
			event.preventDefault();
			Swal.fire({
				icon: 'warning',
				text: '카테고리를 선택해주세요.'
			})
		} else if (title === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '제목을 입력 해주세요.'
         	 });
		 } else if (eventStartDate === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '시작날짜를 선택 해주세요.'
         	 });
		 } else if (eventEndDate === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '종료날짜를 선택 해주세요.'
         	 });
		 } else if (file1 === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '이벤트 메인 이미지를 선택 해주세요.'
         	 });
		 } else if (file2 === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '이벤트 상세 이미지를 선택 해주세요.'
         	 });
		 } else {
			if (file1.length > 10485760) { 
	            Swal.fire({
	                icon: 'warning',
	                text: '첨부파일의 크기가 너무 큽니다. 최대 허용 크기: 10MB',
	            });
	        } else if (file2.length > 10485760) { 
		            Swal.fire({
		                icon: 'warning',
		                text: '첨부파일의 크기가 너무 큽니다. 최대 허용 크기: 10MB',
		            });
	        
	        } else {
				event.preventDefault(); 
	            Swal.fire({
	                icon: 'warning',
	                title: '이벤트를 등록 하시겠습니까?',
	                showCancelButton: true,
	                confirmButtonText: '네',
	                cancelButtonText: '아니오',
	            }).then((result) => {
	                if (result.isConfirmed) {
	                    $("#insertform").submit();
	                } else if (result.dismiss === Swal.DismissReason.cancel) {
	
	                }
	            });
			}
	    }
	})
	
	// 수정 알람 띄우기
	$('#modify-btn-submit').on("click", function(event) {
		let faqCat = $('#eventCat').val();
		let title = $('input[name=title]').val();
		/*
		let startDate = $('input[name=originalstartDate]').val()
		$("#eventStartDate").val(startDate);
		*/
		let eventStartDate = $('input[name=startDate]').val();
		let eventEndDate = $('input[name=endDate]').val();
		let file1 = $('input[name=file1]').val();
		let file2 = $('input[name=file2]').val();
		
		if (faqCat === null) {
			event.preventDefault();
			Swal.fire({
				icon: 'warning',
				text: '카테고리를 선택해주세요.'
			})
		} else if (title === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '제목을 입력 해주세요.'
         	 });
		 } else if (eventStartDate === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '시작날짜를 선택 해주세요.'
         	 });
		 } else if (eventEndDate === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '종료날짜를 선택 해주세요.'
         	 });
		 } else if (file1 === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '이벤트 메인 이미지를 선택 해주세요.'
         	 });
		 } else if (file2 === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '이벤트 상세 이미지를 선택 해주세요.'
         	 });
		 } else {
			if (file1.length > 10485760) { // 1048576 바이트 = 1MB 
	            Swal.fire({
	                icon: 'warning',
	                text: '첨부파일의 크기가 너무 큽니다. 최대 허용 크기: 10MB',
	            });
	        } else if (file2.length > 10485760) { // 1048576 바이트 = 1MB 
		            Swal.fire({
		                icon: 'warning',
		                text: '첨부파일의 크기가 너무 큽니다. 최대 허용 크기: 10MB',
		            });
	        
	        } else {
				event.preventDefault(); 
	            Swal.fire({
	                icon: 'warning',
	                title: '이벤트를 수정 하시겠습니까?',
	                showCancelButton: true,
	                confirmButtonText: '네',
	                cancelButtonText: '아니오',
	            }).then((result) => {
	                if (result.isConfirmed) {
	                    $("#modifyform").submit();
	                } else if (result.dismiss === Swal.DismissReason.cancel) {
	
	                }
	            });
			}
	    }
	})
	
	// 삭제 알람 띄우기
    $("#delete-btn").on("click", function(event) {
	    event.preventDefault();
		let no = $('[name=no]').val();
		
	    Swal.fire({
	        icon: 'warning',
	        title: '정말 삭제하시겠습니까?',
	        showCancelButton: true,
	        confirmButtonText: '네',
	        cancelButtonText: '아니오',
	    }).then((result) => {
	        if (result.isConfirmed) {
	              window.location.href = '/admin/event/delete?no=' + no;      
	              
	        } else if (result.dismiss === Swal.DismissReason.cancel) {
	            
	        }
	    });
	});
	
})