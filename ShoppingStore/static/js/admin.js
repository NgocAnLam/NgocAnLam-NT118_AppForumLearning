// Time processing ----------------------------------------------------------------
var timeNow = document.getElementById('timeNow');
var helloAdmin = document.getElementById('helloAdmin');

function getTime(){
    date = new Date();
    var year = date.getFullYear();
    var month = formatTime(date.getMonth() + 1);
    var day = formatTime(date.getDate());
    var hour = formatTime(date.getHours());
    var minute = formatTime(date.getMinutes());
    var second = formatTime(date.getSeconds());
    return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
}

function formatTime(time){
    time = time.toString();
    if (time.length == 1){time = "0" + time}
    return time;
}

function helloTime(hour){
    if (hour >= 0 && hour < 12) return 'Chào buổi sáng Admin !!!'
    if (hour >= 12 && hour < 18) return 'Chào buổi chiều Admin !!!'
    if (hour >= 18 && hour < 24) return 'Chào buổi tối Admin !!!'
}

function updateTime(){
    timeNow.textContent = getTime();
    helloAdmin.textContent = helloTime(new Date().getHours());
}
setInterval(updateTime, 1000);

// logout processing ----------------------------------------------------
var logout = document.getElementById('logout');
logout.onclick = function(){
    fetch('http://127.0.0.1:5000/admin/logout', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
    })
    .then(response => response.json())
    .then(data => {window.location.href = 'http://127.0.0.1:5000/admin/login'})
    .catch(error => {console.error('GET request error:', error)});
}

// Open Categories Processing ----------------------------------------------------------------
function openCategory(option) {
    var x = document.getElementsByClassName("menu");
    for (var i = 0; i < x.length; i++) {x[i].style.display = "none"} 
    document.getElementById(option).style.display = "block";
    
    if(option == 'customer'){customerProcess()}
    if(option == 'ordersProcessing'){orderProcess()}
    if(option == 'ordersTransit'){orderTransit()}
}

// Customer ----------------------------------------------------------------
var customerSideBar = document.getElementById("customerSideBar");
function customerProcess(){
    fetch('http://127.0.0.1:5000/admin/customer')
    .then(response => response.json())
    .then(data => {
        var infoCustomer = document.getElementById("infoCustomer");
        infoCustomer.innerHTML = "";
        var userList = data.userList;
        console.log(data.userList);

        for (var i = 0; i < userList.length; i++) {
            var linkUser = document.createElement("a");
            linkUser.href = "http://127.0.0.1:5000/admin/customer/" + userList[i]['ID'];
            var row = document.createElement("div");
            row.className = "d-flex flex-row";

            var totalCost = 0;
            var orderList = userList[i]['orderList'];
            if (orderList.length > 0) {
                for (var j = 0; j < orderList.length; j++){
                    Object.keys(orderList[j]).forEach(key => {
                        for (var k = 0; k < orderList[j][key].length; k++){
                            totalCost += orderList[j][key][k]['total'];
                        }
                    });
                }
            }

            function createCell(id, content, minWidth, textAlign) {
                var cell = document.createElement("p");
                cell.id = id;
                cell.textContent = content;
                cell.style.minWidth = minWidth;
                cell.style.textAlign = textAlign || "left";
                row.appendChild(cell);
            }
            createCell("code", userList[i]['ID'], "100px");
            createCell("email", userList[i]['email'], "300px");
            createCell("username", userList[i]['username'], "200px");
            createCell("level", userList[i]['level'], "100px");
            createCell("totalCost", numberWithDots(totalCost) + 'đ', "200px", "right");
            createCell("orderNum", userList[i]['orderList'].length, "200px", "right");
            linkUser.appendChild(row);
            infoCustomer.appendChild(linkUser);
        }
    })
    .catch(error => {console.error('GET request error:', error)});
}

