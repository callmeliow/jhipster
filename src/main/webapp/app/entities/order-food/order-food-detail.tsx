import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order-food.reducer';

export const OrderFoodDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderFoodEntity = useAppSelector(state => state.orderFood.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderFoodDetailsHeading">
          <Translate contentKey="smartServeApp.orderFood.detail.title">OrderFood</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderFoodEntity.id}</dd>
          <dt>
            <span id="foodName">
              <Translate contentKey="smartServeApp.orderFood.foodName">Food Name</Translate>
            </span>
          </dt>
          <dd>{orderFoodEntity.foodName}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="smartServeApp.orderFood.price">Price</Translate>
            </span>
          </dt>
          <dd>{orderFoodEntity.price}</dd>
          <dt>
            <span id="orderItemPrice">
              <Translate contentKey="smartServeApp.orderFood.orderItemPrice">Order Item Price</Translate>
            </span>
          </dt>
          <dd>{orderFoodEntity.orderItemPrice}</dd>
          <dt>
            <Translate contentKey="smartServeApp.orderFood.order">Order</Translate>
          </dt>
          <dd>{orderFoodEntity.order ? orderFoodEntity.order.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-food" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-food/${orderFoodEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderFoodDetail;
