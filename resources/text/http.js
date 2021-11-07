/* favourites */

function sget(url) {
    return sync("GET", url, null);
}

function spost(url) {
    return sync("POST", url, null);
}

function spost(url, body) {
    return sync("POST", url, body);
}

/* base */

function async(method, url, body, handler) {
    const request = new XMLHttpRequest();
    request.open(method, url, true);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.send(JSON.stringify(body));
    request.onreadystatechange = function() {
        if (request.readyState === 4 && request.status === 200) handler();
    }
}

function sync(method, url, body) {
    const request = new XMLHttpRequest();
    request.open(method, url, false);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.send(JSON.stringify(body));
    return request.responseText;
}