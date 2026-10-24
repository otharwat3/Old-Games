import test from 'node:test';
import assert from 'node:assert/strict';
import {SHAPES,rotate,fits,clearLines,winner,rps,snakeStep,spawnFood} from '../web/engine.mjs';
test('all seven tetrominoes preserve four cells through four rotations',()=>{
 for(const original of SHAPES){let shape=original;for(let i=0;i<4;i++){assert.equal(shape.flat().reduce((a,b)=>a+b),4);shape=rotate(shape);}assert.deepEqual(shape,original);}
});
test('placement rejects walls, floor and occupied cells',()=>{const b=Array.from({length:20},()=>Array(10).fill(0));assert.ok(fits(b,SHAPES[0],3,0));assert.ok(!fits(b,SHAPES[0],8,0));assert.ok(!fits(b,SHAPES[0],-1,0));assert.ok(!fits(b,SHAPES[0],0,20));b[0][3]=1;assert.ok(!fits(b,SHAPES[0],3,0));});
test('adjacent full lines clear without aliasing new rows',()=>{const result=clearLines([[0,1],[1,1],[1,1]]);assert.equal(result.count,2);assert.deepEqual(result.board,[[0,0],[0,0],[0,1]]);result.board[0][0]=1;assert.equal(result.board[1][0],0);});
test('every winning line works for either player',()=>{for(const line of [[0,1,2],[3,4,5],[6,7,8],[0,3,6],[1,4,7],[2,5,8],[0,4,8],[2,4,6]])for(const symbol of ['X','O']){const board=Array(9).fill('');line.forEach(i=>board[i]=symbol);assert.equal(winner(board),symbol);}assert.equal(winner(['X','O','X','X','O','O','O','X','X']),'draw');assert.equal(winner(Array(9).fill('')),null);});
test('all nine RPS outcomes are symmetric',()=>{for(let a=0;a<3;a++)for(let b=0;b<3;b++){const result=rps(a,b);assert.equal(result,a===b?'Draw':(a-b+3)%3===1?'You win':'CPU wins');}});
test('snake grows, collides with boundary and permits vacated tail cell',()=>{const body=[{x:1,y:1},{x:0,y:1}];assert.equal(snakeStep(body,{x:1,y:0},{x:2,y:1}).body.length,3);assert.ok(snakeStep([{x:0,y:0}],{x:-1,y:0},{x:2,y:2}).dead);assert.ok(!snakeStep(body,{x:-1,y:0},{x:3,y:3}).dead);});
test('food uses free cells and full boards terminate',()=>{assert.deepEqual(spawnFood([{x:0,y:0},{x:1,y:0},{x:0,y:1}],2),{x:1,y:1});assert.equal(spawnFood([{x:0,y:0}],1),null);});
