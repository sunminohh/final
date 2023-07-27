$(() => {
    $header = $('#header');
    $gnb = $('#gnb');
    $depth1 = $('.gnb-depth1 > li');

    $gnb.find('.gnb-depth1 > li > a').on({

        mouseenter: function () {

            if ($(this).closest('li').find('.gnb-depth2').length > 0) {

                $depth1.removeClass('on');
                $(this).parent('li').addClass('on');
                $gnb.addClass('on');

            } else {
                $gnb.find('li').removeClass('on');
                $gnb.removeClass('on');
            }


        },
        focus: function () {

            if ($(this).closest('li').find('.gnb-depth2').length > 0) {

                $depth1.removeClass('on');
                $(this).parent('li').addClass('on');
                $gnb.addClass('on');

            } else {
                $gnb.find('li').removeClass('on');
                $gnb.removeClass('on');
            }
        }

    });

    $header.on({
        mouseleave: function () {
            $gnb.find('li').removeClass('on');
            $gnb.removeClass('on');
        },
        blur: function () {
            $gnb.find('li').removeClass('on');
            $gnb.removeClass('on');
        }
    });

// 상단 광고 영역
    $top_add = $('.header-add-area');

    if ($top_add.length > 0) {
        $('.container').addClass('area-ad')
    } else {
        $('.container').removeClass('area-ad')
    }


// header : 사이트맵, 검색, mymega 클릭시 레이어
    $(document).on('click', '.link-area .header-open-layer', function (e) {
        e.preventDefault();
        _layer = $(this).attr('href');

        $(this).addClass('target');

        if ($(this).hasClass('on')) {
            $(this).removeClass('on');
            $(this).removeClass('target');

            $(_layer).removeClass('on');
        } else {
            $(this).addClass('on').siblings('.header-open-layer').removeClass('on');
            $(this).addClass('target').siblings('.header-open-layer').removeClass('target');

            $('.header-layer').removeClass('on');
            $(_layer).addClass('on');
            $(_layer).find('a.link-acc').focus();
        }
    });

// header : 사이트맵, 검색, mymega - 접근성
    $(document).on('click', '.header-layer a.link-acc', function (e) {
        e.preventDefault();
    });

// header : 사이트맵, 검색, mymega - 레이어 닫기
    $(document).on('click', '.header-layer .layer-close', function (e) {
        e.preventDefault();

        $('.header-open-layer').each(function () {
            if ($(this).hasClass('target')) {
                $(this).focus();
                $('.header-layer').removeClass('on');
                $(this).removeClass('on');
                $(this).removeClass('target');
            }
        });
    });

// header : 검색 레이어
    $(document).on('click load', '.layer-header-search .tab-rank ul li .btn', function () {

        $(this).closest('li').addClass('on').siblings().removeClass('on');

        liNum = $(this).closest('li').index();

        var rankCont = $(".layer-header-search .wrap .rank-cont");
        var _src = $(rankCont).find(".list ol:eq(" + liNum + ") ").children(":first").find("a").data('imgsrc');
        var _txt = $(rankCont).find(".list ol:eq(" + liNum + ") ").children(":first").find("a").text();

        $(rankCont).find(".img img").attr('src', _src);
        $(rankCont).find(".img img").attr('alt', _txt);
        $(rankCont).find(".list ol").hide();
        $(rankCont).find(".list ol:eq(" + liNum + ")").show();

    });

// header : 검색 레이어 - rank
    $(document).on('mouseenter', '.layer-header-search .wrap .rank-cont .list li a', function () {
        _src = $(this).data('imgsrc');
        _txt = $(this).text();

        $('.layer-header-search .wrap .rank-cont .img img').attr('src', _src);
        $('.layer-header-search .wrap .rank-cont .img img').attr('alt', _txt);
    });

// header 알림 레이어 열기
    $(document).on('click', '#header .util-area .right-link .after .notice', function (e) {
        e.preventDefault();

        $('.layer-header-notice').toggleClass('on');
    });

// header 알림 레이어 닫기
    $(document).on('click', '.layer-header-notice .btn-close-header-notice', function (e) {
        e.preventDefault();

        $(this).closest('.layer-header-notice').removeClass('on');
    });

//header - header 위의 광고영역 닫기
    $(document).on('click', '.header-add-area .btn-del', function (e) {
        e.preventDefault();
        $(this).closest('.header-add-area').hide();
        $(this).closest('body').find('header').addClass('none-ad');

        // 베네피아 존재시
        if ($('#benepiaBanner').length > 0) {
            $('#header').attr('style', 'top:57px')
        }
    });
});