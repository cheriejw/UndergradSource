import numpy as np
import cv2
import subprocess
from SimpleCV import Image
import SimpleCV
from SimpleCV import ColorModel
from SimpleCV import Color
from ShapeDetector import ShapeDetector
from ColorLabeler import ColorLabeler
import argparse
import imutils


cap = cv2.VideoCapture(0)

while True:
    
    _, img = cap.read()

    hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)

    lower_red1 = np.array([0,70,100])
    upper_red1 = np.array([9,255,255])
    lower_red2 = np.array([165,70,100])
    upper_red2 = np.array([175,255,255])

    lower_blue = np.array([105,70,100])
    upper_blue = np.array([115,255,255])

    lower_yellow = np.array([20,140,140])
    upper_yellow = np.array([30,255,255])

    mask_red1 = cv2.inRange(hsv, lower_red1, upper_red1)
    mask_red2 = cv2.inRange(hsv, lower_red2, upper_red2)
    mask_blue = cv2.inRange(hsv, lower_blue, upper_blue)
    mask_yellow = cv2.inRange(hsv, lower_yellow, upper_yellow)
    
    output_red = cv2.bitwise_and(img, img, mask = mask_red1+mask_red2)
    output_blue = cv2.bitwise_and(img, img, mask = mask_blue)
    output_yellow = cv2.bitwise_and(img, img, mask = mask_yellow)
    
    imgray_red = cv2.cvtColor(output_red, cv2.COLOR_BGR2GRAY)
    imgray_blue = cv2.cvtColor(output_blue, cv2.COLOR_BGR2GRAY)
    imgray_yellow = cv2.cvtColor(output_yellow, cv2.COLOR_BGR2GRAY)
    
    ret_red,thresh_red = cv2.threshold(imgray_red,0,255,0)
    ret_blue,thresh_blue = cv2.threshold(imgray_blue,0,255,0)
    ret_yellow,thresh_yellow = cv2.threshold(imgray_yellow,0,255,0)
    
    contours_red,hierarchy_red = cv2.findContours(thresh_red,cv2.RETR_TREE,cv2.CHAIN_APPROX_NONE)
    contours_blue,hierarchy_blue = cv2.findContours(thresh_blue,cv2.RETR_TREE,cv2.CHAIN_APPROX_NONE)
    contours_yellow,hierarchy_yellow = cv2.findContours(thresh_yellow,cv2.RETR_TREE,cv2.CHAIN_APPROX_NONE)

    cnt_r = contours_red[0]
    cnt_b = contours_blue[0]
    cnt_y = contours_yellow[0]

    sd = ShapeDetector()
    
    for contour in contours_red:
        shape = sd.detect(contour)
        if (cv2.contourArea(contour) > 50) and (shape == "rectangle"):
            perimeter = cv2.arcLength(cnt_r, True)
            if perimeter > 15:
                cv2.drawContours(img,contour,-1,(0,0,255),3)
    for contour in contours_blue:
        if (cv2.contourArea(contour) > 50):
            perimeter = cv2.arcLength(cnt_b, True)
            if perimeter > 15:
                cv2.drawContours(img,contour,-1,(255,0,0),3)
    for contour in contours_yellow:
        if (cv2.contourArea(contour) > 50):
            perimeter = cv2.arcLength(cnt_y, True)
            if perimeter > 15:
                cv2.drawContours(img,contour,-1,(0,209,209),3)

    
    
    cv2.imshow("image", img)
    #cv2.imshow("res", output_red+output_blue+output_yellow)
    cv2.waitKey(3)
