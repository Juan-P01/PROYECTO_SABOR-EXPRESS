<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Administrativo | Sabor Express</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/admin.css?v=26">
</head>
<body data-context="${pageContext.request.contextPath}">
<div class="admin-shell">
    <aside class="sidebar" id="sidebar">
        <div class="brand-area">
            <img src="assets/logo.png" alt="Sabor Express">
            <div class="brand-copy"><strong>SABOR</strong><span>EXPRESS</span></div>
        </div>
        <button class="mobile-menu sidebar-brand-toggle sidebar-toggle-fixed" id="mobileMenu" type="button" aria-label="Mostrar u ocultar navegación" aria-expanded="true"><i class="bi bi-list"></i></button>
        <div class="admin-label"><i class="bi bi-shield-lock-fill"></i> Administrador</div>
        <nav class="side-nav">
            <button class="nav-item active" data-section="dashboard"><i class="bi bi-grid-1x2-fill"></i><span>Dashboard</span></button>
            <div class="nav-group open"><button class="nav-group-toggle" type="button" aria-expanded="true"><i class="bi bi-tools"></i><span>Utilidades</span><i class="bi bi-chevron-down ms-auto"></i></button><div class="nav-submenu"><button class="nav-item" data-section="usuarios"><i class="bi bi-people-fill"></i><span>Usuarios</span></button><button class="nav-item" data-section="menu"><i class="bi bi-cup-hot-fill"></i><span>Menú</span></button><button class="nav-item" data-section="mesas"><i class="bi bi-grid-3x3-gap-fill"></i><span>Mesas</span></button><button class="nav-item" data-section="canjeables"><i class="bi bi-stars"></i><span>Canjeables</span></button><button class="nav-item" data-section="reservas"><i class="bi bi-calendar-check"></i><span>Reservas</span></button><button class="nav-item" data-section="fidelizacion"><i class="bi bi-award"></i><span>Fidelización</span></button><button class="nav-item" data-section="pagos"><i class="bi bi-credit-card"></i><span>Pagos</span></button><button class="nav-item" data-section="facturas"><i class="bi bi-file-earmark-text"></i><span>Facturación</span></button></div></div>
            <div class="nav-group open"><button class="nav-group-toggle" type="button" aria-expanded="true"><i class="bi bi-bar-chart-line-fill"></i><span>Reportes</span><i class="bi bi-chevron-down ms-auto"></i></button><div class="nav-submenu"><button class="nav-item" data-section="pedidos"><i class="bi bi-receipt-cutoff"></i><span>Pedidos</span></button><button class="nav-item" data-section="reportes"><i class="bi bi-bar-chart-fill"></i><span>Reportes</span></button></div></div>
        </nav>
        <div class="sidebar-bottom">
            <div class="logged-user"><div class="avatar"><i class="bi bi-person-fill"></i></div><div><strong id="sideUser">Administrador</strong><small>Sabor Express</small></div></div>
            <form novalidate action="${ctx}/CerrarSesion" method="post" class="logout-form"><button class="logout-btn" type="submit" title="Cerrar sesión" aria-label="Cerrar sesión"><i class="bi bi-box-arrow-right"></i></button></form>
        </div>
    </aside>

    <main class="main-content">
        <header class="topbar">
            <div class="top-actions"><button class="icon-btn admin-notification-btn" id="adminNotificationBtn" title="Alertas" aria-label="Alertas"><i class="bi bi-bell"></i><span id="adminNotificationBadge" class="d-none">0</span></button><div id="adminNotificationMenu" class="admin-notification-menu d-none" role="status"><strong>Notificaciones</strong><div id="adminNotificationsList"></div><div class="notification-menu-footer"><button type="button" class="outline-btn btn-sm" id="adminDeleteAllNotifications">Eliminar todas</button></div></div><span class="demo-text">Panel en vivo</span></div>
        </header>

        <div id="alertBox" class="alert admin-alert d-none" role="alert"></div>

        <section class="content-section active" id="section-dashboard">
            <div class="page-head"><div><h1>Panel Administrativo</h1><p id="todayText">Resumen real de Sabor Express</p></div><button class="outline-btn" id="refreshDashboard"><i class="bi bi-arrow-clockwise"></i> Actualizar</button></div>
            <div class="stats-grid">
                <article class="stat-card"><div><small>INGRESOS DEL MES</small><strong id="statIncome">$0</strong><em><i class="bi bi-graph-up-arrow"></i> pagos confirmados</em></div><span class="stat-icon red"><i class="bi bi-cash-stack"></i></span></article>
                <article class="stat-card"><div><small>PEDIDOS HOY</small><strong id="statOrders">0</strong><em><i class="bi bi-cart3"></i> pedidos activos</em></div><span class="stat-icon green"><i class="bi bi-bag-check"></i></span></article>
                <article class="stat-card"><div><small>MESAS ACTIVAS</small><strong id="statTables">0</strong><em><i class="bi bi-grid"></i> registradas</em></div><span class="stat-icon yellow"><i class="bi bi-table"></i></span></article>
                <article class="stat-card"><div><small>CLIENTES HOY</small><strong id="statClients">0</strong><em><i class="bi bi-person-plus"></i> nuevos registros</em></div><span class="stat-icon blue"><i class="bi bi-people"></i></span></article>
            </div>
            <div class="charts-grid">
                <article class="panel-card chart-panel"><div class="panel-title"><h2>Ingresos últimos 7 días</h2><span>Pagos confirmados</span></div><canvas id="incomeChart"></canvas></article>
                <article class="panel-card chart-panel"><div class="panel-title"><h2>Platos más vendidos</h2><span>Ingresos por plato</span></div><canvas id="rankingChart"></canvas><div id="rankingLegend" class="ranking-legend"></div></article>
            </div>
            <article class="panel-card recent-panel"><div class="panel-title"><h2>Pedidos recientes</h2><button class="text-btn" data-section-link="pedidos">Ver todos</button></div><div id="recentOrders" class="orders-list"><div class="loading">Cargando...</div></div></article>
        </section>

        <section class="content-section" id="section-usuarios">
            <div class="page-head"><div><h1>Usuarios</h1><p>Gestiona las cuentas registradas en la base de datos.</p></div><button class="primary-btn" id="newUserBtn"><i class="bi bi-person-plus"></i> Nuevo usuario</button></div>
            <article class="panel-card table-panel"><div class="toolbar admin-filters"><div class="search-box"><i class="bi bi-search"></i><label class="visually-hidden" for="userSearch">Buscar usuarios por nombre, apellido o correo</label><input id="userSearch" type="search" placeholder="Buscar por nombre, apellido o correo..."></div><div class="filter-wrap"><label class="visually-hidden" for="userRoleFilter">Filtrar usuarios por rol</label><select id="userRoleFilter"><option value="">Todos los roles</option></select></div><div class="filter-wrap"><label class="visually-hidden" for="userStatusFilter">Filtrar usuarios por estado</label><select id="userStatusFilter"><option value="1">Activos</option><option value="0">Inactivos</option><option value="">Todos los estados</option></select></div></div><div class="table-responsive"><table><thead><tr><th>Nombre</th><th>Correo</th><th>Rol</th><th>Estado</th><th>Acciones</th></tr></thead><tbody id="usersBody"><tr><td colspan="5" class="loading">Cargando usuarios...</td></tr></tbody></table></div></article>
        </section>

        <section class="content-section" id="section-menu">
            <div class="page-head"><div><h1>Menú</h1><p>Gestiona platos e inventario visual del catálogo.</p></div><div class="page-head-actions"><button class="outline-btn" id="manageCategoriesBtn"><i class="bi bi-tags"></i> Categorías</button><button class="primary-btn" id="newMenuBtn"><i class="bi bi-plus-lg"></i> Nuevo plato</button></div></div>
            <article class="panel-card table-panel"><div class="table-responsive"><table><thead><tr><th>Plato</th><th>Categoría</th><th>Precio</th><th>Disponible</th><th>Estado</th><th>Acciones</th></tr></thead><tbody id="menuBody"><tr><td colspan="6" class="loading">Cargando menú...</td></tr></tbody></table></div></article>
        </section>

        <section class="content-section" id="section-mesas">
            <div class="page-head mesas-page-head">
                <div><h1>Mesas</h1><p>Administra el estado operativo de las mesas y conserva su información histórica.</p></div>
                <button class="primary-btn" id="newTableBtn"><i class="bi bi-plus-lg"></i> Nueva mesa</button>
            </div>
            <article class="panel-card mesas-management">
                <div class="toolbar admin-filters mesas-filters">
                    <div class="search-box"><i class="bi bi-search"></i><label class="visually-hidden" for="tableSearch">Buscar mesa</label><input id="tableSearch" type="search" placeholder="Buscar por número de mesa..."></div>
                    <div class="filter-wrap"><label class="visually-hidden" for="tableOperationalFilter">Filtrar mesa por estado operativo</label><select id="tableOperationalFilter"><option value="">Todos los estados</option></select></div>
                </div>
                <div id="tablesGrid" class="tables-grid"><div class="loading">Cargando mesas...</div></div>
            </article>
        </section>

        <section class="content-section" id="section-reservas">
            <div class="page-head"><div><h1>Reservas</h1><p>Supervisa reservas, clientes, mesas, fechas y estados.</p></div></div>
            <article class="panel-card table-panel"><div class="table-responsive"><table><thead><tr><th>Fecha</th><th>Hora</th><th>Cliente</th><th>Mesa</th><th>Personas</th><th>Estado</th><th>Observaciones</th></tr></thead><tbody id="reservasBody"><tr><td colspan="7" class="loading">Cargando reservas...</td></tr></tbody></table></div></article>
        </section>
        <section class="content-section" id="section-pagos">
            <div class="page-head"><div><h1>Pagos</h1><p>Consulta pagos confirmados, método, factura y pedido asociado.</p></div></div>
            <article class="panel-card table-panel"><div class="table-responsive"><table><thead><tr><th>Fecha</th><th>Pedido</th><th>Cliente</th><th>Método</th><th>Monto</th><th>Factura</th><th>Estado</th></tr></thead><tbody id="pagosBody"><tr><td colspan="7" class="loading">Cargando pagos...</td></tr></tbody></table></div></article>
        </section>
        <section class="content-section" id="section-facturas">
            <div class="page-head"><div><h1>Facturación</h1><p>Consulta comprobantes y el historial de facturación.</p></div></div>
            <article class="panel-card table-panel"><div class="table-responsive"><table><thead><tr><th>Número</th><th>Fecha</th><th>Pedido</th><th>Cliente</th><th>Total</th></tr></thead><tbody id="facturasBody"><tr><td colspan="5" class="loading">Cargando facturas...</td></tr></tbody></table></div></article>
        </section>
        <section class="content-section" id="section-fidelizacion">
            <div class="page-head"><div><h1>Fidelización</h1><p>Consulta saldos de puntos y movimientos de canje de clientes.</p></div></div>
            <article class="panel-card table-panel"><div class="table-responsive"><table><thead><tr><th>Cliente</th><th>Correo</th><th>Puntos</th><th>Canjes</th></tr></thead><tbody id="fidelizacionBody"><tr><td colspan="4" class="loading">Cargando fidelización...</td></tr></tbody></table></div></article>
        </section>

        <section class="content-section" id="section-pedidos">
            <div class="page-head"><div><h1>Pedidos</h1><p>Historial de pedidos reales y su estado actual.</p></div><div class="filter-wrap"><label class="visually-hidden" for="orderFilter">Filtrar pedidos por estado</label><select id="orderFilter"><option value="">Todos los estados</option></select></div></div>
            <article class="panel-card table-panel"><div class="table-responsive"><table><thead><tr><th>Pedido</th><th>Cliente</th><th>Mesa</th><th>Fecha / hora</th><th>Productos</th><th>Total</th><th>Estado</th><th>Incidencia</th></tr></thead><tbody id="ordersBody"><tr><td colspan="8" class="loading">Cargando pedidos...</td></tr></tbody></table></div></article>
        </section>

        <section class="content-section" id="section-reportes">
            <div class="page-head"><div><h1>Reportes</h1><p>Consulta la actividad reciente y compara períodos.</p></div><div class="report-period"><button type="button" class="report-period-btn active" data-period="week">Última semana</button><button type="button" class="report-period-btn" data-period="month">Último mes</button><button type="button" class="outline-btn" id="exportReportBtn"><i class="bi bi-download"></i> Exportar CSV</button></div></div>
            <div class="report-grid"><article class="panel-card chart-panel"><div class="panel-title"><h2>Pedidos por día</h2><span id="ordersPeriodLabel">Últimos 7 días</span></div><canvas id="ordersChart"></canvas></article><article class="panel-card chart-panel"><div class="panel-title"><h2>Ingresos por día</h2><span id="incomePeriodLabel">Pagos confirmados · últimos 7 días</span></div><canvas id="dailyIncomeChart"></canvas></article><article class="panel-card chart-panel"><div class="panel-title"><h2>Nuevos clientes por día</h2><span id="clientsPeriodLabel">Últimos 7 días</span></div><canvas id="clientsChart"></canvas></article><article class="panel-card ranking-panel"><div class="panel-title"><h2>Ranking de platos</h2><span>Mayor ingreso → menor ingreso</span></div><div class="table-responsive"><table><thead><tr><th>#</th><th>Plato</th><th>Categoría</th><th>Ventas</th><th>Ingresos</th></tr></thead><tbody id="rankingBody"></tbody></table></div></article></div>
        </section>

        <section class="content-section" id="section-configuracion">
            <div class="page-head"><div><h1>Utilidades · Configuración</h1><p>Configura parámetros operativos del negocio sin editar código.</p></div></div>
            <article class="panel-card table-panel"><div class="toolbar"><strong>Configuración del negocio</strong></div><div id="configRows" class="row g-3 p-3"></div></article>
        </section>

        <section class="content-section" id="section-canjeables">
            <div class="page-head"><div><h1>Canjeables por puntos</h1><p>Define qué productos pueden canjear los clientes y cuántos puntos requiere cada uno.</p></div><button class="primary-btn" id="newCanjeableBtn"><i class="bi bi-plus-lg"></i> Nuevo canjeable</button></div>
            <article class="panel-card table-panel"><div class="table-responsive"><table><thead><tr><th>Nombre</th><th>Producto</th><th>Puntos</th><th>Estado</th><th>Acciones</th></tr></thead><tbody id="canjeablesBody"><tr><td colspan="5" class="loading">Cargando...</td></tr></tbody></table></div></article>
        </section>
    </main>
