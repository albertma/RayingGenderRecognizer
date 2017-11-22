/***********************************************************
* Developer: Minhas Kamal (minhaskamal024@gmail.com)       *
* Website: https://github.com/MinhasKamal/Intellectron     *
* License: MIT License                                     *
***********************************************************/

package com.raying.ml.intellectron.neuralnetworks;

import java.util.LinkedList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.raying.ml.intellectron.util.NeuralNetworkUtils;

public class DeepNeuralNetwork{

	private LinkedList<MultiLayerNeuralNetwork> neuralNetworks;
	
	public static String DEEP_NEURAL_NETWORK_TAG= "deep-neural-network";
	
	/**
	 * 
	 * @param numbersOfNeuronsInLayers if number of layers is less than 3, i.e. 
	 * <code>(numbersOfNeuronsInLayers<3)</code>, then the network will not be created.
	 * Last 2 layers are trained with back-propagation only & other layer are trained 
	 * like auto-encoder. 
	 * @param learningRateOfAllLayers
	 * @param numberOfInputs
	 */
	public DeepNeuralNetwork(int[] numbersOfNeuronsInLayers, double learningRateOfAllLayers, int numberOfInputs) {

		if(numbersOfNeuronsInLayers.length<3){
			return;
		}
		
		this.neuralNetworks = new LinkedList<MultiLayerNeuralNetwork>();
		
		this.neuralNetworks.add(new MultiLayerNeuralNetwork(
				new int[]{numbersOfNeuronsInLayers[0], numberOfInputs},
				learningRateOfAllLayers, numberOfInputs
			));	//input layer
		for(int i=1; i<numbersOfNeuronsInLayers.length-2; i++){
			this.neuralNetworks.add(new MultiLayerNeuralNetwork(
					new int[]{numbersOfNeuronsInLayers[i], numbersOfNeuronsInLayers[i-1]},
					learningRateOfAllLayers, numbersOfNeuronsInLayers[i-1]
				));	//hidden layers
		}
		this.neuralNetworks.add(new MultiLayerNeuralNetwork(
				new int[]{numbersOfNeuronsInLayers[numbersOfNeuronsInLayers.length-2],
				numbersOfNeuronsInLayers[numbersOfNeuronsInLayers.length-1]},
				learningRateOfAllLayers, numbersOfNeuronsInLayers[numbersOfNeuronsInLayers.length-3]
			));	//output layer
	}
	
	public DeepNeuralNetwork(Node deepNeuralNetworkNode) {
		this.neuralNetworks = new LinkedList<MultiLayerNeuralNetwork>();
		
		NodeList multiLayerNetworkNodeList = deepNeuralNetworkNode.getChildNodes();
		for(int i=0; i<multiLayerNetworkNodeList.getLength(); i++){
			Node multiLayerNetworkNode = multiLayerNetworkNodeList.item(i);
			if(multiLayerNetworkNode.getNodeName()==MultiLayerNeuralNetwork.MULTI_LAYER_NEURAL_NETWORK_TAG){
				this.neuralNetworks.add(new MultiLayerNeuralNetwork(multiLayerNetworkNode));
			}
		}
	}
	
	//PROCESS///////////////////////////////////////////////////////////////
	
	public void processForward(double[] inputs){
		inputs = NeuralNetworkUtils.addBias(inputs);
		
		neuralNetworks.get(0).getLayer(0).processForward(inputs);
		for(int i=1; i<neuralNetworks.size()-1; i++){
			neuralNetworks.get(i).getLayer(0).processForward(neuralNetworks.get(i-1).getLayer(0).getOutputs());
		}
		neuralNetworks.get(neuralNetworks.size()-1).processForward(
				NeuralNetworkUtils.removeBias( neuralNetworks.get(neuralNetworks.size()-2).getLayer(0).getOutputs() ));
	}
	
	public double[] getOutputs(){
		return neuralNetworks.getLast().getOutputs();
	}
	
	//TRAIN//////////////////////////////////////////////////////////////////
	//back-propagation training for last 2 layers, unsupervised auto-encoder//
	//training for the rest.//
	public void train(double[] inputs, double[] expectedOutputs){
		double[] input = inputs.clone();
		
		for(int i=0; i<this.neuralNetworks.size()-1; i++){
			this.neuralNetworks.get(i).processForward(input);
			this.neuralNetworks.get(i).calculateErrors(input);
			this.neuralNetworks.get(i).learn(input);
			
			input = NeuralNetworkUtils.removeBias(this.neuralNetworks.get(i).getLayer(0).getOutputs());
		}
		
		this.neuralNetworks.getLast().processForward(input);
		this.neuralNetworks.getLast().calculateErrors(expectedOutputs);
		this.neuralNetworks.getLast().learn(input);
	}
	
	//after training in unsupervised way, trains the full network using//
	//back-propagation.//
	public void train2(double[] inputs, double[] expectedOutputs){
		train(inputs, expectedOutputs);
		
		calculateErrors();
		learn(inputs.clone());
	}
	
	//ERROR CALCULATION//
	private void calculateErrors(){
		for(int i=neuralNetworks.size()-1; i>0; i--){
			this.neuralNetworks.get(i-1).getLayer(0).calculateErrors(this.neuralNetworks.get(i).getLayer(0));
		}
	}
	
	//LEARN//
	private void learn(double[] inputs){
		inputs = NeuralNetworkUtils.addBias(inputs);
		
		for(int i=this.neuralNetworks.size()-1-1; i>0; i--){
			this.neuralNetworks.get(i).getLayer(0).learn(this.neuralNetworks.get(i-1).getLayer(0));
		}
		this.neuralNetworks.getFirst().getLayer(0).learn(inputs);
	}
	
	//KNOWLEDGE STORE///////////////////////////////////////////////////////
	
	public String dump(){
		String string = "<"+DEEP_NEURAL_NETWORK_TAG+">\n";
		
		for(MultiLayerNeuralNetwork neuralNetwork: this.neuralNetworks){
			string += neuralNetwork.dump()+"\n";
		}
		
		string += "</"+DEEP_NEURAL_NETWORK_TAG+">";
		
		return string;
	}
}



