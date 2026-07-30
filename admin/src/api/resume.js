import request from '../utils/request'

export function getResumeList() {
  return request.get('/admin/resume')
}

export function getResumeById(id) {
  return request.get(`/admin/resume/${id}`)
}

export function createResume(data) {
  return request.post('/admin/resume', data)
}

export function updateResume(id, data) {
  return request.put(`/admin/resume/${id}`, data)
}

export function deleteResume(id) {
  return request.delete(`/admin/resume/${id}`)
}
