const filterToggle = document.querySelector('.filter-toggle');
const filterContent = document.querySelector('.filter-content');

filterToggle.addEventListener('click', function (event) {
    event.preventDefault();
    filterContent.classList.toggle('active');
});

document.addEventListener("DOMContentLoaded", () => {
    const currentYear = new Date().getFullYear();
    const maxInput = document.getElementById("maxPunts");
    if (maxInput) {
        maxInput.max = currentYear;
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const currentYear = new Date().getFullYear();
    const minInput = document.getElementById("minPunts");
    if (minInput) {
        minInput.max = currentYear;
    }
});