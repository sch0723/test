function refreshList(data) {
    //更新商品目錄
    let content = data.content;
    let contentStr = "";
    for (let i = 0; i < content.length; i++) {
        contentStr += "<div class='col-lg-4 col-md-6 col-sm-6'><div class='product__item'><div class='product__item__pic set-bg' ><img src='" + content[i].productImg + "' alt='QQ'><ul class='product__item__pic__hover'><li><a href='#shopping-cart'><i class='fa fa-shopping-cart'></i></a></li></ul></div> <div class='product__item__text'><h6><a href='/product/" + content[i].productId + "'>" + content[i].productName + "</a></h6><h5>$" + content[i].productPrice + "</h5></div></div></div>";
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
        dataType:"JSON",
        success: function (data) {
            refreshList(data)
        }
    });
}

function keysSortOrPages(keywords, sort, page) {
    $.ajax({
        url: "/keys/" + sort + "/" + page,
        method: "GET",
        dataType:"JSON",
        data: {"keywords": keywords},
        success: function (data) {
            refreshList(data)
        }
    });
}

window.onload = function () {

    //初始化需要使用的變數
    let keywords;
    let search = $("#search").val();
    let sort = "productIdASC";
    let page = 1;

    //從其他頁面以搜索攔到商品目錄
    let d = $("#getKeys").val();
    if (d) {
        keywords = d;
        search = "keys";
    }

    $("#totalPages a:first").addClass("pages-focus").siblings().removeClass("pages-focus");



    //添加商品到購物車
    $('.addToCart').on('click', function () {
        let productId=$(this).parent().parent().val();
        alert(productId);
        return false;
    });

    //依分類載入商品
    // $(".category").on('click', function a() {
    //     search = $(this).html();
    //     sort = "productIdASC";
    //     page = 1;
    //
    //     productType(search);
    //
    //     //重置排序
    //     $('.nice-select span').html('Default')
    //     $('.nice-select ul .selected').removeClass('selected')
    //     $('.nice-select ul li:first').addClass('selected');
    //     $("#sort").val('productIdASC');
    //     return false;
    // });

    //依搜索條件載入商品
    // $("#submit").on('click', function a() {
    //     keywords = $("#keywords").val();
    //     search = "keys";
    //     sort = "productIdASC";
    //     page = 1;
    //
    //     productKeys(keywords);
    //
    //     //重置排序
    //     $('.nice-select span').html('Default')
    //     $('.nice-select ul .selected').removeClass('selected')
    //     $('.nice-select ul li:first').addClass('selected');
    //     $("#sort").val('productIdASC');
    //
    //     return false;
    // });

    //依排序
    $("#sortSelect").on('change', function a() {

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

    //依頁碼
    $(document).on("click", ".pages", function a() {
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
};