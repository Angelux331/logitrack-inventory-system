const API_BASE = "http://localhost:8080";

let token = localStorage.getItem("logitrack_token");
let userNombre = localStorage.getItem("logitrack_nombre");
let userRol = localStorage.getItem("logitrack_rol") || "EMPLEADO";

const authSection = document.getElementById("authSection");
const dashboard = document.getElementById("dashboard");

const userInfo = document.getElementById("userInfo");
const userName = document.getElementById("userName");
const userRole = document.getElementById("userRole");
const userAvatar = document.getElementById("userAvatar");

const modal = document.getElementById("modal");
const modalTitle = document.getElementById("modalTitle");
const modalBody = document.getElementById("modalBody");

// =====================================================
// DASHBOARD
// =====================================================

function showDashboard() {
    authSection.classList.add("hidden");
    dashboard.classList.remove("hidden");

    userInfo.classList.remove("hidden");

    userName.textContent = userNombre || "Usuario";
    userRole.textContent = userRol || "EMPLEADO";

    userAvatar.textContent = (userNombre || "U").charAt(0).toUpperCase();

    cargarBodegas();
    cargarProductos();
    cargarCategorias();
    cargarInventario();
    cargarMovimientos();
    cargarReportes();
    cargarAuditorias();
}

function showAuth() {
    authSection.classList.remove("hidden");
    dashboard.classList.add("hidden");
    userInfo.classList.add("hidden");
}

// =====================================================
// TABS LOGIN / REGISTRO
// =====================================================

document.querySelectorAll(".tab-btn").forEach((btn) => {
    btn.addEventListener("click", () => {
        document
            .querySelectorAll(".tab-btn")
            .forEach((b) => b.classList.remove("active"));

        document
            .querySelectorAll(".tab-content")
            .forEach((c) => c.classList.remove("active"));

        btn.classList.add("active");

        document.getElementById(btn.dataset.tab + "Form").classList.add("active");
    });
});

// =====================================================
// LOGIN
// =====================================================

document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("loginEmail").value;

    const password = document.getElementById("loginPassword").value;

    const errorEl = document.getElementById("loginError");

    errorEl.textContent = "";

    try {
        const res = await fetch(`${API_BASE}/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email,
                password,
            }),
        });

        if (!res.ok) {
            const err = await res.json();

            throw new Error(err.message || "Error al iniciar sesión");
        }

        const data = await res.json();

        token = data.token;
        userNombre = data.nombre;
        userRol = data.rol || "EMPLEADO";

        localStorage.setItem(
            "logitrack_token",
            token
        );

        localStorage.setItem(
            "logitrack_nombre",
            userNombre
        );

        localStorage.setItem(
            "logitrack_rol",
            userRol
        );

        localStorage.setItem(
            "logitrack_usuario_id",
            data.idusuario
        );

        showDashboard();
    } catch (err) {
        errorEl.textContent = err.message;
    }
});

// =====================================================
// LOGOUT
// =====================================================

document.getElementById("btnLogout").addEventListener("click", () => {
    localStorage.removeItem("logitrack_token");
    localStorage.removeItem("logitrack_nombre");
    localStorage.removeItem("logitrack_rol");

    token = null;
    userNombre = null;
    userRol = null;

    showAuth();
});

// =====================================================
// NAVEGACIÓN
// =====================================================

document.querySelectorAll(".menu-btn").forEach((btn) => {
    btn.addEventListener("click", () => {
        document
            .querySelectorAll(".menu-btn")
            .forEach((b) => b.classList.remove("active"));

        document
            .querySelectorAll(".view")
            .forEach((v) => v.classList.remove("active"));

        btn.classList.add("active");

        document.getElementById("view-" + btn.dataset.view).classList.add("active");

        switch (btn.dataset.view) {
            case "inicio":
                cargarReportes();
                break;

            case "bodegas":
                cargarBodegas();
                break;

            case "productos":
                cargarProductos();
                break;

            case "categorias":
                cargarCategorias();
                break;

            case "inventario":
                cargarInventario();
                break;

            case "movimientos":
                cargarMovimientos();
                break;

            case "reportes":
                cargarReportes();
                break;

            case "auditorias":
                cargarAuditorias();
                break;
            
            case "usuarios":
                cargarUsuarios();
                break;
        }   

    });
});

// =====================================================
// API GET
// =====================================================

async function apiGet(path) {
    try {

        const res = await fetch(`${API_BASE}${path}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (res.status === 401) {
            showAuth();
            throw new Error("Sesión expirada");
        }

        if (!res.ok) {
            throw new Error(`Error ${res.status}`);
        }

        const json = await res.json();

        return json;

    } catch (error) {
        throw error;
    }
}

// =====================================================
// BODEGAS
// =====================================================

let bodegasData = [];

async function cargarBodegas() {
    try {
        bodegasData = await apiGet("/bodegas");

        renderBodegas(bodegasData);
    } catch (e) {
        console.error(e);
    }
}

