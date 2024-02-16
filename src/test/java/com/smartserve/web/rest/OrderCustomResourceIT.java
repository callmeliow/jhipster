package com.smartserve.web.rest;

import static com.smartserve.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.smartserve.IntegrationTest;
import com.smartserve.domain.OrderCustom;
import com.smartserve.repository.OrderCustomRepository;
import com.smartserve.service.dto.OrderCustomDTO;
import com.smartserve.service.mapper.OrderCustomMapper;
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
 * Integration tests for the {@link OrderCustomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderCustomResourceIT {

    private static final String DEFAULT_CUSTOMIZATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMIZATION_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/order-customs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderCustomRepository orderCustomRepository;

    @Autowired
    private OrderCustomMapper orderCustomMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderCustomMockMvc;

    private OrderCustom orderCustom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderCustom createEntity(EntityManager em) {
        OrderCustom orderCustom = new OrderCustom().customizationName(DEFAULT_CUSTOMIZATION_NAME).price(DEFAULT_PRICE);
        return orderCustom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderCustom createUpdatedEntity(EntityManager em) {
        OrderCustom orderCustom = new OrderCustom().customizationName(UPDATED_CUSTOMIZATION_NAME).price(UPDATED_PRICE);
        return orderCustom;
    }

    @BeforeEach
    public void initTest() {
        orderCustom = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderCustom() throws Exception {
        int databaseSizeBeforeCreate = orderCustomRepository.findAll().size();
        // Create the OrderCustom
        OrderCustomDTO orderCustomDTO = orderCustomMapper.toDto(orderCustom);
        restOrderCustomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderCustomDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderCustom in the database
        List<OrderCustom> orderCustomList = orderCustomRepository.findAll();
        assertThat(orderCustomList).hasSize(databaseSizeBeforeCreate + 1);
        OrderCustom testOrderCustom = orderCustomList.get(orderCustomList.size() - 1);
        assertThat(testOrderCustom.getCustomizationName()).isEqualTo(DEFAULT_CUSTOMIZATION_NAME);
        assertThat(testOrderCustom.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createOrderCustomWithExistingId() throws Exception {
        // Create the OrderCustom with an existing ID
        orderCustom.setId(1L);
        OrderCustomDTO orderCustomDTO = orderCustomMapper.toDto(orderCustom);

        int databaseSizeBeforeCreate = orderCustomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderCustomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustom in the database
        List<OrderCustom> orderCustomList = orderCustomRepository.findAll();
        assertThat(orderCustomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderCustoms() throws Exception {
        // Initialize the database
        orderCustomRepository.saveAndFlush(orderCustom);

        // Get all the orderCustomList
        restOrderCustomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderCustom.getId().intValue())))
            .andExpect(jsonPath("$.[*].customizationName").value(hasItem(DEFAULT_CUSTOMIZATION_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));
    }

    @Test
    @Transactional
    void getOrderCustom() throws Exception {
        // Initialize the database
        orderCustomRepository.saveAndFlush(orderCustom);

        // Get the orderCustom
        restOrderCustomMockMvc
            .perform(get(ENTITY_API_URL_ID, orderCustom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderCustom.getId().intValue()))
            .andExpect(jsonPath("$.customizationName").value(DEFAULT_CUSTOMIZATION_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingOrderCustom() throws Exception {
        // Get the orderCustom
        restOrderCustomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderCustom() throws Exception {
        // Initialize the database
        orderCustomRepository.saveAndFlush(orderCustom);

        int databaseSizeBeforeUpdate = orderCustomRepository.findAll().size();

        // Update the orderCustom
        OrderCustom updatedOrderCustom = orderCustomRepository.findById(orderCustom.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderCustom are not directly saved in db
        em.detach(updatedOrderCustom);
        updatedOrderCustom.customizationName(UPDATED_CUSTOMIZATION_NAME).price(UPDATED_PRICE);
        OrderCustomDTO orderCustomDTO = orderCustomMapper.toDto(updatedOrderCustom);

        restOrderCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderCustomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderCustom in the database
        List<OrderCustom> orderCustomList = orderCustomRepository.findAll();
        assertThat(orderCustomList).hasSize(databaseSizeBeforeUpdate);
        OrderCustom testOrderCustom = orderCustomList.get(orderCustomList.size() - 1);
        assertThat(testOrderCustom.getCustomizationName()).isEqualTo(UPDATED_CUSTOMIZATION_NAME);
        assertThat(testOrderCustom.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingOrderCustom() throws Exception {
        int databaseSizeBeforeUpdate = orderCustomRepository.findAll().size();
        orderCustom.setId(longCount.incrementAndGet());

        // Create the OrderCustom
        OrderCustomDTO orderCustomDTO = orderCustomMapper.toDto(orderCustom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderCustomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustom in the database
        List<OrderCustom> orderCustomList = orderCustomRepository.findAll();
        assertThat(orderCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderCustom() throws Exception {
        int databaseSizeBeforeUpdate = orderCustomRepository.findAll().size();
        orderCustom.setId(longCount.incrementAndGet());

        // Create the OrderCustom
        OrderCustomDTO orderCustomDTO = orderCustomMapper.toDto(orderCustom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustom in the database
        List<OrderCustom> orderCustomList = orderCustomRepository.findAll();
        assertThat(orderCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderCustom() throws Exception {
        int databaseSizeBeforeUpdate = orderCustomRepository.findAll().size();
        orderCustom.setId(longCount.incrementAndGet());

        // Create the OrderCustom
        OrderCustomDTO orderCustomDTO = orderCustomMapper.toDto(orderCustom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderCustomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderCustomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderCustom in the database
        List<OrderCustom> orderCustomList = orderCustomRepository.findAll();
        assertThat(orderCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderCustomWithPatch() throws Exception {
        // Initialize the database
        orderCustomRepository.saveAndFlush(orderCustom);

        int databaseSizeBeforeUpdate = orderCustomRepository.findAll().size();

        // Update the orderCustom using partial update
        OrderCustom partialUpdatedOrderCustom = new OrderCustom();
        partialUpdatedOrderCustom.setId(orderCustom.getId());

        partialUpdatedOrderCustom.customizationName(UPDATED_CUSTOMIZATION_NAME);

        restOrderCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderCustom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderCustom))
            )
            .andExpect(status().isOk());

        // Validate the OrderCustom in the database
        List<OrderCustom> orderCustomList = orderCustomRepository.findAll();
        assertThat(orderCustomList).hasSize(databaseSizeBeforeUpdate);
        OrderCustom testOrderCustom = orderCustomList.get(orderCustomList.size() - 1);
        assertThat(testOrderCustom.getCustomizationName()).isEqualTo(UPDATED_CUSTOMIZATION_NAME);
        assertThat(testOrderCustom.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateOrderCustomWithPatch() throws Exception {
        // Initialize the database
        orderCustomRepository.saveAndFlush(orderCustom);

        int databaseSizeBeforeUpdate = orderCustomRepository.findAll().size();

        // Update the orderCustom using partial update
        OrderCustom partialUpdatedOrderCustom = new OrderCustom();
        partialUpdatedOrderCustom.setId(orderCustom.getId());

        partialUpdatedOrderCustom.customizationName(UPDATED_CUSTOMIZATION_NAME).price(UPDATED_PRICE);

        restOrderCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderCustom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderCustom))
            )
            .andExpect(status().isOk());

        // Validate the OrderCustom in the database
        List<OrderCustom> orderCustomList = orderCustomRepository.findAll();
        assertThat(orderCustomList).hasSize(databaseSizeBeforeUpdate);
        OrderCustom testOrderCustom = orderCustomList.get(orderCustomList.size() - 1);
        assertThat(testOrderCustom.getCustomizationName()).isEqualTo(UPDATED_CUSTOMIZATION_NAME);
        assertThat(testOrderCustom.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingOrderCustom() throws Exception {
        int databaseSizeBeforeUpdate = orderCustomRepository.findAll().size();
        orderCustom.setId(longCount.incrementAndGet());

        // Create the OrderCustom
        OrderCustomDTO orderCustomDTO = orderCustomMapper.toDto(orderCustom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderCustomDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustom in the database
        List<OrderCustom> orderCustomList = orderCustomRepository.findAll();
        assertThat(orderCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderCustom() throws Exception {
        int databaseSizeBeforeUpdate = orderCustomRepository.findAll().size();
        orderCustom.setId(longCount.incrementAndGet());

        // Create the OrderCustom
        OrderCustomDTO orderCustomDTO = orderCustomMapper.toDto(orderCustom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderCustom in the database
        List<OrderCustom> orderCustomList = orderCustomRepository.findAll();
        assertThat(orderCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderCustom() throws Exception {
        int databaseSizeBeforeUpdate = orderCustomRepository.findAll().size();
        orderCustom.setId(longCount.incrementAndGet());

        // Create the OrderCustom
        OrderCustomDTO orderCustomDTO = orderCustomMapper.toDto(orderCustom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderCustomMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderCustomDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderCustom in the database
        List<OrderCustom> orderCustomList = orderCustomRepository.findAll();
        assertThat(orderCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderCustom() throws Exception {
        // Initialize the database
        orderCustomRepository.saveAndFlush(orderCustom);

        int databaseSizeBeforeDelete = orderCustomRepository.findAll().size();

        // Delete the orderCustom
        restOrderCustomMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderCustom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderCustom> orderCustomList = orderCustomRepository.findAll();
        assertThat(orderCustomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
