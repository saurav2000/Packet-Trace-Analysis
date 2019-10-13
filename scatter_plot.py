import matplotlib.pyplot as plt

def plot(s, sy, sx, l=False):
	file1 = open(s, "r")
	allLines = file1.readlines()

	xlist = [0.0]
	ylist = [0.0]

	for line in allLines:
		xy = line.split()
		xlist.append(float(xy[0]))
		ylist.append(float(xy[1]))

	if(l):
		plt.plot(xlist, ylist)
	else:
		plt.scatter(xlist, ylist)
	plt.title(s)
	plt.ylabel(sy)
	plt.xlabel(sx)
	plt.savefig(s+"_figure.png")
	plt.clf()

plot('file1.csv_q4', 'P(Conn. Dur)<X', 'Connection Duration')
plot('file2.csv_q4', 'P(Conn. Dur)<X', 'Connection Duration')
plot('file3.csv_q4', 'P(Conn. Dur)<X', 'Connection Duration')

plot('file1.csv_q5_send_vs_time', 'Incoming(Server) data size', 'Duration of conn.')
plot('file2.csv_q5_send_vs_time', 'Incoming(Server) data size', 'Duration of conn.')
plot('file3.csv_q5_send_vs_time', 'Incoming(Server) data size', 'Duration of conn.')
plot('file1.csv_q5_send_vs_recv', 'Incoming(Server) data size', 'Outgoing(Server) data size')
plot('file2.csv_q5_send_vs_recv', 'Incoming(Server) data size', 'Outgoing(Server) data size')
plot('file3.csv_q5_send_vs_recv', 'Incoming(Server) data size', 'Outgoing(Server) data size')

plot('file1.csv_q6', 'P(Inter-arr time)<X', 'Inter Arrival Time(Connection)')
plot('file2.csv_q6', 'P(Inter-arr time)<X', 'Inter Arrival Time(Connection)')
plot('file3.csv_q6', 'P(Inter-arr time)<X', 'Inter Arrival Time(Connection)')

plot('file1.csv_q7', 'P(Inter-arr time)<X', 'Inter Arrival Time(Packets)')
plot('file2.csv_q7', 'P(Inter-arr time)<X', 'Inter Arrival Time(Packets)')
plot('file3.csv_q7', 'P(Inter-arr time)<X', 'Inter Arrival Time(Packets)')

plot('file1.csv_q8_send', 'P(Packet length)<X', 'Incoming(Server) Packet length')
plot('file2.csv_q8_send', 'P(Packet length)<X', 'Incoming(Server) Packet length')
plot('file3.csv_q8_send', 'P(Packet length)<X', 'Incoming(Server) Packet length')
plot('file1.csv_q8_recv', 'P(Packet length)<X', 'Outgoing(Server) Packet length')
plot('file2.csv_q8_recv', 'P(Packet length)<X', 'Outgoing(Server) Packet length')
plot('file3.csv_q8_recv', 'P(Packet length)<X', 'Outgoing(Server) Packet length')

plot('file1.csv_q11_avg_queue_size', 'Avg Queue Size', 'Rho', True)
plot('file2.csv_q11_avg_queue_size', 'Avg Queue Size', 'Rho', True)
plot('file3.csv_q11_avg_queue_size', 'Avg Queue Size', 'Rho', True)
plot('file1.csv_q11_avg_waiting_time', 'Avg Waiting Time', 'Rho', True)
plot('file2.csv_q11_avg_waiting_time', 'Avg Waiting Time', 'Rho', True)
plot('file3.csv_q11_avg_waiting_time', 'Avg Waiting Time', 'Rho', True)