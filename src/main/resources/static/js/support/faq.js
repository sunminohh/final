
 // 탭메뉴	
$(document).ready(function() {
	  
    $('li.tab-link.current').click();

    $('li.tab-link').click(function() {

      $('li.tab-link').removeClass('current');
      $('button.btn').removeClass('current');

      $(this).addClass('current');
      $(this).find('button.btn').addClass('current');
      
    });
});

// 아코디언 열고닫기
$(document).ready(function() {
    // 첫 번째 아코디언 열기
    $('.faq-list .qut:first').addClass('on');
    $('.faq-list .awn:first').show();

    // 아코디언 헤더(.qut)를 클릭했을 때
    $('.faq-list .qut').on('click', function() {
        if (!$(this).hasClass('on')) {
            // 모든 아코디언 닫기
            $('.faq-list .qut').removeClass('on');
            $('.faq-list .awn').slideUp();

            // 현재 아코디언 열기
            $(this).addClass('on');
            $(this).next('.awn').slideDown();
        }
    });
});














