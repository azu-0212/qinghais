<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="名称" prop="imgName">
        <el-input
          v-model="queryParams.imgName"
          placeholder="请输入重点区域名称"
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
          v-hasPermi="['system:areas:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:areas:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="areasList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="图片" align="center" prop="imgPath">
        <template slot-scope="scope">
          <img class="columnImg" :src="profile.concat(scope.row.imgPath)" alt="">
        </template>
      </el-table-column>
      <el-table-column label="名称" align="center" prop="imgName" />
      <el-table-column label="坐标" align="center" prop="position" />
      <el-table-column label="服务" align="center" prop="serverUrl" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:areas:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:areas:remove']"
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

    <!-- 添加或修改重点区域定位对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="imgName">
          <el-input v-model="form.imgName" placeholder="请输入重点区域名称" />
        </el-form-item>
        <el-row>
          <el-col :span="8">
            <el-form-item label="经度(lon)" prop="lon">
              <el-input v-model="form.lon" placeholder="请输入经度" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="纬度(lat)" prop="lat">
              <el-input v-model="form.lat" placeholder="请输入纬度" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="高程(alt)" prop="alt">
              <el-input v-model="form.alt" placeholder="请输入高程" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="图片" prop="imgPath">
          <el-upload
            ref="areasImg"
            class="upload-demo"
            :action= 'src'
            :data="form"
            accept=".jpg,.png,.jpeg"
            :on-preview="handlePreview"
            :before-remove="beforeRemove"
            :headers="headers"
            multiple
            :limit="1"
            :on-exceed="handleExceed"
            :before-upload="beforeUpload"
            :on-success="uploadOnSuccess"
            :on-error="uploadOnError"
            :auto-upload="true"
            :file-list="fileList"
            >
            <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="地图服务" prop="serverUrl">
          <el-input v-model="form.serverUrl" placeholder="请输入地图服务地址" />
        </el-form-item>
        <el-form-item label="描述">
          <editor v-model="form.description" :min-height="192"/>
        </el-form-item>
        <el-form-item label="全景数据">
          <el-checkbox-group v-model="form.resourceId">
            <el-checkbox
              v-for="panorama in resourceIdOptions"
              :key="panorama.resourceId"
              :label="panorama.resourceId">
              {{panorama.nodeName}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="视频数据">
          <el-select v-model="form.videoId" placeholder="请选择视频" style="width:100%">
            <el-option
              v-for="video in videoListOptions"
              :key="video.id"
              :label="video.videoName"
              :value="video.id"
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
import { listAreas, getAreas, delAreas, addAreas, updateAreas, exportAreas } from "@/api/system/areas";
import { getToken } from '@/utils/auth';
import Editor from '@/layout/sysLayout/components/Editor';
import { getResourceByDataType } from "@/api/system/resource";
import { listVideo } from "@/api/system/video"

export default {
  name: "Areas",
  data() {
    return {
      fileList:[],
      commonFiles:[],
      headers:{
        'Authorization':'Bearer ' + getToken(),
      },
      //上传url
      src: process.env.VUE_APP_BASE_API + "/common/upload",
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      //删除文件使用
      path: "",
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 重点区域定位表格数据
      areasList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      oldPath: "",
      // 全景数据类型
      dataType:3,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        imgPath: undefined,
        imgName: undefined,
        position: undefined,
      },
      // 全景数据数组
      resourceIdOptions:[],
      // 视频啊数据数组
      videoListOptions:[],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        imgName: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        lon: [
          { required: true, message: "经度不能为空", trigger: "blur" }
        ],
        lat: [
          { required: true, message: "经度不能为空", trigger: "blur" }
        ],
        alt: [
          { required: true, message: "高程不能为空", trigger: "blur" }
        ]
      },
      profile:""
    };
  },
  components:{
    Editor
  },
  created() {
    this.profile = process.env.VUE_APP_BASE_API;
    this.getList();
  },
  methods: {
    /** 查询重点区域定位列表 */
    getList() {
      this.loading = true;
      listAreas(this.queryParams).then(response => {
        this.areasList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 查询全景列表
    getPanoramaList(){
      let dataType = this.dataType;
      getResourceByDataType(dataType).then(response => {
        this.resourceIdOptions = response.rows;
        //console.log('this.resourceIdOptions',this.resourceIdOptions);
      })
    },
    // 查询视频列表
    getVideoList(){
      listVideo().then(response=>{
        this.videoListOptions = response.rows;
        this.videoListOptions.unshift({id:0,videoName:'暂无'})
        //console.log('this.videoListOptions',this.videoListOptions);
      })
    },
    // 取消按钮
    cancel() {
      this.clearUploaded();
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: "",
        imgPath: undefined,
        imgName: undefined,
        position: undefined,
        serverUrl: undefined,
        createTime: undefined,
        updateTime: undefined,
        description: undefined,
        lat:undefined,
        lon:undefined,
        alt:undefined,
        delFlag: "",
        resourceId:[],
        videoId:undefined
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
      this.title = "添加重点区域定位";
      // 查询全景数据
      this.getPanoramaList();
    },
    clearUploaded () {
      this.$refs["areasImg"].clearFiles();
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAreas(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改重点区域定位";
        let posJson = JSON.parse(response.data.position)
        response.data.lon = parseFloat(posJson.lon)
        response.data.lat = parseFloat(posJson.lat)
        response.data.alt = parseFloat(posJson.alt)
        this.getPanoramaList();
        this.getVideoList();
        //将String数组转为Number
        if(this.form.resourceId!==null){
          let strArr = this.form.resourceId.split(",");
          let NumArr = [];
          for(let i = 0; i< strArr.length; i++){
            let value = parseInt(strArr[i]);
            NumArr.push(value);
          }
          this.form.resourceId = NumArr;
        }else {
          this.form.resourceId = [];
        }

      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.position = JSON.stringify({
            lon:this.form.lon,
            lat:this.form.lat,
            alt:this.form.alt
          })
          this.form.resourceId = this.form.resourceId.join(",");
          if (this.form.id !== "") {
            let _this = this;
            updateAreas(this.form).then(response => {
              if (response.code === 200) {
                /*console.log(_this.oldPath)
                if(_this.oldPath && _this.commonFiles.length > 0){
                  resourceDelete(_this.oldPath);
                }*/
                this.clearUploaded();
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addAreas(this.form).then(response => {
              if (response.code === 200) {
                this.clearUploaded();
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
      this.$confirm('是否确认删除重点区域定位编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          //return delAreas(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有重点区域定位数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportAreas(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    },
    // 上传预处理
    beforeUpload(file) {
      if (file.type.indexOf('image/') === -1) {
        this.msgError('文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。')
      }
    },
    handlePreview(file) {

    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${
          files.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    //上传成功
    uploadOnSuccess(data){
      this.commonFiles.push(data.fileName);
      let imgPath = data.fileName;
      let index = data.filePath.length;
      imgPath = imgPath.substring(index);
      this.form.imgPath = imgPath;
      this.oldPath = this.form.imgPath;
      this.msgSuccess("上传成功！");
    },
    //上传失败
    uploadOnError(){
      this.msgError("上传失败！");
      this.getList();
    }
  }
}
</script>
<style scoped>
  .columnImg{
    width: 150px;
    height: 150px;
  }
  >>> .el-checkbox-group{
    border:1px solid #eee;
    padding:0 30px;
    border-radius: 4px;
    height: 300px;
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
