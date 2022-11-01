--A program to find the sum of a list of numbers
-- The program uses a subroutine to add 2 numbers, as a demo
-- It also sets up a stack frame, although not needed for this program
-- 4000 = # of nums to sum
-- 4004  = location for sum to be put
-- 4008 = beginning of array of nums
-- 
-- R20, R21 - parameter passing regs
-- R30 = SP
-- R31 = Ret Addr Reg
-- R3 = size of array, in bytes
-- R4 = Address of beginning of array (4008)
-- R5 = first address past array, for loop termination
-- R6 = current address being worked on (loop i variable)
-- R7 = sum
-- R8 = current array data value
-- 
Begin Assembly
ADDI R30, R0, 5000
ADDI R4, R0, 4000
LW R2, 0(R4)
SLL R3, R2, 2
ADDI R4, R4, 8
ADD R5, R4, R3
ADD R6, R4, R0
ADD R7, R0, R0
LABEL LoopStart
BEQ R6, R5, PostLoop
LW R8, 0(R6)
ADD R20, R8, R0
ADD R21, R7, R0
JAL AddThem
ADD R7, R1, R0
ADDI R6, R6, 4
J LoopStart
LABEL PostLoop
SW R7, -4(R4)
HALT

LABEL AddThem
SW R31, 0(R30)
ADDI R30, R30, 4
SW R5, 0(R30)
ADDI R30, R30, 4
ADD R5, R20, R21
ADD R1, R5, R0
ADDI R30, R30, -4
LW R5, 0(R30)
ADDI R30, R30, -4
LW R31, 0(R30)
JR R31
NOP
End Assembly
-- begin main data
Begin Data 4000 44
10
0
23
71
33
5
93
82
34
13
111
23
End Data
-- stack
Begin Data 5000 100
End Data