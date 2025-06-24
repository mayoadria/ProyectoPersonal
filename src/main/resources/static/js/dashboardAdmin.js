const formFiltresAgent = document.getElementById("filtres-form-agent");
const buttonFiltresAgent = document.getElementById("applyFiltersAgent");

buttonFiltresAgent.addEventListener("click", () => {
    formFiltresAgent.submit();
})

const formFiltresClient = document.getElementById("filtres-form-client");
const buttonFiltresClient = document.getElementById("applyFiltersClient");

buttonFiltresClient.addEventListener("click", () => {
    formFiltresClient.submit();
})