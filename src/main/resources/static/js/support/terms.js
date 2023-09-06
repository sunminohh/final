$(function() {
	
	$('.pull-left li a').click(function() {

	    $(this).removeClass('on')
		
		$(this).addClass('on');
		
		$(this).closest('li').siblings('li').find('a').removeClass('on')
		$(this).closest('.pull-left').siblings('.pull-left').find('li a').removeClass('on')
    });
	
})