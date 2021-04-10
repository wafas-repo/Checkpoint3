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
* Allocating global var: x
* <- vardecl
* processing local var: x
* Processing function: minloc
* jump around function body here
12: ST 0, -1(5) 	store return
* -> compound statement
* processing local var: i
* processing local var: x
* processing local var: k
* -> op
* -> id
* looking up id: k
13: LDA 0, -7(5) 	load id address
* <- id
14: ST 0, -8(5) 	op: push left AssignExp
15: LD 1, -8(5) 	op: load left
16: ST 0, 0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: x
17: LDA 0, 0(5) 	load id address
* <- id
18: ST 0, -9(5) 	op: push left AssignExp
19: LD 1, -9(5) 	op: load left
20: ST 0, 0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: i
21: LDA 0, -5(5) 	load id address
* <- id
22: ST 0, -10(5) 	op: push left AssignExp
* -> op
* -> id
* looking up id: low
23: LD 0, -3(5) 	load id value
24: ST 0, -12(5) 	op: push left OP2
* -> constant
25: LDC 0, 1(0) 	load const
* <- constant
26: LD 1, -12(5) 	op: load left
27: ADD 0, 1, 0 op +
* <- op
28: LD 1, -10(5) 	op: load left
29: ST 0, 0(1) 	assign: store value
* <- op
* -> while
* while: jump after body comes back here
* -> op
* -> id
* looking up id: i
30: LD 0, -5(5) 	load id value
31: ST 0, -11(5) 	op: push left OP2
* -> id
* looking up id: high
32: LD 0, -4(5) 	load id value
33: LD 1, -11(5) 	op: load left
34: SUB 0, 1, 0 op <
35: JLT 0, 2(7) 	
36: LDC 0, 0(0) 	false case
37: LDA 7, 1(7) 	unconditional jump
38: LDC 0, 1(0) 	true case
* <- op
* -> compound statement
* -> if
* -> op
39: ST 0, -12(5) 	op: push left OP2
* -> id
* looking up id: x
40: LD 0, 0(5) 	load id value
41: LD 1, -12(5) 	op: load left
42: SUB 0, 1, 0 op <
43: JLT 0, 2(7) 	
44: LDC 0, 0(0) 	false case
45: LDA 7, 1(7) 	unconditional jump
46: LDC 0, 1(0) 	true case
* <- op
* -> compound statement
* -> op
* -> id
* looking up id: x
47: LDA 0, 0(5) 	load id address
* <- id
48: ST 0, -13(5) 	op: push left AssignExp
49: LD 1, -13(5) 	op: load left
50: ST 0, 0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: k
51: LDA 0, -7(5) 	load id address
* <- id
52: ST 0, -14(5) 	op: push left AssignExp
53: LD 1, -14(5) 	op: load left
54: ST 0, 0(1) 	assign: store value
* <- op
* <- compound statement
* <- if
* -> op
* -> id
* looking up id: i
55: LDA 0, -5(5) 	load id address
* <- id
56: ST 0, -13(5) 	op: push left AssignExp
* -> op
* -> id
* looking up id: i
57: LD 0, -5(5) 	load id value
58: ST 0, -15(5) 	op: push left OP2
* -> constant
59: LDC 0, 1(0) 	load const
* <- constant
60: LD 1, -15(5) 	op: load left
61: ADD 0, 1, 0 op +
* <- op
62: LD 1, -13(5) 	op: load left
63: ST 0, 0(1) 	assign: store value
* <- op
* <- compound statement
* <- while
* -> return
* -> id
* looking up id: k
64: LD 0, -7(5) 	load id value
* <- return
* <- compound statement
65: LD 7, -1(5) 	return to caller
11: LDA 7, 54(7) 	Jump around function body
* <- fundecl
* Processing function: sort
* jump around function body here
67: ST 0, -1(5) 	store return
* -> compound statement
* processing local var: i
* processing local var: k
* -> op
* -> id
* looking up id: i
68: LDA 0, -5(5) 	load id address
* <- id
69: ST 0, -7(5) 	op: push left AssignExp
70: LD 1, -7(5) 	op: load left
71: ST 0, 0(1) 	assign: store value
* <- op
* -> while
* while: jump after body comes back here
* -> op
* -> id
* looking up id: i
72: LD 0, -5(5) 	load id value
73: ST 0, -8(5) 	op: push left OP2
74: LD 1, -8(5) 	op: load left
75: SUB 0, 1, 0 op <
76: JLT 0, 2(7) 	
77: LDC 0, 0(0) 	false case
78: LDA 7, 1(7) 	unconditional jump
79: LDC 0, 1(0) 	true case
* <- op
* -> compound statement
* processing local var: t
* -> op
* -> id
* looking up id: k
80: LDA 0, -7(5) 	load id address
* <- id
81: ST 0, -10(5) 	op: push left AssignExp
82: LD 1, -10(5) 	op: load left
83: ST 0, 0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: t
84: LDA 0, -9(5) 	load id address
* <- id
85: ST 0, -11(5) 	op: push left AssignExp
86: LD 1, -11(5) 	op: load left
87: ST 0, 0(1) 	assign: store value
* <- op
* -> op
88: LD 1, -12(5) 	op: load left
89: ST 0, 0(1) 	assign: store value
* <- op
* -> op
90: LD 1, -13(5) 	op: load left
91: ST 0, 0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: i
92: LDA 0, -5(5) 	load id address
* <- id
93: ST 0, -14(5) 	op: push left AssignExp
* -> op
* -> id
* looking up id: i
94: LD 0, -5(5) 	load id value
95: ST 0, -16(5) 	op: push left OP2
* -> constant
96: LDC 0, 1(0) 	load const
* <- constant
97: LD 1, -16(5) 	op: load left
98: ADD 0, 1, 0 op +
* <- op
99: LD 1, -14(5) 	op: load left
100: ST 0, 0(1) 	assign: store value
* <- op
* <- compound statement
* <- while
* <- compound statement
101: LD 7, -1(5) 	return to caller
66: LDA 7, 35(7) 	Jump around function body
* <- fundecl
* Processing function: main
* jump around function body here
103: ST 0, -1(5) 	store return
* -> compound statement
* processing local var: i
* -> op
* -> id
* looking up id: i
104: LDA 0, -5(5) 	load id address
* <- id
105: ST 0, -3(5) 	op: push left AssignExp
* -> constant
106: LDC 0, 0(0) 	load const
* <- constant
107: LD 1, -3(5) 	op: load left
108: ST 0, 0(1) 	assign: store value
* <- op
* -> while
* while: jump after body comes back here
* -> op
* -> id
* looking up id: i
109: LD 0, -5(5) 	load id value
110: ST 0, -4(5) 	op: push left OP2
* -> constant
111: LDC 0, 10(0) 	load const
* <- constant
112: LD 1, -4(5) 	op: load left
113: SUB 0, 1, 0 op <
114: JLT 0, 2(7) 	
115: LDC 0, 0(0) 	false case
116: LDA 7, 1(7) 	unconditional jump
117: LDC 0, 1(0) 	true case
* <- op
* -> compound statement
* -> op
118: LD 1, -5(5) 	op: load left
119: ST 0, 0(1) 	assign: store value
* <- op
* -> op
* -> id
* looking up id: i
120: LDA 0, -5(5) 	load id address
* <- id
121: ST 0, -6(5) 	op: push left AssignExp
* -> op
* -> id
* looking up id: i
122: LD 0, -5(5) 	load id value
123: ST 0, -8(5) 	op: push left OP2
* -> constant
124: LDC 0, 1(0) 	load const
* <- constant
125: LD 1, -8(5) 	op: load left
126: ADD 0, 1, 0 op +
* <- op
127: LD 1, -6(5) 	op: load left
128: ST 0, 0(1) 	assign: store value
* <- op
* <- compound statement
* <- while
* -> call of function: sort
* -> id
* looking up id: x
129: LD 0, 0(5) 	load id value
* -> constant
130: LDC 0, 0(0) 	load const
* <- constant
* -> constant
131: LDC 0, 10(0) 	load const
* <- constant
* <- call
* -> op
* -> id
* looking up id: i
132: LDA 0, -5(5) 	load id address
* <- id
133: ST 0, -6(5) 	op: push left AssignExp
* -> constant
134: LDC 0, 0(0) 	load const
* <- constant
135: LD 1, -6(5) 	op: load left
136: ST 0, 0(1) 	assign: store value
* <- op
* -> while
* while: jump after body comes back here
* -> op
* -> id
* looking up id: i
137: LD 0, -5(5) 	load id value
138: ST 0, -7(5) 	op: push left OP2
* -> constant
139: LDC 0, 10(0) 	load const
* <- constant
140: LD 1, -7(5) 	op: load left
141: SUB 0, 1, 0 op <
142: JLT 0, 2(7) 	
143: LDC 0, 0(0) 	false case
144: LDA 7, 1(7) 	unconditional jump
145: LDC 0, 1(0) 	true case
* <- op
* -> compound statement
* -> call of function: output
* <- call
* -> op
* -> id
* looking up id: i
146: LDA 0, -5(5) 	load id address
* <- id
147: ST 0, -9(5) 	op: push left AssignExp
* -> op
* -> id
* looking up id: i
148: LD 0, -5(5) 	load id value
149: ST 0, -11(5) 	op: push left OP2
* -> constant
150: LDC 0, 1(0) 	load const
* <- constant
151: LD 1, -11(5) 	op: load left
152: ADD 0, 1, 0 op +
* <- op
153: LD 1, -9(5) 	op: load left
154: ST 0, 0(1) 	assign: store value
* <- op
* <- compound statement
* <- while
* <- compound statement
155: LD 7, -1(5) 	return to caller
102: LDA 7, 53(7) 	Jump around function body
* <- fundecl
156: ST 5, -10(5) 	push ofp
157: LDA 5, -10(5) 	push frame
158: LDA 0, 1(7) 	load ac with ret ptr
159: LDA 7, -57(7) 	jump to main loc
160: LD 5, 0(5) 	pop frame
* end of execution.
161: HALT 0, 0, 0 
