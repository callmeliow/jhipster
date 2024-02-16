package com.smartserve.web.rest;

import static com.smartserve.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.smartserve.IntegrationTest;
import com.smartserve.domain.OrderFood;
import com.smartserve.repository.OrderFoodRepository;
import com.smartserve.service.dto.OrderFoodDTO;
import com.smartserve.service.mapper.OrderFoodMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrderFoodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderFoodResourceIT {

    private static final String DEFAULT_FOOD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ORDER_ITEM_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ORDER_ITEM_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/order-foods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderFoodRepository orderFoodRepository;

    @Autowired
    private OrderFoodMapper orderFoodMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderFoodMockMvc;

    private OrderFood orderFood;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderFood createEntity(EntityManager em) {
        OrderFood orderFood = new OrderFood().foodName(DEFAULT_FOOD_NAME).price(DEFAULT_PRICE).orderItemPrice(DEFAULT_ORDER_ITEM_PRICE);
        return orderFood;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderFood createUpdatedEntity(EntityManager em) {
        OrderFood orderFood = new OrderFood().foodName(UPDATED_FOOD_NAME).price(UPDATED_PRICE).orderItemPrice(UPDATED_ORDER_ITEM_PRICE);
        return orderFood;
    }

    @BeforeEach
    public void initTest() {
        orderFood = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderFood() throws Exception {
        int databaseSizeBeforeCreate = orderFoodRepository.findAll().size();
        // Create the OrderFood
        OrderFoodDTO orderFoodDTO = orderFoodMapper.toDto(orderFood);
        restOrderFoodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderFoodDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderFood in the database
        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeCreate + 1);
        OrderFood testOrderFood = orderFoodList.get(orderFoodList.size() - 1);
        assertThat(testOrderFood.getFoodName()).isEqualTo(DEFAULT_FOOD_NAME);
        assertThat(testOrderFood.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testOrderFood.getOrderItemPrice()).isEqualByComparingTo(DEFAULT_ORDER_ITEM_PRICE);
    }

    @Test
    @Transactional
    void createOrderFoodWithExistingId() throws Exception {
        // Create the OrderFood with an existing ID
        orderFood.setId(1L);
        OrderFoodDTO orderFoodDTO = orderFoodMapper.toDto(orderFood);

        int databaseSizeBeforeCreate = orderFoodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderFoodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderFoodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderFood in the database
        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFoodNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderFoodRepository.findAll().size();
        // set the field null
        orderFood.setFoodName(null);

        // Create the OrderFood, which fails.
        OrderFoodDTO orderFoodDTO = orderFoodMapper.toDto(orderFood);

        restOrderFoodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderFoodDTO)))
            .andExpect(status().isBadRequest());

        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderFoods() throws Exception {
        // Initialize the database
        orderFoodRepository.saveAndFlush(orderFood);

        // Get all the orderFoodList
        restOrderFoodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderFood.getId().intValue())))
            .andExpect(jsonPath("$.[*].foodName").value(hasItem(DEFAULT_FOOD_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].orderItemPrice").value(hasItem(sameNumber(DEFAULT_ORDER_ITEM_PRICE))));
    }

    @Test
    @Transactional
    void getOrderFood() throws Exception {
        // Initialize the database
        orderFoodRepository.saveAndFlush(orderFood);

        // Get the orderFood
        restOrderFoodMockMvc
            .perform(get(ENTITY_API_URL_ID, orderFood.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderFood.getId().intValue()))
            .andExpect(jsonPath("$.foodName").value(DEFAULT_FOOD_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.orderItemPrice").value(sameNumber(DEFAULT_ORDER_ITEM_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingOrderFood() throws Exception {
        // Get the orderFood
        restOrderFoodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderFood() throws Exception {
        // Initialize the database
        orderFoodRepository.saveAndFlush(orderFood);

        int databaseSizeBeforeUpdate = orderFoodRepository.findAll().size();

        // Update the orderFood
        OrderFood updatedOrderFood = orderFoodRepository.findById(orderFood.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderFood are not directly saved in db
        em.detach(updatedOrderFood);
        updatedOrderFood.foodName(UPDATED_FOOD_NAME).price(UPDATED_PRICE).orderItemPrice(UPDATED_ORDER_ITEM_PRICE);
        OrderFoodDTO orderFoodDTO = orderFoodMapper.toDto(updatedOrderFood);

        restOrderFoodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderFoodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderFoodDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderFood in the database
        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeUpdate);
        OrderFood testOrderFood = orderFoodList.get(orderFoodList.size() - 1);
        assertThat(testOrderFood.getFoodName()).isEqualTo(UPDATED_FOOD_NAME);
        assertThat(testOrderFood.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testOrderFood.getOrderItemPrice()).isEqualByComparingTo(UPDATED_ORDER_ITEM_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingOrderFood() throws Exception {
        int databaseSizeBeforeUpdate = orderFoodRepository.findAll().size();
        orderFood.setId(longCount.incrementAndGet());

        // Create the OrderFood
        OrderFoodDTO orderFoodDTO = orderFoodMapper.toDto(orderFood);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderFoodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderFoodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderFoodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderFood in the database
        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderFood() throws Exception {
        int databaseSizeBeforeUpdate = orderFoodRepository.findAll().size();
        orderFood.setId(longCount.incrementAndGet());

        // Create the OrderFood
        OrderFoodDTO orderFoodDTO = orderFoodMapper.toDto(orderFood);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderFoodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderFoodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderFood in the database
        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderFood() throws Exception {
        int databaseSizeBeforeUpdate = orderFoodRepository.findAll().size();
        orderFood.setId(longCount.incrementAndGet());

        // Create the OrderFood
        OrderFoodDTO orderFoodDTO = orderFoodMapper.toDto(orderFood);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderFoodMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderFoodDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderFood in the database
        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderFoodWithPatch() throws Exception {
        // Initialize the database
        orderFoodRepository.saveAndFlush(orderFood);

        int databaseSizeBeforeUpdate = orderFoodRepository.findAll().size();

        // Update the orderFood using partial update
        OrderFood partialUpdatedOrderFood = new OrderFood();
        partialUpdatedOrderFood.setId(orderFood.getId());

        partialUpdatedOrderFood.orderItemPrice(UPDATED_ORDER_ITEM_PRICE);

        restOrderFoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderFood.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderFood))
            )
            .andExpect(status().isOk());

        // Validate the OrderFood in the database
        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeUpdate);
        OrderFood testOrderFood = orderFoodList.get(orderFoodList.size() - 1);
        assertThat(testOrderFood.getFoodName()).isEqualTo(DEFAULT_FOOD_NAME);
        assertThat(testOrderFood.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testOrderFood.getOrderItemPrice()).isEqualByComparingTo(UPDATED_ORDER_ITEM_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateOrderFoodWithPatch() throws Exception {
        // Initialize the database
        orderFoodRepository.saveAndFlush(orderFood);

        int databaseSizeBeforeUpdate = orderFoodRepository.findAll().size();

        // Update the orderFood using partial update
        OrderFood partialUpdatedOrderFood = new OrderFood();
        partialUpdatedOrderFood.setId(orderFood.getId());

        partialUpdatedOrderFood.foodName(UPDATED_FOOD_NAME).price(UPDATED_PRICE).orderItemPrice(UPDATED_ORDER_ITEM_PRICE);

        restOrderFoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderFood.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderFood))
            )
            .andExpect(status().isOk());

        // Validate the OrderFood in the database
        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeUpdate);
        OrderFood testOrderFood = orderFoodList.get(orderFoodList.size() - 1);
        assertThat(testOrderFood.getFoodName()).isEqualTo(UPDATED_FOOD_NAME);
        assertThat(testOrderFood.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testOrderFood.getOrderItemPrice()).isEqualByComparingTo(UPDATED_ORDER_ITEM_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingOrderFood() throws Exception {
        int databaseSizeBeforeUpdate = orderFoodRepository.findAll().size();
        orderFood.setId(longCount.incrementAndGet());

        // Create the OrderFood
        OrderFoodDTO orderFoodDTO = orderFoodMapper.toDto(orderFood);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderFoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderFoodDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderFoodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderFood in the database
        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderFood() throws Exception {
        int databaseSizeBeforeUpdate = orderFoodRepository.findAll().size();
        orderFood.setId(longCount.incrementAndGet());

        // Create the OrderFood
        OrderFoodDTO orderFoodDTO = orderFoodMapper.toDto(orderFood);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderFoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderFoodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderFood in the database
        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderFood() throws Exception {
        int databaseSizeBeforeUpdate = orderFoodRepository.findAll().size();
        orderFood.setId(longCount.incrementAndGet());

        // Create the OrderFood
        OrderFoodDTO orderFoodDTO = orderFoodMapper.toDto(orderFood);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderFoodMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderFoodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderFood in the database
        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderFood() throws Exception {
        // Initialize the database
        orderFoodRepository.saveAndFlush(orderFood);

        int databaseSizeBeforeDelete = orderFoodRepository.findAll().size();

        // Delete the orderFood
        restOrderFoodMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderFood.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderFood> orderFoodList = orderFoodRepository.findAll();
        assertThat(orderFoodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
