import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Custom from './custom';
import CustomDetail from './custom-detail';
import CustomUpdate from './custom-update';
import CustomDeleteDialog from './custom-delete-dialog';

const CustomRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Custom />} />
    <Route path="new" element={<CustomUpdate />} />
    <Route path=":id">
      <Route index element={<CustomDetail />} />
      <Route path="edit" element={<CustomUpdate />} />
      <Route path="delete" element={<CustomDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CustomRoutes;
