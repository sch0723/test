

var keywords="";
var search = $("#search").val();
var sort = "Default";
var page = 1;
var d = $("#getKeys").val();
if (d) {
    keywords = d;
    search = "keys";
}

// var stateObject={
//     keywords:keywords,
//     search : search,
//     sort : sort,
//     page : page,
// };
//
// window.onload=function(){
//
//
//     if(!window.history.state){
//         window.history.replaceState(stateObject,null,"");
//
//     }
//
// }





// $(document).on("click", ".toPro", function () {
//     window.sessionStorage.setItem("myhtml",$("#content").html());
// });
//
// window.onload=function(){
//     const myhtml = window.sessionStorage.getItem("myhtml");
//     if(myhtml!=null){
//         $("#content").html(myhtml);
//         window.sessionStorage.clear();
//     }
// }



$("#totalPages a:first").addClass("pages-focus").siblings().removeClass("pages-focus");

function refreshList(data) {
    //更新商品目錄
    let content = data.content;
    let contentStr = "";
    for (let i = 0; i < content.length; i++) {
        contentStr += "<div class='col-lg-4 col-md-6 col-sm-6'><div class='product__item'><div class='product__item__pic set-bg' ><img src='/res/img/product/" + content[i].productId + ".jpg' alt=''><ul class='product__item__pic__hover'><li value='" + content[i].productId + "' class='productId'><a href='#' class='addToCart'><i class='fa fa-shopping-cart'></i></a></li></ul></div> <div class='product__item__text'><h6><a href='/product/" + content[i].productId + "' class='toPro'>" + content[i].productName + "</a></h6><h5>$" + content[i].productPrice + "</h5></div></div></div>";
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

//更新排序樣式
function refreshSort(sort) {
    $('.nice-select span').html(sort)
    $('.nice-select ul .selected').removeClass('selected')
    $('.nice-select ul li[data-value='+sort+']').addClass('selected');
    $("#sort").val(sort);
}

//更新頁碼樣式
function refreshPageIndex(pageIndex) {
    $('.pages[value='+pageIndex+']').addClass("pages-focus").siblings().removeClass("pages-focus");
}

function scrollToTop() {
    $('html,body').animate({
        scrollTop: $("body").offset().top
    }, 500);
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

//ajax更新商品目錄for所有商品和分類商品
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

//ajax更新商品目錄for搜索攔商品
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

// window.addEventListener("popstate", function(e) {
//     var state = e.state;
//
//     if (state!=null&&state.search==="keys"){
//         keysSortOrPages(state.keywords, state.sort, state.page);
//         scrollToTop();
//         refreshPageIndex(state.page);
//         refreshSort(state.sort);
//         // return false;
//     }
//
//     if (state!==null&&state.search!=="keys"){
//         sortOrPages(state.search, state.sort, state.page);
//         scrollToTop();
//         refreshPageIndex(state.page);
//         refreshSort(state.sort);
//         // return false;
//     }
//
// });

//依排序載入商品
$(document).on("change", "#sortSelect", function () {
    $("#totalPages a:first").addClass("pages-focus").siblings().removeClass("pages-focus");
    sort = $("#sortSelect option:selected").val();
    page = 1;

    if (search === "keys") {
        keysSortOrPages(keywords, sort, page)

        // stateObject.keywords=keywords;
        // stateObject.search=search;
        // stateObject.sort=sort;
        // stateObject.page=page;
        // window.history.pushState(stateObject,null,"?keywords="+keywords+"&sort="+sort+"&page="+page);

    } else {
        sortOrPages(search, sort, page)

        // stateObject.keywords=keywords;
        // stateObject.search=search;
        // stateObject.sort=sort;
        // stateObject.page=page;
        // window.history.pushState(stateObject,null,"?search="+search+"&sort="+sort+"&page="+page);

    }
    return false;
});



//依頁碼載入商品
$(document).on("click", ".pages", function () {
    //滾輪上移,頁碼上色
    scrollToTop();
    $(this).addClass("pages-focus").siblings().removeClass("pages-focus");


    page = $(this).html();
    if (search === "keys") {
        keysSortOrPages(keywords, sort, page)

        // stateObject.keywords=keywords;
        // stateObject.search=search;
        // stateObject.sort=sort;
        // stateObject.page=page;
        // window.history.pushState(stateObject,null,"?keywords="+keywords+"&sort="+sort+"&page="+page)
    } else {
        sortOrPages(search, sort, page);

        // stateObject.keywords=keywords;
        // stateObject.search=search;
        // stateObject.sort=sort;
        // stateObject.page=page;
        // window.history.pushState(stateObject,null,"?search="+search+"&sort="+sort+"&page="+page);
    }

    return false;
});

//添加商品到購物車
$(document).on("click", ".addToCart", function () {
    let productId = $(this).parent().val();
    alert(productId);
    $.ajax({
        url: "/cart/" + productId+"/1",
        method: "post",
        dataType: "JSON",

        success: function (data) {

            if(data){
                $(".totalNums").html(data.totalNums);
                $(".totalPrice").html(data.totalPrice);
            }else{
                $(".totalNums").html(0);
                $(".totalPrice").html(0);
            }
        }
    });

    return false;
});



