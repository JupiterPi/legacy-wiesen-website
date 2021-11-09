let stylesheet = document.createElement("link");
stylesheet.setAttribute("rel", "stylesheet");
stylesheet.setAttribute("type", "text/css");
stylesheet.setAttribute("href", "/res/images.css");
document.getElementById("head").appendChild(stylesheet);

function captionTest1() {
    for (let div of document.getElementsByClassName("img-gallery-item")) {
        let src = div.getAttribute("data-img");

        // removed for temp-images-pre.css without caption support
        /*let caption = document.createElement("div");
        caption.classList.add("img-gallery-caption");
        caption.innerText = div.getAttribute("data-caption");
        div.appendChild(caption);*/

        let img = document.createElement("img");
        img.classList.add("img-gallery-img");
        img.src = src;
        div.appendChild(img);

        div.setAttribute("onclick", "openImage(\"" + src + "\");");
    }
}
captionTest1();

function captionTest2() {
    for (let div of document.getElementsByClassName("img-gallery-item")) {
        console.log(div);

        let src = div.getAttribute("data-img");

        let img = document.createElement("img");
        img.classList.add("img-gallery-img");
        img.src = src;
        img.setAttribute("data-caption", div.getAttribute("data-caption"));
        div.appendChild(img);

        div.setAttribute("onclick", "openImage(\"" + src + "\");");
    }
}

function openImage(src) {
    window.open(src);
}