package com.smartserve.web.rest;

import com.smartserve.repository.OrderStageRepository;
import com.smartserve.service.OrderStageService;
import com.smartserve.service.dto.OrderStageDTO;
import com.smartserve.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.smartserve.domain.OrderStage}.
 */
@RestController
@RequestMapping("/api/order-stages")
public class OrderStageResource {

    private final Logger log = LoggerFactory.getLogger(OrderStageResource.class);

    private static final String ENTITY_NAME = "orderStage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderStageService orderStageService;

    private final OrderStageRepository orderStageRepository;

    public OrderStageResource(OrderStageService orderStageService, OrderStageRepository orderStageRepository) {
        this.orderStageService = orderStageService;
        this.orderStageRepository = orderStageRepository;
    }

    /**
     * {@code POST  /order-stages} : Create a new orderStage.
     *
     * @param orderStageDTO the orderStageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderStageDTO, or with status {@code 400 (Bad Request)} if the orderStage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderStageDTO> createOrderStage(@Valid @RequestBody OrderStageDTO orderStageDTO) throws URISyntaxException {
        log.debug("REST request to save OrderStage : {}", orderStageDTO);
        if (orderStageDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderStage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderStageDTO result = orderStageService.save(orderStageDTO);
        return ResponseEntity
            .created(new URI("/api/order-stages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-stages/:id} : Updates an existing orderStage.
     *
     * @param id the id of the orderStageDTO to save.
     * @param orderStageDTO the orderStageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderStageDTO,
     * or with status {@code 400 (Bad Request)} if the orderStageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderStageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderStageDTO> updateOrderStage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderStageDTO orderStageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrderStage : {}, {}", id, orderStageDTO);
        if (orderStageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderStageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderStageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderStageDTO result = orderStageService.update(orderStageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderStageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-stages/:id} : Partial updates given fields of an existing orderStage, field will ignore if it is null
     *
     * @param id the id of the orderStageDTO to save.
     * @param orderStageDTO the orderStageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderStageDTO,
     * or with status {@code 400 (Bad Request)} if the orderStageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderStageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderStageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderStageDTO> partialUpdateOrderStage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderStageDTO orderStageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderStage partially : {}, {}", id, orderStageDTO);
        if (orderStageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderStageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderStageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderStageDTO> result = orderStageService.partialUpdate(orderStageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderStageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-stages} : get all the orderStages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderStages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderStageDTO>> getAllOrderStages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of OrderStages");
        Page<OrderStageDTO> page = orderStageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-stages/:id} : get the "id" orderStage.
     *
     * @param id the id of the orderStageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderStageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderStageDTO> getOrderStage(@PathVariable("id") Long id) {
        log.debug("REST request to get OrderStage : {}", id);
        Optional<OrderStageDTO> orderStageDTO = orderStageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderStageDTO);
    }

    /**
     * {@code DELETE  /order-stages/:id} : delete the "id" orderStage.
     *
     * @param id the id of the orderStageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderStage(@PathVariable("id") Long id) {
        log.debug("REST request to delete OrderStage : {}", id);
        orderStageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
