package com.smartserve.web.rest;

import com.smartserve.repository.OrderCustomRepository;
import com.smartserve.service.OrderCustomService;
import com.smartserve.service.dto.OrderCustomDTO;
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
 * REST controller for managing {@link com.smartserve.domain.OrderCustom}.
 */
@RestController
@RequestMapping("/api/order-customs")
public class OrderCustomResource {

    private final Logger log = LoggerFactory.getLogger(OrderCustomResource.class);

    private static final String ENTITY_NAME = "orderCustom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderCustomService orderCustomService;

    private final OrderCustomRepository orderCustomRepository;

    public OrderCustomResource(OrderCustomService orderCustomService, OrderCustomRepository orderCustomRepository) {
        this.orderCustomService = orderCustomService;
        this.orderCustomRepository = orderCustomRepository;
    }

    /**
     * {@code POST  /order-customs} : Create a new orderCustom.
     *
     * @param orderCustomDTO the orderCustomDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderCustomDTO, or with status {@code 400 (Bad Request)} if the orderCustom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderCustomDTO> createOrderCustom(@RequestBody OrderCustomDTO orderCustomDTO) throws URISyntaxException {
        log.debug("REST request to save OrderCustom : {}", orderCustomDTO);
        if (orderCustomDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderCustom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderCustomDTO result = orderCustomService.save(orderCustomDTO);
        return ResponseEntity
            .created(new URI("/api/order-customs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-customs/:id} : Updates an existing orderCustom.
     *
     * @param id the id of the orderCustomDTO to save.
     * @param orderCustomDTO the orderCustomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderCustomDTO,
     * or with status {@code 400 (Bad Request)} if the orderCustomDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderCustomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderCustomDTO> updateOrderCustom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderCustomDTO orderCustomDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrderCustom : {}, {}", id, orderCustomDTO);
        if (orderCustomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderCustomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderCustomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderCustomDTO result = orderCustomService.update(orderCustomDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderCustomDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-customs/:id} : Partial updates given fields of an existing orderCustom, field will ignore if it is null
     *
     * @param id the id of the orderCustomDTO to save.
     * @param orderCustomDTO the orderCustomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderCustomDTO,
     * or with status {@code 400 (Bad Request)} if the orderCustomDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderCustomDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderCustomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderCustomDTO> partialUpdateOrderCustom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderCustomDTO orderCustomDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderCustom partially : {}, {}", id, orderCustomDTO);
        if (orderCustomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderCustomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderCustomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderCustomDTO> result = orderCustomService.partialUpdate(orderCustomDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderCustomDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-customs} : get all the orderCustoms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderCustoms in body.
     */
    @GetMapping("")
    public List<OrderCustomDTO> getAllOrderCustoms() {
        log.debug("REST request to get all OrderCustoms");
        return orderCustomService.findAll();
    }

    /**
     * {@code GET  /order-customs/:id} : get the "id" orderCustom.
     *
     * @param id the id of the orderCustomDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderCustomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderCustomDTO> getOrderCustom(@PathVariable("id") Long id) {
        log.debug("REST request to get OrderCustom : {}", id);
        Optional<OrderCustomDTO> orderCustomDTO = orderCustomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderCustomDTO);
    }

    /**
     * {@code DELETE  /order-customs/:id} : delete the "id" orderCustom.
     *
     * @param id the id of the orderCustomDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderCustom(@PathVariable("id") Long id) {
        log.debug("REST request to delete OrderCustom : {}", id);
        orderCustomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
