$(() => {
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
    $("#movieSearch").on({
        input: function(e){
            let keyword=$(this).val()
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
                    $("#movieList").children().each(function(i, li){
                        let title=$(this).attr('movie-title')
                        if(!resultSet.has(title)){
                            $(this).find(".tit.movie-title").text(title)
                            $(this).hide()
                        }else{
                            $(this).show()
                            $(this).find(".tit.movie-title").html(highlightTitle(keyword,title))
                        }
                    })

            })
        }
    })
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
    }


$(".modal-layer").show()
})