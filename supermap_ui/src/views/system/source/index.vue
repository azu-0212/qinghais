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
          v-hasPermi="['system:source:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:source:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:source:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading"
              row-key="id"
              default-expand-all
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
              :data="sourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="节点名" align="left" prop="nodeName" />
      <el-table-column label="数据名称" align="center" prop="resource.nodeName" />
      <el-table-column label="数据类型" align="center" prop="resource.dataType">
        <template slot-scope="scope">
          <span v-if="scope.row.resource !== null">{{ layerType[scope.row.resource.dataType].val }}</span>
        </template>
      </el-table-column>
      <el-table-column label="服务地址" align="center" prop="resource.serverUrl"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="!scope.row.resource"
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
            v-hasPermi="['system:source:edit']"
          >修改</el-button>
          <el-button
            v-if="!scope.row.children||scope.row.children.length===0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:source:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改来源类型对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="节点名" prop="nodeName">
          <el-input v-model="form.nodeName" placeholder="请输入节点名" />
        </el-form-item>
        <el-form-item v-if="radioType" label="节点类型">
          <el-select v-model="form.nodeType" placeholder="请选择节点类型">
            <el-option v-for="(item,i) in nodeType" :key="i" :label="item.val" :value="item.key"/>
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.nodeType && form.nodeType!== '0' && checkboxPanel" label="关联资源" prop="resourceId">
          <el-checkbox-group v-model="form.resourceIds">
            <el-checkbox
              v-for="item in sourceListLeaf"
              :key="item.resourceId"
              :label="item.resourceId"
            >
              {{item.nodeName}}
            </el-checkbox>
          </el-checkbox-group>
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
  import { listSource, getSource, delSource, addSource, updateSource, exportSource, listSourceAll } from "@/api/system/source";
  import { listResourceByLeaf } from "@/api/system/resource";

  export default {
    name: "Source",
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
        deleteParentId:0,
        // 来源类型表格数据
        sourceList: [],
        sourceListLeaf:[],
        // 多选框面板
        checkboxPanel:true,
        // 节点类型面板
        radioType:true,
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          dataType: undefined,
          parentId: undefined,
          nodeName: undefined,
          nodeStatus: undefined,
          createDate: undefined,
          updateDate: undefined,
          nodeType: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          nodeType: [
            { required: true, message: "节点类型不能为空", trigger: "blur" }
          ]
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询来源类型列表 */
      getList() {
        this.loading = true;
        listSourceAll().then(response => {
          this.sourceList = this.handleTree(response.rows, 'id');
          this.loading = false;
        });
      },
      // switch 开关
      changeSwitch(status){
        this.status = status;
      },
      // 获取resourceId
      getRow(val){
        //console.log("status开关",val);
        this.resourceId = val.resourceId;
        this.updateStatus();
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
          // 来源类型表格数据id
          resourceIds:[],
          nodeName: undefined,
          nodeStatus: "0",
          createDate: undefined,
          updateDate: undefined,
          updateBy: undefined,
          remark: undefined,
          nodeType: undefined
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
        this.ids = selection.map(item => item.id);
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset();
        this.checkboxPanel = true;
        this.radioType = true;
        this.form.resourceIds = [];
        this.open = true;
        // 获取所有叶子数据
        listResourceByLeaf().then( res => {
          this.sourceListLeaf = res.rows;
        })
        if (row.id) {
          this.form.parentId = row.id;
          console.log('清空',row);
          if(row.children && row.children.length > 0 && !row.children[0].children && row.children[0].resource !== null){
            this.deleteParentId = row.id;
          }
        } else {
          this.form.parentId = 0;
        }
        this.form.nodeType = 0;
        this.form.nodeStatus = 1;
        this.title = "添加来源类型";
      },
      deleteLeafs(id){
        // 先清空原有的叶子数据
        listSource({ parentId: id }).then(response => {
          let idsList = response.rows;
          let ids = ',';
          idsList.forEach( (item) => {
            ids += item.id + ',';
          })
          ids = ids.substring(1,ids.length-1);
          this.ids = ids.split(",");
          delSource(ids.split(","));
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.checkboxPanel = false;
        this.radioType = false;
        // 获取所有叶子数据
        listResourceByLeaf().then( res => {
          this.sourceListLeaf = res.rows;
        })
        const id = row.id || this.ids
        getSource(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改来源类型";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateSource(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                }
              });
            } else {
              if(this.deleteParentId !== 0){
                console.log('确认删除');
                // 清空原有的叶子数据
                // this.deleteLeafs(this.deleteParentId);
              }
              //判断添加的是根节点还是叶子节点
              if(this.form.resourceIds.length > 0){
                let _this = this;
                this.form.resourceIds.map((item) => {
                  console.log('item',item,_this.form);
                  let form = {};
                  form.nodeName = _this.form.nodeName;
                  form.nodeStatus = _this.form.nodeStatus;
                  form.nodeType = _this.form.nodeType;
                  form.resourceId = item;
                  form.parentId = _this.form.parentId;
                  console.log('form',form);
                  addSource(form).then(response => {
                    if (response.code === 200) {
                      this.msgSuccess("新增成功");
                      this.open = false;
                      this.getList();
                    }
                  });
                })
              }else{
                addSource(this.form).then(response => {
                  if (response.code === 200) {
                    this.msgSuccess("新增成功");
                    this.open = false;
                    this.getList();
                  }
                });
              }

            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除来源类型编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSource(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有来源类型数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportSource(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
<style scoped>
  >>> .el-checkbox{
    /*width:20%*/
  }
  >>> .el-checkbox-group{
    border:1px solid #eee;
    padding-left:5px;
    border-radius: 4px;
    height: 400px;
    overflow-y: auto;
    overflow-x: hidden;
  }
  >>> .el-checkbox-group::-webkit-scrollbar {
    width: 4px;
  }

  /* 滚动槽 */
  >>> .el-checkbox-group::-webkit-scrollbar-track {
    -webkit-box-shadow: inset 0 0 6px rgba(175, 175, 175, 0.3);
    background: transparent;
    border-radius: 10px;
  }

  /* 滚动条滑块 */
  >>> .el-checkbox-group::-webkit-scrollbar-thumb {
    border-radius: 2px;
    background: rgba(1, 1, 20, 0.4);
    /*-webkit-box-shadow: inset 0 0 6px rgba(22, 223, 226, 0.5);*/
  }
</style>

