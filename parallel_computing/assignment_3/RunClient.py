import os

for i in range(0, 2):
	os.system('java -cp "Client/" Client ' + str(i) + ' input.txt &')