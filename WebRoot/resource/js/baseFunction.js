var date = new Date();
var year = date.getFullYear();

function getAddress() {
    var port = "8125";

    var addressLocal = "http://127.0.0.1:" + port + "/";
    var addressLocalLAN = "http://192.168.31.230:" + port + "/";
    var addressLAN = "http://192.168.1.3:" + port + "/";
    var addressWAN = "http://mctest.ultra.wang:" + port + "/";

    var envAddress = addressLAN;

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
                alert(jsonResult.msg);
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
function footerShow() {
    var mainBody = document.getElementById("divBody").parentNode;
    var footerDiv = document.getElementsByClassName('footerDiv');
    var footerDiv = mainBody.childElementCount;
    mainBody.appendChild(document.createElement('div'));
    mainBody.children[footerDiv].className = 'footerDiv';
    mainBody.children[footerDiv].innerHTML = "<div id='footer'><p>© 2018-" + year + " | <a href='http://ultra.wang/' target='_blank'>ultra.wang</a></p></div>";
}