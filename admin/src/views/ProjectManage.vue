<template>
  <div class="project-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>项目列表</span>
          <el-button type="primary" @click="openDialog()">
            <el-icon><Plus /></el-icon>
            新增项目
          </el-button>
        </div>
      </template>

      <el-table :data="projects" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="emoji" label="图标" width="80" />
        <el-table-column prop="title" label="项目名称" min-width="200" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column label="标签" width="250">
          <template #default="{ row }">
            <el-tag v-for="tag in row.tags" :key="tag" size="small" class="tag-item">
              {{ tag }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑项目' : '新增项目'" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="图标(Emoji)" prop="emoji">
          <el-input v-model="form.emoji" placeholder="如: ☕" />
        </el-form-item>
        <el-form-item label="项目名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="项目描述" />
        </el-form-item>
        <el-form-item label="链接" prop="href">
          <el-input v-model="form.href" placeholder="GitHub或外部链接" />
        </el-form-item>
        <el-form-item label="标题颜色" prop="titleColor">
          <el-input v-model="form.titleColor" placeholder="如: text-amber-700" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="标签">
          <el-tag v-for="(tag, index) in form.tags" :key="index" closable @close="removeTag(index)">
            {{ tag }}
          </el-tag>
          <el-input v-model="newTag" size="small" style="width: 120px; margin-left: 8px" @keyup.enter="addTag" placeholder="添加标签" />
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
import { getProjects, createProject, updateProject, deleteProject } from '../api/project'

const projects = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const submitting = ref(false)
const formRef = ref(null)
const newTag = ref('')

const form = reactive({
  emoji: '',
  title: '',
  description: '',
  href: '',
  titleColor: '',
  sortOrder: 0,
  tags: []
})

const rules = {
  title: [{ required: true, message: '请输入项目名称', trigger: 'blur' }]
}

onMounted(() => {
  fetchProjects()
})

const fetchProjects = async () => {
  loading.value = true
  try {
    projects.value = await getProjects()
  } catch (error) {
    ElMessage.error('获取项目列表失败')
  } finally {
    loading.value = false
  }
}

const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    editId.value = row.id
    Object.assign(form, {
      emoji: row.emoji,
      title: row.title,
      description: row.description,
      href: row.href,
      titleColor: row.titleColor,
      sortOrder: row.sortOrder,
      tags: [...(row.tags || [])]
    })
  } else {
    isEdit.value = false
    editId.value = null
    Object.assign(form, {
      emoji: '',
      title: '',
      description: '',
      href: '',
      titleColor: '',
      sortOrder: 0,
      tags: []
    })
  }
  dialogVisible.value = true
}

const addTag = () => {
  if (newTag.value && !form.tags.includes(newTag.value)) {
    form.tags.push(newTag.value)
    newTag.value = ''
  }
}

const removeTag = (index) => {
  form.tags.splice(index, 1)
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (isEdit.value) {
      await updateProject(editId.value, { ...form })
      ElMessage.success('更新成功')
    } else {
      await createProject({ ...form })
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchProjects()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除项目「${row.title}」吗？`, '提示', {
      type: 'warning'
    })
    await deleteProject(row.id)
    ElMessage.success('删除成功')
    fetchProjects()
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

.tag-item {
  margin-right: 4px;
  margin-bottom: 4px;
}
</style>
