// Sidebar
const sidebar=document.getElementById('sidebar'),overlay=document.getElementById('overlay');
const openMenu=()=>{sidebar.style.width='280px';overlay.style.display='block';document.body.style.overflow='hidden'};
const closeMenu=()=>{sidebar.style.width='0';overlay.style.display='none';document.body.style.overflow=''};
document.getElementById('openBtn').addEventListener('click',openMenu);
document.getElementById('closeBtn').addEventListener('click',closeMenu);
overlay.addEventListener('click',closeMenu);
document.querySelectorAll('.sidebar-nav a').forEach(l=>l.addEventListener('click',closeMenu));

// Expandir tarjeta
document.querySelectorAll('.product-img-wrapper').forEach(w=>w.addEventListener('click',()=>w.closest('.product-card').classList.toggle('open')));

// Carrusel
const track=document.getElementById('carouselTrack'),prevBtn=document.getElementById('prevBtn'),nextBtn=document.getElementById('nextBtn'),dotsWrap=document.getElementById('carouselDots');
let cur=0,timer=null;
const cv=()=>parseInt(getComputedStyle(document.documentElement).getPropertyValue('--cv'))||1;
const max=()=>track.querySelectorAll('.product-card').length-cv();
const cw=()=>{const c=track.querySelector('.product-card');return c?c.getBoundingClientRect().width+parseFloat(getComputedStyle(track).gap):0};

function goTo(i){
    cur=Math.max(0,Math.min(i,max()));
    track.style.transform=`translateX(-${cur*cw()}px)`;
    prevBtn.disabled=cur===0;nextBtn.disabled=cur>=max();
    dotsWrap.querySelectorAll('.carousel-dot').forEach((d,i)=>d.classList.toggle('active',i===cur));
}

function buildDots(){
    dotsWrap.innerHTML='';
    for(let i=0;i<=max();i++){
        const b=document.createElement('button');
        b.className='carousel-dot'+(i===0?' active':'');
        b.setAttribute('aria-label',`Slide ${i+1}`);
        b.addEventListener('click',()=>{goTo(i);reset()});
        dotsWrap.appendChild(b);
    }
}

const start=()=>timer=setInterval(()=>goTo(cur<max()?cur+1:0),3500);
const reset=()=>{clearInterval(timer);start()};

prevBtn.addEventListener('click',()=>{goTo(cur-1);reset()});
nextBtn.addEventListener('click',()=>{goTo(cur+1);reset()});

let tx=0;
track.addEventListener('touchstart',e=>tx=e.touches[0].clientX,{passive:true});
track.addEventListener('touchend',e=>{const d=tx-e.changedTouches[0].clientX;if(Math.abs(d)>50){goTo(cur+(d>0?1:-1));reset()}});
track.addEventListener('mouseenter',()=>clearInterval(timer));
track.addEventListener('mouseleave',start);

let rt;
window.addEventListener('resize',()=>{clearTimeout(rt);rt=setTimeout(()=>{buildDots();goTo(Math.min(cur,max()))},150)});

buildDots();goTo(0);start();
