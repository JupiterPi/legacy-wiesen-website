function pages(pages, defaultHeaderPic, activePage) {
    let defaultHeaderPicName = defaultHeaderPic[0];
    let defaultHeaderPicBackgroundTone = defaultHeaderPic[1];
    console.log(defaultHeaderPicName);
    console.log(defaultHeaderPicBackgroundTone);

    const pagesContainer = document.getElementById("pages-div");
    for (const i in pages) {
        const page = pages[i];
        const pageItem = document.createElement("div");
        pageItem.classList.add("page-item");
        if (page[0] === "---") {
            pageItem.classList.add("spacing");
        } else {
            if (page[1] === activePage) {
                pageItem.classList.add("active");
                document.getElementsByTagName("title")[0].innerHTML = page[0] + " | Wiesen im Sch&ouml;nengstgau";

                let headerPic = page[2];
                let headerPicName;
                let backgroundTone;
                if (headerPic.length !== 2) {
                    console.log("is empty")
                    headerPicName = defaultHeaderPicName;
                    backgroundTone = defaultHeaderPicBackgroundTone;
                } else {
                    console.log("not empty")
                    headerPicName = headerPic[0];
                    backgroundTone = headerPic[1];
                }
                console.log(headerPic);
                let bodyElement = document.getElementById("body");
                bodyElement.style = "background-image: url('/header-pic/" + headerPicName + "');";
                bodyElement.classList.add(backgroundTone);
            }
            pageItem.innerHTML = page[0];
            pageItem.setAttribute("onclick", "goto('/" + page[1] + "');");
        }
        pagesContainer.appendChild(pageItem);
    }
}

function goto(page) {
    window.location.href = page;
}