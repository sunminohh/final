$(()=>{

    function setOrder(){
        const map = new Map
        const map2 = new Map
        const products= $(".cart-item")
        products.each(function(){
            orderName=$(this).attr('productName')
            if(orderName.includes('패키지')){
                packageName=orderName
            }
            let amount= $(this).find('.cart-amount').text()
            amount= amount ? amount : $(this).find('.cart-amount').val()
            amount=parseInt(amount)
            orderProducts.push( $(this).attr('productNo')+','+$(this).attr('productName')+','+$(this).attr('unitPrice')+','+amount)

            const packageInfo = $(this).attr('packageInfo')
            const items= packageInfo.split('+')
            for (const item of items){
                let arr= item.split(',')
                let no=arr[0]
                let qtt=parseInt(arr[3])
                if(!map.has(no)){
                    map.set(no,qtt*amount)
                }else{
                    map.set(no,parseInt(map.get(no))+qtt*amount)
                }
                map2.set(arr[0],','+arr[1]+','+arr[2]+',')
                totalPrice+=arr[2]*arr[3]*amount
            }
        })
        map.forEach((value, key) => {
            orderSpecificProducts.push(key+map2.get(key)+map.get(key))
        });

        if(packageName){
            orderName=packageName
        }
        if(products.length>1){
            orderName += " 외 " + (products.length-1) + "건"
        }
    }
    let totalPrice =0


    let orderName
    let packageName
    let orderProducts=[]
    let orderSpecificProducts=[]
    setOrder()

    console.log("오더네임 => "+orderName)
    console.log("오더프러덕츠 => "+ orderProducts.join('+'))
    console.log("스페시픽프러덕츠 => "+ orderSpecificProducts.join('+'))


    $(".btn.plus").on('click',function(){
        setOrder()
        console.log("오더네임 => "+orderName)
        console.log("오더프러덕츠 => "+ orderProducts.join('+'))
        console.log("스페시픽프러덕츠 => "+ orderSpecificProducts.join('+'))
    })

    $("#request-payment").on('click',requestPayment)

    function requestPayment(){
        setOrder()
        const order={
            orderId: Date.now() + Math.floor((Math.random()*100)),
            totalPrice:totalPrice,
            orderName:orderName,
            orderProducts:orderProducts.join('+'),
            orderSpecificProducts:orderSpecificProducts.join('+')}

        fetch("/api/order/requestPayment",{
            method:'post',
            headers:{
                'Content-Type': 'application/json'
            },
            body:JSON.stringify(order)
        }).then(res=>res.json()).then(order => {
                let path = "/order/";
                let successUrl = window.location.origin + path + "success";
                let failUrl = window.location.origin + path + "failure";
                let jsons = {
                    "card": {
                        "amount": order.amount,
                        "orderId": order.orderId,
                        "orderName": order.orderName,
                        "successUrl": successUrl,
                        "failUrl": failUrl,
                        "cardCompany": null,
                        "cardInstallmentPlan": null,
                        "maxCardInstallmentPlan": null,
                        "useCardPoint": false,
                        "customerName": order.userName,
                        "customerEmail": null,
                        "customerMobilePhone": null,
                        "useInternationalCardOnly": false,
                        "flowMode": "DEFAULT",
                        "discountCode": null,
                        "appScheme": null
                    }
                }
                pay('카드', jsons.card);
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

  /*      then(res=>res.json()).then(data=> {
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
        }*/

    }
})