</div>


<div class="modal fade" id="incidenciaModal" tabindex="-1" aria-labelledby="incidenciaModalTitle" aria-hidden="true"><div class="modal-dialog modal-dialog-centered"><div class="modal-content admin-modal"><div class="modal-header"><h5 class="modal-title" id="incidenciaModalTitle">Detalle de incidencia</h5><button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button></div><div class="modal-body"><div class="detail-grid"><div class="detail-item"><small>Pedido</small><strong id="incidenciaPedido">—</strong></div><div class="detail-item"><small>Motivo</small><strong id="incidenciaMotivo">—</strong></div><div class="detail-item detail-item-full"><small>Detalle informado por el repartidor</small><strong id="incidenciaDetalle">—</strong></div></div></div><div class="modal-footer"><button type="button" class="outline-btn" data-bs-dismiss="modal">Cerrar</button></div></div></div></div>

<div class="modal fade" id="userModal" tabindex="-1"><div class="modal-dialog modal-lg modal-dialog-centered"><div class="modal-content admin-modal"><div class="modal-header"><h5 class="modal-title" id="userModalTitle">Editar usuario</h5><button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button></div><form novalidate id="userForm" method="post" action="${ctx}/admin-api"><div class="modal-body"><input type="hidden" id="uId" name="id_usuario"><div class="form-grid"><div><label for="uContrasena">Contraseña</label><input id="uContrasena" name="contrasena" type="password" minlength="6" placeholder="Mínimo 6 caracteres"></div><div><label for="uNombre">Nombre</label><input id="uNombre" name="nombre" required></div><div><label for="uApellido">Apellido</label><input id="uApellido" name="apellido" required></div><div><label for="uCorreo">Correo</label><input id="uCorreo" name="correo" type="email" required></div><div><label for="uTelefono">Teléfono</label><input id="uTelefono" name="telefono" required></div><div><label for="uDireccion">Dirección</label><input id="uDireccion" name="direccion" required></div><div><label for="uDocumento">Número de documento</label><input id="uDocumento" name="nroDocumento" type="number" required></div><div><label for="uNacimiento">Fecha de nacimiento</label><input id="uNacimiento" name="fechaNacimiento" type="date" required></div><div><label for="uRol">Rol</label><select id="uRol" name="roles_id_rol" required></select></div><div><label for="uTipoDoc">Tipo de documento</label><select id="uTipoDoc" name="tipoDocumento" required><option value="1">Cédula de Ciudadanía</option><option value="2">Cédula de Extranjería</option><option value="3">Pasaporte</option></select></div><div><label for="uAutorizacion">Autorización de datos</label><select id="uAutorizacion" name="autorizacionDatos"><option value="1">Autorizado</option><option value="0">No autorizado</option></select></div><div><label for="uEstado">Estado</label><select id="uEstado" name="estado"><option value="1">Activo</option><option value="0">Inactivo</option></select></div></div></div><div class="modal-footer"><button type="button" class="outline-btn" data-bs-dismiss="modal">Cancelar</button><button class="primary-btn" type="submit">Guardar cambios</button></div></form></div></div></div>

