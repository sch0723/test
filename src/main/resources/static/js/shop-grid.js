function refreshList(data) {
    //商品
    let content = data.content;
    let contentStr = "";
    for (let i = 0; i < content.length; i++) {
        contentStr += "<div class='col-lg-4 col-md-6 col-sm-6'><div class='product__item'><div class='product__item__pic set-bg' ><img src='" + content[i].productImg + "' alt='QQ'><ul class='product__item__pic__hover'><li><a href='#shopping-cart'><i class='fa fa-shopping-cart'></i></a></li></ul></div> <div class='product__item__text'><h6><a href='#productName'>" + content[i].productName + "</a></h6><h5>$" + content[i].productPrice + "</h5></div></div></div>";
    }
    $("#content").html(contentStr)
}

function refreshAll(data) {
    const totalElements = data.totalElements;
    $("#totalElements").html(totalElements)
    //頁數
    const totalPages = data.totalPages + 1;
    let pageStr = "";
    for (let i = 1; i < totalPages; i++) {
        pageStr += "<a  class='pages' href=''>" + i + "</a>";
    }
    $("#totalPages").html(pageStr)
    refreshList(data)
}


function productType(search) {
    $.ajax({
        url: "/grid/" + search + "/productIdASC/1",
        method: "GET",
        success: function (data) {
            refreshAll(data)
        }
    });
}

function productKeys(keywords) {

    let arr = keywords.split(" ");
    $.ajax({
        url: "/keys/productIdASC/1",
        method: "GET",
        data: {"keywords": arr},
        success: function (data) {
            refreshAll(data)
        }
    });
}


function sortOrPages(search, sort, page, nowthis) {
    $.ajax({
        url: "/grid/" + search + "/" + sort + "/" + page,
        method: "GET",
        success: function (data) {

            if (nowthis.attr('class') === "pages") {
                $('html,body').animate({
                    scrollTop: $("#scrollTop").offset().top
                }, 500);
            }

            refreshList(data)
        }
    });
}

function keysSortOrPages(keywords, sort, page, nowthis) {
    let arr = keywords.split(" ");
    $.ajax({
        url: "/keys/" + sort + "/" + page,
        method: "GET",
        data: {"keywords": arr},
        success: function (data) {

            if (nowthis.attr('class') === "pages") {
                $('html,body').animate({
                    scrollTop: $("#scrollTop").offset().top
                }, 500);
            }

            refreshList(data)
        }
    });
}

window.onload = function () {

    //初始化頁面載入所有商品
    productType("all");

    //初始化需要使用的變數
    let keywords;
    let search = "all";
    let sort = "productIdASC";
    let page = 1;

    //依分類載入商品
    $(".category").on('click', function a() {
        search = $(this).html();
        sort = "productIdASC";
        page = 1;

        productType(search);

        //重置排序
        $('.nice-select span').html('Default')
        $('.nice-select ul .selected').removeClass('selected')
        $('.nice-select ul li:first').addClass('selected');
        $("#sort").val('productIdASC');
        return false;
    });

    //依搜索條件載入商品
    $("#submit").on('click', function a() {

        keywords = $("#keywords").val();
        search = "keys";
        sort = "productIdASC";
        page = 1;

        if (keywords === "") {
            productType("all");
            return false;
        }
        productKeys(keywords);

        $('.nice-select span').html('Default')
        $('.nice-select ul .selected').removeClass('selected')
        $('.nice-select ul li:first').addClass('selected');
        $("#sort").val('productIdASC');

        return false;
    });

    //依排序
    $("#sortSelect").on('change', function a() {
        sort = $("#sortSelect option:selected").val();
        page = 1;

        if (search === "keys") {
            keysSortOrPages(keywords, sort, page, $(this))
        } else {
            sortOrPages(search, sort, page, $(this))
        }
        return false;
    });

    //依頁碼
    $(document).on("click", ".pages", function a() {
        const page = $(this).html();
        if (search === "keys") {
            keysSortOrPages(keywords, sort, page, $(this))
        } else {
            sortOrPages(search, sort, page, $(this))
        }
        return false;
    });
}