function renderBodegas(data) {
    const tabla = document.getElementById("tablaBodegas");

    tabla.innerHTML = data
        .map(
            (b) => `

        <tr>

            <td>${b.idBodega}</td>

            <td>${b.nombre}</td>

            <td>${b.ubicacion}</td>

            <td>${b.capacidad}</td>

            <td>
                <span class="badge ${b.activo ? "badge-success" : "badge-danger"
                }">
                    ${b.activo ? "Activa" : "Inactiva"}
                </span>
            </td>

            <td>

                <button
                    class="action-btn"
                    onclick="editarBodega(${b.idBodega})"
                >
                    Editar
                </button>

                <button
                    class="action-btn delete"
                    onclick="eliminarBodega(${b.idBodega})"
                >
                    Eliminar
                </button>

            </td>

        </tr>

    `,
        )
        .join("");
}

// =====================================================
// PRODUCTOS
// =====================================================

let productosData = [];

async function cargarProductos() {
    try {
        productosData = await apiGet("/productos");

        renderProductos(productosData);
    } catch (e) {
        console.error(e);
    }
}

function renderProductos(data) {
    const tabla = document.getElementById("tablaProductos");

    tabla.innerHTML = data
        .map(
            (p) => `

        <tr>

            <td>${p.idProducto}</td>

            <td>${p.nombre}</td>

            <td>
                ${p.categoria ? p.categoria.nombre : ""}
            </td>

            <td>
                $${Number(p.precio).toLocaleString()}
            </td>

            <td>

                <span class="badge ${p.activo ? "badge-success" : "badge-danger"
                }">

                    ${p.activo ? "Activo" : "Inactivo"}

                </span>

            </td>

            <td>

                <button
                    class="action-btn"
                    onclick="editarProducto(${p.idProducto})"
                >
                    Editar
                </button>

                <button
                    class="action-btn delete"
                    onclick="eliminarProducto(${p.idProducto})"
                >
                    Eliminar
                </button>

            </td>

        </tr>

    `,
        )
        .join("");
}

// =====================================================
// CATEGORÍAS
// =====================================================

async function cargarCategorias() {
    try {
        const data = await apiGet("/categorias");

        renderCategorias(data);
    } catch (e) {
        console.error(e);
    }
}

function renderCategorias(data) {
    document.getElementById("tablaCategorias").innerHTML = data
        .map(
            (c) => `

        <tr>

            <td>${c.idCategoria}</td>

            <td>${c.nombre}</td>

            <td>

                <button
                    class="action-btn"
                    onclick="editarCategoria(${c.idCategoria})"
                >
                    Editar
                </button>

                <button
                    class="action-btn delete"
                    onclick="eliminarCategoria(${c.idCategoria})"
                >
                    Eliminar
                </button>

            </td>

        </tr>

    `,
        )
        .join("");
}

// =====================================================
// INVENTARIO
// =====================================================

async function cargarInventario() {
    try {
        const data = await apiGet("/inventario");

        renderInventario(data);
    } catch (e) {
        console.error(e);
    }
}

function renderInventario(data) {
    document.getElementById("tablaInventario").innerHTML = data
        .map((i) => {
            const stock = i.stock || 0;

            let estado = "Normal";
            let clase = "badge-success";

            if (stock < 10) {
                estado = "Stock bajo";
                clase = "badge-warning";
            }

            if (stock === 0) {
                estado = "Agotado";
                clase = "badge-danger";
            }

            return `

            <tr>

                <td>
                    ${i.bodega ? i.bodega.nombre : ""}
                </td>

                <td>
                    ${i.producto ? i.producto.nombre : ""}
                </td>

                <td>
                    <strong>${stock}</strong>
                </td>

                <td>
                    <span class="badge ${clase}">
                        ${estado}
                    </span>
                </td>

            </tr>

        `;
        })
        .join("");
}

document.getElementById("btnStockBajo").addEventListener("click", async () => {
    try {
        const data = await apiGet("/inventario/stock-bajo?umbral=10");

        renderInventario(data);
    } catch (e) {
        console.error(e);
    }
});

// =====================================================
// MOVIMIENTOS
// =====================================================

async function cargarMovimientos() {
    try {
        const data = await apiGet("/movimientos");

        document.getElementById("tablaMovimientos").innerHTML = data
            .map((m) => {
                let clase = "badge-success";

                if (m.tipo === "SALIDA") clase = "badge-danger";

                if (m.tipo === "TRANSFERENCIA") clase = "badge-warning";

                return `

                <tr>

                    <td>${m.idMovimiento}</td>

                    <td>
                        ${m.fecha ? new Date(m.fecha).toLocaleString() : "-"}
                    </td>

                    <td>
                        <span class="badge ${clase}">
                            ${m.tipo}
                        </span>
                    </td>

                    <td>
                        ${m.usuario ? m.usuario.nombre : ""}
                    </td>

                    <td>
                        ${m.observacion || ""}
                    </td>

                </tr>

            `;
            })
            .join("");
    } catch (e) {
        console.error(e);
    }
}

// =====================================================
// REPORTES
// =====================================================

