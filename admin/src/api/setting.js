import request from '../utils/request'

export function getSettings() {
  return request.get('/admin/settings')
}

export function updateResumePassword(password) {
  return request.put('/admin/settings/resume-password', { password })
}

export function getStats() {
  return request.get('/admin/stats')
}

export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/admin/files/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