<div class="modal fade" id="userDetailModal" tabindex="-1"><div class="modal-dialog modal-lg modal-dialog-centered"><div class="modal-content admin-modal"><div class="modal-header"><h5 class="modal-title">Detalle del usuario</h5><button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button></div><div class="modal-body"><div class="detail-grid" id="userDetailBody"></div></div><div class="modal-footer"><button type="button" class="outline-btn" data-bs-dismiss="modal">Cerrar</button></div></div></div></div>

<div class="modal fade" id="categoryModal" tabindex="-1"><div class="modal-dialog modal-lg modal-dialog-centered"><div class="modal-content admin-modal"><div class="modal-header"><h5 class="modal-title">Categorías del menú</h5><button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button></div><div class="modal-body"><div class="category-admin-head"><strong>Crear, editar o eliminar categorías</strong><button type="button" class="primary-btn" id="newCategoryBtn"><i class="bi bi-plus-lg"></i> Nueva categoría</button></div><div id="categoriesAdminList" class="categories-admin-list"></div></div><div class="modal-footer"><button type="button" class="outline-btn" data-bs-dismiss="modal">Cerrar</button></div></div></div></div>

<div class="modal fade" id="categoryEditModal" tabindex="-1"><div class="modal-dialog modal-md modal-dialog-centered"><div class="modal-content admin-modal"><div class="modal-header"><h5 class="modal-title" id="categoryEditTitle">Nueva categoría</h5><button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button></div><form novalidate id="categoryForm" method="post" action="${ctx}/admin-api"><div class="modal-body"><input type="hidden" id="catId" name="idCategoria"><div class="form-grid"><div class="full"><label for="catNombre">Nombre</label><input id="catNombre" required maxlength="60" name="nombre"></div><div class="full"><label>Icono de categoría</label><input type="hidden" id="catIcono" value="🍓" name="icono"><div id="categoryIconPicker" class="icon-picker" role="group" aria-label="Selecciona un icono"></div><small class="form-help">Selecciona un icono. No se puede escribir texto en este campo.</small></div><div><label for="catEstado">Estado</label><select id="catEstado" name="estado"><option value="1">Activa</option><option value="0">Inactiva</option></select></div></div></div><div class="modal-footer"><button type="button" class="outline-btn" data-bs-dismiss="modal">Cancelar</button><button class="primary-btn" type="submit">Guardar categoría</button></div></form></div></div></div>