async function cargarReportes() {
    try {
        const datos = await apiGet("/reportes/resumen");

        document.getElementById("dashBodegas").textContent = datos.totalBodegas;

        document.getElementById("dashProductos").textContent = datos.totalProductos;

        document.getElementById("dashInventarios").textContent =
            datos.totalInventarios;

        document.getElementById("dashMovimientos").textContent =
            datos.totalMovimientos;

        document.getElementById("dashEntradas").textContent = datos.entradas;

        document.getElementById("dashSalidas").textContent = datos.salidas;

        document.getElementById("dashTransferencias").textContent =
            datos.transferencias;

        const total = datos.totalMovimientos || 1;

        const entradas = Math.round((datos.entradas / total) * 100);

        const salidas = Math.round((datos.salidas / total) * 100);

        const transferencias = Math.round((datos.transferencias / total) * 100);

        document.getElementById("porcentajeEntradas").textContent = entradas + "%";

        document.getElementById("porcentajeSalidas").textContent = salidas + "%";

        document.getElementById("porcentajeTransferencias").textContent =
            transferencias + "%";

        document.getElementById("barraEntradas").style.width = entradas + "%";

        document.getElementById("barraSalidas").style.width = salidas + "%";

        document.getElementById("barraTransferencias").style.width =
            transferencias + "%";

        document.getElementById("reportBodegas").textContent = datos.totalBodegas;

        document.getElementById("reportProductos").textContent =
            datos.totalProductos;

        document.getElementById("reportMovimientos").textContent =
            datos.totalMovimientos;

        document.getElementById("reporteJson").textContent = JSON.stringify(
            datos,
            null,
            2,
        );
    } catch (e) {
        console.error("Error cargando reportes:", e);
    }
}

// =====================================================
// MODAL
// =====================================================

function abrirModal(title, html) {
    modalTitle.textContent = title;

    modalBody.innerHTML = `<div class="modal-form">${html}</div>`;

    modal.classList.remove("hidden");
}

function cerrarModal() {
    modal.classList.add("hidden");

    modalBody.innerHTML = "";
}

document.getElementById("modalClose").addEventListener("click", cerrarModal);

modal.addEventListener("click", (e) => {
    if (e.target === modal) {
        cerrarModal();
    }
});

// =====================================================
// TEMA
// =====================================================

const themeToggle = document.getElementById("themeToggle");

const themeIcon = document.getElementById("themeIcon");

const themeText = document.getElementById("themeText");

function cargarTema() {
    const tema = localStorage.getItem("logitrack_theme");

    if (tema === "light") {
        document.body.classList.add("light");

        themeIcon.textContent = "◐";
        themeText.textContent = "Modo oscuro";
    } else {
        document.body.classList.remove("light");

        themeIcon.textContent = "☀";
        themeText.textContent = "Modo claro";
    }
}

themeToggle.addEventListener("click", () => {
    const light = document.body.classList.toggle("light");

    localStorage.setItem("logitrack_theme", light ? "light" : "dark");

    themeIcon.textContent = light ? "◐" : "☀";

    themeText.textContent = light ? "Modo oscuro" : "Modo claro";
});

// =====================================================
// BÚSQUEDAS
// =====================================================

document.getElementById("buscarBodega").addEventListener("input", (e) => {
    const texto = e.target.value.toLowerCase();

    renderBodegas(
        bodegasData.filter(
            (b) =>
                b.nombre.toLowerCase().includes(texto) ||
                b.ubicacion.toLowerCase().includes(texto),
        ),
    );
});

document.getElementById("buscarProducto").addEventListener("input", (e) => {
    const texto = e.target.value.toLowerCase();

    renderProductos(
        productosData.filter((p) => p.nombre.toLowerCase().includes(texto)),
    );
});

// =====================================================
// BOTONES PREPARADOS
// =====================================================

document.getElementById("btnNuevaBodega").addEventListener("click", () =>
    abrirModal(
        "Nueva bodega",
        `
            <label>Nombre</label>
            <input id="modalBodegaNombre">

            <label>Ubicación</label>
            <input id="modalBodegaUbicacion">

            <label>Capacidad</label>
            <input
                id="modalBodegaCapacidad"
                type="number"
            >

            <button
                class="primary-btn full"
                onclick="crearBodega()"
            >
                Crear bodega
            </button>
            `,
    ),
);

document.getElementById("btnNuevaCategoria").addEventListener("click", () =>
    abrirModal(
        "Nueva categoría",
        `
            <label>Nombre</label>

            <input id="modalCategoriaNombre">

            <button
                class="primary-btn full"
                onclick="crearCategoria()"
            >
                Crear categoría
            </button>
            `,
    ),
);

// =====================================================
// CREAR BODEGA
// =====================================================

