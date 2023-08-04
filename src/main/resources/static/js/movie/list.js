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
})