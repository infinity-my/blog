<template>
  <div class="post-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>文章列表</span>
          <el-button type="primary" @click="openDialog()">
            <el-icon><Plus /></el-icon>
            新增文章
          </el-button>
        </div>
      </template>

      <el-table :data="posts" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="slug" label="Slug" width="150" />
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column label="标签" width="200">
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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑文章' : '新增文章'" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="Slug" prop="slug">
          <el-input v-model="form.slug" placeholder="URL友好的标识符" />
        </el-form-item>
        <el-form-item label="日期" prop="date">
          <el-input v-model="form.date" placeholder="如: 2026-06-28" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="文章描述" />
        </el-form-item>
        <el-form-item label="封面" prop="cover">
          <el-input v-model="form.cover" placeholder="封面图路径" />
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
import { getPosts, createPost, updatePost, deletePost } from '../api/post'

const posts = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const submitting = ref(false)
const formRef = ref(null)
const newTag = ref('')

const form = reactive({
  title: '',
  slug: '',
  date: '',
  description: '',
  cover: '',
  tags: []
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  slug: [{ required: true, message: '请输入Slug', trigger: 'blur' }]
}

onMounted(() => {
  fetchPosts()
})

const fetchPosts = async () => {
  loading.value = true
  try {
    posts.value = await getPosts()
  } catch (error) {
    ElMessage.error('获取文章列表失败')
  } finally {
    loading.value = false
  }
}

const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    editId.value = row.id
    Object.assign(form, {
      title: row.title,
      slug: row.slug,
      date: row.date,
      description: row.description,
      cover: row.cover,
      tags: [...(row.tags || [])]
    })
  } else {
    isEdit.value = false
    editId.value = null
    Object.assign(form, {
      title: '',
      slug: '',
      date: '',
      description: '',
      cover: '',
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
      await updatePost(editId.value, { ...form })
      ElMessage.success('更新成功')
    } else {
      await createPost({ ...form })
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchPosts()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除文章「${row.title}」吗？`, '提示', {
      type: 'warning'
    })
    await deletePost(row.id)
    ElMessage.success('删除成功')
    fetchPosts()
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
