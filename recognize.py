import numpy as np
import cv2 as cv
from urllib.request import urlopen

CAM = 0
URL = 'http://192.168.0.3:8080/shot.jpg'
FRONTAL_FACE_CASCADE = 'haarcascades/haarcascade_frontalface_default.xml'
DATA = 'train_data.yml'
LOGO = cv.imread("Logo.png", flags=1)


def main ():
    faceDetect = cv.CascadeClassifier(FRONTAL_FACE_CASCADE)
    recognizer = cv.face.LBPHFaceRecognizer_create()
    recognizer.read(DATA)
    font = cv.FONT_HERSHEY_COMPLEX_SMALL
    camera = cv.VideoCapture(CAM)

    while True:
        ret, image = camera.read()
        print(ret)
        #image = phoneCamera(URL)
        grayImage = cv.cvtColor(image, cv.COLOR_BGR2GRAY)
        face = faceDetect.detectMultiScale(grayImage, scaleFactor=1.3, minNeighbors=5)
        #image = cv.cvtColor(image, code=-1)
        for (x, y, w, h) in face:
            cv.rectangle(image, pt1=(x,y), pt2=(x+w,y+w), color=(255,255,0), thickness=2)
            id, conf = recognizer.predict(grayImage[y:y+h, x:x+w])

            if int(id) == 1: faceName = "Ivan"
            if int(id) == 2: faceName = "Vega"
            if int(id) == 3: faceName = "Gerardo"
            cv.putText(image, faceName, org=(x,y), fontFace=font, fontScale=1.5, color=(255,255,0), thickness=2)

        imageWithLogo = putLogo(image, LOGO)
        cv.imwrite("logogg.jpg", imageWithLogo)
        cv.imshow("Rostros", image)
        if cv.waitKey(delay=1) == ord('q'):break
    
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
