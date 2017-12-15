import os

if not os.path.exists('Server.class'):
	os.system("javac *.java")

os.system("java Server &")

for i in range(0, 1):
	os.system("ssh -f nuc25 java -cp 'public_html/parallel_computing/Sockets/' Client 1 &")
