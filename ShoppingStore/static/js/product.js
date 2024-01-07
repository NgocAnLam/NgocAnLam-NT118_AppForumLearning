var btnMinus = document.getElementById('minus');
var btnPlus = document.getElementById('plus');
var divQuantityProduct = document.getElementById('quantity');
var itemPrice = document.getElementById('itemPrice');
var calculatePrice = document.getElementById('calculatePrice');
var divDescription = document.getElementById('divDescription');
var btnAddToCart = document.getElementById('btnAddToCart');

// Initial Function
function numberWithDots(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function numberWithoutDots(number) {
    return parseInt(number.replace(/\./g, ''), 10);
}

// Initial Processing
itemPrice.textContent = numberWithDots(itemPrice.textContent);
calculatePrice.textContent = itemPrice.textContent;

divDescription.textContent = divDescription.textContent.replace(new RegExp('width="750"', 'g'), 'width="100%"')
divDescription.textContent = divDescription.textContent.replace(/height="\d+"/g, 'height="auto"');
divDescription.innerHTML = divDescription.textContent;

// add event handlers
btnMinus.addEventListener("click", function() {
    var currentQuantity = parseInt(divQuantityProduct.textContent) - 1;
    var unitPrice = numberWithoutDots(itemPrice.textContent);
    if (currentQuantity > 0){
        divQuantityProduct.textContent = currentQuantity.toString();
        calculatePrice.textContent = numberWithDots(unitPrice * currentQuantity) + 'đ';
    }
});

btnPlus.addEventListener("click", function() {
    var currentQuantity = parseInt(divQuantityProduct.textContent) + 1;
    var unitPrice = numberWithoutDots(itemPrice.textContent);
    divQuantityProduct.textContent = currentQuantity.toString();
    calculatePrice.textContent = numberWithDots(unitPrice * currentQuantity) + 'đ';
});

btnAddToCart.addEventListener("click", function() {
    var divNotificationAddToCart = document.createElement("div");
    divNotificationAddToCart.id = "notificationAddToCart";
    divNotificationAddToCart.textContent = "Đã thêm vào giỏ hàng";
    divNotificationAddToCart.style.marginTop = "30px";
    divNotificationAddToCart.style.padding = "20px";
    divNotificationAddToCart.style.width = "300px";
    divNotificationAddToCart.style.height = "fit-content";
    divNotificationAddToCart.style.backgroundColor = "#81cf8c";
    btnAddToCart.appendChild(divNotificationAddToCart);
    setTimeout(function (){btnAddToCart.removeChild(divNotificationAddToCart)}, 3000);

    var pathnameURL = (window.location.pathname).split('-');
    var productID = pathnameURL[pathnameURL.length - 1].slice(1);

    fetch("http://127.0.0.1:5000/shoppingCart", {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            id: productID, 
            quantity: divQuantityProduct.textContent,
        })
    })
    .then(response => response.json())
    .then(data => {console.log(data)})
    .catch(error => {console.error('GET request error:', error)});
});


var infoUser = document.getElementById('infoUser');
fetch("http://127.0.0.1:5000/info")
.then(response => response.json())
.then(data => {
    console.log(data);
    infoUser.textContent = data.username;
})
.catch(error => {console.error('GET request error:', error)});


var minusRating = document.getElementById('minusRating');
var plusRating = document.getElementById('plusRating');
var scoreRating = document.getElementById('scoreRating');
var postUser = document.getElementById('postUser');
var writeUser = document.getElementById('writeUser');

minusRating.onclick = function () {
    score = parseInt(scoreRating.textContent);
    if (score > 1){scoreRating.textContent =  score - 1}
}

plusRating.onclick = function () {
    score = parseInt(scoreRating.textContent);
    if (score < 5){scoreRating.textContent = score + 1}
}

postUser.onclick = function () {
    fetch("http://127.0.0.1:5000/commentUser", {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            textComment: writeUser.value,
            scoreComment: scoreRating.textContent,
            timeComment: getDateTime()
        })
    })
    .then(response => response.json())
    .then(data => {
        console.log(data)
    })
    .catch(error => {console.error('GET request error:', error)});
}

function getDateTime(){
    var currentDate = new Date();
    var year = currentDate.getFullYear();
    var month = currentDate.getMonth()+1;
    var day = currentDate.getDate();
    var hours = currentDate.getHours();
    var minutes = currentDate.getMinutes();
    var seconds = currentDate.getSeconds();
    var milliseconds = currentDate.getMilliseconds();
    let Time = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds + ':' + milliseconds;
    return Time;
}