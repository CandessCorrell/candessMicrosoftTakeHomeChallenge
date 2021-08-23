package com.example.foodservice.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodDAO{
    /**
     * Possible enhancement notes: Checking requirements to see if this application will be get heavy vs put heavy
     * Datastructures can be modified accordingly for most optimal performance/memory allocation
     */
    private HashMap<Integer, Food> locationMap;
    private HashMap<String, List<Food>> blockMap;

    /**
     * Possible enhancement notes: construct with all Food values and not just the necessary ones here
     */
    public FoodDAO(){
        this.locationMap = new HashMap<>();
        this.blockMap = new HashMap<>();
    }
    /**
     * Possible enhancement notes: construct with all Food values and not just the necessary ones here
     */
    public FoodDAO(HashMap<Integer, Food> locationMap, HashMap<String, List<Food>> blockMap){
        this.locationMap = locationMap;
        this.blockMap = blockMap;
    }

    public List<Food> getAllFood() {
    List<Food> foodList = new ArrayList<>();
        this.locationMap.forEach((k, v) -> {
            foodList.add(v);
        });
        return foodList;
    }


    public Food getFoodByLocationId(int locationId){
       return locationMap.get(locationId);
    }

    public List<Food> getFoodByBlock(String block){
      return blockMap.get(block);
    }

    public Food addNewFood(int locationId, String applicant, String block){
       try {
           Food newFood = new Food(locationId, applicant, block);

           this.locationMap.put(locationId, newFood);

           List<Food> currBlockList;
           if(this.blockMap.get(block) != null) {
               currBlockList = this.blockMap.get(block);
           } else{
               currBlockList = new ArrayList<>();
           }
           currBlockList.add(newFood);
           this.blockMap.put(block, currBlockList);
           return newFood;
       } catch (Exception e){
           e.printStackTrace();
           return null;
        }
    }



    public int saveFood(String fileLocation) throws FileNotFoundException {
        int successCount =0;
        try {
            FileReader fileReader = new FileReader(fileLocation);
            BufferedReader br = new BufferedReader(fileReader);
            String currLine =br.readLine();
            String[] currLineArr = null;
            while ((currLine = br.readLine()) != null) {
               currLineArr = currLine.split(",");
               try {
                   this.addNewFood(Integer.parseInt(currLineArr[0]), currLineArr[1], currLineArr[7]);
                   successCount++;
               } catch(Exception e){
                   e.printStackTrace();
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return successCount;
    }



}