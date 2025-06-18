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
