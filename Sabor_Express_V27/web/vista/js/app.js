let products = [];
let topProducts = [];
const contextPath = document.body.dataset.context || "";
const hasClientSession = document.body.dataset.clientSession === "true";

async function loadProductsFromDatabase() {
    try {
        const response = await fetch("menu-catalogo");
        if (!response.ok) throw new Error("No se pudo consultar el menú.");
        const rows = await response.json();
        if (!Array.isArray(rows)) throw new Error(rows.mensaje || "No se pudo cargar el menú.");
        products = (rows || []).filter(m => m.estado !== false && m.estado !== 0 && m.disponible !== false && m.disponible !== 0).map(m => ({
            id: Number(m.idMenu), name: m.nombre, price: Number(m.precio), description: m.descripcion || "", ingredients: [],
            category: m.categoria || "General", img: normalizeImagePath(m.imagen)
        }));
        try {
            const topResponse = await fetch(`${contextPath}/menu-catalogo?topVentas=1`);
            const topRows = topResponse.ok ? await topResponse.json() : [];
            topProducts = (Array.isArray(topRows) ? topRows : []).map(m => ({
                id: Number(m.idMenu), name: m.nombre, price: Number(m.precio), description: m.descripcion || "", ingredients: [],
                category: m.categoria || "General", img: normalizeImagePath(m.imagen)
            }));
        } catch(e) { topProducts = []; }
        if (!topProducts.length) topProducts = products.slice(0, 6);
        if (totalCartQty() > MAX_CLIENT_PRODUCTS) {
            let remaining = MAX_CLIENT_PRODUCTS;
            cart = cart.map(item => { const qty=Math.min(item.qty, remaining); remaining-=qty; return {...item,qty}; }).filter(item=>item.qty>0);
            saveCart();
        }
        renderCategoryFilters();
        const grid=document.getElementById("menuGrid"); if(grid) grid.classList.remove("d-none");
        renderMenu();
        renderCart();
    } catch (error) {
        console.error(error);
        const empty = document.getElementById("emptyMenu");
        if (empty) {
            empty.classList.remove("d-none");
            empty.querySelector("h3")?.replaceChildren(document.createTextNode("No se pudo cargar el menú"));
            empty.querySelector("p")?.replaceChildren(document.createTextNode("Verifica la conexión con el servidor."));
        }
    }
}

async function renderCategoryFilters() {
    const box = document.getElementById("categoryFilters");
    if (!box) return;
    let categories = [];
    try { const r=await fetch(`${contextPath}/menu-catalogo?categorias=1`); if(r.ok) categories=await r.json(); } catch(e) {}
    if(!categories.length) categories=[...new Set(products.map(p=>p.category).filter(Boolean))].map((nombre,i)=>({id:i,nombre,icono:'🍓'}));
    box.innerHTML = `<button class="category-btn category-card active" data-category="top" type="button"><span class="category-icon category-emoji">🔥</span><span>Top ventas</span></button>` +
        `<button class="category-btn category-card" data-category="todos" type="button"><span class="category-icon category-emoji">🍽️</span><span>Todos los platos</span></button>` +
        categories.map(c => `<button class="category-btn category-card" data-category="${escapeHtml(c.nombre)}" type="button"><span class="category-icon category-emoji">${escapeHtml(categoryIconClass(c.icono))}</span><span>${escapeHtml(c.nombre)}</span></button>`).join("");
    box.querySelectorAll(".category-btn").forEach(btn => btn.addEventListener("click", () => {
        box.querySelectorAll(".category-btn").forEach(b => b.classList.remove("active"));
        btn.classList.add("active");
        activeCategory = btn.dataset.category;
        const label = document.getElementById("menuViewLabel");
        if (label) label.textContent = activeCategory === "top" ? "Top ventas" : activeCategory === "todos" ? "Todos los platos" : `Categoría · ${btn.querySelector("span:last-child")?.textContent || activeCategory}`;
        document.getElementById("menuGrid")?.classList.remove("d-none");
        renderMenu();
    }));
}

