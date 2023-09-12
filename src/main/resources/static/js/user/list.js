$(()=>{
    $(".booking-cancel").on('click',function(){
        const bookingNo=$(this).attr('booking-no')
        Swal.fire({
            icon: 'warning',
            title: '예매 취소',
            text: '정말 예매 취소하시겠습니까?',
            showCancelButton: true,
            confirmButtonText: '확인',
            cancelButtonText: '취소',
        }).then((result) => {
            if (result.isConfirmed) {
                fetch("/api/booking/deleteBooking?bookingNo="+bookingNo).then(res=>res.text()).then(res=>{
                    Swal.fire({
                        icon: 'success',
                        title: '취소 완료',
                        text: '예매가 취소되었습니다.'
                    }).then(() => {
                        location.reload()
                    })
                })
            }
        })
    })



})