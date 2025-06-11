// Cambiar el fondo del navbar al hacer scroll
window.addEventListener('scroll', () => {
    const navbar = document.querySelector('.navbar');
    if (window.scrollY > 50) {
        navbar.classList.add('scrolled');
    } else {
        navbar.classList.remove('scrolled');
    }
});

// Animación al hacer scroll
const content = document.querySelector('.content');
window.addEventListener('scroll', () => {
    const contentPosition = content.getBoundingClientRect().top;
    const screenPosition = window.innerHeight / 1.5;

    if (contentPosition < screenPosition) {
        content.classList.add('visible');
    }
});

