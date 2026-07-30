import request from '../utils/request'

export function getActivities() {
  return request.get('/admin/activities')
}

export function getActivityById(id) {
  return request.get(`/admin/activities/${id}`)
}

export function createActivity(data) {
  return request.post('/admin/activities', data)
}

export function updateActivity(id, data) {
  return request.put(`/admin/activities/${id}`, data)
}

export function deleteActivity(id) {
  return request.delete(`/admin/activities/${id}`)
}
