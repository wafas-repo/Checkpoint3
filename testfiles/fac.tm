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
3: LDA 7, 7(7) 	jump around i/o code
* end of standard prelude.
* Processing function: main
* jump around function body here
12: ST 0, -1(5) 	store return
* -> compound statement
* <- compound statement
13: LD 7, -1(5) 	return to caller
11: LDA 7, 2(7) 	Jump around function body
* <- fundecl
14: ST 5, 0(5) 	push ofp
15: LDA 5, 0(5) 	push frame
16: LDA 0, 1(7) 	load ac with ret ptr
17: LDA 7, -6(7) 	jump to main loc
18: LD 5, 0(5) 	pop frame
* end of execution.
19: HALT 0, 0, 0 
