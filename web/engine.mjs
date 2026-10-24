// Pure rules shared by browser games and Node's dependency-free test runner.
export const SHAPES = [
 [[1,1,1,1]], [[1,0,0],[1,1,1]], [[0,0,1],[1,1,1]],
 [[1,1],[1,1]], [[0,1,1],[1,1,0]], [[0,1,0],[1,1,1]], [[1,1,0],[0,1,1]]
];
export function rotate(shape) { return shape[0].map((_,x) => shape.map(row => row[x]).reverse()); }
export function fits(board, shape, x, y) {
 return shape.every((row,j) => row.every((cell,i) => !cell || (x+i>=0 && x+i<board[0].length && y+j>=0 && y+j<board.length && !board[y+j][x+i])));
}
export function clearLines(board) {
 const remaining=board.filter(row=>row.some(cell=>!cell));
 const count=board.length-remaining.length;
 return {count, board:[...Array.from({length:count},()=>Array(board[0].length).fill(0)),...remaining]};
}
export function winner(cells) {
 for (const [a,b,c] of [[0,1,2],[3,4,5],[6,7,8],[0,3,6],[1,4,7],[2,5,8],[0,4,8],[2,4,6]])
  if(cells[a] && cells[a]===cells[b] && cells[a]===cells[c]) return cells[a];
 return cells.every(Boolean)?'draw':null;
}
export function rps(player,cpu) { return ['Draw','You win','CPU wins'][(player-cpu+3)%3]; }
export function snakeStep(body, direction, food, size=20) {
 const head={x:body[0].x+direction.x,y:body[0].y+direction.y};
 const ate=head.x===food.x && head.y===food.y;
 const occupied=ate?body:body.slice(0,-1);
 const dead=head.x<0||head.y<0||head.x>=size||head.y>=size||occupied.some(p=>p.x===head.x&&p.y===head.y);
 return {body:[head,...(ate?body:body.slice(0,-1))],ate,dead};
}
export function spawnFood(body,size=20,random=Math.random) {
 const free=[]; for(let y=0;y<size;y++) for(let x=0;x<size;x++) if(!body.some(p=>p.x===x&&p.y===y)) free.push({x,y});
 return free.length?free[Math.floor(random()*free.length)]:null;
}
