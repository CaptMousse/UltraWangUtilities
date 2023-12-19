var date = new Date();
var year = date.getFullYear();

function getAddress() {

    var addressLocal = "127.0.0.1";
    var addressLocalLAN = "192.168.31.108";
    var addressLAN = "192.168.1.3";
    var addressIPv6 = "mctest.ultra.wang";
    var addressIPv4 = "121.41.75.41";

    var port = "8124";

    var envAddress = "http://" + addressLocal + ":" + port + "/";

    // WAN环境自动切换地址
    var hostname = window.location.hostname;
    if (hostname.includes("ultra.wang")) {
        envAddress = "http://" + addressIPv4 + ":" + port + "/";
    }

    return envAddress;
}

// 封装XMLHttpRequest
function ajax(method, controller, async, formData) {
    var result;
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            result = this.responseText;
            var jsonResult = JSON.parse(result);
            if (jsonResult.msg != null && jsonResult.msg != "") {
                var msg = jsonResult.msg;
                var status = jsonResult.status;
                if (status) {
                    commonUtil.message(msg, 'success', 2000);
                } else {
                    commonUtil.message(msg, 'danger', 2000);
                }
            }
            if (jsonResult.obj == null) {
                result = status;
            } else {
                result = jsonResult.obj;
            }
        }
    }
    var address = getAddress() + controller;
    var method = method.toUpperCase();
    xhr.open(method, address, async);

    xhr.withCredentials = true;
    //带上Cookie里登录token
    if (docCookies.hasItem("LoginToken")) {
        xhr.setRequestHeader("LoginToken", docCookies.getItem("LoginToken"));
    }
    if (docCookies.hasItem("VisitId")) {
        xhr.setRequestHeader("VisitId", docCookies.getItem("VisitId"));
    }
    xhr.send(formData);
    return result;
}

function ajaxJson(method, address, async, json) {
    var result;
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            result = this.responseText;
            var jsonResult = JSON.parse(result);
            if (jsonResult.msg != null && jsonResult.msg != "") {
                var msg = jsonResult.msg;
                var status = jsonResult.status;
                if (status) {
                    commonUtil.message(msg, 'success', 2000);
                } else {
                    commonUtil.message(msg, 'danger', 2000);
                }
            }
            if (jsonResult.obj == null) {
                result = status;
            } else {
                result = jsonResult.obj;
            }
        }
    }
    address = getAddress() + address;
    method = method.toUpperCase();
    xhr.open(method, address, async);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

    xhr.withCredentials = true;
    //带上Cookie里登录token
    if (docCookies.hasItem("LoginToken")) {
        xhr.setRequestHeader("LoginToken", docCookies.getItem("LoginToken"));
    }
    if (docCookies.hasItem("VisitId")) {
        xhr.setRequestHeader("VisitId", docCookies.getItem("VisitId"));
    }
    xhr.send(JSON.stringify(json));
    return result;
}

function getUltraWangUtilitiesVersion() {
    var version = ajax("GET", "UltraWangUtilities/getVersion", false);
    return version;
}

