$(function() {
    let productLimit = 10;

    $(".line .cont button").click(function () {
        let btn_name = $(this).attr("class");
        let input_d = $(".line .cont input[type='text']");
        let input_num = Number(input_d.val());

        if (btn_name == "btn minus") {
            if (input_num > 1) {
                input_d.val(input_num - 1);
            }
        } else if (btn_name == "btn plus") {
            if (input_num < productLimit) {
                input_d.val(input_num + 1);
            } else if (Number(productLimit) == -1) {
                input_d.val(input_num + 1);
            }
        }

        updateTotalAmount();
    })

    function numberWithCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    function updateTotalAmount() {
        let input_d = $(".line .cont input[type='text']");
        let input_num = Number(input_d.val());
        let price = Number($('#productTotalAmount').val());
        let totalAmount = price * input_num;

        $('#prdtSumAmt').html(numberWithCommas(totalAmount));
    }

    updateTotalAmount();
})