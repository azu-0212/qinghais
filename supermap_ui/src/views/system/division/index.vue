<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="行政编码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="行政编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable size="small">
          <el-option label="省" value="0"/>
          <el-option label="市" value="1"/>
          <el-option label="区县" value="2"/>
          <el-option label="乡镇" value="3"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button>
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd()"
          v-hasPermi="['system:division:add']"
        >新增
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="divisionList"
      row-key="code"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="行政规划名称"  prop="name"/>
      <el-table-column label="行政规划编码"  prop="code"/>
      <el-table-column label="行政规划类型"  prop="type">
        <template slot-scope="scope">
          <span>{{['省级','市级','区县级','乡镇级'][scope.row.type]}}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="显示排序" align="center" prop="orderNum"/>-->
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:division:edit']"
          >修改
          </el-button>
          <el-button
            v-hasPermi="['system:dept:add']"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
          >新增</el-button>
          <el-button
            v-if="scope.row.parentId !== 0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:division:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改行政区划对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="550px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item v-if="form.parentId !== 0" label="上级行政区划" prop="parentId">
          <!--          <el-input v-model="form.parentId" placeholder="父id" />-->
          <treeSelect v-model="form.parentId" :options="divisionOptions" :normalizer="normalizer" placeholder="选择上级行政规划"></treeSelect>
        </el-form-item>
        <el-form-item label="行政区划名称" prop="name">
          <el-input v-model="form.name" placeholder="名称"/>
        </el-form-item>
        <el-form-item label="行政区划编码" prop="code">
          <el-input v-model="form.code" placeholder="行政编码"/>
        </el-form-item>
        <el-form-item label="行政区划类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型">
            <el-option label="省" value="0"/>
            <el-option label="市" value="1"/>
            <el-option label="区县" value="2"/>
            <el-option label="乡镇" value="3"/>
          </el-select>
        </el-form-item>
        <el-form-item label="显示排序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="显示排序"/>
        </el-form-item>
        <el-form-item label="行政区划备注" prop="delFlag">
          <el-input v-model="form.remark" placeholder="备注"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listDivision,
    getDivision,
    delDivision,
    addDivision,
    updateDivision,
    exportDivision
  } from '@/api/system/division'
  import TreeSelect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'

  export default {
    name: 'Division',
    components: { TreeSelect },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 行政区划表格数据
        divisionList: [],
        // 行政区划树选项
        divisionOptions: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          name: undefined,
          code: undefined,
          type: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          parentId: [
            { required: true, message: '上级部门不能为空', trigger: 'blur' }
          ],
          name: [
            { required: true, message: '行政区划名称不能为空', trigger: 'blur' }
          ],
          code :[
            { required: true, message: '行政区划编码不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '行政区划类型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getList()
    },
    methods: {
      /** 查询行政区划列表 */
      getList() {
        this.loading = true
        listDivision(this.queryParams).then(response => {
          this.divisionList = this.handleTree(response.data)
          this.loading = false
          // console.log('this.divisionList',this.divisionList)
        })
      },
      /** 转换行政区划数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children
        }
        return {
          id: node.id,
          label: node.name,
          children: node.children
        }
      },
      /** 查询部门下拉树结构 */
      getTreeSelect() {
        listDivision().then(res => {
          this.divisionOptions = this.handleTree(res.data)
        })
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          parentId: undefined,
          ancestors: undefined,
          name: undefined,
          code: undefined,
          type: undefined,
          orderNum: undefined,
          delFlag: undefined,
          createTime: undefined,
          remark: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset()
        if (row) {
          // console.log('row',row)
          this.form.parentId = row.id
          // this.form.type = (parseInt(row.type)+1)+''
        }
        this.getTreeSelect()
        this.open = true
        this.title = '添加行政区划'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        this.getTreeSelect()
        if (row !== undefined) {
          this.form.id = row.code
        }
        getDivision(row.id).then(response => {
          this.form = response.data
          this.open = true
          this.title = '修改行政区划'
        })
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            if (this.form.id !== undefined) {
              updateDivision(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess('修改成功')
                  this.open = false
                  this.getList()
                }
              })
            } else {
              addDivision(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess('新增成功')
                  this.open = false
                  this.getList()
                }
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        this.$confirm('是否确认删除行政区划编号为"' + row.id + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return delDivision(row.id)
        }).then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        }).catch(function() {
        })
      }
    }
  }
</script>
