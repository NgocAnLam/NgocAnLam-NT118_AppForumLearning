var price = document.getElementsByClassName('price');

function numberWithDots(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

for (var i = 0; i < price.length; i++) {
    price[i].textContent = numberWithDots(price[i].textContent) + 'đ';
}

var inputSearch = document.getElementById('inputSearch');
var infoSearch = document.getElementById('infoSearch');

inputSearch.value = infoSearch.textContent.split('"')[1];