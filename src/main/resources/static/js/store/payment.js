$(()=>{
    const map = new Map
    let totalPrice =0
    let i=1
    const map2 = new Map
    $(".goods-info").each(function(){
        if($(this).attr('data')){
            const q= parseInt($(this).parent().parent().find(".cart-quantity").text())
            const items=$(this).attr('data').split('+')
            for (const item of items){
                let arr= item.split(',')
                let no=arr[0]
                let qtt=parseInt(arr[3])
                if(!map.has(no)){
                    map.set(no,qtt*q)
                }else{
                    map.set(no,parseInt(map.get(no))+qtt*q)
                }
                console.log(i++ + "번째 상품 "+arr[2]+"번호: "+arr[0]+" 단위가격 : " +arr[2]+" 금액 :" + arr[2]*arr[3])
                map2.set(arr[0],arr[1])
                totalPrice+=arr[2]*arr[3]*q
            }

        }
    })
    console.log(totalPrice)
    map.forEach((value, key) => {
        console.log("상품번호: " + key + ", 상품명: " + map2.get(key)+ ", 상품갯수: " +value);
    });
    console.log([...map])

    function requestPayment(){
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
})