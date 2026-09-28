<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mi perfil | Sabor Express</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:wght@400;500;700;800&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/vista/css/styles.css">
</head>
<body class="profile-page">
    <div class="top-ribbon py-2 text-center text-white small fw-bold">
        LA BARRA DEL SABOR · MI CUENTA
    </div>

    <nav class="navbar site-nav border-bottom py-2">
        <div class="container">
            <a class="navbar-brand brand d-flex align-items-center gap-2 m-0" href="${ctx}/Inicio">
                <img src="${ctx}/vista/assets/logo.png" class="brand-logo rounded-circle" alt="Logo La Barra del Sabor">
                <span><small class="d-block">LA BARRA DEL</small><strong class="d-block fs-3">SABOR</strong></span>
            </a>
            <div class="d-flex align-items-center gap-2">
                <span class="fw-bold d-none d-sm-inline">${usuario.nombre} ${usuario.apellido}</span>
                <button class="btn btn-outline-dark rounded-pill px-3" type="button" id="profileBack">
                    <i class="bi bi-arrow-left me-1"></i> Inicio
                </button>
            </div>
        </div>
    </nav>

    <main class="container py-5">
        <div class="row justify-content-center">
            <div class="col-12 col-xl-9">
                <section class="profile-card">
                    <div class="profile-card-head">
                        <div class="profile-avatar"><i class="bi bi-person-fill"></i></div>
                        <div>
                            <span class="eyebrow">CUENTA ACTIVA</span>
                            <h1 class="brand-title mb-1">${usuario.nombre} ${usuario.apellido}</h1>
                            <p class="mb-0 text-secondary">${usuario.correo}</p>
                        </div>
                        <span class="profile-role">${nombreRol}</span>
                    </div>

                    <c:if test="${not empty sessionScope.mensajeFlash}">
                        <div class="alert alert-success mt-3">${sessionScope.mensajeFlash}</div>
                        <c:remove var="mensajeFlash" scope="session"/>
                    </c:if>
                    <c:if test="${not empty errorPerfil}"><div class="alert alert-danger mt-3">${errorPerfil}</div></c:if>

                    <c:if test="${usuario.rolesIdRol == 4}">
                    <div class="card border-0 shadow-sm mt-4 p-4">
                        <div class="d-flex justify-content-between align-items-center mb-3"><div><span class="eyebrow">DATOS PERSONALES</span><h2 class="h4 mb-0">Editar mi información</h2></div><i class="bi bi-pencil-square fs-3"></i></div>
                        <form novalidate method="post" action="${ctx}/Perfil" class="row g-3" id="perfilForm">
                            <div class="col-md-6"><label class="form-label" for="nombre">Nombre</label><input class="form-control" id="nombre" name="nombre" value="${usuario.nombre}" minlength="2" maxlength="50" required></div>
                            <div class="col-md-6"><label class="form-label" for="apellido">Apellido</label><input class="form-control" id="apellido" name="apellido" value="${usuario.apellido}" minlength="2" maxlength="50" required></div>
                            <div class="col-md-6"><label class="form-label" for="correo">Correo electrónico</label><input class="form-control" id="correo" name="correo" type="email" value="${usuario.correo}" maxlength="100" required></div>
                            <div class="col-md-6"><label class="form-label" for="telefono">Teléfono</label><input class="form-control" id="telefono" name="telefono" type="tel" inputmode="numeric" pattern="[0-9]{10}" minlength="10" maxlength="10" value="${usuario.telefono}" required></div>
                            <div class="col-md-8"><label class="form-label" for="direccion">Dirección</label><input class="form-control" id="direccion" name="direccion" value="${usuario.direccion}" minlength="5" maxlength="150" required></div>
                            <div class="col-md-4"><label class="form-label" for="tipoDocumento">Tipo de documento</label><select class="form-select" id="tipoDocumento" name="tipoDocumento" required><option value="1" ${usuario.tiposDocumentoIdTipoDocumento == 1 ? 'selected' : ''}>Cédula de Ciudadanía</option><option value="2" ${usuario.tiposDocumentoIdTipoDocumento == 2 ? 'selected' : ''}>Cédula de Extranjería</option><option value="3" ${usuario.tiposDocumentoIdTipoDocumento == 3 ? 'selected' : ''}>Pasaporte</option></select></div>
                            <div class="col-md-6"><label class="form-label" for="nroDocumento">Número de documento</label><input class="form-control" id="nroDocumento" name="nroDocumento" type="text" inputmode="numeric" pattern="[0-9]{10}" minlength="10" maxlength="10" value="${usuario.nroDocumento}" required></div>
                            <div class="col-12"><button class="btn btn-primary-brand rounded-pill px-4" type="submit"><i class="bi bi-check2-circle me-1"></i> Guardar cambios</button></div>
                        </form>
                    </div>
                    </c:if>

                    <div class="row g-3 mt-2">
                        <div class="col-md-6">
                            <div class="profile-info"><span>Nombre completo</span><strong>${usuario.nombre} ${usuario.apellido}</strong></div>
                        </div>
                        <div class="col-md-6">
                            <div class="profile-info"><span>Correo electrónico</span><strong>${usuario.correo}</strong></div>
                        </div>
                        <div class="col-md-6">
                            <div class="profile-info"><span>Teléfono</span><strong>${usuario.telefono}</strong></div>
                        </div>
                        <div class="col-md-6">
                            <div class="profile-info"><span>Dirección</span><strong>${usuario.direccion}</strong></div>
                        </div>
                        <div class="col-md-6">
                            <div class="profile-info"><span>Tipo de documento</span><strong>${tipoDocumento}</strong></div>
                        </div>
                        <div class="col-md-6">
                            <div class="profile-info"><span>Número de documento</span><strong>${usuario.nroDocumento}</strong></div>
                        </div>
                        <c:if test="${usuario.rolesIdRol == 4}">
                            <div class="col-md-6">
                                <div class="profile-info profile-points"><span>Puntos de fidelización</span><strong>${puntosCliente} puntos</strong></div>
                            </div>
                        </c:if>
                    </div>

                    <div class="d-flex flex-wrap gap-2 mt-4">
                        <c:choose>
                            <c:when test="${usuario.rolesIdRol == 4}">
                                <a class="btn btn-primary-brand rounded-pill px-4" href="${ctx}/Inicio#menu">Ver menú</a>
                                <a class="btn btn-outline-dark rounded-pill px-4" href="${ctx}/MisPedidos">Mis pedidos</a>
                            </c:when>
                            <c:when test="${usuario.rolesIdRol == 1}"><a class="btn btn-primary-brand rounded-pill px-4" href="${ctx}/vista/admin.jsp">Ir al panel</a></c:when>
                            <c:when test="${usuario.rolesIdRol == 2}"><a class="btn btn-primary-brand rounded-pill px-4" href="${ctx}/panel/mesero">Ir al panel</a></c:when>
                            <c:when test="${usuario.rolesIdRol == 3}"><a class="btn btn-primary-brand rounded-pill px-4" href="${ctx}/panel/cocina">Ir al panel</a></c:when>
                            <c:when test="${usuario.rolesIdRol == 5}"><a class="btn btn-primary-brand rounded-pill px-4" href="${ctx}/panel/repartidor">Ir al panel</a></c:when>
                            <c:when test="${usuario.rolesIdRol == 6}"><a class="btn btn-primary-brand rounded-pill px-4" href="${ctx}/panel/cajero">Ir al panel de caja</a></c:when>
                        </c:choose>
                        <form novalidate action="${ctx}/CerrarSesion" method="post" class="d-inline"><button type="submit" class="btn btn-outline-danger rounded-pill px-4">Cerrar sesión</button></form>
                    </div>
                </section>
            </div>
        </div>
    </main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${ctx}/vista/js/form-page.js"></script></body>
</html>
