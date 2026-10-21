<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="行政区划" prop="divisionId">
        <TreeSelect
          style="width:220px"
          v-model="queryParams.divisionId"
          :options="divisionOptions"
          :normalizer="normalizer"
          @input="handleQuery"
          placeholder="请选择行政规划"/>
      </el-form-item>
      <el-form-item label="图层名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入三维图层名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable size="small">
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:info:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:info:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:info:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:info:export']"
        >导出
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="图层名称" align="center" prop="name"/>
      <el-table-column label="行政区划" align="center" prop="divisionName"/>
      <el-table-column label="服务类型" align="center" prop="typeName"/>
      <el-table-column label="服务地址" width="400px" align="center" prop="url"/>
      <el-table-column label="数据年份" align="center" prop="dataYear"/>
      <el-table-column label="数据体量" align="center" prop="dataVolume"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:info:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:info:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改三维图层资源对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="行政区划" prop="divisionId">
              <TreeSelect v-model="form.divisionId" :options="divisionOptions" :normalizer="normalizer"
                          placeholder="请选择行政规划"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务类型">
              <el-select v-model="form.type" placeholder="请选择类型">
                <el-option
                  v-for="dict in typeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="图层名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入三维图层名称"/>
        </el-form-item>
        <el-form-item label="服务地址" prop="url">
          <el-input v-model="form.url" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据年份" prop="dataYear">
              <el-date-picker label="数据年份" v-model="form.dataYear" type="year" placeholder="选择年" value-format="yyyy">
              </el-date-picker>
            </el-form-item>
            <!--            <span class="demonstration">数据年份</span>-->

          </el-col>
          <el-col :span="12">
            <el-form-item label="数据体量(G)" prop="dataVolume">
              <el-input v-model="form.dataVolume" placeholder="请输入数据体量"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider>图层定位</el-divider>
        <el-row>
          <el-col :span="8">
            <el-form-item label="经度(lon)" prop="lon">
              <el-input v-model="form.lon" placeholder="请输入经度"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="纬度(lat)" prop="lat">
              <el-input v-model="form.lat" placeholder="请输入纬度"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="高程(alt)" prop="alt">
              <el-input v-model="form.alt" placeholder="请输入高程"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listInfo, getInfo, delInfo, addInfo, updateInfo, exportInfo } from '@/api/system/info'
  import { listDivision } from '@/api/system/division'
  import TreeSelect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'

  export default {
    name: 'Info',
    components: { TreeSelect },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 三维图层资源表格数据
        infoList: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 类型字典
        typeOptions: [],
        //行政区划
        divisionOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          type: undefined,
          roll: undefined,
          pitch: undefined,
          heading: undefined,
          postion: undefined,
          url: undefined,
          dataYear: undefined,
          dataVolume: undefined,
          divisionId: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          divisionId: [
            { required: true, message: '行政区划不能为空', trigger: 'blur' }
          ],
          name: [
            { required: true, message: '图层名称不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '服务类型不能为空', trigger: 'blur' }
          ],
          url: [
            { required: true, message: '服务地址不能为空', trigger: 'blur' }
          ],
          dataVolume: [
            { required: true, message: '数据体量不能为空', trigger: 'blur' }
          ],
          lat: [
            { required: true, message: '经度不能为空', trigger: 'blur' }
          ],
          lon: [
            { required: true, message: '纬度不能为空', trigger: 'blur' }
          ],
          alt: [
            { required: true, message: '高程不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getList()
      this.getDicts('sys_layer_type').then(response => {
        this.typeOptions = response.data
      })
      listDivision().then(res => {
        this.divisionOptions = this.handleTree(res.data, 'id')
      })
    },
    methods: {
      /** 查询三维图层资源列表 */
      getList() {
        this.loading = true
        listInfo(this.queryParams).then(response => {
          this.infoList = response.rows
          this.total = response.total
          this.loading = false
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
      // 类型字典翻译
      typeFormat(row, column) {
        return this.selectDictLabel(this.typeOptions, row.type)
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
          divisionId: undefined,
          name: undefined,
          type: undefined,
          postion: undefined,
          lat: undefined,
          lon: undefined,
          alt: undefined,
          url: undefined,
          dataYear: undefined,
          dataVolume: undefined,
          updateTime: undefined,
          createTime: undefined,
          delFlag: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.open = true
        this.title = '添加三维图层资源'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        const id = row.id || this.ids
        getInfo(id).then(response => {
          let pos = JSON.parse(response.data.postion)
          response.data.lat = parseFloat(pos.lat)
          response.data.lon = parseFloat(pos.lon)
          response.data.alt = parseFloat(pos.alt)
          this.form = response.data
          this.open = true
          this.title = '修改三维图层资源'
        })
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            this.form.postion = JSON.stringify({
              lat: this.form.lat,
              lon: this.form.lon,
              alt: this.form.alt
            })
            if (this.form.id !== undefined) {

              updateInfo(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess('修改成功')
                  this.open = false
                  this.getList()
                }
              })
            } else {
              addInfo(this.form).then(response => {
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
        const ids = row.id || this.ids
        this.$confirm('是否确认删除三维图层资源编号为"' + ids + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return delInfo(ids)
        }).then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        }).catch(function() {
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams
        this.$confirm('是否确认导出所有三维图层资源数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return exportInfo(queryParams)
        }).then(response => {
          this.download(response.msg)
        }).catch(function() {
        })
      }
    }
  }
</script>