async function crearBodega() {
    const body = {
        nombre: document.getElementById("modalBodegaNombre").value,

        ubicacion: document.getElementById("modalBodegaUbicacion").value,

        capacidad: Number(document.getElementById("modalBodegaCapacidad").value),

        activo: true,
    };

    try {
        const res = await fetch(`${API_BASE}/bodegas`, {
            method: "POST",

            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },

            body: JSON.stringify(body),
        });

        if (!res.ok) throw new Error("No se pudo crear la bodega");

        cerrarModal();

        cargarBodegas();

        cargarReportes();
    } catch (e) {
        alert(e.message);
    }
}

// =====================================================
// ELIMINAR BODEGA
// =====================================================

async function eliminarBodega(id) {
    if (!confirm("¿Seguro que deseas eliminar esta bodega?")) return;

    try {
        const res = await fetch(`${API_BASE}/bodegas/${id}`, {
            method: "DELETE",

            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (!res.ok) throw new Error("No se pudo eliminar");

        cargarBodegas();
        cargarReportes();
    } catch (e) {
        alert(e.message);
    }
}

// =====================================================
// EDITAR BODEGA
// =====================================================

async function editarBodega(id) {
    const b = bodegasData.find((x) => x.idBodega === id);

    if (!b) return;

    abrirModal(
        "Editar bodega",

        `
        <label>Nombre</label>

        <input
            id="modalBodegaNombre"
            value="${b.nombre}"
        >

        <label>Ubicación</label>

        <input
            id="modalBodegaUbicacion"
            value="${b.ubicacion}"
        >

        <label>Capacidad</label>

        <input
            id="modalBodegaCapacidad"
            type="number"
            value="${b.capacidad}"
        >

        <button
            class="primary-btn full"
            onclick="guardarBodega(${id})"
        >
            Guardar cambios
        </button>
        `,
    );
}

async function guardarBodega(id) {
    const body = {
        nombre: document.getElementById("modalBodegaNombre").value,

        ubicacion: document.getElementById("modalBodegaUbicacion").value,

        capacidad: Number(document.getElementById("modalBodegaCapacidad").value),

        activo: true,
    };

    try {
        const res = await fetch(`${API_BASE}/bodegas/${id}`, {
            method: "PUT",

            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },

            body: JSON.stringify(body),
        });

        if (!res.ok) throw new Error("No se pudo actualizar");

        cerrarModal();

        cargarBodegas();
    } catch (e) {
        alert(e.message);
    }
}

// =====================================================
// CATEGORÍAS
// =====================================================

async function crearCategoria() {
    const nombre = document.getElementById("modalCategoriaNombre").value;

    try {
        const res = await fetch(`${API_BASE}/categorias`, {
            method: "POST",

            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },

            body: JSON.stringify({
                nombre,
            }),
        });

        if (!res.ok) throw new Error("No se pudo crear la categoría");

        cerrarModal();

        cargarCategorias();
    } catch (e) {
        alert(e.message);
    }
}

// =====================================================
// EDITAR / ELIMINAR CATEGORÍA
// =====================================================

async function editarCategoria(id) {
    const categorias = await apiGet("/categorias");

    const categoria = categorias.find((c) => c.idCategoria === id);

    if (!categoria) return;

    abrirModal(
        "Editar categoría",

        `
        <label>Nombre</label>

        <input
            id="modalCategoriaNombre"
            value="${categoria.nombre}"
        >

        <button
            class="primary-btn full"
            onclick="guardarCategoria(${id})"
        >
            Guardar cambios
        </button>
        `,
    );
}

async function guardarCategoria(id) {
    const nombre = document.getElementById("modalCategoriaNombre").value;

    try {
        const res = await fetch(`${API_BASE}/categorias/${id}`, {
            method: "PUT",

            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },

            body: JSON.stringify({
                nombre,
            }),
        });

        if (!res.ok) throw new Error("No se pudo actualizar");

        cerrarModal();

        cargarCategorias();
    } catch (e) {
        alert(e.message);
    }
}

