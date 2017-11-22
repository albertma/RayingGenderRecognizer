package com.raying.ml.genderRecognizer;


import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.*;
import org.opencv.imgproc.Imgproc;


/**
 * Created by albertma on 17/11/2017.
 */
public class FaceDetector {
	
	public Mat[] snipFace(String image, Size size){


		Mat matImage = Imgcodecs.imread(image, Imgcodecs.IMREAD_UNCHANGED);

		Rect[] rectFace = detectFace(matImage);
		int rectFaceLength = rectFace.length;

		Mat[] matFace = new Mat[rectFaceLength];
		
		for(int i=0; i<rectFaceLength; i++){
			
			matFace[i] = matImage.submat(rectFace[i]);
			Imgproc.resize(matFace[i], matFace[i], size);
			

		}
		
		return matFace;
	}
	
	
	private Rect[] detectFace(Mat matImage){
		
		MatOfRect faces = new MatOfRect();
		//C:\Users\admin\Desktop\My_Java_Programs\ObjectRecognition\resource\data
		String HumanFace4 = "src/res/knowledge/haarcascade_frontalface_alt.xml";
		
		CascadeClassifier cascadeClassifier = new CascadeClassifier(HumanFace4);

		cascadeClassifier.detectMultiScale(matImage, faces , 1.1, 10, Objdetect.CASCADE_DO_CANNY_PRUNING, new Size(20, 20),
				matImage.size());
		
		//System.out.println(faces.dump());///test
		
		return faces.toArray();
	}
	

}
