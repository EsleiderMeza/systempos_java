// Actualizar fecha y hora en tiempo real
function actualizarFechaHora() {
    const ahora = new Date();
    const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    document.getElementById('fechaActual').textContent = ahora.toLocaleDateString('es-ES', opciones);
    document.getElementById('horaActual').textContent = ahora.toLocaleTimeString('es-ES');
}
actualizarFechaHora();
setInterval(actualizarFechaHora, 1000);
    const productos = /*[[${productos}]]*/ [];
    let carrito = [];

    function agregarProducto(id) {
        const producto = productos.find(p => p.id === id);
        if (!producto || producto.stock === 0) return;

        const existe = carrito.find(item => item.id === id);
        if (existe) {
            if (existe.cantidad < producto.stock) {
                existe.cantidad++;
            } else {
                alert('No hay suficiente stock disponible');
                return;
            }
        } else {
            carrito.push({
                id: producto.id,
                nombre: producto.nombre,
                precio: producto.precioVenta,
                cantidad: 1,
                stockDisponible: producto.stock
            });
        }

        actualizarCarrito();
    }

    function eliminarProducto(id) {
        carrito = carrito.filter(item => item.id !== id);
        actualizarCarrito();
    }

    function cambiarCantidad(id, delta) {
        const item = carrito.find(i => i.id === id);
        if (!item) return;

        const nuevaCantidad = item.cantidad + delta;
        if (nuevaCantidad <= 0) {
            eliminarProducto(id);
            return;
        }
        if (nuevaCantidad > item.stockDisponible) {
            alert('Stock insuficiente');
            return;
        }

        item.cantidad = nuevaCantidad;
        actualizarCarrito();
    }

    function actualizarCarrito() {
        const tbody = document.getElementById('carritoBody');
        const form = document.getElementById('formVenta');

        // Limpiar inputs anteriores
        form.querySelectorAll('input[name="productosIds"], input[name="cantidades"]').forEach(el => el.remove());

        if (carrito.length === 0) {
            tbody.innerHTML = `
                <div class="carrito-vacio">
                    <div class="carrito-vacio-icon">🛒</div>
                    <p>Selecciona productos para comenzar</p>
                </div>
            `;
            document.getElementById('btnFinalizar').disabled = true;
        } else {
            tbody.innerHTML = carrito.map(item => {
                const subtotal = item.precio * item.cantidad;

                // Agregar inputs ocultos al formulario
                const inputId = document.createElement('input');
                inputId.type = 'hidden';
                inputId.name = 'productosIds';
                inputId.value = item.id;
                form.appendChild(inputId);

                const inputCant = document.createElement('input');
                inputCant.type = 'hidden';
                inputCant.name = 'cantidades';
                inputCant.value = item.cantidad;
                form.appendChild(inputCant);

                return `
                    <div class="carrito-item">
                        <div class="carrito-item-info">
                            <div class="carrito-item-nombre">${item.nombre}</div>
                            <div class="carrito-item-precio">$${item.precio.toFixed(2)} × ${item.cantidad}</div>
                        </div>
                        <div class="carrito-item-controls">
                            <div class="qty-control">
                                <button type="button" class="qty-btn" onclick="cambiarCantidad(${item.id}, -1)">−</button>
                                <input type="text" class="qty-input" value="${item.cantidad}" readonly>
                                <button type="button" class="qty-btn" onclick="cambiarCantidad(${item.id}, 1)">+</button>
                            </div>
                            <button type="button" class="delete-btn" onclick="eliminarProducto(${item.id})">×</button>
                        </div>
                    </div>
                `;
            }).join('');

            document.getElementById('btnFinalizar').disabled = false;
        }

        // Calcular totales
        const subtotal = carrito.reduce((sum, item) => sum + (item.precio * item.cantidad), 0);
        const impuesto = subtotal * 0.19;
        const total = subtotal + impuesto;

        document.getElementById('subtotalGeneral').textContent = '$' + subtotal.toFixed(2);
        document.getElementById('impuestoGeneral').textContent = '$' + impuesto.toFixed(2);
        document.getElementById('totalGeneral').textContent = '$' + total.toFixed(2);
    }

    function filtrarProductos() {
        const input = document.getElementById('searchProducto').value.toUpperCase();
        const cards = document.querySelectorAll('.producto-card');

        cards.forEach(card => {
            const nombre = card.dataset.nombre.toUpperCase();
            const codigo = card.dataset.codigo.toUpperCase();

            if (nombre.includes(input) || codigo.includes(input)) {
                card.style.display = '';
            } else {
                card.style.display = 'none';
            }
        });
    }

    function toggleVista() {
    const grid = document.getElementById('ventasGrid');
    const tabla = document.getElementById('ventasTabla');
    const boton = document.getElementById('btnToggleVista');

    if (grid.style.display === 'none') {
        // Cambiar a vista de cards
        grid.style.display = 'grid';
        tabla.style.display = 'none';
        boton.innerHTML = '📊 Ver Tabla';
        boton.style.background = '#667eea';
    } else {
        // Cambiar a vista de tabla
        grid.style.display = 'none';
        tabla.style.display = 'block';
        boton.innerHTML = '🃏 Ver Cards';
        boton.style.background = '#28a745';
    }