<div class="modal fade" id="menuModal" tabindex="-1"><div class="modal-dialog modal-lg modal-dialog-centered"><div class="modal-content admin-modal"><div class="modal-header"><h5 class="modal-title" id="menuModalTitle">Nuevo plato</h5><button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button></div><form novalidate id="menuForm" method="post" action="${ctx}/admin-api" enctype="multipart/form-data"><div class="modal-body"><input type="hidden" id="mId" name="idMenu"><div class="form-grid"><div class="full"><label for="mNombre">Nombre</label><input id="mNombre" required maxlength="45" name="nombre"></div><div class="full"><label for="mDescripcion">Descripción</label><textarea id="mDescripcion" required maxlength="255" name="descripcion"></textarea></div><div><label for="mCategoria">Categoría</label><select id="mCategoria" required name="idCategoria"></select></div><div><label for="mPrecio">Precio</label><input id="mPrecio" type="number" min="0" step="0.01" required name="precio"></div><div><label for="mPuntos">Puntos de fidelización por unidad</label><input id="mPuntos" type="number" min="0" max="100000" step="1" value="0" required name="puntosFidelizacion"></div><div class="full"><label for="mImagenArchivo">Imagen del plato</label><div class="upload-control"><label class="upload-btn" for="mImagenArchivo"><i class="bi bi-cloud-arrow-up"></i> Subir imagen</label><input id="mImagenArchivo" type="file" accept="image/png,image/jpeg,image/webp" name="imagenArchivo"><span id="mImagenNombre" class="upload-name">Ninguna imagen seleccionada</span></div><div class="menu-image-preview-wrap"><img id="mImagenPreview" class="menu-image-preview" src="assets/menu/placeholder.svg" alt="Vista previa del plato"></div><small class="text-muted">PNG, JPG o WEBP · máximo 5 MB. En edición, si no eliges una nueva imagen se conserva la actual.</small><input id="mImagenActual" type="hidden" name="imagenActual"></div><div><label for="mDisponible">Disponible</label><select id="mDisponible" name="disponible"><option value="1">Sí</option><option value="0">No</option></select></div><div><label for="mEstado">Estado del registro</label><select id="mEstado" name="estado"><option value="1">Activo</option><option value="0">Inactivo</option></select></div></div></div><div class="modal-footer"><button type="button" class="outline-btn" data-bs-dismiss="modal">Cancelar</button><button class="primary-btn" type="submit">Guardar plato</button></div></form></div></div></div>

