package com.raying.ml.genderRecognizer;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;


import java.util.ArrayList;



/**
 * Created by albertma on 17/11/2017.
 */
public class PersonDetector {
    private HOGDescriptor hog;

    public PersonDetector (){
        MatOfFloat matOfFloat =  HOGDescriptor.getDefaultPeopleDetector();
        this.hog = new HOGDescriptor();
        hog.setSVMDetector(matOfFloat);
    }


    public void detectAndDraw(Mat img) {
        MatOfRect found = new MatOfRect();
        ArrayList<Rect> foundFiltered = new ArrayList<Rect>();
        MatOfDouble foundWeight = new MatOfDouble();

        double t = Core.getTickCount();
        Size winStride = new Size(16, 16);
        Size padding = new Size(0, 0);

        this.hog.detectMultiScale(img, found, foundWeight, 0.0, winStride, padding, 1.05, 0.0, true);


        t = Core.getTickCount() - t;
        System.out.println("Detection Time: " + (t * 1000. / Core.getTickFrequency()) + " ms");

        Rect []foundRectList = found.toArray();
        double []foundWeightList = foundWeight.toArray();

//        for(int i = 0; i < foundRectList.length; i++){
//
//            Imgproc.rectangle(img, foundRectList[i].tl(), foundRectList[i].br(), new Scalar(0,0,255f) , 10);
//        }



        for(int i = 0; i < foundRectList.length; i++){
            Rect r = foundRectList[i];

            int j;

            for(j = 0; j < foundRectList.length; j++){
                if (j != i && r.equals(foundRectList[j])){

                    break;
                }else{
                    foundFiltered.add(r);
                }
            }
        }

        Rect[] foundFilteredRects = new Rect[foundFiltered.size()];
        draw_detections(img,foundRectList,2);
        draw_detections(img,foundFiltered.toArray(foundFilteredRects),4);
        Imgcodecs.imwrite("/Users/albertma/Pictures/doudou2.jpg", img);
//        System.out.println("foundFiltered size:" + foundFiltered.size());
//        for(int i = 0; i < foundFiltered.size(); i++){
//            Rect r = foundFiltered.get(i);
//            r.x += Math.round(r.width * 0.1);
//            r.width = (int)Math.round(r.width * 0.8);
//            r.y += Math.round(r.height * 0.07);
//            r.height = (int)Math.round(r.height * 0.8);
//            Imgproc.rectangle(img, r.tl(), r.br(), new Scalar(0,255,0), 3);
//        }
    }

    private void draw_detections(Mat img, Rect[]rects, int thickness){
        for (Rect rect: rects){
            Imgproc.rectangle(img, rect.tl(), rect.br(), new Scalar(0,0,255), thickness);
        }

    }

    public static  void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String image = "/Users/albertma/Pictures/doudou.jpg";
        Mat matImage = Imgcodecs.imread(image, Imgcodecs.IMREAD_UNCHANGED);
        PersonDetector personDetector = new PersonDetector();
        //personDetector.detectAndDrawPerson(image);
        personDetector.detectAndDraw(matImage);
    }

    public void detectAndDrawPerson(String imagePath){
        Mat image = Imgcodecs.imread(imagePath);
        double resizeWidth = Math.min(400, image.size().width);

        double resizeHeight = image.size().height;
        if (resizeWidth == 400f) {
            resizeHeight = image.size().height * 400 / image.size().width;
        }
        Size resizedSize = new Size(resizeWidth, resizeHeight);
        Mat resizedImg = Mat.zeros(resizedSize, image.type());

        Imgproc.resize(image, resizedImg,  resizedSize);

        //detect person in image
        Size winStride = new Size(8, 8);
        Size padding = new Size(32, 32);


    }
}
