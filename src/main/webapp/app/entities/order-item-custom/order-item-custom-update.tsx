import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrderItem } from 'app/shared/model/order-item.model';
import { getEntities as getOrderItems } from 'app/entities/order-item/order-item.reducer';
import { ICustom } from 'app/shared/model/custom.model';
import { getEntities as getCustoms } from 'app/entities/custom/custom.reducer';
import { IOrderItemCustom } from 'app/shared/model/order-item-custom.model';
import { getEntity, updateEntity, createEntity, reset } from './order-item-custom.reducer';

export const OrderItemCustomUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const orderItems = useAppSelector(state => state.orderItem.entities);
  const customs = useAppSelector(state => state.custom.entities);
  const orderItemCustomEntity = useAppSelector(state => state.orderItemCustom.entity);
  const loading = useAppSelector(state => state.orderItemCustom.loading);
  const updating = useAppSelector(state => state.orderItemCustom.updating);
  const updateSuccess = useAppSelector(state => state.orderItemCustom.updateSuccess);

  const handleClose = () => {
    navigate('/order-item-custom');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getOrderItems({}));
    dispatch(getCustoms({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...orderItemCustomEntity,
      ...values,
      orderItem: orderItems.find(it => it.id.toString() === values.orderItem.toString()),
      custom: customs.find(it => it.id.toString() === values.custom.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...orderItemCustomEntity,
          orderItem: orderItemCustomEntity?.orderItem?.id,
          custom: orderItemCustomEntity?.custom?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartServeApp.orderItemCustom.home.createOrEditLabel" data-cy="OrderItemCustomCreateUpdateHeading">
            <Translate contentKey="smartServeApp.orderItemCustom.home.createOrEditLabel">Create or edit a OrderItemCustom</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="order-item-custom-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="order-item-custom-orderItem"
                name="orderItem"
                data-cy="orderItem"
                label={translate('smartServeApp.orderItemCustom.orderItem')}
                type="select"
              >
                <option value="" key="0" />
                {orderItems
                  ? orderItems.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-item-custom-custom"
                name="custom"
                data-cy="custom"
                label={translate('smartServeApp.orderItemCustom.custom')}
                type="select"
              >
                <option value="" key="0" />
                {customs
                  ? customs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order-item-custom" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default OrderItemCustomUpdate;
