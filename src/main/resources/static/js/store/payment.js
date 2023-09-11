$(()=>{

    let productLimit = 10;

    let amount;

    $(".line .cont button").click(function () {
        let btn_name = $(this).attr("class");
        let input_d = $(".line .cont input[type='text']");
        let input_num = Number(input_d.val());

        if (btn_name == "btn minus") {
            if (input_num > 1) {
                input_d.val(input_num - 1);
            }
        } else if (btn_name == "btn plus") {
            if (input_num < productLimit) {
                input_d.val(input_num + 1);
            } else if (Number(productLimit) == -1) {
                input_d.val(input_num + 1);
            }
        }

        updateTotalAmount();
    })

    function numberWithCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    function updateTotalAmount() {
        let input_d = $(".line .cont input[type='text']");
        let input_num = Number(input_d.val());
        let discountedPrice = Number($('#discountedPrice').val());
        let originalPrice = Number($('#originalPrice').val());

        amount = discountedPrice * input_num;
        let totalOriginalPrice = originalPrice * input_num;


        $('#prdtSumAmt').html(numberWithCommas(amount));
        $('#totalOriginalPrice').val(totalOriginalPrice);
        $('#totalDiscountedPrice').val(amount);
        $('#productAmount').val(input_num);
    }

    updateTotalAmount();

    // 구매 후 취소 및 상품 이용 안내
    $(".box-pulldown").on("click", function() {
        if ($(this).hasClass("on")) {
            $(this).removeClass("on");
        } else {
            $(this).addClass("on");
        }
    });
    $.urlParam = function(name) {
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        if (results==null) {
            return null;
        } else {
            return results[1] || 0;
        }
    }
    $(document).ready(function() {
        $('#btnCart-user').click(function(event) {
            event.preventDefault();

            const totalDiscountedPrice = $("#totalDiscountedPrice").val();
            const totalOriginalPrice = $("#totalOriginalPrice").val();
            const userId = $("#userId").val();
            const productNo = $("#productNo").val();
            const productAmount = $("#productAmount").val();
            const catNo = $("#catNo").val();

            const requestData = {
                totalDiscountedPrice: totalDiscountedPrice,
                totalOriginalPrice: totalOriginalPrice,
                userId: userId,
                productNo: productNo,
                productAmount: productAmount,
                catNo: catNo
            };

            $.ajax({
                type: "POST",
                url: "/cart/addProductIntoCart",
                data: requestData,
                success: function success() {
                    Swal.fire({
                        icon: 'success',
                        text: "상품이 장바구니에 담겼습니다.",
                        confirmButtonText: '확인'
                    }).then((result) => {
                        if (result.value) {
                            console.log("정보가 성공적으로 서버에 전달되었습니다.");
                            window.location.href = "http://localhost/store";
                        }
                    })
                },
                error: function error() {
                    Swal.fire({
                        icon: 'error',
                        text: "오류가 발생하였습니다. 잠시 후 다시 시도해 주세요."
                    })
                    console.log("정보가 서버에 전달되지 않았습니다.");
                }
            })
        })
    })

    const $username = $("input[name='username']");

    $(".btnCart-not-user").on("click", function (e) {
        e.preventDefault();
        Swal.fire({
            icon: 'warning',
            title: "이용 제한",
            text: "로그인 후 이용가능합니다. 로그인 하시겠습니까?",
            showCancelButton: true,
            confirmButtonText: '네',
            cancelButtonText: '아니오',
            didClose: () => {
                $username.focus();
            }
        }).then((result) => {
            if (result.isConfirmed) {
                $(".btn-login").trigger("click");
            }
        });
    });

    // <-- 여기까지 productDetail JS에서 가져옴

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
                totalPrice += arr[2]*arr[3]*amount
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
            totalPrice: totalPrice,
            orderName: orderName,
            orderProducts: orderProducts.join('+'),
            orderSpecificProducts: orderSpecificProducts.join('+')}

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
                        "amount": order.totalPrice,
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
    }
})