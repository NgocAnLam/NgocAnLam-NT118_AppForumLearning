var date = document.getElementsByClassName('date');
var price = document.getElementsByClassName('price');


function numberWithDots(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

for (var i = 0; i < date.length; i++) {
    date[i].textContent = date[i].textContent.split('.')[0];
}

for (var i = 0; i < price.length; i++) {
    price[i].textContent = numberWithDots(price[i].textContent) + 'đ';
}
