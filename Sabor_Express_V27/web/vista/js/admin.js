(() => {
    const CTX = document.body.dataset.context || '';
    const API = `${CTX}/admin-api`;
    const $ = id => document.getElementById(id);
    const money = n => new Intl.NumberFormat('es-CO', {style:'currency', currency:'COP', maximumFractionDigits:0}).format(Number(n)||0);
    const date = v => v ? new Date(v).toLocaleString('es-CO', {dateStyle:'short', timeStyle:'short'}) : '—';
    const dateOnly = v => v ? String(v).slice(0,10) : '';
    let users = [], menus = [], roles = [], categories = [], mesaStates = [], orderStates = [], canjeables = [], charts = {};
    let reportPeriod = 'week';
    let allTables = [];
    let pendingConfirmation = null;
    let adminNotificationTimer = null;
    const CATEGORY_ICONS = ['🍓','🍦','🥤','🥪','🍰','🍔','🍕','🍫','🧇','🍉','🍋','☕','🥭','🍪'];

    function categoryIconClass(value) { return CATEGORY_ICONS.includes(value) ? value : '🍓'; }

    async function api(action, options = {}) {
        const context = document.body?.dataset?.context || window.location.pathname.split('/vista/')[0] || '';
        const apiUrl = `${context}/admin-api`;
        const method = String(options.method || 'GET').toUpperCase();
        const params = new URLSearchParams();
        params.set('action', action);
        if (options.query) {
            const extra = new URLSearchParams(options.query);
            extra.forEach((value, key) => params.set(key, value));
        }
        const url = `${apiUrl}?${params.toString()}`;
        const headers = {};
        if (method === 'POST' && !options.multipart && !(options.body instanceof FormData)) {
            headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';
        }
        let response;
        try {
            response = await fetch(url, {
                method,
                headers: Object.keys(headers).length ? headers : undefined,
                body: method === 'GET' || method === 'HEAD' ? undefined : (options.body ?? '') ,
                credentials: 'same-origin',
                cache: 'no-store'
            });
        } catch (networkError) {
            throw new Error('No fue posible conectar con /admin-api. Verifica GlassFish, la sesión y la conexión con MySQL.');
        }
        const text = await response.text();
        let data;
        try {
            data = text ? JSON.parse(text) : {};
        } catch (parseError) {
            console.error('Respuesta no JSON de /admin-api:', text);
            throw new Error(`El servidor respondió con un formato inválido (HTTP ${response.status}).`);
        }
        if (response.status === 401 || response.status === 403) {
            window.location.href = `${context}/vista/login.jsp`;
            throw new Error(data.mensaje || 'Sesión no autorizada para el administrador.');
        }
        if (!response.ok || data.ok === false) {
            throw new Error(data.mensaje || `La operación '${action}' falló (HTTP ${response.status}).`);
        }
        return data;
    }

    function formBody(obj) { return new URLSearchParams(obj).toString(); }
    function showAdminModal(id) { const el=$(id); if(!el) throw new Error('No se encontró el modal '+id+'.'); if(!window.bootstrap?.Modal) throw new Error('Bootstrap JS no está disponible.'); bootstrap.Modal.getOrCreateInstance(el).show(); }
    function notify(text, error = false) {
        const box = $('alertBox');
        box.textContent = text;
        box.className = 'alert admin-alert ' + (error ? 'alert-danger' : 'alert-success');
        setTimeout(() => box.classList.add('d-none'), 2800);
    }
    function confirmAction(message, action, title = 'Confirmar acción') {
        pendingConfirmation = action;
        $('confirmModalTitle').textContent = title;
        $('confirmModalMessage').textContent = message;
        bootstrap.Modal.getOrCreateInstance($('confirmModal')).show();
    }
    $('confirmModalAccept')?.addEventListener('click', async () => {
        const action = pendingConfirmation;
        pendingConfirmation = null;
        bootstrap.Modal.getInstance($('confirmModal'))?.hide();
        if (action) await action();
    });

    function statusClass(s) {
        const x = (s || '').toLowerCase();
        if (x.includes('pend')) return 'pendiente';
        if (x.includes('prepar')) return 'preparacion';
        if (x.includes('serv')) return 'servido';
        if (x.includes('pag')) return 'pagado';
        return '';
    }
    function statusTable(s) {
        const x = (s || '').toLowerCase();
        if (x.includes('dispon')) return 'disponible';
        if (x.includes('ocup')) return 'ocupada';
        if (x.includes('reserv')) return 'reservada';
        return 'mantenimiento';
    }
    function escapeHtml(s) { return String(s ?? '').replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c])); }

    function switchSection(name) {
        document.querySelectorAll('.content-section').forEach(x => x.classList.remove('active'));
        $('section-' + name)?.classList.add('active');
        document.querySelectorAll('.nav-item').forEach(x => x.classList.toggle('active', x.dataset.section === name));
        if (name === 'dashboard') loadDashboard();
        if (name === 'usuarios') loadUsers($('userSearch').value);
        if (name === 'menu') loadMenus();
        if (name === 'mesas') loadTables();
        if (name === 'pedidos') loadOrders();
        if (name === 'reportes') loadReports();
        if (name === 'configuracion') loadConfiguracion();
        if (name === 'canjeables') loadCanjeables();
        if (name === 'reservas') loadReservas();
        if (name === 'pagos') loadPagos();
        if (name === 'facturas') loadFacturas();
        if (name === 'fidelizacion') loadFidelizacion();
        if (window.matchMedia('(max-width: 820px)').matches) document.querySelector('.admin-shell').classList.add('sidebar-hidden');
    }

    const adminShell = document.querySelector('.admin-shell');
    if (window.matchMedia('(max-width: 820px)').matches) {
        adminShell.classList.add('sidebar-hidden');
        $('mobileMenu').setAttribute('aria-expanded', 'false');
    }
    document.querySelectorAll('.nav-item').forEach(b => b.addEventListener('click', () => switchSection(b.dataset.section)));
    document.querySelectorAll('.nav-group-toggle').forEach(b => b.addEventListener('click', () => { const group=b.closest('.nav-group'); const open=group.classList.toggle('open'); b.setAttribute('aria-expanded', String(open)); }));
    document.querySelectorAll('[data-section-link]').forEach(b => b.addEventListener('click', () => switchSection(b.dataset.sectionLink)));
    setupAdminNotifications();
    $('mobileMenu')?.addEventListener('click', () => {
        const hidden = adminShell.classList.toggle('sidebar-hidden');
        $('mobileMenu').setAttribute('aria-expanded', String(!hidden));
    });


    async function loadAdminNotifications(){
        try{
            const d=await api('notificaciones');
            const rows=Array.isArray(d)?d:[];
            const badge=$('adminNotificationBadge');
            const list=$('adminNotificationsList');
            if(badge){badge.textContent=rows.length;badge.classList.toggle('d-none',rows.length===0);}
            if(list){list.innerHTML=rows.length?rows.map(n=>`<div class="admin-notification-item"><div class="d-flex gap-2 align-items-start"><div class="flex-grow-1"><b>${escapeHtml(n.titulo||'Notificación')}</b><p>${escapeHtml(n.mensaje||'')}</p><small>${escapeHtml(n.fecha||'')}</small></div><button type="button" class="action-btn danger" data-admin-delete-notification="${n.id}" title="Eliminar"><i class="bi bi-trash"></i></button></div></div>`).join(''):'<div class="text-muted small">No hay notificaciones nuevas.</div>';
            list.querySelectorAll('[data-admin-delete-notification]').forEach(b=>b.onclick=async()=>{try{await api('eliminarNotificacion',{method:'POST',body:formBody({idNotificacion:b.dataset.adminDeleteNotification})});loadAdminNotifications();}catch(e){notify(e.message,true);}});}
            if(rows.length){ const newest=rows[0].mensaje||''; const last=window.__lastAdminNotification||''; if(last && newest!==last) notify(newest,false); window.__lastAdminNotification=newest; }
        }catch(e){ console.error(e); }
    }
    function setupAdminNotifications(){
        const btn=$('adminNotificationBtn'), menu=$('adminNotificationMenu');
        btn?.addEventListener('click',async()=>{menu?.classList.toggle('d-none');await loadAdminNotifications();});
        $('adminDeleteAllNotifications')?.addEventListener('click',async()=>{try{await api('eliminarTodasNotificaciones',{method:'POST',body:''});loadAdminNotifications();}catch(e){notify(e.message,true);}});
        document.addEventListener('click',e=>{if(menu && !menu.contains(e.target) && !btn?.contains(e.target))menu.classList.add('d-none');});
        loadAdminNotifications(); adminNotificationTimer=setInterval(loadAdminNotifications,5000);
    }

    async function loadDashboard() {
        try {
            const d = await api('dashboard');
            $('statIncome').textContent = money(d.data.ingresosMes);
            $('statOrders').textContent = d.data.pedidosHoy;
            $('statTables').textContent = d.data.mesasActivas;
            $('statClients').textContent = d.data.clientesHoy;
            $('todayText').textContent = new Date().toLocaleDateString('es-CO', {weekday:'long', day:'numeric', month:'long', year:'numeric'});
            const r = await api('reportes');
            renderIncome((r.semanal && r.semanal.ingresos) || []);
            renderRankingChart(Array.isArray(r.ranking) ? r.ranking : []);
            renderRecent();
        } catch (e) { notify(e.message, true); }
    }

    async function renderRecent() {
        try {
            const d = await api('pedidos');
            $('recentOrders').innerHTML = d.slice(0,5).map(orderRow).join('') || '<div class="loading">No hay pedidos.</div>';
        } catch (e) { $('recentOrders').innerHTML = '<div class="loading">No se pudieron cargar los pedidos.</div>'; }
    }

    function drawChart(id, type, labels, data, label) {
        if (charts[id]) charts[id].destroy();
        charts[id] = new Chart($(id), {
            type,
            data: {labels, datasets:[{label, data, borderWidth:2, tension:.35, fill:true, pointRadius:2}]},
            options: {responsive:true, maintainAspectRatio:false, plugins:{legend:{display:false}}, scales:{x:{grid:{display:false}}, y:{grid:{color:'#e8e0d8'}, beginAtZero:true}}}
        });
    }
    function renderIncome(rows) { drawChart('incomeChart','line',rows.map(x=>x.fecha.slice(5)),rows.map(x=>Number(x.valor)),'Ingresos'); }
    function renderRankingChart(rows) {
        const top = rows.slice(0,5);
        if (charts.ranking) charts.ranking.destroy();
        charts.ranking = new Chart($('rankingChart'), {
            type:'doughnut',
            data:{labels:top.map(x=>x.nombre), datasets:[{data:top.map(x=>Number(x.ingresos)), borderWidth:2}]},
            options:{cutout:'62%', plugins:{legend:{display:false}}}
        });
        $('rankingLegend').innerHTML = top.map(x=>`<div class="legend-row"><span>${escapeHtml(x.nombre)}</span><strong>${money(x.ingresos)}</strong></div>`).join('') || '<div class="loading">Sin ventas pagadas.</div>';
    }

    async function loadUsers(q = '') {
        try {
            users = await api('usuarios', {query:'q=' + encodeURIComponent(q)});
            if (roles.length === 0) roles = await api('roles');
            fillUserRoleFilter();
            renderUsers();
        } catch (e) {
            console.error('Error cargando usuarios:', e);
            $('usersBody').innerHTML = '<tr><td colspan="5" class="loading text-danger">No se pudieron cargar los usuarios. Revisa la conexión con la base de datos.</td></tr>' ;
            notify(e.message || 'No se pudieron cargar los usuarios.', true);
        }
    }

    async function createUser() {
        try {
            const payload={nombre:$('uNombre').value,apellido:$('uApellido').value,correo:$('uCorreo').value,telefono:$('uTelefono').value,direccion:$('uDireccion').value,nroDocumento:$('uDocumento').value,fechaNacimiento:$('uNacimiento').value,rolesIdRol:$('uRol').value,tipoDocumento:$('uTipoDoc').value,autorizacionDatos:$('uAutorizacion').value,contrasena:$('uContrasena').value};
            await api('crearUsuario',{method:'POST',body:formBody(payload)}); bootstrap.Modal.getInstance($('userModal')).hide(); notify('Usuario creado correctamente.'); loadUsers($('userSearch').value);
        } catch(e){notify(e.message,true);}
    }

    function fillUserRoleFilter() {
        const el = $('userRoleFilter');
        if (!el) return;
        const current = el.value;
        el.innerHTML = '<option value="">Todos los roles</option>' + roles.map(r => `<option value="${r.id}">${escapeHtml(r.nombre)}</option>`).join('');
        el.value = current;
    }

    function renderUsers() {
        const roleFilter = $('userRoleFilter')?.value || '';
        const statusFilter = $('userStatusFilter')?.value ?? '1';
        const q = ($('userSearch')?.value || '').toLowerCase().trim();
        const visible = users.filter(u => {
            const text = `${u.nombre} ${u.apellido} ${u.correo}`.toLowerCase();
            return (!q || text.includes(q)) && (!roleFilter || String(u.rolesIdRol) === String(roleFilter)) && (statusFilter === '' || String(Number(u.estado)) === String(statusFilter));
        });
        $('usersBody').innerHTML = visible.map(u => `<tr>
            <td><div class="user-name">${escapeHtml(u.nombre)} ${escapeHtml(u.apellido)}</div><small class="user-mail">Doc. ${escapeHtml(u.nroDocumento)}</small></td>
            <td><span class="user-mail">${escapeHtml(u.correo)}</span></td>
            <td>${escapeHtml(u.rol)}</td>
            <td><span class="badge-status ${u.estado ? 'status-servido' : 'status-pendiente'}">${u.estado ? 'Activo' : 'Inactivo'}</span></td>
            <td><div class="row-actions">
                <button class="action-btn" title="Ver detalle" data-detail-user="${u.idUsuario}"><i class="bi bi-eye"></i></button>
                <button class="action-btn" title="Editar" data-edit-user="${u.idUsuario}"><i class="bi bi-pencil"></i></button>
                ${u.estado ? `<button class="action-btn danger" title="Eliminar" data-delete-user="${u.idUsuario}"><i class="bi bi-trash"></i></button>` : `<button class="action-btn" title="Activar" data-activate-user="${u.idUsuario}"><i class="bi bi-arrow-counterclockwise"></i></button>`}
            </div></td>
        </tr>`).join('') || '<tr><td colspan="5" class="loading">No se encontraron usuarios.</td></tr>';

        document.querySelectorAll('[data-detail-user]').forEach(b => b.onclick = () => showUserDetail(Number(b.dataset.detailUser)));
        document.querySelectorAll('[data-edit-user]').forEach(b => b.onclick = () => editUser(Number(b.dataset.editUser)));

        document.querySelectorAll('[data-delete-user]').forEach(b => b.onclick = () => changeUserState(Number(b.dataset.deleteUser), false));
        document.querySelectorAll('[data-activate-user]').forEach(b => b.onclick = () => changeUserState(Number(b.dataset.activateUser), true));
    }

    function showUserDetail(id) {
        const u = users.find(x => x.idUsuario === id);
        if (!u) return;
        const fields = [
            ['Nombre completo', `${u.nombre} ${u.apellido}`], ['Correo', u.correo], ['Teléfono', u.telefono],
            ['Dirección', u.direccion], ['Documento', u.nroDocumento], ['Tipo de documento', u.tipoDocumento],
            ['Rol', u.rol], ['Fecha de nacimiento', dateOnly(u.fechaNacimiento)], ['Último acceso', date(u.ultimoAcceso)],
            ['Autorización de datos', Number(u.autorizacionDatos) === 1 ? 'Autorizado' : 'No autorizado'],
            ['Estado', u.estado ? 'Activo' : 'Inactivo']
        ];
        $('userDetailBody').innerHTML = fields.map(([label,value]) => `<div class="detail-item"><small>${escapeHtml(label)}</small><strong>${escapeHtml(value)}</strong></div>`).join('');
        bootstrap.Modal.getOrCreateInstance($('userDetailModal')).show();
    }

    async function editUser(id) {
        const u = users.find(x => x.idUsuario === id);
        if (!u) return;
        if (roles.length === 0) roles = await api('roles');
        $('uId').value = u.idUsuario; $('uContrasena').value=''; $('uContrasena').required=false;
        $('uNombre').value = u.nombre;
        $('uApellido').value = u.apellido;
        $('uCorreo').value = u.correo;
        $('uTelefono').value = u.telefono;
        $('uDireccion').value = u.direccion;
        $('uDocumento').value = u.nroDocumento;
        $('uNacimiento').value = dateOnly(u.fechaNacimiento);
        $('uRol').innerHTML = roles.map(r => `<option value="${r.id}" ${r.id === u.rolesIdRol ? 'selected' : ''}>${escapeHtml(r.nombre)}</option>`).join('');
        $('uTipoDoc').value = u.tipoDocumentoId;
        $('uAutorizacion').value = u.autorizacionDatos;
        $('uEstado').value = u.estado ? '1' : '0';
        bootstrap.Modal.getOrCreateInstance($('userModal')).show();
    }

    async function changeUserState(id, activate) {
        confirmAction(activate ? '¿Deseas activar este usuario?' : 'El usuario se inactivará y conservará toda su información en la base de datos. ¿Deseas continuar?', async () => { try { await api(activate ? 'activarUsuario' : 'eliminarUsuario', {method:'POST', body:formBody({idUsuario:id})}); notify(activate ? 'Usuario activado.' : 'Usuario inactivado.'); await loadUsers($('userSearch').value); } catch (e) { notify(e.message, true); } });
    }

    $('userSearch')?.addEventListener('input', e => { renderUsers(); });
    $('userRoleFilter')?.addEventListener('change', renderUsers);
    $('userStatusFilter')?.addEventListener('change', renderUsers);
    $('userForm')?.addEventListener('submit', async e => {
        e.preventDefault();
        if(!$('uId').value){ createUser(); return; }
        try {
            await api('actualizarUsuario', {method:'POST', body:formBody({
                idUsuario:$('uId').value, nombre:$('uNombre').value, apellido:$('uApellido').value,
                correo:$('uCorreo').value, telefono:$('uTelefono').value, direccion:$('uDireccion').value,
                nroDocumento:$('uDocumento').value, fechaNacimiento:$('uNacimiento').value, rolesIdRol:$('uRol').value,
                tipoDocumento:$('uTipoDoc').value, autorizacionDatos:$('uAutorizacion').value, estado:$('uEstado').value
            })});
            bootstrap.Modal.getInstance($('userModal')).hide();
            notify('Usuario actualizado correctamente.');
            loadUsers($('userSearch').value);
        } catch (e) { notify(e.message, true); }
    });

    async function loadCategoriesAdmin() {
        try { categories = await api('categorias'); const box=$('categoriesAdminList'); if(!box)return; box.innerHTML=categories.map(c=>`<div class="category-admin-row"><div class="category-admin-name"><span class="category-admin-icon category-emoji">${escapeHtml(categoryIconClass(c.icono))}</span><div><strong>${escapeHtml(c.nombre)}</strong><small>Estado: Activa</small></div></div><div class="row-actions"><button type="button" class="action-btn" title="Editar categoría" data-edit-category="${c.id}"><i class="bi bi-pencil"></i></button><button type="button" class="action-btn danger" title="Eliminar categoría" data-delete-category="${c.id}"><i class="bi bi-trash"></i></button></div></div>`).join('') || '<p class="loading">No hay categorías activas.</p>';
            box.querySelectorAll('[data-edit-category]').forEach(b=>b.onclick=()=>editCategory(+b.dataset.editCategory));
            box.querySelectorAll('[data-delete-category]').forEach(b=>b.onclick=()=>deleteCategory(+b.dataset.deleteCategory));
        } catch(e){ notify(e.message,true); }
    }
    function renderCategoryIconPicker(selected) { const box=$('categoryIconPicker'); if(!box)return; box.innerHTML=CATEGORY_ICONS.map(icon=>`<button type="button" class="icon-choice ${icon===selected?'active':''}" data-icon="${icon}" title="Seleccionar ${icon}" aria-label="Seleccionar icono ${icon}"><span class="category-emoji">${icon}</span></button>`).join(''); box.querySelectorAll('[data-icon]').forEach(b=>b.onclick=()=>{ $('catIcono').value=b.dataset.icon; box.querySelectorAll('.icon-choice').forEach(x=>x.classList.remove('active')); b.classList.add('active'); }); }
    function editCategory(id){ const c=categories.find(x=>x.id===id); if(!c)return; $('categoryEditTitle').textContent='Editar categoría'; $('catId').value=c.id; $('catNombre').value=c.nombre; $('catIcono').value=CATEGORY_ICONS.includes(c.icono) ? c.icono : '🍓'; $('catEstado').value='1'; renderCategoryIconPicker($('catIcono').value); bootstrap.Modal.getOrCreateInstance($('categoryEditModal')).show(); }
    $('manageCategoriesBtn')?.addEventListener('click',async()=>{try{await loadCategoriesAdmin();showAdminModal('categoryModal');}catch(e){notify(e.message,true);}});
    $('newCategoryBtn')?.addEventListener('click',()=>{ $('categoryEditTitle').textContent='Nueva categoría'; $('categoryForm').reset(); $('catId').value=''; $('catIcono').value='🍓'; $('catEstado').value='1'; renderCategoryIconPicker('🍓'); showAdminModal('categoryEditModal'); });
    $('categoryForm')?.addEventListener('submit',async e=>{e.preventDefault();try{await api($('catId').value?'actualizarCategoria':'crearCategoria',{method:'POST',body:formBody({idCategoria:$('catId').value,nombre:$('catNombre').value,icono:$('catIcono').value,estado:$('catEstado').value})});const edited=!!$('catId').value; bootstrap.Modal.getInstance($('categoryEditModal'))?.hide();notify(edited?'Categoría actualizada.':'Categoría creada.');await loadCategoriesAdmin();await loadMenus();}catch(e){notify(e.message,true);}});
    async function deleteCategory(id){ confirmAction('La categoría se inactivará para conservar la información histórica. ¿Deseas continuar?', async()=>{try{await api('eliminarCategoria',{method:'POST',body:formBody({idCategoria:id})});notify('Categoría inactivada.');await loadCategoriesAdmin();await loadMenus();}catch(e){notify(e.message,true);}}); }

    async function loadMenus() {
        if (categories.length === 0) categories = await api('categorias');
        try { menus = await api('menus'); renderMenus(); }
        catch (e) { notify(e.message, true); }
    }
    function renderMenus() {
        $('menuBody').innerHTML = menus.map(m => `<tr>
            <td><div class="user-name">${escapeHtml(m.nombre)}</div><small class="user-mail">${escapeHtml(m.descripcion)}</small></td>
            <td>${escapeHtml(m.categoria)}</td><td>${money(m.precio)}</td><td>${m.disponible ? 'Sí' : 'No'}</td>
            <td><span class="badge-status ${m.estado ? 'status-servido' : 'status-pendiente'}">${m.estado ? 'Activo' : 'Inactivo'}</span></td>
            <td><div class="row-actions"><button class="action-btn" title="Editar" data-edit-menu="${m.idMenu}"><i class="bi bi-pencil"></i></button>
            ${m.estado ? `<button class="action-btn danger" title="Eliminar" data-delete-menu="${m.idMenu}"><i class="bi bi-trash"></i></button>` : `<button class="action-btn" title="Activar" data-activate-menu="${m.idMenu}"><i class="bi bi-arrow-counterclockwise"></i></button>`}</div></td>
        </tr>`).join('') || '<tr><td colspan="6" class="loading">No hay platos registrados.</td></tr>';
        document.querySelectorAll('[data-edit-menu]').forEach(b => b.onclick = () => editMenu(+b.dataset.editMenu));
        document.querySelectorAll('[data-delete-menu]').forEach(b => b.onclick = () => changeMenuState(+b.dataset.deleteMenu,false));
        document.querySelectorAll('[data-activate-menu]').forEach(b => b.onclick = () => changeMenuState(+b.dataset.activateMenu,true));
    }
    function normalizeAdminImage(path) { if(!path)return `${CTX}/vista/assets/menu/placeholder.svg`; if(/^https?:\/\//i.test(path))return path; if(path.startsWith('/'))return path; return `${CTX}/${path.replace(/^\.?\//,'')}`; }
    function updateMenuImagePreview(path) { const img=$('mImagenPreview'); if(!img)return; img.src=normalizeAdminImage(path); img.onerror=()=>{img.src=`${CTX}/vista/assets/menu/placeholder.svg`;}; }
    async function editMenu(id) {
        const m = menus.find(x => x.idMenu === id); if (!m) return;
        $('menuModalTitle').textContent = 'Editar plato'; $('mId').value = m.idMenu;
        $('mNombre').value = m.nombre; $('mDescripcion').value = m.descripcion; await fillMenuCategories(m.idCategoria || m.categoria);
        $('mPrecio').value = m.precio; $('mPuntos').value = m.puntosFidelizacion || 0; $('mImagenArchivo').value = ''; $('mImagenNombre').textContent = m.imagen ? 'Imagen actual conservada' : 'Ninguna imagen seleccionada'; $('mImagenActual').value = m.imagen || ''; updateMenuImagePreview(m.imagen); $('mDisponible').value = m.disponible ? '1' : '0'; $('mEstado').value = m.estado ? '1' : '0';
        bootstrap.Modal.getOrCreateInstance($('menuModal')).show();
    }
    async function fillMenuCategories(selected) { const el=$('mCategoria'); if(!el)return; el.innerHTML=categories.map(c=>`<option value="${c.id}" ${(String(c.id)===String(selected)||c.nombre===selected)?'selected':''}>${escapeHtml(c.icono)} ${escapeHtml(c.nombre)}</option>`).join(''); }
    $('newMenuBtn')?.addEventListener('click', async () => {
        $('menuModalTitle').textContent = 'Nuevo plato'; $('menuForm').reset(); $('mImagenNombre').textContent = 'Ninguna imagen seleccionada'; updateMenuImagePreview(''); await fillMenuCategories(categories.find(c=>c.nombre==='General')?.id || categories[0]?.id); $('mId').value = ''; $('mImagenActual').value=''; $('mDisponible').value = '1'; $('mEstado').value = '1';
        showAdminModal('menuModal');
    });
    $('newUserBtn')?.addEventListener('click', async () => {
        $('userModalTitle').textContent='Nuevo usuario'; $('userForm').reset(); $('uId').value=''; $('uContrasena').required=true;
        if(roles.length===0) roles=await api('roles'); $('uRol').innerHTML=roles.map(r=>`<option value="${r.id}">${escapeHtml(r.nombre)}</option>`).join('');
        showAdminModal('userModal');
    });
    $('mImagenArchivo')?.addEventListener('change', () => { const file=$('mImagenArchivo').files[0]; $('mImagenNombre').textContent=file ? file.name : ($('mImagenActual').value ? 'Imagen actual conservada' : 'Ninguna imagen seleccionada'); if(file){ const url=URL.createObjectURL(file); updateMenuImagePreview(url); } else updateMenuImagePreview($('mImagenActual').value); });
    $('menuForm')?.addEventListener('submit', async e => {
        e.preventDefault(); const editing = !!$('mId').value;
        try {
            const data=new FormData();
            data.append('idMenu',$('mId').value); data.append('nombre',$('mNombre').value); data.append('descripcion',$('mDescripcion').value); data.append('idCategoria',$('mCategoria').value); data.append('categoria',$('mCategoria').options[$('mCategoria').selectedIndex]?.text || '');
            data.append('imagenActual',$('mImagenActual').value); data.append('precio',$('mPrecio').value); data.append('puntosFidelizacion',$('mPuntos').value); data.append('disponible',$('mDisponible').value); data.append('estado',$('mEstado').value);
            const file=$('mImagenArchivo').files[0]; if(file) data.append('imagenArchivo',file);
            await api(editing ? 'actualizarMenu' : 'crearMenu', {method:'POST', body:data, multipart:true});
            bootstrap.Modal.getInstance($('menuModal')).hide(); notify(editing ? 'Plato actualizado.' : 'Plato creado.'); loadMenus();
        } catch (e) { notify(e.message, true); }
    });
    async function changeMenuState(id, activate) {
        confirmAction(activate ? '¿Deseas activar este plato?' : 'El plato se inactivará y conservará su información histórica. ¿Deseas continuar?', async()=>{try { await api(activate ? 'activarMenu' : 'eliminarMenu', {method:'POST', body:formBody({idMenu:id})}); notify(activate ? 'Plato activado.' : 'Plato inactivado.'); loadMenus(); } catch (e) { notify(e.message, true); }});
    }

    async function loadCanjeables() {
        try {
            canjeables = await api('canjeables');
            if (menus.length === 0) menus = await api('menus');
            const body = $('canjeablesBody');
            if (!body) return;
            body.innerHTML = canjeables.map(c => `<tr>
                <td><strong>${escapeHtml(c.nombre)}</strong><small class="user-mail d-block">${escapeHtml(c.descripcion || '')}</small></td>
                <td>${escapeHtml(c.producto || 'Sin producto')}</td>
                <td><strong>${Number(c.puntos || 0).toLocaleString('es-CO')} pts</strong></td>
                <td><span class="badge-status ${c.activo ? 'status-servido' : 'status-pendiente'}">${c.activo ? 'Activo' : 'Inactivo'}</span></td>
                <td><div class="row-actions"><button class="action-btn" type="button" title="Editar" data-edit-canjeable="${c.id}"><i class="bi bi-pencil"></i></button>${c.activo ? `<button class="action-btn danger" type="button" title="Desactivar" data-delete-canjeable="${c.id}"><i class="bi bi-trash"></i></button>` : ''}</div></td>
            </tr>`).join('') || '<tr><td colspan="5" class="loading">No hay canjeables configurados.</td></tr>';
            body.querySelectorAll('[data-edit-canjeable]').forEach(b => b.onclick = () => editCanjeable(Number(b.dataset.editCanjeable)));
            body.querySelectorAll('[data-delete-canjeable]').forEach(b => b.onclick = () => deleteCanjeable(Number(b.dataset.deleteCanjeable)));
        } catch (e) { notify(e.message, true); }
    }
    async function fillCanjeableMenus(selected) {
        if (menus.length === 0) menus = await api('menus');
        $('cMenu').innerHTML = '<option value="">Sin producto asociado</option>' + menus.filter(m => m.estado).map(m => `<option value="${m.idMenu}" ${String(m.idMenu) === String(selected ?? '') ? 'selected' : ''}>${escapeHtml(m.nombre)} · ${money(m.precio)}</option>`).join('');
    }
    async function editCanjeable(id) {
        const c = canjeables.find(x => Number(x.id) === id); if (!c) return;
        $('canjeableTitle').textContent = 'Editar canjeable';
        $('cId').value = c.id; $('cNombre').value = c.nombre || ''; $('cDescripcion').value = c.descripcion || '';
        $('cPuntos').value = c.puntos || 1; $('cActivo').value = c.activo ? '1' : '0';
        await fillCanjeableMenus(c.menuId);
        bootstrap.Modal.getOrCreateInstance($('canjeableModal')).show();
    }
    $('newCanjeableBtn')?.addEventListener('click', async () => {
        $('canjeableTitle').textContent = 'Nuevo canjeable'; $('canjeableForm').reset(); $('cId').value = ''; $('cActivo').value = '1'; $('cPuntos').value = '100';
        await fillCanjeableMenus(''); showAdminModal('canjeableModal');
    });
    $('canjeableForm')?.addEventListener('submit', async e => {
        e.preventDefault();
        try {
            await api('guardarCanjeable', {method:'POST', body:formBody({id:$('cId').value,nombre:$('cNombre').value,descripcion:$('cDescripcion').value,menuId:$('cMenu').value,puntos:$('cPuntos').value,activo:$('cActivo').value})});
            const edited=!!$('cId').value; bootstrap.Modal.getInstance($('canjeableModal'))?.hide(); notify(edited ? 'Canjeable actualizado.' : 'Canjeable creado.'); await loadCanjeables();
        } catch (e) { notify(e.message, true); }
    });
    async function deleteCanjeable(id) {
        confirmAction('El canjeable se desactivará y conservará su información. ¿Deseas continuar?', async()=>{try { await api('eliminarCanjeable',{method:'POST',body:formBody({id})}); notify('Canjeable desactivado.'); await loadCanjeables(); } catch(e){ notify(e.message,true); }});
    }

    async function loadTables() {
        try {
            if (mesaStates.length === 0) mesaStates = await api('estadosMesas');
            allTables = await api('mesas');
            fillOperationalFilter();
            renderTables();
        } catch (e) { notify(e.message, true); }
    }
    function fillOperationalFilter(){ const el=$('tableOperationalFilter'); if(!el)return; const current=el.value; el.innerHTML='<option value="">Todos los estados operativos</option>'+mesaStates.map(s=>`<option value="${s.id}">${escapeHtml(s.nombre)}</option>`).join(''); el.value=current; }
    function renderTables(){
            const q=($('tableSearch')?.value||'').trim(); const operational=$('tableOperationalFilter')?.value||'';
            const mesas=allTables.filter(m=>(!q||String(m.numero).includes(q))&&(!operational||String(m.idEstado)===String(operational))&&Number(m.activo)===1);
            $('tablesGrid').innerHTML = mesas.map(m => `<article class="table-card ${m.activo ? '' : 'table-inactive'}">
                <div class="table-card-top"><div class="table-number">Mesa ${m.numero}</div><span class="record-state">${m.activo ? 'Activa' : 'Inactiva'}</span></div>
                <div class="table-state ${statusTable(m.estado)}">${escapeHtml(m.estado)}</div>
                <div class="table-actions"><select data-table-state="${m.idMesa}" ${m.activo ? '' : 'disabled'}>${mesaStates.map(s => `<option value="${s.id}" ${s.id === m.idEstado ? 'selected' : ''}>${escapeHtml(s.nombre)}</option>`).join('')}</select>
                <button class="action-btn" title="Editar mesa" data-edit-table="${m.idMesa}"><i class="bi bi-pencil"></i></button>
                ${m.activo ? `<button class="action-btn danger" title="Eliminar mesa" data-delete-table="${m.idMesa}"><i class="bi bi-trash"></i></button>` : `<button class="action-btn" title="Activar mesa" data-activate-table="${m.idMesa}"><i class="bi bi-arrow-counterclockwise"></i></button>`}</div>
            </article>`).join('') || '<div class="loading">No hay mesas registradas.</div>';
            document.querySelectorAll('[data-table-state]').forEach(s => s.onchange = () => updateTableState(+s.dataset.tableState,+s.value));
            document.querySelectorAll('[data-edit-table]').forEach(b => b.onclick = () => editTable(+b.dataset.editTable));
            document.querySelectorAll('[data-delete-table]').forEach(b => b.onclick = () => changeTableState(+b.dataset.deleteTable,false));
            document.querySelectorAll('[data-activate-table]').forEach(b => b.onclick = () => changeTableState(+b.dataset.activateTable,true));
    }
    function fillTableStates(selected) {
        $('tEstadoMesa').innerHTML = mesaStates.map(s => `<option value="${s.id}" ${s.id === selected ? 'selected' : ''}>${escapeHtml(s.nombre)}</option>`).join('');
    }
    function editTable(id) {
        const card = document.querySelector(`[data-edit-table="${id}"]`)?.closest('.table-card');
        const number = card?.querySelector('.table-number')?.textContent.replace(/\D/g,'');
        const state = card?.querySelector('[data-table-state]')?.value;
        const record = card?.classList.contains('table-inactive') ? '0' : '1';
        $('tableModalTitle').textContent = 'Editar mesa'; $('tId').value = id; $('tNumero').value = number || '';
        fillTableStates(Number(state)); $('tEstadoRegistro').value = record;
        bootstrap.Modal.getOrCreateInstance($('tableModal')).show();
    }
    $('tableSearch')?.addEventListener('input', renderTables);
    $('tableOperationalFilter')?.addEventListener('change', renderTables);
    $('newTableBtn')?.addEventListener('click', () => {
        $('tableModalTitle').textContent = 'Nueva mesa'; $('tableForm').reset(); $('tId').value = '';
        fillTableStates(mesaStates[0]?.id || 1); $('tEstadoRegistro').value = '1';
        showAdminModal('tableModal');
    });
    $('tableForm')?.addEventListener('submit', async e => {
        e.preventDefault(); const editing = !!$('tId').value;
        try {
            await api(editing ? 'actualizarMesa' : 'crearMesa', {method:'POST', body:formBody({
                idMesa:$('tId').value, numero:$('tNumero').value, idEstadoMesa:$('tEstadoMesa').value, estado:$('tEstadoRegistro').value
            })});
            bootstrap.Modal.getInstance($('tableModal')).hide(); notify(editing ? 'Mesa actualizada.' : 'Mesa creada.'); loadTables(); loadDashboard();
        } catch (e) { notify(e.message, true); }
    });
    async function updateTableState(id, state) {
        try { await api('estadoMesa',{method:'POST',body:formBody({idMesa:id,idEstadoMesa:state})}); notify('Estado de mesa actualizado.'); loadTables(); loadDashboard(); }
        catch (e) { notify(e.message,true); }
    }
    async function changeTableState(id, activate) {
        confirmAction(activate ? '¿Deseas activar esta mesa?' : 'La mesa se inactivará y conservará su información. ¿Deseas continuar?', async()=>{
            try { await api(activate ? 'activarMesa' : 'eliminarMesa',{method:'POST',body:formBody({idMesa:id})}); notify(activate ? 'Mesa activada.' : 'Mesa inactivada.'); await loadTables(); loadDashboard(); }
            catch(e){ notify(e.message,true); }
        });
    }

    async function loadOrders() {
        try {
            if (orderStates.length === 0) { orderStates = await api('estadosPedidos'); $('orderFilter').innerHTML = '<option value="">Todos los estados</option>' + orderStates.map(s => `<option value="${s.id}">${escapeHtml(s.nombre)}</option>`).join(''); }
            const state = $('orderFilter').value;
            const d = await api('pedidos',{query:state ? 'estado='+state : ''});
            $('ordersBody').innerHTML = d.map(orderTableRow).join('') || '<tr><td colspan="8" class="loading">No hay pedidos.</td></tr>'; document.querySelectorAll('[data-incidencia]').forEach(b=>b.onclick=()=>{const o=d.find(x=>String(x.idPedido)===String(b.dataset.incidencia));if(!o)return;$('incidenciaPedido').textContent='#'+o.idPedido;$('incidenciaMotivo').textContent=o.incidenciaMotivo||'No especificado';$('incidenciaDetalle').textContent=o.incidenciaDetalle||'Sin detalle adicional.';bootstrap.Modal.getOrCreateInstance($('incidenciaModal')).show();});
            document.querySelectorAll('[data-order-state]').forEach(s => s.onchange = () => updateOrder(+s.dataset.orderState,+s.value));
        } catch (e) { notify(e.message,true); }
    }
    function orderTableRow(o) { const incidencia=(Number(o.idEstado)===8 || String(o.estado||'').toLowerCase().includes('incidencia')) && (o.incidenciaMotivo||o.incidenciaDetalle); return `<tr><td><strong>#${o.idPedido}</strong></td><td>${escapeHtml(o.cliente)}</td><td>Mesa ${o.numeroMesa}</td><td>${date(o.fechaPedido)}</td><td>${escapeHtml(o.productos)}</td><td><strong>${money(o.total)}</strong></td><td><select data-order-state="${o.idPedido}" class="order-state-select">${orderStates.map(s => `<option value="${s.id}" ${s.id===o.idEstado?'selected':''}>${escapeHtml(s.nombre)}</option>`).join('')}</select></td><td>${incidencia?`<button type="button" class="action-btn" data-incidencia="${o.idPedido}" title="Más información"><i class="bi bi-info-circle-fill"></i></button>`:'—'}</td></tr>`; }
    function orderRow(o) { return `<div class="order-row"><div class="order-id">#${o.idPedido}</div><div class="order-main"><strong>Mesa ${o.numeroMesa}</strong><small>${escapeHtml(o.productos)}</small></div><div class="order-total">${money(o.total)}</div><div><span class="badge-status status-${statusClass(o.estado)}">${escapeHtml(o.estado)}</span></div><div class="order-time">${date(o.fechaPedido)}</div></div>`; }
    $('orderFilter')?.addEventListener('change',loadOrders);
    async function updateOrder(id,state) { try { await api('estadoPedido',{method:'POST',body:formBody({idPedido:id,idEstadoPedido:state})}); notify('Estado del pedido actualizado.'); loadOrders(); loadDashboard(); } catch(e) { notify(e.message,true); } }


    async function loadReservas(){
        try{const rows=await api('reservas');const body=$('reservasBody');body.innerHTML=rows.length?rows.map(r=>`<tr><td>${escapeHtml(r.fecha||'')}</td><td>${escapeHtml(r.hora||'')}</td><td>${escapeHtml(r.cliente||'')}</td><td>Mesa ${r.mesa}</td><td>${r.personas}</td><td><select class="admin-inline-select" data-reserva-state="${r.id}"><option ${r.estado==='CONFIRMADA'?'selected':''}>CONFIRMADA</option><option ${r.estado==='ATENDIDA'?'selected':''}>ATENDIDA</option><option ${r.estado==='CANCELADA'?'selected':''}>CANCELADA</option></select></td><td>${escapeHtml(r.observaciones||'—')}</td></tr>`).join(''):'<tr><td colspan="7" class="loading">No hay reservas.</td></tr>';body.querySelectorAll('[data-reserva-state]').forEach(e=>e.onchange=async()=>{try{await api('estadoReserva',{method:'POST',body:formBody({idReserva:e.dataset.reservaState,estado:e.value})});notify('Estado de reserva actualizado.');}catch(err){notify(err.message,true);loadReservas();}});}catch(e){notify(e.message,true);}}
    async function loadPagos(){
        try{const rows=await api('pagos');$('pagosBody').innerHTML=rows.length?rows.map(r=>`<tr><td>${date(r.fecha)}</td><td>#${r.pedido}</td><td>${escapeHtml(r.cliente||'')}</td><td>${escapeHtml(r.metodo||'')}</td><td><strong>${money(r.monto)}</strong></td><td>${escapeHtml(r.factura||'')}</td><td><span class="badge-status ${r.confirmado?'status-pagado':'status-pendiente'}">${r.confirmado?'Confirmado':'Pendiente'}</span></td></tr>`).join(''):'<tr><td colspan="7" class="loading">No hay pagos registrados.</td></tr>';}catch(e){notify(e.message,true);}}
    async function loadFacturas(){
        try{const rows=await api('facturas');$('facturasBody').innerHTML=rows.length?rows.map(r=>`<tr><td><strong>${escapeHtml(r.numero||'')}</strong></td><td>${escapeHtml(r.fecha||'')}</td><td>#${r.pedido}</td><td>${escapeHtml(r.cliente||'')}</td><td><strong>${money(r.total)}</strong></td></tr>`).join(''):'<tr><td colspan="5" class="loading">No hay facturas.</td></tr>';}catch(e){notify(e.message,true);}}
    async function loadFidelizacion(){
        try{const rows=await api('fidelizacion');$('fidelizacionBody').innerHTML=rows.length?rows.map(r=>`<tr><td>${escapeHtml(r.cliente||'')}</td><td>${escapeHtml(r.correo||'')}</td><td><strong>${r.puntos}</strong></td><td>${r.canjes}</td></tr>`).join(''):'<tr><td colspan="4" class="loading">No hay clientes activos.</td></tr>';}catch(e){notify(e.message,true);}}
    async function loadReports() {
        try {
            const r = await api('reportes'); const data=reportPeriod==='month'?r.mensual:r.semanal;
            const label=reportPeriod==='month'?'Últimos 30 días':'Últimos 7 días';
            drawChart('ordersChart','bar',data.pedidos.map(x=>x.fecha.slice(5)),data.pedidos.map(x=>Number(x.valor)),'Pedidos');
            drawChart('dailyIncomeChart','line',data.ingresos.map(x=>x.fecha.slice(5)),data.ingresos.map(x=>Number(x.valor)),'Ingresos');
            drawChart('clientsChart','bar',data.clientes.map(x=>x.fecha.slice(5)),data.clientes.map(x=>Number(x.valor)),'Clientes');
            $('ordersPeriodLabel').textContent=label; $('incomePeriodLabel').textContent=`Pagos confirmados · ${label.toLowerCase()}`; $('clientsPeriodLabel').textContent=label;
            $('rankingBody').innerHTML = r.ranking.map((x,i)=>`<tr><td>${i+1}</td><td><strong>${escapeHtml(x.nombre)}</strong></td><td>${escapeHtml(x.categoria)}</td><td>${x.cantidadVendida}</td><td><strong>${money(x.ingresos)}</strong></td></tr>`).join('') || '<tr><td colspan="5" class="loading">No existen ventas pagadas para generar el ranking.</td></tr>';
        } catch (e) { notify(e.message,true); }
    }

    $('exportReportBtn')?.addEventListener('click',async()=>{
        try{const r=await api('reportes');const rows=[['Plato','Categoría','Cantidad vendida','Ingresos'],...r.ranking.map(x=>[x.nombre,x.categoria,x.cantidadVendida,x.ingresos])];const csv=rows.map(row=>row.map(v=>`"${String(v??'').replace(/"/g,'""')}"`).join(',')).join('\n');const blob=new Blob(["\ufeff"+csv],{type:'text/csv;charset=utf-8'});const a=document.createElement('a');a.href=URL.createObjectURL(blob);a.download=`reporte_sabor_express_${new Date().toISOString().slice(0,10)}.csv`;a.click();URL.revokeObjectURL(a.href);}catch(e){notify(e.message,true);}
    });
    document.querySelectorAll('.report-period-btn').forEach(b=>b.addEventListener('click',()=>{reportPeriod=b.dataset.period;document.querySelectorAll('.report-period-btn').forEach(x=>x.classList.toggle('active',x===b));loadReports();}));

    if ($('refreshDashboard')) $('refreshDashboard').onclick = loadDashboard;
    window.addEventListener('error', e => {
        console.error('Error JavaScript administrador:', e.error || e.message);
        if (e.error && e.error.message) notify(e.error.message, true);
    });
    window.addEventListener('unhandledrejection', e => {
        console.error('Promesa rechazada en administrador:', e.reason);
        if (e.reason?.message) notify(e.reason.message, true);
    });
    (async () => { try { await api('auth'); loadDashboard(); } catch (e) { notify(e.message, true); } })();
})();
