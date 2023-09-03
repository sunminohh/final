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

    const selectedTheaterList = $("#selectedTheaterList")
    const selectedMovieList = $("#selectedMovieList")
    const scheduleDiv= $("#mCSB_3_container")
    let selectedScreen
    let mouseoverSeat
    let selectedSeat
    const seatChoices = new Set
    let screenId
    let screenRow=14
    let screenCol=20+1
    let maxSeatChoices
    let curSeatChoices
    let prevMouseOverSeat
    function apiByDate(date){
        fetch('api/booking/'+date).then(res => res.json()).then(item=>{
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
            alert('영화 3개까지선택가능')
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
            alert('영화3개까지선택가능')
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
                            ctts-ty-div-cd="MVCT01" theab-popup-at="Y" theab-popup-no="2015">
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
        const scheduleId= $(this).attr('schedule-id')
        const mNo= $(this).attr('movie-no')
        const url="/api/booking/step0?schedulId="+scheduleId
        fetch(url).then(res=>console.log(res))
        $("#step0").hide()
        $("#step1").show()
        createSeats(screenRow,screenCol)
        clearChoice()
        const movie = $("#mBtn-"+mNo)
        generateSeatResultByScheduleId(scheduleId)
    })

    function generateSeatResultByScheduleId(id){
        const s=$("#schedule-"+id)
        const mNo=s.attr('movie-no')
        const movie = $("#mBtn-"+mNo)
        $(".seat-result").children()
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
    function createSeats(screenRow,screenCol){
        const seatsDiv = $("#seatsDiv")
        fetch("/api/booking/getDisabledSeats?screenId="+screenId).then(res=>res.json()).then(json=>{
            const allEmptySeats = new Set
            $.each(json,(id,disabledSeatNo)=>{
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
            seatNumbering(screenRow,screenCol)
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
        return `<button type="button" title="${seatNo} (스탠다드/일반)" class="jq-tooltip seat-condition ${state}" id=${seatNo} r=${screenRow} c=${screenCol} seatclasscd="GERN_CLS" seatzonecd="GERN_ZONE">
                                     <span class="num">${seatNo}</span>
                                    <span class="rank">일반</span>
                                                    </button>`
    }
    function createDisabledSeat(seatNo,screenRow,screenCol){
        return `<div class="empty" id="${seatNo}" r="${screenRow}" c="${screenCol}" style="width:20px; height:19px; padding:0px;"></div>`
    }

    $("#seatsDiv").on('click','button',function() {
        const seatsToPick = maxSeatChoices - curSeatChoices
        if($(this).hasClass('choice')){
            removeCurSeatChoice($(this).attr('id'))
            const pair = $(this).attr('pair')
            if(pair){
                removeCurSeatChoice(pair)
            }
            return
        }
        if (seatsToPick==0){
            alert('관람인원 선택하쇼')
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

    }
    function removeCurSeatChoice(seatNo){
        $("#"+seatNo).removeClass('choice')
        $(".my-seat").children(":eq("+--curSeatChoices+")").removeClass('choice').addClass('possible').text('-')
    }
    function incrementMaxSeatChoice(){
        $(".my-seat").children(":eq("+maxSeatChoices+++")").addClass('possible')
    }
    function decrementMaxSeatChoice(){
        $(".my-seat").children(":eq("+(--maxSeatChoices)+")").removeClass('possible')
    }
    $("#seatsDiv").on('mouseover','button',function() {
        const seatsToPick = maxSeatChoices - curSeatChoices
        if(seatsToPick==0 || $(this).hasClass('choice')){
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

    function chooseSeats(){
        const seats=$("#seatsDiv .on")
        seats.each(function(index,el){
            $(this).addClass('choice')
            seatChoices.add($(this).attr('id'))
        })
    }
    function clearSeatChoices(){
        const onSeats=$("#seatsDiv .on").removeClass('on')
        const choiceSeats=$("#seatsDiv .choice").removeClass('choice')
        selectedSeat=undefined
        seatChoices.clear()
    }
    // $(this).replaceWith(`<div class="empty" style="width:18px; height:18x; padding:0px;"></div>`)

    $("#emptySeat").on('click',()=>{
        const seats=$("#seatsDiv .choice")
        seats.each(function(i,e){
            $(this).replaceWith(createDisabledSeat($(this).attr('id'),$(this).attr('r'),$(this).attr('c')))
        })
        clearSeatChoices()


    })

    $("#seatSoftReset").on('click',()=>{
        clearSeatChoices()
        $("#seatsDiv").empty()
        createSeats(screenRow,screenCol)
    })
    $("#clearChoice").on('click',()=>{
        clearChoice()
    })
    $("#updateSeats").on('click',()=> {
        const params = []
        params.push(screenId)
        let x = $(".empty")
        x.each(function (i, e) {
            params.push($(this).attr('id'))
        })
        fetch("/api/booking/updateSeats", {
            method: "POST",
            headers: {
                "Content-Type": "application/json; charset=utf-8"
            },
            body: JSON.stringify(params)
        }).then(res => console.log(res))
        seatNumbering(screenRow,screenCol)
        clearSeatChoices()
        const screenSeatsQuantity= screenRow*(screenCol-1)-(params.length-1)
        console.log(screenId+ " 스크린의 총 좌석수 => " +screenSeatsQuantity)
    })
    $("#seatHardReset").on('click',()=>{
        initDefaultSeats(screenRow,screenCol)
        seatNumbering(screenRow,screenCol)
        clearSeatChoices()
    })
    // $("#mCSB_1_container222").on('mouseleave',()=>{
    //     if(selectedSeat){
    //         const onSeats=$("#seatsDiv .on").removeClass('on')
    //     }
    // })

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
    }
    $(".up").on('click',function(){
        if(maxSeatChoices==8){
            return
        }
        incrementMaxSeatChoice()
        singleSeatCheck()
        let count= $(this).siblings('div').children(':first')
        count.text(parseInt(count.text())+1)
        console.log("cur : "+curSeatChoices+" max : "+maxSeatChoices)
    })

    $(".down").on('click',function(){
        if(maxSeatChoices==0){
            return
        }
        if(maxSeatChoices==curSeatChoices){
            alert('다시 선택')
            clearChoice()
            return
        }
        decrementMaxSeatChoice()
        singleSeatCheck()
        let count= $(this).next().children(':first')
        count.text(parseInt(count.text())-1)
        console.log("cur : "+curSeatChoices+" max : "+maxSeatChoices)
    })
})