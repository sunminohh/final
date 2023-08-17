$(() => {
    // 그냥 모델앤뷰에 담아서 전해줌
    // if( $(".btn-like").attr('data2') !== 'anonymousUser' ){
    //     const likeBtn= $(".btn-like")
    //     const no=likeBtn.attr('data')
    //     const userId=likeBtn.attr('data2')
    //     const url = `http://localhost/api/movie/like/${no}/${userId}`
    //     let liked =0
    //     fetch(url).then((res) =>res.json())
    //         .then(json => {
    //        if(json.movieNo !== 0){
    //            likeBtn.addClass('on')
    //        }
    //     })
    // }
    $(".btn-like").click(function (e){
        const no=$(this).attr('data')
        const userId=$(this).attr('data2')
        if(userId === 'anonymousUser' ){
            alert('로그인해라')
            return
        }
        let like
        let likes=parseInt( $(this).children('span').text())

        if ($(this).hasClass('on')){
            $(this).removeClass('on')
            $(this).children('span').text(likes-1)
            like = false
        }else {
            $(this).addClass('on')
            $(this).children('span').text(likes+1)
            like = true
        }
        const url = `http://localhost/api/movie/like/${no}/${userId}/${like}`
            fetch(url).then((res) => console.log(res));
    })
})