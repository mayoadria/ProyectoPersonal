// Obtener el botón y el menú lateral
const openSidebarButton = document.getElementById('openSidebar');
const sidebar = document.getElementById('sidebar');
const filtres = document.getElementById('filtres')

// Obtener el input tipo range y el span donde se muestra el valor
const minRangeDies = document.getElementById('min-dies');
const maxRangeDies = document.getElementById('max-dies');
const minValueDies = document.getElementById('min-value-dies');
const maxValueDies = document.getElementById('max-value-dies');

// Sincronizar los valores de los rangos
minRangeDies.addEventListener('input', function () {
    if (parseInt(minRangeDies.value) > parseInt(maxRangeDies.value)) {
        minRangeDies.value = maxRangeDies.value;
    }
    minValueDies.textContent = "De: " + minRangeDies.value;
});

maxRangeDies.addEventListener('input', function () {
    if (parseInt(maxRangeDies.value) < parseInt(minRangeDies.value)) {
        maxRangeDies.value = minRangeDies.value;
    }
    maxValueDies.textContent = maxRangeDies.value + " dies";
});

// Obtener el input tipo range y el span donde se muestra el valor
const minRangePreu = document.getElementById('min-preu');
const maxRangePreu = document.getElementById('max-preu');
const minValuePreu = document.getElementById('min-value-preu');
const maxValuePreu = document.getElementById('max-value-preu');

// Sincronizar los valores de los rangos
minRangePreu.addEventListener('input', function () {
    if (parseInt(minRangePreu.value) > parseInt(maxRangePreu.value)) {
        minRangePreu.value = maxRangePreu.value;
    }
    minValuePreu.textContent = "De: " + minRangePreu.value;
});

maxRangePreu.addEventListener('input', function () {
    if (parseInt(maxRangePreu.value) < parseInt(minRangePreu.value)) {
        maxRangePreu.value = minRangePreu.value;
    }
    maxValuePreu.textContent = maxRangePreu.value + " €";
});

// Obtener el input tipo range y el span donde se muestra el valor
const minRangeFian = document.getElementById('min-fian');
const maxRangeFian = document.getElementById('max-fian');
const minValueFian = document.getElementById('min-value-fian');
const maxValueFian = document.getElementById('max-value-fian');

// Sincronizar los valores de los rangos
minRangeFian.addEventListener('input', function () {
    if (parseInt(minRangeFian.value) > parseInt(maxRangeFian.value)) {
        minRangeFian.value = maxRangeFian.value;
    }
    minValueFian.textContent = "De: " + minRangeFian.value;
});

maxRangeFian.addEventListener('input', function () {
    if (parseInt(maxRangeFian.value) < parseInt(minRangeFian.value)) {
        maxRangeFian.value = minRangeFian.value;
    }
    maxValueFian.textContent = maxRangeFian.value + " €";
});


document.addEventListener("DOMContentLoaded", function () {
    const marxesContainer = document.getElementById("marxes-radio");
    const radiosCaixesCanvis = document.querySelectorAll("input[name='caixesCanvis']");

    radiosCaixesCanvis.forEach(radio => {
        radio.addEventListener("change", function () {
            if (this.value.toLowerCase() === "manual") {
                marxesContainer.style.display = "block";
            } else {
                marxesContainer.style.display = "none";
            }
        });
    });
});



// Añadir evento para abrir el menú lateral cuando se haga clic en el botón
openSidebarButton.addEventListener('click', function () {
    sidebar.classList.toggle('open'); // Togglear la clase 'open'
    filtres.classList.toggle('open');
});


