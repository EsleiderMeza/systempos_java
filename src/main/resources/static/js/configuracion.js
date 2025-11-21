    function cambiarTab(tabId) {
        // Ocultar todos los tabs
        document.querySelectorAll('.tab-content').forEach(tab => {
            tab.classList.remove('active');
        });
        document.querySelectorAll('.tab-button').forEach(btn => {
            btn.classList.remove('active');
        });

        // Mostrar tab seleccionado
        document.getElementById('tab-' + tabId).classList.add('active');
        event.target.classList.add('active');
    }

    function cambiarTema(tema) {
        const root = document.documentElement;

        switch(tema) {
            case 'dark':
                root.style.setProperty('--bg-color', '#1a1a1a');
                root.style.setProperty('--text-color', '#ffffff');
                document.body.style.background = '#1a1a1a';
                document.body.style.color = '#ffffff';
                break;
            case 'blue':
                document.querySelector('.sidebar').style.background = '#0d47a1';
                break;
            case 'green':
                document.querySelector('.sidebar').style.background = '#2e7d32';
                break;
            default:
                document.body.style.background = '#f5f7fa';
                document.body.style.color = '#333';
                document.querySelector('.sidebar').style.background = '#1976d2';
        }

        localStorage.setItem('theme', tema);
    }

    function cambiarTamanoFuente(tamano) {
        const body = document.body;

        switch(tamano) {
            case 'small':
                body.style.fontSize = '13px';
                break;
            case 'large':
                body.style.fontSize = '17px';
                break;
            default:
                body.style.fontSize = '15px';
        }

        localStorage.setItem('fontSize', tamano);
    }

    function cambiarColorSidebar(color) {
        document.querySelector('.sidebar').style.background = color;
        localStorage.setItem('sidebarColor', color);
    }

    function guardarPreferencias() {
        alert('✓ Preferencias guardadas exitosamente');
    }

    function restaurarDefecto() {
        localStorage.clear();
        document.body.style.background = '#f5f7fa';
        document.body.style.color = '#333';
        document.body.style.fontSize = '15px';
        document.querySelector('.sidebar').style.background = '#1976d2';
        document.getElementById('theme-selector').value = 'light';
        document.getElementById('font-size').value = 'normal';
        document.getElementById('sidebar-color').value = '#1976d2';
        alert('✓ Configuración restaurada a valores predeterminados');
    }

    function mostrarModalRestaurar() {
        document.getElementById('modalRestaurar').style.display = 'flex';
    }

    function cerrarModalRestaurar() {
        document.getElementById('modalRestaurar').style.display = 'none';
    }

    // Cerrar modal con ESC
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            cerrarModalRestaurar();
        }
    });

    // Scroll automático a la sección de seguridad si viene del hash
    window.addEventListener('DOMContentLoaded', function() {
        const theme = localStorage.getItem('theme');
        const fontSize = localStorage.getItem('fontSize');
        const sidebarColor = localStorage.getItem('sidebarColor');

        if (theme) {
            document.getElementById('theme-selector').value = theme;
            cambiarTema(theme);
        }

        if (fontSize) {
            document.getElementById('font-size').value = fontSize;
            cambiarTamanoFuente(fontSize);
        }

        if (sidebarColor) {
            document.getElementById('sidebar-color').value = sidebarColor;
            cambiarColorSidebar(sidebarColor);
        }

        if (window.location.hash === '#seguridad') {
            cambiarTab('seguridad');
        }
    });