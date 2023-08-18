var date = new Date();
var year = date.getFullYear();

function getAddress() {
    var port = "8125";

    var addressLocal = "http://127.0.0.1:" + port + "/";
    var addressLocalLAN = "http://192.168.31.230:" + port + "/";
    var addressLAN = "http://192.168.1.3:" + port + "/";
    var addressWAN = "http://mctest.ultra.wang:" + port + "/";

    var envAddress = addressLocal;

    // WAN环境自动切换地址
    var hostname = window.location.hostname;
    if (hostname.includes("ultra.wang")) {
        envAddress = addressWAN;
    }

    return envAddress;
}

// 封装XMLHttpRequest
function ajax(method, controller, async, formData) {
    var result;
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            result = this.responseText;

            var jsonResult = JSON.parse(result);

            result = jsonResult.obj;

            if (jsonResult.msg != null && jsonResult.msg != "") {
                var msg = jsonResult.msg;
                console.log(msg);
                // document.getElementById('divAlert').innerText = jsonResult.msg;
                commonUtil.message(msg, 'danger', 2000);  // 调用BootStrap样式的alert框
                // return;
            }
        }
    }
    var address = getAddress() + controller;
    var method = method.toUpperCase();
    xhr.open(method, address, async);
    xhr.send(formData);
    return result;
}

// 清空表格行
function removeTableTr(tr) {
    var tableTr = document.getElementsByClassName(tr);
    var tableTrLength = tableTr.length;
    for (let i = 0; i < tableTrLength; i++) {
        tableTr[0].parentNode.removeChild(tableTr[0]);
    }

    var modalTableTr = document.getElementsByClassName("modalTableTr");
    var modalTableTrLength = modalTableTr.length;
    for (let i = 0; i < modalTableTrLength; i++) {
        modalTableTr[0].parentNode.removeChild(modalTableTr[0]);
    }
}

// 复制到剪贴板
function copyToClipboard(text) {
    if (window.clipboardData) {
        window.clipboardData.setData('text', text);
    } else {
        (function (text) {
            document.oncopy = function (e) {
                e.clipboardData.setData('text', text);
                e.preventDefault();
                document.oncopy = null;
            }
        })(text);
        document.execCommand('Copy');
        document.getElementById('copyMsg').style.color = "green";
        document.getElementById('copyMsg').innerHTML = "复制成功！";
    }
}

// 模态框显隐
function showModal() {
    var modal = document.getElementById('modal-background');
    if (modal.style.visibility == "visible") {
        modal.style.visibility = "hidden";
    } else {
        modal.style.visibility = "visible"
    }
}

// 显示页脚
// function footerShow() {
//     var mainBody = document.getElementById("divBody").parentNode;
//     var footerDiv = document.getElementsByClassName('footerDiv');
//     var footerDiv = mainBody.childElementCount;
//     mainBody.appendChild(document.createElement('div'));
//     mainBody.children[footerDiv].className = 'footerDiv';
//     mainBody.children[footerDiv].innerHTML = "<div id='footer'><p>© 2018-" + year + " | <a href='http://ultra.wang/' target='_blank'>ultra.wang</a></p></div>";
// }


var commonUtil = {
    /**
     * 弹出消息框
     * @param msg 消息内容
     * @param type 消息框类型（参考bootstrap的alert）
     */
    alert: function (msg, type) {
        if (typeof (type) == "undefined") { // 未传入type则默认为success类型的消息框
            type = "success";
        }
        // 创建bootstrap的alert元素
        var divElement = $("<div></div>").addClass('alert').addClass('alert-' + type).addClass('alert-dismissible');
        // console.log("msg.length = " + msg.length);
        var alertWidth = msg.length * 20;
        if (alertWidth < 150) {
            alertWidth = 150;
        }
        var screenWidth = window.innerWidth;
        divElement.css({ // 消息框的定位样式
            "position": "absolute",
            "width": alertWidth,
            "top": "100px",
            "left": (screenWidth - alertWidth) / 2,
            // "right": "45%",
            // "max-width": "200px",
            "text-align": "center",
            // "margin": "auto",
            // "transform": "translate(55%, 0%)"
            // "vertical-align": "middle"
        });
        divElement.text(msg); // 设置消息框的内容
        // 消息框添加可以关闭按钮
        var closeBtn = $('<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>');
        $(divElement).append(closeBtn);
        // 消息框放入到页面中
        $('body').append(divElement);
        return divElement;
    },

    /**
     * 短暂显示后上浮消失的消息框
     * @param msg 消息内容
     * @param type 消息框类型
     */
    message: function (msg, type, countdownTime) {
        var divElement = commonUtil.alert(msg, type); // 生成Alert消息框
        var isIn = false; // 鼠标是否在消息框中

        divElement.on({ // 在setTimeout执行之前先判定鼠标是否在消息框中
            mouseover: function () { isIn = true; },
            mouseout: function () { isIn = false; }
        });

        // 短暂延时后上浮消失
        setTimeout(function () {
            var IntervalMS = 20; // 每次上浮的间隔毫秒
            var floatSpace = 60; // 上浮的空间(px)
            var nowTop = divElement.offset().top; // 获取元素当前的top值
            var stopTop = nowTop - floatSpace;    // 上浮停止时的top值
            divElement.fadeOut(IntervalMS * floatSpace); // 设置元素淡出

            var upFloat = setInterval(function () { // 开始上浮
                if (nowTop >= stopTop) { // 判断当前消息框top是否还在可上升的范围内
                    divElement.css({ "top": nowTop-- }); // 消息框的top上升1px
                } else {
                    clearInterval(upFloat); // 关闭上浮
                    divElement.remove();    // 移除元素
                }
            }, IntervalMS);

            if (isIn) { // 如果鼠标在setTimeout之前已经放在的消息框中，则停止上浮
                clearInterval(upFloat);
                divElement.stop();
            }

            divElement.hover(function () { // 鼠标悬浮时停止上浮和淡出效果，过后恢复
                clearInterval(upFloat);
                divElement.stop();
            }, function () {
                divElement.fadeOut(IntervalMS * (nowTop - stopTop)); // 这里设置元素淡出的时间应该为：间隔毫秒*剩余可以上浮空间
                upFloat = setInterval(function () { // 继续上浮
                    if (nowTop >= stopTop) {
                        divElement.css({ "top": nowTop-- });
                    } else {
                        clearInterval(upFloat); // 关闭上浮
                        divElement.remove();    // 移除元素
                    }
                }, IntervalMS);
            });
        }, countdownTime);
    }
}