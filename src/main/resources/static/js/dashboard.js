
    // Fecha actual
    const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    document.getElementById('fecha-actual').textContent = new Date().toLocaleDateString('es-ES', opciones);

    // Gráfico de ventas de la semana
    const ctx = document.getElementById('chartVentasSemana').getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'],
            datasets: [{
                label: 'Ventas ($)',
                data: [1200, 1900, 800, 1500, 2000, 2400, 1800],
                borderColor: '#667eea',
                backgroundColor: 'rgba(102, 126, 234, 0.1)',
                tension: 0.4,
                fill: true,
                borderWidth: 3
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return '$' + value;
                        }
                    }
                }
            }
        }
    });

    // Actualizar hora en tiempo real
    setInterval(() => {
        const ahora = new Date();
        const hora = ahora.toLocaleTimeString('es-ES');
        // Opcional: mostrar hora en algún lugar
    }, 1000);