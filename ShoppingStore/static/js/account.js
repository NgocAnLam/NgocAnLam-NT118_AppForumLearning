const host = "https://provinces.open-api.vn/api/";
const provinceDropdown = document.getElementById('province');
const districtDropdown = document.getElementById('district');
const wardDropdown = document.getElementById('ward');

// Process input dropdown province, district, ward 

async function callAPI(api) {
    try {
        const response = await fetch(api);
        const data = await response.json();
        renderData(data, "province", "Chọn Tỉnh/Thành Phố");
    } 
    catch (error) {console.error('Error:', error)}
}

async function callApiDistrict(api) {
    try {
        const response = await fetch(api);
        const data = await response.json();
        renderData(data.districts, "district", "Chọn Quận/Huyện");
    } 
    catch (error) {console.error('Error:', error)}
}

async function callApiWard(api) {
    try {
        const response = await fetch(api);
        const data = await response.json();
        renderData(data.wards, "ward", "Chọn Phường/Xã");
    } 
    catch (error) {console.error('Error:', error)}
}

function renderData(array, select, text) {
    let row = ' <option disable value="">' + text + '</option>';
    array.forEach(element => {row += `<option data-id="${element.code}" value="${element.name}">${element.name}</option>`});
    document.querySelector("#" + select).innerHTML = row;
}

callAPI('https://provinces.open-api.vn/api/?depth=1');

provinceDropdown.addEventListener("change", () => {
    callApiDistrict(host + "p/" + $("#province").find(':selected').data('id') + "?depth=2");
});

districtDropdown.addEventListener("change", () => {
    callApiWard(host + "d/" + $("#district").find(':selected').data('id') + "?depth=2");
});