function orderProcess(){
    fetch('http://127.0.0.1:5000/admin/orderProcess')
    .then(response => response.json())
    .then(data => {
        console.log(data.userList);
        var infoOrder = document.getElementById("infoOrderProcessing");
        infoOrder.innerHTML = "";
        var userList = data.userList;
        
        for (var i = 0; i < userList.length; i++) {
            var rowOrder = document.createElement("div");
            rowOrder.className = "rowOrder";
            rowOrder.style.margin = "10px 0px";
            rowOrder.style.padding = "10px";
            rowOrder.style.backgroundColor = "#ffffff";
            rowOrder.style.border = "1px solid #ffffff";
            rowOrder.style.borderRadius = "10px";
            
            var infoOrderDetail = document.createElement("div");
            infoOrder.className = "infoOrder";

            var orderID = document.createElement("div");
            orderID.className = "orderID";
            orderID.textContent = "OrderID: " + userList[i]["orderID"];
            orderID.style.minWidth = "1020px"; 

            var btnConfirmProcessing = document.createElement("div");
            btnConfirmProcessing.className = "btnConfirmProcessing";
            btnConfirmProcessing.textContent = "Confirm Processing";

            var confirm = document.createElement("div");
            confirm.className = "d-flex flex-row confirm";
            confirm.appendChild(orderID);
            confirm.appendChild(btnConfirmProcessing);

            var buyTime = document.createElement("div");
            buyTime.className = "buyTime";
            buyTime.textContent = "Buy Time: " + userList[i]["buyTime"];

            var infoCustomer = document.createElement("div");
            infoCustomer.className = "infoCustomer";
            var customerID = "CustomerID: " + userList[i]["userID"];
            var address = "Address: " + userList[i]["address"];
            var phone = "Phone: " + userList[i]["phone"];
            var email = "Email: " + userList[i]["email"];
            infoCustomer.textContent = customerID + ' | ' +address + ' | ' + phone + ' | ' + email;
            infoCustomer.style.marginBottom = "40px";

            infoOrderDetail.appendChild(confirm);
            infoOrderDetail.appendChild(buyTime);
            infoOrderDetail.appendChild(infoCustomer);

            var detail = userList[i]['detail'];
            for (let j = 0; j < detail.length; j++) {
                var detailOrder = document.createElement("div");
                detailOrder.className = "d-flex flex-row detailOrder";

                function createCell(id, content, width, textAlign, mLeft, mRight) {
                    var cell = document.createElement("p");
                    cell.id = id;
                    cell.textContent = content;
                    cell.style.minWidth = width;
                    cell.style.maxWidth = width;
                    cell.style.marginLeft = mLeft || "0px";
                    cell.style.marginRight = mRight || "0px";
                    cell.style.textAlign = textAlign || "left";
                    detailOrder.appendChild(cell);
                }

                createCell("id", detail[j]['id'], "100px");
                createCell("product", detail[j]['name'], "950px");
                createCell("quantity", detail[j]['quantity'], "50px", "right");
                createCell("total", numberWithDots(detail[j]['total']) + 'đ', "100px", "right");
                infoOrderDetail.appendChild(detailOrder);
            }
            rowOrder.appendChild(infoOrderDetail);
            infoOrder.appendChild(rowOrder);
        }

        var btnConfirmProcessing = document.querySelectorAll('.btnConfirmProcessing');
        var orderIDList = document.querySelectorAll('.orderID');
        var customerIDList = document.querySelectorAll('.infoCustomer');

        for (var i = 0; i < btnConfirmProcessing.length; i++) {
            btnConfirmProcessing[i].addEventListener("click", (function(index) {
                return function() {
                    var orderIDPost = orderIDList[index].textContent.split(" ")[1];
                    var customerIDPost = customerIDList[index].textContent.split(" ")[1];
                    
                    fetch("http://127.0.0.1:5000/admin/order/confirmProcess/" + customerIDPost + '/' + orderIDPost, {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                    })
                    .then(response => response.json())
                    .then(data => {console.log(data)})
                    .catch(error => {console.error('GET request error:', error)});
                };
            })(i));
        }
    })
    .catch(error => {console.error('GET request error:', error)});
}

