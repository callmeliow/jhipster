import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrderFood } from 'app/shared/model/order-food.model';
import { getEntities as getOrderFoods } from 'app/entities/order-food/order-food.reducer';
import { IOrderCustom } from 'app/shared/model/order-custom.model';
import { getEntity, updateEntity, createEntity, reset } from './order-custom.reducer';

export const OrderCustomUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const orderFoods = useAppSelector(state => state.orderFood.entities);
  const orderCustomEntity = useAppSelector(state => state.orderCustom.entity);
  const loading = useAppSelector(state => state.orderCustom.loading);
  const updating = useAppSelector(state => state.orderCustom.updating);
  const updateSuccess = useAppSelector(state => state.orderCustom.updateSuccess);

  const handleClose = () => {
    navigate('/order-custom');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getOrderFoods({}));
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
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }

    const entity = {
      ...orderCustomEntity,
      ...values,
      orderFood: orderFoods.find(it => it.id.toString() === values.orderFood.toString()),
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
          ...orderCustomEntity,
          orderFood: orderCustomEntity?.orderFood?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartServeApp.orderCustom.home.createOrEditLabel" data-cy="OrderCustomCreateUpdateHeading">
            <Translate contentKey="smartServeApp.orderCustom.home.createOrEditLabel">Create or edit a OrderCustom</Translate>
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
                  id="order-custom-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartServeApp.orderCustom.customizationName')}
                id="order-custom-customizationName"
                name="customizationName"
                data-cy="customizationName"
                type="text"
              />
              <ValidatedField
                label={translate('smartServeApp.orderCustom.price')}
                id="order-custom-price"
                name="price"
                data-cy="price"
                type="text"
              />
              <ValidatedField
                id="order-custom-orderFood"
                name="orderFood"
                data-cy="orderFood"
                label={translate('smartServeApp.orderCustom.orderFood')}
                type="select"
              >
                <option value="" key="0" />
                {orderFoods
                  ? orderFoods.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order-custom" replace color="info">
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

export default OrderCustomUpdate;
