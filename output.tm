* C-Minus Compilation to TM Code
* File: filename.tm
* Standard prelude:
0: LD 6, 0(0) 	load gp with maxaddr
1: LDA 5, 0(6) 	Copy gp to fp
2: ST 0, 0(0) 	Clear content at loc
* Jump around i/o routines here
* code for input routine
4: ST 0, -1(5) 	store return
5: IN 0, 0, 0 	input
6: LD 7, -1(5) 	return to caller
* code for output routine
7: ST 0, -1(5) 	load output value
8: OUT 0, 0, 0 	output
9: LD 7, -1(5) 	return to caller
3: LDA 7, 7(7) 	jump around i/o code
10: ST 5, 0(5) 	push ofp
11: LDA 5, 0(5) 	push frame
12: LDA 7 -13(7) 	jump to main loc
13: LD 5, 0(5) 	pop frame
14: HALT 0, 0, 0 	