<div class="modal fade" id="tableModal" tabindex="-1"><div class="modal-dialog modal-md modal-dialog-centered"><div class="modal-content admin-modal"><div class="modal-header"><h5 class="modal-title" id="tableModalTitle">Nueva mesa</h5><button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button></div><form novalidate id="tableForm" method="post" action="${ctx}/admin-api"><div class="modal-body"><input type="hidden" id="tId" name="idMesa"><div class="form-grid"><div><label for="tNumero">Número de mesa</label><input id="tNumero" type="number" min="1" required name="numero"></div><div><label for="tEstadoMesa">Estado operativo</label><select id="tEstadoMesa" required name="idEstadoMesa"></select></div><div><label for="tEstadoRegistro">Estado del registro</label><select id="tEstadoRegistro" name="estado"><option value="1">Activo</option><option value="0">Inactivo</option></select></div></div></div><div class="modal-footer"><button type="button" class="outline-btn" data-bs-dismiss="modal">Cancelar</button><button class="primary-btn" type="submit">Guardar mesa</button></div></form></div></div></div>

<div class="modal fade" id="canjeableModal" tabindex="-1"><div class="modal-dialog modal-md modal-dialog-centered"><div class="modal-content admin-modal"><div class="modal-header"><h5 class="modal-title" id="canjeableTitle">Nuevo canjeable</h5><button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button></div><form novalidate id="canjeableForm" method="post" action="${ctx}/admin-api"><div class="modal-body"><input type="hidden" id="cId" name="idCanjeable"><div class="form-grid"><div class="full"><label for="cNombre">Nombre del canjeable</label><input id="cNombre" required maxlength="100" name="nombre"></div><div class="full"><label for="cDescripcion">Descripción</label><textarea id="cDescripcion" maxlength="255" name="descripcion"></textarea></div><div class="full"><label for="cMenu">Producto del menú</label><select id="cMenu" required name="idMenu"></select></div><div><label for="cPuntos">Puntos requeridos</label><input id="cPuntos" type="number" min="1" required name="puntos"></div><div><label for="cActivo">Estado</label><select id="cActivo" name="activo"><option value="1">Activo</option><option value="0">Inactivo</option></select></div></div></div><div class="modal-footer"><button type="button" class="outline-btn" data-bs-dismiss="modal">Cancelar</button><button class="primary-btn" type="submit">Guardar</button></div></form></div></div></div>
<div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-sm">
    <div class="modal-content admin-modal confirm-modal">
      <div class="modal-header"><h5 class="modal-title" id="confirmModalTitle">Confirmar acción</h5><button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button></div>
      <div class="modal-body"><div class="confirm-icon"><i class="bi bi-exclamation-triangle"></i></div><p id="confirmModalMessage" class="mb-0 text-center">¿Deseas continuar?</p></div>
      <div class="modal-footer"><button type="button" class="outline-btn" data-bs-dismiss="modal">Cancelar</button><button type="button" class="primary-btn" id="confirmModalAccept">Confirmar</button></div>
    </div>
  </div>
</div>
<div class="toast-container position-fixed bottom-0 end-0 p-3"><div id="toast" class="toast admin-toast" role="alert"><div class="toast-body"><i class="bi bi-check-circle-fill"></i><span id="toastText"></span></div></div></div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.4/dist/chart.umd.min.js"></script>
<script src="${ctx}/vista/js/admin.js?v=27"></script>
</body>
</html>
