import csv
from os.path import dirname, join


filename = join(dirname(__file__), "changed2.csv")

def reader():
    read = []
    with open(filename, newline='') as csvfile:
        spamreader = csv.reader(csvfile, delimiter=';', quotechar='|')
        for row in spamreader:
            read.append(int(row[0]))
    csvfile.close()
    return read[0]



def helloworld():

    result = [12,14,16,15]

    with open(filename, 'a', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerows(map(lambda x: [x], result))
    csvfile.close()

    return reader()


