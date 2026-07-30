<template>
  <div class="settings">
    <el-card>
      <template #header>
        <span>系统设置</span>
      </template>

      <el-form :model="settings" label-width="120px">
        <el-divider content-position="left">密码管理</el-divider>
        
        <el-form-item label="简历访问密码">
          <el-input
            v-model="settings.resumePassword"
            type="password"
            show-password
            placeholder="请输入新的密码"
            style="width: 300px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updatePassword" :loading="updating">
            保存密码
          </el-button>
        </el-form-item>

        <el-divider content-position="left">文件上传</el-divider>
        
        <el-form-item label="上传文件">
          <el-upload
            ref="uploadRef"
            :action="'/api/admin/files/upload'"
            :headers="uploadHeaders"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :limit="1"
          >
            <el-button type="primary">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip">
                支持 PDF、图片等文件，单个文件不超过 10MB
              </div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item label="上传结果" v-if="uploadedFile">
          <el-tag type="success">上传成功</el-tag>
          <span class="upload-url">{{ uploadedFile.url }}</span>
          <el-button type="primary" link @click="copyUrl">复制链接</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getSettings, updateResumePassword } from '../api/setting'

const settings = reactive({
  resumePassword: ''
})

const updating = ref(false)
const uploadedFile = ref(null)
const uploadRef = ref(null)

const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${localStorage.getItem('token')}`
}))

onMounted(async () => {
  try {
    const res = await getSettings()
    settings.resumePassword = res.resume_password || ''
  } catch (error) {
    console.error('获取设置失败', error)
  }
})

const updatePassword = async () => {
  if (!settings.resumePassword) {
    ElMessage.warning('请输入密码')
    return
  }
  
  updating.value = true
  try {
    await updateResumePassword(settings.resumePassword)
    ElMessage.success('密码更新成功')
  } catch (error) {
    ElMessage.error('密码更新失败')
  } finally {
    updating.value = false
  }
}

const beforeUpload = (file) => {
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  }
  return true
}

const handleUploadSuccess = (response) => {
  uploadedFile.value = response
  ElMessage.success('文件上传成功')
}

const handleUploadError = () => {
  ElMessage.error('文件上传失败')
}

const copyUrl = () => {
  if (uploadedFile.value?.url) {
    const fullUrl = `${window.location.origin}${uploadedFile.value.url}`
    navigator.clipboard.writeText(fullUrl)
    ElMessage.success('链接已复制')
  }
}
</script>

<style scoped>
.upload-url {
  margin: 0 12px;
  color: #666;
  font-size: 14px;
}
</style>
