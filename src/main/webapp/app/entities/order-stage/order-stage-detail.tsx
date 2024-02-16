import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order-stage.reducer';

export const OrderStageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderStageEntity = useAppSelector(state => state.orderStage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderStageDetailsHeading">
          <Translate contentKey="smartServeApp.orderStage.detail.title">OrderStage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderStageEntity.id}</dd>
          <dt>
            <span id="orderNo">
              <Translate contentKey="smartServeApp.orderStage.orderNo">Order No</Translate>
            </span>
          </dt>
          <dd>{orderStageEntity.orderNo}</dd>
          <dt>
            <span id="orderDate">
              <Translate contentKey="smartServeApp.orderStage.orderDate">Order Date</Translate>
            </span>
          </dt>
          <dd>
            {orderStageEntity.orderDate ? <TextFormat value={orderStageEntity.orderDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="smartServeApp.orderStage.status">Status</Translate>
            </span>
          </dt>
          <dd>{orderStageEntity.status}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="smartServeApp.orderStage.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {orderStageEntity.createdDate ? <TextFormat value={orderStageEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedDate">
              <Translate contentKey="smartServeApp.orderStage.updatedDate">Updated Date</Translate>
            </span>
          </dt>
          <dd>
            {orderStageEntity.updatedDate ? <TextFormat value={orderStageEntity.updatedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="smartServeApp.orderStage.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {orderStageEntity.deletedDate ? <TextFormat value={orderStageEntity.deletedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/order-stage" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-stage/${orderStageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderStageDetail;
