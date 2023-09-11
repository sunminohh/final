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
    dayjs.extend(dayjs_plugin_relativeTime)
    dayjs.locale('ko')
    let day= dayjs().locale('ko')
    day.format()

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
    $("#rating").on('input',function(){
        $("#ratingValue").text($(this).val())
    })

        $(".oneContent").hide()
    $("#to-comment").on('click',function(e){
        e.preventDefault()
        toComment()
        const offset = $("#to-comment").offset()
        $('html,body').animate({scrollTop : offset.top}, 0);
    })
    $("#to-info").on('click',function(e){
        e.preventDefault()
        $(this).addClass('on').siblings().removeClass('on')
        $(".oneContent").hide()
        $(".infoContent").show()
    })

    $("#to-party-form").on('click',function(){
        location.href="/board/party/add?movieTitle="+$("#movieTitle").text()
    })
    $("#to-party-form-disabled").on('click',function(){
        $("#login-modal").addClass('is-open')
        $("#login-modal").find('#id').focus()
    })

 $(".time-diff").each(function(i,e){
     $(this).text(dayjs($(this).attr('date')).fromNow())
 })
    function toComment(){
        $(".tabtab").removeClass('on')
        $("#to-comment").toggleClass('on')
        $(".infoContent").hide()
        $(".oneContent").show()
        const offset = $("#to-comment").offset()
        $('html,body').animate({scrollTop : offset.top}, 0);
    }
    $("#comment-enter").on('click',function(){
        let text= $("#comment-input").val()
        text && uploadComment()
        })
    $("#comment-input").on("keydown",function(e){
        let text= $(this).val()
        if(e.keyCode===29){
            console.log(e.key)
            $(this).blur()
        }else if(text && e.keyCode===13){
            uploadComment()
        }
    })
    const urlParams = new URL(location.href).searchParams;
    const movieNo = urlParams.get('movieNo');
    const tab = urlParams.get('tab')
    if(tab){
        toComment()
    }
    function uploadComment(){
        const text= $("#comment-input").val()
        const movieComment={
            no: Date.now(),
            commentContent : text,
            movieNo: movieNo,
            commentRating : $("#rating").val()
    }
        fetch("/api/movie/comment/insertComment",{
            method:'post',
            headers:{
                'Content-Type': 'application/json'
            },
            body:JSON.stringify(movieComment)
        }).then(res=>res.json()).then(data=>{
            if(tab){
                location.reload()
            }else {
                location.href=location.href+"&tab=1"
            }
        })

    }
    $(".delete-comment").on('click',function(){
        fetch("/api/movie/comment/delete?commentNo="+$(this).attr('commentNo')).then(res=>{
            if(tab){
                location.reload()
            }else {
                location.href=location.href+"&tab=1"
            }
        })
    })
    $(".oneLikeBtn").on('click',function (){
        const icon= $(this).children(":first")
        if(icon.hasClass('ico-like-blue')){
            icon.removeClass('ico-like-blue')
            icon.addClass('ico-like')
            fetch("/api/movie/commentLike/delete?commentNo="+$(this).attr('commentNo')).then(res=>{
               let likes= icon.next()
                likes.text(parseInt(likes.text())-1)
            })

        }else{
            icon.removeClass('ico-like')
            icon.addClass('ico-like-blue')
            fetch("/api/movie/commentLike/insert?commentNo="+$(this).attr('commentNo')).then(res=>console.log(res))
            let likes= icon.next()
            likes.text(parseInt(likes.text())+1)
        }

    })
    function fetchMovieCommnetsByMovieNo(movieNo){
        location.href("")
    }
})