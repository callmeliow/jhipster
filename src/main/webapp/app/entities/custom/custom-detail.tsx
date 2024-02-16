import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './custom.reducer';

export const CustomDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const customEntity = useAppSelector(state => state.custom.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customDetailsHeading">
          <Translate contentKey="smartServeApp.custom.detail.title">Custom</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customEntity.id}</dd>
          <dt>
            <span id="ingredientName">
              <Translate contentKey="smartServeApp.custom.ingredientName">Ingredient Name</Translate>
            </span>
          </dt>
          <dd>{customEntity.ingredientName}</dd>
          <dt>
            <span id="additionalCost">
              <Translate contentKey="smartServeApp.custom.additionalCost">Additional Cost</Translate>
            </span>
          </dt>
          <dd>{customEntity.additionalCost}</dd>
          <dt>
            <span id="imageUrl">
              <Translate contentKey="smartServeApp.custom.imageUrl">Image Url</Translate>
            </span>
          </dt>
          <dd>{customEntity.imageUrl}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="smartServeApp.custom.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>{customEntity.createdDate ? <TextFormat value={customEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedDate">
              <Translate contentKey="smartServeApp.custom.updatedDate">Updated Date</Translate>
            </span>
          </dt>
          <dd>{customEntity.updatedDate ? <TextFormat value={customEntity.updatedDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="smartServeApp.custom.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>{customEntity.deletedDate ? <TextFormat value={customEntity.deletedDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/custom" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/custom/${customEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CustomDetail;
