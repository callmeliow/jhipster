package com.smartserve.web.rest;

import static com.smartserve.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.smartserve.IntegrationTest;
import com.smartserve.domain.OrderStage;
import com.smartserve.domain.enumeration.OrderStatus;
import com.smartserve.repository.OrderStageRepository;
import com.smartserve.service.dto.OrderStageDTO;
import com.smartserve.service.mapper.OrderStageMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link OrderStageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderStageResourceIT {

    private static final String DEFAULT_ORDER_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ORDER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ORDER_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.PENDING;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.COMPLETED;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DELETED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DELETED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/order-stages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderStageRepository orderStageRepository;

    @Autowired
    private OrderStageMapper orderStageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderStageMockMvc;

    private OrderStage orderStage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderStage createEntity(EntityManager em) {
        OrderStage orderStage = new OrderStage()
            .orderNo(DEFAULT_ORDER_NO)
            .orderDate(DEFAULT_ORDER_DATE)
            .status(DEFAULT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE)
            .deletedDate(DEFAULT_DELETED_DATE);
        return orderStage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderStage createUpdatedEntity(EntityManager em) {
        OrderStage orderStage = new OrderStage()
            .orderNo(UPDATED_ORDER_NO)
            .orderDate(UPDATED_ORDER_DATE)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .deletedDate(UPDATED_DELETED_DATE);
        return orderStage;
    }

    @BeforeEach
    public void initTest() {
        orderStage = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderStage() throws Exception {
        int databaseSizeBeforeCreate = orderStageRepository.findAll().size();
        // Create the OrderStage
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(orderStage);
        restOrderStageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderStageDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderStage in the database
        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeCreate + 1);
        OrderStage testOrderStage = orderStageList.get(orderStageList.size() - 1);
        assertThat(testOrderStage.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);
        assertThat(testOrderStage.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrderStage.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrderStage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrderStage.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testOrderStage.getDeletedDate()).isEqualTo(DEFAULT_DELETED_DATE);
    }

    @Test
    @Transactional
    void createOrderStageWithExistingId() throws Exception {
        // Create the OrderStage with an existing ID
        orderStage.setId(1L);
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(orderStage);

        int databaseSizeBeforeCreate = orderStageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderStageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderStageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderStage in the database
        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderStageRepository.findAll().size();
        // set the field null
        orderStage.setOrderNo(null);

        // Create the OrderStage, which fails.
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(orderStage);

        restOrderStageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderStageDTO)))
            .andExpect(status().isBadRequest());

        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderStageRepository.findAll().size();
        // set the field null
        orderStage.setOrderDate(null);

        // Create the OrderStage, which fails.
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(orderStage);

        restOrderStageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderStageDTO)))
            .andExpect(status().isBadRequest());

        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderStageRepository.findAll().size();
        // set the field null
        orderStage.setStatus(null);

        // Create the OrderStage, which fails.
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(orderStage);

        restOrderStageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderStageDTO)))
            .andExpect(status().isBadRequest());

        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderStageRepository.findAll().size();
        // set the field null
        orderStage.setCreatedDate(null);

        // Create the OrderStage, which fails.
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(orderStage);

        restOrderStageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderStageDTO)))
            .andExpect(status().isBadRequest());

        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderStages() throws Exception {
        // Initialize the database
        orderStageRepository.saveAndFlush(orderStage);

        // Get all the orderStageList
        restOrderStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderStage.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(sameInstant(DEFAULT_ORDER_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(sameInstant(DEFAULT_DELETED_DATE))));
    }

    @Test
    @Transactional
    void getOrderStage() throws Exception {
        // Initialize the database
        orderStageRepository.saveAndFlush(orderStage);

        // Get the orderStage
        restOrderStageMockMvc
            .perform(get(ENTITY_API_URL_ID, orderStage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderStage.getId().intValue()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO))
            .andExpect(jsonPath("$.orderDate").value(sameInstant(DEFAULT_ORDER_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)))
            .andExpect(jsonPath("$.deletedDate").value(sameInstant(DEFAULT_DELETED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingOrderStage() throws Exception {
        // Get the orderStage
        restOrderStageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderStage() throws Exception {
        // Initialize the database
        orderStageRepository.saveAndFlush(orderStage);

        int databaseSizeBeforeUpdate = orderStageRepository.findAll().size();

        // Update the orderStage
        OrderStage updatedOrderStage = orderStageRepository.findById(orderStage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderStage are not directly saved in db
        em.detach(updatedOrderStage);
        updatedOrderStage
            .orderNo(UPDATED_ORDER_NO)
            .orderDate(UPDATED_ORDER_DATE)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .deletedDate(UPDATED_DELETED_DATE);
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(updatedOrderStage);

        restOrderStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderStageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderStageDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderStage in the database
        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeUpdate);
        OrderStage testOrderStage = orderStageList.get(orderStageList.size() - 1);
        assertThat(testOrderStage.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
        assertThat(testOrderStage.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrderStage.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderStage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrderStage.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testOrderStage.getDeletedDate()).isEqualTo(UPDATED_DELETED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOrderStage() throws Exception {
        int databaseSizeBeforeUpdate = orderStageRepository.findAll().size();
        orderStage.setId(longCount.incrementAndGet());

        // Create the OrderStage
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(orderStage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderStageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStage in the database
        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderStage() throws Exception {
        int databaseSizeBeforeUpdate = orderStageRepository.findAll().size();
        orderStage.setId(longCount.incrementAndGet());

        // Create the OrderStage
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(orderStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStage in the database
        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderStage() throws Exception {
        int databaseSizeBeforeUpdate = orderStageRepository.findAll().size();
        orderStage.setId(longCount.incrementAndGet());

        // Create the OrderStage
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(orderStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderStageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderStage in the database
        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderStageWithPatch() throws Exception {
        // Initialize the database
        orderStageRepository.saveAndFlush(orderStage);

        int databaseSizeBeforeUpdate = orderStageRepository.findAll().size();

        // Update the orderStage using partial update
        OrderStage partialUpdatedOrderStage = new OrderStage();
        partialUpdatedOrderStage.setId(orderStage.getId());

        partialUpdatedOrderStage
            .orderNo(UPDATED_ORDER_NO)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .deletedDate(UPDATED_DELETED_DATE);

        restOrderStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderStage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderStage))
            )
            .andExpect(status().isOk());

        // Validate the OrderStage in the database
        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeUpdate);
        OrderStage testOrderStage = orderStageList.get(orderStageList.size() - 1);
        assertThat(testOrderStage.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
        assertThat(testOrderStage.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrderStage.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderStage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrderStage.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testOrderStage.getDeletedDate()).isEqualTo(UPDATED_DELETED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOrderStageWithPatch() throws Exception {
        // Initialize the database
        orderStageRepository.saveAndFlush(orderStage);

        int databaseSizeBeforeUpdate = orderStageRepository.findAll().size();

        // Update the orderStage using partial update
        OrderStage partialUpdatedOrderStage = new OrderStage();
        partialUpdatedOrderStage.setId(orderStage.getId());

        partialUpdatedOrderStage
            .orderNo(UPDATED_ORDER_NO)
            .orderDate(UPDATED_ORDER_DATE)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .deletedDate(UPDATED_DELETED_DATE);

        restOrderStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderStage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderStage))
            )
            .andExpect(status().isOk());

        // Validate the OrderStage in the database
        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeUpdate);
        OrderStage testOrderStage = orderStageList.get(orderStageList.size() - 1);
        assertThat(testOrderStage.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
        assertThat(testOrderStage.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrderStage.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderStage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrderStage.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testOrderStage.getDeletedDate()).isEqualTo(UPDATED_DELETED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrderStage() throws Exception {
        int databaseSizeBeforeUpdate = orderStageRepository.findAll().size();
        orderStage.setId(longCount.incrementAndGet());

        // Create the OrderStage
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(orderStage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderStageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStage in the database
        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderStage() throws Exception {
        int databaseSizeBeforeUpdate = orderStageRepository.findAll().size();
        orderStage.setId(longCount.incrementAndGet());

        // Create the OrderStage
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(orderStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderStageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStage in the database
        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderStage() throws Exception {
        int databaseSizeBeforeUpdate = orderStageRepository.findAll().size();
        orderStage.setId(longCount.incrementAndGet());

        // Create the OrderStage
        OrderStageDTO orderStageDTO = orderStageMapper.toDto(orderStage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderStageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderStage in the database
        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderStage() throws Exception {
        // Initialize the database
        orderStageRepository.saveAndFlush(orderStage);

        int databaseSizeBeforeDelete = orderStageRepository.findAll().size();

        // Delete the orderStage
        restOrderStageMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderStage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderStage> orderStageList = orderStageRepository.findAll();
        assertThat(orderStageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
