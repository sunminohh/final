$(() => {
    let isOnlyPlaying=$("#only-playing").hasClass('on')

    $("#only-playing").on("click",function (){
        $(this).toggleClass('on')
        isOnlyPlaying=$("#only-playing").hasClass('on')
        if(isOnlyPlaying){
            $("#movieList").children().each(function(i, li){
                if( $(this).attr('isPlaying') !== 'Y'){
                    $(this).hide()
                }
            })
        }else{
            if($("#movieSearch").val()){
                search()
            }else initList()
        }
        count()
    })

    count()
    function count(){
        let count=0
        $("#movieList").children().each(function(i, li){
            if( $(this).is(":visible")){
                count++
            }
        })
        $("#totCnt").text(count)
    }
    $(".movie-list-info").on({
        mouseenter: function() {
            $(this).find(".movie-score").off().finish().addClass("on").animate({
                opacity: 1
            }, 300)
        },
        mouseleave: function (){
            $(this).find(".movie-score").finish().animate({
                opacity: 0
            }, 300, function() {
                $(this).removeClass("on")
            })
        }
    })
    function search(){
        let keyword=$("#movieSearch").val()
        if(keyword === "" ){
            initList()
            return
        }
        fetch("/api/movie/search?keyword="+keyword).then(res=>res.json())
            .then(data=> {
                const resultSet = new Set
                $.each(data,function(i,movie){
                    resultSet.add(movie)
                })
                const size= resultSet.size
                $("#movieList").children().each(function(i, li){
                    let title=$(this).attr('movie-title')
                    if(resultSet.has(title)){
                        let flag=true
                        if(isOnlyPlaying){
                            if( $(this).attr('isPlaying') !== 'Y'){
                                flag=false
                            }
                        }
                        if (flag){
                            $(this).show()
                            $(this).find(".tit.movie-title").html(highlightTitle(keyword,title))
                        }
                    }else{

                        $(this).find(".tit.movie-title").text(title)
                        $(this).hide()
                    }
                })
                count()

                if(size==0){
                    $("#noDataDiv").show()
                    $("#movieList").hide()
                }else{
                    $("#noDataDiv").hide()
                    $("#movieList").show()
                }

            })
    }

    $("#movieSearch").on('input',search)
    function highlightTitle(keyword, title){
        if(title.indexOf(keyword)>=0){
            return title.replace(keyword,`<span style="background-color:yellow">${keyword}</span>`)
        }
        return title.replace(keyword,`<span style="background-color:yellow">${keyword.substring(0,keyword.length-1)}</span>`)


    }
    function initList(){
        $("#movieList").children().show()
        $(".movie-title").each(function(i){
            $(this).text($(this).attr('title'))
        })
        $("#noDataDiv").hide()
        $("#movieList").show()
        count()
    }


    $(".modal-layer").show()
})