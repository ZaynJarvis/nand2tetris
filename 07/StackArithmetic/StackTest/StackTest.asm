@17
D=A
@SP
AM=M+1
A=A-1
M=D
@17
D=A
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
A=A-1
D=M-D
@LABEL. 0
D;JEQ
D=0
@LABEL. 1
0;JMP
(LABEL. 0)
D=-1
(LABEL. 1)
@SP
A=M-1
M=D
@17
D=A
@SP
AM=M+1
A=A-1
M=D
@16
D=A
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
A=A-1
D=M-D
@LABEL. 2
D;JEQ
D=0
@LABEL. 3
0;JMP
(LABEL. 2)
D=-1
(LABEL. 3)
@SP
A=M-1
M=D
@16
D=A
@SP
AM=M+1
A=A-1
M=D
@17
D=A
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
A=A-1
D=M-D
@LABEL. 4
D;JEQ
D=0
@LABEL. 5
0;JMP
(LABEL. 4)
D=-1
(LABEL. 5)
@SP
A=M-1
M=D
@892
D=A
@SP
AM=M+1
A=A-1
M=D
@891
D=A
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
A=A-1
D=M-D
@LABEL. 6
D;JLT
D=0
@LABEL. 7
0;JMP
(LABEL. 6)
D=-1
(LABEL. 7)
@SP
A=M-1
M=D
@891
D=A
@SP
AM=M+1
A=A-1
M=D
@892
D=A
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
A=A-1
D=M-D
@LABEL. 8
D;JLT
D=0
@LABEL. 9
0;JMP
(LABEL. 8)
D=-1
(LABEL. 9)
@SP
A=M-1
M=D
@891
D=A
@SP
AM=M+1
A=A-1
M=D
@891
D=A
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
A=A-1
D=M-D
@LABEL. 10
D;JLT
D=0
@LABEL. 11
0;JMP
(LABEL. 10)
D=-1
(LABEL. 11)
@SP
A=M-1
M=D
@32767
D=A
@SP
AM=M+1
A=A-1
M=D
@32766
D=A
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
A=A-1
D=M-D
@LABEL. 12
D;JGT
D=0
@LABEL. 13
0;JMP
(LABEL. 12)
D=-1
(LABEL. 13)
@SP
A=M-1
M=D
@32766
D=A
@SP
AM=M+1
A=A-1
M=D
@32767
D=A
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
A=A-1
D=M-D
@LABEL. 14
D;JGT
D=0
@LABEL. 15
0;JMP
(LABEL. 14)
D=-1
(LABEL. 15)
@SP
A=M-1
M=D
@32766
D=A
@SP
AM=M+1
A=A-1
M=D
@32766
D=A
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
A=A-1
D=M-D
@LABEL. 16
D;JGT
D=0
@LABEL. 17
0;JMP
(LABEL. 16)
D=-1
(LABEL. 17)
@SP
A=M-1
M=D
@57
D=A
@SP
AM=M+1
A=A-1
M=D
@31
D=A
@SP
AM=M+1
A=A-1
M=D
@53
D=A
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
A=A-1
D=M+D
M=D
@112
D=A
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
A=A-1
D=M-D
M=D
@SP
A=M-1
D=M
M=-D
@SP
AM=M-1
D=M
A=A-1
D=M&D
M=D
@82
D=A
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
A=A-1
D=M|D
M=D
@SP
A=M-1
D=M
M=!D
(END)
@END
0;JMP
