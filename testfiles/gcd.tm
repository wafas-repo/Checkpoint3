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
* Processing function: gcd
* jump around function body here
12: ST 0, -1(5) 	store return
* -> compound statement
* -> if
* -> op
* -> id
* looking up id: v
13: LD 0, 0(5) 	load id value
14: ST 0, -2(5) 	op: push left OP2
* -> constant
15: LDC 0, 0(0) 	load const
* <- constant
16: LD 1, -2(5) 	op: load left
17: SUB 0, 1, 0 op ==
18: JEQ 0, 2(7) 	
19: LDC 0, 0(0) 	false case
20: LDA 7, 1(7) 	unconditional jump
21: LDC 0, 1(0) 	true case
* <- op
* -> return
* -> id
* looking up id: u
23: LD 0, 0(5) 	load id value
* <- return
22: JEQ 0, 1(7) 	if: jump to else part
* if: jump to else belongs here
* -> return
* -> call of function: gcd
* -> id
* looking up id: v
24: LD 0, 0(5) 	load id value
25: ST 0, -4(5) 	op: push left INT
* -> op
* -> id
* looking up id: u
26: LD 0, 0(5) 	load id value
27: ST 0, -2(5) 	op: push left OP2
28: LD 1, -2(5) 	op: load left
29: SUB 0, 1, 0 op -
* <- op
30: ST 0, -5(5) 	op: push left INT
31: ST 5, -2(5) 	push ofp
32: LDA 5, -2(5) 	Push frame
33: LDA 0, 1(7) 	Load ac with ret ptr
34: LDA 7, -24(7) 	jump to fun loc
35: LD 5, 0(5) 	Pop frame
* <- call
* <- return
* <- if
* <- compound statement
36: LD 7, -1(5) 	return to caller
11: LDA 7, 25(7) 	Jump around function body
* <- fundecl
* Processing function: main
* jump around function body here
38: ST 0, -1(5) 	store return
* -> compound statement
* processing local var: x
* processing local var: y
* -> op
* -> id
* looking up id: x
39: LDA 0, -2(5) 	load id address
* <- id
40: ST 0, -4(5) 	op: push left AssignExp
41: LD 1, -4(5) 	op: load left
42: ST 0, 0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: y
43: LDA 0, -3(5) 	load id address
* <- id
44: ST 0, -5(5) 	op: push left AssignExp
45: LD 1, -5(5) 	op: load left
46: ST 0, 0(1) 	assign: store value
* <- op
* -> call of function: output
* -> call of function: gcd
* -> id
* looking up id: x
47: LD 0, -2(5) 	load id value
48: ST 0, -8(5) 	op: push left INT
* -> id
* looking up id: y
49: LD 0, -3(5) 	load id value
50: ST 0, -9(5) 	op: push left INT
51: ST 5, -6(5) 	push ofp
52: LDA 5, -6(5) 	Push frame
53: LDA 0, 1(7) 	Load ac with ret ptr
54: LDA 7, -44(7) 	jump to fun loc
55: LD 5, 0(5) 	Pop frame
* <- call
56: ST 0, -8(5) 	op: push left INT
57: ST 5, -6(5) 	push ofp
58: LDA 5, -6(5) 	Push frame
59: LDA 0, 1(7) 	Load ac with ret ptr
60: LDA 7, -54(7) 	jump to fun loc
61: LD 5, 0(5) 	Pop frame
* <- call
* <- compound statement
62: LD 7, -1(5) 	return to caller
37: LDA 7, 25(7) 	Jump around function body
* <- fundecl
63: ST 5, 0(5) 	push ofp
64: LDA 5, 0(5) 	push frame
65: LDA 0, 1(7) 	load ac with ret ptr
66: LDA 7, -29(7) 	jump to main loc
67: LD 5, 0(5) 	pop frame
* end of execution.
68: HALT 0, 0, 0 
