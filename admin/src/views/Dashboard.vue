<template>
  <div class="dashboard">
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-info">
              <span class="stat-label">文章数</span>
              <span class="stat-value">{{ stats.posts }}</span>
            </div>
            <el-icon class="stat-icon" style="color: #409eff"><Document /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-info">
              <span class="stat-label">项目数</span>
              <span class="stat-value">{{ stats.projects }}</span>
            </div>
            <el-icon class="stat-icon" style="color: #67c23a"><Folder /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-info">
              <span class="stat-label">简历条目</span>
              <span class="stat-value">{{ stats.resume }}</span>
            </div>
            <el-icon class="stat-icon" style="color: #e6a23c"><User /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-info">
              <span class="stat-label">活动数</span>
              <span class="stat-value">{{ stats.activities }}</span>
            </div>
            <el-icon class="stat-icon" style="color: #f56c6c"><Timer /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="quick-card">
          <template #header>
            <span>快捷操作</span>
          </template>
          <div class="quick-actions">
            <el-button type="primary" @click="$router.push('/posts')">
              <el-icon><Document /></el-icon>
              管理文章
            </el-button>
            <el-button type="success" @click="$router.push('/projects')">
              <el-icon><Folder /></el-icon>
              管理项目
            </el-button>
            <el-button type="warning" @click="$router.push('/resume')">
              <el-icon><User /></el-icon>
              管理简历
            </el-button>
            <el-button type="danger" @click="$router.push('/activities')">
              <el-icon><Timer /></el-icon>
              管理活动
            </el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="quick-card">
          <template #header>
            <span>系统信息</span>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="系统名称">博客管理系统</el-descriptions-item>
            <el-descriptions-item label="后端地址">http://localhost:8080</el-descriptions-item>
            <el-descriptions-item label="数据库">H2 (开发环境)</el-descriptions-item>
            <el-descriptions-item label="管理员">admin</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStats } from '../api/setting'

const stats = ref({
  posts: 0,
  projects: 0,
  resume: 0,
  activities: 0
})

onMounted(async () => {
  try {
    const res = await getStats()
    stats.value = res
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  height: 120px;
}

.stat-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-label {
  color: #999;
  font-size: 14px;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #333;
}

.stat-icon {
  font-size: 48px;
  opacity: 0.3;
}

.quick-card {
  margin-bottom: 20px;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.quick-actions .el-button {
  flex: 1;
  min-width: 120px;
}
</style>
