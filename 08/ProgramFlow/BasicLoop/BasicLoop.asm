@0
D=A
@SP
AM=M+1
A=A-1
M=D
@0
D=A
@1
A=M
D=A+D
@15
M=D
@SP
AM=M-1
D=M
@15
A=M
M=D
(LOOP_START)
@0
D=A
@2
A=M
A=A+D
D=M
@SP
AM=M+1
A=A-1
M=D
@0
D=A
@1
A=M
A=A+D
D=M
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
@0
D=A
@1
A=M
D=A+D
@15
M=D
@SP
AM=M-1
D=M
@15
A=M
M=D
@0
D=A
@2
A=M
A=A+D
D=M
@SP
AM=M+1
A=A-1
M=D
@1
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
@0
D=A
@2
A=M
D=A+D
@15
M=D
@SP
AM=M-1
D=M
@15
A=M
M=D
@0
D=A
@2
A=M
A=A+D
D=M
@SP
AM=M+1
A=A-1
M=D
@SP
AM=M-1
D=M
@LOOP_START
D;JNE
@0
D=A
@1
A=M
A=A+D
D=M
@SP
AM=M+1
A=A-1
M=D
(END)
@END
0;JMP
