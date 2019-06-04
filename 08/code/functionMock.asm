@28
D=A
@SP
M=D

@25
M=1
@26
M=-1
@27
M=1

@377
D=A
@1
M=D
@587
D=A
@2
M=D
@1387
D=A
@3
M=D
@1900
D=A
@4
M=D
// ---

// func call
// store arg to temp
@2              @SP
D=A             MD=M+1
@SP             @15
D=M-D           M=D-1
@15             
M=D             
// store address
@FUNC$RETURN.x
D=A
@SP
A=M
M=D
// Temp frame
@LCL
D=M
@SP
AM=M+1
M=D
@ARG
D=M
@SP
AM=M+1
M=D
@THIS
D=M
@SP
AM=M+1
M=D
@THAT
D=M
@SP
AM=M+1
M=D
// Store ARG
@15
D=M
@ARG
M=D
// Add SP
@SP
M=M+1

@FUNC
0;JMP
(FUNC$RETURN.x)

// mock after return
@SP
AM=M-1
D=M
@12
M=D
(END)
@END
0;JMP



// function dec
(FUNC)
@SP
D=M
@LCL
M=D
// local var
@SP
A=M
M=0
A=A+1
M=0
A=A+1
M=0
A=A+1
M=0
A=A+1
D=A
@SP
M=D
// function content
@99
D=A
@SP
AM=M+1
A=A-1
M=D
// Store temp ARG address
@ARG
D=M
@14
M=D
// restore pointers
@5
D=A
@LCL
D=M-D
@13
M=D
A=D
D=M
@15
M=D
@13
AM=M+1
D=M
@1
M=D
@13
AM=M+1
D=M
@2
M=D
@13
AM=M+1
D=M
@3
M=D
@13
AM=M+1
D=M
@4
M=D
// return
@SP
A=M-1
D=M
@14
A=M
M=D
D=A
@SP
M=D+1
// return to function
@15
A=M
0;JMP
