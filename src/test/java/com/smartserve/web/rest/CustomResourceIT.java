package com.smartserve.web.rest;

import static com.smartserve.web.rest.TestUtil.sameInstant;
import static com.smartserve.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.smartserve.IntegrationTest;
import com.smartserve.domain.Custom;
import com.smartserve.repository.CustomRepository;
import com.smartserve.service.dto.CustomDTO;
import com.smartserve.service.mapper.CustomMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CustomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomResourceIT {

    private static final String DEFAULT_INGREDIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENT_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ADDITIONAL_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_ADDITIONAL_COST = new BigDecimal(2);

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DELETED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DELETED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/customs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomRepository customRepository;

    @Autowired
    private CustomMapper customMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomMockMvc;

    private Custom custom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Custom createEntity(EntityManager em) {
        Custom custom = new Custom()
            .ingredientName(DEFAULT_INGREDIENT_NAME)
            .additionalCost(DEFAULT_ADDITIONAL_COST)
            .imageUrl(DEFAULT_IMAGE_URL)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE)
            .deletedDate(DEFAULT_DELETED_DATE);
        return custom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Custom createUpdatedEntity(EntityManager em) {
        Custom custom = new Custom()
            .ingredientName(UPDATED_INGREDIENT_NAME)
            .additionalCost(UPDATED_ADDITIONAL_COST)
            .imageUrl(UPDATED_IMAGE_URL)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .deletedDate(UPDATED_DELETED_DATE);
        return custom;
    }

    @BeforeEach
    public void initTest() {
        custom = createEntity(em);
    }

    @Test
    @Transactional
    void createCustom() throws Exception {
        int databaseSizeBeforeCreate = customRepository.findAll().size();
        // Create the Custom
        CustomDTO customDTO = customMapper.toDto(custom);
        restCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customDTO)))
            .andExpect(status().isCreated());

        // Validate the Custom in the database
        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeCreate + 1);
        Custom testCustom = customList.get(customList.size() - 1);
        assertThat(testCustom.getIngredientName()).isEqualTo(DEFAULT_INGREDIENT_NAME);
        assertThat(testCustom.getAdditionalCost()).isEqualByComparingTo(DEFAULT_ADDITIONAL_COST);
        assertThat(testCustom.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testCustom.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCustom.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testCustom.getDeletedDate()).isEqualTo(DEFAULT_DELETED_DATE);
    }

    @Test
    @Transactional
    void createCustomWithExistingId() throws Exception {
        // Create the Custom with an existing ID
        custom.setId(1L);
        CustomDTO customDTO = customMapper.toDto(custom);

        int databaseSizeBeforeCreate = customRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Custom in the database
        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIngredientNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customRepository.findAll().size();
        // set the field null
        custom.setIngredientName(null);

        // Create the Custom, which fails.
        CustomDTO customDTO = customMapper.toDto(custom);

        restCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customDTO)))
            .andExpect(status().isBadRequest());

        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImageUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = customRepository.findAll().size();
        // set the field null
        custom.setImageUrl(null);

        // Create the Custom, which fails.
        CustomDTO customDTO = customMapper.toDto(custom);

        restCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customDTO)))
            .andExpect(status().isBadRequest());

        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customRepository.findAll().size();
        // set the field null
        custom.setCreatedDate(null);

        // Create the Custom, which fails.
        CustomDTO customDTO = customMapper.toDto(custom);

        restCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customDTO)))
            .andExpect(status().isBadRequest());

        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustoms() throws Exception {
        // Initialize the database
        customRepository.saveAndFlush(custom);

        // Get all the customList
        restCustomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(custom.getId().intValue())))
            .andExpect(jsonPath("$.[*].ingredientName").value(hasItem(DEFAULT_INGREDIENT_NAME)))
            .andExpect(jsonPath("$.[*].additionalCost").value(hasItem(sameNumber(DEFAULT_ADDITIONAL_COST))))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(sameInstant(DEFAULT_DELETED_DATE))));
    }

    @Test
    @Transactional
    void getCustom() throws Exception {
        // Initialize the database
        customRepository.saveAndFlush(custom);

        // Get the custom
        restCustomMockMvc
            .perform(get(ENTITY_API_URL_ID, custom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(custom.getId().intValue()))
            .andExpect(jsonPath("$.ingredientName").value(DEFAULT_INGREDIENT_NAME))
            .andExpect(jsonPath("$.additionalCost").value(sameNumber(DEFAULT_ADDITIONAL_COST)))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)))
            .andExpect(jsonPath("$.deletedDate").value(sameInstant(DEFAULT_DELETED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingCustom() throws Exception {
        // Get the custom
        restCustomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustom() throws Exception {
        // Initialize the database
        customRepository.saveAndFlush(custom);

        int databaseSizeBeforeUpdate = customRepository.findAll().size();

        // Update the custom
        Custom updatedCustom = customRepository.findById(custom.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustom are not directly saved in db
        em.detach(updatedCustom);
        updatedCustom
            .ingredientName(UPDATED_INGREDIENT_NAME)
            .additionalCost(UPDATED_ADDITIONAL_COST)
            .imageUrl(UPDATED_IMAGE_URL)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .deletedDate(UPDATED_DELETED_DATE);
        CustomDTO customDTO = customMapper.toDto(updatedCustom);

        restCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customDTO))
            )
            .andExpect(status().isOk());

        // Validate the Custom in the database
        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeUpdate);
        Custom testCustom = customList.get(customList.size() - 1);
        assertThat(testCustom.getIngredientName()).isEqualTo(UPDATED_INGREDIENT_NAME);
        assertThat(testCustom.getAdditionalCost()).isEqualByComparingTo(UPDATED_ADDITIONAL_COST);
        assertThat(testCustom.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testCustom.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCustom.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testCustom.getDeletedDate()).isEqualTo(UPDATED_DELETED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCustom() throws Exception {
        int databaseSizeBeforeUpdate = customRepository.findAll().size();
        custom.setId(longCount.incrementAndGet());

        // Create the Custom
        CustomDTO customDTO = customMapper.toDto(custom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Custom in the database
        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustom() throws Exception {
        int databaseSizeBeforeUpdate = customRepository.findAll().size();
        custom.setId(longCount.incrementAndGet());

        // Create the Custom
        CustomDTO customDTO = customMapper.toDto(custom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Custom in the database
        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustom() throws Exception {
        int databaseSizeBeforeUpdate = customRepository.findAll().size();
        custom.setId(longCount.incrementAndGet());

        // Create the Custom
        CustomDTO customDTO = customMapper.toDto(custom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Custom in the database
        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomWithPatch() throws Exception {
        // Initialize the database
        customRepository.saveAndFlush(custom);

        int databaseSizeBeforeUpdate = customRepository.findAll().size();

        // Update the custom using partial update
        Custom partialUpdatedCustom = new Custom();
        partialUpdatedCustom.setId(custom.getId());

        partialUpdatedCustom
            .ingredientName(UPDATED_INGREDIENT_NAME)
            .additionalCost(UPDATED_ADDITIONAL_COST)
            .createdDate(UPDATED_CREATED_DATE);

        restCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustom))
            )
            .andExpect(status().isOk());

        // Validate the Custom in the database
        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeUpdate);
        Custom testCustom = customList.get(customList.size() - 1);
        assertThat(testCustom.getIngredientName()).isEqualTo(UPDATED_INGREDIENT_NAME);
        assertThat(testCustom.getAdditionalCost()).isEqualByComparingTo(UPDATED_ADDITIONAL_COST);
        assertThat(testCustom.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testCustom.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCustom.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testCustom.getDeletedDate()).isEqualTo(DEFAULT_DELETED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCustomWithPatch() throws Exception {
        // Initialize the database
        customRepository.saveAndFlush(custom);

        int databaseSizeBeforeUpdate = customRepository.findAll().size();

        // Update the custom using partial update
        Custom partialUpdatedCustom = new Custom();
        partialUpdatedCustom.setId(custom.getId());

        partialUpdatedCustom
            .ingredientName(UPDATED_INGREDIENT_NAME)
            .additionalCost(UPDATED_ADDITIONAL_COST)
            .imageUrl(UPDATED_IMAGE_URL)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .deletedDate(UPDATED_DELETED_DATE);

        restCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustom))
            )
            .andExpect(status().isOk());

        // Validate the Custom in the database
        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeUpdate);
        Custom testCustom = customList.get(customList.size() - 1);
        assertThat(testCustom.getIngredientName()).isEqualTo(UPDATED_INGREDIENT_NAME);
        assertThat(testCustom.getAdditionalCost()).isEqualByComparingTo(UPDATED_ADDITIONAL_COST);
        assertThat(testCustom.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testCustom.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCustom.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testCustom.getDeletedDate()).isEqualTo(UPDATED_DELETED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCustom() throws Exception {
        int databaseSizeBeforeUpdate = customRepository.findAll().size();
        custom.setId(longCount.incrementAndGet());

        // Create the Custom
        CustomDTO customDTO = customMapper.toDto(custom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Custom in the database
        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustom() throws Exception {
        int databaseSizeBeforeUpdate = customRepository.findAll().size();
        custom.setId(longCount.incrementAndGet());

        // Create the Custom
        CustomDTO customDTO = customMapper.toDto(custom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Custom in the database
        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustom() throws Exception {
        int databaseSizeBeforeUpdate = customRepository.findAll().size();
        custom.setId(longCount.incrementAndGet());

        // Create the Custom
        CustomDTO customDTO = customMapper.toDto(custom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Custom in the database
        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustom() throws Exception {
        // Initialize the database
        customRepository.saveAndFlush(custom);

        int databaseSizeBeforeDelete = customRepository.findAll().size();

        // Delete the custom
        restCustomMockMvc
            .perform(delete(ENTITY_API_URL_ID, custom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Custom> customList = customRepository.findAll();
        assertThat(customList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
