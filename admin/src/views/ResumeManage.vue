<template>
  <div class="resume-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>简历管理</span>
          <el-button type="primary" @click="openDialog()">
            <el-icon><Plus /></el-icon>
            新增简历条目
          </el-button>
        </div>
      </template>

      <el-table :data="resumeList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="type" label="类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTag(row.type)">{{ getTypeName(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="150" />
        <el-table-column prop="subtitle" label="副标题" min-width="150" />
        <el-table-column prop="period" label="时间段" width="150" />
        <el-table-column label="条目" width="200">
          <template #default="{ row }">
            <span v-if="row.items && row.items.length">{{ row.items.length }} 项</span>
            <span v-else>-</span>
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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑简历条目' : '新增简历条目'" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型">
            <el-option label="教育经历" value="education" />
            <el-option label="专业技能" value="skill" />
            <el-option label="项目经历" value="project" />
            <el-option label="论文发表" value="publication" />
            <el-option label="研究兴趣" value="interest" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="副标题">
          <el-input v-model="form.subtitle" placeholder="如: 学校名称、公司名称" />
        </el-form-item>
        <el-form-item label="时间段">
          <el-input v-model="form.period" placeholder="如: 2020-2024" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="详细描述" />
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="form.category" placeholder="可选分类" />
        </el-form-item>
        <el-form-item label="条目列表">
          <div class="items-list">
            <div v-for="(item, index) in form.items" :key="index" class="item-row">
              <el-input v-model="form.items[index]" placeholder="条目内容" />
              <el-button type="danger" link @click="removeItem(index)">删除</el-button>
            </div>
            <el-button type="primary" link @click="addItem">+ 添加条目</el-button>
          </div>
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
import { getResumeList, createResume, updateResume, deleteResume } from '../api/resume'

const resumeList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const submitting = ref(false)
const formRef = ref(null)

const form = reactive({
  type: '',
  title: '',
  subtitle: '',
  period: '',
  description: '',
  category: '',
  items: []
})

const rules = {
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }]
}

const typeMap = {
  education: { name: '教育经历', tag: '' },
  skill: { name: '专业技能', tag: 'success' },
  project: { name: '项目经历', tag: 'warning' },
  publication: { name: '论文发表', tag: 'danger' },
  interest: { name: '研究兴趣', tag: 'info' }
}

const getTypeName = (type) => typeMap[type]?.name || type
const getTypeTag = (type) => typeMap[type]?.tag || ''

onMounted(() => {
  fetchResumeList()
})

const fetchResumeList = async () => {
  loading.value = true
  try {
    resumeList.value = await getResumeList()
  } catch (error) {
    ElMessage.error('获取简历列表失败')
  } finally {
    loading.value = false
  }
}

const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    editId.value = row.id
    Object.assign(form, {
      type: row.type,
      title: row.title,
      subtitle: row.subtitle,
      period: row.period,
      description: row.description,
      category: row.category,
      items: [...(row.items || [])]
    })
  } else {
    isEdit.value = false
    editId.value = null
    Object.assign(form, {
      type: '',
      title: '',
      subtitle: '',
      period: '',
      description: '',
      category: '',
      items: []
    })
  }
  dialogVisible.value = true
}

const addItem = () => {
  form.items.push('')
}

const removeItem = (index) => {
  form.items.splice(index, 1)
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const data = { ...form }
    // 过滤空条目
    data.items = data.items.filter(item => item.trim())
    
    if (isEdit.value) {
      await updateResume(editId.value, data)
      ElMessage.success('更新成功')
    } else {
      await createResume(data)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchResumeList()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除简历条目「${row.title}」吗？`, '提示', {
      type: 'warning'
    })
    await deleteResume(row.id)
    ElMessage.success('删除成功')
    fetchResumeList()
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

.items-list {
  width: 100%;
}

.item-row {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
  align-items: center;
}

.item-row .el-input {
  flex: 1;
}
</style>
