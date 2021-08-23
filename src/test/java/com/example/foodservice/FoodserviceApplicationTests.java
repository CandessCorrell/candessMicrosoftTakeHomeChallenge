package com.example.foodservice;

import com.example.foodservice.controller.Food;
import com.example.foodservice.controller.FoodDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FoodServiceApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FoodDAO foodDAO;

	@Test
	/**
	 * Tests basic upload of given CSV file
	 */
	public void testUpload() throws Exception {
		this.mockMvc.perform(get("/upload"))
				.andDo(print()).
				andExpect(status().isOk())
				.andExpect(content().string(containsString("619")));

	}

	@Test
	/**
	 * Tests foodByLocation after CSV upload and success of present locationId
	 */
	public void testGetFoodByLocationIdSuccess() throws Exception{
		this.mockMvc.perform(get("/upload"));
		mockMvc.perform( MockMvcRequestBuilders
				.get("/foodbylocation?locationId=751253")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.locationId").value(751253));

	}
@Test
/**
 * Tests foodByLocation after CSV upload and failure of asbsent locationId
 */
	public void testGetFoodByLocationIdError() throws Exception{
		this.mockMvc.perform(get("/upload"));
		mockMvc.perform( MockMvcRequestBuilders
				.get("/foodbylocation/{locationId}", 0)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is4xxClientError());

	}
	@Test
	/**
	 * Tests foodByLocation after failure of CSV upload and therefore failure of asbsent locationId
	 */
	public void testGetFoodByLocationNoUpload() throws Exception{
		mockMvc.perform( MockMvcRequestBuilders
				.get("/foodbylocation?locationId=0")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());

	}
@Test
/**
 * Tests foodByBlock after CSV upload and success of present block
 */
	public void testGetFoodByBlockSuccess() throws Exception{
		this.mockMvc.perform(get("/upload"));
		mockMvc.perform( MockMvcRequestBuilders
				.get("/foodbyblock?block=4106")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.FoodItems").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.FoodItems").isNotEmpty());


	}
	@Test
/**
 * Tests foodByLocation after CSV upload and failure of absent block
 */
	public void testGetFoodByBlockError() throws Exception{
		this.mockMvc.perform(get("/upload"));
		mockMvc.perform( MockMvcRequestBuilders
				.get("/foodbyblock?block=NOTHERE")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.FoodItems").doesNotExist());

	}
	@Test
	/**
	 * Tests foodByBlock after failure of CSV upload and therefore failure of absent block
	 */
	public void testGetFoodByBlockNoUpload() throws Exception{
		mockMvc.perform( MockMvcRequestBuilders
				.get("/foodbyblock?block=3707")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.FoodItems").doesNotExist());

	}


	@Test
	/**
	 * Tests successful addNewFood after no CSV upload and therefore only food in datastore
	 */
	public void testAddNewFoodSuccess() throws Exception {
		Food testFood = new Food(123, "test","testblock");
		mockMvc.perform(get("/newfood?locationId=123&applicant=test&block=testblock")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(testFood)))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.locationId").value(123))
				.andExpect(MockMvcResultMatchers.jsonPath("$.applicant").value("test"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.block").value("testblock"));

	}
	@Test
	/**
	 * Tests failure of addNewFood after insufficient params are inputted
	 */
	public void testAddNewFoodInsufficientParams() throws Exception {
		Food testFood = new Food(123, "test","testblock");
		mockMvc.perform(get("/newfood?locationId=123&applicant=test")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(testFood)))
				.andExpect(status().is4xxClientError());
	}



	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}



