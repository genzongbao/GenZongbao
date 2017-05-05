/**
 * Created by Leo on 2016/12/31.
 */
function addHouseSelect() {
    var house = $("#houseId option");
    alert(house.length);
    if (house.length == 0) {
        $("#houseId").attr("name", "");
        $("#houseId").append("<option>你还没有添加房产请添加</option>");
        $("#btn").attr("disabled", "disabled")
    }
}

function checkPhone(phone) {
    var myreg = /^1[3|4|5|7|8][0-9]{9}$/;
    if (myreg.test(phone)) {
        return true;
    } else {
        return false;
    }
}
$(function () {
    $(".close").click(function () {
        $(this).parent().hide();
    })
})