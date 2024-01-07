var btnMinus = document.querySelectorAll('.btnMinusProd');
var btnPlus = document.querySelectorAll('.btnPlusProd');
var divProduct = document.querySelectorAll('.btnQuantityProd');
var itemPrice = document.querySelectorAll('.itemPriceShoppingCart');
var calculatePrice = document.querySelectorAll('.calculatePriceShoppingCart');
var totalPrice = document.getElementById('totalPrice');
var checkBoxShoppingCart = document.querySelectorAll('.checkBoxShoppingCart');
var checkBoxAll = document.querySelector('.checkBoxAll');
var removeItem = document.querySelectorAll('.removeItem');
var removeAll = document.querySelector('.removeAll');
var linkProduct = document.querySelectorAll('.linkProduct');
var infoCustomer = document.getElementById('infoCustomer');
var arrayItemName = document.querySelectorAll('.itemNameShoppingCart');
var arrayItemquantity = document.querySelectorAll('.btnQuantityProd');
// Initial Function
function numberWithDots(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function numberWithoutDots(number) {
    return parseInt(number.replace(/\./g, ''), 10);
}

// Initial Processing
for (let i = 0; i < itemPrice.length; i++) {
    itemPrice[i].textContent = numberWithDots(itemPrice[i].textContent);
    calculatePrice[i].textContent = numberWithDots(calculatePrice[i].textContent);  
}

function customer(){
    fetch("http://127.0.0.1:5000/infoCustomer")
    .then(response => response.json())
    .then(data => {
        console.log(data);
        var user = data.user;

        var title = document.createElement("h4");
        title.textContent = 'Giao tới';
        infoCustomer.appendChild(title);

        var name = document.createElement("p");
        name.textContent = 'Tên người nhận: ' + user.username;
        infoCustomer.appendChild(name);

        var phone = document.createElement("p");
        phone.textContent = 'Số điện thoại: ' + user.phone;
        infoCustomer.appendChild(phone);

        var address = document.createElement("p");
        address.textContent = 'Địa chỉ: ' + user.address;
        infoCustomer.appendChild(address);
    })
    .catch(error => {console.error('GET request error:', error)});
}

// add event handlers
for (let i = 0; i < btnMinus.length; i++){
    btnMinus[i].addEventListener("click", function() {
        var currentQuantity = parseInt(divProduct[i].textContent) - 1;
        var unitPrice = numberWithoutDots(itemPrice[i].textContent);
        if (currentQuantity > 0){
            divProduct[i].textContent = currentQuantity.toString();
            calculatePrice[i].textContent = numberWithDots(unitPrice * currentQuantity) + 'đ';
            var productID = (linkProduct[i].href.split('-').pop()).slice(1);

            fetch("http://127.0.0.1:5000/shoppingCart", {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    id: productID, 
                    quantity: -1,
                })
            })
            .then(response => response.json())
            .then(data => {console.log(data)})
            .catch(error => {console.error('GET request error:', error)});
        }
    });
}

for (let j = 0; j < btnPlus.length; j++){
    btnPlus[j].addEventListener("click", function() {
        var currentQuantity = parseInt(divProduct[j].textContent) + 1;
        var unitPrice = numberWithoutDots(itemPrice[j].textContent);
        divProduct[j].textContent = currentQuantity.toString();
        calculatePrice[j].textContent = numberWithDots(unitPrice * currentQuantity) + 'đ';

        var productID = (linkProduct[j].href.split('-').pop()).slice(1);

        fetch("http://127.0.0.1:5000/shoppingCart", {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                id: productID, 
                quantity: 1,
            })
        })
        .then(response => response.json())
        .then(data => {console.log(data)})
        .catch(error => {console.error('GET request error:', error)});
    });
}

checkBoxAll.addEventListener("change", function(){
    if(checkBoxAll.checked){
        for (let i = 0; i < checkBoxShoppingCart.length; i++){
            checkBoxShoppingCart[i].checked = true;

            var total = 0;
            for(let i = 0; i < checkBoxShoppingCart.length; i++){
                if(checkBoxShoppingCart[i].checked){
                    var sumPriceOfItem = parseInt(numberWithoutDots((calculatePrice[i].textContent).replace('đ', '')));
                    total += sumPriceOfItem;
                }
            }
            totalPrice.textContent = numberWithDots(total) + 'đ';
        }
    }
    else {
        for (let i = 0; i < checkBoxShoppingCart.length; i++){
            checkBoxShoppingCart[i].checked = false;

            var total = 0;
            for(let i = 0; i < checkBoxShoppingCart.length; i++){
                if(checkBoxShoppingCart[i].checked){
                    var sumPriceOfItem = parseInt(numberWithoutDots((calculatePrice[i].textContent).replace('đ', '')));
                    total += sumPriceOfItem;
                }
            }
            totalPrice.textContent = numberWithDots(total) + 'đ';
        }
    }
});

checkBoxShoppingCart.forEach(checkbox => {
    checkbox.addEventListener('change', function(){
        const allChecked = Array.from(checkBoxShoppingCart).every(checkbox => checkbox.checked);
        checkBoxAll.checked = allChecked;
    });
});

window.addEventListener("click", function(){
    var total = 0;
    for(let i = 0; i < checkBoxShoppingCart.length; i++){
        if(checkBoxShoppingCart[i].checked){
            var sumPriceOfItem = parseInt(numberWithoutDots((calculatePrice[i].textContent).replace('đ', '')));
            total += sumPriceOfItem;
        }
    }
    totalPrice.textContent = numberWithDots(total) + 'đ';
});

removeAll.addEventListener("click", function(){
    fetch("http://127.0.0.1:5000/shoppingCart", {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json'},
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);
        window.location.href = '/shoppingCart';
    })
    .catch(error => {console.error('GET request error:', error)});
});

for (let i = 0; i < removeItem.length; i++){
    removeItem[i].addEventListener("click", function() {
        var productID = (linkProduct[i].href.split('-').pop()).slice(1);
        var productQuantity = parseInt(divProduct[i].textContent);
        fetch("http://127.0.0.1:5000/shoppingCart", {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                id: productID, 
                quantity: -productQuantity,
            })
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            window.location.href = './shoppingCart'
        })
        .catch(error => {console.error('GET request error:', error)});
    })
}

var btnBuy = document.getElementById('btnBuy');

function getItemsToBuy(){
    const itemsArray = [];
    for(let i = 0; i < checkBoxShoppingCart.length; i++){
        if(checkBoxShoppingCart[i].checked){
            itemsArray.push(arrayItemName[i].textContent);
        }
    }
    return itemsArray;
}


btnBuy.onclick = function buy(){
    fetch("http://127.0.0.1:5000/buyItems", {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({info : getItemsToBuy()})
    })
    .then(response => response.json())
    .then(data => {
        window.location.href = '/shoppingCart'
    })
    .catch(error => {console.error('GET request error:', error)});
}

// call function
customer()