package com.smartserve.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.smartserve.IntegrationTest;
import com.smartserve.domain.OrderItemCustom;
import com.smartserve.repository.OrderItemCustomRepository;
import com.smartserve.service.dto.OrderItemCustomDTO;
import com.smartserve.service.mapper.OrderItemCustomMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link OrderItemCustomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderItemCustomResourceIT {

    private static final String ENTITY_API_URL = "/api/order-item-customs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderItemCustomRepository orderItemCustomRepository;

    @Autowired
    private OrderItemCustomMapper orderItemCustomMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemCustomMockMvc;

    private OrderItemCustom orderItemCustom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItemCustom createEntity(EntityManager em) {
        OrderItemCustom orderItemCustom = new OrderItemCustom();
        return orderItemCustom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItemCustom createUpdatedEntity(EntityManager em) {
        OrderItemCustom orderItemCustom = new OrderItemCustom();
        return orderItemCustom;
    }

    @BeforeEach
    public void initTest() {
        orderItemCustom = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderItemCustom() throws Exception {
        int databaseSizeBeforeCreate = orderItemCustomRepository.findAll().size();
        // Create the OrderItemCustom
        OrderItemCustomDTO orderItemCustomDTO = orderItemCustomMapper.toDto(orderItemCustom);
        restOrderItemCustomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemCustomDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderItemCustom in the database
        List<OrderItemCustom> orderItemCustomList = orderItemCustomRepository.findAll();
        assertThat(orderItemCustomList).hasSize(databaseSizeBeforeCreate + 1);
        OrderItemCustom testOrderItemCustom = orderItemCustomList.get(orderItemCustomList.size() - 1);
    }

    @Test
    @Transactional
    void createOrderItemCustomWithExistingId() throws Exception {
        // Create the OrderItemCustom with an existing ID
        orderItemCustom.setId(1L);
        OrderItemCustomDTO orderItemCustomDTO = orderItemCustomMapper.toDto(orderItemCustom);

        int databaseSizeBeforeCreate = orderItemCustomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemCustomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemCustom in the database
        List<OrderItemCustom> orderItemCustomList = orderItemCustomRepository.findAll();
        assertThat(orderItemCustomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderItemCustoms() throws Exception {
        // Initialize the database
        orderItemCustomRepository.saveAndFlush(orderItemCustom);

        // Get all the orderItemCustomList
        restOrderItemCustomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItemCustom.getId().intValue())));
    }

    @Test
    @Transactional
    void getOrderItemCustom() throws Exception {
        // Initialize the database
        orderItemCustomRepository.saveAndFlush(orderItemCustom);

        // Get the orderItemCustom
        restOrderItemCustomMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItemCustom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItemCustom.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrderItemCustom() throws Exception {
        // Get the orderItemCustom
        restOrderItemCustomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderItemCustom() throws Exception {
        // Initialize the database
        orderItemCustomRepository.saveAndFlush(orderItemCustom);

        int databaseSizeBeforeUpdate = orderItemCustomRepository.findAll().size();

        // Update the orderItemCustom
        OrderItemCustom updatedOrderItemCustom = orderItemCustomRepository.findById(orderItemCustom.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderItemCustom are not directly saved in db
        em.detach(updatedOrderItemCustom);
        OrderItemCustomDTO orderItemCustomDTO = orderItemCustomMapper.toDto(updatedOrderItemCustom);

        restOrderItemCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemCustomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemCustomDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemCustom in the database
        List<OrderItemCustom> orderItemCustomList = orderItemCustomRepository.findAll();
        assertThat(orderItemCustomList).hasSize(databaseSizeBeforeUpdate);
        OrderItemCustom testOrderItemCustom = orderItemCustomList.get(orderItemCustomList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingOrderItemCustom() throws Exception {
        int databaseSizeBeforeUpdate = orderItemCustomRepository.findAll().size();
        orderItemCustom.setId(longCount.incrementAndGet());

        // Create the OrderItemCustom
        OrderItemCustomDTO orderItemCustomDTO = orderItemCustomMapper.toDto(orderItemCustom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemCustomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemCustom in the database
        List<OrderItemCustom> orderItemCustomList = orderItemCustomRepository.findAll();
        assertThat(orderItemCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderItemCustom() throws Exception {
        int databaseSizeBeforeUpdate = orderItemCustomRepository.findAll().size();
        orderItemCustom.setId(longCount.incrementAndGet());

        // Create the OrderItemCustom
        OrderItemCustomDTO orderItemCustomDTO = orderItemCustomMapper.toDto(orderItemCustom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemCustom in the database
        List<OrderItemCustom> orderItemCustomList = orderItemCustomRepository.findAll();
        assertThat(orderItemCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderItemCustom() throws Exception {
        int databaseSizeBeforeUpdate = orderItemCustomRepository.findAll().size();
        orderItemCustom.setId(longCount.incrementAndGet());

        // Create the OrderItemCustom
        OrderItemCustomDTO orderItemCustomDTO = orderItemCustomMapper.toDto(orderItemCustom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemCustomMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemCustomDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItemCustom in the database
        List<OrderItemCustom> orderItemCustomList = orderItemCustomRepository.findAll();
        assertThat(orderItemCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderItemCustomWithPatch() throws Exception {
        // Initialize the database
        orderItemCustomRepository.saveAndFlush(orderItemCustom);

        int databaseSizeBeforeUpdate = orderItemCustomRepository.findAll().size();

        // Update the orderItemCustom using partial update
        OrderItemCustom partialUpdatedOrderItemCustom = new OrderItemCustom();
        partialUpdatedOrderItemCustom.setId(orderItemCustom.getId());

        restOrderItemCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItemCustom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItemCustom))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemCustom in the database
        List<OrderItemCustom> orderItemCustomList = orderItemCustomRepository.findAll();
        assertThat(orderItemCustomList).hasSize(databaseSizeBeforeUpdate);
        OrderItemCustom testOrderItemCustom = orderItemCustomList.get(orderItemCustomList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateOrderItemCustomWithPatch() throws Exception {
        // Initialize the database
        orderItemCustomRepository.saveAndFlush(orderItemCustom);

        int databaseSizeBeforeUpdate = orderItemCustomRepository.findAll().size();

        // Update the orderItemCustom using partial update
        OrderItemCustom partialUpdatedOrderItemCustom = new OrderItemCustom();
        partialUpdatedOrderItemCustom.setId(orderItemCustom.getId());

        restOrderItemCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItemCustom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItemCustom))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemCustom in the database
        List<OrderItemCustom> orderItemCustomList = orderItemCustomRepository.findAll();
        assertThat(orderItemCustomList).hasSize(databaseSizeBeforeUpdate);
        OrderItemCustom testOrderItemCustom = orderItemCustomList.get(orderItemCustomList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingOrderItemCustom() throws Exception {
        int databaseSizeBeforeUpdate = orderItemCustomRepository.findAll().size();
        orderItemCustom.setId(longCount.incrementAndGet());

        // Create the OrderItemCustom
        OrderItemCustomDTO orderItemCustomDTO = orderItemCustomMapper.toDto(orderItemCustom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItemCustomDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemCustom in the database
        List<OrderItemCustom> orderItemCustomList = orderItemCustomRepository.findAll();
        assertThat(orderItemCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderItemCustom() throws Exception {
        int databaseSizeBeforeUpdate = orderItemCustomRepository.findAll().size();
        orderItemCustom.setId(longCount.incrementAndGet());

        // Create the OrderItemCustom
        OrderItemCustomDTO orderItemCustomDTO = orderItemCustomMapper.toDto(orderItemCustom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemCustom in the database
        List<OrderItemCustom> orderItemCustomList = orderItemCustomRepository.findAll();
        assertThat(orderItemCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderItemCustom() throws Exception {
        int databaseSizeBeforeUpdate = orderItemCustomRepository.findAll().size();
        orderItemCustom.setId(longCount.incrementAndGet());

        // Create the OrderItemCustom
        OrderItemCustomDTO orderItemCustomDTO = orderItemCustomMapper.toDto(orderItemCustom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemCustomMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemCustomDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItemCustom in the database
        List<OrderItemCustom> orderItemCustomList = orderItemCustomRepository.findAll();
        assertThat(orderItemCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderItemCustom() throws Exception {
        // Initialize the database
        orderItemCustomRepository.saveAndFlush(orderItemCustom);

        int databaseSizeBeforeDelete = orderItemCustomRepository.findAll().size();

        // Delete the orderItemCustom
        restOrderItemCustomMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItemCustom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderItemCustom> orderItemCustomList = orderItemCustomRepository.findAll();
        assertThat(orderItemCustomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
