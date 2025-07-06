
const precioTotal = document.getElementById("precioTotal")
const precioDiaElement = document.getElementById('preuDia');
const precioFianzaElement = document.getElementById('precioFianza');

const precioDia = () =>{
    if (precioDiaElement) {
        const valor = parseFloat(precioDiaElement.textContent.replace('€', '').trim());
        return isNaN(valor) ? 0 : valor;
    }
    return 0;
}

const obtenerPrecioFianza = () => {
    if (precioFianzaElement) {
        const valor = parseFloat(precioFianzaElement.textContent.replace('€', '').trim());
        return isNaN(valor) ? 0 : valor;
    }
    return 0;
};

