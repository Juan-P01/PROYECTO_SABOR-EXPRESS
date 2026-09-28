<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="es">
<head>
<meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Cambiar contraseña | Sabor Express</title>
<link rel="preconnect" href="https://fonts.googleapis.com"><link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"><link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
<link rel="stylesheet" href="${ctx}/vista/css/styles.css">
</head>
<body class="password-page">
<main class="min-vh-100 d-flex align-items-center py-5"><div class="container"><div class="row justify-content-center"><div class="col-md-7 col-lg-5">
<div class="password-card p-4 p-md-5">
<div class="security-icon mb-3"><i class="bi bi-shield-lock-fill"></i></div>
<span class="eyebrow">SEGURIDAD DE LA CUENTA</span><h1 class="password-title mb-2">Cambiar contraseña</h1><p class="text-muted mb-4">Ingresa tu correo y establece una nueva contraseña.</p>
<c:if test="${not empty mensaje}"><div class="alert alert-success" role="alert">${mensaje}</div></c:if>
<c:if test="${not empty error}"><div class="alert alert-danger" role="alert">${error}</div></c:if>
<form novalidate method="post" action="${ctx}/RecuperarContrasena">
<div class="mb-3"><label class="form-label fw-bold" for="correo">Correo electrónico</label><input class="form-control" id="correo" name="correo" type="email" autocomplete="email" value="${param.correo}" required></div>
<div class="mb-3"><label class="form-label fw-bold" for="pass">Nueva contraseña</label><input class="form-control" id="pass" name="pass" type="password" minlength="6" autocomplete="new-password" required></div>
<div class="mb-4"><label class="form-label fw-bold" for="confirmar">Confirmar contraseña</label><input class="form-control" id="confirmar" name="confirmar" type="password" minlength="6" autocomplete="new-password" required></div>
<button class="btn btn-primary-brand rounded-pill w-100 py-2 fw-bold" type="submit">Actualizar contraseña</button>
</form><a class="btn btn-outline-dark rounded-pill w-100 mt-3 py-2 fw-bold" href="${ctx}/vista/login.jsp">Volver al inicio de sesión</a>
</div></div></div></div></main>
</body></html>