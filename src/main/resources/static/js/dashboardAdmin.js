const filterToggle = document.querySelector('.filter-toggle');
const filterContent = document.querySelector('.filter-content');

filterToggle.addEventListener('click', function (event) {
    event.preventDefault();
    filterContent.classList.toggle('active');
});