function getUuid() {
    var temp_url = URL.createObjectURL(new Blob());
    var uuid = temp_url.toString();
    URL.revokeObjectURL(temp_url);
    return uuid.substr(uuid.lastIndexOf("/") + 1);
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

function checkIfLogin() {
    if (docCookies.hasItem("LoginToken")) {
        // 从Cookie中获取token去比对
        var result = ajax("get", "blog/user/ifLogin?loginToken=" + docCookies.getItem("LoginToken"), false);
        if (result) {
            return true;
        } else {
            docCookies.removeItem("LoginToken");
            return false;
        }
    } else {
        return false;
    }
}

function insertStr(srcStr, start, insertStr) {
    return srcStr.slice(0, start) + insertStr + srcStr.slice(start);
}



/**
 * 弹出消息框
 * @param msg 消息内容
 * @param type 消息框类型（参考bootstrap的alert）
 */
var commonUtil = {
    alert: function (msg, type) {
        if (typeof (type) == "undefined") { // 未传入type则默认为success类型的消息框
            type = "success";
        }
        // 创建bootstrap的alert元素
        var divElement = $("<div></div>").addClass('alert').addClass('alert-' + type).addClass('alert-dismissible');
        var alertWidth = msg.length * 20;
        if (alertWidth < 150) {
            alertWidth = 150;
        }
        var screenWidth = window.innerWidth;
        divElement.css({ // 消息框的定位样式
            "position": "fixed",
            "width": alertWidth,
            "top": "100px",
            "left": (screenWidth - alertWidth) / 2,
            "text-align": "center",
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
};

/**
 * 文件大小换算
 * **/
function fileSizeConver(limit, i) {
    if (i != 1000) {
        i = 1024;
    }
    var size = "";
    if (limit < 0.1 * i) { //如果小于0.1KB转化成B
        size = limit.toFixed(2) + "B";
    } else if (limit < 0.1 * i * i) {//如果小于0.1MB转化成KB
        size = (limit / i).toFixed(2) + "KB";
    } else if (limit < 0.1 * i * i * i) { //如果小于0.1GB转化成MB
        size = (limit / (i * i)).toFixed(2) + "MB";
    } else { //其他转化成GB
        size = (limit / (i * i * i)).toFixed(2) + "GB";
    }
    var sizestr = size + "";
    var len = sizestr.indexOf("\.");
    var dec = sizestr.substr(len + 1, 2);
    if (dec == "00") {//当小数点后为00时 去掉小数部分
        return sizestr.substring(0, len) + sizestr.substr(len + 3, 2);
    }
    return sizestr;
};

function dateStringConver(dateStr) {
    dateStr = insertStr(dateStr, 12, ":");
    dateStr = insertStr(dateStr, 10, ":");
    dateStr = insertStr(dateStr, 8, " ");
    dateStr = insertStr(dateStr, 6, "-");
    dateStr = insertStr(dateStr, 4, "-");
    return dateStr;
};

function insertStr(str, index, insertStr) {
    console.log("str", str);
    return str.slice(0, index) + insertStr + str.slice(index);
};

/*\
|*|
|*|  :: cookies.js ::
|*|
|*|  A complete cookies reader/writer framework with full unicode support.
|*|
|*|  https://developer.mozilla.org/en-US/docs/DOM/document.cookie
|*|
|*|  This framework is released under the GNU Public License, version 3 or later.
|*|  http://www.gnu.org/licenses/gpl-3.0-standalone.html
|*|
|*|  Syntaxes:
|*|
|*|  * docCookies.setItem(name, value[, end[, path[, domain[, secure]]]])
|*|  * docCookies.getItem(name)
|*|  * docCookies.removeItem(name[, path], domain)
|*|  * docCookies.hasItem(name)
|*|  * docCookies.keys()
|*|
\*/
var docCookies = {
    getItem: function (sKey) {
        return decodeURIComponent(document.cookie.replace(new RegExp("(?:(?:^|.*;)\\s*" + encodeURIComponent(sKey).replace(/[-.+*]/g, "\\$&") + "\\s*\\=\\s*([^;]*).*$)|^.*$"), "$1")) || null;
    },
    setItem: function (sKey, sValue, vEnd, sPath, sDomain, bSecure) {
        if (!sKey || /^(?:expires|max\-age|path|domain|secure)$/i.test(sKey)) { return false; }
        var sExpires = "";
        if (vEnd) {
            switch (vEnd.constructor) {
                case Number:
                    sExpires = vEnd === Infinity ? "; expires=Fri, 31 Dec 9999 23:59:59 GMT" : "; max-age=" + vEnd;
                    break;
                case String:
                    sExpires = "; expires=" + vEnd;
                    break;
                case Date:
                    sExpires = "; expires=" + vEnd.toUTCString();
                    break;
            }
        }
        document.cookie = encodeURIComponent(sKey) + "=" + encodeURIComponent(sValue) + sExpires + (sDomain ? "; domain=" + sDomain : "") + (sPath ? "; path=" + sPath : "") + (bSecure ? "; secure" : "");
        return true;
    },
    removeItem: function (sKey, sPath, sDomain) {
        if (!sKey || !this.hasItem(sKey)) { return false; }
        document.cookie = encodeURIComponent(sKey) + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT" + (sDomain ? "; domain=" + sDomain : "") + (sPath ? "; path=" + sPath : "");
        return true;
    },
    hasItem: function (sKey) {
        return (new RegExp("(?:^|;\\s*)" + encodeURIComponent(sKey).replace(/[-.+*]/g, "\\$&") + "\\s*\\=")).test(document.cookie);
    },
    keys: /* optional method: you can safely remove it! */ function () {
        var aKeys = document.cookie.replace(/((?:^|\s*;)[^\=]+)(?=;|$)|^\s*|\s*(?:\=[^;]*)?(?:\1|$)/g, "").split(/\s*(?:\=[^;]*)?;\s*/);
        for (var nIdx = 0; nIdx < aKeys.length; nIdx++) { aKeys[nIdx] = decodeURIComponent(aKeys[nIdx]); }
        return aKeys;
    }
};



var Base64 = {
    encode(str) {
        return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g,
            function toSolidBytes(match, p1) {
                return String.fromCharCode('0x' + p1);
            }));
    },
    decode(str) {
        return decodeURIComponent(atob(str).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
    }
};