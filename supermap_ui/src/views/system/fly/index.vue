<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="名称" prop="flyName">
        <el-input
          v-model="queryParams.flyName"
          placeholder="请输入飞行路线名称"
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
          v-hasPermi="['system:fly:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:fly:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:fly:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:fly:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="flyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="图片路径" align="center" prop="imgPath" >
        <template slot-scope="scope">
          <img class="imgColumn" :src="getPath(scope.row.imgPath)" alt="" />
        </template>
      </el-table-column>
      <el-table-column label="飞行路线路径" align="center" prop="flyPath" />
      <el-table-column label="飞行路线名称" align="center" prop="flyName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:fly:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:fly:remove']"
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

    <!-- 添加或修改飞行路线对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="flyName">
          <el-input v-model="form.flyName" placeholder="请输入飞行路线名称" />
        </el-form-item>
        <el-form-item label="飞行路线" prop="flyPath">
          <el-upload
            name="file"
            ref="flyFile"
            class="upload-demo"
            accept="fpf,image/jpeg,image/png"
            :action= 'src'
            :data="form"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :headers="headers"
            multiple
            :limit="2"
            :on-exceed="handleExceed"
            :on-success="uploadOnSuccess"
            :on-error="uploadOnError"
            :auto-upload="true"
            :file-list="fileList"
          >
            <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="关联模型" prop="config">
          <el-select v-model="form.resourceId" placeholder="请选择模型" style="width:100%">
            <el-option
              v-for="dict in configList"
              :key="dict.resourceId"
              :label="dict.nodeName"
              :value="dict.resourceId"
            ></el-option>
          </el-select>
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
import { listFly, getFly, delFly, addFly, updateFly, exportFly } from "@/api/system/fly";
import { getToken } from '@/utils/auth';
import { resourceDelete } from '@/api/tool/utils';
import {getResourceByDataType} from "@/api/system/resource";


export default {
  name: "Fly",
  data() {
    return {
      dataType:1,
      // 倾斜摄影列表
      configList:[],
      headers:{
        'Authorization':'Bearer ' + getToken(),
      },
      //上传url
      src: process.env.VUE_APP_BASE_API + "/common/upload",
      fileList: [],
      commonFiles:[],
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
      // 飞行路线表格数据
      flyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        flyPath: undefined,
        flyName: undefined,
        imgPath:undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        flyName: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
      },
      host:""
    };
  },
  created() {
    this.getList();
    this.getConfig();
    this.getConfigKey("sys.host").then(response => {
      this.host = response.msg;
    });
  },
  methods: {
    handleFileChange(event, type){
      this.form[type] = event.target.files[0];
    },
    /** 查询飞行路线列表 */
    getList() {
      this.loading = true;
      listFly(this.queryParams).then(response => {
        this.flyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      if(this.commonFiles.length > 0){
        for(var i=0;i<this.commonFiles.length;i ++){
          resourceDelete(this.commonFiles[i]);
        }
        this.commonFiles.length = 0;
      }
      this.clearUploadedImage();
      this.open = false;
      this.reset();
    },
    // 查询倾斜摄影列表
    getConfig(){
      getResourceByDataType(this.dataType).then((res) => {
        if (res.code === 200) {
          //this.configList = this.handleTree(res.rows, "resourceId","parentId","childId",0)[0].children;
          this.configList = res.rows;
        } else {
          this.$message({
            message: "未找到数据资源",
            type: "warning",
          });
        }
      });
    },
    // 表单重置
    reset() {
      this.form = {
        id: "",
        resourceId: undefined,
        flyPath: undefined,
        flyName: undefined,
        createTime: undefined,
        updateTime: undefined,
        delFlag: ""
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
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加飞行路线";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getFly(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改飞行路线";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != "") {
            updateFly(this.form).then(response => {
              if (response.code === 200) {
                this.clearUploadedImage();
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addFly(this.form).then(response => {
              if (response.code === 200) {
                this.clearUploadedImage();
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
      this.$confirm('是否确认删除飞行路线编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delFly(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有飞行路线数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportFly(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    },
    handlePreview(file) {

    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 2 个文件，本次选择了 ${
          fileList.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
    },

    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    handleRemove(file) {
      this.path = file.response.fileName;
      if(this.path  != null && this.path  != undefined) {
        resourceDelete(this.path).then(res => {
          if (res.code = 200) {
            for (var i = 0; i < _this.commonFiles.length; i++) {
              if (_this.commonFiles[i] === _this.path) {
                _this.commonFiles.splice(i, 1)
                break;
              }
            }
          }
        })
      }
    },
    //上传成功
    uploadOnSuccess(data){
      var _this = this
      var _path = data.fileName;
      let index = data.filePath.length;
      _this.commonFiles.push(_path);
      _path = _path.substring(index);
      if(_path.endsWith(".fpf")){
        _this.form.flyPath = _path;
      }else{
        _this.form.imgPath = _path;
      }
      this.addBool = false;
      this.msgSuccess("上传成功！");
    },
    //上传失败
    uploadOnError(){
      this.addBool = false;
      this.msgError("上传失败！");
      this.getList();
    },
    beforeUpload(file){
      if(file.type.indexOf('image/') == -1){
        this.msgError('文件格式错误，请图片或者 .fpf 后缀的文件。')
      }
    },
    clearUploadedImage () {
        this.$refs["flyFile"].clearFiles();
    },
    getPath(path){
      return `${this.host}${path}`
    }
  },
};
</script>

<style scoped>
  .imgColumn{
    width: 150px;
    height: 150px;
  }
</style>
