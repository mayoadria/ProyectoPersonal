document.addEventListener('DOMContentLoaded', () => {
    let lastScrollY = window.scrollY; // Guarda la posición inicial del scroll
    const nav = document.querySelector('.nav-container'); // Selector del header

    window.addEventListener('scroll', () => {
        if (window.scrollY > lastScrollY) {
            // Si se desplaza hacia abajo, oculta el header
            nav.classList.add('hidden');
        } else {
            // Si se desplaza hacia arriba, muestra el header
            nav.classList.remove('hidden');
        }

        lastScrollY = window.scrollY; // Actualiza la posición del scroll
    });
});


const userButton = document.querySelector('.icono_user');
const userMenu = document.querySelector('.user-menu-container');

// Agregar evento para mostrar/ocultar el menú al hacer clic en el icono
userButton.addEventListener('click', (event) => {
    userMenu.classList.toggle('active'); // Añadir o quitar la clase 'active'
    event.stopPropagation(); // Prevenir que el clic se propague y cierre el menú
});

// Cerrar el menú si se hace clic fuera de él
document.addEventListener('click', (event) => {
    if (!userMenu.contains(event.target)) {
        userMenu.classList.remove('active'); // Cerrar el menú si el clic está fuera
    }
});
