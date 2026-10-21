<template>
  <div class="app-container">
    <div class="addFilePath">
      <el-form ref="formFile" :model="formFile" :rules="rules" label-width="200px">
        <el-form-item label="上传类型" prop="uploadType">
          <el-select  v-model="formFile.uploadType" @change="nodeTypeChanged" placeholder="请选择图片上传类型">
            <el-option v-for="(item,i) in uploadType"  selected :key="i" :label="item.val" :value="item.index" style="width: 200px;"/>
          </el-select>
        </el-form-item>
        <el-form-item label="数据文件上传路径" prop="fileFolder" v-show="formFile.uploadType && formFile.uploadType === 1 ">
          <el-input v-model.trim="formFile.fileFolder" placeholder="请输入图片所在路径，示例如：F://projects/" />
        </el-form-item>
        <el-form-item label="数据文件夹上传路径" prop="fileFolder" v-show="formFile.uploadType && formFile.uploadType === 2">
          <el-input v-model.trim="formFile.fileFolder" placeholder="请输入图片所在路径，示例如：F://projects/" />
        </el-form-item>
        <!--        <el-form-item label="图片类型" prop="imgType">
                  <TreeSelect
                    style="width:220px"
                    v-model.trim="formFile.imgType"
                    :options="typeOptions"
                    :normalizer="normalizer"
                    @input="getInput"
                    placeholder="请选择类型图片类型"/>
                </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { listResourceByTree, getResource } from "@/api/system/resource";
import { saveImgs,saveFileImgs } from "@/api/system/image";
import TreeSelect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  name: "image",
  data(){
    return{
      typeCode:9,
      // 类型字典
      typeOptions: [],
      // 照片类型
      imgType:undefined,
      // 照片类型资源信息
      parentResourceInfo:{},
      uploadType:[
        {index:1,val:"图片文件上传"},
        {index:2,val:"图片文件夹上传"},
      ],
      // 表单参数
      formFile:{},
      rules:{
        fileFolder:[{required: true, message: '文件路径不能为空', trigger: "blur"}],
        uploadType:[{required: true, message: '文件上传类型不能为空', trigger: "blur"}]
      }
    }
  },
  components:{
    TreeSelect
  },
  created() {
    this.getTypeList();
  },
  methods: {
    // 获取图片类型列表
    getTypeList(){
      let _this = this;
      listResourceByTree().then((res) => {
        if (res.code === 200) {
          this.typeOptions = this.handleTreeRoot(res.rows, "resourceId",'parentId','children',0);
          // 过滤图片数据
          this.typeOptions = this.typeOptions[0].children.filter((item)=>{
            return item.parentCode === _this.typeCode;
          });
        } else {
          this.$message({
            message: "未找到数据资源",
            type: "warning",
          });
        }
      });
    },
    /** 转换行政区划数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.resourceId,
        label: node.nodeName,
        children: node.children
      }
    },
    nodeTypeChanged(val){

    },
    // 表单重置
    reset() {
      this.formFile = {
        fileFolder:null,
        uploadType:null
      };
      this.resetForm("formFile");
      this.getTypeList();
    },
    cancel(){
      this.reset();
    },
    submitForm(){
      let loading;
      let _this = this;
      this.$refs['formFile'].validate(valid => {
        if (valid) {
          /**提交照片信息，开始上传*/
          // 加载全屏loading
          loading = this.$loading({
            lock: true,
            text: '图片正在存储中，请稍等...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });

          // 批量存储文件夹下的所有图片
          if(this.formFile.uploadType === 2){
            saveFileImgs(this.formFile).then(res =>{
              if(res === "ok"){
                console.log('数据保存完毕！！！！！！！！！！！！！');
                _this.formFile.fileFolder = undefined;
                this.$message({
                  message: "图片保存成功！",
                  type: "success",
                });
                // 执行完毕关闭loading
                loading.close();
              }else{
                this.$message({
                  message: res,
                  type: "warning",
                });
                setTimeout(function(){
                  loading.close();
                },2000)
              }
            })
            return;
          }

          // 批量存储文件下的所有图片
          saveImgs(this.formFile).then(res =>{
            if(res === "ok"){
              console.log('数据保存完毕！！！！！！！！！！！！！');
              _this.formFile.fileFolder = undefined;
              this.$message({
                message: "图片保存成功！",
                type: "success",
              });
              // 执行完毕关闭loading
              loading.close();
            }else{
              this.$message({
                message: res,
                type: "warning",
              });
              setTimeout(function(){
                loading.close();
              },2000)
            }
          })
        }
      });
    },
    getInput(val){
      if(val === undefined){
        return;
      }
      this.imgType = val;
      //通过图片类型找到对应信息
      /*getResource(val).then(res => {
        this.parentResourceInfo = res.data.nodeName;
      })*/
    },

    /**
     * 全屏加载Loading
     */
    openFullScreen() {
      const loading = this.$loading({
        lock: true,
        text: '图片正在存储中，请稍等...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      setTimeout(() => {
        loading.close();
      }, 2000);
    }
  }
}
</script>

<style scoped>
.addFilePath{
  width:60%;
  margin:0 auto;
}
.dialog-footer{
  width:18%;
  margin:0 auto;
}
</style>
