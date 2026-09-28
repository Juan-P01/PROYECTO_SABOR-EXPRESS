<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="es">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="La Barra del Sabor - Frutifresqueria en Guaduas. Menú, pedidos y domicilios.">
    <title>La Barra del Sabor | Guaduas</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:ital,opsz,wght@0,9..40,100..1000;1,9..40,100..1000&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="vista/css/styles.css">
</head>

<body data-context="${ctx}" data-client-session="${sessionScope.rolUsuario == 4}">
    <c:if test="${not empty sessionScope.mensajeFlash}">
        <div class="alert alert-info alert-dismissible fade show m-3 position-fixed top-0 start-50 translate-middle-x shadow flash-message" role="alert">
            ${sessionScope.mensajeFlash}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
        </div>
        <c:remove var="mensajeFlash" scope="session"/>
    </c:if>
    <div class="top-ribbon py-2 text-center text-white small fw-bold">
        LO MEJOR DE MI REGIÓN · DOMICILIOS · SABOR QUE SE COMPARTE
    </div>

    <nav class="navbar navbar-expand-lg site-nav sticky-top py-2">
        <div class="container">
            <a class="navbar-brand brand d-flex align-items-center gap-2 m-0" href="${ctx}/Inicio#inicio">
                <img src="vista/assets/logo.png" class="brand-logo rounded-circle" alt="Logo La Barra del Sabor">
                <span>
                    <small class="d-block">LA BARRA DEL</small>
                    <strong class="d-block fs-3">SABOR</strong>
                </span>
            </a>
            <button class="navbar-toggler border-0 shadow-none" type="button" data-bs-toggle="collapse"
                data-bs-target="#mainNav" aria-label="Abrir menú">
                <i class="bi bi-list fs-1 text-dark"></i>
            </button>
            <div class="collapse navbar-collapse" id="mainNav">
                <ul class="navbar-nav ms-auto align-items-lg-center gap-lg-2 mt-3 mt-lg-0">
                    <li class="nav-item"><a class="nav-link active px-3 fw-bold" href="#inicio">Inicio</a></li>
                    <li class="nav-item"><a class="nav-link px-3 fw-bold" href="#menu">Menú</a></li>
                    <li class="nav-item"><a class="nav-link px-3 fw-bold" href="#nosotros">Nosotros</a></li>
                    <li class="nav-item"><a class="nav-link px-3 fw-bold" href="${ctx}/panel/cliente">Mis pedidos</a></li>
                    <c:if test="${sessionScope.rolUsuario == 4}"><li class="nav-item"><a class="nav-link px-3 fw-bold" href="${ctx}/Reservas">Reservas</a></li></c:if>
                    <li class="nav-item"><a class="nav-link px-3 fw-bold" href="#beneficios">Beneficios</a></li>
                    <c:choose>
                        <c:when test="${sessionScope.rolUsuario == 4}">
                            <li class="nav-item"><a class="nav-link px-3 fw-bold" href="#misPuntos">Mis puntos</a></li>
                            <li class="nav-item dropdown ms-lg-2">
                                <a class="btn btn-outline-dark rounded-pill px-4 fw-semibold dropdown-toggle w-100 w-lg-auto mb-2 mb-lg-0" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="bi bi-person-circle me-1"></i> ${sessionScope.usuarioLogueado.nombre}
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end rounded-4 shadow-sm border-0 p-2">
                                    <li><a class="dropdown-item rounded-3 fw-semibold" href="${ctx}/Perfil"><i class="bi bi-person-vcard me-2"></i>Mi perfil</a></li>
                                    <li><a class="dropdown-item rounded-3 fw-semibold" href="${ctx}/panel/cliente"><i class="bi bi-bag-heart me-2"></i>Mis pedidos</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><form novalidate action="${ctx}/CerrarSesion" method="post" class="m-0"><button type="submit" class="dropdown-item rounded-3 text-danger fw-semibold"><i class="bi bi-box-arrow-right me-2"></i>Cerrar sesión</button></form></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:when test="${not empty sessionScope.usuarioLogueado}">
                            <li class="nav-item dropdown ms-lg-2">
                                <a class="btn btn-outline-dark rounded-pill px-4 fw-semibold dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"><i class="bi bi-person-circle me-1"></i> ${sessionScope.usuarioLogueado.nombre} <c:if test="${sessionScope.rolUsuario != 4}"><span class="ms-2 small fw-bold text-uppercase">· ${sessionScope.rolUsuario == 1 ? 'ADMINISTRADOR' : sessionScope.rolUsuario == 2 ? 'MESERO' : sessionScope.rolUsuario == 3 ? 'COCINERO' : sessionScope.rolUsuario == 5 ? 'REPARTIDOR' : sessionScope.rolUsuario == 6 ? 'CAJERO' : 'ROL'}</span></c:if></a>
                                <ul class="dropdown-menu dropdown-menu-end rounded-4 shadow-sm border-0 p-2">
                                    <li><a class="dropdown-item rounded-3 fw-semibold" href="${ctx}/Perfil"><i class="bi bi-person-vcard me-2"></i>Mi perfil</a></li>
                                    <c:if test="${sessionScope.rolUsuario == 6}"><li><a class="dropdown-item rounded-3 fw-semibold" href="${ctx}/panel/cajero"><i class="bi bi-cash-coin me-2"></i>Panel de caja</a></li></c:if>
                                    <li><hr class="dropdown-divider"></li><li><form novalidate action="${ctx}/CerrarSesion" method="post" class="m-0"><button type="submit" class="dropdown-item rounded-3 text-danger fw-semibold">Cerrar sesión</button></form></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item ms-lg-2"><a class="btn btn-outline-dark rounded-pill px-4 fw-semibold w-100 w-lg-auto mb-2 mb-lg-0" href="${ctx}/vista/login.jsp"><i class="bi bi-person me-1"></i> Ingresar</a></li>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${sessionScope.rolUsuario == 4}">
                        <li class="nav-item dropdown client-notification-nav">
                            <button id="clientNotificationBtn" class="btn btn-light rounded-circle position-relative notification-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false" title="Notificaciones" aria-label="Notificaciones">
                                <i class="bi bi-bell-fill"></i><span id="clientNotificationBadge" class="notification-badge d-none">0</span>
                            </button>
                            <div class="dropdown-menu dropdown-menu-end notification-menu rounded-4 shadow border-0 p-0">
                                <div class="notification-head"><strong>Notificaciones</strong><span id="notificationCountText">Sin novedades</span></div>
                                <div id="clientNotifications" class="notification-list"><div class="notification-empty">No tienes notificaciones nuevas.</div></div>
                                <div class="notification-footer"><button id="markNotificationsRead" type="button" class="btn btn-sm btn-outline-dark rounded-pill">Marcar como leídas</button></div>
                            </div>
                        </li>
                    </c:if>
                    <li class="nav-item dropdown client-cart-nav">
                        <button id="cartToggle" class="btn btn-primary-brand rounded-circle cart-toggle" type="button" data-bs-toggle="dropdown" data-bs-auto-close="outside" aria-expanded="false" title="Carrito" aria-label="Abrir carrito">
                            <i class="bi bi-bag-heart-fill"></i>
                            <span id="cartBadge" class="cart-badge">0</span>
                        </button>
                        <div id="cartDropdown" class="dropdown-menu dropdown-menu-end cart-dropdown rounded-4 shadow border-0 p-0" aria-labelledby="cartToggle">
                            <div class="cart-dropdown-head"><div><span class="eyebrow">TU PEDIDO</span><h5 class="mb-0 fw-bold">Carrito</h5></div><span class="cart-mini-label"><span id="cartCountLabel">0</span> productos</span></div>
                            <div id="cartItems" class="cart-items"></div>
                            <div id="cartFooter" class="cart-dropdown-footer">
                                <p class="small text-muted mb-3"><i class="bi bi-info-circle me-1"></i>Máximo 20 productos por pedido.</p>
                                <div class="d-flex justify-content-between align-items-center mb-2"><span class="text-muted">Subtotal:</span><strong id="cartSubtotal" class="fs-6">$0</strong></div>
                                <div class="d-flex justify-content-between align-items-center mb-3"><span class="fw-bold">Total:</span><strong id="cartTotal" class="fs-4 text-pink">$0</strong></div>
                                <button id="checkoutBtn" class="btn btn-primary-brand w-100 rounded-pill py-2 fw-bold" type="button">Proceder al pago <i class="bi bi-arrow-right ms-1"></i></button>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <main>
        <section id="inicio" class="hero-section py-5 position-relative">
            <div class="hero-scribble hero-scribble-1 d-none d-md-block">✦</div>
            <div class="hero-scribble hero-scribble-2 d-none d-md-block">✷</div>
            <div class="container py-lg-4 position-relative">
                <div class="row align-items-center g-5">
                    <div class="col-lg-6 text-center text-lg-start">
                        <span class="eyebrow mb-2"><i class="bi bi-stars me-1"></i> Frutifresqueria · Guaduas</span>
                        <h1 class="display-1 brand-title mb-3">LO MEJOR DE<br><span class="text-pink">MI REGIÓN</span></h1>
                        <p class="hero-copy lead text-secondary mb-4 mx-auto mx-lg-0">
                            Frutas, helados, bebidas y preparaciones hechas para compartir. Consulta el menú, arma tu pedido y disfruta el sabor de La Barra del Sabor.
                        </p>
                        <div class="d-flex flex-wrap justify-content-center justify-content-lg-start gap-3">
                            <a class="btn btn-primary-brand btn-lg rounded-pill px-4" href="#menu">
                                Ver menú <i class="bi bi-arrow-right ms-1"></i>
                            </a>
                            <a class="btn btn-light btn-lg rounded-pill px-4 shadow-sm border" href="#domicilios">
                                <i class="bi bi-bicycle me-1"></i> Pedir a domicilio
                            </a>
                        </div>
                        <div class="d-flex justify-content-center justify-content-lg-start gap-4 mt-4 pt-2">
                            <div><strong class="d-block fs-5 text-dark">100%</strong><span class="small text-muted">Sabor fresco</span></div>
                            <div class="vr"></div>
                            <div><strong class="d-block fs-5 text-dark">Local</strong><span class="small text-muted"><i class="bi bi-geo-alt-fill text-warning"></i> Guaduas</span></div>
                            <div class="vr"></div>
                            <div><strong class="d-block fs-5 text-dark">Fácil</strong><span class="small text-muted">Pedido digital</span></div>
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="hero-photo-wrap mx-auto">
                            <div class="hero-photo-back"></div>
                            <img class="hero-photo img-fluid" src="vista/assets/menu/local-equipo.jpg" alt="Fachada y equipo de La Barra del Sabor">
                            <span class="photo-sticker sticker-yellow">SABOR<br>REAL</span>
                            <span class="photo-sticker sticker-pink">¡VEN!</span>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section class="bg-dark text-white py-3">
            <div class="container">
                <div class="row g-3 text-center">
                    <div class="col-6 col-lg-3">
                        <div class="d-flex align-items-center justify-content-center gap-2 fw-bold">
                            <i class="bi bi-phone text-warning fs-5"></i><span>Menú digital</span>
                        </div>
                    </div>
                    <div class="col-6 col-lg-3">
                        <div class="d-flex align-items-center justify-content-center gap-2 fw-bold">
                            <i class="bi bi-bicycle text-warning fs-5"></i><span>Domicilios</span>
                        </div>
                    </div>
                    <div class="col-6 col-lg-3">
                        <div class="d-flex align-items-center justify-content-center gap-2 fw-bold">
                            <i class="bi bi-credit-card text-warning fs-5"></i><span>Pago fácil</span>
                        </div>
                    </div>
                    <div class="col-6 col-lg-3">
                        <div class="d-flex align-items-center justify-content-center gap-2 fw-bold">
                            <i class="bi bi-gift text-warning fs-5"></i><span>Puntos y premios</span>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section id="menu" class="py-5 bg-white">
            <div class="container py-4">
                <div class="menu-heading mb-4">
                    <div>
                        <span class="eyebrow mb-1" id="menuViewLabel">Top ventas</span>
                        <h2 class="display-4 brand-title m-0">Elige tu <span class="text-pink">antojo</span></h2>
                        <p class="text-muted m-0">Fotografías reales del menú. Presiona + para ver ingredientes y agregar al pedido.</p>
                    </div>
                    <div class="input-group rounded-pill overflow-hidden border search-input-group">
                        <span class="input-group-text bg-white border-0 ps-3"><i class="bi bi-search text-muted"></i></span>
                        <label class="visually-hidden" for="menuSearch">Buscar un producto</label><input id="menuSearch" type="search" class="form-control border-0 shadow-none" placeholder="Buscar un producto..." aria-label="Buscar producto">
                    </div>
                </div>

                <div class="d-flex gap-3 flex-wrap mb-4" id="categoryFilters" aria-label="Categorías del menú"></div>

                <div id="menuGrid" class="row g-4"></div>

                <div id="emptyMenu" class="text-center py-5 d-none">
                    <i class="bi bi-emoji-frown display-3 text-muted"></i>
                    <h3 class="mt-3 fw-bold">No encontramos ese antojo</h3>
                    <p class="text-muted">Prueba otro nombre o revisa otra categoría.</p>
                </div>
            </div>
        </section>

        <section id="nosotros" class="py-5 bg-cream">
            <div class="container py-4">
                <div class="row align-items-center g-5">
                    <div class="col-lg-6">
                        <div class="about-photo-grid mx-auto">
                            <img src="vista/assets/menu/local-equipo.jpg" alt="Equipo y establecimiento" class="about-photo main">
                            <img src="vista/assets/menu/fruta-picada.jpg" alt="Fruta picada" class="about-photo small shadow-lg">
                            <span class="about-sticker">HECHO EN<br>GUADUAS</span>
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <span class="eyebrow mb-1">Nosotros</span>
                        <h2 class="display-4 brand-title mb-3">Una parada con <span class="text-pink">sabor local.</span></h2>
                        <p class="lead text-secondary mb-4">
                            La Barra del Sabor es una frutifresqueria de Guaduas donde se encuentran frutas, helados, bebidas y preparaciones para compartir con la mejor atención.
                        </p>
                        <div class="row g-3">
                            <div class="col-sm-4">
                                <div class="bg-white p-3 rounded-4 border text-center text-sm-start h-100 shadow-sm">
                                    <i class="bi bi-geo-alt-fill text-pink fs-3 d-block mb-2"></i>
                                    <strong class="d-block text-dark small">Guaduas</strong>
                                    <small class="text-muted">Cra. 3 #3-34</small>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="bg-white p-3 rounded-4 border text-center text-sm-start h-100 shadow-sm">
                                    <i class="bi bi-clock-fill text-pink fs-3 d-block mb-2"></i>
                                    <strong class="d-block text-dark small">Horario</strong>
                                    <small class="text-muted">Todos los días</small>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="bg-white p-3 rounded-4 border text-center text-sm-start h-100 shadow-sm">
                                    <i class="bi bi-telephone-fill text-pink fs-3 d-block mb-2"></i>
                                    <strong class="d-block text-dark small">322 586 5977</strong>
                                    <small class="text-muted">Domicilios</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section id="domicilios" class="py-5 bg-pink text-white position-relative">
            <div class="container py-4">
                <div class="row align-items-center g-5">
                    <div class="col-lg-6">
                        <span class="eyebrow text-warning mb-2">Domicilios Directos</span>
                        <h2 class="display-4 brand-title text-white mb-3">Pide y te lo <span class="text-warning">llevamos.</span></h2>
                        <p class="text-white-50 lead mb-4">
                            Elige tus productos en el carrito, indícanos tu dirección en Guaduas y procesamos tu pedido de inmediato.
                        </p>
                        <div class="d-flex flex-column gap-3 mb-4">
                            <div class="d-flex align-items-center gap-3">
                                <span class="step-num rounded-circle bg-warning text-dark fw-bold d-flex align-items-center justify-content-center">1</span>
                                <span class="fw-semibold">Arma tu orden en el menú digital</span>
                            </div>
                            <div class="d-flex align-items-center gap-3">
                                <span class="step-num rounded-circle bg-warning text-dark fw-bold d-flex align-items-center justify-content-center">2</span>
                                <span class="fw-semibold">Confirma tus datos y dirección</span>
                            </div>
                            <div class="d-flex align-items-center gap-3">
                                <span class="step-num rounded-circle bg-warning text-dark fw-bold d-flex align-items-center justify-content-center">3</span>
                                <span class="fw-semibold">Recibe en la puerta de tu casa</span>
                            </div>
                        </div>
                        <a class="btn btn-light btn-lg rounded-pill px-4 fw-bold text-dark shadow" href="#menu">
                            <i class="bi bi-bag-plus me-1"></i> Abrir pedido ahora
                        </a>
                    </div>
                    <div class="col-lg-6">
                        <div class="card rounded-4 border-dark border-3 shadow-lg overflow-hidden h-100 bg-dark text-white p-4">
                            <h4 class="fw-bold mb-3"><i class="bi bi-geo-fill text-warning me-2"></i>Zona de Cobertura</h4>
                            <p class="text-white-50 mb-4">Atendemos en todo el casco urbano de Guaduas, Cundinamarca.</p>
                            <div class="bg-light text-dark p-3 rounded-3 mt-auto">
                                <div class="d-flex align-items-center gap-3">
                                    <i class="bi bi-whatsapp fs-2 text-success"></i>
                                    <div>
                                        <div class="small fw-bold text-muted">Línea directa</div>
                                        <div class="fs-5 fw-bold">+57 322 586 5977</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section id="beneficios" class="py-5 bg-white">
            <div class="container py-4 text-center">
                <span class="eyebrow mb-2">Club de Beneficios</span>
                <h2 class="display-4 brand-title mb-4">Acumula puntos y <span class="text-pink">gana premios</span></h2>
                <div class="row g-4 justify-content-center">
                    <div class="col-md-4">
                        <div class="card border rounded-4 p-4 h-100 shadow-sm hover-up">
                            <div class="rounded-circle bg-pink-subtle text-pink mx-auto mb-3 d-flex align-items-center justify-content-center icon-box">
                                <i class="bi bi-person-plus-fill fs-3"></i>
                            </div>
                            <c:if test="${empty sessionScope.usuarioLogueado}"><a class="club-join-btn" href="${ctx}/vista/registro.jsp">Únete al club de puntos</a></c:if>
                            <h5 class="fw-bold">1. Regístrate</h5>
                            <p class="text-muted mb-0">Crea tu cuenta gratis en menos de 1 minuto.</p>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card border rounded-4 p-4 h-100 shadow-sm hover-up">
                            <div class="rounded-circle bg-warning-subtle text-warning-emphasis mx-auto mb-3 d-flex align-items-center justify-content-center icon-box">
                                <i class="bi bi-star-fill fs-3"></i>
                            </div>
                            <h5 class="fw-bold">2. Suma Puntos</h5>
                            <p class="text-muted mb-0">Cada compra registrada acumula puntos canjeables.</p>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card border rounded-4 p-4 h-100 shadow-sm hover-up">
                            <div class="rounded-circle bg-success-subtle text-success mx-auto mb-3 d-flex align-items-center justify-content-center icon-box">
                                <i class="bi bi-gift-fill fs-3"></i>
                            </div>
                            <h5 class="fw-bold">3. Redime Sabores</h5>
                            <p class="text-muted mb-0">Disfruta de descuentos, productos gratis y sorpresas.</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <c:if test="${sessionScope.rolUsuario == 4}">
            <section id="seguimientoPedido" class="py-5 bg-white">
                <div class="container py-4">
                    <span class="eyebrow">TU COMPRA EN TIEMPO REAL</span>
                    <h2 class="display-4 brand-title mb-4">SEGUIMIENTO DEL <span class="text-pink">PEDIDO</span></h2>
                    <div id="seguimientoGrid" class="row g-3"><p class="text-muted">Cargando pedidos activos...</p></div>
                </div>
            </section>
            <section id="historialPedidos" class="py-5 bg-cream">
                <div class="container py-4">
                    <span class="eyebrow">COMPRAS FINALIZADAS</span>
                    <h2 class="display-4 brand-title mb-4">HISTORIAL DE <span class="text-pink">PEDIDOS</span></h2>
                    <div id="historialPedidosGrid" class="row g-3"><p class="text-muted">Cargando historial...</p></div>
                </div>
            </section>
            <section id="misPuntos" class="py-5 bg-cream">
                <div class="container py-4">
                    <div class="row g-4 align-items-stretch">
                        <div class="col-lg-5">
                            <div class="card border-dark border-3 rounded-4 h-100 shadow-sm p-4 bg-white">
                                <span class="eyebrow">CLUB DE BENEFICIOS</span>
                                <h2 class="display-4 brand-title mt-2">MIS <span class="text-pink">PUNTOS</span></h2>
                                <p class="text-muted">Consulta tus puntos acumulados y el historial de movimientos de tu cuenta.</p>
                                <strong id="puntosSaldo" class="display-3 brand-title text-pink">0</strong>
                                <span class="fw-bold">puntos disponibles</span>
                            </div>
                        </div>
                        <div class="col-lg-7">
                            <div class="card border-dark border-3 rounded-4 h-100 shadow-sm p-4 bg-white">
                                <h3 class="brand-title fs-2">HISTORIAL DE PUNTOS</h3>
                                <div id="puntosHistorial" class="mt-3"><p class="text-muted">Cargando tus movimientos...</p></div>
                                <p class="small text-muted mb-0">La equivalencia y las recompensas de canje serán configuradas por la administración del restaurante.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </c:if>
    </main>

    <footer class="bg-dark text-white pt-5 pb-4 border-top">
        <div class="container">
            <div class="row g-4 align-items-center">
                <div class="col-md-6 text-center text-md-start">
                    <h4 class="brand-title m-0">LA BARRA DEL SABOR</h4>
                    <p class="text-white-50 small m-0">Frutifresqueria & Antojos · Guaduas, Cundinamarca</p>
                </div>
                <div class="col-md-6 text-center text-md-end">
                    <div class="d-flex justify-content-center justify-content-md-end gap-3 small">
                        <span>© 2026 La Barra del Sabor</span>
                        <a href="#" class="text-white-50 text-decoration-none hover-white">Términos y Condiciones</a>
                    </div>
                </div>
            </div>
        </div>
    </footer>

    <div class="modal fade" id="detailModal" tabindex="-1" aria-labelledby="detailModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content rounded-4 border-0 shadow">
                <div class="modal-header border-0 pb-0">
                    <h4 class="modal-title fw-bold" id="detailModalLabel">Detalle del Producto</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body p-4">
                    <div class="row g-4 align-items-center">
                        <div class="col-md-6">
                            <img class="detail-photo img-fluid rounded-4 shadow-sm w-100" src="vista/assets/menu/fruta-picada.jpg" alt="Producto">
                        </div>
                        <div class="col-md-6">
                            <p class="detail-description lead fs-6 text-secondary mb-3"></p>
                            <h6 class="fw-bold text-dark text-uppercase small tracking-wider">Ingredientes / Detalles:</h6>
                            <ul class="ingredient-list list-unstyled small text-secondary mb-4 ps-1"></ul>
                            <div class="d-flex align-items-center justify-content-between pt-3 border-top">
                                <strong class="detail-price text-pink fs-3 m-0">$0</strong>
                                <button type="button" class="btn btn-primary-brand rounded-pill px-4" data-detail-add="0">
                                    Agregar al pedido <i class="bi bi-bag-plus ms-1"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="checkoutModal" tabindex="-1" aria-labelledby="checkoutModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <form novalidate class="modal-content rounded-4 border-0 shadow" method="post" action="${ctx}/operaciones" id="checkoutForm">
                <div class="modal-header border-0">
                    <div><span class="eyebrow">Confirmar pedido</span><h2 class="modal-title brand-title fs-2" id="checkoutModalLabel">¿Cómo lo recibes?</h2></div>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body pt-0">
                    <input type="hidden" name="accion" value="crearCliente">
                    <input type="hidden" id="checkoutItems" name="items">
                    <div class="mb-3"><label class="form-label fw-bold d-block">Tipo de pedido</label><label class="me-3"><input type="radio" name="tipoEntrega" value="MESA" checked> Para mesa</label><label><input type="radio" name="tipoEntrega" value="DOMICILIO"> Domicilio</label></div>
                    <div class="mb-3" id="checkoutMesaField"><label class="form-label fw-bold" for="checkoutMesa">Número de mesa</label><select class="form-select" id="checkoutMesa" name="mesa" required><option value="">Cargando mesas...</option></select></div>
                    <div class="mb-3 d-none" id="checkoutDireccionField"><label class="form-label fw-bold" for="checkoutDireccion">Dirección de entrega</label><input class="form-control" id="checkoutDireccion" name="direccion" type="text" placeholder="Barrio, calle y referencias" aria-describedby="coverageHelp"><div id="coverageHelp" class="form-text">Cobertura exclusiva: Guaduas, Cundinamarca.</div></div><div class="mb-3 d-none" id="checkoutObservacionesField"><label class="form-label fw-bold" for="checkoutObservaciones">Observaciones de entrega</label><textarea class="form-control" id="checkoutObservaciones" name="observacionesEntrega" maxlength="500" rows="3" placeholder="Ej. Timbrar en apto 201, llevar cambio..."></textarea></div>
                    <div class="mb-2"><label class="form-label fw-bold d-block">Método de pago</label><label class="me-3"><input type="radio" name="metodoPago" value="EFECTIVO" checked> Efectivo</label><small class="text-muted d-block">El pago demo se confirma en el flujo correspondiente.</small></div>
                    
                </div>
                <div class="modal-footer border-0"><button type="submit" class="btn btn-primary-brand rounded-pill w-100 fw-bold">Enviar pedido a cocina</button></div>
            </form>
        </div>
    </div>

    <div class="toast-container position-fixed bottom-0 end-0 p-3">
        <div id="appToast" class="toast align-items-center text-bg-dark border-0 rounded-4 shadow-lg" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body d-flex align-items-center gap-2">
                    <i class="bi bi-check-circle-fill text-warning fs-5"></i>
                    <span id="toastText" class="fw-semibold"></span>
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Cerrar"></button>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="vista/js/app.js"></script>
</body>

</html>
