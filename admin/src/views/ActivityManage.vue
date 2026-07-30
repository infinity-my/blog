<template>
  <div class="activity-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>活动列表</span>
          <el-button type="primary" @click="openDialog()">
            <el-icon><Plus /></el-icon>
            新增活动
          </el-button>
        </div>
      </template>

      <el-table :data="activities" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="action" label="动作" min-width="120" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="date" label="时间" width="160" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑活动' : '新增活动'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="动作" prop="action">
          <el-input v-model="form.action" placeholder="如: 发布了项目" />
        </el-form-item>
        <el-form-item label="颜色" prop="actionColor">
          <el-input v-model="form.actionColor" placeholder="如: text-amber-600" />
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="活动标题" />
        </el-form-item>
        <el-form-item label="时间" prop="date">
          <el-input v-model="form.date" placeholder="如: 2026-06-28 18:00" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getActivities, createActivity, updateActivity, deleteActivity } from '../api/activity'

const activities = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const submitting = ref(false)
const formRef = ref(null)

const form = reactive({
  action: '',
  actionColor: '',
  title: '',
  date: '',
  sortOrder: 0
})

const rules = {
  action: [{ required: true, message: '请输入动作', trigger: 'blur' }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }]
}

onMounted(() => {
  fetchActivities()
})

const fetchActivities = async () => {
  loading.value = true
  try {
    activities.value = await getActivities()
  } catch (error) {
    ElMessage.error('获取活动列表失败')
  } finally {
    loading.value = false
  }
}

const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    editId.value = row.id
    Object.assign(form, {
      action: row.action,
      actionColor: row.actionColor,
      title: row.title,
      date: row.date,
      sortOrder: row.sortOrder
    })
  } else {
    isEdit.value = false
    editId.value = null
    Object.assign(form, {
      action: '',
      actionColor: '',
      title: '',
      date: '',
      sortOrder: 0
    })
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (isEdit.value) {
      await updateActivity(editId.value, { ...form })
      ElMessage.success('更新成功')
    } else {
      await createActivity({ ...form })
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchActivities()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除活动「${row.title}」吗？`, '提示', {
      type: 'warning'
    })
    await deleteActivity(row.id)
    ElMessage.success('删除成功')
    fetchActivities()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
