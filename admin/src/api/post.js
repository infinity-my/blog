import request from '../utils/request'

export function getPosts() {
  return request.get('/admin/posts')
}

export function getPostById(id) {
  return request.get(`/admin/posts/${id}`)
}

export function createPost(data) {
  return request.post('/admin/posts', data)
}

export function updatePost(id, data) {
  return request.put(`/admin/posts/${id}`, data)
}

export function deletePost(id) {
  return request.delete(`/admin/posts/${id}`)
}
