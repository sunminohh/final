$(function() {
	

      // 오늘 날짜를 구합니다.
      let today = new Date();

      // 일주일 간의 날짜와 요일을 배열에 저장합니다.
      let weekDatesAndDays = [];
      for (let i = 0; i < 7; i++) {
        let date = new Date(today.getTime() + (i * 24 * 60 * 60 * 1000));
        let dateNumber = date.getDate();
        let dayName = date.toLocaleDateString('kr-KR', { weekday: 'short' });

        weekDatesAndDays.push({
          Date : date,
          date: dateNumber,
          day: dayName
        });
      }

      // 결과를 출력합니다.
      $.each(weekDatesAndDays, function(index, item) {
        let dateSpanId = "#span-date-" + (index + 1);
        let daySpanId = "#span-day-" + (index + 1);
        let date = "#btn-date-" + (index + 1);
		
        $(dateSpanId).text(item.date);
        $(daySpanId).text(item.day);
        
        let year = item.Date.getFullYear();
		let month = ("0" + (item.Date.getMonth() + 1)).slice(-2);
		let day = ("0" + item.Date.getDate()).slice(-2);
		
		let formattedDate = year + "/" + month + "/" + day ;
        
        $(date).attr("th:value", formattedDate)
      });

	
	let selectedDate;
	$("#date-select button").click(function() {
		
		$(this).css('border-bottom-color', 'white')
		$(this).parent().siblings().find('button').css('border-bottom-color', '#E6E6FA')
		
		let today = new Date();
		
		selectedDate = $(this).attr("th:value");

		alert(selectedDate)
	})
	
})

