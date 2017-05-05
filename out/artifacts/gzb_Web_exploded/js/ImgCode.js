/**
 * Created by Leo on 2017/1/7.
 */
(function () {
    $.ImgCode = function () {
        var $code = "<div class='imgCode'><div><input type='text' id='imgCode'><img src='getCode1?time=" + new Date() + "'></div>";
        var $sub = "<div><input type='button' id='subCode' value='发送' > </div></div>";
        $("body").append($code + $sub);
    }
    function add() {
        var Cwidth = document.documentElement.clientWidth;
        var Cheight = document.documentElement.clientHeight;
        $(".imgCode").css({width: Cwidth, height: Cwidth, "margin-top": cheight / 2, "margin-left": cWidth / 2});
    }

})();