import numpy as np
import cv2 as cv
from urllib.request import urlopen

CAM = 0
FRONTAL_FACE_CASCADE = 'haarcascades/haarcascade_frontalface_default.xml'
URL = 'http://192.168.0.3:8080/shot.jpg'
LOGO = cv.imread("Logo.png", flags=1)

maxSamples = 20

def main ():
    faceDetect = cv.CascadeClassifier(FRONTAL_FACE_CASCADE)
    camera = cv.VideoCapture(CAM)
    sampleNum = 0

    id = input("Id: ")

    while True:
        ret, image = camera.read()
        #image = phoneCamera(URL) 
        grayImage = cv.cvtColor(image, cv.COLOR_BGR2GRAY)
        face = faceDetect.detectMultiScale(grayImage, scaleFactor=1.3, minNeighbors=5)

        for (x, y, w, h) in face:
            sampleNum = sampleNum + 1
            cv.imwrite("faces/face." + str(id) + "." + str(sampleNum) + ".jpg", grayImage[y:y+h, x:x+w])
            cv.waitKey(delay=100)
            cv.rectangle(image, pt1=(x,y), pt2=(x+h,y+w), color=(255,255,0), thickness=2)

        imageWithLogo = putLogo(image, LOGO)
        cv.imshow("Capturando...", imageWithLogo)
        if sampleNum >= maxSamples: break
    
    camera.release()
    cv.destroyAllWindows()

def putLogo (image, logo):
    rows, cols, channels = logo.shape
    roi = image[0:rows, 0:cols ]

    grayLogo = cv.cvtColor(logo, cv.COLOR_BGR2GRAY)
    ret, mask = cv.threshold(grayLogo, 10, 255, cv.THRESH_BINARY)
    maskInverse = cv.bitwise_not(mask)
    
    imageBg = cv.bitwise_and(roi, roi, mask = maskInverse)
    logoFg = cv.bitwise_and(logo, logo,mask = mask)
    dst = cv.add(imageBg, logoFg)
    image[0:rows, 0:cols ] = dst

    return image

def phoneCamera (url):
    imageResponse = urlopen(url)
    imageNp = np.array(bytearray(imageResponse.read()), dtype='uint8')
    image = cv.imdecode(imageNp, flags=-1)
    return image

if __name__ == '__main__':
    main()
