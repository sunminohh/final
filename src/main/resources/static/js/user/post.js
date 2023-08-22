$(() => {
    $("#btnZipcode").click(function (e) {
        e.preventDefault();

        const target = $(this);
        // daum.postcode.load(function() {
        new daum.Postcode({
            oncomplete: function (d) {
                let extraAddr = "";

                if (d.bname !== '' && /[동|로|가]$/g.test(d.bname)) {
                    extraAddr += d.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if (d.buildingName !== '' && d.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + d.buildingName : d.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }

                target.prev().html(d.zonecode);
                target.next().html(d.address + extraAddr);

                $('[name=zipcd]').val(d.zonecode);
                $('[name=mbAddr]').val(d.address + extraAddr);
            }
        }).open();
        // });
    });

    $("#btnCancel").click(function () {
        history.back();
        return false;
    })

});