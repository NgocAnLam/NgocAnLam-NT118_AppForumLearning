var info = document.getElementById('info');
var back = document.getElementById("back");
var userID = window.location.pathname.split("/")[3];

window.addEventListener("load", (event) => {
    getInfo(userID);
});

function numberWithDots(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function numberWithoutDots(number) {
    return parseInt(number.replace(/\./g, ''), 10);
}

function formatDate(dateString) {
    var dateObject = new Date(dateString);
    var year = dateObject.getUTCFullYear();
    var month = ("0" + (dateObject.getUTCMonth() + 1)).slice(-2);
    var day = ("0" + dateObject.getUTCDate()).slice(-2);
    var hours = ("0" + dateObject.getUTCHours()).slice(-2);
    var minutes = ("0" + dateObject.getUTCMinutes()).slice(-2);
    var seconds = ("0" + dateObject.getUTCSeconds()).slice(-2);
    return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
}

back.onclick = function (){window.location.href = "http://127.0.0.1:5000/admin"}

function openCategory(option) {
    var x = document.getElementsByClassName("option");
    for (var i = 0; i < x.length; i++) {x[i].style.display = "none"}
    document.getElementById(option).style.display = "block";
    
    if(option == 'info'){getInfo(userID)}
    if(option == 'confirmed'){getOrderConfirmed(userID)}
    if(option == 'processing'){getOrderProcessing(userID)}
    if(option == 'transit'){getOrderTransit(userID)}
}

function getInfo(userID) {
    fetch("http://127.0.0.1:5000/admin/customer/" + userID + "/info")
    .then(response => response.json())
    .then(data => {
        var info = data.data;
        var currentTime = new Date();
        var createdTime = new Date(Date.parse(info['createdTime']));
        var loginTime = new Date(Date.parse(info['loginTime']));
        createdTime.setHours(createdTime.getHours() - 7).toString();
        var timeDiffCreate = convertMilliseconds(currentTime.getTime() - createdTime.getTime());
    
        if (loginTime == 'Invalid Date'){var loginTimeString = 'Tài khoản chưa đăng nhập lần nào'}
        else {
            loginTime.setHours(loginTime.getHours() - 7).toString();
            var timeDiffLogin = convertMilliseconds(currentTime.getTime() - loginTime.getTime());
            var loginTimeString = info['loginTime'] + ' (' + timeDiffLogin + ')';
        }
        
        var detailInfo = document.getElementById("detailInfo");
        detailInfo.innerHTML = "";

        function createCell(id, title, content, textAlign) {
            var cell = document.createElement("p");
            var titleCell = document.createElement("p");
            var contentCell = document.createElement("p");
            cell.id = id;
            cell.className = "d-flex flex-row";

            titleCell.textContent = title;
            titleCell.style.minWidth = '200px';
            titleCell.style.marginBottom = '0px';
            contentCell.textContent = content;
            contentCell.style.textAlign = textAlign || "left";
            contentCell.style.marginBottom = '0px';
            cell.appendChild(titleCell);
            cell.appendChild(contentCell);
            detailInfo.appendChild(cell);
        }

        createCell('ID', 'User ID: ', info['ID']);
        createCell('email', 'Email: ', info['email']);
        createCell('username', 'Username: ', info['username']);
        createCell('level', 'Level: ', info['level']);
        createCell('phone', 'Phone: ', info['phone']);
        createCell('address', 'Address: ', info['address']);
        createCell('createTime', 'Created Time: ', info['createdTime'] + ' (' + timeDiffCreate + ')');
        createCell('loginTime', 'Login Time: ',  loginTimeString);
        createCell('countOrder', 'Order Number: ', info['orderList'].length);
    })
    .catch(error => {console.error('GET request error:', error)});
}

function getOrderConfirmed(){
    fetch("http://127.0.0.1:5000/admin/customer/" + userID + "/order/confirmed")
    .then(response => response.json())
    .then(data => {
        var dataConfirmed = data.data;
        var orderConfirmed = document.getElementById('orderConfirmed');
        var sumOrder = document.getElementById("sumOrder");
        var numOrder = document.getElementById("numOrder");
        var avgOrder = document.getElementById("avgOrder");

        var sumOfOrder = 0;
        orderConfirmed.innerHTML = "";

        for (var i = 0; i < dataConfirmed.length; i++){
            var cell = document.createElement('div');
            var detail = dataConfirmed[i].detail;
            for (var j = 0; j < detail.length; j++){
                var row = document.createElement("div");
                row.className = "d-flex flex-row";

                function createCell(className, content, width, textAlign) {
                    var cellContent = document.createElement("p");
                    cellContent.className = className;
                    cellContent.textContent = content;
                    cellContent.style.minWidth = width;
                    cellContent.style.maxWidth = width;
                    cellContent.style.textAlign = textAlign || "left";
                    cellContent.style.paddingRight = "20px";
                    row.appendChild(cellContent);
                }
                
                createCell("productID", detail[j]['id'], '100px')
                createCell("productName", detail[j]['name'], '500px');
                createCell("productBrand", detail[j]['brand'], '150px');
                createCell("productPrice", numberWithDots(detail[j]['price']) + 'đ', '100px', "right");
                createCell("productQuantity", detail[j]['quantity'], '100px', "right");
                createCell("productTotal", numberWithDots(detail[j]['total']) + 'đ', '150px', "right");
                createCell("productDate", formatDate(dataConfirmed[i]['buyTime']), '200px');
                cell.appendChild(row);
                sumOfOrder += detail[j]['total'];
                orderConfirmed.appendChild(cell);
            }
        }
        
        sumOrder.textContent = 'Tổng tiền mua hàng: ' + numberWithDots(sumOfOrder) + 'đ';
        numOrder.textContent = 'Tổng số đơn hàng: ' + dataConfirmed.length;
        if (dataConfirmed.length == 0){
            avgOrder.textContent = 'Trung bình mỗi đơn hàng: 0đ';
        }
        else {
            avgOrder.textContent = 'Trung bình mỗi đơn hàng: ' + numberWithDots(sumOfOrder/dataConfirmed.length) + 'đ';
        }
    })
    .catch(error => {console.error('GET request error:', error)});
}

function getOrderProcessing(){
    fetch("http://127.0.0.1:5000/admin/customer/" + userID + "/order/processing")
    .then(response => response.json())
    .then(data => {
        var dataProcessing = data.data;
        var orderProcessing = document.getElementById('orderProcessing');
        var sumOrder = document.getElementById("sumOrder");
        var numOrder = document.getElementById("numOrder");
        var avgOrder = document.getElementById("avgOrder");
        orderProcessing.innerHTML = "";
        var sumOfOrder = 0;
        
        for (var i = 0; i < dataProcessing.length; i++){
            var cell = document.createElement('div');
            var detail = dataProcessing[i].detail;
            for (var j = 0; j < detail.length; j++){
                var row = document.createElement("div");
                row.className = "d-flex flex-row";

                function createCell(className, content, width, textAlign) {
                    var cellContent = document.createElement("p");
                    cellContent.className = className;
                    cellContent.textContent = content;
                    cellContent.style.minWidth = width;
                    cellContent.style.maxWidth = width;
                    cellContent.style.textAlign = textAlign || "left";
                    cellContent.style.paddingRight = "20px";
                    row.appendChild(cellContent);
                }
                
                createCell("productID", detail[j]['id'], '100px')
                createCell("productName", detail[j]['name'], '500px');
                createCell("productBrand", detail[j]['brand'], '150px');
                createCell("productPrice", numberWithDots(detail[j]['price']) + 'đ', '100px', "right");
                createCell("productQuantity", detail[j]['quantity'], '100px', "right");
                createCell("productTotal", numberWithDots(detail[j]['total']) + 'đ', '150px', "right");
                createCell("productDate", formatDate(dataProcessing[i]['buyTime']), '200px');
                cell.appendChild(row);
                sumOfOrder += detail[j]['total'];
                orderProcessing.appendChild(cell);
            }
        }
        sumOrder.textContent = 'Tổng tiền mua hàng: ' + numberWithDots(sumOfOrder) + 'đ';
        numOrder.textContent = 'Tổng số đơn hàng: ' + dataProcessing.length;
        if (dataProcessing.length == 0){
            avgOrder.textContent = 'Trung bình mỗi đơn hàng: 0đ';
        }
        else {
            avgOrder.textContent = 'Trung bình mỗi đơn hàng: ' + numberWithDots(sumOfOrder/dataProcessing.length) + 'đ';
        }
    })
    .catch(error => {console.error('GET request error:', error)});
}

function getOrderTransit(){
    fetch("http://127.0.0.1:5000/admin/customer/" + userID + "/order/transit")
    .then(response => response.json())
    .then(data => {
        var dataTransit = data.data;
        var orderTransit = document.getElementById('orderTransit');
        orderTransit.innerHTML = "";
        var sumOfOrder = 0;
        
        for (var i = 0; i < dataTransit.length; i++){
            var cell = document.createElement('div');
            var detail = dataTransit[i].detail;
            for (var j = 0; j < detail.length; j++){
                var row = document.createElement("div");
                row.className = "d-flex flex-row";

                function createCell(className, content, width, textAlign) {
                    var cellContent = document.createElement("p");
                    cellContent.className = className;
                    cellContent.textContent = content;
                    cellContent.style.minWidth = width;
                    cellContent.style.maxWidth = width;
                    cellContent.style.textAlign = textAlign || "left";
                    cellContent.style.paddingRight = "20px";
                    row.appendChild(cellContent);
                }
                
                createCell("productID", detail[j]['id'], '100px')
                createCell("productName", detail[j]['name'], '500px');
                createCell("productBrand", detail[j]['brand'], '150px');
                createCell("productPrice", numberWithDots(detail[j]['price']) + 'đ', '100px', "right");
                createCell("productQuantity", detail[j]['quantity'], '100px', "right");
                createCell("productTotal", numberWithDots(detail[j]['total']) + 'đ', '150px', "right");
                createCell("productDate", formatDate(dataTransit[i]['buyTime']), '200px');
                cell.appendChild(row);
                sumOfOrder += detail[j]['total'];
                orderTransit.appendChild(cell);
            }
        }
    })
    .catch(error => {console.error('GET request error:', error)});
}


function convertMilliseconds(milliseconds) {
    var seconds = Math.floor(milliseconds / 1000);
    var minutes = Math.floor(seconds / 60);
    var hours = Math.floor(minutes / 60);
    var days = Math.floor(hours / 24);
    var years = Math.floor(days / 365);
    var months = Math.floor((days % 365) / 30);

    seconds %= 60;
    minutes %= 60;
    hours %= 24;
    days %= 365;

    var result = "";
    if (years > 0) result += years + " years ";
    if (months > 0) result += months + " months ";
    if (days > 0) result += days + " days ";
    if (hours > 0) result += hours + " hours ";
    if (minutes > 0) result += minutes + " minutes ";
    if (seconds > 0) result += seconds + " seconds ";

    return result.trim();
}