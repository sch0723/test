

var keywords;
var search = $("#search").val();
var sort = "productIdASC";
var page = 1;
var d = $("#getKeys").val();
if (d) {
    keywords = d;
    search = "keys";
}

$("#totalPages a:first").addClass("pages-focus").siblings().removeClass("pages-focus");

function refreshList(data) {
    //更新商品目錄
    let content = data.content;
    let contentStr = "";
    for (let i = 0; i < content.length; i++) {
        contentStr += "<div class='col-lg-4 col-md-6 col-sm-6'><div class='product__item'><div class='product__item__pic set-bg' ><img src='" + content[i].productImg + "' alt=''><ul class='product__item__pic__hover'><li value='" + content[i].productId + "' class='productId'><a href='#' class='addToCart'><i class='fa fa-shopping-cart'></i></a></li></ul></div> <div class='product__item__text'><h6><a href='/product/" + content[i].productId + "'>" + content[i].productName + "</a></h6><h5>$" + content[i].productPrice + "</h5></div></div></div>";
    }
    $("#content").html(contentStr)
}

function refreshAll(data) {
    //更新商品數目
    const totalElements = data.totalElements;
    $("#totalElements").html(totalElements)

    //更新頁數
    const totalPages = data.totalPages + 1;
    let pageStr = "";
    for (let i = 1; i < totalPages; i++) {
        pageStr += "<a  class='pages' href=''>" + i + "</a>";
    }
    $("#totalPages").html(pageStr)

    refreshList(data)
}

// function productType(search) {
//     $.ajax({
//         url: "/grid/" + search + "/productIdASC/1",
//         method: "GET",
//         success: function (data) {
//             refreshAll(data)
//         }
//     });
// }
//
// function productKeys(keywords) {
//     let arr = keywords.split(" ");
//     $.ajax({
//         url: "/keys/productIdASC/1",
//         method: "GET",
//         data: {"keywords": arr},
//         success: function (data) {
//             refreshAll(data)
//         }
//     });
// }


function sortOrPages(search, sort, page) {
    $.ajax({
        url: "/grid/" + search + "/" + sort + "/" + page,
        method: "GET",
        dataType: "JSON",
        success: function (data) {
            refreshList(data)
        }
    });
}

function keysSortOrPages(keywords, sort, page) {
    $.ajax({
        url: "/keys/" + sort + "/" + page,
        method: "GET",
        dataType: "JSON",
        data: {"keywords": keywords},
        success: function (data) {
            refreshList(data)
        }
    });
}

//依排序載入商品
$(document).on("change", "#sortSelect", function () {
    $("#totalPages a:first").addClass("pages-focus").siblings().removeClass("pages-focus");

    sort = $("#sortSelect option:selected").val();
    page = 1;

    if (search === "keys") {
        keysSortOrPages(keywords, sort, page)
    } else {
        sortOrPages(search, sort, page)
    }
    return false;
});


//依頁碼載入商品
$(document).on("click", ".pages", function () {
    //滾輪上移,頁碼上色
    $('html,body').animate({
        scrollTop: $("#scrollTop").offset().top
    }, 500);
    $(this).addClass("pages-focus").siblings().removeClass("pages-focus");


    const page = $(this).html();
    if (search === "keys") {
        keysSortOrPages(keywords, sort, page)
    } else {
        sortOrPages(search, sort, page)
    }
    return false;
});

//添加商品到購物車
$(document).on("click", ".addToCart", function () {
    let productId = $(this).parent().val();
    alert(productId);
    $.ajax({
        url: "/addToCart/" + productId+"/1",
        method: "GET",
        dataType: "JSON",

        success: function (data) {
            $(".totalNums").html(data.totalNums);
            $(".totalPrice").html(data.totalPrice);
        }
    });
    return false;
});