async function eliminarCategoria(id) {
    if (!confirm("¿Eliminar esta categoría?")) return;

    try {
        const res = await fetch(`${API_BASE}/categorias/${id}`, {
            method: "DELETE",

            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (!res.ok) throw new Error("No se pudo eliminar");

        cargarCategorias();
    } catch (e) {
        alert(e.message);
    }
}

// =====================================================
// PRODUCTOS
// =====================================================

document
    .getElementById("btnNuevoProducto")
    .addEventListener("click", async () => {
        const categorias = await apiGet("/categorias");

        const opciones = categorias
            .map(
                (c) => `
                    <option
                        value="${c.idCategoria}"
                    >
                        ${c.nombre}
                    </option>
                `,
            )
            .join("");

        abrirModal(
            "Nuevo producto",

            `
                <label>Nombre</label>

                <input id="modalProductoNombre">


                <label>Categoría</label>

                <select id="modalProductoCategoria">

                    ${opciones}

                </select>


                <label>Precio</label>

                <input
                    id="modalProductoPrecio"
                    type="number"
                    step="0.01"
                >


                <label>Descripción</label>

                <input id="modalProductoDescripcion">


                <button
                    class="primary-btn full"
                    onclick="crearProducto()"
                >
                    Crear producto
                </button>
                `,
        );
    });

async function crearProducto() {
    const body = {
        nombre: document.getElementById("modalProductoNombre").value,

        categoria: {
            idCategoria: Number(
                document.getElementById("modalProductoCategoria").value,
            ),
        },

        precio: Number(document.getElementById("modalProductoPrecio").value),

        descripcion: document.getElementById("modalProductoDescripcion").value,

        activo: true,
    };

    try {
        const res = await fetch(`${API_BASE}/productos`, {
            method: "POST",

            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },

            body: JSON.stringify(body),
        });

        if (!res.ok) throw new Error("No se pudo crear el producto");

        cerrarModal();

        cargarProductos();

        cargarReportes();
    } catch (e) {
        alert(e.message);
    }
}

async function eliminarProducto(id) {
    if (!confirm("¿Eliminar este producto?")) return;

    try {
        const res = await fetch(`${API_BASE}/productos/${id}`, {
            method: "DELETE",

            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (!res.ok) throw new Error("No se pudo eliminar");

        cargarProductos();

        cargarReportes();
    } catch (e) {
        alert(e.message);
    }
}

async function editarProducto(id) {
    const producto = productosData.find((p) => p.idProducto === id);

    if (!producto) return;

    const categorias = await apiGet("/categorias");

    const opciones = categorias
        .map(
            (c) => `

            <option
                value="${c.idCategoria}"
                ${producto.categoria &&
                    producto.categoria.idCategoria === c.idCategoria
                    ? "selected"
                    : ""
                }
            >
                ${c.nombre}
            </option>

        `,
        )
        .join("");

    abrirModal(
        "Editar producto",

        `
        <label>Nombre</label>

        <input
            id="modalProductoNombre"
            value="${producto.nombre}"
        >


        <label>Categoría</label>

        <select id="modalProductoCategoria">

            ${opciones}

        </select>


        <label>Precio</label>

        <input
            id="modalProductoPrecio"
            type="number"
            step="0.01"
            value="${producto.precio}"
        >


        <label>Descripción</label>

        <input
            id="modalProductoDescripcion"
            value="${producto.descripcion || ""}"
        >


        <button
            class="primary-btn full"
            onclick="guardarProducto(${id})"
        >
            Guardar cambios
        </button>
        `,
    );
}

async function guardarProducto(id) {
    const body = {
        nombre: document.getElementById("modalProductoNombre").value,

        categoria: {
            idCategoria: Number(
                document.getElementById("modalProductoCategoria").value,
            ),
        },

        precio: Number(document.getElementById("modalProductoPrecio").value),

        descripcion: document.getElementById("modalProductoDescripcion").value,

        activo: true,
    };

    try {
        const res = await fetch(`${API_BASE}/productos/${id}`, {
            method: "PUT",

            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },

            body: JSON.stringify(body),
        });

        if (!res.ok) throw new Error("No se pudo actualizar");

        cerrarModal();

        cargarProductos();
    } catch (e) {
        alert(e.message);
    }
}

// =====================================================
// TEMA + SESIÓN
// =====================================================

cargarTema();

if (token) {
    showDashboard();
} else {
    showAuth();
}

/* =========================================
   MOBILE SIDEBAR
========================================= */

const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const sidebar = document.querySelector(".sidebar");
const sidebarOverlay = document.getElementById("sidebarOverlay");

function abrirMenuMobile() {
    sidebar.classList.add("mobile-open");

    sidebarOverlay.classList.add("active");

    mobileMenuBtn.classList.add("menu-open");

    mobileMenuBtn.textContent = "‹";
}

function cerrarMenuMobile() {
    sidebar.classList.remove("mobile-open");

    sidebarOverlay.classList.remove("active");

    mobileMenuBtn.classList.remove("menu-open");

    mobileMenuBtn.textContent = "☰";
}

mobileMenuBtn.addEventListener("click", () => {
    if (sidebar.classList.contains("mobile-open")) {
        cerrarMenuMobile();
    } else {
        abrirMenuMobile();
    }
});

sidebarOverlay.addEventListener("click", () => {
    cerrarMenuMobile();
});

document.querySelectorAll(".menu-btn").forEach((btn) => {
    btn.addEventListener("click", () => {
        if (window.innerWidth <= 750) {
            cerrarMenuMobile();
        }
    });
});

// =====================================================
// REGISTRAR MOVIMIENTOS
// =====================================================

document
    .getElementById("btnNuevoMovimiento")
    .addEventListener("click", async () => {

        try {

            // Cargamos datos actualizados
            const productos = await apiGet("/productos");
            const bodegas = await apiGet("/bodegas");

            const opcionesProductos = productos
                .map(p => `
                    <option value="${p.idProducto}">
                        ${p.nombre}
                    </option>
                `)
                .join("");

            const opcionesBodegas = bodegas
                .map(b => `
                    <option value="${b.idBodega}">
                        ${b.nombre}
                    </option>
                `)
                .join("");


            abrirModal(
                "Registrar movimiento",

                `
                <div class="movement-form">

                    <label>Tipo de movimiento</label>

                    <select id="modalMovimientoTipo">

                        <option value="ENTRADA">
                            Entrada
                        </option>

                        <option value="SALIDA">
                            Salida
                        </option>

                        <option value="TRANSFERENCIA">
                            Transferencia
                        </option>

                    </select>


                    <label>Producto</label>

                    <select id="modalMovimientoProducto">

                        <option value="">
                            Seleccionar producto...
                        </option>

                        ${opcionesProductos}

                    </select>


                    <div id="campoBodegaOrigen">

                        <label>Bodega origen</label>

                        <select id="modalMovimientoOrigen">

                            <option value="">
                                Seleccionar bodega...
                            </option>

                            ${opcionesBodegas}

                        </select>

                    </div>


                    <div id="campoBodegaDestino">

                        <label>Bodega destino</label>

                        <select id="modalMovimientoDestino">

                            <option value="">
                                Seleccionar bodega...
                            </option>

                            ${opcionesBodegas}

                        </select>

                    </div>


                    <label>Cantidad</label>

                    <input
                        type="number"
                        id="modalMovimientoCantidad"
                        min="1"
                        placeholder="Ej: 10"
                    >


                    <label>Precio unitario</label>

                    <input
                        type="number"
                        id="modalMovimientoPrecio"
                        min="0"
                        step="0.01"
                        placeholder="Ej: 50000"
                    >


                    <label>Observación</label>

                    <textarea
                        id="modalMovimientoObservacion"
                        placeholder="Descripción del movimiento..."
                    ></textarea>


                    <button
                        class="primary-btn full"
                        onclick="registrarMovimiento()">

                        Registrar movimiento

                    </button>

                </div>
                `
            );


            configurarTipoMovimiento();

        } catch (e) {

            alert(
                "No se pudieron cargar los datos: "
                + e.message
            );

        }

    });

function configurarTipoMovimiento() {

    const tipo =
        document.getElementById(
            "modalMovimientoTipo"
        );

    const origen =
        document.getElementById(
            "campoBodegaOrigen"
        );

    const destino =
        document.getElementById(
            "campoBodegaDestino"
        );


    function actualizarCampos() {

        if (tipo.value === "ENTRADA") {

            origen.style.display = "none";
            destino.style.display = "block";

        }


        if (tipo.value === "SALIDA") {

            origen.style.display = "block";
            destino.style.display = "none";

        }


        if (tipo.value === "TRANSFERENCIA") {

            origen.style.display = "block";
            destino.style.display = "block";

        }

    }


    tipo.addEventListener(
        "change",
        actualizarCampos
    );


    actualizarCampos();

}

async function registrarMovimiento() {

    const tipo =
        document.getElementById(
            "modalMovimientoTipo"
        ).value;


    const productoId =
        Number(
            document.getElementById(
                "modalMovimientoProducto"
            ).value
        );


    const cantidad =
        Number(
            document.getElementById(
                "modalMovimientoCantidad"
            ).value
        );


    const precio =
        Number(
            document.getElementById(
                "modalMovimientoPrecio"
            ).value
        );


    const observacion =
        document.getElementById(
            "modalMovimientoObservacion"
        ).value;


    const usuarioId =
        Number(
            localStorage.getItem(
                "logitrack_usuario_id"
            )
        );


    if (!productoId) {

        alert("Selecciona un producto.");

        return;

    }


    if (!cantidad || cantidad <= 0) {

        alert(
            "La cantidad debe ser mayor que cero."
        );

        return;

    }


    if (!usuarioId) {

        alert(
            "No se pudo identificar al usuario."
        );

        return;

    }


    const movimiento = {

        tipo: tipo,

        usuario: {
            idusuario: usuarioId
        },

        observacion: observacion

    };


    const detalle = {

        producto: {
            idProducto: productoId
        },

        cantidad: cantidad,

        precioUnitario: precio

    };


    if (
        tipo === "ENTRADA" ||
        tipo === "TRANSFERENCIA"
    ) {

        const destino =
            Number(
                document.getElementById(
                    "modalMovimientoDestino"
                ).value
            );


        if (!destino) {

            alert(
                "Selecciona la bodega destino."
            );

            return;

        }


        movimiento.bodegaDestino = {

            idBodega: destino

        };

    }


    if (
        tipo === "SALIDA" ||
        tipo === "TRANSFERENCIA"
    ) {

        const origen =
            Number(
                document.getElementById(
                    "modalMovimientoOrigen"
                ).value
            );


        if (!origen) {

            alert(
                "Selecciona la bodega origen."
            );

            return;

        }


        movimiento.bodegaOrigen = {

            idBodega: origen

        };

    }


    if (
        tipo === "TRANSFERENCIA" &&
        movimiento.bodegaOrigen.idBodega ===
        movimiento.bodegaDestino.idBodega
    ) {

        alert(
            "La bodega origen y destino deben ser diferentes."
        );

        return;

    }


    try {

        const res = await fetch(
            `${API_BASE}/movimientos/registrar`,
            {

                method: "POST",

                headers: {

                    "Content-Type":
                        "application/json",

                    "Authorization":
                        `Bearer ${token}`

                },

                body: JSON.stringify({

                    movimiento: movimiento,

                    detalle: detalle

                })

            }
        );


        if (!res.ok) {

            let mensaje =
                "No se pudo registrar el movimiento.";

            try {

                const error =
                    await res.json();

                if (error.message) {
                    mensaje = error.message;
                }

            } catch (_) { }

            throw new Error(mensaje);

        }


        alert(
            "Movimiento registrado correctamente."
        );


        cerrarModal();


        // Actualizamos todo

        cargarMovimientos();

        cargarInventario();

        cargarReportes();

    } catch (e) {

        alert(e.message);

        console.error(
            "Error registrando movimiento:",
            e
        );

    }

}

// ==========================================
// AUDITORÍAS
// ==========================================

let auditoriasData = [];


async function cargarAuditorias() {

    const tabla = document.getElementById("tablaAuditorias");

    if (!tabla) {
        return;
    }

    try {

        const data = await apiGet("/auditorias");

        auditoriasData = Array.isArray(data) ? data : [];

        renderizarAuditorias();

    } catch (error) {

        tabla.innerHTML = `
            <tr>
                <td colspan="7" class="empty-state">
                    Error cargando auditorías:
                    ${error.message}
                </td>
            </tr>
        `;
    }
}

function renderizarAuditorias() {

    const tabla = document.getElementById("tablaAuditorias");

    if (!tabla) return;

    const texto = (
        document.getElementById("buscarAuditorias")?.value || ""
    ).toLowerCase();

    const operacion =
        document.getElementById("filtroAuditoriaOperacion")?.value || "";

    const entidad =
        document.getElementById("filtroAuditoriaEntidad")?.value || "";

    const filtradas = auditoriasData.filter(auditoria => {

        const usuario = auditoria.usuario
            ? `${auditoria.usuario.nombre || ""} ${auditoria.usuario.apellido || ""}`
            : "";

        const textoCompleto = `
            ${auditoria.idAuditoria || ""}
            ${auditoria.entidad || ""}
            ${auditoria.entidadId || ""}
            ${usuario}
            ${auditoria.tipoOperacion || ""}
        `.toLowerCase();

        const coincideTexto =
            textoCompleto.includes(texto);

        const coincideOperacion =
            !operacion ||
            auditoria.tipoOperacion === operacion;

        const coincideEntidad =
            !entidad ||
            auditoria.entidad === entidad;

        return (
            coincideTexto &&
            coincideOperacion &&
            coincideEntidad
        );
    });

    if (filtradas.length === 0) {

        tabla.innerHTML = `
            <tr>
                <td colspan="7" class="empty-state">
                    No se encontraron auditorías.
                </td>
            </tr>
        `;

        return;
    }

    tabla.innerHTML = filtradas.map(auditoria => {

        let clase = "badge-success";

        if (auditoria.tipoOperacion === "UPDATE") {
            clase = "badge-warning";
        }

        if (auditoria.tipoOperacion === "DELETE") {
            clase = "badge-danger";
        }

        const usuario = auditoria.usuario
            ? `${auditoria.usuario.nombre || ""} ${auditoria.usuario.apellido || ""}`
            : "Sistema";

        const fecha = auditoria.fechaHora
            ? new Date(auditoria.fechaHora).toLocaleString()
            : "-";

        return `
            <tr>

                <td>
                    ${auditoria.idAuditoria}
                </td>

                <td>
                    ${fecha}
                </td>

                <td>
                    <span class="badge ${clase}">
                        ${auditoria.tipoOperacion}
                    </span>
                </td>

                <td>
                    ${auditoria.entidad || "-"}
                </td>

                <td>
                    ${auditoria.entidadId || "-"}
                </td>

                <td>
                    ${usuario}
                </td>

                <td>
                    <button
                        class="secondary-btn btn-ver-auditoria"
                        data-id="${auditoria.idAuditoria}"
                    >
                        Ver detalles
                    </button>
                </td>

            </tr>
        `;

    }).join("");

    document
        .querySelectorAll(".btn-ver-auditoria")
        .forEach(btn => {

            btn.addEventListener("click", () => {

                const id = Number(btn.dataset.id);

                const auditoria = auditoriasData.find(
                    a => a.idAuditoria === id
                );

                if (auditoria) {
                    mostrarDetalleAuditoria(auditoria);
                }

            });

        });
}

function mostrarDetalleAuditoria(auditoria) {

    let valoresAnteriores = auditoria.valoresAnteriores;
    let valoresNuevos = auditoria.valoresNuevos;

    try {

        if (valoresAnteriores) {
            valoresAnteriores = JSON.stringify(
                JSON.parse(valoresAnteriores),
                null,
                2
            );
        }

    } catch (e) {
        // Si no es JSON válido, mostramos el texto original
    }

    try {

        if (valoresNuevos) {
            valoresNuevos = JSON.stringify(
                JSON.parse(valoresNuevos),
                null,
                2
            );
        }

    } catch (e) {
        // Si no es JSON válido, mostramos el texto original
    }

    abrirModal(
        `Auditoría #${auditoria.idAuditoria}`,
        `
            <div class="auditoria-detalle">

                <div class="detalle-grid">

                    <div>
                        <span class="eyebrow">
                            OPERACIÓN
                        </span>

                        <strong>
                            ${auditoria.tipoOperacion}
                        </strong>
                    </div>

                    <div>
                        <span class="eyebrow">
                            ENTIDAD
                        </span>

                        <strong>
                            ${auditoria.entidad}
                        </strong>
                    </div>

                    <div>
                        <span class="eyebrow">
                            ID ENTIDAD
                        </span>

                        <strong>
                            ${auditoria.entidadId}
                        </strong>
                    </div>

                    <div>
                        <span class="eyebrow">
                            USUARIO
                        </span>

                        <strong>
                            ${auditoria.usuario
            ? auditoria.usuario.nombre
            : "Sistema"
        }
                        </strong>
                    </div>

                </div>

                <div class="auditoria-json">

                    <h3>Valores anteriores</h3>

                    <pre>${valoresAnteriores || "Sin información"
        }</pre>

                </div>

                <div class="auditoria-json">

                    <h3>Valores nuevos</h3>

                    <pre>${valoresNuevos || "Sin información"
        }</pre>

                </div>

            </div>
        `
    );
}

document.addEventListener("DOMContentLoaded", () => {

    const buscar = document.getElementById("buscarAuditorias");
    const operacion = document.getElementById("filtroAuditoriaOperacion");
    const entidad = document.getElementById("filtroAuditoriaEntidad");

    if (buscar) {
        buscar.addEventListener("input", renderizarAuditorias);
    }

    if (operacion) {
        operacion.addEventListener("change", renderizarAuditorias);
    }

    if (entidad) {
        entidad.addEventListener("change", renderizarAuditorias);
    }

    // Cargar auditorías desde el backend
    cargarAuditorias();

});

// =====================================================
// USUARIOS
// =====================================================

async function cargarUsuarios() {

    if (userRol !== "ADMIN") {
        return;
    }

    try {

        const usuarios = await apiGet("/usuarios");

        const tabla =
            document.getElementById("tablaUsuarios");

        if (!tabla) return;

        tabla.innerHTML = usuarios.map(usuario => `

            <tr>

                <td>${usuario.idusuario}</td>

                <td>
                    ${usuario.nombre} ${usuario.apellido}
                </td>

                <td>
                    ${usuario.email}
                </td>

                <td>
                    <span class="badge badge-warning">
                        ${usuario.rol}
                    </span>
                </td>

                <td>
                    <span class="badge ${
                        usuario.activo
                            ? "badge-success"
                            : "badge-danger"
                    }">
                        ${usuario.activo ? "Activo" : "Inactivo"}
                    </span>
                </td>

            </tr>

        `).join("");

    } catch (error) {

        console.error(
            "Error cargando usuarios:",
            error
        );
    }
}


const btnCrearUsuario =
    document.getElementById("btnCrearUsuario");

if (btnCrearUsuario) {

    btnCrearUsuario.addEventListener(
        "click",
        async () => {

            const mensaje =
                document.getElementById("usuarioMensaje");

            mensaje.textContent = "";

            const usuario = {

                nombre:
                    document.getElementById(
                        "usuarioNombre"
                    ).value.trim(),

                apellido:
                    document.getElementById(
                        "usuarioApellido"
                    ).value.trim(),

                email:
                    document.getElementById(
                        "usuarioEmail"
                    ).value.trim(),

                password:
                    document.getElementById(
                        "usuarioPassword"
                    ).value,

                rol:
                    document.getElementById(
                        "usuarioRol"
                    ).value,

                activo: true
            };

            if (
                !usuario.nombre ||
                !usuario.apellido ||
                !usuario.email ||
                !usuario.password ||
                !usuario.rol
            ) {

                mensaje.textContent =
                    "Completa todos los campos.";

                return;
            }

            try {

                const response = await fetch(
                    `${API_BASE}/usuarios`,
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json",

                            "Authorization":
                                `Bearer ${token}`
                        },

                        body:
                            JSON.stringify(usuario)
                    }
                );

                if (!response.ok) {

                    const texto =
                        await response.text();

                    throw new Error(
                        texto ||
                        `Error ${response.status}`
                    );
                }

                alert(
                    "Usuario creado correctamente."
                );

                document.getElementById(
                    "usuarioNombre"
                ).value = "";

                document.getElementById(
                    "usuarioApellido"
                ).value = "";

                document.getElementById(
                    "usuarioEmail"
                ).value = "";

                document.getElementById(
                    "usuarioPassword"
                ).value = "";

                document.getElementById(
                    "usuarioRol"
                ).value = "EMPLEADO";

                cargarUsuarios();

            } catch (error) {

                console.error(error);

                mensaje.textContent =
                    "No se pudo crear el usuario.";
            }
        }
    );
}
