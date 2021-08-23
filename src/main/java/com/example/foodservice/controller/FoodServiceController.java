package com.example.foodservice.controller;

import com.fasterxml.jackson.annotation.JsonAlias;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
public class FoodServiceController {
    FoodDAO foodDAO = new FoodDAO();

    /**
     * Returns the a count of the amount of Food items successfully saved via file.
     * Currently hardcoded file name
     *
     * @return int of the count of successfully saved items
     *
     * Possible enhancement notes: adding file parameter to allow user file upload
     */
    @GetMapping(path="/upload")
    @ResponseBody
    public int saveFood() throws FileNotFoundException {
        String filename = "Mobile_Food_Facility_Permit.csv";
        return foodDAO.saveFood(filename);
    }

    /**
     * Returns the list of all the Food objects in the current datastore
     *
     * @return JSONObject of List of Food items that are in the current datastore(null if empty)
     *
     * Possible enhancement notes: adding param that will return all food trucks or all pushcarts
     */
    @GetMapping(path="/food")
    @ResponseBody
    public JSONObject getAllFood() {
        List<Food> list = foodDAO.getAllFood();
        if(list == null){
            return null;
        } else{
            return convertToJSON(list);
        }
}

    /**
     * Returns the Food object that has the given locationId
     *
     * @param locationId - the locationId for the requested food object
     * @return Food item with the according locationId (presumed unique). Returns null if food at the given locationId does not exist
     */
    @GetMapping(path="/foodbylocation")
    @ResponseBody
    public Food getFoodByLocationId(@RequestParam("locationId") int locationId) {
        return foodDAO.getFoodByLocationId(locationId);
    }

    /**
     * Returns the list of Food objects that is on the given block
     *
     * @param block - the block for the requested food objects
     * @return JSONObject of List of Food items that have the according block json (null if empty)
     *
     * Possible enhancement notes: adding logic so that block param is case insensitive
     * adding param that will retun all food trucks or push carts
     */
    @GetMapping(path="/foodbyblock")
    @ResponseBody
    public JSONObject getAllByBlock(@RequestParam("block") String block) {
        List<Food> list = foodDAO.getFoodByBlock(block);
        if(list == null){
            return null;
        } else{
            return convertToJSON(list);
        }

    }

    /**
     * Returns the Food object that is successfully uploaded
     *
     * @param locationId - the locationId for the new food object
     * @param applicant - the address for the new food object
     * @param block - the block for the new food object
     * @return Food (null if error occurs with saving food item)
     *
     * Possible enhancement notes: Add check to see if Food item already exists, can modify current Food object if so
     * allows full Food object upload with all values (and not just the required ones here)
     */
    @GetMapping(path="/newfood")
    @ResponseBody
    public Food addNewFoodTruck(@RequestParam("locationId") int locationId, @RequestParam("applicant") String applicant, @RequestParam("block") String block) {
       return foodDAO.addNewFood(locationId, applicant, block);

    }

    /**
     * Returns the JSONObject where the list of given Food objects is converted into
     *
     * @param foodList - the given list of Food object that needs to be converted to a JSONObject for ease of printing
     * @return JSONObject (null if empty)
     */
    private JSONObject convertToJSON (List<Food> foodList){
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(Food food : foodList) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("locationId", food.getLocationId());
            formDetailsJson.put("applicant", food.getApplicant());
            formDetailsJson.put("block", food.getBlock());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("FoodItems", jsonArray);
        return responseDetailsJson;

    }
}