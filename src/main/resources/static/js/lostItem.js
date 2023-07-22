$(() => {

    const $pagination = $(".pagination");
    const $keyword = $("#keyword");
    const $location = $("#location");
    const $answered = $("#answered");
    // 초기 1회 검색
    search(1);

    // 페이지네이션 클릭 이벤트
    $pagination.on("click", "a,strong", function (e) {
        e.preventDefault();
        const pageNo = $(this).attr("href");
        search(pageNo);
    });

    $keyword.keyup(() => search(1));
    $location.change(() => search(1));
    $answered.change(() => search(1));

    // 검색
    function search(pageNo = 0) {
        const keyword = $keyword.val();
        const location = $location.val();
        const answered = $answered.val();

        $.get("/center/lost/list", {pageNo, keyword, location, answered})
            .then(({data}) => {
                $("#totalCnt").text(data.totalElements);
                createContent(data);
                createPagination(data);
            });
    }

    // 내용 Elements 생성
    function createContent(page) {
        const {content: contents} = page;

        let html = "<tr><td colspan='5'>분실물이 존재하지 않습니다.</td></tr>";

        if (contents.length) {
            html = contents.map(({id, title, createDate, answered, locationCode}) =>
                `<tr>
                    <td>${id}</td>
                    <td>${locationCodeToString(locationCode)}</td>
                    <th scope="row">
                        <a href="/center/lost/${id}" class="btn-layer-open moveBtn" >${title}</a>
                    </th>
                    <td>${ answered === 'Y' ? '답변완료' : '미답변' }</td>
                    <td>${moment(createDate).format("YYYY.MM.DD")}</td>
                </tr>`
            ).join("");
        }
        $(".board-list tbody").html(html);
    }

    // Pagination Elements 생성
    function createPagination(page = {number: 0, totalPages: 1}) {
        const block = 5;
        const begin = Math.floor(page.number / block) * block + 1;
        const end = begin + 5 > page.totalPages ? page.totalPages : begin + block - 1;
        $pagination.empty();
        ++page.number

        if (begin !== 1) {
            const first = $(`<a href="1" class='control first'>1</a>`).attr('href', 1);
            const prev = $(`<a href='${begin - 1}' class='control prev'></a>`);
            $pagination.append(first).append(prev);
        }
        for (let i = begin; i <= end; i++) {
            const $page = i === page.number ?  $(`<strong href="${i}">${i}</strong>`) : $(`<a href="${i}">${i}</a>`);
            if (i === page.number) $page.attr('class', 'active');
            $pagination.append($page);
        }
        if (end < page.totalPages) {
            const $next = $(`<a href='${end + 1}' class='control next'></a>`);
            const $last = $(`<a href="${page.totalPages}" class='control last'></a>`);
            $pagination.append($next).append($last);
        }
    }

    function locationCodeToString(code) {
        switch (code) {
            case 0: return "서울";
            case 1: return "경기";
            case 2: return "인천";
            case 3: return "대전/충청/세종";
            case 4: return "부산/대구/경상";
            case 5: return "광주/전라";
            case 6: return "강원";
            case 7: return "제주";
        }
    }

});