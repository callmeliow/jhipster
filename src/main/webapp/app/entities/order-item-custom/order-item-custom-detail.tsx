import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order-item-custom.reducer';

export const OrderItemCustomDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderItemCustomEntity = useAppSelector(state => state.orderItemCustom.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderItemCustomDetailsHeading">
          <Translate contentKey="smartServeApp.orderItemCustom.detail.title">OrderItemCustom</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderItemCustomEntity.id}</dd>
          <dt>
            <Translate contentKey="smartServeApp.orderItemCustom.orderItem">Order Item</Translate>
          </dt>
          <dd>{orderItemCustomEntity.orderItem ? orderItemCustomEntity.orderItem.id : ''}</dd>
          <dt>
            <Translate contentKey="smartServeApp.orderItemCustom.custom">Custom</Translate>
          </dt>
          <dd>{orderItemCustomEntity.custom ? orderItemCustomEntity.custom.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-item-custom" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-item-custom/${orderItemCustomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderItemCustomDetail;