function escapeHtml(value) {
    return String(value ?? "").replace(/[&<>\"]/g, c => ({"&":"&amp;","<":"&lt;",">":"&gt;",'\"':"&quot;"}[c]));
}

let activeCategory = "top";
const MAX_CLIENT_PRODUCTS = 20;
let cart = JSON.parse(localStorage.getItem("cart_items") || "[]").map(item => ({...item, qty:Number(item.qty)||0})).filter(item => item.qty > 0);
function totalCartQty(){ return cart.reduce((sum,item)=>sum+item.qty,0); }
function normalizeImagePath(path){ if(!path)return `${contextPath}/vista/assets/menu/placeholder.svg`; if(/^https?:\/\//i.test(path))return path; if(path.startsWith("/"))return path; return `${contextPath}/${path.replace(/^\.?\//,"")}`; }

function categoryIconClass(value){ const allowed=['🍓','🍦','🥤','🥪','🍰','🍔','🍕','🍫','🧇','🍉','🍋','☕','🥭','🍪']; return allowed.includes(value) ? value : '🍓'; }

const money = (num) => new Intl.NumberFormat("es-CO", { style: "currency", currency: "COP", maximumFractionDigits: 0 }).format(num);


function renderMenu() {
    const grid = document.getElementById("menuGrid");
    const empty = document.getElementById("emptyMenu");
    const searchVal = (document.getElementById("menuSearch")?.value || "").toLowerCase().trim();

    if (!grid) return;

    const source = activeCategory === "top" ? topProducts : products;
    const filtered = source.filter(p => {
        const matchesCategory = activeCategory === "top" || activeCategory === "todos" || p.category === activeCategory;
        const matchesSearch = p.name.toLowerCase().includes(searchVal) || p.description.toLowerCase().includes(searchVal);
        return matchesCategory && matchesSearch;
    });

    if (filtered.length === 0) {
        grid.innerHTML = "";
        empty?.classList.remove("d-none");
        return;
    }

    empty?.classList.add("d-none");
    grid.innerHTML = filtered.map(p => `
        <div class="col-12 col-sm-6 col-lg-4 col-xl-3">
            <div class="card h-100 border product-card shadow-sm">
                <img src="${p.img}" class="card-img-top product-img" alt="${p.name}" loading="lazy" data-fallback="${contextPath}/vista/assets/menu/placeholder.svg">
                <div class="card-body d-flex flex-column p-3">
                    <h5 class="card-title fw-bold fs-6 mb-1 text-truncate">${p.name}</h5>
                    <p class="card-text small text-secondary flex-grow-1 line-clamp-2">${p.description}</p>
                    <div class="d-flex align-items-center justify-content-between pt-2 mt-auto border-top">
                        <strong class="fs-6 text-pink">${money(p.price)}</strong>
                        <div class="d-flex gap-2">
                            <button type="button" class="btn btn-sm btn-outline-secondary rounded-pill px-2" data-detail="${p.id}" title="Ver detalles">
                                <i class="bi bi-eye"></i>
                            </button>
                            <button type="button" class="add-btn shadow-sm" data-add="${p.id}" title="Agregar">
                                <i class="bi bi-plus-lg"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `).join("");

    grid.querySelectorAll("[data-add]").forEach(btn => {
        btn.addEventListener("click", () => addToCart(Number(btn.dataset.add)));
    });

    grid.querySelectorAll("[data-detail]").forEach(btn => {
        btn.addEventListener("click", () => showDetail(Number(btn.dataset.detail)));
    });
    grid.querySelectorAll("img[data-fallback]").forEach(image => image.addEventListener("error", () => {
        if (image.src.endsWith(image.dataset.fallback)) return;
        image.src = image.dataset.fallback;
    }));
}


function showDetail(id) {
    const product = products.find(p => p.id === id);
    if (!product) return;

    const modalEl = document.getElementById("detailModal");
    if (!modalEl) return;

    modalEl.querySelector("#detailModalLabel").textContent = product.name;
    const detailPhoto = modalEl.querySelector(".detail-photo");
    detailPhoto.src = product.img;
    detailPhoto.onerror = () => { detailPhoto.src = `${contextPath}/vista/assets/menu/placeholder.svg`; };
    modalEl.querySelector(".detail-description").textContent = product.description;
    modalEl.querySelector(".detail-price").textContent = money(product.price);
    
    const ingredientsList = modalEl.querySelector(".ingredient-list");
    ingredientsList.innerHTML = product.ingredients.map(ing => `<li><i class="bi bi-check2 text-pink me-2"></i>${ing}</li>`).join("");

    const addBtn = modalEl.querySelector("[data-detail-add]");
    if (addBtn) addBtn.dataset.detailAdd = product.id;

    bootstrap.Modal.getOrCreateInstance(modalEl).show();
}

function addToCart(id) {
    const product = products.find(p => p.id === id);
    if (!product) return;

    if (totalCartQty() >= MAX_CLIENT_PRODUCTS) { showToast("El máximo por cliente es de 20 productos por pedido."); return; }
    const existing = cart.find(item => item.id === id);
    if (existing) existing.qty += 1; else cart.push({ id, qty: 1 });

    saveCart();
    renderCart();
    showToast(`${product.name} agregado al carrito.`);
}

function changeQty(id, delta) {
    const item = cart.find(i => i.id === id);
    if (!item) return;
    if (delta > 0 && totalCartQty() >= MAX_CLIENT_PRODUCTS) { showToast("El máximo por cliente es de 20 productos por pedido."); return; }
    item.qty += delta;
    if (item.qty <= 0) {
        cart = cart.filter(i => i.id !== id);
    }

    saveCart();
    renderCart();
}

function saveCart() {
    localStorage.setItem("cart_items", JSON.stringify(cart));
}

function renderCart() {
    const itemsEl = document.getElementById("cartItems");
    const badgeEl = document.getElementById("cartBadge");
    const countLabel = document.getElementById("cartCountLabel");

    const totalQty = totalCartQty();
    if (badgeEl) badgeEl.textContent = totalQty;
    if (countLabel) countLabel.textContent = totalQty;

    if (!itemsEl) return;

    if (cart.length === 0) {
        itemsEl.innerHTML = `
            <div class="cart-empty">
                <i class="bi bi-bag-x"></i>
                <p>Tu carrito está vacío.</p>
                <small>Agrega productos para comenzar.</small>
            </div>
        `;
    } else {
        itemsEl.innerHTML = cart.map(item => {
            const prod = products.find(p => p.id === item.id);
            if (!prod) return "";
            return `
                <div class="d-flex align-items-center justify-content-between py-2 border-bottom">
                    <div class="me-2 text-truncate">
                        <h6 class="mb-0 fw-bold small text-truncate">${prod.name}</h6>
                        <small class="text-muted">${money(prod.price)} c/u</small>
                    </div>
                    <div class="d-flex align-items-center gap-2">
                        <div class="btn-group btn-group-sm border rounded-pill">
                            <button type="button" class="btn btn-light px-2" data-minus="${item.id}">-</button>
                            <span class="btn btn-light px-2 disabled text-dark fw-bold">${item.qty}</span>
                            <button type="button" class="btn btn-light px-2" data-plus="${item.id}">+</button>
                        </div>
                        <strong class="small text-pink ms-2">${money(prod.price * item.qty)}</strong>
                    </div>
                </div>
            `;
        }).join("");

        itemsEl.querySelectorAll("[data-minus]").forEach(b => b.addEventListener("click", () => changeQty(Number(b.dataset.minus), -1)));
        itemsEl.querySelectorAll("[data-plus]").forEach(b => b.addEventListener("click", () => changeQty(Number(b.dataset.plus), 1)));
    }

    const total = cart.reduce((sum, i) => sum + (products.find(p => p.id === i.id)?.price || 0) * i.qty, 0);
    const subtotalEl = document.getElementById("cartSubtotal");
    const totalEl = document.getElementById("cartTotal");

    if (subtotalEl) subtotalEl.textContent = money(total);
    if (totalEl) totalEl.textContent = money(total);
}


function showToast(text) {
    const toastEl = document.getElementById("appToast");
    if (!toastEl) return;
    document.getElementById("toastText").textContent = text;
    bootstrap.Toast.getOrCreateInstance(toastEl, { delay: 2200 }).show();
}

let notificationTimer = null;
async function loadClientNotifications() {
    if (!hasClientSession) return;
    const list=document.getElementById("clientNotifications"), badge=document.getElementById("clientNotificationBadge"), countText=document.getElementById("notificationCountText");
    if(!list || !badge) return;
    try {
        const response=await fetch(`${contextPath}/operaciones?accion=notificaciones`,{cache:"no-store"});
        if(!response.ok) throw new Error("No se pudieron cargar las notificaciones.");
        const rows=await response.json();
        badge.textContent=rows.length; badge.classList.toggle("d-none",rows.length===0);
        if(countText) countText.textContent=rows.length ? `${rows.length} nueva${rows.length===1?'':'s'}` : "Sin novedades";
        list.innerHTML=rows.length ? rows.map(n=>`<div class="notification-item"><div class="notification-icon"><i class="bi bi-check2-circle"></i></div><div><strong>${escapeHtml(n.titulo)}</strong><p>${escapeHtml(n.mensaje)}</p><small>${escapeHtml(n.fecha)}</small></div></div>`).join("") : '<div class="notification-empty"><i class="bi bi-bell-slash fs-4 d-block mb-1"></i>No tienes notificaciones nuevas.</div>';
    } catch(e) { console.error(e); }
}
function setupClientNotifications(){
    if(!hasClientSession) return;
    document.getElementById("markNotificationsRead")?.addEventListener("click",async()=>{try{await fetch(`${contextPath}/operaciones`,{method:"POST",headers:{"Content-Type":"application/x-www-form-urlencoded;charset=UTF-8"},body:"accion=marcarNotificacionesLeidas"});await loadClientNotifications();}catch(e){console.error(e);}});
    loadClientNotifications(); notificationTimer=setInterval(loadClientNotifications,5000);
}

document.addEventListener("DOMContentLoaded", () => {
    if (hasClientSession) createClientSidebar();
    document.querySelectorAll(`a[href="${contextPath}/CerrarSesion"]`).forEach(link => {
        const form = document.createElement("form");
        form.method = "post";
        form.action = `${contextPath}/CerrarSesion`;
        const button = document.createElement("button");
        button.type = "submit";
        button.className = link.className;
        button.textContent = link.textContent;
        form.append(button);
        link.replaceWith(form);
    });
    if (new URLSearchParams(window.location.search).get("pedidoEnviado") === "1") {
        cart = [];
        saveCart();
        window.history.replaceState({}, document.title, `${contextPath}/index.jsp`);
    }
    document.getElementById("menuSearch")?.addEventListener("input", renderMenu);

    document.querySelector("[data-detail-add]")?.addEventListener("click", (e) => {
        const id = Number(e.currentTarget.dataset.detailAdd);
        addToCart(id);
        bootstrap.Modal.getOrCreateInstance(document.getElementById("detailModal")).hide();
    });

    document.getElementById("checkoutBtn")?.addEventListener("click", async () => {
        if (cart.length === 0) {
            showToast("Agrega al menos un producto al carrito.");
            return;
        }
        if (!hasClientSession) {
            window.location.href = `${contextPath}/vista/login.jsp?checkout=1`;
            return;
        }
        const hiddenItems = document.getElementById("checkoutItems");
        if (hiddenItems) hiddenItems.value = cart.map(item => `${item.id}:${item.qty}`).join(",");
        await configureCheckout();
        bootstrap.Modal.getOrCreateInstance(document.getElementById("checkoutModal")).show();
    });

    document.getElementById("checkoutForm")?.addEventListener("submit", event => {
        const delivery = document.querySelector('input[name="tipoEntrega"]:checked')?.value === "DOMICILIO";
        if (delivery) {
            const value = (document.getElementById("checkoutDireccion")?.value || "").toLowerCase();
            if (!value.includes("guaduas")) {
                event.preventDefault();
                showToast("Los domicilios solo están disponibles en Guaduas, Cundinamarca.");
                document.getElementById("checkoutDireccion")?.focus();
                return;
            }
        }
        if (totalCartQty() > MAX_CLIENT_PRODUCTS) { event.preventDefault(); showToast("El máximo es de 20 productos por pedido."); }
    });

    loadProductsFromDatabase();
    if (hasClientSession) { loadPoints(); loadCustomerOrders(); setupClientNotifications(); }
});

function createClientSidebar() {
    const toggle = document.createElement("button");
    toggle.type = "button";
    toggle.className = "client-sidebar-toggle";
    toggle.setAttribute("aria-label", "Abrir navegación del cliente");
    toggle.textContent = "☰";
    const backdrop = document.createElement("div");
    backdrop.className = "client-sidebar-backdrop";
    const sidebar = document.createElement("aside");
    sidebar.className = "client-sidebar";
    sidebar.setAttribute("aria-label", "Secciones de cliente");
    sidebar.innerHTML = `<div class="client-sidebar-head"><strong>LA BARRA DEL<br>SABOR</strong><button type="button" aria-label="Cerrar navegación">×</button></div><span class="client-sidebar-role">CLIENTE</span><nav><a href="#menu">🍓 Menú</a><button type="button" data-cart-open>🛒 Mi carrito</button><a href="#seguimientoPedido">📦 Seguimiento del pedido</a><a href="#misPuntos">⭐ Fidelización</a><a href="#historialPedidos">🗂️ Historial de pedidos</a></nav><form action="${contextPath}/CerrarSesion" method="post"><button type="submit" class="client-logout">Cerrar sesión</button></form>`;
    const close = () => { sidebar.classList.remove("open"); backdrop.classList.remove("open"); };
    toggle.addEventListener("click", () => { sidebar.classList.add("open"); backdrop.classList.add("open"); });
    sidebar.querySelector(".client-sidebar-head button").addEventListener("click", close);
    backdrop.addEventListener("click", close);
    sidebar.querySelectorAll("a").forEach(link => link.addEventListener("click", close));
    sidebar.querySelector("[data-cart-open]").addEventListener("click", () => { close(); const toggle=document.getElementById('cartToggle'); if(toggle){ bootstrap.Dropdown.getOrCreateInstance(toggle).show(); } });
    document.body.prepend(backdrop); document.body.prepend(sidebar); document.querySelector(".site-nav .container")?.prepend(toggle);
}

function orderCard(order) {
    const items = (order.items || []).map(item => `<li>${escapeHtml(item.cantidad)} × ${escapeHtml(item.nombre)}</li>`).join("");
    const location = order.tipo === "DOMICILIO" ? `Domicilio: ${escapeHtml(order.direccion || "Sin dirección")}` : `Mesa ${escapeHtml(order.mesa)}`;
    const pin = order.tipo === "DOMICILIO" && Number(order.estadoId) === 6 && order.codigoEntrega ? `<div class="alert alert-warning py-2 mb-2"><strong>PIN de entrega:</strong> <span class="fs-5 fw-bold">${escapeHtml(order.codigoEntrega)}</span><small class="d-block">Compártelo con el repartidor al recibir tu pedido.</small></div>` : '';
    return `<article class="col-12 col-md-6 col-lg-4"><div class="customer-order-card"><div class="d-flex justify-content-between gap-2"><strong>Pedido #${escapeHtml(order.id)}</strong><span>${escapeHtml(order.estado)}</span></div><p>${location}</p>${pin}<ul>${items}</ul><div class="d-flex justify-content-between"><span>${escapeHtml(String(order.metodoPago || 'Efectivo').toUpperCase()==='DEMO' ? 'Efectivo' : (order.metodoPago || 'Efectivo'))}</span><strong>${money(order.total)}</strong></div></div></article>`;
}

async function loadCustomerOrders() {
    const tracking = document.getElementById("seguimientoGrid");
    const history = document.getElementById("historialPedidosGrid");
    if (!tracking || !history) return;
    try {
        const response = await fetch(`${contextPath}/operaciones?accion=pedidos`);
        if (!response.ok) throw new Error("No fue posible cargar tus pedidos.");
        const orders = await response.json();
        const completed = orders.filter(order => [3, 7].includes(Number(order.estadoId)));
        const active = orders.filter(order => ![3, 7].includes(Number(order.estadoId)));
        tracking.innerHTML = active.length ? active.map(orderCard).join("") : '<p class="text-muted">No tienes pedidos activos.</p>';
        history.innerHTML = completed.length ? completed.map(orderCard).join("") : '<p class="text-muted">Todavía no tienes pedidos finalizados.</p>';
    } catch (error) {
        tracking.innerHTML = `<p class="text-danger">${escapeHtml(error.message)}</p>`;
        history.innerHTML = `<p class="text-danger">${escapeHtml(error.message)}</p>`;
    }
}

async function loadPoints() {
    const balance = document.getElementById("puntosSaldo");
    const history = document.getElementById("puntosHistorial");
    if (!balance || !history) return;
    try {
        const response = await fetch(`${contextPath}/operaciones?accion=puntos`);
        if (!response.ok) throw new Error("No fue posible cargar tus puntos.");
        const data = await response.json();
        balance.textContent = data.saldo || 0;
        const rows = data.historial || [];
        history.innerHTML = rows.length ? rows.map(row => `<div class="d-flex justify-content-between gap-3 py-2 border-bottom"><div><strong>${escapeHtml(row.tipo)}</strong><br><small class="text-muted">${escapeHtml(row.fecha)}</small></div><div class="text-end"><strong class="text-pink">${row.puntos > 0 ? '+' : ''}${row.puntos}</strong><br><small>${row.restantes} disponibles</small></div></div>`).join("") : '<p class="text-muted">Aún no tienes movimientos de puntos.</p>';
    } catch (error) {
        history.innerHTML = `<p class="text-danger">${escapeHtml(error.message)}</p>`;
    }
}

async function configureCheckout() {
    const mesa=document.getElementById("checkoutMesa");
    try {
        const response=await fetch(`${contextPath}/operaciones?accion=mesas`);
        if(!response.ok) throw new Error("No se pudo cargar la información de mesas.");
        const mesas=await response.json();
        mesa.innerHTML=`<option value="">Selecciona una mesa disponible</option>`+mesas.filter(item=>item.estado==="Disponible").map(item=>`<option value="${item.id}">Mesa ${item.numero} · Disponible</option>`).join("");
        configureCheckoutFields();
    } catch(error) { mesa.innerHTML=`<option value="">No fue posible cargar las mesas</option>`; }
}

function configureCheckoutFields() {
    const mesaField = document.getElementById("checkoutMesaField");
    const addressField = document.getElementById("checkoutDireccionField");
    const mesa = document.getElementById("checkoutMesa");
    const address = document.getElementById("checkoutDireccion");
    const obsField = document.getElementById("checkoutObservacionesField");
    const obs = document.getElementById("checkoutObservaciones");
    const toggleDelivery = () => {
        const delivery = document.querySelector('input[name="tipoEntrega"]:checked').value === "DOMICILIO";
        mesaField.classList.toggle("d-none", delivery);
        addressField.classList.toggle("d-none", !delivery);
        obsField?.classList.toggle("d-none", !delivery);
        mesa.required = !delivery;
        address.required = delivery;
    };
    document.querySelectorAll('input[name="tipoEntrega"]').forEach(input => input.onchange = toggleDelivery);
    toggleDelivery();
}
