import os
import numpy as np
import cv2 as cv
from PIL import Image

PATH = 'faces'

faces = []
facesId = []
faceRecognizer = cv.face.LBPHFaceRecognizer_create()

def getImages (path):
    imagesPath = [os.path.join(path, file) for file in os.listdir(path)]
    print(imagesPath)
    

    for image in imagesPath:
        faceImage = Image.open(image).convert('L')
        faceNp = np.array(faceImage, dtype='uint8')
        id = int(os.path.split(image)[1].split('.')[1])
        print(id)
        faces.append(faceNp)
        facesId.append(id)
        cv.imshow("Entrenando...", faceNp)
        cv.waitKey(delay=100)
    
    print(facesId)
    return faces, np.array(facesId)

def main ():
    faces, ids = getImages(PATH)
    faceRecognizer.train(faces, ids)
    faceRecognizer.save(str('train_data.yml'))
    cv.destroyAllWindows()

if __name__ == '__main__':
    main()