$(()=>{

    //초기 변수 설정
    dayjs.locale('ko-kr')
    const today= dayjs()
    const movieChoices=$("#choiceMovieList")
    let startDay=today.add(-1,'day');
    const selectedMovies =new Set
    const selectedTheaters = new Set
    let selectedDate=today.format('YYYY-MM-DD')
    apiByDate(selectedDate)
    function callStep(stage){
        $("#step0").hide().siblings().hide()
        const scheduleId=$("#playScheduleList").find('[selected=selected]').attr('schedule-id')

        if(stage===0){
            $("#step0").show()
            clearChoice()
            $("#playScheduleList").find('[selected=selected]').removeAttr('selected')
        }
        if(stage===1){
            fetch("/api/booking/fromStep0toStep1?schedulId="+scheduleId).then(res=>res.json()).then(json=>{
                if("fail" == json.result){
                    $("#step0").show()
                    $("#login-modal").addClass('is-open')
                    return
                }else if("success" == json.result){
                    const screenId=$("#schedule-"+scheduleId).attr('screen-id')
                    $("#step1").show()
                    giftTicketNos=new Set
                    createSeats(scheduleId,screenId)
                    generateSeatResultByScheduleId(scheduleId)
                    clearChoice()
                }
            })
        }else if(stage===2){

            $("#step2").show()
            generateStep2ResultByScheduleId(scheduleId)
            insertBookedSeats(scheduleId)
        }
    }

    function finalPay(){
        const usedGiftTickets = [...giftTicketNos].join()
        const giftAmount=calculFinalPrice()
        const schedule = $("#playScheduleList").find('[selected=selected]')
        const scheduleId=$("#playScheduleList").find('[selected=selected]').attr('schedule-id')
        const movieNo=schedule.attr('movie-no')
        const movie=$("#mBtn-"+movieNo)
        const moviePoster=movie.attr('img-path')
        const movieContentRating=movie.attr('contentrating')
        const movieContentRatingKr=movie.attr('contentratingkr')
        const movieTitle=schedule.attr('movie-title')
        const startTime=schedule.attr('start-time')
        const endTime=schedule.attr('end-time')
        const screenId=schedule.attr('screen-id')
        const screenName=schedule.attr('screen-name')
        const theaterNo=schedule.attr('theater-no')
        const theaterName=schedule.attr('theater-name')
        const bookedSeats= $(".my-seat").children('.choice').map((i,e)=>e.innerHTML).get().join()
        const dto={
            no: Date.now() + Math.floor((Math.random()*100)),
            bookingDate: selectedDate,
            movieNo: movieNo,
            title: movieTitle,
            poster:moviePoster,
            contentRating:movieContentRating,
            contentRatingKr:movieContentRatingKr,
            startTime: startTime,
            endTime: endTime,
            scheduleId: scheduleId,
            screenId: screenId,
            screenName: screenName,
            theaterNo: theaterNo,
            theaterName: theaterName,
            totalSeats:adultTickets+underageTickets,
            bookedSeatsNos: bookedSeats,
            adultSeats:adultTickets,
            underageSeats:underageTickets,
            giftAmount: giftAmount,
            payAmount: totalPrice-giftAmount,
            totalPrice: totalPrice,
            payMethod:payMethod,
            usedGiftTickets:usedGiftTickets
        }
        fetch("/api/booking/bookingPay",{
            method:'post',
            headers:{
                'Content-Type': 'application/json'
            },
            body:JSON.stringify(dto)
        }).then(res=>res.json()).then(data=> {
            if('success' == data.result){
                window.location.replace("/booking/success?orderId="+data.bookingNo)
            }else if('fail'== data.result){
                    window.location.replace("/booking?fail=login")

            }else if('pending' == data.result) {
                let path = "/booking/";
                let successUrl = window.location.origin + path + "success";
                let failUrl = window.location.origin + path + "failure";
                let orderId = data.bookingNo
                const orderName = movieTitle + " " + (adultTickets + underageTickets) + " 장"
                let jsons = {
                    "card": {
                        "amount": totalPrice-giftAmount,
                        "orderId": orderId,
                        "orderName": orderName,
                        "successUrl": successUrl,
                        "failUrl": failUrl,
                        "cardCompany": null,
                        "cardInstallmentPlan": null,
                        "maxCardInstallmentPlan": null,
                        "useCardPoint": false,
                        "customerName": data.userName,
                        "customerEmail": null,
                        "customerMobilePhone": null,
                        "useInternationalCardOnly": false,
                        "flowMode": "DEFAULT",
                        "discountCode": null,
                        "appScheme": null
                    }
                }
                pay('카드', jsons.card);
            }
        })

    }

    const selectedTheaterList = $("#selectedTheaterList")
    const selectedMovieList = $("#selectedMovieList")
    const scheduleDiv= $("#mCSB_3_container")
    let screenId
    let screenRow=14
    let screenCol=20+1
    let maxSeatChoices
    let curSeatChoices
    let prevMouseOverSeat
    let ticketPrice = 8000
    let adultTickets
    let underageTickets

    let giftTicketNos
    let payAmount
    let adultPrice
    let underagePrice
    let totalPrice
    let payMethod

    function apiByDate(date){
        fetch('/api/booking/'+date).then(res => res.json()).then(item=>{
            let aa= JSON.stringify(item)
            let json = JSON.parse(aa)
            let ml= $("#mCSB_1_container123").children().children()
            ml.each(function(){
                let mNo= $(this).children().attr('movie-no')
                if (json[mNo]>0){
                    $(this).children().removeClass('disabled')
                }else $(this).children().addClass('disabled')
            })

            $(".theater-list").children().each(function(){
                let tNo= $(this).attr('theater-no')
                if (json[tNo]>0){
                    $(this).removeClass('disabled')
                }else $(this).addClass('disabled')
            })
            $(".locations").each(function(){
                let sz=  $(this).find("li button[class='']").length
                let region = $(this).attr('id')
                $(this).find('span').text(region+"("+sz+")")
            })
        })
    }
    function dateSelect(date){
        clearSelectedElements()
        $("#date-"+selectedDate).removeClass('on')
        selectedDate=date
        $("#date-"+selectedDate).addClass('on')
        apiByDate(selectedDate);
    }
    function clearSelectedElements(){
        selectedMovies.forEach(mNo => deleteSelectBox(mNo))
        selectedTheaters.forEach(tName=>deleteSelectBox(tName))
        selectedMovies.clear()
        selectedTheaters.clear()
        scheduleDiv.children().empty()
        $("#playScheduleNonList").show()
        $("#playScheduleList").hide()
        $(".locations").children().removeClass('on')

    }
    function createDateHtml(date){
        const day=dayjs(date)
        const on =  date==selectedDate ? 'on' :''
        return `<button id="date-${date}" class="${day.day() === 6 ? 'sat' : day.day() === 0 ? 'holi' : ''} ${on}" type="button" date-data="${date}" month="6" tabindex="-1">
                                                <span class="ir"></span>
                                                <em style="pointer-events:none;">${day.date()} <span style="pointer-events:none;" class="ir">일</span>
                                                </em>
                                                <span class="day-kr" style="pointer-events:none;display:inline-block">${day.format('dd')}</span>
                                                <span class="day-en" style="pointer-events:none;display:none">Wed</span>
                                            </button>`
    }


    //날짜 부분
    //날짜 생성
    for (let i =0; i<16; i++){
        let day=startDay.add(i,'day')
        $("#formDeList").children().append(createDateHtml(day.format('YYYY-MM-DD')));
    }
    // 다음날짜버튼
    $(".btn-next").click(function(event){
        const fday= $("#formDeList").children().children().filter(':first-child')
        fday.remove()
        const day=startDay.add(16,'day')
        startDay=startDay.add(1,'day')

        $("#formDeList").children().append(createDateHtml(day.format('YYYY-MM-DD')));
    })
    // 이전날짜버튼
    $(".btn-pre").click(function(event){
        const lday= $("#formDeList").children().children().filter(':last-child')
        lday.remove()
        startDay=startDay.add(-1,'day')
        const day=startDay
        $("#formDeList").children().prepend(createDateHtml(day.format('YYYY-MM-DD')));
    })

    //각날짜 버튼클릭시이벤트
    $('#formDeList div').on('click','button',function(e){
        dateSelect($(this).attr('date-data'))
    })





    // 영화 부분
    //영화버튼 선택시 이벤트
    //영화 선택자
    //영화 리스트
    const movieList=$('#mCSB_1_container123 ul li')
    movieList.on('click',function(e) {
        let mBtn= $(this).children()
        mNo=mBtn.attr('movie-no')
        let len = selectedMovies.size
        if (selectedMovies.has(mNo)) {
            deleteSelectBox(mNo)
        }else if(len<3) {
            selectedMovies.add(mNo)
            createSelectBox(mNo)
        }else
        {
            insertAlert("영화는 최대 3개까지 선택 가능합니다.")
        }
    })

    //선택영화목록창에서 x버튼눌렀을시 선택영화제거
    $('.choice-list').on("click",'button[class=del]',function() {
        deleteSelectBox($(this).attr('box-id'))
    })

    // 선택영화목록에서 선택영화를 지우는 함수
    function deleteSelectBox(id){
        if(selectedMovies.has(id)){
            selectedMovies.delete(id)
            $('#mBtn-'+id).toggleClass('on')
        }else {
            selectedTheaters.delete(id)
            $('#tBtn-'+id).toggleClass('on')
        }

        let box=$("#selected-box-"+id)
        box.parent().append("<div class='bg'></div>")
        box.remove()
        checkIsEmptyChoice()
    }
    function createSelectBox(id){
        if(selectedMovies.has(id)){
            const len=selectedMovies.size-1
            const box = selectedMovieList.children().eq(len)
            const mBtn = $("#mBtn-"+id)
            const html = `<div class="wrap">
                    <div class="img">
                    <img src="${mBtn.attr('img-path')}"
                    movie-no="${id}" ></div>
                    <button type="button" box-id="${id}" class="del">삭제</button></div>`
            box.append(html)
            box.attr('id',"selected-box-"+id)
            mBtn.toggleClass('on')
        }else {
            const len=selectedTheaters.size-1
            const box = selectedTheaterList.children().eq(len)

            const tBtn = $("#tBtn-"+id)
            const html = `<div class="wrap" id="selected">
                        <p class="txt">${id}
                            <button type="button" class="del" box-id="${id}"
                                    brch-no="1572" area-cd="10" spclb-yn="N" theab-kind-cd="10">삭제
                            </button>
                        </p>
                    </div>`
            box.append(html)
            box.attr('id',"selected-box-"+id)
            tBtn.toggleClass('on')
        }
        checkIsEmptyChoice()
    }
    function checkIsEmptyChoice(){
        if(selectedMovies.size==0){
            selectedMovieList.hide()
            selectedMovieList.prev().show()
        }else {
            selectedMovieList.prev().hide()
            selectedMovieList.show()
        }
        if(selectedTheaters.size==0){
            selectedTheaterList.hide()
            selectedTheaterList.prev().show()
        }else {
            selectedTheaterList.prev().hide()
            selectedTheaterList.show()
        }
    }

    // 지역 부분
    //지역리스트 선택시 이벤트
    $("#location-list").on('click',"[data=locbtn]",function(){
        $(this).parent().siblings().children().removeClass('on')
        $(this).toggleClass('on')
        $(this).next().toggleClass('on')
    })

    // 극장 부분
    // 극장 선택시 이벤트
    $('.theater-list').on('click','button',function(){
        let len=selectedTheaters.size
        let tName = $(this).attr('theater-name')
        if(selectedTheaters.has(tName)){
            deleteSelectBox(tName)
        }else if(len<3){
            selectedTheaters.add(tName)
            createSelectBox(tName)
        }else{
            insertAlert("극장은 최대 3개까지 선택 가능합니다.")
            return
        }
        if(selectedTheaters.size>0){
            bookingScheudlesApi();
            $("#playScheduleNonList").hide()
            $("#playScheduleList").show()
        } else{
            $("#playScheduleNonList").show()
            $("#playScheduleList").hide()
            $("#mCSB_3_container").children().empty()
        }
    })


    function bookingScheudlesApi(){
        const tNames= [...selectedTheaters]
        const mNos= [...selectedMovies]
        const url= "/api/booking/selectedSchedule?tNames="+tNames+"&date="+selectedDate+"&mNos="+mNos
        const container = scheduleDiv.children()
        container.empty()
        fetch(url).then(res=>res.json()).then(data=> {
            $.each(data,(index,s) => {
                const scheduleHtml=`<li>
                    <button type="button" class="btn" id="schedule-${s.id}" schedule-id="${s.id}" start="${s.startTime}" play-de="${selectedDate}" end=""
                            turn="1" movie-no="${s.movieNo}" theater-no="${s.theaterNo}" screen-id="${s.screenId}" screen-name="${s.screenName}"
                            theater-name="${s.theaterName}" netfnl-adopt-at="N" rest-seat-cnt="105" start-time="${s.startTime}" end-time="${s.endTime}"
                            movie-title="${s.movieTitle}" theab-popup-at="Y" theab-popup-no="2015">
                        <div class="legend"><i class="iconset ico-sun" title="">조조</i></div>
                        <span class="time"><strong title="상영 시작">${s.startTime}</strong><em title="상영 종료">~${s.endTime}</em></span><span
                        class="title"><strong title="${s.movieTitle}">${s.movieTitle}</strong><em>2D</em></span>
                        <div class="info"><span class="theater" title="${s.theaterName}">${s.theaterName}<br>${s.screenName}</span><span
                            class="seat"><strong class="now" title="잔여 좌석">${s.remainingSeats}</strong><span>/</span><em class="all"
                                                                                                         title="전체 좌석">${s.screenSeats}</em></span>
                        </div>
                    </button>
                </li>`
                container.append(scheduleHtml)
            })
        })
    }













    scheduleDiv.on('click','button',function(){
        screenId=$(this).attr('screen-id')
        $(this).attr('selected','selected')
        callStep(1)
    })

    function generateSeatResultByScheduleId(id){
        const s=$("#schedule-"+id)
        const mNo=s.attr('movie-no')
        const movie = $("#mBtn-"+mNo)
        $("#step1-result").children()
            .children(':first').html(`
                                <span class="movie-grade small age-${movie.attr('contentRating')}">${movie.attr('contentRatingKr')}</span>
                                <p class="tit">${movie.attr('movie-nm')}</p>
                                <p class="cate">2D</p>`)
            .next().html(`<p class="theater">${s.attr('theater-name')}</p>
                                    <p class="special">${s.attr('screen-name')}</p>
                                    <p class="date">
                                        <span>${selectedDate}</span>
                                        <em>(${today.format('dd')})</em>
                                    </p>
                                    <div class="other-time">
                                        <button type="button" class="now">${s.attr('start-time')} ~ ${s.attr('end-time')} <i class="arr"></i>
                                        </button>
                                        <ul class="other">
                                            <li>
                                                <button type="button" choicnt="90" playschdlno="2307311372034" class="btn on ">08:55~10:54</button>
                                            </li>
                                        </ul>
                                    </div>
                                    <p class="poster">
                                        <img src="${movie.attr('img-path')}">
                                    </p>`)
    }
    function createSeats(scheduleId,screenId){
        const seatsDiv = $("#seatsDiv").empty()
        fetch("/api/booking/getDisabledSeats?screenId="+screenId).then(res=>res.json()).then(json=>{
            const disabledSeats=json.disabledSeats
            screenRow=json.row
            screenCol=json.col
            const allEmptySeats = new Set
            $.each(disabledSeats,(id,disabledSeatNo)=>{
                allEmptySeats.add(disabledSeatNo)
            })
            for (let i=0; i<screenRow; i++){
                let rowChar=String.fromCharCode(65+i)

                seatsDiv.append('<div class="row justify-content-center"></div>')
                let row = seatsDiv.children(":last-child")
                row.append(createFirstSeat(rowChar))
                for (let j=1; j<screenCol; j++){
                    let seatNo = rowChar+j
                    if(allEmptySeats.has(seatNo)){
                        row.append(createDisabledSeat(seatNo,i, j))
                    }else row.append(createActiveSeat(seatNo,i,j,"standard common"))
                }
            }
            checkBookedSeats(scheduleId)
            seatNumbering(screenRow,screenCol)
        }).then()

    }
    function checkBookedSeats(scheduleId){
        if(!scheduleId){
            scheduleId=$("#playScheduleList").find('[selected=selected]').attr('schedule-id')
        }
            fetch("/api/booking/getBookedSeats?scheduleId="+scheduleId).then(res=>res.json()).then(bookedSeatNos=>{
                $.each(bookedSeatNos, (id, seatNo)=>{
                    $("#"+seatNo).addClass('finish')
                    console.log(seatNo)
                })
            })
    }
    function initDefaultSeats(screenRow,screenCol){
        const seatsDiv = $("#seatsDiv")
        for (let i=0; i<screenRow; i++){
            let rowChar=String.fromCharCode(65+i)
            seatsDiv.append('<div class="row justify-content-center"></div>')
            let row = seatsDiv.children(":last-child")
            row.append(createFirstSeat(rowChar))
            for (let j=1; j<screenCol; j++) {
                let seatNo = rowChar + j
                row.append(createActiveSeat(seatNo, i, j, "standard common"))
            }
        }
        fetch("api/booking/deleteDisabledSeats?screenId="+screenId).then(res=>console.log(res))
    }
    function seatNumbering(screenRow,screenCol){
        const seatsDiv = $("#seatsDiv")
        const allEmptySeats = new Set
        const noIndexCols= new Set
        const emptySeats=$(".empty")
        emptySeats.each(function(i,seat){
            allEmptySeats.add($(this).attr('id'))
        })
        for(let i=1; i<screenCol+1; i++){
            if(emptySeats.filter("[c="+i+"]").length==screenRow){
                noIndexCols.add(i)
            }
        }
        for (let i=0; i<screenRow; i++){
            let index=1
            let rowChar=String.fromCharCode(65+i)
            let row = seatsDiv.children(":eq("+i+")")
            for (let j=1; j<screenCol; j++){
                if(noIndexCols.has(j)){
                    continue
                }
                let seatNo = rowChar+j
                if(allEmptySeats.has(seatNo)){
                    index++
                    continue
                }
                let seat=$("#"+seatNo)
                seat.attr('title',rowChar+index)
                let span= seat.children().filter(':first').text(index++)
            }
        }
    }

    function createFirstSeat(rowChar){
        return `<button type="button" class="btn-seat-row" title="${rowChar} 행" >${rowChar}</button>`
    }

    function createActiveSeat(seatNo,screenRow,screenCol, state){
        return `<button type="button" title="${seatNo} (스탠다드/일반)" class="jq-tooltip seat-condition ${state}" id=${seatNo} r=${screenRow} c=${screenCol}>
                                     <span class="num">${seatNo}</span>
                                    <span class="rank">일반</span>
                                                    </button>`
    }
    function createDisabledSeat(seatNo,screenRow,screenCol){
        return `<div class="empty" id="${seatNo}" r="${screenRow}" c="${screenCol}" style="width:20px; height:19px; padding:0px;"></div>`
    }

    $("#seatsDiv").on('click','button',function() {
        const seatsToPick = maxSeatChoices - curSeatChoices
        if($(this).hasClass('impossible') || $(this).hasClass('finish')){
            return
        }

        if($(this).hasClass('choice')){
            removeCurSeatChoice($(this).attr('id'))
            const pair = $(this).attr('pair')
            if(pair){
                removeCurSeatChoice(pair)
            }
            checkNextButton()
            singleSeatCheck()
            calculPrice()
            return
        }
        if (seatsToPick==0){
            if(curSeatChoices==0){
                insertAlert("관람하실 인원을 먼저 선택해주세요.")
            }else insertAlert("좌석 선택이 완료되었습니다.")
            return
        }

        const seats= $(this).parent().children('.on').addClass('choice')

        const seat1=$(seats[0]).attr('id')
        let seat2=undefined
        if (seats.length==2){
            seat2=$(seats[1]).attr('id')
        }
        addCurSeatChoice(seat1, seat2)
        singleSeatCheck()
        calculPrice()
        sequncing()

    })
    function addCurSeatChoice(seatNo1, seatNo2){
        const seat1=$("#"+seatNo1).addClass('choice')
        $(".my-seat").children(":eq("+curSeatChoices+++")").addClass('choice').removeClass('possible').text(seatNo1)
        if(seatNo2){
            $("#"+seatNo2).addClass('choice').attr('pair',seatNo1)
            seat1.attr('pair',seatNo2)
            $(".my-seat").children(":eq("+curSeatChoices+++")").addClass('choice').removeClass('possible').text(seatNo2)
        }
        checkNextButton()
    }
    function checkNextButton(){
        if(maxSeatChoices>0 && maxSeatChoices - curSeatChoices==0){
            $("#pageNext").removeClass('disabled').css("background-color","#329eb1")
        }else $("#pageNext").addClass('disabled').css("background-color","#434547")
    }
    function calculPrice(){
        let ticketsPicked = curSeatChoices
        let adultTicketsMax=$("#adult-tickets").text()
        let underageTicketsMax = $("#underage-tickets").text()

        adultTickets = ticketsPicked
        underageTickets = 0
        if(ticketsPicked>adultTicketsMax){
            adultTickets=adultTicketsMax
            underageTickets= ticketsPicked - adultTicketsMax
        }
        adultPrice=ticketPrice*adultTickets*1.5
        underagePrice=ticketPrice*underageTickets
        totalPrice= adultPrice+underagePrice
        let count=""
        if(adultTickets>0){
            count="성인 "+adultTickets
        }
        if(underageTickets>0){
            if(adultTickets>0){
                count+=" · "
            }
            count+="청소년 "+underageTickets
        }
        $(".seat-result").find(".count").text(count)
        const regexp = /\B(?=(\d{3})+(?!\d))/g
        $("#tickets-total-price").text(totalPrice.toString().replace(regexp, ','))
    }
    function removeCurSeatChoice(seatNo){
        $("#"+seatNo).removeClass('choice')
        $(".my-seat").children(":eq("+--curSeatChoices+")").removeClass('choice').addClass('possible').text('-')
        $("pageNext").addClass('disabled')
    }
    function incrementMaxSeatChoice(){
        $(".my-seat").children(":eq("+maxSeatChoices+++")").addClass('possible')
    }
    function decrementMaxSeatChoice(){
        $(".my-seat").children(":eq("+(--maxSeatChoices)+")").removeClass('possible')
    }
    $("#seatsDiv").on('mouseover','button',function() {
        const seatsToPick = maxSeatChoices - curSeatChoices
        if(seatsToPick==0 || $(this).hasClass('choice') || $(this).hasClass('finish')){
            return
        }else if(seatsToPick==1){
            $(this).toggleClass('on')
        }else {

            const l=$(this).attr('seq-len')
            if(l==1){
                $(this).toggleClass('on')
                return
            }
            const k=$(this).attr('seq')
            let left
            if(l>2 && k==l-1){
                if(l%2==1 && prevMouseOverSeat)
                    if(prevMouseOverSeat==$(this).next().attr('id')){
                        left=true
                    }else if(prevMouseOverSeat==$(this).prev().attr('id')){
                        left=false
                    }else left=false
            }else if(k<l-1){
                if(k%2==0){
                    left=true
                }else left=false
            }else left=true
            if(left){
                $(this).addClass('on').prev().addClass('on')
                return
            }else {
                $(this).addClass('on').next().addClass('on')
                return
            }
        }
    })
    $("#seatsDiv").on('mouseleave','button',function() {
        prevMouseOverSeat=$(this).attr('id')
        $(this).removeClass('on').siblings().removeClass('on')
    })


    $("#emptySeat").on('click',()=>{
        const seats=$("#seatsDiv .choice")
        seats.each(function(i,e){
            $(this).replaceWith(createDisabledSeat($(this).attr('id'),$(this).attr('r'),$(this).attr('c')))
        })
        clearSeatChoices()
    })
    $("#clearChoice").on('click',()=>{
        clearChoice()
    })

    function sequncing(rowNum) {
        if (rowNum) {
            sequncingByRowNum(rowNum)
            return
        }
        const seatsDiv = $("#seatsDiv")
        let r = seatsDiv.children().length

        for (let i = 0; i < r; i++) {
            sequncingByRowNum(i)
        }
    }
    function sequncingByRowNum(rowNum){
        const seatsDiv = $("#seatsDiv")
        const seats= seatsDiv.children(":eq("+rowNum+")").children('.standard.common').not('.finish').not('.choice')
        let prev
        const arr  = []
        let k=1
        seats.each(function(i,seat) {
            const c = parseInt($(this).attr('c'))
            const isLast= i === (seats.length-1)
            if (prev &&!isLast && prev.attr('c') == c - 1) {
            } else{
                isLast && arr.push($(this))
                $.each(arr, function (k) {
                    $(this).attr('seq', k + 1).attr('seq-len',arr.length )
                })
                arr.splice(0, arr.length)
            }
            arr.push($(this))
            prev = $(this)
        })
    }
    function blockSingleSeats(rowNum){
        if (rowNum) {
            blockSingleSeatsByRowNum(rowNum)
            return
        }
        const seatsDiv = $("#seatsDiv")
        let r = seatsDiv.children().length
        for (let i = 0; i < r; i++) {
            blockSingleSeatsByRowNum(i)
        }
    }
    function singleSeatCheck(){
        if(maxSeatChoices-curSeatChoices==1){
            sequncing()
            blockSingleSeats()
        }else{
            $("#seatsDiv").find(".impossible").removeClass('impossible view')
        }
    }
    function blockSingleSeatsByRowNum(rowNum){
        const seatsDiv = $("#seatsDiv")
        const seats = seatsDiv.children(":eq("+rowNum+")").children('.standard.common').not('finish').not('.choice')
        seats.each(function(i) {
            let l = parseInt($(this).attr('seq-len'))
            if(l>2){
                let flag
                let k =parseInt( $(this).attr('seq'))
                let x = l-k
                if(k>1 && k<l){
                    if(k === 2 || k === l-1){
                        flag=true
                    }else{
                        let modulo=(k%2 == 0)
                        if(l%2 == 0){
                            let c=$(this).attr('c')*2
                            if(screenCol/2 > (c+x-k)/2){
                                modulo=!modulo
                            }
                        }
                        if (modulo){
                            flag=true
                        }
                    }
                }
                if(flag){
                    $(this).addClass('impossible view')
                }
            }
        })
    }

    function clearChoice(){
        maxSeatChoices=0
        curSeatChoices=0
        $("#tickets-total-price").text(0)
        $(".seat-result").find(".count").text("")
        $("#seatsDiv").find("button").removeClass('on choice impossible view')
        $('.number').children().text(0)
        $(".my-seat").html(`                                            <div class="seat all" title="구매가능 좌석">-</div>
                                            <div class="seat all" title="구매가능 좌석">-</div>
                                            <div class="seat all" title="구매가능 좌석">-</div>
                                            <div class="seat all" title="구매가능 좌석">-</div>
                                            <div class="seat all" title="구매가능 좌석">-</div>
                                            <div class="seat all" title="구매가능 좌석">-</div>
                                            <div class="seat all" title="구매가능 좌석">-</div>
                                            <div class="seat all" title="구매가능 좌석">-</div>`)
        checkNextButton()
        calculPrice()
    }
    $(".up").on('click',function(){
        if(maxSeatChoices==8){
            return
        }
        incrementMaxSeatChoice()
        singleSeatCheck()
        checkNextButton()
        let count= $(this).siblings('div').children(':first')
        count.text(parseInt(count.text())+1)
        $(".seat-count-before").removeClass('on').addClass('off')
    })

    $(".down").on('click',function(){
        if(maxSeatChoices==0){
            return
        }
        if(maxSeatChoices==curSeatChoices){
            insertAlertWithFn(`<div class="wrap"><header class="layer-header"><h3 class="tit">알림</h3></header><div class="layer-con" style="height:200px"><p class="txt-common">선택하신 좌석을 모두 취소하고 다시 선택하시겠습니까?</p><div class="btn-group"><button type="button" class="button lyclose">취소</button><button id="btn-confirm" type="button" class="button purple confirm">확인</button></div></div><button type="button" class="btn-layer-close">레이어 닫기</button></div>`,clearChoice)
            alertOn()

            return
        }
        decrementMaxSeatChoice()
        singleSeatCheck()
        checkNextButton()
        let count= $(this).next().children(':first')
        count.text(parseInt(count.text())-1)
        console.log("cur : "+curSeatChoices+" max : "+maxSeatChoices)
    })


    $("#pageNext").on('click',function(){
        if($(this).hasClass('disabled')){
            return
        }
        const scheduleId=$("#playScheduleList").find('[selected=selected]').attr('schedule-id')
        fetch("/api/booking/getBookedSeats?scheduleId="+scheduleId).then(res=>res.json()).then(bookedSeatNos=>{
            let flag=true
            $.each(bookedSeatNos, (id, seatNo)=>{
               if($("#"+seatNo).hasClass('choice')){
                   flag=false
               }
            })
            if(flag){
                callStep(2)
            }else{
                insertAlert("선택하신 좌석은 이미 예약중인 좌석입니다.")
                clearChoice()
            }
        })

    })

    function alertOn(){
        $("#alert").show()
        $("#alertStyle").show()
    }
    function alertOff(){
        $("#alert").hide()
        $("#alertStyle").hide()
    }
    function insertAlertWithFn(html, fn1){
        $("#alert").html(html);
        $("#alert").on('click','button',alertOff)
        $("#alert").on("click", '.confirm', fn1);
        alertOn()
    }
    function insertAlert(alertMsg){
        $("#alert").html(`<div class="wrap"><header class="layer-header"><h3 class="tit">알림</h3></header><div class="layer-con" style="height:250px"><p class="txt-common">${alertMsg}</p><div class="btn-group" style="display: flex;justify-content: center;"><button type="button" class="button purple confirm">확인</button></div></div><button type="button" class="btn-layer-close">레이어 닫기</button></div>`);
        $("#alert").on('click','button',alertOff)
        alertOn()
    }

    function insertBookedSeats(scheduleId){
        const seatNos=[]
        $("#seatsDiv").find('.choice').each(function(){
            seatNos.push($(this).attr('id'))
        })
      fetch("/api/booking/insertBookedSeats?scheduleId="+scheduleId+"&seatNos="+seatNos).then(res=>console.log(res))
    }
    function deleteBookedSeats(){
        const scheduleId=$("#playScheduleList").find('[selected=selected]').attr('schedule-id')
        if(scheduleId) {


            const seatNos = []
            $("#seatsDiv").find('.choice').each(function () {
                seatNos.push($(this).attr('id'))
            })
            fetch("/api/booking/deleteBookedSeats?scheduleId=" + scheduleId + "&seatNos=" + seatNos).then(res => console.log(res))
        }
    }
    function generateStep2ResultByScheduleId(id){
        const s=$("#schedule-"+id)
        const mNo=s.attr('movie-no')
        const regexp = /\B(?=(\d{3})+(?!\d))/g
        const movie = $("#mBtn-"+mNo)
        let html=`<div class="box">
                                        <div class="data">
                        <span class="tit">성인 <em>${adultTickets}</em>
                        </span>
                                            <span class="price">${(adultTickets*ticketPrice*1.5).toString().replace(regexp, ',')}</span>
                                        </div>`
        if(underageTickets>0){
            html+=`<div class="data">
                        <span class="tit">청소년 <em>${underageTickets}</em>
                        </span>
                                            <span class="price">${(underageTickets*ticketPrice).toString().replace(regexp, ',')}</span>
                                        </div>`
        }
        html+=`<div class="all">
                        <span class="tit">금액
                        </span>
                                            <span class="price">
                          <em>${(underageTickets*ticketPrice+adultTickets*ticketPrice*1.5).toString().replace(regexp, ',')}</em>
                          <span>원
                          </span>
                        </span>
                                        </div>
                                    </div>
                                    <div class="box discout-box">
                                        <div class="all">
                        <span class="tit">관람권 적용
                        </span>
                                            <span class="price">
                          <em id="gift-amount">0</em> 원
                        </span>
                                        </div>
                                    </div>
                                </div>`
        $("#step2-result").children().children(':first').html(`<span class="movie-grade small age-${movie.attr('contentRating')}">${movie.attr('contentRatingKr')}</span>
                                    <p class="tit">${movie.attr('movie-nm')}</p>
                                    <p class="cate" >2D</p>
                                    <p class="theater">${s.attr('theater-name')}/${s.attr('screen-name')}</p>
                                    <p class="date">
                                        <span>${selectedDate}</span>
                                        <em>(${today.format('dd')})</em>
                                        <span class="time" id="playTime">
                        <i class="iconset ico-clock-white"></i>${s.attr('start-time')} ~ ${s.attr('end-time')}</span>
                                    </p>`)
            .next().html(html).next().html(`<div class="add-thing">
                                        <p class="tit">추가차액
                                        </p>
                                        <div class="money">0</div>
                                    </div>
                                    <div class="pay">
                                        <p class="tit">최종결제금액
                                        </p>
                                        <div class="money">
                                            <em id="final-price">${(underageTickets*ticketPrice+adultTickets*ticketPrice*1.5).toString().replace(regexp, ',')}</em>
                                            <span>원
                        </span>`)

    }

    $("#pagePrevious").on('click',()=>{
        callStep(0)
    })
    $("#prevbtnfrom2to1").on('click',()=>{
        deleteBookedSeats()
        clearStep2()
        callStep(1)
    })
    $("#btn-gift-ticket").on('click',()=>{
        $("#background-layer").show()
        $("#gift-ticket-layer").show()
        $.each(giftTicketNos,function(i,e){
            $("#gift-ticket-table").append(`<tr><td>${e}</td><td>몰라</td><td>사용가능</td></tr>`)
        })
        $("#gift-ticket-input").focus()
    })
    $("#gift-ticket-submit").on('click',function(){
        if(giftTicketNos.size >= adultTickets+underageTickets){
            insertAlert('이미 관람권을 등록하였습니다.')
            return
        }
        const input=$("#gift-ticket-input").val()
        if(input.length>5){
            $("#gift-ticket-table").append(`<tr><td>${input}</td><td>몰라</td><td>사용가능</td></tr>`)
            $("#gift-ticket-input").val('')
            giftTicketNos.add(input)
        }else insertAlert("너무짧음")
    })
    $("#gift-ticket-layer").on('keyup','input',function(e){
        if(e.keyCode===27){
            $("#background-layer").hide()
            $("#gift-ticket-layer").hide()
            $("#gift-ticket-input").val('')
            $("#gift-ticket-table").html(`<tr >
                                        <th scope="col">관람권</th>
                                        <th scope="col">유효기간</th>
                                        <th scope="col">사용</th>
                                    </tr>`)
        }
    })
    $("#btn-gift-confirm").on("click",function(){
        calculFinalPrice()
        $("#background-layer").hide()
        $("#gift-ticket-layer").hide()
    })
    function calculFinalPrice(){
        const regexp = /\B(?=(\d{3})+(?!\d))/g
        const size=giftTicketNos.size
        let subAmount=size*ticketPrice*1.5
        if(size>adultTickets){
            subAmount+=(size-adultTickets)*ticketPrice
        }
        $("#gift-amount").text(subAmount.toString().replace(regexp,','))
        $("#final-price").text((totalPrice-subAmount).toString().replace(regexp,','))
        return subAmount
    }
    $("#btn-gift-close, #btn_gift_close_x").on("click",function(){
        $("#background-layer").hide()
        $("#gift-ticket-layer").hide()
        $("#gift-ticket-input").val('')
        $("#gift-ticket-table").html(`<tr >
                                        <th scope="col">관람권</th>
                                        <th scope="col">유효기간</th>
                                        <th scope="col">사용</th>
                                    </tr>`)
    })

    $("#btn-gift-cancle").on("click",function(){
        $("#gift-ticket-input").val('')
        $("#gift-ticket-table").html(`<tr >
                                        <th scope="col">관람권</th>
                                        <th scope="col">유효기간</th>
                                        <th scope="col">사용</th>
                                    </tr>`)
        giftTicketNos.clear()
    })

    $("#clear-step-2").on('click',()=>{
        clearStep2()
    })
    function clearStep2(){
        giftTicketNos.clear()
        if(payMethod){
            $("#pay-info-"+payMethod).hide()
        }
        payMethod=undefined
        $("#gift-ticket-input").val('')
        $("#gift-ticket-table").html(`<tr >
                                        <th scope="col">관람권</th>
                                        <th scope="col">유효기간</th>
                                        <th scope="col">사용</th>
                                    </tr>`)
        calculFinalPrice()
    }

    $("#btn-final-pay").on('click',()=>{
        finalPay()
    })

    $("#tosspay").click(function(){
        payMethod='tosspay'
        $("#pay-info-tosspay").show()
        $("#pay-info-kakaopay").hide()
    })
    $("#kakaopay").click(function(){
        payMethod='kakaopay'
        $("#pay-info-tosspay").hide()
        $("#pay-info-kakaopay").show()
    })

    function pay(method, requestJson) {
        let tossPayments = TossPayments("test_ck_5OWRapdA8dYGaQX9LYB3o1zEqZKL");
        console.log(requestJson);
        tossPayments.requestPayment(method, requestJson)
            .catch(function (error) {

                if (error.code === "USER_CANCEL") {
                    Swal.fire({
                        icon: 'warning',
                        text: "사용자가 취소했습니다."
                    });
                } else {
                    alert(error.message);
                    Swal.fire({
                        icon: 'error',
                        text: error.message
                    });
                }

            });
    }

    $("#login-fail-alert").on("click",'button',()=>{
        $("#login-fail-alert-style").remove()
        $("#login-fail-alert").remove()
    })
})