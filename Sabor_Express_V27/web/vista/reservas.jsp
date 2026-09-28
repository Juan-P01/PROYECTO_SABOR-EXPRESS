<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html><html lang="es"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Reservas | Sabor Express</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"><link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
<link rel="stylesheet" href="${ctx}/vista/css/styles.css"><link rel="stylesheet" href="${ctx}/vista/css/roles.css"></head>
<body class="role-page reservations-page" data-context="${ctx}" data-role="${rolReserva == 2 ? 'mesero' : 'cliente'}"><header class="role-header"><a class="brand-link" href="${ctx}${rolReserva == 2 ? '/panel/mesero' : '/Inicio'}"><img src="${ctx}/vista/assets/logo.png" alt="Logo"><span>LA BARRA DEL<br><b>SABOR</b></span></a><div class="role-profile"><c:if test="${rolReserva == 2}"><span class="role-pill">MESERO</span></c:if><a class="btn btn-outline-dark btn-sm" href="${ctx}${rolReserva == 2 ? '/panel/mesero' : '/Inicio'}">Volver</a><form novalidate action="${ctx}/CerrarSesion" method="post" class="d-inline"><button type="submit" class="btn btn-outline-dark btn-sm">Salir</button></form></div></header>
<main class="container py-4 py-lg-5"><section class="role-hero mb-4"><span class="eyebrow">RESERVACIONES</span><h1>RESERVA TU MESA</h1><p class="text-muted">Puedes reservar con un máximo de 3 meses de anticipación.</p><p class="mb-0">Elige fecha, hora, cantidad de personas y una mesa disponible.</p></section>
<c:if test="${not empty errorReserva}"><div class="alert alert-danger">${errorReserva}</div></c:if>
<c:if test="${not empty sessionScope.mensajeFlash}"><div class="alert alert-success">${sessionScope.mensajeFlash}<c:remove var="mensajeFlash" scope="session"/></div></c:if>
<div class="row g-4">
<c:choose>
<c:when test="${rolReserva == 2}">
<section class="col-12"><div class="section-title"><span class="eyebrow">ATENCIÓN EN SALÓN</span><h2>RESERVAS DE CLIENTES</h2></div>
<div class="row g-3">
<c:forEach var="r" items="${reservas}"><div class="col-12 col-md-6 col-xl-4"><article class="order-card reservation-card"><div class="d-flex justify-content-between gap-2"><h3>Mesa ${r.mesa}</h3><span class="status-label">${r.estado}</span></div><p class="order-meta">${r.fecha} · ${r.hora}</p><p class="order-meta"><strong>Cliente:</strong> ${r.cliente}</p><p class="order-meta">${r.personas} persona(s)</p><c:if test="${not empty r.observaciones}"><p>${r.observaciones}</p></c:if><c:if test="${r.estado == 'CONFIRMADA'}"><form novalidate method="post" action="${ctx}/Reservas" class="mt-auto"><input type="hidden" name="accion" value="confirmarLlegada"><input type="hidden" name="idReserva" value="${r.id}"><button class="btn btn-primary-brand w-100" type="submit">✓ Confirmar llegada</button></form></c:if><c:if test="${r.estado == 'LLEGO'}"><div class="reservation-arrived">✓ Cliente en el local</div></c:if></article></div></c:forEach>
<c:if test="${empty reservas}"><div class="col-12"><p class="text-muted">No hay reservas activas de clientes.</p></div></c:if>
</div></section>
</c:when>
<c:otherwise>
<section class="col-lg-6"><div class="order-summary"><form novalidate method="post" action="${ctx}/Reservas">
<label class="form-label" for="fecha">Fecha</label><input id="fecha" name="fecha" type="date" class="form-control mb-3" min="${limiteReservaMin}" max="${limiteReserva}" required>
<label class="form-label" for="hora">Hora</label><input id="hora" name="hora" type="time" class="form-control mb-3" min="11:00" max="22:00" step="1800" required>
<label class="form-label" for="personas">Cantidad de personas</label><input id="personas" name="personas" type="number" class="form-control mb-3" min="1" max="20" required>
<label class="form-label" for="mesa">Mesa disponible</label><select id="mesa" name="mesa" class="form-select mb-3" required><option value="">Selecciona fecha y hora primero</option></select>
<label class="form-label" for="observaciones">Observaciones</label><textarea id="observaciones" name="observaciones" class="form-control mb-3" maxlength="255"></textarea>
<button class="btn btn-primary-brand w-100" type="submit">Confirmar reserva</button></form></div></section>
<section class="col-lg-6"><div class="section-title"><span class="eyebrow">MIS RESERVAS</span><h2>RESERVAS CONFIRMADAS</h2></div>
<c:forEach var="r" items="${reservas}"><article class="order-card mb-3 reservation-card"><div class="d-flex justify-content-between"><h3>Mesa ${r.mesa}</h3><span class="status-label">${r.estado}</span></div><p class="order-meta">${r.fecha} · ${r.hora}</p><p class="order-meta">${r.personas} persona(s)</p><c:if test="${not empty r.observaciones}"><p>${r.observaciones}</p></c:if></article></c:forEach>
<c:if test="${empty reservas}"><p class="text-muted">No tienes reservas activas.</p></c:if></section>
</c:otherwise>
</c:choose>
</div></main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script><script src="${ctx}/vista/js/reservas.js"></script><script src="${ctx}/vista/js/roles.js"></script></body></html>