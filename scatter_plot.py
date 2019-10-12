import numpy as np
import matplotlib.pyplot as plt

def plot(s):
	file1 = open(s, "r")
	allLines = file1.readlines()

	xlist = [0.0]
	ylist = [0.0]

	for line in allLines:
		xy = line.split()
		xlist.append(float(xy[0]))
		ylist.append(float(xy[1]))

	plt.scatter(xlist, ylist)
	plt.title('Scatter plot 1')
	plt.ylabel('Prob(l<X)')
	plt.xlabel('Packet length')
	plt.savefig(s+"_figure.png")
	plt.clf()

plot('file1.csv_q8_recv')
plot('file2.csv_q8_recv')
plot('file3.csv_q8_recv')
# plt.savefig('q5_superimposed.png')