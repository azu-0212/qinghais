<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="视频名称" prop="videoName">
        <el-input
          v-model="queryParams.videoName"
          placeholder="请输入视频名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="视频状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button v-hasPermi="['system:menu:add']" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="videoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="名称" align="center" prop="videoName" />
      <el-table-column label="简介" align="center" prop="description" />
      <el-table-column label="缩略图" align="center" prop="imgPath">
        <template slot-scope="scope">
          <a href="#" @click="openVideo(scope.row.videoSrc)" >
            <img class="columnImg" :src="profile.concat(scope.row.imgPath)" title="查看视频">
          </a>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" :formatter="videoTypeFormat"/>
      <el-table-column label="坐标" align="center" prop="position" />
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

    <!--视频弹出框-->
    <el-dialog title="查看视频" :visible.sync="dialogVideoVisible" @close="closeDialog">
      <video id="movie" v-if="videoSrc" style="width:100%;" controls="controls" :src="host.concat(videoSrc)"></video>
    </el-dialog>

    <!-- 添加或修改视频对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-form-item label="视频名称" prop="videoName">
            <el-input v-model="form.videoName" placeholder="请输入视频名称" />
          </el-form-item>
          <el-form-item label="视频简介" prop="description">
            <el-input
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4}"
              placeholder="请输入内容"
              v-model="form.description">
            </el-input>
          </el-form-item>
          <el-form-item label="视频类型" prop="videoType">
            <el-select v-model="form.videoType" placeholder="请选择节点类型">
              <el-option v-for="(item,i) in videoType" :key="i" :label="item.dictLabel" :value="item.dictValue"/>
            </el-select>
          </el-form-item>
        </el-row>
        <el-form-item label="视频路径" prop="videoSrc">
          <el-input v-model="form.videoSrc" placeholder="请输入视频名称" />
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
        <el-form-item label="缩略图" prop="imgPath">
          <el-upload
            ref="areasImg"
            class="upload-demo"
            :action= 'src'
            :data="form"
            accept=".jpg,.png,.jpeg"
            :on-preview="handlePreview"
            :before-remove="beforeRemove"
            :on-remove="handleRemove"
            :headers="headers"
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
<!--            <img width="100%" :src="dialogImageUrl" alt="">-->
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
  import {getToken} from "@/utils/auth";
  import { listVideo, getVideo, addVideo, updateVideo, delVideo, exportVideo } from "@/api/system/video";
  import {resourceDelete} from "@/api/tool/utils";

  export default {
    name: "index",
    data(){
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
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 总条数
        total:0,
        // 非单个禁用
        single: true,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 20,
          videoName: null,
          visible: null,
          videoSrc:null,
          imgPath:undefined,
          position:undefined,
          status:undefined,
          createDate: undefined,
          updateDate: undefined,
        },
        // 视频列表数据
        videoList:[],
        // 菜单状态数据字典
        statusOptions: [],
        // 视频类型
        videoType:[],
        // 预览图片地址
        dialogImageUrl: '',
        // 图片预览
        dialogVisible:false,
        // 表单参数
        form:{},
        oldPath: "",
        // 从form获取的imgPath
        formPath:"",
        // 视频弹出
        dialogVideoVisible:false,
        // 视频路径
        videoSrc:'',
        // 表单校验
        rules: {
          videoName: [
            {required: true, message: '视频名称不能为空', trigger: 'blur'},
          ],
          videoType: [
            {required: true, message: '未选择视频类型', trigger: 'blur'},
          ],
          videoSrc: [
            {required: true, message: '视频路径不能为空', trigger: 'blur'},
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
        host:""
      }
    },
    created() {
      this.getList()
      this.profile = process.env.VUE_APP_BASE_API;
      this.getDicts('sys_normal_disable').then(response => {
        this.statusOptions = response.data
      })
      this.getDicts('sys_video_type').then(response => {
        this.videoType = response.data;
      })
      this.getConfigKey("sys.host").then(response => {
        this.host = response.msg;
      });
    },
    methods:{
      /** 查询视频列表 */
      getList() {
        this.loading = true
        listVideo(this.queryParams).then(response => {
          //console.log(response.rows);
          this.videoList = response.rows;
          this.total = response.total;
          this.loading = false
        })
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 菜单状态字典翻译
      videoTypeFormat(row, column) {
        return this.selectDictLabel(this.videoType, row.videoType)
      },
      // 表单重置
      reset() {
        this.form = {
          id: "",
          videoName:null,
          videoType:null,
          description:null,
          videoSrc:null,
          createTime: undefined,
          updateTime: undefined,
          lat:undefined,
          lon:undefined,
          alt:undefined,
        };
        this.resetForm("form");
      },
      // 取消按钮
      cancel() {
        this.clearUploaded();
        this.open = false;
        this.reset();
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true
        this.title = '添加视频'
        //console.log(this.commonFiles);
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length!=1;
      },
      openVideo(url){
        this.dialogVideoVisible = true;
        this.videoSrc = url;
      },
      closeDialog(){
        this.videoSrc = '';
      },
      // 上传预处理
      beforeUpload(file) {
        if (file.type.indexOf('image/') === -1) {
          this.msgError('文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。')
        }
      },
      handleExceed(files, fileList) {
        console.log(fileList);
        this.$message.warning(
          `当前限制选择 1 个文件，本次选择了 ${
            files.length
          } 个文件，共选择了 ${files.length + fileList.length} 个文件`
        );
      },
      clearUploaded () {
        this.$refs["areasImg"].clearFiles();
      },
      handlePreview(file) {
        //console.log("handlePreview",file);
      },
      // 删除前
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${file.name}？`);
      },
      // 删除
      handleRemove(){
        resourceDelete(this.oldPath);
        return this.$confirm(`移除成功！`);
      },
      //上传成功
      uploadOnSuccess(data){
        //console.log("uploadOnSuccess",data);
        this.commonFiles.push(data.fileName);
        let imgPath = data.fileName;
        let index = data.filePath.length;
        imgPath = imgPath.substring(index);
        this.form.imgPath = imgPath;
        this.oldPath = this.form.imgPath;
        this.msgSuccess("上传成功！");
        this.dialogVisible = true
      },
      //上传失败
      uploadOnError(){
        this.msgError("上传失败！");
        this.getList();
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getVideo(id).then(response => {
          let posJson = JSON.parse(response.data.position)
          response.data.lon = parseFloat(posJson.lon)
          response.data.lat = parseFloat(posJson.lat)
          response.data.alt = parseFloat(posJson.alt)
          this.formPath = response.data.imgPath;
          this.form = response.data;
          this.open = true;
          this.title = "修改重点区域定位";
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
          return delVideo(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 提交按钮 */
      submitForm(){
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.position = JSON.stringify({
              lon:this.form.lon,
              lat:this.form.lat,
              alt:this.form.alt
            });
            if (this.form.id !== "") {
              let _this = this;
              console.log("updateVideo",this.form);
              updateVideo(this.form).then(response => {
                if (response.code === 200) {
                  /*if(_this.oldPath && _this.commonFiles.length > 0){
                    resourceDelete(_this.formPath);
                  }*/
                  this.clearUploaded();
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                }
              });
            } else {
              console.log("addVideo",this.form)
              addVideo(this.form).then(response => {
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
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有重点区域定位数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportVideo(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      },
    }
  }
</script>

<style scoped>
  .columnImg{
    width: 150px;
    height: 150px;
  }
</style>
