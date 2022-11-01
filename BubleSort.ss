      -- R1 is boolean flag for work done
      -- R2 is current index
      -- R3 is is unsorted bound
      -- R4, R5, and R6 are work registers
      -- R7 is current loop limit
0:  addi R1, R0, 1
4:  addi R3, R0, 15
LABEL StartSort
8:  addi R2, R0, 4000
12:  addi R7, R0, 4
16:  mul R7, R7, R3
20:  addi R7, R7, 4000
LABEL Bubble
24:  lw R4, 0(R2)
28:  lw R5, 4(R2)
32:  sub R6, R4, R5
36:  BGTZ R6, NeedsSort
LABEL ret
40:  addi R2, R2, 4
44:  bne R2, R7, Bubble
48:  bne R1, R0, Done
52:  addi R3, R3, -1
56:  bne R3, R0, StartSort
LABEL Done
60:  halt
LABEL NeedsSort
64:  add R1, R0, R0
68:  sw R4, 4(R2)
72:  sw R5, 0(R2)
76:  j ret
