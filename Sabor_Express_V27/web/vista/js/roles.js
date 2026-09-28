(function () {
  'use strict';
  const body = document.body;
  const ctx = body.dataset.context || '';
  const role = body.dataset.role || '';
  const cart = new Map();
  const MAX_PRODUCTS = 20;
  const REPARTIDOR_BASE = 'Cra. 3 #3-34, Guaduas, Cundinamarca';
  const money = value => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(value || 0);
  const escape = value => String(value == null ? '' : value).replace(/[&<>'"]/g, char => ({ '&':'&amp;', '<':'&lt;', '>':'&gt;', "'":'&#39;', '"':'&quot;' }[char]));
  const get = async action => { const response = await fetch(`${ctx}/operaciones?accion=${encodeURIComponent(action)}`, { cache:'no-store', headers:{'Accept':'application/json'} }); const data = await response.json().catch(() => null); if (!response.ok) throw new Error(data && data.mensaje ? data.mensaje : 'No fue posible cargar la información.'); if (data && !Array.isArray(data) && data.ok === false) throw new Error(data.mensaje || 'No fue posible cargar la información.'); return data; };

  const roleBadges = { admin:{icon:'⚙️',label:'ADMIN'}, cliente:{icon:'👤',label:'CLIENTE'}, mesero:{icon:'📋',label:'MESERO'}, cocina:{icon:'👨\u200d🍳',label:'COCINERO'}, repartidor:{icon:'🛵',label:'REPARTIDOR'}, cajero:{icon:'💳',label:'CAJERO'} };
  function showRoleNotice(message){ let box=document.getElementById('roleNotice'); if(!box){ box=document.createElement('div'); box.id='roleNotice'; box.className='role-notice position-fixed top-0 start-50 translate-middle-x mt-3 alert alert-warning shadow-lg'; document.body.appendChild(box); } box.textContent=message; clearTimeout(box._timer); box._timer=setTimeout(()=>box.remove(),2400); }

  function setupConfirmations() {
    let pendingForm = null;
    const modal = document.createElement('div');
    modal.className = 'modal fade'; modal.id = 'roleConfirmModal'; modal.tabIndex = -1;
    modal.innerHTML = '<div class="modal-dialog modal-dialog-centered modal-sm"><div class="modal-content role-confirm-modal"><div class="modal-header"><h5 class="modal-title">Confirmar acción</h5><button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button></div><div class="modal-body text-center"><div class="role-confirm-icon">!</div><p id="roleConfirmMessage" class="mb-0"></p></div><div class="modal-footer justify-content-center"><button type="button" class="btn btn-outline-dark rounded-pill" data-bs-dismiss="modal">Cancelar</button><button type="button" class="btn btn-primary-brand rounded-pill" id="roleConfirmAccept">Confirmar</button></div></div></div>';
    document.body.appendChild(modal);
    const instance = bootstrap.Modal.getOrCreateInstance(modal);
    document.addEventListener('submit', event => { const form=event.target.closest('form[data-confirm-message]'); if(!form)return; event.preventDefault(); pendingForm=form; document.getElementById('roleConfirmMessage').textContent=form.dataset.confirmMessage; instance.show(); });
    document.getElementById('roleConfirmAccept').addEventListener('click',()=>{ const form=pendingForm; pendingForm=null; instance.hide(); if(form) form.submit(); });
  }

  function setupRepartidorControls(){
    if(role!=='repartidor') return;
    const turnoBtn=document.getElementById('btnTurnoRepartidor');
    const availabilityBtn=document.getElementById('btnDisponibilidadRepartidor');
    const syncAvailability=async()=>{
      try{
        const state=await get('disponibilidadRepartidor');
        const online=!!state.enLinea;
        document.body.dataset.repartidorOnline=String(online);
        if(availabilityBtn){availabilityBtn.textContent=online?'🟢 En línea':'⚫ Fuera de línea';availabilityBtn.setAttribute('aria-pressed',String(online));availabilityBtn.classList.toggle('online',online);}
        document.querySelectorAll('.delivery-take-button').forEach(b=>{b.disabled=!online;b.title=online?'':'Debes ponerte En línea para tomar pedidos';});
      }catch(e){showRoleNotice(e.message);}
    };
    syncAvailability();
    availabilityBtn?.addEventListener('click',async()=>{
      const online=document.body.dataset.repartidorOnline==='true';
      try{
        const response=await fetch(`${ctx}/operaciones`,{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded;charset=UTF-8'},body:new URLSearchParams({accion:'disponibilidadRepartidor',enLinea:online?'0':'1'})});
        const data=await response.json().catch(()=>null);
        if(!response.ok) throw new Error(data?.mensaje||'No fue posible cambiar la disponibilidad.');
        await syncAvailability();
        showRoleNotice(data?.mensaje||'Disponibilidad actualizada.');
      }catch(e){showRoleNotice(e.message);}
    });
    const moneyLocal=value=>new Intl.NumberFormat('es-CO',{style:'currency',currency:'COP',maximumFractionDigits:0}).format(Number(value||0));
    turnoBtn?.addEventListener('click',async()=>{try{const r=await get('resumenRepartidor');document.getElementById('turnoEntregas').textContent=r.entregas||0;document.getElementById('turnoEfectivo').textContent=moneyLocal(r.efectivo);document.getElementById('turnoLiquidar').textContent=moneyLocal(r.liquidar);bootstrap.Modal.getOrCreateInstance(document.getElementById('turnoRepartidorModal')).show();}catch(e){showRoleNotice('No fue posible cargar el resumen de turno.');}});
    document.addEventListener('click',e=>{const finalBtn=e.target.closest('[data-finalizar-pedido]');if(finalBtn){document.getElementById('pinPedido').value=finalBtn.dataset.finalizarPedido;document.getElementById('pinEntrega').value='';bootstrap.Modal.getOrCreateInstance(document.getElementById('pinEntregaModal')).show();}const cancelBtn=e.target.closest('[data-cancelar-pedido]');if(cancelBtn){document.getElementById('cancelarPedido').value=cancelBtn.dataset.cancelarPedido;document.getElementById('motivoCancelacion').value='';bootstrap.Modal.getOrCreateInstance(document.getElementById('cancelarDomicilioModal')).show();}});
    document.getElementById('motivoCancelacion')?.addEventListener('change',e=>{const wrap=document.getElementById('otraIncidenciaWrap');const area=document.getElementById('detalleIncidencia');const other=e.target.value==='Otra incidencia';wrap?.classList.toggle('d-none',!other);if(area)area.required=other;});
    document.getElementById('pinEntregaForm')?.addEventListener('submit',e=>{const pin=document.getElementById('pinEntrega').value.trim();if(!/^\d{4}$/.test(pin)){e.preventDefault();showRoleNotice('El PIN debe tener exactamente 4 dígitos.');}});
  }

  function setupGeneralClock(){const el=document.getElementById('generalClock');if(!el)return;const tick=()=>{const d=new Date();const pad=n=>String(n).padStart(2,'0');el.textContent=`${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;};tick();setInterval(tick,1000);}
  function setupRoleBadges() {
    const meta = roleBadges[role]; if (!meta) return;
    const pill = document.querySelector('.role-pill'); if (pill) pill.textContent = `${meta.icon} ${meta.label}`;
  }
  function resolveImage(path) {
    const placeholder = `${ctx}/vista/assets/menu/placeholder.svg`;
    if (!path) return placeholder;
    if (/^https?:\/\//i.test(path) || path.startsWith('/')) return path;
    return `${ctx}/${path.replace(/^\.?\//, '')}`;
  }


  const sidebarConfig = {
    admin: { label: 'ADMINISTRADOR', links: [['📊', 'Resumen', `${ctx}/vista/admin.jsp`], ['⚙️', 'Administrar sistema', `${ctx}/vista/admin.jsp`]] },
    cliente: { label: 'CLIENTE', links: [['🍓', 'Menú', `${ctx}/Inicio#menu`], ['🛒', 'Mi carrito', '#pedidoForm'], ['📦', 'Seguimiento', '#pedidosGrid'], ['⭐', 'Fidelización', `${ctx}/Inicio#misPuntos`], ['🗂️', 'Mis pedidos', `${ctx}/MisPedidos`], ['📅', 'Reservas de clientes', `${ctx}/Reservas`]] },
    mesero: { label: 'MESERO', links: [['🍽️', 'Mesas', '#mesa'], ['🧾', 'Nuevo pedido', '#menuGrid'], ['📅', 'Reservas de clientes', `${ctx}/Reservas`], ['🗂️', 'Historial de pedidos', '#historialPedidos']] },
    cocina: { label: 'COCINERO', links: [['🔥', 'Pedidos en cocina', '#pedidosGrid'], ['🕒', 'Reloj de cocina', '#pedidosGrid']] },
    repartidor: { label: 'REPARTIDOR', links: [['🛵', 'Domicilios listos', '#pedidosGrid'], ['📍', 'Pedidos en camino', '#pedidosGrid']] },
    cajero: { label: 'CAJERO', links: [['💳', 'Pagos pendientes', '#pedidosGrid'], ['⭐', 'Puntos de fidelización', '#pedidosGrid']] }
  };
  function setupSidebar() {
    const config = sidebarConfig[role]; if (!config) return;
    const header = document.querySelector('.role-header'); if (!header) return;
    const button = document.createElement('button'); button.type = 'button'; button.className = 'sidebar-toggle'; button.setAttribute('aria-label','Abrir navegación'); button.innerHTML = '☰';
    header.prepend(button);
    const backdrop = document.createElement('div'); backdrop.className = 'sidebar-backdrop';
    const sidebar = document.createElement('aside'); sidebar.className = 'role-sidebar'; sidebar.setAttribute('aria-label','Navegación lateral');
    sidebar.innerHTML = `<div class="sidebar-head"><span class="sidebar-brand">LA BARRA DEL<br>SABOR</span><button class="sidebar-close" type="button" aria-label="Cerrar navegación">×</button></div><span class="sidebar-role">${config.label}</span><nav class="sidebar-nav">${config.links.map((link,index) => `<a class="${index === 0 ? 'active' : ''}" href="${link[2]}"><span>${link[0]}</span>${link[1]}</a>`).join('')}</nav><div class="sidebar-footer"><form action="${ctx}/CerrarSesion" method="post"><button type="submit">Cerrar sesión</button></form></div>`;
    const close = () => { sidebar.classList.remove('open'); backdrop.classList.remove('open'); button.setAttribute('aria-expanded','false'); };
    const open = () => { sidebar.classList.add('open'); backdrop.classList.add('open'); button.setAttribute('aria-expanded','true'); };
    button.addEventListener('click', open); sidebar.querySelector('.sidebar-close').addEventListener('click', close); backdrop.addEventListener('click', close); sidebar.querySelectorAll('a[href^="#"]').forEach(link => link.addEventListener('click', close));
    document.body.prepend(backdrop); document.body.prepend(sidebar);
  }

  function renderMenu(products) {
    const grid = document.getElementById('menuGrid'); if (!grid) return;
    const placeholder = `${ctx}/vista/assets/menu/placeholder.svg`;
    grid.innerHTML = products.map(product => `<article class="col-12 col-sm-6 col-xl-4"><div class="menu-card"><img class="menu-image" src="${escape(resolveImage(product.imagen))}" alt="${escape(product.nombre)}"><div class="menu-card-body"><span class="eyebrow">${escape(product.categoria)}</span><h3>${escape(product.nombre)}</h3><p class="small text-muted flex-grow-1">${escape(product.descripcion)}</p><div class="d-flex justify-content-between align-items-center"><span class="menu-price">${money(product.precio)}</span><button class="btn-add" type="button" data-product="${product.id}" aria-label="Agregar ${escape(product.nombre)}">+</button></div></div></div></article>`).join('') || '<p>No hay productos disponibles por ahora.</p>';
    grid.querySelectorAll('.menu-image').forEach(img => img.addEventListener('error', () => { img.src = placeholder; }, { once:true }));
    grid.querySelectorAll('[data-product]').forEach(button => button.addEventListener('click', () => { const product=products.find(item=>item.id===Number(button.dataset.product)); const total=[...cart.values()].reduce((sum,item)=>sum+item.quantity,0); if(total>=MAX_PRODUCTS){ showRoleNotice('El máximo por cliente es de 20 productos por pedido.'); return; } cart.set(product.id, { ...product, quantity:(cart.get(product.id)?.quantity || 0)+1 }); renderCart(); }));
  }
  function renderCart() {
    const target=document.getElementById('cartItems'); const hidden=document.getElementById('items'); const count=document.getElementById('cartCount'); const totalTarget=document.getElementById('cartTotal'); if(!target)return;
    const entries=[...cart.values()]; const total=entries.reduce((sum,item)=>sum+(Number(item.precio)*item.quantity),0);
    target.innerHTML=entries.length?entries.map(item=>`<div class="cart-line"><div><strong>${escape(item.nombre)}</strong><br><small>${money(item.precio)}</small></div><div class="d-flex align-items-center gap-1"><button class="qty-btn" type="button" data-cart="minus" data-id="${item.id}">−</button><span>${item.quantity}</span><button class="qty-btn" type="button" data-cart="plus" data-id="${item.id}">+</button></div></div>`).join(''):'<p class="text-muted">Todavía no agregas productos.</p>';
    target.querySelectorAll('[data-cart]').forEach(button=>button.addEventListener('click',()=>{const id=Number(button.dataset.id);const item=cart.get(id);if(button.dataset.cart==='plus'){const total=[...cart.values()].reduce((sum,x)=>sum+x.quantity,0);if(total>=MAX_PRODUCTS){showRoleNotice('El máximo por cliente es de 20 productos por pedido.');return;}item.quantity++;}else if(item.quantity===1)cart.delete(id);else item.quantity--;renderCart();}));
    if(hidden)hidden.value=entries.map(item=>`${item.id}:${item.quantity}`).join(','); if(count)count.textContent=entries.reduce((sum,item)=>sum+item.quantity,0); if(totalTarget)totalTarget.textContent=money(total);
  }
  function card(order) {
    const items=(order.items||[]).map(item=>`<li><strong>${escape(item.cantidad)}×</strong> ${escape(item.nombre)}</li>`).join(''); let action='';
    if(role==='cocina') action=order.estadoId===1?'iniciar':order.estadoId===2?'listo':'';
    if(role==='cajero') action=order.estadoId===3?'confirmarPago':'';
    if(role==='mesero') action=order.estadoId===5?'servido':'';
    if(role==='repartidor') action=order.estadoId===5?'tomar':order.estadoId===6?'entregado':'';
    const labels={iniciar:'Iniciar preparación',listo:'Marcar listo',servido:'Marcar listo y enviar a caja',tomar:'Tomar pedido y salir',entregado:'Confirmar entrega',confirmarPago:'Realizar pago demo'};
    const destination=order.tipo==='DOMICILIO'?{icon:'🛵',label:'DOMICILIO',detail:order.direccion || 'Dirección sin registrar'}:{icon:'🍽️',label:'MESA',detail:`Mesa ${escape(order.mesa)}`}; const where=`<div class="order-destination order-destination--${order.tipo==='DOMICILIO'?'domicilio':'mesa'}"><span class="order-destination-icon" aria-hidden="true">${destination.icon}</span><div><span class="order-destination-label">${destination.label}</span><strong>${destination.detail}</strong></div></div>`;
    const metodo = String(order.metodoPago || 'EFECTIVO').toUpperCase()==='DEMO' ? 'Efectivo' : (order.metodoPago || 'Efectivo');
    const cancelClient = role==='cliente' && order.estadoId===1 ? `<form method="post" action="${ctx}/operaciones" class="mt-2"><input type="hidden" name="accion" value="cancelarCliente"><input type="hidden" name="pedido" value="${order.id}"><button type="submit" class="btn btn-outline-danger w-100">Solicitar cancelación</button></form>` : '';
    let timer='';
    return `<article class="col-12 col-md-6 col-xl-4"><div class="order-card status-${order.estadoId}"><div class="d-flex justify-content-between gap-2"><h3>#${order.id}</h3><span class="status-label">${escape(order.estado)}</span></div>${timer}${where}<p class="order-meta">Método de pago: ${escape(metodo)}</p><ul class="item-list">${items}</ul><div class="d-flex justify-content-between fw-bold"><span>Total</span><span>${money(order.total)}</span></div>${action?`<form method="post" action="${ctx}/operaciones" class="mt-3"><input type="hidden" name="accion" value="${action==='confirmarPago'?'confirmarPago':'estado'}"><input type="hidden" name="pedido" value="${order.id}">${action!=='confirmarPago'?`<input type="hidden" name="cambio" value="${action}">`:''}<button class="btn btn-primary-brand w-100" type="submit">${labels[action]}</button></form>`:''}${cancelClient}</div></article>`;
  }
  let deliveryTab='disponibles';
  function zona(direccion){const partes=String(direccion||'').split(',');return partes[1]?partes[1].trim():(partes[0]?partes[0].trim():'Domicilio');}
  function telHref(telefono){return `tel:${String(telefono||'').replace(/[^0-9+]/g,'')}`;}
  function deliveryCard(order){
    const enRuta=order.estadoId===6; const action=enRuta?'entregado':'tomar'; const actionLabel=enRuta?'Finalizar entrega':'+ Tomar y asignar a Mi Ruta'; const availableButton=action==='tomar' ? '<button type="submit" class="btn-delivery-main delivery-take-button">'+actionLabel+'</button>' : '<button type="button" class="btn-delivery-main btn-delivery-main--done" data-finalizar-pedido="'+order.id+'">✅ '+actionLabel+'</button>';
    const payBadge=`<span class="badge-pay badge-pay--cash">💵 ${money(order.total)} (Efectivo)</span>`;
    const whatsapp=String(order.telefono||'').replace(/\D/g,''); const wa=whatsapp?`<a class="btn-whatsapp" target="_blank" rel="noopener" href="https://wa.me/57${whatsapp}" aria-label="Escribir por WhatsApp">💬 WhatsApp</a>`:'';
    const items=(order.items||[]).map(item=>`<li>${escape(item.cantidad)} × ${escape(item.nombre)}</li>`).join('');
    const observation=order.observacionesEntrega?`<div class="delivery-observation"><span class="label-caps">OBSERVACIONES</span><p>${escape(order.observacionesEntrega)}</p></div>`:'';
    const actionMarkup = action==='tomar' ? `<form method="post" action="${ctx}/operaciones"><input type="hidden" name="accion" value="estado"><input type="hidden" name="pedido" value="${order.id}"><input type="hidden" name="cambio" value="tomar">${availableButton}</form>` : availableButton;
    const cancelMarkup = enRuta ? `<button type="button" class="btn-delivery-cancel" data-cancelar-pedido="${order.id}">✕ Cancelar pedido</button>` : '';
    return `<article class="delivery-col"><div class="delivery-card"><div class="delivery-card-top"><h3>PEDIDO #${order.id}</h3><span class="chip-location">📍 ${escape(zona(order.direccion))}</span></div><div class="delivery-main-info"><div class="delivery-row"><span class="label-caps">CLIENTE</span><strong class="delivery-client">${escape(order.creador)}</strong></div><div class="delivery-address-block"><span class="label-caps">DIRECCIÓN</span><p class="delivery-address">${escape(order.direccion||'Dirección sin registrar')}</p></div>${observation}<div class="delivery-details"><span class="label-caps">PRODUCTOS</span><ul class="mb-0">${items||'<li>Sin detalle disponible</li>'}</ul></div></div><div class="delivery-side-info"><div class="delivery-payment-row">${payBadge}</div><div class="delivery-contact-actions"><a class="btn-call" href="${telHref(order.telefono)}">📞 Llamar</a>${wa}</div><div class="delivery-actions">${actionMarkup}${cancelMarkup}</div></div></div></article>`;
  }
  function renderRoutePanel(rutaOrders){
    const wrap=document.getElementById('rutaPanelWrap'); if(!wrap) return;
    const stops=rutaOrders.map(o=>o.direccion).filter(Boolean);
    const origin=REPARTIDOR_BASE; const destination=stops[stops.length-1]||origin; const waypoints=stops.length>1?stops.slice(0,-1).join('|'):'';
    const dirUrl=`https://www.google.com/maps/dir/?api=1&origin=${encodeURIComponent(origin)}&destination=${encodeURIComponent(destination+', Guaduas, Cundinamarca, Colombia')}${waypoints?'&waypoints='+encodeURIComponent(waypoints.split('|').map(x=>x+', Guaduas, Cundinamarca, Colombia').join('|')):''}`;
    wrap.innerHTML=`<div class="route-panel"><div class="route-panel-head"><span>🗺️ RUTA COMPLETA</span><span class="route-count">${rutaOrders.length} parada${rutaOrders.length===1?'':'s'}</span></div><button type="button" class="btn-open-maps" id="btnAbrirMaps"${stops.length?'':' disabled'}>🧭 Abrir recorrido en Google Maps</button><div class="route-map-frame"><div id="deliveryMap" class="delivery-map">${stops.length?'Cargando mapa de la ruta...':'No tienes pedidos en tu ruta todavía.'}</div></div>${rutaOrders.length?`<ol class="route-stops">${rutaOrders.map((o,i)=>`<li><strong>Parada ${i+1} · #${o.id}</strong> · ${escape(o.direccion)}</li>`).join('')}</ol>`:''}</div>`;
    const button=document.getElementById('btnAbrirMaps');
    if(button&&stops.length) button.addEventListener('click',()=>window.open(dirUrl,'_blank','noopener'));
    if(stops.length) renderRouteMap(rutaOrders);
  }
  async function geocodeAddress(address){
    const response=await fetch(`https://nominatim.openstreetmap.org/search?format=jsonv2&limit=1&countrycodes=co&q=${encodeURIComponent(address+', Colombia')}`,{headers:{'Accept':'application/json'}});
    if(!response.ok) throw new Error('No fue posible ubicar una dirección.');
    const rows=await response.json();
    if(rows[0]) return {lat:Number(rows[0].lat),lon:Number(rows[0].lon),fallback:false};
    const centroGuaduas={lat:5.0669,lon:-74.5947,fallback:true};
    return centroGuaduas;
  }
  async function renderRouteMap(rutaOrders){
    const el=document.getElementById('deliveryMap'); if(!el || typeof L==='undefined') return;
    try{
      const points=[];
      const basePoint=await geocodeAddress(REPARTIDOR_BASE);
      if(basePoint) points.push({...basePoint,base:true,order:{id:'BASE',direccion:REPARTIDOR_BASE}});
      for(const order of rutaOrders){ const point=await geocodeAddress(order.direccion||''); if(point) points.push({...point,order}); }
      if(!points.length){el.innerHTML='<div class="map-fallback-message">No se pudo ubicar la ruta automáticamente.<br>Usa <strong>Abrir recorrido en Google Maps</strong> para continuar.</div>';return;}
      el.innerHTML='';
      const map=L.map(el,{scrollWheelZoom:false});
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{maxZoom:19,attribution:'© OpenStreetMap'}).addTo(map);
      const latlngs=points.map(p=>[p.lat,p.lon]);
      points.forEach((p,i)=>{ const aviso=p.fallback?' <em>(zona centro de Guaduas)</em>':''; const titulo=p.base?'Base del restaurante':'Parada '+i+' · #'+p.order.id; L.marker([p.lat,p.lon]).addTo(map).bindPopup(`<strong>${titulo}</strong><br>${escape(p.order.direccion||'')}${aviso}`); });
      if(points.length>1){
        const coords=points.map(p=>`${p.lon},${p.lat}`).join(';');
        const routeResponse=await fetch(`https://router.project-osrm.org/route/v1/driving/${coords}?overview=full&geometries=geojson&steps=false`);
        if(routeResponse.ok){const routeData=await routeResponse.json(); if(routeData.routes?.[0]?.geometry) L.geoJSON(routeData.routes[0].geometry,{style:{weight:5,opacity:.8}}).addTo(map);}
      }
      map.fitBounds(L.latLngBounds(latlngs),{padding:[24,24]});
    }catch(e){el.innerHTML='<div class="map-fallback-message">No fue posible cargar el mapa interactivo.<br>Usa el botón <strong>Abrir recorrido en Google Maps</strong>.</div>';}
  }

  function renderDelivery(orders){
    const disponibles=orders.filter(o=>o.estadoId===5); const ruta=orders.filter(o=>o.estadoId===6);
    if(disponibles.length===0) deliveryTab='ruta';
    else if(deliveryTab==='ruta'&&ruta.length===0&&disponibles.length>0) deliveryTab='disponibles';
    const tabsEl=document.getElementById('deliveryTabs'); const grid=document.getElementById('pedidosGrid');
    if(tabsEl){tabsEl.innerHTML=`<div class="delivery-tabs-row"><button type="button" class="tab-btn${deliveryTab==='disponibles'?' active':''}" data-tab="disponibles">Disponibles (${disponibles.length})</button><button type="button" class="tab-btn${deliveryTab==='ruta'?' active':''}" data-tab="ruta">Mi ruta (${ruta.length})</button></div>`;
      tabsEl.querySelectorAll('[data-tab]').forEach(btn=>btn.addEventListener('click',()=>{deliveryTab=btn.dataset.tab;renderDelivery(orders);}));}
    if(grid){const activeList=deliveryTab==='ruta'?ruta:disponibles;grid.innerHTML=activeList.length?activeList.map(deliveryCard).join(''):'<p class="text-muted">No hay pedidos en esta pestaña.</p>'; const online=document.body.dataset.repartidorOnline==='true';document.querySelectorAll('.delivery-take-button').forEach(b=>{b.disabled=!online;b.title=online?'':'Debes ponerte En línea para tomar pedidos';});}
    renderRoutePanel(ruta);
  }
  async function renderDeliveryHistory(){if(role!=='repartidor')return;const el=document.getElementById('repartidorHistory');if(!el)return;try{const rows=await get('historialRepartidor');el.innerHTML=rows.length?rows.map(x=>`<div class="col-12 col-md-6 col-xl-4"><article class="order-card p-3"><div class="d-flex justify-content-between"><strong>#${x.id}</strong><span>${escape(x.estado)}</span></div><p class="small mb-1">${escape(x.fecha)}</p><p class="mb-1">📍 ${escape(x.direccion||'')}</p><strong>${money(x.total)}</strong></article></div>`).join(''):'<p class="text-muted">Aún no hay pedidos asignados a tu historial.</p>'}catch(e){el.innerHTML='<p class="text-danger">No se pudo cargar el historial.</p>'}}
  async function renderOrders(){const grid=document.getElementById('pedidosGrid');if(!grid)return;try{const orders=await get('pedidos');if(!Array.isArray(orders))throw new Error('La respuesta de pedidos no tiene un formato válido.');if(role==='repartidor'){renderDelivery(orders);return;}orders.sort((a,b)=>new Date(b.fecha)-new Date(a.fecha));grid.innerHTML=orders.length?orders.map(card).join(''):'<p class="text-muted">No hay pedidos para mostrar.</p>';}catch(error){grid.innerHTML=`<div class="alert alert-danger" role="alert">${escape(error.message)}</div>`;}}
  async function loadWaiterHistory(){if(role!=='mesero')return;const grid=document.getElementById('historialPedidosGrid');if(!grid)return;grid.innerHTML='<p class="text-muted">Cargando historial...</p>';try{const response=await fetch(`${ctx}/operaciones?accion=pedidos&historial=1`);if(!response.ok)throw new Error('No fue posible cargar el historial.');const orders=await response.json();grid.innerHTML=orders.length?orders.map(card).join(''):'<p class="text-muted">Aún no tienes comandas servidas.</p>';}catch(error){grid.innerHTML=`<p class="text-danger">${escape(error.message)}</p>`;}}
  function setupWaiterTabs(){if(role!=='mesero')return;const tabs=document.querySelectorAll('[data-waiter-tab]');const sections=document.querySelectorAll('[data-waiter-section]');if(!tabs.length)return;const show=name=>{tabs.forEach(t=>t.classList.toggle('active',t.dataset.waiterTab===name));sections.forEach(sec=>sec.classList.toggle('active',sec.dataset.waiterSection===name));if(name==='activos')renderOrders();if(name==='historial')loadWaiterHistory();};tabs.forEach(t=>t.addEventListener('click',()=>show(t.dataset.waiterTab)));show('nuevo');}
  async function loadTables() {
    const select = document.getElementById('mesa'); if (!select) return;
    const tables = await get('mesas');
    select.innerHTML = '<option value="">Selecciona una mesa disponible</option>' + tables.filter(table => table.estado === 'Disponible').map(table => `<option value="${table.id}">Mesa ${table.numero} · Disponible</option>`).join('');
  }
  function setupWaiterPayment() {
    const form = document.getElementById('pedidoForm'); const mesa = document.getElementById('mesa');
    if (!form || !mesa || document.getElementById('meseroPago')) return;
    const fieldset = document.createElement('fieldset'); fieldset.id = 'meseroPago'; fieldset.className = 'mt-3';
    fieldset.innerHTML = '<legend>Método de pago</legend><label class="d-block mb-2"><input type="radio" name="metodoPago" value="EFECTIVO" checked> Efectivo</label><small class="text-muted">El pago se confirma posteriormente en caja como pago demo.</small>';
    mesa.insertAdjacentElement('afterend', fieldset);
  }

  let clientPanelNotificationTimer=null;
  async function loadClientPanelNotifications(){
    if(role!=='cliente') return;
    const list=document.getElementById('panelClientNotifications'),badge=document.getElementById('panelNotificationBadge'),count=document.getElementById('panelNotificationCount');
    if(!list||!badge)return;
    try{
      const rows=await get('notificaciones');
      badge.textContent=rows.length;badge.classList.toggle('d-none',rows.length===0);
      if(count)count.textContent=rows.length?`${rows.length} nueva${rows.length===1?'':'s'}`:'Sin novedades';
      list.innerHTML=rows.length?rows.map(n=>`<div class="notification-admin-item"><div class="d-flex gap-2 align-items-start"><div class="flex-grow-1"><b>${escape(n.titulo||'Notificación')}</b><p>${escape(n.mensaje||'')}</p><small>${escape(n.fecha||'')}</small></div><button type="button" class="notification-admin-delete" data-delete-notification="${n.id}" title="Eliminar" aria-label="Eliminar notificación"><i class="bi bi-trash"></i></button></div></div>`).join(''):'<div class="text-muted small notification-admin-empty">No hay notificaciones nuevas.</div>';
      list.querySelectorAll('[data-delete-notification]').forEach(btn=>btn.addEventListener('click',async()=>{try{const response=await fetch(`${ctx}/operaciones`,{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded;charset=UTF-8'},body:`accion=eliminarNotificacion&idNotificacion=${encodeURIComponent(btn.dataset.deleteNotification)}`});if(!response.ok)throw new Error('No fue posible eliminar la notificación.');await loadClientPanelNotifications();}catch(e){showRoleNotice(e.message);}}));
    }catch(e){console.error(e);}
  }
  function setupClientPanelNotifications(){
    if(role!=='cliente'||!document.getElementById('panelClientNotifications'))return;
    document.getElementById('panelMarkNotificationsRead')?.addEventListener('click',async()=>{try{const response=await fetch(`${ctx}/operaciones`,{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded;charset=UTF-8'},body:'accion=marcarNotificacionesLeidas'});if(!response.ok)throw new Error('No fue posible marcar las notificaciones.');await loadClientPanelNotifications();}catch(e){showRoleNotice(e.message);}});
    document.getElementById('panelDeleteAllNotifications')?.addEventListener('click',async()=>{try{const response=await fetch(`${ctx}/operaciones`,{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded;charset=UTF-8'},body:'accion=eliminarTodasNotificaciones'});if(!response.ok)throw new Error('No fue posible eliminar las notificaciones.');await loadClientPanelNotifications();}catch(e){showRoleNotice(e.message);}});
    loadClientPanelNotifications();clientPanelNotificationTimer=setInterval(loadClientPanelNotifications,5000);
  }

  async function setupCustomer(){const typeInputs=document.querySelectorAll('input[name="tipoEntrega"]');const mesa=document.getElementById('mesaField');const address=document.getElementById('direccionField');const addressInput=document.getElementById('direccion');const payment=document.querySelectorAll('input[name="metodoPago"]');
    if(!typeInputs.length || !mesa || !address || !addressInput)return;
    const toggleType=()=>{const selected=document.querySelector('input[name="tipoEntrega"]:checked');if(!selected)return;const home=selected.value==='MESA';mesa.classList.toggle('d-none',!home);address.classList.toggle('d-none',home);addressInput.required=!home;const table=document.getElementById('mesa');if(table)table.required=home;};typeInputs.forEach(input=>input.addEventListener('change',toggleType));toggleType();payment.forEach(input=>input.addEventListener('change',()=>{}));
    try{await loadTables();}catch(error){const table=document.getElementById('mesa');if(table)table.innerHTML='<option value="">No fue posible cargar las mesas</option>';}
  }
  function usePostForLogout() { document.querySelectorAll(`a[href="${ctx}/CerrarSesion"]`).forEach(link => { const form=document.createElement('form'); form.method='post'; form.action=`${ctx}/CerrarSesion`; form.className='logout-form'; const button=document.createElement('button'); button.type='submit'; button.className=link.className; button.textContent=link.textContent; form.append(button); link.replaceWith(form); }); }
  async function init(){setupConfirmations();usePostForLogout();setupSidebar();setupRoleBadges();setupGeneralClock();setupRepartidorControls();setupClientPanelNotifications();try{if(document.getElementById('menuGrid'))renderMenu(await get('menu'));if(role==='cliente')await setupCustomer();if(role==='mesero'){setupWaiterPayment();await loadTables();setupWaiterTabs();}await renderOrders();if(role==='mesero'&&document.querySelector('[data-waiter-section=historial].active'))await loadWaiterHistory();await renderDeliveryHistory();if(role==='cajero')setInterval(renderOrders,10000);if(role==='cocina')setInterval(renderOrders,5000);if(role==='repartidor')setInterval(renderOrders,5000);}catch(error){const grid=document.getElementById('menuGrid')||document.getElementById('pedidosGrid');if(grid)grid.innerHTML=`<p class="text-danger">${escape(error.message)}</p>`;}}
  init();
}());