function orderTransit(){
    fetch('http://127.0.0.1:5000/admin/orderTransit')
    .then(response => response.json())
    .then(data => {
        console.log(data.userList);
        var infoOrder = document.getElementById("infoOrderTransit");
        infoOrder.innerHTML = "";
        var userList = data.userList;
        
        for (var i = 0; i < userList.length; i++) {
            var rowOrder = document.createElement("div");
            rowOrder.className = "rowOrder";
            rowOrder.style.margin = "10px 0px";
            rowOrder.style.padding = "10px";
            rowOrder.style.backgroundColor = "#ffffff";
            rowOrder.style.border = "1px solid #ffffff";
            rowOrder.style.borderRadius = "10px";
            
            var infoOrderDetail = document.createElement("div");
            infoOrder.className = "infoOrder";

            var orderID = document.createElement("div");
            orderID.className = "orderID";
            orderID.textContent = "OrderID: " + userList[i]["orderID"];
            orderID.style.minWidth = "1020px"; 

            var btnConfirmTransit = document.createElement("div");
            btnConfirmTransit.className = "btnConfirmTransit";
            btnConfirmTransit.textContent = "Confirm Transit";

            var confirm = document.createElement("div");
            confirm.className = "d-flex flex-row confirm";
            confirm.appendChild(orderID);
            confirm.appendChild(btnConfirmTransit);

            var buyTime = document.createElement("div");
            buyTime.className = "buyTime";
            buyTime.textContent = "Buy Time: " + userList[i]["buyTime"];

            var infoCustomer = document.createElement("div");
            infoCustomer.className = "infoCustomer";
            var customerID = "CustomerID: " + userList[i]["userID"];
            var address = "Address: " + userList[i]["address"];
            var phone = "Phone: " + userList[i]["phone"];
            var email = "Email: " + userList[i]["email"];
            infoCustomer.textContent = customerID + ' | ' +address + ' | ' + phone + ' | ' + email;
            infoCustomer.style.marginBottom = "40px";

            infoOrderDetail.appendChild(confirm);
            infoOrderDetail.appendChild(buyTime);
            infoOrderDetail.appendChild(infoCustomer);

            var detail = userList[i]['detail'];
            for (let j = 0; j < detail.length; j++) {
                var detailOrder = document.createElement("div");
                detailOrder.className = "d-flex flex-row detailOrder";

                function createCell(id, content, width, textAlign, mLeft, mRight) {
                    var cell = document.createElement("p");
                    cell.id = id;
                    cell.textContent = content;
                    cell.style.minWidth = width;
                    cell.style.maxWidth = width;
                    cell.style.marginLeft = mLeft || "0px";
                    cell.style.marginRight = mRight || "0px";
                    cell.style.textAlign = textAlign || "left";
                    detailOrder.appendChild(cell);
                }

                createCell("id", detail[j]['id'], "100px");
                createCell("product", detail[j]['name'], "950px");
                createCell("quantity", detail[j]['quantity'], "50px", "right");
                createCell("total", numberWithDots(detail[j]['total']) + 'đ', "100px", "right");
                infoOrderDetail.appendChild(detailOrder);
            }
            rowOrder.appendChild(infoOrderDetail);
            infoOrder.appendChild(rowOrder);
        }

        var btnConfirmTransit = document.querySelectorAll('.btnConfirmTransit');
        var orderIDList = document.querySelectorAll('.orderID');
        var customerIDList = document.querySelectorAll('.infoCustomer');

        for (var i = 0; i < btnConfirmTransit.length; i++) {
            btnConfirmTransit[i].addEventListener("click", (function(index) {
                return function() {
                    var orderIDPost = orderIDList[index].textContent.split(" ")[1];
                    var customerIDPost = customerIDList[index].textContent.split(" ")[1];
                    
                    fetch("http://127.0.0.1:5000/admin/order/confirmTransit/" + customerIDPost + '/' + orderIDPost, {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                    })
                    .then(response => response.json())
                    .then(data => {console.log(data)})
                    .catch(error => {console.error('GET request error:', error)});
                };
            })(i));
        }
    })
    .catch(error => {console.error('GET request error:', error)});
}



function numberWithDots(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function numberWithoutDots(number) {
    return parseInt(number.replace(/\./g, ''), 10);
}

