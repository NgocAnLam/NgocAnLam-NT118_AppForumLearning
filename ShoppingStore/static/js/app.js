var divLogin = document.getElementById('divLogin');
var divAccount = document.getElementById('divAccount');
var dropdownMenuButton = document.getElementById('dropdownMenuButton');
var btnLogin = document.getElementById('btnLogin');
var btnLogout = document.getElementById('btnLogout');
var divFilter = document.getElementById('filter');
var btnLoginAccount = document.getElementById('btnLoginAccount');

// Initialize functions
function getInfo(){
    fetch("http://127.0.0.1:5000/info")
    .then(response => response.json())
    .then(data => {
        var isLogin = data.isLogin;
        var username = data.username;

        divLogin.hidden = isLogin;
        divAccount.hidden = !isLogin;
        dropdownMenuButton.hidden = !isLogin;
        isLogin ? dropdownMenuButton.textContent = username : btnLogin.textContent = username;
    })
    .catch(error => {console.error('GET request error:', error)});
}

function getCategory(){
    fetch('http://127.0.0.1:5000/category')
    .then(response => response.json())
    .then(data => {
        category = data.categoryList;
        const divCategory = document.querySelector(".category");
        if (divCategory){
            for (let i = 0; i < category.length; i++) {
                const link = document.createElement("a");
                link.classList.add("nav-link", "link-dark");
                link.href = category[i]['name'] + '/' + category[i]['code'];
                const img = document.createElement("img");
                img.id = 'imgCategory';
                img.src = category[i]['icon_url'];
                const linkText = document.createTextNode(category[i]['text']);
                link.appendChild(img);
                link.appendChild(linkText);
                divCategory.appendChild(link);
            }
        }    
    })
    .catch(error => {console.error('GET request error:', error)});
}

function queryStringToObject(queryString) {
    if (!queryString) {return {}}
    queryString = queryString.startsWith('?') ? queryString.slice(1) : queryString;
    return queryString.split('&').reduce((result, pair) => {
        const [key, value] = pair.split('=');
        result[key] = key === 'price' ? value.split(',').map(Number) : isNaN(value) ? value : Number(value);
        return result;
    }, {});
}

function objectToQueryString(obj) {
    const queryString = Object.entries(obj)
        .map(([key, value]) => Array.isArray(value) ? `${key}=${value.join(',')}` : `${key}=${value}`)
        .join('&');
    
    return queryString ? `?${queryString}` : '';
}

function changeValueFromObjMulti(obj, targetKey, targetValue) {
    const keys = Object.keys(obj);
  
    if (keys.includes(targetKey)) {
        var value = obj[targetKey].split(',');
        if (value.includes(targetValue)) {
            value = value.filter(item => item !== targetValue);
            if (value.length > 0){obj[targetKey] = value.join(',')}
            else {delete obj[targetKey]}  
        }
        else { obj[targetKey] = obj[targetKey] + ',' + targetValue}
    } 
    else {obj[targetKey] = targetValue}
    return obj;
}

function changeValueFromObjSingle(obj, targetKey, targetValue) {
    if (!obj.hasOwnProperty(targetKey) || obj[targetKey] !== targetValue) {obj[targetKey] = targetValue};
    return obj;
}

function getFfilterCategory(){
    if (divFilter){
        fetch('http://127.0.0.1:5000/filter/' + window.location.pathname.split('/')[1])
        .then(response => response.json())
        .then(data => {
            for (let i = 0; i < data.length; i++) {
                const title = document.createElement("h6");
                title.className = "titleFilter";
                title.textContent = data[i]['display_name'];
                divFilter.appendChild(title);

                if (data[i]['multi_select'] == false){
                    for (let j = 0; j < data[i]['values'].length; j++){
                        const divValue = document.createElement('div');
                        divValue.className = 'div-value';
                        divValue.textContent = data[i]['values'][j]['display_value'];

                        divValue.onclick = function(){
                            var query_name = data[i]['query_name'];
                            var query_value = data[i]['values'][j]['query_value'];
                            var query_string = window.location.search;
                            var url = window.location.origin + window.location.pathname;

                            var object = queryStringToObject(query_string);
                            var changeValue = changeValueFromObjSingle(object, query_name, query_value);
                            var newUrl = url + objectToQueryString(changeValue);
                            window.location.href = newUrl;
                        };
                        divFilter.appendChild(divValue);
                    }
                }
                else {
                    for (let j = 0; j < data[i]['values'].length; j++){
                        const diplayValue = data[i]['values'][j]['display_value'];
                        const divValue = document.createElement('div');
                        const checkbox = document.createElement('input');
                        checkbox.type = 'checkbox';
                        checkbox.value = diplayValue;
                        checkbox.id = diplayValue;
                        checkbox.name = diplayValue;
                        checkbox.style.margin = "10px 10px 5px 22px";

                        const label = document.createElement('label');
                        label.htmlFor = diplayValue;
                        label.textContent = diplayValue;

                        var query_string = window.location.search;
                        var object = queryStringToObject(query_string);
                        var brandValueString = object.brand;
                        
                        divValue.onclick = function(){
                            var query_name = data[i]['query_name'];
                            var query_value = diplayValue;
                            var url = window.location.origin + window.location.pathname;

                            var changeValue = changeValueFromObjMulti(object, query_name, query_value);
                            var newUrl = url + objectToQueryString(changeValue);
                            window.location.href = newUrl;
                        }

                        if (brandValueString !== undefined) {checkbox.checked = brandValueString.split(',').includes(checkbox.value)} 
                        else {checkbox.checked = false}     
                        divValue.appendChild(checkbox);
                        divValue.appendChild(label);
                        divFilter.appendChild(divValue);
                    }
                }
            }
        })
        .catch(error => {console.error('GET request error:', error)});
    }
}

function changePage(btnPage){
    var query_name = 'page';
    var query_value = btnPage.textContent;
    var query_string = window.location.search;
    var url = window.location.origin + window.location.pathname;

    var object = queryStringToObject(query_string);
    var changeValue = changeValueFromObjSingle(object, query_name, query_value);
    var newUrl = url + objectToQueryString(changeValue);
    window.location.href = newUrl;
}

// Account functions
function logout(){
    if (btnLogout){
        btnLogout.addEventListener('click', function() {
            fetch('http://127.0.0.1:5000/logout', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
            })
            .then(response => response.json())
            .then(data => {})
            .catch(error => {console.error('GET request error:', error)});
        });
    }
}

function login(){
    if (btnLogin){
        btnLogin.addEventListener('click', function() {
            window.location.href = "http://127.0.0.1:5000/login";
        });
    }
}

btnLoginAccount.onclick = function goShoppingCart(){
    fetch("http://127.0.0.1:5000/info")
    .then(response => response.json())
    .then(data => {
        var isLogin = data.isLogin;
        if (isLogin) { window.location.href = "http://127.0.0.1:5000/shoppingCart"}
        else { window.location.href = "http://127.0.0.1:5000/login";}
    })
    .catch(error => {console.error('GET request error:', error)});
}



logout()
login()
getInfo()
getCategory()
getFfilterCategory()


var userAccount = document.getElementById('userAccount');
var userOrder = document.getElementById('userOrder');

userAccount.href = '/user/account';
userOrder.href = '/user/order';


var btnSearch = document.getElementById('btnSearch');
var inputSearch = document.getElementById('inputSearch');
btnSearch.onclick = function search(){
    var inputValue = inputSearch.value;
    
    if (inputValue.length > 0){
        window.location.href = "http://127.0.0.1:5000/search?q=" + inputValue;
    }
}