package com.smartserve.web.rest;

import com.smartserve.repository.OrderItemCustomRepository;
import com.smartserve.service.OrderItemCustomService;
import com.smartserve.service.dto.OrderItemCustomDTO;
import com.smartserve.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.smartserve.domain.OrderItemCustom}.
 */
@RestController
@RequestMapping("/api/order-item-customs")
public class OrderItemCustomResource {

    private final Logger log = LoggerFactory.getLogger(OrderItemCustomResource.class);

    private static final String ENTITY_NAME = "orderItemCustom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderItemCustomService orderItemCustomService;

    private final OrderItemCustomRepository orderItemCustomRepository;

    public OrderItemCustomResource(OrderItemCustomService orderItemCustomService, OrderItemCustomRepository orderItemCustomRepository) {
        this.orderItemCustomService = orderItemCustomService;
        this.orderItemCustomRepository = orderItemCustomRepository;
    }

    /**
     * {@code POST  /order-item-customs} : Create a new orderItemCustom.
     *
     * @param orderItemCustomDTO the orderItemCustomDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderItemCustomDTO, or with status {@code 400 (Bad Request)} if the orderItemCustom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderItemCustomDTO> createOrderItemCustom(@RequestBody OrderItemCustomDTO orderItemCustomDTO)
        throws URISyntaxException {
        log.debug("REST request to save OrderItemCustom : {}", orderItemCustomDTO);
        if (orderItemCustomDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderItemCustom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderItemCustomDTO result = orderItemCustomService.save(orderItemCustomDTO);
        return ResponseEntity
            .created(new URI("/api/order-item-customs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-item-customs/:id} : Updates an existing orderItemCustom.
     *
     * @param id the id of the orderItemCustomDTO to save.
     * @param orderItemCustomDTO the orderItemCustomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderItemCustomDTO,
     * or with status {@code 400 (Bad Request)} if the orderItemCustomDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderItemCustomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemCustomDTO> updateOrderItemCustom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderItemCustomDTO orderItemCustomDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrderItemCustom : {}, {}", id, orderItemCustomDTO);
        if (orderItemCustomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemCustomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderItemCustomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderItemCustomDTO result = orderItemCustomService.update(orderItemCustomDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderItemCustomDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-item-customs/:id} : Partial updates given fields of an existing orderItemCustom, field will ignore if it is null
     *
     * @param id the id of the orderItemCustomDTO to save.
     * @param orderItemCustomDTO the orderItemCustomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderItemCustomDTO,
     * or with status {@code 400 (Bad Request)} if the orderItemCustomDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderItemCustomDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderItemCustomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderItemCustomDTO> partialUpdateOrderItemCustom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderItemCustomDTO orderItemCustomDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderItemCustom partially : {}, {}", id, orderItemCustomDTO);
        if (orderItemCustomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemCustomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderItemCustomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderItemCustomDTO> result = orderItemCustomService.partialUpdate(orderItemCustomDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderItemCustomDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-item-customs} : get all the orderItemCustoms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderItemCustoms in body.
     */
    @GetMapping("")
    public List<OrderItemCustomDTO> getAllOrderItemCustoms() {
        log.debug("REST request to get all OrderItemCustoms");
        return orderItemCustomService.findAll();
    }

    /**
     * {@code GET  /order-item-customs/:id} : get the "id" orderItemCustom.
     *
     * @param id the id of the orderItemCustomDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderItemCustomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemCustomDTO> getOrderItemCustom(@PathVariable("id") Long id) {
        log.debug("REST request to get OrderItemCustom : {}", id);
        Optional<OrderItemCustomDTO> orderItemCustomDTO = orderItemCustomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderItemCustomDTO);
    }

    /**
     * {@code DELETE  /order-item-customs/:id} : delete the "id" orderItemCustom.
     *
     * @param id the id of the orderItemCustomDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItemCustom(@PathVariable("id") Long id) {
        log.debug("REST request to delete OrderItemCustom : {}", id);
        orderItemCustomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
