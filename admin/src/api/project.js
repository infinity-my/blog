import request from '../utils/request'

export function getProjects() {
  return request.get('/admin/projects')
}

export function getProjectById(id) {
  return request.get(`/admin/projects/${id}`)
}

export function createProject(data) {
  return request.post('/admin/projects', data)
}

export function updateProject(id, data) {
  return request.put(`/admin/projects/${id}`, data)
}

export function deleteProject(id) {
  return request.delete(`/admin/projects/${id}`)
}
