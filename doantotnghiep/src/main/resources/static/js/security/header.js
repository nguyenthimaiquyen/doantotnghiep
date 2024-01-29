function setHeader() {
    const isLoggedIn = localStorage.getItem('access-token') !== null;
    const userInfo = JSON.parse(localStorage.getItem('user-info'));
    const userRole = userInfo ? userInfo.roles : null;
    const fullName = userInfo ? userInfo.fullName : null;
    const email = userInfo ? userInfo.email : null;

    let headerMenu, subHeader;

    if (isLoggedIn) {
        if (userRole == 'ADMIN') {
            headerMenu = `
                                <ul class="d-block">
                                    <li><a class="mega-title text-dark" href="/courses/analysis">Dashboard</a></li>
                                    <li><a class="text-dark" href="/courses/management">Khóa học</a></li>
                                    <li ><a class="text-dark" href="/training-fields">Lĩnh vực</a></li>
                                    <li ><a class="text-dark" href="/accounts">Tài khoản</a>
                                    <li ><a class="text-dark" href="/mail-sending">Gửi mail</a></li>
                                    <li ><a class="text-dark" href="/discount-codes">Mã giảm giá</a></li>
                                </ul>
            `;
            subHeader = `
                            <div class="testi-avatar pr-15 rounded-circle dropdown mr-10">
                                <img src="/images/avatar/avatar3.jpg" alt="avatar" id="user-avatar"
                                     data-toggle="dropdown"
                                     class="rounded-circle avatar dropdown-toggle cursor-pointer">
                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="user-avatar">
                                    <p class="dropdown-item font-weight-bold pt-3" id="user-fullName">[full-name]</p>
                                    <p class="dropdown-item" id="user-email">[email]</p>                               
                                    <div class="dropdown-divider"></div>
                                    <button class="dropdown-item cursor-pointer" type="button" onclick="logout()">Đăng xuất</button>
                                </div>
                            </div>
            `;
        } else if (userRole == 'TEACHER') {
            headerMenu = `
                                <ul class="d-block text-dark">
                                    <li><a class="mega-title text-dark" href="/courses/analysis">Dashboard</a></li>
                                    <li><a class="text-dark" href="/courses/management">Khóa học</a></li>         
                                </ul>
            `;
            subHeader = `
                            <div class="testi-avatar pr-15 rounded-circle dropdown mr-10">
                                <img src="/images/avatar/avatar3.jpg" alt="avatar" id="user-avatar"
                                     data-toggle="dropdown"
                                     class="rounded-circle avatar dropdown-toggle cursor-pointer">
                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="user-avatar">
                                    <p class="dropdown-item font-weight-bold pt-3" id="user-fullName">[full-name]</p>
                                    <p class="dropdown-item" id="user-email">[email]</p>
                                    <div class="dropdown-divider"></div>                                   
                                    <a class="dropdown-item cursor-pointer" href="/profile">Chỉnh sửa hồ sơ</a>
                                    <div class="dropdown-divider"></div>
                                    <button class="dropdown-item cursor-pointer" type="button" onclick="logout()">Đăng xuất</button>
                                </div>
                            </div>
            `;
        } else {
            headerMenu = `
                <ul class="d-block">
                    <li><a class="mega-title" href="/">Trang chủ</a></li>
                    <li><a href="/about">Giới thiệu</a></li>
                    <li><a href="/courses">Khóa học</a></li>
                    <li><a href="/teachers">Giảng viên</a></li>
                    <li><a href="/contact">Liên hệ</a></li>
                    <li><a href="/my-learning">Quá trình học tập</a></li>                                                                                               
                </ul>
            `;
            subHeader = `
                            <ul>
                                <li class="d-inline-block fa-lg">
                                    <a class="pr-35 d-inline-block transition-3" href="/wishlist">
                                        <i class="far fa-heart"></i></a>
                                </li>
                                <li class="d-inline-block fa-lg">
                                    <a class="pr-35 d-inline-block transition-3" href="/cart">
                                        <i class="fas fa-shopping-cart"></i></a>
                                </li>
                            </ul><!-- social-link -->
                            <div class="testi-avatar pr-15 rounded-circle dropdown mr-10">
                                <img src="/images/avatar/avatar3.jpg" alt="avatar" id="user-avatar"
                                     data-toggle="dropdown"
                                     class="rounded-circle avatar dropdown-toggle cursor-pointer">
                                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="user-avatar">
                                    <p class="dropdown-item font-weight-bold pt-3" id="user-fullName">[full-name]</p>
                                    <p class="dropdown-item" id="user-email">[email]</p>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item mb-1 cursor-pointer" href="/my-learning">Quá trình học tập của
                                        tôi</a>
                                    <a class="dropdown-item mb-1 cursor-pointer" href="/cart">Giỏ hàng của tôi</a>
                                    <a class="dropdown-item cursor-pointer" href="/wishlist">Mong muốn</a>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item cursor-pointer" href="/profile">Chỉnh sửa hồ sơ</a>
                                    <div class="dropdown-divider"></div>
                                    <button class="dropdown-item cursor-pointer" type="button" onclick="logout()">Đăng xuất</button>
                                </div>
                            </div>            
            `;
        }
    } else {
        headerMenu = `
                <ul class="d-block">
                    <li><a class="mega-title" href="/">Trang chủ</a></li>
                    <li><a href="/about">Giới thiệu</a></li>
                    <li><a href="/courses">Khóa học</a></li>
                    <li><a href="/teachers">Giảng viên</a></li>
                    <li><a href="/contact">Liên hệ</a></li>                                                                                          
                </ul>
            `;
        subHeader =
            `<ul class="header-login mr-15 d-none d-sm-block">
                <li>
                    <a class="d-inline-block main-color" href="/login" data-toggle="tooltip"
                         data-selector="true" data-placement="bottom" title="Login / Register">Đăng nhập</a>                             
                </li>                               
            </ul><!-- /header-login -->
            <div class="my-btn d-none d-sm-block">
                 <a href="/signup" class="btn white-bg">Đăng ký</a>
            </div><!-- /Sign Up -->
        `;
    }
    subHeader = subHeader.replace("[full-name]", fullName).replace("[email]", email);
    $("#header-sticky #header-menu").html(headerMenu);
    $('#header-sticky #subHeader').html(subHeader);
}

setHeader();
