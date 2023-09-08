 $(function () {
	 
   $("#link-btn").on("click", function() {
       // 현재 페이지의 URL을 클립보드에 복사
       navigator.clipboard.writeText(window.location.href)
           .then(() => {
               // 복사 성공 시 팝오버를 표시합니다.
               $("#link-btn").popover('show');
               
               // 2초 후에 팝오버를 숨깁니다.
               setTimeout(function() {
                   $("#link-btn").popover('hide');
               }, 2000);
           })
           .catch((error) => {
               console.error("URL 복사 중 오류가 발생했습니다.", error);
               // 복사 실패 시에 수행할 작업을 여기에 추가할 수 있습니다.
           });
   });
	 
 })