<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="es">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Iniciar sesión | La Barra del Sabor</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:ital,opsz,wght@0,9..40,100..1000;1,9..40,100..1000&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/vista/css/styles.css">
</head>

<body class="bg-cream">
    <div class="top-ribbon py-2 text-center text-white small fw-bold">
        LA BARRA DEL SABOR · ACCESO DE CLIENTES
    </div>

    <nav class="navbar site-nav py-2 border-bottom">
        <div class="container">
            <a class="navbar-brand brand d-flex align-items-center gap-2 m-0" href="${ctx}/Inicio#inicio">
                <img src="${ctx}/vista/assets/logo.png" class="brand-logo rounded-circle" alt="Logo La Barra del Sabor">
                <span>
                    <small class="d-block">LA BARRA DEL</small>
                    <strong class="d-block fs-3">SABOR</strong>
                </span>
            </a>
            <a class="btn btn-outline-dark rounded-pill px-3 fw-semibold" href="${ctx}/Inicio">
                <i class="bi bi-house me-1"></i> Volver al inicio
            </a>
        </div>
    </nav>

    <c:if test="${param.registro == 'exitoso'}"><div class="container pt-4"><div class="alert alert-success rounded-4 border-0 shadow-sm"><i class="bi bi-check-circle me-2"></i>Cuenta creada correctamente. Ahora inicia sesión con tu correo y contraseña.</div></div></c:if>
    <main class="py-5">
        <div class="container py-lg-4">
            <div class="row justify-content-center">
                <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                    <div class="card rounded-4 border-0 shadow-sm p-4 p-md-5 bg-white">
                        <div class="text-center mb-4">
                            <span class="eyebrow mb-1">Acceso</span>
                            <h1 class="display-5 brand-title mb-2">Iniciar sesión</h1>
                            <p class="text-muted small">Ingresa con tu correo y contraseña para consultar tus beneficios.</p>
                        </div>

                        
                        <c:if test="${not empty mensaje}">
                            <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
                                <i class="bi bi-exclamation-circle me-2"></i> ${mensaje}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar mensaje"></button>
                            </div>
                        </c:if>
                        <c:if test="${not empty sessionScope.mensajeFlash}">
                            <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
                                <i class="bi bi-check-circle me-2"></i> ${sessionScope.mensajeFlash}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar mensaje"></button>
                            </div>
                            <c:remove var="mensajeFlash" scope="session"/>
                        </c:if>

                        <form class="needs-validation" method="post" action="${ctx}/IniciarSesion" novalidate>
                            
                            <div class="mb-3">
                                <label class="form-label fw-semibold small" for="correo">Correo electrónico</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light text-muted border-end-0"><i class="bi bi-envelope"></i></span>
                                    <input class="form-control rounded-end-3 border-start-0 ps-0" id="correo" name="correo" type="email" placeholder="ejemplo@correo.com" autocomplete="email" required>
                                </div>
                            </div>
                            
                            <div class="mb-4">
                                <div class="d-flex justify-content-between">
                                    <label class="form-label fw-semibold small" for="pass">Contraseña</label>
                                </div>
                                <div class="input-group">
                                    <span class="input-group-text bg-light text-muted border-end-0"><i class="bi bi-lock"></i></span>
                                    <input class="form-control rounded-end-3 border-start-0 ps-0" id="pass" name="pass" type="password" placeholder="Tu contraseña" autocomplete="current-password" required>
                                </div>
                            </div>
                            <div class="text-end mb-4"><a class="small fw-semibold" href="${ctx}/vista/recuperar-contrasena.jsp">¿Olvidaste tu contraseña?</a></div>
                            
                            <button class="btn btn-primary-brand btn-lg rounded-pill w-100 fw-bold shadow-sm" type="submit">
                                Iniciar sesión
                            </button>
                        </form>
                        
                        <div class="text-center mt-4">
                            <p class="small text-muted mb-0">¿No tienes una cuenta? <a class="text-pink fw-bold text-decoration-none" href="${ctx}/vista/registro.jsp">Crear cuenta</a></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <footer class="py-4 text-center text-muted small border-top bg-white">
        <div class="container">
            © 2026 La Barra del Sabor · Guaduas
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${ctx}/vista/js/form-page.js"></script>
</body>

</html>
