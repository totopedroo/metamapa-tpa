/* Configurable: endpoints de tu API (cuando estén listos) */
const API = {
  coleccionesDestacadas: "/api/public/colecciones?destacadas=true&limit=6",
  hechosIrrestrictos:   "/api/public/hechos?modo=irrestricto&limit=6",
  hechosCurados:        "/api/public/hechos?modo=curado&limit=6"
};

const $ = (sel, ctx=document) => ctx.querySelector(sel);
const $$ = (sel, ctx=document) => Array.from(ctx.querySelectorAll(sel));

/* Año dinámico en footer */
$("#anio").textContent = new Date().getFullYear();

/* Mobile nav toggle */
const navBtn = $(".nav-toggle");
const nav = $("#menu-principal");
if (navBtn){
  navBtn.addEventListener("click", () => {
    const open = nav.classList.toggle("open");
    navBtn.setAttribute("aria-expanded", String(open));
    navBtn.setAttribute("aria-label", open ? "Cerrar menú" : "Abrir menú");
  });
}

/* Helpers de UI */
function badge(label){ return `<span class="badge">${label}</span>`; }
function cardColeccion({titulo, descripcion, handle, fuentes=0, hechos=0}){
  return `
    <article class="card" role="listitem">
      <div class="card-header">
        <h3>${titulo}</h3>
      </div>
      <div class="card-meta">
        ${badge(`${hechos} hechos`)} ${badge(`${fuentes} fuentes`)} ${badge(`#${handle}`)}
      </div>
      <div class="card-body">
        <p>${descripcion}</p>
      </div>
      <div class="card-actions">
        <a class="btn btn-primary" href="/visualizacion?coleccion=${encodeURIComponent(handle)}">Explorar</a>
      </div>
    </article>`;
}
function cardHecho({titulo, descripcion, categoria, lugar, fecha}){
  const fechaFmt = fecha ? new Date(fecha).toLocaleDateString("es-AR") : "—";
  return `
    <article class="card" role="listitem">
      <div class="card-header">
        <h3>${titulo}</h3>
      </div>
      <div class="card-meta">
        ${categoria ? badge(categoria) : ""} ${lugar ? badge(lugar) : ""} ${badge(fechaFmt)}
      </div>
      <div class="card-body">
        <p>${descripcion}</p>
      </div>
      <div class="card-actions">
        <a class="btn btn-secondary" href="/visualizacion">Ver en mapa</a>
      </div>
    </article>`;
}

/* Placeholders locales (se usan si la API no responde) */
const MOCK = {
  colecciones: [
    { titulo:"Incendios forestales en Argentina 2025", descripcion:"Seguimiento de focos activos y prevención.", handle:"incendios-arg-2025", fuentes:3, hechos:128 },
    { titulo:"Desapariciones vinculadas a crímenes de odio", descripcion:"Registro y visibilización para impulsar acciones.", handle:"desapariciones-odio", fuentes:4, hechos:56 },
    { titulo:"Contaminación hídrica Patagonia", descripcion:"Reportes de efluentes y derrames.", handle:"contaminacion-hidrica-patagonia", fuentes:2, hechos:73 },
    { titulo:"Violencia institucional", descripcion:"Hechos reportados por organizaciones.", handle:"violencia-institucional", fuentes:5, hechos:91 },
    { titulo:"Incendios urbanos AMBA", descripcion:"Hechos consolidados por fuentes múltiples.", handle:"incendios-amba", fuentes:3, hechos:44 },
    { titulo:"Deforestación NOA", descripcion:"Avance de la frontera agropecuaria.", handle:"deforestacion-noa", fuentes:2, hechos:61 }
  ],
  hechos: [
    { titulo:"Foco activo en cordón serrano", descripcion:"Columna de humo visible desde ruta provincial.", categoria:"Incendio forestal", lugar:"Córdoba", fecha:"2025-08-01" },
    { titulo:"Derrame en arroyo local", descripcion:"Vecinos reportan manchas y olor fuerte.", categoria:"Contaminación", lugar:"Neuquén", fecha:"2025-07-18" },
    { titulo:"Avistaje de quema de pastizales", descripcion:"Imágenes aportadas por voluntariado.", categoria:"Quema", lugar:"Corrientes", fecha:"2025-06-30" },
    { titulo:"Denuncia por desaparición", descripcion:"Solicitud de colaboración de ONG local.", categoria:"Desaparición", lugar:"Santa Fe", fecha:"2025-08-10" },
    { titulo:"Vertido industrial no autorizado", descripcion:"Registro repetido por dos fuentes.", categoria:"Contaminación", lugar:"Chubut", fecha:"2025-07-05" },
    { titulo:"Foco en zona periurbana", descripcion:"Riesgo para viviendas cercanas.", categoria:"Incendio", lugar:"Salta", fecha:"2025-08-12" }
  ]
};

/* Fetch con fallback a MOCK */
async function getJSON(url){
  try{
    const r = await fetch(url, {headers:{Accept:"application/json"}});
    if(!r.ok) throw new Error("HTTP "+r.status);
    return await r.json();
  }catch(e){
    return null;
  }
}

async function cargarColecciones(){
  const el = $("#colecciones-grid");
  el.innerHTML = ""; // reset
  const data = await getJSON(API.coleccionesDestacadas);
  const colecciones = Array.isArray(data?.items) ? data.items : MOCK.colecciones;
  el.innerHTML = colecciones.map(cardColeccion).join("");
}

async function cargarHechos(modo="irrestricto"){
  const el = $("#hechos-grid");
  el.innerHTML = ""; // reset
  const url = modo === "curado" ? API.hechosCurados : API.hechosIrrestrictos;
  const data = await getJSON(url);
  const hechos = Array.isArray(data?.items) ? data.items : MOCK.hechos;
  el.innerHTML = hechos.map(cardHecho).join("");
}

/* Tabs de modo de navegación */
$$(".tab").forEach(btn=>{
  btn.addEventListener("click", ()=>{
    $$(".tab").forEach(b=>{ b.classList.remove("is-active"); b.setAttribute("aria-selected","false"); });
    btn.classList.add("is-active"); btn.setAttribute("aria-selected","true");
    cargarHechos(btn.dataset.mode);
  });
});

/* Init */
document.addEventListener("DOMContentLoaded", ()=>{
  cargarColecciones();
  cargarHechos("irrestricto");
});
