"""
TODO:
    *Connect database 
    *Fix camera
    *Define id for students
    *Create table for login
"""

import numpy as np
import cv2 as cv
import os.path
import subprocess as sub
from subprocess import STDOUT, PIPE
from urllib.request import urlopen

CAM = 0
FRONTAL_FACE_CASCADE = 'haarcascades/haarcascade_frontalface_default.xml'
DATA = 'train_data.yml'
LOGO = cv.imread("Logo.png", flags=1)

def main ():
    faceDetect = cv.CascadeClassifier(FRONTAL_FACE_CASCADE)
    recognizer = cv.face.LBPHFaceRecognizer_create()
    recognizer.read(DATA)
    font = cv.FONT_HERSHEY_COMPLEX_SMALL
    camera = cv.VideoCapture(CAM)

    faceName = 'None'

    while True:
        ret, image = camera.read()
        print(ret)
        grayImage = cv.cvtColor(image, cv.COLOR_BGR2GRAY)
        face = faceDetect.detectMultiScale(grayImage, scaleFactor=1.3, minNeighbors=5)
        #image = cv.cvtColor(image, flags=-1)
        for (x, y, w, h) in face:
            cv.rectangle(image, pt1=(x,y), pt2=(x+w,y+w), color=(255,255,0), thickness=2)
            id, conf = recognizer.predict(grayImage[y:y+h, x:x+w])

            if int(id) == 18250009: faceName = "Ivan"
            if int(id) == 18260006: faceName = "Vega"
            if int(id) == 18280007: faceName = "Gerardo"
            if int(id) == 18620010: faceName = "Willy"
            numControl = id

            cv.putText(image, faceName, org=(x,y), fontFace=font, fontScale=1.5, color=(255,255,0), thickness=2)

        imageWithLogo = putLogo(image, LOGO)
        cv.putText(image, '"L" -> LOG', (10,425), font, 1, (0,0,255), 2, cv.LINE_AA)
        cv.putText(image, '"Q" -> SALIR', (10,450), font, 1, (0,0,255), 2, cv.LINE_AA)
        cv.imshow("Rostros", image)
        #FIXME:
        if cv.waitKey(delay=1) == ord('l'):
            executeJava('PDBInv.jar', id)
        if cv.waitKey(delay=1) == ord('q'): break


    camera.release()
    cv.destroyAllWindows()

def putLogo (image, logo):
    rows, cols, channels = logo.shape
    roi = image[0:rows, 0:cols ]

    grayLogo = cv.cvtColor(logo, cv.COLOR_BGR2GRAY)
    ret, mask = cv.threshold(src=grayLogo, thresh=10, maxval=255, type=cv.THRESH_BINARY)
    maskInverse = cv.bitwise_not(mask)
    
    imageBg = cv.bitwise_and(roi, roi, mask = maskInverse)
    logoFg = cv.bitwise_and(logo, logo,mask = mask)
    dst = cv.add(imageBg, logoFg)
    #dst = cv.addWeighted(imageBg,0.6,logoFg,0.4, 0)

    image[0:rows, 0:cols ] = dst

    return image

def compileJava (javaFile):
    sub.check_call(['javac', javaFile])

def executeJava (javaFile, stdin):
    javaClass, ext = os.path.splitext(str(javaFile))
    #cmd = ['java', javaFile]
    cmd = 'java -jar {} {}'.format(javaFile, stdin)
    #cmd = 'java Main {}'.format(javaFile).encode('uint8')
    proc = sub.Popen(cmd, shell=True)#stdin=PIPE, stdout=PIPE, stderr=STDOUT)
    stdout, stderr = proc.communicate(stdin)


if __name__ == '__main__':
    #dbConnection()
    main()
