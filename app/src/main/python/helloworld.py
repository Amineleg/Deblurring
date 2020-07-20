import csv
from os.path import dirname, join
import imageio
import numpy as np
from PIL import Image
from keras.models import load_model

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

frame_shape = (84, 84)

def pro(filepath):
    img = Image.open(filepath)
    img = np.array(img)[60:-18, 35:-20, 2]
    path = join(dirname(__file__), "jeu.png")
    #img = np.array(np.resize(img, frame_shape))
    imageio.imwrite(path, np.array(img))
    return path
