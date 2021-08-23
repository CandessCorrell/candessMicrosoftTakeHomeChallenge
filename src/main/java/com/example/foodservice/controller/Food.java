package com.example.foodservice.controller;

public class Food  {
    /**
     * Possible enhancement notes: constuct with all Food values and not just the necessary ones here
     * Setter methods would be added here if we ever wanted to modify any existing Food data
     */
    private int locationId;
    private String applicant;
    private String block;


    public Food(int locationId, String applicant,String block) throws Exception {
        if(applicant != null) {
            this.locationId = locationId;
            this.applicant = applicant;
            this.block = block;
        } else{
            throw new Exception("Values are not appropriate. Please recheck that all values are availble to save Food item");

        }
    }

    public int getLocationId(){
        return this.locationId;
    }

    public String getApplicant(){
        return this.applicant;
    }

    public String getBlock(){
        return block;
    }

    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Food))
            return false;
        Food food = (Food) o;
        return (this.locationId == food.locationId)
                && (this.applicant).equals(food.applicant)
                && (this.block ==food.block);
    }

}