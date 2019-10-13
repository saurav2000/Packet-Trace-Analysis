import matplotlib.pyplot as plt

def plot(s):
	file1 = open(s, "r")
	allLines = file1.readlines()

	xlist = []
	ylist = []
	for line in allLines:
		xy = line.split()
		xlist.append(float(xy[0]))
		ylist.append(float(xy[1]))

	plt.bar(xlist, ylist, width=0.9, align='edge')
	plt.title(s)
	plt.ylabel('No.of Connections')
	plt.xlabel('Hour of the day')
	plt.savefig(s+"_figure.png")
	plt.clf()

plot('file1.csv_q3')
plot('file2.csv_q3')
plot('file3.csv_q3')