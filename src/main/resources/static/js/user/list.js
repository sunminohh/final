$(()=>{
    $(".booking-cancel").on('click',function(){
        const bookingNo=$(this).attr('booking-no')
        fetch("/api/booking/deleteBooking?bookingNo="+bookingNo).then(res=>res.text()).then(res=>{
            location.reload()
        })
    })



})