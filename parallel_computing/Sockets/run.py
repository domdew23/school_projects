import os

os.system("javac Client/*.java")
os.system("javac Server/*.java")
os.system('java -cp "Server/" Server')