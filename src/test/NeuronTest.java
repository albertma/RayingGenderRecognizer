/***********************************************************
* Developer: Minhas Kamal (minhaskamal024@gmail.com)       *
* Website: https://github.com/MinhasKamal/Intellectron     *
* License: MIT License                                     *
***********************************************************/

package test;

import com.raying.ml.intellectron.neuralnetworks.neuronLayers.neuron.Neuron;


public class NeuronTest {
	public static void main(String[] args) throws Exception {
		
		Neuron neuron = new Neuron(3);

		//input & bias//
		double[][] input = new double[][]{
			{0, 0, 1},
			{0, 1, 1},
			{1, 0, 1},
			{1, 1, 1}
		};
		//output//
		double[] output = new double[]{
			1,
			0,
			1,
			0
		};
		
		//processing with gradient descent//
		double error;
		double learningRate = 1;
		for(int cycle=0; cycle<1000; cycle++){
			for(int i=0; i<input.length; i++){
				double o = neuron.processSignalForward(input[i]);
				
				error = o*(1-o)*(output[i]-o);
				neuron.learn(error*learningRate, input[i]);
				
				System.out.print(o + ", ");
			}
			System.out.println(" -> "+cycle);
		}
		
		System.out.println(neuron.dump());
	}
}
