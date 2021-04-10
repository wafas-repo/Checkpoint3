* C-Minus Compilation to TM Code
* File: 
* Standard prelude:
0: LD 6, 0(0) 	load gp with maxaddr
1: LDA 5, 0(6) 	Copy gp to fp
2: ST 0, 0(0) 	clear location 0
* Jump around i/o routines
* Code for input routine
4: ST 0, -1(5) 	store return
5: IN 0, 0, 0 input
6: LD 7, -1(5) 	return to caller
* Code for output routine
7: ST 0, -1(5) 	store return
8: LD 0, -2(5) 	load output value
9: OUT 0, 0, 0 output
10: LD 7, -1(5) 	return to caller
3: LDA 7 7(7) 	jump around i/o code
* end of standard prelude.
* Allocating global var: y
* <- vardecl
* processing local var: y
* Allocating global var: x
* <- vardecl
* processing local var: x
* Processing function: gcd
* jump around function body here
12: ST 0, -1(5) 	store return
* processing local var: u
* processing local var: v
* -> compound statement
* -> if
* -> op
* -> id
* looking up id: v
* <- id
* -> constant
* <- constant
* <- op
* -> return
* -> id
* looking up id: u
* <- id
* <- return
* if: jump to else belongs here
* -> return
* -> call of function: gcd
* -> id
* looking up id: v
* <- id
* -> op
* -> id
* looking up id: u
* <- id
* -> op
* -> op
* -> id
* looking up id: u
* <- id
* -> id
* looking up id: v
* <- id
* <- op
* -> id
* looking up id: v
* <- id
* <- op
* <- op
* <- call
* <- return
* <- if
* <- compound statement
13: LD 7, -1(5) 	return to caller
11: LDA 7 2(7) 	Jump around function body
* <- fundecl
* Processing function: main
* jump around function body here
15: ST 0, -1(5) 	store return
* -> compound statement
* processing local var: x
* processing local var: y
* -> op
* -> id
* looking up id: x
* <- id
* -> call of function: input
* <- call
* <- op
* -> op
* -> id
* looking up id: y
* <- id
* -> call of function: input
* <- call
* <- op
* -> call of function: output
* -> call of function: gcd
* -> id
* looking up id: x
* <- id
* -> id
* looking up id: y
* <- id
* <- call
* <- call
* <- compound statement
16: LD 7, -1(5) 	return to caller
14: LDA 7 2(7) 	Jump around function body
* <- fundecl
17: ST 5, -6(5) 	push ofp
18: LDA 5, -6(5) 	push frame
19: LDA 0, 1(7) 	load ac with ret ptr
20: LDA 7 -6(7) 	jump to main loc
21: LD 5, 0(5) 	pop frame
* end of execution.
22: HALT 0, 0, 0 
