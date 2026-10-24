import {SHAPES,rotate,fits,clearLines,winner,rps,snakeStep,spawnFood} from './engine.mjs';
const $=id=>document.getElementById(id);
const palette=['#7dd3fc','#a78bfa','#ffba72','#f7dc6f','#b8f659','#e7a6ed','#ff738f'];
const games=[
 {id:'tetris',name:'Tetris',tag:'THE BLOCK CLASSIC',desc:'Find your flow. Make every block fit.',meta:'PUZZLE',color:palette[1],controls:'← → move · ↑ rotate · ↓ soft drop · Space hard drop · P pause'},
 {id:'pong',name:'Pong',tag:'FRIENDLY RIVALRY',desc:'Two paddles. One ball. Settle the score.',meta:'2 PLAYERS',color:palette[0],controls:'Player 1: W / S · Player 2: ↑ / ↓ · First to 10 wins. Touch: drag either paddle.'},
 {id:'snake',name:'Snake',tag:'JUST ONE MORE APPLE',desc:'Stay hungry. Stay sharp. Watch your tail.',meta:'SURVIVAL',color:palette[4],controls:'Arrow keys or touch arrows to steer · P pause · Avoid the walls and your tail.'},
 {id:'xo',name:'Tic-Tac-Toe',tag:'MAKE YOUR MOVE',desc:'An old-school showdown, three squares at a time.',meta:'2 PLAYERS',color:palette[6],controls:'Take turns selecting a square. X starts. Three in a row wins.'},
 {id:'rps',name:'Rock Paper Scissors',tag:'TRUST YOUR INSTINCT',desc:'Outsmart the machine. Or leave it to luck.',meta:'YOU VS CPU',color:palette[2],controls:'Choose a move. Rock beats scissors, scissors beat paper, paper beats rock.'}
];
function art(id){
 const start='<svg viewBox="0 0 220 130" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">', end='</svg>';
 if(id==='tetris')return start+[[65,75,1],[85,75,1],[105,75,1],[85,55,1],[125,75,4],[145,75,4],[145,55,4],[165,55,4],[65,95,0],[85,95,0],[105,95,0],[125,95,0],[65,35,6],[65,15,6],[85,35,6],[85,55,6]].map(([x,y,c])=>`<rect x="${x}" y="${y}" width="18" height="18" rx="2" fill="${palette[c]}"/>`).join('')+end;
 if(id==='pong')return start+'<path d="M110 10v110" stroke="#334152" stroke-dasharray="5 6"/><rect x="35" y="25" width="8" height="44" rx="3" fill="#7dd3fc"/><rect x="175" y="66" width="8" height="44" rx="3" fill="#b8f659"/><circle cx="132" cy="48" r="7" fill="#eef2fb"/><path d="M105 60l16-8" stroke="#eef2fb55" stroke-width="4"/>'+end;
 if(id==='snake')return start+'<path d="M48 87h45V48h65v25h-27" fill="none" stroke="#b8f659" stroke-width="16" stroke-linejoin="round"/><circle cx="131" cy="70" r="2" fill="#0c0f18"/><rect x="167" y="94" width="14" height="14" rx="4" fill="#ff738f"/>'+end;
 if(id==='xo')return start+'<path d="M85 15v100m45-100v100M50 48h115M50 82h115" stroke="#3d354c" stroke-width="2"/><path d="M57 20l20 20m0-20L57 40m40 15l20 20m0-20L97 75m40 15l20 20m0-20l-20 20" stroke="#ff738f" stroke-width="5" stroke-linecap="round"/><g fill="none" stroke="#a78bfa" stroke-width="5"><circle cx="150" cy="31" r="11"/><circle cx="66" cy="99" r="11"/></g>'+end;
 return start+'<g fill="none" stroke="#ffba72" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><path d="M25 85V64l10-14 13 3 12 20-9 18H33z"/><path d="M90 90V48q5-10 10 0v18-30q5-10 10 0v28-32q5-10 10 0v34-23q5-10 10 0v35l-10 15z"/><path d="M164 90l12-23-8-32q2-8 7-2l14 28 13-29q5-6 7 0l-7 41-15 19z"/></g>'+end;
}
$('games').innerHTML=games.map((g,i)=>`<button class="card" data-game="${g.id}" style="--accent:${g.color}" aria-label="Play ${g.name}"><div class="card-art"><span class="card-num">0${i+1}</span><span class="card-badge">${g.tag}</span>${art(g.id)}</div><div class="card-body"><div class="card-title"><h3>${g.name}</h3><span class="arrow">↗</span></div><p>${g.desc}</p><div class="card-meta"><span>${g.meta}</span><span>PLAY NOW →</span></div></div></button>`).join('');
const quests=['Clear four lines in Tetris.','Challenge a friend to a Pong rematch.','Eat ten apples in Snake.','Win a round of Tic-Tac-Toe.','Win three rounds of Rock Paper Scissors.'];
const today=new Date(); $('quest').textContent=quests[Math.floor(Date.UTC(today.getFullYear(),today.getMonth(),today.getDate())/86400000)%quests.length];
let active,stop=()=>{},handle=()=>{},paused=false,score=0,over=false,draw=()=>{},loop=null;
const held=new Set();
function setScore(text){$('score').textContent=text;}
function end(text){over=true;setScore(text);draw();}
function open(id){
 stop(); active=games.find(g=>g.id===id); if(!active)return;
 paused=false;over=false;score=0;held.clear();$('pause').textContent='Pause';
 $('game-title').textContent=active.name;$('game-category').textContent=active.tag;$('controls').textContent=active.controls;
 $('stage').replaceChildren();$('touch').hidden=!['snake','tetris'].includes(id);$('pause').hidden=['xo','rps'].includes(id);
 $('touch').querySelector('[data-key=" " ]').hidden=id!=='tetris';
 if(!$('player').open)$('player').showModal();
 $('player').tabIndex=-1; $('player').focus();
 if(id==='xo')startXO();else if(id==='rps')startRPS();else startCanvas(id);
}
function startXO(){
 const cells=Array(9).fill('');let turn='X';setScore('X to move');
 const board=document.createElement('div');board.className='board';
 cells.forEach((_,i)=>{const b=document.createElement('button');b.setAttribute('aria-label',`Row ${Math.floor(i/3)+1}, column ${i%3+1}`);b.onclick=()=>{
 if(over||cells[i])return;cells[i]=turn;b.textContent=turn;b.disabled=true;
 const result=winner(cells);if(result){over=true;setScore(result==='draw'?'A perfect standoff. Draw!':`${result} wins! Play again?`);board.querySelectorAll('button').forEach(button=>button.disabled=true);}
 else{turn=turn==='X'?'O':'X';setScore(`${turn} to move`);}
 };board.append(b);});$('stage').append(board);handle=()=>{};stop=()=>{};
}
function startRPS(){
 let cpuScore=0;setScore('YOU 0 : 0 CPU');const wrap=document.createElement('div');const result=document.createElement('p');result.className='result';result.setAttribute('aria-live','polite');result.textContent='Your move.';
 const choices=document.createElement('div');choices.className='choices';['Rock','Paper','Scissors'].forEach((name,i)=>{const b=document.createElement('button');b.textContent=name;b.onclick=()=>{const cpu=Math.floor(Math.random()*3),outcome=rps(i,cpu);if(outcome==='You win')score++;if(outcome==='CPU wins')cpuScore++;result.textContent=`CPU: ${['Rock','Paper','Scissors'][cpu]} · ${outcome}`;setScore(`YOU ${score} : ${cpuScore} CPU`);};choices.append(b);});wrap.append(result,choices);$('stage').append(wrap);handle=()=>{};stop=()=>{};
}
function startCanvas(id){
 const canvas=document.createElement('canvas');canvas.width=id==='pong'?640:id==='tetris'?300:400;canvas.height=id==='tetris'?600:400;canvas.setAttribute('aria-label',active.name+' game board. '+active.controls);canvas.setAttribute('role','img');$('stage').append(canvas);
 const ctx=canvas.getContext('2d'),w=canvas.width,h=canvas.height;
 function cell(x,y,c,size=20){ctx.fillStyle=c;ctx.fillRect(x*size+1,y*size+1,size-2,size-2);}
 let tick;
 function backdrop(){ctx.fillStyle='#101620';ctx.fillRect(0,0,w,h);ctx.strokeStyle='#1c2533';for(let x=0;x<w;x+=20){ctx.beginPath();ctx.moveTo(x,0);ctx.lineTo(x,h);ctx.stroke();}for(let y=0;y<h;y+=20){ctx.beginPath();ctx.moveTo(0,y);ctx.lineTo(w,y);ctx.stroke();}}
 function overlay(){if(paused||over){ctx.fillStyle='#0c0f18ce';ctx.fillRect(0,h/2-45,w,90);ctx.fillStyle='#b8f659';ctx.font='bold 22px monospace';ctx.textAlign='center';ctx.fillText(paused?'PAUSED':'ROUND COMPLETE',w/2,h/2+8);}}
 let interval=100;
 if(id==='snake'){
 let body=[{x:5,y:10},{x:4,y:10},{x:3,y:10}],dir={x:1,y:0},next=dir,food=spawnFood(body),turned=false;
 draw=()=>{backdrop();body.forEach((p,i)=>cell(p.x,p.y,i?'#78af48':'#b8f659'));if(food)cell(food.x,food.y,'#ff738f');overlay();};
 handle=key=>{const dirs={ArrowLeft:{x:-1,y:0},ArrowRight:{x:1,y:0},ArrowUp:{x:0,y:-1},ArrowDown:{x:0,y:1}};const d=dirs[key];if(d&&!turned&&(d.x!==-dir.x||d.y!==-dir.y)){next=d;turned=true;}};
 tick=()=>{dir=next;turned=false;const step=snakeStep(body,dir,food);if(step.dead){end(`Game over · ${score} apples`);return;}body=step.body;if(step.ate){score++;food=spawnFood(body);if(!food)end('You filled the board!');}setScore(`${score} APPLES`);};interval=130;setScore('0 APPLES');
 }else if(id==='tetris'){
 let board=Array.from({length:20},()=>Array(10).fill(0)),shape,x,y,color,bag=[],lines=0;
 function spawn(){if(!bag.length){bag=Array.from({length:7},(_,i)=>i);for(let i=6;i>0;i--){const j=Math.floor(Math.random()*(i+1));[bag[i],bag[j]]=[bag[j],bag[i]];}}color=bag.pop();shape=SHAPES[color].map(r=>r.slice());x=3;y=0;if(!fits(board,shape,x,y))end(`Game over · ${score} points`);}
 function down(){if(fits(board,shape,x,y+1)){y++;return true;}shape.forEach((row,j)=>row.forEach((v,i)=>{if(v)board[y+j][x+i]=color+1;}));const result=clearLines(board);board=result.board;lines+=result.count;score+=[0,100,300,500,800][result.count];spawn();if(!over)setScore(`${score} PTS / ${lines} LINES`);return false;}
 draw=()=>{backdrop();board.forEach((row,j)=>row.forEach((v,i)=>{if(v)cell(i,j,palette[v-1],30);}));if(!over)shape.forEach((row,j)=>row.forEach((v,i)=>{if(v)cell(x+i,y+j,palette[color],30);}));overlay();};
 handle=key=>{if(key==='ArrowLeft'&&fits(board,shape,x-1,y))x--;if(key==='ArrowRight'&&fits(board,shape,x+1,y))x++;if(key==='ArrowUp'){const rotated=rotate(shape);for(const offset of [0,-1,1,-2,2])if(fits(board,rotated,x+offset,y)){shape=rotated;x+=offset;break;}}if(key==='ArrowDown')down();if(key===' '){while(!over&&fits(board,shape,x,y+1)){y++;score++;}if(!over)down();}draw();};tick=down;interval=550;spawn();setScore('0 PTS / 0 LINES');
 }else{
 let left=160,right=160,bx=320,by=200,vx=4,vy=2,leftScore=0,rightScore=0;
 function serve(){bx=320;by=200;vx=(Math.random()<.5?-1:1)*4;vy=(Math.random()<.5?-1:1)*2;}
 draw=()=>{backdrop();ctx.setLineDash([6,10]);ctx.strokeStyle='#4b566b';ctx.beginPath();ctx.moveTo(320,0);ctx.lineTo(320,h);ctx.stroke();ctx.setLineDash([]);ctx.fillStyle='#b8f659';ctx.fillRect(16,left,10,80);ctx.fillStyle='#a78bfa';ctx.fillRect(614,right,10,80);ctx.fillStyle='#eef2fb';ctx.beginPath();ctx.arc(bx,by,7,0,Math.PI*2);ctx.fill();overlay();};
 tick=()=>{left=Math.max(0,Math.min(320,left+(held.has('s')?6:0)-(held.has('w')?6:0)));right=Math.max(0,Math.min(320,right+(held.has('ArrowDown')?6:0)-(held.has('ArrowUp')?6:0)));bx+=vx;by+=vy;if(by<=7||by>=393){by=Math.max(7,Math.min(393,by));vy=-vy;}
 if(vx<0&&bx<=33&&bx>=16&&by>=left-7&&by<=left+87){bx=33;vx=Math.min(8,Math.abs(vx)+.15);vy=(by-left-40)/12;}
 if(vx>0&&bx>=607&&bx<=624&&by>=right-7&&by<=right+87){bx=607;vx=-Math.min(8,Math.abs(vx)+.15);vy=(by-right-40)/12;}
 if(bx<0){rightScore++;serve();}if(bx>w){leftScore++;serve();}setScore(`PLAYER 1 ${leftScore} : ${rightScore} PLAYER 2`);if(leftScore>=10||rightScore>=10)end(`Player ${leftScore>=10?1:2} wins!`);
 };handle=()=>{};interval=16;setScore('PLAYER 1 0 : 0 PLAYER 2');
 canvas.style.touchAction='none';canvas.onpointerdown=e=>{canvas.setPointerCapture(e.pointerId);position(e);};canvas.onpointermove=e=>{if(e.buttons)position(e);};function position(e){if(paused||over)return;const r=canvas.getBoundingClientRect();const yy=Math.max(0,Math.min(320,(e.clientY-r.top)/r.height*400-40));if((e.clientX-r.left)/r.width<.5)left=yy;else right=yy;draw();}
 }
 draw();loop=setInterval(()=>{if(!paused&&!over){tick();draw();}},interval);stop=()=>{clearInterval(loop);held.clear();};
}
function togglePause(){if(['xo','rps'].includes(active?.id)||over)return;paused=!paused;held.clear();$('pause').textContent=paused?'Resume':'Pause';draw();}
$('games').onclick=e=>{const card=e.target.closest('[data-game]');if(card)open(card.dataset.game);};$('surprise').onclick=()=>open(games[Math.floor(Math.random()*games.length)].id);
$('close').onclick=()=>$('player').close();$('player').addEventListener('close',()=>{stop();active=null;});$('restart').onclick=()=>open(active.id);$('pause').onclick=togglePause;
window.addEventListener('keydown',e=>{if(!$('player').open||e.target.matches('input'))return;if(e.key==='Escape')return;const key=e.key.length===1?e.key.toLowerCase():e.key;if(['ArrowLeft','ArrowRight','ArrowUp','ArrowDown',' ','w','s','p'].includes(key)){e.preventDefault();if(key==='p'){if(!e.repeat)togglePause();return;}held.add(key);if(!paused&&!over)handle(key);}});
window.addEventListener('keyup',e=>held.delete(e.key.length===1?e.key.toLowerCase():e.key));
window.addEventListener('blur',()=>{held.clear();if(active&&!paused&&!over)togglePause();});
document.addEventListener('visibilitychange',()=>{if(document.hidden&&active&&!paused&&!over)togglePause();});
$('touch').querySelectorAll('button').forEach(b=>b.onpointerdown=e=>{e.preventDefault();if(!paused&&!over)handle(b.dataset.key);});
let countdown;
$('timer-form').onsubmit=e=>{e.preventDefault();if(!$('timer-form').reportValidity())return;clearInterval(countdown);const deadline=Date.now()+Number($('minutes').value)*60000;const update=()=>{const seconds=Math.max(0,Math.ceil((deadline-Date.now())/1000));$('countdown').textContent=seconds?`${Math.floor(seconds/60)}:${String(seconds%60).padStart(2,'0')} left`:'Time for a break!';if(!seconds)clearInterval(countdown);};update();countdown=setInterval(update,1000);};
