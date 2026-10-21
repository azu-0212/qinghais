<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="节点名" prop="nodeName">
        <el-input
          v-model="queryParams.nodeName"
          placeholder="请输入节点名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据类型" prop="dataType">
        <el-select v-model="queryParams.dataType" placeholder="请选择数据类型" clearable size="small">
          <el-option v-for="(item,i) in layerType" :key="i"  :label="item.val" :value="item.key"/>
        </el-select>
      </el-form-item>
      <el-form-item label="数据年份" prop="dataYear">
        <el-input
          v-model="queryParams.dataYear"
          placeholder="请输入数据年份"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['system:contrast:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:contrast:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading"
              row-key="id"
              default-expand-all
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
              :data="contrastList"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="节点名" align="left" prop="nodeName" />
      <el-table-column label="数据类型" align="center" prop="dataType" >
        <template slot-scope="scope">
          <span v-if="scope.row.dataType !== -1">{{ layerType[scope.row.dataType].val }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据年份" align="center" prop="dataYear" />
      <el-table-column label="服务地址" align="center" prop="serverUrl" />
      <el-table-column label="节点状态" align="center" prop="nodeStatus">
        <template slot-scope="scope">
          <el-switch
            v-if="!scope.row.children||scope.row.children.length===0"
            v-model="scope.row.nodeStatus"
            active-color="green"
            inactive-color="red"
            :active-value="1"
            :inactive-value="0"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.nodeType === 0"
            type="text"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd(scope.row)"
            style="color: green"
            v-hasPermi="['system:resource:add']"
          >新增
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:contrast:edit']"
          >修改</el-button>
          <el-button
            v-if="!scope.row.children||scope.row.children.length===0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:contrast:remove']"
            style="color: red"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改影像对比对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="节点名" prop="nodeName">
          <el-input v-model="form.nodeName" placeholder="请输入节点名" />
        </el-form-item>
        <el-form-item label="节点类型">
          <el-select v-model="form.nodeType" @change="nodeTypeChanged" placeholder="请选择节点类型">
            <el-option v-for="(item,i) in nodeType" :key="i" :label="item.val" :value="item.key"/>
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.nodeType && form.nodeType !== '0'" label="数据类型">
          <el-select  v-model="form.dataType" placeholder="请选择数据类型">
            <el-option v-for="(item,i) in layerType"  selected :key="i" :label="item.val" :value="item.key"/>
          </el-select>
        </el-form-item>
        <el-form-item label="服务地址" v-if="form.nodeType && form.nodeType!== '0'" prop="serverUrl">
          <el-input v-model="form.serverUrl" placeholder="请输入服务地址" />
        </el-form-item>
        <el-form-item label="数据年份" v-if="form.nodeType && form.nodeType!== '0'" prop="dataYear">
          <el-input v-model="form.dataYear" placeholder="请输入数据年份" />
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
  import { listContrast, getContrast, delContrast, addContrast, updateContrast, exportContrast } from "@/api/system/contrast";

  export default {
    name: "Contrast",
    data() {
      return {
        layerType: [
          {val:"影像图层"},
          {val:"倾斜摄影"},
          {val:"MVT图层"},
          {val:"全景数据"},
          {val:"WMTS影像"}
        ],
        nodeType: [
          {key:"0",val:"资源目录",},
          {key:"1",val: "资源节点",},
        ],
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
        // 影像对比表格数据
        contrastList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 数据类型字典
        dataTypeOptions: [],
        // 查询参数
        queryParams: {
          parentId: undefined,
          nodeName: undefined,
          nodeType: undefined,
          dataType: undefined,
          serverUrl: undefined,
          orderBy: undefined,
          nodeStatus: undefined,
          childNames: undefined,
          createDate: undefined,
          updateDate: undefined,
          bindArea: undefined,
          dataYear: undefined,
          dataVolume: undefined,
          dataParam: undefined,
          position: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          nodeType: [
            { required: true, message: "节点类型不能为空", trigger: "blur" }
          ],
          childNames: [
            { required: true, message: "图层名称不能为空", trigger: "blur" }
          ],
        }
      };
    },
    created() {
      this.getList();
      this.getDicts("sys_layer_type").then(response => {
        this.layerType = [];
        response.data.map((item,i) => {
          this.layerType[i] = {};
          this.layerType[i].key = item.dictValue;
          this.layerType[i].val = item.dictLabel;
        });
      });
    },
    methods: {
      /** 查询影像对比列表 */
      getList() {
        this.loading = true;
        listContrast(this.queryParams).then(response => {
          this.contrastList = this.handleTree(response.rows, 'id');
          //this.total = response.total;
          this.loading = false;
        });
      },
      // 数据类型字典翻译
      dataTypeFormat(row, column) {
        return this.selectDictLabel(this.dataTypeOptions, row.dataType);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          parentId: undefined,
          nodeName: undefined,
          nodeType: undefined,
          dataType: undefined,
          serverUrl: undefined,
          orderBy: undefined,
          nodeStatus: "0",
          childNames: undefined,
          createDate: undefined,
          updateDate: undefined,
          updateBy: undefined,
          bindArea: undefined,
          remark: undefined,
          dataYear: undefined,
          dataVolume: undefined,
          dataParam: undefined,
          position: undefined
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
        this.ids = selection.map(item => item.id)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset();
        if(row){
          this.form.parentId = row.id;
        }else {
          this.form.parentId = 0;
        }
        this.form.nodeStatus = 1;
        this.open = true;
        this.title = "添加影像对比";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getContrast(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改影像对比";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateContrast(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                }
              });
            } else {
              addContrast(this.form).then(response => {
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
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除影像对比编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delContrast(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有影像对比数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportContrast(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      },
      nodeTypeChanged() {
        if (this.form.nodeType === '0') {
          this.form.dataType = -1;
        } else {
          this.form.dataType = undefined;
        }
      }
    }
  };
</script>
