$(document).ready(() => {

    const isLoggedIn = localStorage.getItem('access-token') !== null;
    const userInfo = JSON.parse(localStorage.getItem('user-info'));
    const userRole = userInfo ? userInfo.role : null;
    const fullName = userInfo.name;
    const email = userInfo.email;

    if (isLoggedIn) {
        if (userRole === 'ADMIN') {
            $("#header-sticky #header-menu").innerHTML = `
                                <ul class="d-block">
                                    <li><a class="mega-title" th:href="@{/api/v1/courses/analysis}">Dashboard</a></li>
                                    <li><a th:href="@{/api/v1/courses/admin}">Khóa học</a></li>
                                    <li ><a th:href="@{/api/v1/categories}">Thể loại</a></li>
                                    <li ><a th:href="@{/api/v1/accounts}">Tài khoản</a>
                                    <li ><a th:href="@{/api/v1/mailSending}">Gửi mail</a></li>
                                    <li ><a th:href="@{/api/v1/discountCodes}">Mã giảm giá</a></li>
                                </ul>
            `;
            $('#header-sticky #subHeader').innerHTML = `
                            <div class="testi-avatar pr-15 rounded-circle dropdown mr-10">
                                <img src="/images/avatar/avatar3.jpg" alt="avatar" id="user-avatar"
                                     data-toggle="dropdown"
                                     class="rounded-circle avatar dropdown-toggle">
                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="user-avatar">
                                    <p class="dropdown-item font-weight-bold pt-3" id="user-fullName"></p>
                                    <p class="dropdown-item" id="user-email"></p>
                                    <div class="dropdown-divider"></div>                                   
                                    <a class="dropdown-item" href="#">Chỉnh sửa hồ sơ</a>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item logout-link" href="#">Đăng xuất</a>
                                </div>
                            </div>   
            `;
        } else if (userRole === 'TEACHER') {
            $("#header-sticky #header-menu").innerHTML = `
                                <ul class="d-block">
                                    <li><a class="mega-title" th:href="@{/api/v1/courses/analysis}">Dashboard</a></li>
                                    <li><a th:href="@{/api/v1/courses/admin}">Khóa học</a></li>         
                                </ul>
            `;
            $('#header-sticky #subHeader').innerHTML = `
                            <div class="testi-avatar pr-15 rounded-circle dropdown mr-10">
                                <img src="/images/avatar/avatar3.jpg" alt="avatar" id="user-avatar"
                                     data-toggle="dropdown"
                                     class="rounded-circle avatar dropdown-toggle">
                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="user-avatar">
                                    <p class="dropdown-item font-weight-bold pt-3" id="user-fullName"></p>
                                    <p class="dropdown-item" id="user-email"></p>
                                    <div class="dropdown-divider"></div>                                   
                                    <a class="dropdown-item" href="#">Chỉnh sửa hồ sơ</a>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item logout-link" href="#">Đăng xuất</a>
                                </div>
                            </div>     
            `;
        } else {
            $("#header-sticky #header-menu").innerHTML = `
                <ul class="d-block">
                    <li><a class="mega-title" th:href="@{/api/v1}">Trang chủ</a></li>
                    <li><a th:href="@{/api/v1/about}">Giới thiệu</a></li>
                    <li><a th:href="@{/api/v1/courses}">Khóa học</a></li>
                    <li><a th:href="@{/api/v1/instructors}">Giảng viên</a></li>
                    <li><a th:href="@{/api/v1/contact}">Liên hệ</a></li>
                    <li><a th:href="@{/api/v1/myLearning}">Quá trình học tập</a></li>                                                                                               
                </ul>
            `;
            $('#header-sticky #subHeader').innerHTML = `
                            <ul>
                                <li class="d-inline-block fa-lg">
                                    <a class="pr-35 d-inline-block transition-3" th:href="@{/api/v1/wishlist}">
                                        <i class="far fa-heart"></i></a>
                                </li>
                                <li class="d-inline-block fa-lg">
                                    <a class="pr-35 d-inline-block transition-3" th:href="@{/api/v1/cart}">
                                        <i class="fas fa-shopping-cart"></i></a>
                                </li>
                            </ul><!-- social-link -->
                            <div class="testi-avatar pr-15 rounded-circle dropdown mr-10">
                                <img src="/images/avatar/avatar3.jpg" alt="avatar" id="user-avatar"
                                     data-toggle="dropdown"
                                     class="rounded-circle avatar dropdown-toggle">
                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="user-avatar">
                                    <p class="dropdown-item font-weight-bold pt-3" id="user-fullName"></p>
                                    <p class="dropdown-item" id="user-email"></p>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item mb-1" th:href="@{/api/v1/myLearning}">Quá trình học tập của
                                        tôi</a>
                                    <a class="dropdown-item mb-1" th:href="@{/api/v1/cart}">Giỏ hàng của tôi</a>
                                    <a class="dropdown-item" th:href="@{/api/v1/wishlist}">Mong muốn</a>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item mb-1" href="#">Phương thức thanh toán</a>
                                    <a class="dropdown-item" href="#">Chỉnh sửa hồ sơ</a>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item logout-link" href="#">Đăng xuất</a>
                                </div>
                            </div>            
            `;
        }
    } else {
        $("#header-sticky #header-menu").innerHTML = `
                <ul class="d-block">
                    <li><a class="mega-title" th:href="@{/api/v1}">Trang chủ</a></li>
                    <li><a th:href="@{/api/v1/about}">Giới thiệu</a></li>
                    <li><a th:href="@{/api/v1/courses}">Khóa học</a></li>
                    <li><a th:href="@{/api/v1/instructors}">Giảng viên</a></li>
                    <li><a th:href="@{/api/v1/contact}">Liên hệ</a></li>                                                                                          
                </ul>
            `;
        $('#header-sticky #subHeader').innerHTML =
            `<ul class="header-login mr-15 d-none d-sm-block">
                <li>
                    <a class="d-inline-block main-color" th:href="@{/api/v1/login}" data-toggle="tooltip"
                        data-selector="true" data-placement="bottom" title="Login / Register">
                        Đăng nhập</a>                                   
                </li>
                                
            </ul><!-- /header-login -->
            <div class="my-btn d-none d-sm-block">
                 <a th:href="@{/api/v1/signup}" class="btn white-bg">Đăng ký</a>
            </div><!-- /Sign Up -->`;
    }

});