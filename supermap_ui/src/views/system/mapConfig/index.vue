<template>
  <div class="app-container">
    <div class="leftContainer" :style="{'width':open? '50%':'100%'}">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['system:mapConfig:add']"
          >新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['system:mapConfig:export']"
          >导出</el-button>
        </el-col>
      </el-row>

      <el-table v-loading="loading" :data="mapConfigList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="名称" align="center" prop="configName" />
        <el-table-column label="备注" align="center" prop="configRemark" />
        <el-table-column label="修改时间" align="center" prop="configUptime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.configUptime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:mapConfig:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:mapConfig:remove']"
            >删除</el-button>
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
    </div>
    <transition name="el-fade-in-linear">
      <div v-show="open" class="jEditorDivContainer">
      <el-form style="width: 95%" ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="配置名称" prop="configName">
          <el-input v-model="form.configName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="备注" prop="configRemark">
          <el-input v-model="form.configRemark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div id="jEditorDiv" style="width: 100%;height:60%"></div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </div>
    </transition>
  </div>
</template>

<script>
import { listMapConfig, getMapConfig, delMapConfig, addMapConfig, updateMapConfig, exportMapConfig } from "@/api/system/mapConfig";

import vueJsonEditor from 'vue-json-editor'

export default {
  name: "MapConfig",
  components:{vueJsonEditor},
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
      // 地图配置表格数据
      mapConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        configName: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        configName: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        configJson: [
          { required: true, message: "json字符串不能为空", trigger: "blur" }
        ],
      },
      editorOptions: {
        mode: 'code',
        modes: ['code','tree'],
        onError: function(err) {
          alert(err.toString())
        }
      },
      codeEditor:{},
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    let container = document.getElementById('jEditorDiv')
    this.codeEditor = new this.$jsoneditor(container, this.editorOptions)
    // this.codeEditor.set(JSON.parse("[{\"a\":\"abc\"},{\"a\":[\"abc\",\"def\"]}]"))
  },
  methods: {
    /** 查询地图配置列表 */
    getList() {
      this.loading = true;
      listMapConfig(this.queryParams).then(response => {
        this.mapConfigList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        configId: undefined,
        configName: "",
        configJson: "",
        configCode: "",
        configRemark: "",
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.configId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加地图配置";
      this.codeEditor.set({})
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const configId = row.configId || this.ids
      getMapConfig(configId).then(response => {
        this.form = response.data;
        this.codeEditor.set(JSON.parse(response.data.configJson))
        this.open = true;
        this.title = "修改地图配置";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.configJson = JSON.stringify(this.codeEditor.get());
          if (this.form.configId !== undefined) {
            updateMapConfig(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addMapConfig(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const configIds = row.configId || this.ids;
      this.$confirm('是否确认删除地图配置编号为"' + configIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delMapConfig(configIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有地图配置数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportMapConfig(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>
<style scoped>
  .jEditorDivContainer{
    position: absolute;
    top: 100px;
    right: 0;
    width: 48%;
    height: 100%;
  }
  .dialog-footer{
    text-align: center;
    /*margin-top: 30px;*/
  }
</style>
