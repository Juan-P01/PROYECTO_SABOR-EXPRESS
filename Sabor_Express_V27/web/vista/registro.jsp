<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="es">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crear cuenta | La Barra del Sabor</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:ital,opsz,wght@0,9..40,100..1000;1,9..40,100..1000&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/vista/css/styles.css">
</head>

<body class="bg-cream">
    <div class="top-ribbon py-2 text-center text-white small fw-bold">
        LA BARRA DEL SABOR · REGISTRO DE CLIENTES
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

    <main class="py-5">
        <div class="container py-lg-4">
            <div class="row justify-content-center">
                <div class="col-12 col-md-10 col-lg-8 col-xl-7">
                    <div class="card rounded-4 border-0 shadow-sm p-4 p-md-5 bg-white">
                        <div class="text-center mb-4">
                            <span class="eyebrow mb-1">Registro</span>
                            <h1 class="display-5 brand-title mb-2">Crear cuenta</h1>
                            <p class="text-muted small">Regístrate para gestionar tus pedidos y acumular puntos.</p>
                        </div>

                        
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mb-4" role="alert">
                                <i class="bi bi-exclamation-triangle me-2"></i> ${error}
                            </div>
                        </c:if>

                        <form class="row g-3" action="${ctx}/RegistrarUsuario" method="post" novalidate>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold small" for="nombre">Nombre</label>
                                <input class="form-control rounded-3" id="nombre" name="nombre" type="text"
                                       placeholder="Tu nombre" value="${paramNombre}">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold small" for="apellido">Apellido</label>
                                <input class="form-control rounded-3" id="apellido" name="apellido" type="text"
                                       placeholder="Tu apellido" value="${paramApellido}">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold small" for="tipoDocumento">Tipo de Documento</label>
                                <select class="form-select rounded-3" id="tipoDocumento" name="tipoDocumento">
                                    <option value="" ${empty paramTipoDocumento ? 'selected' : ''} disabled>Selecciona tipo...</option>
                                    <option value="1" ${paramTipoDocumento == '1' ? 'selected' : ''}>Cédula de Ciudadanía</option>
                                    <option value="2" ${paramTipoDocumento == '2' ? 'selected' : ''}>Tarjeta de Identidad</option>
                                    <option value="3" ${paramTipoDocumento == '3' ? 'selected' : ''}>Cédula de Extranjería</option>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold small" for="nroDocumento">Número de Documento</label>
                                <input class="form-control rounded-3" id="nroDocumento" name="nroDocumento" type="text"
                                       inputmode="numeric" pattern="[0-9]{10}" minlength="10" maxlength="10"
                                       placeholder="Ej: 1000123456" value="${paramNroDocumento}" required>
                            </div>

                            <div class="col-12">
                                <label class="form-label fw-semibold small" for="correo">Correo electrónico</label>
                                <input class="form-control rounded-3" id="correo" name="correo" type="email"
                                       placeholder="ejemplo@correo.com" autocomplete="email" value="${paramCorreo}">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold small" for="telefono">Teléfono / Celular</label>
                                <input class="form-control rounded-3" id="telefono" name="telefono" type="tel"
                                       inputmode="numeric" pattern="[0-9]{10}" minlength="10" maxlength="10"
                                       placeholder="3220000000" value="${paramTelefono}" required>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold small" for="direccion">Dirección (Guaduas)</label>
                                <input class="form-control rounded-3" id="direccion" name="direccion" type="text"
                                       placeholder="Calle / Carrera / Barrio" value="${paramDireccion}">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold small" for="pass">Contraseña</label>
                                <input class="form-control rounded-3" id="pass" name="pass" type="password"
                                       minlength="6" placeholder="Mínimo 6 caracteres" autocomplete="new-password">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold small" for="confirmar">Confirmar contraseña</label>
                                <input class="form-control rounded-3" id="confirmar" name="confirmar" type="password"
                                       placeholder="Repite tu contraseña" autocomplete="new-password">
                            </div>

                            <div class="col-12 mt-3">
                                <div class="form-check">
                                    <input class="form-check-input" id="autorizacionDatos" name="autorizacionDatos"
                                           type="checkbox" value="1" ${paramAutorizacionDatos == '1' ? 'checked' : ''}>
                                    <label class="form-check-label small text-secondary" for="autorizacionDatos">
                                        Autorizo el tratamiento de mis datos personales según las políticas de privacidad.
                                    </label>
                                </div>
                            </div>

                            <div class="col-12 mt-4 d-grid gap-2">
                                <button class="btn btn-primary-brand btn-lg rounded-pill fw-bold" type="submit">
                                    Crear cuenta
                                </button>
                                <a class="btn btn-outline-secondary rounded-pill fw-semibold mt-2" href="${ctx}/vista/login.jsp">
                                    ¿Ya tienes cuenta? Inicia sesión
                                </a>
                            </div>
                        </form>
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
